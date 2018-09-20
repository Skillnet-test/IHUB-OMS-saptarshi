/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
/* ===========================================================================
* Copyright (c) 1998, 2014, Oracle and/or its affiliates. All rights reserved.
 * ===========================================================================
 * $Header: rgbustores/modules/domain/src/oracle/retail/stores/domain/arts/JdbcSaveOrderByTransaction.java /rgbustores_13.4.8_generic_branch/1 2014/06/30 01:18:05 spurkaya Exp $
 * ===========================================================================
 * NOTES
 * <other useful comments, qualifications, etc.>
 *
 * MODIFIED    (MM/DD/YY)
 *    spurkaya  06/30/14 - Add business date to the qualifiers
 *    cgreene   05/28/10 - convert to oracle packaging
 *    cgreene   05/27/10 - convert to oracle packaging
 *    cgreene   05/27/10 - convert to oracle packaging
 *    cgreene   05/26/10 - convert to oracle packaging
 *    cgreene   04/28/10 - updating deprecated names
 *    abondala  01/03/10 - update header date
 *    mahising  02/26/09 - Rework for PDO functionality
 *    jswan     02/21/09 - Merge.
 *    arathore  02/15/09 - Added null check for sale amount.
 *    mahising  01/13/09 - fix QA issue
 *    aphulamb  11/22/08 - Checking files after code review by Naga
 *    aphulamb  11/13/08 - Check in all the files for Pickup Delivery Order
 *                         functionality
 *
 * ===========================================================================
 * $Log:
 *    5    360Commerce 1.4         4/27/2006 7:26:59 PM   Brett J. Larsen CR
 *         17307 - remove inventory functionality - stage 2
 *    4    360Commerce 1.3         1/25/2006 4:11:22 PM   Brett J. Larsen merge
 *          7.1.1 changes (aka. 7.0.3 fixes) into 360Commerce view
 *    3    360Commerce 1.2         3/31/2005 4:28:44 PM   Robert Pearse
 *    2    360Commerce 1.1         3/10/2005 10:22:49 AM  Robert Pearse
 *    1    360Commerce 1.0         2/11/2005 12:12:03 PM  Robert Pearse
 *:
 *    5    .v700     1.2.1.1     11/16/2005 16:26:08    Jason L. DeLeau 4215:
 *         Get rid of redundant ArtsDatabaseifc class
 *    4    .v700     1.2.1.0     11/7/2005 15:09:27     Deepanshu       CR
 *         5273: Persist the training flag as String instead of int
 *    3    360Commerce1.2         3/31/2005 15:28:44     Robert Pearse
 *    2    360Commerce1.1         3/10/2005 10:22:49     Robert Pearse
 *    1    360Commerce1.0         2/11/2005 12:12:03     Robert Pearse
 *
 *   Revision 1.8  2004/08/17 23:19:44  crain
 *   @scr 6843 Order table has DC_DY_BSN_CHG with zero time component
 *
 *   Revision 1.7  2004/06/29 21:58:58  aachinfiev
 *   Merge the changes for inventory & POS integration
 *
 *   Revision 1.6  2004/04/09 16:55:46  cdb
 *   @scr 4302 Removed double semicolon warnings.
 *
 *   Revision 1.5  2004/02/17 17:57:37  bwf
 *   @scr 0 Organize imports.
 *
 *   Revision 1.4  2004/02/17 16:18:46  rhafernik
 *   @scr 0 log4j conversion
 *
 *   Revision 1.3  2004/02/12 17:13:18  mcs
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
 *    Rev 1.0   Aug 29 2003 15:32:52   CSchellenger
 * Initial revision.
 *
 *    Rev 1.1   Jun 10 2002 11:14:56   epd
 * Merged in changes for Oracle
 * Resolution for Domain SCR-83: Merging database fixes into base code
 *
 *    Rev 1.3   Jun 07 2002 17:47:42   epd
 * Merging in fixes made for McDonald's Oracle demo
 * Resolution for Domain SCR-83: Merging database fixes into base code
 *
 *    Rev 1.2   16 May 2002 22:02:34   vxs
 * Db2 port fixes, quotation issues with Strings/integers.
 *
 *    Rev 1.1   Mar 18 2002 22:48:30   msg
 * - updated copyright
 *
 *    Rev 1.0   Mar 18 2002 12:08:20   msg
 * Initial revision.
 *
 *    Rev 1.9   08 Feb 2002 18:49:26   cir
 * Removed FIELD_ORDER_TOTAL from update; added it to insert only
 * Resolution for POS SCR-260: Special Order feature for release 5.0
 *
 *    Rev 1.8   Feb 08 2002 17:04:44   dfh
 * updates to better order the orders from oldest on top to newest / updated on bottom
 * Resolution for POS SCR-1170: Order List not sorted correctly when multiple statuses are listed
 *
 *    Rev 1.7   Jan 24 2002 16:44:16   dfh
 * updates to support order item location, default value is NA
 * Resolution for POS SCR-260: Special Order feature for release 5.0
 *
 *    Rev 1.6   Jan 20 2002 20:59:18   dfh
 * use now* for order modfied timestamp
 * Resolution for POS SCR-260: Special Order feature for release 5.0
 *
 *    Rev 1.5   Dec 20 2001 17:25:10   dfh
 * a little closer to crossreach
 * Resolution for POS SCR-260: Special Order feature for release 5.0
 *
 *    Rev 1.4   03 Dec 2001 19:18:34   mpm
 * Added database support for minimum deposit amount, balance due.
 * Resolution for POS SCR-260: Special Order feature for release 5.0
 *
 *    Rev 1.3   30 Nov 2001 07:53:50   mpm
 * Completed work on order summary entry searches.
 * Resolution for POS SCR-260: Special Order feature for release 5.0
 *
 *    Rev 1.2   29 Nov 2001 16:14:08   mpm
 * Corrected default-gift-registry problem.
 * Resolution for POS SCR-260: Special Order feature for release 5.0
 *
 *    Rev 1.1   29 Nov 2001 07:05:32   mpm
 * Continuing order modifications.
 * Resolution for POS SCR-260: Special Order feature for release 5.0
 *
 *    Rev 1.0   27 Nov 2001 06:25:40   mpm
 * Initial revision.
 * Resolution for POS SCR-260: Special Order feature for release 5.0
 * ===========================================================================
 */
