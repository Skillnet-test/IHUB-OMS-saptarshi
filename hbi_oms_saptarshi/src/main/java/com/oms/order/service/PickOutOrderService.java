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
import com.oms.order.formatter.OMSOrderFormatter;
import com.oms.order.formatter.OMSOrderRequest;
import com.oms.order.formatter.OMSOrderResponse;
import com.oms.order.formatter.PickOutOrderFormatter;

/**
 * @author Jigar
 */
@Service
public class PickOutOrderService
{

    private static final Logger logger = Logger.getLogger(PickOutOrderService.class);

    @Autowired
    OMSOrderConnector omsOrderConnector;

    @Autowired
    PickOutOrderFormatter pickOutOrderFormatter;
    
    @Value("${oms.getfromQueue.url}")
    private String omsGetFromQueueEndPoint;
    
    @Value("${oms.authorization.value}")
    private String omsAuthorizationValue;

    @Value("${oms.getfromQueue.contentType}")
    private String omsgetfromQueueContentType;
    
    /**
     * 
     */
    public PickOutOrderService()
    {

    }

    public OMSOrderResponse getOrderFromOMS()
    {
        OMSOrderResponse omsPickOutResponse = null;
        String responseStr = "";
        try
        {
            OMSOrderRequest omsOrderRequest = new OMSOrderRequest();
            omsOrderRequest
                    .setEndpoint(omsGetFromQueueEndPoint);
            omsOrderRequest.setAuthorizationValue(omsAuthorizationValue);
            omsOrderRequest.setContentTypeValue(omsgetfromQueueContentType);

            JSONObject invoiceOutJSONObjectRequest = new JSONObject();
            invoiceOutJSONObjectRequest.put(OrderConstantIfc.INVOICE_OUT_JSON_QUEUE_NAME_KEY,
                    OrderConstantIfc.INVOICE_OUT_JSON_PICK_OUT_REQUEST);

            omsOrderRequest.setRequest(invoiceOutJSONObjectRequest.toString());

            responseStr = (String)omsOrderConnector.processRequest(omsOrderRequest);
            
            
            /*StringBuffer buf = new StringBuffer();
            BufferedReader in;
            in = new BufferedReader(new FileReader("D:\\test.txt"));
            String line = null;
            try
            {
                while ((line = in.readLine()) != null)
                {
                    buf.append(line);
                }
            }
            finally
            {
                in.close();
            }
            responseStr = buf.toString();*/
            
            
            omsPickOutResponse = pickOutOrderFormatter.formatPickOutToResponseObject(responseStr);
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }

        if (omsPickOutResponse.getStatus().equalsIgnoreCase(OrderConstantIfc.INVOICE_OUT_RESPONSE_STATUS_EOF))
        {
            System.out.println("No Order Found to Process for Pick Out!");
        }

        return omsPickOutResponse;
    }

}
