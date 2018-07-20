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

import com.alee.api.data.Orientation;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.layout.CompactFlowLayout;
import com.alee.extended.split.MultiSplitConstraints;
import com.alee.extended.split.WebMultiSplitPane;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.text.LoremIpsum;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebMultiSplitPaneExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "multisplitpane";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "multisplitpane";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new WebLafWikiPage ( "How to use WebMultiSplitPane" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new MultiSplitPanePreview ( "basic", FeatureState.beta, StyleId.multisplitpane, false, true, true ),
                new MultiSplitPanePreview ( "basic", FeatureState.beta, StyleId.multisplitpane, false, true, false ),
                new MultiSplitPanePreview ( "transparent", FeatureState.beta, StyleId.multisplitpaneTransparent, true, false, true ),
                new MultiSplitPanePreview ( "transparent", FeatureState.beta, StyleId.multisplitpaneTransparent, true, false, false ),
                new MultiSplitPanePreview ( "decorated", FeatureState.beta, StyleId.multisplitpaneDecorated, false, true, true ),
                new MultiSplitPanePreview ( "decorated", FeatureState.beta, StyleId.multisplitpaneDecorated, false, true, false ),
                new MultiSplitPanePreview ( "focusable", FeatureState.beta, StyleId.multisplitpaneFocusable, false, true, false ),
                new MultiSplitPanePreview ( "focusable", FeatureState.beta, StyleId.multisplitpaneFocusable, true, false, false )
        );
    }

    /**
     * {@link WebMultiSplitPane} preview.
     */
    protected class MultiSplitPanePreview extends AbstractStylePreview
    {
        /**
         * Whether or not should use continuous layout.
         */
        private final boolean continuous;

        /**
         * Whether or not should be one-touch expandable.
         */
        private final boolean oneTouchExpandable;

        /**
         * Whether or not should use simple sides content.
         */
        private final boolean simpleContent;

        /**
         * Constructs new {@link MultiSplitPanePreview}.
         *
         * @param id                 preview ID
         * @param featureState       feature state
         * @param styleId            preview style ID
         * @param continuous         whether or not should use continuous layout
         * @param oneTouchExpandable whether or not should be one-touch expandable
         * @param simpleContent      whether or not should use simple sides content
         */
        public MultiSplitPanePreview ( final String id, final FeatureState featureState, final StyleId styleId, final boolean continuous,
                                       final boolean oneTouchExpandable, final boolean simpleContent )
        {
            super ( WebMultiSplitPaneExample.this, id, featureState, styleId );
            this.continuous = continuous;
            this.oneTouchExpandable = oneTouchExpandable;
            this.simpleContent = simpleContent;
        }

        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new CompactFlowLayout ( FlowLayout.LEADING, 8, 0 );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            return CollectionUtils.asList (
                    createSplitPane ( Orientation.horizontal ),
                    createSplitPane ( Orientation.vertical )
            );
        }

        /**
         * Returns new {@link WebMultiSplitPane} instance with the specified orientation.
         *
         * @param orientation {@link WebMultiSplitPane} orientation
         * @return new {@link WebMultiSplitPane} instance with the specified orientation
         */
        protected WebMultiSplitPane createSplitPane ( final Orientation orientation )
        {
            final WebMultiSplitPane splitPane = new WebMultiSplitPane ( getStyleId (), orientation );
            splitPane.setPreferredSize ( new Dimension ( 300, 200 ) );
            splitPane.setContinuousLayout ( continuous );
            splitPane.setOneTouchExpandable ( oneTouchExpandable );
            splitPane.add ( createSide (), MultiSplitConstraints.FILL );
            splitPane.add ( createSide (), MultiSplitConstraints.FILL );
            splitPane.add ( createSide (), MultiSplitConstraints.FILL );
            return splitPane;
        }

        /**
         * Returns sample {@link JComponent} to fill-in as {@link WebMultiSplitPane} content.
         *
         * @return sample {@link JComponent} to fill-in as {@link WebMultiSplitPane} content
         */
        protected JComponent createSide ()
        {
            final JComponent component;
            if ( simpleContent )
            {
                final String text = getExampleLanguageKey ( "content" );
                component = new WebLabel ( text, WebLabel.CENTER );
            }
            else
            {
                final String text = new LoremIpsum ().getParagraphs ( 3 );

                final WebTextArea leftArea = new WebTextArea ( StyleId.textareaNonOpaque, text, 0, 0 );
                leftArea.setPreferredWidth ( 0 );
                leftArea.setLineWrap ( true );
                leftArea.setWrapStyleWord ( true );

                component = new WebScrollPane ( StyleId.scrollpaneTransparentHovering, leftArea );
            }
            return component;
        }
    }
}