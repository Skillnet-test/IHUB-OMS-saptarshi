package com.oms.rtlog.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oms.rtlog.OMSRTLogExportConfiguration;

import oracle.retail.stores.common.data.JdbcUtilities;
import oracle.retail.stores.common.data.jdbchelper.OracleHelper;
import oracle.retail.stores.domain.manager.rtlog.RTLogExportBatchGeneratorIfc;
import oracle.retail.stores.domain.manager.rtlog.RTLogExportException;

/**
 * Handles requests for the Employee JDBC Service.
 */
@RestController
public class RTLogController {
	
	private static final Logger logger = Logger.getLogger(RTLogController.class);
	
	@Autowired
	RTLogExportBatchGeneratorIfc batchGenerator;
	
	@Autowired
	OMSRTLogExportConfiguration rtLogConfiguration;


	@RequestMapping(value = "/oms/rtlog", method = RequestMethod.GET)
	public String generateRTLog() 
	{
	    String result="Success";
		logger.info("Start generate RTLog.");
		try
        {
		    JdbcUtilities.setJdbcHelperClass(new OracleHelper());
            batchGenerator.setExportFileConfiguration(rtLogConfiguration.getExportFileConfig());
            batchGenerator.setMaximumTransactionsToExport(rtLogConfiguration.getMaximumTransactionsToExport());
            batchGenerator.generateBatch();
        }
        catch (RTLogExportException e)
        {
            result="Failed";
            logger.error("Error while RTLog Generation");
        }
		
		return result;
	}

}
