/**
 * 
 */
package com.oms.order.formatter;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.order.common.OrderConstantIfc;
import com.oms.order.formatter.schema.custhistin.CustHistInMessage;
import com.oms.order.formatter.schema.invoiceout.InvoiceOutMessage;
import com.oms.order.formatter.schema.orderout.OrderOutMessage;
import com.oms.order.formatter.schema.orderout.OrderOutMessage.Header;
import com.oms.order.formatter.schema.orderout.OrderOutMessage.Header.Payments;
import com.oms.order.formatter.schema.orderout.OrderOutMessage.Header.Payments.Payment;



/**
 * @author Jigar
 *
 */
@Component
public class OMSCustHistFormatter
{

	private static final Logger logger = Logger.getLogger(OMSCustHistFormatter.class);
    /**
     * 
     */
    public OMSCustHistFormatter()
    {
        // TODO Auto-generated constructor stub
    }
    
    
    public String prepareRequest( OMSCustHistRequest omsCustHistRequest)
    {
        String requestStr = null;
        try
        {
            
            
            JAXBContext jaxbContext = JAXBContext.newInstance(CustHistInMessage.class);
            
            CustHistInMessage custHistInMessage = new CustHistInMessage();
            
            CustHistInMessage.CustomerHistoryRequest  customerHistoryRequest = new CustHistInMessage.CustomerHistoryRequest ();
            
            custHistInMessage.setSource("IDC");
            custHistInMessage.setTarget("CUSTHISTIN");
            custHistInMessage.setType("CWCUSTHISTIN");
            
            customerHistoryRequest.setDirectOrderNumber( omsCustHistRequest.getOrderNumber());
            customerHistoryRequest.setCompany("51");
            customerHistoryRequest.setSendDetail("Y");
            custHistInMessage.setCustomerHistoryRequest(customerHistoryRequest);
            
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
         // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter writer = new StringWriter();
            
            jaxbMarshaller.marshal(custHistInMessage, writer);
            requestStr =  writer.getBuffer().toString();
        }
        catch( Exception ex)
        {
            ex.printStackTrace();
        }
        return requestStr;
    }
    
    public OMSCustHistResponse translateResponse( String response)
    {
        OMSCustHistResponse omsCustHistResponse = new OMSCustHistResponse();
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(OrderOutMessage.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(response);
            OrderOutMessage orderOutMessage = ( OrderOutMessage )unmarshaller.unmarshal(reader);
            
            Header header = orderOutMessage.getHeader();
            if( header != null)
            {
                Payments payments = header.getPayments();
                if( payments != null)
                {
                    List<Payment> paymentList = payments.getPayment();
                    for( Payment payment : paymentList)
                    {
                        System.out.println( "CreditCardAuthNbr : " + payment.getCreditCardAuthNbr());
                        omsCustHistResponse.setCreditCardAuthNumber(payment.getCreditCardAuthNbr());
                    }
                }
            }
            System.out.println( orderOutMessage.getType());
        }
        catch( Exception ex)
        {
            ex.printStackTrace();
        }
        
        return omsCustHistResponse;
    }
    public OMSOrderResponse formatInvoiceOutToResponseObject(String invoiceOutResponseStr) 
	{
    	OMSOrderResponse invoiceOutResponse = new OMSOrderResponse();
	  ObjectMapper mapper =  new ObjectMapper();
      try 
      {
    	  if(StringUtils.isNotEmpty(invoiceOutResponseStr))
    	  {
	          JSONObject invoiceOutJSONObject = new JSONObject(invoiceOutResponseStr);
	         
	          String invoiceOutResponseStatus=invoiceOutJSONObject.getString(OrderConstantIfc.INVOICE_OUT_JSON_STATUS_KEY);
	          invoiceOutResponse.setStatus(invoiceOutResponseStatus);
	 
	          if(invoiceOutResponseStatus.equalsIgnoreCase(OrderConstantIfc.INVOICE_OUT_RESPONSE_STATUS_OK))
	          {
	            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	            InvoiceOutMessage invoiceOutMessage=mapper.readValue( XML.toJSONObject(StringEscapeUtils.unescapeJava(invoiceOutResponseStr)).get(OrderConstantIfc.INVOICE_OUT_JSON_MESSAGE_KEY).toString(),InvoiceOutMessage.class);
	            invoiceOutResponse.setInvoiceOutMessage(invoiceOutMessage);
	          }
    	  }
      } 
      catch (Exception e) 
      {
    	  invoiceOutResponse.setStatus(OrderConstantIfc.INVOICE_OUT_RESPONSE_STATUS_FAILED);
    	  logger.error("Exception caught while reading Invoice Out Queue "+e.getStackTrace());
      }
	  return invoiceOutResponse;
	}

}
