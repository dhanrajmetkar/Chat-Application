package com.chatApp.ChatApplication.services;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.Role;
import com.chatApp.ChatApplication.entity.User;
import com.chatApp.ChatApplication.entity.VerificationToken;
import com.chatApp.ChatApplication.model.UserModel;
import com.chatApp.ChatApplication.repositories.GroupOfUserRepository;
import com.chatApp.ChatApplication.repositories.UserRepository;
import com.chatApp.ChatApplication.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupOfUserRepository groupRepository;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserModel userModel) {
        User user = User
                .builder()
                .email(userModel.getEmail())
                .firstname(userModel.getFirstname())
                .lastname(userModel.getLastname())
                .password(passwordEncoder.encode(userModel.getPassword()))
                .role(Role.USER)
                .enabled(false)
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
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "invalid";
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }
    @Override
    public GroupOfUser createGroup(int id, String groupName) {
        Optional<User> users = userRepository.findById(id);
        if (users.isPresent()) {
            User user = users.get();
            if (!user.isEnabled()) {
                throw new RuntimeException("user not verified you cannot create group :");
            }
            GroupOfUser group = new GroupOfUser();
            group.setName(groupName.trim());
            user.setRole(Role.ADMIN);
            group.setAdmin(user);
            groupRepository.save(group);
            userRepository.save(user);
            System.out.println("in the last step");
            return group;
        } else {
            throw new RuntimeException("user not found");
        }
    }

    @Override
    public List<User> getUsersFromGroup(int groupId) {
        GroupOfUser groupOfUser = groupRepository.findById(groupId).get();

        return groupOfUser.getUsers();
    }

    @Override
    public User getUsersAdminFromGroupId(int groupId) {
        GroupOfUser groupOfUser = groupRepository.findById(groupId).get();
        return groupOfUser.getAdmin();
    }

    @Override
    public String makaGroupPublic(int userId, int groupId) {
        GroupOfUser groupOfUser = groupRepository.findById(groupId).get();
        if (groupOfUser.getAdmin().getId() == userId) {
            groupOfUser.setIsPublic(true);
            groupRepository.save(groupOfUser);
            return "Group is Public now ";
        }
        return "Incorrect Admin You can not access group ";
    }
}
