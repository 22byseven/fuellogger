package com.parousia.fuellogger.model;

public class FuelEntry {

	long id;
	String fuelDateTime;
	long odometer;
	Double fuelPrice;
	Double fuelAmount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFuelDateTime() {
		return fuelDateTime;
	}

	public void setFuelDateTime(String fuelDateTime) {
		this.fuelDateTime = fuelDateTime;
	}

	public long getOdometer() {
		return odometer;
	}

	public void setOdometer(long odometer) {
		this.odometer = odometer;
	}

	public Double getFuelPrice() {
		return fuelPrice;
	}

	public void setFuelPrice(Double fuelPrice) {
		this.fuelPrice = fuelPrice;
	}

	public Double getFuelAmount() {
		return fuelAmount;
	}

	public void setFuelAmount(Double fuelAmount) {
		this.fuelAmount = fuelAmount;
	}

}
