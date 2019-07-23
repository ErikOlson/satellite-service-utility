package com.erikhakonolson.apps.utils;

public class DiscoveredSatelliteService {
	private int serviceId;
	private String shortName;
	private String fullName;
	private DishServiceFormat formatType; 
	private DishOrbital orbital;
	private String transponder;

	
	public DiscoveredSatelliteService(int serviceId,
			String shortName, 
			String fullName,
			String formatType,
			String orbital,
			String transponder) throws Exception {
		
		this.serviceId = serviceId;
		this.shortName = shortName;
		this.fullName = fullName;

		setFormatType(formatType);

		//Some output with decimal was just outputing as the whole number 
		//so added extra cases (ie. "118" also for "118.7")
		switch (orbital) {
			case "61.5":
			case "61":
				this.orbital = DishOrbital.EAST_61_5;
				break;
			case "72.7":
			case "72":
				this.orbital = DishOrbital.EAST_72_7;
				break;
			case "77":
				this.orbital = DishOrbital.EAST_77;
				break;
			case "110":
				this.orbital = DishOrbital.WEST_110;
				break;
			case "118.7":
			case "118":
				this.orbital = DishOrbital.WEST_118_7;
				break;
			case "119":
				this.orbital = DishOrbital.WEST_119;
				break;
			case "129":
				this.orbital = DishOrbital.WEST_129;
				break;
			default:
				//throw new Exception("Unknown orbital string: " + orbital);
				break;
		}
		
		this.transponder = transponder;
	}

	
	public void setFormatType(String formatType) throws Exception {
		
		switch (formatType) {
			case "HD ENCRYPTONLY":
				break;
			case "HD":
				this.formatType = DishServiceFormat.HD;
				break;
			case "SD":
				this.formatType = DishServiceFormat.SD;
				break;
			case "VOD":
				this.formatType = DishServiceFormat.VOD;
				break;
			case "ITV":
				this.formatType = DishServiceFormat.ITV;
				break;
			case "AUD":
				this.formatType = DishServiceFormat.AUD;
				break;
			case "UNKNWN":
				this.formatType = DishServiceFormat.UNKNWN;
				break;
			default:
				throw new Exception("Unknown service format string: " + formatType);
		}
		
	}
	
	
	public String toString() {
		return fullName + " - "	+ shortName + "\r\n"
				+ "Service ID: " + serviceId + "\r\n"
				+ "Format: " + formatType + "\r\n"
				+ "Orbital: " + orbital.toString() + "\r\n"
				+ "Transponder: " + transponder;
	}
	
	public int getServiceId() {
		return serviceId;
	}

	public String getShortName() {
		return shortName;
	}

	public String getFullName() {
		return fullName;
	}

	public DishServiceFormat getFormatType() {
		return formatType;
	}

	public DishOrbital getOrbital() {
		return orbital;
	}

	public String getTransponder() {
		return transponder;
	}

}
