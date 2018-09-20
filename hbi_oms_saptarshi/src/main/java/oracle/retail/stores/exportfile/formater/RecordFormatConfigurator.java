/* 
------------------------------------------------------------------------------------------- FILE HISTORY ---------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     	DEEFECT ID/FSD MOD.             DESCRIPTION
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	13/10/2017  Ikshita      	HBI Everest    					New type set done for VALUE_AS_IS.      																			
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/
package oracle.retail.stores.exportfile.formater;

import java.io.InputStream;
import oracle.retail.stores.common.utility.Util;
import oracle.retail.stores.exportfile.AbstractConfigurator;
import oracle.retail.stores.exportfile.ExportFileException;
import oracle.retail.stores.exportfile.MappingObjectFactoryContainer;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RecordFormatConfigurator extends AbstractConfigurator implements
		RecordFormatConfiguratorIfc {
	protected static final Logger logger = Logger
			.getLogger(RecordFormatConfigurator.class);

	public RecordFormatConfigurator() {
		this.configuratorName = "RecordFormatConfigurator";
	}

	public RecordFormatCatalogIfc configureRecordFormat(InputStream stream,
			String configFileName) throws ExportFileException {
		this.configFileName = configFileName;

		RecordFormatCatalogIfc catalog = MappingObjectFactoryContainer
				.getInstance().getFormatObjectFactory()
				.getRecordFormatCatalogInstance();

		Document cfgDoc = getDocument(stream);
		Element root = cfgDoc.getDocumentElement();

		verifyTag(root, TAG_RECORD_FORMATS);

		Element[] children = getChildElements(root);
		String tagName = null;
		int versionIndex = 0;

		for (int i = 0; i < children.length; ++i) {
			tagName = children[i].getTagName();
			if ((i == 0) && (tagName.equals(TAG_COMMENT))) {
				versionIndex = 1;
			} else if (i == versionIndex) {
				configureVersion(children[i], catalog);
			} else {
				configureRecord(children[i], catalog);
			}
		}

		return catalog;
	}

	protected void configureVersion(Element element,
			RecordFormatCatalogIfc catalog) throws ExportFileException {
		verifyTag(element, TAG_RECORD_FORMAT_VERSION);

		catalog.setFormatVersion(getRequiredAttribute(element, ATTR_VERSION));
	}

	protected void configureRecord(Element record,
			RecordFormatCatalogIfc catalog) throws ExportFileException {
		verifyTag(record, TAG_RECORD_FORMAT);

		RecordFormatIfc rFormat = catalog.addFormat(getRequiredAttribute(
				record, ATTR_NAME));

		Element[] children = getChildElements(record);
		for (int i = 0; i < children.length; ++i) {
			configureField(children[i], rFormat);
		}
	}

	protected void configureField(Element fieldElement, RecordFormatIfc rFormat)
			throws ExportFileException {
		verifyTag(fieldElement, TAG_FIELD_FORMAT);

		FieldFormatIfc fFormat = MappingObjectFactoryContainer.getInstance()
				.getFormatObjectFactory().getFieldFormatInstance();

		fFormat.setName(getRequiredAttribute(fieldElement, ATTR_NAME));
		validateAndSetType(fFormat, getRequiredAttribute(fieldElement, ATTR_TYPE));

		if (fFormat.getLength() == -1) {
			String length = getRequiredAttribute(fieldElement, ATTR_LENGTH);
			if (fFormat.getType() == 3) {
				validateAndSetDecimalLength(fFormat, length);
			} else {
				validateAndSetLength(fFormat, length);
			}

		}

		String attr = fieldElement.getAttribute(ATTR_VALUE);
		if (attr.length() > 0) {
			fFormat.setValue(attr);
		}

		attr = fieldElement.getAttribute(ATTR_PAD_NO_VALUE_WITH_SPACES);
		if (attr.length() > 0) {
			fFormat.setPadNoValueWithSpaces(Boolean.valueOf(attr)
					.booleanValue());
		}

		attr = fieldElement.getAttribute(LEFT_JUSTIFY_VALUE_WITH_SPACES);
		if (attr.length() > 0) {
			fFormat.setLeftJustifyValueWithSpaces(Boolean.valueOf(attr)
					.booleanValue());
		}

		rFormat.addField(fFormat);
	}

	protected void validateAndSetType(FieldFormatIfc format, String type)
			throws ExportFileException {
		if (type.equals(VALUE_CHAR)) {
			format.setType(0);
		} else if (type.equals(VALUE_INTEGER)) {
			format.setType(4);
		} else if (type.equals(VALUE_BYTE)) {
			format.setType(5);
		} else if (type.equals(VALUE_IDECIMAL)) {
			format.setType(3);
		} else if (type.equals(VALUE_DATE)) {
			format.setType(1);
		} else if (type.equals(VALUE_DATETIME)) {
			format.setType(2);
		}else if (type.equals(VALUE_AS_IS)) {
			format.setType(6);
		} else {
			String message = "Error in "
					+ this.configFileName
					+ " for RecordFormatConfigurator; "
					+ "The 'type' attribute for field '"
					+ format.getName()
					+ "' is not valid.  "
					+ Util.EOL
					+ "      Valid types are char, integer, idecimal, ,date, datetime, asis.  "
					+ "The 'type' attribute value is '" + type + "'.";

			logger.error(message);
			throw new ExportFileException(message);
		}
	}

	protected void validateAndSetLength(FieldFormatIfc format, String length)
			throws ExportFileException {
		int len = -1;
		try {
			len = Integer.parseInt(length);
		} catch (NumberFormatException nfe) {
			String message = "Error in " + this.configFileName
					+ " for RecordFormatConfigurator; "
					+ "The 'length' attribute for field '" + format.getName()
					+ "' is not numeric.  " + Util.EOL
					+ "      The 'length' attribute value is '" + length + "'.";

			logger.error(message);
			throw new ExportFileException(message);
		}

		format.setLength(len);
	}

	protected void validateAndSetDecimalLength(FieldFormatIfc format,
			String length) throws ExportFileException {
		int decimalPos = length.indexOf(46);
		if (decimalPos == -1) {
			String message = "Error in " + this.configFileName
					+ " for RecordFormatConfigurator; "
					+ "The 'length' attribute for decimal field '"
					+ format.getName() + "' must contain a decimal point.  "
					+ Util.EOL + "      The 'length' attribute value is '"
					+ length + "'.";

			logger.error(message);
			throw new ExportFileException(message);
		}

		if (decimalPos == 0) {
			String message = "Error in " + this.configFileName
					+ " for RecordFormatConfigurator; "
					+ "The 'length' attribute for decimal field '"
					+ format.getName() + "' must have a whole number length.  "
					+ Util.EOL + "      The 'length' attribute value is '"
					+ length + "'.";

			logger.error(message);
			throw new ExportFileException(message);
		}

		String whole = length.substring(0, decimalPos);
		String dec = length.substring(decimalPos + 1);

		int len = -1;
		try {
			len = Integer.parseInt(whole);
		} catch (NumberFormatException nfe) {
			String message = "Error in " + this.configFileName
					+ " for RecordFormatConfigurator; "
					+ "The 'length' attribute for decimal field '"
					+ format.getName() + "' is not numeric.  " + Util.EOL
					+ "      The 'length' attribute value is '" + length + "'.";

			logger.error(message);
			throw new ExportFileException(message);
		}
		format.setLength(len);
		try {
			len = Integer.parseInt(dec);
		} catch (NumberFormatException nfe) {
			String message = "Error in " + this.configFileName
					+ " for RecordFormatConfigurator; "
					+ "The 'length' attribute for decimal field '"
					+ format.getName() + "' is not numeric.  " + Util.EOL
					+ "      The 'length' attribute value is '" + length + "'.";

			logger.error(message);
			throw new ExportFileException(message);
		}
		format.setDecimalLength(len);
	}
}