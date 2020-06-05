package com.freeboard03.domain.board;

import com.freeboard03.domain.board.enums.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


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

}
