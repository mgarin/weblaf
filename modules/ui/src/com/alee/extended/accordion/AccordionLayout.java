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

package com.alee.extended.accordion;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.data.BoxOrientation;
import com.alee.api.merge.Mergeable;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.laf.grouping.AbstractGroupingLayout;
import com.alee.managers.animation.easing.Easing;
import com.alee.managers.animation.transition.AbstractTransition;
import com.alee.managers.animation.transition.TimedTransition;
import com.alee.managers.animation.transition.Transition;
import com.alee.managers.animation.transition.TransitionAdapter;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.general.Pair;
import com.alee.utils.parsing.DurationUnits;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link LayoutManager} for {@link AccordionPane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see WebAccordion
 * @see AccordionPane
 * @see AccordionModel
 * @see WebAccordionModel
 */
@XStreamAlias ( "AccordionLayout" )
public class AccordionLayout extends AbstractGroupingLayout implements Mergeable, Cloneable, Serializable
{
    /**
     * Gap in pixels between {@link AccordionPane}s.
     * Cane also be {@code null} in which case default value will be used.
     */
    @Nullable
    protected Integer panesGap;

    /**
     * {@link Easing} used for expansion and collapse animation.
     * Can be set to {@code null} to disable animated transition.
     */
    @Nullable
    protected Easing easing;

    /**
     * Single transition duration.
     * Can be set to {@code null} or {@code 0} to disable animated transition.
     *
     * @see DurationUnits for more information on duration format
     */
    @Nullable
    protected String duration;

    /**
     * Transition used for expansion and collapse animations.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient Map<String, AbstractTransition> transitions;

    /**
     * Fractional size of the contents, could only have values between {@code 0.0f} and {@code 1.0f}.
     * Whenever content is fully visible it is {@code 1.0f}, whenever content is not visible it is {@code 0.0f}.
     * This is a lazy map and will not contain all sizes at all times, use {@link #size(WebAccordion, String)} to retrieve live values.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient Map<String, Float> contentSizes;

    /**
     * Constructs new {@link AccordionLayout} that doesn't use any animations.
     */
    public AccordionLayout ()
    {
        this ( null, null );
    }

    /**
     * Constructs new {@link AccordionLayout} that doesn't use any animations.
     *
     * @param easing   {@link Easing} used for expansion and collapse animation or {@code null} to disable animation
     * @param duration single transition duration in milliseconds or either {@code null} or {@code 0L} to disable animation
     */
    public AccordionLayout ( @Nullable final Easing easing, @Nullable final Long duration )
    {
        setEasing ( easing );
        setDuration ( duration );
    }

    /**
     * Returns gap in pixels between {@link AccordionPane}s.
     *
     * @return gap in pixels between {@link AccordionPane}s
     */
    public int getPanesGap ()
    {
        return panesGap != null ? panesGap : 0;
    }

    /**
     * Sets gap in pixels between {@link AccordionPane}s
     *
     * @param gap gap in pixels between {@link AccordionPane}s
     */
    public void setPanesGap ( final int gap )
    {
        this.panesGap = gap;
    }

    /**
     * Returns {@link Easing} used for expansion and collapse animation or {@code null} if animation is disabled.
     *
     * @return {@link Easing} used for expansion and collapse animation or {@code null} if animation is disabled
     */
    @Nullable
    public Easing getEasing ()
    {
        return easing;
    }

    /**
     * Sets {@link Easing} used for expansion and collapse animation or {@code null} to disable animation.
     *
     * @param easing {@link Easing} used for expansion and collapse animation or {@code null} to disable animation
     */
    public void setEasing ( @Nullable final Easing easing )
    {
        this.easing = easing;
    }

    /**
     * Returns single transition duration in milliseconds or either {@code null} or {@code 0L} if animation is disabled
     *
     * @return single transition duration in milliseconds or either {@code null} or {@code 0L} if animation is disabled
     */
    public long getDuration ()
    {
        return duration != null ? DurationUnits.get ().fromString ( duration ) : 0L;
    }

    /**
     * Sets single transition duration in milliseconds or either {@code null} or {@code 0L} to disable animation.
     *
     * @param duration single transition duration in milliseconds or either {@code null} or {@code 0L} to disable animation
     */
    public void setDuration ( @Nullable final Long duration )
    {
        this.duration = duration != null ? DurationUnits.get ().toString ( duration ) : null;
    }

    /**
     * Installs this {@link AccordionModel} into specified {@link WebAccordion}.
     *
     * @param accordion {@link WebAccordion} this model should be installed into
     */
    public void install ( @NotNull final WebAccordion accordion )
    {
        // Setting up resources
        contentSizes = new HashMap<String, Float> ( accordion.getComponentCount () );
        transitions = new HashMap<String, AbstractTransition> ( 3 );

        // Re-adding components added before this layout was set into accordion
        for ( final AccordionPane pane : accordion.getPanes () )
        {
            addComponent ( pane, null );
        }
    }

    /**
     * Uninstalls this {@link AccordionModel} from the specified {@link WebAccordion}.
     *
     * @param accordion {@link WebAccordion} this model should be uninstalled from
     */
    public void uninstall ( @NotNull final WebAccordion accordion )
    {
        // Removing all components from this layout
        for ( final AccordionPane pane : accordion.getPanes () )
        {
            removeComponent ( pane );
        }

        // Cleaning up resources
        if ( transitions != null )
        {
            for ( final Map.Entry<String, AbstractTransition> entry : transitions.entrySet () )
            {
                entry.getValue ().stop ();
            }
            transitions = null;
        }
        contentSizes = null;
    }

    /**
     * Returns current content size of {@link AccordionPane} with the specified identifier.
     * If value haven't been stored in this layout yet it will be retrieved from {@link WebAccordion}.
     *
     * @param accordion {@link WebAccordion}
     * @param id        {@link AccordionPane} identifier
     * @return current content size of {@link AccordionPane} with the specified identifier
     */
    protected float size ( final WebAccordion accordion, final String id )
    {
        final float size;
        if ( contentSizes.containsKey ( id ) )
        {
            size = contentSizes.get ( id );
        }
        else
        {
            size = accordion.isPaneExpanded ( id ) ? 1f : 0f;
            contentSizes.put ( id, size );
        }
        return size;
    }

    /**
     * Asks layout to expand {@link AccordionPane} with the specified identifier.
     *
     * @param accordion {@link WebAccordion}
     * @param id        {@link AccordionPane} identifier
     */
    public void expandPane ( @NotNull final WebAccordion accordion, @NotNull final String id )
    {
        changePaneState ( accordion, id, size ( accordion, id ), 1f, new Runnable ()
        {
            @Override
            public void run ()
            {
                final AccordionPane pane = accordion.getPane ( id );
                pane.fireExpanding ( accordion );
                accordion.fireExpanding ( pane );
            }
        }, new Runnable ()
        {
            @Override
            public void run ()
            {
                final AccordionPane pane = accordion.getPane ( id );
                pane.fireExpanded ( accordion );
                accordion.fireExpanded ( pane );
            }
        } );
    }

    /**
     * Asks layout to collapse {@link AccordionPane} with the specified identifier.
     *
     * @param accordion {@link WebAccordion}
     * @param id        {@link AccordionPane} identifier
     */
    public void collapsePane ( @NotNull final WebAccordion accordion, @NotNull final String id )
    {
        changePaneState ( accordion, id, size ( accordion, id ), 0f, new Runnable ()
        {
            @Override
            public void run ()
            {
                final AccordionPane pane = accordion.getPane ( id );
                pane.fireCollapsing ( accordion );
                accordion.fireCollapsing ( pane );
            }
        }, new Runnable ()
        {
            @Override
            public void run ()
            {
                final AccordionPane pane = accordion.getPane ( id );
                pane.fireCollapsed ( accordion );
                accordion.fireCollapsed ( pane );
            }
        } );
    }

    /**
     * Asks layout to change state for {@link AccordionPane} with the specified identifier.
     *
     * @param accordion   {@link WebAccordion}
     * @param id          {@link AccordionPane} identifier
     * @param start       starting {@link AccordionPane} content size
     * @param target      target {@link AccordionPane} content size
     * @param preChange   {@link Runnable} to be executed once state change has started
     * @param afterChange {@link Runnable} to be executed once state change has completed
     */
    protected void changePaneState ( @NotNull final WebAccordion accordion, @NotNull final String id,
                                     final float start, final float target,
                                     @NotNull final Runnable preChange, @NotNull final Runnable afterChange )
    {
        // Make sure we stop previous transition
        final AbstractTransition transition = transitions.get ( id );
        if ( transition != null )
        {
            transition.stop ();
            transitions.remove ( id );
        }

        final Easing easing = getEasing ();
        final long fullDuration = getDuration ();
        if ( accordion.isAnimated () && accordion.isShowing () && easing != null && fullDuration > 0L )
        {
            final TimedTransition<Float> stateTransition = new TimedTransition<Float> ( start, target, easing, fullDuration );
            transitions.put ( id, stateTransition );

            // Pre-change action
            preChange.run ();

            stateTransition.addListener ( new TransitionAdapter<Float> ()
            {
                @Override
                public void adjusted ( final Transition transition, final Float value )
                {
                    // Updating size
                    contentSizes.put ( id, value );
                    SwingUtils.update ( accordion );
                }

                @Override
                public void finished ( final Transition transition, final Float value )
                {
                    SwingUtilities.invokeLater ( new Runnable ()
                    {
                        @Override
                        public void run ()
                        {
                            // Removing unused queue
                            transitions.remove ( id );

                            // Post-change action
                            afterChange.run ();
                        }
                    } );
                }
            } );
            stateTransition.play ();
        }
        else
        {
            // Pre-change action
            preChange.run ();

            // Updating size
            contentSizes.put ( id, target );
            SwingUtils.update ( accordion );

            // Post-change action
            afterChange.run ();
        }
    }

    /**
     * Returns whether or not {@link AccordionPane} with the specified identifier is in transition to either of two expansion states.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} with the specified identifier is in transition, {@code false} otherwise
     */
    public boolean isPaneInTransition ( @NotNull final String id )
    {
        return transitions.containsKey ( id );
    }

    @Override
    public void layoutContainer ( @NotNull final Container parent )
    {
        final WebAccordion accordion = ( WebAccordion ) parent;
        final BoxOrientation headerPosition = accordion.getHeaderPosition ();
        final int panesGap = getPanesGap ();
        final boolean vertical = headerPosition.isTop () || headerPosition.isBottom ();
        final int count = accordion.getComponentCount ();
        final int w = parent.getWidth ();
        final int h = parent.getHeight ();
        final Insets insets = parent.getInsets ();
        final int cw = w - insets.left - insets.right;
        final int ch = h - insets.top - insets.bottom;

        float totalExpanded = 0f;
        int expandedCount = 0;
        int freePixels = vertical ? ch : cw;
        final Dimension[] preferred = new Dimension[ count ];
        for ( int i = 0; i < count; i++ )
        {
            final AccordionPane pane = ( AccordionPane ) accordion.getComponent ( i );

            // Pane preferred size
            final Dimension cps = pane.getPreferredSize ();
            preferred[ i ] = cps;

            // Counting expanded panes
            final float expansion = size ( accordion, pane.getId () );
            final boolean fullyExpanded = Float.compare ( expansion, 1f ) == 0;
            final boolean partiallyExpanded = !fullyExpanded && Float.compare ( expansion, 0f ) == 1;
            totalExpanded += fullyExpanded || partiallyExpanded ? expansion : 0f;
            expandedCount += fullyExpanded || partiallyExpanded ? 1 : 0;

            // Pixels left for expanded panes
            freePixels -= vertical ? cps.height : cps.width;
            freePixels -= i > 0 ? panesGap : 0;
        }

        final float totalFreePixels = freePixels;
        int x = insets.left;
        int y = insets.top;
        int expandedPositioned = 0;
        for ( int i = 0; i < count; i++ )
        {
            final AccordionPane pane = ( AccordionPane ) accordion.getComponent ( i );
            final Dimension cps = preferred[ i ];
            final float expanded = size ( accordion, pane.getId () );
            if ( Float.compare ( expanded, 0f ) == 1 )
            {
                // Positioning fully or partially expanded pane
                final int panePixels;
                if ( expandedPositioned < expandedCount - 1 )
                {
                    panePixels = Math.round ( totalFreePixels * expanded / totalExpanded );
                }
                else
                {
                    panePixels = freePixels;
                }
                pane.setBounds (
                        x, y,
                        vertical ? cw : cps.width + panePixels,
                        vertical ? cps.height + panePixels : ch
                );
                freePixels -= panePixels;
                expandedPositioned++;
            }
            else
            {
                // Positioning collapsed pane
                pane.setBounds (
                        x, y,
                        vertical ? cw : cps.width,
                        vertical ? cps.height : ch
                );
            }
            x += vertical ? 0 : pane.getWidth () + panesGap;
            y += vertical ? pane.getHeight () + panesGap : 0;
        }
    }

    /*
    todo Slightly smoother visual size sharing, but have issues with specific cases

    @Override
    public void layoutContainer ( @NotNull final Container parent )
    {
        final WebAccordion accordion = ( WebAccordion ) parent;
        final BoxOrientation headerPosition = accordion.getHeaderPosition ();
        final int panesGap = getPanesGap ();
        final boolean vertical = headerPosition.isTop () || headerPosition.isBottom ();
        final int count = accordion.getComponentCount ();
        final int w = parent.getWidth ();
        final int h = parent.getHeight ();
        final Insets insets = parent.getInsets ();
        final int cw = w - insets.left - insets.right;
        final int ch = h - insets.top - insets.bottom;

        // Calculating total expanded panes size and free space available for the expanded panes
        // Note that "expanded" in this context means that pane content is visible
        // Panes mentioned as expanded further on might be actually expanded or be in process of expanding or even collapsing
        // As long as they are visible on the screen they are interesting for us and will be accounted for
        //float totalExpanded = 0f;
        int fullyExpandedCount = 0;
        int partiallyExpandedCount = 0;
        float totalPartiallyExpanded = 0f;
        int freePixels = vertical ? ch : cw;
        final Dimension[] preferred = new Dimension[ count ];
        for ( int i = 0; i < count; i++ )
        {
            final AccordionPane pane = ( AccordionPane ) accordion.getComponent ( i );

            // Pane preferred size
            final Dimension cps = pane.getPreferredSize ();
            preferred[ i ] = cps;

            // Counting expanded panes
            final float expansion = contentSizes.get ( pane.getId () );
            final boolean fullyExpanded = Float.compare ( expansion, 1f ) == 0;
            final boolean partiallyExpanded = !fullyExpanded && Float.compare ( expansion, 0f ) == 1;
            fullyExpandedCount += fullyExpanded ? 1 : 0;
            partiallyExpandedCount += partiallyExpanded ? 1 : 0;
            totalPartiallyExpanded += partiallyExpanded ? expansion : 0f;

            // Pixels left for expanded panes
            freePixels -= vertical ? cps.height : cps.width;
            freePixels -= i > 0 ? panesGap : 0;
        }
        freePixels = Math.max ( 0, freePixels );

        // Workaround for edge cases when one of the panes already fully collapsed or expanded
        //        if ( partiallyExpandedCount == 1 )
        //        {
        //            if ( Math.round ( totalPartiallyExpanded ) == 1 )
        //            {
        //                fullyExpandedCount += 1;
        //            }
        //            partiallyExpandedCount = 0;
        //        }

        // Calculating total expanded pane sizes
        final int totalPixelsForFullyExpanded;
        final int totalPixelsForPartiallyExpanded;
        if ( fullyExpandedCount > 0 && partiallyExpandedCount > 0 )
        {
            final int totalExpandedPaneSpots = fullyExpandedCount + Math.round ( totalPartiallyExpanded );
            totalPixelsForFullyExpanded = Math.round ( ( float ) freePixels / totalExpandedPaneSpots );
            totalPixelsForPartiallyExpanded = freePixels - totalPixelsForFullyExpanded;
        }
        else if ( fullyExpandedCount > 0 )
        {
            totalPixelsForFullyExpanded = freePixels;
            totalPixelsForPartiallyExpanded = 0;
        }
        else if ( partiallyExpandedCount > 0 )
        {
            totalPixelsForFullyExpanded = 0;
            totalPixelsForPartiallyExpanded = freePixels;
        }
        else
        {
            totalPixelsForFullyExpanded = 0;
            totalPixelsForPartiallyExpanded = 0;
        }

        // Calculating component preferred sizes
        int x = insets.left;
        int y = insets.top;
        int fullyExpandedPositioned = 0;
        int fullyExpandedFreePixels = totalPixelsForFullyExpanded;
        int partiallyExpandedPositioned = 0;
        int partiallyExpandedFreePixels = totalPixelsForPartiallyExpanded;
        for ( int i = 0; i < count; i++ )
        {
            final AccordionPane pane = ( AccordionPane ) accordion.getComponent ( i );
            final Dimension cps = preferred[ i ];
            final float expanded = contentSizes.get ( pane.getId () );
            if ( Float.compare ( expanded, 0f ) == 1 )
            {
                // Positioning fully or partially expanded pane
                final int panePixels;
                if ( Float.compare ( expanded, 1f ) == 0 )
                {
                    if ( fullyExpandedPositioned < fullyExpandedCount - 1 )
                    {
                        panePixels = totalPixelsForFullyExpanded / fullyExpandedCount;
                    }
                    else
                    {
                        panePixels = fullyExpandedFreePixels;
                    }
                    fullyExpandedFreePixels -= panePixels;
                    fullyExpandedPositioned++;
                }
                else
                {
                    if ( partiallyExpandedPositioned < partiallyExpandedCount - 1 )
                    {
                        panePixels = Math.round ( totalPixelsForPartiallyExpanded * expanded / totalPartiallyExpanded );
                    }
                    else
                    {
                        panePixels = partiallyExpandedFreePixels;
                    }
                    partiallyExpandedFreePixels -= panePixels;
                    partiallyExpandedPositioned++;
                }
                pane.setBounds (
                        x, y,
                        vertical ? cw : cps.width + panePixels,
                        vertical ? cps.height + panePixels : ch
                );
                freePixels -= panePixels;
            }
            else
            {
                // Positioning collapsed pane
                pane.setBounds (
                        x, y,
                        vertical ? cw : cps.width,
                        vertical ? cps.height : ch
                );
            }
            x += vertical ? 0 : pane.getWidth () + panesGap;
            y += vertical ? pane.getHeight () + panesGap : 0;
        }
    }*/

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container parent )
    {
        final Dimension ps = new Dimension ( 0, 0 );

        final WebAccordion accordion = ( WebAccordion ) parent;
        final BoxOrientation headerPosition = accordion.getHeaderPosition ();
        final int panesGap = getPanesGap ();
        final boolean vertical = headerPosition.isTop () || headerPosition.isBottom ();
        final int count = accordion.getComponentCount ();
        final Insets insets = parent.getInsets ();

        // Including panes preferred sizes
        for ( int i = 0; i < count; i++ )
        {
            final AccordionPane pane = ( AccordionPane ) accordion.getComponent ( i );
            final Dimension cps = pane.getPreferredSize ();
            if ( vertical )
            {
                ps.width = Math.max ( ps.width, cps.width );
                ps.height += cps.height + ( i > 0 ? panesGap : 0 ) + ( i > 0 ? panesGap : 0 );
            }
            else
            {
                ps.width += cps.width + ( i > 0 ? panesGap : 0 ) + ( i > 0 ? panesGap : 0 );
                ps.height = Math.max ( ps.height, cps.height );
            }
        }

        // Accounting for minimum preferred content size
        if ( vertical )
        {
            ps.height += accordion.getMinimumPaneContentSize () * accordion.getMinimumExpandedPaneCount ();
        }
        else
        {
            ps.width += accordion.getMinimumPaneContentSize () * accordion.getMinimumExpandedPaneCount ();
        }

        // Counting insets in
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;

        return ps;
    }

    @NotNull
    @Override
    protected String sides ()
    {
        return sides != null ? sides : ( sides = "0,0,0,0" );
    }

    @NotNull
    @Override
    public Pair<String, String> getDescriptors ( @NotNull final Container container, @NotNull final Component component, final int index )
    {
        final Pair<String, String> descriptors;
        if ( getPanesGap () == 0 )
        {
            final WebAccordion accordion = ( WebAccordion ) container;
            final int last = container.getComponentCount () - 1;
            final boolean hor = accordion.getHeaderPosition ().isLeft () || accordion.getHeaderPosition ().isRight ();
            final boolean top = ( hor || index == 0 ) && isPaintTop ();
            final boolean left = ( index == 0 || !hor ) && isPaintLeft ();
            final boolean bottom = ( hor || index == last ) && isPaintBottom ();
            final boolean right = ( index == last || !hor ) && isPaintLeft ();
            final String sides = DecorationUtils.toString ( top, left, bottom, right );
            final String lines = DecorationUtils.toString ( false, false, !hor && index != last, hor && index != last );
            descriptors = new Pair<String, String> ( sides, lines );
        }
        else
        {
            descriptors = new Pair<String, String> ();
        }
        return descriptors;
    }

    /**
     * The UI resource version of {@link AccordionLayout}.
     */
    @XStreamAlias ( "AccordionLayout$UIResource" )
    public static final class UIResource extends AccordionLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link AccordionLayout}.
         */
    }
}