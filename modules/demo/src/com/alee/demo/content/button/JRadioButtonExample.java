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

import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.UnselectableButtonGroup;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JRadioButtonExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "jradiobutton";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "radiobutton";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Radio Buttons", "button" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new TextRadioButton ( "basic", StyleId.radiobutton, "Simple button text" ),
                new TextRadioButton ( "styled", StyleId.radiobuttonStyled, "{Styled:b} {button:u} {text:c(red)}" ),
                new TextRadioButton ( "link", StyleId.radiobuttonLink, "Link-like {radio:b} button" )
        );
    }

    /**
     * Radio button preview.
     */
    protected class TextRadioButton extends AbstractStylePreview
    {
        /**
         * Radio button text.
         */
        protected String text;

        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         * @param text    radio button text
         */
        public TextRadioButton ( final String id, final StyleId styleId, final String text )
        {
            super ( JRadioButtonExample.this, id, FeatureState.updated, styleId );
            this.text = text;
        }

        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new VerticalFlowLayout ( 0, 8, false, false );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JRadioButton rb1 = createRadioButton ( "1", true );
            final JRadioButton rb2 = createRadioButton ( "2", false );
            final JRadioButton rb3 = createRadioButton ( "3", false );
            UnselectableButtonGroup.group ( rb1, rb2, rb3 );
            return CollectionUtils.asList ( rb1, rb2, rb3 );
        }

        /**
         * Returns new radio button instance.
         *
         * @param suffix   radio button text suffix
         * @param selected whether or not radio button is selected
         * @return new radio button instance
         */
        protected JRadioButton createRadioButton ( final String suffix, final boolean selected )
        {
            final JRadioButton radioButton = new JRadioButton ( text + " " + suffix, selected );
            radioButton.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return radioButton;
        }
    }
}