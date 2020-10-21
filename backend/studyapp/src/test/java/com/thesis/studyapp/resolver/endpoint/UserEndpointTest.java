package com.thesis.studyapp.resolver.endpoint;

import com.thesis.studyapp.model.User;
import com.thesis.studyapp.service.UserService;
import com.thesis.studyapp.util.TestBeans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserEndpointTest {
    public static final Long USER_ID = TestBeans.USER_ID;
    public static final User USER = TestBeans.USER;

    @Mock
    private UserService mockUserService;

    private UserEndpoint userEndpoint;

    @BeforeEach
    void setUpService() {
        userEndpoint = new UserEndpoint(mockUserService);
    }

    @Test
    void getUser_getsTheUser() {
        doReturn(USER).when(mockUserService).getUser(USER_ID);

        User user = userEndpoint.user(USER_ID);

        assertEquals(user, USER);
    }

}
