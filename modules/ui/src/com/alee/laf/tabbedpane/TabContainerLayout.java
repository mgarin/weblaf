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

package com.alee.laf.tabbedpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.merge.Mergeable;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.SizeCache;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Custom layout for {@link TabContainer}.
 * Unlike default Swing tab layout this one uses different wrapping and positioning logic.
 * All tabs are laid out from left to right and from top to bottom in {@link JTabbedPane#TOP} and {@link JTabbedPane#BOTTOM} tab placements.
 * In {@link JTabbedPane#LEFT} and {@link JTabbedPane#RIGHT} tab placements tabs are laid out from top to bottom and from left to right.
 * Positions are mirrored for RTL orientation if it is applied to the {@link JTabbedPane}.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "TabContainerLayout" )
public class TabContainerLayout extends AbstractLayoutManager implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link TabStretchType}.
     */
    @Nullable
    @XStreamAlias ( "tabStretchType" )
    protected TabStretchType tabStretchType;

    /**
     * Total run count.
     */
    protected transient int runCount = 1;

    /**
     * Returns {@link TabStretchType}.
     *
     * @return {@link TabStretchType}
     */
    @NotNull
    public TabStretchType getTabStretchType ()
    {
        return tabStretchType != null ? tabStretchType : TabStretchType.multiline;
    }

    /**
     * Returns cached tab run count.
     *
     * @return cached tab run count
     */
    public int getRunCount ()
    {
        return runCount;
    }

    @Override
    public void layoutContainer ( @NotNull final Container parent )
    {
        final TabContainer tabContainer = ( TabContainer ) parent;
        final SizeCache sizeCache = new SizeCache ( tabContainer );
        final Insets insets = tabContainer.getInsets ();
        final int tabPlacement = tabContainer.getTabbedPane ().getTabPlacement ();
        final boolean horizontal = tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM;
        final int tabCount = tabContainer.getComponentCount ();
        final TabStretchType tabStretchType = getTabStretchType ();
        final int width = tabContainer.getWidth () - insets.left - insets.right;
        final int height = tabContainer.getHeight () - insets.top - insets.bottom;
        int runWidth = 0;
        int runHeight = 0;
        int runLength = 0;
        int runStartIndex = 0;
        int x = insets.left;
        int y = insets.top;
        if ( tabCount > 0 )
        {
            int runCount = 0;
            final boolean ltr = tabContainer.getTabbedPane ().getComponentOrientation ().isLeftToRight ();
            for ( int i = 0; i < tabCount; i++ )
            {
                final Dimension cps = sizeCache.preferred ( tabContainer, i );
                runWidth = horizontal ? runWidth + cps.width : Math.max ( runWidth, cps.width );
                runHeight = horizontal ? Math.max ( runHeight, cps.height ) : runHeight + cps.height;
                runLength += horizontal ? cps.width : cps.height;

                if ( i == tabCount - 1 ||
                        horizontal && runWidth + sizeCache.preferred ( tabContainer, i + 1 ).width > width ||
                        !horizontal && runHeight + sizeCache.preferred ( tabContainer, i + 1 ).height > height )
                {
                    final int runComponentCount = 1 + i - runStartIndex;
                    for ( int j = runStartIndex; j <= i; j++ )
                    {
                        final Tab tab = ( Tab ) tabContainer.getComponent ( j );
                        final Dimension tps = sizeCache.preferred ( tab );
                        if ( horizontal )
                        {
                            final int w;
                            if ( tabStretchType == TabStretchType.always ||
                                    tabStretchType == TabStretchType.multiline && ( i < tabCount - 1 || runCount > 0 ) )
                            {
                                final int freeWidth = width - runLength;
                                final int bonus = j - runStartIndex < freeWidth % runComponentCount ? 1 : 0;
                                w = tps.width + freeWidth / runComponentCount + bonus;
                            }
                            else
                            {
                                w = tps.width;
                            }

                            tab.setBounds ( ltr ? x : insets.left + width - ( x - insets.left ) - w, y, w, runHeight );
                            x += w;
                        }
                        else
                        {
                            final int h;
                            if ( tabStretchType == TabStretchType.always ||
                                    tabStretchType == TabStretchType.multiline && ( i < tabCount - 1 || runCount > 0 ) )
                            {
                                final int freeHeight = height - runLength;
                                final int bonus = j - runStartIndex < freeHeight % runComponentCount ? 1 : 0;
                                h = tps.height + freeHeight / runComponentCount + bonus;
                            }
                            else
                            {
                                h = tps.height;
                            }

                            tab.setBounds ( x, y, runWidth, h );
                            y += h;
                        }
                    }

                    x = horizontal ? insets.left : x + runWidth;
                    y = horizontal ? y + runHeight : insets.top;

                    runWidth = 0;
                    runHeight = 0;
                    runLength = 0;
                    runStartIndex = i + 1;
                    runCount++;
                }
            }
            this.runCount = runCount;
        }
        else
        {
            // There is always one tab run
            // Even if there is no tabs at all
            this.runCount = 1;
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container parent )
    {
        final Dimension ps = new Dimension ( 0, 0 );
        final TabContainer tabContainer = ( TabContainer ) parent;
        final SizeCache sizeCache = new SizeCache ( tabContainer );
        final Insets insets = tabContainer.getInsets ();
        final int tabLayoutPolicy = tabContainer.getTabbedPane ().getTabLayoutPolicy ();
        final int tabPlacement = tabContainer.getTabbedPane ().getTabPlacement ();
        final boolean horizontal = tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM;
        if ( tabContainer.getWidth () == 0 || tabLayoutPolicy == JTabbedPane.SCROLL_TAB_LAYOUT )
        {
            for ( int i = 0; i < tabContainer.getComponentCount (); i++ )
            {
                final Dimension cps = sizeCache.preferred ( tabContainer, i );
                ps.width = horizontal ? ps.width + cps.width : Math.max ( ps.width, cps.width );
                ps.height = horizontal ? Math.max ( ps.height, cps.height ) : ps.height + cps.height;
            }
        }
        else
        {
            final int width = tabContainer.getWidth () - insets.left - insets.right;
            final int height = tabContainer.getHeight () - insets.top - insets.bottom;
            int runWidth = 0;
            int runHeight = 0;
            for ( int i = 0; i < tabContainer.getComponentCount (); i++ )
            {
                final Dimension cps = sizeCache.preferred ( tabContainer, i );
                runWidth = horizontal ? runWidth + cps.width : Math.max ( runWidth, cps.width );
                runHeight = horizontal ? Math.max ( runHeight, cps.height ) : runHeight + cps.height;

                if ( i == tabContainer.getComponentCount () - 1 ||
                        horizontal && runWidth + sizeCache.preferred ( tabContainer, i + 1 ).width > width ||
                        !horizontal && runHeight + sizeCache.preferred ( tabContainer, i + 1 ).height > height )
                {
                    ps.width = horizontal ? Math.min ( Math.max ( ps.width, runWidth ), width ) : ps.width + runWidth;
                    ps.height = horizontal ? ps.height + runHeight : Math.min ( Math.min ( ps.height, runHeight ), height );
                    runWidth = 0;
                    runHeight = 0;
                }
            }
        }
        SwingUtils.increase ( ps, insets );
        return ps;
    }

    /**
     * The UI resource version of {@link TabContainerLayout}.
     */
    @XStreamAlias ( "TabContainerLayout$UIResource" )
    public static final class UIResource extends TabContainerLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link TabContainerLayout}.
         */
    }
}