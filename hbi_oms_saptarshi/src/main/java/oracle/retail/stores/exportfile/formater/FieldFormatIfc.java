/* 
------------------------------------------------------------------------------------------- FILE HISTORY ---------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     	DEEFECT ID/FSD MOD.             DESCRIPTION
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	13/10/2017  Ikshita      	HBI Everest    					New type variable added AS_IS.      																			
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/
package oracle.retail.stores.exportfile.formater;

import oracle.retail.stores.exportfile.ExportFileException;

public abstract interface FieldFormatIfc {
	public static final int CHAR = 0;
	public static final int DATE = 1;
	public static final int DATETIME = 2;
	public static final int IMPLIED_DECIMAL = 3;
	public static final int INTEGER = 4;
	public static final int BYTE = 5;
	public static final int AS_IS = 6;

	public abstract StringBuffer getFomattedValue() throws ExportFileException;

	public abstract StringBuffer getFormattedByteValue()
			throws ExportFileException;

	public abstract int getDecimalLength();

	public abstract void setDecimalLength(int paramInt);

	public abstract int getLength();

	public abstract void setLength(int paramInt);

	public abstract String getName();

	public abstract void setName(String paramString) throws ExportFileException;

	public abstract int getType();

	public abstract void setType(int paramInt);

	public abstract String getValue();

	public abstract void setValue(String paramString);

	public abstract byte[] getByteValue();

	public abstract void setByteValue(byte[] paramArrayOfByte);

	public abstract int getStartIndex();

	public abstract void setStartIndex(int paramInt);

	public abstract int getRecordFormatIndex();

	public abstract boolean isPadNoValueWithSpaces();

	public abstract void setPadNoValueWithSpaces(boolean paramBoolean);

	public abstract boolean isLeftJustifyValueWithSpaces();

	public abstract void setLeftJustifyValueWithSpaces(boolean paramBoolean);

	public abstract void setRecordFormatIndex(int paramInt);

	public abstract void makeDeepCopyOfTemplate(
			FieldFormatIfc paramFieldFormatIfc);

	public abstract String trimLeadingZeros(String paramString)
			throws ExportFileException;

	public abstract String trimTrailingZeros(String paramString)
			throws ExportFileException;

	public abstract void checkForNumeric(String paramString)
			throws ExportFileException;

	public abstract String postpendCharacter(String paramString,
			char paramChar, int paramInt);

	public abstract String prependCharacter(String paramString, char paramChar);

	public abstract Object clone();
}