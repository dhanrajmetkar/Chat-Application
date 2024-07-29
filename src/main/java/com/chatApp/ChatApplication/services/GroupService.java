package com.chatApp.ChatApplication.services;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.model.AddMemberToGroupModel;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface GroupService {
    Page<GroupOfUser> getGroups(int pageNo, int pageSize);

    Set<GroupOfUser> getAdminGroups(int adminId);

    GroupOfUser addMembers(AddMemberToGroupModel addMemberToGroupModel);
}
