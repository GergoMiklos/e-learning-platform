package com.thesis.studyapp.util;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.model.UserTestTaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

//todo static innerek ide? van értelme? vagy getComparator()-ok?
//todo thenComparing? Ehhez kell a 0-val való vissza térés!
public class ComparatorUtil {

    private static final Logger logger = LoggerFactory.getLogger(ComparatorUtil.class);

    public static Comparator<Task> getTaskComparator() {
        return Comparator.comparing(Task::getQuestion);
    }

    public static Comparator<Test> getTestComparator() {
        return Comparator.comparing(Test::getName);
    }

    public static Comparator<Group> getGroupComparator() {
        return Comparator.comparing(Group::getName);
    }

    public static Comparator<User> getUserComparator() {
        return Comparator.comparing(User::getName);
    }

    public static Comparator<TestTask> getTestTaskComparator() {
        return Comparator.comparing(TestTask::getLevel).thenComparing(TestTask::getRatio);
    }

    public static Comparator<UserTestStatus> getUserTestStatusComparator() {
        return ((Comparator<UserTestStatus>) (uts1, uts2) -> {
            if (uts1.getUser() == null || uts2.getUser() == null) {
                logger.error("Relationships needed for comparing UserTestStatuses!");
                return 0;
            } else {
                return uts1.getUser().getName().compareTo(uts2.getUser().getName());
            }
        }).thenComparing((uts1, uts2) -> {
            if (uts1.getTest() == null || uts2.getTest() == null) {
                logger.error("Relationships needed for comparing UserTestStatuses!");
                return 0;
            } else {
                return uts1.getTest().getName().compareTo(uts2.getTest().getName());
            }
        });
    }

    public static Comparator<UserTestTaskStatus> getUserTestTaskStatusComparator() {
        return ((Comparator<UserTestTaskStatus>) (utts1, utts2) -> {
            if (utts1.getAllSolutions() == 0 || utts2.getAllSolutions() == 0) {
                return 0;
            }
            if (utts1.getTestTask() == null || utts2.getTestTask() == null) {
                logger.error("Relationships needed for comparing UserTestTaskStatuses!");
                return utts1.getRatio() > utts2.getRatio() ? 1 : -1;
            } else {
                double ratioDiff1 = utts1.getRatio() - utts1.getTestTask().getRatio();
                double ratioDiff2 = utts2.getRatio() - utts2.getTestTask().getRatio();
                return ratioDiff1 > ratioDiff2 ? 1 : -1;
            }
        });
    }

}
