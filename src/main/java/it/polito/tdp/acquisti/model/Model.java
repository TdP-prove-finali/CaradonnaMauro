package it.polito.tdp.acquisti.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.acquisti.dao.OrderDao;
import it.polito.tdp.acquisti.model.SimulatorItem.State;
import javafx.util.Callback;

public class Model {
	
	private Map<String, Order> orderMap;
	private Map<String, Order> currentPeriodOrderMap;
	private Map<String, Order> pastPeriodOrderMap;
	private Map<String, Buyer> buyerMap;
	private Map<String, Supplier> supplierMap;
	private Map<String, Item> itemMap;
	private Map<String, Invoice> invoiceMap;
	private Set<String> commoditySet;
	private OrderDao dao;
	private LocalDate modelDate;
	private List<CostVarianceRow> costVarianceByItemRows;
	private List<CostVarianceRow> costVarianceBySiteRows;
	private List<CostVarianceRow> costVarianceByBuyerRows;
	private List<CostVarianceByCommodityRow> costVarianceByCommoditiesRows;
	private Set<String> buyerWithCVRow;
	private Set<String> siteWithCVRow;
	private Set<String> commodityWithCVRow;
	private Map<Item, CostVarianceData> pastCVDByItemMap;
	private Map<Item, CostVarianceData> currentCVDByItemMap;
	
	//Simulator
	private Simulator simulator;
	private List<SimulatorItem> actual;
    private Map<String, Integer> bom;
    private Map<String, Probability> probabilities;
    private Map<String , List<PriceTrend>> trendsByItem;
    private LocalDate startDate;
    
	public Model(){
		this.dao = new OrderDao();
		this.simulator = new Simulator();
		actual = new ArrayList<SimulatorItem>();
		bom = new HashMap<String, Integer>();
		probabilities = new HashMap<String, Probability>();
		trendsByItem = new HashMap<String, List<PriceTrend>>();
		dao.getAllTrends(trendsByItem);
	}
	
	public void init(LocalDate modelDate){
		this.orderMap = new HashMap<String, Order>();
		this.buyerMap = new HashMap<String, Buyer>();
		this.supplierMap = new HashMap<String, Supplier>();
		this.itemMap = new HashMap<String, Item>();
		this.invoiceMap = new HashMap<String, Invoice>();
		this.commoditySet = new HashSet<String>();
		dao.getAllBuyers(this.buyerMap);
		dao.getAllSuppliers(this.supplierMap);
		dao.getAllItems(this.itemMap, this.commoditySet);
		dao.getAllInvoices(this.invoiceMap);
		dao.getAllOrders(this.orderMap, this.buyerMap, this.supplierMap, this.itemMap, this.invoiceMap);
		this.currentPeriodOrderMap = new HashMap<String, Order>();
		this.pastPeriodOrderMap = new HashMap<String, Order>();
		
		this.modelDate = modelDate;
		this.uploadRepetitiveness(modelDate);
		
		pastCVDByItemMap = new HashMap<Item, CostVarianceData>();
		currentCVDByItemMap = new HashMap<Item, CostVarianceData>();
		Map<String, HashMap<Item, CostVarianceData>> pastCVDBySiteMap = new HashMap<String, HashMap<Item, CostVarianceData>>();
		Map<String, HashMap<Item, CostVarianceData>> currentCVDBySiteMap = new HashMap<String, HashMap<Item, CostVarianceData>>();
		Map<Buyer, HashMap<Item, CostVarianceData>> pastCVDByBuyerMap = new HashMap<Buyer, HashMap<Item, CostVarianceData>>();
		Map<Buyer, HashMap<Item, CostVarianceData>> currentCVDByBuyerMap = new HashMap<Buyer, HashMap<Item, CostVarianceData>>();
		
		this.buyerWithCVRow = new HashSet<String>();
		this.commodityWithCVRow = new HashSet<String>();
		this.siteWithCVRow = new HashSet<String>();
		
		this.getPastPeriodCostVarianceData(pastCVDByItemMap, pastCVDBySiteMap, pastCVDByBuyerMap);
		this.getCurrentPeriodCostVarianceData(currentCVDByItemMap, currentCVDBySiteMap, currentCVDByBuyerMap);
		this.costVarianceByItemRows = this.computeAllCostVarianceByItemRows(pastCVDByItemMap, currentCVDByItemMap);
		this.costVarianceBySiteRows = this.computeAllCostVarianceBySiteRows(pastCVDBySiteMap, currentCVDBySiteMap, siteWithCVRow);
		this.costVarianceByBuyerRows = this.computeAllCostVarianceByBuyerRows(pastCVDByBuyerMap, currentCVDByBuyerMap, buyerWithCVRow);
		this.costVarianceByCommoditiesRows = this.computeAllCostVarianceByCommodityRows(commodityWithCVRow);
	}
	

