package it.polito.tdp.acquisti.model;

public class Supplier {
	
	private String supplierId;
	private String supplierDescription;
	
	public Supplier(String supplierId, String supplierDescription) {
		super();
		this.supplierId = supplierId;
		this.supplierDescription = supplierDescription;
	}
	
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierDescription() {
		return supplierDescription;
	}
	public void setSupplierDescription(String supplierDescription) {
		this.supplierDescription = supplierDescription;
	}
	
	@Override
	public String toString() {
		return this.supplierDescription;
	}
}
