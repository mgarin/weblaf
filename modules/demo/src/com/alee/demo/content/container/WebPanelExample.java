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
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.layout.CompactFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebPanelExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "webpanel";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "panel";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicPanel ( "basic", FeatureState.updated, StyleId.panel ),
                new BasicPanel ( "decorated", FeatureState.updated, StyleId.panelDecorated ),
                new BasicPanel ( "transparent", FeatureState.updated, StyleId.panelTransparent ),
                new BasicPanel ( "focusable", FeatureState.updated, StyleId.panelFocusable )
        );
    }

    /**
     * Panel preview.
     */
    protected class BasicPanel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public BasicPanel ( final String id, final FeatureState featureState, final StyleId styleId )
        {
            super ( WebPanelExample.this, id, featureState, styleId );
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
            final WebPanel panel = new WebPanel ( getStyleId (), new BorderLayout ( 15, 15 ) );
            panel.setPadding ( 15 );
            panel.add ( new WebLabel ( DemoStyles.placeholderLabel, "NORTH", WebLabel.CENTER ), BorderLayout.NORTH );
            panel.add ( new WebLabel ( DemoStyles.placeholderLabel, "EAST" ), BorderLayout.EAST );
            panel.add ( new WebLabel ( DemoStyles.placeholderLabel, "SOUTH", WebLabel.CENTER ), BorderLayout.SOUTH );
            panel.add ( new WebLabel ( DemoStyles.placeholderLabel, "WEST" ), BorderLayout.WEST );
            panel.add ( new WebButton ( "CENTER" ), BorderLayout.CENTER );
            return CollectionUtils.asList ( panel );
        }
    }
}