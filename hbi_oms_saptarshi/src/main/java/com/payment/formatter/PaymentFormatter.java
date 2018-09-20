package com.payment.formatter;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;

import org.springframework.stereotype.Component;

import com.payment.constant.PaymentConstantIfc;
import com.payment.data.PaymentData;
import com.payment.ncresponse.Ncresponse;

@Component
public class PaymentFormatter implements PaymentConstantIfc
{

    public PaymentRequest prepareRequest(PaymentData paymentData)
    {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(paymentData.getAmount());
        paymentRequest.setCurrency(paymentData.getCurrency());
        paymentRequest.setOperation(paymentData.getOperation());
        paymentRequest.setOrderid(paymentData.getOrderid());
        paymentRequest.setPayid(paymentData.getPayid());
        paymentRequest.setPspid(paymentData.getPspid());
        paymentRequest.setUser(paymentData.getUserid());
        paymentRequest.setPassword(paymentData.getPswd());

        return paymentRequest;
    }

    public PaymentResponse translateResponse(String response, PaymentData paymentData)
    {
        PaymentResponse paymentResponse = new PaymentResponse();
        try
        {
            paymentResponse.setResponse(response);
            System.out.println(paymentResponse.getResponse());

            JAXBContext jContext = JAXBContext.newInstance(CONTEXT);
            StringReader reader = new StringReader(paymentResponse.getResponse());
            Ncresponse ncresponse = (Ncresponse)jContext.createUnmarshaller().unmarshal(reader);
            System.out.println("NCStatus :" + ncresponse.getNCSTATUS());
            paymentResponse.setNcresponse(ncresponse.getNCSTATUS());
            paymentResponse.setPayment_id(paymentData.getPayment_id());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return paymentResponse;
    }


}
