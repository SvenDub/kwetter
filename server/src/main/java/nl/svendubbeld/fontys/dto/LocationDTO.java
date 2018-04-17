package nl.svendubbeld.fontys.dto;

import nl.svendubbeld.fontys.model.Location;

public class LocationDTO implements ToEntityConvertible<Location> {

    private String label;

    private Float latitude;

    private Float longitude;

    public LocationDTO() {
    }

    public LocationDTO(String label, Float latitude, Float longitude) {
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @Override
    public Location convert(DTOHelper dtoHelper) {
        return new Location(getLabel(), getLatitude(), getLongitude());
    }
}
