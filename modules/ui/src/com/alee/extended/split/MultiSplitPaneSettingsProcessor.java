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

import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;

import java.awt.*;

/**
 * {@link SettingsProcessor} for {@link WebMultiSplitPane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class MultiSplitPaneSettingsProcessor extends SettingsProcessor<WebMultiSplitPane, MultiSplitState>
        implements MultiSplitResizeListener, MultiSplitExpansionListener
{
    /**
     * Constructs new {@link MultiSplitPaneSettingsProcessor}.
     *
     * @param data {@link SettingsProcessorData}
     */
    public MultiSplitPaneSettingsProcessor ( final SettingsProcessorData data )
    {
        super ( data );
    }

    @Override
    protected void doInit ( final WebMultiSplitPane multiSplitPane )
    {
        multiSplitPane.addResizeListener ( this );
        multiSplitPane.addExpansionListener ( this );
    }

    @Override
    protected void doDestroy ( final WebMultiSplitPane multiSplitPane )
    {
        multiSplitPane.removeExpansionListener ( this );
        multiSplitPane.removeResizeListener ( this );
    }

    @Override
    public void viewResizeStarted ( final WebMultiSplitPane multiSplitPane, final WebMultiSplitPaneDivider divider )
    {
        /**
         * Only save upon resize completion.
         */
    }

    @Override
    public void viewResized ( final WebMultiSplitPane multiSplitPane, final WebMultiSplitPaneDivider divider )
    {
        /**
         * Only save upon resize completion.
         */
    }

    @Override
    public void viewResizeEnded ( final WebMultiSplitPane multiSplitPane, final WebMultiSplitPaneDivider divider )
    {
        save ();
    }

    @Override
    public void viewSizeAdjusted ( final WebMultiSplitPane multiSplitPane )
    {
        save ();
    }

    @Override
    public void viewExpanded ( final WebMultiSplitPane multiSplitPane, final Component view )
    {
        save ();
    }

    @Override
    public void viewCollapsed ( final WebMultiSplitPane multiSplitPane, final Component view )
    {
        save ();
    }

    @Override
    protected void doLoad ( final WebMultiSplitPane multiSplitPane )
    {
        final MultiSplitState state = loadValue ();
        if ( state != null )
        {
            multiSplitPane.setMultiSplitState ( state );
        }
    }

    @Override
    protected void doSave ( final WebMultiSplitPane multiSplitPane )
    {
        final MultiSplitState state = multiSplitPane.getMultiSplitState ();
        if ( state != null )
        {
            saveValue ( state );
        }
    }
}