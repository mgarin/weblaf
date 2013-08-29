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

package com.alee.laf.text;

import com.alee.laf.StyleConstants;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * User: mgarin Date: 17.08.11 Time: 23:02
 */

public class WebTextPaneUI extends WebEditorPaneUI
{
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebTextPaneUI ();
    }

    @Override
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        JTextComponent textComponent = getComponent ();

        // Default settings
        SwingUtils.setOrientation ( textComponent );
        textComponent.setMargin ( new Insets ( 2, 2, 2, 2 ) );
        textComponent.setFocusable ( true );
        textComponent.setOpaque ( true );
        textComponent.setBackground ( Color.WHITE );
        textComponent.setSelectionColor ( StyleConstants.textSelectionColor );
        textComponent.setForeground ( Color.BLACK );
        textComponent.setSelectedTextColor ( Color.BLACK );
        textComponent.setCaretColor ( Color.GRAY );
    }
}