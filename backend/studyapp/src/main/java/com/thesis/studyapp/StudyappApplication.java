package com.thesis.studyapp;

import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.model.*;
import org.neo4j.driver.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StudyappApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyappApplication.class, args);
	}

	@Bean
	CommandLineRunner demo(UserRepo userRepo) {
		return args -> {
//			userRepo.deleteAll();
//
//			User user = new User(); user.setUserName("User");
//			Group group = new Group(); group.setName("Group");
//			GroupUserState groupUserState = new GroupUserState();
//
//			Task task = new Task(); task.setQuestion("Task");
//			Test test = new Test(); test.setName("Test");
//			TestTaskState testTaskState = new TestTaskState();
//
//			LiveTest liveTest = new LiveTest(); liveTest.setName("LiveTest");
//			LiveTestUserState liveTestUserState = new LiveTestUserState();
//
//			user.addGroup(groupUserState);
//			groupUserState.setGroup(group);
//			groupUserState.setUser(user);
//			group.addLiveTest(liveTest);
//			liveTest.addLiveTestSate(liveTestUserState);
//			liveTestUserState.setUser(user);
//			liveTestUserState.setLiveTest(liveTest);
//			liveTest.setTest(test);
//			test.addTask(testTaskState);
//			testTaskState.setTest(test);
//			testTaskState.setTask(task);
//
//			userRepo.save(user);
		};
	}
}
