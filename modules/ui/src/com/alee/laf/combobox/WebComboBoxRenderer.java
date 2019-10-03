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

import com.alee.api.annotations.NotNull;
import com.alee.laf.list.WebListCellRenderer;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Default {@link ListCellRenderer} implementation based on {@link WebListCellRenderer} for {@link JComboBox}.
 * Unlike {@link javax.swing.plaf.basic.BasicComboBoxRenderer} it has generics for value and list types.
 * It also retains all of the {@link WebListCellRenderer} methods that can be overridden for renderer customization.
 *
 * @param <V> cell value type
 * @param <C> {@link JList} type
 * @param <P> {@link ComboBoxCellParameters} type
 * @author Mikle Garin
 * @see ComboBoxCellParameters
 */
public class WebComboBoxRenderer<V, C extends JList, P extends ComboBoxCellParameters<V, C>>
        extends WebListCellRenderer<V, C, P>
{
    @Override
    protected void updateStates ( final P parameters )
    {
        // Adding base states
        super.updateStates ( parameters );

        // Adding press and expansion states
        if ( parameters.comboBox ().isPopupVisible () )
        {
            states.add ( DecorationState.pressed );
            states.add ( DecorationState.expanded );
        }
        else
        {
            states.add ( DecorationState.collapsed );
        }
    }

    @Override
    protected void updateStyleId ( final P parameters )
    {
        if ( parameters.index () == -1 )
        {
            setStyleId ( StyleId.comboboxBoxRenderer.at ( parameters.list () ) );
        }
        else
        {
            setStyleId ( StyleId.comboboxListRenderer.at ( parameters.list () ) );
        }
    }

    @Override
    protected P getRenderingParameters ( final C list, final V value, final int index,
                                         final boolean isSelected, final boolean hasFocus )
    {
        return ( P ) new ComboBoxCellParameters<V, C> ( list, value, index, isSelected, hasFocus );
    }

    @NotNull
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
     * A subclass of {@link WebComboBoxRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     *
     * @param <V> cell value type
     * @param <C> {@link JList} type
     * @param <P> {@link ComboBoxCellParameters} type
     */
    public static final class UIResource<V, C extends JList, P extends ComboBoxCellParameters<V, C>>
            extends WebComboBoxRenderer<V, C, P> implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebComboBoxRenderer}.
         */
    }
}