/**
 * 
 */
package com.oms.order.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oms.order.common.OrderConstantIfc;
import com.oms.order.formatter.OMSCustHistResponse;
import com.oms.order.formatter.OMSOrderResponse;
import com.oms.order.formatter.schema.pickout.PickOutMessage;
import com.oms.order.formatter.schema.pickout.PickOutMessage.PickHeader;
import com.oms.order.formatter.schema.pickout.PickOutMessage.PickHeader.PickDetails;
import com.oms.order.formatter.schema.pickout.PickOutMessage.PickHeader.PickDetails.PickDetail;
import com.oms.order.service.CustHistInOrderService;
import com.oms.order.service.OMSOrderService;
import com.oms.order.service.PickOutOrderService;
import com.payment.dao.PaymentManager;
import com.payment.data.PaymentData;

/**
 * @author Jigar
 */
@Component
public class OMSPickOutScheduledTask
{
    protected static final Logger logger = Logger.getLogger(OMSPickOutScheduledTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    PickOutOrderService pickOutOrderService;
    
    @Autowired
    CustHistInOrderService custHistInOrderService;

    @Autowired
    PaymentManager paymentManager;

    /**
     * 
     */
    public OMSPickOutScheduledTask()
    {
        logger.info("OMSPickOutScheduledTask Initialised");
    }

    @Scheduled(cron = "${pickout.cron.expression}")
    public void getOMSInvoiceOut()
    {
        logger.info("Get OMS Pick OUT Started at " + dateFormat.format(new Date()));
        OMSOrderResponse orderFromOMS = pickOutOrderService.getOrderFromOMS();
        if (!orderFromOMS.getStatus().equalsIgnoreCase(OrderConstantIfc.INVOICE_OUT_RESPONSE_STATUS_EOF))
        {
            PaymentData paymentData = paymentManager.parsePaymentData(orderFromOMS);
            // PayId will get from CUSTHistory
            OMSCustHistResponse omsCustHistResponse = custHistInOrderService.getOrderFromOMS(paymentData.getOrderid());
            String creditCardAuthNumber = omsCustHistResponse.getCreditCardAuthNumber();
            if( creditCardAuthNumber != null)
            {
                paymentData.setPayid(creditCardAuthNumber);
            }
            else
            {
                paymentData.setPayid("NOT");
            }
            //paymentData.setPayid("3027070765");
            paymentManager.insertPaymentData(paymentData);
        }
        logger.info("OMS Pick OUT Ended at " + dateFormat.format(new Date()));

    }

}
