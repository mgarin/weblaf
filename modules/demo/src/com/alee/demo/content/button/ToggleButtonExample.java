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
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.grouping.GroupPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class ToggleButtonExample extends AbstractExample
{
    @Override
    public String getId ()
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
        final TextToggleButton e2 = new TextToggleButton ( StyleId.togglebuttonRolloverOnly );
        final IconToggleButton e3 = new IconToggleButton ( StyleId.togglebuttonIconOnly );
        final IconToggleButton e4 = new IconToggleButton ( StyleId.togglebuttonRolloverIconOnly );
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
            super ( ToggleButtonExample.this, "text", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId id )
        {
            final WebToggleButton button = new WebToggleButton ( getStyleId (), "Click me", true );
            final WebToggleButton first = new WebToggleButton ( getStyleId (), "First", true );
            final WebToggleButton second = new WebToggleButton ( getStyleId (), "Second" );
            final WebToggleButton icon = new WebToggleButton ( getStyleId (), "With icon", WebLookAndFeel.getIcon ( 16 ) );
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
            super ( ToggleButtonExample.this, "icon", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId id )
        {
            final WebToggleButton button = new WebToggleButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            final WebToggleButton first = new WebToggleButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            final WebToggleButton second = new WebToggleButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            return CollectionUtils.asList ( button, new GroupPane ( first, second ) );
        }
    }
}