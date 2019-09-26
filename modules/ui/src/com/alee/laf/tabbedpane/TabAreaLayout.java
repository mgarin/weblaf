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
import com.alee.api.merge.Mergeable;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Custom layout for {@link TabArea}.
 * todo Support for leading/trailing components: https://github.com/mgarin/weblaf/issues/539
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "TabAreaLayout" )
public class TabAreaLayout extends AbstractLayoutManager implements Mergeable, Cloneable, Serializable
{
    @Override
    public void layoutContainer ( @NotNull final Container parent )
    {
        final TabArea tabArea = ( TabArea ) parent;

        // Looking for components
        // todo Replace with get(Class) in ContainerMethods
        TabViewport tabViewport = null;
        TabMenuButton tabMenuButton = null;
        for ( int i = 0; i < tabArea.getComponentCount (); i++ )
        {
            final Component component = tabArea.getComponent ( i );
            if ( component instanceof TabViewport )
            {
                tabViewport = ( TabViewport ) component;
            }
            else if ( component instanceof TabMenuButton )
            {
                tabMenuButton = ( TabMenuButton ) component;
            }
        }

        // Actual available bounds
        final Insets insets = tabArea.getInsets ();
        final Rectangle available = SwingUtils.shrink (
                new Rectangle ( 0, 0, parent.getWidth (), parent.getHeight () ),
                insets
        );

        // Placing TabMenuButton
        final int tabPlacement = tabArea.getTabbedPane ().getTabPlacement ();
        final boolean horizontal = tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM;
        if ( tabMenuButton != null && tabMenuButton.isVisible () )
        {
            final boolean ltr = tabArea.getTabbedPane ().getComponentOrientation ().isLeftToRight ();
            final Dimension mps = tabMenuButton.getPreferredSize ();
            tabMenuButton.setBounds (
                    horizontal ?
                            ltr ? available.x + available.width - mps.width : available.x :
                            available.x,
                    horizontal ?
                            available.y :
                            available.y + available.height - mps.height,
                    horizontal ?
                            mps.width :
                            available.width,
                    horizontal ?
                            available.height :
                            mps.height
            );
            available.x += horizontal && !ltr ? mps.width : 0;
            available.width -= horizontal ? mps.width : 0;
            available.height -= horizontal ? 0 : mps.height;
        }
        if ( tabViewport != null )
        {
            tabViewport.setBounds ( available );
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container parent )
    {
        final TabArea tabArea = ( TabArea ) parent;

        // Looking for components
        // todo Replace with get(Class) in ContainerMethods
        TabViewport tabViewport = null;
        TabMenuButton tabMenuButton = null;
        for ( int i = 0; i < tabArea.getComponentCount (); i++ )
        {
            final Component component = tabArea.getComponent ( i );
            if ( component instanceof TabViewport )
            {
                tabViewport = ( TabViewport ) component;
            }
            else if ( component instanceof TabMenuButton )
            {
                tabMenuButton = ( TabMenuButton ) component;
            }
        }

        // Preferred sizes
        final Dimension vps = tabViewport != null ? tabViewport.getPreferredSize () : new Dimension ();
        final Dimension mps = tabMenuButton != null && tabMenuButton.isVisible () ? tabMenuButton.getPreferredSize () : new Dimension ();

        // Resulting preferred size
        // Minimum length is either viewport preferred width or menu button 2x width to have some space left for tabs
        final Insets insets = tabArea.getInsets ();
        final int tabPlacement = tabArea.getTabbedPane ().getTabPlacement ();
        final boolean horizontal = tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM;
        return new Dimension (
                horizontal ?
                        insets.left + Math.max ( vps.width, mps.width * 2 ) + insets.right :
                        insets.left + Math.max ( vps.width, mps.width ) + insets.right,
                horizontal ?
                        insets.top + Math.max ( vps.height, mps.height ) + insets.bottom :
                        insets.top + Math.max ( vps.height, mps.height * 2 ) + insets.bottom
        );
    }

    /**
     * The UI resource version of {@link TabAreaLayout}.
     */
    @XStreamAlias ( "TabAreaLayout$UIResource" )
    public static final class UIResource extends TabAreaLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link TabAreaLayout}.
         */
    }
}