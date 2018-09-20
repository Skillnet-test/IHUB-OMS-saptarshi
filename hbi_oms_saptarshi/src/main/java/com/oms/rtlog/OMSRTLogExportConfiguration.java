/**
 * 
 */
package com.oms.rtlog;



import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import oracle.retail.stores.domain.DomainGateway;
import oracle.retail.stores.domain.utility.EYSTime;
import oracle.retail.stores.exportfile.CurrencyAdapterIfc;
import oracle.retail.stores.exportfile.DatabaseEntityAdapterIfc;
import oracle.retail.stores.exportfile.EncryptionAdapterIfc;
import oracle.retail.stores.exportfile.EntityMappingObjectFactoryIfc;
import oracle.retail.stores.exportfile.ExportFileConfigurationIfc;
import oracle.retail.stores.exportfile.ExportFileException;
import oracle.retail.stores.exportfile.ExportFileResultAuditLogIfc;
import oracle.retail.stores.exportfile.MappingObjectFactoryContainer;
import oracle.retail.stores.exportfile.RecordFormatObjectFactoryIfc;
import oracle.retail.stores.exportfile.rtlog.RTLogExportResultIndicator;
import oracle.retail.stores.exportfile.rtlog.RTLogExportResultIndicatorIfc;
import oracle.retail.stores.exportfile.rtlog.RTLogOutputAdapterIfc;
import oracle.retail.stores.foundation.tour.gate.Gateway;
import oracle.retail.stores.foundation.utility.config.ConfigurationException;
import oracle.retail.stores.xmlreplication.ExtractorObjectFactoryIfc;
import oracle.retail.stores.xmlreplication.ReplicationObjectFactoryContainer;

/**
 * @author Jigar
 *
 */

@Component
@PropertySource("classpath:rtlogconfig.properties")
@ConfigurationProperties
public class OMSRTLogExportConfiguration
{

    private static Logger logger =  Logger.getLogger(OMSRTLogExportConfiguration.class.getName());
    
    protected  Long errorBatchID = new Long(-2L);

    protected  Long blockedBatchID = new Long(-100L);

    protected String exportDirectoryName;

    protected String formatConfigurationFileName;

    protected String entityReaderConfigurationFileName;

    protected String entityMappingConfigurationFileName;

    protected String mappingObjectFactoryClassName;

    protected String recordFormatObjectFactoryClassName;

    protected String extractorObjectFactoryClassName;

    protected String databaseAdapterClassName;

    protected String encryptionAdapterClassName;

    protected String outputAdapterClassName;

    protected String currencyAdapterClassName;

    protected String resultAuditLogClassName;

    protected String exportFileConfigurationClassName;

    protected ExportFileConfigurationIfc exportFileConfig = null;

    protected int maximumTransactionsToExport = 1;
    
    /**
     * RTLog Export Configuration
     */
    public OMSRTLogExportConfiguration()
    {
        logger.info("Loading OMSRTLogExportConfiguration!");
    }

    
    /**
     * RTLog Export Configuration
     */
    @PostConstruct
    public void init()
    {
        try
        {
            setupExportFileConfiguration();
            logger.info("Successfully set  OMS RTLog Export Configuration!");
        }
        catch (ConfigurationException e)
        {
            logger.error("Unable to load OMS RTLog Configuration!");
        }
    }
    
    protected void setupExportFileConfiguration() throws ConfigurationException 
    {
        instantiateExportFileClasses();

        instantiateObjectFactories();

        this.exportFileConfig.setFileFormatConfiguration(this.formatConfigurationFileName);
        this.exportFileConfig.setEntityReaderConfiguration(this.entityReaderConfigurationFileName);
        this.exportFileConfig.setMappingConfiguration(this.entityMappingConfigurationFileName);

        RTLogExportResultIndicatorIfc ind = new RTLogExportResultIndicator();
        ind.setResult(errorBatchID);
        this.exportFileConfig.setFailureIndicator(ind);

        ind = new RTLogExportResultIndicator();
        ind.setResult(blockedBatchID);
        this.exportFileConfig.setExportBlockedIndicator(ind);
        try 
        {
            this.exportFileConfig.init();
        } 
        catch (ExportFileException efe) 
        {
            throw new ConfigurationException(efe.getMessage(), efe);
        }
        
    }
    