	/**
	 * This method splits all orders by date, putting them in pastPeriodOrderMap or currentPeriodOrderMap.
	 * @param modelDate
	 */
	private void uploadRepetitiveness(LocalDate modelDate){
		for(Order o : orderMap.values()){
			if(o.getInvoice() != null) {
				if(o.getInvoice().getInvoiceDate().getYear() < modelDate.getYear() && o.getInvoice().getInvoiceDate().getYear() > (modelDate.getYear() - 2)){
					o.getItem().setPreviouslyPurchased(true);
					this.pastPeriodOrderMap.put(o.getOrderId(), o);
				} else{
					o.getItem().setCurrentlyPurchased(true);
					this.currentPeriodOrderMap.put(o.getOrderId(), o);  
				}
			}
			
		}
	}
	
	/**
	 * this method sets the values of the maps, passed as parameters, that contain all the past data necessary for the cost variance analysis 
	 * @param pastCVDByBuyerMap 
	 * @param pastCVDBySiteMap 
	 * @param pastCVDByItemMap 
	 */
	private void getPastPeriodCostVarianceData(Map<Item, CostVarianceData> pastCVDByItemMap, Map<String,
			HashMap<Item, CostVarianceData>> pastCVDBySiteMap, Map<Buyer, HashMap<Item, CostVarianceData>> pastCVDByBuyerMap){
		
		for(Order o : this.pastPeriodOrderMap.values()){
			Item item = o.getItem();
			Invoice invoice = o.getInvoice();
			LocalDate purchaseDate = invoice.getInvoiceDate();
			String siteId = o.getSiteId();
			Buyer buyer = o.getBuyer();
			
			if(pastCVDByItemMap.containsKey(item) ){
				CostVarianceData cvd = pastCVDByItemMap.get(item);
				if(cvd.getLastPurchaseDate().isBefore(purchaseDate)){
					cvd.setLastPurchaseDate(purchaseDate);
					cvd.setLastPrice(invoice.getUnitPurchasePrice());
				}
				cvd.setQuantity(cvd.getQuantity() + invoice.getQuantity());
				cvd.setTotalPrice(cvd.getTotalPrice() + invoice.getTotalPurchasePrice());
			} else {
				pastCVDByItemMap.put(item, new CostVarianceData(item, invoice.getTotalPurchasePrice(),
						invoice.getQuantity(), invoice.getUnitPurchasePrice(), purchaseDate) );
			}
			
			if(pastCVDBySiteMap.containsKey(siteId)) {
				HashMap<Item, CostVarianceData> thisPastCVDByItemMap = pastCVDBySiteMap.get(siteId);
				if(thisPastCVDByItemMap.containsKey(item) ){
					CostVarianceData cvd = thisPastCVDByItemMap.get(item);
					if(cvd.getLastPurchaseDate().isBefore(purchaseDate)){
						cvd.setLastPurchaseDate(purchaseDate);
						cvd.setLastPrice(invoice.getUnitPurchasePrice());
					}
					cvd.setQuantity(cvd.getQuantity() + invoice.getQuantity());
					cvd.setTotalPrice(cvd.getTotalPrice() + invoice.getTotalPurchasePrice());
				} else {
					thisPastCVDByItemMap.put(item, new CostVarianceData(item, invoice.getTotalPurchasePrice(), 
							invoice.getQuantity(), invoice.getUnitPurchasePrice(), purchaseDate) );
				}
			} else {
				pastCVDBySiteMap.put(siteId, new HashMap<Item, CostVarianceData>());
				pastCVDBySiteMap.get(siteId).put(item, new CostVarianceData(item, invoice.getTotalPurchasePrice(),
						invoice.getQuantity(), invoice.getUnitPurchasePrice(), purchaseDate) );
			}
			
			if(pastCVDByBuyerMap.containsKey(buyer)) {
				HashMap<Item, CostVarianceData> thisPastCVDByItemMap = pastCVDByBuyerMap.get(buyer);
				if(thisPastCVDByItemMap.containsKey(item) ){
					CostVarianceData cvd = thisPastCVDByItemMap.get(item);
					if(cvd.getLastPurchaseDate().isBefore(purchaseDate)){
						cvd.setLastPurchaseDate(purchaseDate);
						cvd.setLastPrice(invoice.getUnitPurchasePrice());
					}
					cvd.setQuantity(cvd.getQuantity() + invoice.getQuantity());
					cvd.setTotalPrice(cvd.getTotalPrice() + invoice.getTotalPurchasePrice());
				} else {
					thisPastCVDByItemMap.put(item, new CostVarianceData(item, invoice.getTotalPurchasePrice(),
							invoice.getQuantity(), invoice.getUnitPurchasePrice(), purchaseDate) );
				}
			} else {
				pastCVDByBuyerMap.put(buyer, new HashMap<Item, CostVarianceData>());
				pastCVDByBuyerMap.get(buyer).put(item, new CostVarianceData(item, invoice.getTotalPurchasePrice(),
						invoice.getQuantity(), invoice.getUnitPurchasePrice(), purchaseDate) );
			}
		}
	}

