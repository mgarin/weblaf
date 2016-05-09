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
 * @author Mikle Garin
 */

public class VerticalFlowLayout extends AbstractLayoutManager
{
    /**
     * todo 1. Return possibility to wrap columns?
     * todo 2. Vertical alignment (in column)?
     */

    /**
     * Description of the Field
     */
    public final static int TOP = 0;
    public final static int MIDDLE = 1;
    public final static int BOTTOM = 2;

    protected int align;
    protected int hgap;
    protected int vgap;
    protected boolean hfill;
    protected boolean vfill;

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
    public VerticalFlowLayout ( final boolean hfill, final boolean vfill )
    {
        this ( TOP, 0, 0, hfill, vfill );
    }

    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt.
     */
    public VerticalFlowLayout ( final int hgap, final int vgap )
    {
        this ( TOP, hgap, vgap, true, false );
    }

    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt.
     */
    public VerticalFlowLayout ( final int align, final int hgap, final int vgap )
    {
        this ( align, hgap, vgap, true, false );
    }

    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt.
     */
    public VerticalFlowLayout ( final int align )
    {
        this ( align, 0, 0, true, false );
    }

    /**
     * Construct a new VerticalFlowLayout.
     */
    public VerticalFlowLayout ( final int align, final boolean hfill, final boolean vfill )
    {
        this ( align, 0, 0, hfill, vfill );
    }

    /**
     * Construct a new VerticalFlowLayout.
     */
    public VerticalFlowLayout ( final int hgap, final int vgap, final boolean hfill, final boolean vfill )
    {
        this ( TOP, hgap, vgap, hfill, vfill );
    }

    /**
     * Construct a new VerticalFlowLayout.
     */
    public VerticalFlowLayout ( final int align, final int hgap, final int vgap, final boolean hfill, final boolean vfill )
    {
        super ();
        this.align = align;
        this.hgap = hgap;
        this.vgap = vgap;
        this.hfill = hfill;
        this.vfill = vfill;
    }

    public void setHgap ( final int hgap )
    {
        this.hgap = hgap;
    }

    public void setVgap ( final int vgap )
    {
        this.vgap = vgap;
    }

    /**
     * Sets the VerticalFill attribute of the VerticalLayout object
     */
    public void setVerticalFill ( final boolean vfill )
    {
        this.vfill = vfill;
    }

    /**
     * Sets the HorizontalFill attribute of the VerticalLayout object
     */
    public void setHorizontalFill ( final boolean hfill )
    {
        this.hfill = hfill;
    }

    public int getHgap ()
    {
        return hgap;
    }

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


    @Override
    public Dimension preferredLayoutSize ( final Container target )
    {
        final Dimension tarsiz = new Dimension ( 0, 0 );
        for ( int i = 0; i < target.getComponentCount (); i++ )
        {
            final Component m = target.getComponent ( i );
            if ( m.isVisible () )
            {
                final Dimension d = m.getPreferredSize ();
                tarsiz.width = Math.max ( tarsiz.width, d.width );
                if ( i > 0 )
                {
                    tarsiz.height += vgap;
                }
                tarsiz.height += d.height;
            }
        }
        final Insets insets = target.getInsets ();
        tarsiz.width += insets.left + insets.right;
        tarsiz.height += insets.top + insets.bottom;
        return tarsiz;
    }

    @Override
    public Dimension minimumLayoutSize ( final Container target )
    {
        final Dimension tarsiz = new Dimension ( 0, 0 );
        for ( int i = 0; i < target.getComponentCount (); i++ )
        {
            final Component m = target.getComponent ( i );
            if ( m.isVisible () )
            {
                final Dimension d = m.getMinimumSize ();
                tarsiz.width = Math.max ( tarsiz.width, d.width );
                if ( i > 0 )
                {
                    tarsiz.height += vgap;
                }
                tarsiz.height += d.height;
            }
        }
        final Insets insets = target.getInsets ();
        tarsiz.width += insets.left + insets.right;
        tarsiz.height += insets.top + insets.bottom;
        return tarsiz;
    }

    @Override
    public void layoutContainer ( final Container target )
    {
        final Insets insets = target.getInsets ();
        final Dimension size = target.getSize ();
        final int maxwidth = size.width - ( insets.left + insets.right );
        final int maxheight = size.height - ( insets.top + insets.bottom );
        final int numcomp = target.getComponentCount ();
        final int pheight = !vfill && align != TOP ? calculatePreferredHeight ( target ) : 0;

        int y = 0;
        for ( int i = 0; i < numcomp; i++ )
        {
            final Component component = target.getComponent ( i );
            if ( component.isVisible () )
            {
                final Dimension ps = component.getPreferredSize ();
                final int w = hfill ? maxwidth : Math.min ( maxwidth, ps.width );
                final int h = vfill && i == numcomp - 1 ? maxheight - y : ps.height;
                if ( vfill )
                {
                    component.setBounds ( insets.left, insets.top + y, w, h );
                }
                else
                {
                    switch ( align )
                    {
                        case MIDDLE:
                        {
                            component.setBounds ( insets.left, insets.top + maxheight / 2 - pheight / 2 + y, w, ps.height );
                            break;
                        }
                        case BOTTOM:
                        {
                            component.setBounds ( insets.left, size.height - insets.bottom - pheight + y, w, ps.height );
                            break;
                        }
                        default:
                        {
                            component.setBounds ( insets.left, insets.top + y, w, ps.height );
                            break;
                        }
                    }
                }
                y += h + vgap;
            }
        }
    }

    /**
     * Calculates preferred height required for components within this layout.
     *
     * @param target container to calculate components preferred height for
     * @return preferred height required for components within this layout
     */
    protected int calculatePreferredHeight ( final Container target )
    {
        int ph = 0;
        for ( int i = 0; i < target.getComponentCount (); i++ )
        {
            ph += target.getComponent ( i ).getPreferredSize ().height;
            if ( i < target.getComponentCount () - 1 )
            {
                ph += vgap;
            }
        }
        return ph;
    }
}