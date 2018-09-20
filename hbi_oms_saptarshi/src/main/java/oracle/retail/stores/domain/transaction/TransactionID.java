/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
/* ===========================================================================
* Copyright (c) 1998, 2013, Oracle and/or its affiliates. All rights reserved. 
 * ===========================================================================
 * $Header: rgbustores/modules/domain/src/oracle/retail/stores/domain/transaction/TransactionID.java /rgbustores_13.4x_generic_branch/3 2013/05/02 09:50:58 lapattab Exp $
 * ===========================================================================
 * NOTES
 * <other useful comments, qualifications, etc.>
 *
 * MODIFIED    (MM/DD/YY)
 *    lapattab  04/29/13 - Fix for Bug#16688566 - Added code to prefix leading
 *                         zeros to the receipt number manually entered by the
 *                         operator
 *    cgreene   09/20/11 - removed deprecated methods and separated the three
 *                         formatters
 *    cgreene   05/26/10 - convert to oracle packaging
 *    abondala  01/03/10 - update header date
 *
 * ===========================================================================
 * $Log:
 *    5    360Commerce 1.4         4/12/2008 5:44:57 PM   Christian Greene
 *         Upgrade StringBuffer to StringBuilder
 *    4    360Commerce 1.3         1/25/2006 4:11:53 PM   Brett J. Larsen merge
 *          7.1.1 changes (aka. 7.0.3 fixes) into 360Commerce view
 *    3    360Commerce 1.2         3/31/2005 4:30:34 PM   Robert Pearse   
 *    2    360Commerce 1.1         3/10/2005 10:26:22 AM  Robert Pearse   
 *    1    360Commerce 1.0         2/11/2005 12:15:14 PM  Robert Pearse   
 *:
 *    4    .v700     1.2.1.0     11/10/2005 10:30:32    Rohit Sachdeva
 *         Optional date in the transaction ID should be taken up as business
 *         date when entered as per format specified
 *    3    360Commerce1.2         3/31/2005 15:30:34     Robert Pearse
 *    2    360Commerce1.1         3/10/2005 10:26:22     Robert Pearse
 *    1    360Commerce1.0         2/11/2005 12:15:14     Robert Pearse
 *
 *   Revision 1.17  2004/09/23 00:30:51  kmcbride
 *   @scr 7211: Inserting serialVersionUIDs in these Serializable classes
 *
 *   Revision 1.16  2004/07/16 17:01:35  mweis
 *   @scr 5564 Return Item Info panel needs to display full transaction ID (when business date part of trans ID).
 *
 *   Revision 1.15  2004/07/14 01:26:46  kmcbride
 *   @scr 3992: Adding transaction date to the transaction id object.  Also, fixed code in TransactionID that was eating an IllegalArgumentException without logging
 *
 *   Revision 1.14  2004/03/25 17:38:52  pkillick
 *   @scr 3999 -Added code to compensate for varied transaction formats.
 *
 *   Revision 1.13  2004/03/12 18:13:34  aarvesen
 *   @scr 0 allow null or zero length string for business date
 *
 *   Revision 1.12  2004/03/11 20:34:37  baa
 *   @scr 3561 add changes to handle transaction variable length id
 *
 *   Revision 1.11  2004/03/11 17:36:39  rzurga
 *   @scr 3991 Fix getting the business date as null when the feature is disabled
 *
 *   Revision 1.10  2004/03/05 23:52:02  rzurga
 *   @scr 3898 Protect setXxxx calls from null params
 *
 *   Revision 1.9  2004/03/05 17:38:22  cdb
 *   @scr 0 Corrected null pointer exception in
 *   TransactionID.setStoreID() and added unit test for the problem.
 *
 *   Revision 1.8  2004/03/05 10:08:32  rzurga
 *   @scr 3898 Fix constructor from string and the string value so as to make them consistent and more in line with how the things were before
 *
 *   Revision 1.7  2004/02/27 00:48:30  jdeleau
 *   @scr 0 The clone and equals methods were failing with NPE on
 *   voidTransaction objects created with the default constructor.
 *   This corrects those problems.
 *
 *   Revision 1.6  2004/02/26 23:33:44  rzurga
 *   @scr 3898 Make no date in barcode default
 *
 *   Revision 1.5  2004/02/26 16:47:08  rzurga
 *   @scr 0 Add optional and customizable date to the transaction id and its receipt barcode
 *
 *   Revision 1.4  2004/02/17 16:18:52  rhafernik
 *   @scr 0 log4j conversion
 *
 *   Revision 1.3  2004/02/12 17:14:42  mcs
 *   Forcing head revision
 *
 *   Revision 1.2  2004/02/11 23:28:50  bwf
 *   @scr 0 Organize imports.
 *
 *   Revision 1.1.1.1  2004/02/11 01:04:34  cschellenger
 *   updating to pvcs 360store-current
 *
 *
 *
 *    Rev 1.3   Feb 03 2004 17:17:42   cdb
 * Previous change to TransactionID lost checking for minimum length. Updated Transaction Unit Test to not extend DomainDispatcherTestCase.
 *
 *    Rev 1.2   Jan 13 2004 16:17:32   rzurga
 * - Add business date to transaction ID
 * - Make initialization static
 * - Make all getXxxxLength() functions public and static
 *
 *    Rev 1.0   Aug 29 2003 15:41:12   CSchellenger
 * Initial revision.
 *
 *    Rev 1.1   May 10 2003 16:27:18   mpm
 * Added support for post-processing-status code.
 *
 *    Rev 1.0   Jun 03 2002 17:06:42   msg
 * Initial revision.
 *
 * ===========================================================================
 */
