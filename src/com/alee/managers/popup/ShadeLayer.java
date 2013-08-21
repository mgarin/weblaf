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

package com.alee.managers.popup;

import com.alee.extended.layout.AlignLayout;
import com.alee.laf.StyleConstants;
import com.alee.utils.LafUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 25.05.11 Time: 16:02
 */

public class ShadeLayer extends PopupLayer
{
    private boolean animate = ShadeLayerStyle.animate;

    private int opacity = 0;
    private WebTimer animator;
    private boolean blockClose = false;

    private AlignLayout layout;

    public ShadeLayer ()
    {
        super ();

        setOpaque ( false );

        layout = new AlignLayout ();
        setLayout ( layout );

        MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
            {
                if ( !blockClose )
                {
                    hideAllPopups ();
                }
            }
        };
        addMouseListener ( mouseAdapter );
        addMouseMotionListener ( mouseAdapter );
    }

    @Override
    public void showPopup ( WebPopup popup )
    {
        showPopup ( popup, false, false );
    }

    public void showPopup ( WebPopup popup, boolean hfill, boolean vfill )
    {
        // Updating layout settings
        layout.setHfill ( hfill );
        layout.setVfill ( vfill );

        // Adding popup
        removeAll ();
        add ( popup, SwingConstants.CENTER + AlignLayout.SEPARATOR + SwingConstants.CENTER, 0 );
        setBounds ( new Rectangle ( 0, 0, getParent ().getWidth (), getParent ().getHeight () ) );
        setVisible ( true );
        revalidate ();
        repaint ();
    }

    public boolean isAnimate ()
    {
        return animate;
    }

    public void setAnimate ( boolean animate )
    {
        this.animate = animate;
    }

    public boolean isBlockClose ()
    {
        return blockClose;
    }

    public void setBlockClose ( boolean blockClose )
    {
        this.blockClose = blockClose;
    }

    @Override
    public void paint ( Graphics g )
    {
        LafUtils.setupAlphaComposite ( ( Graphics2D ) g, ( float ) opacity / 100, opacity < 100 );
        super.paint ( g );
    }

    @Override
    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        Graphics2D g2d = ( Graphics2D ) g;
        Object old = LafUtils.setupAntialias ( g2d );

        Composite comp = LafUtils.setupAlphaComposite ( g2d, 0.8f );
        g2d.setPaint ( Color.LIGHT_GRAY );
        g2d.fillRect ( 0, 0, getWidth (), getHeight () );
        LafUtils.restoreComposite ( g2d, comp );

        LafUtils.restoreAntialias ( g2d, old );
    }

    @Override
    public void setVisible ( boolean visible )
    {
        super.setVisible ( visible );
        if ( visible )
        {
            stopAnimator ();
            if ( animate )
            {
                opacity = 0;
                animator = new WebTimer ( "ShadeLayer.fadeIn", StyleConstants.animationDelay, new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( ActionEvent e )
                    {
                        if ( opacity < 100 )
                        {
                            opacity += 25;
                            ShadeLayer.this.repaint ();
                        }
                        else
                        {
                            animator.stop ();
                        }
                    }
                } );
                animator.start ();
            }
            else
            {
                opacity = 100;
                ShadeLayer.this.repaint ();
            }
        }
    }

    private void stopAnimator ()
    {
        if ( animator != null )
        {
            animator.stop ();
        }
    }

    @Override
    public boolean contains ( int x, int y )
    {
        return normalContains ( x, y );
    }
}
