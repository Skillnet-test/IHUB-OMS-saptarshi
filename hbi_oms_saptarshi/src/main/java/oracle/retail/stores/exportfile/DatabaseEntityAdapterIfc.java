/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/

// Warning: No line numbers available in class file

package oracle.retail.stores.exportfile;

import java.util.Collection;

import oracle.retail.stores.domain.arts.DataReplicationDataTransaction;
import oracle.retail.stores.domain.arts.TransactionWriteDataTransaction;
import oracle.retail.stores.xmlreplication.extractor.EntityReaderCatalogIfc;
import oracle.retail.stores.xmlreplication.extractor.EntitySearchIfc;
import oracle.retail.stores.xmlreplication.result.EntityIfc;

public abstract interface DatabaseEntityAdapterIfc
{
  public abstract EntityIfc getEntity(EntityReaderCatalogIfc paramEntityReaderCatalogIfc, EntitySearchIfc paramEntitySearchIfc)
    throws ExportFileException;

  public abstract boolean postResults(Collection<EntitySearchIfc> paramCollection, ExportResultIndicatorIfc[] paramArrayOfExportResultIndicatorIfc);
  
  public DataReplicationDataTransaction getDrdTransaction();
  
  public TransactionWriteDataTransaction getTwdTransaction();
  
  public void setDrdTransaction(DataReplicationDataTransaction drdTransaction);
  
  public void setTwdTransaction(TransactionWriteDataTransaction twdTransaction);
}
