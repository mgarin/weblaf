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

package com.alee.examples.groups.combobox;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.filefilter.AbstractFileFilter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.GlobalConstants;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.combobox.WebComboBoxCellRenderer;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 14.02.12 Time: 14:21
 */

public class StyledComboBoxExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Styled combobox";
    }

    @Override
    public String getDescription ()
    {
        return "Styled combobox renderer";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        WebComboBox styled = new WebComboBox ( GlobalConstants.DEFAULT_FILTERS.toArray () );
        styled.setSelectedIndex ( 0 );
        styled.setRenderer ( new WebComboBoxCellRenderer ( styled )
        {
            @Override
            public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
            {
                JLabel renderer = ( JLabel ) super.getListCellRendererComponent ( list, value, index, isSelected, cellHasFocus );
                AbstractFileFilter abstractFileFilter = ( AbstractFileFilter ) value;
                renderer.setIcon ( abstractFileFilter.getIcon () );
                renderer.setText ( abstractFileFilter.getDescription () );
                return renderer;
            }
        } );
        return new GroupPanel ( styled );
    }
}