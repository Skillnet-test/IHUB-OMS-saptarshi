/*********************************************************************************************
 * Description:
 * Created By: 
 * 
 * Created Date: 
 * 
 *  History:
 *  
 *  Vers       Date                By                   Spec                         Description
 *  1.0        25/11/16            Pranali M            HBI Payment Integration      Added constants for Ingenico Receipt Table        
 ********************************************************************************************/
package nbty.retail.stores.persistence.utility;

import oracle.retail.stores.persistence.utility.ARTSDatabaseIfc;

/**
 * This class used constants for table & columns
 * 
 * @author Manjushri Jadhav
 */
public interface NbtyARTSDatabaseIfc extends ARTSDatabaseIfc
{

    public static final String FIELD_REFERENCE_NUMBER = "REF_NUM";

    public static final String NBTY_NOTIF_PREF = "NBTY_NOTIF_PREF";

    public static final String TABLE_NBTY_GV_NUMBERS = "NBTY_GV_NUMBERS";

    public static final String FIELD_GV_AMOUNT = "GV_AMT";

    public static final String FIELD_GV_NUMBER = "GV_NMB";

    public static String TABLE_NBTY_CODE_LIST = "NBTY_EXCOMP_DISC";

    public static String FIELD_DE_CMP_LST = "DE_CD_CMP";

    public static String FIELD_PR_CMP_DISC = "PR_CMP_DISC";

    public static String FIELD_COMP_CODES = "COMPDISC_CODES";

    public static final String TABLE_NBTY_AER_RETURN = "NBTY_TR_LTM_AER_RTN";

    public static final String FIELD_BATCH_NUMBER = "BTCH_NUM";

    public static final String FIELD_REACTION = "RECTN";

    public static final String FIELD_EXP_DATE = "EXP_DATE";

    public static final String FIELD_EMAIL = "EMAIL";

    public static final String FIELD_COUNTY = "COUNTY";

    public static String TABLE_NBTY_LOGIN_DETAILS = "NBTY_EM_TM_ENR";

    public static final String FIELD_NBTY_ID_WS = "ID_WS";

    public static final String FIELD_NBTY_LOGIN_STATUS = "LOGIN_STATUS";

    public static final String TABLE_NBTY_PH_TRN = "NBTY_PH_TRN";

    public static final String TABLE_NBTY_TR_LTM_PH = "NBTY_TR_LTM_PH";

    public static final String FIELD_TS_TRN = "TS_TRN";

    public static final String FIELD_ID_TRN = "ID_TRN";

    public static final String FIELD_COUPON_NAME = "CPN_NAME";

    public static final String FIELD_CAPTURE_CUSTOMER_EMAIL = "NBTY_EMAIL";

    /** Commissions module : added by gauri start */
    // For Sale
    public static final String TABLE_NBTY_SLS_CO_MDFR_CMN = "NBTY_SLS_CO_MDFR_CMN";

    public static final String FIELD_COMMISSION_LINE_ITEM_EXTENDED_DISCOUNTED_AMOUNT = "TTL_LN_ITM_AMT";

    public static final String FIELD_COMMISSION_RATE_FOR_SALES_ASSOCIATE = "EM_CMN_RATE";

    public static final String FIELD_PM_AMOUNT = "PM_AMOUNT";

    public static final String FIELD_PM_MONIES_FLAG = "PM_MONIES_FLAG";

    public static final String FIELD_ITEM_COMMISSION_ELIGIBLE_FLAG = "FL_PE_CMN_AMT";

    public static final String FIELD_BATCH_ERROR_MESSAGE = "BTCH_MSG_DSC";

    // For Return
    public static final String TABLE_NBTY_RTN_CO_MDFR_CMN = "NBTY_RTN_CO_MDFR_CMN";

    public static final String FIELD_COMMISSION_RETURN_STORE_ID = "ID_STR_RT";

    public static final String FIELD_COMMISSION_RETURN_WORKSTATION_ID = "ID_WS";

