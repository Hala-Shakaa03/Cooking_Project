package com.cooking.core;

public interface NotificationService {
    void sendNotification(String recipient, String message);
    
    // Add these methods for testing support
    boolean wasNotificationSentAbout(String original, String substitute);
    void clearNotifications();
}