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

import com.alee.api.Identifiable;
import com.alee.api.annotations.Nullable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * State settings for single {@link AccordionPane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see WebAccordion
 * @see AccordionPane
 * @see AccordionModel
 * @see WebAccordionModel
 */
@XStreamAlias ( "AccordionPaneState" )
public class AccordionPaneState implements Identifiable, Cloneable, Serializable
{
    /**
     * {@link AccordionPane} identifier.
     */
    @XStreamAsAttribute
    protected String id;

    /**
     * Whether or not {@link AccordionPane} is expanded.
     * Depending on {@link WebAccordion} settings either single or multiple {@link AccordionPane}s may be expanded at the same time.
     */
    @XStreamAsAttribute
    protected boolean expanded;

    /**
     * Time in milliseconds when {@link AccordionPaneState} state was updated.
     * It might be used for deciding which {@link AccordionPane} will be auto-collapsed whenever there are too many expanded.
     */
    @XStreamAsAttribute
    protected long time;

    /**
     * Constructs new {@link AccordionPaneState}.
     */
    public AccordionPaneState ()
    {
        this.id = null;
        this.expanded = false;
        this.time = System.currentTimeMillis ();
    }

    /**
     * Constructs new {@link AccordionPaneState}.
     *
     * @param id       {@link AccordionPane} identifier
     * @param expanded whether or not {@link AccordionPane} is expanded
     */
    public AccordionPaneState ( final String id, final boolean expanded )
    {
        this.id = id;
        this.expanded = expanded;
        this.time = System.currentTimeMillis ();
    }

    @Nullable
    @Override
    public String getId ()
    {
        return id;
    }

    /**
     * Returns {@link AccordionPane} is expansion state.
     *
     * @return {@code true} if {@link AccordionPane} is expanded, {@code false} otherwise
     */
    public boolean isExpanded ()
    {
        return expanded;
    }

    /**
     * Returns time in milliseconds {@link AccordionPane} was expanded.
     *
     * @return time in milliseconds {@link AccordionPane} was expanded
     */
    public long getTime ()
    {
        return time;
    }

    /**
     * Modifies {@link AccordionPane} expansion state.
     *
     * @param expanded whether or not {@link AccordionPane} is expanded
     */
    public void setExpanded ( final boolean expanded )
    {
        this.expanded = expanded;
        this.time = System.currentTimeMillis ();
    }
}