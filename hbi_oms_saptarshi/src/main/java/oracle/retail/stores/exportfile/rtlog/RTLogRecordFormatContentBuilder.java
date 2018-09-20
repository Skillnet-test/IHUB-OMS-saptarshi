/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/

/* 
------------------------------------------------------------------------------------------- FILE HISTORY --------------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     DEEFECT ID/FSD MOD.     DESCRIPTION
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	16/08/2017   Ikshita       HBI Everest             Aadded method addTransactionCustomerToContent and call for the same.
   2.     	11/10/2017   Ikshita       HBI Everest             Modified call of method addTransactionCustomerToContent.
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/


package oracle.retail.stores.exportfile.rtlog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import nbty.retail.stores.exportfile.rtlog.NbtyRTLogRecordFormatConstantsIfc;
import oracle.retail.stores.common.utility.Util;
import oracle.retail.stores.exportfile.ExportFileException;
import oracle.retail.stores.exportfile.formater.FieldFormatIfc;
import oracle.retail.stores.exportfile.formater.RecordFormatContentBuilderIfc;
import oracle.retail.stores.exportfile.formater.RecordFormatIfc;
import oracle.retail.stores.exportfile.mapper.MappingResultIfc;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class RTLogRecordFormatContentBuilder implements
		RecordFormatContentBuilderIfc, NbtyRTLogRecordFormatConstantsIfc {
	protected static final Logger logger = Logger
			.getLogger(RTLogRecordFormatContentBuilder.class);

	protected int permanentFileLineIdentifier = 1;

	protected int fileLineIdentifier = 0;

	protected int transactionRecordCounter = 0;

	protected int permanentFileRecordCounter = 0;

	protected int fileRecordCounter = 0;

	protected StringBuffer rtlog = null;

	protected boolean tenderExported = false;

	public RTLogRecordFormatContentBuilder() {
		this.rtlog = new StringBuffer();
	}

	public void addFileHeaderToContent(MappingResultIfc results,
			String version, String batchID) throws ExportFileException {
		RecordFormatIfc fileHeader = ((RTLogMappingResultIfc) results)
				.getFileHeader();

		if (fileHeader == null) {
			String message = "No transaction was found in the database.  Make sure the  the list transaction keys passed into the file generator are valid.";

			logger.error(message);
			throw new ExportFileException(message);
		}

		String refValue = version + ":BID " + batchID;
		FieldFormatIfc fFormat = fileHeader.getFieldFormat("ReferenceNumber");
		fFormat.setValue(refValue);
		addFileRecordToContent(fileHeader, 0);
	}

	public void addFileTailToContent(MappingResultIfc results)
			throws ExportFileException {
		RecordFormatIfc fileTail = ((RTLogMappingResultIfc) results)
				.getFileTail();
		addFileRecordToContent(fileTail, 7);
	}

	protected void addFileRecordToContent(RecordFormatIfc recordFormat,
			int index) throws ExportFileException {
		if (recordFormat == null) {
			String message = "Export file generation failed due to the fact there is no transaction in the database associated with the Store Number, Workstation, Business Day and Transaction ID you requested.";

			logger.error(message);
			throw new ExportFileException(message);
		}

		if (!(recordFormat.isExportable()))
			return;
		setCountsAndDate(recordFormat, index);
		this.rtlog.append(recordFormat.getFormatedRecord());
	}

	public void addTransactionToContent(MappingResultIfc results)
			throws ExportFileException {
		this.tenderExported = false;
		RTLogMappingResultIfc rlogResults = (RTLogMappingResultIfc) results;

		RecordFormatIfc thRecordFormat = rlogResults.getTransactionHeader();
		exportTransactionHeaderRecord(thRecordFormat);

		exportTransactionDataRecords(rlogResults, thRecordFormat);

		exportTransactionTailRecord(rlogResults);

		addTotalsListToContent(thRecordFormat);
	}

	protected void exportTransactionHeaderRecord(RecordFormatIfc thRecordFormat)
			throws ExportFileException {
		setCountsAndDate(thRecordFormat,
				thRecordFormat.getRecordFormatCatalogIndex());
		this.rtlog.append(thRecordFormat.getFormatedRecord());
	}

	protected void exportTransactionDataRecords(
			RTLogMappingResultIfc rlogResults, RecordFormatIfc thRecordFormat)
			throws ExportFileException {
		Collection itemRecords = rlogResults.getItemRecords();
		itemRecords = checkItemsForVoids(itemRecords);
		addListToContent(itemRecords.iterator());

		addTransactionFeeToContent(thRecordFormat);

		addListToContent(rlogResults.getTransactionShippingRecords().iterator());

		addListToContent(rlogResults.getTaxRecords().values().iterator());

		addTransactionPaymentToContent(thRecordFormat);

		addListToContent(rlogResults.getTenderRecords().iterator());
		
		addTransactionCustomerToContent(rlogResults.getTransactionCustomer());
		
		addOtherTransactionTender(rlogResults.getOtherTransactionTender());

		if ((this.tenderExported)
				|| (!(transactionRequiresTender(thRecordFormat.getFieldFormat(
						"TransactionType").getValue()))))
			return;
		addOtherTransactionTender(rlogResults.getDefaultTransactionTender());
	}

	protected void addTransactionCustomerToContent(RecordFormatIfc transactionCustomer) 
	{
		if(transactionCustomer != null && transactionCustomer.isExportable())
		{
			try 
			{
				String customerId = transactionCustomer.getFieldFormat(CUSTOMER_ID).getValue();
				if(StringUtils.isNotBlank(customerId))
				{
					setCountsAndDate(transactionCustomer,
							transactionCustomer.getRecordFormatCatalogIndex());
					this.rtlog.append(transactionCustomer.getFormatedRecord());
				}
			} 
			catch (ExportFileException e)
			{
				logger.info("Error While extracting Customer id from TransactionCustomer in class RTLogRecordFormatContentBuilder ");
			}
		}
	}

	protected void exportTransactionTailRecord(RTLogMappingResultIfc rlogResults)
			throws ExportFileException {
		RecordFormatIfc tRrecordFormat = rlogResults.getTransactionTail();
		setCountsAndDate(tRrecordFormat,
				tRrecordFormat.getRecordFormatCatalogIndex());
		this.rtlog.append(tRrecordFormat.getFormatedRecord());
	}

	protected void addTransactionFeeToContent(RecordFormatIfc thRecordFormat)
			throws ExportFileException {
		if (thRecordFormat.getContainedRecords() == null) {
			return;
		}
		RTLogTransactionContainedRecordsIfc tcRecords = (RTLogTransactionContainedRecordsIfc) thRecordFormat
				.getContainedRecords();

		if (tcRecords.getFeeRecord() != null) {
			setCountsAndDate(tcRecords.getFeeRecord(), tcRecords.getFeeRecord()
					.getRecordFormatCatalogIndex());
			this.rtlog.append(tcRecords.getFeeRecord().getFormatedRecord());
		}

		if (tcRecords.getLayawayDeleteFeeRecord() == null)
			return;
		setCountsAndDate(tcRecords.getLayawayDeleteFeeRecord(), tcRecords
				.getLayawayDeleteFeeRecord().getRecordFormatCatalogIndex());
		this.rtlog.append(tcRecords.getLayawayDeleteFeeRecord()
				.getFormatedRecord());
	}

	protected void addTransactionPaymentToContent(RecordFormatIfc thRecordFormat)
			throws ExportFileException {
		if (thRecordFormat.getContainedRecords() == null)
			return;
		RTLogTransactionContainedRecordsIfc tcRecords = (RTLogTransactionContainedRecordsIfc) thRecordFormat
				.getContainedRecords();

		if (tcRecords.getPaymentRecord() == null)
			return;
		setCountsAndDate(tcRecords.getPaymentRecord(), tcRecords
				.getPaymentRecord().getRecordFormatCatalogIndex());
		this.rtlog.append(tcRecords.getPaymentRecord().getFormatedRecord());
	}

	protected boolean transactionRequiresTender(String transactionType) {
		boolean requiresTender = false;

		if (("RETURN".equals(transactionType))
				|| ("SALE".equals(transactionType))
				|| ("PAIDIN".equals(transactionType))
				|| ("PAIDOU".equals(transactionType))
				|| ("PULL".equals(transactionType))
				|| ("LOAN".equals(transactionType))
				|| ("SPLORD".equals(transactionType))) {
			requiresTender = true;
		}
		return requiresTender;
	}

	protected void addOtherTransactionTender(RecordFormatIfc recordFormat)
			throws ExportFileException {
		if (recordFormat == null)
			return;
		setCountsAndDate(recordFormat,
				recordFormat.getRecordFormatCatalogIndex());
		this.rtlog.append(recordFormat.getFormatedRecord());
		if (!(recordFormat.isExportable()))
			return;
		this.tenderExported = true;
	}

	protected void addListToContent(Iterator<RecordFormatIfc> records)
			throws ExportFileException {
		while (records.hasNext()) {
			RecordFormatIfc recordFormat = (RecordFormatIfc) records.next();
			if (recordFormat.isExportable()) {
				setCountsAndDate(recordFormat,
						recordFormat.getRecordFormatCatalogIndex());
				this.rtlog.append(recordFormat.getFormatedRecord());
				if ((((recordFormat.getRecordFormatCatalogIndex() == 2) || (recordFormat
						.getRecordFormatCatalogIndex() == 8)))
						&& (recordFormat.getContainedRecords() != null)) {
					RTLogItemContainedRecordsIfc containedRecords = (RTLogItemContainedRecordsIfc) recordFormat
							.getContainedRecords();

					addListToContent(containedRecords.getDiscountRecords(0)
							.iterator());
					addListToContent(containedRecords.getDiscountRecords(1)
							.iterator());
					addListToContent(containedRecords.getDiscountRecords(2)
							.iterator());
					addListToContent(containedRecords.getItemTaxRecords()
							.iterator());

					if (containedRecords.getRestockingFeeRecord() != null) {
						setCountsAndDate(
								containedRecords.getRestockingFeeRecord(),
								containedRecords.getRestockingFeeRecord()
										.getRecordFormatCatalogIndex());

						this.rtlog.append(containedRecords
								.getRestockingFeeRecord().getFormatedRecord());
					}
				}

				if (recordFormat.getRecordFormatCatalogIndex() == 5) {
					this.tenderExported = true;
				}
			}
		}
	}

	protected void addTotalsListToContent(RecordFormatIfc thRecordFormat)
			throws ExportFileException {
		if (thRecordFormat.getContainedRecords() == null)
			return;
		RTLogTransactionContainedRecordsIfc tcRecords = (RTLogTransactionContainedRecordsIfc) thRecordFormat
				.getContainedRecords();

		if (tcRecords == null)
			return;
		Iterator headers = tcRecords.getTransactionHeaderTotalRecords()
				.iterator();
		Iterator tails = tcRecords.getTransactionTailTotalRecords().iterator();
		while (headers.hasNext()) {
			RecordFormatIfc headerFormat = (RecordFormatIfc) headers.next();
			RecordFormatIfc tailFormat = (RecordFormatIfc) tails.next();
			if (headerFormat.isExportable()) {
				FieldFormatIfc transactionID = headerFormat
						.getFieldFormat("TransactionNumber");
				FieldFormatIfc workstationID = headerFormat
						.getFieldFormat("WorkstationID");
				FieldFormatIfc headTransactionDate = headerFormat
						.getFieldFormat("RegisterTransactionDate");
				FieldFormatIfc thTransactionDate = thRecordFormat
						.getFieldFormat("RegisterTransactionDate");
				headTransactionDate.setValue(thTransactionDate.getValue());

				transactionID.setType(0);
				workstationID.setType(0);

				setCountsAndDate(headerFormat,
						headerFormat.getRecordFormatCatalogIndex());
				this.rtlog.append(headerFormat.getFormatedRecord());

				setCountsAndDate(tailFormat,
						tailFormat.getRecordFormatCatalogIndex());
				this.rtlog.append(tailFormat.getFormatedRecord());
			}
		}
	}

	protected void setCountsAndDate(RecordFormatIfc recordFormat,
			int formatIndex) {
		
		if (!(recordFormat.isExportable()))
			return;
		this.fileLineIdentifier += 1;

		this.fileRecordCounter += 1;
		this.transactionRecordCounter += 1;
		FieldFormatIfc fFormat;
		switch (formatIndex) {
		case 0:
			this.fileRecordCounter -= 1;
			this.transactionRecordCounter -= 1;
			setFileDate(recordFormat);
			break;
		case 1:
		case 10:
			this.fileRecordCounter = (this.permanentFileRecordCounter + 1);
			this.fileLineIdentifier = (this.permanentFileLineIdentifier + 1);
			this.transactionRecordCounter = 0;
			break;
		case 6:
			this.transactionRecordCounter -= 1;
			fFormat = recordFormat.getFieldFormat(2);
			fFormat.setValue(Integer.toString(this.transactionRecordCounter));

			this.permanentFileRecordCounter = this.fileRecordCounter;
			this.permanentFileLineIdentifier = this.fileLineIdentifier;

			break;
		case 7:
			this.fileRecordCounter -= 1;
			fFormat = recordFormat.getFieldFormat(2);
			fFormat.setValue(Integer.toString(this.fileRecordCounter));

			this.fileRecordCounter = (this.permanentFileRecordCounter + 1);
			this.fileLineIdentifier = (this.permanentFileLineIdentifier + 1);
		case 2:
		case 3:
		case 4:
		case 5:
		case 8:
		case 9:
		}

		fFormat = recordFormat.getFieldFormat(1);
		fFormat.setValue(Integer.toString(this.fileLineIdentifier));
	}

	protected void setFileDate(RecordFormatIfc recordFormat) {
		FieldFormatIfc fFormat = recordFormat.getFieldFormat(3);
		Calendar cal = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
		String value = sdf.format(cal.getTime());
		fFormat.setValue(value);
	}

	/** @deprecated */
	public String getContent() {
		String log = this.rtlog.toString();
		clearContent();
		return log;
	}

	public char[] getContentChars() {
		char[] log = new char[this.rtlog.length()];
		this.rtlog.getChars(0, this.rtlog.length(), log, 0);
		clearContent();
		return log;
	}

	public void clearContent() {
		Util.flushStringBuffer(this.rtlog);

		this.rtlog = new StringBuffer();
	}

	protected Collection<RecordFormatIfc> checkItemsForVoids(
			Collection<RecordFormatIfc> itemList) throws ExportFileException {
		ArrayList newItemList = new ArrayList();

		for (RecordFormatIfc recordFormat : itemList) {
			if ((recordFormat.isExportable()) && (recordFormat.isVoided())) {
				FieldFormatIfc itemStatus = recordFormat
						.getFieldFormat("ItemStatus");
				if ((itemStatus.getValue().equals("ORI"))
						|| (itemStatus.getValue().equals("ORD"))
						|| (itemStatus.getValue().equals("LIN"))) {
					itemStatus.setValue("S");
				}

				newItemList.add(recordFormat);
				RecordFormatIfc voided = generateVoidedRecordFormat(recordFormat);

				newItemList.add(voided);
			}

		}

		for (RecordFormatIfc recordFormat : itemList) {
			if ((recordFormat.isExportable()) && (!(recordFormat.isVoided()))) {
				newItemList.add(recordFormat);
			}
		}

		return newItemList;
	}

	protected RecordFormatIfc generateVoidedRecordFormat(
			RecordFormatIfc recordFormat) throws ExportFileException {
		RecordFormatIfc voided = (RecordFormatIfc) recordFormat.clone();
		FieldFormatIfc format = voided.getFieldFormat("ItemStatus");
		format.setValue("V");
		reverseQuantity(voided);

		RTLogItemContainedRecordsIfc containedRecords = (RTLogItemContainedRecordsIfc) voided
				.getContainedRecords();
		reverseDiscountQuantity(containedRecords.getDiscountRecords(0));
		reverseDiscountQuantity(containedRecords.getDiscountRecords(1));
		reverseDiscountQuantity(containedRecords.getDiscountRecords(2));
		if (containedRecords.getRestockingFeeRecord() != null) {
			format = containedRecords.getRestockingFeeRecord().getFieldFormat(
					"ItemStatus");
			format.setValue("V");
			reverseQuantity(containedRecords.getRestockingFeeRecord());
		}

		return voided;
	}

	private void reverseQuantity(RecordFormatIfc recordFormat)
			throws ExportFileException {
		FieldFormatIfc format = recordFormat.getFieldFormat("QuantitySign");
		if ("P".equals(format.getValue())) {
			format.setValue("N");
		} else {
			format.setValue("P");
		}
	}

	private void reverseDiscountQuantity(
			ArrayList<RecordFormatIfc> discountRecords)
			throws ExportFileException {
		for (int i = 0; i < discountRecords.size(); ++i) {
			RecordFormatIfc recordFormat = (RecordFormatIfc) discountRecords
					.get(i);
			reverseQuantity(recordFormat);
		}
	}

	public String getContentString() {
		throw new UnsupportedOperationException(
				"It is not legal to call this method for RTLog export");
	}
}