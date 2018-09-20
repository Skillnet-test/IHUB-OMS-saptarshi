/* ===========================================================================
* Copyright (c) 2009, 2012, Oracle and/or its affiliates. All rights reserved. 
 * ===========================================================================
 * $Header: rgbustores/modules/domain/src/oracle/retail/stores/domain/arts/JdbcRetrieveTransactionIDsByBatchID.java /rgbustores_13.4x_generic_branch/2 2012/09/26 15:53:25 cgreene Exp $
 * ===========================================================================
 * NOTES
 * <other useful comments, qualifications, etc.>
 *
 * MODIFIED    (MM/DD/YY)
 *    cgreene   09/24/12 - Implement maximum customer record retrieval for dtm
 *                         export
 *    cgreene   05/27/10 - convert to oracle packaging
 *    cgreene   05/27/10 - convert to oracle packaging
 *    cgreene   05/26/10 - convert to oracle packaging
 *    abondala  01/03/10 - update header date
 *
 * ===========================================================================
 * $Log:
 *    9    360Commerce 1.8         6/27/2007 9:26:08 AM   Jack G. Swan
 *         Reduced logging for Daemon technicians when a transaction is not
 *         found.
 *         Moved the log file to the \pos\logs directory and renamed the file
 *         to orpos.log rather than client.log.
 *    8    360Commerce 1.7         5/1/2007 9:45:27 AM    Jack G. Swan
 *         Changes for merge to Trunk.
 *    7    360Commerce 1.6         11/9/2006 7:28:30 PM   Jack G. Swan
 *         Modifided for XML Data Replication and CTR.
 *    6    360Commerce 1.5         8/7/2006 12:21:23 PM   Christian Greene
 *         Updated Javadoc on parseSelectTransactionIDsResultSet method to
 *         reflect that a DataException would occur if the ResultSet passed to
 *          it was empty.
 *    5    360Commerce 1.4         4/24/2006 5:51:32 PM   Charles D. Baker
 *         Merge of NEP62
 *    4    360Commerce 1.3         1/25/2006 4:11:20 PM   Brett J. Larsen merge
 *          7.1.1 changes (aka. 7.0.3 fixes) into 360Commerce view
 *    3    360Commerce 1.2         3/31/2005 4:28:43 PM   Robert Pearse
 *    2    360Commerce 1.1         3/10/2005 10:22:47 AM  Robert Pearse
 *    1    360Commerce 1.0         2/11/2005 12:12:01 PM  Robert Pearse
 *:
 *    4    .v700     1.2.2.0     11/16/2005 16:28:06    Jason L. DeLeau 4215:
 *         Get rid of redundant ArtsDatabaseifc class
 *    3    360Commerce1.2         3/31/2005 15:28:43     Robert Pearse
 *    2    360Commerce1.1         3/10/2005 10:22:47     Robert Pearse
 *    1    360Commerce1.0         2/11/2005 12:12:01     Robert Pearse
 *
 *   Revision 1.7  2004/07/14 01:26:46  kmcbride
 *   @scr 3992: Adding transaction date to the transaction id object.  Also, fixed code in TransactionID that was eating an IllegalArgumentException without logging
 *
 *   Revision 1.6  2004/04/09 16:55:47  cdb
 *   @scr 4302 Removed double semicolon warnings.
 *
 *   Revision 1.5  2004/02/17 17:57:39  bwf
 *   @scr 0 Organize imports.
 *
 *   Revision 1.4  2004/02/17 16:18:50  rhafernik
 *   @scr 0 log4j conversion
 *
 *   Revision 1.3  2004/02/12 17:13:18  mcs
 *   Forcing head revision
 *
 *   Revision 1.2  2004/02/11 23:25:27  bwf
 *   @scr 0 Organize imports.
 *
 *   Revision 1.1.1.1  2004/02/11 01:04:28  cschellenger
 *   updating to pvcs 360store-current
 *
 *
 *
 *    Rev 1.0   Aug 29 2003 15:32:36   CSchellenger
 * Initial revision.
 *
 *    Rev 1.2   May 10 2003 16:23:16   mpm
 * Added support for post-processing-status-code.
 *
 *    Rev 1.1   Jan 22 2003 15:05:04   mpm
 * Preliminary merge of 5.1/5.5 code.
 * Resolution for Domain SCR-104: Merge 5.1/5.5 into 6.0
 *
 *    Rev 1.3   Jul 02 2002 13:26:40   vpn-mpm
 * Changed batch identifier to a string.
 *
 *    Rev 1.2   Apr 11 2002 09:18:56   mpm
 * Migrated oracle/retail/stores/domain/translation/ixretail to oracle/retail/stores/domain/ixretail
 * Resolution for Domain SCR-45: TLog facility
 *
 *    Rev 1.1   Apr 08 2002 17:15:18   mpm
 * Added additional support for translation.
 * Resolution for Domain SCR-45: TLog facility
 *
 *    Rev 1.0   Apr 02 2002 15:50:30   mpm
 * Initial revision.
 * Resolution for Domain SCR-45: TLog facility
 * ===========================================================================
 */
