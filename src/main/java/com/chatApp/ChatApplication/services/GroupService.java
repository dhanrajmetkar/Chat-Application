package com.chatApp.ChatApplication.services;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import org.springframework.data.domain.Page;

public interface GroupService {
    Page<GroupOfUser> getGroups(int pageNo, int pageSize);
}
