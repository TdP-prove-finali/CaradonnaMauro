package it.polito.tdp.provaLaurea.model;

import java.time.LocalDate;

import it.polito.tdp.provaLaurea.model.Event.EventType;
import it.polito.tdp.provaLaurea.model.SimulatorItem.State;

public class SimulatorRow {
	
	private LocalDate time;
	private EventType type;
	private State state;
	private LocalDate arrivalDate;
	private Integer id;
	private Double purchasePrice;
	private String itemId;
	private Integer quantity;
	public SimulatorRow(LocalDate time, EventType type, State state, LocalDate arrivalDate, Integer id,
			Double purchasePrice, String itemId, Integer quantity) {
		super();
		this.time = time;
		this.type = type;
		this.state = state;
		this.arrivalDate = arrivalDate;
		this.id = id;
		this.purchasePrice = purchasePrice;
		this.itemId = itemId;
		this.quantity = quantity;
	}
	public LocalDate getTime() {
		return time;
	}
	public void setTime(LocalDate time) {
		this.time = time;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
