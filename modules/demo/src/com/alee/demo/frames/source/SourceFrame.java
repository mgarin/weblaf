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

package com.alee.demo.frames.source;

import com.alee.api.data.CompassDirection;
import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.ExampleData;
import com.alee.demo.skin.DemoIcons;
import com.alee.extended.dock.DockableFrameState;
import com.alee.extended.dock.WebDockableFrame;
import com.alee.extended.syntax.SyntaxPreset;
import com.alee.extended.syntax.WebSyntaxArea;
import com.alee.extended.tab.DocumentAdapter;
import com.alee.extended.tab.PaneData;
import com.alee.managers.style.StyleId;

import java.awt.*;

/**
 * Focused example source code frame.
 *
 * @author Mikle Garin
 */
public final class SourceFrame extends WebDockableFrame
{
    /**
     * Frame ID.
     */
    public static final String ID = "demo.source";

    /**
     * Constructs examples frame.
     *
     * @param application demo application
     */
    public SourceFrame ( final DemoApplication application )
    {
        super ( StyleId.dockableframeCompact, ID, DemoIcons.source16, "demo.source.title" );
        setState ( DockableFrameState.minimized );
        setPosition ( CompassDirection.east );
        setPreferredSize ( 300, 200 );

        // Source code area
        final String emptyText = "Select an example to see its source code...";
        final WebSyntaxArea sourceViewer = new WebSyntaxArea ( emptyText, SyntaxPreset.java, SyntaxPreset.viewable );
        sourceViewer.applyPresets ( SyntaxPreset.base, SyntaxPreset.margin, SyntaxPreset.size, SyntaxPreset.historyLimit );
        add ( sourceViewer.createScroll ( StyleId.syntaxareaScrollUndecorated ), BorderLayout.CENTER );

        // Selected example listener
        application.getExamplesPane ().addDocumentListener ( new DocumentAdapter<ExampleData> ()
        {
            @Override
            public void selected ( final ExampleData document, final PaneData<ExampleData> pane, final int index )
            {
                // Retrieving source code from the example
                sourceViewer.setText ( document.getExample ().getSourceCode () );
                sourceViewer.setCaretPosition ( 0 );
            }

            @Override
            public void closed ( final ExampleData document, final PaneData<ExampleData> pane, final int index )
            {
                // Resetting source code
                if ( application.getExamplesPane ().getDocumentsCount () == 0 )
                {
                    sourceViewer.setText ( emptyText );
                    sourceViewer.setCaretPosition ( 0 );
                }
            }
        } );
    }
}