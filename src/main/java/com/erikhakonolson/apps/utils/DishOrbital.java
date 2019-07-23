package com.erikhakonolson.apps.utils;

public enum DishOrbital {
	
	EAST_61_5("61.5"),
	EAST_72_7("72.7"),
	EAST_77("77"),
	WEST_110("110"),
	WEST_118_7("118.7"),
	WEST_119("119"),
	WEST_129("129");
	
	private final String orbitalString;
	
	DishOrbital(String s) {
		this.orbitalString = s;
	}
	
	public String toString() {
		return orbitalString;
	}
	
}