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

package com.alee.extended.behavior;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom {@link Behavior} that allows you to track {@link Component} parent.
 * You need to specify {@link Component} for which parent will be tracked.
 * Use {@link #install()} and {@link #uninstall()} methods to setup and remove this behavior.
 *
 * @param <C> {@link Component} type
 * @author Mikle Garin
 */
public abstract class ParentChangeBehavior<C extends JComponent> extends AbstractComponentBehavior<C> implements AncestorListener
{
    /**
     * Whether or not should artificially trigger events on {@link #install()} and {@link #uninstall()}.
     * If set to {@code true} - {@link #parentChanged(Container, Container)} event will be triggered.
     */
    private final boolean initTriggers;

    /**
     * Whether or not only direct {@link #component} parent change should be tracked.
     * If set to {@code true} - only direct parent change will trigger {@link #parentChanged(Container, Container)} event.
     * If set to {@code false} - parent change on any level will trigger {@link #parentChanged(Container, Container)} event.
     */
    private final boolean directParentOnly;

    /**
     * {@link List} of current {@link #component} parents.
     * Starts from direct parent at {@code 0} index and ends with topmost parent at highest index.
     * This {@link List} can also be empty if component doesn't currently have any parents.
     */
    private List<Container> parents;

    /**
     * Constructs new {@link ParentChangeBehavior}.
     *
     * @param component        {@link Component} into which this behavior is installed
     * @param initTriggers     whether or not should artificially trigger events on {@link #install()} and {@link #uninstall()}
     * @param directParentOnly whether or not only direct {@link Component} parent or it's whole tree should be tracked
     */
    public ParentChangeBehavior ( @NotNull final C component, final boolean initTriggers, final boolean directParentOnly )
    {
        super ( component );
        this.initTriggers = initTriggers;
        this.directParentOnly = directParentOnly;
    }

    /**
     * Returns {@link List} of {@link Container}s parent to the {@link Component} this behavior is installed into.
     *
     * @return {@link List} of {@link Container}s parent to the {@link Component} this behavior is installed into
     */
    @NotNull
    protected List<Container> getParents ()
    {
        final List<Container> parents;
        if ( directParentOnly )
        {
            parents = CollectionUtils.asList ( component.getParent () );
        }
        else
        {
            Container parent = component.getParent ();
            if ( parent != null )
            {
                parents = new ArrayList<Container> ( 5 );
                while ( parent != null )
                {
                    parents.add ( parent );
                    parent = parent.getParent ();
                }
            }
            else
            {
                parents = CollectionUtils.asList ( ( Container ) null );
            }
        }
        return parents;
    }

    /**
     * Installs behavior into {@link Component}.
     */
    public void install ()
    {
        parents = getParents ();
        component.addAncestorListener ( this );
        if ( initTriggers )
        {
            parentChanged ( null, parents.get ( 0 ) );
        }
    }

    /**
     * Uninstalls behavior from the {@link Component}.
     */
    public void uninstall ()
    {
        if ( initTriggers )
        {
            parentChanged ( parents.get ( 0 ), null );
        }
        component.removeAncestorListener ( this );
        parents = null;
    }

    @Override
    public void ancestorAdded ( @NotNull final AncestorEvent event )
    {
        ancestorChanged ( event );
    }

    @Override
    public void ancestorRemoved ( @NotNull final AncestorEvent event )
    {
        ancestorChanged ( event );
    }

    @Override
    public void ancestorMoved ( @NotNull final AncestorEvent event )
    {
        ancestorChanged ( event );
    }

    /**
     * Informs about {@link AncestorEvent} that has occurred.
     *
     * @param event {@link AncestorEvent}
     */
    protected void ancestorChanged ( @NotNull final AncestorEvent event )
    {
        if ( directParentOnly )
        {
            final List<Container> newParents = getParents ();
            if ( newParents.get ( 0 ) != parents.get ( 0 ) )
            {
                final List<Container> oldParents = parents;
                parents = newParents;
                parentChanged ( oldParents.get ( 0 ), newParents.get ( 0 ) );
            }
        }
        else
        {
            final List<Container> newParents = getParents ();
            if ( !CollectionUtils.equals ( newParents, parents ) )
            {
                final List<Container> oldParents = parents;
                parents = newParents;
                parentChanged ( oldParents.get ( 0 ), newParents.get ( 0 ) );
            }
        }
    }

    /**
     * Informs about parent change.
     * This method only passes direct component parents which means in case {@link #directParentOnly} is set to {@code false} it might pass
     * two identical parent {@link Container}s due to higher level parent change.
     *
     * @param oldParent old parent {@link Container}
     * @param newParent new parent {@link Container}
     */
    public abstract void parentChanged ( @Nullable Container oldParent, @Nullable Container newParent );
}