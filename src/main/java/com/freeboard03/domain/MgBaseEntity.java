package com.freeboard03.domain;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class MgBaseEntity {

    @Id
    @Setter
    protected ObjectId id;

    protected LocalDateTime createdAt = LocalDateTime.now();

    protected LocalDateTime updatedAt = LocalDateTime.now();

}
