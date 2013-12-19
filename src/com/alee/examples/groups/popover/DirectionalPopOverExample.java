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

package com.alee.examples.groups.popover;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.window.PopOverDirection;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Directional pop-over dialogs example.
 *
 * @author Mikle Garin
 */

public class DirectionalPopOverExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Directional pop-over dialog";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Directional web-styled pop-over dialog";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Button to display pop-over dialog below
        final WebButton showDialog = new WebButton ( "Show pop-over dialogs", loadIcon ( "directional.png" ), new ActionListener ()
        {
            private final List<WebPopOver> toClose = new ArrayList<WebPopOver> ( 4 );

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( toClose.size () > 0 )
                {
                    closePopOverDialogs ();
                }
                else
                {
                    final WebButton source = ( WebButton ) e.getSource ();
                    toClose.add ( createPopOver ( "\"Up\" direction pop-over" ).show ( source, PopOverDirection.up ) );
                    toClose.add ( createPopOver ( "\"Left\" direction pop-over" ).show ( source, PopOverDirection.left ) );
                    toClose.add ( createPopOver ( "\"Down\" direction pop-over" ).show ( source, PopOverDirection.down ) );
                    toClose.add ( createPopOver ( "\"Right\" direction pop-over" ).show ( source, PopOverDirection.right ) );
                }
            }

            private WebPopOver createPopOver ( final String text )
            {
                final WebPopOver popOver = new WebPopOver ( owner );
                popOver.setMargin ( 10 );
                popOver.setLayout ( new BorderLayout ( 10, 0 ) );
                popOver.setFocusableWindowState ( false );
                popOver.add ( new WebLabel ( text ), BorderLayout.CENTER );
                popOver.add ( new WebButton ( loadIcon ( "cross.png" ), new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        closePopOverDialogs ();
                    }
                } ).setUndecorated ( true ), BorderLayout.EAST );
                popOver.addWindowListener ( new WindowAdapter ()
                {
                    @Override
                    public void windowClosed ( final WindowEvent e )
                    {
                        toClose.remove ( popOver );
                    }
                } );
                return popOver;
            }

            private void closePopOverDialogs ()
            {
                for ( final WebPopOver popOver : toClose )
                {
                    popOver.dispose ();
                }
            }
        } );
        return new GroupPanel ( showDialog );
    }
}