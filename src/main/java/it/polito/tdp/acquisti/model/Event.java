package it.polito.tdp.acquisti.model;

import java.time.LocalDate;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		FINDING,     			//cerco i pezzi mancanti
		PURCHASE,               //acquisto ad un prezzo
		EMERGENCY_PURCHASE,		//ACQUISTO AD UN PREZZO PIù ALTO
		ARRIVAL,				//IL PEZZO ARRIVA
		DELAY,   		     	//IL PEZZO è IN RITARDO
		ANNULMENT,				//IL PEZZO NON SARà CONSEGNATO
		TAKING,					//IL PREZZO VIENE PRELEVATO PER IL MONTAGGIO
		SALE,					//ESCE
	};
	
	private LocalDate time;
	private EventType type;
	private SimulatorItem item;
	
	public Event(LocalDate time, EventType type, SimulatorItem item) {
		super();
		this.time = time;
		this.type = type;
		this.item = item;
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

	public SimulatorItem getItem() {
		return item;
	}

	public void setItem(SimulatorItem item) {
		this.item = item;
	}

	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.time);
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + ", item=" + item + "]";
	}
	
	

}
