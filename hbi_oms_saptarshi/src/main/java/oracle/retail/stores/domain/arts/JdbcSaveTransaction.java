/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
/* ===========================================================================
* Copyright (c) 1998, 2011, Oracle and/or its affiliates. All rights reserved. 
 * ===========================================================================
 * $Header: rgbustores/modules/domain/src/oracle/retail/stores/domain/arts/JdbcSaveTransaction.java /rgbustores_13.4x_generic_branch/1 2011/05/04 11:49:00 mszekely Exp $
 * ===========================================================================
 * NOTES
 * <other useful comments, qualifications, etc.>
 *
 * MODIFIED    (MM/DD/YY)
 *    cgreene   05/26/10 - convert to oracle packaging
 *    cgreene   04/26/10 - XbranchMerge cgreene_tech75 from
 *                         st_rgbustores_techissueseatel_generic_branch
 *    cgreene   03/30/10 - remove deprecated ARTSDatabaseIfcs and change
 *                         SQLException to DataException
 *    abondala  01/03/10 - update header date
 *    npoola    02/26/09 - moved the logic from JDBCSaveTransaction to the
 *                         TenderCompleteRoad
 *    mahising  02/25/09 - Fixed dummy pickup delivery order issue
 *    mchellap  01/07/09 - Changes to getTransactionEndDateString to avoid
 *                         setting transactions' endtime
 *    acadar    11/11/08 - forward port for bug id: 7355567
 *
 * ===========================================================================
 * $Log:
 *    4    360Commerce 1.3         3/25/2008 2:40:32 PM   Vikram Gopinath CR
 *         #29942, ported changes from v12x.
 *    3    360Commerce 1.2         4/1/2005 2:58:45 AM    Robert Pearse
 *    2    360Commerce 1.1         3/10/2005 9:52:50 PM   Robert Pearse
 *    1    360Commerce 1.0         2/11/2005 11:42:04 PM  Robert Pearse
 *
 *   Revision 1.11  2004/07/12 14:38:07  jdeleau
 *   @scr 6153 Fix return on gift card, removing print statement
 *
 *   Revision 1.10  2004/07/09 20:59:25  jdeleau
 *   @scr 6077 If the dates on the EJ screen are both blank dont throw an
 *   error message, instead use the  business date
 *
 *   Revision 1.9  2004/04/15 20:49:22  blj
 *   @scr 3871 - fixed problems with postvoid.
 *
 *   Revision 1.8  2004/04/09 15:27:20  bjosserand
 *   @scr 4093 Transaction Reentry
 *
 *   Revision 1.7  2004/04/08 22:32:45  bjosserand
 *   @scr 4093 Transaction Reentry
 *
 *   Revision 1.6  2004/04/08 22:04:15  bjosserand
 *   @scr 4093 Transaction Reentry
 *
 *   Revision 1.5  2004/02/17 17:57:37  bwf
 *   @scr 0 Organize imports.
 *
 *   Revision 1.4  2004/02/17 16:18:46  rhafernik
 *   @scr 0 log4j conversion
 *
 *   Revision 1.3  2004/02/12 17:13:19  mcs
 *   Forcing head revision
 *
 *   Revision 1.2  2004/02/11 23:25:23  bwf
 *   @scr 0 Organize imports.
 *
 *   Revision 1.1.1.1  2004/02/11 01:04:28  cschellenger
 *   updating to pvcs 360store-current
 *
 *
 *
 *    Rev 1.2   Nov 13 2003 15:24:44   nrao
 * Added method to extract sales associate id from the transaction and write to the database.
 *
 *    Rev 1.1   Sep 03 2003 16:21:40   mrm
 * DB2 support
 * Resolution for POS SCR-3357: Add support needed by RSS
 *
 *    Rev 1.0   Aug 29 2003 15:33:04   CSchellenger
 * Initial revision.
 *
 *    Rev 1.3   May 10 2003 16:23:42   mpm
 * Added support for post-processing-status-code.
 *
 *    Rev 1.2   Feb 15 2003 17:26:04   mpm
 * Merged 5.1 changes.
 * Resolution for Domain SCR-104: Merge 5.1/5.5 into 6.0
 *
 *    Rev 1.1   24 Jun 2002 11:48:38   jbp
 * merge from 5.1 SCR 1726
 * Resolution for POS SCR-1726: Void - Void of new special order gets stuck in the queue in DB2
 *
 *    Rev 1.0   Jun 03 2002 16:40:28   msg
 * Initial revision.
 *
 * ===========================================================================
 */
