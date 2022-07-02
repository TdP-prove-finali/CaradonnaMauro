package it.polito.tdp.acquisti.model;

import java.time.LocalDate;

public class PriceTrend {
	
	private LocalDate date;
	private double price;
	
	public PriceTrend(LocalDate date, double price) {
		super();
		this.date = date;
		this.price = price;
	}
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	

}
