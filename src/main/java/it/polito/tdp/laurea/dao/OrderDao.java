package it.polito.tdp.laurea.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.laurea.model.Buyer;
import it.polito.tdp.laurea.model.Invoice;
import it.polito.tdp.laurea.model.Item;
import it.polito.tdp.laurea.model.Order;
import it.polito.tdp.laurea.model.PriceTrend;
import it.polito.tdp.laurea.model.SimulatorItem;
import it.polito.tdp.laurea.model.Supplier;
import it.polito.tdp.laurea.model.SimulatorItem.State;


public class OrderDao {

	public void getAllBuyers(Map<String, Buyer> buyerMap) {
		String sql = "SELECT * "
				   + "FROM buyer";
		//buyerMap = new HashMap<String, Buyer>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String buyerId = res.getString("id");
				String buyerDescription = res.getString("value");
				
				Buyer n = new Buyer(buyerId, buyerDescription);
				buyerMap.put(buyerId, n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void getAllSuppliers(Map<String, Supplier> supplierMap) {
		String sql = "SELECT * "
				   + "FROM supplier";
		//supplierMap = new HashMap<String, Supplier>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String supplierId = res.getString("id");
				String supplierDescription = res.getString("value");
				
				Supplier n = new Supplier(supplierId, supplierDescription);
				supplierMap.put(supplierId, n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void getAllItems(Map<String, Item> itemMap, Set<String> commoditySet) {
		String sql = "SELECT id, value, commodity "
				   + "FROM item";
		//itemMap = new HashMap<String, Item>();
		//commoditySet = new HashSet<String>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String itemId = res.getString("id");
				String itemDescription = res.getString("value");
				String commodityDescription = res.getString("commodity");
				
				Item n = new Item(itemId, itemDescription, commodityDescription);
				itemMap.put(itemId, n);
				commoditySet.add(commodityDescription);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void getAllInvoices(Map<String, Invoice> invoiceMap) {
		String sql = "SELECT * "
				   + "FROM invoice";
		//invoiceMap = new HashMap<String, Invoice>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String invoiceId = res.getString("id");
				LocalDate invoiceDate = res.getDate("date").toLocalDate();
				int quantity = res.getInt("quantity");
				double unitPurchasePrice = res.getDouble("unitPurchasePrice");
				double totalPurchasePrice = res.getDouble("totalPurchasePrice");
				
				Invoice n = new Invoice(invoiceId, invoiceDate, quantity, unitPurchasePrice, totalPurchasePrice);
				invoiceMap.put(invoiceId, n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void getAllOrders(Map<String, Order> orderMap, Map<String, Buyer> buyerMap, Map<String, Supplier> supplierMap, Map<String, Item> itemMap, Map<String, Invoice> invoiceMap) {
		String sql = "SELECT * "
				   + "FROM `order`";
		//orderMap = new HashMap<String, Order>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String orderId = res.getString("id");
				String siteId = res.getString("site");
				Supplier supplier = supplierMap.get(res.getString("supplier"));
				Invoice invoice = invoiceMap.get(res.getString("invoice"));
				Item item = itemMap.get(res.getString("item"));
				Buyer buyer = buyerMap.get(res.getString("buyer"));
				
				Order n = new Order(orderId, siteId, supplier, invoice, item, buyer);
				orderMap.put(orderId, n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void addInvoice(Invoice invoice) {
		String sql = "INSERT INTO invoice "
				+ "VALUES (?,?,?,?,?)";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, invoice.getInvoiceId());
			st.setDate(2, Date.valueOf(invoice.getInvoiceDate()));
			st.setInt(3, invoice.getQuantity());
			st.setDouble(4, invoice.getUnitPurchasePrice());
			st.setDouble(5, invoice.getTotalPurchasePrice());
			st.executeQuery();
			
			st.close();
			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void addOrder(Order order) {
		String sql = "INSERT INTO `order` "
				   + "VALUES (?,?,?,?,?,?)";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, order.getOrderId());
			st.setString(2, order.getSiteId());
			st.setString(3, order.getSupplier().getSupplierId());
			st.setString(4, order.getInvoice().getInvoiceId());
			st.setString(5, order.getItem().getItemId());
			st.setString(6, order.getBuyer().getBuyerId());
			st.executeQuery();

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	public List<String> getAllBom() {
		String sql = "SELECT DISTINCT itemId "
				   + "FROM bom";
		//buyerMap = new HashMap<String, Buyer>();
		List<String> result = new ArrayList<String>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String itemId = res.getString("itemId");
				result.add(itemId);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<SimulatorItem> getAllWarehouse() {
		String sql = "SELECT * "
				+ "FROM warehouse";
		List<SimulatorItem> result = new ArrayList<SimulatorItem>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String itemId = res.getString("itemId");
				LocalDate date = res.getDate("arrivalDate").toLocalDate();
				int quantity = res.getInt("quantity");
				double price = res.getDouble("price");
				SimulatorItem si = new SimulatorItem(State.WAREHOUSE_ARRIVAL, date,-1 , price, itemId, quantity);
				result.add(si);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, Integer> getBomMap(String item) {
		String sql = "SELECT componentId, quantity "
				+ "FROM bom "
				+ "WHERE itemId =?";
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, item);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String itemId = res.getString("componentId");
				int quantity = res.getInt("quantity");
				result.put(itemId, quantity);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void getAllTrends(Map<String, List<PriceTrend>> trendsByItem) {
		String sql = "SELECT * "
				+ "FROM trend ";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String itemId = res.getString("itemId");
				LocalDate date = res.getDate("date").toLocalDate();
				double value = res.getDouble("price");
				PriceTrend priceTrend = new PriceTrend(date, value);
				List<PriceTrend> pricetrendList;
				if(!trendsByItem.containsKey(itemId)) {
					pricetrendList = new ArrayList<PriceTrend>();
					trendsByItem.put(itemId, pricetrendList);
				}
				else
					pricetrendList = trendsByItem.get(itemId);
				pricetrendList.add(priceTrend);
			}

			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, Period> getLeadTimeByItemId() {
		String sql = "SELECT id,leadTime "
				+ "FROM item";
		Map<String, Period> result = new HashMap<String, Period>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String itemId = res.getString("id");
				int leadTime = res.getInt("leadTime");
				result.put(itemId, Period.ofDays(leadTime));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}