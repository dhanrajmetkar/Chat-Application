package com.chatApp.ChatApplication.controller;


import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.User;
import com.chatApp.ChatApplication.event.RegistrationCompleteEvent;
import com.chatApp.ChatApplication.model.AddMemberToGroupModel;
import com.chatApp.ChatApplication.model.UserModel;
import com.chatApp.ChatApplication.services.GroupService;
import com.chatApp.ChatApplication.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

@RestController
public class MainController {
    private static final int pageSize = 3;
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;
    @Autowired
    ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)));
        return "Check your mail for registration  :";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User Verified Successfully :";
        } else {
            return "badUser";
        }
    }

    @GetMapping("/users/{pageNo}")
    public List<User> getUsers(@PathVariable("pageNo") int pageNo) {
        Page<User> userPage = userService.getUsers(pageSize, pageNo);
        List<User> users = userPage.getContent();
        return users;
    }

    @GetMapping("/groups")
    public List<GroupOfUser> getGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/publicGroups/{pageNo}")
    public List<GroupOfUser> getPublicGroups(@PathVariable("pageNo") int pageNo) {
        Page<GroupOfUser> groupPage = groupService.getGroups(pageNo, pageSize);
        return groupPage.getContent();
    }

    @GetMapping("/admin/getGroupMembers")
    public List<User> getMembers(@RequestParam("group_id") int group_id, @RequestParam("admin_id") int admin_id) {
        return groupService.getMembers(admin_id, group_id);
    }

    @PostMapping("/createGroup")
    public GroupOfUser createGroup(@RequestParam("user_id") int id, @RequestParam("groupName") String groupName) {
        return userService.createGroup(id, groupName);
    }

    @PostMapping("/makeGroupPublic")
    public String makeGroupPublic(@RequestParam("user_id") int user_id, @RequestParam("group_id") int group_id) {
        return userService.makaGroupPublic(user_id, group_id);
    }

    @GetMapping("/admin/getAdminGroups/{admin_id}")
    public Set<GroupOfUser> getGroupsByAdminId(@PathVariable("admin_id") int admin_id) {
        return groupService.getAdminGroups(admin_id);
    }

    @PostMapping("/admin/addMembers")
    public ResponseEntity<?> addMembersToGroup(@RequestBody AddMemberToGroupModel addMemberToGroupModel, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl(request);
        return groupService.addMembers(addMemberToGroupModel, url);
    }

    @GetMapping("/admin/verifyUser")
    public Set<User> verifyUser(@RequestParam("user_id") int user_id, @RequestParam("group_id") int group_id) {
        return groupService.addVerifiedMembers(user_id, group_id);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }

}

