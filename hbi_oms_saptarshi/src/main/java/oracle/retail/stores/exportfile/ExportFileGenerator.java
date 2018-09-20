/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package oracle.retail.stores.exportfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oracle.retail.stores.common.processcontroller.WorkUnitControllerException;
import oracle.retail.stores.common.processcontroller.WorkUnitIfc;
import oracle.retail.stores.common.utility.Util;
import oracle.retail.stores.domain.manager.rtlog.RTLogDatabaseAdapter;
import oracle.retail.stores.exportfile.formater.RecordFormatCatalogIfc;
import oracle.retail.stores.exportfile.formater.RecordFormatContentBuilderIfc;
import oracle.retail.stores.exportfile.mapper.EntityMapperIfc;
import oracle.retail.stores.exportfile.mapper.EntityMappingCatalogIfc;
import oracle.retail.stores.xmlreplication.extractor.EntityReaderCatalogIfc;
import oracle.retail.stores.xmlreplication.extractor.EntitySearchIfc;
import oracle.retail.stores.xmlreplication.result.EntityIfc;

@Component
public class ExportFileGenerator implements ExportFileGeneratorIfc {
	protected static final Logger logger = Logger.getLogger(ExportFileGenerator.class);

	@Autowired
	protected RTLogDatabaseAdapter entityAdapter;

	protected EncryptionAdapterIfc encryptionAdapter = null;

	protected CurrencyAdapterIfc currencyAdapter = null;

	protected RecordFormatCatalogIfc formatCatalog = null;

	protected EntityReaderCatalogIfc readerCatalog = null;

	protected EntityMappingCatalogIfc mappingCatalog = null;

	protected OutputAdapterIfc outputAdapter = null;

	protected boolean initFailed = false;

	protected boolean firstInBatch = true;

	protected ExportResultIndicatorIfc[] resultsArray = null;

	protected ExportResultIndicatorIfc successIndicator = null;

	protected ExportResultIndicatorIfc failureIndicator = null;

	protected ExportFileResultAuditLogIfc resultLogger = null;

	protected int totalTargets = 0;

	protected int successfulReads = 0;

	protected int failedReads = 0;

	protected int successfulMappings = 0;

	protected int failedMappings = 0;

	protected int successfulWrites = 0;
	protected ExportResultIndicatorIfc blockedIndicator;
	protected int blockedExports;

	public void process(WorkUnitIfc workUnit) throws WorkUnitControllerException {
		if (this.initFailed) {
			String message = "Export file initialization failed;  no records will be exported.  Please check the log for other initialization error messages.";

			logger.error(message);

			throw new WorkUnitControllerException(message);
		}

		EntityMapperIfc mapper = null;
		try {
			mapper = MappingObjectFactoryContainer.getInstance().getMappingObjectFactory().getEntityMapperInstance();

			mapper.setCatalogs(this.formatCatalog, this.mappingCatalog, this.readerCatalog);
			mapper.setEncryptionAdapter(this.encryptionAdapter);
			mapper.setCurrencyAdapter(this.currencyAdapter);
		} catch (ExportFileException efe) {
			throw new WorkUnitControllerException(efe.getMessage(), efe.getCause());
		} catch (Exception e) {
			String message = "Export file generation failed due to an unexpected exception while setting the EntityMapper.";
			logger.error(message, e);
			throw new WorkUnitControllerException(message, e);
		}

		try {
			this.outputAdapter.open();
			this.resultLogger.setExportFileName(this.outputAdapter.getFileName());
		} catch (ExportFileException efe) {
			throw new WorkUnitControllerException(efe.getMessage(), efe.getCause());
		} catch (Exception e) {
			String message = "Export file generation failed due to an unexpected exception while opening the output adapter.";
			logger.error(message, e);
			throw new WorkUnitControllerException(message, e);
		}

		RecordFormatContentBuilderIfc recordBuilder = MappingObjectFactoryContainer.getInstance()
				.getFormatObjectFactory().getRecordFormatContentBuilderInstance();

		Collection objects = workUnit.getTargets();
		this.totalTargets = workUnit.getTargets().size();
		this.resultsArray = new ExportResultIndicatorIfc[this.totalTargets];
		ArrayList searches = new ArrayList();
		try {
			int resultIndex = 0;
			StringBuffer fileHeader = new StringBuffer();

			for (Iterator i$ = objects.iterator(); i$.hasNext();) {
				Object obj = i$.next();

				EntitySearchIfc searchFields = (EntitySearchIfc) obj;
				searches.add(searchFields);

				EntityIfc entity = getEntity(this.readerCatalog, searchFields, resultIndex);

				if ((entity != null)
						&& (mapEntityToRecordFormat(entity, resultIndex, recordBuilder, mapper, fileHeader))) {
					if ((fileHeader != null) && (fileHeader.length() > 0)) {
						this.outputAdapter.write(fileHeader.toString(), false);
						fileHeader = null;
					}

					char[] chars = recordBuilder.getContentChars();
					this.outputAdapter.write(chars, false);
					Util.flushCharArray(chars);
					chars = null;
					this.successfulWrites += 1;
					this.resultsArray[resultIndex] = this.successIndicator;
				}

				++resultIndex;
			}

			if (this.successfulWrites > 0) {
				recordBuilder.addFileTailToContent(mapper.getResults());
				char[] chars = recordBuilder.getContentChars();
				this.outputAdapter.write(chars, true);
				Util.flushCharArray(chars);
				chars = null;
			}
		} catch (ExportFileException efe) {
			this.outputAdapter.cleanupFailure();

			throw new WorkUnitControllerException(efe.getMessage(), efe.getCause());
		} catch (Exception e) {
			this.outputAdapter.cleanupFailure();
			String message = "Export file generation failed due to an unexpected exception";
			logger.error(message, e);
			throw new WorkUnitControllerException(message, e);
		}

		if (!(this.entityAdapter.postResults(searches, this.resultsArray))) {
			this.outputAdapter.cleanupFailure();
		} else if (this.successfulWrites == 0) {
			this.outputAdapter.cleanupFailure();
		} else {
			try {
				this.outputAdapter.close();
			} catch (ExportFileException efe) {
			} catch (Exception e) {
				this.outputAdapter.cleanupFailure();
				String message = "Export file generation failed due to an unexpected exception";
				logger.error(message, e);
			}

		}

		this.resultLogger.setExportFormatVersion(this.formatCatalog.getFormatVersion());
		this.resultLogger.setSuccessIndicator(this.successIndicator);
		this.resultLogger.setFailedMappings(this.failedMappings);
		this.resultLogger.setBlockedExports(this.blockedExports);
		this.resultLogger.setFailedReads(this.failedReads);
		this.resultLogger.setSuccessfulMappings(this.successfulMappings);
		this.resultLogger.setSuccessfulReads(this.successfulReads);
		this.resultLogger.setSuccessfulWrites(this.successfulWrites);
		this.resultLogger.setTotalTargets(this.totalTargets);
		this.resultLogger.logResults();
	}

