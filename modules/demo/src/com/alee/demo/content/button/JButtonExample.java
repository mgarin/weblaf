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

import com.alee.demo.api.*;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.grouping.GroupPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class JButtonExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "jbutton";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "button";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final TextButton e1 = new TextButton ( StyleId.button );
        final TextButton e2 = new TextButton ( StyleId.buttonHover );
        final IconButton e3 = new IconButton ( StyleId.buttonIcon );
        final IconButton e4 = new IconButton ( StyleId.buttonIconHover );
        return CollectionUtils.<Preview>asList ( e1, e2, e3, e4 );
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

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JButton button = new JButton ( "Click me" );
            button.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JButton first = new JButton ( "First" );
            first.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JButton second = new JButton ( "Second" );
            second.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JButton icon = new JButton ( "With icon", WebLookAndFeel.getIcon ( 16 ) );
            icon.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            return CollectionUtils.asList ( button, new GroupPane ( first, second ), icon );
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

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JButton button = new JButton ( WebLookAndFeel.getIcon ( 16 ) );
            button.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JButton first = new JButton ( WebLookAndFeel.getIcon ( 16 ) );
            first.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JButton second = new JButton ( WebLookAndFeel.getIcon ( 16 ) );
            second.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            return CollectionUtils.asList ( button, new GroupPane ( first, second ) );
        }
    }
}