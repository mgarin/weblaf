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

package com.alee.managers.focus;

import com.alee.utils.SwingUtils;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.collection.WeakHashSet;

import javax.swing.FocusManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * Default {@link FocusTracker} implementation for {@link FocusTracker} usage convenience.
 * This implementation also offers customization of additional focusable {@link Component}s attached to tracked {@link JComponent}.
 *
 * @author Mikle Garin
 */

public abstract class DefaultFocusTracker implements FocusTracker
{
    /**
     * Tracked {@link JComponent}.
     */
    protected final JComponent component;

    /**
     * Whether or not tracking is currently enabled.
     */
    protected boolean enabled;

    /**
     * Whether or not tracked {@link JComponent} and its children should be counted as a single focusable unit.
     */
    protected boolean uniteWithChildren;

    /**
     * Whether or not tracked {@link JComponent} is currently focused according to this tracker settings.
     */
    protected boolean focused;

    /**
     * Additional focusable children which should be tracked in addition to the main tracked {@link JComponent}.
     * Note that this {@link Set} is backed by {@link WeakHashSet} implementation to avoid any memory leaks.
     * So it is safe to add any components here, even ones that might be removed at some point.
     */
    protected Set<Component> focusableChildren;

    /**
     * Constructs new {@link DefaultFocusTracker}.
     *
     * @param component         tracked {@link JComponent}
     * @param uniteWithChildren whether or not tracked {@link JComponent} and its children should be counted as a single focusable unit
     */
    public DefaultFocusTracker ( final JComponent component, final boolean uniteWithChildren )
    {
        super ();
        this.component = component;
        this.enabled = true;
        this.uniteWithChildren = uniteWithChildren;
        this.focused = isInvolved ( component, FocusManager.getCurrentManager ().getFocusOwner () );
    }

    @Override
    public boolean isEnabled ()
    {
        return enabled && component.isShowing ();
    }

    @Override
    public void setEnabled ( final boolean enabled )
    {
        this.enabled = enabled;
    }

    @Override
    public boolean isFocused ()
    {
        return focused;
    }

    @Override
    public void setFocused ( final boolean focused )
    {
        this.focused = focused;
    }

    /**
     * Returns whether or not tracked {@link JComponent} and its children should be counted as a single focusable unit.
     * In case component and its children are counted as one unit - focus changes between them will be ignored by tracker.
     *
     * @return {@code true} if tracked {@link JComponent} and its children should be counted as a single focusable unit, {@code false} otherwise
     */
    public boolean isUniteWithChildren ()
    {
        return uniteWithChildren;
    }

    /**
     * Sets whether or not tracked {@link JComponent} and its children should be counted as a single focusable unit.
     * In case component and its children are counted as one unit - focus changes between them will be ignored by tracker.
     *
     * @param unite whether or not tracked {@link JComponent} and its children should be counted as a single focusable unit
     */
    public void setUniteWithChildren ( final boolean unite )
    {
        this.uniteWithChildren = unite;
    }

    /**
     * Returns additional focusable children which should be tracked in addition to the main tracked {@link JComponent}.
     * Note that {@link #uniteWithChildren} setting also affects how these children focus is checked.
     *
     * @return custom children which should be tracked together with this component
     */
    public List<Component> getFocusableChildren ()
    {
        return new ImmutableList<Component> ( focusableChildren );
    }

    /**
     * Adds new focusable child {@link Component}.
     * Note that {@link #uniteWithChildren} setting also affects how these children focus is checked.
     *
     * @param child focusable child {@link Component} to add
     */
    public void addFocusableChild ( final Component child )
    {
        if ( focusableChildren == null )
        {
            focusableChildren = new WeakHashSet<Component> ( 1 );
        }
        focusableChildren.add ( child );
    }

    /**
     * Removes focusable child {@link Component}.
     * Note that {@link #uniteWithChildren} setting also affects how these children focus is checked.
     *
     * @param child focusable child {@link Component} to remove
     */
    public void removeFocusableChild ( final Component child )
    {
        if ( focusableChildren != null )
        {
            focusableChildren.remove ( child );
        }
    }

    @Override
    public boolean isInvolved ( final JComponent tracked, final Component component )
    {
        // Focus left application
        if ( component == null )
        {
            return false;
        }

        // Checking component directly
        if ( isChildInvolved ( tracked, component ) )
        {
            return true;
        }

        // Checking registered children
        if ( focusableChildren != null )
        {
            for ( final Component child : focusableChildren )
            {
                if ( isChildInvolved ( child, component ) )
                {
                    return true;
                }
            }
        }

        // None involved
        return false;
    }

    /**
     * Returns whether specified {@link Component} is involved with this tracked {@link Component} or not.
     *
     * @param tracked   tracked {@link Component}
     * @param component {@link Component} to check for involvement
     * @return {@code true} if specified {@link Component} is involved with this tracked {@link Component}, {@code false} otherwise
     */
    protected boolean isChildInvolved ( final Component tracked, final Component component )
    {
        final boolean involved;
        if ( isUniteWithChildren () )
        {
            if ( SwingUtils.isEqualOrChild ( tracked, component ) )
            {
                // Component or one of its children involved
                involved = true;
            }
            else if ( tracked instanceof JRootPane )
            {
                // JRootPane's window or one of its children involved
                // Special workaround to include window components into focus checks
                // This works exclusively for JRootPane components as it basically represents window
                final Window window = SwingUtils.getWindowAncestor ( tracked );
                involved = window != null && SwingUtils.isEqualOrChild ( window, component );
            }
            else
            {
                // None involved
                involved = false;
            }
        }
        else
        {
            if ( tracked == component )
            {
                // Component involved directly
                involved = true;
            }
            else if ( tracked instanceof JRootPane )
            {
                // JRootPane's window involved directly
                // Special workaround to include window components into focus checks
                // This works exclusively for JRootPane components as it basically represents window
                final Window window = SwingUtils.getWindowAncestor ( tracked );
                involved = window != null && window == component;
            }
            else
            {
                // None involved
                involved = false;
            }
        }
        return involved;
    }
}