package oracle.retail.stores.domain.arts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import oracle.retail.stores.common.data.JdbcUtilities;
import oracle.retail.stores.common.sql.SQLSelectStatement;
import oracle.retail.stores.domain.DomainGateway;
import oracle.retail.stores.domain.ixretail.log.POSLogTransactionEntryIfc;
import oracle.retail.stores.domain.transaction.TransactionConstantsIfc;
import oracle.retail.stores.domain.transaction.TransactionIfc;
import oracle.retail.stores.domain.utility.EYSDate;
import oracle.retail.stores.foundation.manager.data.DataException;
import oracle.retail.stores.foundation.manager.data.JdbcDataConnection;
import oracle.retail.stores.foundation.manager.ifc.data.DataActionIfc;
import oracle.retail.stores.foundation.manager.ifc.data.DataConnectionIfc;
import oracle.retail.stores.foundation.manager.ifc.data.DataTransactionIfc;
import oracle.retail.stores.foundation.utility.Util;
import oracle.retail.stores.persistence.utility.ARTSDatabaseIfc;

/**
 * This operation reads a list of transaction IDs and business dates from a
 * database.
 * 
 * @version $Revision: /rgbustores_13.4x_generic_branch/2 $
 */
public class JdbcRetrieveTransactionIDsByBatchID
extends JdbcDataOperation
{
    /**  */
    private static final long serialVersionUID = -2916486628821426837L;
    /**
     * The logger to which log messages will be sent.
     */
    private static final Logger logger = Logger.getLogger(JdbcRetrieveTransactionIDsByBatchID.class);

    /**
     * Class constructor.
     *
     */
    public JdbcRetrieveTransactionIDsByBatchID()
    {
        super();
        setName("JdbcRetrieveTransactionIDsByBatchID");
    }
    
    /**
     * Executes the SQL statements against the database.
     * @param  dataTransaction     The data transaction
     * @param  dataConnection      The connection to the data source
     * @param  action              The information passed by the valet
     * @exception DataException upon error
     */
    public void execute(DataTransactionIfc dataTransaction,
                        DataConnectionIfc dataConnection,
                        DataActionIfc action)
                        throws DataException
    {
        if (logger.isDebugEnabled()) logger.debug(
                     "JdbcRetrieveTransactionIDsByBatchID.execute");

        POSLogTransactionEntryIfc[] entries =
          selectTransactionIDs
            ((JdbcDataConnection) dataConnection,
             (POSLogTransactionEntryIfc) action.getDataObject());

        dataTransaction.setResult(entries);

        if (logger.isDebugEnabled()) logger.debug(
                    "JdbcRetrieveTransactionIDsByBatchID.execute");
    }
    

    /**
     * Executes the SQL statements against the database.
     * @param  dataTransaction     The data transaction
     * @param  jdbcTemplate      The connection to the data source
     * @param  action              The information passed by the valet
     * @exception DataException upon error
     */
    public void execute(DataTransactionIfc dataTransaction,
                        JdbcTemplate jdbcTemplate,
                        DataActionIfc action)
                        throws DataException
    {
        if (logger.isDebugEnabled()) logger.debug(
                     "JdbcRetrieveTransactionIDsByBatchID.execute");

        POSLogTransactionEntryIfc[] entries =
          selectTransactionIDs
            (jdbcTemplate,
             (POSLogTransactionEntryIfc) action.getDataObject());

        dataTransaction.setResult(entries);

        if (logger.isDebugEnabled()) logger.debug(
                    "JdbcRetrieveTransactionIDsByBatchID.execute");
    }
    
    
    
    /**
     * Selects transaction IDs matching input criteria.
     * @param dataConnection JDBC data connection
     * @param tLogEntry transaction TLog entry object used as search criteria
     * @return array of transaction IDs matching input criteria
     * @throws DataException thrown if error occurs
     */
    protected POSLogTransactionEntryIfc[] selectTransactionIDs(JdbcDataConnection dataConnection,
                                                             POSLogTransactionEntryIfc tLogEntry)
        throws DataException
    {
        // build sql statement
        SQLSelectStatement sql = buildSelectTransactionIDsStatement(tLogEntry);
        POSLogTransactionEntryIfc[] entries = null;
        // execute select statement and parse result set
        try
        {
            String sqlString = sql.getSQLString();
            int maxRows = tLogEntry.getMaximumTransactionsToExport();
            if (maxRows > 0)
            {
                StringBuilder sqlMaxString = new StringBuilder(100);
                sqlMaxString.append(" SELECT ");
                sqlMaxString.append(JdbcUtilities.numberLimitingSelectPart(maxRows));
                sqlMaxString.append(" * FROM (");
                sqlMaxString.append(sqlString);
                sqlMaxString.append(")" );
                sqlMaxString.append(JdbcUtilities.numberLimitingWhereClauseEndPart(maxRows));
                sqlMaxString.append(JdbcUtilities.numberLimitingEndPart(maxRows));
                sqlString = sqlMaxString.toString();
            }

            // execute sql and get result set
            dataConnection.execute(sqlString);
            ResultSet rs = (ResultSet) dataConnection.getResult();
            // parse result set
            entries = parseSelectTransactionIDsResultSet(rs);
        }
        catch (DataException de)
        {
            // The NO_DATA condition is very common here; if the Daemon calling
            // this class has a short sleep interval, it will flood the
            // log with messages.  Setting this to info gives the system
            // implementator the option of removing it from log using
            // the log4j filter.
            if (de.getErrorCode() == DataException.NO_DATA)
            {
                logger.info("No transactions found by batch id.");
            }
            else
            {
                logger.warn(de.toString());
            }
            throw de;
        }
        catch (SQLException se)
        {
            logger.error(
                         Util.throwableToString(se));
            dataConnection.logSQLException(se, "select transaction IDs");
            throw new DataException(DataException.SQL_ERROR, "select transaction IDs", se);
        }
        catch (Exception e)
        {
            logger.error(
                         Util.throwableToString(e));
            throw new DataException(DataException.UNKNOWN, "transaction table", e);
        }

        return(entries);
    }
    

    /**
     * Selects transaction IDs matching input criteria.
     * @param jdbcTemplate JDBC data connection
     * @param tLogEntry transaction TLog entry object used as search criteria
     * @return array of transaction IDs matching input criteria
     * @throws DataException thrown if error occurs
     */
    protected POSLogTransactionEntryIfc[] selectTransactionIDs(JdbcTemplate jdbcTemplate,
                                                             POSLogTransactionEntryIfc tLogEntry)
        throws DataException
    {
        // build sql statement
        SQLSelectStatement sql = buildSelectTransactionIDsStatement(tLogEntry);
        POSLogTransactionEntryIfc[] entries = null;
        // execute select statement and parse result set
        try
        {
        	String sqlString = sql.getSQLString();
        	int maxRows = tLogEntry.getMaximumTransactionsToExport();
            if (maxRows > 0)
            {
            	StringBuilder sqlMaxString = new StringBuilder(100);
            	sqlMaxString.append(" SELECT ");
            	sqlMaxString.append(JdbcUtilities.numberLimitingSelectPart(maxRows));
            	sqlMaxString.append(" * FROM (");
            	sqlMaxString.append(sqlString);
            	sqlMaxString.append(")" );
                sqlMaxString.append(JdbcUtilities.numberLimitingWhereClauseEndPart(maxRows));
                sqlMaxString.append(JdbcUtilities.numberLimitingEndPart(maxRows));
                sqlString = sqlMaxString.toString();
            }

            // execute sql and get result set
            
            List<Map<String,Object>> rs=jdbcTemplate.queryForList(sqlString);
            // parse result set
            entries = parseSelectTransactionIDsResultSet(rs);
        }
        catch (DataException de)
        {
            // The NO_DATA condition is very common here; if the Daemon calling
            // this class has a short sleep interval, it will flood the
            // log with messages.  Setting this to info gives the system
            // implementator the option of removing it from log using
            // the log4j filter.
            if (de.getErrorCode() == DataException.NO_DATA)
            {
                logger.info("No transactions found by batch id.");
            }
            else
            {
                logger.warn(de.toString());
            }
            throw de;
        }
        catch (SQLException se)
        {
            logger.error(
                         Util.throwableToString(se));
            //jdbcTemplate.logSQLException(se, "select transaction IDs");
            throw new DataException(DataException.SQL_ERROR, "select transaction IDs", se);
        }
        catch (Exception e)
        {
            logger.error(
                         Util.throwableToString(e));
            throw new DataException(DataException.UNKNOWN, "transaction table", e);
        }

        return(entries);
    }

    /**
     * Builds select statement for retrieving transaction IDs.
     * @param tLogEntry transaction TLog entry object used as search criteria
     * @return SQLSelectStatement to be used for retrieving transaction IDs.
     */
    protected SQLSelectStatement buildSelectTransactionIDsStatement(POSLogTransactionEntryIfc tLogEntry)
    {
        SQLSelectStatement sql = new SQLSelectStatement();

        // set table
        sql.addTable(ARTSDatabaseIfc.TABLE_TRANSACTION);

        // add columns
        addSelectTransactionIDsColumns(sql, tLogEntry.getColumnID());

        // add qualifiers
        addSelectTransactionIDsQualifiers(sql, tLogEntry);

        // add ordering
        addSelectTransactionIDsOrdering(sql);

        if (logger.isDebugEnabled())
        {
            String strSQL = "";
            try
            {
                strSQL = sql.getSQLString();
            }
            catch (Exception e)
            {
                strSQL = "Exception Message: " + e.getLocalizedMessage();
            }
            logger.debug("buildSelectTransactionIDsStatement - SQL for Transaction ID retrieval: " + strSQL);
        }
        return sql;
    }

    /**
     * Add columns to SQL statement for selecting transaction IDs.
     * 
     * @param sql SQLSelectStatement under construction
     */
    protected void addSelectTransactionIDsColumns(SQLSelectStatement sql)
    {
        addSelectTransactionIDsColumns(sql, POSLogTransactionEntryIfc.USE_BATCH_ARCHIVE);
    }

    /**
     * Add columns to SQL statement for selecting transaction IDs.
     * 
     * @param sql SQLSelectStatement under construction
     * @param columnID indcates if the tlog or batch archive column will be used
     */
    protected void addSelectTransactionIDsColumns(SQLSelectStatement sql, int columnID)
    {
        sql.addColumn(ARTSDatabaseIfc.FIELD_RETAIL_STORE_ID);
        sql.addColumn(ARTSDatabaseIfc.FIELD_WORKSTATION_ID);
        sql.addColumn(ARTSDatabaseIfc.FIELD_BUSINESS_DAY_DATE);
        sql.addColumn(ARTSDatabaseIfc.FIELD_TRANSACTION_SEQUENCE_NUMBER);
        if (columnID == POSLogTransactionEntryIfc.USE_TLOG_ID)
        {
            sql.addColumn(ARTSDatabaseIfc.FIELD_TRANSACTION_TLOG_BATCH_IDENTIFIER);
        }
        else
        if (columnID == POSLogTransactionEntryIfc.USE_RTLOG_ID)
        {
            sql.addColumn(ARTSDatabaseIfc.FIELD_TRANSACTION_RTLOG_BATCH_IDENTIFIER);
        }
        else
        if (columnID == POSLogTransactionEntryIfc.USE_INVENTORY_RESERVATION_ID)
        {
            sql.addColumn(ARTSDatabaseIfc.FIELD_TRANSACTION_INVENTORY_RESERVATION_BATCH_IDENTIFIER);
        }
        else
        {
        	sql.addColumn(ARTSDatabaseIfc.FIELD_TRANSACTION_ARCHIVE_BATCH_IDENTIFIER);
        }
        sql.addColumn(ARTSDatabaseIfc.FIELD_TRANSACTION_END_DATE_TIMESTAMP);
    }

    /**
     * Adds qualifiers for SQL statement to be used for selecting transaction
     * IDs.
     * 
     * @param sql SQLSelectStatement under construction
     * @param tLogEntry TLog entry object to be used as key
     */
    protected void addSelectTransactionIDsQualifiers(SQLSelectStatement sql,
                                                     POSLogTransactionEntryIfc tLogEntry)
    {
        // add store ID qualifier, if necessary
        if (!Util.isEmpty(tLogEntry.getStoreID()))
        {
            sql.addQualifier(ARTSDatabaseIfc.FIELD_RETAIL_STORE_ID,
                             inQuotes(tLogEntry.getStoreID()));
        }

        // add business date qualifier, if necessary
        if (tLogEntry.getBusinessDate() != null)
        {
            sql.addQualifier(ARTSDatabaseIfc.FIELD_BUSINESS_DAY_DATE,
                             dateToSQLDateString(tLogEntry.getBusinessDate()));
        }

        // If the start time is not null, then the add a qualifier that searhes
        // for transactions which have a transaction end time which equal to or
        // or greater than the start time.
        if (tLogEntry.getStartTime() != null)
        {
            sql.addQualifier(ARTSDatabaseIfc.FIELD_TRANSACTION_END_DATE_TIMESTAMP + " >= "
                    + dateToSQLTimestampString(tLogEntry.getStartTime().dateValue()));
        }

        // If the end time is not null, then the add a qualifier that searhes
        // for transactions which have a transaction end time which equal to or
        // or less than the end time.
        if (tLogEntry.getEndTime() != null)
        {
            sql.addQualifier(ARTSDatabaseIfc.FIELD_TRANSACTION_END_DATE_TIMESTAMP + " <= "
                    + dateToSQLTimestampString(tLogEntry.getEndTime().dateValue()));
        }

        if (tLogEntry.getColumnID() == POSLogTransactionEntryIfc.USE_TLOG_ID)
        {
	        // add batch ID for DataReplication
	        sql.addQualifier(ARTSDatabaseIfc.FIELD_TRANSACTION_TLOG_BATCH_IDENTIFIER,
	                         tLogEntry.getBatchID());
        }
        else
        if (tLogEntry.getColumnID() == POSLogTransactionEntryIfc.USE_RTLOG_ID)
        {
            // add batch ID for RTLog
            sql.addQualifier(ARTSDatabaseIfc.FIELD_TRANSACTION_RTLOG_BATCH_IDENTIFIER,
                            inQuotes(tLogEntry.getBatchID()));
        }
        else if (tLogEntry.getColumnID() == POSLogTransactionEntryIfc.USE_INVENTORY_RESERVATION_ID)
        {
            sql.addQualifier(ARTSDatabaseIfc.FIELD_TRANSACTION_INVENTORY_RESERVATION_BATCH_IDENTIFIER,
                             inQuotes(tLogEntry.getBatchID()));

            // add qualifier to filter order type transactions
            StringBuilder qualifier = new StringBuilder();

            // add qualifier to retrieve only completed and post voided transactions
            StringBuilder statusQualifier = new StringBuilder();

            // Filter out training mode transactions
            String trainingMode = "0";
            sql.addQualifier(ARTSDatabaseIfc.FIELD_TRANSACTION_TRAINING_FLAG , inQuotes(trainingMode));

            // Retrieve completed and voided transactions for inventory reservation
            statusQualifier = statusQualifier.append("(")
            .append(ARTSDatabaseIfc.FIELD_TRANSACTION_STATUS_CODE + " = " + String.valueOf(TransactionIfc.STATUS_COMPLETED))
            .append(" OR ")
            .append(ARTSDatabaseIfc.FIELD_TRANSACTION_STATUS_CODE + " = " + String.valueOf(TransactionIfc.STATUS_VOIDED))
            .append(")");

            sql.addQualifier(statusQualifier.toString());

            qualifier.append("(")
            .append(ARTSDatabaseIfc.FIELD_TRANSACTION_TYPE_CODE + " = " + inQuotes(TransactionConstantsIfc.TYPE_VOID))
            .append(" OR ")
            .append(ARTSDatabaseIfc.FIELD_TRANSACTION_TYPE_CODE + " = " + inQuotes(TransactionConstantsIfc.TYPE_LAYAWAY_INITIATE))
            .append(" OR ")
            .append(ARTSDatabaseIfc.FIELD_TRANSACTION_TYPE_CODE + " = " + inQuotes(TransactionConstantsIfc.TYPE_LAYAWAY_DELETE))
            .append(" OR ")
            .append(ARTSDatabaseIfc.FIELD_TRANSACTION_TYPE_CODE + " = " + inQuotes(TransactionConstantsIfc.TYPE_LAYAWAY_COMPLETE))
            .append(" OR ")
            .append(ARTSDatabaseIfc.FIELD_TRANSACTION_TYPE_CODE + " = " + inQuotes(TransactionConstantsIfc.TYPE_ORDER_INITIATE))
            .append(" OR ")
            .append(ARTSDatabaseIfc.FIELD_TRANSACTION_TYPE_CODE + " = " + inQuotes(TransactionConstantsIfc.TYPE_ORDER_PARTIAL))
            .append(" OR ")
            .append(ARTSDatabaseIfc.FIELD_TRANSACTION_TYPE_CODE + " = " + inQuotes(TransactionConstantsIfc.TYPE_ORDER_COMPLETE))
            .append(" OR ")
            .append(ARTSDatabaseIfc.FIELD_TRANSACTION_TYPE_CODE + " = " + inQuotes(TransactionConstantsIfc.TYPE_ORDER_CANCEL))
            .append(")");

            sql.addQualifier(qualifier.toString());
        }
        else
        {
	        // add batch ID for POSLog
	        sql.addQualifier(ARTSDatabaseIfc.FIELD_TRANSACTION_ARCHIVE_BATCH_IDENTIFIER,
	                         inQuotes(tLogEntry.getBatchID()));
        }
        logger.debug("EXITING JdbcRetrieveTransactionIDsByBatchID.addSelectTransactionIDsQualifiers");
    }

    /**
     * Adds ordering to SQL statement for select transaction IDs.
     * @param sql SQLSelectStatement under construction
     */
    protected void addSelectTransactionIDsOrdering(SQLSelectStatement sql)
    {
        sql.addOrdering(ARTSDatabaseIfc.FIELD_TRANSACTION_END_DATE_TIMESTAMP);
    }

    
    /**
     * Parses ResultSet and returns list of entries.  If the ResultSet is empty,
     * then a DataException will be thrown stating &quot;No transactions found
     * matching search criteria.&quot;
     * <P>
     * @param rs result set
     * @return list of POSLogTransactionEntryIfc entries
     * @exception SQLException thrown if SQL error occurs
     * @exception DataException if <code>rs</code> is empty or if there is an
     *                          error retrieving EYSDate from string.
     * @see JdbcDataOperation#getEYSDateFromString(ResultSet, int)
     */
    protected POSLogTransactionEntryIfc[] parseSelectTransactionIDsResultSet(ResultSet rs)
            throws SQLException, DataException
    {
        ArrayList<POSLogTransactionEntryIfc> entryList = new ArrayList<POSLogTransactionEntryIfc>();
        POSLogTransactionEntryIfc entry = null;
        int index = 0;

        while (rs.next())
        {
            index = 0;
            // instantiate entry and set values
            entry =
              DomainGateway.getFactory().getPOSLogTransactionEntryInstance();
            entry.setStoreID(getSafeString(rs, ++index));
            entry.getTransactionID().setWorkstationID(getSafeString(rs, ++index));
            entry.setBusinessDate(getEYSDateFromString(rs, ++index));
            entry.getTransactionID().setSequenceNumber(rs.getInt(++index));
            entry.setBatchID(getSafeString(rs, ++index));

            // KLM: Set the business date on the transaction id,
            // this will allow the system to append it to the
            // transaction id if the domain properties are configured
            // to do that
            //
            entry.getTransactionID().setBusinessDate(getEYSDateFromString(rs, ++index));
            // add entry to list
            entryList.add(entry);
        }

        // if no entries, throw exception
        if (entryList.size() == 0)
        {
            throw new DataException(DataException.NO_DATA,
                                    "No transactions found matching search criteria.");
        }
        // copy list into array
        POSLogTransactionEntryIfc[] entries =
          new POSLogTransactionEntryIfc[entryList.size()];
        entryList.toArray(entries);

        return(entries);
    }
    
    
    
    
    /**
     * Parses ResultSet and returns list of entries.  If the ResultSet is empty,
     * then a DataException will be thrown stating &quot;No transactions found
     * matching search criteria.&quot;
     * <P>
     * @param rs result set
     * @return list of POSLogTransactionEntryIfc entries
     * @exception SQLException thrown if SQL error occurs
     * @exception DataException if <code>rs</code> is empty or if there is an
     *                          error retrieving EYSDate from string.
     * @see JdbcDataOperation#getEYSDateFromString(ResultSet, int)
     */
    protected POSLogTransactionEntryIfc[] parseSelectTransactionIDsResultSet(List<Map<String, Object>> recordsList)
            throws SQLException, DataException
    {
        ArrayList<POSLogTransactionEntryIfc> entryList = new ArrayList<POSLogTransactionEntryIfc>();
        POSLogTransactionEntryIfc entry = null;

        for(Map<String,Object> record : recordsList)
        {
            
            // instantiate entry and set values
            entry =
              DomainGateway.getFactory().getPOSLogTransactionEntryInstance();
            entry.setStoreID(String.valueOf(record.get(ARTSDatabaseIfc.FIELD_RETAIL_STORE_ID)));
            entry.getTransactionID().setWorkstationID(String.valueOf(record.get(ARTSDatabaseIfc.FIELD_WORKSTATION_ID)));
            
            String businessDateString= String.valueOf(record.get(ARTSDatabaseIfc.FIELD_BUSINESS_DAY_DATE));
            
            EYSDate businessDate = null;
            if (businessDateString != null && businessDateString.length() > 0) {
                businessDate = DomainGateway.getFactory().getEYSDateInstance();
                businessDate.setYear(Integer.parseInt(businessDateString.substring(0, 4)));
                businessDate.setMonth(Integer.parseInt(businessDateString.substring(5, 7)));
                businessDate.setDay(Integer.parseInt(businessDateString.substring(8, 10)));
                businessDate.setType(8);
            }
            
            entry.setBusinessDate(businessDate);
            entry.getTransactionID().setSequenceNumber(Long.valueOf(String.valueOf(record.get(ARTSDatabaseIfc.FIELD_TRANSACTION_SEQUENCE_NUMBER))));
            entry.setBatchID(String.valueOf(record.get(ARTSDatabaseIfc.FIELD_TRANSACTION_RTLOG_BATCH_IDENTIFIER)));

            // KLM: Set the business date on the transaction id,
            // this will allow the system to append it to the
            // transaction id if the domain properties are configured
            // to do that
            //
            entry.getTransactionID().setBusinessDate(businessDate);
            // add entry to list
            entryList.add(entry);
        }

        // if no entries, throw exception
        if (entryList.size() == 0)
        {
            throw new DataException(DataException.NO_DATA,
                                    "No transactions found matching search criteria.");
        }
        // copy list into array
        POSLogTransactionEntryIfc[] entries =
          new POSLogTransactionEntryIfc[entryList.size()];
        entryList.toArray(entries);

        return(entries);
    }
}
