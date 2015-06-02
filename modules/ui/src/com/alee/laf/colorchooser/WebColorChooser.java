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

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DialogOptions;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import java.awt.*;

/**
 * User: mgarin Date: 01.02.2010 Time: 15:00:20
 */

public class WebColorChooser extends JColorChooser implements Styleable, ShapeProvider, DialogOptions
{
    public WebColorChooser ()
    {
        super ();
    }

    public WebColorChooser ( final Color initialColor )
    {
        super ( initialColor );
    }

    public WebColorChooser ( final ColorSelectionModel model )
    {
        super ( model );
    }

    public boolean isShowButtonsPanel ()
    {
        return getWebUI ().isShowButtonsPanel ();
    }

    public void setShowButtonsPanel ( final boolean showButtonsPanel )
    {
        getWebUI ().setShowButtonsPanel ( showButtonsPanel );
    }

    public boolean isWebOnlyColors ()
    {
        return getWebUI ().isWebOnlyColors ();
    }

    public void setWebOnlyColors ( final boolean webOnlyColors )
    {
        getWebUI ().setWebOnlyColors ( webOnlyColors );
    }

    public Color getOldColor ()
    {
        return getWebUI ().getOldColor ();
    }

    public void setOldColor ( final Color oldColor )
    {
        getWebUI ().setOldColor ( oldColor );
    }

    public void resetResult ()
    {
        getWebUI ().resetResult ();
    }

    public void setResult ( final int result )
    {
        getWebUI ().setResult ( result );
    }

    public int getResult ()
    {
        return getWebUI ().getResult ();
    }

    public void addColorChooserListener ( final ColorChooserListener colorChooserListener )
    {
        getWebUI ().addColorChooserListener ( colorChooserListener );
    }

    public void removeColorChooserListener ( final ColorChooserListener colorChooserListener )
    {
        getWebUI ().removeColorChooserListener ( colorChooserListener );
    }

    /**
     * Returns color chooser painter.
     *
     * @return color chooser painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets color chooser painter.
     * Pass null to remove color chooser painter.
     *
     * @param painter new color chooser painter
     * @return this color chooser
     */
    public WebColorChooser setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        getWebUI ().setStyleId ( id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebColorChooserUI getWebUI ()
    {
        return ( WebColorChooserUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebColorChooserUI ) )
        {
            try
            {
                setUI ( ( WebColorChooserUI ) ReflectUtils.createInstance ( WebLookAndFeel.colorChooserUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebColorChooserUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    public static Color showDialog ( final Component parent )
    {
        return showDialog ( parent, null, null );
    }

    public static Color showDialog ( final Component parent, final String title )
    {
        return showDialog ( parent, title, null );
    }

    public static Color showDialog ( final Component parent, final Color color )
    {
        return showDialog ( parent, null, color );
    }

    public static Color showDialog ( final Component parent, final String title, final Color color )
    {
        final WebColorChooserDialog wcc = new WebColorChooserDialog ( parent, title );
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