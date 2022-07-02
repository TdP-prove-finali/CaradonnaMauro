package it.polito.tdp.acquisti.model;

import java.time.LocalDate;

public class NewOrder {
	
	private String orderId ;
	private String siteId ;
	private String supplierId ;
	private String invoiceId ;
	private LocalDate invoiceDate;
	private int quantity ;
	private double unitPurchasePrice;
	private double totalPurchasePrice;
	private String itemId;
	private String buyerId ;
	
	public NewOrder(String orderId, String siteId, String supplierId, String invoiceId, LocalDate invoiceDate,
			int quantity, double unitPurchasePrice, double totalPurchasePrice, String itemId, String buyerId) {
		super();
		this.orderId = orderId;
		this.siteId = siteId;
		this.supplierId = supplierId;
		this.invoiceId = invoiceId;
		this.invoiceDate = invoiceDate;
		this.quantity = quantity;
		this.unitPurchasePrice = unitPurchasePrice;
		this.totalPurchasePrice = totalPurchasePrice;
		this.itemId = itemId;
		this.buyerId = buyerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
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

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	
	

}
