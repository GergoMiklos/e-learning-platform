package com.thesis.studyapp.util;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.model.StudentTaskStatus;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class ComparatorUtil {

    private static final Logger logger = LoggerFactory.getLogger(ComparatorUtil.class);

    public static Comparator<Task> getTaskComparator() {
        return Comparator.comparing(Task::getQuestion, String::compareToIgnoreCase);
    }

    public static Comparator<Test> getTestComparator() {
        return Comparator.comparing(Test::getName, String::compareToIgnoreCase);
    }

    public static Comparator<Group> getGroupComparator() {
        return Comparator.comparing(Group::getName, String::compareToIgnoreCase);
    }

    public static Comparator<User> getUserComparator() {
        return Comparator.comparing(User::getName, String::compareToIgnoreCase);
    }

    public static Comparator<TestTask> getTestTaskComparator() {
        return Comparator.comparing(TestTask::getLevel).thenComparing((task1, task2) -> {
            if (task1.getTask() == null || task2.getTask() == null) {
                logger.error("Relationships needed for comparing TestTasks!");
                return 0;
            } else {
                return task1.getTask().getQuestion().compareToIgnoreCase(task2.getTask().getQuestion());
            }
        });
    }

    public static Comparator<StudentStatus> getStudentStatusComparator() {
        return ((Comparator<StudentStatus>) (status1, status2) -> {
            if (status1.getUser() == null || status2.getUser() == null) {
                logger.error("Relationships needed for comparing UserTestStatuses!");
                return 0;
            } else {
                return status1.getUser().getName().compareToIgnoreCase(status2.getUser().getName());
            }
        }).thenComparing((status1, status2) -> {
            if (status1.getTest() == null || status2.getTest() == null) {
                logger.error("Relationships needed for comparing UserTestStatuses!");
                return 0;
            } else {
                return status1.getTest().getName().compareToIgnoreCase(status2.getTest().getName());
            }
        });
    }

    public static Comparator<StudentTaskStatus> getStudentTaskStatusComparator() {
        return ((Comparator<StudentTaskStatus>) (status1, status2) -> {
            if (status1.getAllSolutions() == 0 && status2.getAllSolutions() == 0) {
                return 0;
            }
            if (status1.getTestTask() == null || status2.getTestTask() == null) {
                logger.error("Relationships needed for comparing UserTestTaskStatuses!");
                return status1.getRatio() > status2.getRatio() ? 1 : -1;
            } else {
                double ratioDiff1 = status1.getRatio() - status1.getTestTask().getRatio();
                double ratioDiff2 = status2.getRatio() - status2.getTestTask().getRatio();
                return ratioDiff1 > ratioDiff2 ? 1 : -1;
            }
        });
    }

}
