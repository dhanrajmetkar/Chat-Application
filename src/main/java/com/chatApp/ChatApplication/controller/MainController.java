package com.chatApp.ChatApplication.controller;


import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.User;
import com.chatApp.ChatApplication.model.UserModel;
import com.chatApp.ChatApplication.services.GroupService;
import com.chatApp.ChatApplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {
    private static final int pageSize = 3;
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;

    @GetMapping("/Users/{pageNo}")
    public List<User> getUsers(@PathVariable("pageNo") int pageNo) {
        Page<User> userPage = userService.getUsers(pageSize, pageNo);
        return userPage.getContent();
    }

    @GetMapping("/Groups/{pageNo}")
    public List<GroupOfUser> getGroups(@PathVariable("pageNo") int pageNo) {
        Page<GroupOfUser> groupPage = groupService.getGroups(pageNo, pageSize);
        return groupPage.getContent();
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel) {
        User user = userService.registerUser(userModel);
        return "User register successfully :";
    }

    @PostMapping("/createGroup")
    public GroupOfUser createGroup(@RequestParam("userId") int id, @RequestParam("groupName") String groupName) {
        GroupOfUser userGroup = userService.createGroup(id, groupName);
        return userGroup;
    }
}
