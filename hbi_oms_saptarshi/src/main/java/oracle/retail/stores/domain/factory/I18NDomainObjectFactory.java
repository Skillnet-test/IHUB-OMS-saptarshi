/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
/* ===========================================================================
* Copyright (c) 2008, 2011, Oracle and/or its affiliates. All rights reserved. 
 * ===========================================================================
 * $Header: rgbustores/modules/domain/src/oracle/retail/stores/domain/factory/I18NDomainObjectFactory.java /rgbustores_13.4x_generic_branch/2 2011/10/06 12:38:52 sgu Exp $
 * ===========================================================================
 * NOTES
 * <other useful comments, qualifications, etc.>
 *
 * MODIFIED    (MM/DD/YY)
 *    sgu       10/04/11 - rework table tax using tax rules instead of
 *                         calculator
 *    nkgautam  06/22/10 - bill pay changes
 *    cgreene   05/26/10 - convert to oracle packaging
 *    cgreene   04/22/10 - updating deprecated names
 *    abondala  01/03/10 - update header date
 *    cgreene   03/19/09 - add getItemImageInstance method
 *    aphulamb  11/27/08 - fixed merge issue
 *    aphulamb  11/13/08 - Check in all the files for Pickup Delivery Order
 *                         functionality
 *    mahising  11/13/08 - Added for Customer module for both ORPOS and ORCO
 *    rkar      11/04/08 - Added code for POS-RM integration
 *    akandru   10/31/08 - EJ Changes_I18n
 *    akandru   10/30/08 - EJ changes
 *    ddbaker   10/24/08 - Updates due to merge
 *    akandru   10/20/08 - EJ -- I18N
 *    akandru   10/20/08 -
 *    ddbaker   10/17/08 - Domain portion of I18N ItemIfc description updates.
 *    ddbaker   10/13/08 - Updated to populate the locale map with the
 *                         supported locales for unit testing
 *    ddbaker   10/13/08 - Updated to use factory to create localized text
 *                         objects
 *    cgreene   10/02/08 - merged with tip
 *    mchellap  09/30/08 - Updated copy right header
 *
 * ===========================================================================
 *     $Log:
 *      7    360Commerce 1.6         4/25/2007 10:00:57 AM  Anda D. Cadar   I18N
 *           merge
 *      6    360Commerce 1.5         11/9/2006 7:28:31 PM   Jack G. Swan
 *           Modifided for XML Data Replication and CTR.
 *      5    360Commerce 1.4         10/26/2006 4:06:18 PM  Gennady Ioffe
 *           Report Removal: removed 6.x Post-Processor
 *      4    360Commerce 1.3         9/29/2006 10:42:54 AM  Rohit Sachdeva
 *           21237: Password Policy Service Domain Updates
 *      3    360Commerce 1.2         8/29/2006 6:31:37 PM   Brett J. Larsen CR
 *           20917 - remove stock item return disposition codes (aka inventory
 *           reason codes)
 *
 *           part of inventory feature which is no longer supported
 *      2    360Commerce 1.1         4/27/2006 7:27:24 PM   Brett J. Larsen CR
 *           17307 - remove inventory functionality - stage 2
 *      1    360Commerce 1.0         1/25/2006 2:32:57 PM   Brett J. Larsen
 *     $
 *     Revision 1.1  2004/10/15 21:32:01  jdeleau
 *     @scr 5959 Services Impact - Internationalize Domain Object Factory
 *
 *===========================================================================
 */
package oracle.retail.stores.domain.factory;
import java.util.Locale;

