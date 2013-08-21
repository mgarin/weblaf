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

import com.alee.laf.StyleConstants;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 11.04.12 Time: 17:30
 */

public class SlidingLayout implements LayoutManager
{
    private int slideY = 0;
    private WebTimer animator = null;
    private int height = 0;
    private int slideSpeed = 5;

    private JComponent container;

    public SlidingLayout ( JComponent container )
    {
        super ();
        this.container = container;
    }

    public void slideIn ()
    {
        if ( animator != null && animator.isRunning () )
        {
            animator.stop ();
        }

        slideY = 0;
        animator = new WebTimer ( "SlidingLayout.slideInTimer", StyleConstants.avgAnimationDelay, new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( slideY < height )
                {
                    slideY += slideSpeed;
                    container.revalidate ();
                }
                else
                {
                    slideY = height;
                    animator.stop ();
                }
            }
        } );
        animator.start ();
    }

    public int getSlideSpeed ()
    {
        return slideSpeed;
    }

    public void setSlideSpeed ( int slideSpeed )
    {
        this.slideSpeed = slideSpeed;
    }

    public void slideOut ()
    {
        if ( animator != null && animator.isRunning () )
        {
            animator.stop ();
        }

        slideY = height;
        animator = new WebTimer ( "SlidingLayout.slideOutTimer", StyleConstants.avgAnimationDelay, new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( slideY > 0 )
                {
                    slideY -= slideSpeed;
                    container.revalidate ();
                }
                else
                {
                    slideY = 0;
                    animator.stop ();
                }
            }
        } );
        animator.start ();
    }

    @Override
    public void addLayoutComponent ( String name, Component comp )
    {
        //
    }

    @Override
    public void removeLayoutComponent ( Component comp )
    {
        //
    }

    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        Dimension ps = new Dimension ( 0, 0 );
        for ( Component c : parent.getComponents () )
        {
            ps = SwingUtils.max ( ps, c.getPreferredSize () );
        }
        ps.height = slideY < ps.height ? slideY : ps.height;
        return ps;
    }

    @Override
    public Dimension minimumLayoutSize ( Container parent )
    {
        return preferredLayoutSize ( parent );
    }

    @Override
    public void layoutContainer ( Container parent )
    {
        for ( Component c : parent.getComponents () )
        {
            Dimension ps = c.getPreferredSize ();
            c.setBounds ( 0, slideY < ps.height ? slideY - ps.height : 0, parent.getWidth (), ps.height );
            height = Math.max ( height, ps.height );
        }
    }
}