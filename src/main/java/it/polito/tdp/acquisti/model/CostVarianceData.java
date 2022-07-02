package it.polito.tdp.acquisti.model;

import java.time.LocalDate;

public class CostVarianceData {
	
	private Item item;
	private double totalPrice;
	private int quantity;
	private double lastPrice;
	private LocalDate lastPurchaseDate;
	
	public CostVarianceData(Item item, double totalPrice, int quantity, double lastPrice, LocalDate lastPurchaseDate) {
		super();
		this.item = item;
		this.totalPrice = totalPrice;
		this.quantity = quantity;
		this.lastPrice = lastPrice;
		this.lastPurchaseDate = lastPurchaseDate;
	}
	
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}
	public LocalDate getLastPurchaseDate() {
		return lastPurchaseDate;
	}
	public void setLastPurchaseDate(LocalDate lastPurchaseDate) {
		this.lastPurchaseDate = lastPurchaseDate;
	}

	/**
	 * @return average purchase price
	 */
	public double getAveragePrice() {
		return this.totalPrice / this.quantity;
	}
	
	
}
