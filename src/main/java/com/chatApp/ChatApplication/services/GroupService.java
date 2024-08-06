package com.chatApp.ChatApplication.services;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.User;
import com.chatApp.ChatApplication.model.AddMemberToGroupModel;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface GroupService {
    Page<GroupOfUser> getGroups(int pageNo, int pageSize);

    List<GroupOfUser> getAdminGroups(int adminId);

    ResponseEntity<?> addMembers(AddMemberToGroupModel addMemberToGroupModel, String url) throws MessagingException, UnsupportedEncodingException;

    List<User> addVerifiedMembers(int adminId, int userId, int groupId);

    List<GroupOfUser> getAllGroups();

    List<String> getMembers(int adminId, int groupId);

}
