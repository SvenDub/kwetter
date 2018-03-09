package nl.svendubbeld.fontys.model;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.LocationDTO;
import nl.svendubbeld.fontys.dto.ToDTOConvertible;
import nl.svendubbeld.fontys.validation.constraints.LabelOrLocation;
import nl.svendubbeld.fontys.validation.constraints.MaxFloat;
import nl.svendubbeld.fontys.validation.constraints.MinFloat;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Optional;

/**
 * A geographical location with a label.
 */
@Embeddable
@LabelOrLocation
public class Location implements ToDTOConvertible<LocationDTO> {

    /**
     * The label associated with the location.
     */
    @Nullable
    @Column
    private String label;

    /**
     * The latitude of the location.
     */
    @Nullable
    @MinFloat(-90)
    @MaxFloat(90)
    @Column
    private Float latitude;

    /**
     * The longitude of the location.
     */
    @Nullable
    @MinFloat(-180)
    @MaxFloat(180)
    @Column
    private Float longitude;

    protected Location() {
    }

    /**
     * Create a new location with a label but without coordinates.
     *
     * @param label The label describing the location.
     */
    public Location(@Nullable String label) {
        this.label = label;
    }

    /**
     * Create a new location with a label and coordinates.
     *
     * @param label     The label associated with the location.
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     */
    public Location(@Nullable String label, float latitude, float longitude) {
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Create a new location with coordinates but without a label.
     *
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     */
    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return The label associated with the location.
     */
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
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

    @Override
    public LocationDTO convert(DTOHelper dtoHelper) {
        return new LocationDTO(label, latitude, longitude);
    }
}
