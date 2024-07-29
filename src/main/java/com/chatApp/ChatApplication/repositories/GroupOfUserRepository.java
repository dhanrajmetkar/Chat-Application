package com.chatApp.ChatApplication.repositories;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import com.chatApp.ChatApplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GroupOfUserRepository extends JpaRepository<GroupOfUser, Integer> {

    @Override
    Page<GroupOfUser> findAll(Pageable pageable);

    Page<GroupOfUser> findByisPublic(boolean isPublic, Pageable pageable);

    Set<GroupOfUser> findByAdmin(User user);
}
