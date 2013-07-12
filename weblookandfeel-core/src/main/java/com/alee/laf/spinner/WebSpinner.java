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

package com.alee.laf.spinner;

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 25.07.11 Time: 17:10
 */

public class WebSpinner extends JSpinner implements ShapeProvider, FontMethods<WebSpinner>
{
    public WebSpinner ()
    {
        super ();
    }

    public WebSpinner ( SpinnerModel model )
    {
        super ( model );
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public void setShadeWidth ( int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public void setRound ( int round )
    {
        getWebUI ().setRound ( round );
    }

    public boolean isDrawBorder ()
    {
        return getWebUI ().isDrawBorder ();
    }

    public void setDrawBorder ( boolean drawBorder )
    {
        getWebUI ().setDrawBorder ( drawBorder );
    }

    public boolean isDrawFocus ()
    {
        return getWebUI ().isDrawFocus ();
    }

    public void setDrawFocus ( boolean drawFocus )
    {
        getWebUI ().setDrawFocus ( drawFocus );
    }

    protected JComponent createEditor ( SpinnerModel model )
    {
        if ( model instanceof SpinnerDateModel )
        {
            DateEditor dateEditor = new DateEditor ( this );
            WebSpinnerUI.installFieldUI ( dateEditor.getTextField (), WebSpinner.this );
            return dateEditor;
        }
        else if ( model instanceof SpinnerListModel )
        {
            ListEditor listEditor = new ListEditor ( this );
            WebSpinnerUI.installFieldUI ( listEditor.getTextField (), WebSpinner.this );
            return listEditor;
        }
        else if ( model instanceof SpinnerNumberModel )
        {
            NumberEditor numberEditor = new NumberEditor ( this );
            WebSpinnerUI.installFieldUI ( numberEditor.getTextField (), WebSpinner.this );
            return numberEditor;
        }
        else
        {
            DefaultEditor defaultEditor = new DefaultEditor ( this );
            WebSpinnerUI.installFieldUI ( defaultEditor.getTextField (), WebSpinner.this );
            return defaultEditor;
        }
    }

    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebSpinnerUI getWebUI ()
    {
        return ( WebSpinnerUI ) getUI ();
    }

    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebSpinnerUI ) )
        {
            try
            {
                setUI ( ( WebSpinnerUI ) ReflectUtils.createInstance ( WebLookAndFeel.spinnerUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebSpinnerUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
        revalidate ();
    }

    /**
     * Font methods
     */

    /**
     * {@inheritDoc}
     */
    public WebSpinner setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebSpinner setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebSpinner setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebSpinner setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebSpinner setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebSpinner setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    public WebSpinner changeFontSize ( int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    /**
     * {@inheritDoc}
     */
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebSpinner setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebSpinner setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebSpinner setFontName ( String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    /**
     * {@inheritDoc}
     */
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }
}