package com.payment.formatter;

import com.payment.ncresponse.Ncresponse;

public class PaymentResponse
{

    private String response;
    
    private int payment_id;
    
    private String ncresponse;

    public String getResponse()
    {
        return response;
    }

    public void setResponse(String response)
    {
        this.response = response;
    }

    public int getPayment_id()
    {
        return payment_id;
    }

    public void setPayment_id(int payment_id)
    {
        this.payment_id = payment_id;
    }

    public String getNcresponse()
    {
        return ncresponse;
    }

    public void setNcresponse(String ncresponse)
    {
        this.ncresponse = ncresponse;
    }

    @Override
    public String toString()
    {
        return "PaymentResponse [response=" + response + ", payment_id=" + payment_id + ", ncresponse=" + ncresponse
                + "]";
    }

  

}
