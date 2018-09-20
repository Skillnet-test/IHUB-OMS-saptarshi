package com.oms.order.formatter;

import okhttp3.FormBody;

/**
 * @author Jigar
 *
 */
public class OMSOrderRequest
{
    private String contentTypeValue;
    
    private String endpoint;
    
    private String authorizationValue;
	
    protected String request;
    
    protected FormBody formBody;
    
    public FormBody getFormBody()
    {
        return formBody;
    }

    public void setFormBody(FormBody formBody)
    {
        this.formBody = formBody;
    }

    /**
     * @return the request
     */
    public String getRequest()
    {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(String request)
    {
        this.request = request;
    }

    public String getContentTypeValue() {
		return contentTypeValue;
	}

	public void setContentTypeValue(String contentTypeValue) {
		this.contentTypeValue = contentTypeValue;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAuthorizationValue() {
		return authorizationValue;
	}

	public void setAuthorizationValue(String authorizationValue) {
		this.authorizationValue = authorizationValue;
	}

	public OMSOrderRequest()
    {
        // TODO Auto-generated constructor stub
    }

}
