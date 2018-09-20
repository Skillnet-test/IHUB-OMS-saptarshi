/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/

/* 
------------------------------------------------------------------------------------------- FILE HISTORY --------------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     DEEFECT ID/FSD MOD.     DESCRIPTION
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	16/08/2017   Ikshita       HBI Everest             Added Class for adding TransactionCustomer(TCUST) tag in RTLOg files.
   2.     	10/10/2017   Ikshita       HBI Everest             Added condition for not adding TransactionCustomer(TCUST) tag in RTLOg files if transaction type is post void, void or no sale.
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/

package nbty.retail.stores.exportfile.rtlog.accessors;

import nbty.retail.stores.exportfile.rtlog.NbtyRTLogRecordFormatConstantsIfc;
import oracle.retail.stores.exportfile.ExportFileException;
import oracle.retail.stores.exportfile.MappingObjectFactoryContainer;
import oracle.retail.stores.exportfile.formater.FieldFormatIfc;
import oracle.retail.stores.exportfile.formater.RecordFormatCatalogIfc;
import oracle.retail.stores.exportfile.formater.RecordFormatIfc;
import oracle.retail.stores.exportfile.mapper.AbstractAccessor;
import oracle.retail.stores.exportfile.mapper.AccessorIfc;
import oracle.retail.stores.exportfile.mapper.MappingResultIfc;
import oracle.retail.stores.exportfile.rtlog.RTLogMappingResult;
import oracle.retail.stores.xmlreplication.result.EntityIfc;
import oracle.retail.stores.xmlreplication.result.Row;

public class NbtyAccessTransactionCustomer extends AbstractAccessor implements AccessorIfc, NbtyRTLogRecordFormatConstantsIfc 
{
	public RecordFormatIfc getWorkingRecordFormat(String recordName,String tableName, Row row, MappingResultIfc results,RecordFormatCatalogIfc formatCatalog, EntityIfc entity)
			throws ExportFileException 
	{
		RTLogMappingResult rtlogResults = (RTLogMappingResult) results;
		RecordFormatIfc transCustomerRecordFormat = rtlogResults.getTransactionCustomer();

		if (transCustomerRecordFormat == null) 
		{
			transCustomerRecordFormat = MappingObjectFactoryContainer.getInstance().getFormatObjectFactory().getRecordFormatInstance();

			RecordFormatIfc template = formatCatalog.getFormat(TRANSACTION_CUSTOMER);
			template.makeDeepCopyOfTemplate(transCustomerRecordFormat);
			rtlogResults.setTransactionCustomer(transCustomerRecordFormat);
		}
		
		RecordFormatIfc transactionHeader = rtlogResults.getTransactionHeader();
		FieldFormatIfc transactionTypeField = transactionHeader.getFieldFormat("TransactionType");
		if ((transactionTypeField.getValue().equals("PVOID")) || (transactionTypeField.getValue().equals("NOSALE")) || (transactionTypeField.getValue().equals("VOID"))) 
		{
			transCustomerRecordFormat.setExportable(false);
		}
		
		return transCustomerRecordFormat;
	}
}