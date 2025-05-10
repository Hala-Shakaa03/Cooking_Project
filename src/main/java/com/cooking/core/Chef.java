package com.cooking.core;

public class Chef {
	private String name;
    private String email;
    private String expertise;
    private int currentTasks;
    private static final int MAX_TASKS = 5;
    private NotificationService notificationService;
    
    public Chef(String email, NotificationService notificationService) {
    	this.email = email;
    	this.notificationService = notificationService;
    }

    public Chef(String name, String email, String expertise, int currentTaskCount) {
        this.name = name;
        this.email = email;
        this.expertise = expertise;
        this.currentTasks = currentTaskCount;      
    }
    public void addTask(CookingTask task) {
        if (!canAcceptTask()) {
            throw new IllegalStateException("Chef is at maximum task capacity");
        }
        currentTasks++;
    }
    public static int getMaxTasks() {
        return MAX_TASKS;
    }
    public boolean canAcceptTask() {
        return currentTasks < MAX_TASKS;
    }
   /* public void assignTask(CookingTask task) {
        currentTasks++;
    }*/
    public String getName() { return name; }
    public String getExpertise() { return expertise; }
    public int getCurrentTasks() { return currentTasks; }
    public NotificationService getNotificationService() { return notificationService; }
    public String getEmail() {
        return email;
    }
    
    public boolean canApproveSubstitution() {
        return true;
    }
    
    public boolean canModifySubstitution() {
        return true;
    }
    
    public void notifyAboutSubstitution(String original, String substitute) {
        notificationService.sendNotification(email, 
            "Substitution suggested: " + original + " → " + substitute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chef chef = (Chef) o;
        return email.equalsIgnoreCase(chef.email);
    }
    
}