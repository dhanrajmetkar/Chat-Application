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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<GroupOfUser> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Page<GroupOfUser> getGroups(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        boolean isPublic = true;
        return groupRepository.findByisPublic(isPublic, pageable);
    }

    @Override
    public List<GroupOfUser> getAdminGroups(int adminId) {
        User user = userRepository.findById(adminId).get();
        return groupRepository.findByAdmin(user);
    }

    @Override
    public List<User> getMembers(int adminId, int groupId) {
        User user = userRepository.findById(adminId).get();
        if (user.isEnabled()) {
            GroupOfUser groupOfUser = groupRepository.findById(groupId).get();
            return new ArrayList<>(groupOfUser.getUsers());
        }
        throw new RuntimeException("Incorrect Admin Id");
    }

    @Override
    public ResponseEntity<?> addMembers(AddMemberToGroupModel addMemberToGroupModel, String url) throws MessagingException, UnsupportedEncodingException {
        Optional<GroupOfUser> groupOfUserOptional = groupRepository.findById(addMemberToGroupModel.getGroupId());
        if (groupOfUserOptional.isPresent()) {

            GroupOfUser groupOfUser = groupOfUserOptional.get();
            //checking if the group is public or not if group is private then add the members to group by finding the admin id
            if (!groupOfUser.getIsPublic()) {
                Optional<User> userOptional = userRepository.findById(addMemberToGroupModel.getAdminId());
                User user;
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
                    if (userOptional1.isPresent()) {
                        user1 = userOptional1.get();
                    } else {
                        throw new RuntimeException("User with user id not found");
                    }
                    urls.add(url + "/admin/verifyUser?user_id=" + user1.getId() + "&group_id=" + groupOfUser.getId() + "&admin_id=" + groupOfUser.getAdmin().getId());
                    i++;
                    size--;
                }
                System.out.println(urls);
                // sendMail(addMemberToGroupModel.getUserIds().toString(), urls,user);
                return ResponseEntity.ok("You will be added to group soon once the admin will verify ");
            } else {
                List<User> userList = new ArrayList<>();
                int size = addMemberToGroupModel.getUserIds().size();
                int i = 0;
                while (size >= 1) {
                    User user1;
                    Optional<User> userOptional = userRepository.findById(addMemberToGroupModel.getUserIds().get(i));
                    if (userOptional.isPresent()) {
                        user1 = userOptional.get();
                        userList.add(user1);
                    } else {
                        throw new RuntimeException("User with user id not found");
                    }
                    i++;
                    size--;
                }
                groupOfUser.setUsers(userList);
                groupRepository.save(groupOfUser);
                return ResponseEntity.ok(groupOfUser.getUsers());
            }
        } else {
            throw new RuntimeException("group not found");
        }
    }

    @Override
    public List<User> addVerifiedMembers(int adminId, int userId, int groupId) {

        Optional<GroupOfUser> groupOfUserOptional = groupRepository.findById(groupId);
        if (groupOfUserOptional.isPresent()) {
            GroupOfUser groupOfUser = groupOfUserOptional.get();
            if (groupOfUser.getAdmin().getId().equals(adminId)) {
                Optional<User> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    groupOfUser.getUsers().add(user);
                    user.getGroups().add(groupOfUser);
                    groupRepository.save(groupOfUser);
                    return List.of(user);
                } else {
                    throw new RuntimeException(" user not found");
                }
            } else {
                throw new RuntimeException("invalid admin");
            }
        }
        throw new RuntimeException("group not found with given id");
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
