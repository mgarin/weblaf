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
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.CompactFlowLayout;
import com.alee.laf.label.WebLabel;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;
import com.alee.utils.ArrayUtils;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JTabbedPaneExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jtabbedpane";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "tabbedpane";
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
        return new OracleWikiPage ( "How to Use Tabbed Panes", "tabbedpane" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new TabbedPanePreview ( "top", FeatureState.updated, JTabbedPane.TOP ),
                new TabbedPanePreview ( "left", FeatureState.updated, JTabbedPane.LEFT ),
                new TabbedPanePreview ( "bottom", FeatureState.updated, JTabbedPane.BOTTOM ),
                new TabbedPanePreview ( "right", FeatureState.updated, JTabbedPane.RIGHT )
        );
    }

    /**
     * {@link JTabbedPane} preview.
     */
    protected class TabbedPanePreview extends AbstractStylePreview
    {
        /**
         * Tab placement.
         */
        protected final int tabPlacement;

        /**
         * Constructs new {@link TabbedPanePreview}.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param tabPlacement tab placement
         */
        public TabbedPanePreview ( final String id, final FeatureState featureState, final int tabPlacement )
        {
            super ( JTabbedPaneExample.this, id, featureState, StyleId.tabbedpane );
            this.tabPlacement = tabPlacement;
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
                    createTabbedPane ( JTabbedPane.WRAP_TAB_LAYOUT ),
                    createTabbedPane ( JTabbedPane.SCROLL_TAB_LAYOUT )
            );
        }

        /**
         * Returns single {@link JTabbedPane} instance with specified layout policy.
         *
         * @param layoutPolicy layout policy
         * @return single {@link JTabbedPane} instance with specified layout policy
         */
        @NotNull
        protected JTabbedPane createTabbedPane ( final int layoutPolicy )
        {
            final JTabbedPane tabbedPane = new JTabbedPane ( tabPlacement, layoutPolicy );
            tabbedPane.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            for ( int i = 0; i < 8; i++ )
            {
                final String key = getExampleLanguageKey ( "data.tab" + i );
                final Icon icon = ArrayUtils.roundRobin ( i, Icons.leaf, Icons.magnifier, Icons.computer, Icons.globe );
                tabbedPane.addTab ( key, icon, createTabContent () );
            }
            tabbedPane.setPreferredSize ( new Dimension (
                    350,
                    tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM ? 250 : 150
            ) );
            return tabbedPane;
        }

        /**
         * Returns single tab content {@link JComponent}.
         *
         * @return single tab content {@link JComponent}
         */
        @NotNull
        protected JComponent createTabContent ()
        {
            final WebStyledLabel content = new WebStyledLabel (
                    DemoStyles.placeholderLabel,
                    getExampleLanguageKey ( "data.content" ),
                    WebLabel.CENTER
            );
            content.setFocusable ( true );
            content.addMouseListener ( new MouseAdapter ()
            {
                @Override
                public void mousePressed ( final MouseEvent e )
                {
                    content.requestFocusInWindow ();
                }
            } );
            return content;
        }
    }
}