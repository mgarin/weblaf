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
import com.alee.managers.style.BoundsType;
import com.alee.managers.style.StyleException;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.ComponentSize;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Custom layout for {@link JTabbedPane}.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "TabbedPaneLayout" )
public class TabbedPaneLayout extends AbstractLayoutManager implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link ComponentSize} settings for content.
     */
    @XStreamAlias ( "ContentSize" )
    protected ComponentSize contentSize;

    /**
     * Content bounds.
     */
    protected transient Rectangle contentBounds = null;

    /**
     * Returns {@link ComponentSize} settings for content.
     *
     * @return {@link ComponentSize} settings for content
     */
    @NotNull
    public ComponentSize getContentSize ()
    {
        if ( contentSize == null )
        {
            throw new StyleException ( "ContentSize must be specified for TabbedPaneLayout" );
        }
        return contentSize;
    }

    /**
     * Returns cached content bounds.
     *
     * @return cached content bounds
     */
    @NotNull
    public Rectangle getContentBounds ()
    {
        return contentBounds != null ? new Rectangle ( contentBounds ) : new Rectangle ();
    }

    @Override
    public void layoutContainer ( @NotNull final Container parent )
    {
        final JTabbedPane tabbedPane = ( JTabbedPane ) parent;

        // Retrieving content bounds
        final Rectangle contentBounds = BoundsType.padding.bounds ( tabbedPane );

        // Positioning tab area
        final TabArea tabArea = getTabArea ( tabbedPane );
        if ( tabArea != null )
        {
            final boolean ltr = tabbedPane.getComponentOrientation ().isLeftToRight ();
            final Dimension tabAreaSize = tabArea.getPreferredSize ();
            final int tabPlacement = tabbedPane.getTabPlacement ();
            if ( tabPlacement == SwingConstants.TOP )
            {
                tabArea.setBounds (
                        contentBounds.x,
                        contentBounds.y,
                        contentBounds.width,
                        tabAreaSize.height
                );
                contentBounds.y += tabAreaSize.height;
                contentBounds.height -= Math.min ( tabAreaSize.height, contentBounds.height );
            }
            else if ( tabPlacement == SwingConstants.BOTTOM )
            {
                tabArea.setBounds (
                        contentBounds.x,
                        contentBounds.y + contentBounds.height - tabAreaSize.height,
                        contentBounds.width,
                        tabAreaSize.height
                );
                contentBounds.height -= Math.min ( tabAreaSize.height, contentBounds.height );
            }
            else if ( tabPlacement == ( ltr ? SwingConstants.LEFT : SwingConstants.RIGHT ) )
            {
                tabArea.setBounds (
                        contentBounds.x,
                        contentBounds.y,
                        tabAreaSize.width,
                        contentBounds.height
                );
                contentBounds.x += tabAreaSize.width;
                contentBounds.width -= Math.min ( tabAreaSize.width, contentBounds.width );
            }
            else if ( tabPlacement == ( ltr ? SwingConstants.RIGHT : SwingConstants.LEFT ) )
            {
                tabArea.setBounds (
                        contentBounds.x + contentBounds.width - tabAreaSize.width,
                        contentBounds.y,
                        tabAreaSize.width,
                        contentBounds.height
                );
                contentBounds.width -= Math.min ( tabAreaSize.width, contentBounds.width );
            }
        }

        // Positioning selected content component
        final Component selectedComponent = tabbedPane.getSelectedComponent ();
        if ( selectedComponent != null )
        {
            // Updating visibility
            selectedComponent.setVisible ( true );

            // Updating content bounds
            this.contentBounds = contentBounds;

            // Positioning selected content component
            selectedComponent.setBounds ( SwingUtils.shrink (
                    contentBounds,
                    getContentSize ().getInsets ()
            ) );
        }
        else
        {
            // Updating content bounds
            this.contentBounds = BoundsType.padding.bounds ( tabbedPane );
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container parent )
    {
        final Dimension preferredSize;

        // Calculating maximum content preferred size
        final JTabbedPane tabbedPane = ( JTabbedPane ) parent;
        final ComponentSize contentSize = getContentSize ();
        Dimension contentPreferredSize = new Dimension ( 0, 0 );
        for ( int i = 0; i < tabbedPane.getTabCount (); i++ )
        {
            final Component componentAt = tabbedPane.getComponentAt ( i );
            if ( componentAt != null )
            {
                contentPreferredSize = SwingUtils.maxNonNull (
                        contentPreferredSize,
                        contentSize.size ( componentAt )
                );
            }
        }

        // Calculating resulting preferred size
        final TabArea tabArea = getTabArea ( tabbedPane );
        final Dimension tabAreaSize = tabArea != null ? tabArea.getPreferredSize () : new Dimension ();
        switch ( tabbedPane.getTabPlacement () )
        {
            default:
            case SwingConstants.TOP:
            case SwingConstants.BOTTOM:
            {
                preferredSize = new Dimension (
                        Math.max ( tabAreaSize.width, contentPreferredSize.width ),
                        tabAreaSize.height + contentPreferredSize.height
                );
            }
            break;

            case SwingConstants.LEFT:
            case SwingConstants.RIGHT:
            {
                preferredSize = new Dimension (
                        tabAreaSize.width + contentPreferredSize.width,
                        Math.max ( tabAreaSize.height, contentPreferredSize.height )
                );
            }
            break;
        }

        // Adding tabbed pane insets
        SwingUtils.increase ( preferredSize, tabbedPane.getInsets () );

        return preferredSize;
    }

    /**
     * Workaround for no event being fired in {@link JTabbedPane}.
     *
     * @see WebTabbedPane#setEnabledAt(int, boolean)
     * @see WebTabbedPane#setIconAt(int, Icon)
     * @see WebTabbedPane#setDisabledIconAt(int, Icon)
     */
    @Override
    public void invalidateLayout ( @NotNull final Container container )
    {
        // We need to do some extra updates for basic JTabbedPane component
        // WebTabbedPane fixes those things, so it doesn't require that workaround
        final JTabbedPane tabbedPane = ( JTabbedPane ) container;
        if ( !( tabbedPane instanceof WebTabbedPane ) )
        {
            // We ignore cases when WebLaF UI is not available
            final WTabbedPaneUI ui = LafUtils.getUI ( tabbedPane );
            if ( ui != null )
            {
                // We ignore cases when TabContainer is not available
                final TabContainer tabContainer = ui.getTabContainer ();
                if ( tabContainer != null && tabContainer.getComponentCount () == tabbedPane.getTabCount () )
                {
                    // Invalidating tab settings
                    // All tab properties that are inconsistent with the model get updated here
                    // Excluding the properties that actualy fire a reliable event
                    for ( int i = 0; i < tabbedPane.getTabCount (); i++ )
                    {
                        final Tab tab = ( Tab ) tabContainer.getComponent ( i );

                        // Firing tab foreground change if needed
                        // todo This is not properly fired by JTabbedPane, only repaint is called
                        if ( tab.getForeground () != tabbedPane.getForegroundAt ( i ) )
                        {
                            // Index is passed instead of the actual value due to how events are handled
                            SwingUtils.firePropertyChanged ( tabbedPane, WebTabbedPane.FOREGROUND_AT_PROPERTY, null, i );
                        }

                        // Firing tab background change if needed
                        // todo This is not properly fired by JTabbedPane, only repaint is called
                        if ( tab.getBackground () != tabbedPane.getBackgroundAt ( i ) )
                        {
                            // Index is passed instead of the actual value due to how events are handled
                            SwingUtils.firePropertyChanged ( tabbedPane, WebTabbedPane.BACKGROUND_AT_PROPERTY, null, i );
                        }

                        // Firing tab enabled state change if needed
                        if ( tab.isEnabled () != tabbedPane.isEnabledAt ( i ) )
                        {
                            // Index is passed instead of the actual value due to how events are handled
                            SwingUtils.firePropertyChanged ( tabbedPane, WebTabbedPane.ENABLED_AT_PROPERTY, null, i );
                        }

                        // Firing tab icon change if needed
                        if ( tab.getIcon () != tabbedPane.getIconAt ( i ) )
                        {
                            // Index is passed instead of the actual value due to how events are handled
                            SwingUtils.firePropertyChanged ( tabbedPane, WebTabbedPane.ICON_AT_PROPERTY, null, i );
                        }

                        // Firing tab disabled icon change if needed
                        if ( tab.getDisabledIcon () != tabbedPane.getDisabledIconAt ( i ) )
                        {
                            // Index is passed instead of the actual value due to how events are handled
                            SwingUtils.firePropertyChanged ( tabbedPane, WebTabbedPane.DISABLED_ICON_AT_PROPERTY, null, i );
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns {@link TabArea} of the specified {@link JTabbedPane}.
     *
     * @param tabbedPane {@link JTabbedPane}
     * @return {@link TabArea} of the specified {@link JTabbedPane}
     */
    @Nullable
    protected TabArea getTabArea ( @NotNull final JTabbedPane tabbedPane )
    {
        return SwingUtils.getFirst ( tabbedPane, TabArea.class );
    }

    /**
     * The UI resource version of {@link TabbedPaneLayout}.
     */
    @XStreamAlias ( "TabbedPaneLayout$UIResource" )
    public static final class UIResource extends TabbedPaneLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link TabbedPaneLayout}.
         */
    }
}