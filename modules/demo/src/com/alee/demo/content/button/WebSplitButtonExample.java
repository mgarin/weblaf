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

import com.alee.demo.icons.DemoIcons;
import com.alee.demo.api.*;
import com.alee.extended.button.WebSplitButton;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.grouping.GroupPane;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.menu.PopupMenuGenerator;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebSplitButtonExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "websplitbutton";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "splitbutton";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final TextButton e1 = new TextButton ( StyleId.splitbutton );
        final TextButton e2 = new TextButton ( StyleId.splitbuttonRolloverOnly );
        final IconButton e3 = new IconButton ( StyleId.splitbuttonIconOnly );
        final IconButton e4 = new IconButton ( StyleId.splitbuttonIconRolloverOnly );
        return CollectionUtils.<Preview>asList ( e1, e2, e3, e4 );
    }

    /**
     * Split button preview.
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
            super ( WebSplitButtonExample.this, "text", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebSplitButton button = new WebSplitButton ( getStyleId (), "Click me" );
            button.setPopupMenu ( createSamplePopupMenu () );

            final WebSplitButton first = new WebSplitButton ( getStyleId (), "First" );
            first.setPopupMenu ( createSamplePopupMenu () );

            final WebSplitButton second = new WebSplitButton ( getStyleId (), "Second" );
            second.setPopupMenu ( createSamplePopupMenu () );

            final WebSplitButton icon = new WebSplitButton ( getStyleId (), "With icon", WebLookAndFeel.getIcon ( 16 ) );
            icon.setPopupMenu ( createSamplePopupMenu () );

            return CollectionUtils.asList ( button, new GroupPane ( first, second ), icon );
        }
    }

    /**
     * Icon split button preview.
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
            super ( WebSplitButtonExample.this, "icon", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebSplitButton button = new WebSplitButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            button.setPopupMenu ( createSamplePopupMenu () );

            final WebSplitButton first = new WebSplitButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            first.setPopupMenu ( createSamplePopupMenu () );

            final WebSplitButton second = new WebSplitButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            second.setPopupMenu ( createSamplePopupMenu () );

            return CollectionUtils.asList ( button, new GroupPane ( first, second ) );
        }
    }

    /**
     * Returns sample popup menu.
     *
     * @return sample popup menu
     */
    protected static WebPopupMenu createSamplePopupMenu ()
    {
        final PopupMenuGenerator generator = new PopupMenuGenerator ();
        generator.addItem ( DemoIcons.facebook16, "Facebook", null );
        generator.addItem ( DemoIcons.twitter16, "Twitter", null );
        generator.addItem ( DemoIcons.googleplus16, "Google Plus", null );
        generator.addItem ( DemoIcons.linkedin16, "Linked In", null );
        generator.addItem ( DemoIcons.pinterest16, "Pinterest", null );
        generator.addSeparator ();
        generator.addItem ( DemoIcons.youtube16, "Youtube", null );
        generator.addItem ( DemoIcons.vimeo16, "Vimeo", null );
        return generator.getMenu ();
    }
}