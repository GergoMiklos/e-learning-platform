package com.thesis.studyapp;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.TaskAnswer;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class StudyappApplication {
    public static boolean RUNTEST = true;
    public static boolean HUGETEST = false;

    public static void main(String[] args) {
        SpringApplication.run(StudyappApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(UserRepository userRepository) {
        return args -> {
            if (!RUNTEST)
                return;
            if (userRepository.count() != 0)
                return;

            User user = new User();
            user.setName("User");
            user.setCode("UUU001");

            User user2 = new User();
            user2.setName("User2");
            user2.setCode("UUU002");
            User user3 = new User();
            user3.setName("User3");
            user3.setCode("UUU003");
            User user4 = new User();
            user4.setName("User4");
            user4.setCode("UUU004");

            Group group = Group.builder()
                    .name("Group")
                    .description("group 1 init disc")
                    .news("News!")
                    .newsChangedDate(LocalDateTime.now())
                    .code("GGG001")
                    .build();
            Group group2 = Group.builder()
                    .name("Group2")
                    .description("group 2 init disc")
                    .news("News 2!")
                    .newsChangedDate(LocalDateTime.now())
                    .code("GGG002")
                    .build();

            Set<TaskAnswer> answers = new HashSet<>();
            answers.add(TaskAnswer.builder().number(1).answer("Answer 1 is here").build());
            answers.add(TaskAnswer.builder().number(2)
                    .answer("Answer 2 is a little bit longer than answer 1 if you don't mind (and lets have some extra %/=* character too").build());
            answers.add(TaskAnswer.builder().number(3).answer("3.").build());
            answers.add(TaskAnswer.builder().number(4).answer("This is only for testing").build());

            Task task = Task.builder()
                    .question("Task1, the soltuion is the number 1")
                    .solutionNumber(1)
                    .answers(answers)
                    .usage(1L)
                    .build();
            Task task2 = Task.builder()
                    .question("Task2 is a little bit longer for making the development testing easier for me. Solution is number 2!")
                    .solutionNumber(2)
                    .answers(answers)
                    .usage(2L)
                    .build();

            Test test = new Test();
            test.setName("Test");
            test.setDescription("Test1 desc. about something interesting");

            TestTask testTask = new TestTask();
            testTask.setLevel(4);
            test.addTask(testTask);
            testTask.setTask(task);
            testTask.setTest(test);

            user.addTeacherGroup(group);
            user2.addTeacherGroup(group);
            user.addStudentGroup(group);
            user.addStudentGroup(group2);
            user2.addStudentGroup(group);
            user2.addStudentGroup(group2);
            user3.addStudentGroup(group);
            user4.addStudentGroup(group2);

            UserTestStatus userTestStatus = UserTestStatus.builder()
                    .status(UserTestStatus.Status.IN_PROGRESS)
                    .statusChangedDate(LocalDateTime.now())
                    .user(user)
                    .test(test)
                    .allAnswers(56)
                    .correctAnswers(18)
                    .build();
            group.addTest(test);
            test.addUserTestStatus(userTestStatus);
            user.addUserTestStatus(userTestStatus);
            userTestStatus.setCurrentTask(task2);
            testTask.setTask(task);
            test.addTask(testTask);

            userRepository.save(user);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);

            if (HUGETEST) {
                for (int i = 1; i < 100; i++) {
                    user = new User();
                    user.setName("UserTest" + i);
                    user.addStudentGroup(group);
                    userRepository.save(user);
                }
            }
        };
    }
}
