package com.chatApp.ChatApplication.repositories;

import com.chatApp.ChatApplication.entity.GroupOfUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupOfUserRepository extends JpaRepository<GroupOfUser, Integer> {

    @Override
    Page<GroupOfUser> findAll(Pageable pageable);
}
