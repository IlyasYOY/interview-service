package com.github.interview.interviewservice.converter;

import com.github.interview.interviewservice.domain.value.User;
import com.github.interview.interviewservice.model.UserModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserModelConverter implements Converter<User, UserModel> {

    @Override
    public UserModel convert(User source) {
        return UserModel.builder()
                .name(source.getName())
                .creator(source.isCreator())
                .build();
    }
}
