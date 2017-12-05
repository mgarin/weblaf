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

import com.alee.laf.window.WebDialog;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import java.awt.*;
import java.awt.event.*;

/**
 * @author Mikle Garin
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

        updater = new WebTimer ( "WebFadeDialog.updater", SwingUtils.frameRateDelay ( 48 ), this );
        addWindowFocusListener ( this );
        addWindowListener ( this );
    }

    public float getMaximumOpacity ()
    {
        return maximumOpacity;
    }

    public void setMaximumOpacity ( final float maximumOpacity )
    {
        this.maximumOpacity = maximumOpacity;
    }

    public float getMinimumOpacity ()
    {
        return minimumOpacity;
    }

    public void setMinimumOpacity ( final float minimumOpacity )
    {
        this.minimumOpacity = minimumOpacity;
    }

    public int getMinimumOpacityDistance ()
    {
        return minimumOpacityDistance;
    }

    public void setMinimumOpacityDistance ( final int minimumOpacityDistance )
    {
        this.minimumOpacityDistance = minimumOpacityDistance;
    }

    @Override
    public void windowGainedFocus ( final WindowEvent e )
    {
        updater.stop ();
        opacity = maximumOpacity;
        updateOpacity ();
    }

    @Override
    public void windowLostFocus ( final WindowEvent e )
    {
        if ( isShowing () )
        {
            updater.start ();
        }
    }

    @Override
    public void windowOpened ( final WindowEvent e )
    {
        //
    }

    @Override
    public void windowClosed ( final WindowEvent e )
    {
        updater.stop ();
    }

    @Override
    public void windowClosing ( final WindowEvent e )
    {

    }

    @Override
    public void windowIconified ( final WindowEvent e )
    {

    }

    @Override
    public void windowDeiconified ( final WindowEvent e )
    {

    }

    @Override
    public void windowActivated ( final WindowEvent e )
    {

    }

    @Override
    public void windowDeactivated ( final WindowEvent e )
    {

    }

    @Override
    public void actionPerformed ( final ActionEvent e )
    {
        final float newOpacity;
        if ( !WebFadeDialog.this.isActive () )
        {
            final Point mp = CoreSwingUtils.getMouseLocation ();
            final Rectangle bounds = WebFadeDialog.this.getBounds ();
            if ( bounds.contains ( mp ) )
            {
                newOpacity = maximumOpacity;
            }
            else
            {
                final int distance;
                if ( mp.y < bounds.y )
                {
                    if ( mp.x < bounds.x )
                    {
                        distance = minimumOpacityDistance -
                                MathUtils.sqrt ( MathUtils.sqr ( bounds.y - mp.y ) + MathUtils.sqr ( bounds.x - mp.x ) );
                    }
                    else if ( mp.x > bounds.x + bounds.width )
                    {
                        distance = minimumOpacityDistance -
                                MathUtils.sqrt ( MathUtils.sqr ( bounds.y - mp.y ) + MathUtils.sqr ( mp.x - bounds.x - bounds.width ) );
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
                        distance = minimumOpacityDistance -
                                MathUtils.sqrt ( MathUtils.sqr ( mp.y - bounds.y - bounds.height ) + MathUtils.sqr ( bounds.x - mp.x ) );
                    }
                    else if ( mp.x > bounds.x + bounds.width )
                    {
                        distance = minimumOpacityDistance - MathUtils.sqrt ( MathUtils.sqr ( mp.y - bounds.y - bounds.height ) +
                                MathUtils.sqr ( mp.x - bounds.x - bounds.width ) );
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