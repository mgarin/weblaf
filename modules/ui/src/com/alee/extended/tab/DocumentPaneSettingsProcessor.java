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

package com.alee.extended.tab;

import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

/**
 * {@link SettingsProcessor} implementation that handles {@link WebDocumentPane} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class DocumentPaneSettingsProcessor extends SettingsProcessor<WebDocumentPane, DocumentPaneState, Configuration<DocumentPaneState>>
{
    /**
     * {@link DocumentListener} for tracking {@link WebDocumentPane} documents.
     */
    protected transient DocumentListener documentListener;

    /**
     * {@link DocumentPaneListener} for tracking {@link WebDocumentPane} documents positioning.
     */
    protected transient DocumentPaneListener documentPaneListener;

    /**
     * Constructs new {@link DocumentPaneSettingsProcessor}.
     *
     * @param documentPane  {@link WebDocumentPane} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public DocumentPaneSettingsProcessor ( final WebDocumentPane documentPane, final Configuration configuration )
    {
        super ( documentPane, configuration );
    }

    @Override
    protected void register ( final WebDocumentPane documentPane )
    {
        documentListener = new DocumentListener ()
        {
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
                /**
                 * We only save settings after actual close event.
                 */
                return true;
            }

            @Override
            public void closed ( final DocumentData document, final PaneData pane, final int index )
            {
                save ();
            }
        };
        documentPane.addDocumentListener ( documentListener );

        documentPaneListener = new DocumentPaneListener ()
        {
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
        };
        documentPane.addDocumentPaneListener ( documentPaneListener );
    }

    @Override
    protected void unregister ( final WebDocumentPane documentPane )
    {
        documentPane.removeDocumentPaneListener ( documentPaneListener );
        documentPaneListener = null;

        documentPane.removeDocumentListener ( documentListener );
        documentListener = null;
    }

    @Override
    protected void loadSettings ( final WebDocumentPane documentPane )
    {
        final DocumentPaneState state = loadSettings ();
        if ( state != null )
        {
            documentPane.setDocumentPaneState ( state );
        }
    }

    @Override
    protected void saveSettings ( final WebDocumentPane documentPane )
    {
        saveSettings ( documentPane.getDocumentPaneState () );
    }
}