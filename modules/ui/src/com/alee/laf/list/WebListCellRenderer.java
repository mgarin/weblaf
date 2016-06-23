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
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default cell renderer for {@link com.alee.laf.list.WebListUI} list.
 *
 * @author Mikle Garin
 */

public class WebListCellRenderer extends WebStyledLabel implements ListCellRenderer, Stateful
{
    /**
     * Additional renderer decoration states.
     */
    protected final List<String> states = new ArrayList<String> (3);

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
     * Returns list cell renderer component.
     *
     * @param list       tree
     * @param value      cell value
     * @param index      cell index
     * @param isSelected whether cell is selected or not
     * @param hasFocus   whether cell has focus or not
     * @return list cell renderer component
     */
    @Override
    public Component getListCellRendererComponent ( final JList list, final Object value, final int index, final boolean isSelected,
                                                    final boolean hasFocus )
    {
        // Updating base states
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

        // Updating style ID
        setStyleId ( getStyleId ( list, value, index, isSelected, hasFocus ) );

        // Updating renderer visual settings
        setEnabled ( list.isEnabled () );
        setFont ( list.getFont () );
        setComponentOrientation ( list.getComponentOrientation () );

        // Foreground
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

        // Icon
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

        // Text
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
            return icon == null ? " " : "";
        }
        else
        {
            return value instanceof Icon ? "" : value.toString ();
        }
    }

    /**
     * Returns list cell renderer component style ID.
     *
     * @param list         tree
     * @param value        cell value
     * @param index        cell index
     * @param isSelected   whether cell is selected or not
     * @param cellHasFocus whether cell has focus or not
     * @return list cell renderer component style ID
     */
    protected StyleId getStyleId ( final JList list, final Object value, final int index, final boolean isSelected,
                                   final boolean cellHasFocus )
    {
        return getIcon () != null ? StyleId.listIconCellRenderer.at ( list ) : StyleId.listTextCellRenderer.at ( list );
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