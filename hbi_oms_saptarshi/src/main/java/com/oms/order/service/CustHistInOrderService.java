/**
 * 
 */
package com.oms.order.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oms.order.common.OrderConstantIfc;
import com.oms.order.connector.OMSOrderConnector;
import com.oms.order.formatter.OMSCustHistFormatter;
import com.oms.order.formatter.OMSCustHistRequest;
import com.oms.order.formatter.OMSCustHistResponse;
import com.oms.order.formatter.OMSOrderFormatter;
import com.oms.order.formatter.OMSOrderRequest;
import com.oms.order.formatter.OMSOrderResponse;
import com.oms.order.formatter.PickOutOrderFormatter;

/**
 * @author Jigar
 */
@Service
public class CustHistInOrderService
{

    private static final Logger logger = Logger.getLogger(CustHistInOrderService.class);

    @Autowired
    OMSOrderConnector omsOrderConnector;

    @Autowired
    OMSCustHistFormatter omsCustHistFormatter;
    
    @Value("${oms.cwMessageIn.url}")
    private String omscwMessageEndPoint;
    
    @Value("${oms.authorization.value}")
    private String omsAuthorizationValue;

    @Value("${oms.cwMessageIn.contentType}")
    private String omscwMessageInContentType;

    /**
     * 
     */
    public CustHistInOrderService()
    {

    }

    public OMSCustHistResponse getOrderFromOMS( String orderID)
    {
        OMSCustHistResponse omsCustHistResponse = null;
        OMSCustHistRequest omsCustHistRequest = new OMSCustHistRequest();
        omsCustHistRequest
                .setEndpoint(omscwMessageEndPoint);
        omsCustHistRequest.setAuthorizationValue(omsAuthorizationValue);
        omsCustHistRequest.setContentTypeValue(omscwMessageInContentType);
        
        omsCustHistRequest.setOrderNumber(orderID);;
        String request = omsCustHistFormatter.prepareRequest(omsCustHistRequest);
        omsCustHistRequest.setRequest(request);     
        try
        {
            String response = (String)omsOrderConnector.processRequest(omsCustHistRequest);
            omsCustHistResponse = omsCustHistFormatter.translateResponse(response);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return omsCustHistResponse;

    }

}
