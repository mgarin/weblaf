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
public class JTextAreaExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "jtextarea";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "textarea";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Text Areas", "textarea" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicArea ( "basic", StyleId.textarea ),
                new FixedArea ( "fixed", StyleId.textarea ),
                new ScrollableArea ( "scrollable", StyleId.textarea ),
                new ReadOnlyArea ( "readonly", StyleId.textarea )
        );
    }

    /**
     * Basic text area preview.
     */
    protected class BasicArea extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public BasicArea ( final String id, final StyleId styleId )
        {
            super ( JTextAreaExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTextArea textArea = new JTextArea ( "Sample text" );
            textArea.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( textArea );
        }
    }

    /**
     * Fixed size text area preview.
     */
    protected class FixedArea extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public FixedArea ( final String id, final StyleId styleId )
        {
            super ( JTextAreaExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTextArea textArea = new JTextArea ( 3, 20 );
            textArea.setText ( "Sample\nmultiline\ntext" );
            textArea.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( textArea );
        }
    }

    /**
     * Scrollable text area preview.
     */
    protected class ScrollableArea extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public ScrollableArea ( final String id, final StyleId styleId )
        {
            super ( JTextAreaExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTextArea textArea = new JTextArea ( 3, 20 );
            textArea.setText ( "Sample\nmultiline\ntext" );
            textArea.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( new WebScrollPane ( textArea ) );
        }
    }

    /**
     * Read only text area preview.
     */
    protected class ReadOnlyArea extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public ReadOnlyArea ( final String id, final StyleId styleId )
        {
            super ( JTextAreaExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTextArea textArea = new JTextArea ( "Read/copy only" );
            textArea.setEditable ( false );
            textArea.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( new WebScrollPane ( textArea ) );
        }
    }
}