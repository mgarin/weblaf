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

package com.alee.demo.content.button;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JCheckBoxExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jcheckbox";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "checkbox";
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
        return new OracleWikiPage ( "How to Use Check Boxes", "button" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new TextCheckBox ( "basic", StyleId.checkbox, "Simple check text" ),
                new TextCheckBox ( "styled", StyleId.checkboxStyled, "{Styled:b} {check:u} {text:c(red)}" ),
                new TextCheckBox ( "link", StyleId.checkboxLink, "Link-like {check:b} box" )
        );
    }

    /**
     * Check box preview.
     */
    protected class TextCheckBox extends AbstractStylePreview
    {
        /**
         * Check box text.
         */
        protected String text;

        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         * @param text    check box text
         */
        public TextCheckBox ( final String id, final StyleId styleId, final String text )
        {
            super ( JCheckBoxExample.this, id, FeatureState.updated, styleId );
            this.text = text;
        }

        @NotNull
        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new VerticalFlowLayout ( 0, 8, false, false );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JCheckBox cb1 = createCheckBox ( "1", true );
            final JCheckBox cb2 = createCheckBox ( "2", false );
            final JCheckBox cb3 = createCheckBox ( "3", false );
            return CollectionUtils.asList ( cb1, cb2, cb3 );
        }

        /**
         * Returns new check box instance.
         *
         * @param suffix   check box text suffix
         * @param selected whether or not check box is selected
         * @return new check box instance
         */
        protected JCheckBox createCheckBox ( final String suffix, final boolean selected )
        {
            final JCheckBox checkBox = new JCheckBox ( text + " " + suffix, selected );
            checkBox.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return checkBox;
        }
    }
}