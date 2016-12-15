package ca.uoit.csci4100.assessments.finalexamstarter;

public class Store {
    private String name;
    private String streetAddress;
    private String city;
    private String postalCode;
    private double distance;
    private int id;
    private boolean preferred = false;

    public Store(String name, String streetAddress, String city, String postalCode) {
        this.setName(name);
        this.setStreetAddress(streetAddress);
        this.setCity(city);
        this.setPostalCode(postalCode);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String toString() {
        return streetAddress + ", " + city;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
}