package oracle.retail.stores.domain.transaction;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.retail.stores.domain.DomainGateway;
import oracle.retail.stores.domain.utility.AbstractRoutable;
import oracle.retail.stores.domain.utility.EYSDate;
import oracle.retail.stores.foundation.utility.Util;

/**
 * This class implements a tripartite transaction identifier key, composed of
 * the store number, workstation number and a sequence number.
 * <P>
 * The formatting specifications can be accessed through DomainGateway
 * properties. It is assumed the resulting transaction identifier string by
 * numerical and composed of a store identifier followed by a workstation
 * identifier and a sequence number.
 * 
 * @version $Revision: /rgbustores_13.4x_generic_branch/3 $
 */
public class TransactionID extends AbstractRoutable implements TransactionIDIfc
{
    // This id is used to tell the compiler not to generate a new serialVersionUID.
    static final long serialVersionUID = -5722186428365634283L;

    /** revision number supplied by source-code-control system */
    public static final String revisionNumber = "$Revision: /rgbustores_13.4x_generic_branch/3 $";
    /** debug logger */
    protected static final Logger logger = Logger.getLogger(TransactionID.class);
    /**
     * default store identifier length
     */
    protected static final int DEFAULT_STORE_ID_LENGTH = 5;
    /**
     * default workstation identifier length
     */
    protected static final int DEFAULT_WORKSTATION_ID_LENGTH = 3;
    /**
     * default sequence number length
     */
    protected static final int DEFAULT_SEQUENCE_NUMBER_LENGTH = 4;
    /**
     * default barcode date format
     */
    protected static final String DEFAULT_BARCODE_DATE_FORMAT = "";
    /**
     * store ID length
     */
    protected static int storeIDLength = DEFAULT_STORE_ID_LENGTH;
    /**
     * workstation ID length
     */
    protected static int workstationIDLength = DEFAULT_WORKSTATION_ID_LENGTH;
    /**
     * sequence number length
     */
    protected static int sequenceNumberLength = DEFAULT_SEQUENCE_NUMBER_LENGTH;
    /**
     * default barcode date length
     */
    protected static int barcodeDateLength = 0;
    /**
     * sequence barcode date format
     */
    protected static String barcodeDateFormat = DEFAULT_BARCODE_DATE_FORMAT;
    /**
     * store ID length property name
     */
    public static final String STORE_ID_LENGTH_PROPERTY_NAME = "TransactionIDStoreIDLength";
    /**
     * workstation ID length property name
     */
    public static final String WORKSTATION_ID_LENGTH_PROPERTY_NAME = "TransactionIDWorkstationIDLength";
    /**
     * seqeuence number length property name
     */
    public static final String SEQUENCE_NUMBER_LENGTH_PROPERTY_NAME = "TransactionIDSequenceNumberLength";
    /**
     * barcode date length property name
     */
    public static final String BARCODE_DATE_LENGTH_PROPERTY_NAME = "TransactionIDBarcodeDateLength";
    /**
     * barcode date length property name
     */
    public static final String BARCODE_DATE_FORMAT_PROPERTY_NAME = "TransactionIDBarcodeDateFormat";
    /**
     * barcode date length property name
     */
    public static final String BARCODE_DATE_FORMAT_SCREEN_PROPERTY_NAME = "TransactionIDBarcodeDateFormatScreen";
    /**
     * source of formatting pattern
     */
    protected static final String FORMAT_PATTERN_SOURCE = "0000000000000000000000000000000000000000";
    /**
     * initialized flag
     */
    protected static boolean paramsInitialized = false;
    /**
     * store identifier
     */
    protected String storeID = "";
    /**
     * workstation identifier
     */
    protected String workstationID = "";
    /**
     * transaction sequence number
     */
    protected long sequenceNumber = 0;
    /**
     * transaction sequence number
     */
    protected String businessDate = "";
    /**
     * decimal formatter
     * @deprecated as of 13.4. Use {@link #formatterStoreID} instead.
     */
    protected transient DecimalFormat formatter = null;
    protected static DecimalFormat formatterSequenceNumber;
    protected static DecimalFormat formatterStoreID = null;
    protected static DecimalFormat formatterWorkstationID = null;

