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

package com.alee.extended.panel;

import com.alee.api.merge.Mergeable;
import com.alee.utils.xml.ListToStringConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;
import java.util.List;

/**
 * {@link WebAccordion} expansion state holder.
 *
 * @author Mikle Garin
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
     * Indices selected in {@link WebAccordion}.
     */
    @XStreamAsAttribute
    @XStreamConverter ( ListToStringConverter.class )
    private final List<Integer> selectedIndices;

    /**
     * Constructs default {@link AccordionState}.
     */
    public AccordionState ()
    {
        this.selectedIndices = null;
    }

    /**
     * Constructs new {@link AccordionState} with settings from {@link WebAccordion}.
     *
     * @param accordion {@link WebAccordion} to retreive state from
     */
    public AccordionState ( final WebAccordion accordion )
    {
        this.selectedIndices = accordion.getSelectedIndices ();
    }

    /**
     * Returns indices selected in {@link WebAccordion}.
     *
     * @return indices selected in {@link WebAccordion}
     */
    public List<Integer> getSelectedIndices ()
    {
        return selectedIndices;
    }

    /**
     * Applies this {@link AccordionState} to the specified {@link WebAccordion}.
     *
     * @param accordion {@link WebAccordion} to apply this {@link AccordionState} to
     */
    public void apply ( final WebAccordion accordion )
    {
        accordion.setSelectedIndices ( selectedIndices );
    }
}