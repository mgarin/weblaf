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

package com.alee.examples.groups.overlay;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebOverlay;
import com.alee.laf.label.WebLabel;
import com.alee.laf.text.WebTextField;
import com.alee.utils.swing.ConditionalVisibilityListener;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 08.11.12 Time: 16:49
 */

public class TextFieldOverlayExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Overlayed text field";
    }

    @Override
    public String getDescription ()
    {
        return "Text field overlayed with a label";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Overlay
        final WebOverlay overlayPanel = new WebOverlay ();

        // Overlayed text field
        final WebTextField textField = new WebTextField ( 15 );
        overlayPanel.setComponent ( textField );

        // Label displayed as overlay when component is not focused
        final WebLabel overlay = new WebLabel ( "Enter text here..." )
        {
            @Override
            public boolean contains ( final int x, final int y )
            {
                // Making label invisible for mouse events
                return false;
            }
        };
        overlay.setForeground ( Color.GRAY );
        textField.addFocusListener ( new ConditionalVisibilityListener ( overlay, false, null )
        {
            @Override
            public boolean isVisible ()
            {
                return super.isVisible () && textField.getText ().equals ( "" );
            }
        } );
        overlayPanel.addOverlay ( overlay, SwingConstants.CENTER, SwingConstants.CENTER );

        return new GroupPanel ( overlayPanel );
    }
}