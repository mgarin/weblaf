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

import com.alee.api.*;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import javax.swing.plaf.ListUI;
import javax.swing.plaf.basic.BasicHTML;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link ListCellRenderer} implementation based on {@link WebStyledLabel}.
 * Unlike {@link DefaultListCellRenderer} it has generics for value and list types.
 * It also contains multiple methods for convenient renderer customization that can be overridden.
 * And since it is based on {@link WebStyledLabel} it retains all of its extra features.
 *
 * @param <V> cell value type
 * @param <C> list type
 * @author Mikle Garin
 */

public class WebListCellRenderer<V, C extends JList> extends WebStyledLabel implements ListCellRenderer, Stateful
{
    /**
     * Additional renderer decoration states.
     */
    protected final List<String> states;

    /**
     * Constructs new {@link WebListCellRenderer}.
     */
    public WebListCellRenderer ()
    {
        super ();
        setName ( "List.cellRenderer" );
        states = new ArrayList<String> ( 3 );
    }

    @Override
    public List<String> getStates ()
    {
        return states;
    }

    /**
     * Updates custom renderer states based on render cycle settings.
     *
     * @param list       {@link JList}
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     */
    protected void updateStates ( final C list, final V value, final int index,
                                  final boolean isSelected, final boolean hasFocus )
    {
        // Resetting states
        states.clear ();

        // Selection state
        states.add ( isSelected ? DecorationState.selected : DecorationState.unselected );

        // Focus state
        if ( hasFocus )
        {
            states.add ( DecorationState.focused );
        }

        // Hover state
        final ListUI ui = list.getUI ();
        if ( ui instanceof WListUI )
        {
            if ( ( ( WListUI ) ui ).getHoverIndex () == index )
            {
                states.add ( DecorationState.hover );
            }
        }

        // Extra states provided by value
        states.addAll ( DecorationUtils.getExtraStates ( value ) );
    }

    /**
     * Updates renderer component style identifier.
     *
     * @param list         {@link JList}
     * @param value        cell value
     * @param index        cell index
     * @param isSelected   whether or not cell is selected
     * @param cellHasFocus whether or not cell has focus
     */
    protected void updateStyleId ( final C list, final V value, final int index,
                                   final boolean isSelected, final boolean cellHasFocus )
    {
        StyleId id = null;
        if ( value instanceof ChildStyleSupport )
        {
            final ChildStyleId childStyleId = ( ( ChildStyleSupport ) value ).getChildStyleId ();
            if ( childStyleId != null )
            {
                id = childStyleId.at ( list );
            }
        }
        else if ( value instanceof StyleSupport )
        {
            final StyleId styleId = ( ( StyleSupport ) value ).getStyleId ();
            if ( styleId != null )
            {
                id = styleId;
            }
        }
        if ( id == null )
        {
            id = getIcon () != null ? StyleId.listIconCellRenderer.at ( list ) : StyleId.listTextCellRenderer.at ( list );
        }
        setStyleId ( id );
    }

    /**
     * Updating renderer based on the provided settings.
     *
     * @param list       {@link JList}
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     */
    protected void updateView ( final C list, final V value, final int index,
                                final boolean isSelected, final boolean hasFocus )
    {
        setEnabled ( enabledForValue ( list, value, index, isSelected, hasFocus ) );
        setComponentOrientation ( orientationForValue ( list, value, index, isSelected, hasFocus ) );
        setFont ( fontForValue ( list, value, index, isSelected, hasFocus ) );
        setForeground ( foregroundForValue ( list, value, index, isSelected, hasFocus ) );
        setIcon ( iconForValue ( list, value, index, isSelected, hasFocus ) );
        setText ( textForValue ( list, value, index, isSelected, hasFocus ) );
    }

    /**
     * Returns whether or not renderer for the specified cell value should be enabled.
     *
     * @param list       {@link JList}
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @return {@code true} if renderer for the specified cell value should be enabled, {@code false} otherwise
     */
    protected boolean enabledForValue ( final C list, final V value, final int index,
                                        final boolean isSelected, final boolean hasFocus )
    {
        return list.isEnabled ();
    }

