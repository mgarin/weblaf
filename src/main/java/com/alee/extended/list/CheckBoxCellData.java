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

package com.alee.extended.list;

import com.alee.utils.TextUtils;

/**
 * This class contains minimum data for single checkbox list cell.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class CheckBoxCellData
{
    /**
     * Checkbox cell data ID prefix.
     */
    private static final String ID_PREFIX = "CBCD";

    /**
     * Checkbox cell data ID.
     */
    private String id = null;

    /**
     * Whether checkbox is selected or not.
     */
    private boolean selected = false;

    /**
     * Whether checkbox is enabled or not.
     */
    private boolean enabled = true;

    /**
     * User object for cell rendering.
     */
    private Object userObject = null;

    /**
     * Constructs default checkbox cell data with null user object.
     */
    public CheckBoxCellData ()
    {
        super ();
    }

    /**
     * Constructs default checkbox cell data with specified user object.
     *
     * @param userObject user object
     */
    public CheckBoxCellData ( Object userObject )
    {
        super ();
        this.userObject = userObject;
    }

    /**
     * Constructs default checkbox cell data with specified user object and checkbox selection state.
     *
     * @param userObject user object
     * @param selected   whether checkbox is selected or not
     */
    public CheckBoxCellData ( Object userObject, boolean selected )
    {
        super ();
        this.selected = selected;
        this.userObject = userObject;
    }

    /**
     * Returns checkbox cell data ID.
     *
     * @return checkbox cell data ID
     */
    public String getId ()
    {
        if ( id == null )
        {
            id = TextUtils.generateId ( ID_PREFIX );
        }
        return id;
    }

    /**
     * Sets checkbox cell data ID.
     *
     * @param id new checkbox cell data ID
     */
    public void setId ( String id )
    {
        this.id = id;
    }

    /**
     * Returns user object.
     *
     * @return user object
     */
    public Object getUserObject ()
    {
        return userObject;
    }

    /**
     * Sets user object.
     *
     * @param userObject new user object
     */
    public void setUserObject ( Object userObject )
    {
        this.userObject = userObject;
    }

    /**
     * Returns whether checkbox is selected or not.
     *
     * @return true if checkbox is selected, false otherwise
     */
    public boolean isSelected ()
    {
        return selected;
    }

    /**
     * Sets whether checkbox is selected or not.
     *
     * @param selected whether checkbox is selected or not
     */
    public void setSelected ( boolean selected )
    {
        this.selected = selected;
    }

    /**
     * Inverts checkbox selection.
     */
    public void invertSelection ()
    {
        setSelected ( !isSelected () );
    }

    /**
     * Returns whether checkbox is enabled or not.
     *
     * @return true if checkbox is enabled, false otherwise
     */
    public boolean isEnabled ()
    {
        return enabled;
    }

    /**
     * Sets whether checkbox is enabled or not.
     *
     * @param enabled whether checkbox is enabled or not
     */
    public void setEnabled ( boolean enabled )
    {
        this.enabled = enabled;
    }
}