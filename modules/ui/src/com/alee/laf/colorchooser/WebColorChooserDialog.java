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

import com.alee.laf.window.WebDialog;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.swing.DialogOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Mikle Garin
 */
public class WebColorChooserDialog extends WebDialog implements DialogOptions
{
    public static final ImageIcon COLOR_CHOOSER_ICON =
            new ImageIcon ( WebColorChooserDialog.class.getResource ( "icons/color_chooser.png" ) );

    private final WebColorChooser colorChooser;

    public WebColorChooserDialog ()
    {
        this ( new WebColorChooser () );
    }

    public WebColorChooserDialog ( final Component parent )
    {
        this ( new WebColorChooser (), parent );
    }

    public WebColorChooserDialog ( final String title )
    {
        this ( new WebColorChooser (), title );
    }

    public WebColorChooserDialog ( final Component parent, final String title )
    {
        this ( new WebColorChooser (), parent, title );
    }

    public WebColorChooserDialog ( final WebColorChooser webColorChooser )
    {
        this ( webColorChooser, null, null );
    }

    public WebColorChooserDialog ( final WebColorChooser webColorChooser, final Component parent )
    {
        this ( webColorChooser, parent, null );
    }

    public WebColorChooserDialog ( final WebColorChooser webColorChooser, final String title )
    {
        this ( webColorChooser, null, title );
    }

    public WebColorChooserDialog ( final WebColorChooser webColorChooser, final Component parent, final String title )
    {
        super ( CoreSwingUtils.getWindowAncestor ( parent ), title != null ? title : "weblaf.colorchooser.title" );
        setIconImage ( COLOR_CHOOSER_ICON.getImage () );
        setLayout ( new BorderLayout ( 0, 0 ) );

        colorChooser = webColorChooser;
        colorChooser.setOpaque ( false );
        colorChooser.setShowButtonsPanel ( true );
        getContentPane ().add ( colorChooser, BorderLayout.CENTER );

        addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowClosed ( final WindowEvent e )
            {
                colorChooser.setResult ( CLOSE_OPTION );
            }
        } );

        colorChooser.addColorChooserListener ( new ColorChooserListener ()
        {
            @Override
            public void okPressed ( final ActionEvent e )
            {
                WebColorChooserDialog.this.dispose ();
            }

            @Override
            public void resetPressed ( final ActionEvent e )
            {
                //
            }

            @Override
            public void cancelPressed ( final ActionEvent e )
            {
                WebColorChooserDialog.this.dispose ();
            }
        } );

        setResizable ( false );
        setModal ( true );
        pack ();
        setDefaultCloseOperation ( JDialog.DISPOSE_ON_CLOSE );
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

    public void setColor ( final Color color )
    {
        colorChooser.setColor ( color );
    }

    @Override
    public void setVisible ( final boolean b )
    {
        if ( b )
        {
            colorChooser.resetResult ();
            setLocationRelativeTo ( getOwner () );
        }
        super.setVisible ( b );
    }
}