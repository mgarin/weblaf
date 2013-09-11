/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.managers.focus;

/**
 * Small extension class for FocusTracker that simplifies its creation.
 *
 * @author Mikle Garin
 */

public abstract class DefaultFocusTracker implements FocusTracker
{
    /**
     * Whether tracking is currently enabled or not.
     */
    private boolean enabled;

    /**
     * Whether component and its childs in components tree should be counted as a single component or not.
     */
    private boolean uniteWithChilds;

    /**
     * Constructs new tracker with the specified tracked component.
     */
    public DefaultFocusTracker ()
    {
        this ( true );
    }

    /**
     * Constructs new tracker with the specified tracked component.
     *
     * @param uniteWithChilds whether component and its childs in components tree should be counted as a single component or not
     */
    public DefaultFocusTracker ( boolean uniteWithChilds )
    {
        super ();
        this.enabled = true;
        this.uniteWithChilds = uniteWithChilds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTrackingEnabled ()
    {
        return enabled;
    }

    /**
     * Sets whether tracking is currently enabled or not.
     *
     * @param enabled whether tracking is currently enabled or not
     */
    public void setTrackingEnabled ( boolean enabled )
    {
        this.enabled = enabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUniteWithChilds ()
    {
        return uniteWithChilds;
    }

    /**
     * Sets whether component and its childs in components tree should be counted as a single component or not.
     *
     * @param uniteWithChilds whether component and its childs in components tree should be counted as a single component or not
     */
    public void setUniteWithChilds ( boolean uniteWithChilds )
    {
        this.uniteWithChilds = uniteWithChilds;
    }
}