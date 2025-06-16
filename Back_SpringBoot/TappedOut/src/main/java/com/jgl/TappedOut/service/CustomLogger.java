package com.jgl.TappedOut.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Utility class to handle how to log in every service
 * 
 * ? This class needs to be injected at every service to be used
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Component
public class CustomLogger {
    private final Logger logger;

    public CustomLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public void trace(String msg) {
        logger.trace(msg);
    }

    public void trace(String format, Object... args) {
        logger.trace(format, args);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void debug(String format, Object... args) {
        logger.debug(format, args);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(String format, Object... args) {
        logger.info(format, args);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void warn(String format, Object... args) {
        logger.warn(format, args);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String format, Object... args) {
        logger.error(format, args);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}