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
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.layout.CompactFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.managers.style.StyleId;
import com.alee.painter.PainterSupport;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JPanelExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "jpanel";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "panel";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Panels", "panel" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new PanelPreview ( "basic", FeatureState.updated, StyleId.panel ),
                new PanelPreview ( "decorated", FeatureState.updated, StyleId.panelDecorated ),
                new PanelPreview ( "transparent", FeatureState.updated, StyleId.panelTransparent ),
                new PanelPreview ( "focusable", FeatureState.updated, StyleId.panelFocusable )
        );
    }

    /**
     * {@link JPanel} preview.
     */
    protected class PanelPreview extends AbstractStylePreview
    {
        /**
         * Constructs new {@link PanelPreview}.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public PanelPreview ( final String id, final FeatureState featureState, final StyleId styleId )
        {
            super ( JPanelExample.this, id, featureState, styleId );
        }

        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new CompactFlowLayout ( FlowLayout.LEADING, 8, 0 );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JPanel panel = new JPanel ( new BorderLayout ( 15, 15 ) );
            panel.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            PainterSupport.setPadding ( panel, new Insets ( 15, 15, 15, 15 ) );
            panel.add ( new WebLabel ( DemoStyles.placeholderLabel, "NORTH", WebLabel.CENTER ), BorderLayout.NORTH );
            panel.add ( new WebLabel ( DemoStyles.placeholderLabel, "EAST" ), BorderLayout.EAST );
            panel.add ( new WebLabel ( DemoStyles.placeholderLabel, "SOUTH", WebLabel.CENTER ), BorderLayout.SOUTH );
            panel.add ( new WebLabel ( DemoStyles.placeholderLabel, "WEST" ), BorderLayout.WEST );
            panel.add ( new WebButton ( "CENTER" ), BorderLayout.CENTER );
            return CollectionUtils.asList ( panel );
        }
    }
}