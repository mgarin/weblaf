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

package com.alee.laf.toolbar;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Custom component to create spacings where layout can't do the stuff you need.
 * It is basically better to use this, instead of combining two containers with two layouts.
 * The best case is of course when layout handles the spacing, but its not always that easy.
 *
 * @author Mikle Garin
 */

public class WhiteSpace extends JComponent implements SwingConstants
{
    /**
     * Spacing amount in px.
     */
    private int spacing;

    /**
     * Spacing orientation.
     */
    private int orientation;

    /**
     * Constructs new spacer.
     */
    public WhiteSpace ()
    {
        this ( 2 );
    }

    /**
     * Constructs new spacer.
     *
     * @param spacing space amount
     */
    public WhiteSpace ( final int spacing )
    {
        this ( spacing, -1 );
    }

    /**
     * Constructs new spacer.
     *
     * @param spacing     space amount
     * @param orientation space orientation
     */
    public WhiteSpace ( final int spacing, final int orientation )
    {
        super ();
        SwingUtils.setOrientation ( this );
        setSpacing ( spacing );
        setOrientation ( orientation );
    }

    /**
     * Returns spacing orientation.
     *
     * @return spacing orientation
     */
    public int getOrientation ()
    {
        return orientation;
    }

    /**
     * Sets spacing orientation.
     * Specify {@code -1} to have spacing for both orientations.
     *
     * @param orientation new spacing orientation
     */
    public void setOrientation ( final int orientation )
    {
        this.orientation = orientation;
    }

    /**
     * Returns spacing amount in px.
     *
     * @return spacing amount in px
     */
    public int getSpacing ()
    {
        return spacing;
    }

    /**
     * Sets spacing amount in px.
     *
     * @param spacing new spacing amount in px
     */
    public void setSpacing ( final int spacing )
    {
        this.spacing = spacing;
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Container container = getParent ();
        if ( container != null && container.getLayout () instanceof ToolbarLayout )
        {
            final ToolbarLayout layout = ( ToolbarLayout ) container.getLayout ();
            return new Dimension ( layout.getOrientation () != VERTICAL ? spacing : 0,
                    layout.getOrientation () != HORIZONTAL ? spacing : 0 );
        }
        else
        {
            return new Dimension ( orientation != VERTICAL ? spacing : 0, orientation != HORIZONTAL ? spacing : 0 );
        }
    }
}