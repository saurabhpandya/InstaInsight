package com.instainsight.listeners;

/**
 * Base for all Listener interfaces in the app. Only concerned with error handling
 */
public interface BaseListener {
    void onError(String header, String message);
}