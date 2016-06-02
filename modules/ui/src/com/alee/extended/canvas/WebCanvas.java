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

package com.alee.extended.canvas;

import com.alee.extended.WebComponent;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleableComponent;

import javax.swing.*;

/**
 * Simple canvas implementation.
 * Unlike {@link java.awt.Canvas} it is based on {@link javax.swing.JComponent} and supports customizeable UI and painter.
 * <p>
 * Component itself doesn't contain any customizable data so any custom painter can be easily provided for it.
 * It exists to prevent creation of multiple helper components for various small UI elements performing simple tasks.
 *
 * @author Mikle Garin
 */

public class WebCanvas extends WebComponent<WebCanvasUI, WebCanvas>
{
    /**
     * Constructs new canvas.
     */
    public WebCanvas ()
    {
        this ( StyleId.canvas );
    }

    /**
     * Constructs new canvas.
     *
     * @param id style ID
     */
    public WebCanvas ( final StyleId id )
    {
        super ();
        updateUI ();
        setStyleId ( id );
    }

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the {@link com.alee.extended.canvas.CanvasUI} object that renders this component
     */
    public CanvasUI getUI ()
    {
        return ( CanvasUI ) ui;
    }

    /**
     * Sets the L&amp;F object that renders this component.
     *
     * @param ui {@link com.alee.extended.canvas.CanvasUI}
     */
    public void setUI ( final CanvasUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public WebCanvasUI getWebUI ()
    {
        return ( WebCanvasUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebCanvasUI ) )
        {
            try
            {
                setUI ( ( WebCanvasUI ) UIManager.getUI ( this ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebCanvasUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public String getUIClassID ()
    {
        return StyleableComponent.canvas.getUIClassID ();
    }
}