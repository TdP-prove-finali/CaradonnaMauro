package it.polito.tdp.acquisti.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.acquisti.model.SimulatorItem.State;

public class TestModel {

	public static void main(String[] args) {
		
		Simulator simulator = new Simulator();
		
		SimulatorItem si1 = new SimulatorItem(State.WAREHOUSE_ARRIVAL, LocalDate.of(2022, 5, 16), -1, 10.0, "itemId1", 2);
		List<SimulatorItem> actual = new ArrayList<SimulatorItem>();
		actual.add(si1);
		
		Map<String, Integer> bom = new HashMap<String, Integer>();
		bom.put("1055.54293.002", 1);
		bom.put("894.1A.030", 1);
		bom.put("IE03458", 1);
		bom.put("E002616", 1);
		bom.put("IM047984", 1);
		
		Map<String, Probability> probabilities = new HashMap<String, Probability>();
		Probability p1 = new Probability(0.5,0.1);
		probabilities.put("itemId1", p1);
		Probability p2 = new Probability(0.8,0.1);
		probabilities.put("itemId2", p2);
		Probability p3 = new Probability(0.1,0.8);
		probabilities.put("itemId3", p3);
		Probability p4 = new Probability(0.1,0.1);
		probabilities.put("itemId4", p4);
		Probability p5 = new Probability(0.0,0.0);
		probabilities.put("itemId5", p5);
		
		Map<String , List<PriceTrend>> trendsByItem = new HashMap<String, List<PriceTrend>>();
		PriceTrend pt1_1 = new PriceTrend(LocalDate.of(2022, 5, 16), 10);
		PriceTrend pt1_2 = new PriceTrend(LocalDate.of(2022, 6, 16), 15);
		PriceTrend pt1_3 = new PriceTrend(LocalDate.of(2022, 7, 16), 13);
		PriceTrend pt1_4 = new PriceTrend(LocalDate.of(2022, 12, 16), 11);
		PriceTrend pt2_1 = new PriceTrend(LocalDate.of(2022, 5, 16), 100);
		PriceTrend pt2_2 = new PriceTrend(LocalDate.of(2022, 6, 16), 150);
		PriceTrend pt2_3 = new PriceTrend(LocalDate.of(2022, 7, 16), 130);
		PriceTrend pt2_4 = new PriceTrend(LocalDate.of(2022, 12, 16), 110);
		List<PriceTrend> ptl1 = new ArrayList<PriceTrend>();
		List<PriceTrend> ptl2 = new ArrayList<PriceTrend>();
		ptl1.add(pt1_1);
		ptl1.add(pt1_2);
		ptl1.add(pt1_3);
		ptl1.add(pt1_4);
		ptl2.add(pt2_1);
		ptl2.add(pt2_2);
		ptl2.add(pt2_3);
		ptl2.add(pt2_4);
		trendsByItem.put("itemId1", ptl1);
		trendsByItem.put("itemId2", ptl1);
		trendsByItem.put("itemId3", ptl1);
		trendsByItem.put("itemId4", ptl2);
		trendsByItem.put("itemId5", ptl2);
		
		simulator.setActualWarehouse(actual);
		simulator.setBom(bom);
		simulator.setStartTime(LocalDate.of(2022, 5, 16));
		simulator.setEndTime(LocalDate.of(2022, 12, 31));
		simulator.setProbabilities(probabilities);
		simulator.setTrendsByItem(trendsByItem);
		
		//simulator.init();
		simulator.run();
		
	}

}