    /**
     * Constructs TransactionID object. The string value of the transaction ID
     * is not assembled.
     * <P>
     */
    public TransactionID()
    {
        setFormattingSpecifications();
    }

    /**
     * Sets primary attributes and assembles transaction identifier string.
     * 
     * @param store store identifier
     * @param workstation workstation identifier
     * @param sequence transaction sequence number
     */
    public void setTransactionID(String store, String workstation, long sequence)
    {
        setStoreID(store);
        setWorkstationID(workstation);
        setSequenceNumber(sequence);
    }

    /**
     * Sets primary attributes and assembles transaction identifier string.
     * 
     * @param store store identifier
     * @param workstation workstation identifier
     * @param sequence transaction sequence number
     * @param date business date
     */
    public void setTransactionID(String store, String workstation, long sequence, String date)
    {
        setTransactionID(store, workstation, sequence);
        setBusinessDate(date);
    }

    /**
     * Sets formatting specifications based on properties gleaned from
     * DomainGateway.
     * <P>
     */
    protected static void setFormattingSpecifications()
    {
        if (!paramsInitialized)
        {
            String storeIDLengthProperty = DomainGateway.getProperty(STORE_ID_LENGTH_PROPERTY_NAME,
                    Integer.toString(DEFAULT_STORE_ID_LENGTH));
            String workstationIDLengthProperty = DomainGateway.getProperty(WORKSTATION_ID_LENGTH_PROPERTY_NAME,
                    Integer.toString(DEFAULT_WORKSTATION_ID_LENGTH));
            String sequenceNumberLengthProperty = DomainGateway.getProperty(SEQUENCE_NUMBER_LENGTH_PROPERTY_NAME,
                    Integer.toString(DEFAULT_SEQUENCE_NUMBER_LENGTH));
            String barcodeDateFormatProperty = "ddMMyyyy"; /*DomainGateway.getProperty(BARCODE_DATE_FORMAT_PROPERTY_NAME,
                    DEFAULT_BARCODE_DATE_FORMAT)*/;
            setStoreIDLength(Integer.parseInt(storeIDLengthProperty));
            setWorkstationIDLength(Integer.parseInt(workstationIDLengthProperty));
            setSequenceNumberLength(Integer.parseInt(sequenceNumberLengthProperty));
            setBarcodeDateFormat(barcodeDateFormatProperty);

            paramsInitialized = true;
        }
    }

