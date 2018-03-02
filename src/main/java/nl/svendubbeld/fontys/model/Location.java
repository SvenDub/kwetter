package nl.svendubbeld.fontys.model;

import org.jetbrains.annotations.Nullable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.Optional;

/**
 * A geographical location with a label.
 */
@Embeddable
public class Location {

    /**
     * The label associated with the location.
     */
    private String label;

    /**
     * The latitude of the location.
     */
    @Nullable
    private Float latitude;

    /**
     * The longitude of the location.
     */
    @Nullable
    private Float longitude;

    protected Location() {
    }

    /**
     * Create a new location with a label but without coordinates.
     *
     * @param label The label describing the location.
     */
    public Location(String label) {
        this.label = label;
    }

    /**
     * Create a new location with a label and coordinates.
     *
     * @param label     The label associated with the location.
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     */
    public Location(String label, float latitude, float longitude) {
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return The label associated with the location.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return The latitude of the location.
     */
    public Optional<Float> getLatitude() {
        return Optional.ofNullable(latitude);
    }

    /**
     * @return The longitude of the location.
     */
    public Optional<Float> getLongitude() {
        return Optional.ofNullable(longitude);
    }
}
