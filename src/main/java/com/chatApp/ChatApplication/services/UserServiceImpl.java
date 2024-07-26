package com.chatApp.ChatApplication.services;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.User;;
import com.chatApp.ChatApplication.model.UserModel;
import com.chatApp.ChatApplication.repositories.GroupOfUserRepository;
import com.chatApp.ChatApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupOfUserRepository groupRepository;

    @Override
    public User registerUser(UserModel userModel) {
        User user = User
                .builder()
                .email(userModel.getEmail())
                .firstname(userModel.getFirstname())
                .lastname(userModel.getLastname())
                .password(userModel.getPassword())
                .role("USER")
                .build();
        System.out.println(user);
        userRepository.save(user);
        return user;
    }

    @Override
    public Page<User> getUsers(int pageSize, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return userRepository.findAll(pageable);
    }

    @Override
    public GroupOfUser createGroup(int id, String groupName) {
        User user = userRepository.findById(id).get();
        GroupOfUser userGroup = new GroupOfUser();
        userGroup.setName(groupName);
        userGroup.setAdmin(user);
        Set<User> userSet=new HashSet<>();
        userSet.add(user);
        userGroup.setUsers(userSet);
        groupRepository.save(userGroup);
        Set<GroupOfUser> userGroupSet = user.getGroups();
        userGroupSet.add(userGroup);
        user.setGroups(userGroupSet);
        userRepository.save(user);
        return userGroup;
    }
}
