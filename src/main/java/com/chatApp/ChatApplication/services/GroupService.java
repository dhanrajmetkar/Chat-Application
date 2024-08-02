package com.chatApp.ChatApplication.services;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.User;
import com.chatApp.ChatApplication.model.AddMemberToGroupModel;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

public interface GroupService {
    Page<GroupOfUser> getGroups(int pageNo, int pageSize);

    Set<GroupOfUser> getAdminGroups(int adminId);

    ResponseEntity<?> addMembers(AddMemberToGroupModel addMemberToGroupModel, String url) throws MessagingException, UnsupportedEncodingException;

    Set<User> addVerifiedMembers(int userId, int groupId);

    List<GroupOfUser> getAllGroups();

    List<User> getMembers(int adminId, int groupId);
}
