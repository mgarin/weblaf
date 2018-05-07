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

package com.alee.laf.scroll.layout;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.Serializable;

/**
 * Replacement for default Swing {@link ScrollPaneLayout} used within {@link JScrollPane} to layout components.
 * It allows deeper customization of scroll bar placement and a few enhancements to default calculations.
 *
 * Note: The only reason why this layout is based on {@link ScrollPaneLayout} is because {@link JScrollPane#setLayout(LayoutManager)}
 * method accepts only {@link ScrollPaneLayout}-based layouts and nothing else. Unfortunately there is no good workaround so it have been
 * used as a base for this layout and all unserializable fields have been emptied and omitted for XStream.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "WebScrollPaneLayout" )
public class WebScrollPaneLayout extends ScrollPaneLayout implements ScrollPaneConstants, Mergeable, Cloneable, Serializable
{
    /**
     * {@link ScrollBarSettings} for vertical scroll bar.
     * These settings must always be specified otherwise you will have {@link NullPointerException}.
     */
    @XStreamAlias ( "Vertical" )
    protected ScrollBarSettings vpos;

    /**
     * {@link ScrollBarSettings} for horizontal scroll bar.
     * These settings must always be specified otherwise you will have {@link NullPointerException}.
     */
    @XStreamAlias ( "Horizontal" )
    protected ScrollBarSettings hpos;

    /**
     * Returns {@link ScrollBarSettings} for vertical scroll bar.
     *
     * @return {@link ScrollBarSettings} for vertical scroll bar
     */
    public ScrollBarSettings getVerticalScrollBarPosition ()
    {
        return vpos;
    }

    /**
     * Sets {@link ScrollBarSettings} for vertical scroll bar.
     *
     * @param position {@link ScrollBarSettings} for vertical scroll bar
     */
    public void setVerticalScrollBarPosition ( final ScrollBarSettings position )
    {
        this.vpos = position;
    }

    /**
     * Returns {@link ScrollBarSettings} for horizontal scroll bar.
     *
     * @return {@link ScrollBarSettings} for horizontal scroll bar
     */
    public ScrollBarSettings getHorizontalScrollBarPosition ()
    {
        return hpos;
    }

    /**
     * Sets {@link ScrollBarSettings} for horizontal scroll bar.
     *
     * @param position {@link ScrollBarSettings} for horizontal scroll bar
     */
    public void setHorizontalScrollBarPosition ( final ScrollBarSettings position )
    {
        this.hpos = position;
    }

    @Override
    public void addLayoutComponent ( final String s, final Component c )
    {
        // Disabled to avoid instance serialization issues
    }

    @Override
    public void removeLayoutComponent ( final Component c )
    {
        // Disabled to avoid instance serialization issues
    }

    @Override
    public void syncWithScrollPane ( final JScrollPane scrollPane )
    {
        // Disabled to avoid instance serialization issues
    }

    @Override
    public void layoutContainer ( final Container container )
    {
        /**
         * Retrieving various {@link JScrollPane} settings.
         */
        final JScrollPane scrollPane = ( JScrollPane ) container;
        final JViewport viewport = scrollPane.getViewport ();
        final JScrollBar vsb = scrollPane.getVerticalScrollBar ();
        final JScrollBar hsb = scrollPane.getHorizontalScrollBar ();
        final JViewport rowHead = scrollPane.getRowHeader ();
        final JViewport colHead = scrollPane.getColumnHeader ();
        final Component lowerLeft = scrollPane.getCorner ( LOWER_LEFT_CORNER );
        final Component lowerRight = scrollPane.getCorner ( LOWER_RIGHT_CORNER );
        final Component upperLeft = scrollPane.getCorner ( UPPER_LEFT_CORNER );
        final Component upperRight = scrollPane.getCorner ( UPPER_RIGHT_CORNER );
        final int vsbPolicy = scrollPane.getVerticalScrollBarPolicy ();
        final int hsbPolicy = scrollPane.getHorizontalScrollBarPolicy ();

        final Rectangle availR = scrollPane.getBounds ();
        availR.x = availR.y = 0;

        final Insets insets = container.getInsets ();
        availR.x = insets.left;
        availR.y = insets.top;
        availR.width -= insets.left + insets.right;
        availR.height -= insets.top + insets.bottom;

        /**
         * Get the scrollPane's orientation.
         */
        final boolean ltr = scrollPane.getComponentOrientation ().isLeftToRight ();

        /**
         * If there's a visible column header remove the space it needs from the top of availR.
         * The column header is treated as if it were fixed height, arbitrary width.
         */
        final Rectangle colHeadR = new Rectangle ( 0, availR.y, 0, 0 );
        if ( colHead != null && colHead.isVisible () )
        {
            final int colHeadHeight = Math.min ( availR.height, colHead.getPreferredSize ().height );
            colHeadR.height = colHeadHeight;
            availR.y += colHeadHeight;
            availR.height -= colHeadHeight;
        }

        /**
         * If there's a visible row header remove the space it needs from the left or right of availR.
         * The row header is treated as if it were fixed width, arbitrary height.
         */
        final Rectangle rowHeadR = new Rectangle ( 0, 0, 0, 0 );
        if ( rowHead != null && rowHead.isVisible () )
        {
            final int rowHeadWidth = Math.min ( availR.width, rowHead.getPreferredSize ().width );
            rowHeadR.width = rowHeadWidth;
            availR.width -= rowHeadWidth;
            if ( ltr )
            {
                rowHeadR.x = availR.x;
                availR.x += rowHeadWidth;
            }
            else
            {
                rowHeadR.x = availR.x + availR.width;
            }
        }

        /**
         * If there's a JScrollPane.viewportBorder, remove the space it occupies for availR.
         */
        final Border viewportBorder = scrollPane.getViewportBorder ();
        final Insets viewInsets;
        if ( viewportBorder != null )
        {
            viewInsets = viewportBorder.getBorderInsets ( container );
            availR.x += viewInsets.left;
            availR.y += viewInsets.top;
            availR.width -= viewInsets.left + viewInsets.right;
            availR.height -= viewInsets.top + viewInsets.bottom;
        }
        else
        {
            viewInsets = new Insets ( 0, 0, 0, 0 );
        }

        /**
         * At this point availR is the space available for the viewport and scrollbars.
         * rowHeadR is correct except for its height and y and colHeadR is correct except for its width and x.
         * Once we're through computing the dimensions of these three parts we can go back and set the dimensions of rowHeadR.height,
         * rowHeadR.y, colHeadR.width, colHeadR.x and the bounds for the corners.
         *
         * We'll decide about putting up scrollbars by comparing the viewport views preferred size with the viewports extent
         * size (generally just its size). Using the preferredSize is reasonable because layout proceeds top down - so we expect
         * the viewport to be laid out next. And we assume that the viewports layout manager will give the view it's preferred
         * size. One exception to this is when the view implements Scrollable and Scrollable.getViewTracksViewport{Width,Height}
         * methods return true. If the view is tracking the viewports width we don't bother with a horizontal scrollbar, similarly
         * if view.getViewTracksViewport(Height) is true we don't bother with a vertical scrollbar.
         */

        final Component view = viewport != null ? viewport.getView () : null;
        final Dimension viewPrefSize = view != null ? view.getPreferredSize () : new Dimension ( 0, 0 );
        Dimension extentSize = viewport != null ? viewport.toViewCoordinates ( availR.getSize () ) : new Dimension ( 0, 0 );

        boolean viewTracksViewportWidth = false;
        boolean viewTracksViewportHeight = false;
        final boolean isEmpty = availR.width < 0 || availR.height < 0;
        final Scrollable sv;

        /**
         * Don't bother checking the Scrollable methods if there is no room for the viewport,
         * we aren't going to show any scrollbars in this case anyway.
         */
        if ( !isEmpty && view instanceof Scrollable )
        {
            sv = ( Scrollable ) view;
            viewTracksViewportWidth = sv.getScrollableTracksViewportWidth ();
            viewTracksViewportHeight = sv.getScrollableTracksViewportHeight ();
        }
        else
        {
            sv = null;
        }

        /**
         * If there's a vertical scrollbar and we need one, allocate space for it (we'll make it visible later).
         * A vertical scrollbar is considered to be fixed width, arbitrary height.
         */
        final Rectangle vsbR = new Rectangle ( 0, availR.y - viewInsets.top, 0, 0 );
        boolean vsbNeeded = !isEmpty && vsbPolicy != VERTICAL_SCROLLBAR_NEVER &&
                ( vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS || !viewTracksViewportHeight && viewPrefSize.height > extentSize.height );

        if ( vsb != null && vsbNeeded )
        {
            adjustForVSB ( vsb, true, availR, vsbR, viewInsets, ltr );
            extentSize = viewport.toViewCoordinates ( availR.getSize () );
        }

        /**
         * If there's a horizontal scrollbar and we need one, allocate space for it (we'll make it visible later)
         * A horizontal scrollbar is considered to be fixed height, arbitrary width
         */
        final Rectangle hsbR = new Rectangle ( availR.x - viewInsets.left, 0, 0, 0 );
        boolean hsbNeeded = !isEmpty && hsbPolicy != HORIZONTAL_SCROLLBAR_NEVER &&
                ( hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS || !viewTracksViewportWidth && viewPrefSize.width > extentSize.width );

        if ( hsb != null && hsbNeeded )
        {
            adjustForHSB ( hsb, true, availR, hsbR, viewInsets );

            /**
             * If we added the horizontal scrollbar then we've implicitly reduced  the vertical space available to the viewport.
             * As a consequence we may have to add the vertical scrollbar, if that hasn't been done so already.
             * Of course we don't bother with any of this if the vsbPolicy is NEVER.
             */
            if ( vsb != null && !vsbNeeded && vsbPolicy != VERTICAL_SCROLLBAR_NEVER )
            {
                extentSize = viewport.toViewCoordinates ( availR.getSize () );
                vsbNeeded = viewPrefSize.height > extentSize.height;

                if ( vsbNeeded )
                {
                    adjustForVSB ( vsb, true, availR, vsbR, viewInsets, ltr );
                }
            }
        }

        /**
         * Set the size of the viewport first, and then recheck the Scrollable methods.
         * Some components base their return values for the Scrollable methods on the size of the Viewport, so that if we don't
         * ask after resetting the bounds we may have gotten the wrong answer.
         */
        if ( viewport != null )
        {
            viewport.setBounds ( availR );

            if ( sv != null )
            {
                extentSize = viewport.toViewCoordinates ( availR.getSize () );

                final boolean oldHSBNeeded = hsbNeeded;
                final boolean oldVSBNeeded = vsbNeeded;
                viewTracksViewportWidth = sv.getScrollableTracksViewportWidth ();
                viewTracksViewportHeight = sv.getScrollableTracksViewportHeight ();
                if ( vsb != null && vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED )
                {
                    final boolean newVSBNeeded = !viewTracksViewportHeight && viewPrefSize.height > extentSize.height;
                    if ( newVSBNeeded != vsbNeeded )
                    {
                        vsbNeeded = newVSBNeeded;
                        adjustForVSB ( vsb, vsbNeeded, availR, vsbR, viewInsets, ltr );
                        extentSize = viewport.toViewCoordinates ( availR.getSize () );
                    }
                }
                if ( hsb != null && hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED )
                {
                    final boolean newHSBbNeeded = !viewTracksViewportWidth && viewPrefSize.width > extentSize.width;
                    if ( newHSBbNeeded != hsbNeeded )
                    {
                        hsbNeeded = newHSBbNeeded;
                        adjustForHSB ( hsb, hsbNeeded, availR, hsbR, viewInsets );
                        if ( vsb != null && !vsbNeeded && vsbPolicy != VERTICAL_SCROLLBAR_NEVER )
                        {
                            extentSize = viewport.toViewCoordinates ( availR.getSize () );
                            vsbNeeded = viewPrefSize.height > extentSize.height;
                            if ( vsbNeeded )
                            {
                                adjustForVSB ( vsb, true, availR, vsbR, viewInsets, ltr );
                            }
                        }
                    }
                }
                if ( oldHSBNeeded != hsbNeeded || oldVSBNeeded != vsbNeeded )
                {
                    viewport.setBounds ( availR );

                    /**
                     * You could argue that we should recheck the Scrollable methods again until they stop changing,
                     * but they might never stop changing, so we stop here and don't do any additional checks.
                     */
                }
            }
        }

        /**
         * We now have the final size of the viewport: availR.
         * Now fixup the header and scrollbar widths/heights.
         */
        vsbR.height = availR.height + viewInsets.top + viewInsets.bottom;
        if ( hsbNeeded )
        {
            // Increase height to take space of the trailing corner
            if ( vpos.isTrailing () )
            {
                vsbR.height += hsbR.height;
            }

            // Reduce height to give space for the corner
            if ( vpos.isHovering () )
            {
                vsbR.height -= hsbR.height;
            }
        }
        hsbR.width = availR.width + viewInsets.left + viewInsets.right;
        if ( vsbNeeded )
        {
            // Increase width to take space of the trailing corner
            if ( hpos.isTrailing () )
            {
                hsbR.width += vsbR.width;
            }

            // Reduce width to give space for the corner
            if ( hpos.isHovering () )
            {
                hsbR.width -= vsbR.width;
            }
        }

        rowHeadR.height = availR.height + viewInsets.top + viewInsets.bottom;
        rowHeadR.y = availR.y - viewInsets.top;
        colHeadR.width = availR.width + viewInsets.left + viewInsets.right;
        colHeadR.x = availR.x - viewInsets.left;

        /**
         * Set the bounds of the remaining components.
         * The scrollbars are made invisible if they're not needed.
         */
        if ( rowHead != null )
        {
            rowHead.setBounds ( rowHeadR );
        }
        if ( colHead != null )
        {
            colHead.setBounds ( colHeadR );
        }
        if ( vsb != null )
        {
            if ( vsbNeeded )
            {
                /**
                 * This is used primarily for GTK LaF, which needs to extend the vertical scrollbar to fill the upper
                 * corner near the column header. Note that we skip this step (and use the default behavior) if the
                 * user has set a custom corner component.
                 */
                if ( colHead != null && ( vpos.isLeading () || UIManager.getBoolean ( "ScrollPane.fillUpperCorner" ) &&
                        ( ltr && upperRight == null || !ltr && upperLeft == null ) ) )
                {
                    vsbR.y = colHeadR.y;
                    vsbR.height += colHeadR.height;
                }

                vsb.setVisible ( true );
                vsb.setBounds ( vsbR );

                /**
                 * Moving horizontal scroll bar to the top-most position to ensure it is painted on top of everything else.
                 */
                container.setComponentZOrder ( vsb, 0 );
            }
            else
            {
                vsb.setVisible ( false );
            }
        }
        if ( hsb != null )
        {
            if ( hsbNeeded )
            {
                /**
                 * This is used primarily for GTK LaF, which needs to extend the horizontal scrollbar to fill the lower
                 * corner near the row header. Note that we skip this step (and use the default behavior) if the
                 * user has set a custom corner component.
                 */
                if ( rowHead != null && ( hpos.isLeading () || UIManager.getBoolean ( "ScrollPane.fillLowerCorner" ) &&
                        ( ltr && lowerLeft == null || !ltr && lowerRight == null ) ) )
                {
                    if ( ltr )
                    {
                        hsbR.x = rowHeadR.x;
                    }
                    hsbR.width += rowHeadR.width;
                }

                hsb.setVisible ( true );
                hsb.setBounds ( hsbR );

                /**
                 * Moving horizontal scroll bar to the second top-most position to ensure it is painted right below vertical scrollbar.
                 */
                container.setComponentZOrder ( hsb, vsbNeeded ? 1 : 0 );
            }
            else
            {
                hsb.setVisible ( false );
            }
        }
        if ( lowerLeft != null )
        {
            lowerLeft.setBounds ( ltr ? rowHeadR.x : vsbR.x, hsbR.y, ltr ? rowHeadR.width : vsbR.width, hsbR.height );
        }
        if ( lowerRight != null )
        {
            lowerRight.setBounds ( ltr ? vsbR.x : rowHeadR.x, hsbR.y, ltr ? vsbR.width : rowHeadR.width, hsbR.height );
        }
        if ( upperLeft != null )
        {
            upperLeft.setBounds ( ltr ? rowHeadR.x : vsbR.x, colHeadR.y, ltr ? rowHeadR.width : vsbR.width, colHeadR.height );
        }
        if ( upperRight != null )
        {
            upperRight.setBounds ( ltr ? vsbR.x : rowHeadR.x, colHeadR.y, ltr ? vsbR.width : rowHeadR.width, colHeadR.height );
        }

        /**
         * Moving scroll viewport to bottom-most position to ensure it is painted last.
         * This is necessary to ensure components intersecting with viewport are always painted first.
         */
        scrollPane.setComponentZOrder ( viewport, scrollPane.getComponentCount () - 1 );
    }

    /**
     * Adjusts the {@code Rectangle} {@code available} based on if the vertical scrollbar is needed ({@code wantsVSB}).
     * The location of the vsb is updated in {@code vsbR}, and the viewport border insets ({@code vpbInsets}) are used to offset the vsb.
     * This is only called when {@code wantsVSB} has changed, eg you shouldn't invoke adjustForVSB(true) twice.
     *
     * @param vsb         vertical scroll bar
     * @param needed      whether or not vertical scroll bar is needed
     * @param view        space available for view
     * @param scroll      vertical scroll bar bounds
     * @param viewInsets  viewport border insets
     * @param leftToRight component orientation
     */
    protected void adjustForVSB ( final JScrollBar vsb, final boolean needed, final Rectangle view, final Rectangle scroll,
                                  final Insets viewInsets, final boolean leftToRight )
    {
        final int oldWidth = scroll.width;
        if ( needed )
        {
            final int vsbWidth = Math.max ( 0, Math.min ( vsb.getPreferredSize ().width, view.width ) );

            // Shrink available area width if scroll bar is not hovering
            if ( !vpos.isHovering () )
            {
                view.width -= vsbWidth;
            }

            // Adjust vertical scroll bar position and available area
            scroll.width = vsbWidth;
            if ( leftToRight )
            {
                scroll.x = view.x + view.width + ( vpos.isHovering () ? -vsbWidth : 0 ) + viewInsets.right;
            }
            else
            {
                scroll.x = view.x - viewInsets.left;
                view.x += !vpos.isHovering () ? vsbWidth : 0;
            }
        }
        else
        {
            // Adjust available area
            view.width += oldWidth;
        }
    }

    /**
     * Adjusts the {@code Rectangle} {@code available} based on if the horizontal scrollbar is needed ({@code wantsHSB}).
     * The location of the hsb is updated in {@code hsbR}, and the viewport border insets ({@code vpbInsets}) are used to offset the hsb.
     * This is only called when {@code wantsHSB} has changed, eg you shouldn't invoked adjustForHSB(true) twice.
     *
     * @param hsb        horizontal scroll bar
     * @param needed     whether or not horizontal scroll bar is needed
     * @param view       space available for view
     * @param scroll     horizontal scroll bar bounds
     * @param viewInsets viewport border insets
     */
    protected void adjustForHSB ( final JScrollBar hsb, final boolean needed, final Rectangle view, final Rectangle scroll,
                                  final Insets viewInsets )
    {
        final int oldHeight = scroll.height;
        if ( needed )
        {
            final int hsbHeight = Math.max ( 0, Math.min ( view.height, hsb.getPreferredSize ().height ) );

            // Shrink available area height if scroll bar is not hovering
            if ( !hpos.isHovering () )
            {
                view.height -= hsbHeight;
            }

            // Adjust horizontal scroll bar position
            scroll.y = view.y + view.height + ( hpos.isHovering () ? -hsbHeight : 0 ) + viewInsets.bottom;
            scroll.height = hsbHeight;
        }
        else
        {
            // Adjust available area
            view.height += oldHeight;
        }
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        /**
         * Retrieving various {@link JScrollPane} settings.
         */
        final JScrollPane scrollPane = ( JScrollPane ) container;
        final JViewport viewport = scrollPane.getViewport ();
        final JScrollBar vsb = scrollPane.getVerticalScrollBar ();
        final JScrollBar hsb = scrollPane.getHorizontalScrollBar ();
        final JViewport rowHead = scrollPane.getRowHeader ();
        final JViewport colHead = scrollPane.getColumnHeader ();
        final int vsbPolicy = scrollPane.getVerticalScrollBarPolicy ();
        final int hsbPolicy = scrollPane.getHorizontalScrollBarPolicy ();

        final Insets insets = container.getInsets ();
        int prefWidth = insets.left + insets.right;
        int prefHeight = insets.top + insets.bottom;

        /**
         * Note that viewport.getViewSize() is equivalent to viewport.getView().getPreferredSize()
         * modulo a null view or a view whose size was explicitly set.
         */

        Dimension extentSize = null;
        Dimension viewSize = null;
        Component view = null;
        if ( viewport != null )
        {
            extentSize = viewport.getPreferredSize ();
            viewSize = viewport.getViewSize ();
            view = viewport.getView ();
        }

        /**
         * If there's a viewport add its preferredSize
         */
        if ( extentSize != null )
        {
            prefWidth += extentSize.width;
            prefHeight += extentSize.height;
        }

        /**
         * If there's a JScrollPane.viewportBorder, add its insets
         */
        final Border viewportBorder = scrollPane.getViewportBorder ();
        if ( viewportBorder != null )
        {
            final Insets vpbInsets = viewportBorder.getBorderInsets ( container );
            prefWidth += vpbInsets.left + vpbInsets.right;
            prefHeight += vpbInsets.top + vpbInsets.bottom;
        }

        /**
         * If a header exists and it's visible, factor its preferred size in
         */
        if ( rowHead != null && rowHead.isVisible () )
        {
            prefWidth += rowHead.getPreferredSize ().width;
        }
        if ( colHead != null && colHead.isVisible () )
        {
            prefHeight += colHead.getPreferredSize ().height;
        }

        /**
         * If a scroll bar is going to appear, factor its preferred size in.
         * Though if {@link ScrollBarSettings#isExtending()} is {@code false} then scroll bar sizes are completely ignored.
         *
         * If the scrollbars policy is AS_NEEDED, this can be a little tricky:
         *
         * - If the view is a Scrollable then scrollableTracksViewportWidth and scrollableTracksViewportHeight can be used to effectively
         * disable scrolling (if they're true) in their respective dimensions.
         *
         * - Assuming that a scrollbar hasn't been disabled by the previous constraint, we need to decide if the scrollbar is going
         * to appear to correctly compute the JScrollPanes preferred size. To do this we compare the preferredSize of the viewport (the
         * extentSize) to the preferredSize of the view. Although we're not responsible for laying out the view we'll assume that the
         * JViewport will always give it its preferredSize.
         */
        if ( vsb != null && vsbPolicy != VERTICAL_SCROLLBAR_NEVER &&
                ( !vpos.isHovering () || vpos.isHovering () && vpos.isExtending () ) )
        {
            if ( vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS )
            {
                prefWidth += vsb.getPreferredSize ().width;
            }
            else if ( viewSize != null && extentSize != null )
            {
                boolean canScroll = true;
                if ( view instanceof Scrollable )
                {
                    canScroll = !( ( Scrollable ) view ).getScrollableTracksViewportHeight ();
                }
                if ( canScroll && viewSize.height > extentSize.height )
                {
                    prefWidth += vsb.getPreferredSize ().width;
                }
            }
        }
        if ( hsb != null && hsbPolicy != HORIZONTAL_SCROLLBAR_NEVER &&
                ( !hpos.isHovering () || hpos.isHovering () && hpos.isExtending () ) )
        {
            if ( hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS )
            {
                prefHeight += hsb.getPreferredSize ().height;
            }
            else if ( viewSize != null && extentSize != null )
            {
                boolean canScroll = true;
                if ( view instanceof Scrollable )
                {
                    canScroll = !( ( Scrollable ) view ).getScrollableTracksViewportWidth ();
                }
                if ( canScroll && viewSize.width > extentSize.width )
                {
                    prefHeight += hsb.getPreferredSize ().height;
                }
            }
        }

        return new Dimension ( prefWidth, prefHeight );
    }

    /**
     * The minimum size of a {@link JScrollPane} is the size of the insets plus minimum size of the viewport, plus the scrollpane's
     * viewportBorder insets, plus the minimum size of the visible headers, plus the minimum size of the scrollbars whose displayPolicy
     * isn't {@link JScrollPane#VERTICAL_SCROLLBAR_NEVER} or {@link JScrollPane#HORIZONTAL_SCROLLBAR_NEVER}.
     *
     * @param container the {@link Container} that will be laid out
     * @return a {@link Dimension} object specifying the minimum size
     */
    @Override
    public Dimension minimumLayoutSize ( final Container container )
    {
        /**
         * Retrieving various {@link JScrollPane} settings.
         */
        final JScrollPane scrollPane = ( JScrollPane ) container;
        final JViewport viewport = scrollPane.getViewport ();
        final JScrollBar vsb = scrollPane.getVerticalScrollBar ();
        final JScrollBar hsb = scrollPane.getHorizontalScrollBar ();
        final JViewport rowHead = scrollPane.getRowHeader ();
        final JViewport colHead = scrollPane.getColumnHeader ();
        final int vsbPolicy = scrollPane.getVerticalScrollBarPolicy ();
        final int hsbPolicy = scrollPane.getHorizontalScrollBarPolicy ();

        final Insets insets = container.getInsets ();
        int minWidth = insets.left + insets.right;
        int minHeight = insets.top + insets.bottom;

        /**
         * If there's a viewport add its minimumSize.
         */
        if ( viewport != null )
        {
            final Dimension size = viewport.getMinimumSize ();
            minWidth += size.width;
            minHeight += size.height;
        }

        /**
         * If there's a JScrollPane.viewportBorder, add its insets.
         */
        final Border viewportBorder = scrollPane.getViewportBorder ();
        if ( viewportBorder != null )
        {
            final Insets vpbInsets = viewportBorder.getBorderInsets ( container );
            minWidth += vpbInsets.left + vpbInsets.right;
            minHeight += vpbInsets.top + vpbInsets.bottom;
        }

        /**
         * If a header exists and it's visible, factor its minimum size in.
         */
        if ( rowHead != null && rowHead.isVisible () )
        {
            final Dimension size = rowHead.getMinimumSize ();
            minWidth += size.width;
            minHeight = Math.max ( minHeight, size.height );
        }
        if ( colHead != null && colHead.isVisible () )
        {
            final Dimension size = colHead.getMinimumSize ();
            minWidth = Math.max ( minWidth, size.width );
            minHeight += size.height;
        }

        /**
         * If a scrollbar might appear, factor its minimum size in.
         * Though if {@link ScrollBarSettings#isExtending()} is {@code false} then scroll bar sizes are completely ignored.
         */
        if ( vpos.isExtending () && vsb != null && vsbPolicy != VERTICAL_SCROLLBAR_NEVER )
        {
            final Dimension size = vsb.getMinimumSize ();
            minWidth += size.width;
            minHeight = Math.max ( minHeight, size.height );
        }
        if ( hpos.isExtending () && hsb != null && hsbPolicy != HORIZONTAL_SCROLLBAR_NEVER )
        {
            final Dimension size = hsb.getMinimumSize ();
            minWidth = Math.max ( minWidth, size.width );
            minHeight += size.height;
        }

        return new Dimension ( minWidth, minHeight );
    }

    /**
     * The UI resource version of {@link WebScrollPaneLayout}.
     */
    @XStreamAlias ( "WebScrollPaneLayout$UIResource" )
    public static final class UIResource extends WebScrollPaneLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebScrollPaneLayout}.
         */
    }
}