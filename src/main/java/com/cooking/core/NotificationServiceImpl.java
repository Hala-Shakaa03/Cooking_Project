// NotificationServiceImpl.java (production implementation)
package com.cooking.core;

public class NotificationServiceImpl implements NotificationService {
	@Override
    public void sendNotification(String recipient, String message) {
        // Actual notification logic (email, SMS, etc.)
        System.out.println("[PRODUCTION] Sending to " + recipient + ": " + message);
    }

    @Override
    public boolean wasNotificationSentAbout(String original, String substitute) {
        throw new UnsupportedOperationException(
            "This tracking method should only be used in test implementations");
    }

    @Override
    public void clearNotifications() {
        // No-op in production
        System.out.println("[PRODUCTION] Notification clearing requested - no action taken");
    }
}