package oracle.retail.stores.domain.arts;
// java imports
// bedrock imports
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import oracle.retail.stores.commerceservices.ixretail.schematypes.Currency;
import oracle.retail.stores.common.sql.SQLInsertStatement;
import oracle.retail.stores.common.sql.SQLUpdatableStatementIfc;
import oracle.retail.stores.common.sql.SQLUpdateStatement;
import oracle.retail.stores.common.utility.LocaleMap;
import oracle.retail.stores.domain.customer.CustomerIfc;
import oracle.retail.stores.domain.lineitem.AbstractTransactionLineItemIfc;
import oracle.retail.stores.domain.order.OrderStatusIfc;
import oracle.retail.stores.domain.transaction.OrderTransactionIfc;
import oracle.retail.stores.domain.transaction.TransactionIfc;
import oracle.retail.stores.domain.utility.EYSStatusIfc;
import oracle.retail.stores.foundation.manager.data.DataException;
import oracle.retail.stores.foundation.manager.ifc.data.DataActionIfc;
import oracle.retail.stores.foundation.manager.ifc.data.DataConnectionIfc;
import oracle.retail.stores.foundation.manager.ifc.data.DataTransactionIfc;
import oracle.retail.stores.foundation.utility.Util;
//import oracle.retail.stores.foundation.utility.Util;
import oracle.retail.stores.persistence.utility.ARTSDatabaseIfc;

