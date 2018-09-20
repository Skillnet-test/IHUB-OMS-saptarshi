package com.payment.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oms.order.common.OrderConstantIfc;
import com.oms.order.connector.OMSOrderConnector;
import com.oms.order.formatter.OMSOrderRequest;
import com.oms.order.formatter.OMSOrderResponse;
import com.oms.order.service.PickOutOrderService;
import com.payment.constant.PaymentConstantIfc;
import com.payment.data.PaymentData;
import com.payment.formatter.AdyenPaymentFormatter;
import com.payment.formatter.PaymentFormatter;
import com.payment.formatter.PaymentRequest;
import com.payment.formatter.PaymentResponse;

import okhttp3.FormBody;
import okhttp3.Response;

@Component
public class PaymentService implements PaymentConstantIfc
{

    private static final Logger logger = Logger.getLogger(PaymentService.class);

    @Autowired
    OMSOrderConnector omsOrderConnector;
    
    @Autowired
    PaymentFormatter paymentFormatter;
    
    @Autowired
    AdyenPaymentFormatter adyenPaymentFormatter;
    
    @Value("${ingenico.maintenancedirect.url}")
    private String ingenicoMaintenancedirectUrl;

    @Value("${ingenico.contentType}")
    private String ingenicoContentType;
    
    /**
     * 
     */
    public PaymentService()
    {
    }

    public PaymentResponse processPayment( PaymentData paymentData)
    {
        PaymentResponse paymentResponse = null;
        
        try
        {
            PaymentRequest paymentRequest = paymentFormatter.prepareRequest(paymentData);
            FormBody formBody = new FormBody.Builder().add(AMOUNT, paymentRequest.getAmount())
                    .add(CURRENCY, paymentRequest.getCurrency()).add(OPERATION, paymentRequest.getOperation())
                    .add(ORDERID, paymentRequest.getOrderid()).add(PAYID, paymentRequest.getPayid())
                    .add(PSPID, paymentRequest.getPspid()).add(PSWD, paymentRequest.getPassword())
                    .add(USERID, paymentRequest.getUser()).build();
            
            OMSOrderRequest omsOrderRequest = new OMSOrderRequest();
            omsOrderRequest
                    .setEndpoint(ingenicoMaintenancedirectUrl);
            omsOrderRequest.setContentTypeValue(ingenicoContentType);
            omsOrderRequest.setFormBody(formBody);
            String response = (String)omsOrderConnector.processRequest(omsOrderRequest);
            paymentResponse = paymentFormatter.translateResponse(response, paymentData);
            
            //Check on company code for Adyen
            //OMSOrderRequest omsOrderRequest1 = adyenPaymentFormatter.prepareRequest(paymentData);
            //response = (String)omsOrderConnector.processRequest(omsOrderRequest);
            //System.out.println(response);
            //paymentResponse = adyenPaymentFormatter.translateResponse(response, paymentData);
            
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        
        return paymentResponse;

    }
}
