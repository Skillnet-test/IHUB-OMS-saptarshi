/* 
------------------------------------------------------------------------------------------- FILE HISTORY --------------------------------------------------------------------------
FILE VER.   DATE        DEVELOPER     DEEFECT ID/FSD MOD.     DESCRIPTION
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   1.     	20/7/2017   Ikshita       HBI Everest             Added constants CUSTOMER_TYPE_NON_LOYALTY and NON_LOYALTY_CUSTOMER_ID_FIELD.
   2.     	21/7/2017   Ikshita       HBI Everest             Added constants for setting Ref fields flags.
   3.     	16/8/2017   Ikshita       HBI Everest             Added constants for Customer related fields and deleted Order in store related flag.
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/
package nbty.retail.stores.exportfile.rtlog;

import oracle.retail.stores.exportfile.rtlog.RTLogRecordFormatConstantsIfc;

public abstract interface NbtyRTLogRecordFormatConstantsIfc extends RTLogRecordFormatConstantsIfc 
{

	public static final int TRANSACTION_TYPE_BO_CASH_IN = 45;
	public static final int TRANSACTION_TYPE_BO_CASH_OUT = 46;
	
	public static final String ITEM_SALES_TYPE_FIELD="SalesType";
	
	public static final String ITEM_SALES_TYPE_R="R";
	public static final String ITEM_SALES_TYPE_E="E";
	public static final String ITEM_SALES_TYPE_I="I";

	public static final int CUSTOMER_TYPE_NON_LOYALTY = 0;
	public static final String NON_LOYALTY_CUSTOMER_ID_FIELD="ReferenceNumber27";
	
	public static final String SET_FLAG_Y="Y";
	public static final String SET_FLAG_N="N";
	
	public static final String CUSTOMER_ORDER_LINE_NUMBER_FIELD = "CustomerOrderLineNo";
	
	public static final int TRANSACTION_CUSTOMER = 16;
	public static final String CUSTOMER_ID = "CustomerID";
	public static final String TRANSACTION_CUSTOMER_NAME = "TransactionCustomer";
	
}