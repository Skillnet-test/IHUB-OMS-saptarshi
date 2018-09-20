package com.oms.order.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.oms.order.common.OrderConstantIfc;

import oracle.retail.stores.domain.arts.JdbcSaveOrderByTransaction;
import oracle.retail.stores.domain.arts.JdbcSaveRetailTransaction;
import oracle.retail.stores.domain.arts.JdbcSaveRetailTransactionLineItems;
import oracle.retail.stores.domain.arts.JdbcSaveTenderLineItems;
import oracle.retail.stores.domain.arts.JdbcUpdateOrderByTransaction;
import oracle.retail.stores.domain.transaction.OrderTransactionIfc;
import oracle.retail.stores.domain.transaction.TransactionIfc;
import oracle.retail.stores.foundation.manager.data.DataException;

@Repository
public class OMSOrderDao implements OrderConstantIfc
{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    


	public void persistOMSInvoiceOut(OrderTransactionIfc orderTransaction) 
	{
			JdbcSaveRetailTransaction jdbcTransaction= new JdbcSaveRetailTransaction();
			JdbcSaveRetailTransactionLineItems jdbcLineItemsTransaction = new JdbcSaveRetailTransactionLineItems();
			JdbcSaveOrderByTransaction jdbcSaveOrderByTransaction = new JdbcSaveOrderByTransaction();
			JdbcUpdateOrderByTransaction jdbcUpdateOrderByTransaction = new JdbcUpdateOrderByTransaction();
			JdbcSaveTenderLineItems jdbcSaveTenderLineItems = new JdbcSaveTenderLineItems();
			try 
			{
				jdbcTransaction.execute(orderTransaction, jdbcTemplate);
				jdbcLineItemsTransaction.execute(orderTransaction, jdbcTemplate);
				jdbcSaveTenderLineItems.execute(orderTransaction, jdbcTemplate);
				
				switch(orderTransaction.getTransactionType())
		        {                               // begin set operations by type
		            case TransactionIfc.TYPE_ORDER_INITIATE:
		            	jdbcSaveOrderByTransaction.execute((OrderTransactionIfc) orderTransaction, jdbcTemplate);
		                break;
		            case TransactionIfc.TYPE_ORDER_PARTIAL:
		            case TransactionIfc.TYPE_ORDER_COMPLETE:
		            case TransactionIfc.TYPE_ORDER_CANCEL:
		            	jdbcUpdateOrderByTransaction.execute((OrderTransactionIfc) orderTransaction, jdbcTemplate);
		                break;
		            default:
		                break;
		        }       
			} 
			catch (DataException e) 
			{
				e.printStackTrace();
			}
	}
	
	
	public String getTransactionSequenceNo(String storeId)
	{
		String transId=null;
		JdbcSaveRetailTransaction jdbcTransaction = new JdbcSaveRetailTransaction();
		try 
		{
			transId = jdbcTransaction.getTransactionSequenceNumber(jdbcTemplate, storeId);
		} 
		catch (DataException e) 
		{
			e.printStackTrace();
		}
		return transId; 
	}
}
