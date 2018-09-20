package oracle.retail.stores.domain.arts;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import oracle.retail.stores.domain.DomainGateway;
import oracle.retail.stores.domain.ixretail.log.POSLogTransactionEntryIfc;
import oracle.retail.stores.domain.utility.EYSDate;
import oracle.retail.stores.foundation.manager.data.DataException;
import oracle.retail.stores.foundation.manager.data.DataTransaction;
import oracle.retail.stores.foundation.manager.ifc.data.DataActionIfc;
import oracle.retail.stores.foundation.manager.ifc.data.DataTransactionIfc;
import oracle.retail.stores.foundation.utility.Util;

@Component
public class TransactionReadDataTransaction extends DataTransaction implements DataTransactionIfc
{

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    static final long serialVersionUID = -3159317257797343146L;

    /**
     * The logger to which log messages will be sent.
     */
    private static final Logger logger = Logger.getLogger(TransactionReadDataTransaction.class);



    /**
     * The default name that links this transaction to a command within
     * DataScript.
     */
    public static String dataCommandName = "TransactionReadDataTransaction";




    public TransactionReadDataTransaction()
    {
        super(dataCommandName);
    }


    public TransactionReadDataTransaction(String name)
    {
        super(name);
    }







    /**
     * Retrieves transaction identifiers for TLog creation tlog batch code,
     * business date and store ID. The business date and store identifier
     * parameters are optional.
     * 
     * @param storeID store identifier
     * @param businessDate business date (optional)
     * @param batchID TLog batch identifier
     * @return array of transaction tlog entry objects
     * @exception DataException thrown if error occurs
     * @throws SQLException 
     */
    public POSLogTransactionEntryIfc[] retrieveTransactionIDsByBatchID(String storeID, EYSDate businessDate,
            String batchID) throws DataException, SQLException
    {
        return retrieveTransactionIDsByBatchID(storeID, businessDate, batchID,
                POSLogTransactionEntryIfc.USE_BATCH_ARCHIVE);
    }

    /**
     * Retrieves transaction identifiers for TLog creation tlog batch code,
     * business date and store ID. The business date and store identifier
     * parameters are optional.
     * 
     * @param storeID store identifier
     * @param businessDate business date (optional)
     * @param batchID TLog batch identifier
     * @param columnID indcates if the TLog or Batch Archive column will be
     *            updated.
     * @return array of transaction tlog entry objects
     * @exception DataException thrown if error occurs
     * @throws SQLException 
     */
    public POSLogTransactionEntryIfc[] retrieveTransactionIDsByBatchID(String storeID, EYSDate businessDate,
            String batchID, int columnID) throws DataException, SQLException
    {
        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionIDsByBatchID");

        // set data actions and execute
        POSLogTransactionEntryIfc searchKey = DomainGateway.getFactory().getPOSLogTransactionEntryInstance();
        searchKey.setStoreID(storeID);
        searchKey.setBusinessDate(businessDate);
        searchKey.setBatchID(batchID);
        searchKey.setColumnID(columnID);

        DataActionIfc[] dataActions = new DataActionIfc[1];
        dataActions[0] = createDataAction(searchKey, "RetrieveTransactionIDsByBatchID");
        setDataActions(dataActions);


        JdbcRetrieveTransactionIDsByBatchID jdbcTransaction= new JdbcRetrieveTransactionIDsByBatchID();
        
        jdbcTransaction.execute(this, jdbcTemplate, dataActions[0]);
        POSLogTransactionEntryIfc[] entries = (POSLogTransactionEntryIfc[])this.getResult();
        
        if (logger.isDebugEnabled())
        {
            logger.debug("TransactionReadDataTransaction.retrieveTransactionIDsByBatchID from data manager: "
                    + getDataManager().getName());
            for (int i = 0; i < dataActions.length; i++)
            {
                logger.debug("DataAction[" + i + "] - " + dataActions[i].getDataOperationName() + ", "
                        + dataActions[i].getDataObject());
            }
        }
        return (entries);
    }

