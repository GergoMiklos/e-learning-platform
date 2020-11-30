package com.thesis.studyapp.service;

import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.UserRepository;
import com.thesis.studyapp.util.TestBeans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    public static final Long USER_ID = TestBeans.USER_ID;
    public static final User USER = TestBeans.USER;

    @Mock
    private UserRepository mockUserRepository;

    private DefaultUserService userService;

    @BeforeEach
    void setUpService() {
        userService = new DefaultUserService(mockUserRepository);
    }

    @Test
    void getUser_getsTheUser() {
        doReturn(Optional.of(USER)).when(mockUserRepository).findById(USER_ID, 1);

        User user = userService.getUser(USER_ID);

        assertEquals(user, USER);
    }

    @Test
    void getUser_error_whenNoUser() {
        doReturn(Optional.empty()).when(mockUserRepository).findById(USER_ID, 1);

        assertThrows(NotFoundException.class, () -> userService.getUser(USER_ID));
    }

}
