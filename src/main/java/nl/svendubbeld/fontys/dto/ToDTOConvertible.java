package nl.svendubbeld.fontys.dto;

/**
 * Marks an entity as being convertible to a Data Transfer Object.
 *
 * @param <T> The type of DTO to convert to.
 */
public interface ToDTOConvertible<T> {

    /**
     * Obtain a DTO representation of this object. An open Hibernate session might be required.
     *
     * @param dtoHelper Helper for use in DTO conversion.
     * @return The DTO representation of this object.
     */
    T convert(DTOHelper dtoHelper);
}