	protected EntityIfc getEntity(EntityReaderCatalogIfc readerCatalog, EntitySearchIfc entitySearch, int searchIndex)
			throws ExportFileException {
		EntityIfc entity = null;
		try {
			entity = this.entityAdapter.getEntity(readerCatalog, entitySearch);
			this.successfulReads += 1;
		} catch (ExportFileException efe) {
			if (efe.isFatalException()) {
				throw efe;
			}

			this.resultsArray[searchIndex] = this.failureIndicator;
			this.failedReads += 1;
		} catch (Exception e) {
			String message = "Export file generation failed due to an unexpected exception that occurred while reading the Entity from the database.";
			this.resultsArray[searchIndex] = this.failureIndicator;
			logger.error(message, e);
			this.failedReads += 1;
		}

		return entity;
	}

	protected boolean mapEntityToRecordFormat(EntityIfc entity, int searchIndex,
			RecordFormatContentBuilderIfc recordBuilder, EntityMapperIfc mapper, StringBuffer fileHeader) {
		boolean succeeded = false;
		try {
			mapper.map(entity);

			if (mapper.getResults().isTransactionExportable()) {
				if (this.firstInBatch) {
					recordBuilder.addFileHeaderToContent(mapper.getResults(), this.formatCatalog.getFormatVersion(),
							this.successIndicator.toFormattedString());

					char[] chars = recordBuilder.getContentChars();
					fileHeader.append(chars);
					Util.flushCharArray(chars);
					chars = null;
					//this.firstInBatch = false;
				}

				recordBuilder.addTransactionToContent(mapper.getResults());
				succeeded = true;
				this.successfulMappings += 1;
			} else {
				this.resultsArray[searchIndex] = this.blockedIndicator;
				this.blockedExports += 1;

				recordBuilder.clearContent();
			}

		} catch (ExportFileException efe) {
			this.resultsArray[searchIndex] = this.failureIndicator;
			this.failedMappings += 1;

			recordBuilder.clearContent();
		} catch (Exception e) {
			String message = "Export file generation failed due to an unexpected exception that occurred while mapping the Entity to the output format.";
			this.resultsArray[searchIndex] = this.failureIndicator;
			logger.error(message, e);
			this.failedMappings += 1;

			recordBuilder.clearContent();
		}

		return succeeded;
	}

	public void setExportFileConfiguration(ExportFileConfigurationIfc config) {
		this.formatCatalog = config.getFormatCatalog();
		this.mappingCatalog = config.getMappingCatalog();
		this.readerCatalog = config.getReaderCatalog();
		//this.entityAdapter = config.getEntityAdapter();
		this.encryptionAdapter = config.getEncryptionAdapter();
		this.outputAdapter = config.getOutputAdapter();
		this.initFailed = config.isInitFailed();
		this.successIndicator = config.getSuccessIndicator();
		this.failureIndicator = config.getFailureIndicator();
		this.blockedIndicator = config.getExportBlockedIndicator();
		this.resultLogger = config.getResultLogger();
		this.currencyAdapter = config.getCurrencyAdapter();
	}

}