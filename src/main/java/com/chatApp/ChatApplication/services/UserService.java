package com.chatApp.ChatApplication.services;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.User;
import com.chatApp.ChatApplication.entity.VerificationToken;
import com.chatApp.ChatApplication.model.UserModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User registerUser(UserModel userModel);

    Page<User> getUsers(int pageSize, int pageNo);

    GroupOfUser createGroup(int id, String groupName);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    List<User> getUsersFromGroup(int groupId);

    User getUsersAdminFromGroupId(int groupId);

    String makaGroupPublic(int userId, int groupId);
}