    /**
     * Returns renderer {@link ComponentOrientation} for the specified cell value.
     *
     * @param list       {@link JList}
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @return renderer {@link ComponentOrientation} for the specified cell value
     */
    protected ComponentOrientation orientationForValue ( final C list, final V value, final int index,
                                                         final boolean isSelected, final boolean hasFocus )
    {
        return list.getComponentOrientation ();
    }

    /**
     * Returns renderer {@link Font} for the specified cell value.
     *
     * @param list       {@link JList}
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @return renderer {@link Font} for the specified cell value
     */
    protected Font fontForValue ( final C list, final V value, final int index,
                                  final boolean isSelected, final boolean hasFocus )
    {
        return list.getFont ();
    }

    /**
     * Returns renderer foreground color for the specified cell value.
     *
     * @param list       {@link JList}
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @return renderer foreground color for the specified cell value
     */
    protected Color foregroundForValue ( final C list, final V value, final int index,
                                         final boolean isSelected, final boolean hasFocus )
    {
        final Color foreground;
        if ( value instanceof ColorSupport )
        {
            final Color color = ( ( ColorSupport ) value ).getColor ();
            foreground = color != null ? color : isSelected ? list.getSelectionForeground () : list.getForeground ();
        }
        else
        {
            foreground = isSelected ? list.getSelectionForeground () : list.getForeground ();
        }
        return foreground;
    }

    /**
     * Returns renderer icon for the specified cell value.
     *
     * @param list       {@link JList}
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @return renderer icon for the specified cell value
     */
    protected Icon iconForValue ( final C list, final V value, final int index,
                                  final boolean isSelected, final boolean hasFocus )
    {
        final Icon icon;
        if ( value instanceof IconSupport )
        {
            icon = ( ( IconSupport ) value ).getIcon ();
        }
        else
        {
            icon = value instanceof Icon ? ( Icon ) value : null;
        }
        return icon;
    }

    /**
     * Returns renderer text for the specified cell value.
     *
     * @param list       {@link JList}
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @return renderer text for the specified cell value
     */
    protected String textForValue ( final C list, final V value, final int index,
                                    final boolean isSelected, final boolean hasFocus )
    {
        final String text;
        if ( value instanceof TitleSupport )
        {
            text = ( ( TitleSupport ) value ).getTitle ();
        }
        else
        {
            text = value != null && !( value instanceof Icon ) ? value.toString () : "";
        }
        return text;
    }

    /**
     * Returns list cell renderer component.
     *
     * @param list       {@link JList}
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @return list cell renderer component
     */
    @Override
    public Component getListCellRendererComponent ( final JList list, final Object value, final int index,
                                                    final boolean isSelected, final boolean hasFocus )
    {
        // Updating custom states
        updateStates ( ( C ) list, ( V ) value, index, isSelected, hasFocus );

        // Updating style ID
        updateStyleId ( ( C ) list, ( V ) value, index, isSelected, hasFocus );

        // Updating renderer view
        updateView ( ( C ) list, ( V ) value, index, isSelected, hasFocus );

        // Updating decoration states for this render cycle
        DecorationUtils.fireStatesChanged ( this );

        return this;
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
    public void repaint ()
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
        if ( CompareUtils.equals ( pn, StyleId.STYLE_PROPERTY, StyleId.PARENT_STYLE_PROPERTY,
                AbstractDecorationPainter.DECORATION_STATES_PROPERTY, WebStyledLabel.STYLE_RANGES_PROPERTY,
                WebLookAndFeel.TEXT_PROPERTY, WebLookAndFeel.BORDER_PROPERTY, WebLookAndFeel.MODEL_PROPERTY ) )
        {
            super.firePropertyChange ( pn, oldValue, newValue );
        }
        else if ( CompareUtils.equals ( pn, WebLookAndFeel.FONT_PROPERTY, WebLookAndFeel.FOREGROUND_PROPERTY ) &&
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

    /**
     * A subclass of {@link WebListCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     *
     * @param <V> cell value type
     * @param <C> list type
     */
    public static class UIResource<V, C extends JList> extends WebListCellRenderer<V, C> implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebListCellRenderer}.
         */
    }
}