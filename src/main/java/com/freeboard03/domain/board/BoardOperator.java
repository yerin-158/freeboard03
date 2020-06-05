package com.freeboard03.domain.board;

import com.freeboard03.domain.board.enums.SearchType;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
public class BoardOperator {

    private MongoOperations operations;

    @Autowired
    public BoardOperator(MongoTemplate template) {
        Assert.notNull(template, "MongoTemplate must not be null!");
        this.operations = template;
    }

    public Page<BoardEntity> findAllByLike(SearchType searchType, String keyword, Pageable pageable) {
        Query query = getQuery(searchType, keyword, pageable);
        List<BoardEntity> boards = operations.find(query, BoardEntity.class);
        Page<BoardEntity> boardPage = PageableExecutionUtils.getPage(boards, pageable, () -> operations.count(Query.of(query).limit(-1).skip(-1), BoardEntity.class));
        return boardPage;
    }

    private Query getQuery(SearchType searchType, String keyword, Pageable pageable) {
        Query query = new Query();

        if (searchType.equals(SearchType.ALL)) {
            query.addCriteria(new Criteria().orOperator(Criteria.where("contents").regex(keyword), Criteria.where("title").regex(keyword)));
        } else if (searchType.equals(SearchType.CONTENTS)) {
            query.addCriteria(new Criteria().where("contents").regex(keyword));
        } else if (searchType.equals(SearchType.TITLE)) {
            query.addCriteria(new Criteria().where("title").regex(keyword));
        } else if (searchType.equals(SearchType.WRITER)) {
            query.addCriteria(new Criteria().where("writer.accountId").regex(keyword));
        }

        return query.with(pageable);
    }

    public UpdateResult update(ObjectId id, BoardEntity updatedEntity) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = getUpdate(updatedEntity);
        return operations.updateFirst(query, update, BoardEntity.class);
    }

    private Update getUpdate(BoardEntity updatedEntity) {

        try {
            List getterMethodNames = getBoardEntityGetterMethodName();
            Update update = new Update().currentDate("updatedAt");

            for (Method method : BoardEntity.class.getDeclaredMethods()) {
                if (getterMethodNames.contains(method.getName())) {
                    Object obj = method.invoke(updatedEntity);
                    Optional.ofNullable(obj).ifPresent(none -> update.set(getField(method), obj));
                }
            }

            return update;

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.getStackTrace();
            throw new RuntimeException();
        }
    }

    private List getBoardEntityGetterMethodName() {
        Field[] fields = BoardEntity.class.getDeclaredFields();
        return Arrays.asList(fields).stream().map(field -> "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)).collect(Collectors.toList());
    }

    private String getField(Method method) {
        String Field = method.getName().substring(3);
        return Field.substring(0, 1).toLowerCase() + Field.substring(1);
    }

}