	/**
	 * this method sets the values of the maps, passed as parameters, that contain all the current data necessary for the cost variance analysis 
	 * @param currentCVDByBuyerMap 
	 * @param currentCVDBySiteMap 
	 * @param currentCVDByItemMap 
	 */	
	private void getCurrentPeriodCostVarianceData(Map<Item, CostVarianceData> currentCVDByItemMap, Map<String, HashMap<Item, CostVarianceData>> currentCVDBySiteMap, Map<Buyer, HashMap<Item, CostVarianceData>> currentCVDByBuyerMap) {
		
		for(Order o : this.currentPeriodOrderMap.values()){
			Item item = o.getItem();
			Invoice invoice = o.getInvoice();
			LocalDate purchaseDate = invoice.getInvoiceDate();
			String siteId = o.getSiteId();
			Buyer buyer = o.getBuyer();
			
			if(currentCVDByItemMap.containsKey(item) ){
				CostVarianceData cvd = currentCVDByItemMap.get(item);
				if(cvd.getLastPurchaseDate().isBefore(purchaseDate)){
					cvd.setLastPurchaseDate(purchaseDate);
					cvd.setLastPrice(invoice.getUnitPurchasePrice()); 
				}
				cvd.setQuantity(cvd.getQuantity() + invoice.getQuantity());
				cvd.setTotalPrice(cvd.getTotalPrice() + invoice.getTotalPurchasePrice());
			} else {
				currentCVDByItemMap.put(item, new CostVarianceData(item, invoice.getTotalPurchasePrice(), invoice.getQuantity(), invoice.getUnitPurchasePrice(), purchaseDate) );
			}
			
			if(currentCVDBySiteMap.containsKey(siteId)) {
				HashMap<Item, CostVarianceData> thisCurrentCVDByItemMap = currentCVDBySiteMap.get(siteId);
				if(thisCurrentCVDByItemMap.containsKey(item) ){
					CostVarianceData cvd = thisCurrentCVDByItemMap.get(item);
					if(cvd.getLastPurchaseDate().isBefore(purchaseDate)){
						cvd.setLastPurchaseDate(purchaseDate);
						cvd.setLastPrice(invoice.getUnitPurchasePrice());
					}
					cvd.setQuantity(cvd.getQuantity() + invoice.getQuantity());
					cvd.setTotalPrice(cvd.getTotalPrice() + invoice.getTotalPurchasePrice());
				} else {
					thisCurrentCVDByItemMap.put(item, new CostVarianceData(item, invoice.getTotalPurchasePrice(), invoice.getQuantity(), invoice.getUnitPurchasePrice(), purchaseDate) );
				}
			} else {
				currentCVDBySiteMap.put(siteId, new HashMap<Item, CostVarianceData>());
				currentCVDBySiteMap.get(siteId).put(item, new CostVarianceData(item, invoice.getTotalPurchasePrice(), invoice.getQuantity(), invoice.getUnitPurchasePrice(), purchaseDate) );
			}
			
			if(currentCVDByBuyerMap.containsKey(buyer)) {
				HashMap<Item, CostVarianceData> thisCurrentCVDByItemMap = currentCVDByBuyerMap.get(buyer);
				if(thisCurrentCVDByItemMap.containsKey(item) ){
					CostVarianceData cvd = thisCurrentCVDByItemMap.get(item);
					if(cvd.getLastPurchaseDate().isBefore(purchaseDate)){
						cvd.setLastPurchaseDate(purchaseDate);
						cvd.setLastPrice(invoice.getUnitPurchasePrice());
					}
					cvd.setQuantity(cvd.getQuantity() + invoice.getQuantity());
					cvd.setTotalPrice(cvd.getTotalPrice() + invoice.getTotalPurchasePrice());
				} else {
					thisCurrentCVDByItemMap.put(item, new CostVarianceData(item, invoice.getTotalPurchasePrice(), invoice.getQuantity(), invoice.getUnitPurchasePrice(), purchaseDate) );
				}
			} else {
				currentCVDByBuyerMap.put(buyer, new HashMap<Item, CostVarianceData>());
				currentCVDByBuyerMap.get(buyer).put(item, new CostVarianceData(item, invoice.getTotalPurchasePrice(), invoice.getQuantity(), invoice.getUnitPurchasePrice(), purchaseDate) );
			}
		}
	}

