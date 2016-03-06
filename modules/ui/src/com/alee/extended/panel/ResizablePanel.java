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

package com.alee.extended.panel;

import com.alee.laf.panel.WebPanel;
import com.alee.utils.LafUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.SwingConstants.*;

/**
 * @author Mikle Garin
 */

public class ResizablePanel extends WebPanel
{
    public static final int SIZER = 12;

    private int widthChange = 0;
    private int heightChange = 0;

    private int resizeCorner = -1;
    private boolean resizing = false;
    private int startX = -1;
    private int startY = -1;

    private Insets innerSpacing = new Insets ( SIZER, SIZER, SIZER, SIZER );

    public ResizablePanel ()
    {
        this ( null );
    }

    public ResizablePanel ( final Component component )
    {
        super ();

        setOpaque ( false );
        updateSpacing ();

        if ( component != null )
        {
            setLayout ( new BorderLayout () );
            add ( component, BorderLayout.CENTER );
        }

        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            private void updateResizeType ( final MouseEvent e )
            {
                if ( new Rectangle ( 0, 0, SIZER, SIZER ).contains ( e.getPoint () ) )
                {
                    resizeCorner = NORTH_WEST;
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.NW_RESIZE_CURSOR ) );
                }
                else if ( new Rectangle ( getWidth () / 2 - SIZER / 2, 0, SIZER, SIZER ).contains ( e.getPoint () ) )
                {
                    resizeCorner = NORTH;
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.N_RESIZE_CURSOR ) );
                }
                else if ( new Rectangle ( getWidth () - SIZER, 0, SIZER, SIZER ).contains ( e.getPoint () ) )
                {
                    resizeCorner = NORTH_EAST;
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.NE_RESIZE_CURSOR ) );
                }
                else if ( new Rectangle ( 0, getHeight () / 2 - SIZER / 2, SIZER, SIZER ).contains ( e.getPoint () ) )
                {
                    resizeCorner = WEST;
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.W_RESIZE_CURSOR ) );
                }
                else if ( new Rectangle ( getWidth () - SIZER, getHeight () / 2 - SIZER / 2, SIZER, SIZER ).contains ( e.getPoint () ) )
                {
                    resizeCorner = EAST;
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.E_RESIZE_CURSOR ) );
                }
                else if ( new Rectangle ( 0, getHeight () - SIZER, SIZER, SIZER ).contains ( e.getPoint () ) )
                {
                    resizeCorner = SOUTH_WEST;
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.SW_RESIZE_CURSOR ) );
                }
                else if ( new Rectangle ( getWidth () / 2 - SIZER / 2, getHeight () - SIZER, SIZER, SIZER ).contains ( e.getPoint () ) )
                {
                    resizeCorner = SOUTH;
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.S_RESIZE_CURSOR ) );
                }
                else if ( new Rectangle ( getWidth () - SIZER, getHeight () - SIZER, SIZER, SIZER ).contains ( e.getPoint () ) )
                {
                    resizeCorner = SOUTH_EAST;
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.SE_RESIZE_CURSOR ) );
                }
                else
                {
                    resizeCorner = -1;
                    setCursor ( Cursor.getDefaultCursor () );
                }
            }

            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                updateResizeType ( e );
            }

            @Override
            public void mousePressed ( final MouseEvent e )
            {
                updateResizeType ( e );
                if ( SwingUtilities.isLeftMouseButton ( e ) && resizeCorner != -1 )
                {
                    resizing = true;
                }
                startX = e.getXOnScreen ();
                startY = e.getYOnScreen ();
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                if ( resizing && startX != -1 && startY != -1 )
                {
                    widthChange += ( startX - e.getXOnScreen () ) * 2 *
                            ( resizeCorner == NORTH || resizeCorner == SOUTH ? 0 : resizeCorner == NORTH_WEST || resizeCorner == WEST ||
                                    resizeCorner == SOUTH_WEST ? 1 : -1 );
                    widthChange = Math.max ( widthChange, 0 );

                    heightChange += ( startY - e.getYOnScreen () ) * 2 *
                            ( resizeCorner == WEST || resizeCorner == EAST ? 0 : resizeCorner == NORTH_WEST || resizeCorner == NORTH ||
                                    resizeCorner == NORTH_EAST ? 1 : -1 );
                    heightChange = Math.max ( heightChange, 0 );

                    revalidate ();
                }
                startX = e.getXOnScreen ();
                startY = e.getYOnScreen ();
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) )
                {
                    resizing = false;
                }
                updateResizeType ( e );
            }


        };
        addMouseListener ( mouseAdapter );
        addMouseMotionListener ( mouseAdapter );
    }

    private void updateSpacing ()
    {
        setPadding ( innerSpacing.top, innerSpacing.left, innerSpacing.bottom, innerSpacing.right );
    }

    public Insets getInnerSpacing ()
    {
        return innerSpacing;
    }

    public void setInnerSpacing ( final int innerSpacing )
    {
        setInnerSpacing ( new Insets ( innerSpacing, innerSpacing, innerSpacing, innerSpacing ) );
    }

    public void setInnerSpacing ( final Insets innerSpacing )
    {
        this.innerSpacing = innerSpacing;
        updateSpacing ();
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        LafUtils.drawWebIconedSelection ( ( Graphics2D ) g, new Rectangle ( 6, 6, getWidth () - 13, getHeight () - 13 ), true, true, true );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension ps = super.getPreferredSize ();
        ps.width = ps.width + widthChange;
        ps.height = ps.height + heightChange;
        return ps;
    }

    public void reset ()
    {
        widthChange = 0;
        heightChange = 0;
        revalidate ();
    }
}