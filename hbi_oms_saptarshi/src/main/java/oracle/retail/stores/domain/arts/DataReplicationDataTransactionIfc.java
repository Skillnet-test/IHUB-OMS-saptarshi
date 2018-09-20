/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package oracle.retail.stores.domain.arts;

import oracle.retail.stores.foundation.manager.data.DataException;
import oracle.retail.stores.xmlreplication.result.EntityIfc;

public abstract interface DataReplicationDataTransactionIfc {
	public abstract EntityIfc getDataReplicationBatch(DataReplicationSearchCriteria paramDataReplicationSearchCriteria)
			throws DataException;

}