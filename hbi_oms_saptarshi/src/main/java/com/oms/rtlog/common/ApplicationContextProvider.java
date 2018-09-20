package com.oms.rtlog.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This class is used to load the beans in the context at the startup.
 */
public class ApplicationContextProvider implements ApplicationContextAware
{

    private static ApplicationContext context;


    public static ApplicationContext getApplicationContext()
    {
        return context;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        context = applicationContext;
    }

}
