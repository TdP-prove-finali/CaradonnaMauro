package it.polito.tdp.provaLaurea.model;

public class Item{
	
	private String itemId;
	private String itemDescription;
	private String commodityDescription;
	private boolean previouslyPurchased;
	private boolean currentlyPurchased;
	
	public Item(String itemId, String itemDescription, String commodityId) {
		super();
		this.itemId = itemId;
		this.itemDescription = itemDescription;
		this.commodityDescription = commodityId;
		this.previouslyPurchased = false;
		this.currentlyPurchased = false;
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getCommodityDescription() {
		return commodityDescription;
	}
	public void setCommodityDescription(String commodityDescription) {
		this.commodityDescription = commodityDescription;
	}
	public boolean isPreviouslyPurchased() {
		return previouslyPurchased;
	}
	public void setPreviouslyPurchased(boolean previouslyPurchased) {
		this.previouslyPurchased = previouslyPurchased;
	}
	public boolean isCurrentlyPurchased() {
		return currentlyPurchased;
	}
	public void setCurrentlyPurchased(boolean currentlyPurchased) {
		this.currentlyPurchased = currentlyPurchased;
	}
	
	@Override
	public String toString() {
		return this.itemId;
	}

	

}
