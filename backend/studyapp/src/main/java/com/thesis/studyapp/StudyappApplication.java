package com.thesis.studyapp;

import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StudyappApplication {
	public static boolean RUNTEST = true;
	public static boolean HUGETEST = false;

	public static void main(String[] args) {
		SpringApplication.run(StudyappApplication.class, args);
	}

	@Bean
	CommandLineRunner demo(UserRepo userRepo) {
		return args -> {
			if(!RUNTEST)
				return;
			if(userRepo.count() != 0)
				return;

			User user = new User(); user.setName("User"); user.setCode("ASD111");
			userRepo.save(user);

			User user2 = new User(); user2.setName("User2"); user2.setCode("ASD222");
			User user3 = new User(); user3.setName("User3"); user3.setCode("ASD333");
			User user4 = new User(); user4.setName("User4"); user4.setCode("ASD444");
			Group group = new Group(); group.setName("Group"); group.setCode("GSDG11"); group.setDescription("Helóbeló kobaka");
			Group group2 = new Group(); group2.setName("Group2"); group2.setCode("GSDG22"); group2.setDescription("Helóbeló kobaka2");

			Task task = new Task(); task.setQuestion("Task");
			Task task2 = new Task(); task2.setQuestion("Task2");
			Test test = new Test(); test.setName("Test"); test.setDescription("Test1 leírása az élő állatokról");
			task.setOwner(user);
			test.setOwner(user);

			LiveTest liveTest = new LiveTest();
			LiveTestState liveTestState = new LiveTestState();

			News news = new News();
			news.setText("News Title");
			group.setNews(news);

			user.addManagedGroup(group);
			user2.addManagedGroup(group);
			user.addGroup(group);
			user.addGroup(group2);
			user2.addGroup(group);
			user2.addGroup(group2);
			user3.addGroup(group);
			user4.addGroup(group2);

			group.addLiveTest(liveTest);
			liveTest.addLiveTestSate(liveTestState);
			liveTestState.setUser(user);
			liveTestState.setLiveTest(liveTest);
			liveTestState.setCurrentTask(task2);
			liveTest.setTest(test);
			test.addTask(task);

			userRepo.save(user);
			userRepo.save(user2);
			userRepo.save(user3);
			userRepo.save(user4);

			if(HUGETEST) {
				for(int i = 1; i < 100; i++) {
					user = new User(); user.setName("UserTest" + i);
					user.addGroup(group);
					userRepo.save(user);
				}
			}
		};
	}
}
