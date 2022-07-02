package it.polito.tdp.laurea.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.laurea.model.Buyer;
import it.polito.tdp.laurea.model.CostVarianceRow;
import it.polito.tdp.laurea.model.Invoice;
import it.polito.tdp.laurea.model.Item;
import it.polito.tdp.laurea.model.Model;
import it.polito.tdp.laurea.model.Order;
import it.polito.tdp.laurea.model.Supplier;

public class TestDao {
	
public static void main(String[] args) {
		
		OrderDao dao = new OrderDao();
		Set<String> s = new HashSet<String>();
		Map<String, Order> orderMap = new HashMap<String, Order>();
		Map<String, Buyer> buyerMap = new HashMap<String, Buyer>();
		Map<String, Supplier> supplierMap = new HashMap<String, Supplier>();
		Map<String, Item> itemMap = new HashMap<String, Item>();
		Map<String, Invoice> invoiceMap = new HashMap<String, Invoice>();
		dao.getAllBuyers(buyerMap);
		dao.getAllInvoices(invoiceMap);
		dao.getAllItems(itemMap, s);
		dao.getAllSuppliers(supplierMap);
		dao.getAllOrders(orderMap, buyerMap, supplierMap, itemMap, invoiceMap);
		
		for(Order ss : orderMap.values())
			System.out.println(ss.getOrderId());
	}

}
