package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.TaskAnswer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TaskResolver implements GraphQLResolver<Task> {

    public List<TaskAnswer> answers(Task task) {
        if (task.getAnswers() != null) {
            List<TaskAnswer> answers = new ArrayList<>(task.getAnswers());
            Collections.shuffle(answers);
            return answers;
        } else {
            return new ArrayList<>();
        }
    }
}