import oracle.retail.stores.commerceservices.common.currency.CurrencyDecimal;
import oracle.retail.stores.commerceservices.common.currency.CurrencyIfc;
import oracle.retail.stores.commerceservices.common.currency.CurrencyType;
import oracle.retail.stores.commerceservices.common.currency.CurrencyTypeIfc;
import oracle.retail.stores.commerceservices.common.currency.CurrencyTypeList;
import oracle.retail.stores.commerceservices.common.currency.CurrencyTypeListIfc;
import oracle.retail.stores.commerceservices.security.EmployeeComplianceIfc;
import oracle.retail.stores.commerceservices.security.PasswordPolicyEvaluatorIfc;
import oracle.retail.stores.commerceservices.security.SinglePasswordPolicyComplianceObject;
import oracle.retail.stores.commerceservices.security.SinglePasswordPolicyEvaluator;
import oracle.retail.stores.common.utility.LocaleMap;
import oracle.retail.stores.common.utility.LocalizedText;
import oracle.retail.stores.common.utility.LocalizedTextIfc;
import oracle.retail.stores.domain.alert.AlertEntry;
import oracle.retail.stores.domain.alert.AlertEntryIfc;
import oracle.retail.stores.domain.alert.AlertList;
import oracle.retail.stores.domain.alert.AlertListIfc;
import oracle.retail.stores.domain.customer.CaptureCustomer;
import oracle.retail.stores.domain.customer.CaptureCustomerIfc;
import oracle.retail.stores.domain.customer.Customer;
import oracle.retail.stores.domain.customer.CustomerGroup;
import oracle.retail.stores.domain.customer.CustomerGroupIfc;
import oracle.retail.stores.domain.customer.CustomerIfc;
import oracle.retail.stores.domain.customer.CustomerInfo;
import oracle.retail.stores.domain.customer.CustomerInfoIfc;
import oracle.retail.stores.domain.customer.event.CustomerEventBaby;
import oracle.retail.stores.domain.customer.event.CustomerEventBabyIfc;
import oracle.retail.stores.domain.customer.event.CustomerEventSpecial;
import oracle.retail.stores.domain.customer.event.CustomerEventSpecialIfc;
import oracle.retail.stores.domain.customer.event.CustomerEventWedding;
import oracle.retail.stores.domain.customer.event.CustomerEventWeddingIfc;
import oracle.retail.stores.domain.customer.event.MerchandisePreference;
import oracle.retail.stores.domain.customer.event.MerchandisePreferenceIfc;
import oracle.retail.stores.domain.discount.AdvancedPricingRule;
import oracle.retail.stores.domain.discount.AdvancedPricingRuleIfc;
import oracle.retail.stores.domain.discount.AdvancedPricingRuleSearchCriteria;
import oracle.retail.stores.domain.discount.AdvancedPricingRuleSearchCriteriaIfc;
import oracle.retail.stores.domain.discount.BestDealGroup;
import oracle.retail.stores.domain.discount.BestDealGroupIfc;
import oracle.retail.stores.domain.discount.CustomerDiscountByPercentage;
import oracle.retail.stores.domain.discount.CustomerDiscountByPercentageIfc;
import oracle.retail.stores.domain.discount.DiscountCalculationIfc;
import oracle.retail.stores.domain.discount.DiscountList;
import oracle.retail.stores.domain.discount.DiscountListEntry;
import oracle.retail.stores.domain.discount.DiscountListEntryIfc;
import oracle.retail.stores.domain.discount.DiscountListIfc;
import oracle.retail.stores.domain.discount.DiscountRule;
import oracle.retail.stores.domain.discount.DiscountRuleIfc;
import oracle.retail.stores.domain.discount.ItemDiscountByAmountIfc;
import oracle.retail.stores.domain.discount.ItemDiscountByAmountStrategy;
import oracle.retail.stores.domain.discount.ItemDiscountByFixedPriceStrategy;
import oracle.retail.stores.domain.discount.ItemDiscountByPercentageIfc;
import oracle.retail.stores.domain.discount.ItemDiscountByPercentageStrategy;
import oracle.retail.stores.domain.discount.ItemTransactionDiscountAudit;
import oracle.retail.stores.domain.discount.ItemTransactionDiscountAuditIfc;
import oracle.retail.stores.domain.discount.ReturnItemTransactionDiscountAudit;
import oracle.retail.stores.domain.discount.ReturnItemTransactionDiscountAuditIfc;
import oracle.retail.stores.domain.discount.SourceCriteria;
import oracle.retail.stores.domain.discount.StandardDiscountCalculation;
import oracle.retail.stores.domain.discount.SuperGroup;
import oracle.retail.stores.domain.discount.SuperGroupIfc;
import oracle.retail.stores.domain.discount.TargetCriteria;
import oracle.retail.stores.domain.discount.TransactionDiscountByAmountIfc;
import oracle.retail.stores.domain.discount.TransactionDiscountByAmountStrategy;
import oracle.retail.stores.domain.discount.TransactionDiscountByPercentageIfc;
import oracle.retail.stores.domain.discount.TransactionDiscountByPercentageStrategy;
import oracle.retail.stores.domain.emessage.EMessage;
import oracle.retail.stores.domain.emessage.EMessageIfc;
import oracle.retail.stores.domain.employee.Employee;
import oracle.retail.stores.domain.employee.EmployeeClockEntry;
import oracle.retail.stores.domain.employee.EmployeeClockEntryIfc;
import oracle.retail.stores.domain.employee.EmployeeIfc;
import oracle.retail.stores.domain.employee.Role;
import oracle.retail.stores.domain.employee.RoleFunction;
import oracle.retail.stores.domain.employee.RoleFunctionGroup;
import oracle.retail.stores.domain.employee.RoleFunctionGroupIfc;
import oracle.retail.stores.domain.employee.RoleFunctionIfc;
import oracle.retail.stores.domain.employee.RoleIfc;
import oracle.retail.stores.domain.event.ItemMaintenanceEvent;
import oracle.retail.stores.domain.event.ItemMaintenanceEventIfc;
import oracle.retail.stores.domain.event.ItemPriceMaintenanceEvent;
import oracle.retail.stores.domain.event.ItemPriceMaintenanceEventIfc;
import oracle.retail.stores.domain.event.MaintenanceEvent;
import oracle.retail.stores.domain.event.MaintenanceEventIfc;
import oracle.retail.stores.domain.event.PriceChange;
import oracle.retail.stores.domain.event.PriceChangeIfc;
import oracle.retail.stores.domain.event.PriceDerivationRuleMaintenanceEvent;
import oracle.retail.stores.domain.event.PriceDerivationRuleMaintenanceEventIfc;
import oracle.retail.stores.domain.financial.AssociateProductivity;
import oracle.retail.stores.domain.financial.AssociateProductivityIfc;
import oracle.retail.stores.domain.financial.BillPay;
import oracle.retail.stores.domain.financial.BillPayIfc;
import oracle.retail.stores.domain.financial.DepartmentActivity;
import oracle.retail.stores.domain.financial.DepartmentActivityIfc;
import oracle.retail.stores.domain.financial.Drawer;
import oracle.retail.stores.domain.financial.DrawerIfc;
import oracle.retail.stores.domain.financial.EmployeeActivity;
import oracle.retail.stores.domain.financial.EmployeeActivityIfc;
import oracle.retail.stores.domain.financial.FinancialCount;
import oracle.retail.stores.domain.financial.FinancialCountIfc;
import oracle.retail.stores.domain.financial.FinancialCountTenderItem;
import oracle.retail.stores.domain.financial.FinancialCountTenderItemIfc;
import oracle.retail.stores.domain.financial.FinancialTotals;
import oracle.retail.stores.domain.financial.FinancialTotalsIfc;
import oracle.retail.stores.domain.financial.HardTotals;
import oracle.retail.stores.domain.financial.HardTotalsBuilderIfc;
import oracle.retail.stores.domain.financial.HardTotalsIfc;
import oracle.retail.stores.domain.financial.HardTotalsStringBuilder;
import oracle.retail.stores.domain.financial.Layaway;
import oracle.retail.stores.domain.financial.LayawayIfc;
import oracle.retail.stores.domain.financial.LayawaySummaryEntry;
import oracle.retail.stores.domain.financial.LayawaySummaryEntryIfc;
import oracle.retail.stores.domain.financial.Payment;
import oracle.retail.stores.domain.financial.PaymentIfc;
import oracle.retail.stores.domain.financial.ReconcilableCount;
import oracle.retail.stores.domain.financial.ReconcilableCountIfc;
import oracle.retail.stores.domain.financial.Register;
import oracle.retail.stores.domain.financial.RegisterIfc;
import oracle.retail.stores.domain.financial.ReportingPeriod;
import oracle.retail.stores.domain.financial.ReportingPeriodIfc;
import oracle.retail.stores.domain.financial.ShippingMethod;
import oracle.retail.stores.domain.financial.ShippingMethodIfc;
import oracle.retail.stores.domain.financial.StoreSafe;
import oracle.retail.stores.domain.financial.StoreSafeIfc;
import oracle.retail.stores.domain.financial.StoreStatus;
import oracle.retail.stores.domain.financial.StoreStatusAndTotals;
import oracle.retail.stores.domain.financial.StoreStatusAndTotalsIfc;
import oracle.retail.stores.domain.financial.StoreStatusIfc;
import oracle.retail.stores.domain.financial.TaxTotals;
import oracle.retail.stores.domain.financial.TaxTotalsContainer;
import oracle.retail.stores.domain.financial.TaxTotalsContainerIfc;
import oracle.retail.stores.domain.financial.TaxTotalsIfc;
import oracle.retail.stores.domain.financial.Till;
import oracle.retail.stores.domain.financial.TillIfc;
import oracle.retail.stores.domain.financial.TimeIntervalActivity;
import oracle.retail.stores.domain.financial.TimeIntervalActivityIfc;
import oracle.retail.stores.domain.giftregistry.GiftRegistry;
import oracle.retail.stores.domain.giftregistry.GiftRegistryIfc;
import oracle.retail.stores.domain.ixretail.log.POSLogBatchGenerator;
import oracle.retail.stores.domain.ixretail.log.POSLogBatchGeneratorIfc;
import oracle.retail.stores.domain.ixretail.log.POSLogTransactionEntry;
import oracle.retail.stores.domain.ixretail.log.POSLogTransactionEntryIfc;
import oracle.retail.stores.domain.job.ActiveJob;
import oracle.retail.stores.domain.job.ActiveJobIfc;
import oracle.retail.stores.domain.job.NotificationRecipients;
import oracle.retail.stores.domain.job.NotificationRecipientsIfc;
import oracle.retail.stores.domain.job.ScheduledJob;
import oracle.retail.stores.domain.job.ScheduledJobIfc;
import oracle.retail.stores.domain.job.message.JobControlEventMessage;
import oracle.retail.stores.domain.job.message.JobControlEventMessageIfc;
import oracle.retail.stores.domain.job.schedule.CustomScheduleDocument;
import oracle.retail.stores.domain.job.schedule.CustomScheduleDocumentIfc;
import oracle.retail.stores.domain.job.schedule.DailyScheduleDocument;
import oracle.retail.stores.domain.job.schedule.DailyScheduleDocumentIfc;
import oracle.retail.stores.domain.job.schedule.MonthlyByDayScheduleDocument;
import oracle.retail.stores.domain.job.schedule.MonthlyByDayScheduleDocumentIfc;
import oracle.retail.stores.domain.job.schedule.WeeklyScheduleDocument;
import oracle.retail.stores.domain.job.schedule.WeeklyScheduleDocumentIfc;
import oracle.retail.stores.domain.job.task.TaskInfo;
import oracle.retail.stores.domain.job.task.TaskInfoIfc;
import oracle.retail.stores.domain.lineitem.ItemContainerProxy;
import oracle.retail.stores.domain.lineitem.ItemContainerProxyIfc;
import oracle.retail.stores.domain.lineitem.ItemPrice;
import oracle.retail.stores.domain.lineitem.ItemPriceIfc;
import oracle.retail.stores.domain.lineitem.ItemTax;
import oracle.retail.stores.domain.lineitem.ItemTaxIfc;
import oracle.retail.stores.domain.lineitem.KitComponentLineItem;
import oracle.retail.stores.domain.lineitem.KitComponentLineItemIfc;
import oracle.retail.stores.domain.lineitem.KitHeaderLineItem;
import oracle.retail.stores.domain.lineitem.KitHeaderLineItemIfc;
import oracle.retail.stores.domain.lineitem.OrderItemStatus;
import oracle.retail.stores.domain.lineitem.OrderItemStatusIfc;
import oracle.retail.stores.domain.lineitem.OrderLineItem;
import oracle.retail.stores.domain.lineitem.OrderLineItemIfc;
import oracle.retail.stores.domain.lineitem.PriceAdjustmentLineItem;
import oracle.retail.stores.domain.lineitem.PriceAdjustmentLineItemIfc;
import oracle.retail.stores.domain.lineitem.ReturnItem;
import oracle.retail.stores.domain.lineitem.ReturnItemIfc;
import oracle.retail.stores.domain.lineitem.ReturnResponseLineItem;
import oracle.retail.stores.domain.lineitem.ReturnResponseLineItemIfc;
import oracle.retail.stores.domain.lineitem.SaleReturnLineItem;
import oracle.retail.stores.domain.lineitem.SaleReturnLineItemIfc;
import oracle.retail.stores.domain.lineitem.SendPackageLineItem;
import oracle.retail.stores.domain.lineitem.SendPackageLineItemIfc;
import oracle.retail.stores.domain.manager.datareplication.DataReplicationBatchGenerator;
import oracle.retail.stores.domain.manager.datareplication.DataReplicationBatchGeneratorIfc;
import oracle.retail.stores.domain.manager.datareplication.DataReplicationCustomerEntry;
import oracle.retail.stores.domain.manager.datareplication.DataReplicationCustomerEntryIfc;
import oracle.retail.stores.domain.manager.report.ReportBean;
import oracle.retail.stores.domain.manager.report.ReportBeanIfc;
import oracle.retail.stores.domain.order.Order;
import oracle.retail.stores.domain.order.OrderDeliveryDetail;
import oracle.retail.stores.domain.order.OrderDeliveryDetailIfc;
import oracle.retail.stores.domain.order.OrderIfc;
import oracle.retail.stores.domain.order.OrderRecipient;
import oracle.retail.stores.domain.order.OrderRecipientIfc;
import oracle.retail.stores.domain.order.OrderStatus;
import oracle.retail.stores.domain.order.OrderStatusIfc;
import oracle.retail.stores.domain.order.OrderSummaryEntry;
import oracle.retail.stores.domain.order.OrderSummaryEntryIfc;
import oracle.retail.stores.domain.purchasing.PurchaseOrder;
import oracle.retail.stores.domain.purchasing.PurchaseOrderIfc;
import oracle.retail.stores.domain.purchasing.PurchaseOrderLineItem;
import oracle.retail.stores.domain.purchasing.PurchaseOrderLineItemIfc;
import oracle.retail.stores.domain.purchasing.Supplier;
import oracle.retail.stores.domain.purchasing.SupplierIfc;
import oracle.retail.stores.domain.registry.RegistryIDIfc;
import oracle.retail.stores.domain.returns.ReturnTenderDataContainer;
import oracle.retail.stores.domain.returns.ReturnTenderDataElement;
import oracle.retail.stores.domain.stock.AlterationPLUItem;
import oracle.retail.stores.domain.stock.AlterationPLUItemIfc;
import oracle.retail.stores.domain.stock.GiftCardPLUItem;
import oracle.retail.stores.domain.stock.GiftCardPLUItemIfc;
import oracle.retail.stores.domain.stock.GiftCertificateItem;
import oracle.retail.stores.domain.stock.GiftCertificateItemIfc;
import oracle.retail.stores.domain.stock.Item;
import oracle.retail.stores.domain.stock.ItemClassification;
import oracle.retail.stores.domain.stock.ItemClassificationIfc;
import oracle.retail.stores.domain.stock.ItemColor;
import oracle.retail.stores.domain.stock.ItemColorIfc;
import oracle.retail.stores.domain.stock.ItemIfc;
import oracle.retail.stores.domain.stock.ItemImage;
import oracle.retail.stores.domain.stock.ItemImageIfc;
import oracle.retail.stores.domain.stock.ItemInfo;
import oracle.retail.stores.domain.stock.ItemInfoIfc;
import oracle.retail.stores.domain.stock.ItemInquirySearchCriteria;
import oracle.retail.stores.domain.stock.ItemInquirySearchCriteriaIfc;
import oracle.retail.stores.domain.stock.ItemKit;
import oracle.retail.stores.domain.stock.ItemKitIfc;
import oracle.retail.stores.domain.stock.ItemSize;
import oracle.retail.stores.domain.stock.ItemSizeIfc;
import oracle.retail.stores.domain.stock.ItemStyle;
import oracle.retail.stores.domain.stock.ItemStyleIfc;
import oracle.retail.stores.domain.stock.ItemType;
import oracle.retail.stores.domain.stock.ItemTypeIfc;
import oracle.retail.stores.domain.stock.KitComponent;
import oracle.retail.stores.domain.stock.KitComponentIfc;
import oracle.retail.stores.domain.stock.Manufacturer;
import oracle.retail.stores.domain.stock.ManufacturerIfc;
import oracle.retail.stores.domain.stock.MerchandiseClassification;
import oracle.retail.stores.domain.stock.MerchandiseClassificationIfc;
import oracle.retail.stores.domain.stock.PLUItem;
import oracle.retail.stores.domain.stock.PLUItemIfc;
import oracle.retail.stores.domain.stock.Product;
import oracle.retail.stores.domain.stock.ProductGroup;
import oracle.retail.stores.domain.stock.ProductGroupIfc;
import oracle.retail.stores.domain.stock.ProductIfc;
import oracle.retail.stores.domain.stock.StockItem;
import oracle.retail.stores.domain.stock.StockItemIfc;
import oracle.retail.stores.domain.stock.UnitOfMeasure;
import oracle.retail.stores.domain.stock.UnitOfMeasureIfc;
import oracle.retail.stores.domain.stock.UnknownItem;
import oracle.retail.stores.domain.stock.UnknownItemIfc;
import oracle.retail.stores.domain.stock.classification.MerchandiseHierarchyGroup;
import oracle.retail.stores.domain.stock.classification.MerchandiseHierarchyGroupIfc;
import oracle.retail.stores.domain.stock.classification.MerchandiseHierarchyLevel;
import oracle.retail.stores.domain.stock.classification.MerchandiseHierarchyLevelIfc;
import oracle.retail.stores.domain.stock.classification.MerchandiseHierarchyLevelKey;
import oracle.retail.stores.domain.stock.classification.MerchandiseHierarchyLevelKeyIfc;
import oracle.retail.stores.domain.stock.classification.MerchandiseHierarchyTree;
import oracle.retail.stores.domain.stock.classification.MerchandiseHierarchyTreeIfc;
import oracle.retail.stores.domain.store.Department;
import oracle.retail.stores.domain.store.DepartmentIfc;
import oracle.retail.stores.domain.store.District;
import oracle.retail.stores.domain.store.DistrictIfc;
import oracle.retail.stores.domain.store.Region;
import oracle.retail.stores.domain.store.RegionIfc;
import oracle.retail.stores.domain.store.Store;
import oracle.retail.stores.domain.store.StoreIfc;
import oracle.retail.stores.domain.store.Workstation;
import oracle.retail.stores.domain.store.WorkstationIfc;
import oracle.retail.stores.domain.supply.SupplyCategory;
import oracle.retail.stores.domain.supply.SupplyCategoryIfc;
import oracle.retail.stores.domain.supply.SupplyItem;
import oracle.retail.stores.domain.supply.SupplyItemIfc;
import oracle.retail.stores.domain.supply.SupplyItemSearchCriteria;
import oracle.retail.stores.domain.supply.SupplyItemSearchCriteriaIfc;
import oracle.retail.stores.domain.supply.SupplyOrder;
import oracle.retail.stores.domain.supply.SupplyOrderIfc;
import oracle.retail.stores.domain.supply.SupplyOrderLineItem;
import oracle.retail.stores.domain.supply.SupplyOrderLineItemIfc;
import oracle.retail.stores.domain.tax.ExciseTaxRule;
import oracle.retail.stores.domain.tax.ExciseTaxRuleIfc;
import oracle.retail.stores.domain.tax.FixedAmountTaxCalculator;
import oracle.retail.stores.domain.tax.FixedAmountTaxCalculatorIfc;
import oracle.retail.stores.domain.tax.InclusiveTaxRateCalculator;
import oracle.retail.stores.domain.tax.InternalTaxEngine;
import oracle.retail.stores.domain.tax.NewTaxRuleIfc;
import oracle.retail.stores.domain.tax.OverrideItemTaxByAmountRule;
import oracle.retail.stores.domain.tax.OverrideItemTaxByAmountRuleIfc;
import oracle.retail.stores.domain.tax.OverrideItemTaxByRateRule;
import oracle.retail.stores.domain.tax.OverrideItemTaxByRateRuleIfc;
import oracle.retail.stores.domain.tax.OverrideItemTaxRuleIfc;
import oracle.retail.stores.domain.tax.OverrideItemTaxToggleOffRule;
import oracle.retail.stores.domain.tax.OverrideTransactionTaxByAmountRule;
import oracle.retail.stores.domain.tax.OverrideTransactionTaxByAmountRuleIfc;
import oracle.retail.stores.domain.tax.OverrideTransactionTaxByRateProrateRule;
import oracle.retail.stores.domain.tax.OverrideTransactionTaxByRateRuleIfc;
import oracle.retail.stores.domain.tax.ReturnTaxCalculator;
import oracle.retail.stores.domain.tax.ReturnTaxCalculatorIfc;
import oracle.retail.stores.domain.tax.ReverseItemTaxRule;
import oracle.retail.stores.domain.tax.ReverseItemTaxRuleIfc;
import oracle.retail.stores.domain.tax.ReverseTaxCalculator;
import oracle.retail.stores.domain.tax.ReverseTaxCalculatorIfc;
import oracle.retail.stores.domain.tax.TableTaxRule;
import oracle.retail.stores.domain.tax.TableTaxRuleIfc;
import oracle.retail.stores.domain.tax.TaxByLineRule;
import oracle.retail.stores.domain.tax.TaxEngineIfc;
import oracle.retail.stores.domain.tax.TaxExemptTaxRule;
import oracle.retail.stores.domain.tax.TaxExemptTaxRuleIfc;
import oracle.retail.stores.domain.tax.TaxInformation;
import oracle.retail.stores.domain.tax.TaxInformationContainer;
import oracle.retail.stores.domain.tax.TaxInformationContainerIfc;
import oracle.retail.stores.domain.tax.TaxInformationIfc;
import oracle.retail.stores.domain.tax.TaxProrateRule;
import oracle.retail.stores.domain.tax.TaxRateCalculator;
import oracle.retail.stores.domain.tax.TaxRateCalculatorIfc;
import oracle.retail.stores.domain.tax.TaxRuleItemContainer;
import oracle.retail.stores.domain.tax.TaxRuleItemContainerIfc;
import oracle.retail.stores.domain.tax.TaxTableLineItem;
import oracle.retail.stores.domain.tax.TaxTableLineItemIfc;
import oracle.retail.stores.domain.tax.ValueAddedTaxByLineRule;
import oracle.retail.stores.domain.tax.ValueAddedTaxProrateRule;
import oracle.retail.stores.domain.tax.ValueAddedTaxRuleIfc;
import oracle.retail.stores.domain.tender.TenderCash;
import oracle.retail.stores.domain.tender.TenderCashIfc;
import oracle.retail.stores.domain.tender.TenderCharge;
import oracle.retail.stores.domain.tender.TenderChargeIfc;
import oracle.retail.stores.domain.tender.TenderCheck;
import oracle.retail.stores.domain.tender.TenderCheckIfc;
import oracle.retail.stores.domain.tender.TenderCoupon;
import oracle.retail.stores.domain.tender.TenderCouponIfc;
import oracle.retail.stores.domain.tender.TenderDebit;
import oracle.retail.stores.domain.tender.TenderDebitIfc;
import oracle.retail.stores.domain.tender.TenderDescriptor;
import oracle.retail.stores.domain.tender.TenderDescriptorIfc;
import oracle.retail.stores.domain.tender.TenderGiftCard;
import oracle.retail.stores.domain.tender.TenderGiftCardIfc;
import oracle.retail.stores.domain.tender.TenderGiftCertificate;
import oracle.retail.stores.domain.tender.TenderGiftCertificateIfc;
import oracle.retail.stores.domain.tender.TenderLimits;
import oracle.retail.stores.domain.tender.TenderLimitsIfc;
import oracle.retail.stores.domain.tender.TenderMailBankCheck;
import oracle.retail.stores.domain.tender.TenderMailBankCheckIfc;
import oracle.retail.stores.domain.tender.TenderMoneyOrder;
import oracle.retail.stores.domain.tender.TenderMoneyOrderIfc;
import oracle.retail.stores.domain.tender.TenderPurchaseOrder;
import oracle.retail.stores.domain.tender.TenderPurchaseOrderIfc;
import oracle.retail.stores.domain.tender.TenderStoreCredit;
import oracle.retail.stores.domain.tender.TenderStoreCreditIfc;
import oracle.retail.stores.domain.tender.TenderTravelersCheck;
import oracle.retail.stores.domain.tender.TenderTravelersCheckIfc;
import oracle.retail.stores.domain.transaction.BankDepositTransaction;
import oracle.retail.stores.domain.transaction.BankDepositTransactionIfc;
import oracle.retail.stores.domain.transaction.BillPayTransaction;
import oracle.retail.stores.domain.transaction.BillPayTransactionIfc;
import oracle.retail.stores.domain.transaction.InstantCreditTransaction;
import oracle.retail.stores.domain.transaction.InstantCreditTransactionIfc;
import oracle.retail.stores.domain.transaction.ItemSummary;
import oracle.retail.stores.domain.transaction.ItemSummaryIfc;
import oracle.retail.stores.domain.transaction.LayawayPaymentTransaction;
import oracle.retail.stores.domain.transaction.LayawayPaymentTransactionIfc;
import oracle.retail.stores.domain.transaction.LayawayTransaction;
import oracle.retail.stores.domain.transaction.LayawayTransactionIfc;
import oracle.retail.stores.domain.transaction.NoSaleTransaction;
import oracle.retail.stores.domain.transaction.NoSaleTransactionIfc;
import oracle.retail.stores.domain.transaction.OrderTransaction;
import oracle.retail.stores.domain.transaction.OrderTransactionIfc;
import oracle.retail.stores.domain.transaction.PaymentTransaction;
import oracle.retail.stores.domain.transaction.PaymentTransactionIfc;
import oracle.retail.stores.domain.transaction.PurgeCriteria;
import oracle.retail.stores.domain.transaction.PurgeCriteriaIfc;
import oracle.retail.stores.domain.transaction.PurgeResult;
import oracle.retail.stores.domain.transaction.PurgeResultIfc;
import oracle.retail.stores.domain.transaction.PurgeTransactionEntry;
import oracle.retail.stores.domain.transaction.PurgeTransactionEntryIfc;
import oracle.retail.stores.domain.transaction.RedeemTransaction;
import oracle.retail.stores.domain.transaction.RedeemTransactionIfc;
import oracle.retail.stores.domain.transaction.RegisterOpenCloseTransaction;
import oracle.retail.stores.domain.transaction.RegisterOpenCloseTransactionIfc;
import oracle.retail.stores.domain.transaction.SaleReturnTransaction;
import oracle.retail.stores.domain.transaction.SaleReturnTransactionIfc;
import oracle.retail.stores.domain.transaction.SearchCriteria;
import oracle.retail.stores.domain.transaction.SearchCriteriaIfc;
import oracle.retail.stores.domain.transaction.StoreOpenCloseTransaction;
import oracle.retail.stores.domain.transaction.StoreOpenCloseTransactionIfc;
import oracle.retail.stores.domain.transaction.TillAdjustmentTransaction;
import oracle.retail.stores.domain.transaction.TillAdjustmentTransactionIfc;
import oracle.retail.stores.domain.transaction.TillOpenCloseTransaction;
import oracle.retail.stores.domain.transaction.TillOpenCloseTransactionIfc;
import oracle.retail.stores.domain.transaction.Transaction;
import oracle.retail.stores.domain.transaction.TransactionID;
import oracle.retail.stores.domain.transaction.TransactionIDIfc;
import oracle.retail.stores.domain.transaction.TransactionIfc;
import oracle.retail.stores.domain.transaction.TransactionKey;
import oracle.retail.stores.domain.transaction.TransactionKeyIfc;
import oracle.retail.stores.domain.transaction.TransactionSummary;
import oracle.retail.stores.domain.transaction.TransactionSummaryIfc;
import oracle.retail.stores.domain.transaction.TransactionTax;
import oracle.retail.stores.domain.transaction.TransactionTaxIfc;
import oracle.retail.stores.domain.transaction.TransactionTotals;
import oracle.retail.stores.domain.transaction.TransactionTotalsIfc;
import oracle.retail.stores.domain.transaction.VoidTransaction;
import oracle.retail.stores.domain.transaction.VoidTransactionIfc;
import oracle.retail.stores.domain.utility.Address;
import oracle.retail.stores.domain.utility.AddressIfc;
import oracle.retail.stores.domain.utility.Alteration;
import oracle.retail.stores.domain.utility.AlterationIfc;
import oracle.retail.stores.domain.utility.Card;
import oracle.retail.stores.domain.utility.CardIfc;
import oracle.retail.stores.domain.utility.CardType;
import oracle.retail.stores.domain.utility.CardTypeIfc;
import oracle.retail.stores.domain.utility.CodeEntry;
import oracle.retail.stores.domain.utility.CodeEntryIfc;
import oracle.retail.stores.domain.utility.CodeList;
import oracle.retail.stores.domain.utility.CodeListIfc;
import oracle.retail.stores.domain.utility.CodeListMap;
import oracle.retail.stores.domain.utility.CodeListMapIfc;
import oracle.retail.stores.domain.utility.Country;
import oracle.retail.stores.domain.utility.CountryIfc;
import oracle.retail.stores.domain.utility.DiscountTypeCodeEntry;
import oracle.retail.stores.domain.utility.DiscountTypeCodeEntryIfc;
import oracle.retail.stores.domain.utility.EYSDate;
import oracle.retail.stores.domain.utility.EYSStatus;
import oracle.retail.stores.domain.utility.EYSStatusIfc;
import oracle.retail.stores.domain.utility.EYSTime;
import oracle.retail.stores.domain.utility.EmailAddress;
import oracle.retail.stores.domain.utility.EmailAddressIfc;
import oracle.retail.stores.domain.utility.GiftCard;
import oracle.retail.stores.domain.utility.GiftCardIfc;
import oracle.retail.stores.domain.utility.HouseCard;
import oracle.retail.stores.domain.utility.HouseCardIfc;
import oracle.retail.stores.domain.utility.InstantCredit;
import oracle.retail.stores.domain.utility.InstantCreditIfc;
import oracle.retail.stores.domain.utility.Person;
import oracle.retail.stores.domain.utility.PersonIfc;
import oracle.retail.stores.domain.utility.PersonName;
import oracle.retail.stores.domain.utility.PersonNameIfc;
import oracle.retail.stores.domain.utility.Phone;
import oracle.retail.stores.domain.utility.PhoneIfc;
import oracle.retail.stores.domain.utility.RuleBinRange;
import oracle.retail.stores.domain.utility.RuleIfc;
import oracle.retail.stores.domain.utility.RuleLength;
import oracle.retail.stores.domain.utility.RuleMask;
import oracle.retail.stores.domain.utility.SecurityOverride;
import oracle.retail.stores.domain.utility.SecurityOverrideIfc;
import oracle.retail.stores.domain.utility.State;
import oracle.retail.stores.domain.utility.StateIfc;
import oracle.retail.stores.domain.utility.StoreCredit;
import oracle.retail.stores.domain.utility.StoreCreditIfc;
import oracle.retail.stores.domain.utility.StoreSearchCriteria;
import oracle.retail.stores.domain.utility.StoreSearchCriteriaIfc;
import oracle.retail.stores.domain.utility.calendar.AggregateCalendarLevel;
import oracle.retail.stores.domain.utility.calendar.BusinessCalendar;
import oracle.retail.stores.domain.utility.calendar.BusinessCalendarIfc;
import oracle.retail.stores.domain.utility.calendar.CalendarLevelFactory;
import oracle.retail.stores.domain.utility.calendar.CalendarLevelFactoryIfc;
import oracle.retail.stores.domain.utility.calendar.CalendarLevelIfc;
import oracle.retail.stores.domain.utility.calendar.CalendarLevelKey;
import oracle.retail.stores.domain.utility.calendar.CalendarLevelKeyIfc;
import oracle.retail.stores.domain.utility.calendar.CalendarPeriod;
import oracle.retail.stores.domain.utility.calendar.CalendarPeriodIfc;
import oracle.retail.stores.domain.utility.calendar.CalendarPeriodKey;
import oracle.retail.stores.domain.utility.calendar.CalendarPeriodKeyIfc;
import oracle.retail.stores.domain.utility.calendar.DayCalendarLevel;
import oracle.retail.stores.domain.utility.calendar.HourCalendarLevel;
import oracle.retail.stores.domain.utility.calendar.MinuteCalendarLevel;
import oracle.retail.stores.domain.utility.calendar.MonthDayCalendarLevel;
import oracle.retail.stores.domain.utility.calendar.NthWeekDayCalendarLevel;
import oracle.retail.stores.domain.utility.calendar.RootCalendarLevel;
import oracle.retail.stores.domain.utility.calendar.WeekDayCalendarLevel;
import oracle.retail.stores.domain.utility.calendar.YearDayCalendarLevel;
import oracle.retail.stores.foundation.utility.Util;