    /**
     * Retrieves transaction identifiers for TLog creation tlog batch code,
     * business date and store ID. The business date and store identifier
     * parameters are optional.
     * 
     * @param storeID store identifier
     * @param businessDate business date (optional)
     * @param batchID TLog batch identifier
     * @param columnID indcates if the TLog or Batch Archive column will be
     *            updated.
     * @param maximumTransactionsToExport indicates the maxTrans to be exported
     *            at one batch.
     * @return array of transaction tlog entry objects
     * @exception DataException thrown if error occurs
     * @throws SQLException 
     */
    public POSLogTransactionEntryIfc[] retrieveTransactionIDsByBatchID(String storeID, EYSDate businessDate,
            String batchID, int columnID, int maxTransactionsToExport) throws DataException, SQLException
    {
        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionIDsByBatchID");

        // set data actions and execute
        POSLogTransactionEntryIfc searchKey = DomainGateway.getFactory().getPOSLogTransactionEntryInstance();
        searchKey.setStoreID(storeID);
        searchKey.setBusinessDate(businessDate);
        searchKey.setBatchID(batchID);
        searchKey.setColumnID(columnID);
        searchKey.setMaximumTransactionsToExport(maxTransactionsToExport);

        DataActionIfc[] dataActions = new DataActionIfc[1];
        dataActions[0] = createDataAction(searchKey, "RetrieveTransactionIDsByBatchID");
        setDataActions(dataActions);

        JdbcRetrieveTransactionIDsByBatchID jdbcTransaction= new JdbcRetrieveTransactionIDsByBatchID();
        
       
        
        jdbcTransaction.execute(this,jdbcTemplate, dataActions[0]);
        POSLogTransactionEntryIfc[] entries = (POSLogTransactionEntryIfc[])this.getResult();

        if (logger.isDebugEnabled())
        {
           /* logger.debug("TransactionReadDataTransaction.retrieveTransactionIDsByBatchID from data manager: "
                    + getDataManager().getName());
            for (int i = 0; i < dataActions.length; i++)
            {
                logger.debug("DataAction[" + i + "] - " + dataActions[i].getDataOperationName() + ", "
                        + dataActions[i].getDataObject());
            }*/
        }
        return (entries);
    }

    /**
     * Retrieves transaction identifiers for TLog creation tlog batch code,
     * business date and store ID. The store identifier parameters is optional.
     * 
     * @param storeID store identifier
     * @param businessDate business date (optional)
     * @param batchID TLog batch identifier
     * @param columnID indcates if the TLog or Batch Archive column will be
     *            updated.
     * @return array of transaction tlog entry objects
     * @exception DataException thrown if error occurs
     */
    public POSLogTransactionEntryIfc[] retrieveTransactionsByTimePeriod(String storeID, EYSDate start, EYSDate end)
            throws DataException
    {
        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionsByTimePeriod");

        // set data actions and execute
        POSLogTransactionEntryIfc searchKey = DomainGateway.getFactory().getPOSLogTransactionEntryInstance();
        searchKey.setStoreID(storeID);
        searchKey.setStartTime(start);
        searchKey.setEndTime(end);

        DataActionIfc[] dataActions = new DataActionIfc[1];
        dataActions[0] = createDataAction(searchKey, "RetrieveTransactionIDsByTimePeriod");
        setDataActions(dataActions);

        POSLogTransactionEntryIfc[] entries = (POSLogTransactionEntryIfc[]) getDataManager().execute(this);

        if (logger.isDebugEnabled())
        {
            logger.debug("TransactionReadDataTransaction.retrieveTransactionIDsByTimePeriod from data manager: "
                    + getDataManager().getName());
            for (int i = 0; i < dataActions.length; i++)
            {
                logger.debug("DataAction[" + i + "] - " + dataActions[i].getDataOperationName() + ", "
                        + dataActions[i].getDataObject());
            }
        }
        return (entries);
    }

