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

package com.alee.extended.layout;

import java.awt.*;

/**
 * User: mgarin Date: 16.05.11 Time: 13:10
 */

public class VerticalFlowLayout extends FlowLayout
{
    int hgap;
    int vgap;
    boolean hfill;
    boolean vfill;

    /**
     * Description of the Field
     */
    public final static int TOP = 0;
    public final static int MIDDLE = 1;
    public final static int BOTTOM = 2;


    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt, and the fill to edge flag set.
     */
    public VerticalFlowLayout ()
    {
        this ( TOP, 0, 0, true, false );
    }


    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt.
     */
    public VerticalFlowLayout ( boolean hfill, boolean vfill )
    {
        this ( TOP, 0, 0, hfill, vfill );
    }


    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt.
     */
    public VerticalFlowLayout ( int hgap, int vgap )
    {
        this ( TOP, hgap, vgap, true, false );
    }


    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt.
     */
    public VerticalFlowLayout ( int align, int hgap, int vgap )
    {
        this ( align, hgap, vgap, true, false );
    }


    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt.
     */
    public VerticalFlowLayout ( int align )
    {
        this ( align, 0, 0, true, false );
    }


    /**
     * Construct a new VerticalFlowLayout.
     */
    public VerticalFlowLayout ( int align, boolean hfill, boolean vfill )
    {
        this ( align, 0, 0, hfill, vfill );
    }


    /**
     * Construct a new VerticalFlowLayout.
     */
    public VerticalFlowLayout ( int align, int hgap, int vgap, boolean hfill, boolean vfill )
    {
        setAlignment ( align );
        this.hgap = hgap;
        this.vgap = vgap;
        this.hfill = hfill;
        this.vfill = vfill;
    }


    /**
     * Sets the horizontal gap between components.
     */
    public void setHgap ( int hgap )
    {
        super.setHgap ( hgap );
        this.hgap = hgap;
    }


    /**
     * Sets the vertical gap between components.
     */
    public void setVgap ( int vgap )
    {
        super.setVgap ( vgap );
        this.vgap = vgap;
    }


    /**
     * Sets the VerticalFill attribute of the VerticalLayout object
     */
    public void setVerticalFill ( boolean vfill )
    {
        this.vfill = vfill;
    }


    /**
     * Sets the HorizontalFill attribute of the VerticalLayout object
     */
    public void setHorizontalFill ( boolean hfill )
    {
        this.hfill = hfill;
    }


    /**
     * Gets the horizontal gap between components.
     */
    public int getHgap ()
    {
        return hgap;
    }


    /**
     * Gets the vertical gap between components.
     */
    public int getVgap ()
    {
        return vgap;
    }


    /**
     * Gets the VerticalFill attribute of the VerticalLayout object
     */
    public boolean getVerticalFill ()
    {
        return vfill;
    }


    /**
     * Gets the HorizontalFill attribute of the VerticalLayout object
     */
    public boolean getHorizontalFill ()
    {
        return hfill;
    }


    /**
     * Returns the preferred dimensions given the components in the target container.
     */
    public Dimension preferredLayoutSize ( Container target )
    {
        Dimension tarsiz = new Dimension ( 0, 0 );

        for ( int i = 0; i < target.getComponentCount (); i++ )
        {
            Component m = target.getComponent ( i );
            if ( m.isVisible () )
            {
                Dimension d = m.getPreferredSize ();
                tarsiz.width = Math.max ( tarsiz.width, d.width );
                if ( i > 0 )
                {
                    tarsiz.height += vgap;
                }
                tarsiz.height += d.height;
            }
        }
        Insets insets = target.getInsets ();
        tarsiz.width += insets.left + insets.right;
        tarsiz.height += insets.top + insets.bottom;
        return tarsiz;
    }


    /**
     * Returns the minimum size needed to layout the target container
     */
    public Dimension minimumLayoutSize ( Container target )
    {
        Dimension tarsiz = new Dimension ( 0, 0 );

        for ( int i = 0; i < target.getComponentCount (); i++ )
        {
            Component m = target.getComponent ( i );
            if ( m.isVisible () )
            {
                Dimension d = m.getMinimumSize ();
                tarsiz.width = Math.max ( tarsiz.width, d.width );
                if ( i > 0 )
                {
                    tarsiz.height += vgap;
                }
                tarsiz.height += d.height;
            }
        }
        Insets insets = target.getInsets ();
        tarsiz.width += insets.left + insets.right;
        tarsiz.height += insets.top + insets.bottom;
        return tarsiz;
    }

    /**
     * Lays out the container.
     */
    public void layoutContainer ( Container target )
    {
        Insets insets = target.getInsets ();
        int maxheight = target.getSize ().height - ( insets.top + insets.bottom );
        int maxwidth = target.getSize ().width - ( insets.left + insets.right );
        int numcomp = target.getComponentCount ();
        int x = insets.left;
        int y = 0;
        int colw = 0;
        int start = 0;

        for ( int i = 0; i < numcomp; i++ )
        {
            Component m = target.getComponent ( i );
            if ( m.isVisible () )
            {
                Dimension d = m.getPreferredSize ();
                // fit last component to remaining height
                if ( ( this.vfill ) && ( i == ( numcomp - 1 ) ) )
                {
                    d.height = Math.max ( ( maxheight - y ), m.getPreferredSize ().
                            height );
                }
                // fit componenent size to container width
                if ( this.hfill )
                {
                    m.setSize ( maxwidth, d.height );
                    d.width = maxwidth;
                }
                else
                {
                    m.setSize ( d.width, d.height );
                }

                if ( y + d.height > maxheight )
                {
                    placethem ( target, x, insets.top, colw, maxheight - y, start, i );
                    y = d.height;
                    x += hgap + colw;
                    colw = d.width;
                    start = i;
                }
                else
                {
                    if ( y > 0 )
                    {
                        y += vgap;
                    }
                    y += d.height;
                    colw = Math.max ( colw, d.width );
                }
            }
        }
        placethem ( target, x, insets.top, colw, maxheight - y, start, numcomp );
    }


    /**
     * places the components defined by first to last within the target container using the bounds box defined
     */
    private void placethem ( Container target, int x, int y, int width, int height, int first, int last )
    {
        int align = getAlignment ();
        //if ( align == this.TOP )
        //  y = 0;
        if ( align == MIDDLE )
        {
            y += height / 2;
        }
        if ( align == BOTTOM )
        {
            y += height;
        }

        for ( int i = first; i < last; i++ )
        {
            Component m = target.getComponent ( i );
            Dimension md = m.getSize ();
            if ( m.isVisible () )
            {
                int px = x + ( width - md.width ) / 2;
                m.setLocation ( px, y );
                y += vgap + md.height;
            }
        }
    }
}