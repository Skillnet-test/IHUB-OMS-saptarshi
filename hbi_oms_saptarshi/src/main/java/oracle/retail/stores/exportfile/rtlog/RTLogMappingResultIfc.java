/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/

/* 
------------------------------------------------------------------------------------------- FILE HISTORY --------------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     DEEFECT ID/FSD MOD.     DESCRIPTION
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	16/8/2017   Ikshita       HBI Everest             Aadded Getter and Setter method for transactionCustomer.
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/


package oracle.retail.stores.exportfile.rtlog;

import java.util.Collection;
import java.util.Map;
import oracle.retail.stores.exportfile.formater.RecordFormatIfc;
import oracle.retail.stores.exportfile.mapper.MappingResultIfc;

public abstract interface RTLogMappingResultIfc extends MappingResultIfc {
	public abstract RecordFormatIfc getFileHeader();

	public abstract void setFileHeader(RecordFormatIfc paramRecordFormatIfc);

	public abstract RecordFormatIfc getFileTail();

	public abstract void setFileTail(RecordFormatIfc paramRecordFormatIfc);

	public abstract RecordFormatIfc getTransactionHeader();

	public abstract void setTransactionHeader(
			RecordFormatIfc paramRecordFormatIfc);

	public abstract RecordFormatIfc getTransactionTail();

	public abstract void setTransactionTail(RecordFormatIfc paramRecordFormatIfc);

	public abstract Collection<RecordFormatIfc> getTransactionRecords();

	public abstract RecordFormatIfc getTransactionRecord(int paramInt);

	public abstract void addTransactionRecord(
			RecordFormatIfc paramRecordFormatIfc, int paramInt);

	public abstract RecordFormatIfc getTransactionShippingRecord(int paramInt);

	public abstract void addTransactionShippingRecord(
			RecordFormatIfc paramRecordFormatIfc, int paramInt);

	public abstract Collection<RecordFormatIfc> getTransactionShippingRecords();

	public abstract Collection<RecordFormatIfc> getItemRecords();

	public abstract Collection<RecordFormatIfc> getTenderRecords();

	public abstract Map<String, RecordFormatIfc> getTaxRecords();

	public abstract void addTaxRecord(String paramString,
			RecordFormatIfc paramRecordFormatIfc);

	public abstract RecordFormatIfc getTaxRecord(String paramString);

	public abstract boolean isTransactionSupportsItemAndTax();

	public abstract void setTransactionSupportsItemAndTax(boolean paramBoolean);

	public abstract RecordFormatIfc getOtherTransactionTender();

	public abstract void setOtherTransactionTender(
			RecordFormatIfc paramRecordFormatIfc);

	public abstract RecordFormatIfc getDefaultTransactionTender();

	public abstract void setDefaultTransactionTender(
			RecordFormatIfc paramRecordFormatIfc);
	
	//HBI EVEREST
	public abstract RecordFormatIfc getTransactionCustomer();

	public abstract void setTransactionCustomer(RecordFormatIfc paramRecordFormatIfc);
}