    /**
     * Decodes the transaction ID into the appropriate attributes.
     * 
     * @param transID The transaction ID to set
     * @throws IllegalArgumentException If the format of the transaction ID is
     *             invalid
     */
    public void setTransactionID(String transID)
    {
        // Set the string value of the the transaction id
        if (transID == null || transID.length() == 0)
        {
            setStoreID("");
            setWorkstationID("");
            setSequenceNumber(0);
            if (barcodeDateLength > 0)
                setBusinessDate("");
        }
        else
        {
            // Attempt to parse the id into object required
            try
            {
              // Allow for leading zeroes
                int transactionIDLength = getTransactionIDLength();
                int transactionIDLengthMin = transactionIDLength - getBarcodeDateLength();
                if (transID.length() > transactionIDLength)
                {
                    throw new IllegalArgumentException("Length of transaction id: " + transID
                            + " longer than expected length of: " + transactionIDLength);
                }
                else if (transID.length() < transactionIDLengthMin)
                {
                    throw new IllegalArgumentException("Length of transaction id: " + transID
                            + " shorter than expected length of: " + transactionIDLengthMin);
                }

                // This computes the length of the store ID by allowing
                // leading zeroes to exist.
                int storePos = 0;
                int storeLen = getStoreIDLength();
                int wsPos = storePos + storeLen;
                int wsLen = getWorkstationIDLength();
                int sequenceNumberPos = wsPos + wsLen;
                int sequenceNumberLen = getSequenceNumberLength();
                int sequenceNumberLenLastIndex = sequenceNumberPos + sequenceNumberLen;
                int busDatePos = getTransactionIDLength() - getBarcodeDateLength();

                // Check to ensure that the variable used in the sequenceString
                // assignment below does
                // not go beyond the transID.length() bounds.
                if (sequenceNumberLenLastIndex > transID.length())
                {
                    sequenceNumberLenLastIndex = transID.length();
                }

                // Split the string into its components
                String store = transID.substring(storePos, storeLen);
                String ws = transID.substring(wsPos, wsPos + wsLen);
                String sequenceString = transID.substring(sequenceNumberPos, sequenceNumberLenLastIndex);
                String busDate = "";
                if (transID.length() > busDatePos)
                {
                    busDate = transID.substring(busDatePos);
                }
                // Convert the sequence number
                Long value = new Long(sequenceString);
                long transactionSequenceNumber = value.longValue();

                // set values in this object
                if (busDate.length() > 0)
                {
                    setTransactionID(store, ws, transactionSequenceNumber, busDate);
                }
                else
                {
                    setTransactionID(store, ws, transactionSequenceNumber);
                }
            }
            catch (IllegalArgumentException e)
            {
                // KLM: Adding a logger statement to ease the pain of
                // debugging
                //
                logger.warn("Error occurred while building transaction id: " + e);

                // Set the parsed values to zeros
                sequenceNumber = 0;
                storeID = "";
                workstationID = "";
            }
        }

    }

    /**
     * Creates clone of this object.
     * 
     * @return Object clone of this object
     */
    public Object clone()
    {
        // instantiate new object
        TransactionID clone = new TransactionID();

        // set clone attributes
        setCloneAttributes(clone);

        // pass back Object
        return clone;
    }

    /**
     * Sets attributes in clone.
     * 
     * @param clone new instance of class
     */
    protected void setCloneAttributes(TransactionID clone)
    {
        super.setCloneAttributes(clone);
        // set values
        if (storeID != null)
        {
            clone.setStoreID(storeID);
        }
        if (workstationID != null)
        {
            clone.setWorkstationID(workstationID);
        }
        clone.setSequenceNumber(sequenceNumber);
        if (businessDate != null)
        {
            clone.setBusinessDate(businessDate);
        }
    }

    /**
     * Determine if two objects are identical.
     * 
     * @param obj object to compare with
     * @return true if the objects are identical, false otherwise
     */
    public boolean equals(Object obj)
    {
        boolean isEqual = true;
        TransactionID c = (TransactionID)obj; // downcast the input object
        // compare all the attributes of TransactionID
        if (Util.isObjectEqual(getTransactionIDString(), c.getTransactionIDString()) &&
                // use formatted values to offset possibility of leading zeroes,
                // which shouldn't affect equal-ability
                Util.isObjectEqual(getFormattedStoreID(), c.getFormattedStoreID()) &&
                Util.isObjectEqual(getFormattedWorkstationID(), c.getFormattedWorkstationID()) &&
                sequenceNumber == c.getSequenceNumber() &&
                Util.isObjectEqual(getBusinessDateString(), c.getBusinessDateString()))
        {
            isEqual = true; // set the return code to true
        }
        else
        {
            isEqual = false; // set the return code to false
        }
        return isEqual;
    }

