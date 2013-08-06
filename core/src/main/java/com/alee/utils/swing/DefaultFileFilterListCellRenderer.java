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

import com.alee.extended.filefilter.DefaultFileFilter;
import com.alee.laf.combobox.WebComboBoxCellRenderer;
import com.alee.laf.label.WebLabel;

import javax.swing.*;
import java.awt.*;

/**
 * Custom list and combobox cell renderer for file filters data.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class DefaultFileFilterListCellRenderer extends WebComboBoxCellRenderer
{
    /**
     * Constructs a default file filter cell renderer.
     *
     * @param comboBox chooser combobox
     */
    public DefaultFileFilterListCellRenderer ( JComboBox comboBox )
    {
        super ( comboBox );
    }

    /**
     * {@inheritDoc}
     */
    public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
        final DefaultFileFilter defaultFileFilter = ( DefaultFileFilter ) value;
        final WebLabel renderer = ( WebLabel ) super.getListCellRendererComponent ( list, "", index, isSelected, cellHasFocus );
        renderer.setIcon ( defaultFileFilter.getIcon () );
        renderer.setText ( defaultFileFilter.getDescription () );
        return renderer;
    }
}