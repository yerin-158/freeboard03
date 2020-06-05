package com.freeboard03.domain.board;

import com.freeboard03.domain.MgBaseEntity;
import com.freeboard03.domain.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document(collection = "boards")
public class MgBoardEntity extends MgBaseEntity {

    private UserEntity writer;

    @Setter
    private String contents;

    private String title;

    @Builder
    public MgBoardEntity(UserEntity writer, String contents, String title){
        this.writer = writer;
        this.contents = contents;
        this.title = title;
    }

    public MgBoardEntity update(MgBoardEntity newBoard){
        this.writer = newBoard.getWriter();
        this.contents = newBoard.getContents();
        this.title = newBoard.getTitle();
        return this;
    }
}
