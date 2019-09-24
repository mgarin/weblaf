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

import java.util.EventListener;

/**
 * Custom {@link EventListener} for {@link AccordionPane} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see AccordionPaneAdapter
 * @see WebAccordion
 * @see AccordionPane
 * @see AccordionModel
 * @see WebAccordionModel
 */
public interface AccordionPaneListener extends EventListener
{
    /**
     * Informs about {@link AccordionPane} expansion start.
     * At this point {@link AccordionPane} is already marked as expanded.
     *
     * @param accordion {@link WebAccordion}
     * @param pane      {@link AccordionPane} that is being expanded
     */
    public void expanding ( @NotNull WebAccordion accordion, @NotNull AccordionPane pane );

    /**
     * Informs about {@link AccordionPane} expansion finish.
     *
     * @param accordion {@link WebAccordion}
     * @param pane      {@link AccordionPane} that was expanded
     */
    public void expanded ( @NotNull WebAccordion accordion, @NotNull AccordionPane pane );

    /**
     * Informs about {@link AccordionPane} collapse start.
     * At this point {@link AccordionPane} is already marked as collapsed.
     *
     * @param accordion {@link WebAccordion}
     * @param pane      {@link AccordionPane} that is being collapsed
     */
    public void collapsing ( @NotNull WebAccordion accordion, @NotNull AccordionPane pane );

    /**
     * Informs about {@link AccordionPane} collapse finish.
     *
     * @param accordion {@link WebAccordion}
     * @param pane      {@link AccordionPane} that was collapsed
     */
    public void collapsed ( @NotNull WebAccordion accordion, @NotNull AccordionPane pane );
}