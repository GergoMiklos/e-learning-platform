package com.thesis.studyapp.util;

import com.thesis.studyapp.model.Task;

import java.util.Comparator;

//todo static innerek ide? van értelme? vagy getComparator()-ok?
//todo thenComparing? Ehhez kell a 0-val való vissza térés!
public class ComparatorUtil {
    public static Comparator<Task> getTaskComparator() {
        return new Comparator<Task>() {
            @Override public int compare(Task t1, Task t2) {
                return t1.getQuestion().compareTo(t2.getQuestion());
            }
        };
    }
}
