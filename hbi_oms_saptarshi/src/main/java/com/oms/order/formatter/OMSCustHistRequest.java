package com.oms.order.formatter;


/**
 * @author Jigar
 *
 */
public class OMSCustHistRequest extends OMSOrderRequest
{
    
    private String company;
    
    private String orderNumber;
    
    private String sendDetail;
    
	public OMSCustHistRequest()
    {
        // TODO Auto-generated constructor stub
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public String getSendDetail()
    {
        return sendDetail;
    }

    public void setSendDetail(String sendDetail)
    {
        this.sendDetail = sendDetail;
    }

}
