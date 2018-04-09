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
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This special popup layer is used to place modal-like popups atop of it.
 * It basically provides a semi-transparent layer that covers the whole window and leaves only modal popup in focus.
 *
 * @author Mikle Garin
 * @see PopupManager
 */
public class ShadeLayer extends PopupLayer
{
    /**
     * Whether modal shade should be animated or not.
     * It might cause serious lags in case it is used in a large window with lots of UI elements.
     */
    protected boolean animate = true;

    /**
     * Layer current opacity.
     */
    protected int opacity = 0;

    /**
     * Layer opacity animator.
     */
    protected WebTimer animator;

    /**
     * Whether popup close attemps should be blocked or not.
     */
    protected boolean blockClose = false;

    /**
     * Constructs new shade layer.
     */
    public ShadeLayer ()
    {
        super ( new AlignLayout () );

        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
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
    public void showPopup ( final WebInnerPopup popup )
    {
        showPopup ( popup, false, false );
    }

    /**
     * Displays the specified popup on this popup layer.
     *
     * @param popup popup to display
     * @param hfill whether popup should fill the whole available window width or not
     * @param vfill whether popup should fill the whole available window height or not
     */
    public void showPopup ( final WebInnerPopup popup, final boolean hfill, final boolean vfill )
    {
        // Informing that popup will now become visible
        popup.firePopupWillBeOpened ();

        // Updating layout settings
        final LayoutManager layoutManager = getLayout ();
        if ( layoutManager instanceof AlignLayout )
        {
            final AlignLayout layout = ( AlignLayout ) layoutManager;
            layout.setHfill ( hfill );
            layout.setVfill ( vfill );
        }

        // Updating popup layer
        setBounds ( new Rectangle ( 0, 0, getParent ().getWidth (), getParent ().getHeight () ) );

        // Adding popup
        removeAll ();
        add ( popup, SwingConstants.CENTER + AlignLayout.SEPARATOR + SwingConstants.CENTER, 0 );
        setVisible ( true );
        revalidate ();
        repaint ();
    }

    /**
     * Returns whether modal shade should be animated or not.
     *
     * @return true if modal shade should be animated, false otherwise
     */
    public boolean isAnimate ()
    {
        return animate;
    }

    /**
     * Sets whether modal shade should be animated or not.
     *
     * @param animate whether modal shade should be animated or not
     */
    public void setAnimate ( final boolean animate )
    {
        this.animate = animate;
    }

    /**
     * Returns whether popup close attemps should be blocked or not.
     *
     * @return true if popup close attemps should be blocked, false otherwise
     */
    public boolean isBlockClose ()
    {
        return blockClose;
    }

    /**
     * Sets whether popup close attemps should be blocked or not.
     *
     * @param blockClose whether popup close attemps should be blocked or not
     */
    public void setBlockClose ( final boolean blockClose )
    {
        this.blockClose = blockClose;
    }

    @Override
    public void paint ( final Graphics g )
    {
        // todo Really bad workaround
        GraphicsUtils.setupAlphaComposite ( ( Graphics2D ) g, ( float ) opacity / 100, opacity < 100 );
        super.paint ( g );
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        final Object old = GraphicsUtils.setupAntialias ( g2d );
        final Composite comp = GraphicsUtils.setupAlphaComposite ( g2d, 0.8f );

        g2d.setPaint ( Color.LIGHT_GRAY );
        g2d.fillRect ( 0, 0, getWidth (), getHeight () );

        GraphicsUtils.restoreComposite ( g2d, comp );
        GraphicsUtils.restoreAntialias ( g2d, old );
    }

    @Override
    public void setVisible ( final boolean visible )
    {
        super.setVisible ( visible );
        if ( visible )
        {
            if ( animator != null )
            {
                animator.stop ();
            }
            if ( animate )
            {
                opacity = 0;
                animator = new WebTimer ( "ShadeLayer.fadeIn", SwingUtils.frameRateDelay ( 24 ), new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
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

    /**
     * Returns whether the specified point is within bounds of this popup layer or not.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if the specified point is within bounds of this popup layer, false otherwise
     */
    @Override
    public boolean contains ( final int x, final int y )
    {
        return normalContains ( x, y );
    }
}