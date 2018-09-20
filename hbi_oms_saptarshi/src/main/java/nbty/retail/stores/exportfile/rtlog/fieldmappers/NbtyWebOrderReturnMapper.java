/* 
------------------------------------------------------------------------------------------- FILE HISTORY ---------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     	DEEFECT ID/FSD MOD.             DESCRIPTION
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	20/7/2017   Ikshita      	HBI Everest    					For setting the value to null if its zero.        																			
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/

package nbty.retail.stores.exportfile.rtlog.fieldmappers;

import nbty.retail.stores.exportfile.rtlog.NbtyRTLogRecordFormatConstantsIfc;
import nbty.retail.stores.persistence.utility.NbtyARTSDatabaseIfc;
import oracle.retail.stores.exportfile.ExportFileException;
import oracle.retail.stores.exportfile.formater.FieldFormatIfc;
import oracle.retail.stores.exportfile.formater.RecordFormatIfc;
import oracle.retail.stores.exportfile.mapper.ColumnMapIfc;
import oracle.retail.stores.exportfile.mapper.EntityMapperIfc;
import oracle.retail.stores.exportfile.mapper.FieldMapper;
import oracle.retail.stores.exportfile.mapper.FieldMapperIfc;
import oracle.retail.stores.xmlreplication.result.EntityIfc;
import oracle.retail.stores.xmlreplication.result.Row;

public class NbtyWebOrderReturnMapper extends FieldMapper implements FieldMapperIfc,NbtyRTLogRecordFormatConstantsIfc,NbtyARTSDatabaseIfc
{

	public static final String VALUE_ZERO = "0";
	public static final String SET_VALUE_NULL = "";
    public int map(String columnValue, Row row, ColumnMapIfc columnMap, FieldFormatIfc field, RecordFormatIfc record, EntityIfc entity, EntityMapperIfc entityMapper)
        throws ExportFileException
    {
    	FieldFormatIfc customerOrderLineNumberField = record.getFieldFormat(CUSTOMER_ORDER_LINE_NUMBER_FIELD);
    	String webOrderLineItemSequenceNumber;
		try 
		{
			webOrderLineItemSequenceNumber = row.getFieldValueAsString(FIELD_WEB_ORDER_LINE_ITEM_SEQ_NO);
			if(webOrderLineItemSequenceNumber.equals(VALUE_ZERO))
				customerOrderLineNumberField.setValue(SET_VALUE_NULL);
	        else
	        	customerOrderLineNumberField.setValue(webOrderLineItemSequenceNumber);
	
		 if(columnValue.equals(VALUE_ZERO))
	        	field.setValue(SET_VALUE_NULL);
	        else
	        	field.setValue(columnValue);
		}  
		catch(Exception e)
		{
			logger.info("Unable to map the web order line item sequence number in NbtyWebOrderReturnMapper class" + e.getStackTrace());
		}
		 return 1;
    }
}