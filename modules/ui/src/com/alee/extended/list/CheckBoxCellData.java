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
 * @param <T> data type
 * @author Mikle Garin
 */
public class CheckBoxCellData<T>
{
    /**
     * Checkbox cell data ID prefix.
     */
    protected static final String ID_PREFIX = "CBCD";

    /**
     * Checkbox cell data ID.
     */
    protected String id;

    /**
     * Whether checkbox is selected or not.
     */
    protected boolean selected;

    /**
     * Whether checkbox is enabled or not.
     */
    protected boolean enabled;

    /**
     * User object for cell rendering.
     */
    protected T userObject;

    /**
     * Constructs new {@link CheckBoxCellData} with {@code null} user object.
     */
    public CheckBoxCellData ()
    {
        this ( null, false );
    }

    /**
     * Constructs new {@link CheckBoxCellData} with specified user object.
     *
     * @param userObject user object
     */
    public CheckBoxCellData ( final T userObject )
    {
        this ( userObject, false );
    }

    /**
     * Constructs new {@link CheckBoxCellData} with specified user object and selection state.
     *
     * @param userObject user object
     * @param selected   whether checkbox is selected or not
     */
    public CheckBoxCellData ( final T userObject, final boolean selected )
    {
        this.id = null;
        this.selected = selected;
        this.enabled = true;
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
    public void setId ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns user object.
     *
     * @return user object
     */
    public T getUserObject ()
    {
        return userObject;
    }

    /**
     * Sets user object.
     *
     * @param userObject new user object
     */
    public void setUserObject ( final T userObject )
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
    public void setSelected ( final boolean selected )
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
    public void setEnabled ( final boolean enabled )
    {
        this.enabled = enabled;
    }
}