    protected void instantiateExportFileClasses() throws ConfigurationException 
    {
        try 
        {
            this.exportFileConfig = ((ExportFileConfigurationIfc) instantiateClass(
                    this.exportFileConfigurationClassName));
        } catch (ClassCastException cce) 
        {
            String message = "Error in the conduit script .  Class from name '" + this.exportFileConfigurationClassName
                    + "' is not an instance of ExportFileConfigurationIfc.";

            logger.error(message);
            throw new ConfigurationException(message);
        }

        try {
            this.exportFileConfig
                    .setEntityAdapter((DatabaseEntityAdapterIfc) instantiateClass(this.databaseAdapterClassName));
        } catch (ClassCastException cce) {
            String message = "Error in the conduit script .  Class from name '" + this.databaseAdapterClassName
                    + "' is not an instance of DatabaseEntityAdapterIfc.";

            logger.error(message);
            throw new ConfigurationException(message);
        }

        try {
            this.exportFileConfig
                    .setEncryptionAdapter((EncryptionAdapterIfc) instantiateClass(this.encryptionAdapterClassName));
        } catch (ClassCastException cce) {
            String message = "Error in the conduit script .  Class from name '" + this.encryptionAdapterClassName
                    + "' is not an instance of EncryptionAdapterIfc.";

            logger.error(message);
            throw new ConfigurationException(message);
        }

        try {
            String storeID = Gateway.getProperty("application", "StoreID", "99999");
            if (storeID.length() == 0) {
                String message = "Error error retriving Store ID from properties.";
                logger.error(message);
                throw new ConfigurationException(message);
            }
            RTLogOutputAdapterIfc adapter = (RTLogOutputAdapterIfc) instantiateClass(this.outputAdapterClassName);
            adapter.setExportDirectoryName(this.exportDirectoryName);
            adapter.setStoreID(storeID);
            this.exportFileConfig.setOutputAdapter(adapter);
        } catch (ClassCastException cce) {
            String message = "Error in the conduit script .  Class from name '" + this.databaseAdapterClassName
                    + "' is not an instance of OutputAdapterIfc.";

            logger.error(message);
            throw new ConfigurationException(message);
        }

        try {
            this.exportFileConfig
                    .setResultLogger((ExportFileResultAuditLogIfc) instantiateClass(this.resultAuditLogClassName));
        } catch (ClassCastException cce) {
            String message = "Error in the conduit script .  Class from name '" + this.resultAuditLogClassName
                    + "' is not an instance of ExportFileResultAuditLogIfc.";

            logger.error(message);
            throw new ConfigurationException(message);
        }

        try {
            this.exportFileConfig
                    .setCurrencyAdapter((CurrencyAdapterIfc) instantiateClass(this.currencyAdapterClassName));
        } catch (ClassCastException cce) {
            String message = "Error in the conduit script .  Class from name '" + this.resultAuditLogClassName
                    + "' is not an instance of ExportFileResultAuditLogIfc.";

            logger.error(message);
            throw new ConfigurationException(message);
        }
    }

    private void instantiateObjectFactories() throws ConfigurationException {
        if (ReplicationObjectFactoryContainer.getInstance().getExtractorObjectFactory() == null) {
            try {
                ExtractorObjectFactoryIfc factory = (ExtractorObjectFactoryIfc) instantiateClass(
                        this.extractorObjectFactoryClassName);

                ReplicationObjectFactoryContainer.getInstance().setExtractorObjectFactory(factory);
            } catch (ClassCastException cce) {
                String message = "Error in the conduit script .  Class from name '"
                        + this.extractorObjectFactoryClassName + "' is not an instance of ExtractorObjectFactoryIfc.";

                logger.error(message);
                throw new ConfigurationException(message);
            }
        }
        if (MappingObjectFactoryContainer.getInstance().getMappingObjectFactory() == null) {
            try {
                EntityMappingObjectFactoryIfc factory = (EntityMappingObjectFactoryIfc) instantiateClass(
                        this.mappingObjectFactoryClassName);

                MappingObjectFactoryContainer.getInstance().setMappingObjectFactory(factory);
            } catch (ClassCastException cce) {
                String message = "Error in the conduit script .  Class from name '" + this.mappingObjectFactoryClassName
                        + "' is not an instance of EntityMappingObjectFactoryIfc.";

                logger.error(message);
                throw new ConfigurationException(message);
            }
        }
        if (MappingObjectFactoryContainer.getInstance().getFormatObjectFactory() != null)
            return;
        try {
            RecordFormatObjectFactoryIfc factory = (RecordFormatObjectFactoryIfc) instantiateClass(
                    this.recordFormatObjectFactoryClassName);

            MappingObjectFactoryContainer.getInstance().setFormatObjectFactory(factory);
        } catch (ClassCastException cce) {
            String message = "Error in the conduit script .  Class from name '"
                    + this.recordFormatObjectFactoryClassName + "' is not an instance of RecordFormatObjectFactoryIfc.";

            logger.error(message);
            throw new ConfigurationException(message);
        }
    }

    @SuppressWarnings("rawtypes")
    protected Object instantiateClass(String className) throws ConfigurationException {
        try {
            Class processClass = Class.forName(className);
            return processClass.newInstance();
        } catch (Exception e) {
            String message = "Error in the conduit script .  Failed to instantiate class from name '" + className
                    + "'.";

            logger.error(message, e);
            throw new ConfigurationException(message, e);
        }
    }
    
    

    public String getDatabaseAdapterClassName() {
        return this.databaseAdapterClassName;
    }

    public void setDatabaseAdapterClassName(String databaseAdapterName) {
        this.databaseAdapterClassName = databaseAdapterName;
    }

    public String getEncryptionAdapterClassName() {
        return this.encryptionAdapterClassName;
    }

