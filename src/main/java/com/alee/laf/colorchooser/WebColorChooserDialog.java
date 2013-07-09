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

package com.alee.laf.colorchooser;

import com.alee.laf.StyleConstants;
import com.alee.laf.rootpane.WebDialog;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: mgarin Date: 20.12.12 Time: 18:31
 */

public class WebColorChooserDialog extends WebDialog
{
    public static final ImageIcon COLOR_CHOOSER_ICON =
            new ImageIcon ( WebColorChooserDialog.class.getResource ( "icons/color_chooser.png" ) );

    private WebColorChooser colorChooser;

    public WebColorChooserDialog ()
    {
        this ( new WebColorChooser () );
    }

    public WebColorChooserDialog ( Component parent )
    {
        this ( new WebColorChooser (), parent );
    }

    public WebColorChooserDialog ( String title )
    {
        this ( new WebColorChooser (), title );
    }

    public WebColorChooserDialog ( Component parent, String title )
    {
        this ( new WebColorChooser (), parent, title );
    }

    public WebColorChooserDialog ( WebColorChooser webColorChooser )
    {
        this ( webColorChooser, null, null );
    }

    public WebColorChooserDialog ( WebColorChooser webColorChooser, Component parent )
    {
        this ( webColorChooser, parent, null );
    }

    public WebColorChooserDialog ( WebColorChooser webColorChooser, String title )
    {
        this ( webColorChooser, null, title );
    }

    public WebColorChooserDialog ( WebColorChooser webColorChooser, Component parent, String title )
    {
        super ( SwingUtils.getWindowAncestor ( parent ) );
        updateTitle ( title );
        setIconImage ( COLOR_CHOOSER_ICON.getImage () );
        setLayout ( new BorderLayout ( 0, 0 ) );

        colorChooser = webColorChooser;
        colorChooser.setOpaque ( false );
        colorChooser.setShowButtonsPanel ( true );
        getContentPane ().add ( colorChooser, BorderLayout.CENTER );

        addWindowListener ( new WindowAdapter ()
        {
            public void windowClosed ( WindowEvent e )
            {
                colorChooser.setResult ( StyleConstants.CLOSE_OPTION );
            }
        } );

        colorChooser.addColorChooserListener ( new ColorChooserListener ()
        {
            public void okPressed ( ActionEvent e )
            {
                WebColorChooserDialog.this.dispose ();
            }

            public void resetPressed ( ActionEvent e )
            {
                //
            }

            public void cancelPressed ( ActionEvent e )
            {
                WebColorChooserDialog.this.dispose ();
            }
        } );

        setResizable ( false );
        setModal ( true );
        pack ();
        setDefaultCloseOperation ( JDialog.DISPOSE_ON_CLOSE );
    }

    public void updateTitle ( String title )
    {
        if ( title == null )
        {
            setLanguage ( "weblaf.colorchooser.title" );
        }
        else
        {
            removeLanguage ();
            setTitle ( title );
        }
    }

    public int getResult ()
    {
        return colorChooser.getResult ();
    }

    public int showDialog ()
    {
        setVisible ( true );
        return getResult ();
    }

    public Color getColor ()
    {
        return colorChooser.getColor ();
    }

    public void setColor ( Color color )
    {
        colorChooser.setColor ( color );
    }

    public void setVisible ( boolean b )
    {
        if ( b )
        {
            colorChooser.resetResult ();
            setLocationRelativeTo ( getOwner () );
        }
        super.setVisible ( b );
    }
}
