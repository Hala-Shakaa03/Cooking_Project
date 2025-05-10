package com.cooking.core;

public class AssignmentResult {
    private final Chef assignedChef;
    private final CookingTask task;

    public AssignmentResult(Chef assignedChef, CookingTask task) {
        this.assignedChef = assignedChef;
        this.task = task;
    }

    public Chef getAssignedChef() { return assignedChef; }
    public CookingTask getTask() { return task; }
}