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

import com.alee.managers.style.StyleId;
import com.alee.laf.checkbox.WebCheckBox;

import java.awt.*;

/**
 * Component for default checkbox list cell rendering.
 *
 * @author Mikle Garin
 */

public class WebCheckBoxListElement extends WebCheckBox
{
    /**
     * Constructs default checkbox list cell renderer element in the specified state.
     *
     * @param id       style ID
     * @param selected whether or not checkbox is selected
     */
    public WebCheckBoxListElement ( final StyleId id, final boolean selected )
    {
        super ( id, selected );
        setName ( "List.cellRenderer" );
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void validate ()
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void invalidate ()
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void repaint ()
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void revalidate ()
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void repaint ( final long tm, final int x, final int y, final int width, final int height )
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void repaint ( final Rectangle r )
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    protected void firePropertyChange ( final String propertyName, final Object oldValue, final Object newValue )
    {
        // Strings get interned
        // noinspection StringEquality
        if ( propertyName == "text" || ( ( propertyName == "font" || propertyName == "foreground" ) && oldValue != newValue &&
                getClientProperty ( javax.swing.plaf.basic.BasicHTML.propertyKey ) != null ) )
        {

            super.firePropertyChange ( propertyName, oldValue, newValue );
        }
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange ( final String propertyName, final byte oldValue, final byte newValue )
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange ( final String propertyName, final char oldValue, final char newValue )
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange ( final String propertyName, final short oldValue, final short newValue )
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange ( final String propertyName, final int oldValue, final int newValue )
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange ( final String propertyName, final long oldValue, final long newValue )
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange ( final String propertyName, final float oldValue, final float newValue )
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange ( final String propertyName, final double oldValue, final double newValue )
    {
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange ( final String propertyName, final boolean oldValue, final boolean newValue )
    {
    }
}