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
	public static boolean HUGETEST = false;

	public static void main(String[] args) {
		SpringApplication.run(StudyappApplication.class, args);
	}

	@Bean
	CommandLineRunner demo(UserRepo userRepo) {
		return args -> {
			if(userRepo.count() != 0)
				return;
			//			userRepo.deleteAll();

			User user = new User(); user.setUserName("User");
			User user2 = new User(); user2.setUserName("User2");
			User user3 = new User(); user3.setUserName("User3");
			User user4 = new User(); user4.setUserName("User4");
			Group group = new Group(); group.setName("Group");
			Group group2 = new Group(); group2.setName("Group2");

			Task task = new Task(); task.setQuestion("Task");
			Test test = new Test(); test.setName("Test");
			TestTaskState testTaskState = new TestTaskState();
			task.setOwner(user);
			test.setOwner(user);

			LiveTest liveTest = new LiveTest(); liveTest.setName("LiveTest");
			LiveTestUserState liveTestUserState = new LiveTestUserState();

			News news = new News();
			news.setTitle("News Title");
			group.addNews(news);

			user2.addManagedGroup(group);
			user.addGroup(group);
			user.addGroup(group2);
			user2.addGroup(group);
			user2.addGroup(group2);
			user3.addGroup(group);
			user4.addGroup(group2);

			group.addLiveTest(liveTest);
			liveTest.addLiveTestSate(liveTestUserState);
			liveTestUserState.setUser(user);
			liveTestUserState.setLiveTest(liveTest);
			liveTest.setTest(test);
			test.addTask(testTaskState);
			testTaskState.setTest(test);
			testTaskState.setTask(task);

			userRepo.save(user);
			userRepo.save(user2);
			userRepo.save(user3);
			userRepo.save(user4);

			if(HUGETEST) {
				for(int i = 1; i < 100; i++) {
					user = new User(); user.setUserName("UserTest" + i);
					user.addGroup(group);
					userRepo.save(user);
				}
			}
		};
	}
}
