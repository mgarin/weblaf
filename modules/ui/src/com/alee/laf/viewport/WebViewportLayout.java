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

package com.alee.laf.viewport;

import com.alee.api.merge.Mergeable;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.laf.scroll.layout.ScrollBarSettings;
import com.alee.laf.scroll.layout.WebScrollPaneLayout;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Replacement for default Swing {@link ViewportLayout} used within {@link JViewport} to layout view.
 * It is added to support some of the {@link WebScrollPaneLayout} features for scrollbar placement.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "WebViewportLayout" )
public class WebViewportLayout extends AbstractLayoutManager implements Mergeable, Cloneable, Serializable
{
    /**
     * Called by the AWT when the specified container needs to be laid out.
     *
     * @param container the container to lay out
     * @throws AWTError if the target isn't the container specified to the {@code BoxLayout} constructor
     */
    @Override
    public void layoutContainer ( final Container container )
    {
        final JViewport viewport = ( JViewport ) container;
        final Component view = viewport.getView ();
        Scrollable scrollableView = null;

        if ( view == null )
        {
            return;
        }
        else if ( view instanceof Scrollable )
        {
            scrollableView = ( Scrollable ) view;
        }

        /**
         * All of the dimensions below are in view coordinates, except vpSize which we're converting.
         */

        final Dimension viewPrefSize = view.getPreferredSize ();
        final Dimension vpSize = viewport.getSize ();
        final Dimension extentSize = viewport.toViewCoordinates ( vpSize );
        final Dimension viewSize = new Dimension ( viewPrefSize );

        if ( scrollableView != null )
        {
            if ( scrollableView.getScrollableTracksViewportWidth () )
            {
                viewSize.width = vpSize.width;
            }
            if ( scrollableView.getScrollableTracksViewportHeight () )
            {
                viewSize.height = vpSize.height;
            }
        }

        /**
         * Adding extra width/height to the view size whenever it should include hovering scroll bar.
         */
        final Container scroll = viewport.getParent ();
        if ( scroll instanceof JScrollPane )
        {
            final JScrollPane scrollPane = ( JScrollPane ) scroll;
            final LayoutManager layout = scrollPane.getLayout ();
            if ( layout instanceof WebScrollPaneLayout )
            {
                /**
                 * Vertical scroll bar width.
                 */
                final ScrollBarSettings vpos = ( ( WebScrollPaneLayout ) layout ).getVerticalScrollBarPosition ();
                if ( vpos.isHovering () && vpos.isExtending () )
                {
                    final JScrollBar vsb = scrollPane.getVerticalScrollBar ();
                    if ( vsb != null && vsb.isShowing () )
                    {
                        viewSize.width += vsb.getPreferredSize ().width;
                    }
                }

                /**
                 * Horizontal scroll bar height.
                 */
                final ScrollBarSettings hpos = ( ( WebScrollPaneLayout ) layout ).getHorizontalScrollBarPosition ();
                if ( hpos.isHovering () && hpos.isExtending () )
                {
                    final JScrollBar hsb = scrollPane.getHorizontalScrollBar ();
                    if ( hsb != null && hsb.isShowing () )
                    {
                        viewSize.height += hsb.getPreferredSize ().height;
                    }
                }
            }
        }

        final Point viewPosition = viewport.getViewPosition ();

        /**
         * If the new viewport size would leave empty space to the right of the view, right justify the view or left justify
         * the view when the width of the view is smaller than the container.
         */
        if ( scrollableView == null ||
                viewport.getParent () == null ||
                viewport.getParent ().getComponentOrientation ().isLeftToRight () )
        {
            if ( viewPosition.x + extentSize.width > viewSize.width )
            {
                viewPosition.x = Math.max ( 0, viewSize.width - extentSize.width );
            }
        }
        else
        {
            if ( extentSize.width > viewSize.width )
            {
                viewPosition.x = viewSize.width - extentSize.width;
            }
            else
            {
                viewPosition.x = Math.max ( 0, Math.min ( viewSize.width - extentSize.width, viewPosition.x ) );
            }
        }

        /**
         * If the new viewport size would leave empty space below the view, bottom justify the view or top justify the view when
         * the height of the view is smaller than the container.
         */
        if ( viewPosition.y + extentSize.height > viewSize.height )
        {
            viewPosition.y = Math.max ( 0, viewSize.height - extentSize.height );
        }

        /**
         * If we haven't been advised about how the viewports size should change wrt to the viewport, i.e. if the view isn't
         * an instance of Scrollable, then adjust the views size as follows.
         *
         * If the origin of the view is showing and the viewport is bigger than the views preferred size, then make the view
         * the same size as the viewport.
         */
        if ( scrollableView == null )
        {
            if ( viewPosition.x == 0 && vpSize.width > viewPrefSize.width )
            {
                viewSize.width = vpSize.width;
            }
            if ( viewPosition.y == 0 && vpSize.height > viewPrefSize.height )
            {
                viewSize.height = vpSize.height;
            }
        }

        viewport.setViewPosition ( viewPosition );
        viewport.setViewSize ( viewSize );
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        final JViewport viewport = ( JViewport ) container;
        final Component view = viewport.getView ();

        /**
         * Calculating viewport preferred size.
         */
        final Dimension ps;
        if ( view == null )
        {
            ps = new Dimension ( 0, 0 );
        }
        else if ( view instanceof Scrollable )
        {
            final Scrollable scrollable = ( Scrollable ) view;
            ps = scrollable.getPreferredScrollableViewportSize ();
        }
        else
        {
            ps = view.getPreferredSize ();
        }

        /**
         * Adding extra width/height to the viewport preferred size whenever it should include hovering scroll bar.
         */
        final Container scroll = viewport.getParent ();
        if ( scroll instanceof JScrollPane )
        {
            final JScrollPane scrollPane = ( JScrollPane ) scroll;
            final LayoutManager layout = scrollPane.getLayout ();
            if ( layout instanceof WebScrollPaneLayout )
            {
                /**
                 * Vertical scroll bar width.
                 */
                final ScrollBarSettings vpos = ( ( WebScrollPaneLayout ) layout ).getVerticalScrollBarPosition ();
                if ( vpos.isHovering () && vpos.isExtending () )
                {
                    final JScrollBar vsb = scrollPane.getVerticalScrollBar ();
                    if ( vsb != null && vsb.isShowing () )
                    {
                        ps.width += vsb.getPreferredSize ().width;
                    }
                }

                /**
                 * Horizontal scroll bar height.
                 */
                final ScrollBarSettings hpos = ( ( WebScrollPaneLayout ) layout ).getHorizontalScrollBarPosition ();
                if ( hpos.isHovering () && hpos.isExtending () )
                {
                    final JScrollBar hsb = scrollPane.getHorizontalScrollBar ();
                    if ( hsb != null && hsb.isShowing () )
                    {
                        ps.height += hsb.getPreferredSize ().height;
                    }
                }
            }
        }

        return ps;
    }

    @Override
    public Dimension minimumLayoutSize ( final Container container )
    {
        return new Dimension ( 4, 4 );
    }

    /**
     * The UI resource version of {@link WebViewportLayout}.
     */
    @XStreamAlias ( "WebViewportLayout$UIResource" )
    public static final class UIResource extends WebViewportLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebViewportLayout}.
         */
    }
}