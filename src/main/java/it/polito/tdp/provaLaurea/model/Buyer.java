package it.polito.tdp.provaLaurea.model;

public class Buyer {
	
	private String buyerId;
	private String buyerDescription;
	
	public Buyer(String buyerId, String buyerDescription) {
		super();
		this.buyerId = buyerId;
		this.buyerDescription = buyerDescription;
	}
	
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerDescription() {
		return buyerDescription;
	}
	public void setBuyerDescription(String buyerDescription) {
		this.buyerDescription = buyerDescription;
	}

	@Override
	public String toString() {
		return buyerDescription;
	}
	
	
}
