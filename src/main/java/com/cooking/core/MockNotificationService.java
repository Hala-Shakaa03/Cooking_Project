package com.cooking.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockNotificationService implements NotificationService {
    private final List<String> notifications = new ArrayList<>();
    
    @Override
    public void sendNotification(String recipient, String message) {
    	notifications.add("To:" + recipient + "|Msg:" + message);
    }
    
    public boolean wasNotificationSentAbout(String original, String substitute) {
        return notifications.stream()
            .anyMatch(n -> n.contains(original) && n.contains(substitute));
    }
    
    public List<String> getAllNotifications() {
        return Collections.unmodifiableList(notifications);
    }
    
    public void clearNotifications() {
        notifications.clear();
    }
}