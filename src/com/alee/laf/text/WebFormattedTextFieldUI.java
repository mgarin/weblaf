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

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

/**
 * User: mgarin Date: 17.08.11 Time: 22:51
 */

public class WebFormattedTextFieldUI extends WebTextFieldUI implements ActionListener
{
    public WebFormattedTextFieldUI ( JFormattedTextField textField )
    {
        super ( textField );
    }

    public WebFormattedTextFieldUI ( JFormattedTextField textField, boolean drawBorder )
    {
        super ( textField, drawBorder );
    }

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebFormattedTextFieldUI ( ( JFormattedTextField ) c );
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        ( ( JFormattedTextField ) c ).addActionListener ( WebFormattedTextFieldUI.this );
    }

    @Override
    public void uninstallUI ( JComponent c )
    {
        super.uninstallUI ( c );

        ( ( JFormattedTextField ) c ).removeActionListener ( WebFormattedTextFieldUI.this );
    }

    @Override
    public void actionPerformed ( ActionEvent e )
    {
        JFormattedTextField ftf = ( JFormattedTextField ) getComponent ();
        try
        {
            ftf.commitEdit ();
            ftf.setValue ( ftf.getValue () );
        }
        catch ( ParseException e1 )
        {
            ftf.setValue ( ftf.getValue () );
        }
    }
}