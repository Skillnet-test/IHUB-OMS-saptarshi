/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package oracle.retail.stores.common.utility;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import org.apache.log4j.Logger;

public class LocaleMapLoader implements LocaleMapLoaderIfc {
	public static final String revisionNumber = "$Revision: /rgbustores_13.4x_generic_branch/1 $";
	protected static Logger logger = Logger.getLogger(LocaleMapLoader.class);

	public LocaleMap loadLocaleMap(String name, ClassLoader loader) {
		if (logger.isDebugEnabled()) {
			logger.debug("Loading locales from " + name);
		}

		LocaleMap localeMap = new LocaleMap();
		PropertiesLoader propertiesLoader = new PropertiesLoader();
		Properties properties = propertiesLoader.loadProperties(name);
		loadLocaleMap(properties);
		return localeMap;
	}

	public void loadLocaleMap(Properties properties) {
		for (Enumeration e = properties.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			if (key.startsWith(LocaleMapLoaderIfc.LOCALE_PREFIX)) {
				String value = properties.getProperty(key);
				Locale newLocale = getLocaleFromString(value);
				LocaleMap.putLocale(key, newLocale);
				if (logger.isDebugEnabled()) {
					logger.debug(key + ": " + LocaleMap.getLocale(key));
				}
			}

			if (key.startsWith(LocaleMapLoaderIfc.SUPPORTED_LOCALE_PREFIX)) {
				String localeCodeList = properties.getProperty(key);

				logger.info("Processing Supported Locales: " + localeCodeList);

				if (localeCodeList != null) {
					String[] codes = localeCodeList.split(LocaleMapLoaderIfc.COMMA_DELIMITER);
					for (int i = 0; i < codes.length; ++i) {
						String code = codes[i].trim();
						if (code.length() <= 0)
							continue;
						Locale newLocale = getLocaleFromString(code);
						LocaleMap.putSupportedLocale(key, newLocale);
						logger.debug(key + ": " + newLocale);
					}
				}
			}
		}
	}

	public LocaleMap loadLocaleMap(String name) {
		return loadLocaleMap(name, Thread.currentThread().getContextClassLoader());
	}

	public Locale getLocaleFromString(String value) throws IllegalArgumentException {
		Locale alocale = null;

		if (checkLocaleSubsystemValueExists(value)) {
			if (value.length() > 3) {
				String language = value.substring(0, 2);

				if (value.length() > 6) {
					String country = value.substring(3, 5);
					String variant = value.substring(6);
					alocale = new Locale(language, country, variant);
				} else {
					String country = value.substring(3);
					alocale = new Locale(language, country);
				}

			} else {
				alocale = new Locale(value);
			}
		} else {
			if (logger.isInfoEnabled())
				logger.info(
						"The value passed to getLocaleFromString(): " + value + " does not represents  a valid locale");

			throw new IllegalArgumentException();
		}

		return alocale;
	}

	private boolean checkLocaleSubsystemValueExists(String value) {
		boolean localeValue = true;
		if ((Util.isEmpty(value)) || (value.indexOf(LocaleMapLoaderIfc.COMMA_DELIMITER) >= 0)) {
			localeValue = false;
		}
		return localeValue;
	}
}