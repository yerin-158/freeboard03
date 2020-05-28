package com.freeboard03.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @CreationTimestamp
    @Column
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    protected LocalDateTime updatedAt;

}
