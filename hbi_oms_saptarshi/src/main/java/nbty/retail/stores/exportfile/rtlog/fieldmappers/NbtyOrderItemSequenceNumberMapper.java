/* 
------------------------------------------------------------------------------------------- FILE HISTORY ---------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     	DEEFECT ID/FSD MOD.             DESCRIPTION
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	13/10/2017  Ikshita      	HBI Everest    					For setting the type of field to custom made data type asis.      																			
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/
package nbty.retail.stores.exportfile.rtlog.fieldmappers;

import oracle.retail.stores.exportfile.ExportFileException;
import oracle.retail.stores.exportfile.formater.FieldFormatIfc;
import oracle.retail.stores.exportfile.formater.RecordFormatIfc;
import oracle.retail.stores.exportfile.mapper.ColumnMapIfc;
import oracle.retail.stores.exportfile.mapper.EntityMapperIfc;
import oracle.retail.stores.exportfile.mapper.FieldMapper;
import oracle.retail.stores.exportfile.mapper.FieldMapperIfc;
import oracle.retail.stores.exportfile.rtlog.RTLogRecordFormatConstantsIfc;
import oracle.retail.stores.persistence.utility.ARTSDatabaseIfc;
import oracle.retail.stores.xmlreplication.result.EntityIfc;
import oracle.retail.stores.xmlreplication.result.Row;
import org.apache.log4j.Logger;

public class NbtyOrderItemSequenceNumberMapper extends FieldMapper implements
		FieldMapperIfc, RTLogRecordFormatConstantsIfc, ARTSDatabaseIfc {
	protected static final Logger logger = Logger
			.getLogger(NbtyOrderItemSequenceNumberMapper.class);

	public int map(String columnValue, Row currentRow, ColumnMapIfc columnMap,
			FieldFormatIfc field, RecordFormatIfc record, EntityIfc entity,
			EntityMapperIfc entityMapper) throws ExportFileException {
		field.setPadNoValueWithSpaces(false);
		field.setValue(columnValue);
		field.setType(FieldFormatIfc.AS_IS);
		return 1;
	}
}