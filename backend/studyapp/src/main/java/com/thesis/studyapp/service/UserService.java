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
        Optional<User> user = userRepo.findById(id);
        return user.map(this::convertToDTO).orElse(null);
    }

    public UserDTO getUserByUserName(String userName) {
        Optional<User> user = userRepo.findByUserName(userName);
        return user.map(this::convertToDTO).orElse(null);
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
