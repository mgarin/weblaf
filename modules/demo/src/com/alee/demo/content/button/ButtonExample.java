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
import com.alee.laf.button.WebButton;
import com.alee.laf.grouping.GroupPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class ButtonExample extends AbstractExample
{
    @Override
    public String getId ()
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
        final TextButton e2 = new TextButton ( StyleId.buttonRolloverOnly );
        final IconButton e3 = new IconButton ( StyleId.buttonIconOnly );
        final IconButton e4 = new IconButton ( StyleId.buttonRolloverIconOnly );
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
            super ( ButtonExample.this, "text", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId id )
        {
            final WebButton button = new WebButton ( getStyleId (), "Click me" );
            final WebButton first = new WebButton ( getStyleId (), "First" );
            final WebButton second = new WebButton ( getStyleId (), "Second" );
            final WebButton icon = new WebButton ( getStyleId (), "With icon", WebLookAndFeel.getIcon ( 16 ) );
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
            super ( ButtonExample.this, "icon", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId id )
        {
            final WebButton button = new WebButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            final WebButton first = new WebButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            final WebButton second = new WebButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            return CollectionUtils.asList ( button, new GroupPane ( first, second ) );
        }
    }
}