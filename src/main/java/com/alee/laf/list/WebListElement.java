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

package com.alee.laf.list;

import com.alee.laf.label.WebLabel;

/**
 * Component for default list cell rendering.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebListElement extends WebLabel
{
    /**
     * Constructs default list cell renderer element.
     */
    public WebListElement ()
    {
        super ();
        setOpaque ( false );
    }

    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void validate ()
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void invalidate ()
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void repaint ()
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void revalidate ()
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void repaint ( long tm, int x, int y, int width, int height )
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void repaint ( Rectangle r )
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    protected void firePropertyChange ( String propertyName, Object oldValue, Object newValue )
    //    {
    //        // Strings get interned
    //        // noinspection StringEquality
    //        if ( propertyName == "text" || ( ( propertyName == "font" || propertyName == "foreground" ) && oldValue != newValue &&
    //                getClientProperty ( javax.swing.plaf.basic.BasicHTML.propertyKey ) != null ) )
    //        {
    //
    //            super.firePropertyChange ( propertyName, oldValue, newValue );
    //        }
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void firePropertyChange ( String propertyName, byte oldValue, byte newValue )
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void firePropertyChange ( String propertyName, char oldValue, char newValue )
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void firePropertyChange ( String propertyName, short oldValue, short newValue )
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void firePropertyChange ( String propertyName, int oldValue, int newValue )
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void firePropertyChange ( String propertyName, long oldValue, long newValue )
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void firePropertyChange ( String propertyName, float oldValue, float newValue )
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void firePropertyChange ( String propertyName, double oldValue, double newValue )
    //    {
    //    }
    //
    //    /**
    //     * Overridden for performance reasons.
    //     */
    //    public void firePropertyChange ( String propertyName, boolean oldValue, boolean newValue )
    //    {
    //    }
}