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

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 01.06.11 Time: 23:25
 */

public class WebComboBoxCellRenderer extends DefaultListCellRenderer
{
    protected Component component;
    protected WebComboBoxElement renderer;

    public WebComboBoxCellRenderer ( Component component )
    {
        super ();
        this.component = component;
        this.renderer = new WebComboBoxElement ();
    }

    public WebComboBoxElement getRenderer ()
    {
        return renderer;
    }

    @Override
    public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
        renderer.setIndex ( index );
        renderer.setTotalElements ( list.getModel ().getSize () );
        renderer.setSelected ( isSelected );
        renderer.updatePainter ();

        renderer.setEnabled ( component.isEnabled () );
        renderer.setFont ( component.getFont () );
        renderer.setForeground ( isSelected ? list.getSelectionForeground () : list.getForeground () );
        renderer.setComponentOrientation ( list.getComponentOrientation () );

        if ( value instanceof Icon )
        {
            renderer.setIcon ( ( Icon ) value );
            renderer.setText ( "" );
        }
        else
        {
            renderer.setIcon ( null );
            renderer.setText ( value == null ? "" : value.toString () );
        }

        return renderer;
    }
}