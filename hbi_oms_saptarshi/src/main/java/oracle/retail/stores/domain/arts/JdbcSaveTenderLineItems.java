/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
/* ===========================================================================
* Copyright (c) 1998, 2013, Oracle and/or its affiliates. All rights reserved.
 * ===========================================================================
 * $Header: rgbustores/modules/domain/src/oracle/retail/stores/domain/arts/JdbcSaveTenderLineItems.java /rgbustores_13.4x_generic_branch/23 2013/08/13 09:53:16 vkaruppu Exp $
 * ===========================================================================
 * NOTES
 * <other useful comments, qualifications, etc.>
 *
 * MODIFIED    (MM/DD/YY)
 *    vkarup 08/13/13 - XbranchMerge spurkaya_16630559_13.4.6_5 from
 *                      rgbustores_13.4.6_generic_branch
 *    vkarup 08/12/13 - XbranchMerge spurkaya_16630559_13.4.6_2 from
 *                      rgbustores_13.4.6_generic_branch
 *    spurka 06/13/13 - Order and Layaway penny rounding changes
 *    spurka 05/29/13 - Modified for Currency Rounding
 *    asinto 02/06/12 - null check for getEntryMethod(TenderCheckIfc)
 *    jswan  09/12/11 - Modifications for reversals of Gift Cards when escaping
 *                      from the Tender Tour.
 *    cgreen 09/12/11 - revert aba number encryption, which is not sensitive
 *    ohorne 08/29/11 - masked MICR Number formatting
 *    tkshar 08/19/11 - Made column names consistent for Encryption CR
 *    mkutia 08/16/11 - Removed depricated hashAccount field EncipheredCardData
 *                      and related classes
 *    rsnaya 08/16/11 - Fix to update Change due
 *    blarse 08/02/11 - Renamed token to accountNumberToken to be consistent.
 *    asinto 07/28/11 - retrieve and store the journal key to support reversals
 *                      with ISD
 *    cgreen 07/18/11 - remove hashed number column from gift card tables
 *    blarse 07/15/11 - Fix misspelled word: retrival
 *    cgreen 07/15/11 - removed encrypted expiration date from datamodel
 *    blarse 07/14/11 - Persisting new authorization journal key
 *    asinto 07/12/11 - fixed some entry method coding
 *    cgreen 07/07/11 - convert entryMethod to an enum
 *    rrkohl 07/04/11 - adding ABA number and Account number
 *    rrkohl 06/30/11 - encryptrion CR
 *    asinto 06/29/11 - Refactored to use EntryMethod and AuthorizationMethod
 *                      enums.
 *    cgreen 06/29/11 - add token column and remove encrypted/hashed account
 *                      number column in credit-debit tender table.
 *    cgreen 06/28/11 - rename hashed credit card field to token
 *    blarse 06/16/11 - Saving payment service token.
 *    cgreen 06/09/11 - added dao to persist and retrieve ICC card details
 *    cgreen 01/10/11 - refactor blob helpers into one
 *    asinto 12/20/10 - XbranchMerge asinton_bug-10407292 from
 *                      rgbustores_13.3x_generic_branch
 *    asinto 12/17/10 - deprecated hashed account ID.
 *    asinto 09/22/10 - Adding Credit Card Accountability Responsibility and
 *                      Disclosure Act of 2009 changes.
 *    asinto 05/28/10 - KSN bytes need to be captured from CPOI device and
 *                      formatted in the ISD request message for debit
 *    cgreene   05/28/10 - convert to oracle packaging
 *    cgreene   05/27/10 - convert to oracle packaging
 *    cgreene   05/27/10 - convert to oracle packaging
 *    cgreene   05/26/10 - convert to oracle packaging
 *    asinton   05/06/10 - Added Prepaid Remaining Balance to receipt and
 *                         ejournal
 *    cgreene   04/26/10 - XbranchMerge cgreene_tech75 from
 *                         st_rgbustores_techissueseatel_generic_branch
 *    cgreene   03/30/10 - remove deprecated ARTSDatabaseIfcs and change
 *                         SQLException to DataException
 *    abondala  01/03/10 - update header date
 *    asinton   12/09/09 - Changes per code review.
 *    asinton   12/03/09 - Changes to support credit card authorizations on
 *                         returns and voids.
 *    jswan     06/09/09 - Fix I18N/area code issues with saving and testing
 *                         the check tender table.
 *    cgreene   03/19/09 - change access to database blob helper
 *    acadar    02/09/09 - use default locale for display of date and time
 *    jswan     02/03/09 - Modified to quote personal ID data.
 *    vikini    02/02/09 - Null Check for ID Expiration Date
 *    cgreene   11/11/08 - switch to mail check getPrimaryAddress and Phone
 *                         methods
 *    abondala  11/06/08 - updated files related to reason codes
 *    abondala  11/05/08 - updated files related to the reason codes
 *                         CheckIDTypes and MailBankCheckIDTypes
 *
 * ===========================================================================
 * $Log:
 *  27   360Commerce 1.26        5/27/2008 4:48:04 PM   Sameer Thajudin checks
 *       the balance variable for null before fetching it's value.
 *       The methods to refer are getGiftCardInitialBalance and
 *       getGiftCardRemainingBalance
 *  26   360Commerce 1.25        5/2/2008 4:54:42 PM    Christian Greene 31553
 *       Remove deletion of gift cert tender when txn is voided
 *  25   360Commerce 1.24        4/22/2008 1:41:26 AM   Manas Sahu      Gift
 *       Card authorization date time are needed to be proper values. The
 *       hardcoded values of 1980-01-01 was being used earlier. Code Reviewed
 *       by Naveen
 *  24   360Commerce 1.23        3/11/2008 5:31:51 PM   Michael P. Barnett In
 *       insertGiftCardTenderLineItem() and updateGiftCardTenderLineItem,
 *       persist the hashed and masked values of the gift card account number.
 *  23   360Commerce 1.22        2/1/2008 5:55:16 PM    Alan N. Sinton  CR
 *       30110: Made JdbcSaveTenderLineItems use DatabaseBlobHelper class to
 *       save the customer signuture data.  These code changes were reviewed
 *       by Anil Bondalapati.
 *  22   360Commerce 1.21        1/28/2008 4:28:39 PM   Sandy Gu        save
 *       foreign currency id, description, issuing country code etc for
 *       foreign gift certificate and store credit tenders.
 *  21   360Commerce 1.20        12/14/2007 8:59:59 AM  Alan N. Sinton  CR
 *       29761: Removed non-PABP compliant methods and modified card RuleIfc
 *       to take an instance of EncipheredCardData.
 *  20   360Commerce 1.19        11/15/2007 10:28:04 AM Christian Greene Belize
 *        merge - add settlement columns
 *  19   360Commerce 1.18        11/12/2007 6:37:26 PM  Anil Bondalapati
 *       updated related to PABP.
 *  18   360Commerce 1.17        11/12/2007 4:28:28 PM  Alan N. Sinton  CR
 *       29598 - Changes for PABP.
 *  17   360Commerce 1.16        11/12/2007 2:14:22 PM  Tony Zgarba
 *       Deprecated all existing encryption APIs and migrated the code to the
 *       new encryption API.
 *  16   360Commerce 1.15        8/3/2007 9:59:17 AM    Michael P. Barnett In
 *       insertCreditDebitCardTenderLineItem(), check for null expiration
 *       date.
 *  15   360Commerce 1.14        7/19/2007 5:27:17 PM   Alan N. Sinton  CR
 *       27675 Populate Gift Certificate information if tender appears as
 *       refund or change.
 *  14   360Commerce 1.13        7/11/2007 4:26:40 PM   Mathews Kochummen
 *       update date parse
 *  13   360Commerce 1.12        6/11/2007 2:08:04 PM   Anda D. Cadar   SCR
 *       27106: Use the DateTimeServiceifc to convert the credit card
 *       expiration date
 *  12   360Commerce 1.11        5/29/2007 6:29:14 PM   Ashok.Mondal    Insert
 *       currencyID to GF, GC and tender line item tables.
 *  11   360Commerce 1.10        5/29/2007 12:28:26 PM  Ashok.Mondal    Insert
 *       currencyID to store credit tender line item table.
 *  10   360Commerce 1.9         5/18/2007 12:45:10 AM  Prakash Shanmugam i18n
 *       date changes done.
 *  9    360Commerce 1.8         4/25/2007 10:01:09 AM  Anda D. Cadar   I18N
 *       merge
 *  8    360Commerce 1.7         7/26/2006 4:15:07 PM   Charles D. Baker CR
 *       10,753 - Corrected typo, added safety, and removed unnecessary
 *       comments.
 *  7    360Commerce 1.6         7/25/2006 7:53:04 PM   Charles D. Baker
 *       Updated to handle DB update breaking single address line column into
 *       two. Corrected other special handling as mail bank check shares
 *       Customer object with Capture Cutomer.
 *  6    360Commerce 1.5         7/19/2006 12:51:40 PM  Brendan W. Farrell
 *       Create wrapper around encryption manager and service so that this can
 *        be used in either store server environment.
 *  5    360Commerce 1.4         1/25/2006 4:11:24 PM   Brett J. Larsen merge
 *       7.1.1 changes (aka. 7.0.3 fixes) into 360Commerce view
 *  4    360Commerce 1.3         12/13/2005 4:43:45 PM  Barry A. Pape
 *       Base-lining of 7.1_LA
 *  3    360Commerce 1.2         3/31/2005 4:28:44 PM   Robert Pearse
 *  2    360Commerce 1.1         3/10/2005 10:22:50 AM  Robert Pearse
 *  1    360Commerce 1.0         2/11/2005 12:12:04 PM  Robert Pearse
 * $
 * ===========================================================================
 */
package oracle.retail.stores.domain.arts;
import java.awt.Point;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import oracle.retail.stores.commerceservices.common.currency.CurrencyIfc;
import oracle.retail.stores.commerceservices.common.datetime.DateTimeServiceIfc;
import oracle.retail.stores.commerceservices.common.datetime.DateTimeServiceLocator;
import oracle.retail.stores.common.data.JdbcUtilities;
import oracle.retail.stores.common.sql.SQLDeleteStatement;
import oracle.retail.stores.common.sql.SQLInsertStatement;
import oracle.retail.stores.common.sql.SQLUpdateStatement;
import oracle.retail.stores.common.utility.LocaleMap;
import oracle.retail.stores.domain.DomainGateway;
import oracle.retail.stores.domain.tender.AuthorizableTenderIfc;
import oracle.retail.stores.domain.tender.TenderAlternateCurrencyIfc;
import oracle.retail.stores.domain.tender.TenderCashIfc;
import oracle.retail.stores.domain.tender.TenderChargeIfc;
import oracle.retail.stores.domain.tender.TenderCheckIfc;
import oracle.retail.stores.domain.tender.TenderCouponIfc;
import oracle.retail.stores.domain.tender.TenderGiftCardIfc;
import oracle.retail.stores.domain.tender.TenderGiftCertificateIfc;
import oracle.retail.stores.domain.tender.TenderLineItemConstantsIfc;
import oracle.retail.stores.domain.tender.TenderLineItemIfc;
import oracle.retail.stores.domain.tender.TenderMailBankCheckIfc;
import oracle.retail.stores.domain.tender.TenderMoneyOrderIfc;
import oracle.retail.stores.domain.tender.TenderPurchaseOrderIfc;
import oracle.retail.stores.domain.tender.TenderStoreCreditIfc;
import oracle.retail.stores.domain.tender.TenderTravelersCheckIfc;
import oracle.retail.stores.domain.transaction.SaleReturnTransactionIfc;
import oracle.retail.stores.domain.transaction.TenderableTransactionIfc;
import oracle.retail.stores.domain.transaction.TransactionIfc;
import oracle.retail.stores.domain.utility.AddressIfc;
import oracle.retail.stores.domain.utility.DomainUtil;
import oracle.retail.stores.domain.utility.EYSDate;
import oracle.retail.stores.domain.utility.EntryMethod;
import oracle.retail.stores.foundation.manager.data.DataException;
import oracle.retail.stores.foundation.manager.data.JdbcDataConnection;
import oracle.retail.stores.foundation.manager.device.EncipheredDataIfc;
import oracle.retail.stores.foundation.manager.ifc.data.DataActionIfc;
import oracle.retail.stores.foundation.manager.ifc.data.DataConnectionIfc;
import oracle.retail.stores.foundation.manager.ifc.data.DataTransactionIfc;
import oracle.retail.stores.foundation.utility.Util;
import oracle.retail.stores.persistence.utility.ARTSDatabaseIfc;
import oracle.retail.stores.persistence.utility.DatabaseBlobHelperFactory;
import oracle.retail.stores.persistence.utility.DatabaseBlobHelperIfc;
/**
 * Save all tender line items of the Transaction to the database.
 */
