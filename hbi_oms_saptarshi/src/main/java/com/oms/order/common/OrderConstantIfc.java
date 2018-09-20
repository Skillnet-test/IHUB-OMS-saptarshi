package com.oms.order.common;

public interface OrderConstantIfc
{

	public static final String INVOICE_OUT_JSON_QUEUE_NAME_KEY="queueName";
	
	public static final String INVOICE_OUT_JSON_INVOICE_OUT_REQUEST="INVOIC_OUT_OUT";
	
	public static final String INVOICE_OUT_JSON_PICK_OUT_REQUEST="PICK_OUT_OUT";
	
    public static final String INVOICE_OUT_JSON_STATUS_KEY = "status";
    
    public static final String INVOICE_OUT_JSON_MESSAGE_KEY = "Message";

    public static final String INVOICE_OUT_RESPONSE_STATUS_OK = "OK";

    public static final String INVOICE_OUT_RESPONSE_STATUS_EOF = "EOF";
    
    public static final String INVOICE_OUT_RESPONSE_STATUS_FAILED = "FALIED";
    
    public static final String REQUEST_AUTHORIZATION_TOKEN_NAME="authorization";
    
    public static final String REQUEST_CONTENT_TYPE_TOKEN_NAME="content-type"; 
    
    public static final int ORDER_STATUS_UNDEFINED = -1;
	public static final int ORDER_STATUS_NEW = 0;
	public static final int ORDER_STATUS_PRINTED = 1;
	public static final int ORDER_STATUS_PARTIAL = 2;
	public static final int ORDER_STATUS_FILLED = 3;
	public static final int ORDER_STATUS_CANCELED = 4;
	public static final int ORDER_STATUS_COMPLETED = 5;
	public static final int ORDER_STATUS_VOIDED = 6;
	public static final int ORDER_STATUS_SUSPENDED_CANCELED = 7;
	
	public static final int ORDER_TYPE_SPECIAL = 1;
	public static final int ORDER_TYPE_ON_HAND = 2;
	public static final int ORDER_TYPE_WEB = 3;
	public static final int ORDER_TYPE_UNDEFINED = -1;
	
    
    public static final int ORDER_CHANNEL_MOBILE_APP = 0;

    public static final int ORDER_CHANNEL_CALL_CENTER = 1;
    
    public static final int ORDER_CHANNEL_OIS = 2;
    
    public static final int ORDER_CHANNEL_WEB = 3;

    public static final int ORDER_CHANNEL_NOT_APPLICABLE = -1;

    public static final String[] ORDER_CHANNEL_DESCRIPTORS = { "A", "C", "P","I" };

    public static final String[] ORDER_CHANNEL_FULL_DESCRIPTORS = { "Mobile App", "Call Center","OIS","Internet" };
    
    public static final String WORKSTATION_ID="001";
    
    public static final String TILL_ID="001";
    
    public static final String EMPLOYEE_ID="0";
    
    public static final String RFL_REASON_CODE="999";
    
    public static final String RFL_REASON_CODE_NAME="REWARD DISCOUNT";
    
    public static final String DATE_FORMAT_YYYY_MM_DD="YYYY-MM-DD";

}
