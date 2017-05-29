package com.acentic.cloudservices.session.web;

import com.acentic.cloudservices.hotel.SystemHotel.beans.SystemHotelBean;
import java.io.Serializable;

public class SERHotel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ident;
	private String hotelname;
	private String country;
	private String city;

	public SERHotel() {
	}
	
	public SERHotel(SystemHotelBean selectedHotel) {
		super();
		this.ident = selectedHotel.getIdent();
		this.hotelname = selectedHotel.getHotelName();
		this.country = selectedHotel.getCountry();
		this.city = selectedHotel.getCity();
	}
	
	
	public String getIdent() {
		return ident;
	}
	public void setIdent(String ident) {
		this.ident = ident;
	}
	public String getHotelname() {
		return hotelname;
	}
	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
	
}
