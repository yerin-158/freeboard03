package com.freeboard03.api.user;

import com.freeboard03.domain.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserForm {

    private String accountId;
    private String password;

    @Builder
    public UserForm(String accountId, String password){
        this.accountId = accountId;
        this.password = password;
    }

    public UserEntity convertUserEntity(){
        return UserEntity.builder()
                .accountId(this.accountId)
                .password(this.password)
                .build();
    }

}
