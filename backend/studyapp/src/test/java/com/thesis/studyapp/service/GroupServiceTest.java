package com.thesis.studyapp.service;

import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.UserRepository;
import com.thesis.studyapp.util.AuthenticationUtil;
import com.thesis.studyapp.util.DateUtil;
import com.thesis.studyapp.util.TestBeans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
    public static final Long USER_ID = TestBeans.USER_ID;
    public static final User USER = TestBeans.USER;
    public static final String GROUP_CODE = TestBeans.GROUP_CODE;
    public static final Group GROUP = TestBeans.GROUP;

    @Mock
    private GroupRepository mockGroupRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private AuthenticationUtil mockAuthenticationUtil;
    @Mock
    private DateUtil mockDateUtil;

    private GroupService groupService;

    @BeforeEach
    void setUpService() {
        groupService = new GroupService(mockGroupRepository, mockUserRepository, mockAuthenticationUtil, mockDateUtil);
    }

    @Test
    void addStudentToGroupFromCode_addsStudentToGroup() {
        doReturn(Optional.of(USER)).when(mockUserRepository).findById(USER_ID, 1);
        doReturn(Optional.of(GROUP)).when(mockGroupRepository).findByCodeIgnoreCase(GROUP_CODE, 1);
        doAnswer(args -> args.getArgument(0, Group.class)).when(mockGroupRepository).save(any(Group.class), anyInt());

        Group group = groupService.addStudentToGroupFromCode(USER_ID, GROUP_CODE);

        assertEquals(group.getId(), GROUP.getId());
        assertEquals(group.getName(), GROUP.getName());
        assertNotNull(group.getStudents());
        assertEquals(group.getStudents().size(), 1);
        assertTrue(group.getStudents().contains(USER));
    }

    @Test
    void addStudentToGroupFromCode_error_whenNoGroupFound() {
        doReturn(Optional.empty()).when(mockGroupRepository).findByCodeIgnoreCase(GROUP_CODE, 1);

        assertThrows(NotFoundException.class, () -> groupService.addStudentToGroupFromCode(USER_ID, GROUP_CODE));

        verify(mockGroupRepository, never()).save(any(Group.class), anyInt());
    }

}
