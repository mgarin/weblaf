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

package com.alee.demo.content.text.area;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JEditorPaneExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jeditorpane";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "editorpane";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Editor Panes", "editorpane" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicPane ( "basic", StyleId.editorpane ),
                new ScrollablePane ( "scrollable", StyleId.editorpane ),
                new ReadOnlyPane ( "readonly", StyleId.editorpane )
        );
    }

    /**
     * Basic editor pane preview.
     */
    protected class BasicPane extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public BasicPane ( final String id, final StyleId styleId )
        {
            super ( JEditorPaneExample.this, id, FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JEditorPane editorPane = new JEditorPane ( "text/html", createHtmlText () );
            editorPane.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( editorPane );
        }
    }

    /**
     * Scrollable editor pane preview.
     */
    protected class ScrollablePane extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public ScrollablePane ( final String id, final StyleId styleId )
        {
            super ( JEditorPaneExample.this, id, FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JEditorPane editorPane = new JEditorPane ( "text/html", createHtmlText () );
            editorPane.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( new WebScrollPane ( editorPane ) );
        }
    }

    /**
     * Read only editor pane preview.
     */
    protected class ReadOnlyPane extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public ReadOnlyPane ( final String id, final StyleId styleId )
        {
            super ( JEditorPaneExample.this, id, FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JEditorPane editorPane = new JEditorPane ( "text/html", createHtmlText () );
            editorPane.setEditable ( false );
            editorPane.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( new WebScrollPane ( editorPane ) );
        }
    }

    /**
     * Returns html text.
     *
     * @return html text
     */
    protected String createHtmlText ()
    {
        return "<html><b>Sample HTML content</b><br>" + "<i>with multiple lines</i><br>" +
                "<font color=red>and custom font style</font></html>";
    }
}