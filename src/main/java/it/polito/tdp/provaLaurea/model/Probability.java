package it.polito.tdp.provaLaurea.model;

public class Probability {
	
	private double delayProbability;
	private double annulmentProbability;
	
	public Probability(double delayProbability, double annulmentProbability) {
		super();
		this.delayProbability = delayProbability;
		this.annulmentProbability = annulmentProbability;
	}
	
	public double getDelayProbability() {
		return delayProbability;
	}
	public void setDelayProbability(double delayProbability) {
		this.delayProbability = delayProbability;
	}
	public double getAnnulmentProbability() {
		return annulmentProbability;
	}
	public void setAnnulmentProbability(double annulmentProbability) {
		this.annulmentProbability = annulmentProbability;
	}
	
	

}
