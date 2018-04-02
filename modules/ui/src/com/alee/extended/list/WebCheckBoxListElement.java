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

import com.alee.api.jdk.Objects;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.AbstractDecorationPainter;

import javax.swing.plaf.basic.BasicHTML;
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

    @Override
    public void validate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void invalidate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void repaint ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void revalidate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void repaint ( final long tm, final int x, final int y, final int width, final int height )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void repaint ( final Rectangle r )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    protected void firePropertyChange ( final String pn, final Object oldValue, final Object newValue )
    {
        /**
         * Overridden for performance reasons.
         * {@link WebLookAndFeel#BORDER_PROPERTY} is listened to ensure that custom borders are preserved.
         */
        if ( Objects.equals ( pn, StyleId.STYLE_PROPERTY, StyleId.PARENT_STYLE_PROPERTY,
                AbstractDecorationPainter.DECORATION_STATES_PROPERTY, WebStyledLabel.STYLE_RANGES_PROPERTY,
                WebLookAndFeel.TEXT_PROPERTY, WebLookAndFeel.BORDER_PROPERTY, WebLookAndFeel.MODEL_PROPERTY ) )
        {
            super.firePropertyChange ( pn, oldValue, newValue );
        }
        else if ( Objects.equals ( pn, WebLookAndFeel.FONT_PROPERTY, WebLookAndFeel.FOREGROUND_PROPERTY ) &&
                oldValue != newValue && getClientProperty ( BasicHTML.propertyKey ) != null )
        {
            super.firePropertyChange ( pn, oldValue, newValue );
        }
    }

    @Override
    public void firePropertyChange ( final String propertyName, final byte oldValue, final byte newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final char oldValue, final char newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final short oldValue, final short newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final int oldValue, final int newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final long oldValue, final long newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final float oldValue, final float newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final double oldValue, final double newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final boolean oldValue, final boolean newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }
}