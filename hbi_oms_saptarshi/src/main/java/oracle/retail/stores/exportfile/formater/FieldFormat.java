/* 
------------------------------------------------------------------------------------------- FILE HISTORY ---------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     	DEEFECT ID/FSD MOD.             DESCRIPTION
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	13/10/2017  Ikshita      	HBI Everest    					New case added for AS_IS type.      																			
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/
package oracle.retail.stores.exportfile.formater;

import oracle.retail.stores.common.utility.Util;
import oracle.retail.stores.exportfile.ExportFileException;
import org.apache.log4j.Logger;

public class FieldFormat implements FieldFormatIfc {
	protected static final Logger logger = Logger.getLogger(FieldFormat.class);

	protected String name = null;

	protected String value = "";

	protected byte[] byteValue = new byte[0];

	protected int type = -1;

	protected int length = -1;

	protected int decimalLength = 0;

	protected boolean padNoValueWithSpaces = false;

	protected boolean leftJustifyValueWithSpaces = false;

	protected int startIndex = -1;

	protected int recordFormatIndex = -1;

	public StringBuffer getFomattedValue() throws ExportFileException {
		StringBuffer formatted = new StringBuffer(this.value);
		switch (this.type) {
		case 0:
			formatted = new StringBuffer(getCharFomattedValue());
			break;
		case 1:
			formatted = new StringBuffer(getDateFomattedValue());
			break;
		case 2:
			formatted = new StringBuffer(getDateTimeFomattedValue());
			break;
		case 3:
			formatted = new StringBuffer(getImpliedDecimalFomatedValue());
			break;
		case 4:
			formatted = new StringBuffer(getIntegerFomattedValue());
			break;
		case 5:
			formatted = getFormattedByteValue();
			break;
		case 6:
			formatted = new StringBuffer(getAsIsFomattedValue());
		}

		return formatted;
	}

	public StringBuffer getFormattedByteValue() throws ExportFileException {
		StringBuffer formattedReturnValue = new StringBuffer(this.length);

		if (this.byteValue.length > this.length) {
			String message = "FieldFormat object " + this.name
					+ " contains a value "
					+ " which is longer than the expected length of "
					+ this.length + ".";

			logger.error(message);
			throw new ExportFileException(message);
		}

		int index = 0;

		for (; index < this.byteValue.length; ++index) {
			formattedReturnValue.append((char) this.byteValue[index]);
		}

		for (; index < this.length; ++index) {
			formattedReturnValue.append(' ');
		}
		Util.flushByteArray(this.byteValue);
		this.byteValue = null;
		return formattedReturnValue;
	}

	protected String getCharFomattedValue() throws ExportFileException {
		String formatted = this.value.trim();

		if (formatted.length() > this.length) {
			String message = "FieldFormat object " + this.name
					+ " contains the value " + formatted
					+ " which is longer than the expected length of "
					+ this.length + ".";

			logger.error(message);
			throw new ExportFileException(message);
		}

		if (formatted.length() < this.length) {
			formatted = postpendCharacter(formatted, ' ', this.length);
		}
		return formatted;
	}

	protected String getIntegerFomattedValue() throws ExportFileException {
		return getIntegerFomattedValue(trimLeadingZeros(this.value),
				this.length);
	}
	
	protected String getAsIsFomattedValue() throws ExportFileException {
		return getIntegerFomattedValue(this.value,
				this.length);
	}

	protected String getImpliedDecimalFomatedValue() throws ExportFileException {
		String wholeNumber = trimLeadingZeros(getWholeNumber());
		if (wholeNumber.length() > this.length) {
			String message = "The implied decimal FieldFormat object "
					+ this.name + " contains the whole number value "
					+ wholeNumber
					+ " which is longer than the expected length of "
					+ this.length + ".";

			logger.error(message);
			throw new ExportFileException(message);
		}

		int wholeNumberLength = wholeNumber.length();

		String decimalPart = trimTrailingZeros(getDecimalPart());
		checkForNumeric(decimalPart);
		if (decimalPart.length() > this.decimalLength) {
			String message = "The implied decimal FieldFormat object "
					+ this.name + " contains the decimal value " + decimalPart
					+ " which is longer than the expected length of "
					+ this.decimalLength + ".";

			logger.error(message);
			throw new ExportFileException(message);
		}

		int decimalPartLength = decimalPart.length();

		if ((this.padNoValueWithSpaces) && (wholeNumberLength == 0)
				&& (decimalPartLength == 0)) {
			wholeNumber = prependCharacter(wholeNumber, ' ');
			decimalPart = postpendCharacter(decimalPart, ' ',
					this.decimalLength);
		} else {
			if (wholeNumberLength < this.length) {
				wholeNumber = prependCharacter(wholeNumber, '0');
			}
			if (decimalPartLength < this.length) {
				decimalPart = postpendCharacter(decimalPart, '0',
						this.decimalLength);
			}
		}

		return wholeNumber + decimalPart;
	}

	protected String getDateTimeFomattedValue() throws ExportFileException {
		return getCharFomattedValue();
	}

	protected String getDateFomattedValue() throws ExportFileException {
		return getCharFomattedValue();
	}

	public String trimLeadingZeros(String stringValue)
			throws ExportFileException {
		String formatted = stringValue.trim();
		checkForNumeric(formatted);
		while ((formatted.length() > 0) && (formatted.charAt(0) == '0')) {
			formatted = formatted.substring(1);
		}
		return formatted;
	}

	public String trimTrailingZeros(String stringValue)
			throws ExportFileException {
		String formatted = stringValue.trim();
		checkForNumeric(formatted);
		while ((formatted.length() > 0)
				&& (formatted.charAt(formatted.length() - 1) == '0')) {
			formatted = formatted.substring(0, formatted.length() - 1);
		}
		return formatted;
	}

	public void checkForNumeric(String formatted) throws ExportFileException {
		if (formatted.length() <= 0)
			return;
		for (int i = 0; i < formatted.length(); ++i) {
			if (Character.isDigit(formatted.charAt(i)))
				continue;
			String message = "Numeric FieldFormat object '" + this.name
					+ "' contains the non-numeric value " + formatted + ".";

			logger.error(message);
			throw new ExportFileException(message);
		}
	}

	protected String getIntegerFomattedValue(String value, int len)
			throws ExportFileException {
		String formatted = value;

		if (formatted.length() > len) {
			String message = "FieldFormat object " + this.name
					+ " contains the value " + formatted
					+ " which is longer than the expected length of " + len
					+ ".";

			logger.error(message);
			throw new ExportFileException(message);
		}

		if ((formatted.length() == 0)
				&& (((this.padNoValueWithSpaces) || (this.leftJustifyValueWithSpaces)))) {
			formatted = prependCharacter(formatted, ' ');
		}
		if ((formatted.length() > 0) && (this.leftJustifyValueWithSpaces)) {
			formatted = postpendCharacter(formatted, ' ', len);
		} else if (formatted.length() < len) {
			formatted = prependCharacter(formatted, '0');
		}

		return formatted;
	}

	protected String getWholeNumber() throws ExportFileException {
		int index = this.value.indexOf(".");
		String wholePart;
		//String wholePart;
		if (index == -1) {
			wholePart = this.value.trim();
		} else {
			wholePart = this.value.substring(0, index).trim();
		}

		checkForNumeric(wholePart);
		return wholePart;
	}

	protected String getDecimalPart() throws ExportFileException {
		int index = this.value.indexOf(".");
		String decimalPart;
		//String decimalPart;
		if (index == -1) {
			decimalPart = "0";
		} else {
			decimalPart = this.value.substring(index + 1).trim();
		}

		return decimalPart;
	}

	public String postpendCharacter(String formatted, char post, int neededLen) {
		int addChar = neededLen - formatted.length();
		StringBuffer buff = new StringBuffer(neededLen);
		buff.append(formatted);
		for (int i = 0; i < addChar; ++i) {
			buff.append(post);
		}
		return buff.toString();
	}

	public String prependCharacter(String formatted, char pre) {
		int addChar = this.length - formatted.length();
		StringBuffer buff = new StringBuffer(this.length);
		for (int i = 0; i < addChar; ++i) {
			buff.append(pre);
		}
		return buff.append(formatted).toString();
	}

	public int getDecimalLength() {
		return this.decimalLength;
	}

	public void setDecimalLength(int decimalLength) {
		this.decimalLength = decimalLength;
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) throws ExportFileException {
		this.name = name;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
		if (type == 1) {
			this.length = 8;
		}
		if (type != 2)
			return;
		this.length = 14;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		if (getType() == 5) {
			if (value == null)
				return;
			setByteValue(value.getBytes());
		} else {
			this.value = value;
		}
	}

	public byte[] getByteValue() {
		return this.byteValue;
	}

	public void setByteValue(byte[] byteValue) {
		this.byteValue = byteValue;
	}

	public boolean isPadNoValueWithSpaces() {
		return this.padNoValueWithSpaces;
	}

	public void setPadNoValueWithSpaces(boolean padNoValueWithSpaces) {
		this.padNoValueWithSpaces = padNoValueWithSpaces;
	}

	public boolean isLeftJustifyValueWithSpaces() {
		return this.leftJustifyValueWithSpaces;
	}

	public void setLeftJustifyValueWithSpaces(boolean leftJustifyValueWithSpaces) {
		this.leftJustifyValueWithSpaces = leftJustifyValueWithSpaces;
	}

	public int getStartIndex() {
		return this.startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public void makeDeepCopyOfTemplate(FieldFormatIfc format) {
		try {
			format.setName(this.name);
		} catch (ExportFileException e) {
		}

		format.setValue(this.value);
		format.setType(this.type);
		format.setLength(this.length);
		format.setDecimalLength(this.decimalLength);
		format.setStartIndex(this.startIndex);
		format.setRecordFormatIndex(this.recordFormatIndex);
		format.setPadNoValueWithSpaces(this.padNoValueWithSpaces);
		format.setLeftJustifyValueWithSpaces(this.leftJustifyValueWithSpaces);
	}

	public int getRecordFormatIndex() {
		return this.recordFormatIndex;
	}

	public void setRecordFormatIndex(int recordFormatIndex) {
		this.recordFormatIndex = recordFormatIndex;
	}

	public Object clone() {
		FieldFormat c = new FieldFormat();

		setCloneAttributes(c);

		return c;
	}

	protected void setCloneAttributes(FieldFormat c) {
		try {
			c.setName(this.name);
		} catch (ExportFileException e) {
		}

		c.setValue(this.value);
		c.setType(this.type);
		c.setLength(this.length);
		c.setDecimalLength(this.decimalLength);
		c.setPadNoValueWithSpaces(this.padNoValueWithSpaces);
		c.setLeftJustifyValueWithSpaces(this.leftJustifyValueWithSpaces);
		c.setStartIndex(this.startIndex);
		c.setRecordFormatIndex(this.recordFormatIndex);

		byte[] bv = new byte[this.byteValue.length];
		for (int i = 0; i < this.byteValue.length; ++i) {
			bv[i] = this.byteValue[i];
		}
		c.setByteValue(bv);
	}
}