    /**
     * Retrieves transaction identifiers for transactions which haven't been
     * assigned to a batch. The business date and store identifier parameters
     * are optional.
     * 
     * @param storeID store identifier
     * @param businessDate business date (optional)
     * @return array of tlog transaction entry objects
     * @exception DataException thrown if error occurs
     * @throws SQLException 
     */
    public POSLogTransactionEntryIfc[] retrieveTransactionsNotInBatch(String storeID, EYSDate businessDate)
            throws DataException, SQLException
    {
        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionsNotInBatch");

        POSLogTransactionEntryIfc[] entries = retrieveTransactionIDsByBatchID(storeID, businessDate,
                POSLogTransactionEntryIfc.NO_BATCH_IDENTIFIED);

        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionsNotInBatch");

        return (entries);
    }

    /**
     * Retrieves transaction identifiers for transactions which haven't been
     * assigned to a batch. The business date and store identifier parameters
     * are optional.
     * 
     * @param storeID store identifier
     * @return array of tlog transaction entry objects
     * @exception DataException thrown if error occurs
     * @throws SQLException 
     */
    public POSLogTransactionEntryIfc[] retrieveTransactionsNotInBatch(String storeID) throws DataException, SQLException
    {
        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionsNotInBatch");

        POSLogTransactionEntryIfc[] entries = retrieveTransactionIDsByBatchID(storeID, null,
                POSLogTransactionEntryIfc.NO_BATCH_IDENTIFIED);

        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionsNotInBatch");

        return (entries);
    }

    /**
     * Retrieves transaction identifiers for transactions which haven't been
     * assigned to a batch. The business date and store identifier parameters
     * are optional.
     * 
     * @param storeID store identifier
     * @param columnID indcates if the TLog or Batch Archive column will be
     *            used.
     * @param maximumTransactionsToExport
     * @return array of tlog transaction entry objects
     * @exception DataException thrown if error occurs
     * @throws SQLException 
     */
    public POSLogTransactionEntryIfc[] retrieveTransactionsNotInBatch(String storeID, int columnID,
            int maximumTransactionsToExport)

    throws DataException, SQLException
    {

        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionsNotInBatch");

        POSLogTransactionEntryIfc[] entries = retrieveTransactionIDsByBatchID(storeID, null,
                POSLogTransactionEntryIfc.NO_BATCH_IDENTIFIED, columnID, maximumTransactionsToExport);

        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionsNotInBatch");

        return (entries);
    }

    /**
     * Retrieves transaction identifiers for transactions for a particular
     * business date. This is used for post-processing testing.
     * 
     * @param storeID store identifier
     * @param businessDate business date
     * @return array of tlog transaction entry objects
     * @exception DataException thrown if error occurs
     */
    public POSLogTransactionEntryIfc[] retrieveTransactionIDsByBusinessDate(String storeID, EYSDate businessDate)
            throws DataException
    {
        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionIDsByBusinessDate");

        // set data actions and execute
        POSLogTransactionEntryIfc searchKey = DomainGateway.getFactory().getPOSLogTransactionEntryInstance();
        searchKey.setStoreID(storeID);
        searchKey.setBusinessDate(businessDate);

        DataActionIfc[] dataActions = new DataActionIfc[1];
        dataActions[0] = createDataAction(searchKey, "RetrieveTransactionIDsByBusinessDate");
        setDataActions(dataActions);

        POSLogTransactionEntryIfc[] entries = (POSLogTransactionEntryIfc[]) getDataManager().execute(this);

        if (logger.isDebugEnabled())
            logger.debug("TransactionReadDataTransaction.retrieveTransactionIDsByBusinessDate");

        return (entries);
    }



    /**
     * Returns the revision number of this class.
     * 
     * @return String representation of revision number
     */
    public String getRevisionNumber()
    {
        return (Util.parseRevisionNumber(revisionNumber));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder strResult = Util.classToStringHeader(getClass().getName(), getRevisionNumber(), hashCode());
        return (strResult.toString());
    }
}