/**
 * This class contains all the methods of DomainObjectFactory that take a locale
 * as an argument. This was separated from the DomainObjectFactory class because
 * the file was becoming too large. All the real work for DomainObjectFactory is
 * done in this class, the DomainObjectFactory passes the default locale on to
 * its super method in in this class.
 *
 * @version $Revision: /rgbustores_13.4x_generic_branch/2 $
 */
public abstract class I18NDomainObjectFactory extends I18NDomainObjectsFactory
{
    /**
     * revision number supplied by source-code-control system
     */
    public static final String revisionNumber = "$Revision: /rgbustores_13.4x_generic_branch/2 $";

    /**
     * Default constructor
     */
    public I18NDomainObjectFactory()
    {
        setLocale(LocaleMap.getLocale(LocaleMap.DEFAULT));
    }

    /**
     * Constructs I18NDomainObjectFactory object. <P>
     * @param locale Locale to get an object for. locale to use
     */
    public I18NDomainObjectFactory(Locale locale)
    {
        super(locale);
    }

    /**
     * Returns instance of LocalizedTextIfc class initialized to empty strings
     * for all supported locales.
     *
     * @param locale Locale to get an object for
     * @return LocalizedTextIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getLocalizedText(Locale)
     */
    public LocalizedTextIfc getLocalizedText(Locale locale)
    {
        LocalizedTextIfc returnValue = new LocalizedText();
        returnValue.initialize(new Locale[]{new Locale("en")}, "");
        return returnValue;
    }

    /**
     * Returns instance of ItemSummaryIfc class. <P>
     *
     * @param locale Locale to get an object for. Locale of the object to return
     * @return ItemSummaryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemSummaryInstance(java.util.Locale)
     */
    public ItemSummaryIfc getItemSummaryInstance(Locale locale)
    {
        return new ItemSummary();
    }

    /**
     * Returns instance of AlertEntryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return AlertEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getAlertEntryInstance(java.util.Locale)
     */
    public AlertEntryIfc getAlertEntryInstance(Locale locale)
    {
        return new AlertEntry();
    }

    /**
     * Returns instance of AlertListIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return AlertListIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getAlertListInstance(java.util.Locale)
     */
    public AlertListIfc getAlertListInstance(Locale locale)
    {
        return new AlertList();
    }

    /**
     * Returns instance of CountryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CountryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCountryInstance(java.util.Locale)
     */
    public CountryIfc getCountryInstance(Locale locale)
    {
        return new Country();
    }

    /**
     * Returns instance of StateIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return StateIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getStateInstance(java.util.Locale)
     */
    public StateIfc getStateInstance(Locale locale)
    {
        return new State();
    }

    /**
     * Returns instance of CurrencyTypeIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CurrencyTypeIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCurrencyTypeInstance(java.util.Locale)
     */
    public CurrencyTypeIfc getCurrencyTypeInstance(Locale locale)
    {
        return new CurrencyType();
    }

    /**
     * Returns instance of CurrencyIfc class. <P>
     *
     * @param currencyType type of currency to return
     * @return CurrencyIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCurrencyInstance(CurrencyTypeIfc)
     */
    public CurrencyIfc getCurrencyInstance(CurrencyTypeIfc currencyType)
    {
        return new CurrencyDecimal(currencyType);
    }

    /**
     * Returns instance of CurrencyTypeListIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CurrencyTypeListIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCurrencyTypeListInstance(java.util.Locale)
     */
    public CurrencyTypeListIfc getCurrencyTypeListInstance(Locale locale)
    {
        return new CurrencyTypeList();
    }

    /**
     * Returns instance of CustomerGroupIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CustomerGroupIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCustomerGroupInstance(java.util.Locale)
     */
    public CustomerGroupIfc getCustomerGroupInstance(Locale locale)
    {
        return new CustomerGroup();
    }

    /**
     * Returns instance of CustomerIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CustomerIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCustomerInstance(java.util.Locale)
     */
    public CustomerIfc getCustomerInstance(Locale locale)
    {
        return new Customer();
    }

