package edu.illinois.data;

/**
 * This class represents the location of the client.
 * 
 * @author Tianyi Wang
 */
public class SelfLocation {
	
	private String locationName;
	
	private String country;
	private String state;
	private String city;
	private String buildName;
	private String roomName;
	
	/**
	 * Constructor.
	 * 
	 * @param locationName the location name used in this system.
	 */
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