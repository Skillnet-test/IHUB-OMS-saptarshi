/* 
------------------------------------------------------------------------------------------- FILE HISTORY --------------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     DEEFECT ID/FSD MOD.             DESCRIPTION
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     13/7/2017      Jigar N      HBI Everest            To Set Item SalesType as E if Order In Store Item else R.
   2.     21/7/2017      Ikshita      HBI Everest            To Set OIS Flag Field
   3.     16/8/2017      Ikshita      HBI Everest            Deleted set flag for OIS Flag Field.
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/

package nbty.retail.stores.exportfile.rtlog.fieldmappers;

import nbty.retail.stores.exportfile.rtlog.NbtyRTLogRecordFormatConstantsIfc;
import oracle.retail.stores.exportfile.ExportFileException;
import oracle.retail.stores.exportfile.formater.FieldFormatIfc;
import oracle.retail.stores.exportfile.formater.RecordFormatIfc;
import oracle.retail.stores.exportfile.mapper.ColumnMapIfc;
import oracle.retail.stores.exportfile.mapper.EntityMapperIfc;
import oracle.retail.stores.exportfile.mapper.FieldMapper;
import oracle.retail.stores.exportfile.mapper.FieldMapperIfc;
import oracle.retail.stores.persistence.utility.ARTSDatabaseIfc;
import oracle.retail.stores.xmlreplication.result.EntityIfc;
import oracle.retail.stores.xmlreplication.result.Row;
import oracle.retail.stores.xmlreplication.result.TableIfc;

public class NbtyItemSaleTypeMapper extends FieldMapper implements FieldMapperIfc, NbtyRTLogRecordFormatConstantsIfc,ARTSDatabaseIfc 
{
	public int map(String columnValue, Row row, ColumnMapIfc columnMap, FieldFormatIfc field, RecordFormatIfc record,
			EntityIfc entity, EntityMapperIfc entityMapper) throws ExportFileException 
	{
		field.setValue(columnValue);
		FieldFormatIfc itemSalesTypeField = record.getFieldFormat(ITEM_SALES_TYPE_FIELD);
		TableIfc table = entity.getTable(TABLE_ORDER_LINE_ITEM_STATUS);
		String transctionSequenceNumber;
		try 
		{
			transctionSequenceNumber = row.getFieldValueAsString(FIELD_TRANSACTION_SEQUENCE_NUMBER);

			Row[] rows = findRowWhere(table, FIELD_TRANSACTION_SEQUENCE_NUMBER, transctionSequenceNumber);
		
			if (rows.length > 0) 
			{
				itemSalesTypeField.setValue(ITEM_SALES_TYPE_E);
			}
			else
			{
				itemSalesTypeField.setValue(ITEM_SALES_TYPE_R);
			}
		}
		catch (Exception e) 
		{
			logger.info("Unable to map the Item SalesType in NbtyItemSaleTypeMapper class" + e.getStackTrace());
		}
		return 1;
	}
}