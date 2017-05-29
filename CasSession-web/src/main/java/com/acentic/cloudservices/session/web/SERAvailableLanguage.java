package com.acentic.cloudservices.session.web;

import java.io.Serializable;

public class SERAvailableLanguage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String locale;
	private String displayLocale;
	private String iconPath;
	private boolean selected;

	public SERAvailableLanguage(String locale, String displayLocale, String iconPath, boolean selected) {
		this.locale = locale;
		this.displayLocale = displayLocale;
		this.iconPath = iconPath;
		this.selected = selected;

	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getDisplayLocale() {
		return displayLocale;
	}
	public void setDisplayLocale(String displayLocale) {
		this.displayLocale = displayLocale;
	}
	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