	/** 
	 * @param currentCVDByItemMap 
	 * @param pastCVDByItemMap 
	 * @return a list containing the data of the cost variance
	 */
	private List<CostVarianceRow> computeAllCostVarianceByItemRows(Map<Item, CostVarianceData> pastCVDByItemMap, Map<Item, CostVarianceData> currentCVDByItemMap){
		List<CostVarianceRow> result = new ArrayList<CostVarianceRow>();
		
		for(Item i : this.itemMap.values() ){
			if(i.isPreviouslyPurchased() ){
				if(i.isCurrentlyPurchased() ){
				//compute Cost Variance
				CostVarianceData pastCVD = pastCVDByItemMap.get(i);
				CostVarianceData currentCVD = currentCVDByItemMap.get(i);
				double currentQuantity = currentCVD.getQuantity();
				
				double averageCostVariance = (currentCVD.getAveragePrice() - pastCVD.getAveragePrice()) * currentQuantity;
				double averagePercentageCostVariance = averageCostVariance / (pastCVD.getAveragePrice() * currentQuantity );
				double lastCostVariance = (currentCVD.getLastPrice() - pastCVD.getLastPrice()) * currentQuantity;
				double lastPercentageCostVariance = lastCostVariance / (pastCVD.getLastPrice() * currentQuantity );
				
				result.add(new CostVarianceRow(i.getItemId(), i.getItemDescription(), currentCVD.getTotalPrice(), null, null, 
						averageCostVariance, averagePercentageCostVariance , lastCostVariance , lastPercentageCostVariance) );
				} else{
				}
			}else if(i.isCurrentlyPurchased()){
			}
		}
		
		return result;
	}

		/** 
	 * @param currentCVDBySiteMap 
	 * @param pastCVDBySiteMap 
		 * @param siteWithCVRow2 
	 * @return a list containing the data of the cost variance
	 */
	private List<CostVarianceRow> computeAllCostVarianceBySiteRows(
			Map<String, HashMap<Item, CostVarianceData>> pastCVDBySiteMap, 
			Map<String, HashMap<Item, CostVarianceData>> currentCVDBySiteMap, Set<String> setSite){
		List<CostVarianceRow> result = new ArrayList<CostVarianceRow>();
		
		for(String siteId : currentCVDBySiteMap.keySet()) {
			if(pastCVDBySiteMap.containsKey(siteId)) {
				setSite.add(siteId);
				double totalBySite = 0.0;
				double repetitiveBySite = 0.0;
				double notRepetitiveBySite = 0.0;
				double averageCostVarianceBySite = 0.0;
				double pastAverageCostBySite = 0.0;
				double lastCostVarianceBySite = 0.0;
				double pastLastCostBySite = 0.0;
				
				HashMap<Item, CostVarianceData> thisCurrentCVDByItemMap = currentCVDBySiteMap.get(siteId);
				HashMap<Item, CostVarianceData> thisPastCVDByItemMap = pastCVDBySiteMap.get(siteId);
				for(Item currentItem : thisCurrentCVDByItemMap.keySet()) {
					if(thisPastCVDByItemMap.containsKey(currentItem)) {
						//CV
						CostVarianceData pastCVD = thisPastCVDByItemMap.get(currentItem);
						CostVarianceData currentCVD = thisCurrentCVDByItemMap.get(currentItem);
						double currentQuantity = currentCVD.getQuantity();
						double pastAverageCost = (pastCVD.getAveragePrice() * currentQuantity );
						double pastLastCost = (pastCVD.getLastPrice() * currentQuantity );
						
						double averageCostVariance = (currentCVD.getAveragePrice() - pastCVD.getAveragePrice()) * currentQuantity;
						double lastCostVariance = (currentCVD.getLastPrice() - pastCVD.getLastPrice()) * currentQuantity;
						
						averageCostVarianceBySite += averageCostVariance;
						pastAverageCostBySite += pastAverageCost;
						lastCostVarianceBySite += lastCostVariance;
						pastLastCostBySite += pastLastCost;
						totalBySite += currentCVD.getTotalPrice();
						repetitiveBySite += currentCVD.getTotalPrice();
					}
				}
				result.add(new CostVarianceRow(siteId, null, totalBySite, repetitiveBySite, null, averageCostVarianceBySite, (averageCostVarianceBySite / pastAverageCostBySite), lastCostVarianceBySite, (lastCostVarianceBySite / pastLastCostBySite)));
			}
		}
		
		return result;
	}
	
