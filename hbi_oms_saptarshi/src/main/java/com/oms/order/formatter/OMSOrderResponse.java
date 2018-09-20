/**
 * 
 */
package com.oms.order.formatter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oms.order.formatter.schema.invoiceout.InvoiceOutMessage;
import com.oms.order.formatter.schema.pickout.PickOutMessage;

/**
 * @author Jigar
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OMSOrderResponse {

	
	@JsonProperty("status")
    protected String status;

    protected InvoiceOutMessage invoiceOutMessage = null;
    
    protected PickOutMessage pickOutMessage = null;
    
	public PickOutMessage getPickOutMessage()
    {
        return pickOutMessage;
    }

    public void setPickOutMessage(PickOutMessage pickOutMessage)
    {
        this.pickOutMessage = pickOutMessage;
    }

    /**
	 * 
	 */
	public OMSOrderResponse() {
		// TODO Auto-generated constructor stub
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public InvoiceOutMessage getInvoiceOutMessage() {
		return invoiceOutMessage;
	}

	public void setInvoiceOutMessage(InvoiceOutMessage invoiceOutMessage) {
		this.invoiceOutMessage = invoiceOutMessage;
	}


}
