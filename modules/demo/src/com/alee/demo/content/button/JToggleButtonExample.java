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
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.grouping.GroupPane;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JToggleButtonExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "jtogglebutton";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "togglebutton";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Toggle Buttons", "button" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new TextToggleButton ( StyleId.togglebutton ),
                new TextToggleButton ( StyleId.togglebuttonHover ),
                new IconToggleButton ( StyleId.togglebuttonIcon ),
                new IconToggleButton ( StyleId.togglebuttonIconHover )
        );
    }

    /**
     * Toggle button preview.
     */
    protected class TextToggleButton extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public TextToggleButton ( final StyleId styleId )
        {
            super ( JToggleButtonExample.this, "text", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JToggleButton basic = new JToggleButton ( "", true );
            basic.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            UILanguageManager.registerComponent ( basic, getPreviewLanguagePrefix () + "basic" );

            final JToggleButton group1 = new JToggleButton ( "", true );
            group1.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            UILanguageManager.registerComponent ( group1, getPreviewLanguagePrefix () + "group1" );

            final JToggleButton group2 = new JToggleButton ();
            group2.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            UILanguageManager.registerComponent ( group2, getPreviewLanguagePrefix () + "group2" );

            final JToggleButton group3 = new JToggleButton ();
            group3.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            UILanguageManager.registerComponent ( group3, getPreviewLanguagePrefix () + "group3" );

            final JToggleButton icon = new JToggleButton ( WebLookAndFeel.getIcon ( 16 ) );
            icon.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            UILanguageManager.registerComponent ( icon, getPreviewLanguagePrefix () + "icon" );

            return CollectionUtils.asList ( basic, new GroupPane ( group1, group2, group3 ), icon );
        }
    }

    /**
     * Icon toggle button preview.
     */
    protected class IconToggleButton extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public IconToggleButton ( final StyleId styleId )
        {
            super ( JToggleButtonExample.this, "icon", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JToggleButton button = new JToggleButton ( WebLookAndFeel.getIcon ( 16 ) );
            button.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JToggleButton first = new JToggleButton ( WebLookAndFeel.getIcon ( 16 ) );
            first.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JToggleButton second = new JToggleButton ( WebLookAndFeel.getIcon ( 16 ) );
            second.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            return CollectionUtils.asList ( button, new GroupPane ( first, second ) );
        }
    }
}