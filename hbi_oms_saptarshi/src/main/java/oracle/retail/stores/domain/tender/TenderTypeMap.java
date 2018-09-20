package oracle.retail.stores.domain.tender;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is a singleton. It provides the mapping between int tender types, String tender type codes (used in the
 * DB) and String tender descriptors (used for display and printing). This class cannot be extended. If you need to add
 * new tender items and cannot modify this class, you should implement a new one and use it in your object factory.
 * 
 * @author Amitkumar Patel
 */
public class TenderTypeMap implements TenderTypeMapIfc {

    // The only instance of this class
    protected static final TenderTypeMap map = new TenderTypeMap();
    // Descriptors
    protected static ArrayList<String> descriptors = new ArrayList<String>(Arrays.asList(new String[] { "Cash",
            "Credit", "Check", "TravCheck", "GiftVoucher", "MailCheck", "Debit", "NonScanningCoupon", "GiftCard",
            "StoreCredit", "MallCert", "PurchaseOrder", "MoneyOrder", "E-Check", "LuncheonVoucher", "Supplier Coupon","WebPayment","PayPal","ETender",
            "Canadian Cash", "Canadian Check", "U.K. Cash", "U.K. Check", "Euro Cash", "Euro Check" }));
    /**Spec Name/#: [Bug 16747] Incorrect tender name displays at Summary Count and Discrepancy Confirm screen for 
     * Supplier Coupon (Till Reconciliation) Start
     * Developer: Pratik Shirsat
     * Defect :16747
     */
    protected static ArrayList<String> descriptorsGNC = new ArrayList<String>(Arrays.asList(new String[] { "Cash",
            "Credit", "Check", "TravCheck", "Gift Voucher", "MailCheck", "Debit", "NonScan Coupon", "GiftCard",
            "StoreCredit", "MallCert", "PurchaseOrder", "MoneyOrder", "E-Check", "LuncheonVoucher", "Supplier Coupon","WebPayment","PayPal","ETender","Canadian Cash",
            "Canadian Check", "U.K. Cash", "U.K. Check", "Euro Cash", "Euro Check" }));

    /**Spec Name/#: [Bug 16747] Incorrect tender name displays at Summary Count and Discrepancy Confirm screen for 
     * Supplier Coupon (Till Reconciliation) End
     * Developer: Pratik Shirsat
     * Defect :16747
     */
    protected static ArrayList<String> descriptorsBallina = new ArrayList<String>(Arrays.asList(new String[] { "Cash",
            "Credit", "Check", "TravCheck", "Gift Voucher", "MailCheck", "Debit", "NonScan Coupon", "GiftCard",
            "StoreCredit", "MallCert", "PurchaseOrder", "MoneyOrder", "E-Check", "Supplier Coupon", "Canadian Cash",
            "WebPayment","PayPal","ETender", "U.K. Cash", "U.K. Check", "Euro Cash", "Euro Check", "LuncheonVoucher" }));

    protected static ArrayList<String> IXRetailDescriptors = new ArrayList<String>(
            Arrays.asList(new String[] { "Cash", "Credit", "Check", "Trav. Check", "Gift Voucher", "Mail Check",
                    "Debit", "NonScan Coupon", "Gift Card", "Store Credit", "Mall Cert.", "Purchase Order",
                    "Money Order", "E-Check", "LuncheonVoucher", "Supplier Coupon","WebPayment","PayPal","ETender", "Canadian Cash", "Canadian Check",
                    "U.K. Cash", "U.K. Check", "Euro Cash", "Euro Check" }));

    protected static ArrayList<String> IXRetailDescriptorsGNC = new ArrayList<String>(Arrays.asList(new String[] {
            "Cash", "Credit", "Check", "Trav. Check", "Gift Voucher", "Mail Check", "Debit", "NonScan Coupon",
            "Gift Card", "Store Credit", "Mall Cert.", "Purchase Order", "Money Order", "E-Check", "LuncheonVoucher", "Supplier Coupon","WebPayment","PayPal","ETender",
            "Canadian Cash", "Canadian Check", "U.K. Cash", "U.K. Check", "Euro Cash", "Euro Check", ""}));
    
    protected static ArrayList<String> IXRetailDescriptorsBallina = new ArrayList<String>(Arrays.asList(new String[] {
            "Cash", "Credit", "Check", "Trav. Check", "Gift Voucher", "Mail Check", "Debit", "NonScan Coupon",
            "Gift Card", "Store Credit", "Mall Cert.", "Purchase Order", "Money Order", "E-Check", "Supplier Coupon",
            "Canadian Cash", "WebPayment", "PayPal","ETender","U.K. Cash", "U.K. Check", "Euro Cash", "Euro Check", "" }));
    
