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

package com.alee.extended.overlay;

import com.alee.api.annotations.NotNull;
import com.alee.api.merge.Mergeable;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Custom layout for {@link WebOverlay} component that allows you to add overlaying components on top of another component.
 * Although it does require added overlaying component and all of its children to be non-opaque, otherwise an exception will be thrown.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "OverlayLayout" )
public class OverlayLayout extends AbstractLayoutManager implements SwingConstants, Mergeable, Cloneable, Serializable
{
    @Override
    public void layoutContainer ( @NotNull final Container parent )
    {
        final WebOverlay container = ( WebOverlay ) parent;
        final Insets overlayPadding = container.getInsets ();
        final Insets contentPadding = SwingUtils.increase ( overlayPadding, getOverlaysPadding ( container ) );
        final Rectangle contentBounds = new Rectangle (
                contentPadding.left,
                contentPadding.top,
                container.getWidth () - contentPadding.left - contentPadding.right,
                container.getHeight () - contentPadding.top - contentPadding.bottom
        );

        // Laying out content
        final JComponent content = container.getContent ();
        if ( content != null )
        {
            container.setComponentZOrder ( content, container.getComponentCount () - 1 );
            content.setBounds ( contentBounds );
        }

        // Laying out overlays
        for ( final Overlay overlay : container.getOverlays () )
        {
            // Each overlay is aligned according to it's own extra padding
            final Insets padding = overlay.margin ();
            overlay.component ().setBounds (
                    overlay.bounds (
                            container,
                            content,
                            new Rectangle (
                                    contentBounds.x - padding.left,
                                    contentBounds.y - padding.top,
                                    contentBounds.width + padding.left + padding.right,
                                    contentBounds.height + padding.top + padding.bottom
                            )
                    )
            );
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container parent )
    {
        Dimension ps = new Dimension ( 0, 0 );

        // Adding content size
        final WebOverlay container = ( WebOverlay ) parent;
        final JComponent content = container.getContent ();
        if ( content != null )
        {
            final Dimension cps = content.getPreferredSize ();
            ps = SwingUtils.maxNonNull ( ps, cps );
        }

        // Adding maximum overlay margin
        ps = SwingUtils.increase ( ps, getOverlaysPadding ( container ) );

        // Adding container insets
        SwingUtils.increase ( ps, container.getInsets () );

        return ps;
    }

    /**
     * Returns maximum overlays padding.
     *
     * @param container {@link WebOverlay}
     * @return maximum overlays padding
     */
    @NotNull
    protected Insets getOverlaysPadding ( @NotNull final WebOverlay container )
    {
        Insets maxMargin = new Insets ( 0, 0, 0, 0 );
        for ( final Overlay overlay : container.getOverlays () )
        {
            maxMargin = SwingUtils.maxNonNull ( maxMargin, overlay.margin () );
        }
        return maxMargin;
    }

    /**
     * The UI resource version of {@link OverlayLayout}.
     */
    @XStreamAlias ( "OverlayLayout$UIResource" )
    public static final class UIResource extends OverlayLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link OverlayLayout}.
         */
    }
}