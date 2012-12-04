package edu.illinois.data;

/**
 * @author Tianyi Wang
 */
public class SelfLocation {
	private String locationName;
	
	private String country;
	private String state;
	private String city;
	private String buildName;
	private String roomName;
	
	public SelfLocation(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationName() {
		return locationName;
	}

	@Override
	public String toString() {
		return "SelfLocation [locationName=" + locationName + ", country="
				+ country + ", state=" + state + ", city=" + city
				+ ", buildName=" + buildName + ", roomName=" + roomName + "]";
	}
}