    /**
     * Returns an instance of the CaptureCustomerIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CaptureCustomerIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCaptureCustomerInstance(java.util.Locale)
     */
    public CaptureCustomerIfc getCaptureCustomerInstance(Locale locale)
    {
        return new CaptureCustomer();
    }

    /**
     * Returns instance of CustomerEventBabyIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CustomerEventBabyIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCustomerEventBabyInstance(java.util.Locale)
     */
    public CustomerEventBabyIfc getCustomerEventBabyInstance(Locale locale)
    {
        return new CustomerEventBaby();
    }

    /**
     * Returns instance of CustomerEventSpecialIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CustomerEventSpecialIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCustomerEventSpecialInstance(java.util.Locale)
     */
    public CustomerEventSpecialIfc getCustomerEventSpecialInstance(Locale locale)
    {
        return new CustomerEventSpecial();
    }

    /**
     * Returns instance of CustomerEventWeddingIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CustomerEventWeddingIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCustomerEventWeddingInstance(java.util.Locale)
     */
    public CustomerEventWeddingIfc getCustomerEventWeddingInstance(Locale locale)
    {
        return new CustomerEventWedding();
    }

    /**
     * Returns instance of MerchandisePreferenceIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return MerchandisePreferenceIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getMerchandisePreferenceInstance(java.util.Locale)
     */
    public MerchandisePreferenceIfc getMerchandisePreferenceInstance(Locale locale)
    {
        return new MerchandisePreference();
    }

    /**
     * Returns instance of AdvancedPricingRuleIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return AdvancedPricingRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getAdvancedPricingRuleInstance(java.util.Locale)
     */
    public AdvancedPricingRuleIfc getAdvancedPricingRuleInstance(Locale locale)
    {
        return new AdvancedPricingRule();
    }

    /**
     * Returns instance of BestDealGroupIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return BestDealGroupIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getBestDealGroupInstance(java.util.Locale)
     */
    public BestDealGroupIfc getBestDealGroupInstance(Locale locale)
    {
        return new BestDealGroup();
    }

    /**
     * Returns instance of SuperGroupIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return SuperGroupIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSuperGroupInstance(java.util.Locale)
     */
    public SuperGroupIfc getSuperGroupInstance(Locale locale)
    {
        return new SuperGroup();
    }

    /**
     * Returns instance of CustomerDiscountByPercentageIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CustomerDiscountByPercentageIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCustomerDiscountByPercentageInstance(java.util.Locale)
     */
    public CustomerDiscountByPercentageIfc getCustomerDiscountByPercentageInstance(Locale locale)
    {
        return new CustomerDiscountByPercentage();
    }

    /**
     * Returns instance of DiscountCalculationIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DiscountCalculationIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDiscountCalculationInstance(java.util.Locale)
     */
    public DiscountCalculationIfc getDiscountCalculationInstance(Locale locale)
    {
        return new StandardDiscountCalculation();
    }

    /**
     * Returns instance of DiscountRuleIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DiscountRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDiscountRuleInstance(java.util.Locale)
     */
    public DiscountRuleIfc getDiscountRuleInstance(Locale locale)
    {
        return new DiscountRule();
    }

    /**
     * Returns instance of DiscountListIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DiscountListIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDiscountListInstance(java.util.Locale)
     */
    public DiscountListIfc getDiscountListInstance(Locale locale)
    {
        return new DiscountList();
    }

    /**
     * Returns instance of SourceCriteria class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DiscountListIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSourceCriteriaInstance(java.util.Locale)
     */
    public DiscountListIfc getSourceCriteriaInstance(Locale locale)
    {
        return new SourceCriteria();
    }

    /**
     * Returns instance of TargetCriteria class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DiscountListIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTargetCriteriaInstance(java.util.Locale)
     */
    public DiscountListIfc getTargetCriteriaInstance(Locale locale)
    {
        return new TargetCriteria();
    }

    /**
     * Returns instance of DiscountListEntryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DiscountListEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDiscountListEntryInstance(java.util.Locale)
     */
    public DiscountListEntryIfc getDiscountListEntryInstance(Locale locale)
    {
        return new DiscountListEntry();
    }

    /**
     * Returns instance of CodeEntryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CodeEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDiscountTypeCodeEntryInstance(java.util.Locale)
     */
    public DiscountTypeCodeEntryIfc getDiscountTypeCodeEntryInstance(Locale locale)
    {
        return new DiscountTypeCodeEntry();
    }

    /**
     * Returns instance of ItemDiscountByAmountIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemDiscountByAmountIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemDiscountByAmountInstance(java.util.Locale)
     */
    public ItemDiscountByAmountIfc getItemDiscountByAmountInstance(Locale locale)
    {
        return new ItemDiscountByAmountStrategy();
    }

    /**
     * Returns instance of ItemDiscountByPercentageIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemDiscountByPercentageIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemDiscountByPercentageInstance(java.util.Locale)
     */
    public ItemDiscountByPercentageIfc getItemDiscountByPercentageInstance(Locale locale)
    {
        return new ItemDiscountByPercentageStrategy();
    }

    /**
     * Returns instance of ItemDiscountByFixedPriceStrategy class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemDiscountByAmountIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemDiscountByFixedPriceStrategyInstance(java.util.Locale)
     */
    public ItemDiscountByAmountIfc getItemDiscountByFixedPriceStrategyInstance(Locale locale)
    {
        return new ItemDiscountByFixedPriceStrategy();
    }

    /**
     * Returns instance of ItemTransactionDiscountAuditIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemTransactionDiscountAuditIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemTransactionDiscountAuditInstance(java.util.Locale)
     */
    public ItemTransactionDiscountAuditIfc getItemTransactionDiscountAuditInstance(Locale locale)
    {
        return new ItemTransactionDiscountAudit();
    }

    /**
     * Returns instance of ReturnItemTransactionDiscountAuditIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ReturnItemTransactionDiscountAuditIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReturnItemTransactionDiscountAuditInstance(java.util.Locale)
     */
    public ReturnItemTransactionDiscountAuditIfc getReturnItemTransactionDiscountAuditInstance(Locale locale)
    {
        return new ReturnItemTransactionDiscountAudit();
    }

    /**
     * Returns instance of TransactionDiscountByAmountIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TransactionDiscountByAmountIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTransactionDiscountByAmountInstance(java.util.Locale)
     */
    public TransactionDiscountByAmountIfc getTransactionDiscountByAmountInstance(Locale locale)
    {
        return new TransactionDiscountByAmountStrategy();
    }

    /**
     * Returns instance of TransactionDiscountByPercentageIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TransactionDiscountByPercentageIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTransactionDiscountByPercentageInstance(java.util.Locale)
     */
    public TransactionDiscountByPercentageIfc getTransactionDiscountByPercentageInstance(Locale locale)
    {
        return new TransactionDiscountByPercentageStrategy();
    }

    /**
     * Returns instance of EMessageIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return EMessageIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getEMessageInstance(java.util.Locale)
     */
    public EMessageIfc getEMessageInstance(Locale locale)
    {
        return new EMessage();
    }

    /**
     * Returns instance of EmployeeIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return EmployeeIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getEmployeeInstance(java.util.Locale)
     */
    public EmployeeIfc getEmployeeInstance(Locale locale)
    {
        return new Employee();
    }

    /**
     * Returns instance of Password Policy Evaluator. <P>
     *
     * @param locale Locale to get an object for.
     * @return PasswordPolicyEvaluatorIfc instance
     */
    public PasswordPolicyEvaluatorIfc getPasswordPolicyEvaluatorInstance(Locale locale)
    {
        return new SinglePasswordPolicyEvaluator();
    }

    /**
     * Returns instance of Employee Compliance. <P>
     *
     * @param locale Locale to get an object for.
     * @return EmployeeComplianceIfc instance
     */
    public EmployeeComplianceIfc getEmployeeCompliance(Locale locale)
    {
        return new SinglePasswordPolicyComplianceObject();
    }

    /**
     * Returns instance of EmployeeClockEntryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return EmployeeClockEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getEmployeeClockEntryInstance(java.util.Locale)
     */
    public EmployeeClockEntryIfc getEmployeeClockEntryInstance(Locale locale)
    {
        return new EmployeeClockEntry();
    }

    /**
     * Returns instance of RoleFunctionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return RoleFunctionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRoleFunctionInstance(java.util.Locale)
     */
    public RoleFunctionIfc getRoleFunctionInstance(Locale locale)
    {
        return new RoleFunction();
    }

    /**
     * Returns instance of RoleFunctionGroupIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return RoleFunctionGroupIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRoleFunctionGroupInstance(java.util.Locale)
     */
    public RoleFunctionGroupIfc getRoleFunctionGroupInstance(Locale locale)
    {
        return new RoleFunctionGroup();
    }

    /**
     * Returns instance of RoleIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return RoleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRoleInstance(java.util.Locale)
     */
    public RoleIfc getRoleInstance(Locale locale)
    {
        return new Role();
    }

    /**
     * Returns instance of ItemMaintenanceEventIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemMaintenanceEventIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemMaintenanceEventInstance(java.util.Locale)
     */
    public ItemMaintenanceEventIfc getItemMaintenanceEventInstance(Locale locale)
    {
        return new ItemMaintenanceEvent();
    }

    /**
     * Returns instance of ItemPriceMaintenanceEventIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemPriceMaintenanceEventIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemPriceMaintenanceEventInstance(java.util.Locale)
     */
    public ItemPriceMaintenanceEventIfc getItemPriceMaintenanceEventInstance(Locale locale)
    {
        return new ItemPriceMaintenanceEvent();
    }

    /**
     * Returns instance of MaintenanceEventIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return MaintenanceEventIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getMaintenanceEventInstance(java.util.Locale)
     */
    public MaintenanceEventIfc getMaintenanceEventInstance(Locale locale)
    {
        return new MaintenanceEvent();
    }

    /**
     * Returns instance of PriceChangeIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PriceChangeIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPriceChangeInstance(java.util.Locale)
     */
    public PriceChangeIfc getPriceChangeInstance(Locale locale)
    {
        return new PriceChange();
    }

    /**
     * Returns instance of PriceDerivationRuleMaintenanceEventIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PriceDerivationRuleMaintenanceEventIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPriceDerivationRuleMaintenanceEventInstance(java.util.Locale)
     */
    public PriceDerivationRuleMaintenanceEventIfc getPriceDerivationRuleMaintenanceEventInstance(Locale locale)
    {
        return new PriceDerivationRuleMaintenanceEvent();
    }

    /**
     * Returns instance of DepartmentActivityIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DepartmentActivityIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDepartmentActivityInstance(java.util.Locale)
     */
    public DepartmentActivityIfc getDepartmentActivityInstance(Locale locale)
    {
        return new DepartmentActivity();
    }

    /**
     * Returns instance of DrawerIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DrawerIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDrawerInstance(java.util.Locale)
     */
    public DrawerIfc getDrawerInstance(Locale locale)
    {
        return new Drawer();
    }

    /**
     * Returns instance of EmployeeActivityIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return EmployeeActivityIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getEmployeeActivityInstance(java.util.Locale)
     */
    public EmployeeActivityIfc getEmployeeActivityInstance(Locale locale)
    {
        return new EmployeeActivity();
    }

    /**
     * Returns instance of FinancialCountIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return FinancialCountIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getFinancialCountInstance(java.util.Locale)
     */
    public FinancialCountIfc getFinancialCountInstance(Locale locale)
    {
        return new FinancialCount();
    }

    /**
     * Returns instance of FinancialCountTenderItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return FinancialCountTenderItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getFinancialCountTenderItemInstance(java.util.Locale)
     */
    public FinancialCountTenderItemIfc getFinancialCountTenderItemInstance(Locale locale)
    {
        return new FinancialCountTenderItem();
    }

    /**
     * Returns instance of FinancialTotalsIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return FinancialTotalsIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getFinancialTotalsInstance(java.util.Locale)
     */
    public FinancialTotalsIfc getFinancialTotalsInstance(Locale locale)
    {
        return new FinancialTotals();
    }

    /**
     * Returns instance of HardTotalsIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return HardTotalsIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getHardTotalsInstance(java.util.Locale)
     */
    public HardTotalsIfc getHardTotalsInstance(Locale locale)
    {
        return new HardTotals();
    }

    /**
     * Returns instance of HardTotalsBuilderIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return HardTotalsBuilderIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getHardTotalsBuilderInstance(java.util.Locale)
     */
    public HardTotalsBuilderIfc getHardTotalsBuilderInstance(Locale locale)
    {
        return new HardTotalsStringBuilder();
    }

    /**
     * Returns instance of LayawayIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return LayawayIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getLayawayInstance(java.util.Locale)
     */
    public LayawayIfc getLayawayInstance(Locale locale)
    {
        return new Layaway();
    }

    /**
     * Returns instance of LayawaySummaryEntryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return LayawaySummaryEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getLayawaySummaryEntryInstance(java.util.Locale)
     */
    public LayawaySummaryEntryIfc getLayawaySummaryEntryInstance(Locale locale)
    {
        return new LayawaySummaryEntry();
    }

    /**
     * Returns instance of PaymentIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PaymentIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPaymentInstance(java.util.Locale)
     */
    public PaymentIfc getPaymentInstance(Locale locale)
    {
        return new Payment();
    }

    /**
     * Returns instance of ReconcilableCountIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ReconcilableCountIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReconcilableCountInstance(java.util.Locale)
     */
    public ReconcilableCountIfc getReconcilableCountInstance(Locale locale)
    {
        return new ReconcilableCount();
    }

    /**
     * Returns instance of RegisterIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return RegisterIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRegisterInstance(java.util.Locale)
     */
    public RegisterIfc getRegisterInstance(Locale locale)
    {
        return new Register();
    }