    public static final String FIELD_COMMISSION_RETURN_TRANSACTION_BUSINESS_DATE = "DC_DY_BSN";

    public static final String FIELD_COMMISSION_RETURN_TRANSACTION_SEQUENCE_NUMBER = "AI_TRN";

    public static final String FIELD_COMMISSION_RETURN_EMPLOYEE_ID = "RTN_ID_EM";

    public static final String FIELD_COMMISSION_RETURN_ITEM_ID = "RTN_ID_ITM";

    public static final String FIELD_COMMISSION_RETURN_LINE_ITEM_EXTENDED_DISCOUNTED_AMOUNT = "RTN_TTL_LN_ITM_AMT";

    public static final String FIELD_COMMISSION_RETURN_PM_AMOUNT = "RTN_PM_AMOUNT";

    public static final String FIELD_COMMISSION_RETURN_PM_MONIES_FLAG = "RTN_PM_MONIES_FLAG";

    public static final String FIELD_COMMISSION_RETURN_ITEM_COMMISSION_ELIGIBLE_FLAG = "RTN_FL_PE_CMN_AMT";

    public static final String FIELD_COMMISSION_CROSS_RETURN_FLAG = "IS_CROSS_RTN";

    public static final String FIELD_COMMISSION_WITH_RECEIPT_FLAG = "IS_RTN_WITHRECT";

    public static final String FIELD_COMMISSION_ORIGINAL_EMPLOYEE_ID = "SLS_ID_EM";

    public static final String FIELD_COMMISSION_ORIGINAL_WORKSTATION_ID = "SLS_ID_WS";

    public static final String FIELD_COMMISSION_ORIGINAL_TRANSACTION_BUSINESS_DATE = "SLS_DC_DY_BSN";

    public static final String FIELD_COMMISSION_ORIGINAL_TRANSACTION_SEQUENCE_NUMBER = "SLS_AI_TRN";

    public static final String FIELD_COMMISSION_ORIGINAL_STORE_ID = "SLS_ID_STR_RT";

    public static final String FIELD_MASKED_CUSTOMER_LOYALTY_FLAG = "LTY_CUST_FLG";

    // For Layaway
    public static final String FIELD_NBTY_FN_CT = "NBTY_FN_CT";

    public static final String FIELD_NBTY_LN_CT = "NBTY_LN_CT";

    public static final String FIELD_NBTY_TL_CT = "NBTY_TL_CT";

    public static final String FIELD_NBTY_EML_CT = "NBTY_EML_CT";

    // For Clock In-Out
    public static final String FIELD_CLOCKINOUT_MODIFED_FLAG = "MOD_FLAG";

    /** Commissions module : added by gauri end */

    /**
     * SIEBEL PROMOTIONS
     */
    public static final String TABLE_NBTY_POINT_CALCULATION_RESPONSE = "NBTY_SBL_PT_CAL_RESP";

    public static final String FIELD_LOYALTY_MEM_ID = "LOY_MEM_ID";

    public static final String FIELD_LOYALTY_CARD_NUMBER = "LOY_CARD_NUMBER";

    public static final String FIELD_LOYALTY_PROGRAM_NAME = "NM_PROGRAM";

    public static final String FIELD_LOYALTY_BALANCE_BEGIN = "PTS_BALANCE_BEGIN";

    public static final String FIELD_LOYALTY_BALANCE_END = "PTS_BALANCE_END";

    public static final String FIELD_LOYALTY_BONUS_POINTS = "PTS_BONUS";

    public static final String FIELD_LOYALTY_BASE_POINTS = "PTS_BASE";

    public static final String FIELD_SIEBEL_STATUS = "STATUS";

    public static final String FIELD_TERMS_AND_CONDITIONS_FLAG = "FL_TERMS_COND";

    /**
     * SIEBEL PROMOTIONS LINE ITEM
     */
    public static final String TABLE_NBTY_POINT_CALCULATION_RESPONSE_LINE_ITEM = "NBTY_SBL_PT_CAL_RESP_LN_ITM";

