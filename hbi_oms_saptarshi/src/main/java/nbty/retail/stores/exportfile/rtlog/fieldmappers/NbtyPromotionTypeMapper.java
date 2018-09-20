/* 
------------------------------------------------------------------------------------------- FILE HISTORY ---------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     DEEFECT ID/FSD MOD.             DESCRIPTION
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     12/7/2017      Jigar N      HBI Everest            	Removed isStoreCoupon() since this was referring CD_BAS_PRDV which was removed from some table in 13.4.8																		
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/

package nbty.retail.stores.exportfile.rtlog.fieldmappers;

import oracle.retail.stores.common.utility.Util;
import oracle.retail.stores.exportfile.ExportFileException;
import oracle.retail.stores.exportfile.formater.FieldFormatIfc;
import oracle.retail.stores.exportfile.formater.RecordFormatIfc;
import oracle.retail.stores.exportfile.mapper.ColumnMapIfc;
import oracle.retail.stores.exportfile.mapper.EntityMapperIfc;
import oracle.retail.stores.exportfile.mapper.FieldMapperIfc;
import oracle.retail.stores.exportfile.rtlog.RTLogRecordFormatConstantsIfc;
import oracle.retail.stores.exportfile.rtlog.fieldmappers.PromotionTypeMapper;
import oracle.retail.stores.persistence.utility.ARTSDatabaseIfc;
import oracle.retail.stores.xmlreplication.result.EntityIfc;
import oracle.retail.stores.xmlreplication.result.Row;

public class NbtyPromotionTypeMapper extends PromotionTypeMapper 
    implements FieldMapperIfc, RTLogRecordFormatConstantsIfc, ARTSDatabaseIfc
{
 
    public int map(String columnValue, Row row, ColumnMapIfc columnMap, FieldFormatIfc field, RecordFormatIfc record, EntityIfc entity, EntityMapperIfc entityMapper)
        throws ExportFileException
    {
        String promotionType = "";
        FieldFormatIfc discountReferenceNumber = record.getFieldFormat(DISCOUNT_REFERENCE_NUMBER_FIELD);
        if(discountReferenceNumber.getValue().equals("0") || Util.isEmpty(discountReferenceNumber.getValue()))
            promotionType = IN_STORE_PROMOTION_TYPE;
        else
            promotionType = TEMPORARY_PRICE_CHANGE_PROMOTION_TYPE;
        field.setValue(promotionType);
        return 1;
    }
}