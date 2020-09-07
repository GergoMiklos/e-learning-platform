package com.thesis.studyapp;

import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.LiveTest;
import com.thesis.studyapp.model.LiveTestState;
import com.thesis.studyapp.model.News;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

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
            if (!RUNTEST)
                return;
            if (userRepo.count() != 0)
                return;

            User user = new User();
            user.setName("User");
            user.setCode("UUU001");
            user.setId(new Long(57));
            userRepo.save(user);

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
            group.setDescription("Helóbeló kobaka");
            Group group2 = new Group();
            group2.setName("Group2");
            group2.setCode("GGG002");
            group2.setDescription("Helóbeló kobaka2");

            ArrayList<String> answers = new ArrayList<String>();
            answers.add("Lóhere táncol");
            answers.add("Csipike is táncol");
            answers.add("hamradik válasz");
            answers.add("de vajon melyik a hejes??");
            Task task = new Task();
            task.setQuestion("Task");
            task.setAnswers(answers);
			Task task2 = new Task(); task2.setQuestion("Task2"); task2.setAnswers(answers);
			TestTask testTask = new TestTask(); testTask.setLevel(4);
			Test test = new Test(); test.setName("Test"); test.setDescription("Test1 leírása az élő állatokról");
			task.setOwner(user);

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
			testTask.setTask(task);
			test.addTask(testTask);

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
