package com.freeboard03.domain.user;

import com.freeboard03.domain.BaseEntity;
import com.freeboard03.domain.user.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor
@Document(collection = "users")
@Entity
public class UserEntity extends BaseEntity {

    private String accountId;

    private String password;

    @Setter
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public UserEntity(String accountId, String password, UserRole role) {
        this.accountId = accountId;
        this.password = password;
        this.role = role;
    }

}
