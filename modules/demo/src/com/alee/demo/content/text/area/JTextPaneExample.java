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

import com.alee.demo.api.*;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class JTextPaneExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "jtextpane";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "textpane";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final BasicPane e1 = new BasicPane ( "basic", StyleId.textpane );
        final ScrollablePane e2 = new ScrollablePane ( "scrollable", StyleId.textpane );
        final ReadOnlyPane e3 = new ReadOnlyPane ( "readonly", StyleId.textpane );
        return CollectionUtils.<Preview>asList ( e1, e2, e3 );
    }

    /**
     * Basic text pane preview.
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
            super ( JTextPaneExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JTextPane textPane = new JTextPane ();
            textPane.setText ( "Sample text" );
            textPane.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( textPane );
        }
    }

    /**
     * Scrollable text pane preview.
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
            super ( JTextPaneExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JTextPane textPane = new JTextPane ();
            textPane.setText ( "Sample\nmultiline\ntext" );
            textPane.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( new WebScrollPane ( textPane ) );
        }
    }

    /**
     * Read only text pane preview.
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
            super ( JTextPaneExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JTextPane textPane = new JTextPane ();
            textPane.setText ( "Read/copy only" );
            textPane.setEditable ( false );
            textPane.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( new WebScrollPane ( textPane ) );
        }
    }
}