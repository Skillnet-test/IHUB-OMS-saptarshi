package com.payment.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.payment.dao.PaymentManager;

@Component
public class PaymentScheduledTask
{

    protected static final Logger logger = Logger.getLogger(PaymentScheduledTask.class);
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Autowired
    PaymentManager paymentManager;
    
    @Scheduled(cron = "${payment.cron.expression}")
    public void paymentSettlement()
    {
        logger.info(" Payment Settlement Generation Started at "+dateFormat.format(new Date()));
        try
        {
            paymentManager.paymentSettlement();
            logger.info("Payment Settlement Generation Completed at "+dateFormat.format(new Date()));
        }
        catch (Exception e)
        {
            logger.error("Error while Payment Settlement Generation");
        }
    }
}
