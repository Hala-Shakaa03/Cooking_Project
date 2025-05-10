/*package com.cooking.core;

import java.util.*;
import java.util.stream.Collectors;

public class TaskAssignmentService {
    private static final int MAX_TASKS_PER_CHEF = 5;
    private final NotificationService notificationService;

    public TaskAssignmentService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public AssignmentResult assignTask(CookingTask task, List<Chef> availableChefs) {
        List<Chef> eligibleChefs = availableChefs.stream()
            .filter(chef -> chef.getCurrentTasks() < MAX_TASKS_PER_CHEF)
            .sorted(Comparator.comparingInt(Chef::getCurrentTasks))
            .collect(Collectors.toList());

        if (eligibleChefs.isEmpty()) {
            String message = "Redistribution needed - all chefs are at max capacity";
            notificationService.sendSystemNotification(message);
            throw new IllegalStateException("Cannot assign task - all chefs are overloaded");
        }

        Chef assignedChef = eligibleChefs.get(0);
        assignedChef.assignTask(task);
        
        notificationService.sendNotification(
            assignedChef.getEmail(),
            "New task assigned: " + task.getName() + " (Priority: " + task.getPriority() + ")"
        );

        return new AssignmentResult(assignedChef, task);
    }
}*/