public class JdbcSaveTenderLineItems extends JdbcSaveRetailTransactionLineItems implements ARTSDatabaseIfc
{
    /** Serial ID */
    private static final long serialVersionUID = -2401829364714460844L;

    /**
     * The logger to which log messages will be sent.
     */
    private static final Logger logger = Logger.getLogger(JdbcSaveTenderLineItems.class);

    /**
     * the retail transaction line item type
     */
    protected final static String TYPE_TENDER = "TN";
    protected final static String TYPE_TENDER_CHANGE = "TENDER CHANGE";
    protected static DateTimeServiceIfc dateTimeService = DateTimeServiceLocator.getDateTimeService();

    /**
     * Class constructor.
     */
    public JdbcSaveTenderLineItems()
    {
        super();
        setName("JdbcSaveTenderLineItems");
    }

    /**
     * Execute the SQL statements against the database.
     *
     * @param dataTransaction
     *            reference to the complete data manager transaction
     * @param dataConnection
     *            reference to the db connection
     * @param action
     *            contains the business object to put in the db
     * @exception DataException
     *                error saving data to the db
     */
    public void execute(DataTransactionIfc dataTransaction, DataConnectionIfc dataConnection, DataActionIfc action)
        throws DataException
    {
       /* if (logger.isDebugEnabled()) logger.debug( "JdbcSaveTenderLineItems.execute()");

        
         * getUpdateCount() is about the only thing outside of
         * DataConnectionIfc that we need.
         
        JdbcDataConnection connection = (JdbcDataConnection) dataConnection;

        // Navigate the input object to obtain values that will be inserted
        // into the database.
        ARTSTransaction trans = (ARTSTransaction) action.getDataObject();

        TenderableTransactionIfc tt = (TenderableTransactionIfc) trans.getPosTransaction();
        saveTenderLineItems(connection, tt);

        if (tt.getUniqueID() != null)
        {
            // if transaction has a unique Id update workstation table
            updateWorkstationUniqueID(connection, tt);
        }

        if (logger.isDebugEnabled()) logger.debug( "JdbcSaveTenderLineItems.execute()");*/
    }
    
    
    
    public void execute(TenderableTransactionIfc transaction, JdbcTemplate connection)
            throws DataException
        {
            if (logger.isDebugEnabled()) logger.debug( "JdbcSaveTenderLineItems.execute()");



            saveTenderLineItems(connection, transaction);

            if (transaction.getUniqueID() != null)
            {
                // if transaction has a unique Id update workstation table
                //updateWorkstationUniqueID(connection, transaction);
            }

            if (logger.isDebugEnabled()) logger.debug( "JdbcSaveTenderLineItems.execute()");
        }

