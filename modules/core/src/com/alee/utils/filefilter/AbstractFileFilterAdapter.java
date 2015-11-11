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

package com.alee.utils.filefilter;

import javax.swing.*;

/**
 * File filter adapter class.
 *
 * @author Mikle Garin
 */

public abstract class AbstractFileFilterAdapter extends AbstractFileFilter
{
    /**
     * File filter icon.
     */
    protected ImageIcon icon;

    /**
     * File filter description.
     */
    protected String description;

    /**
     * Constructs new custom file filter.
     *
     * @param icon        file filter icon
     * @param description file filter description
     */
    public AbstractFileFilterAdapter ( final ImageIcon icon, final String description )
    {
        super ();
        this.icon = icon;
        this.description = description;
    }

    @Override
    public ImageIcon getIcon ()
    {
        return icon;
    }

    /**
     * Sets file filter icon.
     *
     * @param icon new file filter icon
     */
    public void setIcon ( final ImageIcon icon )
    {
        this.icon = icon;
    }

    @Override
    public String getDescription ()
    {
        return description;
    }

    /**
     * Sets short file filter description.
     *
     * @param description new short file filter description
     */
    public void setDescription ( final String description )
    {
        this.description = description;
    }
}