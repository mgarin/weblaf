package com.alee.utils.compare;

/**
 * This interface provides a base for filtering any type of objects in any situation.
 * This class is similar to FileFilter from default file chooser, but it doesn't require any specific object type like File.
 *
 * @author Mikle Garin
 */

public interface Filter<E>
{
    /**
     * Returns whether the specified object is accepted by this filter or not.
     *
     * @param object object to process
     * @return true if the specified object is accepted by this filter, false otherwise
     */
    public boolean accept ( E object );
}