    /**
     * Updates the unique ID used for store credit, layaway and special order
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            tenderableTransaction
     * @exception DataException
     *                upon error
     */
    public void updateWorkstationUniqueID(JdbcTemplate dataConnection, TenderableTransactionIfc transaction)
        throws DataException
    {
        if (logger.isDebugEnabled()) logger.debug(
            "JdbcSaveTenderLineItems.updateWorkstationUniqueID()");

        SQLUpdateStatement sql = new SQLUpdateStatement();
        sql.setTable(TABLE_WORKSTATION);
        sql.addColumn(FIELD_UNIQUE_IDENTIFIER_EXTENSION, inQuotes(transaction.getUniqueID()));
        sql.addQualifier(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID, getWorkstationID(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error(de);
            throw de;
        }
        catch (Exception e)
        {
            logger.error(e);
            throw new DataException(DataException.UNKNOWN, "updateWorkstationUniqueID", e);
        }

        if (logger.isDebugEnabled()) logger.debug( "JdbcSaveTenderLineItems.updateWorkstationUniqueID()");
    }

    /**
     * Deletes the voided tender store credit.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error deleting data from the db
        public void deleteVoidStoreCredit(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        TenderStoreCreditIfc lineItem)
        throws DataException
    {
        SQLDeleteStatement sql = new SQLDeleteStatement();

        // Table
        sql.setTable(TABLE_STORE_CREDIT_TENDER_LINE_ITEM);

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(FIELD_STORE_CREDIT_ID + " = " + lineItem.getNumber());
        if (TenderLineItemIfc.CERTIFICATE_TYPE_FOREIGN.equals(((TenderCertificateIfc) lineItem).getCertificateType()))
        {
            sql.addQualifier(
                    FIELD_STORE_CREDIT_FOREIGN_FACE_VALUE_AMOUNT
                    + " = "
                    + lineItem.getAlternateCurrencyTendered().abs().toString());
        }
        else
        {
            sql.addQualifier(FIELD_STORE_CREDIT_BALANCE + " = " + lineItem.getAmountTender().abs().toString());
        }

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (SQLException se)
        {
            throw new DataException(DataException.SQL_ERROR, "deleteVoidStoreCredit", se);
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "deleteVoidStoreCredit", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "deleteVoidStoreCredit");
        }
    }
    */
    /**
     * Saves the tender line items to the data store.
     * @param connection
     *            connection to the db
     * @param transaction
     *            retail transaction which holds all tender line items
     * @exception DataException
     *                upon error
     */
    public void saveTenderLineItems(JdbcTemplate connection, TenderableTransactionIfc transaction)
        throws DataException
    {
        /*
         * These aren't numbered in the domain object, so let's start at zero.
         */
        int lineItemSequenceNumber = 0;

        if (transaction instanceof SaleReturnTransactionIfc)
        {
            /*
             * Initialize the line item sequence number Need to count
             * sale/return line items, tax line items and discount line items
             */
            SaleReturnTransactionIfc srt = (SaleReturnTransactionIfc) transaction;
            int numDiscounts = 0;
            if (srt.getTransactionDiscounts() != null)
            {
                numDiscounts = srt.getTransactionDiscounts().length;
            }
            lineItemSequenceNumber = srt.getLineItems().length + 1 // TaxLineItem
            +numDiscounts;
        }

        /*
         * Loop through each line item. Continue through them all even if one
         * has failed.
         */
        int numItems = 0;
        TenderLineItemIfc[] tenderLineItems = transaction.getTenderLineItems();
        if (tenderLineItems != null)
        {
            numItems = tenderLineItems.length;
        }
        for (int i = 0; i < numItems; i++)
        {
            TenderLineItemIfc lineItem = tenderLineItems[i];

            saveTenderLineItem(connection, transaction, lineItemSequenceNumber, lineItem);
            ++lineItemSequenceNumber;
        }

        /*
         * If there was change returned, save that too.
         */
        if (transaction.getTransactionStatus() == TransactionIfc.STATUS_COMPLETED
            && transaction.getTenderTransactionTotals().getChangeDue().signum() != CurrencyIfc.ZERO)
        {
            insertTenderChangeLineItem(connection, transaction, lineItemSequenceNumber);
            ++lineItemSequenceNumber;
        }

        /*
         * If there was rounded change, save that too.
         */
        CurrencyIfc adjustment = getTenderChangeRoundedAmount(transaction);
        if (transaction.getTransactionStatus() == TransactionIfc.STATUS_COMPLETED
            && adjustment.signum() != CurrencyIfc.ZERO)
        {
            insertRoundingTenderChangeLineItem(connection, transaction, lineItemSequenceNumber);
        }

    }

    /**
     * Saves one tender line item.
     * @param connection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lieItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void saveTenderLineItem(
        JdbcTemplate connection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderLineItemIfc lineItem)
        throws DataException
    {
        if (lineItem instanceof TenderCashIfc)
        {
            /*
             * Cash tender updates the Tender Line Item and Retail Transaction
             * Line Item tables.
             */
            insertTenderLineItem(connection, transaction, lineItemSequenceNumber, lineItem);
        }
        else
            if (lineItem instanceof TenderMoneyOrderIfc)
            {
                /*
                 * Cash tender updates the Tender Line Item and Retail
                 * Transaction Line Item tables.
                 */
                insertMoneyOrderTenderLineItem(
                    connection,
                    transaction,
                    lineItemSequenceNumber,
                    (TenderMoneyOrderIfc) lineItem);
            }
            else
                if (lineItem instanceof TenderGiftCardIfc)
                {
                    /*
                     * This one MUST come before TenderChargeIfc because
                     * TenderGiftCardIfc extends it. Gift Card tender updates
                     * the Gift Card Tender Line Item, Tender Line Item, and
                     * Retail Transaction Line Item tables.
                     */
                    insertGiftCardTenderLineItem(
                        connection,
                        transaction,
                        lineItemSequenceNumber,
                        (TenderGiftCardIfc) lineItem);
                }
                else
                    if (lineItem instanceof TenderChargeIfc)
                    {
                        /*
                         * Charge tender updates the Credit/Debit Card Tender
                         * Line Item, Tender Line Item, and Retail Transaction
                         * Line Item tables.
                         */
                        insertCreditDebitCardTenderLineItem(
                            connection,
                            transaction,
                            lineItemSequenceNumber,
                            (TenderChargeIfc) lineItem);
                    }
                    else
                        if (lineItem instanceof TenderCheckIfc)
                        {
                            /*
                             * Check tender updates the Check Tender Line Item,
                             * Tender Line Item, and Retail Transaction Line
                             * Item tables.
                             */
                            insertCheckTenderLineItem(
                                connection,
                                transaction,
                                lineItemSequenceNumber,
                                (TenderCheckIfc) lineItem);
                        }
                        else
                            if (lineItem instanceof TenderGiftCertificateIfc)
                            {
                                /*
                                 * Gift Certificate tender updates the Gift
                                 * Certificate Tender Line Item, Tender Line
                                 * Item, and Retail Transaction Line Item
                                 * tables.
                                 */
                                if (!transaction.isTrainingMode())
                                {
                                    insertGiftCertificateTenderLineItem(
                                        connection,
                                        transaction,
                                        lineItemSequenceNumber,
                                        (TenderGiftCertificateIfc) lineItem);
                                }
                            }
                            else
                                if (lineItem instanceof TenderMailBankCheckIfc)
                                {
                                    /*
                                     * Mail Bank Check tender updates the Send
                                     * Check Tender Line Item, Tender Line
                                     * Item, and Retail Transaction Line Item
                                     * tables.
                                     */
                                    insertSendCheckTenderLineItem(
                                        connection,
                                        transaction,
                                        lineItemSequenceNumber,
                                        (TenderMailBankCheckIfc) lineItem);
                                }
                                else
                                    if (lineItem instanceof TenderTravelersCheckIfc)
                                    {
                                        /*
                                         * Travelers Check tender updates the
                                         * Send Check Tender Line Item, Tender
                                         * Line Item, and Retail Transaction
                                         * Line Item tables.
                                         */
                                        insertTravelersCheckTenderLineItem(
                                            connection,
                                            transaction,
                                            lineItemSequenceNumber,
                                            (TenderTravelersCheckIfc) lineItem);
                                    }
                                    else
                                        if (lineItem instanceof TenderCouponIfc)
                                        {
                                            /*
                                             * Coupon tender updates the Coupon
                                             * Tender Line Item, Tender Line
                                             * Item, and Retail Transaction
                                             * Line Item tables.
                                             */
                                            insertCouponTenderLineItem(
                                                connection,
                                                transaction,
                                                lineItemSequenceNumber,
                                                (TenderCouponIfc) lineItem);
                                        }
                                        else
                                            if (lineItem instanceof TenderStoreCreditIfc)
                                            {
                                                /*
                                                 * Store credit tender updates
                                                 * the Store Credit Tender Line
                                                 * Item, Tender Line Item, and
                                                 * Retail Transaction Line Item
                                                 * tables.
                                                 */
                                                 insertStoreCreditTenderLineItem(
                                                    connection,
                                                    transaction,
                                                    lineItemSequenceNumber,
                                                    (TenderStoreCreditIfc) lineItem);
                                            }
                                            else
                                                if (lineItem instanceof TenderPurchaseOrderIfc)
                                                {
                                                    /*
                                                     * Purchase Order tender
                                                     * updates the Send Check
                                                     * Tender Line Item, Tender
                                                     * Line Item, and Retail
                                                     * Transaction Line Item
                                                     * tables.
                                                     */
                                                    insertPurchaseOrderTenderLineItem(
                                                        connection,
                                                        transaction,
                                                        lineItemSequenceNumber,
                                                        (TenderPurchaseOrderIfc) lineItem);
                                                }
                                                else
                                                {
                                                    /*
                                                     * Unknown tender line item
                                                     * type
                                                     */
                                                    String msg =
                                                        "Unknown Tender type: " + lineItem.getClass().getName();
                                                    logger.error(
                                                        "" + msg + "");
                                                    throw new DataException(DataException.DATA_FORMAT, msg);
                                                }
    }

    /**
     * Inserts a tender change line item.
     * @param connection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lieItemSequenceNumber
     *            sequence number associated with this line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertTenderChangeLineItem(
        JdbcTemplate connection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber)
        throws DataException
    {
        /*
         * Update the Retail Transaction Line Item table first
         */
        insertRetailTransactionLineItem(connection, transaction, lineItemSequenceNumber, TYPE_TENDER_CHANGE);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_TENDER_CHANGE_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_TENDER_TYPE_CODE, getTenderChangeType(transaction));
        sql.addColumn(FIELD_TENDER_CHANGE_LINE_ITEM_AMOUNT, getTenderChangeAmount(transaction));
        sql.addColumn(FIELD_RECORD_LAST_MODIFIED_TIMESTAMP, getSQLCurrentTimestampFunction());
        sql.addColumn(FIELD_RECORD_CREATION_TIMESTAMP, getSQLCurrentTimestampFunction());

        try
        {
            connection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertTenderLineItem", e);
        }
    }

    /**
     * Inserts a tender change line item for rounded change amount.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lieItemSequenceNumber
     *            sequence number associated with this line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertRoundingTenderChangeLineItem(
            JdbcTemplate dataConnection,
            TenderableTransactionIfc transaction, int lineItemSequenceNumber)
            throws DataException
    {
        /*
         * Update the Retail Transaction Line Item table first
         */
        insertRetailTransactionLineItem(dataConnection, transaction, lineItemSequenceNumber, TYPE_TENDER_CHANGE);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_TENDER_CHANGE_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_TENDER_TYPE_CODE, inQuotes(TenderLineItemConstantsIfc.CASH_ROUNDING_NAME));
        sql.addColumn(FIELD_TENDER_CHANGE_LINE_ITEM_AMOUNT, getTenderChangeRoundedAmount(transaction).toString());
        sql.addColumn(FIELD_RECORD_LAST_MODIFIED_TIMESTAMP, getSQLCurrentTimestampFunction());
        sql.addColumn(FIELD_RECORD_CREATION_TIMESTAMP, getSQLCurrentTimestampFunction());

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertTenderLineItem", e);
        }
    }
    
    
    /**
     * Updates a record in the tender line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void updateTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderLineItemIfc lineItem)
        throws DataException
    {
        /*
         * Update the Retail Transaction Line Item table first
         */
        updateRetailTransactionLineItem(dataConnection, transaction, lineItemSequenceNumber, TYPE_TENDER);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_TENDER_TYPE_CODE, getTenderType(lineItem));
        sql.addColumn(FIELD_TENDER_LINE_ITEM_AMOUNT, getTenderAmount(lineItem));
        sql.addColumn(FIELD_RECORD_LAST_MODIFIED_TIMESTAMP, getSQLCurrentTimestampFunction());

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        // alternate tender support - remove strings when artsdatabaseifc is
        // available !
        if ((lineItem instanceof TenderAlternateCurrencyIfc)
            && ((TenderAlternateCurrencyIfc) lineItem).getAlternateCurrencyTendered() != null)
        {
            CurrencyIfc lineItemiAlt = ((TenderAlternateCurrencyIfc) lineItem).getAlternateCurrencyTendered();
            sql.addColumn(FIELD_TENDER_LOCAL_CURRENCY_DESCRIPTION, getLocalCurrencyDescription());
            sql.addColumn(FIELD_TENDER_FOREIGN_CURRENCY_DESCRIPTION, getAlternateCurrencyDescription(lineItemiAlt));
            sql.addColumn(FIELD_EXCHANGE_RATE_TO_BUY_AMOUNT, lineItemiAlt.getBaseConversionRate().toString());
            sql.addColumn(FIELD_TENDER_FOREIGN_CURRENCY_COUNTRY_CODE, getCountryCode(lineItemiAlt));
            sql.addColumn(FIELD_TENDER_FOREIGN_CURRENCY_AMOUNT_TENDERED, lineItemiAlt.toString());
            sql.addColumn(FIELD_CURRENCY_ID, lineItemiAlt.getType().getCurrencyId());
        }
        else
        {
            sql.addColumn(FIELD_CURRENCY_ID, lineItem.getAmountTender().getType().getCurrencyId());
        }

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updateTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update TenderLineItem");
        }
    }

    /**
     * Updates the tender credit debit line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void updateCreditDebitCardTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderChargeIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        updateTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_CREDIT_DEBIT_CARD_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_TENDER_TYPE_CODE, getTenderType(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DEBIT_CREDIT_CARD_ACCOUNT_MASKED, makeSafeString(lineItem.getMaskedCardNumber()));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_ACCOUNT_NUMBER_TOKEN, makeSafeString(lineItem.getAccountNumberToken()));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CREDIT_DEBIT_CARD_ADJUDICATION_CODE, getAuthorizationCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CARD_NUMBER_SWIPED_OR_KEYED_CODE, getEntryMethod(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_TENDER_MEDIA_ISSUER_ID, getCardType(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CARD_TYPE,getTenderDescription(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_METHOD_CODE, getMethodCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_JOURNAL_KEY, getAuthorizationJournalKey(lineItem));

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updateCreditDebitCardTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update CreditDebitCardTenderLineItem");
        }
    }

    /**
     * Updates the tender check line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception error
     *                saving data to the db
     */
    public void updateCheckTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderCheckIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        updateTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_CHECK_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_TENDER_CHECK_AUTHORIZATION_PERSONAL_ID_REQUIRED_TYPE_CODE, getIDType(lineItem));
        sql.addColumn(FIELD_TENDER_CHECK_AUTHORIZATION_ENCRYPTED_PERSONAL_ID_NUMBER, getEncryptedIDNumber(lineItem));
        sql.addColumn(FIELD_TENDER_CHECK_AUTHORIZATION_MASKED_PERSONAL_ID_NUMBER, getMaskedIDNumber(lineItem));
        sql.addColumn(FIELD_TENDER_CHECK_AUTHORIZATION_PERSONAL_ID_ISSUER, makeSafeString(lineItem.getIDIssuer()));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CHECK_BIRTH_DATE, getDateOfBirth(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CHECK_ADJUDICATION_CODE, getAuthorizationCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CHECK_BANK_ID, getBankID(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_ENCRYPTED_CHECK_ACCOUNT_NUMBER, getEncryptedCheckAccountNumber(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_MASKED_CHECK_ACCOUNT_NUMBER, getMaskedCheckAccountNumber(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CHECK_SEQUENCE_NUMBER, getCheckSequenceNumber(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_METHOD_CODE, getMethodCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CHECK_DATA_SCANNED_OR_KEYED_CODE, getEntryMethod(lineItem));

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updateCheckTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update CheckTenderLineItem");
        }
    }

    /**
     * Updates the tender gift certificate line item and gift certificate
     * tender line item tables.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void updateGiftCertificateTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderGiftCertificateIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        updateTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_GIFT_CERTIFICATE_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_GIFT_CERTIFICATE_SERIAL_NUMBER, getGiftCertificateSerialNumber(lineItem));
        sql.addColumn(FIELD_ISSUING_STORE_NUMBER, makeSafeString(lineItem.getStoreNumber()));
        sql.addColumn(FIELD_GIFT_CERTIFICATE_FACE_VALUE_AMOUNT, lineItem.getAmountTender().toString());
        if (((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered() != null)
        {
            sql.addColumn(
                FIELD_GIFT_CERTIFICATE_FOREIGN_FACE_VALUE_AMOUNT,
                ((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered().toString());
            sql.addColumn(FIELD_CURRENCY_ID, ((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered().getType().getCurrencyId());
        }
        else
        {
            sql.addColumn(FIELD_CURRENCY_ID, lineItem.getAmountTender().getType().getCurrencyId());
        }

        if (lineItem.getCertificateType() != null)
        {
            sql.addColumn(FIELD_GIFT_CERTIFICATE_SUBTENDER_TYPE,
                          makeSafeString(lineItem.getCertificateType()));
        }

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updateGiftCertificateTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update GiftCertificateTenderLineItem");
        }
    }

    /**
     * Deletes the voided tender gift certificate.
     *
     * @param dataConnection connection to the db
     * @param transaction retail transaction
     * @param lineItem the tender line item
     * @exception DataException  error deleting data from the db
     * @deprecated as of 13.0 TenderGiftCertificateIfc should not be deleted or else missing certs will break POSLog export
     */
    public void deleteVoidGiftCertificate(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        TenderGiftCertificateIfc lineItem)
        throws DataException
    {
        SQLDeleteStatement sql = new SQLDeleteStatement();

        // Table
        sql.setTable(TABLE_GIFT_CERTIFICATE_TENDER_LINE_ITEM);

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(FIELD_GIFT_CERTIFICATE_SERIAL_NUMBER + " = " + getGiftCertificateSerialNumber(lineItem));
        if (((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered() != null)
        {
            sql.addQualifier(
                FIELD_GIFT_CERTIFICATE_FOREIGN_FACE_VALUE_AMOUNT
                    + " = "
                    + ((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered().toString());
        }
        else
        {
            sql.addQualifier(
                FIELD_GIFT_CERTIFICATE_FACE_VALUE_AMOUNT + " = " + lineItem.getAmountTender().negate().toString());
        }
        sql.addQualifier(FIELD_ISSUING_STORE_NUMBER + " = " + makeSafeString(lineItem.getStoreNumber()));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "deleteVoidGiftCertificate", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "deleteVoidGiftCertificate");
        }
    }

    /**
     * Updates the send check tender line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void updateSendCheckTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderMailBankCheckIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        updateTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_SEND_CHECK_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_ADDRESS_LINE_1, getSendCheckPayableAddressLine1(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_ADDRESS_LINE_2, getSendCheckPayableAddressLine2(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_CITY, getSendCheckCity(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_POSTAL_CODE, getSendCheckPostalCode(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_STATE, getSendCheckState(lineItem));
        //sql.addColumn(FIELD_SEND_CHECK_REASON_CODE,
        // getSendCheckReasonCode(lineItem));

        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_NAME_PREFIX, getSendCheckNamePrefix(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_FIRST_NAME, getSendCheckFirstName(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_MIDDLE_NAME, getSendCheckMiddleName(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_LAST_NAME, getSendCheckLastName(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_NAME_SUFFIX, getSendCheckNameSuffix(lineItem));

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updateSendCheckTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update SendCheckTenderLineItem");
        }
    }

    /**
     * Updates the travelers check tender line item table.
     *
     * @param dataConnection connection to the db
     * @param transaction retail transaction
     * @param lineItemSequenceNumber sequence number associated with this line
     *            item
     * @param lineItem the tender line item
     * @exception DataException error saving data to the db
     */
    public void updateTravelersCheckTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderTravelersCheckIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        updateTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_TRAVELERS_CHECK_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_TRAVELERS_CHECK_COUNT, lineItem.getNumberChecks());

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updateTravelersCheckTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update TravelersCheckTenderLineItem");
        }
    }

    /**
     * Updates the money order tender line item table.
     *
     * @param dataConnection connection to the db
     * @param transaction retail transaction
     * @param lineItemSequenceNumber sequence number associated with this line
     *            item
     * @param lineItem the tender line item
     * @exception DataException error saving data to the db
     */
    public void updateMoneyOrderTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderMoneyOrderIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        updateTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_MONEY_ORDER_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_TENDER_LINE_ITEM_AMOUNT, getTenderAmount(lineItem));

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updateMoneyOrderTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update MoneyOrderTenderLineItem");
        }
    }

    /**
     * Updates the tender gift card line item table.
     *
     * @param dataConnection connection to the db
     * @param transaction retail transaction
     * @param lineItemSequenceNumber sequence number associated with this line
     *            item
     * @param lineItem the tender line item
     * @exception DataException error saving data to the db
     */
    public void updateGiftCardTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderGiftCardIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        updateTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_GIFT_CARD_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_GIFT_CARD_SERIAL_NUMBER, getGiftCardSerialNumber(lineItem));
        sql.addColumn(FIELD_MASKED_GIFT_CARD_SERIAL_NUMBER, getMaskedGiftCardSerialNumber(lineItem));
        sql.addColumn(FIELD_GIFT_CARD_ADJUDICATION_CODE, getGiftCardAuthorizationCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CARD_NUMBER_SWIPED_OR_KEYED_CODE, getEntryMethod(lineItem));
        sql.addColumn(FIELD_GIFT_CARD_CREDIT_FLAG, makeStringFromBoolean(lineItem.isGiftCardCredit()));
        sql.addColumn(FIELD_GIFT_CARD_REQUEST_TYPE, inQuotes(lineItem.getRequestCode()));
        sql.addColumn(FIELD_CURRENCY_ID, lineItem.getAmountTender().getType().getCurrencyId());

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updateGiftCardTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update GiftCardTenderLineItem");
        }
    }

    /**
     * Updates the coupon tender line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void updateCouponTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderCouponIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        updateTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_COUPON_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_COUPON_SCAN_CODE, getCouponNumber(lineItem));
        sql.addColumn(FIELD_COUPON_TYPE, getCouponType(lineItem));
        sql.addColumn(FIELD_COUPON_KEY_ENTERED_FLAG, getCouponEntryMethod(lineItem));

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updateCouponTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update CouponTenderLineItem");
        }
    }

    /**
     * Updates the purchase order tender line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void updatePurchaseOrderTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderPurchaseOrderIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        updateTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_PURCHASE_ORDER_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(PURCHASE_ORDER_NUMBER, getPurchaseOrderNumber(lineItem));
        sql.addColumn(PURCHASE_ORDER_AMOUNT, getPurchaseOrderAmount(lineItem));
        sql.addColumn(PURCHASE_ORDER_AGENCY_NAME, getPurchaseOrderAgencyName(lineItem));

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updatePurchaseOrderTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update PurchaseOrderTenderLineItem");
        }
    }

    /**
     * Updates the store credit tender line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void updateStoreCreditTenderlineitem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderStoreCreditIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        updateTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLUpdateStatement sql = new SQLUpdateStatement();

        // Table
        sql.setTable(TABLE_STORE_CREDIT_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_STORE_CREDIT_ID, getStoreCreditNumber(lineItem));

        sql.addColumn(FIELD_STORE_CREDIT_BALANCE, lineItem.getAmountTender().toString());
        if (((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered() != null)
        {
            sql.addColumn(
                    FIELD_STORE_CREDIT_FOREIGN_FACE_VALUE_AMOUNT,
                    ((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered().toString());
            //+I18N
            sql.addColumn(FIELD_CURRENCY_ID, ((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered().getType().getCurrencyId());
            //-I18N
        }
        else
        {
            // NOT NULL field designation
            sql.addColumn(
                    FIELD_STORE_CREDIT_FOREIGN_FACE_VALUE_AMOUNT,
                    "0.00");
            //+I18N
            sql.addColumn(FIELD_CURRENCY_ID, lineItem.getAmount().getType().getCurrencyId());
            //-I18N
        }

        // Qualifiers
        sql.addQualifier(FIELD_RETAIL_STORE_ID + " = " + getStoreID(transaction));
        sql.addQualifier(FIELD_WORKSTATION_ID + " = " + getWorkstationID(transaction));
        sql.addQualifier(FIELD_TRANSACTION_SEQUENCE_NUMBER + " = " + getTransactionSequenceNumber(transaction));
        sql.addQualifier(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER + " = " + getSequenceNumber(lineItemSequenceNumber));
        sql.addQualifier(FIELD_BUSINESS_DAY_DATE + " = " + getBusinessDayString(transaction));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "updateStoreCreditTenderLineItem", e);
        }

        if (0 >= dataConnection.getFetchSize())
        {
            throw new DataException(DataException.NO_DATA, "Update StoreCreditTenderLineItem");
        }
    }

    /**
     * Inserts a tender line Item.
     * @param connection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertTenderLineItem(
        JdbcTemplate connection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderLineItemIfc lineItem)
        throws DataException
    {
        /*
         * Update the Retail Transaction Line Item table first
         */
        insertRetailTransactionLineItem(connection, transaction, lineItemSequenceNumber, TYPE_TENDER);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));
        sql.addColumn(FIELD_TENDER_TYPE_CODE, getTenderType(lineItem));
        sql.addColumn(FIELD_TENDER_LINE_ITEM_AMOUNT, getTenderAmount(lineItem));

        // alternate currency support --
        if ((lineItem instanceof TenderAlternateCurrencyIfc)
            && ((TenderAlternateCurrencyIfc) lineItem).getAlternateCurrencyTendered() != null)
        {
            CurrencyIfc lineItemiAlt = ((TenderAlternateCurrencyIfc) lineItem).getAlternateCurrencyTendered();
            sql.addColumn(FIELD_TENDER_LOCAL_CURRENCY_DESCRIPTION, getLocalCurrencyDescription());
            sql.addColumn(FIELD_TENDER_FOREIGN_CURRENCY_DESCRIPTION, getAlternateCurrencyDescription(lineItemiAlt));
            sql.addColumn(FIELD_EXCHANGE_RATE_TO_BUY_AMOUNT, lineItemiAlt.getBaseConversionRate().toString());
            sql.addColumn(FIELD_TENDER_FOREIGN_CURRENCY_COUNTRY_CODE, getCountryCode(lineItemiAlt));
            sql.addColumn(FIELD_TENDER_FOREIGN_CURRENCY_AMOUNT_TENDERED, lineItemiAlt.toString());
            sql.addColumn(FIELD_CURRENCY_ID, lineItemiAlt.getType().getCurrencyId());
        }
        else
        {
            sql.addColumn(FIELD_CURRENCY_ID, lineItem.getAmountTender().getType().getCurrencyId());
        }
        sql.addColumn(FIELD_RECORD_LAST_MODIFIED_TIMESTAMP, getSQLCurrentTimestampFunction());
        sql.addColumn(FIELD_RECORD_CREATION_TIMESTAMP, getSQLCurrentTimestampFunction());

        try
        {
            connection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertTenderLineItem", e);
        }
    }

    /**
     * Inserts into the tender credit debit line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @return true if successful
     * @exception DataException
     *                error saving data to the db
     */
    public void insertCreditDebitCardTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderChargeIfc lineItem)
        throws DataException
    {
        // Update the Tender Line Item table first.
        insertTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_CREDIT_DEBIT_CARD_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));
        sql.addColumn(FIELD_TENDER_TYPE_CODE, getTenderType(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DEBIT_CREDIT_CARD_ACCOUNT_MASKED, makeSafeString(lineItem.getMaskedCardNumber()));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_ACCOUNT_NUMBER_TOKEN, makeSafeString(lineItem.getAccountNumberToken()));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CREDIT_DEBIT_CARD_ADJUDICATION_CODE, getAuthorizationCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CARD_NUMBER_SWIPED_OR_KEYED_CODE, getEntryMethod(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_TENDER_MEDIA_ISSUER_ID, getCardType(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CARD_TYPE,getTenderDescription(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_METHOD_CODE, getMethodCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DEBIT_CREDIT_ID_TYPE, makeSafeString(lineItem.getPersonalIDType().getCode()));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DEBIT_CREDIT_ID_COUNTRY, getIDCountry(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DEBIT_CREDIT_ID_STATE, getIDState(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DEBIT_CREDIT_ID_EXPIRATION_DATE, getIDExpirationDate(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_SETTLEMENT_DATA, getAuthorizationSettlementData(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DATE_TIME, getAuthorizationDateTime(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_MESSAGE_SEQUENCE_NUMBER, getReferenceCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DATE, getAuthorizationDate(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_TIME, getAuthorizationTime(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_RETRIEVAL_REFERENCE_NUMBER, getRetrievalReferenceNumber(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_RESPONSE_CODE, getAuthResponseCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_ORIGINAL_AUTHORIZATION_ACCOUNT_DATA_SOURCE_CODE, getAccountDataSource(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_ORIGINAL_AUTHORIZATION_PAYMENT_SERVICE_INDICATOR, getPaymentServiceIndicator(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_ORIGINAL_AUTHORIZATION_TRANSACTION_IDENTIFICATION_NUMBER, getTransactionIdentificationNumber(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_ORIGINAL_AUTHORIZATION_VALIDATION_CODE, getValidationCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_ORIGINAL_AUTHORIZATION_SOURCE_CODE, getAuthorizationSource(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_ORIGINAL_HOST_TRANSACTION_REFERENCE_NUMBER, getHostReference(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_TRANSACTION_TRACE_NUMBER, getTraceNumber(lineItem));
        sql.addColumn(FIELD_TENDER_REMAINING_PREPAID_BALANCE_AMOUNT, getPrepaidRemainingBalanceAmount(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_TRANSACTION_KSN_NUMBER,getKSN_20(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_AFTER_PROMOTION_ACCOUNT_APR,getAccountAPR(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_AFTER_PROMOTION_ACCOUNT_APR_TYPE,getAccountAPRType(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DURING_PROMOTION_APR,getPromotionAPR(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DURING_PROMOTION_APR_TYPE,getPromotionAPRType(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_PROMOTION_DESCRIPTION,getPromotionDescription(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_PROMOTION_DURATION,getPromotionDuration(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_JOURNAL_KEY, getAuthorizationJournalKey(lineItem));
        try
        {
            dataConnection.execute(sql.getSQLString());
            /*HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(FIELD_RETAIL_STORE_ID, transaction.getWorkstation().getStoreID());
            map.put(FIELD_WORKSTATION_ID, transaction.getWorkstation().getWorkstationID());
            map.put(FIELD_TRANSACTION_SEQUENCE_NUMBER, Integer.valueOf(getTransactionSequenceNumber(transaction)));
            map.put(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, Integer.valueOf(getSequenceNumber(lineItemSequenceNumber)));
            SimpleDateFormat format = new SimpleDateFormat(JdbcUtilities.YYYYMMDD_DATE_FORMAT_STRING);
            String businessDate = format.format(transaction.getBusinessDay().dateValue());
            map.put(FIELD_BUSINESS_DAY_DATE, businessDate);
            DatabaseBlobHelperIfc helper = DatabaseBlobHelperFactory.getInstance()
                    .getDatabaseBlobHelper(dataConnection.getDataSource().getConnection());
            String signatureData = getSignatureData(lineItem);
            if(helper != null)
            {
                helper.updateBlob(dataConnection.getDataSource().getConnection(),
                        TABLE_CREDIT_DEBIT_CARD_TENDER_LINE_ITEM,
                        FIELD_TENDER_AUTHORIZATION_CUSTOMER_SIGNATURE_IMAGE,
                        signatureData.getBytes(),
                        map);
            }*/
            // save ICC details of chip card
            /*if (lineItem.getICCDetails() != null)
            {
                IntegratedChipCardDetailsDAOIfc dao = (IntegratedChipCardDetailsDAOIfc)BeanLocator.getPersistenceBean(IntegratedChipCardDetailsDAOIfc.DAO_BEAN_KEY);
                dao.persist(transaction.getWorkstation().getStoreID(),
                        transaction.getWorkstation().getWorkstationID(), businessDate,
                        transaction.getTransactionSequenceNumber(),
                        lineItemSequenceNumber, lineItem.getICCDetails());
            }*/
        }
        catch (DataException de)
        {
            logger.error(de);
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertCreditDebitCardTenderLineItem", e);
        }
    }

    /**
     * Inserts into the tender check line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertCheckTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderCheckIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        insertTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_CHECK_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));

        sql.addColumn(FIELD_TENDER_CHECK_AUTHORIZATION_PERSONAL_ID_REQUIRED_TYPE_CODE, makeSafeString(lineItem.getPersonalIDType().getCode()));
        sql.addColumn(FIELD_TENDER_CHECK_AUTHORIZATION_ENCRYPTED_PERSONAL_ID_NUMBER, getEncryptedIDNumber(lineItem));
        sql.addColumn(FIELD_TENDER_CHECK_AUTHORIZATION_MASKED_PERSONAL_ID_NUMBER, getMaskedIDNumber(lineItem));
        sql.addColumn(FIELD_TENDER_CHECK_AUTHORIZATION_PERSONAL_ID_ISSUER, makeSafeString(lineItem.getIDIssuer()));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CHECK_BIRTH_DATE, getDateOfBirth(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CHECK_ADJUDICATION_CODE, getAuthorizationCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CHECK_BANK_ID, getBankID(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_ENCRYPTED_CHECK_ACCOUNT_NUMBER, getEncryptedCheckAccountNumber(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_MASKED_CHECK_ACCOUNT_NUMBER, getMaskedCheckAccountNumber(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CHECK_SEQUENCE_NUMBER, getCheckSequenceNumber(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_METHOD_CODE, getMethodCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CHECK_DATA_SCANNED_OR_KEYED_CODE, getEntryMethod(lineItem));

        // echeck info
        sql.addColumn(FIELD_CUSTOMER_PHONE_NUMBER, makeSafeString(lineItem.getPhoneNumber()));
        sql.addColumn(FIELD_TENDER_ENCRYPTED_CHECK_MICR_NUMBER, makeSafeString(lineItem.getMICREncipheredData().getEncryptedNumber()));
        sql.addColumn(FIELD_TENDER_MASKED_CHECK_MICR_NUMBER, makeSafeString(formatMaskedMICRNumber(lineItem.getABANumber(), lineItem.getAccountNumberEncipheredData())));
        sql.addColumn(FIELD_TENDER_CHECK_MICR_COUNTRY_CODE, Integer.toString(lineItem.getMICRCountryCode()));
        sql.addColumn(FIELD_TENDER_CHECK_STATE_CODE, makeSafeString(lineItem.getStateCode()));
        sql.addColumn(FIELD_TENDER_CHECK_ID_SWIPED, makeStringFromBoolean(lineItem.isIDSwiped()));
        if (lineItem.getIDTrack2Data() != null)
        {
            sql.addColumn(FIELD_TENDER_CHECK_ID_TRACK2, makeSafeString(new String(lineItem.getIDTrack2Data())));
            sql.addColumn(FIELD_TENDER_CHECK_ID_TRACK1, makeSafeString(new String(lineItem.getIDTrack1Data())));
        }

        sql.addColumn(FIELD_CONVERSION_FLAG, makeSafeString(lineItem.getConversionFlag()));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertCheckTenderLineItem", e);
        }
    }

    /**
     * Inserts into the tender gift certificate line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertGiftCertificateTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderGiftCertificateIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        insertTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        // if this represents a refund or change insert into DO_CF_GF.
        if(transaction.getTransactionType() != TransactionIfc.TYPE_VOID && lineItem.getAmountTender().signum() == -1)
        {
            insertGiftCertificate(dataConnection, transaction, lineItem);
        }
        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_GIFT_CERTIFICATE_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER,
            getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));

        sql.addColumn(FIELD_GIFT_CERTIFICATE_SERIAL_NUMBER, getGiftCertificateSerialNumber(lineItem));
        sql.addColumn(FIELD_ISSUING_STORE_NUMBER, makeSafeString(lineItem.getStoreNumber()));
        sql.addColumn(FIELD_GIFT_CERTIFICATE_FACE_VALUE_AMOUNT, lineItem.getAmountTender().toString());
        if (((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered() != null)
        {
            sql.addColumn(
                FIELD_GIFT_CERTIFICATE_FOREIGN_FACE_VALUE_AMOUNT,
                ((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered().toString());
            sql.addColumn(FIELD_CURRENCY_ID, ((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered().getType().getCurrencyId());
        }
        else
        {
            sql.addColumn(FIELD_CURRENCY_ID, lineItem.getAmountTender().getType().getCurrencyId());
        }

        if (lineItem.getCertificateType() != null)
        {
            sql.addColumn(FIELD_GIFT_CERTIFICATE_SUBTENDER_TYPE,
                          makeSafeString(lineItem.getCertificateType()));
        }

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertGiftCertificateTenderLineItem", e);
        }
    }

    /**
     * Inserts the necessary information for Gift Certificate into the DO_CF_GF table.
     *
     * @param dataConnection
     * @param transaction
     * @param lineItem
     * @exception DataException
     */
    protected void insertGiftCertificate(
            JdbcTemplate dataConnection,
            TenderableTransactionIfc transaction,
            TenderGiftCertificateIfc lineItem)
    throws DataException
    {
        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_GIFT_CERTIFICATE);
        String boolValue = makeStringFromBoolean(lineItem.isTrainingMode());

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, String.valueOf(lineItem.getLineNumber()));
        sql.addColumn(FIELD_GIFT_CERTIFICATE_SERIAL_NUMBER, inQuotes(lineItem.getGiftCertificateNumber()));
        sql.addColumn(FIELD_GIFT_CERTIFICATE_FACE_VALUE_AMOUNT, lineItem.getAmountTender().negate().getStringValue());
        sql.addColumn(FIELD_TRAINING_MODE, boolValue);
        sql.addColumn(FIELD_CURRENCY_ID, lineItem.getCurrencyID());

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error("" + de + "");
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertGiftCertificate", e);
        }
    }

    /**
     * Inserts into the send check tender line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertSendCheckTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderMailBankCheckIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        insertTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_SEND_CHECK_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));

        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_ADDRESS_LINE_1, getSendCheckPayableAddressLine1(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_ADDRESS_LINE_2, getSendCheckPayableAddressLine2(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_CITY, getSendCheckCity(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_POSTAL_CODE, getSendCheckPostalCode(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_STATE, getSendCheckState(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_REASON_CODE, makeSafeString(lineItem.getPersonalIDType().getCode()));

        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_NAME_PREFIX, getSendCheckNamePrefix(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_FIRST_NAME, getSendCheckFirstName(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_MIDDLE_NAME, getSendCheckMiddleName(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_LAST_NAME, getSendCheckLastName(lineItem));
        sql.addColumn(FIELD_SEND_CHECK_PAYABLE_TO_NAME_SUFFIX, getSendCheckNameSuffix(lineItem));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertSendCheckTenderLineItem", e);
        }
    }

    /**
     * Inserts into the travelers check tender line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertTravelersCheckTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderTravelersCheckIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        insertTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_TRAVELERS_CHECK_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));

        sql.addColumn(FIELD_TRAVELERS_CHECK_COUNT, lineItem.getNumberChecks());

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertTravelersCheckTenderLineItem", e);
        }
    }

    /**
     * Inserts into the travelers check tender line item table.
     * @param connection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertMoneyOrderTenderLineItem(
        JdbcTemplate connection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderMoneyOrderIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        insertTenderLineItem(connection, transaction, lineItemSequenceNumber, lineItem);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_MONEY_ORDER_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));

        sql.addColumn(FIELD_TENDER_LINE_ITEM_AMOUNT, getTenderAmount(lineItem));

        try
        {
            connection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertMoneyOrderTenderLineItem", e);
        }
    }

    /**
     * Inserts into the tender gift card line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertGiftCardTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderGiftCardIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        insertTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_GIFT_CARD_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));
        sql.addColumn(FIELD_GIFT_CARD_SERIAL_NUMBER, getGiftCardSerialNumber(lineItem));
        sql.addColumn(FIELD_MASKED_GIFT_CARD_SERIAL_NUMBER, getMaskedGiftCardSerialNumber(lineItem));
        sql.addColumn(FIELD_GIFT_CARD_ADJUDICATION_CODE, getGiftCardAuthorizationCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_CARD_NUMBER_SWIPED_OR_KEYED_CODE, getEntryMethod(lineItem));
        sql.addColumn(FIELD_AUTHORIZATION_METHOD_CODE, getGiftCardAuthorizationMethod(lineItem));
        sql.addColumn(FIELD_GIFT_CARD_INITIAL_BALANCE, getGiftCardInitialBalance(lineItem));
        sql.addColumn(FIELD_GIFT_CARD_CURRENT_BALANCE, getGiftCardRemainingBalance(lineItem));
        sql.addColumn(FIELD_GIFT_CARD_CREDIT_FLAG, makeStringFromBoolean(lineItem.isGiftCardCredit()));
        sql.addColumn(FIELD_GIFT_CARD_REQUEST_TYPE, inQuotes(lineItem.getRequestCode()));
        sql.addColumn(FIELD_CURRENCY_ID, lineItem.getAmountTender().getType().getCurrencyId());
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_SETTLEMENT_DATA, getAuthorizationSettlementData(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_JOURNAL_KEY, getAuthorizationJournalKey(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_MESSAGE_SEQUENCE_NUMBER, getReferenceCode(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_DATE, getAuthorizationDate(lineItem));
        sql.addColumn(FIELD_TENDER_AUTHORIZATION_TIME, getAuthorizationTime(lineItem));
        sql.addColumn(FIELD_GIFT_CARD_ACCOUNT_TYPE, getAccountType(lineItem)); 
                
        String authDateTime = getAuthorizationDateTime(lineItem);
        if (
        	(authDateTime == null) ||
        	(authDateTime.equals("")) ||
        	(authDateTime.equals("null"))
           )
        {
        	sql.addColumn(FIELD_TENDER_AUTHORIZATION_DATE_TIME, getSQLCurrentTimestampFunction());
        }
        else
        {
        	sql.addColumn(FIELD_TENDER_AUTHORIZATION_DATE_TIME, authDateTime);
        }

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertGiftCardTenderLineItem", e);
        }
    }

    /**
     * Inserts into the coupon tender line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertCouponTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderCouponIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        insertTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_COUPON_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));

        sql.addColumn(FIELD_COUPON_SCAN_CODE, getCouponNumber(lineItem));
        sql.addColumn(FIELD_COUPON_TYPE, getCouponType(lineItem));
        sql.addColumn(FIELD_COUPON_KEY_ENTERED_FLAG, getCouponEntryMethod(lineItem));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertCouponTenderLineItem", e);
        }
    }

    /**
     * Inserts into the purchase order tender line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertPurchaseOrderTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderPurchaseOrderIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        insertTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_PURCHASE_ORDER_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER, getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));
        sql.addColumn(PURCHASE_ORDER_NUMBER, getPurchaseOrderNumber(lineItem));
        sql.addColumn(PURCHASE_ORDER_AMOUNT, getPurchaseOrderAmount(lineItem));
        sql.addColumn(PURCHASE_ORDER_AGENCY_NAME, getPurchaseOrderAgencyName(lineItem));

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertPurchaseOrderTenderLineItem", e);
        }
    }

    /**
     * Inserts into the store credit tender line item table.
     * @param dataConnection
     *            connection to the db
     * @param transaction
     *            retail transaction
     * @param lineItemSequenceNumber
     *            sequence number associated with this line item
     * @param lineItem
     *            the tender line item
     * @exception DataException
     *                error saving data to the db
     */
    public void insertStoreCreditTenderLineItem(
        JdbcTemplate dataConnection,
        TenderableTransactionIfc transaction,
        int lineItemSequenceNumber,
        TenderStoreCreditIfc lineItem)
        throws DataException
    {
        /*
         * Update the Tender Line Item table first.
         */
        insertTenderLineItem(dataConnection, transaction, lineItemSequenceNumber, lineItem);

        SQLInsertStatement sql = new SQLInsertStatement();

        // Table
        sql.setTable(TABLE_STORE_CREDIT_TENDER_LINE_ITEM);

        // Fields
        sql.addColumn(FIELD_RETAIL_STORE_ID, getStoreID(transaction));
        sql.addColumn(FIELD_WORKSTATION_ID, getWorkstationID(transaction));
        sql.addColumn(FIELD_TRANSACTION_SEQUENCE_NUMBER, getTransactionSequenceNumber(transaction));
        sql.addColumn(
            FIELD_RETAIL_TRANSACTION_LINE_ITEM_SEQUENCE_NUMBER,
            getSequenceNumber(lineItemSequenceNumber));
        sql.addColumn(FIELD_BUSINESS_DAY_DATE, getBusinessDayString(transaction));

        sql.addColumn(FIELD_STORE_CREDIT_ID, getStoreCreditNumber(lineItem));
        sql.addColumn(FIELD_STORE_CREDIT_BALANCE, getStoreCreditAmount(lineItem));
        sql.addColumn(FIELD_STORE_CREDIT_TENDER_STATE, getStoreCreditState(lineItem));
        sql.addColumn(FIELD_STORE_CREDIT_FIRST_NAME, getStoreCreditFirstName(lineItem));
        sql.addColumn(FIELD_STORE_CREDIT_LAST_NAME, getStoreCreditLastName(lineItem));
        sql.addColumn(FIELD_STORE_CREDIT_ID_TYPE, getStoreCreditIdType(lineItem));
        if (((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered() != null)
        {
            sql.addColumn(
                    FIELD_STORE_CREDIT_FOREIGN_FACE_VALUE_AMOUNT,
                    ((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered().toString());
            //+I18N
            sql.addColumn(FIELD_CURRENCY_ID, ((TenderAlternateCurrencyIfc)lineItem).getAlternateCurrencyTendered().getType().getCurrencyId());
            //-I18N
        }
        else
        {
            // NOT NULL field designation
            sql.addColumn(
                    FIELD_STORE_CREDIT_FOREIGN_FACE_VALUE_AMOUNT,
                    "0.00");
            //+I18N
            sql.addColumn(FIELD_CURRENCY_ID, lineItem.getAmount().getType().getCurrencyId());
            //-I18N
        }

        try
        {
            dataConnection.execute(sql.getSQLString());
        }
        catch (DataException de)
        {
            logger.error( de.toString());
            throw de;
        }
        catch (Exception e)
        {
            throw new DataException(DataException.UNKNOWN, "insertStoreCreditTenderLineItem", e);
        }
    }

    /**
     * Returns the type of tender line item.
     * @param lineItem
     *            the tender line item
     * @return the type of tender line item.
     */
    public String getTenderType(TenderLineItemIfc lineItem)
    {
        return (makeSafeString(lineItem.getTypeCodeString()));
    }

    /**
     * Returns the amount of the tender line item
     * @param lineItem
     *            the tender line item
     * @return the amount of the tender line item
     */
    public String getTenderAmount(TenderLineItemIfc lineItem)
    {
        return (lineItem.getAmountTender().toString());
    }

    /**
     * Returns the amount of change returned
     * @param transaction
     *            the retail transaction
     * @return the amount of change returned
     */
    public String getTenderChangeAmount(TenderableTransactionIfc transaction)
    {
        return (transaction.calculateChangeGiven().abs().toString());
    }

    /**
     * Returns the amount of change returned
     * @param transaction
     *            the retail transaction
     * @return the amount of change returned
     */
    public CurrencyIfc getTenderChangeRoundedAmount(TenderableTransactionIfc transaction)
    {
        return transaction.getTenderTransactionTotals().getCashChangeRoundingAdjustment();
    }    
    
    /**
     * Returns the type of change returned
     * @param transaction
     *            the retail transaction
     * @return the type of change returned
     */
    public String getTenderChangeType(TenderableTransactionIfc transaction)
    {
        // for now, always return cash
        return (
            "'"
                + DomainGateway.getFactory().getTenderTypeMapInstance().getCode(TenderLineItemIfc.TENDER_TYPE_CASH)
                + "'");
    }

    /**
     * Returns the method of entry
     * @param lineItem
     *            the tender line item
     * @return the method of entry
     */
    public String getEntryMethod(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getEntryMethod().toString());
    }

    /**
     * Returns the method of entry
     * @param lineItem
     *            the tender line item
     * @return the method of entry
     */
    public String getEntryMethod(TenderCheckIfc lineItem)
    {
        String entryMethodString = "";
        EntryMethod entryMethod = lineItem.getEntryMethod();
        if (entryMethod != null)
        {
            entryMethodString = entryMethod.toString();
        }
        return (inQuotes(entryMethodString));
    }

    /**
     * Returns entry method for a gift card.
     *
     * @param line
     *            item tender line item
     * @return SQL-ready string containing entry method
     */
    protected String getEntryMethod(TenderGiftCardIfc lineItem)
    {
        EntryMethod entryMethod = lineItem.getEntryMethod();
        if (entryMethod == null)
        {
            if(lineItem.getGiftCard().getEntryMethod() != null)
            {
                entryMethod = lineItem.getGiftCard().getEntryMethod();
            }
        }
        return (inQuotes(entryMethod.toString()));
    }

    /**
     * Returns database safe string for the given EntryMethod.
     * @param entryMethod
     * @return database safe string for the given EntryMethod.
     */
    protected String getEntryMethod(EntryMethod entryMethod)
    {
        String returnValue = inQuotes("");
        if(entryMethod != null)
        {
            returnValue = inQuotes(entryMethod.toString());
        }
        return returnValue;
    }

    /**
     * Returns the type of card
     * @param lineItem
     *            the tender line item
     * @return the type of card
     */
    public String getCardType(TenderChargeIfc lineItem)
    {
        return (makeSafeString(lineItem.getCardType()));
    }

    public String getTenderDescription(TenderChargeIfc lineItem)
    {
    	return (makeSafeString(lineItem.getTypeDescriptorString()));
    }

    /**
     * Return a formatted expiration date for data persistence.
     *
     * @param lineItem the tender line item
     * @return the expiration date
     */
    public String getExpirationDate(TenderChargeIfc lineItem) throws ParseException
    {
        String dateString = lineItem.getExpirationDateString(); // short date string
        // Expiration date is parameter driven, so it may not be set
        if (dateString != null)
        {
            if (dateString.indexOf('/') == -1 && dateString.length() > 3)
            {
                dateString = dateString.substring(0, 2) + "/" + dateString.substring(2);
            }

            Date expDate = null;
            try
            {
                expDate = dateTimeService.parseDate(dateString,
                        LocaleMap.getLocale(LocaleMap.DEFAULT),
                        JdbcUtilities.CREDIT_CARD_EXPIRATION_DATE_FORMAT);
            }
            catch (ParseException e)
            {
                expDate = dateTimeService.parseDate(dateString,
                        LocaleMap.getLocale(LocaleMap.DEFAULT),
                        JdbcUtilities.CREDIT_CARD_EXPIRATION_YYMM_DATE_FORMAT);
            }
            dateString = dateToSQLDateFunction(expDate); // format
        }
        return dateString;
    }

    /**
     * Returns the data from track 2 of the magnetic stripe.
     * @param lineItem
     *            the tender line item
     * @return the track 2 data
     */
    public String getTrack2Data(TenderChargeIfc lineItem)
    {
        String value = null;
        byte[] data = lineItem.getTrack2Data();

        if (data != null)
        {
            value = makeSafeString(new String(data));
        }
        return (value);
    }

    /**
     * Returns the digitized signature data.
     * @param lineItem
     *            the tender line item
     * @return the signature data
     */
    public String getSignatureData(TenderChargeIfc lineItem)
    {
        Point[] data = (Point[]) lineItem.getSignatureData();
        StringBuffer value = new StringBuffer();

        if (data != null)
        {
            Point p = null;
            for (int i = 0; i < data.length; i++)
            {
                p = data[i];
                value.append("x" + Integer.toString(p.x) + "y" + Integer.toString(p.y));
            }
        }
        else
        {
            value.append("null");
        }
        return value.toString();
    }

    /**
     * Returns the authorization code
     * @param lineItem
     *            the tender line item
     * @return the authorization code
     */
    public String getAuthorizationCode(AuthorizableTenderIfc lineItem)
    {
        return (makeSafeString(lineItem.getAuthorizationCode()));
    }

    /**
     * Returns the authorization-method code
     *
     * @param lineItem
     *            the tender line item
     * @return the authorization code
     */
    public String getMethodCode(AuthorizableTenderIfc lineItem)
    {
        return makeSafeString(lineItem.getAuthorizationMethod());
    }

    /**
     * Returns the type of ID
     * @param lineItem
     *            the tender line item
     * @return the country of origin of ID
     */
    public String getIDCountry(TenderChargeIfc lineItem)
    {
        return (makeSafeString(lineItem.getIDCountry()));
    }

    /**
     * Returns the type of ID
     * @param lineItem
     *            the tender line item
     * @return the state of origin of ID
     */
    public String getIDState(TenderChargeIfc lineItem)
    {
        return (makeSafeString(lineItem.getIDState()));
    }

    /**
     * Returns the type of ID
     * @param lineItem
     *            the tender line item
     * @return the state of origin of ID
     */
    public String getIDExpirationDate(TenderChargeIfc lineItem)
    {
      String date = "null";
      if (lineItem.getIDExpirationDate() != null)
      {
          date = dateToSQLTimestampString(lineItem.getIDExpirationDate().dateValue());
      }

      return date;
      //return (dateToSQLTimestampString(lineItem.getIDExpirationDate())); -- Changed as apart of Forward port
    }

    /**
     * Returns the type of ID
     * @param lineItem
     *            the tender line item
     * @return the type of ID
     */
    public String getIDType(TenderCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getPersonalIDType().getCode()));
    }

    /**
     * Returns the ID Number
     * @param lineItem
     *            the tender line item
     * @return the ID Number
     * @deprecated as of 13.4. Use {@link #getEncryptedIDNumber(TenderCheckIfc)} instead.
     */
    public String getIDNumber(TenderCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getIDNumber()));
    }

    /**
     * Returns the encrypted ID Number
     * <p>
     *
     * @param lineItem the tender line item
     * @return the Encrypted ID Number
     */
    public String getEncryptedIDNumber(TenderCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getPersonalID().getEncryptedNumber()));
    }

    /**
     * Returns the masked ID Number
     * <p>
     *
     * @param lineItem the tender line item
     * @return the masked ID Number
     */
    public String getMaskedIDNumber(TenderCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getPersonalID().getMaskedNumber()));
    }

    /**
     * Returns the date of birth
     * @param lineItem
     *            the tender line item
     * @return the date of birth
     */
    public String getDateOfBirth(TenderCheckIfc lineItem)
    {
        String value = "null";
        EYSDate birthDate = lineItem.getDateOfBirth();
        if (birthDate != null)
        {
            value = dateToSQLDateString(birthDate.dateValue());
        }

        return (value);
    }

    /**
     * Returns the bank ID
     * @param lineItem
     *            the tender line item
     * @return the bank ID
     */
    public String getBankID(TenderCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getABANumber()));
    }

    /**
     * Returns the check account number
     * @param lineItem
     *            the tender line item
     * @return the check account number
     * @deprecated as of 13.4. Use {@link #getEncryptedCheckAccountNumber(TenderCheckIfc)} instead.
     */
    public String getCheckAccountNumber(TenderCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getAccountNumber()));
    }

    /**
     * Returns the encrypted check account number
     * <p>
     *
     * @param lineItem the tender line item
     * @return the encrypted check account number
     */
    public String getEncryptedCheckAccountNumber(TenderCheckIfc lineItem)
    {
        String returnValue = "";
        if (lineItem.getAccountNumberEncipheredData() != null)
        {
            returnValue = lineItem.getAccountNumberEncipheredData().getEncryptedNumber();
        }
        return makeSafeString(returnValue);
    }

    /**
     * Returns the Masked check account number
     * <p>
     *
     * @param lineItem the tender line item
     * @return the Masked check account number
     */
    public String getMaskedCheckAccountNumber(TenderCheckIfc lineItem)
    {
        String returnValue = "";
        if (lineItem.getAccountNumberEncipheredData() != null)
        {
            returnValue = lineItem.getAccountNumberEncipheredData().getMaskedNumber();
        }
        return makeSafeString(returnValue);
    }

    /**
     * Returns the check sequence number
     * @param lineItem
     *            the tender line item
     * @return the check sequence number
     */
    public String getCheckSequenceNumber(TenderCheckIfc lineItem)
    {
        String returnString;

        if (lineItem.getCheckNumber() == null || lineItem.getCheckNumber().length() == 0)
        {
            returnString = "0";
        }
        else
        {
            returnString = new String(lineItem.getCheckNumber());
        }
        return makeSafeString(returnString);
    }

    /**
     * Returns the gift certificate number
     * @param lineItem
     *            the tender line item
     * @return the gift certificate number
     */
    public String getGiftCertificateSerialNumber(TenderGiftCertificateIfc lineItem)
    {
        return (makeSafeString(lineItem.getGiftCertificateNumber()));
    }

    /**
     * Returns customer's title.
     * @param lineItem
     *            the tender line Item
     * @return properly quoted title
     */
    public String getSendCheckNamePrefix(TenderMailBankCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getPayeeName().getSalutation()));
    }

    /**
     * Returns customer's first name.
     * @param lineItem
     *            the tender line Item
     * @return properly quoted first name
     */
    public String getSendCheckFirstName(TenderMailBankCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getPayeeName().getFirstName()));
    }

    /**
     * Returns customer's middle name.
     * @param lineItem
     *            the tender line Item
     * @return properly quoted middle name
     */
    public String getSendCheckMiddleName(TenderMailBankCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getPayeeName().getMiddleName()));
    }

    /**
     * Returns customer's last name.
     * @param lineItem
     *            the tender line Item
     * @return properly quoted last name
     */
    public String getSendCheckLastName(TenderMailBankCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getPayeeName().getLastName()));
    }

    /**
     * Returns customer's suffix.
     * @param lineItem
     *            the tender line Item
     * @return properly quoted suffix
     */
    public String getSendCheckNameSuffix(TenderMailBankCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getPayeeName().getNameSuffix()));
    }

    /**
     * Returns Check refund reason.
     * @param lineItem
     *            the tender line Item
     * @return properly quoted ID type
     */
    public String getSendCheckIDType(TenderMailBankCheckIfc lineItem)
    {
        return (makeSafeString(lineItem.getPersonalIDType().getCode()));
    }

    /**
     * Returns customer's address.
     * @param lineItem
     *            the tender line Item
     * @return address
     */
    public AddressIfc getSendCheckAddress(TenderMailBankCheckIfc lineItem)
    {
        AddressIfc address = lineItem.getPrimaryAddress();

        if (address == null)
        {
            /*
             * Make sure it doesn't return null since it makes it easier for
             * routines using this one.
             */
            address = DomainGateway.getFactory().getAddressInstance();
        }

        return (address);
    }

    /**
     * Returns customer's payable address.
     * @param lineItem
     *            the tender line Item
     * @return properly quoted payable address
     */
    public String getSendCheckPayableAddressLine1(TenderMailBankCheckIfc lineItem)
    {
        String returnValue = "''";
        AddressIfc address = getSendCheckAddress(lineItem);

        List<String> lines = address.getLines();
        if (lines.size() > 0 && (lines.get(0) != null))
        {
            returnValue = makeSafeString(lines.get(0));
        }

        return returnValue;
    }
    /**
     * Returns customer's payable address.
     * @param lineItem
     *            the tender line Item
     * @return properly quoted payable address
     */
    public String getSendCheckPayableAddressLine2(TenderMailBankCheckIfc lineItem)
    {
        String returnValue = "''";
        AddressIfc address = getSendCheckAddress(lineItem);

        List<String> lines = address.getLines();
        if (lines.size() > 1 && (lines.get(1) != null))
        {
            returnValue = makeSafeString(lines.get(1));
        }

        return returnValue;
    }

    /**
     * Returns customer's city
     * @param lineItem
     *            the tender line Item
     * @return properly quoted payable city
     */
    public String getSendCheckCity(TenderMailBankCheckIfc lineItem)
    {
        return (makeSafeString(getSendCheckAddress(lineItem).getCity()));
    }

    /**
     * Returns customer's state
     * @param lineItem
     *            the tender line Item
     * @return properly quoted payable state
     */
    public String getSendCheckState(TenderMailBankCheckIfc lineItem)
    {
        return (makeSafeString(getSendCheckAddress(lineItem).getState()));
    }

    /**
     * Returns customer's postal code
     * @param lineItem
     *            the tender line Item
     * @return properly quoted payable postal code
     */
    public String getSendCheckPostalCode(TenderMailBankCheckIfc lineItem)
    {
        return (makeSafeString(getSendCheckAddress(lineItem).getPostalCode()));
    }

    /**
     * Returns the number of travelers checks
     * @param lineItem
     *            the tender line Item
     * @return the number of travelers checks
     */
    public String getCheckCount(TenderTravelersCheckIfc lineItem)
    {
        return ("'" + lineItem.getNumberChecks() + "'");
    }

    /**
     * Returns the gift card number
     * @param lineItem
     *            the tender line item
     * @return the gift card number
     */
    public String getGiftCardSerialNumber(TenderGiftCardIfc lineItem)
    {
        String returnValue = "";
        if(lineItem.getEncipheredCardData() != null)
        {
            returnValue = lineItem.getEncipheredCardData().getEncryptedAcctNumber();
        }
        return makeSafeString(returnValue);
    }

    /**
     * Returns the masked gift card number
     * @param lineItem
     *            the tender line item
     * @return the masked gift card number
     */
    public String getMaskedGiftCardSerialNumber(TenderGiftCardIfc lineItem)
    {
        String returnValue = "";
        if(lineItem.getEncipheredCardData() != null)
        {
            returnValue = lineItem.getEncipheredCardData().getMaskedAcctNumber();
        }
        return makeSafeString(returnValue);
    }

    /**
     * Returns authorization code. In some cases, this will be the code from an
     * activation.
     * @param lineItem
     *            tender gift card line item
     * @return SQL-ready authorization code string
     */
    protected String getGiftCardAuthorizationCode(TenderGiftCardIfc lineItem)
    {
        String authCode = lineItem.getAuthorizationCode();
        if (Util.isEmpty(authCode))
        {
            if (lineItem.getGiftCard() != null)
            {
                authCode = lineItem.getGiftCard().getApprovalCode();
            }
        }
        if (Util.isEmpty(authCode))
        {
            authCode = "";
        }
        return (inQuotes(authCode));
    }

    /**
     * Returns authorization code. In some cases, this will be the code from an
     * activation.
     * @param lineItem
     *            tender gift card line item
     * @return SQL-ready authorization code string
     */
    protected String getGiftCardAuthorizationMethod(TenderGiftCardIfc lineItem)
    {
        String authMethod = lineItem.getAuthorizationMethod();
        if ( authMethod == null )
        {
            authMethod = "";
        }
        return inQuotes(authMethod);
    }

    /**
     * Returns initial balance.
     * @param lineItem
     *            tender gift card line item
     * @return initial balance string
     */
    protected String getGiftCardInitialBalance(TenderGiftCardIfc lineItem)
    {
        return "0.00";
    }

    /**
     * Returns remaining balance.
     * @param lineItem
     *            tender gift card line item
     * @return remaining balance string
     */
    protected String getGiftCardRemainingBalance(TenderGiftCardIfc lineItem)
    {
        String remainingBalance = "0.00";
        if ( lineItem.getGiftCard() != null )
        {
            CurrencyIfc balance = lineItem.getGiftCard().getCurrentBalance();
            if(balance != null)
            {
                remainingBalance = balance.getStringValue();
            }
            else
            {
                logger.info("Gift Card remaining balance is null.");
            }
        }
        return remainingBalance;
    }

    /**
     * Returns coupon number
     * @param lineItem
     *            the tender line Item
     * @return the coupon id number
     */
    public String getCouponNumber(TenderCouponIfc lineItem)
    {
        return (makeSafeString(lineItem.getCouponNumber()));
    }

    /**
     * Returns sql-ready boolean value for key entered flag.
     *
     * @param lineItem
     *            coupon tender line item @returns sql-ready boolean value for
     *            key entered flag
     */
    protected String getCouponEntryMethod(TenderCouponIfc lineItem)
    {
        boolean keyEntry = false;
        if (lineItem != null && EntryMethod.Manual.equals(lineItem.getEntryMethod()))
        {
            keyEntry = true;
        }
        return (makeStringFromBoolean(keyEntry));
    }

    /**
     * Returns coupon type
     * @param lineItem
     *            the tender line Item
     * @return the coupon type
     */
    public String getCouponType(TenderCouponIfc lineItem)
    {
        return (makeSafeString(TenderCouponIfc.COUPON_TYPE_CODE[lineItem.getCouponType()]));
    }

    /**
     * Returns store credit number
     * @param lineItem
     *            the tender line Item
     * @return the store credit id number
     */
    public String getStoreCreditNumber(TenderStoreCreditIfc lineItem)
    {
        return (makeSafeString(lineItem.getStoreCreditID()));
    }

    /**
     * Returns store credit Amount
     * @param lineItem
     *            the tender line Item
     * @return the store credit amount
     */
    public String getStoreCreditAmount(TenderStoreCreditIfc lineItem)
    {
        return (lineItem.getAmount().toString());
    }

    /**
     * Returns store credit State
     * @param lineItem
     *            the tender line Item
     * @return the store credit amount
     */
    public String getStoreCreditState(TenderStoreCreditIfc lineItem)
    {
        return (makeSafeString(lineItem.getState()));
    }

    /**
     * Returns store credit First Name
     * @param lineItem
     *            the tender line Item
     * @return the store credit first name
     */
    public String getStoreCreditFirstName(TenderStoreCreditIfc lineItem)
    {
        return (makeSafeString(lineItem.getFirstName()));
    }

    /**
     * Returns store credit Last Name
     * @param lineItem
     *            the tender line Item
     * @return the store credit last name
     */
    public String getStoreCreditLastName(TenderStoreCreditIfc lineItem)
    {
        return (makeSafeString(lineItem.getLastName()));
    }

    /**
     * Returns store credit Id Type
     * @param lineItem
     *            the tender line Item
     * @return the store credit id type
     */
    public String getStoreCreditIdType(TenderStoreCreditIfc lineItem)
    {
        return (makeSafeString(lineItem.getPersonalIDType().getCode()));
    }

    /**
     * Returns Purchase Order Number
     * @param lineItem
     *            the tender line Item
     * @return the purchase order number
     */
    public String getPurchaseOrderNumber(TenderPurchaseOrderIfc lineItem)
    {
        return (makeSafeString(lineItem.getPurchaseOrderNumber()));
    }

    /**
     * Returns Purchase Order Amount
     * @param lineItem
     *            the tender line Item
     * @return the purchase order tender amount
     */
    public String getPurchaseOrderAmount(TenderPurchaseOrderIfc lineItem)
    {
        return (lineItem.getFaceValueAmount().toString());
    }

    /**
     * Returns Purchase Order Agency Name
     * @param lineItem
     *            the tender line Item
     * @return the Agency Name
     */
    public String getPurchaseOrderAgencyName(TenderPurchaseOrderIfc lineItem)
    {
        return (makeSafeString(lineItem.getAgencyName()));
    }

    /**
     * Returns local/base currency description
     * @param currencyifc
     *            local/base currency
     * @return local/base currency description
     */
    public String getLocalCurrencyDescription()
    {
        return ("'" + DomainGateway.getBaseCurrencyInstance().getDescription() + "'");
    }
    /**
     * Returns alternate currency description
     * @param currencyifc
     *            alternate currency
     * @return alternate currency description
     */
    public String getAlternateCurrencyDescription(CurrencyIfc lineItemiAlt)
    {
        return ("'" + lineItemiAlt.getDescription() + "'");
    }
    /**
     * Returns the alternate currency exchange rate
     * @param currencyifc
     *            alternate currency
     * @return exchange rate
     */
    public String getExchangeRate(CurrencyIfc lineItemiAlt)
    {
        return ("'" + lineItemiAlt.getBaseConversionRate() + "'");
    }
    /**
     * Returns the alternate currency exchange rate
     * @param currencyifc
     *            alternate currency
     * @return exchange rate
     */
    public String getCountryCode(CurrencyIfc lineItemiAlt)
    {
        return ("'" + lineItemiAlt.getCountryCode() + "'");
    }
    /**
     * Returns the alternate currency exchange rate
     * @param currencyifc
     *            alternate currency
     * @return exchange rate
     */
    public String getAlternateAmountTendered(CurrencyIfc lineItemiAlt)
    {
        return ("'" + lineItemiAlt.getStringValue() + "'");
    }

    /**
     * Get a safe string for Authorization Settlement Data
     * @param lineItem
     * @return
     */
    public String getAuthorizationSettlementData(TenderChargeIfc lineItem)
    {
        return (makeSafeString(lineItem.getSettlementData()));
    }

    /**
     * Get a safe string for Authorization Journal Key
     * @param lineItem
     * @return
     */
    public String getAuthorizationJournalKey(TenderChargeIfc lineItem)
    {
        return (makeSafeString(lineItem.getJournalKey()));
    }

    /**
     * Get a safe string for Authorization Date
     * @param lineItem
     * @return
     */
    public String getAuthorizationDateTime(TenderChargeIfc lineItem)
    {
        String date = "null";
        if (lineItem.getAuthorizedDateTime() != null)
        {
            date = dateToSQLTimestampString(lineItem.getAuthorizedDateTime().dateValue());
        }

        return date;
    }

    /**
     * Get a safe string for Reference Code
     * @param lineItem
     * @return String
     */
    public String getReferenceCode(AuthorizableTenderIfc lineItem)
    {
        return (makeSafeString(lineItem.getReferenceCode()));
    }

    /**
     * Get a safe string for Authorization Date
     * @param lineItem
     * @return String
     */
    public String getAuthorizationDate(AuthorizableTenderIfc lineItem)
    {
        return makeSafeString(lineItem.getAuthorizationDate());
    }

    /**
     * Get a safe string for Authorization Time
     * @param lineItem
     * @return String
     */
    public String getAuthorizationTime(AuthorizableTenderIfc lineItem)
    {
        return makeSafeString(lineItem.getAuthorizationTime());
    }

    /**
     * Gets the formatted account type from a tender gift card object.
     * @param lineItem
     * @return String
     */
    public String getAccountType(TenderGiftCardIfc lineItem)
    {
        if (lineItem.getAccountType() != null)
        {
            return inQuotes(lineItem.getAccountType());
        }
        else
        {
            return "null";
        }
    }

    /**
     * Get a safe string for Retrieval Reference Number
     * @param lineItem
     * @return String
     */
    public String getRetrievalReferenceNumber(AuthorizableTenderIfc lineItem)
    {
        return (makeSafeString(lineItem.getRetrievalReferenceNumber()));
    }

    /**
     * Get a safe string for Auth Response Code
     * @param lineItem
     * @return String
     */
    public String getAuthResponseCode(AuthorizableTenderIfc lineItem)
    {
        return (makeSafeString(lineItem.getAuthResponseCode()));
    }

    /**
     * Returns the safe string trace number.
     * @param lineItem
     * @return String
     */
    protected String getTraceNumber(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getTraceNumber());
    }

    /**
     * Returns the ksn number.
     * @param lineItem
     * @return
     */
    protected String getKSN_20(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getAdditionalSecurityInfo());
    }

    /**
     * Returns the safe string host reference.
     * @param lineItem
     * @return String
     */
    protected String getHostReference(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getHostReference());
    }

    /**
     * Returns the safe string authorization source code.
     * @param lineItem
     * @return String
     */
    protected String getAuthorizationSource(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getAuthorizationSource());
    }

    /**
     * Returns the safe string validation code.
     * @param lineItem
     * @return String
     */
    protected String getValidationCode(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getValidationCode());
    }

    /**
     * Returns the safe string transaction identification number.
     * @param lineItem
     * @return String
     */
    protected String getTransactionIdentificationNumber(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getTransactionIdentificationNumber());
    }

    /**
     * Returns the safe string payment service indicator.
     * @param lineItem
     * @return String
     */
    protected String getPaymentServiceIndicator(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getPaymentServiceIndicator());
    }

    /**
     * Returns the safe string account data source
     * @param lineItem
     * @return String
     */
    protected String getAccountDataSource(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getAccountDataSource());
    }

    /**
     * Returns the remaining balance for prepaid credit cards.
     * @param lineItem
     * @return
     */
    protected String getPrepaidRemainingBalanceAmount(TenderChargeIfc lineItem)
    {
        String returnValue = null;
        if(lineItem.getPrepaidRemainingBalance() != null)
        {
            returnValue = lineItem.getPrepaidRemainingBalance().toString();
        }
        return returnValue;
    }

    /**
     * Returns the promotion duration.
     * @param lineItem
     * @return The promotion duration.
     */
    protected String getPromotionDuration(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getPromotionDuration());
    }

    /**
     * Returns the promotion description.
     * @param lineItem
     * @return The promotion description.
     */
    protected String getPromotionDescription(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getPromotionDescription());
    }

    /**
     * Returns the promotion APR Type.
     * @param lineItem
     * @return The promotion APR Type.
     */
    protected String getPromotionAPRType(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getPromotionAPRType());
    }

    /**
     * Returns the promotion APR.
     * @param lineItem
     * @return The promotion APR.
     */
    protected String getPromotionAPR(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getPromotionAPR());
    }

    /**
     * Returns the account APR type.
     * @param lineItem
     * @return The account APR type.
     */
    protected String getAccountAPRType(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getAccountAPRType());
    }

    /**
     * Returns the account APR.
     * @param lineItem
     * @return The account APR.
     */
    protected String getAccountAPR(TenderChargeIfc lineItem)
    {
        return makeSafeString(lineItem.getAccountAPR());
    }

    /**
     * Returns the masked MICR number constructed from the
     * supplied Bank Routing and Bank Account Numbers.  The returned
     * text is a useful alternative to the actual MICR number when
     * used as search criteria.
     * <p>
     * This method provides a way to create masked MICR number with
     * a consistent format regardless of whether the check bank numbers
     * were entered by keypad or micr-device.
     * <p>
     * MICR numbers collected by MICR-device will often
     * have control characters, and may contain a trailing check number
     * (i.e. t111900659t0274812345a1085).  This raw format complicates search
     * since logic about the format is required.
     * <p>
     * Text constructed solely on the masked concatenation of the bank routing
     * and bank account number (i.e. 111900659 + 0274812345 = 111900659******2345)
     * provides a consistent MICR format upon which to search without the knowledge
     * of the format generated by the micr-device used to read the check.
     * <p>
     * @param abaNumberEncipheredData the check's Bank Routing Number
     * @param accountNumberEncipheredData check's the Bank Account Number
     * @return the Masked MICR number in standardized format
     * @see DomainUtil#getNumberOfMicrFirstDigits()
     * @see DomainUtil#getNumberOfMicrLastDigits()
     */
    protected String formatMaskedMICRNumber(String abaNumberString,
                                            EncipheredDataIfc accountNumberEncipheredData)
    {
        byte[] abaNumber = new byte[0];
        byte[] accountNumber = new byte[0];
        byte[] micrNumber = null;
        String maskedMicrNumber = null;
        try
        {
            if (abaNumberString != null)
            {
                abaNumber = abaNumberString.getBytes();
            }
            if (accountNumberEncipheredData != null)
            {
                accountNumber = accountNumberEncipheredData.getDecryptedNumber().getBytes();
            }

            //concatenate numbers
            micrNumber = new byte[abaNumber.length + accountNumber.length];
            System.arraycopy(abaNumber, 0, micrNumber, 0, abaNumber.length);
            System.arraycopy(accountNumber, 0, micrNumber, abaNumber.length, accountNumber.length);

            //get the number of unmasked leading/trailing digits for a MICR Number (configured in domain.properties)
            int unMaskedfirstDigits = DomainUtil.getNumberOfMicrFirstDigits();
            int unMaskedLastDigits = DomainUtil.getNumberOfMicrLastDigits();
            byte maskChar = (byte)DomainUtil.getMaskChar();

            //apply mask
            for (int i = unMaskedfirstDigits; i < micrNumber.length; i++)
            {
                if (i < (micrNumber.length - unMaskedLastDigits))
                {
                    micrNumber[i]= maskChar;
                }
            }
            maskedMicrNumber = new String(micrNumber);
        }
        finally
        {
            //flush clear-text numbers from memory
            Util.flushByteArray(abaNumber);
            Util.flushByteArray(accountNumber);
            Util.flushByteArray(micrNumber);
        }
        return maskedMicrNumber;
    }
}
