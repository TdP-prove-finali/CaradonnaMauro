package it.polito.tdp.provaLaurea.model;

public class Order {
	
	private String orderId ;
	private String siteId ;
	private Supplier supplier ;
	private Invoice invoice ;
	private Item item;
	private Buyer buyer ;
	
	public Order(String orderId, String siteId, Supplier supplier, Invoice invoice, Item item, Buyer buyer) {
		super();
		this.orderId = orderId;
		this.siteId = siteId;
		this.supplier = supplier;
		this.invoice = invoice;
		this.item = item;
		this.buyer = buyer;
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
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Invoice getInvoice() {
		return invoice;
	}
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Buyer getBuyer() {
		return buyer;
	}
	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}
	
	
}
