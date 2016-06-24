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

import com.alee.api.ColorSupport;
import com.alee.api.IconSupport;
import com.alee.api.TitleSupport;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CompareUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default cell renderer for WebLaF lists.
 *
 * @author Mikle Garin
 */

public class WebListCellRenderer extends WebStyledLabel implements ListCellRenderer, Stateful
{
    /**
     * Additional renderer decoration states.
     */
    protected final List<String> states = new ArrayList<String> ( 3 );

    /**
     * Constructs default list cell renderer.
     */
    public WebListCellRenderer ()
    {
        super ();
        setName ( "List.cellRenderer" );
    }

    @Override
    public List<String> getStates ()
    {
        return states;
    }

    /**
     * Updates custom renderer states based on render cycle settings.
     *
     * @param list       tree
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateStates ( final JList list, final Object value, final int index, final boolean isSelected, final boolean hasFocus )
    {
        states.clear ();
        if ( isSelected )
        {
            states.add ( DecorationState.selected );
        }
        if ( hasFocus )
        {
            states.add ( DecorationState.focused );
        }
        if ( value instanceof Stateful )
        {
            states.addAll ( ( ( Stateful ) value ).getStates () );
        }
    }

    /**
     * Updates list cell renderer component style ID.
     *
     * @param list         tree
     * @param value        cell value
     * @param index        cell index
     * @param isSelected   whether or not cell is selected
     * @param cellHasFocus whether or not cell has focus
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateStyleId ( final JList list, final Object value, final int index, final boolean isSelected,
                                   final boolean cellHasFocus )
    {
        setStyleId ( getIcon () != null ? StyleId.listIconCellRenderer.at ( list ) : StyleId.listTextCellRenderer.at ( list ) );
    }

    /**
     * Updating renderer based on the provided settings.
     *
     * @param list       tree
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateView ( final JList list, final Object value, final int index, final boolean isSelected, final boolean hasFocus )
    {
        // Updating renderer visual settings
        setEnabled ( list.isEnabled () );
        setFont ( list.getFont () );
        setComponentOrientation ( list.getComponentOrientation () );

        // Updating foreground
        if ( value instanceof ColorSupport )
        {
            final Color color = ( ( ColorSupport ) value ).getColor ();
            if ( color != null )
            {
                setForeground ( color );
            }
            else
            {
                setForeground ( isSelected ? list.getSelectionForeground () : list.getForeground () );
            }
        }
        else
        {
            setForeground ( isSelected ? list.getSelectionForeground () : list.getForeground () );
        }

        // Updating icon
        final Icon icon;
        if ( value instanceof IconSupport )
        {
            icon = ( ( IconSupport ) value ).getIcon ();
        }
        else
        {
            icon = value instanceof Icon ? ( Icon ) value : null;
        }
        setIcon ( icon );

        // Updating text
        final String text;
        if ( value instanceof TitleSupport )
        {
            text = getCheckedText ( icon, ( ( TitleSupport ) value ).getTitle () );
        }
        else
        {
            text = getCheckedText ( icon, value );
        }
        setText ( text );
    }

    /**
     * Returns list cell renderer component.
     *
     * @param list       tree
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @return list cell renderer component
     */
    @Override
    public Component getListCellRendererComponent ( final JList list, final Object value, final int index, final boolean isSelected,
                                                    final boolean hasFocus )
    {
        // Updating custom states
        updateStates ( list, value, index, isSelected, hasFocus );

        // Updating style ID
        updateStyleId ( list, value, index, isSelected, hasFocus );

        // Updating renderer view
        updateView ( list, value, index, isSelected, hasFocus );

        // Updating decoration states for this render cycle
        DecorationUtils.fireStatesChanged ( this );

        return this;
    }

    /**
     * Returns checked text for the specified value.
     *
     * @param icon  rendered icon
     * @param value rendered value
     * @return checked text for the specified value
     */
    protected String getCheckedText ( final Icon icon, final Object value )
    {
        if ( value == null || TextUtils.isEmpty ( value.toString () ) )
        {
            return icon != null ? "" : " ";
        }
        else
        {
            return value instanceof Icon ? "" : value.toString ();
        }
    }

    @Override
    public void validate ()
    {
        // Overridden for performance reasons
    }

    @Override
    public void invalidate ()
    {
        // Overridden for performance reasons
    }

    @Override
    public void revalidate ()
    {
        // Overridden for performance reasons
    }

    @Override
    public void repaint ( final long tm, final int x, final int y, final int width, final int height )
    {
        // Overridden for performance reasons
    }

    @Override
    public void repaint ( final Rectangle r )
    {
        // Overridden for performance reasons
    }

    @Override
    public void repaint ()
    {
        // Overridden for performance reasons
    }

    @Override
    protected void firePropertyChange ( final String pn, final Object oldValue, final Object newValue )
    {
        // Overridden for performance reasons
        if ( CompareUtils.equals ( pn, StyleId.STYLE_PROPERTY, StyleId.PARENT_STYLE_PROPERTY, WebLookAndFeel.TEXT_PROPERTY,
                AbstractDecorationPainter.DECORATION_STATES_PROPERTY, WebStyledLabel.STYLE_RANGES_PROPERTY ) )
        {
            super.firePropertyChange ( pn, oldValue, newValue );
        }
        else if ( CompareUtils.equals ( pn, WebLookAndFeel.FONT_PROPERTY, WebLookAndFeel.FOREGROUND_PROPERTY ) &&
                oldValue != newValue && getClientProperty ( javax.swing.plaf.basic.BasicHTML.propertyKey ) != null )
        {
            super.firePropertyChange ( pn, oldValue, newValue );
        }
    }

    @Override
    public void firePropertyChange ( final String propertyName, final byte oldValue, final byte newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final char oldValue, final char newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final short oldValue, final short newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final int oldValue, final int newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final long oldValue, final long newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final float oldValue, final float newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final double oldValue, final double newValue )
    {
        // Overridden for performance reasons
    }

    @Override
    public void firePropertyChange ( final String propertyName, final boolean oldValue, final boolean newValue )
    {
        // Overridden for performance reasons
    }

    /**
     * A subclass of {@link com.alee.laf.list.WebListCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     */
    public static class UIResource extends WebListCellRenderer implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link com.alee.laf.list.WebListCellRenderer}.
         */
    }
}