    /**
     * Retrieves transaction identifier string.
     * 
     * @return transaction identifier string
     */
    public String getTransactionIDString()
    {
        String retval = "";
        if (storeID.length() != 0 || workstationID.length() != 0 || sequenceNumber > 0)
        {
            retval = paddedString(getFormattedStoreID(), storeIDLength)
                    + paddedString(getFormattedWorkstationID(), workstationIDLength)
                    + getFormattedTransactionSequenceNumber();
            if (getBarcodeDateLength() > 0)
                retval += businessDate;
        }
        return retval;
    }

    /**
     * Get formatted transaction sequence number string.
     * 
     * @return formatted transaction sequence number string
     */
    public String getFormattedTransactionSequenceNumber()
    {
        // format to required number of digits
        DecimalFormat df = getSequenceNumberFormatter();
        String strSequence = df.format(sequenceNumber);
        return strSequence;
    }

    /**
     * Get formatted store identifier string. If store ID is numeric, format to
     * five digits.
     * 
     * @return formatted store identifier string
     */
    public String getFormattedStoreID()
    {
        String returnString = storeID;
        try
        {
            Integer tempInt;
            DecimalFormat df = getStoreIDFormatter();
            // if no store ID, set integer value to zero
            if (Util.isEmpty(storeID))
            {
                tempInt = new Integer(0);
            }
            else
            {
                // attempt to parse store ID to integer. if it fails,
                // it ain't a number
                tempInt = Integer.valueOf(storeID);
            }
            returnString = df.format(tempInt.longValue());
        }
        catch (NumberFormatException e)
        {
            // if can't format, use raw
        }
        // pass back formatted string
        return returnString;

    }

    /**
     * Get formatted workstation identifier string. If workstation ID is
     * numeric, format to three digits.
     * 
     * @return formatted workstation identifier string
     */
    public String getFormattedWorkstationID()
    {
        String returnString;
        try
        {
            Integer tempInt;
            DecimalFormat df = getWorkstationIDFormatter();
            // if no workstation ID, set integer value to zero
            if (Util.isEmpty(workstationID))
            {
                tempInt = new Integer(0);
            }
            else
            {
                // attempt to parse workstation ID to integer. if it fails,
                // it ain't a number
                tempInt = Integer.valueOf(workstationID);
            }
            returnString = df.format(tempInt.longValue());
        }
        catch (NumberFormatException e)
        {
            // if can't format, use raw
            returnString = workstationID;
        }
        // pass back value
        return returnString;

    }

    /**
     * Retrieves store identifier.
     * 
     * @return store identifier
     */
    public String getStoreID()
    {
        return storeID;
    }

    /**
     * Sets store identifier.
     * 
     * @param value store identifier
     */
    public void setStoreID(String value)
    {
        if (value == null)
        {
            storeID = "";
        }
        else
        {
            String trimmedValue = value.trim();
            if (trimmedValue.length() <= getStoreIDLength())
            {
                storeID = trimmedValue;
            }
            else
            {
                throw new IllegalArgumentException("Invalid length for store ID");
            }
        }

    }

    /**
     * Retrieves workstation identifier.
     * 
     * @return workstation identifier
     */
    public String getWorkstationID()
    {
        return workstationID;
    }

    /**
     * Sets workstation identifier.
     * 
     * @param value workstation identifier
     */
    public void setWorkstationID(String value)
    {
        if (value == null)
        {
            workstationID = "";
        }
        else
        {
            String trimmedValue = value.trim();
            if (trimmedValue.length() <= getWorkstationIDLength())
            {
                workstationID = trimmedValue;
            }
            else
            {
                throw new IllegalArgumentException("Invalid length for workstation ID");
            }
        }

    }

    /**
     * Retrieves transaction sequence number.
     * 
     * @return transaction sequence number
     */
    public long getSequenceNumber()
    {
        return sequenceNumber;
    }

    /**
     * Sets transaction sequence number.
     * 
     * @param value transaction sequence number
     */
    public void setSequenceNumber(long value)
    {
        sequenceNumber = value;
    }