	private List<CostVarianceRow> computeAllCostVarianceByBuyerRows(
			Map<Buyer, HashMap<Item, CostVarianceData>> pastCVDByBuyerMap,
			Map<Buyer, HashMap<Item, CostVarianceData>> currentCVDByBuyerMap, Set<String> buyerSet) {
		List<CostVarianceRow> result = new ArrayList<CostVarianceRow>();
		
		for(Buyer buyer : currentCVDByBuyerMap.keySet()) {
			if(pastCVDByBuyerMap.containsKey(buyer)) {
				buyerSet.add(buyer.getBuyerDescription());
				double totalByBuyer = 0.0;
				double repetitiveByBuyer = 0.0;
				double notRepetitiveByBuyer = 0.0;
				double averageCostVarianceByBuyer = 0.0;
				double pastAverageCostByBuyer = 0.0;
				double lastCostVarianceByBuyer = 0.0;
				double pastLastCostByBuyer = 0.0;
				
				HashMap<Item, CostVarianceData> thisCurrentCVDByItemMap = currentCVDByBuyerMap.get(buyer);
				HashMap<Item, CostVarianceData> thisPastCVDByItemMap = pastCVDByBuyerMap.get(buyer);
				for(Item currentItem : thisCurrentCVDByItemMap.keySet()) {
					if(thisPastCVDByItemMap.containsKey(currentItem)) {
						//CV
						CostVarianceData pastCVD = thisPastCVDByItemMap.get(currentItem);
						CostVarianceData currentCVD = thisCurrentCVDByItemMap.get(currentItem);
						double currentQuantity = currentCVD.getQuantity();
						double pastAverageCost = (pastCVD.getAveragePrice() * currentQuantity );
						double pastLastCost = (pastCVD.getLastPrice() * currentQuantity );
						
						double averageCostVariance = (currentCVD.getAveragePrice() - pastCVD.getAveragePrice()) * currentQuantity;
						double lastCostVariance = (currentCVD.getLastPrice() - pastCVD.getLastPrice()) * currentQuantity;
						
						averageCostVarianceByBuyer += averageCostVariance;
						pastAverageCostByBuyer += pastAverageCost;
						lastCostVarianceByBuyer += lastCostVariance;
						pastLastCostByBuyer += pastLastCost;
						totalByBuyer += currentCVD.getTotalPrice();
						repetitiveByBuyer += currentCVD.getTotalPrice();
					}
				}
				result.add(new CostVarianceRow(buyer.getBuyerId(), buyer.getBuyerDescription(), totalByBuyer,
						repetitiveByBuyer, null, averageCostVarianceByBuyer, 
						(averageCostVarianceByBuyer / pastAverageCostByBuyer), lastCostVarianceByBuyer, 
								(lastCostVarianceByBuyer / pastLastCostByBuyer)));
			}
			
		}
		
		return result;
	}
	
	private List<CostVarianceByCommodityRow> computeAllCostVarianceByCommodityRows(Set<String> commoditySet) {
		Map<String, CostVarianceByCommodityRow> resultMap = new HashMap<String, CostVarianceByCommodityRow>();
		
		for(CostVarianceRow cvbiRow : this.costVarianceByItemRows) {
			Item item = this.itemMap.get(cvbiRow.getFieldId());
			String commodity = item.getCommodityDescription();
			if(resultMap.containsKey(commodity)) {
				//cv
				CostVarianceByCommodityRow cvbcRow = resultMap.get(commodity);
				cvbcRow.setTotal(cvbcRow.getTotal() + cvbiRow.getTotal());
				cvbcRow.setRepetitive(cvbcRow.getRepetitive() + cvbiRow.getTotal());
				//cvbcRow.setPastAverageCost(cvbcRow.getPastAverageCost() + (cvbiRow.getAverageCostVariance() / cvbiRow.getAveragePercentageCostVariance()));
				cvbcRow.setPastAverageCost(cvbcRow.getPastAverageCost() + (this.pastCVDByItemMap.get(item).getAveragePrice() * this.currentCVDByItemMap.get(item).getQuantity()));
				cvbcRow.setPastLastCost(cvbcRow.getPastLastCost() + (this.pastCVDByItemMap.get(item).getLastPrice() * this.currentCVDByItemMap.get(item).getQuantity()));
				cvbcRow.setAverageCostVariance(cvbcRow.getAverageCostVariance() + cvbiRow.getAverageCostVariance());
				cvbcRow.setAveragePercentageCostVariance(cvbcRow.getAverageCostVariance() / cvbcRow.getPastAverageCost());
				cvbcRow.setLastCostVariance(cvbcRow.getLastCostVariance() + cvbiRow.getLastCostVariance());
				cvbcRow.setLastPercentageCostVariance(cvbcRow.getLastCostVariance() / cvbcRow.getPastLastCost());
			} else {
				commoditySet.add(commodity);
				resultMap.put(commodity, new CostVarianceByCommodityRow(commodity, cvbiRow.getTotal(), cvbiRow.getTotal(), null, cvbiRow.getAverageCostVariance(), cvbiRow.getAveragePercentageCostVariance(), cvbiRow.getLastCostVariance(), cvbiRow.getLastPercentageCostVariance()));
				resultMap.get(commodity).setPastLastCost(this.pastCVDByItemMap.get(item).getLastPrice() * this.currentCVDByItemMap.get(item).getQuantity());
				resultMap.get(commodity).setPastAverageCost(this.pastCVDByItemMap.get(item).getAveragePrice() * this.currentCVDByItemMap.get(item).getQuantity());
			}
		}
		List<CostVarianceByCommodityRow> result = new ArrayList<CostVarianceByCommodityRow>(resultMap.values());
		return result;
	}
	
