package com.wz.modules.kk.entity;

public class LineUpper {

	private String line;

	private String lineName;

	private String electricityLimit;

	private String powerUpper;

	private String deviceBoxMac;

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getElectricityLimit() {
		return electricityLimit;
	}

	public void setElectricityLimit(String electricityLimit) {
		this.electricityLimit = electricityLimit;
	}

	public String getPowerUpper() {
		return powerUpper;
	}

	public void setPowerUpper(String powerUpper) {
		this.powerUpper = powerUpper;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getDeviceBoxMac() {
		return deviceBoxMac;
	}

	public void setDeviceBoxMac(String deviceBoxMac) {
		this.deviceBoxMac = deviceBoxMac;
	}
	
	public static void main(String args[]){
		String str = "JW201801SE000017";
		
		System.out.println(Integer.parseInt(str.substring(10)) );
	}

}