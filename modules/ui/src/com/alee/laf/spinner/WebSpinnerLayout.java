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

import com.alee.extended.layout.AbstractLayoutManager;

import java.awt.*;

/**
 * Replacement for spinner layout provided by {@link javax.swing.plaf.basic.BasicSpinnerUI}.
 * It properly provides equal space for both spinner buttons and calculates preferred size.
 * It also fixes a few minor issues and flaws in the layout.
 *
 * @author Mikle Garin
 */

public class WebSpinnerLayout extends AbstractLayoutManager
{
    /**
     * Editor layout constraint.
     */
    public static final String EDITOR = "Editor";

    /**
     * Next (down) button layout constraint.
     */
    public static final String NEXT = "Next";

    /**
     * Previous (up) button layout constraint.
     */
    public static final String PREVIOUS = "Previous";

    /**
     * Editor component.
     */
    protected Component editor = null;

    /**
     * Next (down) button.
     */
    protected Component nextButton = null;

    /**
     * Previous (up) button.
     */
    protected Component previousButton = null;

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        if ( EDITOR.equals ( constraints ) )
        {
            editor = component;
        }
        else if ( NEXT.equals ( constraints ) )
        {
            nextButton = component;
        }
        else if ( PREVIOUS.equals ( constraints ) )
        {
            previousButton = component;
        }
    }

    @Override
    public void removeComponent ( final Component component )
    {
        if ( component == editor )
        {
            editor = null;
        }
        else if ( component == nextButton )
        {
            nextButton = null;
        }
        else if ( component == previousButton )
        {
            previousButton = null;
        }
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        final Insets b = parent.getInsets ();
        final Dimension s = parent.getSize ();
        final Dimension next = nextButton != null ? nextButton.getPreferredSize () : new Dimension ( 0, 0 );
        final Dimension prev = previousButton != null ? previousButton.getPreferredSize () : new Dimension ( 0, 0 );
        final int bw = Math.max ( next.width, prev.width );
        final int bah = s.height - b.top - b.bottom;
        final int nh = bah % 2 == 0 ? bah / 2 : ( bah - 1 ) / 2 + 1;
        final int ph = bah % 2 == 0 ? bah / 2 : ( bah - 1 ) / 2;
        final boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        if ( editor != null )
        {
            editor.setBounds ( b.left + ( ltr ? 0 : bw ), b.top, s.width - b.left - b.right - bw, s.height - b.top - b.bottom );
        }
        if ( nextButton != null )
        {
            nextButton.setBounds ( ltr ? s.width - b.right - bw : b.left, b.top, bw, nh );
        }
        if ( previousButton != null )
        {
            previousButton.setBounds ( ltr ? s.width - b.right - bw : b.left, b.top + nh, bw, ph );
        }
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        final Insets b = parent.getInsets ();
        final Dimension ed = editor != null ? editor.getPreferredSize () : new Dimension ( 0, 0 );
        final Dimension next = nextButton != null ? nextButton.getPreferredSize () : new Dimension ( 0, 0 );
        final Dimension prev = previousButton != null ? previousButton.getPreferredSize () : new Dimension ( 0, 0 );
        final int w = b.left + ed.width + Math.max ( next.width, prev.width ) + b.right;
        final int h = b.top + Math.max ( ed.height, next.height + prev.height ) + b.bottom;
        return new Dimension ( w, h );
    }
}