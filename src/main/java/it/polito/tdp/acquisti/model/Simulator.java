package it.polito.tdp.acquisti.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.acquisti.model.Event.EventType;
import it.polito.tdp.acquisti.model.SimulatorItem.State;

public class Simulator {
	
		
		private List<SimulatorRow> simulatorRowList;
		private LocalDate saleDate;
	
		// Coda degli eventi
		private PriorityQueue<Event> queue;
		
		// Modello del mondo
		private List<SimulatorItem> warehouse;
		private List<SimulatorItem> lineItem;
		private Map<String, SimulatorItem> absentItems;
		private int purchaseId;
		
		// Parametri di input
		private List<SimulatorItem> actualWarehouse; 
		private Map<String, Integer> bom;
		
		
		private Period DURATION_FINDING =Period.ofDays(3);
		private Period DURATION_DEFAULT_LEAD_TIME = Period.ofDays(42);
		private Map<String, Period> leadTimeByItemId;
		private Period DURATION_DELAY =Period.ofDays(21);
		private Period DURATION_PRODUCTION =Period.ofDays(45);
		
		private Period CHECK_INTERVAL = Period.ofDays(2);
		
		private LocalDate startTime = LocalDate.of(2022, 5, 15);
		private LocalDate endTime = LocalDate.of(2022, 12, 31);
		private Map<String, Probability> probabilities;// = new HashMap<String ,Probability>();
		private Map<String , List<PriceTrend>> trendsByItem;// = new HashMap<String , List<PriceTrend>>();
		
		
		// Parametri di output
		private double totalPrice;

		// Inizializza il simulatore e crea gli eventi iniziali
		public void init(List<SimulatorItem> actualInput, Map<String, Integer> bomInput, LocalDate startDateInput,
				Map<String, Probability> probabilitiesInput, Map<String, List<PriceTrend>> trendsByItemInput, Map<String, Period> leadTimeByItemId) {
			this.simulatorRowList = new LinkedList<SimulatorRow>();
			
			this.queue = new PriorityQueue<Event>();
			this.actualWarehouse = actualInput;
			this.bom = bomInput;
			this.startTime = startDateInput;
			this.endTime = startDateInput.plusYears(1);
			this.probabilities = probabilitiesInput;
			this.trendsByItem = trendsByItemInput;
			this.leadTimeByItemId = leadTimeByItemId;
			
			this.purchaseId = this.actualWarehouse.size();
			if(purchaseId > 0) {
				for(int i = 0; i < purchaseId; i++) {
					this.actualWarehouse.get(i).setId(i);
				}
				this.warehouse = new ArrayList<SimulatorItem>(this.actualWarehouse);
			}else {
				this.warehouse = new ArrayList<SimulatorItem>();
			}
			this.lineItem = new ArrayList<SimulatorItem>();
			
			this.absentItems = new HashMap<String, SimulatorItem>();
			for(String itemId : this.bom.keySet()) {
				SimulatorItem si = new SimulatorItem(State.ABSENT, null, -1, -1, itemId, this.bom.get(itemId));
				this.absentItems.put(itemId, si);
			}
			
			List<SimulatorItem> toTake = new ArrayList<SimulatorItem>();
			for(SimulatorItem item: this.warehouse) {
				String itemId = item.getItemId();
				if(this.absentItems.keySet().contains(itemId)) {
					SimulatorItem absentItem = this.absentItems.get(itemId);
					int absentItemQty = absentItem.getQuantity();
					int itemQty = item.getQuantity();
					if(absentItemQty < itemQty) {
						item.setQuantity(itemQty - absentItemQty);
						this.absentItems.remove(itemId);
						this.lineItem.add(new SimulatorItem(SimulatorItem.State.LINE_ARRIVAL, item.getArrivalDate(), item.getId(), item.getPurchasePrice(), itemId, absentItemQty));
					}else if(absentItemQty == itemQty) {
						toTake.add(item);
						this.absentItems.remove(itemId);
						this.lineItem.add(new SimulatorItem(SimulatorItem.State.LINE_ARRIVAL, item.getArrivalDate(), item.getId(), item.getPurchasePrice(), itemId, absentItemQty));
					}else {
						absentItem.setQuantity(absentItemQty - itemQty);
						toTake.add(item);
						this.lineItem.add(new SimulatorItem(SimulatorItem.State.LINE_ARRIVAL, item.getArrivalDate(), item.getId(), item.getPurchasePrice(), itemId, itemQty));
					}
				}
			}
			this.warehouse.removeAll(toTake);
			
			this.totalPrice = 0.0;
			
			LocalDate today = this.startTime;
			
			while(today.isBefore(endTime) ) {
				Event e = new Event(today, EventType.TAKING, null);
				this.queue.add(e);
				today = today.plus(CHECK_INTERVAL);
			}
			
			today = this.startTime;
			
			while(today.isBefore(endTime) ) {
				Event e = new Event(today, EventType.FINDING, null);
				this.queue.add(e);
				today = today.plusDays(3);
			}
		}
		
