package com.chatApp.ChatApplication.repositories;

import com.chatApp.ChatApplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Override
    Page<User> findAll(Pageable pageable);

    User findByEmail(String username);

    @Query("SELECT u FROM User u JOIN u.groups g WHERE g.isPublic = true")
    Page<User> findUsersByEnabledGroups(Pageable pageable);
}