    protected static ArrayList<String> typeCodes = new ArrayList<String>(Arrays.asList(new String[] { "CASH", "CRDT",
            "CHCK", "TRAV", "GVCR", "MBCK", "DBIT", "NSQPON", "GCRD", "STCR", "MACT", "PRCH", "MNYO", "ECHK", "LVCR",
            "MCPN","SCPN","PYPL","ETND","CCSH","CCHK" }));
    /**Spec Name/#: [Bug 16747] Incorrect tender name displays at Summary Count and Discrepancy Confirm screen for 
     * Supplier Coupon (Till Reconciliation) Start
     * Developer: Pratik Shirsat
     * Defect :16747
     */ static ArrayList<String> typeCodesGNC = new ArrayList<String>(Arrays.asList(new String[] { "CASH",
            "CRDT", "CHCK", "TRAV", "GVCR", "MBCK", "DBIT", "NSQPON", "GCRD", "STCR", "MACT", "PRCH", "MNYO", "ECHK","LVCR",
            "MCPN",  "SCPN","PYPL","ETND","CCSH", "CCHK" }));
     /**Spec Name/#: [Bug 16747] Incorrect tender name displays at Summary Count and Discrepancy Confirm screen for 
      * Supplier Coupon (Till Reconciliation) End
      * Developer: Pratik Shirsat
      * Defect :16747
      */
     
    protected static ArrayList<String> typeCodesBallina = new ArrayList<String>(Arrays.asList(new String[] { "CASH",
            "CRDT", "CHCK", "TRAV", "GVCR", "MBCK", "DBIT", "NSQPON", "GCRD", "STCR", "MACT", "PRCH", "MNYO", "ECHK",
            "MCPN", "CCSH", "WEBPAYMENT", "PYPL" ,"ETND", "SCPN"}));

    protected static ArrayList<String> countedTenderDescriptors = new ArrayList<String>(Arrays.asList(new String[] {
            "Cash", "Credit", "Check", "TravelCheck", "GiftVoucher",
            "", // MailCheck not counted
            "Debit", "NonScanningCoupon", "GiftCard", "StoreCredit", "MallCert", "PurchaseOrder", "MoneyOrder", "",
            "LuncheonVoucher", "Supplier Coupon", "","PayPal","ETender", // E-Check 
    // not
    // counted
            }));

    protected static ArrayList<String> countedTenderDescriptorsGNC = new ArrayList<String>(Arrays.asList(new String[] {
            "Cash", "Credit", "Check", "TravelCheck", "GiftVoucher",
            "", // MailCheck not counted
            "Debit", "NonScanningCoupon", "GiftCard", "StoreCredit", "MallCert", "PurchaseOrder", "MoneyOrder", "","",
            "Supplier Coupon", "","PayPal","ETender" }));

    protected static ArrayList<String> countedTenderDescriptorsBallina = new ArrayList<String>(Arrays.asList(new String[] {
            "Cash", "Credit", "Check", "TravelCheck", "GiftVoucher",
            "", // MailCheck not counted
            "Debit", "NonScanningCoupon", "GiftCard", "StoreCredit", "MallCert", "PurchaseOrder", "MoneyOrder", "",
            "Supplier Coupon", "","WEBPAYMENT","PayPal","ETender"}));

    /**
     * This is a singleton, so the constructor is not accessible.
     */
    protected TenderTypeMap() {

    }

    /**
     * Returns the instance of the map.
     */
    public static TenderTypeMapIfc getTenderTypeMap() {

        return map;
    }

    /**
     * The data is static, so there's no point in cloning.
     * 
     * @return Object
     */
    @Override
    public Object clone() {

        return this;
    }

    /**
     * Maps tender type to tender type code.
     * 
     * @return String tender type code string
     */
    @Override
    public String getCode(int type) {

        String code;

        try 
        {
        	 code = typeCodes.get(type);
        }
        catch (IndexOutOfBoundsException e) {
            code = "UNKN";
        }
        return code;
    }

    /**
     * Maps tender type to tender type descriptor.
     * 
     * @return String tender type description.
     */
    @Override
    public String getDescriptor(int type) {

        String desc;

        try {
        	desc = descriptors.get(type);

        } catch (IndexOutOfBoundsException e) {
            desc = "DescriptorUnknown";
        }
        return desc;
    }

    /**
     * Maps tender type code to tender type
     * 
     * @return int tender type
     */
    @Override
    public int getTypeFromCode(String code) {

        int idex = 0;
        
        idex = typeCodes.indexOf(code);
       
        return idex;

    }

    /**
     * Maps tender type descriptor to tender type
     * 
     * @return int tender type
     */
    @Override
    public int getTypeFromDescriptor(String desc) {

        int idex = 0;
        
        idex = descriptors.indexOf(desc);
       
        return idex;
    }

    /**
     * Maps tender type to tender type descriptor.
     * 
     * @return String tender type description.
     */
    @Override
    public String getIXRetailDescriptor(int type) {

        String desc;

        desc = "DescriptorUnknown";

        return desc;
    }

    /**
     * Maps tender type descriptor to tender type
     * 
     * @return int tender type
     */
    @Override
    public int getTypeFromIXRetailDescriptor(String desc) {

        int idex = 0;
        
        idex = IXRetailDescriptors.indexOf(desc);
        
        return idex;

    }

    /**
     * Maps tender type to counted tender descriptor.
     * 
     * @return String tender type description.
     */
    @Override
    public String getCountedTenderDescriptor(int type) {

        String desc;

    	desc=countedTenderDescriptors.get(type);
        
        return desc;
    }

    /**
     * Maps counted tender descriptor to tender type
     * 
     * @return int tender type
     */
    @Override
    public int getTypeFromCountedTenderDescriptor(String desc) {

        int idex = 0;
        
        idex = countedTenderDescriptors.indexOf(desc);
        
        return idex;

    }

}
