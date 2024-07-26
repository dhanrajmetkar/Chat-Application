package com.chatApp.ChatApplication.services;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.User;
import com.chatApp.ChatApplication.model.UserModel;
import org.springframework.data.domain.Page;

public interface UserService {
    User registerUser(UserModel userModel);

    Page<User> getUsers(int pageSize, int pageNo);

    GroupOfUser createGroup(int id, String groupName);
}
