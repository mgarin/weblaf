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
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.label.WebStyledLabel;
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
public class WebStyledLabelExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "webstyledlabel";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "styledlabel";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new WebLafWikiPage ( "How to use WebStyledLabel" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new StyledLabel ( "basic", FeatureState.updated, StyleId.styledlabel ),
                new StyledLabel ( "shadow", FeatureState.updated, StyleId.styledlabelShadow ),
                new StyledLabel ( "tag", FeatureState.release, StyleId.styledlabelTag ),
                new StyledLabel ( "ccw", FeatureState.release, StyleId.styledlabelVerticalCCW ),
                new StyledLabel ( "cw", FeatureState.release, StyleId.styledlabelVerticalCW ),
                new SeparatorStyledLabel ( "separator", FeatureState.beta, StyleId.styledlabelSeparator )
        );
    }

    /**
     * Button preview.
     */
    protected class StyledLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public StyledLabel ( final String id, final FeatureState featureState, final StyleId styleId )
        {
            super ( WebStyledLabelExample.this, id, featureState, styleId );
        }

        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new CompactFlowLayout ( FlowLayout.LEADING, 8, 0 );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final String labelText = "{Custom:b} {styled:c(blue)} {2:sup} text" + "\n" + "{And:b} {And:c(red)} And {another:b} row";
            final WebStyledLabel label = new WebStyledLabel ( getStyleId (), labelText );

            final String iconText = "{Iconed:b} text";
            final WebStyledLabel icon = new WebStyledLabel ( getStyleId (), iconText, WebLookAndFeel.getIcon ( 16 ) );

            return CollectionUtils.asList ( label, icon );
        }
    }

    /**
     * Separator label preview.
     */
    protected class SeparatorStyledLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public SeparatorStyledLabel ( final String id, final FeatureState featureState, final StyleId styleId )
        {
            super ( WebStyledLabelExample.this, id, featureState, styleId );
        }

        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new VerticalFlowLayout ( 8, true, false );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebStyledLabel leading = new WebStyledLabel ( getStyleId (), WebStyledLabel.LEADING );
            leading.setText ( "{Leading:b} {text:c(blue)}" );

            final ImageIcon icon = WebLookAndFeel.getIcon ( 16 );
            final WebStyledLabel center = new WebStyledLabel ( getStyleId (), icon, WebStyledLabel.CENTER );
            center.setText ( "{Centered:b} {text:c(blue)} {with:u} {icon:c(red)}" );

            final WebStyledLabel trailing = new WebStyledLabel ( getStyleId (), WebStyledLabel.TRAILING );
            trailing.setText ( "{Trailing:c(blue)} {text:b}" );

            return CollectionUtils.asList ( leading, center, trailing );
        }
    }
}