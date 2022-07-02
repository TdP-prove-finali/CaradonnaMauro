package it.polito.tdp.acquisti.model;

public class NotFoundPriceException extends Exception {

	String notFound;
	public NotFoundPriceException(String s) {
		this.notFound = s;
	}
	public String getNotFound() {
		return notFound;
	}
	

}
