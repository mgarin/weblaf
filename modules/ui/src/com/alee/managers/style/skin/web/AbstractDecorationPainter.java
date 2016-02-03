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

package com.alee.managers.style.skin.web;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.managers.style.PainterShapeProvider;
import com.alee.managers.style.skin.web.data.DecorationState;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;
import com.alee.painter.AbstractPainter;
import com.alee.painter.SectionPainter;
import com.alee.utils.*;
import com.alee.utils.swing.AbstractHoverBehavior;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Abstract web-style decoration painter that can be used by any custom and specific painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public abstract class AbstractDecorationPainter<E extends JComponent, U extends ComponentUI, D extends IDecoration<E, D>>
        extends AbstractPainter<E, U> implements PainterShapeProvider<E>
{
    /**
     * Decoration states.
     */
    protected List<D> decorations;

    /**
     * Listeners.
     */
    protected transient FocusTracker focusTracker;
    protected transient AbstractHoverBehavior<E> hoverTracker;

    /**
     * Runtime variables.
     */
    protected transient List<String> states;
    protected transient Map<String, D> decorationCache;
    protected transient boolean focused = false;
    protected transient boolean hover = false;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Determining initial decoration state
        this.states = collectDecorationStates ();

        // Installing focus state listener
        if ( usesState ( DecorationState.focused ) )
        {
            focusTracker = new DefaultFocusTracker ()
            {
                @Override
                public void focusChanged ( final boolean focused )
                {
                    // Updating focus state
                    AbstractDecorationPainter.this.focused = focused;

                    // Updating decoration
                    if ( isSettingsUpdateAllowed () )
                    {
                        updateDecorationState ();
                    }
                }
            };
            FocusManager.addFocusTracker ( component, focusTracker );
        }

        // Installing hover state listener
        if ( usesState ( DecorationState.hover ) )
        {
            hoverTracker = new AbstractHoverBehavior<E> ( component, false )
            {
                @Override
                public void hoverChanged ( final boolean hover )
                {
                    // Updating hover state
                    AbstractDecorationPainter.this.hover = hover;

                    // Updating decoration
                    if ( isSettingsUpdateAllowed () )
                    {
                        updateDecorationState ();
                    }
                }
            };
            hoverTracker.install ();
        }
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        if ( hoverTracker != null )
        {
            hoverTracker.uninstall ();
            hoverTracker = null;
        }
        if ( focusTracker != null )
        {
            FocusManager.removeFocusTracker ( focusTracker );
            focusTracker = null;
        }

        // Cleaning up variables
        this.decorationCache = null;
        this.states = null;

        super.uninstall ( c, ui );
    }

    @Override
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChange ( property, oldValue, newValue );

        // Updating decoration state
        if ( isSettingsUpdateAllowed () )
        {
            if ( CompareUtils.equals ( property, WebLookAndFeel.ENABLED_PROPERTY ) )
            {
                if ( usesState ( DecorationState.disabled ) )
                {
                    updateDecorationState ();
                }
            }
        }
    }

    /**
     * Returns section painters used within this painter.
     * Might also return {@code null} in case no section painters are used within this one.
     * This method is used for various internal update mechanisms involving section painters.
     *
     * @return section painters used within this painter
     */
    protected List<SectionPainter<E, U>> getSectionPainters ()
    {
        return null;
    }

    /**
     * Returns section painters list in a most optimal way.
     * Utility method for usage inside of classed extending this one.
     *
     * @param sections section painters, some or all of them can be {@code null}
     * @return section painters list in a most optimal way
     */
    protected final List<SectionPainter<E, U>> asList ( final SectionPainter<E, U>... sections )
    {
        ArrayList<SectionPainter<E, U>> list = null;
        if ( sections != null )
        {
            for ( final SectionPainter<E, U> section : sections )
            {
                if ( section != null )
                {
                    if ( list == null )
                    {
                        list = new ArrayList<SectionPainter<E, U>> ( sections.length );
                    }
                    list.add ( section );
                }
            }
        }
        return list;
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
     * Returns whether or not component is in hover state.
     *
     * @return true if component is in hover state, false otherwise
     */
    protected boolean isHover ()
    {
        return hover;
    }

    /**
     * Returns whether or not component is in enabled state.
     *
     * @return true if component is in enabled state, false otherwise
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
    protected final List<String> collectDecorationStates ()
    {
        // Retrieving current decoration states
        final List<String> states = getDecorationStates ();

        // Sorting states to always keep the same order
        Collections.sort ( states );

        return states;
    }

    /**
     * Returns current component decoration states.
     *
     * @return current component decoration states
     */
    protected List<String> getDecorationStates ()
    {
        final List<String> states = new ArrayList<String> ();
        if ( !isEnabled () )
        {
            states.add ( DecorationState.disabled );
        }
        if ( isFocused () )
        {
            states.add ( DecorationState.focused );
        }
        if ( isHover () )
        {
            states.add ( DecorationState.hover );
        }
        return states;
    }

    /**
     * Returns whether component has decoration associated with specified state.
     *
     * @param state decoration state
     * @return true if component has decoration associated with specified state, false otherwise
     */
    protected boolean usesState ( final String state )
    {
        // Checking whether or not this painter uses this decoration state
        boolean usesState = usesState ( decorations, state );

        // Checking whether or not section painters used by this painter use it
        if ( !usesState )
        {
            final List<SectionPainter<E, U>> sectionPainters = getSectionPainters ();
            if ( !CollectionUtils.isEmpty ( sectionPainters ) )
            {
                for ( final SectionPainter<E, U> section : sectionPainters )
                {
                    if ( section instanceof AbstractDecorationPainter )
                    {
                        if ( usesState ( ( ( AbstractDecorationPainter ) section ).decorations, state ) )
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
     * @return true if specified decorations are associated with specified state, false otherwise
     */
    protected final boolean usesState ( final List<D> decorations, final String state )
    {
        if ( !CollectionUtils.isEmpty ( decorations ) )
        {
            for ( final D decoration : decorations )
            {
                final List<String> states = decoration.getStates ();
                if ( states != null && states.contains ( state ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns decorations for the specified states.
     *
     * @param forStates decoration states to retrieve decoration for
     * @return decorations for the specified states
     */
    protected List<D> getDecorations ( final List<String> forStates )
    {
        if ( !CollectionUtils.isEmpty ( decorations ) )
        {
            final List<D> d = new ArrayList<D> ( 1 );
            for ( final D decoration : decorations )
            {
                final List<String> states = decoration.getStates ();
                if ( !CollectionUtils.isEmpty ( states ) )
                {
                    boolean containsAll = true;
                    for ( final String state : states )
                    {
                        if ( CollectionUtils.isEmpty ( forStates ) || !forStates.contains ( state ) )
                        {
                            containsAll = false;
                            break;
                        }
                    }
                    if ( containsAll )
                    {
                        d.add ( decoration );
                    }
                }
                else
                {
                    d.add ( decoration );
                }
            }
            return d;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns decoration matching current states.
     * Decorations returned here are cached copies of the data presented in skins.
     * This was made to avoid corrupting inital data and to increase the decoration retrieval speed.
     *
     * @return decoration matching current states
     */
    protected D getDecoration ()
    {
        // Optimization for painter without decorations
        if ( !CollectionUtils.isEmpty ( decorations ) )
        {
            // Decoration key
            // States are properly sorted, so their order is always the same
            final String key = TextUtils.listToString ( states, "," );

            // Creating decorations cache
            if ( decorationCache == null )
            {
                decorationCache = new HashMap<String, D> ( decorations.size () );
            }

            // Resolving decoration if it wasn't cached yet
            if ( !decorationCache.containsKey ( key ) )
            {
                // Retrieving all decorations fitting current states
                final List<D> decorations = getDecorations ( states );

                // Resolving resulting decoration
                final D decoration;
                if ( CollectionUtils.isEmpty ( decorations ) )
                {
                    // No decoration for the states available
                    decoration = null;
                }
                else if ( decorations.size () == 1 )
                {
                    // Single existing decoration for the states
                    decoration = MergeUtils.clone ( decorations.get ( 0 ) );
                }
                else
                {
                    // Merging multiple decorations together
                    decoration = MergeUtils.clone ( decorations.get ( 0 ) );
                    for ( int i = 1; i < decorations.size (); i++ )
                    {
                        decoration.merge ( decorations.get ( i ) );
                    }
                }

                // Caching resulting decoration
                decorationCache.put ( key, decoration );
            }

            return decorationCache.get ( key );
        }
        else
        {
            return null;
        }
    }

    /**
     * Performs current decoration state update.
     */
    protected void updateDecorationState ()
    {
        final List<String> states = collectDecorationStates ();
        if ( !CollectionUtils.equals ( this.states, states ) )
        {
            // Saving new decoration states
            this.states = states;

            // Updating section painters decoration states
            // This is required to provide state changes into section painters used within this painter
            final List<SectionPainter<E, U>> sectionPainters = getSectionPainters ();
            if ( !CollectionUtils.isEmpty ( sectionPainters ) )
            {
                for ( final SectionPainter<E, U> section : sectionPainters )
                {
                    if ( section instanceof AbstractDecorationPainter )
                    {
                        ( ( AbstractDecorationPainter ) section ).updateDecorationState ();
                    }
                }
            }

            // Updating state if allowed
            if ( isSettingsUpdateAllowed () )
            {
                // States debug message
                // System.out.println ( ReflectUtils.getClassName ( getClass () ) + ": " + TextUtils.listToString ( states, "," ) );

                // Updating component visual state
                // todo Visual updates? Optimized?
                revalidate ();
            }
        }
    }

    @Override
    public Insets getBorders ()
    {
        final IDecoration decoration = getDecoration ();
        return decoration != null ? decoration.getBorderInsets () : null;
    }

    @Override
    public Shape provideShape ( final E component, final Rectangle bounds )
    {
        final IDecoration decoration = getDecoration ();
        return decoration != null ? decoration.provideShape ( component, bounds ) : bounds;
    }

    @Override
    public Boolean isOpaque ()
    {
        // Returns null to disable automatic opacity changes by default
        // You may still provide a non-null opacity in your own painter implementations
        final IDecoration decoration = getDecoration ();
        return decoration == null || !decoration.isVisible () ? null : false;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final IDecoration decoration = getDecoration ();
        if ( decoration != null && decoration.isVisible () )
        {
            // Painting current decoration state
            decoration.paint ( g2d, bounds, c );
        }
        else if ( c.isOpaque () )
        {
            // Paint simple background if undecorated & opaque
            // Otherwise component will cause various visual glitches
            g2d.setPaint ( c.getBackground () );
            g2d.fillRect ( bounds.x, bounds.y, bounds.width, bounds.height );
        }
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension ps = super.getPreferredSize ();
        final D decoration = getDecoration ();
        return decoration != null ? SwingUtils.max ( decoration.getPreferredSize (), ps ) : ps;
    }
}