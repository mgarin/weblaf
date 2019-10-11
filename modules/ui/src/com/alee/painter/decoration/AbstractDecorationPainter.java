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

package com.alee.painter.decoration;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.Clone;
import com.alee.api.jdk.Objects;
import com.alee.api.merge.Merge;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.grouping.GroupingLayout;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.managers.focus.GlobalFocusListener;
import com.alee.managers.hover.DefaultHoverTracker;
import com.alee.managers.hover.GlobalHoverListener;
import com.alee.managers.hover.HoverManager;
import com.alee.managers.hover.HoverTracker;
import com.alee.managers.style.Bounds;
import com.alee.managers.style.BoundsType;
import com.alee.managers.style.PainterShapeProvider;
import com.alee.managers.style.StyleManager;
import com.alee.painter.AbstractPainter;
import com.alee.painter.SectionPainter;
import com.alee.utils.*;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.List;
import java.util.*;

/**
 * Abstract decoration painter that can be used by any custom and specific painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public abstract class AbstractDecorationPainter<C extends JComponent, U extends ComponentUI, D extends IDecoration<C, D>>
        extends AbstractPainter<C, U> implements IDecorationPainter<C, U, D>, PainterShapeProvider<C>
{
    /**
     * Decoratable states property.
     */
    public static final String DECORATION_STATES_PROPERTY = "decorationStates";
    public static final String DECORATION_BORDER_PROPERTY = "decorationBorder";

    /**
     * Available decorations.
     * Each decoration provides a visual representation of specific component state.
     */
    protected Decorations<C, D> decorations;

    /**
     * Listeners.
     */
    protected transient ContainerListener containerListener;
    protected transient FocusTracker focusStateTracker;
    protected transient GlobalFocusListener inFocusedParentTracker;
    protected transient AncestorListener inFocusedParentAncestorListener;
    protected transient HoverTracker hoverStateTracker;
    protected transient GlobalHoverListener inHoveredParentTracker;
    protected transient AncestorListener inHoveredParentAncestorListener;
    protected transient HierarchyListener hierarchyTracker;
    protected transient ContainerListener neighboursTracker;

    /**
     * Runtime variables.
     */
    protected transient List<String> states;
    protected transient Map<String, D> stateDecorationCache;
    protected transient Map<String, D> decorationCache;
    protected transient String current;
    protected transient boolean focused;
    protected transient boolean inFocusedParent;
    protected transient boolean hover;
    protected transient boolean inHoveredParent;
    protected transient Container ancestor;

    @Override
    protected void afterInstall ()
    {
        /**
         * Determining initial decoration states.
         * We should always update states last to ensure initial values are correct.
         * Although we still do it before updating border in {@link super#afterInstall()}.
         */
        this.states = collectDecorationStates ();

        /**
         * Performing basic actions after installation ends.
         */
        super.afterInstall ();
    }

    @Override
    protected void beforeUninstall ()
    {
        /**
         * Performing basic actions before uninstallation starts.
         */
        super.beforeUninstall ();

        /**
         * Making sure last used decoration is properly deactivated.
         * If we don't deactivate last decoration it will stay active and will keep recieving component updates.
         */
        deactivateLastDecoration ( component );
    }

    @Override
    protected void afterUninstall ()
    {
        /**
         * Cleaning up decoration caches.
         */
        this.stateDecorationCache = null;
        this.decorationCache = null;
        this.states = null;

        /**
         * Performing basic actions after uninstallation ends.
         */
        super.afterUninstall ();
    }

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();

        // Installing various extra listeners
        installChildrenListeners ();
        installFocusListeners ();
        installInFocusedParentListeners ();
        installHoverListeners ();
        installInHoveredParentListeners ();
        installBorderListeners ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        // Uninstalling various extra listeners
        uninstallBorderListeners ();
        uninstallInHoveredParentListeners ();
        uninstallHoverListeners ();
        uninstallInFocusedParentListeners ();
        uninstallFocusListeners ();
        uninstallChildrenListeners ();

        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating focus listener
        if ( Objects.equals ( property, WebLookAndFeel.FOCUSABLE_PROPERTY ) )
        {
            updateFocusListeners ();
        }

        // Updating custom decoration states
        if ( Objects.equals ( property, DECORATION_STATES_PROPERTY ) )
        {
            updateDecorationState ();
        }

        // Updating border
        if ( Objects.equals ( property, DECORATION_BORDER_PROPERTY ) )
        {
            updateBorder ();
        }

        // Updating enabled state
        if ( Objects.equals ( property, WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            if ( usesState ( DecorationState.enabled ) || usesState ( DecorationState.disabled ) )
            {
                updateDecorationState ();
            }
        }
    }

    /**
     * Overridden to provide decoration states update instead of simple view updates.
     */
    @Override
    protected void orientationChange ()
    {
        // Saving new orientation
        saveOrientation ();

        // Updating decoration states
        updateDecorationState ();
    }

    /**
     * Returns whether or not component has distinct container view.
     * todo Replace with {@link #usesState(String)} override
     *
     * @return {@code true} if component has distinct container view, {@code false} otherwise
     */
    protected boolean usesContainerView ()
    {
        return usesState ( DecorationState.hasChildren ) || usesState ( DecorationState.hasNoChildren );
    }

    /**
     * Installs {@link ContainerListener} that will perform decoration updates on children change.
     */
    protected void installChildrenListeners ()
    {
        if ( usesContainerView () )
        {
            containerListener = new ContainerListener ()
            {
                @Override
                public void componentAdded ( @NotNull final ContainerEvent event )
                {
                    AbstractDecorationPainter.this.childrenChanged ( event );
                }

                @Override
                public void componentRemoved ( @NotNull final ContainerEvent event )
                {
                    AbstractDecorationPainter.this.childrenChanged ( event );
                }
            };
            component.addContainerListener ( containerListener );
        }
    }

    /**
     * Informs about {@link Container} children change.
     *
     * @param event {@link ContainerEvent}
     */
    protected void childrenChanged ( @NotNull final ContainerEvent event )
    {
        updateDecorationState ();
    }

    /**
     * Uninstalls {@link ContainerListener}.
     */
    protected void uninstallChildrenListeners ()
    {
        if ( containerListener != null )
        {
            component.removeContainerListener ( containerListener );
            containerListener = null;
        }
    }


    /**
     * Returns whether or not component has distinct focused view.
     * Note that this is exactly distinct view and not state, distinct focused state might actually be missing.
     * todo Replace with {@link #usesState(String)} override
     *
     * @return {@code true} if component has distinct focused view, {@code false} otherwise
     */
    protected boolean usesFocusedView ()
    {
        return component.isFocusable () && usesState ( DecorationState.focused );
    }

    /**
     * Installs listener that will perform decoration updates on focus state change.
     */
    protected void installFocusListeners ()
    {
        if ( usesFocusedView () )
        {
            focusStateTracker = new DefaultFocusTracker ( component, true )
            {
                @Override
                public void focusChanged ( final boolean focused )
                {
                    AbstractDecorationPainter.this.focusChanged ( focused );
                }
            };
            FocusManager.addFocusTracker ( component, focusStateTracker );
            this.focused = focusStateTracker.isFocused ();
        }
        else
        {
            this.focused = false;
        }
    }

    /**
     * Informs about focus state changes.
     * Note that this method will only be fired when component {@link #usesFocusedView()}.
     *
     * @param focused whether or not component has focus
     */
    protected void focusChanged ( final boolean focused )
    {
        // Ensure component is still available
        // This might happen if painter is replaced from another FocusTracker
        if ( AbstractDecorationPainter.this.component != null )
        {
            this.focused = focused;
            updateDecorationState ();
        }
    }

    /**
     * Returns whether or not component has focus.
     *
     * @return true if component has focus, false otherwise
     */
    protected boolean isFocused ()
    {
        return focused;
    }

    /**
     * Uninstalls focus listener.
     */
    protected void uninstallFocusListeners ()
    {
        if ( focusStateTracker != null )
        {
            FocusManager.removeFocusTracker ( component, focusStateTracker );
            focusStateTracker = null;
            focused = false;
        }
    }

    /**
     * Updates focus listener usage.
     */
    protected void updateFocusListeners ()
    {
        if ( usesFocusedView () )
        {
            installFocusListeners ();
        }
        else
        {
            uninstallFocusListeners ();
        }
    }

    /**
     * Returns whether or not component has distinct in-focused-parent view.
     * Note that this is exactly distinct view and not state, distinct in-focused-parent state might actually be missing.
     * todo Replace with {@link #usesState(String)} override
     *
     * @return {@code true} if component has distinct in-focused-parent view, {@code false} otherwise
     */
    protected boolean usesInFocusedParentView ()
    {
        return usesState ( DecorationState.inFocusedParent );
    }

    /**
     * Installs listener that performs decoration updates on focused parent appearance and disappearance.
     */
    protected void installInFocusedParentListeners ()
    {
        if ( usesInFocusedParentView () )
        {
            inFocusedParent = updateInFocusedParent ();
            inFocusedParentTracker = new GlobalFocusListener ()
            {
                @Override
                public void focusChanged ( @Nullable final Component oldFocus, @Nullable final Component newFocus )
                {
                    AbstractDecorationPainter.this.updateInFocusedParent ();
                }
            };
            FocusManager.registerGlobalFocusListener ( component, inFocusedParentTracker );
            inFocusedParentAncestorListener = new AncestorListener ()
            {
                @Override
                public void ancestorAdded ( @NotNull final AncestorEvent event )
                {
                    AbstractDecorationPainter.this.updateInFocusedParent ();
                }

                @Override
                public void ancestorRemoved ( @NotNull final AncestorEvent event )
                {
                    AbstractDecorationPainter.this.updateInFocusedParent ();
                }

                @Override
                public void ancestorMoved ( @NotNull final AncestorEvent event )
                {
                    AbstractDecorationPainter.this.updateInFocusedParent ();
                }
            };
            component.addAncestorListener ( inFocusedParentAncestorListener );
        }
        else
        {
            inFocusedParent = false;
        }
    }

    /**
     * Updates {@link #inFocusedParent} state.
     * For that we are checking whether or not any related component gained or lost focus.
     * Note that this method will only be fired when component {@link #usesInFocusedParentView()}.
     * It is not recommended to use this state unless component's direct or close parent uses {@code 'focused'} state.
     *
     * @return {@code true} if component is placed within focused parent, {@code false} otherwise
     */
    protected boolean updateInFocusedParent ()
    {
        // Ensure component is still available
        // This might happen if painter is replaced from another GlobalFocusListener
        if ( AbstractDecorationPainter.this.component != null )
        {
            final boolean old = inFocusedParent;
            inFocusedParent = false;
            Container current = component;
            while ( current != null )
            {
                if ( current.isFocusOwner () )
                {
                    // Directly in a focused parent
                    inFocusedParent = true;
                    break;
                }
                else if ( current != component && current instanceof JComponent )
                {
                    // Ensure that component supports styling
                    final JComponent jComponent = ( JComponent ) current;
                    if ( LafUtils.hasWebLafUI ( jComponent ) )
                    {
                        // In a parent that tracks children focus and visually displays it
                        // This case is not obvious but really important for correct visual representation of the state
                        final ComponentUI ui = LafUtils.getUI ( jComponent );
                        if ( ui != null )
                        {
                            // todo Replace with proper painter retrieval upon Paintable interface implementation
                            final Object painter = ReflectUtils.getFieldValueSafely ( ui, "painter" );
                            if ( painter != null && painter instanceof AbstractDecorationPainter )
                            {
                                final AbstractDecorationPainter dp = ( AbstractDecorationPainter ) painter;
                                if ( dp.usesFocusedView () )
                                {
                                    inFocusedParent = dp.isFocused ();
                                    break;
                                }
                            }
                        }
                    }
                }
                current = current.getParent ();
            }
            if ( Objects.notEquals ( old, inFocusedParent ) )
            {
                updateDecorationState ();
            }
        }
        return inFocusedParent;
    }

    /**
     * Returns whether or not one of this component parents displays focused state.
     * Returns {@code true} if one of parents owns focus directly or indirectly due to one of its children being focused.
     * Indirect focus ownership is only accepted from parents which use {@link DecorationState#focused} decoration state.
     *
     * @return {@code true} if one of this component parents displays focused state, {@code false} otherwise
     */
    protected boolean isInFocusedParent ()
    {
        return inFocusedParent;
    }

    /**
     * Uninstalls global focus listener.
     */
    protected void uninstallInFocusedParentListeners ()
    {
        if ( inFocusedParentTracker != null )
        {
            component.removeAncestorListener ( inFocusedParentAncestorListener );
            inFocusedParentAncestorListener = null;
            FocusManager.unregisterGlobalFocusListener ( component, inFocusedParentTracker );
            inFocusedParentTracker = null;
            inFocusedParent = false;
        }
    }

    /**
     * Returns whether or not component has distinct hover view.
     * Note that this is exactly distinct view and not state, distinct hover state might actually be missing.
     * todo Replace with {@link #usesState(String)} override
     *
     * @return {@code true} if component has distinct hover view, {@code false} otherwise
     */
    protected boolean usesHoverView ()
    {
        return usesState ( DecorationState.hover );
    }

    /**
     * Installs listener that will perform decoration updates on hover state change.
     */
    protected void installHoverListeners ()
    {
        if ( usesHoverView () )
        {
            hoverStateTracker = new DefaultHoverTracker ( component, false )
            {
                @Override
                public void hoverChanged ( final boolean hover )
                {
                    AbstractDecorationPainter.this.hoverChanged ( hover );
                }
            };
            HoverManager.addHoverTracker ( component, hoverStateTracker );
            this.hover = hoverStateTracker.isHovered ();
        }
        else
        {
            hover = false;
        }
    }

    /**
     * Informs about hover state changes.
     * Note that this method will only be fired when component {@link #usesHoverView()}.
     *
     * @param hover whether or not mouse is on the component
     */
    protected void hoverChanged ( final boolean hover )
    {
        // Ensure component is still available
        // This might happen if painter is replaced from another DefaultHoverTracker
        if ( AbstractDecorationPainter.this.component != null )
        {
            this.hover = hover;
            updateDecorationState ();
        }
    }

    /**
     * Returns whether or not component is in hover state.
     *
     * @return true if component is in hover state, false otherwise
     */
    protected boolean isHover ()
    {
        return hover;
    }

    /**
     * Uninstalls hover listener.
     */
    protected void uninstallHoverListeners ()
    {
        if ( hoverStateTracker != null )
        {
            HoverManager.removeHoverTracker ( component, hoverStateTracker );
            hoverStateTracker = null;
            hover = false;
        }
    }

    /**
     * Updates hover listener usage.
     */
    protected void updateHoverListeners ()
    {
        if ( usesHoverView () )
        {
            installHoverListeners ();
        }
        else
        {
            uninstallHoverListeners ();
        }
    }

    /**
     * Returns whether or not component has distinct in-hovered-parent view.
     * Note that this is exactly distinct view and not state, distinct in-hovered-parent state might actually be missing.
     * todo Replace with {@link #usesState(String)} override
     *
     * @return {@code true} if component has distinct in-hovered-parent view, {@code false} otherwise
     */
    protected boolean usesInHoveredParentView ()
    {
        return usesState ( DecorationState.inHoveredParent );
    }

    /**
     * Installs listener that performs decoration updates on hovered parent appearance and disappearance.
     */
    protected void installInHoveredParentListeners ()
    {
        if ( usesInHoveredParentView () )
        {
            inHoveredParent = updateInHoveredParent ();
            inHoveredParentTracker = new GlobalHoverListener ()
            {
                @Override
                public void hoverChanged ( @Nullable final Component oldHover, @Nullable final Component newHover )
                {
                    AbstractDecorationPainter.this.updateInHoveredParent ();
                }
            };
            HoverManager.registerGlobalHoverListener ( component, inHoveredParentTracker );
            inHoveredParentAncestorListener = new AncestorListener ()
            {
                @Override
                public void ancestorAdded ( @NotNull final AncestorEvent event )
                {
                    AbstractDecorationPainter.this.updateInHoveredParent ();
                }

                @Override
                public void ancestorRemoved ( @NotNull final AncestorEvent event )
                {
                    AbstractDecorationPainter.this.updateInHoveredParent ();
                }

                @Override
                public void ancestorMoved ( @NotNull final AncestorEvent event )
                {
                    AbstractDecorationPainter.this.updateInHoveredParent ();
                }
            };
            component.addAncestorListener ( inHoveredParentAncestorListener );
        }
        else
        {
            inHoveredParent = false;
        }
    }

    /**
     * Updates {@link #inHoveredParent} mark.
     * For that we check whether or not any related component gained or lost hover.
     * Note that this method will only be fired when component {@link #usesInHoveredParentView()}.
     * It is not recommended to use this state unless component's direct or close parent uses {@code 'hover'} state.
     *
     * @return {@code true} if component is placed within hovered parent, {@code false} otherwise
     */
    protected boolean updateInHoveredParent ()
    {
        // Ensure component is still available
        // This might happen if painter is replaced from another GlobalHoverListener
        if ( component != null )
        {
            final boolean old = inHoveredParent;
            inHoveredParent = false;
            Container current = component;
            while ( current != null )
            {
                if ( current == HoverManager.getHoverOwner () )
                {
                    // Directly in a hovered parent
                    inHoveredParent = true;
                    break;
                }
                else if ( current != component && current instanceof JComponent )
                {
                    // Ensure that component supports styling
                    final JComponent jComponent = ( JComponent ) current;
                    if ( LafUtils.hasWebLafUI ( jComponent ) )
                    {
                        // In a parent that tracks children hover and visually displays it
                        // This case is not obvious but really important for correct visual representation of the state
                        final ComponentUI ui = LafUtils.getUI ( jComponent );
                        if ( ui != null )
                        {
                            // todo Replace with proper painter retrieval upon Paintable interface implementation
                            final Object painter = ReflectUtils.getFieldValueSafely ( ui, "painter" );
                            if ( painter != null && painter instanceof AbstractDecorationPainter )
                            {
                                final AbstractDecorationPainter dp = ( AbstractDecorationPainter ) painter;
                                if ( dp.usesHoverView () )
                                {
                                    inHoveredParent = dp.isHover ();
                                    break;
                                }
                            }
                        }
                    }
                }
                current = current.getParent ();
            }
            if ( Objects.notEquals ( old, inHoveredParent ) )
            {
                updateDecorationState ();
            }
        }
        return inHoveredParent;
    }

    /**
     * Returns whether or not one of this component parents displays hover state.
     * Returns {@code true} if one of parents owns hover directly or indirectly due to one of its children being hover.
     * Indirect hover ownership is only accepted from parents which use {@link DecorationState#hover} decoration state.
     *
     * @return {@code true} if one of this component parents displays hover state, {@code false} otherwise
     */
    protected boolean isInHoveredParent ()
    {
        return inHoveredParent;
    }

    /**
     * Uninstalls global hover listener.
     */
    protected void uninstallInHoveredParentListeners ()
    {
        if ( inHoveredParentTracker != null )
        {
            component.removeAncestorListener ( inHoveredParentAncestorListener );
            inHoveredParentAncestorListener = null;
            HoverManager.unregisterGlobalHoverListener ( component, inHoveredParentTracker );
            inHoveredParentTracker = null;
            inHoveredParent = false;
        }
    }

    /**
     * Returns whether or not component has distinct hierarchy-based view.
     *
     * @return {@code true} if component has distinct hierarchy-based view, {@code false} otherwise
     */
    protected boolean usesHierarchyBasedView ()
    {
        return true;
    }

    /**
     * Installs listener that will perform border updates on component hierarchy changes.
     * This is required to properly update decoration borders in case it was moved from or into container with grouping layout.
     * It also tracks neighbour components addition and removal to update this component border accordingly.
     */
    protected void installBorderListeners ()
    {
        if ( usesHierarchyBasedView () )
        {
            neighboursTracker = new ContainerListener ()
            {
                @Override
                public void componentAdded ( @NotNull final ContainerEvent event )
                {
                    // Ensure component is still available
                    // This might happen if painter is replaced from another ContainerListener
                    if ( AbstractDecorationPainter.this.component != null )
                    {
                        // Updating border when a child was added nearby
                        if ( ancestor != null && ancestor.getLayout () instanceof GroupingLayout &&
                                event.getChild () != AbstractDecorationPainter.this.component )
                        {
                            AbstractDecorationPainter.this.updateBorder ();
                        }
                    }
                }

                @Override
                public void componentRemoved ( @NotNull final ContainerEvent event )
                {
                    // Ensure component is still available
                    // This might happen if painter is replaced from another ContainerListener
                    if ( AbstractDecorationPainter.this.component != null )
                    {
                        // Updating border when a child was removed nearby
                        if ( ancestor != null && ancestor.getLayout () instanceof GroupingLayout &&
                                event.getChild () != AbstractDecorationPainter.this.component )
                        {
                            AbstractDecorationPainter.this.updateBorder ();
                        }
                    }
                }
            };
            hierarchyTracker = new HierarchyListener ()
            {
                @Override
                public void hierarchyChanged ( @NotNull final HierarchyEvent event )
                {
                    AbstractDecorationPainter.this.hierarchyChanged ( event );
                }
            };
            component.addHierarchyListener ( hierarchyTracker );
        }
        else
        {
            ancestor = null;
        }
    }

    /**
     * Informs about hierarchy changes.
     * Note that this method will only be fired when component {@link #usesHierarchyBasedView()}.
     *
     * @param event {@link HierarchyEvent}
     */
    protected void hierarchyChanged ( @NotNull final HierarchyEvent event )
    {
        // Ensure component is still available
        // This might happen if painter is replaced from another HierarchyListener
        if ( AbstractDecorationPainter.this.component != null )
        {
            // Listening only for parent change event
            // It will inform us when parent container for this component changes
            // Ancestor listener is not really reliable because it might inform about consequent parent changes
            if ( ( event.getChangeFlags () & HierarchyEvent.PARENT_CHANGED ) == HierarchyEvent.PARENT_CHANGED )
            {
                // If there was a previous container...
                if ( ancestor != null )
                {
                    // Stop tracking neighbours
                    ancestor.removeContainerListener ( neighboursTracker );
                }

                // Updating ancestor
                ancestor = AbstractDecorationPainter.this.component.getParent ();

                // If there is a new container...
                if ( ancestor != null )
                {
                    // Start tracking neighbours
                    ancestor.addContainerListener ( neighboursTracker );

                    // Updating border
                    updateBorder ();
                }
            }
        }
    }

    /**
     * Uninstalls hierarchy listener.
     */
    protected void uninstallBorderListeners ()
    {
        if ( hierarchyTracker != null )
        {
            component.removeHierarchyListener ( hierarchyTracker );
            hierarchyTracker = null;
            if ( ancestor != null )
            {
                ancestor.removeContainerListener ( neighboursTracker );
                ancestor = null;
            }
            neighboursTracker = null;
        }
    }

    /**
     * Returns whether or not component is in enabled state.
     *
     * @return {@code true} if component is in enabled state, {@code false} otherwise
     */
    protected boolean isEnabled ()
    {
        return component != null && component.isEnabled ();
    }

    /**
     * Returns properly sorted current component decoration states.
     *
     * @return properly sorted current component decoration states
     */
    @NotNull
    protected final List<String> collectDecorationStates ()
    {
        // Retrieving current decoration states
        final List<String> states = getDecorationStates ();

        // Adding custom Skin decoration states
        states.addAll ( DecorationUtils.getExtraStates ( StyleManager.getSkin ( component ) ) );

        // Adding custom UI decoration states
        states.addAll ( DecorationUtils.getExtraStates ( ui ) );

        // Adding custom component decoration states
        states.addAll ( DecorationUtils.getExtraStates ( component ) );

        // Sorting states to always keep the same order
        Collections.sort ( states );

        return states;
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = new ArrayList<String> ( 12 );
        states.add ( SystemUtils.getShortOsName () );
        states.add ( isEnabled () ? DecorationState.enabled : DecorationState.disabled );
        states.add ( ltr ? DecorationState.leftToRight : DecorationState.rightToLeft );
        states.add ( component.getComponentCount () > 0 ? DecorationState.hasChildren : DecorationState.hasNoChildren );
        if ( isFocused () )
        {
            states.add ( DecorationState.focused );
        }
        if ( isInFocusedParent () )
        {
            states.add ( DecorationState.inFocusedParent );
        }
        if ( isHover () )
        {
            states.add ( DecorationState.hover );
        }
        if ( isInHoveredParent () )
        {
            states.add ( DecorationState.inHoveredParent );
        }
        return states;
    }

    @Override
    public final boolean usesState ( @NotNull final String state )
    {
        // Checking whether or not this painter uses this decoration state
        boolean usesState = usesState ( decorations, state );

        // Checking whether or not section painters used by this painter use it
        if ( !usesState )
        {
            final List<SectionPainter<C, U>> sectionPainters = getInstalledSectionPainters ();
            if ( CollectionUtils.notEmpty ( sectionPainters ) )
            {
                for ( final SectionPainter<C, U> section : sectionPainters )
                {
                    if ( section instanceof IDecorationPainter )
                    {
                        if ( ( ( IDecorationPainter ) section ).usesState ( state ) )
                        {
                            usesState = true;
                            break;
                        }
                    }
                }
            }
        }

        return usesState;
    }

    /**
     * Returns whether specified decorations are associated with specified state.
     *
     * @param decorations decorations
     * @param state       decoration state
     * @return {@code true} if specified decorations are associated with specified state, {@code false} otherwise
     */
    protected final boolean usesState ( @Nullable final Decorations<C, D> decorations, final String state )
    {
        boolean usesState = false;
        if ( decorations != null && decorations.size () > 0 )
        {
            for ( final D decoration : decorations )
            {
                if ( decoration.usesState ( state ) )
                {
                    usesState = true;
                    break;
                }
            }
        }
        return usesState;
    }

    /**
     * Returns decorations for the specified states.
     *
     * @param forStates decoration states to retrieve decoration for
     * @return decorations for the specified states
     */
    @NotNull
    protected final List<D> getDecorations ( @NotNull final List<String> forStates )
    {
        final List<D> result = new ArrayList<D> ();
        if ( decorations != null && decorations.size () > 0 )
        {
            for ( final D decoration : decorations )
            {
                if ( decoration.isApplicableTo ( forStates ) )
                {
                    result.add ( decoration );
                }
            }
        }
        return result;
    }

    @Nullable
    @Override
    public final D getDecoration ()
    {
        final D result;
        if ( decorations != null && decorations.size () > 0 )
        {
            // Decoration key
            // States are properly sorted, so their order is always the same
            final String previous = this.current;
            current = TextUtils.listToString ( states, "," );

            // Creating decoration caches
            if ( stateDecorationCache == null )
            {
                // State decorations cache
                // Entry: [ component state -> built decoration reference ]
                // It is used for fastest possible access to component state decorations
                stateDecorationCache = new HashMap<String, D> ( decorations.size () );

                // Decoration combinations cache
                // Entry: [ decorations combination key -> built decoration reference ]
                // It is used to avoid excessive memory usage by duplicate decoration combinations for each specific state
                decorationCache = new HashMap<String, D> ( decorations.size () );
            }

            // Resolving state decoration if it is not yet cached
            if ( !stateDecorationCache.containsKey ( current ) )
            {
                // Retrieving all decorations fitting current states
                final List<D> decorations = getDecorations ( states );

                // Retrieving unique key for decorations combination
                final String decorationsKey = getDecorationsKey ( decorations );

                // Retrieving existing decoration or building a new one
                final D decoration;
                if ( decorationCache.containsKey ( decorationsKey ) )
                {
                    // Retrieving decoration from existing built decorations cache
                    decoration = decorationCache.get ( decorationsKey );
                }
                else
                {
                    // Building single decoration from a set
                    if ( CollectionUtils.isEmpty ( decorations ) )
                    {
                        // No decoration for the states available
                        decoration = null;
                    }
                    else if ( decorations.size () == 1 )
                    {
                        // Single existing decoration for the states
                        decoration = Clone.deep ().clone ( decorations.get ( 0 ) );
                    }
                    else
                    {
                        // Filter out possible decorations of different type
                        // We always use type of the last one available since it has higher priority
                        final Class<? extends IDecoration> type = decorations.get ( decorations.size () - 1 ).getClass ();
                        final Iterator<D> iterator = decorations.iterator ();
                        while ( iterator.hasNext () )
                        {
                            final D d = iterator.next ();
                            if ( d.getClass () != type )
                            {
                                iterator.remove ();
                            }
                        }

                        // Merging multiple decorations together
                        decoration = Merge.deep ().merge ( decorations );
                    }

                    // Updating built decoration settings
                    if ( decoration != null )
                    {
                        // Updating section mark
                        // This is done for each cached decoration once as it doesn't change
                        decoration.setSection ( isSectionPainter () );
                    }

                    // Caching built decoration
                    decorationCache.put ( decorationsKey, decoration );
                }

                // Caching resulting decoration under the state key
                stateDecorationCache.put ( current, decoration );
            }

            // Performing decoration activation and deactivation if needed
            if ( previous == null && current == null )
            {
                // Activating initial decoration
                final D initialDecoration = stateDecorationCache.get ( current );
                if ( initialDecoration != null )
                {
                    initialDecoration.activate ( component );
                }
            }
            else if ( Objects.notEquals ( previous, current ) )
            {
                // Checking that decoration was actually changed
                final D previousDecoration = stateDecorationCache.get ( previous );
                final D currentDecoration = stateDecorationCache.get ( current );
                if ( previousDecoration != currentDecoration )
                {
                    // Deactivating previous decoration
                    if ( previousDecoration != null )
                    {
                        previousDecoration.deactivate ( component );
                    }
                    // Activating current decoration
                    if ( currentDecoration != null )
                    {
                        currentDecoration.activate ( component );
                    }
                }
            }

            // Returning existing decoration
            result = stateDecorationCache.get ( current );
        }
        else
        {
            // No decorations added
            result = null;
        }
        return result;
    }

    /**
     * Returns unique decorations combination key.
     *
     * @param decorations decorations to retrieve unique combination key for
     * @return unique decorations combination key
     */
    @NotNull
    protected final String getDecorationsKey ( @NotNull final List<D> decorations )
    {
        final StringBuilder key = new StringBuilder ( 15 * decorations.size () );
        for ( final D decoration : decorations )
        {
            if ( key.length () > 0 )
            {
                key.append ( ";" );
            }
            key.append ( decoration.getId () );
        }
        return key.toString ();
    }

    /**
     * Performs deactivation of the recently used decoration.
     *
     * @param c painted component
     */
    protected final void deactivateLastDecoration ( @NotNull final C c )
    {
        final D decoration = getDecoration ();
        if ( decoration != null )
        {
            decoration.deactivate ( c );
        }
    }

    @Override
    public final void updateDecorationState ()
    {
        final List<String> states = collectDecorationStates ();
        if ( !CollectionUtils.equals ( this.states, states, true ) )
        {
            // Saving new decoration states
            this.states = states;

            // Updating section painters decoration states
            // This is required to provide state changes into section painters used within this painter
            // Section painters that use default states collection mechanism are dependant on origin painter states
            final List<SectionPainter<C, U>> sectionPainters = getInstalledSectionPainters ();
            if ( CollectionUtils.notEmpty ( sectionPainters ) )
            {
                for ( final SectionPainter<C, U> section : sectionPainters )
                {
                    if ( section instanceof IDecorationPainter )
                    {
                        ( ( IDecorationPainter ) section ).updateDecorationState ();
                    }
                }
            }

            // Updating component visual state
            revalidate ();
            repaint ();
        }
    }

    @Nullable
    @Override
    protected Insets getBorder ()
    {
        final Insets insets;
        final D decoration = getDecoration ();
        if ( decoration != null && isDecorationAvailable ( decoration ) )
        {
            insets = decoration.getBorderInsets ( component );
        }
        else
        {
            insets = null;
        }
        return insets;
    }

    @NotNull
    @Override
    public Shape provideShape ( @NotNull final C component, @NotNull final Rectangle bounds )
    {
        final Shape shape;
        final D decoration = getDecoration ();
        if ( decoration != null && isDecorationAvailable ( decoration ) )
        {
            shape = decoration.provideShape ( component, bounds );
        }
        else
        {
            shape = bounds;
        }
        return shape;
    }

    @Nullable
    @Override
    public Boolean isOpaque ()
    {
        final Boolean opaque;
        final D decoration = getDecoration ();
        if ( decoration != null && isDecorationAvailable ( decoration ) )
        {
            opaque = isOpaqueDecorated ();
        }
        else
        {
            opaque = isOpaqueUndecorated ();
        }
        return opaque;
    }

    /**
     * Returns opacity state for decorated component.
     * This is separated from base opacity state method to allow deep customization.
     *
     * @return opacity state for decorated component
     */
    protected Boolean isOpaqueDecorated ()
    {
        // Returns {@code false} to make component non-opaque when decoration is available
        // This is convenient because almost any custom decoration might have transparent spots in it
        // And if it has any it cannot be opaque or it will cause major painting glitches
        return false;
    }

    /**
     * Returns opacity state for undecorated component.
     * This is separated from base opacity state method to allow deep customization.
     *
     * @return opacity state for undecorated component
     */
    protected Boolean isOpaqueUndecorated ()
    {
        // Returns {@code null} to disable automatic opacity changes by default
        // You may still provide a non-null opacity in your own painter implementations
        return null;
    }

    @Override
    public boolean contains ( @NotNull final C c, @NotNull final U ui, @NotNull final Bounds bounds, final int x, final int y )
    {
        final boolean contains;
        final D decoration = getDecoration ();
        if ( decoration != null && isDecorationAvailable ( decoration ) )
        {
            // Creating additional bounds
            final Bounds marginBounds = new Bounds ( bounds, BoundsType.margin, c, decoration );

            // Using decoration contains method
            contains = decoration.contains ( c, marginBounds, x, y );
        }
        else
        {
            // Using default contains method
            contains = super.contains ( c, ui, bounds, x, y );
        }
        return contains;
    }

    @Override
    public int getBaseline ( @NotNull final C c, @NotNull final U ui, @NotNull final Bounds bounds )
    {
        final int baseline;
        final D decoration = getDecoration ();
        if ( decoration != null && isDecorationAvailable ( decoration ) )
        {
            // Creating additional bounds
            final Bounds marginBounds = new Bounds ( bounds, BoundsType.margin, c, decoration );

            // Calculating decoration baseline
            baseline = decoration.getBaseline ( c, marginBounds );
        }
        else
        {
            // Calculating default baseline
            baseline = super.getBaseline ( c, ui, bounds );
        }
        return baseline;
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final C c, @NotNull final U ui )
    {
        final Component.BaselineResizeBehavior behavior;
        final D decoration = getDecoration ();
        if ( decoration != null && isDecorationAvailable ( decoration ) )
        {
            // Returning decoration baseline behavior
            behavior = decoration.getBaselineResizeBehavior ( c );
        }
        else
        {
            // Returning default baseline behavior
            behavior = super.getBaselineResizeBehavior ( c, ui );
        }
        return behavior;
    }

    @Override
    public void paint ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Bounds bounds )
    {
        // Checking whether plain background is required
        if ( isPlainBackgroundRequired ( c ) )
        {
            // Painting simple background
            // This block added to avoid various visual glitches
            g2d.setPaint ( c.getBackground () );
            g2d.fill ( bounds.get () );
        }

        // Painting current decoration state
        final D decoration = getDecoration ();
        if ( decoration != null && isDecorationAvailable ( decoration ) )
        {
            // Creating additional bounds
            final Bounds marginBounds = new Bounds ( bounds, BoundsType.margin, c, decoration );

            // Painting current decoration state
            decoration.paint ( g2d, c, marginBounds );
        }

        // Painting content
        paintContent ( g2d, c, ui, bounds.get () );
    }

    /**
     * Paints additional custom content provided by this painter.
     * todo This might eventually be removed if all contents will be painted within IContent implementations
     *
     * @param g2d    graphics context
     * @param c      painted component
     * @param ui     painted component UI
     * @param bounds painting bounds
     */
    protected void paintContent ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Rectangle bounds )
    {
        /**
         * No content available by default.
         */
    }

    /**
     * Returns whether or not painting plain component background is required.
     * When component is opaque we must fill every single pixel in its bounds with something to avoid issues.
     * By default this condition is limited to component being opaque.
     *
     * @param c component to paint background for
     * @return {@code true} if painting plain component background is required, {@code false} otherwise
     */
    protected boolean isPlainBackgroundRequired ( final C c )
    {
        return c.isOpaque ();
    }

    /**
     * Returns whether or not painting specified decoration is available.
     *
     * @param decoration decoration to be painted
     * @return {@code true} if painting specified decoration is available, {@code false} otherwise
     */
    protected boolean isDecorationAvailable ( @NotNull final D decoration )
    {
        return true;
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension ps = super.getPreferredSize ();
        final D d = getDecoration ();
        return d != null ? SwingUtils.max ( d.getPreferredSize ( component ), ps ) : ps;
    }
}