    /**
     * Returns instance of ReportingPeriodIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ReportingPeriodIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReportingPeriodInstance(java.util.Locale)
     */
    public ReportingPeriodIfc getReportingPeriodInstance(Locale locale)
    {
        return new ReportingPeriod();
    }

    /**
     * Returns instance of StoreStatusAndTotalsIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return StoreStatusAndTotalsIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getStoreStatusAndTotalsInstance(java.util.Locale)
     */
    public StoreStatusAndTotalsIfc getStoreStatusAndTotalsInstance(Locale locale)
    {
        return new StoreStatusAndTotals();
    }

    /**
     * Returns instance of StoreStatusIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return StoreStatusIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getStoreStatusInstance(java.util.Locale)
     */
    public StoreStatusIfc getStoreStatusInstance(Locale locale)
    {
        return new StoreStatus();
    }

    /**
     * Returns instance of TillIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TillIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTillInstance(java.util.Locale)
     */
    public TillIfc getTillInstance(Locale locale)
    {
        return new Till();
    }

    /**
     * Returns instance of TimeIntervalActivityIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TimeIntervalActivityIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTimeIntervalActivityInstance(java.util.Locale)
     */
    public TimeIntervalActivityIfc getTimeIntervalActivityInstance(Locale locale)
    {
        return new TimeIntervalActivity();
    }

    /**
     * Returns instance of SupplyOrderIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return SupplyOrderIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSupplyOrderInstance(java.util.Locale)
     */
    public SupplyOrderIfc getSupplyOrderInstance(Locale locale)
    {
        return new SupplyOrder();
    }

    /**
     * Returns instance of SupplyOrderLineItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return SupplyOrderLineItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSupplyOrderLineItemInstance(java.util.Locale)
     */
    public SupplyOrderLineItemIfc getSupplyOrderLineItemInstance(Locale locale)
    {
        return new SupplyOrderLineItem();
    }

    /**
     * Returns instance of SupplyItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return SupplyItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSupplyItemInstance(java.util.Locale)
     */
    public SupplyItemIfc getSupplyItemInstance(Locale locale)
    {
        return new SupplyItem();
    }

    /**
     * Returns instance of SupplyItemSearchCriteriaIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return SupplyItemSearchCriteriaIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSupplyItemSearchCriteriaInstance(java.util.Locale)
     */
    public SupplyItemSearchCriteriaIfc getSupplyItemSearchCriteriaInstance(Locale locale)
    {
        return new SupplyItemSearchCriteria();
    }

    /**
     * Returns instance of SupplyCategoryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return SupplyCategoryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSupplyCategoryInstance(java.util.Locale)
     */
    public SupplyCategoryIfc getSupplyCategoryInstance(Locale locale)
    {
        return new SupplyCategory();
    }


    /**
     * Returns instance of ItemContainerProxyIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemContainerProxyIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemContainerProxyInstance(java.util.Locale)
     */
    public ItemContainerProxyIfc getItemContainerProxyInstance(Locale locale)
    {
        return new ItemContainerProxy();
    }

    /**
     * Returns instance of ItemPriceIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemPriceIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemPriceInstance(java.util.Locale)
     */
    public ItemPriceIfc getItemPriceInstance(Locale locale)
    {
        return new ItemPrice();
    }

    /**
     * Returns instance of ItemTaxIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemTaxIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemTaxInstance(java.util.Locale)
     */
    public ItemTaxIfc getItemTaxInstance(Locale locale)
    {
        return new ItemTax();
    }

    /**
     * Returns instance of OrderItemStatusIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return OrderItemStatusIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOrderItemStatusInstance(java.util.Locale)
     */
    public OrderItemStatusIfc getOrderItemStatusInstance(Locale locale)
    {
        return new OrderItemStatus();
    }

    /**
     * Returns instance of OrderLineItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return OrderLineItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOrderLineItemInstance(java.util.Locale)
     */
    public OrderLineItemIfc getOrderLineItemInstance(Locale locale)
    {
        return new OrderLineItem();
    }

    /**
     * Returns instance of ReturnItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ReturnItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReturnItemInstance(java.util.Locale)
     */
    public ReturnItemIfc getReturnItemInstance(Locale locale)
    {
        return new ReturnItem();
    }

    /**
     * Returns instance of SaleReturnLineItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return instance of SaleREturnLineItemIfc
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSaleReturnLineItemInstance(java.util.Locale)
     */
    public SaleReturnLineItemIfc getSaleReturnLineItemInstance(Locale locale)
    {
        return new SaleReturnLineItem();
    }

    /**
     * Returns instance of PriceAdjustmentLineItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return Instance of PriceAdjustmentLineItemIfc class.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPriceAdjustmentLineItemInstance(java.util.Locale)
     */
    public PriceAdjustmentLineItemIfc getPriceAdjustmentLineItemInstance(Locale locale)
    {
        return new PriceAdjustmentLineItem();
    }

    /**
     * Returns instance of OrderIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return OrderIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOrderInstance(java.util.Locale)
     */
    public OrderIfc getOrderInstance(Locale locale)
    {
        return new Order();
    }

    /**
     * Returns instance of OrderStatusIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return OrderStatusIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOrderStatusInstance(java.util.Locale)
     */
    public OrderStatusIfc getOrderStatusInstance(Locale locale)
    {
        return new OrderStatus();
    }

    /**
     * Returns instance of OrderSummaryEntryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return OrderSummaryEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOrderSummaryEntryInstance(java.util.Locale)
     */
    public OrderSummaryEntryIfc getOrderSummaryEntryInstance(Locale locale)
    {
        return new OrderSummaryEntry();
    }

    /**
     * Returns instance of GiftCardPLUItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return GiftCardPLUItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getGiftCardPLUItemInstance(java.util.Locale)
     */
    public GiftCardPLUItemIfc getGiftCardPLUItemInstance(Locale locale)
    {
        return new GiftCardPLUItem();
    }

    /**
     * Return instance of ItemImageIfc.
     *
     * @param locale
     * @return
     */
    public ItemImageIfc getItemImageInstance(Locale locale)
    {
        return new ItemImage();
    }

    /**
     * Returns instance of ItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemInstance(java.util.Locale)
     */
    public ItemIfc getItemInstance(Locale locale)
    {
        return new Item();
    }

    /**
     * Returns instance of ItemClassificationIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemClassificationIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemClassificationInstance(java.util.Locale)
     */
    public ItemClassificationIfc getItemClassificationInstance(Locale locale)
    {
        return new ItemClassification();
    }

    /**
     * Returns instance of ItemColorIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemColorIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemColorInstance(java.util.Locale)
     */
    public ItemColorIfc getItemColorInstance(Locale locale)
    {
        return new ItemColor();
    }

    /**
     * Returns instance of ItemTypeIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemTypeIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemTypeInstance(java.util.Locale)
     */
    public ItemTypeIfc getItemTypeInstance(Locale locale)
    {
        return new ItemType();
    }

    /**
     * Returns instance of ItemKitIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemKitIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemKitInstance(java.util.Locale)
     */
    public ItemKitIfc getItemKitInstance(Locale locale)
    {
        return new ItemKit();
    }

    /**
     * Returns instance of ItemSizeIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemSizeIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemSizeInstance(java.util.Locale)
     */
    public ItemSizeIfc getItemSizeInstance(Locale locale)
    {
        return new ItemSize();
    }

    /**
     * Returns instance of ItemStyleIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemStyleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemStyleInstance(java.util.Locale)
     */
    public ItemStyleIfc getItemStyleInstance(Locale locale)
    {
        return new ItemStyle();
    }

    /**
     * Returns instance of KitComponentIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return KitComponentIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getKitComponentInstance(java.util.Locale)
     */
    public KitComponentIfc getKitComponentInstance(Locale locale)
    {
        return new KitComponent();
    }

    /**
     * Returns instance of KitHeaderLineItemIfc. <P>
     *
     * @param locale Locale to get an object for.
     * @return KitHeaderLineItemIfc
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getKitHeaderLineItemInstance(java.util.Locale)
     */
    public KitHeaderLineItemIfc getKitHeaderLineItemInstance(Locale locale)
    {
        return new KitHeaderLineItem();
    }

    /**
     * Returns instance of KitComponentLineItemIfc. <P>
     *
     * @param locale Locale to get an object for.
     * @return KitComponentLineItemIfc
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getKitComponentLineItemInstance(java.util.Locale)
     */
    public KitComponentLineItemIfc getKitComponentLineItemInstance(Locale locale)
    {
        return new KitComponentLineItem();
    }

    /**
     * Returns instance of ManufacturerIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ManufacturerIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getManufacturerInstance(java.util.Locale)
     */
    public ManufacturerIfc getManufacturerInstance(Locale locale)
    {
        return new Manufacturer();
    }

    /**
     * Returns instance of MerchandiseClassificationIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return MerchandiseClassificationIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getMerchandiseClassificationInstance(java.util.Locale)
     */
    public MerchandiseClassificationIfc getMerchandiseClassificationInstance(Locale locale)
    {
        return new MerchandiseClassification();
    }

    /**
     * Returns instance of PLUItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPLUItemInstance(java.util.Locale)
     */
    public PLUItemIfc getPLUItemInstance(Locale locale)
    {
        return new PLUItem();
    }

    /**
     * Returns instance of ProductGroupIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ProductGroupIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getProductGroupInstance(java.util.Locale)
     */
    public ProductGroupIfc getProductGroupInstance(Locale locale)
    {
        return new ProductGroup();
    }

    /**
     * Returns instance of ProductIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ProductIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getProductInstance(java.util.Locale)
     * @deprecated 02JUL2007 Concept of Product has been replaced by Merchandise Hierarchy.
     */
    public ProductIfc getProductInstance(Locale locale)
    {
        return new Product();
    }

    /**
     * Returns instance of StockItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return StockItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getStockItemInstance(java.util.Locale)
     */
    public StockItemIfc getStockItemInstance(Locale locale)
    {
        return new StockItem();
    }

    /**
     * Returns instance of UnitOfMeasureIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return UnitOfMeasureIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getUnitOfMeasureInstance(java.util.Locale)
     */
    public UnitOfMeasureIfc getUnitOfMeasureInstance(Locale locale)
    {
        return new UnitOfMeasure();
    }

    /**
     * Returns instance of UnknownItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return UnknownItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getUnknownItemInstance(java.util.Locale)
     */
    public UnknownItemIfc getUnknownItemInstance(Locale locale)
    {
        return new UnknownItem();
    }

    /**
     * Returns instance of DepartmentIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DepartmentIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDepartmentInstance(java.util.Locale)
     */
    public DepartmentIfc getDepartmentInstance(Locale locale)
    {
        return new Department();
    }

    /**
     * Returns instance of DistrictIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DistrictIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDistrictInstance(java.util.Locale)
     */
    public DistrictIfc getDistrictInstance(Locale locale)
    {
        return new District();
    }

    /**
     * Returns instance of RegionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return RegionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRegionInstance(java.util.Locale)
     */
    public RegionIfc getRegionInstance(Locale locale)
    {
        return new Region();
    }

    /**
     * Returns instance of StoreIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return StoreIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getStoreInstance(java.util.Locale)
     */
    public StoreIfc getStoreInstance(Locale locale)
    {
        return new Store();
    }

    /**
     * Returns instance of WorkstationIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return WorkstationIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getWorkstationInstance(java.util.Locale)
     */
    public WorkstationIfc getWorkstationInstance(Locale locale)
    {
        return new Workstation();
    }

    /**
     * Returns instance of TenderCashIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderCashIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderCashInstance(java.util.Locale)
     */
    public TenderCashIfc getTenderCashInstance(Locale locale)
    {
        return new TenderCash();
    }

    /**
     * Returns instance of TenderMoneyOrderIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderMoneyOrderIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderMoneyOrderInstance(java.util.Locale)
     */
    public TenderMoneyOrderIfc getTenderMoneyOrderInstance(Locale locale)
    {
        return new TenderMoneyOrder();
    }

    /**
     * Returns instance of TenderChargeIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderChargeIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderChargeInstance(java.util.Locale)
     */
    public TenderChargeIfc getTenderChargeInstance(Locale locale)
    {
        return new TenderCharge();
    }

    /**
     * Returns instance of TenderCheckIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderCheckIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderCheckInstance(java.util.Locale)
     */
    public TenderCheckIfc getTenderCheckInstance(Locale locale)
    {
        return new TenderCheck();
    }

    /**
     * Returns instance of TenderCouponIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderCouponIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderCouponInstance(java.util.Locale)
     */
    public TenderCouponIfc getTenderCouponInstance(Locale locale)
    {
        return new TenderCoupon();
    }

    /**
     * Returns instance of TenderDebitIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderDebitIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderDebitInstance(java.util.Locale)
     */
    public TenderDebitIfc getTenderDebitInstance(Locale locale)
    {
        return new TenderDebit();
    }

    /**
     * Returns instance of TenderGiftCardIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderGiftCardIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderGiftCardInstance(java.util.Locale)
     */
    public TenderGiftCardIfc getTenderGiftCardInstance(Locale locale)
    {
        return new TenderGiftCard();
    }

    /**
     * Returns instance of TenderGiftCertificateIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderGiftCertificateIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderGiftCertificateInstance(java.util.Locale)
     */
    public TenderGiftCertificateIfc getTenderGiftCertificateInstance(Locale locale)
    {
        return new TenderGiftCertificate();
    }

    /**
     * Returns instance of TenderLimitsIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderLimitsIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderLimitsInstance(java.util.Locale)
     */
    public TenderLimitsIfc getTenderLimitsInstance(Locale locale)
    {
        return new TenderLimits();
    }

    /**
     * Returns instance of TenderMailBankCheckIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderMailBankCheckIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderMailBankCheckInstance(java.util.Locale)
     */
    public TenderMailBankCheckIfc getTenderMailBankCheckInstance(Locale locale)
    {
        return new TenderMailBankCheck();
    }

