package com.thesis.studyapp.service;

import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public UserDTO getUserById(Long id) {
        System.out.println("UserService: getUserById");
        Optional<User> user = userRepo.findById(id);
        return user.map(this::convertToDTO).orElse(null);
    }

    public UserDTO getUserByUserName(String userName) {
        System.out.println("UserService: getUserByUserName");
        Optional<User> user = userRepo.findByUserName(userName);
        return user.map(this::convertToDTO).orElse(null);
    }

    public List<UserDTO> getUserByGroupId(Long groupId) {
        System.out.println("UserService: getUserByGroupId");
        return convertToDTO(userRepo.findByGroupId(groupId));
    }

    public List<UserDTO> getUserByGroupIds(List<Long> groupIds) {
        System.out.println("UserService: getUserByGroupIdsss!");
        return convertToDTO(userRepo.findByGroupIds(groupIds));
    }

    public List<UserDTO> getUserByIds(List<Long> ids) {
        System.out.println("UserService: getUserIdsss!");
        List<User> u = userRepo.findByIds(ids);
        System.out.println("UserService ids size: " + u.size());
        return convertToDTO(u);
    }



    @Autowired
    private ModelMapper modelMapper;

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private List<UserDTO> convertToDTO(List<User> user) {
        Type listType = new TypeToken<List<UserDTO>>() {}.getType();
        return modelMapper.map(user, listType);
    }
}
