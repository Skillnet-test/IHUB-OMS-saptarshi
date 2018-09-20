/**
 * 
 */
package com.oms.order.formatter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.order.common.OrderConstantIfc;
import com.oms.order.formatter.schema.pickout.PickOutMessage;

/**
 * @author Jigar
 */
@Component
public class PickOutOrderFormatter
{

    private static final Logger logger = Logger.getLogger(PickOutOrderFormatter.class);

    /**
     * 
     */
    public PickOutOrderFormatter()
    {
        // TODO Auto-generated constructor stub
    }

    public OMSOrderResponse formatPickOutToResponseObject(String invoiceOutResponseStr)
    {
        OMSOrderResponse pickOutResponse = new OMSOrderResponse();
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            if (StringUtils.isNotEmpty(invoiceOutResponseStr))
            {
                JSONObject invoiceOutJSONObject = new JSONObject(invoiceOutResponseStr);

                String pickOutResponseStatus = invoiceOutJSONObject
                        .getString(OrderConstantIfc.INVOICE_OUT_JSON_STATUS_KEY);
                pickOutResponse.setStatus(pickOutResponseStatus);

                if (pickOutResponseStatus.equalsIgnoreCase(OrderConstantIfc.INVOICE_OUT_RESPONSE_STATUS_OK))
                {
                    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                    PickOutMessage pickOutMessage = mapper.readValue(
                            XML.toJSONObject(StringEscapeUtils.unescapeJava(invoiceOutResponseStr))
                                    .get(OrderConstantIfc.INVOICE_OUT_JSON_MESSAGE_KEY).toString(),
                            PickOutMessage.class);
                    pickOutResponse.setPickOutMessage(pickOutMessage);
                }
            }
        }
        catch (Exception e)
        {
            pickOutResponse.setStatus(OrderConstantIfc.INVOICE_OUT_RESPONSE_STATUS_FAILED);
            logger.error("Exception caught while reading Pick Out Queue " + e.getStackTrace());
        }
        return pickOutResponse;
    }

}
