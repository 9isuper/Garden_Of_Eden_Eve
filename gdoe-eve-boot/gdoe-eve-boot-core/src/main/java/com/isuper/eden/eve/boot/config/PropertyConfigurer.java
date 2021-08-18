package com.isuper.eden.eve.boot.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Component
public class PropertyConfigurer implements ApplicationContextAware {

    private static ApplicationContext appCtx;

    public static String getContextProperty(String name) {
        return appCtx.getEnvironment().getProperty(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }
}
