package com.erikhakonolson.apps;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.erikhakonolson.apps.utils.DiscoveredSatelliteService;
import com.erikhakonolson.apps.utils.SatelliteServiceDiscoveryUtility;

public class SatelliteServiceDiscoveryDemo {

	
	public void runDemo() {
		
		
        System.out.println( "Executing Satellite Service Discovery!" );
        
        try {
			
        	SatelliteServiceDiscoveryUtility discoveredServices 
        		= new SatelliteServiceDiscoveryUtility();
	        
	        System.out.println("");
	        System.out.println("");
	        System.out.println("");
	        System.out.println("");
	        System.out.println("");
	        
	        System.out.println("If this utility was integrated with hardware, "
	        		+ "we could now set up services on our units...");
	        System.out.println("");
	        System.out.println("For instnace...");
	        System.out.println("");
	        
	        ArrayList<DiscoveredSatelliteService> hdServiceList = 
	        		discoveredServices.getDiscoveredHDServiceArrayList();
	        
	        System.out.println("An example HD service we could use is: ");
	        System.out.println(hdServiceList.get(0).toString());
	        System.out.println("");
	        
	        
	        ArrayList<DiscoveredSatelliteService> sdServiceList = 
	        		discoveredServices.getDiscoveredSDServiceArrayList();
	        
	        System.out.println("An example SD service we could use is: ");
	        System.out.println(sdServiceList.get(0).toString());
	        System.out.println("");
	            
	        
	        HashMap<Integer, ArrayList<DiscoveredSatelliteService>> serviceIdToServiceMap =
	        		discoveredServices.getServiceIdMapToServiceArrayList();
	        
	        System.out.println("");
	        System.out.println("Some Service IDs have multiple services and we keep track of that. \r\n"
	        		+ "For instance...");
	        
	        for (Entry<Integer, ArrayList<DiscoveredSatelliteService>> entry : serviceIdToServiceMap.entrySet()) {
	        	if (entry.getValue().size() > 2) {
	        		System.out.println("");
	        		for (DiscoveredSatelliteService service : entry.getValue()) System.out.println(service.toString() + "\r\n");
	        		break;
	        	}
	        }
	        
	        System.out.println("");
	        System.out.println("");
	        System.out.println("That's it for now, thanks! :)");
        
        } catch (UnknownHostException e) { 
        	System.out.println("");
        	System.err.println("Unable to connect to the network... is your wi-fi on/ethernet cable connected?");
        	System.out.println("");
        	e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
            
	}
	
}
