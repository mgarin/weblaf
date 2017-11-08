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

import com.alee.laf.list.WebListCellRenderer;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Default {@link ListCellRenderer} implementation based on {@link com.alee.extended.label.WebStyledLabel}.
 * Unlike {@link javax.swing.plaf.basic.BasicComboBoxRenderer} it contains multiple methods for convenient renderer customization.
 * Also since it is based on {@link com.alee.extended.label.WebStyledLabel} it retains all of its extra features.
 *
 * @author Mikle Garin
 */

public class WebComboBoxRenderer extends WebListCellRenderer
{
    /**
     * todo 1. Add generic type for values
     */

    @Override
    protected void updateStates ( final JList list, final Object value, final int index,
                                  final boolean isSelected, final boolean hasFocus )
    {
        // Adding base states
        super.updateStates ( list, value, index, isSelected, hasFocus );

        // Adding press and expansion states
        final JComboBox comboBox = getComboBox ( list );
        if ( comboBox != null )
        {
            if ( comboBox.isPopupVisible () )
            {
                states.add ( DecorationState.pressed );
                states.add ( DecorationState.expanded );
            }
            else
            {
                states.add ( DecorationState.collapsed );
            }
        }
    }

    @Override
    protected void updateStyleId ( final JList list, final Object value, final int index,
                                   final boolean isSelected, final boolean cellHasFocus )
    {
        setStyleId ( index == -1 ? StyleId.comboboxBoxRenderer.at ( list ) : StyleId.comboboxListRenderer.at ( list ) );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension size;
        if ( isEmptyData () )
        {
            /**
             * Returns corrected preferred size in case text and icon were not specified.
             * This will generally prevent combobox and popup list from being shrinked when it contains empty label.
             * This is not really efficient so it is recommended to provide at least space as text when customizing renderer value.
             * Otherwise this mechanism will start working with those values in unefficient way to prevent visual issues.
             * This is simply a copy of {@link javax.swing.plaf.basic.BasicComboBoxRenderer#getPreferredSize()} workaround.
             */
            setText ( " " );
            size = super.getPreferredSize ();
            setText ( "" );
        }
        else
        {
            size = super.getPreferredSize ();
        }
        return size;
    }

    /**
     * Returns whether or not renderer has completely empty data.
     *
     * @return {@code true} if renderer has completely empty data, {@code false} otherwise
     */
    protected boolean isEmptyData ()
    {
        return TextUtils.isEmpty ( getText () ) && getIcon () == null;
    }

    /**
     * Returns {@link JComboBox} for which specified {@link JList} is used in popup.
     *
     * @param list {@link JList} to retrieve {@link JComboBox} for
     * @return {@link JComboBox} for which specified {@link JList} is used in popup
     */
    protected JComboBox getComboBox ( final JList list )
    {
        return ( JComboBox ) list.getClientProperty ( WebComboBoxUI.COMBOBOX_INSTANCE );
    }

    /**
     * A subclass of {@link WebComboBoxRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     */
    public static final class UIResource extends WebComboBoxRenderer implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebComboBoxRenderer}.
         */
    }
}