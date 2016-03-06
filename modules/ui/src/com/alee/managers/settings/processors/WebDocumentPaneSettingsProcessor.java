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

import com.alee.extended.tab.*;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;

/**
 * Custom SettingsProcessor for WebDocumentPane component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class WebDocumentPaneSettingsProcessor extends SettingsProcessor<WebDocumentPane, DocumentPaneState>
        implements DocumentListener, DocumentPaneListener
{
    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public WebDocumentPaneSettingsProcessor ( final SettingsProcessorData data )
    {
        super ( data );
    }

    @Override
    protected void doInit ( final WebDocumentPane component )
    {
        component.addDocumentListener ( this );
        component.addDocumentPaneListener ( this );
    }

    @Override
    protected void doDestroy ( final WebDocumentPane component )
    {
        component.removeDocumentPaneListener ( this );
        component.removeDocumentListener ( this );
    }

    @Override
    public void opened ( final DocumentData document, final PaneData pane, final int index )
    {
        save ();
    }

    @Override
    public void selected ( final DocumentData document, final PaneData pane, final int index )
    {
        save ();
    }

    @Override
    public boolean closing ( final DocumentData document, final PaneData pane, final int index )
    {
        return true;
    }

    @Override
    public void closed ( final DocumentData document, final PaneData pane, final int index )
    {
        save ();
    }

    @Override
    public void splitted ( final WebDocumentPane documentPane, final PaneData splittedPane, final SplitData newSplitData )
    {
        save ();
    }

    @Override
    public void merged ( final WebDocumentPane documentPane, final SplitData mergedSplit, final StructureData newStructureData )
    {
        save ();
    }

    @Override
    public void orientationChanged ( final WebDocumentPane documentPane, final SplitData splitData )
    {
        save ();
    }

    @Override
    public void sidesSwapped ( final WebDocumentPane documentPane, final SplitData splitData )
    {
        save ();
    }

    @Override
    public void dividerLocationChanged ( final WebDocumentPane documentPane, final SplitData splitData )
    {
        save ();
    }

    @Override
    protected void doLoad ( final WebDocumentPane component )
    {
        final DocumentPaneState state = loadValue ();
        if ( state != null )
        {
            component.setDocumentPaneState ( state );
        }
    }

    @Override
    protected void doSave ( final WebDocumentPane component )
    {
        saveValue ( component.getDocumentPaneState () );
    }
}