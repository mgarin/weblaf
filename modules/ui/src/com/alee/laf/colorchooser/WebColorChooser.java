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

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.ReflectUtils;
import com.alee.utils.swing.DialogOptions;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import java.awt.*;

/**
 * User: mgarin Date: 01.02.2010 Time: 15:00:20
 */

public class WebColorChooser extends JColorChooser implements DialogOptions
{
    public WebColorChooser ()
    {
        super ();
    }

    public WebColorChooser ( Color initialColor )
    {
        super ( initialColor );
    }

    public WebColorChooser ( ColorSelectionModel model )
    {
        super ( model );
    }

    public boolean isShowButtonsPanel ()
    {
        return getWebUI ().isShowButtonsPanel ();
    }

    public void setShowButtonsPanel ( boolean showButtonsPanel )
    {
        getWebUI ().setShowButtonsPanel ( showButtonsPanel );
    }

    public boolean isWebOnlyColors ()
    {
        return getWebUI ().isWebOnlyColors ();
    }

    public void setWebOnlyColors ( boolean webOnlyColors )
    {
        getWebUI ().setWebOnlyColors ( webOnlyColors );
    }

    public Color getOldColor ()
    {
        return getWebUI ().getOldColor ();
    }

    public void setOldColor ( Color oldColor )
    {
        getWebUI ().setOldColor ( oldColor );
    }

    public void resetResult ()
    {
        getWebUI ().resetResult ();
    }

    public void setResult ( int result )
    {
        getWebUI ().setResult ( result );
    }

    public int getResult ()
    {
        return getWebUI ().getResult ();
    }

    public void addColorChooserListener ( ColorChooserListener colorChooserListener )
    {
        getWebUI ().addColorChooserListener ( colorChooserListener );
    }

    public void removeColorChooserListener ( ColorChooserListener colorChooserListener )
    {
        getWebUI ().removeColorChooserListener ( colorChooserListener );
    }

    public WebColorChooserUI getWebUI ()
    {
        return ( WebColorChooserUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebColorChooserUI ) )
        {
            try
            {
                setUI ( ( WebColorChooserUI ) ReflectUtils.createInstance ( WebLookAndFeel.colorChooserUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebColorChooserUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    public static Color showDialog ( Component parent )
    {
        return showDialog ( parent, null, null );
    }

    public static Color showDialog ( Component parent, String title )
    {
        return showDialog ( parent, title, null );
    }

    public static Color showDialog ( Component parent, Color color )
    {
        return showDialog ( parent, null, color );
    }

    public static Color showDialog ( Component parent, String title, Color color )
    {
        WebColorChooserDialog wcc = new WebColorChooserDialog ( parent, title );
        if ( color != null )
        {
            wcc.setColor ( color );
        }
        wcc.setVisible ( true );
        if ( wcc.getResult () == OK_OPTION )
        {
            return wcc.getColor ();
        }
        else
        {
            return null;
        }
    }
}