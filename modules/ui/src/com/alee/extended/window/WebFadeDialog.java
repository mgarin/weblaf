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

package com.alee.extended.window;

import com.alee.global.StyleConstants;
import com.alee.laf.rootpane.WebDialog;
import com.alee.utils.MathUtils;
import com.alee.utils.swing.WebTimer;

import java.awt.*;
import java.awt.event.*;

/**
 * User: mgarin Date: 24.07.12 Time: 17:53
 */

public class WebFadeDialog extends WebDialog implements ActionListener, WindowFocusListener, WindowListener
{
    private float maximumOpacity = 0.9f;
    private float minimumOpacity = 0.3f;
    private int minimumOpacityDistance = 200;

    private float opacity = 1f;
    private WebTimer updater;

    public WebFadeDialog ()
    {
        super ();

        updater = new WebTimer ( "WebFadeDialog.updater", StyleConstants.fastAnimationDelay, this );
        addWindowFocusListener ( this );
        addWindowListener ( this );
    }

    public float getMaximumOpacity ()
    {
        return maximumOpacity;
    }

    public void setMaximumOpacity ( float maximumOpacity )
    {
        this.maximumOpacity = maximumOpacity;
    }

    public float getMinimumOpacity ()
    {
        return minimumOpacity;
    }

    public void setMinimumOpacity ( float minimumOpacity )
    {
        this.minimumOpacity = minimumOpacity;
    }

    public int getMinimumOpacityDistance ()
    {
        return minimumOpacityDistance;
    }

    public void setMinimumOpacityDistance ( int minimumOpacityDistance )
    {
        this.minimumOpacityDistance = minimumOpacityDistance;
    }

    @Override
    public void windowGainedFocus ( WindowEvent e )
    {
        updater.stop ();
        opacity = maximumOpacity;
        updateOpacity ();
    }

    @Override
    public void windowLostFocus ( WindowEvent e )
    {
        if ( isShowing () )
        {
            updater.start ();
        }
    }

    @Override
    public void windowOpened ( WindowEvent e )
    {
        //
    }

    @Override
    public void windowClosed ( WindowEvent e )
    {
        updater.stop ();
    }

    @Override
    public void windowClosing ( WindowEvent e )
    {

    }

    @Override
    public void windowIconified ( WindowEvent e )
    {

    }

    @Override
    public void windowDeiconified ( WindowEvent e )
    {

    }

    @Override
    public void windowActivated ( WindowEvent e )
    {

    }

    @Override
    public void windowDeactivated ( WindowEvent e )
    {

    }

    @Override
    public void actionPerformed ( ActionEvent e )
    {
        float newOpacity;
        if ( !WebFadeDialog.this.isActive () )
        {
            Point mp = MouseInfo.getPointerInfo ().getLocation ();
            Rectangle bounds = WebFadeDialog.this.getBounds ();
            if ( bounds.contains ( mp ) )
            {
                newOpacity = maximumOpacity;
            }
            else
            {
                int distance;
                if ( mp.y < bounds.y )
                {
                    if ( mp.x < bounds.x )
                    {
                        distance = minimumOpacityDistance -
                                ( MathUtils.sqrt ( MathUtils.sqr ( bounds.y - mp.y ) + MathUtils.sqr ( bounds.x - mp.x ) ) );
                    }
                    else if ( mp.x > bounds.x + bounds.width )
                    {
                        distance = minimumOpacityDistance -
                                ( MathUtils.sqrt ( MathUtils.sqr ( bounds.y - mp.y ) + MathUtils.sqr ( mp.x - bounds.x - bounds.width ) ) );
                    }
                    else
                    {
                        distance = minimumOpacityDistance - ( bounds.y - mp.y );
                    }
                }
                else if ( mp.y > bounds.y && mp.y < bounds.y + bounds.height )
                {
                    if ( mp.x < bounds.x )
                    {
                        distance = minimumOpacityDistance - ( bounds.x - mp.x );
                    }
                    else
                    {
                        distance = minimumOpacityDistance - ( mp.x - bounds.x - bounds.width );
                    }
                }
                else
                {
                    if ( mp.x < bounds.x )
                    {
                        distance = minimumOpacityDistance - ( MathUtils
                                .sqrt ( MathUtils.sqr ( mp.y - bounds.y - bounds.height ) + MathUtils.sqr ( bounds.x - mp.x ) ) );
                    }
                    else if ( mp.x > bounds.x + bounds.width )
                    {
                        distance = minimumOpacityDistance - ( MathUtils.sqrt ( MathUtils.sqr ( mp.y - bounds.y - bounds.height ) +
                                MathUtils.sqr ( mp.x - bounds.x - bounds.width ) ) );
                    }
                    else
                    {
                        distance = minimumOpacityDistance - ( mp.y - bounds.y - bounds.height );
                    }
                }
                newOpacity = minimumOpacity +
                        ( maximumOpacity - minimumOpacity ) * ( ( float ) Math.max ( 0, distance ) / minimumOpacityDistance );
            }
        }
        else
        {
            newOpacity = maximumOpacity;
        }

        if ( newOpacity != opacity )
        {
            opacity = newOpacity;
            updateOpacity ();
        }
    }

    private void updateOpacity ()
    {
        setWindowOpacity ( opacity );
    }
}