	public List<CostVarianceRow> getAllCostVarianceByItemRows(){
		return this.costVarianceByItemRows;
	}
	
	public List<CostVarianceRow> getRowsWith(String id){
		List<CostVarianceRow> result = new ArrayList<CostVarianceRow>();
		for(CostVarianceRow row : this.costVarianceByItemRows) {
			String fieldId = row.getFieldId();
			if(fieldId.contains(id))
				result.add(row);
		}
		return result;
	}
	
	public List<CostVarianceRow> getAllCostVarianceBySiteRows(){
		return this.costVarianceBySiteRows;
	}

	public List<CostVarianceRow> getAllCostVarianceByBuyerRows(){
		return this.costVarianceByBuyerRows;
	}

	public List<CostVarianceRow> getAllCostVarianceByCommodityRows(){
		List<CostVarianceRow> result = new ArrayList<CostVarianceRow>(this.costVarianceByCommoditiesRows);
		return result;
	}
	
	public Set<String> getAllBuyersWithCVRow(){
		return this.buyerWithCVRow;
	}
	
	public Set<String> getAllSitesWithCVRow(){
		return this.siteWithCVRow;
	}
	
	public Set<String> getAllCommoditiesWithCVRow(){
		return this.commodityWithCVRow;
	}
	
	public List<NewOrder> readFile(String nomeFile) {
		//List<Order> result = new ArrayList<Order>();
		List<NewOrder> result = new ArrayList<NewOrder>();
		try {
			FileReader fr=new FileReader(nomeFile);
			BufferedReader br=new BufferedReader(fr);
			String line;
			while((line=br.readLine())!=null) {
				try {
					String fields[]=line.split(";");
					String orderId=fields[0];
					String siteId=fields[1];
					String supplierId=fields[2];
					String invoiceId=fields[3];
					LocalDate invoiceDate = LocalDate.parse(fields[4]) ;
					int qta = Integer.parseInt(fields[5]);
					double unitPrice;
					if(fields[6].contains(","))
						unitPrice=Double.parseDouble(fields[6].replace(",", "."));
					else
						unitPrice=Double.parseDouble(fields[6]);
					double totalPrice;
					if(fields[7].contains(","))
						totalPrice=Double.parseDouble(fields[7].replace(",", "."));
					else
						totalPrice=Double.parseDouble(fields[7]);
					String itemId = fields[8];
					String buyerId=fields[9];
					/*Invoice invoice = new Invoice(invoiceId, invoiceDate, qta, unitPrice, totalPrice);
					Order order = new Order(orderId, siteId, supplierMap.get(supplierId), invoice, itemMap.get(itemId), buyerMap.get(buyerId));
					result.add(order);*/
					NewOrder newOrder = new NewOrder(orderId, siteId, supplierId, invoiceId, invoiceDate, qta, unitPrice, totalPrice, itemId, buyerId);
					result.add(newOrder);
				}catch(Exception e) {
					System.out.println("errore");
				}
			}
			fr.close();
			br.close();
		}catch(Exception e) {
			//e.printStackTrace();
			System.out.println("errore");
		}
		return result;
	}
	