package oracle.retail.stores.domain.arts;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import oracle.retail.stores.common.sql.SQLInsertStatement;
import oracle.retail.stores.common.sql.SQLSelectStatement;
import oracle.retail.stores.common.sql.SQLUpdateStatement;
import oracle.retail.stores.domain.customer.CustomerInfoIfc;
import oracle.retail.stores.domain.transaction.SaleReturnTransactionIfc;
import oracle.retail.stores.domain.transaction.TransactionIfc;
import oracle.retail.stores.foundation.manager.data.DataException;
import oracle.retail.stores.foundation.manager.data.JdbcDataConnection;
import oracle.retail.stores.foundation.utility.Util;
import oracle.retail.stores.persistence.utility.ARTSDatabaseIfc;

/**
 * This operation is the base operation for saving all transactions in the CRF
 * POS. It contains the method that saves to the transaction table in the
 * database.
 * 
 * @version $Revision: /rgbustores_13.4x_generic_branch/1 $
 */
@Component
public abstract class JdbcSaveTransaction extends JdbcDataOperation implements ARTSDatabaseIfc
{
    private static final long serialVersionUID = 7703441589418527112L;

    /**
        The logger to which log messages will be sent.
     */
    private static final Logger logger = Logger.getLogger(JdbcSaveTransaction.class);

    /**
       revision number supplied by source-code-control system
     */
    public static final String revisionNumber = "$Revision: /rgbustores_13.4x_generic_branch/1 $";