    public void setEncryptionAdapterClassName(String encryptionAdapterName) {
        this.encryptionAdapterClassName = encryptionAdapterName;
    }

    public String getEntityMappingConfigurationFileName() {
        return this.entityMappingConfigurationFileName;
    }

    public void setEntityMappingConfigurationFileName(String entityMappingConfigurationFileName) {
        this.entityMappingConfigurationFileName = entityMappingConfigurationFileName;
    }

    public String getEntityReaderConfigurationFileName() {
        return this.entityReaderConfigurationFileName;
    }

    public void setEntityReaderConfigurationFileName(String entityReaderConfigurationFileName) {
        this.entityReaderConfigurationFileName = entityReaderConfigurationFileName;
    }

    public ExportFileConfigurationIfc getExportFileConfig() {
        return this.exportFileConfig;
    }

    public void setExportFileConfig(ExportFileConfigurationIfc exportFileConfig) {
        this.exportFileConfig = exportFileConfig;
    }

    public String getExportFileConfigurationClassName() {
        return this.exportFileConfigurationClassName;
    }

    public void setExportFileConfigurationClassName(String exportFileConfigurationName) {
        this.exportFileConfigurationClassName = exportFileConfigurationName;
    }

    public String getFormatConfigurationFileName() {
        return this.formatConfigurationFileName;
    }

    public void setFormatConfigurationFileName(String formatConfigurationFileName) {
        this.formatConfigurationFileName = formatConfigurationFileName;
    }

    public String getOutputAdapterClassName() {
        return this.outputAdapterClassName;
    }

    public void setOutputAdapterClassName(String outputAdapterName) {
        this.outputAdapterClassName = outputAdapterName;
    }

    public String getResultAuditLogClassName() {
        return this.resultAuditLogClassName;
    }

    public void setResultAuditLogClassName(String resultAutditLogName) {
        this.resultAuditLogClassName = resultAutditLogName;
    }

    public String getExtractorObjectFactoryClassName() {
        return this.extractorObjectFactoryClassName;
    }

    public void setExtractorObjectFactoryClassName(String extractorObjectFactoryClassName) {
        this.extractorObjectFactoryClassName = extractorObjectFactoryClassName;
    }

    public String getMappingObjectFactoryClassName() {
        return this.mappingObjectFactoryClassName;
    }

    public void setMappingObjectFactoryClassName(String mappingObjectFactoryClassName) {
        this.mappingObjectFactoryClassName = mappingObjectFactoryClassName;
    }

    public String getRecordFormatObjectFactoryClassName() {
        return this.recordFormatObjectFactoryClassName;
    }

    public void setRecordFormatObjectFactoryClassName(String recordFormatObjectFactoryClassName) {
        this.recordFormatObjectFactoryClassName = recordFormatObjectFactoryClassName;
    }

    public String getExportDirectoryName() {
        return this.exportDirectoryName;
    }

    public void setExportDirectoryName(String exportDirectoryName) {
        this.exportDirectoryName = exportDirectoryName;
    }

    public void setMaximumTransactionsToExport(int maximumTransactionsToExport) {
        this.maximumTransactionsToExport = maximumTransactionsToExport;
    }
    
    public int getMaximumTransactionsToExport()
    {
        return maximumTransactionsToExport;
    }

    public void setMaximumTransactionsToExport(String maxTransactionsToExport) {
        try {
            int max = Integer.parseInt(maxTransactionsToExport);
            setMaximumTransactionsToExport(max);
        } catch (Exception e) {
        }
    }


    protected EYSTime convertToTime(String time) {
        EYSTime eTime = null;
        try {
            int hour = Integer.parseInt(time.substring(0, 2));
            int min = Integer.parseInt(time.substring(3, 5));
            int sec = Integer.parseInt(time.substring(6, 8));
            eTime = DomainGateway.getFactory().getEYSTimeInstance();
            eTime.setHour(hour);
            eTime.setMinute(min);
            eTime.setSecond(sec);
            eTime.setMillisecond(0);
        } catch (Exception e) {
        }

        return eTime;
    }

    public String getCurrencyAdapterClassName() {
        return this.currencyAdapterClassName;
    }

    public void setCurrencyAdapterClassName(String currencyAdapterClassName) {
        this.currencyAdapterClassName = currencyAdapterClassName;
    }


    /**
     * @return the errorBatchID
     */
    public Long getErrorBatchID()
    {
        return errorBatchID;
    }


    /**
     * @return the blockedBatchID
     */
    public Long getBlockedBatchID()
    {
        return blockedBatchID;
    }


    /**
     * @param errorBatchID the errorBatchID to set
     */
    public void setErrorBatchID(String errorBatchID)
    {
        this.errorBatchID = Long.valueOf(errorBatchID);
    }


    /**
     * @param blockedBatchID the blockedBatchID to set
     */
    public void setBlockedBatchID(String blockedBatchID)
    {
        this.blockedBatchID = Long.valueOf(blockedBatchID);
    }





}