    public static final String FIELD_PROMOTION_LINE_ITEM_ID = "PRM_LN_ITM";

    public static final String FIELD_LOYALTY_TRANSACTION_ID = "LOY_TRN_ID";

    public static final String FIELD_LOYALTY_TRANSACTION_NUMBER = "LOY_TRN_NUM";

    public static final String FIELD_LOYALTY_PROMOTION_NAME = "LOY_PRM_NM";

    public static final String FIELD_LOYALTY_PROMOTION_RULE_NAME = "LOY_PRM_RL_NM";

    public static final String FIELD_LOYALTY_PROMOTION_ACTION_NAME = "LOY_PRM_ACT_NM";

    public static final String FIELD_LOYALTY_POINT_TYPE = "LOY_PT_TYPE";

    public static final String FIELD_LOYALTY_SUB_POINT_TYPE = "LOY_SUB_PT_TYPE";

    public static final String FIELD_LOYALTY_PROGRAM_ID = "LOY_PRG_ID";

    public static final String FIELD_LOYALTY_POINTS_ACCRUED = "LOY_PT_ACC";

    /**
     * FOR STORE OPEN PREVIOUS DATE HISTORY
     */
    public static final String TABLE_NBTY_CLOSE_STORE_NEXT_DAY_HISTORY = "NBTY_HST_CLS_STR_NXT_DY";

    public static final String FIELD_STORE_ID = "ID_STR_RT";

    public static final String FIELD_BUSINESS_DATE = "DC_DY_BSN";

    public static final String FIELD_EMPLOYEEID = "ID_EM";

    public static final String FIELD_STORE_OPEN_FLAG = "SL_FLG";

    public static final String FIELD_RECORD_CREATED_TIME_STAMP = "TS_CRT_RCRD";

    public static final String FIELD_LYLT_PT = "LYLT_PT";

    public static final String TABLE_NBTY_ABS_FUT_HR_HD = "NBTY_ABS_FUT_HR_HD";

    public static final String FIELD_ABS_FUT_HR_HD_ID_STR_RT = "ID_STR_RT";

    public static final String FIELD_ABS_FUT_HR_HD_ID_UN = "ID_UN";

    public static final String FIELD_ABS_FUT_HR_HD_ID_EM = "ID_EM";

    public static final String FIELD_ABS_FUT_HR_HD_DY_BSN_STR = "DY_BSN_STR";

    public static final String FIELD_ABS_FUT_HR_HD_DY_BSN_END = "DY_BSN_END";

    public static final String FIELD_ABS_FUT_HR_HD_TS_CRT_RCRD = "TS_CRT_RCRD";

    public static final String FIELD_ABS_FUT_HR_HD_TS_MDF_RCRD = "TS_MDF_RCRD";

    public static final String FIELD_ABS_FUT_HR_HD_BTCH_MSG_DSC = "BTCH_MSG_DSC";

    public static final String TABLE_NBTY_ABS_FUT_HR_DT = "NBTY_ABS_FUT_HR_DT";

    public static final String FIELD_ABS_FUT_HR_DT_ID_STR_RT = "ID_STR_RT";

    public static final String FIELD_ABS_FUT_HR_DT_ID_UN = "ID_UN";

    public static final String FIELD_ABS_FUT_HR_DT_ID_HR = "ID_HR";

    public static final String FIELD_ABS_FUT_HR_DT_DC_DY_BSN = "DC_DY_BSN";

    public static final String FIELD_ABS_FUT_HR_DT_ABS_HRS = "ABS_HRS";

    public static final String FIELD_ABS_FUT_HR_DT_RSN_CD = "RSN_CD";

    public static final String FIELD_ABS_FUT_HR_DT_BTCH_MSG_DSC = "BTCH_MSG_DSC";

