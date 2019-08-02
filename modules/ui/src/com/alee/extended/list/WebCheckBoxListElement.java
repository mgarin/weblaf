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
    public final void validate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void invalidate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void revalidate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ( final long tm, final int x, final int y, final int width, final int height )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ( final Rectangle r )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    /**
     * Checks whether or not specified property change should actually be fired.
     * All property fire methods are overridden and made final for performance reasons.
     *
     * @param propertyName changed property name
     * @param oldValue     old property value
     * @param newValue     new property value
     */
    protected void checkPropertyChange ( final String propertyName, final Object oldValue, final Object newValue )
    {
        if ( Objects.equals ( propertyName, StyleId.STYLE_PROPERTY, StyleId.PARENT_STYLE_PROPERTY,
                AbstractDecorationPainter.DECORATION_STATES_PROPERTY, WebStyledLabel.STYLE_RANGES_PROPERTY,
                WebLookAndFeel.TEXT_PROPERTY, WebLookAndFeel.BORDER_PROPERTY, WebLookAndFeel.MODEL_PROPERTY ) )
        {
            allowPropertyChange ( propertyName, oldValue, newValue );
        }
        else if ( Objects.equals ( propertyName, WebLookAndFeel.FONT_PROPERTY, WebLookAndFeel.FOREGROUND_PROPERTY ) &&
                oldValue != newValue && getClientProperty ( BasicHTML.propertyKey ) != null )
        {
            allowPropertyChange ( propertyName, oldValue, newValue );
        }
    }

    /**
     * Allows property change event to be fired.
     *
     * @param propertyName changed property name
     * @param oldValue     old property value
     * @param newValue     new property value
     */
    protected void allowPropertyChange ( final String propertyName, final Object oldValue, final Object newValue )
    {
        super.firePropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    protected final void firePropertyChange ( final String propertyName, final Object oldValue, final Object newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final byte oldValue, final byte newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final char oldValue, final char newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final short oldValue, final short newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final int oldValue, final int newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final long oldValue, final long newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final float oldValue, final float newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final double oldValue, final double newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final boolean oldValue, final boolean newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }
}