//--------------------------------------------------------------------------
/**
    This operation creates an order in the order/transaction database. <P>
    Note that this data operation includes columns used only in web-channel
    orders as well as those required for special order. <P>
    @version $Revision: /rgbustores_13.4.8_generic_branch/1 $
**/
//--------------------------------------------------------------------------
public class JdbcSaveOrderByTransaction
extends JdbcDataOperation
{
    /** UID */
    private static final long serialVersionUID = 6468080965638636678L;
    /**
        The logger to which log messages will be sent.
    **/
    private static Logger logger = Logger.getLogger(oracle.retail.stores.domain.arts.JdbcSaveOrderByTransaction.class);

    
	@Override
	public void execute(DataTransactionIfc paramDataTransactionIfc, DataConnectionIfc paramDataConnectionIfc,
			DataActionIfc paramDataActionIfc) throws DataException {
		// TODO Auto-generated method stub
		
	}
    
    /**
     * Execute the SQL statements against the database.
     *
     * @param dataTransaction The data transaction
     * @param dataConnection The connection to the data source
     * @param action The information passed by the valet
     * @exception DataException upon error
     */

    public void execute(OrderTransactionIfc transaction, JdbcTemplate connection)
            throws DataException
    {
        if (logger.isDebugEnabled())
            logger.debug("JdbcSaveRetailTransactionLineItems.execute()");

 
        insertOrder(connection, transaction);

        if (logger.isDebugEnabled())
            logger.debug("JdbcSaveRetailTransactionLineItems.execute()");
    }
    
    
    //---------------------------------------------------------------------
    /**
       Inserts into the order table.
       <P>
       @param  transaction  the connection to the data source
       @param  connection  The order to create in the database
       @exception DataException
    **/
    //---------------------------------------------------------------------
    protected void insertOrder(JdbcTemplate connection,OrderTransactionIfc transaction)
        throws DataException
    {
        // get an sql object
        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(ARTSDatabaseIfc.TABLE_ORDER);

        // add insert columns
        addInsertColumns(sql,
        		transaction);

        try
        {
        	connection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( "" + de + "");
            throw de;
        }
        catch (Exception e)
        {
            logger.error( "" + Util.throwableToString(e) + "");
            throw new DataException(DataException.UNKNOWN, "insertOrder", e);
        }
    }

    //---------------------------------------------------------------------
    /**
       Updates the order table.
       <P>
       @param  dataConnection  the connection to the data source
       @param  orderTransaction  The order to create in the database
       @exception DataException
    **/
    //---------------------------------------------------------------------
    protected void updateOrder(JdbcTemplate dataConnection,OrderTransactionIfc orderTransaction)
        throws DataException
    {
        // get an sql object
        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(ARTSDatabaseIfc.TABLE_ORDER);

        // add update columns
        addUpdateColumns(sql,
                         orderTransaction);

        // add update qualifier
        addUpdateQualifiers(sql,
        		orderTransaction);

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( "" + de + "");
            throw de;
        }
        catch (Exception e)
        {
            logger.error( "" + Util.throwableToString(e) + "");
            throw new DataException(DataException.UNKNOWN, "updateOrder", e);
        }
    }

    //---------------------------------------------------------------------
    /**
       Adds insert columns to the SQL statement. <P>
       @param  sql sql statement
       @param  orderTransaction  The order to create in the database
    **/
    //---------------------------------------------------------------------
    protected void addInsertColumns(SQLUpdatableStatementIfc sql,
                                 OrderTransactionIfc orderTransaction)
    {
        // add insert columns
        addUpdateColumns(sql,
                         orderTransaction);

        // add field for order ID
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_ID,
                      inQuotes(orderTransaction.getOrderID()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_TOTAL, orderTransaction.getTransactionTotals().
                        getGrandTotal().toString());
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_LOCATION,
                      inQuotes(orderTransaction.getOrderStatus().getLocation()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_RECORD_CREATION_TIMESTAMP,
                      getSQLCurrentTimestampFunction());
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_CREATE_TIMESTAMP,
                      dateToSQLTimestampFunction
                        (orderTransaction.getOrderStatus().getTimestampCreated()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_BEGIN_TIMESTAMP,
                      dateToSQLTimestampFunction
                        (orderTransaction.getOrderStatus().getTimestampBegin()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_TYPE, orderTransaction.getOrderType());
        
        String saleAmount = Currency.ZERO.toString();
        //In case of suspend transaction SaleAmount Value is null.
        if(orderTransaction.getTransactionStatus() != TransactionIfc.STATUS_SUSPENDED)
        {
        	saleAmount = orderTransaction.getOrderStatus().getSaleAmount().toString();
        }
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_SALE_AMOUNT, saleAmount);
    }

    //---------------------------------------------------------------------
    /**
       Adds update columns to the SQL statement. <P>
       @param  sql sql statement
       @param  orderTransaction  The order to create in the database
    **/
    //---------------------------------------------------------------------
    protected void addUpdateColumns(SQLUpdatableStatementIfc sql,
                                 OrderTransactionIfc orderTransaction)
    {
        OrderStatusIfc order = orderTransaction.getOrderStatus();
        EYSStatusIfc status = order.getStatus();
        CustomerIfc customer = orderTransaction.getCustomer();

        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_STATUS,
                      Integer.toString(status.getStatus()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_CUSTOMER_ID,
                      inQuotes(customer.getCustomerID()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_BUSINESS_DAY_DATE,
                      dateToSQLDateString
                        (orderTransaction.getBusinessDay()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_STATUS_PREVIOUS,
                      Integer.toString(status.getPreviousStatus()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_RETAIL_STORE_ID,
                      inQuotes
                        (orderTransaction.getWorkstation().getStoreID()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_STATUS_CHANGE,
                        dateToSQLDateString
                        (status.getLastStatusChange()));
        if (orderTransaction.getItemContainerProxy().
             getDefaultRegistry() != null)
        {
            sql.addColumn(ARTSDatabaseIfc.FIELD_GIFT_REGISTRY_ID,
                          inQuotes
                            (orderTransaction.getItemContainerProxy().
                              getDefaultRegistry().getID()));
        }
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_BEGIN,
                      dateToSQLDateString
                        (order.getTimestampBegin()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_OPERATOR_ID,
                      inQuotes
                        (orderTransaction.getCashier().getEmployeeID()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_CONTACT_FIRST_NAME,
                      makeSafeString
                        (customer.getFirstName()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_CONTACT_MIDDLE_INITIAL,
                      makeSafeString
                        (customer.getMiddleName()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_CONTACT_LAST_NAME,
                      makeSafeString
                        (customer.getLastName()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_EMAIL_ADDRESS,
                      inQuotes
                        (customer.getEMailAddress()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_DEPOSIT_AMOUNT,
                      order.getDepositAmount().toString());
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_INITIATION_CHANNEL,
                      inQuotes(order.getInitiatingChannel()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_MINIMUM_DEPOSIT_AMOUNT,
                      order.getMinimumDepositAmount().toString());
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_BALANCE_DUE,
                      order.getBalanceDue().toString());
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_ORIGINAL_STORE_ID,
                      inQuotes(order.getInitialTransactionID().getStoreID()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_ORIGINAL_WORKSTATION_ID,
                      inQuotes(order.getInitialTransactionID().getWorkstationID()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_ORIGINAL_TRANSACTION_BUSINESS_DATE,
                      dateToSQLDateString
                        (order.getInitialTransactionBusinessDate()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_ORIGINAL_TRANSACTION_SEQUENCE_NUMBER,
                      Long.toString
                        (order.getInitialTransactionID().getSequenceNumber()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_RECORDING_STORE_ID,
                      inQuotes(order.getRecordingTransactionID().getStoreID()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_RECORDING_WORKSTATION_ID,
                      inQuotes(order.getRecordingTransactionID().getWorkstationID()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_RECORDING_TRANSACTION_BUSINESS_DATE,
                      emptyStringToSpaceString(dateToSQLDateString
                        (order.getRecordingTransactionBusinessDate())));
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_RECORDING_TRANSACTION_SEQUENCE_NUMBER,
                      Long.toString
                        (order.getRecordingTransactionID().getSequenceNumber()));
        sql.addColumn(ARTSDatabaseIfc.FIELD_RECORD_LAST_MODIFIED_TIMESTAMP,
                      getSQLCurrentTimestampFunction());
        sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_MODIFIED_TIMESTAMP,
                      getSQLCurrentTimestampFunction());

        String trainingMode = "0";
        if (orderTransaction.isTrainingMode())
        {
            trainingMode = "1";
        }
        sql.addColumn(ARTSDatabaseIfc.FIELD_TRANSACTION_TRAINING_FLAG,
                      inQuotes(trainingMode));
        // set description column
        setDescriptionColumn(sql, orderTransaction);

    }

    //---------------------------------------------------------------------
    /**
        Adds update qualifier columns to SQL statement. <P>
        @param sql SQLUpdateStatement
        @param orderTransaction order transaction object
    **/
    //---------------------------------------------------------------------
    protected void addUpdateQualifiers(SQLUpdateStatement sql,
                                    OrderTransactionIfc orderTransaction)
    {                                   // begin addUpdateQualifiers()
        sql.addQualifier(ARTSDatabaseIfc.FIELD_ORDER_ID,
                         inQuotes(orderTransaction.getOrderID()));
        sql.addQualifier(ARTSDatabaseIfc.FIELD_BUSINESS_DAY_DATE,
                dateToSQLDateString(orderTransaction.getBusinessDay()));
    }                                   // end addUpdateQualifiers()

    //---------------------------------------------------------------------
    /**
       Sets description from first line item, if available, into SQL statement.
       This is transactional data
       @param sql SQLUpdatableStatementIfc object
       @param orderTransaction order transaction object
    **/
    //---------------------------------------------------------------------
    protected void setDescriptionColumn(SQLUpdatableStatementIfc sql,
                                        OrderTransactionIfc orderTransaction)
    {                                   // begin setDescriptionColumn()
        String description = null;
        AbstractTransactionLineItemIfc[] lineItems =
          orderTransaction.getLineItems();
        if (lineItems.length > 0)
        {
            description = lineItems[0].getItemDescription(LocaleMap.getLocale(LocaleMap.DEFAULT));
        }

        if (!Util.isEmpty(description))
        {
            sql.addColumn(ARTSDatabaseIfc.FIELD_ORDER_DESCRIPTION,
                          makeSafeString(description));
        }

    }                                   // end setDescriptionColumn()




}