    public static final String FIELD_NBTY_LAST_ACCESS_STATUS = "LST_ACS_STATUS";

    public static final String FIELD_NBTY_SBL_PROM_FLAG = "FL_PT_PRDV";

    public static final String FIELD_BASE_STORE = "ID_STR_RT_EM";

    public static final String FIELD_NBTY_PROMOTIONITM_FLAG = "NBTY_FL_PR_ITM";

    public static final String FIELD_NBTY_PR_ITM_INDEX = "NBTY_PR_ITM_INDEX";

    public static final String FIELD_NBTY_PR_NAME = "NBTY_PR_NAME";

    public static final String FIELD_NBTY_PR_AMT = "NBTY_PR_AMT";

    public static final String FIELD_NBTY_PR_RULEID = "NBTY_PR_RULEID";

    // Added for CR-3734 Virtual Category Extenstion.
    public static final String FIELD_NBTY_ITEM_MERCHANDISE_CLASSIFICATION_CODE0 = "NBTY_ID_STRC_MR_CD0";

    public static final String FIELD_NBTY_ITEM_MERCHANDISE_CLASSIFICATION_CODE1 = "NBTY_ID_STRC_MR_CD1";

    public static final String FIELD_NBTY_ITEM_MERCHANDISE_CLASSIFICATION_CODE2 = "NBTY_ID_STRC_MR_CD2";

    public static final String FIELD_NBTY_ITEM_MERCHANDISE_CLASSIFICATION_CODE3 = "NBTY_ID_STRC_MR_CD3";

    public static final String FIELD_NBTY_ITEM_MERCHANDISE_CLASSIFICATION_CODE4 = "NBTY_ID_STRC_MR_CD4";

    public static final String FIELD_NBTY_ITEM_MERCHANDISE_CLASSIFICATION_CODE5 = "NBTY_ID_STRC_MR_CD5";

    public static final String FIELD_NBTY_ITEM_MERCHANDISE_CLASSIFICATION_CODE6 = "NBTY_ID_STRC_MR_CD6";

    public static final String FIELD_NBTY_ITEM_MERCHANDISE_CLASSIFICATION_CODE7 = "NBTY_ID_STRC_MR_CD7";

    public static final String FIELD_NBTY_ITEM_MERCHANDISE_CLASSIFICATION_CODE8 = "NBTY_ID_STRC_MR_CD8";

    public static final String FIELD_NBTY_ITEM_MERCHANDISE_CLASSIFICATION_CODE9 = "NBTY_ID_STRC_MR_CD9";

    public static final String ALIAS_EMAIL = "em";

    public static final String ALIAS_STORE_HIERARCHY = "sh";

    // added for bounce back coupon

    public static final String TABLE_BCPN_EL_TRN_RDM = "BCPN_EL_TRN_RDM";

    public static final String ALIAS_BCPN_EL_TRN_RDM = "RDM";

    public static final String FIELD_ID_SEQ = "ID_SEQ";

    public static final String FIELD_STORE_GROUP = "ID_STR_GRP";

    public static final String FIELD_COUPON_ID = "ID_BCPN";

    public static final String FIELD_TYPE_COUPON = "TY_BCPN";

    public static final String FIELD_BASKET_VALUE = "MO_EL_TRN_RDM";

    public static final String FIELD_AMOUNT_OFF = "MO_BCPN_DSC";

    public static final String FIELD_PERCENT_OFF = "PE_BCPN_DSC";

    public static final String FIELD_COUPON_AMOUNT = "MO_BCPN";

    public static final String TABLE_TR_BCPN_RDM = "TR_BCPN_RDM";

    public static final String TABLE_BCPN_EL_ISS = "BCPN_EL_ISS";

    public static final String ALIAS_BCPN_EL_ISS = "RDM";

    public static final String FIELD_TIGGER_LEVEL = "TY_EL_ISS";

    public static final String TABLE_BCPN_EL_ISS_TRN = "BCPN_EL_ISS_TRN";

