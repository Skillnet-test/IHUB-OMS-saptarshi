/* 
------------------------------------------------------------------------------------------- FILE HISTORY ---------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     	DEEFECT ID/FSD MOD.             DESCRIPTION
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	13/10/2017  Ikshita      	HBI Everest    					New type variable added VALUE_AS_IS.      																			
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/
package oracle.retail.stores.exportfile.formater;

import java.io.InputStream;
import oracle.retail.stores.exportfile.ExportFileException;

public abstract interface RecordFormatConfiguratorIfc {
	public static final String TAG_RECORD_FORMATS = "RECORD_FORMATS";
	public static final String TAG_COMMENT = "COMMENT";
	public static final String TAG_RECORD_FORMAT = "RECORD_FORMAT";
	public static final String TAG_RECORD_FORMAT_VERSION = "RECORD_FORMAT_VERSION";
	public static final String ATTR_VERSION = "version";
	public static final String ATTR_NAME = "name";
	public static final String TAG_FIELD_FORMAT = "FIELD_FORMAT";
	public static final String ATTR_TYPE = "type";
	public static final String ATTR_LENGTH = "length";
	public static final String ATTR_VALUE = "value";
	public static final String VALUE_CHAR = "char";
	public static final String VALUE_INTEGER = "integer";
	public static final String VALUE_BYTE = "byte";
	public static final String VALUE_IDECIMAL = "idecimal";
	public static final String VALUE_DATE = "date";
	public static final String VALUE_DATETIME = "datetime";
	public static final String ATTR_PAD_NO_VALUE_WITH_SPACES = "padNoValueWithSpaces";
	public static final String LEFT_JUSTIFY_VALUE_WITH_SPACES = "leftJustifyValueWithSpaces";
	
	public static final String VALUE_AS_IS = "asis";

	public abstract RecordFormatCatalogIfc configureRecordFormat(
			InputStream paramInputStream, String paramString)
			throws ExportFileException;
}