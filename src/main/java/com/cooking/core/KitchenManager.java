package com.cooking.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KitchenManager {
    private static final Logger logger = LoggerFactory.getLogger(KitchenManager.class);
    private List<Chef> availableChefs = new ArrayList<>();
    private boolean workloadRedistributionSuggested = false;
    private Chef lastNotifiedChef;

    public void setAvailableChefs(List<Chef> chefs) {
        logger.debug("Setting available chefs list ({} chefs)", chefs.size());
        this.availableChefs = new ArrayList<>(chefs);
        logChefAvailability();
    }

    public void addChef(Chef chef) {
        logger.info("Adding chef: {} ({}), Expertise: {}, Current Tasks: {}",
            chef.getName(),
            chef.getEmail(),
            chef.getExpertise(),
            chef.getCurrentTasks());
        availableChefs.add(chef);
    }

    public Chef assignTask(CookingTask task) {
    	// Log task assignment attempt (shows HIGH/LOW priority only)
        logger.info("Assigning '{}' ({}{})", 
            task.getDescription(), 
            task.getCuisineType(),
            task.getPriority() != CookingTask.Priority.MEDIUM ? " " + task.getPriority() : "");

        Optional<Chef> chefOptional = findBestChefForTask(task);
        
        if (!chefOptional.isPresent()) {
            logger.warn("No chef available for {} ({} cuisine)", 
                task.getDescription(), 
                task.getCuisineType());
            workloadRedistributionSuggested = true;
            throw new IllegalStateException("No suitable chef available");
        }

        Chef chef = chefOptional.get();
        chef.addTask(task);
        
        // Only show expertise if it doesn't clearly match the cuisine type
        String expertiseNote = !chef.getExpertise().toLowerCase().contains(task.getCuisineType().toLowerCase())
            ? ", Expertise: " + chef.getExpertise()
            : "";
        
        logger.info("Assigned to {} ({} tasks{})", 
            chef.getName(),
            chef.getCurrentTasks(),
            expertiseNote);
        
        sendNotification(chef);
        return chef;
    	
    }

    private Optional<Chef> findBestChefForTask(CookingTask task) {
        logger.debug("Finding best chef for task: {}", task.getDescription());
        logger.debug("Task requirements - Cuisine: {}, Priority: {}",
            task.getCuisineType(),
            task.getPriority());
        
        return availableChefs.stream()
            .filter(chef -> {
                boolean canAccept = chef.canAcceptTask();
                if (!canAccept) {
                    logger.trace("Chef {} at max capacity ({} tasks)",
                        chef.getName(),
                        chef.getCurrentTasks());
                }
                return canAccept;
            })
            .min((c1, c2) -> {
                // Log comparison details
                logger.debug("Comparing chefs: {} vs {} for task {}",
                    c1.getName(), c2.getName(), task.getDescription());
                
                boolean c1Matches = c1.getExpertise().toLowerCase().contains(task.getCuisineType().toLowerCase());
                boolean c2Matches = c2.getExpertise().toLowerCase().contains(task.getCuisineType().toLowerCase());
                
                if (c1Matches && !c2Matches) {
                    logger.debug("{} has better expertise match", c1.getName());
                    return -1;
                }
                if (!c1Matches && c2Matches) {
                    logger.debug("{} has better expertise match", c2.getName());
                    return 1;
                }
                
                int taskCompare = Integer.compare(c1.getCurrentTasks(), c2.getCurrentTasks());
                logger.debug("Equal expertise match. Comparing workload: {} has {} tasks, {} has {} tasks",
                    c1.getName(), c1.getCurrentTasks(),
                    c2.getName(), c2.getCurrentTasks());
                
                return taskCompare;
            });
    }

    private void sendNotification(Chef chef) {
        logger.info("Sending notification to chef {} ({}) about new task assignment",
            chef.getName(),
            chef.getEmail());
        lastNotifiedChef = chef;
        // In a real system, this would send an actual notification
    }

    public boolean wasNotificationSent(Chef chef) {
        boolean wasSent = lastNotifiedChef != null && lastNotifiedChef.equals(chef);
        logger.debug("Notification sent check for chef {}: {}",
            chef.getName(),
            wasSent ? "YES" : "NO");
        return wasSent;
    }

    public boolean getWorkloadRedistributionSuggested() {
        logger.debug("Workload redistribution suggestion flag: {}",
            workloadRedistributionSuggested);
        return workloadRedistributionSuggested;
    }

    private void logChefAvailability() {
        if (logger.isDebugEnabled()) {
            logger.debug("Current chef availability:");
            availableChefs.forEach(chef ->
                logger.debug(" - {} ({}): {} tasks (Max: {}), Expertise: {}",
                    chef.getName(),
                    chef.getEmail(),
                    chef.getCurrentTasks(),
                    Chef.getMaxTasks(),
                    chef.getExpertise()));
        }
    }
}