    public static final String FIELD_TIGGER_LEVEL_AMOUNT = "MO_TRN_ISS";

    public static final String TABLE_BCPN_EL_ISS_CUST = "BCPN_EL_ISS_CUST";

    public static final String FIELD_TIGGER_CUST_LEVEL = "TY_CUST_EL";

    public static final String ALIAS_ISS_TRAN_CUST = "TRAN";

    public static final String TABLE_BCPN_STR_GP = "BCPN_STR_GP";

    public static final String ALIAS_BCPN_STR_GP = "GRP";

    public static final String FIELD_ISSUE_EFFECTIVE_DATE = "DT_BCPN_ISS_EF";

    public static final String FIELD_ISSUE_EXPIRY_DATE = "DT_BCPN_ISS_EP";

    public static final String FIELD_REDEEM_EFFECTIVE_DATE = "DT_BCPN_RDM_EF";

    public static final String FIELD_REDEEM_EXPIRY_DATE = "DT_BCPN_RDM_EP";

    public static final String FIELD_COUPON_PRIORTY = "PRI_BCPN";

    public static final String FIELD_ID_TLOG_BTCH = "ID_TLOG_BTCH";

    public static final String FIELD_STORE_ADDRESS_FLAG = "CPT_STR_RT_DT";

    public static final String TABLE_BCPN_EL_ISS_ITM = "BCPN_EL_ISS_ITM";

    public static final String ALIAS_ISS_ITM = "ITM";

    public static final String FIELD_ISSUE_ITEM = "ID_ITM_EL";

    public static final String TABLE_BCPN_TY_I8 = "BCPN_TY_I8";

    public static final String ALIAS_BCPN_TY_I8 = "BCP";

    public static final String FIELD_BCPN_COUPON_NAME = "NM_BCPN";

    public static final String FIELD_BCPN_COUPON_DEC1 = "DE_BCPN_1";

    public static final String FIELD_BCPN_COUPON_DEC2 = "DE_BCPN_2";

    public static final String FIELD_WEB_ID = "ID_WEB";

    public static final String FIELD_WEB_DEC = "DE_WEB";

    public static final String FIELD_TERM_CONDITION = "BCPN_TNC";

    public static final String FIELD_INSTRUCTION = "ID_BCPN_INSTR";

    // Defect #15548
    public static final String FIELD_STORE_CREDIT_RFND_AMOUNT = "STR_CRDT_RFND_AMT";

    public static final String TABLE_SEGMENTED_BOUNCE_BACK = "BCPN_EL_ISS_RDM_CUST_SEG";

    public static final String ALIAS_SEGMENTED_BOUNCE_BACK = "SEGID";

    public static final String FIELD_SEGMENTED_ID = "SEG_ID";

    public static final String FIELD_COUPON_NO = "COUPON_NO";

    public static final String FIELD_COUPON_ACTION = "COUPON_ACTION";

    public static final String FIELD_TENDER_REFERENCE_ID = "REFERENCE_ID";

    // Added new column for Order In Store
    public static final String NBTY_FLG_OIS = "FLG_OIS";

    public static final String NBTY_ATG_ORD_ID = "NBTY_ATG_ORD_ID";

    public static final String FIELD_STORE_ORDER_IN_STORE_NEW_TOTAL_AMOUNT = "NBTY_MO_OIS_NEW_TOT";

    // SR #3-9236040241 Changes added
    public static final String FIELD_RETURN_TENDER_SEQUENCE_NUMBER = "AI_RTN_TND";

    public static final String FIELD_WEB_ORDER_LINE_ITEM_STATUS = "WEB_LN_ITM_STS";

    public static final String FIELD_WEB_ORDER_LINE_ITEM_SEQ_NO = "WEB_LN_ITM_SEQ";

    public static final String TABLE_OIS_LN_ITM = "NBTY_OIS_LN_ITM";

    public static final String FIELD_OIS_ID_ITM = "ID_ITM";

