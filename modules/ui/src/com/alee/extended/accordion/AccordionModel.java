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

import java.io.Serializable;
import java.util.List;

/**
 * Model for {@link WebAccordion} that handles expansion states of {@link AccordionPane}s.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see WebAccordion
 * @see AccordionPane
 * @see WebAccordionModel
 */
public interface AccordionModel extends Serializable
{
    /**
     * Installs this {@link AccordionModel} into specified {@link WebAccordion}.
     *
     * @param accordion {@link WebAccordion} this model should be installed into
     */
    public void install ( @NotNull WebAccordion accordion );

    /**
     * Uninstalls this {@link AccordionModel} from the specified {@link WebAccordion}.
     *
     * @param accordion {@link WebAccordion} this model should be uninstalled from
     */
    public void uninstall ( @NotNull WebAccordion accordion );

    /**
     * Returns {@link AccordionState}.
     * Note that returned states will only contain states for panes that are actually available in {@link WebAccordion} at the time.
     * All other states will be trimmed to avoid piling up unnecessary junk in the saved state.
     * The only exception is when model is not yet installed anywhere, in that case all available states will be returned.
     *
     * @return {@link AccordionState}
     */
    @NotNull
    public AccordionState getAccordionState ();

    /**
     * Sets {@link AccordionState}.
     * Note that provided states will be set "as is" to avoid trimming state for any lazy-loaded panes.
     *
     * @param state {@link AccordionState}
     */
    public void setAccordionState ( @NotNull AccordionState state );

    /**
     * Returns first expanded {@link AccordionPane}.
     *
     * @return first expanded {@link AccordionPane}
     */
    @Nullable
    public AccordionPane getFirstExpandedPane ();

    /**
     * Returns last expanded {@link AccordionPane}.
     *
     * @return last expanded {@link AccordionPane}
     */
    @Nullable
    public AccordionPane getLastExpandedPane ();

    /**
     * Returns {@link List} of expanded {@link AccordionPane}s.
     *
     * @return {@link List} of expanded {@link AccordionPane}s
     */
    @NotNull
    public List<AccordionPane> getExpandedPanes ();

    /**
     * Returns {@link List} of identifiers of expanded {@link AccordionPane}s.
     *
     * @return {@link List} of identifiers of expanded {@link AccordionPane}s
     */
    @NotNull
    public List<String> getExpandedPaneIds ();

    /**
     * Changes state of the {@link AccordionPane}s with identifiers from the specified {@link List} to expanded.
     *
     * @param ids {@link List} of identifiers of {@link AccordionPane}s to set expanded
     */
    public void setExpandedPaneIds ( @NotNull List<String> ids );

    /**
     * Returns array of indices of expanded {@link AccordionPane}s.
     *
     * @return array of indices of expanded {@link AccordionPane}s
     */
    @NotNull
    public int[] getExpandedPaneIndices ();

    /**
     * Changes state of the {@link AccordionPane}s at the specified indices to expanded.
     *
     * @param indices indices of {@link AccordionPane}s to set expanded
     */
    public void setExpandedPaneIndices ( @NotNull int[] indices );

    /**
     * Returns whether or not {@link AccordionPane} with the specified identifier is expanded.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} with the specified identifier is expanded, {@code false} otherwise
     */
    public boolean isPaneExpanded ( @NotNull String id );

    /**
     * Asks model to expand {@link AccordionPane} with the specified identifier.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} was successfully expanded, {@code false} otherwise
     */
    public boolean expandPane ( @NotNull String id );

    /**
     * Returns {@link List} of collapsed {@link AccordionPane}s.
     *
     * @return {@link List} of collapsed {@link AccordionPane}s
     */
    @NotNull
    public List<AccordionPane> getCollapsedPanes ();

    /**
     * Returns {@link List} of identifiers of collapsed {@link AccordionPane}s.
     *
     * @return {@link List} of identifiers of collapsed {@link AccordionPane}s
     */
    @NotNull
    public List<String> getCollapsedPaneIds ();

    /**
     * Returns array of indices of collapsed {@link AccordionPane}s.
     *
     * @return array of indices of collapsed {@link AccordionPane}s
     */
    @NotNull
    public int[] getCollapsedPaneIndices ();

    /**
     * Returns whether or not {@link AccordionPane} with the specified identifier is collapsed.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} with the specified identifier is collapsed, {@code false} otherwise
     */
    public boolean isPaneCollapsed ( @NotNull String id );

    /**
     * Asks model to collapse {@link AccordionPane} with the specified identifier.
     *
     * @param id {@link AccordionPane} identifier
     * @return {@code true} if {@link AccordionPane} was successfully collapsed, {@code false} otherwise
     */
    public boolean collapsePane ( @NotNull String id );
}