package com.company.dento.ui.localization;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.google.common.base.Preconditions;

/**
 * Class that provides methods for localizing messages. Uses a message source bound to a locale.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public final class Localizer {
	
	/**
	 * Single instance of this class.
	 */
	private static final Localizer instance = new Localizer();
	/**
	 * Spring message source that reads localized messages saved in the 
	 * localization files.
	 */
	@Autowired
	private MessageSource messageSource;
	/**
	 * Current locale.
	 */
	private Locale locale;
	
	private Localizer() {}
	
	public static Localizer getInstance() {
		return instance;
	}
	
	/**
	 * Read localized string from the message source identified by id using the current locale.
	 * @param id of the localized string to be read.
	 * @return the localized string.
	 */
	public static String getLocalizedString(final String id) {
		Preconditions.checkNotNull(id);
		Preconditions.checkNotNull(instance.locale);
		String localizedName = id;
		try {
			localizedName = instance.messageSource.getMessage(id, null, instance.locale);
		} catch(NoSuchMessageException e) {
			// skip
		}
		return localizedName;
	}
	
	/**
	 * Change the current locale.
	 * @param locale the new locale.
	 */
	public static void setLocale(final Locale locale) {
		instance.locale = locale;
	}
	
	/**
	 * Retrieve the current locale.
	 * @return the current locale.
	 */
	public static Locale getCurrentLocale() {
		return instance.locale;
	}
}