	public void addNewOrders(List<NewOrder> newOrdersLists, Set<String> newItemsId) {
		for(NewOrder no : newOrdersLists) {
			Invoice invoice = new Invoice(no.getInvoiceId(), no.getInvoiceDate(), no.getQuantity(), no.getUnitPurchasePrice(), no.getTotalPurchasePrice());
			Order order = new Order(no.getOrderId(), no.getSiteId(), supplierMap.get(no.getSupplierId()), invoice, itemMap.get(no.getItemId()),buyerMap.get(no.getBuyerId()));
			dao.addInvoice(invoice);
			dao.addOrder(order);
			newItemsId.add(no.getItemId());
		}
	}

	public Map<Item, CostVarianceData> getPastCVDByItemMap() {
		return pastCVDByItemMap;
	}

	public Map<Item, CostVarianceData> getCurrentCVDByItemMap() {
		return currentCVDByItemMap;
	}
	
	public void simulate() throws NotFoundPriceException {
		
		this.init(LocalDate.of(2022, 1, 1));
		actual = dao.getAllWarehouse();
		for(String s : this.bom.keySet()) {
			if(!probabilities.containsKey(s))
				probabilities.put(s, new Probability(0, 0));
			if(!trendsByItem.containsKey(s)) {
				List<PriceTrend> list = new ArrayList<PriceTrend>();
				if(this.currentCVDByItemMap.containsKey(itemMap.get(s))) {
					list.add(new PriceTrend(startDate, currentCVDByItemMap.get(itemMap.get(s)).getLastPrice()));
					trendsByItem.put(s, list);
				}else if(this.pastCVDByItemMap.containsKey(itemMap.get(s))) {
					list.add(new PriceTrend(startDate, pastCVDByItemMap.get(itemMap.get(s)).getLastPrice()));
					trendsByItem.put(s, list);
				}else {
					boolean notFound = true;
					for(SimulatorItem si : this.actual) {
						if(si.getItemId().equals(s)) {
							list.add(new PriceTrend(startDate, si.getPurchasePrice()));
							trendsByItem.put(s, list);
							notFound = false;
						}
					}
					if(notFound) {
						this.simulator = new Simulator();
						actual = new ArrayList<SimulatorItem>();
						bom = new HashMap<String, Integer>();
						probabilities = new HashMap<String, Probability>();
						trendsByItem = new HashMap<String, List<PriceTrend>>();
						throw new NotFoundPriceException(s);
					}
				}
			}else {
				List<PriceTrend> list = trendsByItem.get(s);
				boolean trovato = false;
				for(PriceTrend pt : list) {
					if(pt.getDate().isEqual(startDate)) {
						trovato = true;
						break;
					}
				}
				if(!trovato) {
					if(this.currentCVDByItemMap.containsKey(itemMap.get(s))) {
						list.add(0, new PriceTrend(startDate, currentCVDByItemMap.get(itemMap.get(s)).getLastPrice()));
						trendsByItem.put(s, list);
					}else if(this.pastCVDByItemMap.containsKey(itemMap.get(s))) {
						list.add(0, new PriceTrend(startDate, pastCVDByItemMap.get(itemMap.get(s)).getLastPrice()));
						trendsByItem.put(s, list);
					}else {
						boolean notFound = true;
						for(SimulatorItem si : this.actual) {
							if(si.getItemId().equals(s)) {
								list.add(0, new PriceTrend(startDate, si.getPurchasePrice()));
								trendsByItem.put(s, list);
								notFound = false;
							}
						}
						if(notFound) {
							this.simulator = new Simulator();
							actual = new ArrayList<SimulatorItem>();
							bom = new HashMap<String, Integer>();
							probabilities = new HashMap<String, Probability>();
							trendsByItem = new HashMap<String, List<PriceTrend>>();
							throw new NotFoundPriceException(s);
						}
					}
				}
			}
		}
		
		Map<String, Probability> probabilitiesToInput = new HashMap<String, Probability>();
		for(String key : this.probabilities.keySet()) {
			Probability value = this.probabilities.get(key);
			probabilitiesToInput.put(key, new Probability(value.getDelayProbability(), value.getAnnulmentProbability()));
		}
		simulator.init(actual, bom, startDate, probabilitiesToInput, trendsByItem,this.dao.getLeadTimeByItemId());
		simulator.run();
	}

	public boolean handleBomFile() {
		try {
			FileReader fr=new FileReader("bomFile.csv");
			BufferedReader br=new BufferedReader(fr);
			String line;
			while((line=br.readLine())!=null) {
				String firstLine ="﻿itemId;quantity";
				if(!line.equals(firstLine)) {
					try {
						String fields[] = line.split(";");
						String itemId = fields[0];
						int quantity = Integer.parseInt(fields[1]);
						bom.put(itemId, quantity);
					}catch(Exception e) {
						System.out.println("File reading error");
						fr.close();
						br.close();
						return false;
					}
				}
			}
			fr.close();
			br.close();
		}catch(Exception e) {
			System.out.println("File reading error");
		}
		return true;
		
	}

	