		public void run() {
			while(!this.queue.isEmpty()) {
				Event e = this.queue.poll();
				processEvent(e);
				System.out.println(e);
				if(e.getType() != EventType.FINDING && e.getType() != EventType.TAKING) {
					SimulatorItem si = e.getItem();
					if(si != null) {
						if(si.getId() == -1 || si.getPurchasePrice() == -1) {
							this.simulatorRowList.add(new SimulatorRow(e.getTime(), e.getType(), si.getState(), si.getArrivalDate(), null, null, si.getItemId(), si.getQuantity()));
						} else
							this.simulatorRowList.add(new SimulatorRow(e.getTime(), e.getType(), si.getState(), si.getArrivalDate(), si.getId(), si.getPurchasePrice(), si.getItemId(), si.getQuantity()));
					}
					else
						this.simulatorRowList.add(new SimulatorRow(e.getTime(), e.getType(), null, null, null, null, null, null));
				}
//					this.eventString += e.toString()+"\n";
//					this.eventList.add(e);
			}
			System.out.println(totalPrice);
		}

		private void processEvent(Event e) {
			LocalDate today = e.getTime();
			SimulatorItem item = e.getItem();
			
			switch (e.getType()) {
			case FINDING:
				for(SimulatorItem toFind : this.absentItems.values()) {
					if(toFind.getState() == State.ABSENT) {
						toFind.setState(State.IN_FINDING);
						this.queue.add(new Event(today.plus(DURATION_FINDING), EventType.PURCHASE, toFind));
					}
				}
				break;
			case PURCHASE:
				item.setState(State.PURCHASED);
				String itemId = item.getItemId();
				Period DURATION_LEAD_TIME;
				if(leadTimeByItemId.containsKey(itemId))
					DURATION_LEAD_TIME = leadTimeByItemId.get(itemId);
				else
					DURATION_LEAD_TIME = DURATION_DEFAULT_LEAD_TIME;
				double num = Math.random();
				Probability probability = this.probabilities.get(itemId);
				double delayProbability = probability.getDelayProbability();
				double annulmentProbability = probability.getAnnulmentProbability();
				if(num < delayProbability) {
					List<PriceTrend> priceTrends = this.trendsByItem.get(itemId);
					int pricetrendsSize = priceTrends.size();
					if(pricetrendsSize > 1) {
						for(int i = 0; i < pricetrendsSize - 1; i++) {
							if(today.isAfter(priceTrends.get(i).getDate()) && today.isBefore(priceTrends.get(i + 1).getDate())) {
								item.setPurchasePrice(priceTrends.get(i).getPrice());
							}
						}
					} else if(today.isAfter(priceTrends.get(pricetrendsSize - 1).getDate())){
						item.setPurchasePrice(priceTrends.get(pricetrendsSize - 1).getPrice());
					}
					item.setId(purchaseId);
					purchaseId ++;
					queue.add(new Event(today.plus(DURATION_LEAD_TIME), EventType.DELAY, item));
				}
				else if(num < delayProbability + annulmentProbability) {
					probability.setAnnulmentProbability(0);
					probability.setDelayProbability(0);
					queue.add(new Event(today.plus(DURATION_LEAD_TIME), EventType.ANNULMENT, item));
				}
				else {
					List<PriceTrend> priceTrends = this.trendsByItem.get(itemId);
					int pricetrendsSize = priceTrends.size();
					if(pricetrendsSize > 1) {
						for(int i = 0; i < pricetrendsSize - 1; i++) {
							if(today.isAfter(priceTrends.get(i).getDate()) && today.isBefore(priceTrends.get(i + 1).getDate())) {
								item.setPurchasePrice(priceTrends.get(i).getPrice());
							}
						}
					} else if(today.isAfter(priceTrends.get(pricetrendsSize - 1).getDate())){
						item.setPurchasePrice(priceTrends.get(pricetrendsSize - 1).getPrice());
					}
					item.setId(purchaseId);
					purchaseId ++;
					queue.add(new Event(today.plus(DURATION_LEAD_TIME), EventType.ARRIVAL, item));
				}
				break;
			case ARRIVAL:
				item.setState(State.WAREHOUSE_ARRIVAL);
				item.setArrivalDate(today);
				this.warehouse.add(item);
				break;
			case DELAY:
				queue.add(new Event(today.plus(DURATION_DELAY), EventType.ARRIVAL, item));
				break;
			case ANNULMENT:
				item.setState(State.ABSENT);
				break;
			case TAKING:
				List<SimulatorItem> toTake = new ArrayList<SimulatorItem>();
				for(SimulatorItem si : this.warehouse) {
					String siId = si.getItemId();
					if(this.absentItems.keySet().contains(siId)) {
						SimulatorItem absentItem = this.absentItems.get(siId);
						int absentItemQty = absentItem.getQuantity();
						int siQty = si.getQuantity();
						if(absentItemQty < siQty) {
							si.setQuantity(siQty - absentItemQty);
							this.absentItems.remove(siId);
							this.lineItem.add(new SimulatorItem(SimulatorItem.State.LINE_ARRIVAL, si.getArrivalDate(), si.getId(), si.getPurchasePrice(), siId, absentItemQty));
						}else if(absentItemQty == siQty) {
							toTake.add(si);
							this.absentItems.remove(siId);
							this.lineItem.add(new SimulatorItem(SimulatorItem.State.LINE_ARRIVAL, si.getArrivalDate(), si.getId(), si.getPurchasePrice(), siId, absentItemQty));
						}else {
							absentItem.setQuantity(absentItemQty - siQty);
							toTake.add(si);
							this.lineItem.add(new SimulatorItem(SimulatorItem.State.LINE_ARRIVAL, si.getArrivalDate(), si.getId(), si.getPurchasePrice(), siId, siQty));
						}
					}
				}
				this.warehouse.removeAll(toTake);
				if(this.absentItems.keySet().size() == 0)
					queue.add(new Event(today.plus(DURATION_PRODUCTION), EventType.SALE, null));
				break;
			case SALE:
				for(SimulatorItem si : this.lineItem) {
					si.setState(State.OUT);
 					this.totalPrice += (si.getPurchasePrice()*si.getQuantity());
				}
				queue.clear(); 
				saleDate = today;
				System.out.println(today);
				break;

			default:
				break;
			}
		}

