package com.payment.data;

import java.io.Serializable;

public class PaymentData implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int payment_id;

    private String amount;

    private String currency;

    private String operation;

    private String orderid;

    private String payid;

    private String pspid;

    private String pswd;

    private String userid;

    private int id_status;
    
    private String company_Code;

    public String getCompany_Code()
    {
        return company_Code;
    }

    public void setCompany_Code(String company_Code)
    {
        this.company_Code = company_Code;
    }

    public int getPayment_id()
    {
        return payment_id;
    }

    public void setPayment_id(int payment_id)
    {
        this.payment_id = payment_id;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getOperation()
    {
        return operation;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    public String getOrderid()
    {
        return orderid;
    }

    public void setOrderid(String orderid)
    {
        this.orderid = orderid;
    }

    public String getPayid()
    {
        return payid;
    }

    public void setPayid(String payid)
    {
        this.payid = payid;
    }

    public String getPspid()
    {
        return pspid;
    }

    public void setPspid(String pspid)
    {
        this.pspid = pspid;
    }

    public String getPswd()
    {
        return pswd;
    }

    public void setPswd(String pswd)
    {
        this.pswd = pswd;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }

    public int getId_status()
    {
        return id_status;
    }

    public void setId_status(int id_status)
    {
        this.id_status = id_status;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((company_Code == null) ? 0 : company_Code.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + id_status;
        result = prime * result + ((operation == null) ? 0 : operation.hashCode());
        result = prime * result + ((orderid == null) ? 0 : orderid.hashCode());
        result = prime * result + ((payid == null) ? 0 : payid.hashCode());
        result = prime * result + payment_id;
        result = prime * result + ((pspid == null) ? 0 : pspid.hashCode());
        result = prime * result + ((pswd == null) ? 0 : pswd.hashCode());
        result = prime * result + ((userid == null) ? 0 : userid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PaymentData other = (PaymentData)obj;
        if (amount == null)
        {
            if (other.amount != null)
                return false;
        }
        else if (!amount.equals(other.amount))
            return false;
        if (company_Code == null)
        {
            if (other.company_Code != null)
                return false;
        }
        else if (!company_Code.equals(other.company_Code))
            return false;
        if (currency == null)
        {
            if (other.currency != null)
                return false;
        }
        else if (!currency.equals(other.currency))
            return false;
        if (id_status != other.id_status)
            return false;
        if (operation == null)
        {
            if (other.operation != null)
                return false;
        }
        else if (!operation.equals(other.operation))
            return false;
        if (orderid == null)
        {
            if (other.orderid != null)
                return false;
        }
        else if (!orderid.equals(other.orderid))
            return false;
        if (payid == null)
        {
            if (other.payid != null)
                return false;
        }
        else if (!payid.equals(other.payid))
            return false;
        if (payment_id != other.payment_id)
            return false;
        if (pspid == null)
        {
            if (other.pspid != null)
                return false;
        }
        else if (!pspid.equals(other.pspid))
            return false;
        if (pswd == null)
        {
            if (other.pswd != null)
                return false;
        }
        else if (!pswd.equals(other.pswd))
            return false;
        if (userid == null)
        {
            if (other.userid != null)
                return false;
        }
        else if (!userid.equals(other.userid))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "PaymentData [payment_id=" + payment_id + ", amount=" + amount + ", currency=" + currency
                + ", operation=" + operation + ", orderid=" + orderid + ", payid=" + payid + ", pspid=" + pspid
                + ", pswd=" + pswd + ", userid=" + userid + ", id_status=" + id_status + ", company_Code="
                + company_Code + "]";
    }


}
