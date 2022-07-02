package it.polito.tdp.acquisti.model;

import java.time.LocalDate;

public class Invoice {
	
	private String invoiceId;
	private LocalDate invoiceDate;
	private int quantity ;
	private double unitPurchasePrice;
	private double totalPurchasePrice;
	
	public Invoice(String invoiceId, LocalDate invoiceDate, int quantity, double unitPurchasePrice, double totalPurchasePrice) {
		super();
		this.invoiceId = invoiceId;
		this.invoiceDate = invoiceDate;
		this.quantity = quantity;
		this.unitPurchasePrice = unitPurchasePrice;
		this.totalPurchasePrice = totalPurchasePrice;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	
	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPurchasePrice() {
		return unitPurchasePrice;
	}

	public void setUnitPurchasePrice(double unitPurchasePrice) {
		this.unitPurchasePrice = unitPurchasePrice;
	}

	public double getTotalPurchasePrice() {
		return totalPurchasePrice;
	}

	public void setTotalPurchasePrice(double totalPurchasePrice) {
		this.totalPurchasePrice = totalPurchasePrice;
	}
	
	@Override
	public String toString() {
		return this.invoiceId;
	}

}