		public double getTotalPrice() {
			return totalPrice;
		}

		public void setActualWarehouse(List<SimulatorItem> actualWarehouse) {
			this.actualWarehouse = actualWarehouse;
		}

		public void setBom(Map<String, Integer> bom) {
			this.bom = bom;
		}

		public void setDURATION_FINDING(Period dURATION_FINDING) {
			DURATION_FINDING = dURATION_FINDING;
		}

		public void setDURATION_DEFAULT_LEAD_TIME(Period dURATION_DEFAULT_LEAD_TIME) {
			DURATION_DEFAULT_LEAD_TIME = dURATION_DEFAULT_LEAD_TIME;
		}

		public void setDURATION_DELAY(Period dURATION_DELAY) {
			DURATION_DELAY = dURATION_DELAY;
		}

		public void setDURATION_PRODUCTION(Period dURATION_PRODUCTION) {
			DURATION_PRODUCTION = dURATION_PRODUCTION;
		}

		public void setCHECK_INTERVAL(Period cHECK_INTERVAL) {
			CHECK_INTERVAL = cHECK_INTERVAL;
		}

		public void setStartTime(LocalDate startTime) {
			this.startTime = startTime;
		}

		public void setEndTime(LocalDate endTime) {
			this.endTime = endTime;
		}

		public void setProbabilities(Map<String, Probability> probabilities) {
			this.probabilities = probabilities;
		}

		public void setTrendsByItem(Map<String, List<PriceTrend>> trendsByItem) {
			this.trendsByItem = trendsByItem;
		}

		/*public String getEventString() {
			return eventString;
		}*/
		
		public List<SimulatorRow> getSimulatoRowList(){
			return simulatorRowList;
		}

		public LocalDate getSaleDate() {
			return saleDate;
		}

		

}
