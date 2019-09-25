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

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.layout.CompactFlowLayout;
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
public class JSplitPaneExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jsplitpane";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "splitpane";
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
        return new OracleWikiPage ( "How to Use Split Panes", "splitpane" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new SplitPanePreview ( "basic", FeatureState.updated, StyleId.splitpane, false, true, true ),
                new SplitPanePreview ( "basic", FeatureState.updated, StyleId.splitpane, false, true, false ),
                new SplitPanePreview ( "transparent", FeatureState.updated, StyleId.splitpaneTransparent, true, false, true ),
                new SplitPanePreview ( "transparent", FeatureState.updated, StyleId.splitpaneTransparent, true, false, false ),
                new SplitPanePreview ( "decorated", FeatureState.updated, StyleId.splitpaneDecorated, false, true, true ),
                new SplitPanePreview ( "decorated", FeatureState.updated, StyleId.splitpaneDecorated, false, true, false ),
                new SplitPanePreview ( "focusable", FeatureState.updated, StyleId.splitpaneFocusable, false, true, false ),
                new SplitPanePreview ( "focusable", FeatureState.updated, StyleId.splitpaneFocusable, true, false, false )
        );
    }

    /**
     * {@link JSplitPane} preview.
     */
    protected class SplitPanePreview extends AbstractStylePreview
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
         * Constructs new {@link SplitPanePreview}.
         *
         * @param id                 preview ID
         * @param featureState       feature state
         * @param styleId            preview style ID
         * @param continuous         whether or not should use continuous layout
         * @param oneTouchExpandable whether or not should be one-touch expandable
         * @param simpleContent      whether or not should use simple sides content
         */
        public SplitPanePreview ( final String id, final FeatureState featureState, final StyleId styleId, final boolean continuous,
                                  final boolean oneTouchExpandable, final boolean simpleContent )
        {
            super ( JSplitPaneExample.this, id, featureState, styleId );
            this.continuous = continuous;
            this.oneTouchExpandable = oneTouchExpandable;
            this.simpleContent = simpleContent;
        }

        @NotNull
        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new CompactFlowLayout ( FlowLayout.LEADING, 8, 0 );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            return CollectionUtils.asList (
                    createSplitPane ( JSplitPane.HORIZONTAL_SPLIT ),
                    createSplitPane ( JSplitPane.VERTICAL_SPLIT )
            );
        }

        /**
         * Returns new {@link JSplitPane} instance with the specified orientation.
         *
         * @param orientation {@link JSplitPane} orientation
         * @return new {@link JSplitPane} instance with the specified orientation
         */
        protected JSplitPane createSplitPane ( final int orientation )
        {
            final JComponent left = createSide ( SwingConstants.LEFT );
            final JComponent right = createSide ( SwingConstants.RIGHT );

            final JSplitPane splitPane = new JSplitPane ( orientation, left, right );
            splitPane.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            splitPane.setPreferredSize ( new Dimension ( 300, 200 ) );
            splitPane.setResizeWeight ( 0.5 );
            splitPane.setContinuousLayout ( continuous );
            splitPane.setOneTouchExpandable ( oneTouchExpandable );

            return splitPane;
        }

        /**
         * Returns new {@link JComponent} for split pane side content.
         *
         * @param side {@link SwingConstants#LEFT} or {@link SwingConstants#RIGHT}
         * @return new {@link JComponent} for split pane side content
         */
        protected JComponent createSide ( final int side )
        {
            final JComponent component;
            if ( simpleContent )
            {
                final String languageKey = getExampleLanguageKey ( side == SwingConstants.LEFT ? "left" : "right" );
                component = new WebLabel ( languageKey, WebLabel.CENTER );
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