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

package com.alee.extended.split;

import com.alee.api.annotations.NotNull;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

import java.awt.*;

/**
 * {@link SettingsProcessor} implementation that handles {@link WebMultiSplitPane} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
public class MultiSplitPaneSettingsProcessor extends SettingsProcessor<WebMultiSplitPane, MultiSplitState, Configuration<MultiSplitState>>
{
    /**
     * {@link MultiSplitResizeListener} for tracking {@link WebMultiSplitPane} size changes.
     */
    protected transient MultiSplitResizeAdapter multiSplitResizeListener;

    /**
     * {@link MultiSplitExpansionListener} for tracking {@link WebMultiSplitPane} parts expansion.
     */
    protected transient MultiSplitExpansionAdapter multiSplitExpansionListener;

    /**
     * Constructs new {@link MultiSplitPaneSettingsProcessor}.
     *
     * @param multiSplitPane {@link WebMultiSplitPane} which settings are being managed
     * @param configuration  {@link Configuration}
     */
    public MultiSplitPaneSettingsProcessor ( final WebMultiSplitPane multiSplitPane, final Configuration configuration )
    {
        super ( multiSplitPane, configuration );
    }

    @Override
    protected void register ( final WebMultiSplitPane multiSplitPane )
    {
        multiSplitResizeListener = new MultiSplitResizeAdapter ()
        {
            @Override
            public void viewResizeEnded ( @NotNull final WebMultiSplitPane multiSplitPane, @NotNull final WebMultiSplitPaneDivider divider )
            {
                save ();
            }

            @Override
            public void viewSizeAdjusted ( @NotNull final WebMultiSplitPane multiSplitPane )
            {
                save ();
            }
        };
        multiSplitPane.addResizeListener ( multiSplitResizeListener );

        multiSplitExpansionListener = new MultiSplitExpansionAdapter ()
        {
            @Override
            public void viewExpanded ( @NotNull final WebMultiSplitPane multiSplitPane, @NotNull final Component view )
            {
                save ();
            }

            @Override
            public void viewCollapsed ( @NotNull final WebMultiSplitPane multiSplitPane, @NotNull final Component view )
            {
                save ();
            }
        };
        multiSplitPane.addExpansionListener ( multiSplitExpansionListener );
    }

    @Override
    protected void unregister ( final WebMultiSplitPane multiSplitPane )
    {
        multiSplitPane.removeExpansionListener ( multiSplitExpansionListener );
        multiSplitExpansionListener = null;

        multiSplitPane.removeResizeListener ( multiSplitResizeListener );
        multiSplitResizeListener = null;
    }

    @Override
    protected void loadSettings ( final WebMultiSplitPane multiSplitPane )
    {
        final MultiSplitState state = loadSettings ();
        if ( state != null )
        {
            multiSplitPane.setMultiSplitState ( state );
        }
    }

    @Override
    protected void saveSettings ( final WebMultiSplitPane multiSplitPane )
    {
        final MultiSplitState state = multiSplitPane.getMultiSplitState ();
        if ( state != null )
        {
            saveSettings ( state );
        }
    }
}