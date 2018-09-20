/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package oracle.retail.stores.commerceservices.common.datetime;

import oracle.retail.stores.commerceservices.ServiceLocator;

public class DateTimeServiceLocator extends ServiceLocator {
	public static DateTimeServiceIfc getDateTimeService() {
		return ((DateTimeServiceIfc) new DateTimeService(new DateTimeConverter(), new DateTimeValidator()));
	}
}