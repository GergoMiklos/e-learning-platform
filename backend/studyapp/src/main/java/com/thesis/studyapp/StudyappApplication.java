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

            Group group = new Group();
            group.setName("Group");
            group.setCode("GGG001");
            group.setDescription("Group1 initial test desc.");
            group.setNews("Test news!");
            group.setNewsChangedDate(LocalDateTime.now());
            Group group2 = new Group();
            group2.setName("Group2");
            group2.setCode("GGG002");
            group2.setDescription("And Group2 initial test desc.");

//            Map<Integer, String> answers = new LinkedHashMap<>();
//            answers.put(1, "Answer 1 is here");
//            answers.put(2, "Answer 2 is a little bit longer than answer 1 if you don't mind");
//            answers.put(3, "3.");
//            answers.put(4, "This is only for testing");
            Set<TaskAnswer> answers = new HashSet<>();
            answers.add(TaskAnswer.builder().number(1).answer("Answer 1 is here").build());
            answers.add(TaskAnswer.builder().number(1)
                    .answer("Answer 2 is a little bit longer than answer 1 if you don't mind (and lets have some extra %/=* character too").build());
            answers.add(TaskAnswer.builder().number(1).answer("3.").build());
            answers.add(TaskAnswer.builder().number(1).answer("This is only for testing").build());

            Task task = Task.builder()
                    .question("Task1, the soltuion is the number 1")
                    .solutionNumber(1)
                    .answers(answers)
                    .build();
            Task task2 = Task.builder()
                    .question("Task2 is a little bit longer for making the development testing easier for me. Solution is number 2!")
                    .solutionNumber(2)
                    .answers(answers)
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

            UserTestStatus userTestStatus = new UserTestStatus();
            group.addTest(test);
            test.addUserTestStatus(userTestStatus);
            userTestStatus.setUser(user);
            user.addUserTestStatus(userTestStatus);
            userTestStatus.setTest(test);
            userTestStatus.setCurrentTask(task2);
            userTestStatus.setDeprecated(false);
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
