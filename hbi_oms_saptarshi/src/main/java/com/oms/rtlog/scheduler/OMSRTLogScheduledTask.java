/**
 * 
 */
package com.oms.rtlog.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oms.rtlog.OMSRTLogExportConfiguration;

import oracle.retail.stores.common.data.JdbcUtilities;
import oracle.retail.stores.common.data.jdbchelper.OracleHelper;
import oracle.retail.stores.domain.manager.rtlog.RTLogExportBatchGeneratorIfc;
import oracle.retail.stores.domain.manager.rtlog.RTLogExportException;

/**
 * @author Jigar
 *
 */
@Component
public class OMSRTLogScheduledTask
{
    protected static final Logger logger = Logger.getLogger(OMSRTLogScheduledTask.class);
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Autowired
    RTLogExportBatchGeneratorIfc batchGenerator;
    
    @Autowired
    OMSRTLogExportConfiguration rtLogConfiguration;
    
    /**
     * 
     */
    public OMSRTLogScheduledTask()
    {
        logger.info("OMSRTLogScheduledTask Initialised");
    }
    


    @Scheduled(fixedRate = 1000000)
    public void generateRTLog()
    {
        logger.info("RTLOG Generation Started at "+dateFormat.format(new Date()));
        
        try
        {
            JdbcUtilities.setJdbcHelperClass(new OracleHelper());
            batchGenerator.setExportFileConfiguration(rtLogConfiguration.getExportFileConfig());
            batchGenerator.setMaximumTransactionsToExport(rtLogConfiguration.getMaximumTransactionsToExport());
            batchGenerator.generateBatch();
            logger.info("RTLOG Generation Completed at "+dateFormat.format(new Date()));
        }
        catch (RTLogExportException e)
        {
            logger.error("Error while RTLog Generation");
        }

    }



}
