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

import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Small extension class for {@link com.alee.managers.focus.FocusTracker} that simplifies its creation.
 *
 * @author Mikle Garin
 */

public abstract class DefaultFocusTracker implements FocusTracker
{
    /**
     * Whether tracking is currently enabled or not.
     */
    private boolean enabled;

    /**
     * Whether component and its children in components tree should be counted as a single component or not.
     */
    private boolean uniteWithChildren;

    /**
     * Custom children which should be tracked together with this component.
     */
    private List<WeakReference<Component>> customChildren;

    /**
     * Constructs new tracker with the specified tracked component.
     */
    public DefaultFocusTracker ()
    {
        this ( true );
    }

    /**
     * Constructs new tracker with the specified tracked component.
     *
     * @param uniteWithChildren whether component and its children in components tree should be counted as a single component or not
     */
    public DefaultFocusTracker ( final boolean uniteWithChildren )
    {
        super ();
        this.enabled = true;
        this.uniteWithChildren = uniteWithChildren;
    }

    @Override
    public boolean isTrackingEnabled ()
    {
        return enabled;
    }

    /**
     * Sets whether tracking is currently enabled or not.
     *
     * @param enabled whether tracking is currently enabled or not
     */
    public void setTrackingEnabled ( final boolean enabled )
    {
        this.enabled = enabled;
    }

    @Override
    public boolean isInvolved ( final Component component, final Component tracked )
    {
        if ( isUniteWithChildren () )
        {
            if ( SwingUtils.isEqualOrChild ( tracked, component ) )
            {
                return true;
            }
            if ( customChildren != null )
            {
                final Iterator<WeakReference<Component>> iterator = customChildren.iterator ();
                while ( iterator.hasNext () )
                {
                    final WeakReference<Component> next = iterator.next ();
                    final Component customChild = next.get ();
                    if ( customChild == null )
                    {
                        iterator.remove ();
                    }
                    else
                    {
                        if ( SwingUtils.isEqualOrChild ( customChild, component ) )
                        {
                            return true;
                        }
                    }
                }
            }
        }
        else
        {
            if ( tracked == component )
            {
                return true;
            }
            if ( customChildren != null )
            {
                final Iterator<WeakReference<Component>> iterator = customChildren.iterator ();
                while ( iterator.hasNext () )
                {
                    final WeakReference<Component> next = iterator.next ();
                    final Component customChild = next.get ();
                    if ( customChild == null )
                    {
                        iterator.remove ();
                    }
                    else
                    {
                        if ( customChild == component )
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns whether component and its children in components tree should be counted as a single component or not.
     * In case component and its children are counted as one focus changes within them will be ignored by tracker.
     *
     * @return true if component and its children in components tree should be counted as a single component, false otherwise
     */
    public boolean isUniteWithChildren ()
    {
        return uniteWithChildren;
    }

    /**
     * Sets whether component and its children in components tree should be counted as a single component or not.
     *
     * @param uniteWithChildren whether component and its children in components tree should be counted as a single component or not
     */
    public void setUniteWithChildren ( final boolean uniteWithChildren )
    {
        this.uniteWithChildren = uniteWithChildren;
    }

    /**
     * Returns custom children which should be tracked together with this component.
     * Note that `isUniteWithChildren` value will also affect how these children focus is checked.
     *
     * @return custom children which should be tracked together with this component
     */
    public List<Component> getCustomChildren ()
    {
        final List<Component> children = new ArrayList<Component> ( customChildren.size () );
        final Iterator<WeakReference<Component>> iterator = customChildren.iterator ();
        while ( iterator.hasNext () )
        {
            final WeakReference<Component> next = iterator.next ();
            final Component component = next.get ();
            if ( component == null )
            {
                iterator.remove ();
            }
            else
            {
                children.add ( component );
            }
        }
        return children;
    }

    /**
     * Returns weakly-referenced custom children.
     *
     * @return weakly-referenced custom children
     */
    public List<WeakReference<Component>> getWeakCustomChildren ()
    {
        return customChildren;
    }

    /**
     * Sets custom children which should be tracked together with this component.
     *
     * @param customChildren custom children which should be tracked together with this component
     */
    public void setCustomChildren ( final List<Component> customChildren )
    {
        for ( final Component customChild : customChildren )
        {
            addCustomChild ( customChild );
        }
    }

    /**
     * Adds new custom child.
     *
     * @param customChild custom child to add
     */
    public void addCustomChild ( final Component customChild )
    {
        if ( customChildren == null )
        {
            customChildren = new ArrayList<WeakReference<Component>> ( 1 );
        }
        customChildren.add ( new WeakReference<Component> ( customChild ) );
    }

    /**
     * Removes custom child.
     *
     * @param customChild custom child to remove
     */
    public void removeCustomChild ( final Component customChild )
    {
        if ( customChildren != null )
        {
            final Iterator<WeakReference<Component>> iterator = customChildren.iterator ();
            while ( iterator.hasNext () )
            {
                final WeakReference<Component> next = iterator.next ();
                final Component component = next.get ();
                if ( component == null || component == customChild )
                {
                    iterator.remove ();
                }
            }
        }
    }
}