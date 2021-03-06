package nl.svendubbeld.fontys.dto;

/**
 * Marks an entity as being convertible to a Data Transfer Object.
 *
 * @param <T> The type of entity to convert to.
 */
public interface ToEntityConvertible<T> {

    /**
     * Obtain an entity from the DTO representation. An open Hibernate session might be required.
     *
     * @param dtoHelper Helper for use in DTO conversion.
     * @return The entity this DTO represents.
     */
    T convert(DTOHelper dtoHelper);
}
