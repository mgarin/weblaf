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

package com.alee.laf.filechooser;

import com.alee.extended.window.TestFrame;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Custom UI for JFileChooser component.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebFileChooserUI extends BasicFileChooserUI
{
    /**
     * Returns an instance of the WebFileChooserUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebFileChooserUI
     */
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebFileChooserUI ( ( JFileChooser ) c );
    }

    /**
     * Constructs new WebFileChooserUI for the specified JFileChooser.
     *
     * @param b JFileChooser for which UI is constructed
     */
    public WebFileChooserUI ( JFileChooser b )
    {
        super ( b );
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    public void installUI ( JComponent c )
    {
        super.installUI ( c );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    public void uninstallUI ( JComponent c )
    {
        super.uninstallUI ( c );
    }

    public static void main ( String[] args )
    {
        WebLookAndFeel.install ();
        new TestFrame ( new WebButton ( "Open" )
        {
            {
                final WebButton p = this;
                addActionListener ( new ActionListener ()
                {
                    public void actionPerformed ( ActionEvent e )
                    {
                        new JFileChooser ().showOpenDialog ( p );
                    }
                } );
            }
        }, 50 );
    }
}