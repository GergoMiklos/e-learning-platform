package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.GroupRepo;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.NewsDTO;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.News;
import com.thesis.studyapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Component
public class GroupQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private UserRepo userRepo;

    public Optional<GroupDTO> getGroup(Long id) {
        return groupRepo.getById(id);
    }

    public List<GroupDTO> getByManyGroupIds(List<Long> groupIds) {
        return groupRepo.getByManyIds(groupIds);
    }

    @Transactional
    public Optional<GroupDTO> createGroup(Long userId, String name, String description) {
        User user = userRepo.findById(userId, 0).orElseThrow(()-> new CustomGraphQLException("No user with id: " + userId));
        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        //Todo külön osztály és ellenőrízni?
        group.setCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        group.addAdmin(user);
        group = groupRepo.save(group);
        return groupRepo.getById(group.getId());
    }

    @Transactional
    public Optional<GroupDTO> editGroup(Long id, String name, String description) {
        Group group = groupRepo.findById(id, 0).orElseThrow(()-> new CustomGraphQLException("No group with id: " + id));
        group.setName(name);
        group.setDescription(description);
        group = groupRepo.save(group);
        return groupRepo.getById(group.getId());
    }

    @Transactional
    public Optional<UserDTO> addUserFromCodeToGroup(Long groupId, String userCode) {
        //Todo
        //TODO itt leirom mit kéne csinálni, ez a GroupService.
        //Mindent bonts fel a lehető legkisebb függvényekbe, pl. itt ne groupRepot hívj, hanem egy másik függvény ezt már megcsilta,
        //vagy épp  ne userRepot
        //Mindent a tesztnek !!!

        Group group = groupRepo.findById(groupId, 0).orElseThrow(()-> new CustomGraphQLException("No group with id: " + groupId));
        Optional<User> user = userRepo.findByCode(userCode.toUpperCase());
        if(!user.isPresent()) {
            return null;
        } else {
            group.addUser(user.get());
            groupRepo.save(group);
            return userRepo.getById(user.get().getId());
        }
    }

    @Transactional
    public Optional<UserDTO> addAdminFromCodeToGroup(Long groupId, String userCode) {
        Group group = groupRepo.findById(groupId, 0).orElseThrow(()-> new CustomGraphQLException("No group with id: " + groupId));
        Optional<User> user = userRepo.findByCode(userCode.toUpperCase());
        if(!user.isPresent()) {
            return null;
        } else {
            group.addAdmin(user.get());
            groupRepo.save(group);
            return userRepo.getById(user.get().getId());
        }
    }

    @Transactional
    public Optional<GroupDTO> addUserToGroupFromCode(Long userId, String groupCode) {
        User user = userRepo.findById(userId, 0).orElseThrow(()-> new CustomGraphQLException("No user with id: " + userId));
        Optional<Group> group = groupRepo.findByCode(groupCode.toUpperCase());
        if(!group.isPresent()) {
            return null;
        } else {
            user.addGroup(group.get());
            userRepo.save(user);
            return groupRepo.getById(group.get().getId());
        }
    }

    public void deleteUserFromGroup(Long userId, Long groupId) {
        //TODO EZEKET A DELETE PATCHEKET lehet jobb lenne csak queryben megírni
        groupRepo.deleteUserFromGroup(userId, groupId);
    }

    public void deleteAdminFromGroup(Long userId, Long groupId) {
        //Todo return boolean
        groupRepo.deleteAdminFromGroup(userId, groupId);
    }

}
