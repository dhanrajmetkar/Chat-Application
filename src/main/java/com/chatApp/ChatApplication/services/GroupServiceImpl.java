package com.chatApp.ChatApplication.services;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.User;
import com.chatApp.ChatApplication.model.AddMemberToGroupModel;
import com.chatApp.ChatApplication.repositories.GroupOfUserRepository;
import com.chatApp.ChatApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    GroupOfUserRepository groupRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Page<GroupOfUser> getGroups(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        boolean isPublic = true;

        return groupRepository.findByisPublic(isPublic, pageable);
    }

    @Override
    public Set<GroupOfUser> getAdminGroups(int adminId) {
        User user = userRepository.findById(adminId).get();
        return groupRepository.findByAdmin(user);
    }

    @Override
    public GroupOfUser addMembers(AddMemberToGroupModel addMemberToGroupModel) {
        User user;
        GroupOfUser groupOfUser = groupRepository.findById(addMemberToGroupModel.getGroupId()).get();
        if (!groupOfUser.getIsPublic()) {
            user = userRepository.findById(addMemberToGroupModel.getAdminId()).get();
        }
        Set<User> userSet = new HashSet<>();
        int size = addMemberToGroupModel.getUserIds().size();
        int i = 0;
        while (size >= 0) {
            User user1;
            Optional<User> userOptional = userRepository.findById(addMemberToGroupModel.getUserIds().get(i));
            if (userOptional.isPresent()) {
                user1 = userOptional.get();
            } else {
                throw new RuntimeException("User with user id not found");
            }
            userSet.add(user1);
            i++;
            size--;

        }
        groupOfUser.setUsers(userSet);

        return groupOfUser;

    }
}
