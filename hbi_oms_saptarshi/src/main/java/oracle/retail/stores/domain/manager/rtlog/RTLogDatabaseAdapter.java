/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package oracle.retail.stores.domain.manager.rtlog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oracle.retail.stores.domain.DomainGateway;
import oracle.retail.stores.domain.arts.DataReplicationDataTransaction;
import oracle.retail.stores.domain.arts.DataReplicationSearchCriteria;
import oracle.retail.stores.domain.arts.TransactionWriteDataTransaction;
import oracle.retail.stores.domain.ixretail.log.POSLogTransactionEntry;
import oracle.retail.stores.domain.ixretail.log.POSLogTransactionEntryIfc;
import oracle.retail.stores.domain.transaction.TransactionIDIfc;
import oracle.retail.stores.domain.utility.EYSDate;
import oracle.retail.stores.exportfile.DatabaseEntityAdapterIfc;
import oracle.retail.stores.exportfile.ExportFileException;
import oracle.retail.stores.exportfile.ExportResultIndicatorIfc;
import oracle.retail.stores.exportfile.rtlog.RTLogExportResultIndicatorIfc;
import oracle.retail.stores.foundation.manager.data.DataException;
import oracle.retail.stores.xmlreplication.extractor.EntityReaderCatalogIfc;
import oracle.retail.stores.xmlreplication.extractor.EntitySearchFieldIfc;
import oracle.retail.stores.xmlreplication.extractor.EntitySearchIfc;
import oracle.retail.stores.xmlreplication.result.EntityIfc;

@Repository
public class RTLogDatabaseAdapter implements DatabaseEntityAdapterIfc 
{
    @Autowired
    DataReplicationDataTransaction drdTransaction;
    
    @Autowired
    TransactionWriteDataTransaction twdTransaction;
    
	/**
     * @return the drdTransaction
     */
    public DataReplicationDataTransaction getDrdTransaction()
    {
        return drdTransaction;
    }

    /**
     * @return the twdTransaction
     */
    public TransactionWriteDataTransaction getTwdTransaction()
    {
        return twdTransaction;
    }

    /**
     * @param drdTransaction the drdTransaction to set
     */
    public void setDrdTransaction(DataReplicationDataTransaction drdTransaction)
    {
        this.drdTransaction = drdTransaction;
    }

    /**
     * @param twdTransaction the twdTransaction to set
     */
    public void setTwdTransaction(TransactionWriteDataTransaction twdTransaction)
    {
        this.twdTransaction = twdTransaction;
    }

    public EntityIfc getEntity(EntityReaderCatalogIfc catalog, EntitySearchIfc entitySearch)
			throws ExportFileException {
		EntityIfc entity = null;
		try {
			DataReplicationSearchCriteria criteria = new DataReplicationSearchCriteria();
			criteria.setCatalog(catalog);

			criteria.setEntitySearch(entitySearch);
			entity = drdTransaction.getDataReplicationBatch(criteria);
		} catch (DataException de) {
			ExportFileException efe = new ExportFileException(de.getMessage(), de);

			if (de.getErrorCode() == 3) {
				efe.setFatalException(true);
			}
			throw efe;
		}

		return entity;
	}

	public boolean postResults(Collection<EntitySearchIfc> searches, ExportResultIndicatorIfc[] resultsArray) {
		POSLogTransactionEntryIfc[] entries = convertToPOSLogTransactionEntry(searches, resultsArray);
		boolean noError = true;
		try
		{
		    twdTransaction.updateTransactionRTLogIDs(entries);
		} 
		catch (Exception e) 
		{
			noError = false;
		}

		return noError;
	}

	protected POSLogTransactionEntryIfc[] convertToPOSLogTransactionEntry(Collection<EntitySearchIfc> searches,
			ExportResultIndicatorIfc[] resultsArray) {
		POSLogTransactionEntryIfc[] entries = new POSLogTransactionEntryIfc[searches.size()];

		int resultIndex = 0;
		for (EntitySearchIfc searchFields : searches) {
			String storeID = getEntitySearchValue(searchFields, "ID_STR_RT");
			String workstationID = getEntitySearchValue(searchFields, "ID_WS");
			String transSeqNum = getEntitySearchValue(searchFields, "AI_TRN");
			String businessDay = getEntitySearchValue(searchFields, "DC_DY_BSN");

			EYSDate date = new EYSDate();
			String year = businessDay.substring(0, 4);
			String month = businessDay.substring(5, 7);
			String day = businessDay.substring(8);
			date.setYear(Integer.parseInt(year));
			date.setMonth(Integer.parseInt(month));
			date.setDay(Integer.parseInt(day));

			TransactionIDIfc transactionID = DomainGateway.getFactory().getTransactionIDInstance();
			transactionID.setBusinessDate(date);
			transactionID.setStoreID(storeID);
			transactionID.setSequenceNumber(Long.parseLong(transSeqNum));
			transactionID.setWorkstationID(workstationID);

			POSLogTransactionEntryIfc entry = new POSLogTransactionEntry();
			entry.setTransactionID(transactionID);
			RTLogExportResultIndicatorIfc indicator = (RTLogExportResultIndicatorIfc) resultsArray[resultIndex];
			entry.setBatchID(indicator.getResult().toString());
			entry.setBusinessDate(date);
			entry.setStoreID(storeID);

			entries[resultIndex] = entry;
			++resultIndex;
		}

		return entries;
	}

	private String getEntitySearchValue(EntitySearchIfc search, String columnName) {
		EntitySearchFieldIfc field = search.getSearchField(columnName.toUpperCase());
		return field.getFieldValue().toString();
	}
}