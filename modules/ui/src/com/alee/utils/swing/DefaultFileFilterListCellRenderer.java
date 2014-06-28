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

package com.alee.utils.swing;

import com.alee.laf.combobox.WebComboBoxCellRenderer;
import com.alee.laf.combobox.WebComboBoxElement;
import com.alee.utils.filefilter.AbstractFileFilter;

import javax.swing.*;
import java.awt.*;

/**
 * Custom list and combobox cell renderer for file filters data.
 *
 * @author Mikle Garin
 */

public class DefaultFileFilterListCellRenderer extends WebComboBoxCellRenderer
{
    /**
     * Constructs a default file filter cell renderer.
     */
    public DefaultFileFilterListCellRenderer ()
    {
        super ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent ( final JList list, final Object value, final int index, final boolean isSelected,
                                                    final boolean cellHasFocus )
    {
        final AbstractFileFilter abstractFileFilter = ( AbstractFileFilter ) value;
        final WebComboBoxElement renderer =
                ( WebComboBoxElement ) super.getListCellRendererComponent ( list, "", index, isSelected, cellHasFocus );
        renderer.setIcon ( abstractFileFilter.getIcon () );
        renderer.setText ( abstractFileFilter.getDescription () );
        return renderer;
    }
}