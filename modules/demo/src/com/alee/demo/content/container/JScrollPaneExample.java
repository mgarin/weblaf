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

package com.alee.demo.content.container;

import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.frames.examples.ExamplesTree;
import com.alee.extended.layout.CompactFlowLayout;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JScrollPaneExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "jscrollpane";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "scrollpane";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Scroll Panes", "scrollpane" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicScrollPane ( "basic", FeatureState.updated, StyleId.scrollpane ),
                new BasicScrollPane ( "nofocus", FeatureState.updated, StyleId.scrollpaneNoFocus ),
                new BasicScrollPane ( "undecorated", FeatureState.updated, StyleId.scrollpaneUndecorated ),
                new BasicScrollPane ( "transparent", FeatureState.updated, StyleId.scrollpaneTransparent ),
                new BasicScrollPane ( "buttonless", FeatureState.updated, StyleId.scrollpaneButtonless ),
                new BasicScrollPane ( "undecorated.buttonless", FeatureState.updated, StyleId.scrollpaneUndecoratedButtonless ),
                new BasicScrollPane ( "transparent.buttonless", FeatureState.updated, StyleId.scrollpaneTransparentButtonless ),
                new BasicScrollPane ( "hovering", FeatureState.release, StyleId.scrollpaneHovering ),
                new BasicScrollPane ( "transparent.hovering", FeatureState.release, StyleId.scrollpaneTransparentHovering )
        );
    }

    /**
     * Scroll pane preview.
     */
    protected class BasicScrollPane extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public BasicScrollPane ( final String id, final FeatureState featureState, final StyleId styleId )
        {
            super ( JScrollPaneExample.this, id, featureState, styleId );
        }

        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new CompactFlowLayout ( FlowLayout.LEADING, 8, 0 );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JScrollPane scrollPane = new JScrollPane ( new ExamplesTree () );
            scrollPane.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            scrollPane.setPreferredSize ( new Dimension ( 200, 160 ) );
            return CollectionUtils.asList ( scrollPane );
        }
    }
}