package nl.svendubbeld.fontys.dto;

/**
 * Marks an entity as being convertible to a Data Transfer Object.
 *
 * @param <DTO> The type of DTO to convert to.
 */
public interface ToDTOConvertible<DTO> {

    /**
     * Obtain a DTO representation of this object. An open Hibernate session might be required.
     *
     * @return The DTO representation of this object.
     */
    DTO convert(DTOHelper dtoHelper);
}
