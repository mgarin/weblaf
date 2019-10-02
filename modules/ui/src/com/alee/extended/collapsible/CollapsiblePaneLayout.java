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

package com.alee.extended.collapsible;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.data.BoxOrientation;
import com.alee.api.jdk.Objects;
import com.alee.api.merge.Mergeable;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.managers.animation.easing.Easing;
import com.alee.managers.animation.transition.QueueTransition;
import com.alee.managers.animation.transition.TimedTransition;
import com.alee.managers.animation.transition.Transition;
import com.alee.managers.animation.transition.TransitionAdapter;
import com.alee.managers.style.StyleId;
import com.alee.utils.SwingUtils;
import com.alee.utils.parsing.DurationUnits;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * {@link LayoutManager} for {@link WebCollapsiblePane}.
 * It supports 4 header positions, RTL orientation and animated content expansion and collapse.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebCollapsiblePane">How to use WebCollapsiblePane</a>
 * @see WebCollapsiblePane
 */
@XStreamAlias ( "CollapsiblePaneLayout" )
public class CollapsiblePaneLayout extends AbstractLayoutManager implements PropertyChangeListener, Mergeable, Cloneable, Serializable
{
    /**
     * Whether or not header panel should be used for preferred size calculations.
     * Either {@code null} or {@code true} value means header should fit into {@link WebCollapsiblePane} preferred width.
     */
    @Nullable
    protected Boolean fitHeader;

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
    protected transient QueueTransition transitionsQueue;

    /**
     * Fractional size of the content, always remains between {@code 0.0d} and {@code 1.0d}.
     * Whenever content is fully visible it is {@code 1.0d}, whenever content is not visible it is {@code 0.0d}.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient float contentSize;

    /**
     * Constructs new {@link CollapsiblePaneLayout} that doesn't use any animations.
     */
    public CollapsiblePaneLayout ()
    {
        this ( null, null, null );
    }

    /**
     * Constructs new {@link CollapsiblePaneLayout} that doesn't use any animations.
     *
     * @param fitHeader whether or not header panel should be used for preferred size calculations
     */
    public CollapsiblePaneLayout ( @Nullable final Boolean fitHeader )
    {
        this ( fitHeader, null, null );
    }

    /**
     * Constructs new {@link CollapsiblePaneLayout} that doesn't use any animations.
     *
     * @param fitHeader whether or not header panel should be used for preferred size calculations
     * @param easing    {@link Easing} used for expansion and collapse animation or {@code null} to disable animation
     * @param duration  single transition duration in milliseconds or either {@code null} or {@code 0L} to disable animation
     */
    public CollapsiblePaneLayout ( @Nullable final Boolean fitHeader, @Nullable final Easing easing, @Nullable final Long duration )
    {
        setFitHeader ( fitHeader );
        setEasing ( easing );
        setDuration ( duration );
    }

    /**
     * Returns whether or not header panel should be used for preferred size calculations.
     *
     * @return {@code true} if header panel should be used for preferred size calculations, {@code false} otherwise
     */
    public boolean isFitHeader ()
    {
        return fitHeader == null || fitHeader;
    }

