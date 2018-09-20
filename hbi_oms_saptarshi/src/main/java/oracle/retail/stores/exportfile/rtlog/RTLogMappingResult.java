/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/

/* 
------------------------------------------------------------------------------------------- FILE HISTORY --------------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     DEEFECT ID/FSD MOD.     DESCRIPTION
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	16/8/2017   Ikshita       HBI Everest             Aadded transactionCustomer and its getter,setter.
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/

package oracle.retail.stores.exportfile.rtlog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import oracle.retail.stores.exportfile.ExportFileException;
import oracle.retail.stores.exportfile.MappingObjectFactoryContainer;
import oracle.retail.stores.exportfile.formater.FieldFormatIfc;
import oracle.retail.stores.exportfile.formater.RecordFormatIfc;
import oracle.retail.stores.exportfile.mapper.EntityMapperIfc;

public class RTLogMappingResult implements RTLogMappingResultIfc,
		RTLogRecordFormatConstantsIfc {
	protected RecordFormatIfc fileHeader = null;

	protected RecordFormatIfc fileTail = null;

	protected RecordFormatIfc transactionHeader = null;

	protected RecordFormatIfc transactionTail = null;

	protected ArrayList<RecordFormatIfc> transactionRecords = null;

	protected HashMap<String, RecordFormatIfc> taxRecords = null;

	protected boolean transactionSupportsItemAndTax = true;

	protected ArrayList<RecordFormatIfc> transactionShippingRecords = null;

	protected RecordFormatIfc otherTransactionTender = null;

	protected boolean transactionExportable = true;

	protected RecordFormatIfc defaultTransactionTender = null;
	
	//HBI EVEREST
	protected RecordFormatIfc transactionCustomer = null;

	public RTLogMappingResult() {
		this.transactionRecords = new ArrayList();
		this.transactionShippingRecords = new ArrayList();
		this.taxRecords = new HashMap();
	}

	public void initialize(EntityMapperIfc entityMapper)
			throws ExportFileException {
		this.defaultTransactionTender = MappingObjectFactoryContainer
				.getInstance().getFormatObjectFactory()
				.getRecordFormatInstance();

		RecordFormatIfc template = entityMapper.getFormatCatalog().getFormat(5);
		template.makeDeepCopyOfTemplate(this.defaultTransactionTender);

		FieldFormatIfc tndType = this.defaultTransactionTender
				.getFieldFormat("TenderTypeGroup");
		FieldFormatIfc subType = this.defaultTransactionTender
				.getFieldFormat("TenderTypeID");
		FieldFormatIfc tAmount = this.defaultTransactionTender
				.getFieldFormat("TenderAmount");

		tndType.setValue("CASH");
		subType.setValue("1000");
		tAmount.setValue("0.00");
	}

	public RecordFormatIfc getFileHeader() {
		return this.fileHeader;
	}

	public void setFileHeader(RecordFormatIfc fileHeader) {
		this.fileHeader = fileHeader;
	}

	public RecordFormatIfc getFileTail() {
		return this.fileTail;
	}

	public void setFileTail(RecordFormatIfc fileTail) {
		this.fileTail = fileTail;
	}

	public RecordFormatIfc getTransactionHeader() {
		return this.transactionHeader;
	}

	public void setTransactionHeader(RecordFormatIfc transactionHeader) {
		this.transactionHeader = transactionHeader;
	}

	public RecordFormatIfc getTransactionTail() {
		return this.transactionTail;
	}

	public void setTransactionTail(RecordFormatIfc transactionTail) {
		this.transactionTail = transactionTail;
	}

	public Collection<RecordFormatIfc> getTransactionRecords() {
		return this.transactionRecords;
	}

	public RecordFormatIfc getTransactionRecord(int index) {
		if (index >= this.transactionRecords.size()) {
			return null;
		}
		return ((RecordFormatIfc) this.transactionRecords.get(index));
	}

	public void addTransactionRecord(RecordFormatIfc transactionRecord,
			int lineNumber) {
		if (lineNumber == this.transactionRecords.size()) {
			this.transactionRecords.add(transactionRecord);
		} else if (lineNumber <= this.transactionRecords.size()) {
			this.transactionRecords.set(lineNumber, transactionRecord);
		} else {
			for (int i = this.transactionRecords.size(); i < lineNumber; ++i) {
				this.transactionRecords.add(new RTLogPlaceHolderRecordFormat());
			}
			this.transactionRecords.add(transactionRecord);
		}
	}

	public Collection<RecordFormatIfc> getItemRecords() {
		return getTransactionRecordsByType(2);
	}

	public Collection<RecordFormatIfc> getTenderRecords() {
		return getTransactionRecordsByType(5);
	}

	protected Collection<RecordFormatIfc> getTransactionRecordsByType(int type) {
		ArrayList items = new ArrayList();

		for (int i = 0; i < this.transactionRecords.size(); ++i) {
			RecordFormatIfc item = (RecordFormatIfc) this.transactionRecords
					.get(i);
			if (item.getRecordFormatCatalogIndex() != type)
				continue;
			items.add(item);
		}

		return items;
	}

	public Map<String, RecordFormatIfc> getTaxRecords() {
		return this.taxRecords;
	}

	public void addTaxRecord(String authority, RecordFormatIfc taxRecord) {
		this.taxRecords.put(authority, taxRecord);
	}

	public RecordFormatIfc getTaxRecord(String authority) {
		return ((RecordFormatIfc) this.taxRecords.get(authority));
	}

	public boolean isTransactionSupportsItemAndTax() {
		return this.transactionSupportsItemAndTax;
	}

	public void setTransactionSupportsItemAndTax(
			boolean transactionSupportsItemAndTax) {
		this.transactionSupportsItemAndTax = transactionSupportsItemAndTax;
	}

	public RecordFormatIfc getTransactionShippingRecord(int line) {
		int index = line - 1;
		if (index >= this.transactionShippingRecords.size()) {
			return null;
		}
		return ((RecordFormatIfc) this.transactionShippingRecords.get(index));
	}

	public void addTransactionShippingRecord(
			RecordFormatIfc transactionShippingRecord, int lineNumber) {
		int index = lineNumber - 1;
		if (index == this.transactionShippingRecords.size()) {
			this.transactionShippingRecords.add(transactionShippingRecord);
		} else if (index <= this.transactionRecords.size()) {
			this.transactionShippingRecords.set(lineNumber,
					transactionShippingRecord);
		} else {
			for (int i = this.transactionShippingRecords.size(); i < lineNumber; ++i) {
				this.transactionShippingRecords
						.add(new RTLogPlaceHolderRecordFormat());
			}
			this.transactionShippingRecords.add(transactionShippingRecord);
		}
	}

	public Collection<RecordFormatIfc> getTransactionShippingRecords() {
		return this.transactionShippingRecords;
	}

	public RecordFormatIfc getOtherTransactionTender() {
		return this.otherTransactionTender;
	}

	public void setOtherTransactionTender(RecordFormatIfc otherTransactionTender) {
		this.otherTransactionTender = otherTransactionTender;
	}

	public boolean isTransactionExportable() {
		return this.transactionExportable;
	}

	public void setTransactionExportable(boolean transactionExportable) {
		this.transactionExportable = transactionExportable;
	}

	public RecordFormatIfc getDefaultTransactionTender() {
		return this.defaultTransactionTender;
	}

	public void setDefaultTransactionTender(
			RecordFormatIfc defaultTransactionTender) {
		this.defaultTransactionTender = defaultTransactionTender;
	}
	
	//HBI EVEREST
	public RecordFormatIfc getTransactionCustomer() {
		return transactionCustomer;
	}
	
	public void setTransactionCustomer(RecordFormatIfc transactionCustomer) {
		this.transactionCustomer = transactionCustomer;
	}
}