    public static final String FIELD_OIS_DE_ITM = "DE_ITM";

    public static final String FIELD_OIS_QU_ITM_LN_RTN = "QU_ITM_LN_RTN";

    public static final String FIELD_OIS_FINAL_PRICE = "FINAL_PRICE";

    public static final String FIELD_OIS_UNIT_PRICE = "UNIT_PRICE";

    public static final String FIELD_OIS_PRM_REF = "PRM_REF";

    public static final String TABLE_OIS_DSC_LN_ITM = "NBTY_OIS_DSC_LN_ITM";

    public static final String FIELD_OIS_DE_DSC = "DE_DSC";

    public static final String FIELD_OIS_DSC_REF = "DSC_REF";

    public static final String FIELD_OIS_DSC_AMT = "DSC_AMT";

    public static final String FIELD_OIS_DSC_TYPE = "DSC_TYPE";

    public static final String FIELD_OIS_MO_OIS_TOT = "MO_OIS_TOT";

    public static final String FIELD_OIS_MO_OIS_DSC_TOT = "MO_OIS_DSC_TOT";

    public static final String ID_ITM_SHP = "ID_ITM_SHP";

    public static final String FIELD_OIS_PROMO_NAME = "PROMO_NAME";

    public static final String FIELD_OIS_PROMO_AMT = "PROMO_AMT";

    public static final String OIS_ORDER_TYPE = "4";

    // POS COMMISSONS CHANGES
    public static final String FIELD_COMMISSION_LINE_ITEM_EXTENDED_DISCOUNTED_MULTIPLIED_AMOUNT = "TTL_LN_ITM_MUL_AMT";

    public static final String FIELD_COMMISSION_CODE = "CMN_MUL_CD";

    public static final String FIELD_COMMISSION_AMOUNT = "CMN_MUL_AMT";

    public static final String FIELD_COMMISSION_RETURN_LINE_ITEM_EXTENDED_DISCOUNTED_MULTIPLIED_AMOUNT = "RTN_TTL_LN_ITM_MUL_AMT";

    public static final String FIELD_RETURN_COMMISSION_CODE = "RTN_CMN_MUL_CD";

    public static final String FIELD_RETURN_COMMISSION_AMOUNT = "RTN_CMN_MUL_AMT";

    // POS COMMISSONS CHANGES

    public static final String TABLE_INGENICO_CREDIT_DEBIT_CARD_EFT_RECEIPT = "HBI_TR_LTM_PYMT_RCPT";

    public static final String FIELD_INGENICO_RETAIL_STORE_ID = "ID_STR_RT";

    public static final String FIELD_INGENICO_WORKSTATION_ID = "ID_WS";

    public static final String FIELD_INGENICO_TRANSACTION_SEQUENCE_NUMBER = "AI_TRN";

    public static final String FIELD_INGENICO_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER = "AI_LN_ITM";

    public static final String FIELD_INGENICO_BUSINESS_DAY_DATE = "DC_DY_BSN";

    public static final String FIELD_INGENICO_RECORD_CREATION_TIMESTAMP = "TS_CRT_RCRD";

    public static final String FIELD_INGENICO_EFT_RECEIPT = "EFT_RECEIPT";

    public static final String FIELD_INGENICO_STORE_EFT_RECEIPT = "STR_EFT_RECEIPT";
    
    public static final String FIELD_NBTY_PREF_LANG = "NBTY_PREF_LANG";
    
    public static final String FIELD_NBTY_VAT_RECPT_FLAG = "NBTY_VAT_RECPT_FLAG";
    
    // CR #244 OIS Enable for DT/EZ
    public static final String TABLE_ETENDER_TENDER_LINE_ITEM="NBTY_TR_LTM_ETND";
    
    public static final String FIELD_RETRIEVED_ACCOUNT_TOKEN_NO = "ID_RTRVD_REF";
    
    public static final String FIELD_ETENDER_ACQUIRER = "ACQUIRER";
    

}
