/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package oracle.retail.stores.common.utility;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocaleMap {
	public static final String DEFAULT = "locale_Default";
	
	private static Map<String, Locale> map = new Hashtable();
	
	private static String default_locale="en_GB";

	private static Map<String, Map<String, Locale>> supportedMap = new Hashtable();
	protected static final String DEFAULT_PROPERTIES = "application.properties";
	protected static final Logger logger = Logger.getLogger(LocaleMap.class);

	protected static boolean pluginMode = false;
	public static final String UNDERSCORE_DELIMITER = "_";

	public static void putLocale(String key, Locale locale) {
		map.put(key, locale);
	}

	public static Locale getLocale(String key) {
		Locale locale = (Locale) map.get(key);

		if (locale == null) {
			locale = (Locale) map.get(DEFAULT);
		}
		return locale;
	}
	
	public static Locale getDefaultLocale() {
		
		Locale defaultLocale = (Locale) map.get(DEFAULT);
		return defaultLocale;
	}

	public static void putSupportedLocale(String topic, Locale locale) {
		Map localesMap = (Map) supportedMap.get(topic);

		if (localesMap == null) {
			localesMap = new Hashtable();
			supportedMap.put(topic, localesMap);
		}

		localesMap.put(locale.getLanguage() + UNDERSCORE_DELIMITER + locale.getCountry(), locale);
	}

	public static boolean isSupported(String topic, String language, String territory) {
		boolean supported = false;

		Map localesMap = (Map) supportedMap.get(topic);

		if (localesMap != null) {
			supported = localesMap.containsKey(language + UNDERSCORE_DELIMITER + territory);
		}
		return supported;
	}

	public static BestMatchLocaleInDepth getBestMatchInDepth(Locale userLocale) throws LocaleNotFoundException {
		return findBestMatchInDepth("", userLocale);
	}

	public static Locale getBestMatch(Locale userLocale) throws LocaleNotFoundException {
		return getBestMatch("", userLocale);
	}

	public static Locale getBestMatch(String topic, Locale userLocale) throws LocaleNotFoundException {
		BestMatchLocaleInDepth locale = findBestMatchInDepth(topic, userLocale);
		return locale.getBestMatchLocale();
	}

	public static Set<Locale> getBestMatch(String topic, Collection<Locale> locales) throws LocaleNotFoundException {
		Set bestMatchLocales = new HashSet();
		Iterator iter = locales.iterator();

		while (iter.hasNext()) {
			Locale bestMatchLocale = getBestMatch(topic, (Locale) iter.next());
			bestMatchLocales.add(bestMatchLocale);
		}

		return bestMatchLocales;
	}

	private static BestMatchLocaleInDepth findBestMatchInDepth(String topic, Locale userLocale)
			throws LocaleNotFoundException {
		if (pluginMode) {
			return BestMatchLocaleInDepth.getInstance(new Locale(userLocale.getLanguage()), 0);
		}

		String key = "supported_locales";
		if (!(StringUtils.isEmpty(topic))) {
			key = key + UNDERSCORE_DELIMITER + topic;
		}
		Map localesMap = (Map) supportedMap.get(key);
		if (localesMap == null) {
			throw new LocaleNotFoundException("There are no supported locales defined in application.properties. key: "
					+ key + ", userLocale: " + userLocale + ", Supported Locales: " + supportedMap);
		}

		BestMatchLocaleInDepth locale = findBestMatch(localesMap, userLocale);

		if (locale.getBestMatchLocale() == null) {
			throw new LocaleNotFoundException(
					"No matching locale was found in the supported locale list. locale: " + locale);
		}

		return locale;
	}

	private static BestMatchLocaleInDepth findBestMatch(Map<String, Locale> localesMap, Locale userLocale) {
		BestMatchLocaleInDepth bestMatchedLocale = lookupSupportedLocales(localesMap, userLocale);
		int depth = bestMatchedLocale.getDepth();

		if (bestMatchedLocale.getBestMatchLocale() == null) {
			bestMatchedLocale = lookupSupportedLocales(localesMap, (Locale) map.get(DEFAULT));
			depth += bestMatchedLocale.getDepth();

			if (bestMatchedLocale.getBestMatchLocale() == null) {
				bestMatchedLocale = lookupSupportedLocales(localesMap, Locale.getDefault());
				depth += bestMatchedLocale.getDepth();
			}
		}

		return BestMatchLocaleInDepth.getInstance(bestMatchedLocale.getBestMatchLocale(), depth);
	}

	private static BestMatchLocaleInDepth lookupSupportedLocales(Map<String, Locale> localesMap, Locale userLocale) {
		Locale bestMatchedLocale = null;
		int depth = 1;

		Locale locale = userLocale;
		if (localesMap.containsValue(locale)) {
			bestMatchedLocale = locale;
		} else {
			locale = new Locale(userLocale.getLanguage(), userLocale.getCountry());

			++depth;
			if (localesMap.containsValue(locale)) {
				bestMatchedLocale = locale;
			} else {
				locale = new Locale(userLocale.getLanguage());
				++depth;
				if (localesMap.containsValue(locale)) {
					bestMatchedLocale = locale;
				}
			}
		}
		return BestMatchLocaleInDepth.getInstance(bestMatchedLocale, depth);
	}

	public static Locale adjustLocale(Locale locale) {
		Locale defaultLocale = (Locale) map.get(DEFAULT);

		if ((Util.isEmpty(locale.getCountry())) && (Util.isEmpty(locale.getVariant()))) {
			locale = new Locale(locale.getLanguage(), defaultLocale.getCountry(), defaultLocale.getVariant());
		}

		if (Util.isEmpty(locale.getCountry())) {
			locale = new Locale(locale.getLanguage(), defaultLocale.getCountry(), locale.getVariant());
		}

		if (Util.isEmpty(locale.getVariant())) {
			locale = new Locale(locale.getLanguage(), locale.getCountry(), defaultLocale.getVariant());
		}

		return locale;
	}

	public static Locale[] getSupportedLocales() throws LocaleNotFoundException {
		return getSupportedLocales("");
	}

	public static Locale[] getSupportedLocales(String topic) throws LocaleNotFoundException {
		if (pluginMode) {
			return new Locale[] { Locale.getDefault() };
		}
		String key = "supported_locales";
		if (!(StringUtils.isEmpty(topic))) {
			key = key + UNDERSCORE_DELIMITER + topic;
		}
		Map localesMap = (Map) supportedMap.get(key);
		if (localesMap == null) {
			throw new LocaleNotFoundException("There are no supported locales defined in application.properties. key: "
					+ key + ", Supported Locales: " + supportedMap);
		}

		Locale[] supportedLocales = new Locale[localesMap.size()];
		return ((Locale[]) localesMap.values().toArray(supportedLocales));
	}

	public static LocaleRequestor getSupportedLocaleRequestor() throws LocaleNotFoundException {
		LocaleRequestor localeRequestor = null;

		Locale[] supportedLocales = getSupportedLocales();
		Locale defaultLocaleMatch = getBestMatch(getLocale(DEFAULT));
		int defaultIndex = 0;
		for (int i = supportedLocales.length - 1; i >= 0; --i) {
			if (!(defaultLocaleMatch.equals(supportedLocales[i])))
				continue;
			defaultIndex = i;
			break;
		}

		localeRequestor = new LocaleRequestor(supportedLocales, defaultIndex);
		return localeRequestor;
	}

	static 
	{
		map.put(DEFAULT, new Locale(default_locale));
		try {
			new LocaleMapLoader().loadLocaleMap(DEFAULT_PROPERTIES);
		} catch (IllegalArgumentException iae) {
			pluginMode = true;
		} catch (Exception e) {
			logger.warn(e);
		}
	}
}