    /**
     * Returns formatted transaction identifier length. This is the sum of the
     * lengths of the store identifier, workstation identifier and sequence
     * number. If the business date is part of the transaction, then this
     * length is also included.
     * 
     * @return formatted transaction identifier length
     */
    public static int getTransactionIDLength()
    {
        setFormattingSpecifications();
        return getStoreIDLength() + getWorkstationIDLength() + getSequenceNumberLength() + getBarcodeDateLength();
    }

    /**
     * Returns store identifier length
     * 
     * @return store identifier length
     */
    public static int getStoreIDLength()
    {
        setFormattingSpecifications();
        return storeIDLength;
    }

    /**
     * Sets store identifier length
     * 
     * @param value store identifier length
     */
    protected static void setStoreIDLength(int value)
    {
        storeIDLength = value;
    }

    /**
     * Returns workstation identifier length
     * 
     * @return workstation identifier length
     */
    public static int getWorkstationIDLength()
    {
        setFormattingSpecifications();
        return workstationIDLength;
    }

    /**
     * Sets workstation identifier length
     * 
     * @param value workstation identifier length
     */
    protected static void setWorkstationIDLength(int value)
    {
        workstationIDLength = value;
    }

    /**
     * Returns sequence number length
     * 
     * @return sequence number length
     */
    public static int getSequenceNumberLength()
    {
        setFormattingSpecifications();
        return sequenceNumberLength;
    }

    /**
     * Sets sequence number length
     * 
     * @param value sequence number length
     */
    protected static void setSequenceNumberLength(int value)
    {
        sequenceNumberLength = value;
    }

    /**
     * Returns barcode date length
     * 
     * @return barcode date length
     */
    public static int getBarcodeDateLength()
    {
        setFormattingSpecifications();
        return barcodeDateLength;
    }

    /**
     * Sets barcode date length
     * 
     * @param value barcode date length
     */
    protected static void setBarcodeDateLength(int value)
    {
        barcodeDateLength = value;
    }

    /**
     * Returns barcode date format
     * 
     * @return barcode date format
     */
    public static String getBarcodeDateFormat()
    {
        setFormattingSpecifications();
        return barcodeDateFormat;
    }

    /**
     * Sets barcode date format
     * 
     * @param value barcode date format
     */
    protected static void setBarcodeDateFormat(String value)
    {
        barcodeDateFormat = value;
        setBarcodeDateLength(barcodeDateFormat.length());
    }

    /**
     * Returns store identifier format pattern.
     * 
     * @return store identifier format pattern
     */
    protected String getStoreIDFormatPattern()
    {
        return getFormatPattern(getStoreIDLength());
    }

    /**
     * Returns workstation identifier format pattern.
     * 
     * @return workstation identifier format pattern
     */
    protected String getWorkstationIDFormatPattern()
    {
        return getFormatPattern(getWorkstationIDLength());
    }

    /**
     * Returns sequence number format pattern.
     * 
     * @return sequence number format pattern
     */
    protected String getSequenceNumberFormatPattern()
    {
        return getFormatPattern(getSequenceNumberLength());
    }

    /**
     * Returns format pattern of specified length.
     * 
     * @param len length of pattern
     * @return format pattern of specified length.
     */
    protected String getFormatPattern(int len)
    {
        return FORMAT_PATTERN_SOURCE.substring(0, len);
    }

    /**
     * Returns formatter for specified pattern. If formatter has not been
     * instantiated, the formatter is instantiated.
     * 
     * @param pattern pattern for formatter
     * @return formatter for specified pattern
     * @deprecated as of 13.4. Use {@link #getStoreIDFormatter()} instead.
     */
    protected DecimalFormat getFormatter(String pattern)
    {
        if (formatter == null)
        {
            formatter = new DecimalFormat(pattern);
        }
        else if (!formatter.toPattern().equals(pattern))
        {
            formatter.applyPattern(pattern);
        }
        return formatter;
    }

    /**
     * Returns formatter for SequenceNumber pattern. If formatter has not been
     * instantiated, the formatter is instantiated.
     * 
     * @return formatter for specified pattern
     */
    protected synchronized DecimalFormat getSequenceNumberFormatter()
    {
        if (formatterSequenceNumber == null)
        {
            formatterSequenceNumber = new DecimalFormat(getSequenceNumberFormatPattern());
        }
        return formatterSequenceNumber;
    }

