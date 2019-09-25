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
public class JButtonExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jbutton";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "button";
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
        return new OracleWikiPage ( "How to Use Buttons", "button" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new TextButton ( StyleId.button ),
                new TextButton ( StyleId.buttonHover ),
                new IconButton ( StyleId.buttonIcon ),
                new IconButton ( StyleId.buttonIconHover )
        );
    }

    /**
     * Button preview.
     */
    protected class TextButton extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public TextButton ( final StyleId styleId )
        {
            super ( JButtonExample.this, "text", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JButton basic = new JButton ();
            basic.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            UILanguageManager.registerComponent ( basic, getPreviewLanguagePrefix () + "basic" );

            final JButton group1 = new JButton ();
            group1.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            UILanguageManager.registerComponent ( group1, getPreviewLanguagePrefix () + "group1" );

            final JButton group2 = new JButton ();
            group2.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            UILanguageManager.registerComponent ( group2, getPreviewLanguagePrefix () + "group2" );

            final JButton group3 = new JButton ();
            group3.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            UILanguageManager.registerComponent ( group3, getPreviewLanguagePrefix () + "group3" );

            final JButton icon = new JButton ( WebLookAndFeel.getIcon ( 16 ) );
            icon.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            UILanguageManager.registerComponent ( icon, getPreviewLanguagePrefix () + "icon" );

            return CollectionUtils.asList ( basic, new GroupPane ( group1, group2, group3 ), icon );
        }
    }

    /**
     * Icon button preview.
     */
    protected class IconButton extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public IconButton ( final StyleId styleId )
        {
            super ( JButtonExample.this, "icon", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JButton basic = new JButton ( WebLookAndFeel.getIcon ( 16 ) );
            basic.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JButton group1 = new JButton ( WebLookAndFeel.getIcon ( 16 ) );
            group1.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JButton group2 = new JButton ( WebLookAndFeel.getIcon ( 16 ) );
            group2.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JButton group3 = new JButton ( WebLookAndFeel.getIcon ( 16 ) );
            group3.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            return CollectionUtils.asList ( basic, new GroupPane ( group1, group2, group3 ) );
        }
    }
}