package com.oms;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcProperties.LocaleResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.oms.companycode.dao.CompanyCodeDao;
import com.oms.companycode.dao.CompanyCodeManager;
import com.oms.companycode.data.CompanyCodeData;



@SpringBootApplication
@ComponentScan({ "com.oms","oracle", "com.payment","payment" })
@EnableScheduling
public class Application {
	
		
    public static void main(String[] args) {
    	// getting the context object of ConfigurableApplicationContext
    	ConfigurableApplicationContext context =SpringApplication.run(Application.class, args);
    	CompanyCodeDao companyCode=context.getBean(CompanyCodeDao.class);
    	companyCode.getcompanyCodeData(); //geting the data in startup 
    	CompanyCodeManager companyCodeManage=context.getBean(CompanyCodeManager.class);
    	companyCodeManage.getCompanyCode();// geting the list of data in manager class.
    }

    @Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) 
    {
        return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {

			    System.out.println("Let's inspect the beans provided by Spring Boot:");

			    String[] beanNames = ctx.getBeanDefinitionNames();
			    Arrays.sort(beanNames);
			    for (String beanName : beanNames) {
			        System.out.println(beanName);
			    }
			    
			    

			}
		};
		
		
		
    }
    
    
}