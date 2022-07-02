package it.polito.tdp.acquisti.model;

public class CostVarianceRow {
	
	private String fieldId;
	private String fieldDescription;
	private double total;
	private Double repetitive;
	private Double notRepetitive;
	private Double averageCostVariance;
	private Double averagePercentageCostVariance;
	private Double lastCostVariance;
	private Double lastPercentageCostVariance;
	
	public CostVarianceRow(String fieldId, String fieldDescription, double total, Double repetitive, Double notRepetitive,
			Double averageCostVariance, Double averagePercentageCostVariance, Double lastCostVariance,
			Double lastPercentageCostVariance) {
		super();
		this.fieldId = fieldId;
		this.fieldDescription = fieldDescription;
		this.total = total;
		this.repetitive = repetitive;
		this.notRepetitive = notRepetitive;
		this.averageCostVariance = averageCostVariance;
		this.averagePercentageCostVariance = averagePercentageCostVariance;
		this.lastCostVariance = lastCostVariance;
		this.lastPercentageCostVariance = lastPercentageCostVariance;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldDescription() {
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}

	public double getTotalToOutput() {
		return Math.round((total)/10.0)/100.0;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Double getRepetitiveToOutput() {
		if(repetitive != null)
			return Math.round((repetitive)/10.0)/100.0;
		return null;
	}

	public void setRepetitive(Double repetitive) {
		this.repetitive = repetitive;
	}

	public Double getNotRepetitiveToOutput() {
		if(notRepetitive != null)
			return Math.round((notRepetitive)/10.0)/100.0;
		return null;
	}

	public void setNotRepetitive(Double notRepetitive) {
		this.notRepetitive = notRepetitive;
	}

	public Double getAverageCostVarianceToOutput() {
		return Math.round((averageCostVariance)/10.0)/100.0;
	}

	public void setAverageCostVariance(Double averageCostVariance) {
		this.averageCostVariance = averageCostVariance;
	}

	public Double getAveragePercentageCostVarianceToOutput() {
		return Math.round((averagePercentageCostVariance)*10000.0)/100.0;
	}

	public void setAveragePercentageCostVariance(Double averagePercentageCostVariance) {
		this.averagePercentageCostVariance = averagePercentageCostVariance;
	}

	public Double getLastCostVarianceToOutput() {
		return Math.round((lastCostVariance)/10.0)/100.0;
	}

	public void setLastCostVariance(Double lastCostVariance) {
		this.lastCostVariance = lastCostVariance;
	}

	public Double getLastPercentageCostVarianceToOutput() {
		return Math.round((lastPercentageCostVariance)*10000.0)/100.0;
	}

	public void setLastPercentageCostVariance(Double lastPercentageCostVariance) {
		this.lastPercentageCostVariance = lastPercentageCostVariance;
	}

	public double getTotal() {
		return total;
	}

	public Double getRepetitive() {
		return repetitive;
	}

	public Double getNotRepetitive() {
		return notRepetitive;
	}

	public Double getAverageCostVariance() {
		return averageCostVariance;
	}

	public Double getAveragePercentageCostVariance() {
		return averagePercentageCostVariance;
	}

	public Double getLastCostVariance() {
		return lastCostVariance;
	}

	public Double getLastPercentageCostVariance() {
		return lastPercentageCostVariance;
	}

	
}
