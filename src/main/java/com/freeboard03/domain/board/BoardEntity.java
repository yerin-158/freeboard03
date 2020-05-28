package com.freeboard03.domain.board;

import com.freeboard03.domain.BaseEntity;
import com.freeboard03.domain.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "board")
@NoArgsConstructor
@DynamicUpdate
public class BoardEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "writerId", nullable = false)
    private UserEntity writer;

    @Setter
    @Column
    private String contents;

    @Column
    private String title;

    @Builder
    public BoardEntity(UserEntity writer, String contents, String title){
        this.writer = writer;
        this.contents = contents;
        this.title = title;
    }

    public BoardEntity update(BoardEntity newBoard){
        this.writer = newBoard.getWriter();
        this.contents = newBoard.getContents();
        this.title = newBoard.getTitle();
        return this;
    }
}
