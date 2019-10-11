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
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.CompactFlowLayout;
import com.alee.extended.tab.DocumentData;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.label.WebLabel;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;
import com.alee.utils.ArrayUtils;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.Customizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebDocumentPaneExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "documentpane";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "documentpane";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new WebLafWikiPage ( "How to use WebDocumentPane" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new DocumentPanePreview ( "top", FeatureState.updated, JTabbedPane.WRAP_TAB_LAYOUT, JTabbedPane.TOP ),
                new DocumentPanePreview ( "top", FeatureState.updated, JTabbedPane.SCROLL_TAB_LAYOUT, JTabbedPane.TOP ),
                new DocumentPanePreview ( "left", FeatureState.updated, JTabbedPane.WRAP_TAB_LAYOUT, JTabbedPane.LEFT ),
                new DocumentPanePreview ( "left", FeatureState.updated, JTabbedPane.SCROLL_TAB_LAYOUT, JTabbedPane.LEFT ),
                new DocumentPanePreview ( "bottom", FeatureState.updated, JTabbedPane.WRAP_TAB_LAYOUT, JTabbedPane.BOTTOM ),
                new DocumentPanePreview ( "bottom", FeatureState.updated, JTabbedPane.SCROLL_TAB_LAYOUT, JTabbedPane.BOTTOM ),
                new DocumentPanePreview ( "right", FeatureState.updated, JTabbedPane.WRAP_TAB_LAYOUT, JTabbedPane.RIGHT ),
                new DocumentPanePreview ( "right", FeatureState.updated, JTabbedPane.SCROLL_TAB_LAYOUT, JTabbedPane.RIGHT )
        );
    }

    /**
     * {@link WebDocumentPane} preview.
     */
    protected class DocumentPanePreview extends AbstractStylePreview
    {
        /**
         * Tab layout policy.
         */
        protected final int layoutPolicy;

        /**
         * Tab placement.
         */
        protected final int tabPlacement;

        /**
         * Constructs new {@link DocumentPanePreview}.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param layoutPolicy tab layout policy
         * @param tabPlacement tab placement
         */
        public DocumentPanePreview ( final String id, final FeatureState featureState, final int layoutPolicy, final int tabPlacement )
        {
            super ( WebDocumentPaneExample.this, id, featureState, StyleId.documentpane );
            this.layoutPolicy = layoutPolicy;
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
            final WebDocumentPane documentPane = new WebDocumentPane ( getStyleId (), new Customizer<WebTabbedPane> ()
            {
                @Override
                public void customize ( @NotNull final WebTabbedPane tabbedPane )
                {
                    tabbedPane.setTabLayoutPolicy ( layoutPolicy );
                    tabbedPane.setTabPlacement ( tabPlacement );
                }
            } );

            for ( int i = 0; i < 8; i++ )
            {
                final String key = getExampleLanguageKey ( "data.tab" + i );
                final Icon icon = ArrayUtils.roundRobin ( i, Icons.leaf, Icons.magnifier, Icons.computer, Icons.globe );
                documentPane.openDocument ( new DocumentData ( key, icon, key, createTabContent () ) );
            }

            documentPane.setPreferredSize ( new Dimension ( 550, 450 ) );
            return CollectionUtils.asList ( documentPane );
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