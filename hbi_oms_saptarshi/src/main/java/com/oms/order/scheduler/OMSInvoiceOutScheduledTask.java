/**
 * 
 */
package com.oms.order.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oms.companycode.dao.CompanyCodeManager;
import com.oms.order.service.OMSOrderService;

/**
 * @author Jigar
 *
 */
@Component
public class OMSInvoiceOutScheduledTask
{
    protected static final Logger logger = Logger.getLogger(OMSInvoiceOutScheduledTask.class);
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
	@Autowired
	OMSOrderService omsOrderService;
	
	@Autowired
	CompanyCodeManager companyCodeManager;
    
    /**
     * 
     */
    public OMSInvoiceOutScheduledTask()
    {
        logger.info("OMSInvoiceOutScheduledTask Initialised");
    }
    


    @Scheduled(cron = "${invoiceout.cron.expression}")
    public void getOMSInvoiceOut()
    {
    	String s=companyCodeManager.getCompanyDescription(1);
    	System.out.println("saptarshi-checking"+s);
    	
        logger.info("Get OMS Invoice OUT Started at "+dateFormat.format(new Date()));
        omsOrderService.getOrderFromOMS();
        logger.info("OMS Invoice OUT Ended at "+dateFormat.format(new Date()));

    }



}
