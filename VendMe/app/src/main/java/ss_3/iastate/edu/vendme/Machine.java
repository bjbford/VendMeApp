package ss_3.iastate.edu.vendme;


/**
 * Vending Machine Object.
 * Created by brianbradford on 3/23/18.
 */

public class Machine {

    private int machine_Number;
    private double locationLat;
    private double locationLng;
    private String building;
    private String machine_Type;
    private String locationDesc;

    public Machine(){
        this.machine_Number = 0;
        this.locationLat = 0.0;
        this.locationLng = 0.0;
        this.building = "";
        this.machine_Type = "";
        this.locationDesc = "";
    }

    /**
     * Setter for Machine_Number parameter.
     * @param num
     */
    public void setMachineNumber(int num){
        machine_Number = num;
    }

    /**
     * Getter for Machine_Number parameter.
     */
    public int getMachineNumber(){
        return machine_Number;
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
     * Pull the contents of a machine from database based on the ID of the machine object that
     * called this method.
     */
    public void pullMachineContents(){
        // use Machine_Number as ID for table
        // Update data structure that holds contents
    }

    /**
     * Pull the prices of a machines contents from database based on the ID of the machine object
     * that called this method.
     */
    public void pullMachinePrices(){
        // use Machine_Number as ID for table
        // Update data structure that holds prices
    }
}
