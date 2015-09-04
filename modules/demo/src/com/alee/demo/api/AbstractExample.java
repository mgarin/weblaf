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

package com.alee.demo.api;

import com.alee.demo.skin.DemoStyles;
import com.alee.extended.syntax.SyntaxPreset;
import com.alee.extended.syntax.WebSyntaxArea;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.FileUtils;
import com.alee.utils.NetUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.reflection.JarEntry;
import com.alee.utils.xml.ResourceFile;

import javax.swing.*;
import java.io.File;

/**
 * @author Mikle Garin
 */

public abstract class AbstractExample extends AbstractExampleElement implements Example
{
    private static final String commentStart = "/*";
    private static final String commentEnd = "*/\n\n";

    @Override
    public Icon getIcon ()
    {
        return new ImageIcon ( getClass ().getResource ( "icons/" + getId () + ".png" ) );
    }

    @Override
    public String getTitle ()
    {
        return "demo." + getGroupId () + "." + getId () + ".title";
    }

    @Override
    public String getDescription ()
    {
        return "demo." + getGroupId () + "." + getId () + ".description";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.stable;
    }

    @Override
    public String getStyleCode ()
    {
        final ResourceFile styleFile = getStyleFile ();
        switch ( styleFile.getLocation () )
        {
            case nearClass:
            {
                final Class<Object> nearClass = ReflectUtils.getClassSafely ( styleFile.getClassName () );
                return FileUtils.readToString ( nearClass, styleFile.getSource () );
            }
            case filePath:
            {
                return FileUtils.readToString ( new File ( styleFile.getSource () ) );
            }
            case url:
            {
                return FileUtils.readToString ( NetUtils.getURL ( styleFile.getSource () ) );
            }
            default:
            {
                return "";
            }
        }
    }

    /**
     * Returns style file representing styles for this example.
     * Styling system doesn't really force you to create separate files, but default style has them for convenience.
     * Demo application uses that fact to show separate examples for each specific component.
     *
     * @return style file representing styles for this example
     */
    protected abstract ResourceFile getStyleFile ();

    @Override
    public String getSourceCode ()
    {
        // Reading source code
        final JarEntry entry = ExamplesManager.getClassEntry ( getClass () );
        String source = entry != null ? FileUtils.readToString ( entry.getInputStream () ) : "";

        // Removing space-eating license notice
        if ( source.startsWith ( commentStart ) )
        {
            final int index = source.indexOf ( commentEnd );
            if ( index != -1 )
            {
                source = source.substring ( index + commentEnd.length () );
            }
        }

        return source;
    }

    @Override
    public JComponent createContent ()
    {
        // Preview area
        final JComponent preview = createPreviewArea ();

        // Settings area
        final JComponent settings = createSettingsArea ();

        // Main split
        final WebSplitPane split = new WebSplitPane ( WebSplitPane.HORIZONTAL_SPLIT, preview, settings );
        split.setContinuousLayout ( true );
        split.setDividerLocation ( 0.5f );
        split.setResizeWeight ( 0.5f );
        return split;
    }

    protected JComponent createPreviewArea ()
    {
        return new WebPanel ();
    }

    protected JComponent createSettingsArea ()
    {
        final WebTabbedPane settings = new WebTabbedPane ( DemoStyles.stretchedTabbedPane );
        settings.addTab ( "demo.content.settings", createSettings () );
        settings.addTab ( "demo.content.style", createStyle () );
        settings.addTab ( "demo.content.source", createSource () );
        return settings;
    }

    protected JComponent createSource ()
    {
        return createSourceViewer ( getSourceCode (), SyntaxPreset.java, SyntaxPreset.viewable );
    }

    protected JComponent createStyle ()
    {
        return createSourceViewer ( getStyleCode (), SyntaxPreset.xml );
    }

    protected JComponent createSourceViewer ( final String source, final SyntaxPreset... presets )
    {
        final WebSyntaxArea sourceViewer = new WebSyntaxArea ( source, presets );
        sourceViewer.applyPresets ( SyntaxPreset.base );
        sourceViewer.applyPresets ( SyntaxPreset.margin );
        sourceViewer.applyPresets ( SyntaxPreset.size );
        sourceViewer.applyPresets ( SyntaxPreset.historyLimit );
        sourceViewer.setCaretPosition ( 0 );
        return sourceViewer.createScroll ( StyleId.syntaxareaScrollUndecorated );
    }

    protected JComponent createSettings ()
    {
        return new WebPanel ();
    }
}