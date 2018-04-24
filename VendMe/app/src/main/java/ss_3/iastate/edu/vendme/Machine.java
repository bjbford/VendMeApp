package ss_3.iastate.edu.vendme;

import java.util.ArrayList;

/**
 * Vending Machine Object.
 * Created by brianbradford on 3/23/18.
 */
public class Machine {

    private double locationLat;
    private double locationLng;
    private String building;
    private String machine_Type;
    private String locationDesc;
    private ArrayList<String> contents;
    private ArrayList<String> prices;

    public Machine(){
        this.locationLat = 0.0;
        this.locationLng = 0.0;
        this.building = "";
        this.machine_Type = "";
        this.locationDesc = "";
        this.contents = new ArrayList<String>();
        this.prices = new ArrayList<String>();
    }

    /**
     * Setter for Location Latitude parameter.
     */
    public void setLocationLat(double latitude){
        locationLat = latitude;
    }

    /**
     * Getter for Location Latitude parameter.
     */
    public double getLocationLat(){
        return locationLat;
    }

    /**
     * Setter for Location Latitude parameter.
     */
    public void setLocationLng(double longitude){
        locationLng = longitude;
    }

    /**
     * Getter for Location Longitude parameter.
     */
    public double getLocationLng(){
        return locationLng;
    }

    /**
     * Setter for Building Name parameter.
     */
    public void setBuilding(String buildingName){
        building = buildingName;
    }

    /**
     * Getter for Building Name parameter.
     */
    public String getBuilding(){
        return building;
    }

    /**
     * Setter for Machine Type parameter.
     */
    public void setType(String type){
        machine_Type = type;
    }

    /**
     * Getter for Machine Type parameter.
     */
    public String getType(){
        return machine_Type;
    }

    /**
     * Setter for Location Description of Machine parameter.
     */
    public void setLocationDescription(String description){
        locationDesc = description;
    }

    /**
     * Getter for Location Description of Machine parameter.
     */
    public String getLocationDescription(){
        return locationDesc;
    }

    /**
     * Setter that parses the input string and generates an ArrayList of the Machine's contents.
     */
    public void setMachineContents(String input){
        String contentsArr[] = input.split(", ");
        for(String i : contentsArr){
            contents.add(i);
        }
    }

    /**
     * Getter for the ArrayList of contents, Machine parameter.
     */
    public ArrayList<String> getMachineContents(){
        return contents;
    }

    /**
     * Setter that parses the input string and generates an ArrayList of the Machine's prices.
     */
    public void setMachinePrices(String input){
        String pricesArr[] = input.split(",");
        for(String i: pricesArr){
            prices.add(i);
        }
    }

    /**
     * Getter for the ArrayList of prices, Machine parameter.
     */
    public ArrayList<String> getMachinePrices(){
        return prices;
    }
}
