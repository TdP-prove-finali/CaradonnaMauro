package it.polito.tdp.acquisti.model;

import java.time.LocalDate;
import java.util.Objects;

public class SimulatorItem {

	public enum State{
		ABSENT,                          // ITEM MANCANTE
		IN_FINDING,						//si sta cercando
		PURCHASED,					    // ACQUISTATO
		WAREHOUSE_ARRIVAL, 	 	   		//IL PEZZO è ARRIVATO
		LINE_ARRIVAL,					//ARRIVO SULLA LINEA
		OUT                           // ESCE DAL MAGAZZINO
	};
	
	private State state;
	private LocalDate arrivalDate;
	private int id;
	private double purchasePrice;
	private String itemId;
	private int quantity;
	
	
	public SimulatorItem(State state, LocalDate arrivalDate, int id, double purchasePrice, String itemId,
			int quantity) {
		super();
		this.state = state;
		this.arrivalDate = arrivalDate;
		this.id = id;
		this.purchasePrice = purchasePrice;
		this.itemId = itemId;
		this.quantity = quantity;
	}


	
	public int getQuantity() {
		return quantity;
	}



	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	public String getItemId() {
		return itemId;
	}


	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}



	@Override
	public int hashCode() {
		return Objects.hash(itemId);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimulatorItem other = (SimulatorItem) obj;
		return Objects.equals(itemId, other.itemId);
	}



	@Override
	public String toString() {
		return "SimulatorItem [itemId=" + itemId + ", arrivalDate=" + arrivalDate + ", id=" + id + ", purchasePrice="
				+ purchasePrice + ",state=" + state  + ", quantity=" + quantity + "]";
	}

	
}
