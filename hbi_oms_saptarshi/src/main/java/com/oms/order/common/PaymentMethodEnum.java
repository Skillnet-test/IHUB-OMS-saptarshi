/**
 * 
 */
package com.oms.order.common;

/**
 * @author Jigar
 *
 */
public enum PaymentMethodEnum 
{
	CASH_PAYMENT(1,"CASH"),
	CARD_PAYMENT(5,"CRDT");
	
	private final int value;
	
	private final String type;

	PaymentMethodEnum(int v,String t) {
        value = v;
        type= t;
    }

    public int getPayMethodValue() 
    {
        return value;
    }
    
    public String getPayMethodType() 
    {
        return type;
    }

    public static PaymentMethodEnum fromValue(int v) {
        for (PaymentMethodEnum c: PaymentMethodEnum.values()) {
            if (c.value == v) {
                return c;
            }
        }
        throw new IllegalArgumentException();
    }
    
    public static PaymentMethodEnum fromType(String t) {
        for (PaymentMethodEnum c: PaymentMethodEnum.values()) {
            if (c.type.equals(t)) {
                return c;
            }
        }
        throw new IllegalArgumentException();
    }

}
