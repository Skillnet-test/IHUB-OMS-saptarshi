/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package oracle.retail.stores.domain.arts;

import java.io.Serializable;
import java.sql.Connection;

import oracle.retail.stores.xmlreplication.extractor.EntityReaderCatalogIfc;
import oracle.retail.stores.xmlreplication.extractor.EntitySearchIfc;

public class DataReplicationSearchCriteria implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected EntityReaderCatalogIfc catalog = null;

	protected EntitySearchIfc entitySearch = null;
	
	protected Connection connection=null;

	/**
     * @return the connection
     */
    public Connection getConnection()
    {
        return connection;  
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }

    public EntityReaderCatalogIfc getCatalog() {
		return this.catalog;
	}

	public void setCatalog(EntityReaderCatalogIfc catalog) {
		this.catalog = catalog;
	}

	public EntitySearchIfc getEntitySearch() {
		return this.entitySearch;
	}

	public void setEntitySearch(EntitySearchIfc entitySearch) {
		this.entitySearch = entitySearch;
	}
}