	public boolean handleProbabilityFile() {
		try {
			FileReader fr=new FileReader("probabilityFile.csv");
			BufferedReader br=new BufferedReader(fr);
			String line;
			while((line=br.readLine())!=null) {
				String firstLine = "﻿itemId;P(delay);P(annulment)";
				if(!line.equals(firstLine)) {
					try {
						String fields[] = line.split(";");
						String itemId = fields[0];
						double delay = Double.parseDouble(fields[1]);
						double annulment = Double.parseDouble(fields[2]);
						Probability probability = new Probability(delay, annulment);
			    		probabilities.put(itemId, probability);
					}catch(Exception e) {
						System.out.println("File reading error");
						br.close();
						return false;
					}
				}
			}
			fr.close();
			br.close();
		}catch(Exception e) {
			System.out.println("File reading error");
		}
		return true;
	}

	public boolean handleTrendFile() {
		try {
			FileReader fr=new FileReader("trendFile.csv");
			BufferedReader br=new BufferedReader(fr);
			String line;
			while((line=br.readLine())!=null) {
				String firstLine = "﻿Item;Date;Value";
				if(!line.equals(firstLine)) {
					try {
						String fields[] = line.split(";");
						String itemId = fields[0];
						LocalDate date;
						try {
			    			date = LocalDate.parse(fields[1]);
			    		}catch (Exception e) {
			    			br.close();
			    			return false;
			    		}
						double value = Double.parseDouble(fields[2]);
						if(trendsByItem.containsKey(itemId)) {
							List<PriceTrend> pricetrendList = this.trendsByItem.get(itemId);
							for(PriceTrend pt : pricetrendList) {
								LocalDate datePt = pt.getDate();
								double price = pt.getPrice();
								if(!datePt.isAfter(date)) {
									double newPrice = price * (1.0 + value);
									pt.setPrice(newPrice);
								}
							}
						}
					}catch(Exception e) {
						System.out.println("File reading error");
						br.close();
						return false;
					}
				}
				
			}
			fr.close();
			br.close();
		}catch(Exception e) {
			System.out.println("File reading error");
		}
		return true;
	}

	public void enterInBom(String itemId, int quantity) {
		bom.put(itemId, quantity);
		
	}

	
	public void addInProbabilities(String itemId, double delay, double annulment) {
		Probability probability = new Probability(delay, annulment);
		probabilities.put(itemId, probability);
		
	}
	
	public void addAllInProbabilities(double delay, double annulment) {
		Probability probability = new Probability(delay, annulment);
		Set<String> items = bom.keySet();
		for(String item : items)
			probabilities.put(item, probability);
	}
	
	public void addAllTrend(LocalDate date, double value) {
		Set<String> items = trendsByItem.keySet();
		for(String item : items) {
			List<PriceTrend> pricetrendList = this.trendsByItem.get(item);
			for(PriceTrend pt : pricetrendList) {
				LocalDate datePt = pt.getDate();
				double price = pt.getPrice();
				if(!datePt.isAfter(date)) {
					double newPrice = price * (1.0 + value);
					pt.setPrice(newPrice);
				}
			}
		}
	}

	public void enterStartDate(int year, int month, int day) {
		this.startDate = LocalDate.of(year, month, day);
		
	}

	public void addTrend(LocalDate date, double value, String itemId) {
		if(trendsByItem.containsKey(itemId)) {
			List<PriceTrend> pricetrendList = this.trendsByItem.get(itemId);
			for(PriceTrend pt : pricetrendList) {
				LocalDate datePt = pt.getDate();
				double price = pt.getPrice();
				if(!datePt.isAfter(date)) {
					double newPrice = price * (1.0 + value);
					pt.setPrice(newPrice);
				}
			}
		}
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	
	public List<SimulatorRow> getSimulatorRowList(){
		return simulator.getSimulatoRowList();
	}

	public String getSimulatorTotalPrice() {
		return simulator.getTotalPrice() + "€";
	}

	public void reset() {
		this.simulator = new Simulator();
		actual = new ArrayList<SimulatorItem>();
		bom = new HashMap<String, Integer>();
		probabilities = new HashMap<String, Probability>();
		trendsByItem = new HashMap<String, List<PriceTrend>>();
		startDate = null;
	}

	public String getSimulatorSaleDate() {
		return simulator.getSaleDate().toString();
	}

	public List<String> getAllBom() {
		return dao.getAllBom();
	}

	public void handleBomDb(String item) {
		bom = this.dao.getBomMap(item);			
	}
}
