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

public class JToggleButtonExample extends AbstractExample
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
    protected List<Preview> createPreviews ()
    {
        final TextToggleButton e1 = new TextToggleButton ( StyleId.togglebutton );
        final TextToggleButton e2 = new TextToggleButton ( StyleId.togglebuttonHover );
        final IconToggleButton e3 = new IconToggleButton ( StyleId.togglebuttonIcon );
        final IconToggleButton e4 = new IconToggleButton ( StyleId.togglebuttonIconHover );
        return CollectionUtils.<Preview>asList ( e1, e2, e3, e4 );
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
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JToggleButton button = new JToggleButton ( "Click me", true );
            button.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JToggleButton first = new JToggleButton ( "First", true );
            first.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JToggleButton second = new JToggleButton ( "Second" );
            second.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JToggleButton icon = new JToggleButton ( "With icon", WebLookAndFeel.getIcon ( 16 ) );
            icon.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            return CollectionUtils.asList ( button, new GroupPane ( first, second ), icon );
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
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
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