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
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Default combobox renderer.
 *
 * @author Mikle Garin
 */

public class WebComboBoxRenderer extends WebListCellRenderer
{
    @Override
    protected void updateStyleId ( final JList list, final Object value, final int index, final boolean isSelected,
                                   final boolean cellHasFocus )
    {
        setStyleId ( index == -1 ? StyleId.comboboxBoxRenderer.at ( list ) : StyleId.comboboxListRenderer.at ( list ) );
    }

    /**
     * Returns corrected preferred size in case text and icon were not specified.
     * This will generally prevent combobox and popup list from being shrinked when it contains empty label.
     * <p>
     * This is not really performance-efficient so it is recommended to provide at least space as text when customizing renderer value.
     * Otherwise this mechanism will start working with those values in unefficient way to prevent visual issues.
     *
     * @return corrected preferred size in case text and icon were not specified
     */
    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension size;
        if ( TextUtils.isEmpty ( getText () ) && getIcon () == null )
        {
            // todo Optimize this somehow?
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
     * A subclass of WebComboBoxCellRenderer that implements UIResource.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     */
    public static final class UIResource extends WebComboBoxRenderer implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link com.alee.laf.combobox.WebComboBoxRenderer}.
         */
    }
}