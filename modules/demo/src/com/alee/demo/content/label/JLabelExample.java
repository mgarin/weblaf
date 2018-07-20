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

package com.alee.demo.content.label;

import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.layout.CompactFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JLabelExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "jlabel";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "label";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Labels", "label" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicLabel ( "basic", FeatureState.updated, StyleId.label ),
                new BasicLabel ( "shadow", FeatureState.updated, StyleId.labelShadow ),
                new BasicLabel ( "tag", FeatureState.release, StyleId.labelTag ),
                new BasicLabel ( "ccw", FeatureState.release, StyleId.labelVerticalCCW ),
                new BasicLabel ( "cw", FeatureState.release, StyleId.labelVerticalCW ),
                new SeparatorLabel ( "separator", FeatureState.beta, StyleId.labelSeparator )
        );
    }

    /**
     * Label preview.
     */
    protected class BasicLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public BasicLabel ( final String id, final FeatureState featureState, final StyleId styleId )
        {
            super ( JLabelExample.this, id, featureState, styleId );
        }

        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new CompactFlowLayout ( FlowLayout.LEADING, 8, 0 );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JLabel label = new JLabel ( "Simple text", JLabel.LEADING );
            label.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JLabel icon = new JLabel ( "Iconed text", WebLookAndFeel.getIcon ( 16 ), JLabel.LEADING );
            icon.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            return CollectionUtils.asList ( label, icon );
        }
    }

    /**
     * Separator label preview.
     */
    protected class SeparatorLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public SeparatorLabel ( final String id, final FeatureState featureState, final StyleId styleId )
        {
            super ( JLabelExample.this, id, featureState, styleId );
        }

        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, 8, true, false );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JLabel leading = new JLabel ( "Leading text", JLabel.LEADING );
            leading.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final ImageIcon icon = WebLookAndFeel.getIcon ( 16 );
            final JLabel center = new JLabel ( "Centered text with icon", icon, JLabel.CENTER );
            center.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JLabel trailing = new JLabel ( "Trailing text", JLabel.TRAILING );
            trailing.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            return CollectionUtils.asList ( leading, center, trailing );
        }
    }
}