    /**
     * Sets whether or not header panel should be used for preferred size calculations.
     *
     * @param fitHeader whether or not header panel should be used for preferred size calculations
     */
    public void setFitHeader ( @Nullable final Boolean fitHeader )
    {
        this.fitHeader = fitHeader;
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
     * Installs this {@link CollapsiblePaneLayout} into the specified {@link WebCollapsiblePane}.
     *
     * @param pane {@link WebCollapsiblePane} to install this {@link CollapsiblePaneLayout} into
     */
    public void install ( @NotNull final WebCollapsiblePane pane )
    {
        contentSize = pane.isExpanded () ? 1.0f : 0.0f;
        pane.addPropertyChangeListener ( this );
    }

    /**
     * Uninstalls this {@link CollapsiblePaneLayout} from the specified {@link WebCollapsiblePane}.
     *
     * @param pane {@link WebCollapsiblePane} to uninstall this {@link CollapsiblePaneLayout} from
     */
    public void uninstall ( @NotNull final WebCollapsiblePane pane )
    {
        pane.removePropertyChangeListener ( this );
        if ( transitionsQueue != null )
        {
            transitionsQueue.stop ();
            transitionsQueue = null;
        }
        contentSize = 0.0f;
    }

    @Override
    public void addComponent ( @NotNull final Component component, @Nullable final Object constraints )
    {
        final WebCollapsiblePane collapsiblePane = ( WebCollapsiblePane ) component.getParent ();
        if ( component == collapsiblePane.getHeaderComponent () &&
                collapsiblePane.getHeaderComponent () instanceof AbstractHeaderPanel.UIResource )
        {
            StyleId.collapsiblepaneHeaderPanel.at ( collapsiblePane ).set (
                    ( AbstractHeaderPanel.UIResource ) collapsiblePane.getHeaderComponent ()
            );
        }
    }

    @Override
    public void removeComponent ( @NotNull final Component component )
    {
        final WebCollapsiblePane collapsiblePane = ( WebCollapsiblePane ) component.getParent ();
        if ( component == collapsiblePane.getHeaderComponent () &&
                collapsiblePane.getHeaderComponent () instanceof AbstractHeaderPanel.UIResource )
        {
            ( ( AbstractHeaderPanel.UIResource ) collapsiblePane.getHeaderComponent () ).resetStyleId ();
        }
    }

    @Override
    public void propertyChange ( @NotNull final PropertyChangeEvent event )
    {
        final WebCollapsiblePane pane = ( WebCollapsiblePane ) event.getSource ();
        final String property = event.getPropertyName ();
        if ( Objects.equals ( property, WebCollapsiblePane.EXPANDED_PROPERTY ) )
        {
            final boolean expanded = ( Boolean ) event.getNewValue ();
            final Easing easing = getEasing ();
            final long fullDuration = getDuration ();
            if ( pane.isAnimated () && pane.isShowing () && easing != null && fullDuration > 0L )
            {
                // Performing animation
                final float target = expanded ? 1.0f : 0.0f;
                if ( transitionsQueue == null )
                {
                    // Resetting position
                    contentSize = expanded ? 0.0f : 1.0f;

                    // Custom transition for background animations
                    transitionsQueue = new QueueTransition ( false );

                    // Adding transition
                    transitionsQueue.add ( new TimedTransition<Float> ( contentSize, target, easing, fullDuration ) );

                    // Value update listener
                    transitionsQueue.addListener ( new TransitionAdapter<Float> ()
                    {
                        @Override
                        public void adjusted ( final Transition transition, final Float value )
                        {
                            contentSize = value;
                            SwingUtils.update ( pane );
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
                                    transitionsQueue = null;

                                    // Firing appropriate event
                                    if ( pane.isExpanded () )
                                    {
                                        pane.fireExpanded ();
                                    }
                                    else
                                    {
                                        pane.fireCollapsed ();
                                    }
                                }
                            } );
                        }
                    } );

                    // Playing transition
                    transitionsQueue.play ();
                }
                else
                {
                    // Aborting current transition
                    transitionsQueue.stop ();

                    // Removing all previously added transitions
                    transitionsQueue.clear ();

                    // Adding new partial transition
                    final float targetDistance = expanded ? 1.0f - contentSize : contentSize;
                    final long partialDuration = Math.round ( fullDuration * targetDistance );
                    transitionsQueue.add ( new TimedTransition<Float> ( contentSize, target, easing, partialDuration ) );

                    // Playing transition
                    transitionsQueue.play ();
                }

                // Firing appropriate event
                if ( expanded )
                {
                    pane.fireExpanding ();
                }
                else
                {
                    pane.fireCollapsing ();
                }
            }
            else
            {
                // Resetting position
                contentSize = expanded ? 1.0f : 0.0f;

                // Firing appropriate event
                if ( expanded )
                {
                    pane.fireExpanding ();
                    pane.fireExpanded ();
                }
                else
                {
                    pane.fireCollapsing ();
                    pane.fireCollapsed ();
                }
                SwingUtils.update ( pane );
            }
        }
        else if ( Objects.equals ( property, WebCollapsiblePane.TITLE_COMPONENT_PROPERTY, WebCollapsiblePane.TITLE_PROPERTY,
                WebCollapsiblePane.HEADER_POSITION_PROPERTY, WebCollapsiblePane.CONTENT_PROPERTY ) )
        {
            SwingUtils.update ( pane );
        }
    }

    /**
     * Returns whether or not {@link CollapsiblePaneLayout} is in transition to either of two expansion states.
     * Note this will only be {@code true} whenever {@link WebCollapsiblePane} expansion state changes and {@link CollapsiblePaneLayout}
     * is configured to perform an animated transition on the {@link WebCollapsiblePane#content}.
     *
     * @return {@code true} if {@link CollapsiblePaneLayout} is in transition, {@code false} otherwise
     */
    public boolean isInTransition ()
    {
        return transitionsQueue != null;
    }

    @Override
    public void layoutContainer ( @NotNull final Container parent )
    {
        final WebCollapsiblePane pane = ( WebCollapsiblePane ) parent;
        final Component header = pane.getHeaderComponent ();
        final Component content = pane.getContent ();
        final Insets insets = pane.getInsets ();
        final int availableWidth = pane.getWidth () - insets.left - insets.right;
        final int availableHeight = pane.getHeight () - insets.top - insets.bottom;
        final Rectangle bounds = new Rectangle ( insets.left, insets.top, availableWidth, availableHeight );

        final boolean ltr = pane.getComponentOrientation ().isLeftToRight ();
        final BoxOrientation position = pane.getHeaderPosition ();
        final Dimension hps = header.getPreferredSize ();

        final int x;
        if ( position.isTop () || position.isBottom () || ( ltr ? position.isLeft () : position.isRight () ) )
        {
            x = bounds.x;
        }
        else
        {
            x = bounds.x + bounds.width - Math.min ( availableWidth, hps.width );
        }

        final int y;
        if ( position.isTop () || position.isLeft () || position.isRight () )
        {
            y = bounds.y;
        }
        else
        {
            y = bounds.y + bounds.height - Math.min ( availableHeight, hps.height );
        }

        final int w;
        if ( position.isTop () || position.isBottom () )
        {
            w = availableWidth;
        }
        else
        {
            w = Math.min ( availableWidth, hps.width );
        }

        final int h;
        if ( position.isLeft () || position.isRight () )
        {
            h = availableHeight;
        }
        else
        {
            h = Math.min ( availableHeight, hps.height );
        }

        header.setBounds ( x, y, w, h );

        if ( content != null )
        {
            if ( position.isTop () )
            {
                bounds.y += h;
                bounds.height -= h;
            }
            else if ( position.isBottom () )
            {
                bounds.height -= h;
            }
            else if ( ltr && position.isLeft () || !ltr && position.isRight () )
            {
                bounds.x += w;
                bounds.width -= w;
            }
            else
            {
                bounds.width -= w;
            }

            content.setBounds ( bounds );
            content.setVisible ( transitionsQueue != null || pane.isExpanded () );
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container parent )
    {
        final WebCollapsiblePane pane = ( WebCollapsiblePane ) parent;
        final Component header = pane.getHeaderComponent ();
        final Component content = pane.getContent ();
        final Insets insets = pane.getInsets ();
        final Dimension ps = new Dimension ( 0, 0 );

        final BoxOrientation position = pane.getHeaderPosition ();
        if ( content != null )
        {
            final Dimension cps = content.getPreferredSize ();
            ps.width = position.isLeft () || position.isRight () ? Math.round ( cps.width * contentSize ) : cps.width;
            ps.height = position.isTop () || position.isBottom () ? Math.round ( cps.height * contentSize ) : cps.height;
        }

        final Dimension hps = header.getPreferredSize ();
        if ( position.isTop () || position.isBottom () )
        {
            if ( isFitHeader () )
            {
                ps.width = Math.max ( ps.width, hps.width );
            }
            ps.height += hps.height;
        }
        else
        {
            if ( isFitHeader () )
            {
                ps.height = Math.max ( ps.height, hps.height );
            }
            ps.width += hps.width;
        }

        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;
        return ps;
    }

    /**
     * The UI resource version of {@link CollapsiblePaneLayout}.
     */
    @XStreamAlias ( "CollapsiblePaneLayout$UIResource" )
    public static final class UIResource extends CollapsiblePaneLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link CollapsiblePaneLayout}.
         */
    }
}