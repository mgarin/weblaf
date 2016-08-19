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

package com.alee.demo.api.example;

import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.wiki.NoWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.content.ExamplesManager;
import com.alee.demo.skin.DemoStyles;
import com.alee.demo.util.ExampleUtils;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.log.Log;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleId;
import com.alee.utils.*;
import com.alee.utils.reflection.JarEntry;
import com.alee.utils.xml.Resource;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikle Garin
 */

public abstract class AbstractExample extends AbstractExampleElement implements Example
{
    /**
     * License text trimming constants.
     */
    protected static final String commentStart = "/*";
    protected static final String commentEnd = "*/\n\n";

    /**
     * Previews cache.
     */
    protected List<Preview> previews;

    /**
     * Preview pane.
     */
    protected WebPanel examplesPane;

    /**
     * Skin currently selected for example previews.
     */
    protected Skin selectedSkin = DemoApplication.skins.get ( 0 );

    @Override
    public Icon getIcon ()
    {
        return loadIcon ( getId () + ".png" );
    }

    @Override
    public FeatureState getFeatureState ()
    {
        final List<Preview> previews = getPreviews ();
        final List<FeatureState> states = new ArrayList<FeatureState> ( previews.size () );
        for ( final Preview preview : previews )
        {
            states.add ( preview.getFeatureState () );
        }
        return ExampleUtils.getResultingState ( states );
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new NoWikiPage ();
    }

    @Override
    public String getStyleCode ( final Skin skin )
    {
        final Resource styleFile = getStyleFile ( skin );
        switch ( styleFile.getLocation () )
        {
            case nearClass:
            {
                final Class<Object> nearClass = ReflectUtils.getClassSafely ( styleFile.getClassName () );
                return FileUtils.readToString ( nearClass, styleFile.getPath () );
            }
            case filePath:
            {
                return FileUtils.readToString ( new File ( styleFile.getPath () ) );
            }
            case url:
            {
                return FileUtils.readToString ( NetUtils.getURL ( styleFile.getPath () ) );
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
     * @param skin skin to retrieve style file for
     * @return style file representing styles for this example
     */
    protected Resource getStyleFile ( final Skin skin )
    {
        final String path = "resources/" + getStyleFileName () + ".xml";
        final Resource resource = new Resource ( skin.getClass (), path );
        if ( skin.getClass ().getResource ( path ) == null )
        {
            Log.get ().warn ( "Unable to find style resource: " + path + " for skin: " + skin );
        }
        return resource;
    }

    /**
     * Returns example style file name.
     *
     * @return example style file name
     */
    protected String getStyleFileName ()
    {
        return getId ();
    }

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
        final JComponent content = getPreviewContent ();
        final JComponent toolBar = createPreviewToolBar ();
        final JComponent contentScroll = new WebScrollPane ( StyleId.scrollpaneUndecorated, content );
        return new GroupPanel ( GroupingType.fillLast, false, toolBar, contentScroll );
    }

    /**
     * Returns preview content.
     *
     * @return preview content
     */
    protected JComponent getPreviewContent ()
    {
        if ( examplesPane == null )
        {
            // Creating preview components
            examplesPane = new WebPanel ( DemoStyles.previewsPanel, new VerticalFlowLayout ( 0, -32, true, false ) );
            final List<Component> components = new ArrayList<Component> ();
            final List<Preview> previews = getPreviews ();
            for ( int i = 0; i < previews.size (); i++ )
            {
                // Preview
                final Preview preview = previews.get ( i );

                // Preview component
                final JComponent previewComponent = preview.getPreview ( previews, i );
                examplesPane.add ( previewComponent );

                // Equalizing preview elements
                CollectionUtils.addAllNonNull ( components, preview.getEqualizableWidthComponent () );
            }
            SwingUtils.equalizeComponentsWidth ( Arrays.asList ( AbstractButton.TEXT_CHANGED_PROPERTY ), components );
        }
        return examplesPane;
    }

    /**
     * Returns preview toolbar.
     *
     * @return preview toolbar
     */
    protected JComponent createPreviewToolBar ()
    {
        final WebToolBar toolbar = new WebToolBar ( DemoStyles.exampleToolbar );
        toolbar.setFloatable ( false );
        toolbar.add ( getWikiPage ().createLink () );
        return toolbar;
    }

    /**
     * Returns cached previews.
     *
     * @return cached previews
     */
    protected List<Preview> getPreviews ()
    {
        if ( previews == null )
        {
            previews = createPreviews ();
        }
        return previews;
    }

    /**
     * Returns all example previews.
     *
     * @return all example previews
     */
    protected abstract List<Preview> createPreviews ();
}