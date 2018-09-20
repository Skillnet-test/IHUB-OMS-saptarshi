package com.payment.formatter;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oms.order.connector.OMSOrderConnector;
import com.oms.order.formatter.OMSOrderRequest;
import com.payment.data.PaymentData;

@Component
public class AdyenPaymentFormatter
{
    private static final String REFERENCE = "reference";

    private static final String ORIGINAL_REFERENCE = "originalReference";

    private static final String MODIFICATION_AMOUNT = "modificationAmount";

    private static final String CURRENCY = "currency";

    private static final String VALUE = "value";

    private static final String MERCHANT_ACCOUNT = "merchantAccount";

    @Autowired
    OMSOrderConnector omsOrderConnector;
    
    @Value("${adyen.refund.url}")
    private String adyenRefundEndPoint;
    
    @Value("${adyen.authorization.value}")
    private String adyenAuthorizationValue;
    
    
    @Value("${adyen.contentType}")
    private String adyenContentType;
    
    
    
    private final String PAY_SUCCESS = "1";

    
    public PaymentResponse translateResponse(String response, PaymentData paymentData)
    {
        PaymentResponse paymentResponse = new PaymentResponse();
        try
        {
            paymentResponse.setResponse(response);
            paymentResponse.setNcresponse(PAY_SUCCESS);
            paymentResponse.setPayment_id(paymentData.getPayment_id());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return paymentResponse;

    }

    public OMSOrderRequest prepareRequest(PaymentData paymentData)
    {
        JSONObject requestObject = new JSONObject();

        requestObject.put(MERCHANT_ACCOUNT, paymentData.getPspid());

        JSONObject modificationRequest = new JSONObject();

        modificationRequest.put(VALUE, paymentData.getAmount());
        modificationRequest.put(CURRENCY, paymentData.getCurrency());

        requestObject.put(MODIFICATION_AMOUNT, modificationRequest);
        requestObject.put(ORIGINAL_REFERENCE, paymentData.getPayid());
        requestObject.put(REFERENCE, paymentData.getOrderid());

        System.out.print(requestObject);

        OMSOrderRequest omsOrderRequest = new OMSOrderRequest();
        omsOrderRequest.setEndpoint(adyenRefundEndPoint);
        omsOrderRequest.setAuthorizationValue(adyenAuthorizationValue);
        omsOrderRequest.setContentTypeValue(adyenContentType);

        omsOrderRequest.setRequest(requestObject.toString());

        return omsOrderRequest;

    }

   }
