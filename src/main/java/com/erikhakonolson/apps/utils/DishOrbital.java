package com.erikhakonolson.apps.utils;

public enum DishOrbital {
	
	EAST_61_5,
	EAST_72_7,
	EAST_77,
	WEST_110,
	WEST_118_7,
	WEST_119,
	WEST_129;
	
	public String toString() {
		switch (this) {
			case EAST_61_5:
				return "61.5";
			case EAST_72_7:
				return "72.7";
			case EAST_77:
				return "77";
			case WEST_110:
				return "110";
			case WEST_118_7:
				return "118.7";
			case WEST_119:
				return "119";
			case WEST_129:
				return "129";
			default:
				return null;
				
		}
	}
}