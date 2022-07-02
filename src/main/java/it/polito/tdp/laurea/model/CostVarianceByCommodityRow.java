package it.polito.tdp.laurea.model;

public class CostVarianceByCommodityRow extends CostVarianceRow{
	
	private Double pastAverageCost;
	private Double pastLastCost;
	
	public CostVarianceByCommodityRow(String commodityDescription, double total, Double repetitive,
			Double notRepetitive, Double averageCostVariance, Double averagePercentageCostVariance,
			Double lastCostVariance, Double lastPercentageCostVariance) {
		super(commodityDescription,commodityDescription,total,repetitive,notRepetitive,averageCostVariance,averagePercentageCostVariance,lastCostVariance,lastPercentageCostVariance);
		this.pastAverageCost = (averageCostVariance / averagePercentageCostVariance);
		this.pastLastCost = (lastCostVariance / lastPercentageCostVariance);
	}

	public String getCommodityDescription() {
		return super.getFieldDescription();
	}

	public void setCommodityDescription(String commodityDescription) {
		super.setFieldDescription(commodityDescription);
	}

	public double getTotal() {
		return super.getTotal();
	}

	public void setTotal(double total) {
		super.setTotal(total);
	}

	public Double getRepetitive() {
		return super.getRepetitive();
	}

	public void setRepetitive(Double repetitive) {
		super.setRepetitive(repetitive);
	}

	public Double getNotRepetitive() {
		return super.getNotRepetitive();
	}

	public void setNotRepetitive(Double notRepetitive) {
		super.setNotRepetitive(notRepetitive);
	}

	public Double getAverageCostVariance() {
		return super.getAverageCostVariance();
	}

	public void setAverageCostVariance(Double averageCostVariance) {
		super.setAverageCostVariance(averageCostVariance);
	}

	public Double getAveragePercentageCostVariance() {
		return super.getAveragePercentageCostVariance();
	}

	public void setAveragePercentageCostVariance(Double averagePercentageCostVariance) {
		super.setAveragePercentageCostVariance(averagePercentageCostVariance);
	}

	public Double getLastCostVariance() {
		return super.getLastCostVariance();
	}

	public void setLastCostVariance(Double lastCostVariance) {
		super.setLastCostVariance(lastCostVariance);
	}

	public Double getLastPercentageCostVariance() {
		return super.getLastPercentageCostVariance();
	}

	public void setLastPercentageCostVariance(Double lastPercentageCostVariance) {
		super.setLastPercentageCostVariance(lastPercentageCostVariance);
	}

	public Double getPastAverageCost() {
		return pastAverageCost;
	}

	public void setPastAverageCost(Double pastAverageCost) {
		this.pastAverageCost = pastAverageCost;
	}

	public Double getPastLastCost() {
		return pastLastCost;
	}

	public void setPastLastCost(Double pastLastCost) {
		this.pastLastCost = pastLastCost;
	}
	
	

}
