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

package com.alee.laf.combobox;

import com.alee.laf.label.WebLabel;

/**
 * User: mgarin Date: 27.07.12 Time: 19:17
 */

public class WebComboBoxElement extends WebLabel
{
    /**
     * Runtime variables.
     */
    protected int totalElements;
    protected int index;
    protected boolean selected;

    public WebComboBoxElement ()
    {
        super ();
        setName ( "List.cellRenderer" );
    }

    public int getTotalElements ()
    {
        return totalElements;
    }

    public void setTotalElements ( final int totalElements )
    {
        this.totalElements = totalElements;
    }

    public int getIndex ()
    {
        return index;
    }

    public void setIndex ( final int index )
    {
        this.index = index;
    }

    public boolean isSelected ()
    {
        return selected;
    }

    public void setSelected ( final boolean selected )
    {
        this.selected = selected;
    }
}