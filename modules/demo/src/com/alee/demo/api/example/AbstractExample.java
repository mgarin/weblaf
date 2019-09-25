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

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.wiki.NoWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.content.ExamplesManager;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleId;
import com.alee.utils.FileUtils;
import com.alee.utils.jar.JarEntry;

import javax.swing.*;
import java.awt.*;

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

    @NotNull
    @Override
    public Icon getIcon ()
    {
        return loadIcon ( getId () + ".png" );
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new NoWikiPage ();
    }

    @NotNull
    @Override
    public String getStyleCode ( @NotNull final Skin skin )
    {
        return "";
    }

    @NotNull
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

    @NotNull
    @Override
    public JComponent createContent ()
    {
        // Main example container
        final WebPanel container = new WebPanel ( new BorderLayout ( 0, 0 ) );

        // Example toolbar
        container.add ( createPreviewToolBar (), BorderLayout.NORTH );

        // Actual example content
        final WebScrollPane scrollPane = new WebScrollPane ( StyleId.scrollpaneUndecoratedButtonless, createContentImpl () );
        scrollPane.getVerticalScrollBar ().setUnitIncrement ( 20 );
        scrollPane.getVerticalScrollBar ().setBlockIncrement ( 50 );
        scrollPane.getHorizontalScrollBar ().setUnitIncrement ( 20 );
        scrollPane.getHorizontalScrollBar ().setBlockIncrement ( 50 );
        container.add ( scrollPane, BorderLayout.CENTER );

        // Example visibility behavior
        new VisibilityBehavior<WebPanel> ( container )
        {
            @Override
            protected void displayed ( @NotNull final WebPanel component )
            {
                AbstractExample.this.displayed ();
            }

            @Override
            protected void hidden ( @NotNull final WebPanel component )
            {
                AbstractExample.this.hidden ();
            }
        }.install ();

        return container;
    }

    /**
     * Informs about example becoming visible on the screen.
     */
    protected void displayed ()
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * Informs about example becoming hidden from the screen.
     */
    protected void hidden ()
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * Returns preview toolbar.
     *
     * @return preview toolbar
     */
    @NotNull
    protected JComponent createPreviewToolBar ()
    {
        final WebToolBar toolbar = new WebToolBar ( DemoStyles.exampleToolbar );
        toolbar.setFloatable ( false );
        toolbar.add ( getWikiPage ().createLink () );
        return toolbar;
    }

    /**
     * Returns example content component.
     *
     * @return example content component
     */
    @NotNull
    protected abstract JComponent createContentImpl ();
}