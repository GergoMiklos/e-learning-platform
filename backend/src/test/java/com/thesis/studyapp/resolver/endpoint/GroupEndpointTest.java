package com.thesis.studyapp.resolver.endpoint;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.service.DefaultGroupService;
import com.thesis.studyapp.util.TestBeans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class GroupEndpointTest {
    public static final Long USER_ID = TestBeans.USER_ID;
    public static final String GROUP_CODE = TestBeans.GROUP_CODE;
    public static final Group GROUP = TestBeans.GROUP;

    @Mock
    private DefaultGroupService mockGroupService;

    private GroupEndpoint groupEndpoint;

    @BeforeEach
    void setUpService() {
        groupEndpoint = new GroupEndpoint(mockGroupService);
    }

    @Test
    void addStudentToGroupFromCode_addsStudentToGroup() {
        doReturn(GROUP).when(mockGroupService).addStudentToGroupFromCode(USER_ID, GROUP_CODE);

        Group group = groupEndpoint.addStudentToGroupFromCode(USER_ID, GROUP_CODE);

        assertEquals(group, GROUP);
    }

}
