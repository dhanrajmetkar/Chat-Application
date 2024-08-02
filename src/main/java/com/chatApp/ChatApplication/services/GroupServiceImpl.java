package com.chatApp.ChatApplication.services;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.User;
import com.chatApp.ChatApplication.model.AddMemberToGroupModel;
import com.chatApp.ChatApplication.repositories.GroupOfUserRepository;
import com.chatApp.ChatApplication.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    GroupOfUserRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    public GroupServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

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
    public ResponseEntity<?> addMembers(AddMemberToGroupModel addMemberToGroupModel, String url) throws MessagingException, UnsupportedEncodingException {
        User user;
        GroupOfUser groupOfUser = null;
        Optional<GroupOfUser> groupOfUserOptional = groupRepository.findById(addMemberToGroupModel.getGroupId());
        if (groupOfUserOptional.isPresent()) {
            groupOfUser = groupOfUserOptional.get();
        } else {
            throw new RuntimeException("group with given id not present ");
        }
        System.out.println(!groupOfUser.getIsPublic());
        if (!groupOfUser.getIsPublic()) {
            Optional<User> userOptional = userRepository.findById(addMemberToGroupModel.getAdminId());
            if (userOptional.isPresent()) {
                user = userOptional.get();
            } else {
                throw new RuntimeException("Invalid Admin Id ");
            }
            if (!user.getId().equals(groupOfUser.getAdmin().getId())) {
                throw new RuntimeException("invalid admin you can not be added to this group");
            }
            ArrayList<String> urls = new ArrayList<>();
            int size = addMemberToGroupModel.getUserIds().size();
            int i = 0;
            while (size >= 1) {
                User user1;
                Optional<User> userOptional1 = userRepository.findById(addMemberToGroupModel.getUserIds().get(i));
                if (userOptional.isPresent()) {
                    user1 = userOptional1.get();
                } else {
                    throw new RuntimeException("User with user id not found");
                }
                urls.add(url + "/admin/verifyUser?user_id=" + user1.getId() + "&group_id=" + groupOfUser.getId());
                i++;
                size--;
            }
            System.out.println(urls);

            // sendMail(addMemberToGroupModel.getUserIds().toString(), urls,user);
            return ResponseEntity.ok("You will be added to group soon once the admin will verify ");
        } else {
            Set<User> userSet = new HashSet<>();
            int size = addMemberToGroupModel.getUserIds().size();
            int i = 0;
            while (size >= 1) {
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
            return ResponseEntity.ok(groupOfUser.getUsers());
        }


    }

    @Override
    public Set<User> addVerifiedMembers(int userId, int groupId) {
        User user = userRepository.findById(userId).get();
        if (user.isEnabled()) {
            GroupOfUser groupOfUser = groupRepository.findById(groupId).get();
            Set<User> userSet = new HashSet<>();
            userSet.addAll(groupOfUser.getUsers());
            userSet.add(user);
            groupOfUser.setUsers(userSet);
            return groupOfUser.getUsers();
        }
        throw new RuntimeException("user is not verified please verify the user :");
    }

    @Override
    public List<GroupOfUser> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public List<User> getMembers(int adminId, int groupId) {
        User user = userRepository.findById(adminId).get();
        List<User> users = new ArrayList<>();
        if (user.isEnabled()) {
            GroupOfUser groupOfUser = groupRepository.findById(groupId).get();
            users.addAll(groupOfUser.getUsers());
            return users;
        }
        throw new RuntimeException("Incorrect Admin Id");
    }

    private void sendMail(String ids, ArrayList<String> urls, User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom("your-email@example.com", "Verify");
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        helper.setTo(user.getEmail());

        helper.setSubject(" below users want to request you to add in your group");
        helper.setText(ids + "\n " + urls, true);

        mailSender.send(message);
    }
}
