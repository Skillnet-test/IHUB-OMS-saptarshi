/**
 * 
 */
package com.oms.order.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oms.order.common.OrderConstantIfc;
import com.oms.order.connector.OMSOrderConnector;
import com.oms.order.dao.OMSOrderManager;
import com.oms.order.formatter.OMSOrderFormatter;
import com.oms.order.formatter.OMSOrderRequest;
import com.oms.order.formatter.OMSOrderResponse;

/**
 * @author Jigar
 *
 */
@Service
public class OMSOrderService 
{
	
	private static final Logger logger = Logger.getLogger(OMSOrderService.class);
	
	@Autowired
	OMSOrderConnector omsOrderConnector;
	
	@Autowired
	OMSOrderFormatter omsOrderFormatter;
	
	@Autowired
	OMSOrderManager omsOrderManager;

	/**
	 * 
	 */
	public OMSOrderService() 
	{

	}
	
	public String getOrderFromOMS()
	{
		OMSOrderResponse omsInvoiceOutResponse=null;
		String responseStr="";
		try 
		{
			OMSOrderRequest omsOrderRequest= new OMSOrderRequest();
			omsOrderRequest.setEndpoint("https://hob01-oroms-uat.retail.oracleindustry.com/JMS-REST/jms/jmsREST/getFromQueue");
			omsOrderRequest.setAuthorizationValue("Basic U05TOlNraWxsQDEyMzRz");
			omsOrderRequest.setContentTypeValue("application/json");
			
			
			JSONObject invoiceOutJSONObjectRequest = new JSONObject();
			invoiceOutJSONObjectRequest.put(OrderConstantIfc.INVOICE_OUT_JSON_QUEUE_NAME_KEY,OrderConstantIfc.INVOICE_OUT_JSON_INVOICE_OUT_REQUEST ); 
			
			omsOrderRequest.setRequest(invoiceOutJSONObjectRequest.toString());

			responseStr=(String) omsOrderConnector.processRequest(omsOrderRequest);
			
			omsInvoiceOutResponse=omsOrderFormatter.formatInvoiceOutToResponseObject(responseStr);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			
		}

		if(omsInvoiceOutResponse.getStatus().equalsIgnoreCase(OrderConstantIfc.INVOICE_OUT_RESPONSE_STATUS_EOF))
		  return "No Order Found to Process!";
		else
			omsOrderManager.persistOMSInvoiceOut(omsInvoiceOutResponse);
		
		
		
		return omsInvoiceOutResponse.getStatus().toString();
	}
	
	
	

}
