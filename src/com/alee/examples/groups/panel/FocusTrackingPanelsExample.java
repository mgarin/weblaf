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

package com.alee.examples.groups.panel;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.alee.utils.SwingUtils;

import java.awt.*;

/**
 * User: mgarin Date: 17.09.12 Time: 15:40
 */

public class FocusTrackingPanelsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Focus-tracking panel";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled focus-tracking panel";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Panel with focusable field
        final WebPanel panel1 = new WebPanel ( true );
        panel1.setPaintFocus ( true );
        panel1.setMargin ( 10 );
        panel1.add ( new WebLabel ( "Some field:", WebLabel.CENTER ), BorderLayout.NORTH );
        panel1.add ( new WebTextField ( "TextField", 6 ), BorderLayout.CENTER );

        // Panel with focusable button
        final WebPanel panel2 = new WebPanel ( true );
        panel2.setPaintFocus ( true );
        panel2.setMargin ( 10 );
        panel2.add ( new WebLabel ( "Some button:", WebLabel.CENTER ), BorderLayout.NORTH );
        panel2.add ( new WebButton ( "Button" ), BorderLayout.CENTER );

        // Panel with focusable combobox
        final WebPanel panel3 = new WebPanel ( true );
        panel3.setPaintFocus ( true );
        panel3.setMargin ( 10 );
        panel3.add ( new WebLabel ( "Some combo:", WebLabel.CENTER ), BorderLayout.NORTH );
        panel3.add ( new WebComboBox ( new String[]{ "ComboBox" } ), BorderLayout.CENTER );

        // Equalizing panel widths
        SwingUtils.equalizeComponentsWidths ( panel1, panel2, panel3 );

        return new GroupPanel ( 4, panel1, panel2, panel3 );
    }
}