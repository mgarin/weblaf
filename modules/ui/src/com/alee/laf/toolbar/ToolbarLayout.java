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

import com.alee.api.annotations.NotNull;
import com.alee.api.merge.Mergeable;
import com.alee.extended.layout.AbstractLineLayout;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;

/**
 * Custom {@link AbstractLineLayout} for {@link JToolBar} component.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "ToolBarLayout" )
public class ToolbarLayout extends AbstractLineLayout implements Mergeable, Cloneable
{
    /**
     * Constructs new {@link ToolbarLayout}.
     */
    public ToolbarLayout ()
    {
        this ( 2, 20 );
    }

    /**
     * Constructs new {@link ToolbarLayout}.
     *
     * @param spacing spacing between layout elements
     */
    public ToolbarLayout ( final int spacing )
    {
        this ( spacing, 20 );
    }

    /**
     * Constructs new {@link ToolbarLayout}.
     *
     * @param spacing      spacing between layout elements
     * @param partsSpacing spacing between {@link #START}, {@link #MIDDLE} and {@link #END} layout parts
     */
    public ToolbarLayout ( final int spacing, final int partsSpacing )
    {
        super ( spacing, partsSpacing );
    }

    @Override
    public int getOrientation ( @NotNull final Container container )
    {
        return ( ( JToolBar ) container ).getOrientation ();
    }

    /**
     * The UI resource version of {@link ToolbarLayout}.
     */
    @XStreamAlias ( "ToolBarLayout$UIResource" )
    public static final class UIResource extends ToolbarLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link ToolbarLayout}.
         */
    }
}