    /**
     * Returns instance of TenderTravelersCheckIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderTravelersCheckIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderTravelersCheckInstance(java.util.Locale)
     */
    public TenderTravelersCheckIfc getTenderTravelersCheckInstance(Locale locale)
    {
        return new TenderTravelersCheck();
    }

    /**
     * Returns instance of TenderPurchaseOrderIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderPurchaseOrderIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderPurchaseOrderInstance(java.util.Locale)
     */
    public TenderPurchaseOrderIfc getTenderPurchaseOrderInstance(Locale locale)
    {
        return new TenderPurchaseOrder();
    }

    /**
     * Returns instance of TenderStoreCreditIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderStoreCreditIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderStoreCreditInstance(java.util.Locale)
     */
    public TenderStoreCreditIfc getTenderStoreCreditInstance(Locale locale)
    {
        return new TenderStoreCredit();
    }

    /**
     * Returns instance of BankDepositTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return BankDepositTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getBankDepositTransactionInstance(java.util.Locale)
     */
    public BankDepositTransactionIfc getBankDepositTransactionInstance(Locale locale)
    {
        return new BankDepositTransaction();
    }

    /**
     * Returns instance of LayawayPaymentTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return LayawayPaymentTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getLayawayPaymentTransactionInstance(java.util.Locale)
     */
    public LayawayPaymentTransactionIfc getLayawayPaymentTransactionInstance(Locale locale)
    {
        return new LayawayPaymentTransaction();
    }

    /**
     * Returns instance of LayawayTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return LayawayTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getLayawayTransactionInstance(java.util.Locale)
     */
    public LayawayTransactionIfc getLayawayTransactionInstance(Locale locale)
    {
        return new LayawayTransaction();
    }

    /**
     * Returns instance of NoSaleTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return NoSaleTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getNoSaleTransactionInstance(java.util.Locale)
     */
    public NoSaleTransactionIfc getNoSaleTransactionInstance(Locale locale)
    {
        return new NoSaleTransaction();
    }

    /**
     * Returns instance of TillAdjustmentTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TillAdjustmentTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTillAdjustmentTransactionInstance(java.util.Locale)
     */
    public TillAdjustmentTransactionIfc getTillAdjustmentTransactionInstance(Locale locale)
    {
        return new TillAdjustmentTransaction();
    }

    /**
     * Returns instance of OrderTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return OrderTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOrderTransactionInstance(java.util.Locale)
     */
    public OrderTransactionIfc getOrderTransactionInstance(Locale locale)
    {
        return new OrderTransaction();
    }

    /**
     * Returns instance of OrderDeliveryRecordIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return OrderDeliveryDetailIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOrderDeliveryRecordInstance(java.util.Locale)
     */
    public OrderDeliveryDetailIfc getOrderDeliveryDetailInstance(Locale locale)
    {
        return new OrderDeliveryDetail();
    }

    /**
     * Returns instance of OrderDeliveryRecordIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return OrderDeliveryDetailIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOrderDeliveryRecordInstance(java.util.Locale)
     */
    public OrderRecipientIfc getOrderRecipientInstance(Locale locale)
    {
        return new OrderRecipient();
    }

    /**
     * Returns instance of PurchaseOrderIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PurchaseOrderIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPurchaseOrderInstance(java.util.Locale)
     */
    public PurchaseOrderIfc getPurchaseOrderInstance(Locale locale)
    {
        return new PurchaseOrder();
    }

    /**
     * Returns instance of PurchaseOrderLineItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PurchaseOrderLineItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPurchaseOrderLineItemInstance(java.util.Locale)
     */
    public PurchaseOrderLineItemIfc getPurchaseOrderLineItemInstance(Locale locale)
    {
        return new PurchaseOrderLineItem();
    }

    /**
     * Returns instance of PaymentTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PaymentTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPaymentTransactionInstance(java.util.Locale)
     */
    public PaymentTransactionIfc getPaymentTransactionInstance(Locale locale)
    {
        return new PaymentTransaction();
    }

    /**
     * Returns instance of PurgeCriteriaIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PurgeCriteriaIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPurgeCriteriaInstance(java.util.Locale)
     */
    public PurgeCriteriaIfc getPurgeCriteriaInstance(Locale locale)
    {
        return new PurgeCriteria();
    }

    /**
     * Returns instance of PurgeResultIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PurgeResultIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPurgeResultInstance(java.util.Locale)
     */
    public PurgeResultIfc getPurgeResultInstance(Locale locale)
    {
        return new PurgeResult();
    }

    /**
     * Returns instance of PurgeTransactionEntryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PurgeTransactionEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPurgeTransactionEntryInstance(java.util.Locale)
     */
    public PurgeTransactionEntryIfc getPurgeTransactionEntryInstance(Locale locale)
    {
        return new PurgeTransactionEntry();
    }

    /**
     * Returns instance of RegisterOpenCloseTransactionIfc class. <P>
     *
     *
     * @param locale Locale to get an object for.
     * @return RegisterOpenCloseTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRegisterOpenCloseTransactionInstance(java.util.Locale)
     */
    public RegisterOpenCloseTransactionIfc getRegisterOpenCloseTransactionInstance(Locale locale)
    {
        return new RegisterOpenCloseTransaction();
    }

    /**
     * Returns instance of SaleReturnTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return SaleReturnTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSaleReturnTransactionInstance(java.util.Locale)
     */
    public SaleReturnTransactionIfc getSaleReturnTransactionInstance(Locale locale)
    {
        return new SaleReturnTransaction();
    }

    /**
     * Returns instance of SearchCriteriaIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return SearchCriteriaIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSearchCriteriaInstance(java.util.Locale)
     */
    public SearchCriteriaIfc getSearchCriteriaInstance(Locale locale)
    {
        return new SearchCriteria();
    }

    /**
     * Returns instance of StoreSearchCriteriaIfc class. <P>
     *
     * @return StoreSearchCriteriaIfc instance
     */
    public StoreSearchCriteriaIfc getStoreSearchCriteriaInstance()
    {
        return new StoreSearchCriteria();
    }

    /**
     * Returns instance of ItemInquirySearchCriteriaIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemInquirySearchCriteriaIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemInquirySearchCriteriaInstance(java.util.Locale)
     */
    public ItemInquirySearchCriteriaIfc getItemInquirySearchCriteriaInstance(Locale locale)
    {
        return new ItemInquirySearchCriteria();
    }

    /**
     * Returns instance of ItemInfoIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ItemInfoIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getItemInfoInstance(java.util.Locale)
     */
    public ItemInfoIfc getItemInfoInstance(Locale locale)
    {
        return new ItemInfo();
    }

    /**
     * Returns instance of StoreOpenCloseTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return StoreOpenCloseTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getStoreOpenCloseTransactionInstance(java.util.Locale)
     */
    public StoreOpenCloseTransactionIfc getStoreOpenCloseTransactionInstance(Locale locale)
    {
        return new StoreOpenCloseTransaction();
    }

    /**
     * Returns instance of TillOpenCloseTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TillOpenCloseTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTillOpenCloseTransactionInstance(java.util.Locale)
     */
    public TillOpenCloseTransactionIfc getTillOpenCloseTransactionInstance(Locale locale)
    {
        return new TillOpenCloseTransaction();
    }

    /**
     * Returns instance of TransactionIDIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TransactionIDIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTransactionIDInstance(java.util.Locale)
     */
    public TransactionIDIfc getTransactionIDInstance(Locale locale)
    {
        return new TransactionID();
    }

    /**
     * Returns instance of TransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTransactionInstance(java.util.Locale)
     */
    public TransactionIfc getTransactionInstance(Locale locale)
    {
        return new Transaction();
    }

    /**
     * Returns instance of TransactionKeyIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TransactionKeyIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTransactionKeyInstance(java.util.Locale)
     */
    public TransactionKeyIfc getTransactionKeyInstance(Locale locale)
    {
        return new TransactionKey();
    }


    /**
     * Returns instance of TransactionSummaryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TransactionSummaryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTransactionSummaryInstance(java.util.Locale)
     */
    public TransactionSummaryIfc getTransactionSummaryInstance(Locale locale)
    {
        return new TransactionSummary();
    }

    /**
     * Returns instance of TransactionTaxIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TransactionTaxIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTransactionTaxInstance(java.util.Locale)
     */
    public TransactionTaxIfc getTransactionTaxInstance(Locale locale)
    {
        return new TransactionTax();
    }

    /**
     * Returns instance of TransactionTotalsIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TransactionTotalsIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTransactionTotalsInstance(java.util.Locale)
     */
    public TransactionTotalsIfc getTransactionTotalsInstance(Locale locale)
    {
        return new TransactionTotals();
    }

    /**
     * Returns instance of VoidTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return VoidTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getVoidTransactionInstance(java.util.Locale)
     */
    public VoidTransactionIfc getVoidTransactionInstance(Locale locale)
    {
        return new VoidTransaction();
    }

    /**
     * Returns instance of VoidTransactionIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return VoidTransactionIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRedeemTransactionInstance(java.util.Locale)
     */
    public RedeemTransactionIfc getRedeemTransactionInstance(Locale locale)
    {
        return new RedeemTransaction();
    }

    /**
     * Returns instance of POSLogTransactionEntryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return POSLogTransactionEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPOSLogTransactionEntryInstance(java.util.Locale)
     */
    public POSLogTransactionEntryIfc getPOSLogTransactionEntryInstance(Locale locale)
    {
        return new POSLogTransactionEntry();
    }

    /**
     * Returns instance of DataReplicationCustomerEntryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DataReplicationCustomerEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDataReplicationCustomerEntryInstance(java.util.Locale)
     */
    public DataReplicationCustomerEntryIfc getDataReplicationCustomerEntryInstance(Locale locale)
    {
        return new DataReplicationCustomerEntry();
    }

   /**
     * Returns instance of AddressIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return AddressIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getAddressInstance(java.util.Locale)
     */
    public AddressIfc getAddressInstance(Locale locale)
    {
        return new Address();
    }

    /**
     * Returns instance of EmailAddressIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return EmailAddressIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getEmailAddressInstance(java.util.Locale)
     */
    public EmailAddressIfc getEmailAddressInstance(Locale locale)
    {
        return new EmailAddress();
    }

    /**
     * Returns an instance of Card class <P>
     *
     * @param locale Locale to get an object for.
     * @return CardIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCardInstance(java.util.Locale)
     */
    public CardIfc getCardInstance(Locale locale)
    {
        return new Card();
    }

    /**
     * Returns instance of CardTypeIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CardTypeIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCardTypeInstance(java.util.Locale)
     */
    public CardTypeIfc getCardTypeInstance(Locale locale)
    {
        return new CardType();
    }

    /**
     * Returns instance of CardTypeIfc class. <P>
     *
     * @param setDefaults set to true if default card types are desired,
     * false if a more custom approach is needed
     * @param locale Locale to get an object for.
     * @return CardTypeIfc instance
     */
    public CardTypeIfc getCardTypeInstance(boolean setDefaults, Locale locale)
    {
        return new CardType(setDefaults);
    }

    /**
     * Returns an instance of RuleBinRange class <P>
     *
     * @param locale Locale to get an object for.
     * @return RuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRuleBinRangeInstance(java.util.Locale)
     */
    public RuleIfc getRuleBinRangeInstance(Locale locale)
    {
        return new RuleBinRange();
    }

    /**
     * Returns an instance of RuleLength class <P>
     *
     * @param locale Locale to get an object for.
     * @return RuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRuleLengthInstance(java.util.Locale)
     */
    public RuleIfc getRuleLengthInstance(Locale locale)
    {
        return new RuleLength();
    }

    /**
     * Returns an instance of RuleMask class <P>
     *
     * @param locale Locale to get an object for.
     * @return RuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRuleMaskInstance(java.util.Locale)
     */
    public RuleIfc getRuleMaskInstance(Locale locale)
    {
        return new RuleMask();
    }

    /**
     * Returns instance of CodeEntryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CodeEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCodeEntryInstance(java.util.Locale)
     */
    public CodeEntryIfc getCodeEntryInstance(Locale locale)
    {
        return new CodeEntry();
    }

    /**
     * Returns instance of CodeListIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CodeListIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCodeListInstance(java.util.Locale)
     */
    public CodeListIfc getCodeListInstance(Locale locale)
    {
        return new CodeList();
    }

    /**
     * Returns instance of CodeListMapIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CodeListMapIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCodeListMapInstance(java.util.Locale)
     */
    public CodeListMapIfc getCodeListMapInstance(Locale locale)
    {
        return new CodeListMap();
    }

    /**
     * Returns instance of EYSDate class. <P>
     *
     * @param locale Locale to get an object for.
     * @return EYSDate instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getEYSDateInstance(java.util.Locale)
     */
    public EYSDate getEYSDateInstance(Locale locale)
    {
        return new EYSDate();
    }

    /**
     * Returns instance of EYSStatus class. <P>
     *
     * @param locale Locale to get an object for.
     * @return EYSStatus instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getEYSStatusInstance(java.util.Locale)
     */
    public EYSStatusIfc getEYSStatusInstance(Locale locale)
    {
        return new EYSStatus();
    }

    /**
     * Returns instance of EYSTime class. <P>
     *
     * @param locale Locale to get an object for.
     * @return EYSTime instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getEYSTimeInstance(java.util.Locale)
     */
    public EYSTime getEYSTimeInstance(Locale locale)
    {
        return new EYSTime();
    }

    /**
     * Returns instance of GiftCardIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return GiftCardIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getGiftCardInstance(java.util.Locale)
     */
    public GiftCardIfc getGiftCardInstance(Locale locale)
    {
        return new GiftCard();
    }

    /**
     * Returns instance of GiftRegistryIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return GiftRegistryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getGiftRegistryInstance(java.util.Locale)
     */
    public GiftRegistryIfc getGiftRegistryInstance(Locale locale)
    {
        return new GiftRegistry();
    }

