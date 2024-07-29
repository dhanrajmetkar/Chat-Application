package com.chatApp.ChatApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class AddMemberToGroupModel {
    int groupId;
    int adminId;
    List<Integer> userIds;
}