    /**
       Inserts into the transaction table.
       <P>
       @param  connection  connection to the db
       @param  transaction     a pos transaction
       @exception  DataException upon error
     */
    public void insertTransaction(JdbcTemplate connection, TransactionIfc transaction) throws DataException
    {
        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_TRANSACTION);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_OPERATOR_ID, getOperatorID(transaction));
        sql.addColumn(FIELD_TRANSACTION_BEGIN_DATE_TIMESTAMP, getTransactionBeginDateString(transaction));
        sql.addColumn(FIELD_TRANSACTION_END_DATE_TIMESTAMP, getTransactionEndDateString(transaction));
        sql.addColumn(FIELD_TRANSACTION_TYPE_CODE, getTransactionType(transaction));
        sql.addColumn(FIELD_TRANSACTION_TRAINING_FLAG, getTrainingFlag(transaction));
        sql.addColumn(FIELD_EMPLOYEE_ID, getSalesAssociateID(transaction));
        sql.addColumn(FIELD_CUSTOMER_INFO, getCustomerInfo(transaction));
        sql.addColumn(FIELD_CUSTOMER_INFO_TYPE, getCustomerInfoType(transaction));
        sql.addColumn(FIELD_TRANSACTION_STATUS_CODE, getTransactionStatus(transaction));
        sql.addColumn(FIELD_TENDER_REPOSITORY_ID, getTillID(transaction));
        sql.addColumn(FIELD_TRANSACTION_POST_PROCESSING_STATUS_CODE, transaction.getPostProcessingStatus());
        sql.addColumn(FIELD_RECORD_LAST_MODIFIED_TIMESTAMP, getSQLCurrentTimestampFunction());
        sql.addColumn(FIELD_RECORD_CREATION_TIMESTAMP, getSQLCurrentTimestampFunction());
        sql.addColumn(FIELD_TRANSACTION_REENTRY_FLAG, getTransReentryFlag(transaction));
        sql.addColumn(FIELD_TRANSACTION_SALES_ASSOCIATE_MODIFIED,getSalesAssociateModifiedFlag(transaction));

        try
        {
            connection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error(de.toString());
            throw de;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            throw new DataException(DataException.UNKNOWN, "insertTransaction", e);
        }

        // update transaction sequence number for register after each transaction.
        updateTransactionSequenceNumber(connection, transaction);

    }

    /**
       Updates the transaction sequence number for the workstation.  If the
       workstation does not exist, it is inserted. <P>
       @param connection connection to database
       @param transaction transaction object
       @exception DataException thrown if error occurs
     */
    protected void updateTransactionSequenceNumber(JdbcTemplate connection, TransactionIfc transaction)
        throws DataException
    {
        SQLUpdateStatement wsSQL = new SQLUpdateStatement();
        wsSQL.setTable(TABLE_WORKSTATION);
        wsSQL.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, String.valueOf(transaction.getTransactionSequenceNumber()+1));
        wsSQL.addQualifier(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        try
        {
            int cnt=connection.update(wsSQL.getSQLString());
            // if update fails to update rows, insert row
            if (cnt<= 0 || getTransactionSequenceNumber(transaction)==String.valueOf(999))
            {
                insertTransactionSequenceNumber(connection, transaction.getWorkstation().getStoreID());
            }
        }
        catch (DataException de)
        {
            logger.error("" + de + "");
            throw de;
        }
        catch (Exception e)
        {
            logger.error("" + e + "");
            throw new DataException(DataException.UNKNOWN, "update transaction sequence number", e);
        }
    }
    
    
    
    
	    /**
	    Updates the transaction sequence number for the workstation.  If the
	    workstation does not exist, it is inserted. <P>
	    @param connection connection to database
	    @param storeId transaction object
	    @exception DataException thrown if error occurs
	  */
	 protected void insertTransactionSequenceNumber(JdbcTemplate connection, String storeId)
	 {
	     SQLInsertStatement wsSQL = new SQLInsertStatement();
	     wsSQL.setTable(TABLE_WORKSTATION);
	     wsSQL.addColumn(FIELD_RETAIL_STORE_ID, makeSafeString(storeId));
	     try
	     {
	         connection.execute(wsSQL.getSQLString());
	     }
	     catch (DataException de)
	     {
	         logger.error("" + de + "");
	     }
	     catch (Exception e)
	     {
	         logger.error("update transaction sequence number "+e);
	     }
	 }

    /**
       Updates the transaction sequence number for the workstation.  If the
       workstation does not exist, it is inserted. <P>
       @param connection connection to database
       @param transaction transaction object
       @exception DataException thrown if error occurs
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public String getTransactionSequenceNumber(final JdbcTemplate connection, final String storeId)
        throws DataException
    {
    	String transactionID=null;
        SQLSelectStatement sqlSelect = new SQLSelectStatement();
        sqlSelect.setTable(TABLE_WORKSTATION);
        sqlSelect.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER);
        sqlSelect.addQualifier(FIELD_RETAIL_STORE_ID, makeSafeString(storeId));
        try
        {
        	transactionID = connection.queryForObject(sqlSelect.getSQLString(), new RowMapper<String>() 
        	{
        		public String mapRow(ResultSet rs, int arg) 
				{
        			String transID = null;
					try
					{
						transID= rs.getString(FIELD_TRANSACTION_SEQUENCE_NUMBER);
					}
					catch(Exception e)
					{
						logger.info("Unable to get Transaction Sequence Number " + e);

					}
					return transID;
				}
        		
        		
			}) ;
        }
        catch (DataException de)
        {
        	logger.info("Unable to get Transaction Sequence Number " + de);
			insertTransactionSequenceNumber(connection, storeId);
			transactionID="1";
        }
        catch (Exception e)
        {
        	logger.info("Unable to get Transaction Sequence Number " + e);
			insertTransactionSequenceNumber(connection, storeId);
			transactionID="1";
        }
        return transactionID;
    }

    /**
       This method adds default workstation values to the SQL record. <P>
       @param transaction transaction object
       @param sql sql statement object
     */
    protected void addDefaultWorkstationValues(TransactionIfc transaction, SQLInsertStatement sql)
    {
        sql.addColumn(ARTSDatabaseIfc.FIELD_WORKSTATION_CLASSIFICATION, inQuotes("RegularSales"));
    }

    /**
       Updates the transaction table.
       <P>
       @param  dataConnection  connection to the db
       @param  transaction     a pos transaction
       @exception  DataException upon error
     */
    public void updateTransaction(JdbcDataConnection dataConnection, TransactionIfc transaction) throws DataException
    {
        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_TRANSACTION);

        // Fields
        sql.addColumn(FIELD_OPERATOR_ID, getOperatorID(transaction));
        sql.addColumn(FIELD_TRANSACTION_BEGIN_DATE_TIMESTAMP, getTransactionBeginDateString(transaction));
        sql.addColumn(FIELD_TRANSACTION_END_DATE_TIMESTAMP, getTransactionEndDateString(transaction));
        sql.addColumn(FIELD_TRANSACTION_TYPE_CODE, getTransactionType(transaction));
        sql.addColumn(FIELD_TRANSACTION_TRAINING_FLAG, getTrainingFlag(transaction));
        sql.addColumn(FIELD_TRANSACTION_STATUS_CODE, getTransactionStatus(transaction));
        sql.addColumn(FIELD_RECORD_LAST_MODIFIED_TIMESTAMP, getSQLCurrentTimestampFunction());
        sql.addColumn(FIELD_TRANSACTION_SALES_ASSOCIATE_MODIFIED,getSalesAssociateModifiedFlag(transaction));

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error(de.toString());
            throw de;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            throw new DataException(DataException.UNKNOWN, "updateTransaction", e);
        }

        if (0 >= dataConnection.getUpdateCount())
        {
            throw new DataException(DataException.NO_DATA, "Update Transaction");
        }
    }

    /**
       Returns the Workstation ID
       <P>
       @param  transaction     a pos transaction
       @return  The Workstation ID
     */
    public String getWorkstationID(TransactionIfc transaction)
    {
        return ("'" + transaction.getWorkstation().getWorkstationID() + "'");
    }

    /**
       Returns the Store ID
       <P>
       @param  transaction     a pos transaction
       @return  The Store ID
     */
    public String getStoreID(TransactionIfc transaction)
    {
        return ("'" + transaction.getWorkstation().getStoreID() + "'");
    }

    /**
       Returns the Operator ID
       <P>
       @param  transaction     a pos transaction
       @return  The Operator ID
     */
    public String getOperatorID(TransactionIfc transaction)
    {
        return ("'" + transaction.getCashier().getEmployeeID() + "'");
    }

    /**
       Returns the transaction sequence number
       <P>
       @param  transaction     a pos transaction
       @return  The transaction sequence number
     */
    public String getTransactionSequenceNumber(TransactionIfc transaction)
    {
        return (String.valueOf(transaction.getTransactionSequenceNumber()));
    }

    /**
       Returns the transaction type
       <P>
       @param  transaction     a pos transaction
       @return  The transaction type
     */
    public String getTransactionType(TransactionIfc transaction)
    {
        return ("'" + transaction.getTransactionType() + "'");
    }

    /**
       Returns the string value for the business day
       <P>
       @param  transaction     a pos transaction
       @return  The business day
     */
    public String getBusinessDayString(TransactionIfc transaction)
    {
        return (dateToSQLDateString(transaction.getBusinessDay()));
    }

    /**
       Returns the string value for the transaction begin time
       <P>
       @param  transaction     a pos transaction
       @return  The transaction begin time
     */
    public String getTransactionBeginDateString(TransactionIfc transaction)
    {
        return (dateToSQLTimestampString(transaction.getTimestampBegin().dateValue()));
    }

    /**
       Returns the string value for the transaction end time
       <P>
       @param  transaction     a pos transaction
       @return  The transaction end time
     */
    public String getTransactionEndDateString(TransactionIfc transaction)
    {
        if (transaction.getTimestampEnd() == null)
        {
            // Set the end time for the transaction as current time
            transaction.setTimestampEnd();
        }
        return (dateToSQLTimestampString(transaction.getTimestampEnd().dateValue()));
    }

    /**
       Returns the transaction training flag
       <P>
       @param  transaction     a pos transaction
       @return  The transaction training flag
     */
    public String getTrainingFlag(TransactionIfc transaction)
    {
        String rc = "'0'";

        if (transaction.isTrainingMode())
        {
            rc = "'1'";
        }

        return (rc);
    }

    /**
     Returns the transaction reentry flag
     <P>
     @param  transaction     a pos transaction
     @return  The transaction reentry flag
     */
    public String getTransReentryFlag(TransactionIfc transaction)
    {
        String rc = "'0'";

        if (transaction.isReentryMode())
        {
            rc = "'1'";
        }

        return (rc);
    }

    /**
       Returns the transaction status
       <P>
       @param  transaction     a pos transaction
       @return  The transaction status
     */
    public String getTransactionStatus(TransactionIfc transaction)
    {
        return (String.valueOf(transaction.getTransactionStatus()));
    }

    /**
       Returns the till identifier. <P>
       @param  transaction     a pos transaction
       @return  String containing the till identifier
     */
    public String getTillID(TransactionIfc transaction)
    {
        String value = null;
        String tillID = transaction.getTillID();

        if (tillID == null)
        {
            value = "null";
        }
        else
        {
            value = "'" + tillID + "'";
        }
        return (value);
    }

    /**
        Returns the string value to be used in the database for the
        customer ID
        <p>
        @param  the transaction object containing the customer
        @return The gift registry value
     */
    public String getCustomerInfo(TransactionIfc transaction)
    {
        CustomerInfoIfc customer = transaction.getCustomerInfo();
        String customerInfo = null;
        if (customer != null)
        {
            if (customer.getCustomerInfo() != null)
            {
                customerInfo = customer.getCustomerInfo();
            }
        }
        return (makeSafeString(customerInfo));
    }

    /**
        Returns the string value to be used in the database for the
        customer ID
        <p>
        @param  the transaction object containing the customer
        @return The gift registry value
     */
    public String getCustomerInfoType(TransactionIfc transaction)
    {
        CustomerInfoIfc customer = transaction.getCustomerInfo();
        String type = "0";
        if (customer != null)
        {
            type = Integer.valueOf(customer.getCustomerInfoType()).toString();
        }
        return (type);
    }

    /**
        Returns the string value to be used in the database for the
        sales associate ID
        <p>
        @param  the transaction object containing the customer
        @return The sales associate ID
     */
    public String getSalesAssociateID(TransactionIfc transaction)
    {
        String value = null;
        String empID = null;
        // if sales associate ID is available, get it
        if (transaction.getSalesAssociate() != null)
        {
            empID = transaction.getSalesAssociate().getEmployeeID();
        }

        if (empID == null)
        {
            value = "null";
        }
        else
        {
            value = "'" + empID + "'";
        }
        return (value);
    }

    /**
        Returns the string value to be used in the database for the
        sales associate modified flag
        <p>
        @param  the transaction object containing the customer
        @return string
     */
    public String getSalesAssociateModifiedFlag(TransactionIfc transaction)
    {
        if (transaction instanceof SaleReturnTransactionIfc)
        {
            if (((SaleReturnTransactionIfc)transaction).getSalesAssociateModifiedFlag())
            	return "'1'";
        }
        return ("'0'");
    }

    /**
       Returns the source-code-control system revision number. <P>
       @return String representation of revision number
     */
    public String getRevisionNumber()
    {
        return (Util.parseRevisionNumber(revisionNumber));
    }
}
