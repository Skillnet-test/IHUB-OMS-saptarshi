package com.oms.order.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oms.order.formatter.OMSOrderFormatter;
import com.oms.order.formatter.OMSOrderResponse;

import oracle.retail.stores.domain.transaction.OrderTransactionIfc;

@Component
public class OMSOrderManager
{

    @Autowired
    OMSOrderDao omsOrderDao;
    
    @Autowired
    OMSOrderFormatter omsOrderFormatter;
    


	public void persistOMSInvoiceOut(OMSOrderResponse omsInvoiceOutResponse)
	{
		OrderTransactionIfc orderTransaction = omsOrderFormatter.formatInvoicOutResponseToPOSObject(omsInvoiceOutResponse);
		omsOrderDao.persistOMSInvoiceOut(orderTransaction);
		
		OrderTransactionIfc completedOrderTransaction= omsOrderFormatter.getCompletedOrderTransaction(orderTransaction);
		omsOrderDao.persistOMSInvoiceOut(completedOrderTransaction);
		System.out.println();
		
	}
}
