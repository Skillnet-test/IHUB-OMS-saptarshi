/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package oracle.retail.stores.domain.arts;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import oracle.retail.stores.foundation.manager.data.DataException;
import oracle.retail.stores.xmlreplication.ExtractorObjectFactory;
import oracle.retail.stores.xmlreplication.ReplicationObjectFactoryContainer;
import oracle.retail.stores.xmlreplication.extractor.EntityReaderCatalogIfc;
import oracle.retail.stores.xmlreplication.extractor.EntitySearchIfc;
import oracle.retail.stores.xmlreplication.extractor.ReplicationExportException;
import oracle.retail.stores.xmlreplication.result.EntityIfc;

@Component
public class DataReplicationDataTransaction  implements DataReplicationDataTransactionIfc {

	private static final Logger logger = Logger.getLogger(DataReplicationDataTransaction.class);
	   
    @Autowired
    JdbcTemplate jdbcTemplate;
	
	public DataReplicationDataTransaction() 
	{
		System.out.println("Inside DataReplicationData Transaction");
	}
	

	public EntityIfc getDataReplicationBatch(DataReplicationSearchCriteria criteria) throws DataException 
	{
	
	    if (ReplicationObjectFactoryContainer.getInstance().getExtractorObjectFactory() == null) 
	    {
            ExtractorObjectFactory factory = new ExtractorObjectFactory();
            ReplicationObjectFactoryContainer.getInstance().setExtractorObjectFactory(factory);
        }
	    EntityIfc entity = ReplicationObjectFactoryContainer.getInstance().getExtractorObjectFactory()
                .getEntityInstance("Transaction");

        try 
        {
            Connection connection= jdbcTemplate.getDataSource().getConnection();
    
            EntityReaderCatalogIfc entityReaderCatalog = criteria.getCatalog();
            EntitySearchIfc entitySearch = criteria.getEntitySearch();
    
            if (logger.isDebugEnabled())
                  logger.debug(entitySearch);
            entityReaderCatalog.readEntity(entity, entitySearch, connection);
        } 
        catch (ReplicationExportException | SQLException dee) 
        {
            int type = 0;
            if (dee.getCause() instanceof SQLException) 
            {
                type = 1;
            }
            throw new DataException(type, "Error reading replication data.", dee);
        }
	    
		return entity;
	}


}