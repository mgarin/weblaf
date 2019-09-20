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
import com.alee.api.jdk.Objects;
import com.alee.laf.WebUI;
import com.alee.laf.button.WebButton;
import com.alee.laf.viewport.WebViewport;
import com.alee.managers.hover.GlobalHoverListener;
import com.alee.managers.hover.HoverManager;
import com.alee.managers.language.UILanguageManager;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TabbedPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Pluggable look and feel interface for any component based on {@link JTabbedPane}.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class WTabbedPaneUI<C extends JTabbedPane> extends TabbedPaneUI implements WebUI<C>
{
    /**
     * {@link PropertyChangeListener} for the {@link JTabbedPane}.
     */
    @Nullable
    protected PropertyChangeListener propertyChangeListener;

    /**
     * {@link ChangeListener} for the {@link JTabbedPane}.
     */
    @Nullable
    protected ChangeListener changeListener;

    /**
     * {@link ContainerListener} for listening to added and removed {@link JTabbedPane} tabs.
     */
    @Nullable
    protected ContainerListener containerListener;

    /**
     * Component listener to properly adjust to tabbed pane size changes
     */
    @Nullable
    protected ComponentListener componentListener;

    /**
     * {@link TabbedPaneInputListener} for the {@link JTabbedPane}.
     */
    @Nullable
    protected TabbedPaneInputListener<C> inputListener;

    /**
     * {@link GlobalHoverListener} used for displaying {@link JTabbedPane} custom tooltips.
     * We're able to use {@link GlobalHoverListener} due to new {@link WTabbedPaneUI} implementation that uses {@link Tab} components.
     * With default {@link javax.swing.plaf.basic.BasicTabbedPaneUI} we would have been forced to use {@link MouseListener}.
     */
    @Nullable
    protected GlobalHoverListener toolTipHoverListener;

    /**
     * Runtime variables.
     */
    protected C tabbedPane;
    protected TabArea tabArea;
    protected WebButton tabMenuButton;
    protected TabViewport tabViewport;
    protected TabContainer tabContainer;

    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "TabbedPane.";
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving component reference
        tabbedPane = ( C ) c;

        // Installing default component settings
        installDefaults ();

        // Installing default components
        installComponents ();

        // Installing default component listeners
        installListeners ();
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling default component listeners
        uninstallListeners ();

        // Uninstalling default components
        uninstallComponents ();

        // Uninstalling default component settings
        uninstallDefaults ();

        // Removing component reference
        tabbedPane = null;
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LafUtils.installDefaults ( tabbedPane, getPropertyPrefix () );
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LafUtils.uninstallDefaults ( tabbedPane );
    }

    /**
     * Installs default components.
     */
    protected void installComponents ()
    {
        // Creating tab area that will host all related components
        tabArea = createTabArea ();
        tabbedPane.add ( tabArea );

        // Tab menu
        tabMenuButton = createTabMenuButton ();
        tabArea.add ( tabMenuButton );

        // Creating tab viewport that enables scrolling through tab titles
        tabViewport = createTabViewport ();
        tabArea.add ( tabViewport );

        // Creating tab container that will host all tab title components
        tabContainer = createTabContainer ();
        tabViewport.setView ( tabContainer );

        // Initializing missing tab components
        // This is a better way to handle tabs painting instead of a having a mix of directly painted and component-bsaed tabs
        for ( int index = 0; index < tabbedPane.getTabCount (); index++ )
        {
            addTab ( index );
        }
    }

    /**
     * Returns new {@link TabArea} instance.
     *
     * @return new {@link TabArea} instance
     */
    @NotNull
    protected TabArea createTabArea ()
    {
        return new TabArea ( tabbedPane );
    }

    /**
     * Returns new {@link TabMenuButton} instance.
     *
     * @return new {@link TabMenuButton} instance
     */
    @NotNull
    protected TabMenuButton createTabMenuButton ()
    {
        return new TabMenuButton ( tabbedPane, tabArea );
    }

    /**
     * Returns new {@link TabViewport} instance.
     *
     * @return new {@link TabViewport} instance
     */
    @NotNull
    protected TabViewport createTabViewport ()
    {
        return new TabViewport ( tabbedPane, tabArea );
    }

    /**
     * Returns new {@link TabContainer} instance.
     *
     * @return new {@link TabContainer} instance
     */
    @NotNull
    protected TabContainer createTabContainer ()
    {
        return new TabContainer ( tabbedPane, tabViewport );
    }

    /**
     * Uninstalls default components.
     */
    protected void uninstallComponents ()
    {
        // Removing tab area and all related references
        tabbedPane.remove ( tabArea );
        tabContainer = null;
        tabViewport = null;
        tabArea = null;
    }

    /**
     * Adds {@link Tab} at the specified index.
     *
     * @param index index to add {@link Tab} at
     */
    protected void addTab ( final int index )
    {
        final Tab tab = createTab ( index );
        final Component tabComponent = tabbedPane.getTabComponentAt ( index );
        if ( tabComponent != null )
        {
            tab.add ( tabComponent );
        }
        tabContainer.add ( tab, index );
        if ( inputListener != null )
        {
            inputListener.tabAdded ( tab, index );
        }
        updateTabAreaStates ();
        recalculateViewSizes ();
    }

    /**
     * Returns {@link Tab} created for the specified index.
     *
     * @param index index to create {@link Tab} for
     * @return {@link Tab} created for the specified index
     */
    @NotNull
    protected Tab createTab ( final int index )
    {
        return new Tab ( tabbedPane, tabContainer, index );
    }

    /**
     * Removes {@link Tab} from the specified index.
     *
     * @param index index to remove {@link Tab} from
     */
    protected void removeTab ( final int index )
    {
        tabContainer.remove ( index );
        if ( inputListener != null )
        {
            inputListener.tabRemoved ( index );
        }
        updateTabAreaStates ();
        recalculateViewSizes ();
        tabbedPane.putClientProperty ( WebTabbedPane.REMOVED_TAB_INDEX, null );
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        propertyChangeListener = createPropertyChangeListener ();
        if ( propertyChangeListener != null )
        {
            tabbedPane.addPropertyChangeListener ( propertyChangeListener );
        }
        changeListener = createChangeListener ();
        if ( changeListener != null )
        {
            tabbedPane.addChangeListener ( changeListener );
        }
        containerListener = createContainerListener ();
        if ( containerListener != null )
        {
            tabbedPane.addContainerListener ( containerListener );
        }
        componentListener = createComponentListener ();
        if ( componentListener != null )
        {
            tabbedPane.addComponentListener ( componentListener );
        }
        inputListener = createTabbedPaneInputListener ();
        if ( inputListener != null )
        {
            inputListener.install ( tabbedPane );
        }
        updateToolTipHoverListener ();
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        uninstallToolTipHoverListener ();
        if ( inputListener != null )
        {
            inputListener.uninstall ( tabbedPane );
            inputListener = null;
        }
        if ( componentListener != null )
        {
            tabbedPane.removeComponentListener ( componentListener );
            componentListener = null;
        }
        if ( containerListener != null )
        {
            tabbedPane.removeContainerListener ( containerListener );
            containerListener = null;
        }
        if ( changeListener != null )
        {
            tabbedPane.removeChangeListener ( changeListener );
            changeListener = null;
        }
        if ( propertyChangeListener != null )
        {
            tabbedPane.removePropertyChangeListener ( propertyChangeListener );
            propertyChangeListener = null;
        }
    }

    /**
     * Returns {@link PropertyChangeListener} for the {@link JTabbedPane}.
     * Can be overridden to return {@code null} to disable this particular listener.
     *
     * @return {@link PropertyChangeListener} for the {@link JTabbedPane}
     */
    @Nullable
    protected PropertyChangeListener createPropertyChangeListener ()
    {
        return new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                // todo Solution for WebTabbedPane.FOREGROUND_AT_PROPERTY and WebTabbedPane.BACKGROUND_AT_PROPERTY?
                final String property = evt.getPropertyName ();
                if ( Objects.equals ( property, WebTabbedPane.TAB_LAYOUT_POLICY_PROPERTY, WebTabbedPane.TAB_PLACEMENT_PROPERTY ) )
                {
                    tabbedPane.revalidate ();
                    updateTabAreaStates ();
                    recalculateViewSizes ();
                }
                else if ( Objects.equals ( property, WebTabbedPane.INDEX_FOR_TITLE_PROPERTY ) )
                {
                    final int index = ( Integer ) evt.getNewValue ();
                    final Tab tab = ( Tab ) tabContainer.getComponent ( index );
                    final String newTitle = tabbedPane.getTitleAt ( index );
                    tab.setText ( UILanguageManager.getInitialText ( newTitle ) );
                    UILanguageManager.registerInitialLanguage ( tab, newTitle );
                    updateTabAreaStates ();
                    recalculateViewSizes ();
                }
                else if ( Objects.equals ( property, WebTabbedPane.INDEX_FOR_TAB_COMPONENT_PROPERTY ) )
                {
                    final int index = ( Integer ) evt.getNewValue ();
                    final Tab tab = ( Tab ) tabContainer.getComponent ( index );
                    tab.setComponent ( tabbedPane.getTabComponentAt ( index ) );
                    updateTabAreaStates ();
                    recalculateViewSizes ();
                }
                else if ( Objects.equals ( property, WebTabbedPane.INDEX_FOR_NULL_COMPONENT_PROPERTY ) )
                {
                    // This is a JDK workaround for null component addition
                    final Integer addedIndex = ( Integer ) evt.getNewValue ();
                    if ( addedIndex != null && addedIndex != -1 )
                    {
                        addTab ( addedIndex );
                    }
                }
                else if ( Objects.equals ( property, WebTabbedPane.REMOVED_TAB_INDEX ) )
                {
                    // This is a JDK workaround we are using to detect removed tab index
                    final Integer index = ( Integer ) evt.getNewValue ();
                    if ( index != null )
                    {
                        removeTab ( index );
                    }
                }
                else if ( Objects.equals ( property, WebTabbedPane.ENABLED_AT_PROPERTY ) )
                {
                    final int index = ( Integer ) evt.getNewValue ();
                    final Tab tab = ( Tab ) tabContainer.getComponent ( index );
                    tab.setEnabled ( tabbedPane.isEnabledAt ( index ) );
                    updateTabAreaStates ();
                    recalculateViewSizes ();
                }
                else if ( Objects.equals ( property, WebTabbedPane.ICON_AT_PROPERTY ) )
                {
                    final int index = ( Integer ) evt.getNewValue ();
                    final Tab tab = ( Tab ) tabContainer.getComponent ( index );
                    tab.setIcon ( tabbedPane.getIconAt ( index ) );
                    updateTabAreaStates ();
                    recalculateViewSizes ();
                }
                else if ( Objects.equals ( property, WebTabbedPane.DISABLED_ICON_AT_PROPERTY ) )
                {
                    final int index = ( Integer ) evt.getNewValue ();
                    final Tab tab = ( Tab ) tabContainer.getComponent ( index );
                    tab.setDisabledIcon ( tabbedPane.getDisabledIconAt ( index ) );
                    updateTabAreaStates ();
                    recalculateViewSizes ();
                }
                else if ( Objects.equals ( property, WebTabbedPane.TOOLTIP_PROVIDER_PROPERTY ) )
                {
                    updateToolTipHoverListener ();
                }
            }
        };
    }

    /**
     * Returns {@link ChangeListener} for the {@link JTabbedPane}.
     * Can be overridden to return {@code null} to disable this particular listener.
     *
     * @return {@link ChangeListener} for the {@link JTabbedPane}
     */
    @Nullable
    protected ChangeListener createChangeListener ()
    {
        return new ChangeListener ()
        {
            @Override
            public void stateChanged ( @NotNull final ChangeEvent e )
            {
                updateTabAreaStates ();
                recalculateViewSizes ();
            }
        };
    }

    /**
     * Returns {@link ContainerListener} for the {@link JTabbedPane}.
     * Can be overridden to return {@code null} to disable this particular listener.
     *
     * @return {@link ContainerListener} for the {@link JTabbedPane}
     */
    @Nullable
    protected ContainerListener createContainerListener ()
    {
        return new ContainerAdapter ()
        {
            @Override
            public void componentAdded ( @NotNull final ContainerEvent e )
            {
                final Component component = e.getChild ();
                if ( component != tabArea )
                {
                    // Simply asking tabbed pane to find added component index
                    final int index = tabbedPane.indexOfComponent ( component );
                    if ( index != -1 )
                    {
                        addTab ( index );
                    }
                }
            }

            /* This part is not needed as it is handled within PropertyChangeListener workaround */
            /*@Override
            public void componentRemoved ( @NotNull final ContainerEvent e )
            {
                final Component component = e.getChild ();
                if ( component != tabArea )
                {
                    // This is a JDK workaround we are using to detect removed tab index
                    final Integer index = ( Integer ) tabbedPane.getClientProperty ( WebTabbedPane.REMOVED_TAB_INDEX );
                    if ( index != null )
                    {
                        removeTab ( index );
                    }
                }
            }*/
        };
    }

    /**
     * Returns {@link ComponentListener} for the {@link JTabbedPane}.
     * Can be overridden to return {@code null} to disable this particular listener.
     *
     * @return {@link ComponentListener} for the {@link JTabbedPane}
     */
    @Nullable
    protected ComponentListener createComponentListener ()
    {
        return new ComponentAdapter ()
        {
            @Override
            public void componentResized ( @NotNull final ComponentEvent e )
            {
                recalculateViewSizes ();
            }
        };
    }

    /**
     * Updates decoration states of all {@link TabArea} elements.
     */
    protected void updateTabAreaStates ()
    {
        for ( int i = 0; i < tabContainer.getComponentCount (); i++ )
        {
            DecorationUtils.fireStatesChanged ( tabContainer.getComponent ( i ) );
        }
        DecorationUtils.fireStatesChanged ( tabContainer );
        DecorationUtils.fireStatesChanged ( tabArea );
    }

    /**
     * Recalculates {@link TabViewport} sizes.
     */
    protected void recalculateViewSizes ()
    {
        final Dimension oldViewSize = tabViewport.getViewSize ();
        final Dimension oldExtentSize = tabViewport.getExtentSize ();

        // Ensure tab container has updated it's preferred size
        tabContainer.revalidate ();
        tabbedPane.revalidate ();

        // Calculating new view and extent sizes
        final Dimension tabContainerSize = tabContainer.getPreferredSize ();
        final Dimension viewSize = new Dimension ( tabContainerSize );
        final Dimension extentSize = new Dimension ( tabContainerSize );
        switch ( tabbedPane.getTabPlacement () )
        {
            default:
            case SwingConstants.TOP:
            case SwingConstants.BOTTOM:
            {
                // We have to check JTabbedPane width here because TabArea could still be placed at the old location
                final Insets tpInsets = tabbedPane.getInsets ();
                final Insets taInsets = tabArea.getInsets ();
                viewSize.width = tabbedPane.getWidth () - tpInsets.left - tpInsets.right - taInsets.left - taInsets.right;
                extentSize.width = Math.max ( extentSize.width, viewSize.width );
            }
            break;

            case SwingConstants.LEFT:
            case SwingConstants.RIGHT:
            {
                // We have to check JTabbedPane width here because TabArea could still be placed at the old location
                final Insets tpInsets = tabbedPane.getInsets ();
                final Insets taInsets = tabArea.getInsets ();
                viewSize.height = tabbedPane.getHeight () - tpInsets.top - tpInsets.bottom - taInsets.top - taInsets.bottom;
                extentSize.height = Math.max ( extentSize.height, viewSize.height );
            }
            break;
        }

        // Perform updates only if values changed
        if ( !oldViewSize.equals ( viewSize ) || !oldExtentSize.equals ( extentSize ) )
        {
            // Updating viewport values
            tabViewport.setViewSize ( viewSize );
            tabViewport.setExtentSize ( extentSize );
            tabContainer.revalidate ();
            tabContainer.repaint ();

            // Updating menu button visibility before we update the layout
            updateTabMenuButtonVisibility ();

            // Moving selected tab into visible area
            moveSelectedTabIntoVisibleArea ();
        }
    }

    /**
     * Updates {@link TabMenuButton} visibility.
     */
    protected void updateTabMenuButtonVisibility ()
    {
        if ( tabbedPane.getTabLayoutPolicy () == JTabbedPane.SCROLL_TAB_LAYOUT )
        {
            final int tabPlacement = tabbedPane.getTabPlacement ();
            final boolean horizontal = tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM;
            final Dimension extentSize = tabViewport.getExtentSize ();
            final Dimension viewSize = tabViewport.getViewSize ();
            tabMenuButton.setVisible (
                    horizontal ?
                            extentSize.width > viewSize.width :
                            extentSize.height > viewSize.height
            );
        }
        else
        {
            tabMenuButton.setVisible ( false );
        }
    }

    /**
     * Moves selected {@link Tab} into visible part of {@link TabViewport}.
     */
    protected void moveSelectedTabIntoVisibleArea ()
    {
        if ( tabbedPane.getTabLayoutPolicy () == JTabbedPane.SCROLL_TAB_LAYOUT )
        {
            // We have to do this update later, after the layout update
            SwingUtilities.invokeLater ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    final int selectedIndex = tabbedPane.getSelectedIndex ();
                    if ( selectedIndex != -1 )
                    {
                        final Tab selectedTab = getTab ( selectedIndex );
                        if ( selectedTab != null )
                        {
                            final Rectangle selectedBounds = selectedTab.getBounds ();
                            if ( !tabContainer.getVisibleRect ().contains ( selectedBounds ) )
                            {
                                tabContainer.scrollRectToVisible ( selectedBounds );
                            }
                        }
                    }
                }
            } );
        }
    }

    /**
     * Returns {@link TabbedPaneInputListener} for the {@link JTabbedPane}.
     * Can be overridden to return {@code null} to disable this particular listener.
     *
     * @return {@link TabbedPaneInputListener} for the {@link JTabbedPane}
     */
    @Nullable
    protected TabbedPaneInputListener<C> createTabbedPaneInputListener ()
    {
        return new WTabbedPaneInputListener<C, WTabbedPaneUI<C>> ();
    }

    /**
     * Installs {@link GlobalHoverListener} used for displaying {@link JTabbedPane} custom tooltips.
     */
    protected void installToolTipHoverListener ()
    {
        if ( toolTipHoverListener == null )
        {
            toolTipHoverListener = createToolTipHoverListener ();
            if ( toolTipHoverListener != null )
            {
                HoverManager.registerGlobalHoverListener ( tabbedPane, toolTipHoverListener );
            }
        }
    }

    /**
     * Uninstalls {@link GlobalHoverListener} used for displaying {@link JTabbedPane} custom tooltips.
     */
    protected void uninstallToolTipHoverListener ()
    {
        if ( toolTipHoverListener != null )
        {
            HoverManager.unregisterGlobalHoverListener ( tabbedPane, toolTipHoverListener );
            toolTipHoverListener = null;
        }
    }

    /**
     * Updates {@link GlobalHoverListener} used for displaying {@link JTabbedPane} custom tooltips.
     */
    protected void updateToolTipHoverListener ()
    {
        if ( getToolTipProvider () != null )
        {
            installToolTipHoverListener ();
        }
        else
        {
            uninstallToolTipHoverListener ();
        }
    }

    /**
     * Returns {@link GlobalHoverListener} used for displaying {@link JTabbedPane} custom tooltips.
     * Can be overridden to return {@code null} to disable this particular listener.
     *
     * @return {@link GlobalHoverListener} used for displaying {@link JTabbedPane} custom tooltips
     */
    @Nullable
    protected GlobalHoverListener createToolTipHoverListener ()
    {
        return new GlobalHoverListener ()
        {
            @Override
            public void hoverChanged ( @Nullable final Component oldHover, @Nullable final Component newHover )
            {
                final TabbedPaneToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    final Tab oldHoverTab = getOwnedTab ( oldHover );
                    final Tab newHoverTab = getOwnedTab ( newHover );
                    if ( oldHoverTab != newHoverTab )
                    {
                        provider.hoverAreaChanged (
                                tabbedPane,
                                oldHoverTab != null ? new TabbedPaneTabArea ( oldHoverTab.getIndex () ) : null,
                                newHoverTab != null ? new TabbedPaneTabArea ( newHoverTab.getIndex () ) : null
                        );
                    }
                }
            }

            /**
             * Returns {@link Tab} owned by {@link JTabbedPane} represented by this UI.
             *
             * @param component event {@link Component}
             * @return {@link Tab} owned by {@link JTabbedPane} represented by this UI
             */
            @Nullable
            private Tab getOwnedTab ( @Nullable final Component component )
            {
                Tab tab = null;
                if ( component instanceof Tab )
                {
                    final Tab oldHoverTab = ( Tab ) component;
                    if ( oldHoverTab.getTabbedPane () == tabbedPane )
                    {
                        tab = oldHoverTab;
                    }
                }
                return tab;
            }
        };
    }

    /**
     * Returns {@link TabbedPaneToolTipProvider} for {@link JTabbedPane} or {@code null} if it is not specified.
     *
     * @return {@link TabbedPaneToolTipProvider} for {@link JTabbedPane} or {@code null} if it is not specified
     */
    @Nullable
    protected TabbedPaneToolTipProvider getToolTipProvider ()
    {
        return tabbedPane != null ?
                ( TabbedPaneToolTipProvider ) tabbedPane.getClientProperty ( WebTabbedPane.TOOLTIP_PROVIDER_PROPERTY ) :
                null;
    }

    /**
     * Returns {@link TabArea}.
     *
     * @return {@link TabArea}
     */
    @Nullable
    public TabArea getTabArea ()
    {
        return tabArea;
    }

    /**
     * Returns tab {@link WebViewport}.
     *
     * @return tab {@link WebViewport}
     */
    @Nullable
    public WebViewport getTabViewport ()
    {
        return tabViewport;
    }

    /**
     * Returns {@link TabContainer}.
     *
     * @return {@link TabContainer}
     */
    @Nullable
    public TabContainer getTabContainer ()
    {
        return tabContainer;
    }

    /**
     * Returns {@link Tab} at the specified index.
     *
     * @param index {@link Tab} index
     * @return {@link Tab} at the specified index
     */
    @Nullable
    public Tab getTab ( final int index )
    {
        return tabContainer != null ? ( Tab ) tabContainer.getComponent ( index ) : null;
    }

    @Override
    public int tabForCoordinate ( @NotNull final JTabbedPane pane, final int x, final int y )
    {
        final int tabIndex;
        if ( tabArea != null )
        {
            final Point tabAreaLocation = CoreSwingUtils.getRelativeLocation ( tabContainer, tabbedPane );
            final Component possibleTab = tabContainer.getComponentAt ( x - tabAreaLocation.x, y - tabAreaLocation.y );
            tabIndex = tabContainer.getComponentZOrder ( possibleTab );
        }
        else
        {
            throw new RuntimeException ( "UI have not been initialized" );
        }
        return tabIndex;
    }

    @Nullable
    @Override
    public Rectangle getTabBounds ( @NotNull final JTabbedPane pane, final int index )
    {
        final Rectangle tabBounds;
        if ( tabContainer != null )
        {
            if ( tabContainer.isShowing () )
            {
                tabBounds = SwingUtils.moveBy (
                        tabContainer.getComponent ( index ).getBounds (),
                        CoreSwingUtils.getRelativeLocation ( tabContainer, tabbedPane )
                );
            }
            else
            {
                tabBounds = null;
            }
        }
        else
        {
            throw new RuntimeException ( "UI have not been initialized" );
        }
        return tabBounds;
    }

    @Override
    public int getTabRunCount ( @NotNull final JTabbedPane pane )
    {
        final int runCount;
        if ( tabContainer != null )
        {
            if ( tabContainer.getLayout () instanceof TabContainerLayout )
            {
                final TabContainerLayout layout = ( TabContainerLayout ) tabContainer.getLayout ();
                runCount = layout.getRunCount ();
            }
            else
            {
                runCount = 1;
            }
        }
        else
        {
            throw new RuntimeException ( "UI have not been initialized" );
        }
        return runCount;
    }
}