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
import com.alee.api.jdk.Objects;
import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.*;

/**
 * {@link Serializable} state object for {@link WebAccordion}.
 * It can be used to save and restore {@link AccordionPane}s state in runtime.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see WebAccordion
 * @see AccordionPane
 * @see AccordionModel
 * @see WebAccordionModel
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see AccordionSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "AccordionState" )
public class AccordionState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link List} of {@link AccordionPaneState}.
     */
    @Nullable
    @XStreamImplicit
    protected List<AccordionPaneState> states;

    /**
     * Constructs new {@link AccordionState}.
     */
    public AccordionState ()
    {
        this.states = new ArrayList<AccordionPaneState> ();
    }

    /**
     * Constructs new {@link AccordionState}.
     *
     * @param states {@link AccordionPaneState}s
     */
    public AccordionState ( @NotNull final AccordionPaneState... states )
    {
        this.states = new ArrayList<AccordionPaneState> ( states.length );
        for ( final AccordionPaneState state : states )
        {
            addState ( state );
        }
    }

    /**
     * Constructs new {@link AccordionState}.
     *
     * @param states {@link Map} of {@link AccordionPaneState}s
     */
    public AccordionState ( @NotNull final Map<String, AccordionPaneState> states )
    {
        this.states = new ArrayList<AccordionPaneState> ( states.values () );
    }

    /**
     * Returns {@link Map} of {@link AccordionPaneState}s.
     *
     * @return {@link Map} of {@link AccordionPaneState}s
     */
    @NotNull
    public Map<String, AccordionPaneState> states ()
    {
        final Map<String, AccordionPaneState> states = new HashMap<String, AccordionPaneState> ();
        if ( this.states != null )
        {
            for ( final AccordionPaneState state : this.states )
            {
                states.put ( state.getId (), state );
            }
        }
        return states;
    }

    /**
     * Adds specified {@link AccordionPaneState}.
     *
     * @param state {@link AccordionPaneState} to add
     */
    public void addState ( @NotNull final AccordionPaneState state )
    {
        if ( this.states == null )
        {
            this.states = new ArrayList<AccordionPaneState> ();
        }
        final Iterator<AccordionPaneState> iterator = this.states.iterator ();
        while ( iterator.hasNext () )
        {
            if ( Objects.equals ( iterator.next ().getId (), state.getId () ) )
            {
                iterator.remove ();
            }
        }
        this.states.add ( state );
    }

    /**
     * Sets {@link Map} of {@link AccordionPaneState}s.
     *
     * @param states {@link Map} of {@link AccordionPaneState}s
     */
    public void setStates ( @NotNull final Map<String, AccordionPaneState> states )
    {
        this.states = new ArrayList<AccordionPaneState> ( states.values () );
    }
}