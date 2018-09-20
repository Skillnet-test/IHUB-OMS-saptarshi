package com.oms.order.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oms.order.service.OMSOrderService;

/**
 * Handles requests for the Employee JDBC Service.
 */
@RestController
public class OMSOrderController {
	
	private static final Logger logger = Logger.getLogger(OMSOrderController.class);
	
	@Autowired
	OMSOrderService omsOrderService;
	
	
	
	@RequestMapping(value = "/oms/getInvoiceOutQueue", method = RequestMethod.GET)
	public String getOMSInvoiceOutQueue() 
	{
		String response="";
		logger.info("Get OMS Invoice Out Queue.");
		try
        {
			response=omsOrderService.getOrderFromOMS();
        }
        catch (Exception e)
        {
            logger.error("Error getting Order");
        }
		
		return response;
	}

}