    /**
     * Returns instance of PersonIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PersonIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPersonInstance(java.util.Locale)
     */
    public PersonIfc getPersonInstance(Locale locale)
    {
        return new Person();
    }

    /**
     * Returns instance of PersonNameIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PersonNameIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPersonNameInstance(java.util.Locale)
     */
    public PersonNameIfc getPersonNameInstance(Locale locale)
    {
        return new PersonName();
    }

    /**
     * Returns instance of PhoneIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return PhoneIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPhoneInstance(java.util.Locale)
     */
    public PhoneIfc getPhoneInstance(Locale locale)
    {
        return new Phone();
    }

    /**
     * Returns instance of HouseCardIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return HouseCardIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getHouseCardInstance(java.util.Locale)
     */
    public HouseCardIfc getHouseCardInstance(Locale locale)
    {
        return new HouseCard();
    }

    /**
     * Returns instance of RegistryIDIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return RegistryIDIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRegistryIDInstance(java.util.Locale)
     */
    public RegistryIDIfc getRegistryIDInstance(Locale locale)
    {
        return new GiftRegistry();
    }

    /**
     * Returns instance of CustomerInfoIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CustomerInfoIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCustomerInfoInstance(java.util.Locale)
     */
    public CustomerInfoIfc getCustomerInfoInstance(Locale locale)
    {
        return new CustomerInfo();
    }

    /**
     * Returns instance of AdvancedPricingRuleSearchCriteriaIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return AdvancedPricingRuleSearchCriteriaIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getAdvancedPricingRuleSearchCriteriaInstance(java.util.Locale)
     */
    public AdvancedPricingRuleSearchCriteriaIfc getAdvancedPricingRuleSearchCriteriaInstance(Locale locale)
    {
        return new AdvancedPricingRuleSearchCriteria();
    }

    /**
     * Returns instance of ActiveJobIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ActiveJobIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getActiveJobInstance(java.util.Locale)
     */
    public ActiveJobIfc getActiveJobInstance(Locale locale)
    {
        return new ActiveJob();
    }

    /**
     * Returns instance of ScheduledJobIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ScheduledJobIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getScheduledJobInstance(java.util.Locale)
     */
    public ScheduledJobIfc getScheduledJobInstance(Locale locale)
    {
        return new ScheduledJob();
    }

    /**
     * Returns instance of NotificationRecipientsIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return NotificationRecipientsIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getNotificationRecipientsInstance(java.util.Locale)
     */
    public NotificationRecipientsIfc getNotificationRecipientsInstance(Locale locale)
    {
        return new NotificationRecipients();
    }

    /**
     * Returns instance of TaskInfoIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TaskInfoIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaskInfoInstance(java.util.Locale)
     */
    public TaskInfoIfc getTaskInfoInstance(Locale locale)
    {
        return new TaskInfo();
    }

    /**
     * Returns instance of JobControlEventMessageIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return JobControlEventMessageIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getJobControlEventMessageInstance(java.util.Locale)
     */
    public JobControlEventMessageIfc getJobControlEventMessageInstance(Locale locale)
    {
        return new JobControlEventMessage();
    }

    /**
     * Returns instance of DailyScheduleDocumentIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return DailyScheduleDocumentIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDailyScheduleDocumentInstance(java.util.Locale)
     */
    public DailyScheduleDocumentIfc getDailyScheduleDocumentInstance(Locale locale)
    {
        return new DailyScheduleDocument();
    }

    /**
     * Returns instance of WeeklyScheduleDocumentIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return WeeklyScheduleDocumentIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getWeeklyScheduleDocumentInstance(java.util.Locale)
     */
    public WeeklyScheduleDocumentIfc getWeeklyScheduleDocumentInstance(Locale locale)
    {
        return new WeeklyScheduleDocument();
    }

    /**
     * Returns instance of MonthlyByDayScheduleDocumentIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return MonthlyByDayScheduleDocumentIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getMonthlyByDayScheduleDocumentInstance(java.util.Locale)
     */
    public MonthlyByDayScheduleDocumentIfc getMonthlyByDayScheduleDocumentInstance(Locale locale)
    {
        return new MonthlyByDayScheduleDocument();
    }

    /**
     * Returns instance of CustomScheduleDocumentIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return CustomScheduleDocumentIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCustomScheduleDocumentInstance(java.util.Locale)
     */
    public CustomScheduleDocumentIfc getCustomScheduleDocumentInstance(Locale locale)
    {
        return new CustomScheduleDocument();
    }

    /**
     * Returns instance of StoreSafeIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return StoreSafeIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getStoreSafeInstance(java.util.Locale)
     */
    public StoreSafeIfc getStoreSafeInstance(Locale locale)
    {
        return new StoreSafe();
    }

    /**
     * Returns instance of ShippingMethodIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ShippingMethodIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getShippingMethodInstance(java.util.Locale)
     */
    public ShippingMethodIfc getShippingMethodInstance(Locale locale)
    {
        return new ShippingMethod();
    }

    /**
     * Returns instance of TenderDescriptorIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return TenderDescriptorIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTenderDescriptorInstance(java.util.Locale)
     */
    public TenderDescriptorIfc getTenderDescriptorInstance(Locale locale)
    {
        return new TenderDescriptor();
    }

    /**
     * Returns instance of StoreCreditIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return StoreCreditIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getStoreCreditInstance(java.util.Locale)
     */
    public StoreCreditIfc getStoreCreditInstance(Locale locale)
    {
        return new StoreCredit();
    }

    /**
     * Returns instance of AssociateProductivity class. <P>
     *
     * @param locale Locale to get an object for.
     * @return AssociateProductivityIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getAssociateProductivityInstance(java.util.Locale)
     */
    public AssociateProductivityIfc getAssociateProductivityInstance(Locale locale)
    {
        return new AssociateProductivity();
    }

    /**
     * Returns instance of AlterationIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return AlertEntryIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getAlterationInstance(java.util.Locale)
     */
    public AlterationIfc getAlterationInstance(Locale locale)
    {
        return new Alteration();
    }

    /**
     * Returns instance of AlterationPLUItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return AlterationPLUItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getAlterationPLUItemInstance(java.util.Locale)
     */
    public AlterationPLUItemIfc getAlterationPLUItemInstance(Locale locale)
    {
        return new AlterationPLUItem();
    }

    /**
     * Returns instance of Supplier class. <P>
     *
     * @param locale Locale to get an object for.
     * @return SupplierIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSupplierInstance(java.util.Locale)
     */
    public SupplierIfc getSupplierInstance(Locale locale)
    {
        return new Supplier();
    }

    /**
     * Returns instance of BusinessCalendarIfc.
     *
     * @param locale Locale to get an object for.
     * @return BusinessCalendarIfc instance.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getBusinessCalendarInstance(java.util.Locale)
     */
    public BusinessCalendarIfc getBusinessCalendarInstance(Locale locale)
    {
        return new BusinessCalendar();
    }

    /**
     * Returns instance of CalendarLevelKeyIfc.
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelKeyIfc instance.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCalendarLevelKeyInstance(java.util.Locale)
     */
    public CalendarLevelKeyIfc getCalendarLevelKeyInstance(Locale locale)
    {
        return new CalendarLevelKey();
    }

    /**
     * Returns instance of CalendarLevelFactoryIfc.
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelFactoryIfc instance.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCalendarLevelFactoryInstance(java.util.Locale)
     */
    public CalendarLevelFactoryIfc getCalendarLevelFactoryInstance(Locale locale)
    {
        return new CalendarLevelFactory();
    }

    /**
     * Returns requested instance of CalendarLevelIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelInstance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getRootCalendarLevelInstance(java.util.Locale)
     */
    public CalendarLevelIfc getRootCalendarLevelInstance(Locale locale)
    {
        return new RootCalendarLevel();
    }

    /**
     * Returns requested instance of CalendarLevelIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelInstance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getAggregateCalendarLevelInstance(java.util.Locale)
     */
    public CalendarLevelIfc getAggregateCalendarLevelInstance(Locale locale)
    {
        return new AggregateCalendarLevel();
    }

    /**
     * Returns requested instance of CalendarLevelIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelInstance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getMinuteCalendarLevelInstance(java.util.Locale)
     */
    public CalendarLevelIfc getMinuteCalendarLevelInstance(Locale locale)
    {
        return new MinuteCalendarLevel();
    }

    /**
     * Returns requested instance of CalendarLevelIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelInstance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getHourCalendarLevelInstance(java.util.Locale)
     */
    public CalendarLevelIfc getHourCalendarLevelInstance(Locale locale)
    {
        return new HourCalendarLevel();
    }

    /**
     * Returns requested instance of CalendarLevelIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelInstance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getDayCalendarLevelInstance(java.util.Locale)
     */
    public CalendarLevelIfc getDayCalendarLevelInstance(Locale locale)
    {
        return new DayCalendarLevel();
    }

    /**
     * Returns requested instance of CalendarLevelIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelInstance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getWeekDayCalendarLevelInstance(java.util.Locale)
     */
    public CalendarLevelIfc getWeekDayCalendarLevelInstance(Locale locale)
    {
        return new WeekDayCalendarLevel();
    }

    /**
     * Returns requested instance of CalendarLevelIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelInstance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getNthWeekDayCalendarLevelInstance(java.util.Locale)
     */
    public CalendarLevelIfc getNthWeekDayCalendarLevelInstance(Locale locale)
    {
        return new NthWeekDayCalendarLevel();
    }

    /**
     * Returns requested instance of CalendarLevelIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelInstance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getMonthDayCalendarLevelInstance(java.util.Locale)
     */
    public CalendarLevelIfc getMonthDayCalendarLevelInstance(Locale locale)
    {
        return new MonthDayCalendarLevel();
    }

    /**
     * Returns requested instance of CalendarLevelIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarLevelInstance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getYearDayCalendarLevelInstance(java.util.Locale)
     */
    public CalendarLevelIfc getYearDayCalendarLevelInstance(Locale locale)
    {
        return new YearDayCalendarLevel();
    }

    /**
     * Returns instance of CalendarPeriodIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarPeriodIfc instance.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCalendarPeriodInstance(java.util.Locale)
     */
    public CalendarPeriodIfc getCalendarPeriodInstance(Locale locale)
    {
        return new CalendarPeriod();
    }

    /**
     * Returns instance of CalendarPeriodKeyIfc
     *
     * @param locale Locale to get an object for.
     * @return CalendarPeriodKeyIfc instance.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getCalendarPeriodKeyInstance(java.util.Locale)
     */
    public CalendarPeriodKeyIfc getCalendarPeriodKeyInstance(Locale locale)
    {
        return new CalendarPeriodKey();
    }

    /**
     * Returns instance of MerchandiseHierarchyTreeIfc.
     *
     * @param locale Locale to get an object for.
     * @return MerchandiseHierarchyTreeIfc instance.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getMerchandiseHierarchyTreeInstance(java.util.Locale)
     */
    public MerchandiseHierarchyTreeIfc getMerchandiseHierarchyTreeInstance(Locale locale)
    {
        return new MerchandiseHierarchyTree();
    }

    /**
     * Returns instance of MerchandiseHierarchyLevelIfc.
     *
     * @param locale Locale to get an object for.
     * @return MerchandiseHierarchyLevelIfc instance.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getMerchandiseHierarchyLevelInstance(java.util.Locale)
     */
    public MerchandiseHierarchyLevelIfc getMerchandiseHierarchyLevelInstance(Locale locale)
    {
        return new MerchandiseHierarchyLevel();
    }

    /**
     * Returns instance of MerchandiseHierarchyLevelKeyIfc.
     *
     * @param locale Locale to get an object for.
     * @return MerchandiseHierarchyLevelKeyIfc instance.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getMerchandiseHierarchyLevelKeyInstance(java.util.Locale)
     */
    public MerchandiseHierarchyLevelKeyIfc getMerchandiseHierarchyLevelKeyInstance(Locale locale)
    {
        return new MerchandiseHierarchyLevelKey();
    }

    /**
     * Returns instance of MerchandiseHierarchyGroupIfc
     *
     * @param locale Locale to get an object for.
     * @return MerchandiseHierarchyGroupIfc instance.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getMerchandiseHierarchyGroupInstance(java.util.Locale)
     */
    public MerchandiseHierarchyGroupIfc getMerchandiseHierarchyGroupInstance(Locale locale)
    {
        return new MerchandiseHierarchyGroup();
    }

    /**
     * Returns instance of ReportBean (implementing ReportBeanIfc)
     *
     * @param locale Locale to get an object for.
     * @return ReportBean instance.
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReportBeanInstance(java.util.Locale)
     */
    public ReportBeanIfc getReportBeanInstance(Locale locale)
    {
        return new ReportBean();
    }

    /**
     * Returns instance of POSLogBatchGeneratorIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return POSLogBatchGeneratorIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getPOSLogBatchGeneratorInstance(java.util.Locale)
     */
    public POSLogBatchGeneratorIfc getPOSLogBatchGeneratorInstance(Locale locale)
    {
        return new POSLogBatchGenerator();
    }

    /**
     * Returns instance of DataReplicationBatchGeneratorIfc class. <P>
     *
     * @return DataReplicationBatchGeneratorIfc instance
     */
    public DataReplicationBatchGeneratorIfc getDataReplicationBatchGeneratorInstance(Locale locale)
    {
        return new DataReplicationBatchGenerator();
    }

    /**
     * Returns instance of SecurityOverrideIfc class.
     *
     * @param locale Locale to get an object for.
     * @return instance of SecurityOVerrideIfc
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getSecurityOverrideInstance(java.util.Locale)
     */
    public SecurityOverrideIfc getSecurityOverrideInstance(Locale locale)
    {
        return new SecurityOverride();
    }

    /**
     * Returns instance of InstantCreditIfc class.
     *
     * @param locale Locale to get an object for.
     * @return instance of InstantCreditIfc
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getInstantCreditInstance(java.util.Locale)
     */
    public InstantCreditIfc getInstantCreditInstance(Locale locale)
    {
        return new InstantCredit();
    }

