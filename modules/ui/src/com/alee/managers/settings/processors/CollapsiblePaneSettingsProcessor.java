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

package com.alee.managers.settings.processors;

import com.alee.extended.panel.CollapsiblePaneListener;
import com.alee.extended.panel.WebCollapsiblePane;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;

/**
 * Custom SettingsProcessor for {@link com.alee.extended.panel.WebCollapsiblePane} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class CollapsiblePaneSettingsProcessor extends SettingsProcessor<WebCollapsiblePane, Boolean> implements CollapsiblePaneListener
{
    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public CollapsiblePaneSettingsProcessor ( final SettingsProcessorData data )
    {
        super ( data );
    }

    @Override
    public Boolean getDefaultValue ()
    {
        Boolean defaultValue = super.getDefaultValue ();
        if ( defaultValue == null )
        {
            defaultValue = true;
        }
        return defaultValue;
    }

    @Override
    protected void doInit ( final WebCollapsiblePane collapsiblePane )
    {
        collapsiblePane.addCollapsiblePaneListener ( this );
    }

    @Override
    protected void doDestroy ( final WebCollapsiblePane collapsiblePane )
    {
        collapsiblePane.removeCollapsiblePaneListener ( this );
    }

    @Override
    public void expanding ( final WebCollapsiblePane pane )
    {
        save ();
    }

    @Override
    public void expanded ( final WebCollapsiblePane pane )
    {
        // This even is irrelevant
    }

    @Override
    public void collapsing ( final WebCollapsiblePane pane )
    {
        save ();
    }

    @Override
    public void collapsed ( final WebCollapsiblePane pane )
    {
        // This even is irrelevant
    }

    @Override
    protected void doLoad ( final WebCollapsiblePane collapsiblePane )
    {
        collapsiblePane.setExpanded ( loadValue () );
    }

    @Override
    protected void doSave ( final WebCollapsiblePane collapsiblePane )
    {
        saveValue ( collapsiblePane.isExpanded () );
    }
}