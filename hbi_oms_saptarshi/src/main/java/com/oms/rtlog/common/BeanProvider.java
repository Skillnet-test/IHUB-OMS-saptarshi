package com.oms.rtlog.common;

import org.springframework.context.ApplicationContext;



/**
 * This class is used a helper class to find get the general or the specific
 * bean from spring context
 */
public class BeanProvider
{

    private BeanProvider()
    {
    }

    /**
     * Gets the bean from the Spring Context loaded
     * 
     * @param beanId
     * @return
     */
    public static Object getBean(String beanId)
    {
        Object bean;
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        bean = context.getBean(beanId);
        return bean;
    }

 /*   public static DomainObjectFactoryIfc getDomainFactory()
    {
        DomainObjectFactoryIfc domainFactory = (DomainObjectFactoryIfc)getBean(
                BeanProviderConstantsIfc.BEAN_DOMAIN_FACTORY);
        return domainFactory;
    }

    public static DataServiceManagerIfc getDataServiceManager()
    {
        DataServiceManagerIfc dataServiceManager = (DataServiceManagerIfc)getBean(
                BeanProviderConstantsIfc.BEAN_DATA_SERVICE_MANAGER);
        return dataServiceManager;
    }*/

}
