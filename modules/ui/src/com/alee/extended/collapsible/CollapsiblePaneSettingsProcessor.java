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

package com.alee.extended.collapsible;

import com.alee.api.annotations.NotNull;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

/**
 * {@link SettingsProcessor} implementation that handles {@link WebCollapsiblePane} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebCollapsiblePane">How to use WebCollapsiblePane</a>
 * @see WebCollapsiblePane
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class CollapsiblePaneSettingsProcessor extends SettingsProcessor<WebCollapsiblePane, CollapsiblePaneState,
        Configuration<CollapsiblePaneState>>
{
    /**
     * {@link CollapsiblePaneAdapter} for tracking {@link WebCollapsiblePane} expansion state.
     */
    protected transient CollapsiblePaneAdapter collapsiblePaneListener;

    /**
     * Constructs new {@link CollapsiblePaneSettingsProcessor}.
     *
     * @param collapsiblePane {@link WebCollapsiblePane} which settings are being managed
     * @param configuration   {@link Configuration}
     */
    public CollapsiblePaneSettingsProcessor ( final WebCollapsiblePane collapsiblePane, final Configuration configuration )
    {
        super ( collapsiblePane, configuration );
    }

    @Override
    protected void register ( final WebCollapsiblePane collapsiblePane )
    {
        collapsiblePaneListener = new CollapsiblePaneAdapter ()
        {
            @Override
            public void expanding ( @NotNull final WebCollapsiblePane pane )
            {
                save ();
            }

            @Override
            public void collapsing ( @NotNull final WebCollapsiblePane pane )
            {
                save ();
            }
        };
        collapsiblePane.addCollapsiblePaneListener ( collapsiblePaneListener );
    }

    @Override
    protected void unregister ( final WebCollapsiblePane collapsiblePane )
    {
        collapsiblePane.removeCollapsiblePaneListener ( collapsiblePaneListener );
        collapsiblePaneListener = null;
    }

    @Override
    protected CollapsiblePaneState createDefaultValue ()
    {
        return new CollapsiblePaneState ( component () );
    }

    @Override
    protected void loadSettings ( final WebCollapsiblePane collapsiblePane )
    {
        loadSettings ().apply ( collapsiblePane );
    }

    @Override
    protected void saveSettings ( final WebCollapsiblePane collapsiblePane )
    {
        saveSettings ( new CollapsiblePaneState ( collapsiblePane ) );
    }
}