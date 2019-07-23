package com.erikhakonolson.apps.utils;


import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SatelliteServiceDiscoveryUtility {

	private static final String JAMESLONGURL = "http://uplink.jameslong.name/channels.html";

	private ArrayList<DiscoveredSatelliteService> discoveredServiceArrayList;
	private ArrayList<DiscoveredSatelliteService> discoveredSDServiceArrayList;
	private ArrayList<DiscoveredSatelliteService> discoveredHDServiceArrayList;
	private ArrayList<DiscoveredSatelliteService> discoveredHDServiceEncryptOnlyArrayList;
	private HashMap<Integer, ArrayList<DiscoveredSatelliteService>> serviceIdMapToServiceArrayList;
	private ArrayList<String> orbitalStringArrayList = new ArrayList<String>();
	
	
	public SatelliteServiceDiscoveryUtility() throws Exception {
		
		discoveredServiceArrayList = 
				new ArrayList<DiscoveredSatelliteService>();
		
		discoveredSDServiceArrayList = 
				new ArrayList<DiscoveredSatelliteService>();
		
		discoveredHDServiceArrayList = 
				new ArrayList<DiscoveredSatelliteService>();
		
		discoveredHDServiceEncryptOnlyArrayList = 
				new ArrayList<DiscoveredSatelliteService>();
		
		serviceIdMapToServiceArrayList = 
				new HashMap<Integer, ArrayList<DiscoveredSatelliteService>>();
		
		orbitalStringArrayList = new ArrayList<String>();
		
		
		System.out.println("");
		System.out.println("Initiating satellite service discovery!");
		
		parseJamesLong(JAMESLONGURL);
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println( "Satellite Service Discovery complete!" );
		System.out.println("");
		System.out.println("Total number of discovered services: " + discoveredServiceArrayList.size());
		System.out.println("Number of discovered SD services: " + discoveredSDServiceArrayList.size());
		System.out.println("Number of discovered HD services: " + discoveredHDServiceArrayList.size());
		System.out.println("Number of entries in Service ID to Service hashmap: " + serviceIdMapToServiceArrayList.size());
		System.out.println("");
	}
	

	public void parseJamesLong(String url) throws Exception {
		
		System.out.println("");
		System.out.println("Parsing service content on: " + url);
		
		Document doc = Jsoup.connect(url).get();
		Elements tables = doc.body().getElementsByTag("table");
		
		//There is a table at the beginning of the doc that is irrelevant 
		//so skip forward to location "1"
		Element table = tables.get(1);
		
		Elements rows = table.getElementsByTag("tr");
		
		String[] rowContent = new String[17];
		
		for (int i = 1; i < rows.size(); i++) {
			Element row = rows.get(i);
			rowContent = parseJamesLongRow(row);
			parseServiceFields(rowContent);	
		}

	}


	private String[] parseJamesLongRow(Element row) throws Exception {
		
		//Each column in the row is labeled by the <td> tag
		Elements columns = row.getElementsByTag("td");
		
		String trContentList[] = new String[17];
		
		int currentColumn = 0;
		
		//Iterate through the contents of each row
		for (Element column : columns) {
			
			//Some rows contain a "colspan" element that spans multiple columns
			//We must adjust our count by its size if one is present
			if (column.attr("colspan") != null &&
					column.attr("colspan").length() > 0) {
				currentColumn += Integer.parseInt(column.attr("colspan")) - 1;
				
				//Don't shift out of usable territory
				//One less than length for 0 reference and one less than length for currentColumn++ later
				if (currentColumn > trContentList.length - 2) currentColumn = trContentList.length - 2; 
			}
			
			String currentContent = null;
			
			switch(currentColumn) {
				case 0:
				case 1:
				case 2:
				case 3:
				
				case 6:
				case 10:
				case 14:
				
				case 16:
					currentContent = column.text();
					break;
				
				case 4:
				case 8:
				case 12:
	
					if (column.html().contains("hd") 
							&& !column.html().contains("hd3")) {
						currentContent = "HD";
					} else if (column.html().contains("sd")) {
						currentContent = "SD";
					} else if (column.html().contains("vod")){
						currentContent = "VOD";
					} else if (column.html().contains("aud")){
						currentContent = "AUD"; 
					} else if (column.html().contains("itv")){
						currentContent = "ITV";
					} else {
						currentContent = "UNKNWN";
					}
	
					//TODO - may not need these additional flags since we now use 
					//the suid to check if the service is valid
					
					//"HR" services seem to not be working so we need to track this
					if (column.html().contains("HR")) {
						currentContent += " HR";
					}
					
					//HD services with the "O" modifier seem to not be working so we need to track this
					if (column.html().contains("O")) {
						currentContent += " O";
					}
					
					//The 'D' on the HD ones can only be encrypted
					if (column.html().contains(" D") && currentContent.contains("HD")) {
						currentContent += " ENCRYPTONLY";
					}
					
					break;
					
				case 5:
				case 9:
				case 13:
					//s = s.replaceAll("[^\\x00-\\x7F]", ""); //replaces all unicode 'black diamond questionmarks'
					currentContent = column.text().replaceAll("[^\\x00-\\x7F]", "");
					if (currentContent == null || currentContent.isEmpty()) {
						currentContent = "-";
					}
					break;
					
				case 7:
				case 11:
				case 15:
					currentContent = "";
					break;
			}
			
			trContentList[currentColumn] = currentContent;
				
			currentColumn++;
		}
		
		return trContentList;
	}

	
	private void parseServiceFields(String[] rowContent) throws Exception {
		
		int serviceId = Integer.MAX_VALUE;
		
		//The service ID may be in row 0 or row 3 so try both
		try {
			serviceId = Integer.parseInt(rowContent[0]);
		} catch (NumberFormatException e) {
			try {
				serviceId = Integer.parseInt(rowContent[3]);
			} catch (NumberFormatException f) {
				return;
			}
		}
		
		String shortName = rowContent[1];
		String fullName = rowContent[2];
		
		for (int orbitalOffset = 0; orbitalOffset < 3; orbitalOffset++) {
			
			int baseOffset = 4 + (orbitalOffset * 4);
			
			String formatType = rowContent[baseOffset];
			

			if (formatType == null 
					|| formatType.isEmpty()) {
				continue;
			}
			
			if (!formatType.contains("HD")
					&& !formatType.contains("SD")) {
				continue;
			}
			
			if (formatType.contains("HR")) {
				System.err.println("Caught a bad service... contains 'HR'...");
				continue;
			}
			
			if (formatType.contains("HD")
					&& formatType.contains(" O")) {
				System.err.println("Caught a bad service... HD service contains 'O'...");
				continue;
			}
			
			if (formatType.contains("SD")
					&& formatType.contains(" O")) {
				formatType = "SD"; //remove flag because breaking new DiscoveredService()
			}
			
			if (rowContent[0].contains("-")) {
				System.err.println("Caught a service with a '-' in the number... not adding...");
				continue;
			}
			
			if (rowContent[2].contains("Commercial Acct HD")) {
				System.err.println("Caught a 'Commercial Acct HD' service... not adding...");
				continue;
			}
			
			String orbital = rowContent[baseOffset + 1];
			String transponder = rowContent[baseOffset + 2];
			
			DiscoveredSatelliteService service = new DiscoveredSatelliteService(serviceId,
					shortName, 
					fullName,
					formatType,
					orbital,
					transponder);
			
			discoveredServiceArrayList.add(service);
			System.out.println("Adding Service to our list: " + service.getServiceId()
					+ " " + service.getFullName());
			
			if (formatType.contains("HD")) {
				if (formatType.contains("ENCRYPTONLY")) {
					service.setFormatType("HD");
					discoveredHDServiceEncryptOnlyArrayList.add(service);
				}
				discoveredHDServiceArrayList.add(service);
			}
			
			if (formatType.contains("SD")) {
				discoveredSDServiceArrayList.add(service);
			}
			
			ArrayList<DiscoveredSatelliteService> dsList = new ArrayList<DiscoveredSatelliteService>();
			if (serviceIdMapToServiceArrayList.get(service.getServiceId()) != null) {
				dsList = serviceIdMapToServiceArrayList.get(service.getServiceId());
			}
			dsList.add(service);
			serviceIdMapToServiceArrayList.put(service.getServiceId(), dsList);
			
		}
		
	}


	public ArrayList<DiscoveredSatelliteService> getDiscoveredServiceArrayList() {
		return discoveredServiceArrayList;
	}


	public ArrayList<DiscoveredSatelliteService> getDiscoveredSDServiceArrayList() {
		return discoveredSDServiceArrayList;
	}


	public ArrayList<DiscoveredSatelliteService> getDiscoveredHDServiceArrayList() {
		return discoveredHDServiceArrayList;
	}


	public ArrayList<DiscoveredSatelliteService> getDiscoveredHDServiceEncryptOnlyArrayList() {
		return discoveredHDServiceEncryptOnlyArrayList;
	}


	public HashMap<Integer, ArrayList<DiscoveredSatelliteService>> getServiceIdMapToServiceArrayList() {
		return serviceIdMapToServiceArrayList;
	}


	public ArrayList<String> getOrbitalStringArrayList() {
		return orbitalStringArrayList;
	}
	
}