    /**
     * Returns instance of InstantCreditTransactionIfc class.
     *
     * @param locale Locale to get an object for.
     * @return instance of InstantCreditTransactionIfc
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getInstantCreditTransactionInstance(java.util.Locale)
     */
    public InstantCreditTransactionIfc getInstantCreditTransactionInstance(Locale locale)
    {
        return new InstantCreditTransaction();
    }

    /**
     * Returns instance of GiftCertificateItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return GiftCertificateItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getGiftCertificateItemInstance(java.util.Locale)
     */
    public GiftCertificateItemIfc getGiftCertificateItemInstance(Locale locale)
    {
        return new GiftCertificateItem();
    }

    /**
     * Returns instance of ReturnTenderDataContainer class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ReturnTenderDataContainer instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReturnTenderDataContainerInstance(java.util.Locale)
     */
    public ReturnTenderDataContainer getReturnTenderDataContainerInstance(Locale locale)
    {
        return new ReturnTenderDataContainer();
    }

    /**
     * Returns instance of ReturnTenderDataElement class. <P>
     *
     * @param locale Locale to get an object for.
     * @return ReturnTenderDataElement instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReturnTenderDataElementInstance(java.util.Locale)
     */
    public ReturnTenderDataElement getReturnTenderDataElementInstance(Locale locale)
    {
        return new ReturnTenderDataElement();
    }

    /**
     * Returns an instance of TaxInformation
     *
     * @param locale Locale to get an object for.
     * @return TaxInformation instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxInformationInstance(java.util.Locale)
     */
    public TaxInformationIfc getTaxInformationInstance(Locale locale)
    {
        return new TaxInformation();
    }

    /**
     * Returns and instance of TaxInformationContainerIfc. <P>
     *
     * @param locale Locale to get an object for.
     * @return TaxInformationContainerIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxInformationContainerInstance(java.util.Locale)
     */
    public TaxInformationContainerIfc getTaxInformationContainerInstance(Locale locale)
    {
        return new TaxInformationContainer();
    }

    /**
     * Returns an instance of TaxRateCalculatorIfc. <P>
     *
     * @param locale Locale to get an object for.
     * @return TaxRateCalculatorInstance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxRateCalculatorInstance(java.util.Locale)
     */
    public TaxRateCalculatorIfc getTaxRateCalculatorInstance(Locale locale)
    {
        return getTaxRateCalculatorInstance(locale, false);
    }

    /**
     * Returns an instance of TaxRateCalculatorIfc. <P>
     * @param locale Locale to get an object for
     * @param inclusiveTaxFlag boolean flag indicating if inclusive or
     * 		exclusive tax rate calculator must be returned.
     * @return TaxRateCalculatorIfc instance
     */
    public TaxRateCalculatorIfc getTaxRateCalculatorInstance(Locale locale, boolean inclusiveTaxFlag)
    {
    	TaxRateCalculatorIfc taxRateCalculator = null;
    	if (inclusiveTaxFlag)
    	{
    		taxRateCalculator = new InclusiveTaxRateCalculator();
    	}
    	else
    	{
    		taxRateCalculator = new TaxRateCalculator();
    	}

    	return taxRateCalculator;
    }


    /**
     * Returns an instance of FixedAmountTaxCalculatorIfc
     *
     * @param locale Locale to get an object for.
     * @return FixedAmountTaxCalculatorIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getFixedAmountTaxCalculatorInstance(java.util.Locale)
     */
    public FixedAmountTaxCalculatorIfc getFixedAmountTaxCalculatorInstance(Locale locale)
    {
        return new FixedAmountTaxCalculator();
    }

    /**
     * Returns an instance of OverrideTransactionTaxByAmountRuleIfc. <P>
     *
     * @param locale Locale to get an object for.
     * @return OverrirdeTransactionTaxByAmountRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOverrideTransactionTaxByAmountRuleInstance(java.util.Locale)
     */
    public OverrideTransactionTaxByAmountRuleIfc getOverrideTransactionTaxByAmountRuleInstance(Locale locale)
    {
        return new OverrideTransactionTaxByAmountRule();
    }

    /**
     * Returns an instance of OverrideTransactionTaxByRateRuleIfc <P>
     *
     * @param locale Locale to get an object for.
     * @return OverrideTransactionTaxByRateRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOverrideTransactionTaxByRateRuleInstance(java.util.Locale)
     */
    public OverrideTransactionTaxByRateRuleIfc getOverrideTransactionTaxByRateRuleInstance(Locale locale)
    {

        return new OverrideTransactionTaxByRateProrateRule();
    }

    /**
     * Returns an instance of TaxExemptRuleIfc
     *
     * @param locale Locale to get an object for.
     * @return TaxExemptRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxExemptTaxRuleInstance(java.util.Locale)
     */
    public TaxExemptTaxRuleIfc getTaxExemptTaxRuleInstance(Locale locale)
    {
        return new TaxExemptTaxRule();
    }


    /**
     * Returns an instance of TaxRuleItemContainerIfc <P>
     *
     * @param locale Locale to get an object for.
     * @return TaxRuleItemContainerIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxRuleItemContainerInstance(java.util.Locale)
     */
    public TaxRuleItemContainerIfc getTaxRuleItemContainerInstance(Locale locale)
    {
        return new TaxRuleItemContainer();
    }

    /**
     * Returns an instance of OverrideItemTaxByAmountRuleIfc. <P>
     *
     * @param locale Locale to get an object for.
     * @return OverrideItemTaxByAmountRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOverrideItemTaxByAmountRuleInstance(java.util.Locale)
     */
    public OverrideItemTaxByAmountRuleIfc getOverrideItemTaxByAmountRuleInstance(Locale locale)
    {
        return new OverrideItemTaxByAmountRule();
    }

    /**
     * Return an instance of OverrideItemTaxByRateRuleIfc
     *
     * @param locale Locale to get an object for.
     * @return OverrideItemTaxByRateRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOverrideItemTaxByRateRuleInstance(java.util.Locale)
     */
    public OverrideItemTaxByRateRuleIfc getOverrideItemTaxByRateRuleInstance(Locale locale)
    {
        return new OverrideItemTaxByRateRule();
    }

    /**
     * Return an instance of Toggle Item Tax Off rule
     *
     * @param locale Locale to get an object for.
     * @return OverrideItemTaxRuleIfc type that toggles tax Off
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getOverrideItemTaxToggleOffRuleInstance(java.util.Locale)
     */
    public OverrideItemTaxRuleIfc getOverrideItemTaxToggleOffRuleInstance(Locale locale)
    {
        return new OverrideItemTaxToggleOffRule();
    }

    /**
     * Return an instance of TaxEngineIfc
     *
     * @param locale Locale to get an object for.
     * @return TaxEngineIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxEngineInstance(java.util.Locale)
     */
    public TaxEngineIfc getTaxEngineInstance(Locale locale)
    {
        return new InternalTaxEngine();
    }

    /**
     * Return an instance of ExciseTaxRuleIfc
     *
     * @param locale Locale to get an object for.
     * @return ExciseTaxRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getExciseTaxRuleInstance(java.util.Locale)
     */
    public ExciseTaxRuleIfc getExciseTaxRuleInstance(Locale locale)
    {
        return new ExciseTaxRule();
    }

    /**
     * Return an instance of TableTaxRuleIfc
     *
     * @param locale Locale to get an object for
     * @return TableTaxRuleIfc instance
     */
    public TableTaxRuleIfc getTableTaxRuleInstance(Locale locale)
    {
        return new TableTaxRule();
    }

    /**
     * Return an instance of NewTaxRuleIfc which calculates
     * tax by the line item
     *
     * @param locale Locale to get an object for.
     * @return NewTaxRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxByLineRuleInstance(java.util.Locale)
     */
    public NewTaxRuleIfc getTaxByLineRuleInstance(Locale locale)
    {
        return new TaxByLineRule();
    }

    /**
     * Return an instance of NewTaxRuleIfc which calculates
     * tax by the transaction and then prorates it down to
     * the line items.
     *
     * @param locale Locale to get an object for.
     * @return NewTaxRuleIfc instance for a pro-rated tax rule
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxProrateRuleInstance(java.util.Locale)
     */
    public NewTaxRuleIfc getTaxProrateRuleInstance(Locale locale)
    {
        return new TaxProrateRule();
    }

    /**
     * Return an instance of ValueAddedTaxRuleIfc which calculates
     * the value added tax by line item
     *
     * @param locale Locale to get an object for.
     * @return ValueAddedTaxRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getValueAddedTaxByLineRuleInstance(java.util.Locale)
     */
    public ValueAddedTaxRuleIfc getValueAddedTaxByLineRuleInstance(Locale locale)
    {
        return new ValueAddedTaxByLineRule();
    }

    /**
     * Return an instance of ValueAddedTaxRuleIfc which calculates
     * the value added tax by line item
     *
     * @param locale Locale to get an object for.
     * @return ValueAddedTaxRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getValueAddedTaxProrateRuleInstance(java.util.Locale)
     */
    public ValueAddedTaxRuleIfc getValueAddedTaxProrateRuleInstance(Locale locale)
    {
        return new ValueAddedTaxProrateRule();
    }

    /**
     * Return an instance of TaxTableLineItemIfc
     *
     * @param locale Locale to get an object for.
     * @return TaxTableLineItemIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxTableLineItemInstance(java.util.Locale)
     */
    public TaxTableLineItemIfc getTaxTableLineItemInstance(Locale locale)
    {
        return new TaxTableLineItem();
    }

    /**
     * Return an object of type TaxTotals
     *
     * @param locale Locale to get an object for.
     * @return tax totals instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxTotalsInstance(java.util.Locale)
     */
    public TaxTotalsIfc getTaxTotalsInstance(Locale locale)
    {
        return new TaxTotals();
    }

    /**
     * Return an object of type taxTotalsContainer
     *
     * @param locale Locale to get an object for.
     * @return taxTotalsContainer
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getTaxTotalsContainerInstance(java.util.Locale)
     */
    public TaxTotalsContainerIfc getTaxTotalsContainerInstance(Locale locale)
    {
        return new TaxTotalsContainer();
    }

    /**
     * Return an instance of ReverseItemTaxRuleIfc
     *
     * @param locale Locale to get an object for.
     * @return ReverseItemTaxRuleIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReturnItemTaxRuleInstance(java.util.Locale)
     */
    public ReverseItemTaxRuleIfc getReturnItemTaxRuleInstance(Locale locale)
    {
        return new ReverseItemTaxRule();
    }

    /**
     * Return an instance of the calculator used in returns
     *
     * @param locale Locale to get an object for.
     * @return ReturnTaxCalculatorIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReturnTaxCalculatorInstance(Locale locale)
     */
    public ReturnTaxCalculatorIfc getReturnTaxCalculatorInstance(Locale locale)
    {
        return new ReturnTaxCalculator();
    }

    /**
     * Return an instance of the calculator used in reverse
     * transactions other than returns
     *
     * @param locale Locale to get an object for.
     * @return ReverseTaxCalculatorIfc instance
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReverseTaxCalculatorInstance(Locale locale)
     */
    public ReverseTaxCalculatorIfc getReverseTaxCalculatorInstance(Locale locale)
    {
        return new ReverseTaxCalculator();
    }

    /**
     * Returns instance of SendPackageLineItemIfc class. <P>
     *
     * @param locale Locale to get an object for
     * @return SendPackageLineItemIfc instance
     */
    public SendPackageLineItemIfc getSendPackageLineItemInstance(Locale locale)
    {
    	return new SendPackageLineItem();
    }

    /**
     * Returns instance of ReturnResponseLineItemIfc class. <P>
     *
     * @param locale Locale to get an object for.
     * @return instance of ReturnResponseLineItemIfc
     * @see oracle.retail.stores.domain.factory.DomainObjectFactoryIfc#getReturnResponseLineItemInstance(java.util.Locale)
     */
    public ReturnResponseLineItemIfc getReturnResponseLineItemInstance(Locale locale)
    {
        return new ReturnResponseLineItem();
    }

    /**
     * Returns instance of BillPayTransactionIfc class.
     * @param locale Locale to get an object for.
     */
    public BillPayTransactionIfc getBillPayTransactionInstance(Locale locale)
    {
        return new BillPayTransaction();
    }

    /**
     * Returns instance of BillPayIfc class.
     * @param locale Locale to get an object for.
     */
    public BillPayIfc getBillPayInstance(Locale locale)
    {
        return new BillPay();
    }

    /**
     * Creates clone of this object. <P>
     *
     * @return Object clone of this object
     * @see java.lang.Object#clone()
     */
    public Object clone()
    {
        DomainObjectFactory c = new DomainObjectFactory();

        setCloneAttributes(c);

        return c;
    }

    /**
     * Sets attributes in clone of this object. <P>
     *
     * @param newClass new instance of object
     */
    public void setCloneAttributes(DomainObjectFactory newClass)
    {
        Locale cloneLocale = new Locale(this.getLocale().getLanguage(),
                this.getLocale().getCountry(),
                this.getLocale().getVariant());
        newClass.setLocale(cloneLocale);
    }


    /**
     * Determine if two objects are identical. <P>
     *
     * @param obj object to compare with
     * @return true if the objects are identical, false otherwise
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        boolean isEqual = false;

        if (obj instanceof I18NDomainObjectFactory)
        {
            I18NDomainObjectFactory otherFactory = (I18NDomainObjectFactory) obj;
            isEqual = getLocale().equals(otherFactory.getLocale());
        }
        return isEqual;
    }

    /**
     * Returns default display string. <P>
     *
     * @return String representation of object
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return Util.classToStringHeader("I18NDomainObjectFactory", revisionNumber, hashCode()).toString();
    }

}