    /**
     * Returns formatter for StoreID pattern. If formatter has not been
     * instantiated, the formatter is instantiated.
     * 
     * @return formatter for specified pattern
     */
    protected synchronized DecimalFormat getStoreIDFormatter()
    {
        if (formatterStoreID == null)
        {
            formatterStoreID = new DecimalFormat(getStoreIDFormatPattern());
        }
        return formatterStoreID;
    }

    /**
     * Returns formatter for WorkstationID pattern. If formatter has not been
     * instantiated, the formatter is instantiated.
     * 
     * @return formatter for specified pattern
     */
    protected synchronized DecimalFormat getWorkstationIDFormatter()
    {
        if (formatterWorkstationID == null)
        {
            formatterWorkstationID = new DecimalFormat(getWorkstationIDFormatPattern());
        }
        return formatterWorkstationID;
    }

    /**
     * Returns default display string.
     * 
     * @return String representation of object
     */
    public String toString()
    {
        // build result string
        StringBuilder strResult = Util
                .classToStringHeader("TransactionID", getRevisionNumber(), hashCode())
                // add attributes to string
                .append(Util.formatToStringEntry("transactionIDString", getTransactionIDString()))
                .append(Util.formatToStringEntry("storeID", storeID))
                .append(Util.formatToStringEntry("workstationID", workstationID))
                .append(Util.formatToStringEntry("sequenceNumber", Long.toString(sequenceNumber)))
                .append(Util.formatToStringEntry("businessDate", businessDate))
                // take care of superclass
                .append(super.toString());
        // pass back result
        return strResult.toString();
    }

    /**
     * Retrieves the source-code-control system revision number.
     * 
     * @return String representation of revision number
     */
    public String getRevisionNumber()
    {
        // return string
        return Util.parseRevisionNumber(revisionNumber);
    }

    /**
     * Returns the formatted date.
     * 
     * @param aDate the date
     * @param format the format pattern
     * @return The formatted date.
     */
    protected String getDateStamp(Date aDate, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateStamp = formatter.format(aDate);
        return dateStamp;
    }

    /**
     * Returns date from the barcode This string is in the format specified in
     * the domain properties. For an internationalize formatted string retrieve
     * the EYSDate version and call the toFormattedString with the correct
     * locale.
     * 
     * @return value of the barcode date
     */
    public String getBusinessDateString()
    {
        return businessDate;
    }

    /**
     * Returns the business date for the transaction.
     * 
     * @return The business date for the transaction.
     */
    public EYSDate getBusinessDate()
    {
        EYSDate eysDate = null;

        if (businessDate.length() > 0)
        {
            eysDate = new EYSDate();
            eysDate.initialize(businessDate, barcodeDateFormat);
        }

        return eysDate;
    }

    /**
     * Sets the barcode date.
     * 
     * @param string barcode date as a String
     */
    public void setBusinessDate(String string)
    {
        if (string == null || string.length() == 0)
        {
            businessDate = "";
        }
        else if (string.length() == barcodeDateLength)
        {
            businessDate = string;
        }
        else
        {
            throw new IllegalArgumentException("Invalid length for business date");
        }
    }

    /**
     * Sets the barcode date.
     * 
     * @param businessDate barcode businessDate
     */
    public void setBusinessDate(Date businessDate)
    {
        if (businessDate == null)
            setBusinessDate("");
        else
            setBusinessDate(getDateStamp(businessDate, barcodeDateFormat));
    }

    /**
     * Sets the barcode date.
     * 
     * @param businessDate barcode businessDate
     */
    public void setBusinessDate(EYSDate businessDate)
    {
        if (businessDate != null)
            this.businessDate = businessDate.toFormattedString(barcodeDateFormat);
        else
            setBusinessDate("");
    }

    private String paddedString(String s, int len)
    {
        String retval = s;

        while (retval.length() < len)
            retval += " ";
        return retval;
    }
    
    public String prefixLeadingZeroes(String transactionID)
    {
        int transactionIDLength = getTransactionIDLength();
        while (transactionID.length() < transactionIDLength)
        {
            transactionID = "0" + transactionID;
        }
        return transactionID;
    }


}
