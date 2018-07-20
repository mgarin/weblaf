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
import com.alee.demo.skin.DemoIcons;
import com.alee.extended.button.WebSplitButton;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.grouping.GroupPane;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.menu.PopupMenuGenerator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebSplitButtonExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "splitbutton";
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
        return CollectionUtils.<Preview>asList (
                new TextButton ( StyleId.splitbutton ),
                new TextButton ( StyleId.splitbuttonHover ),
                new IconButton ( StyleId.splitbuttonIcon ),
                new IconButton ( StyleId.splitbuttonIconHover )
        );
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
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebSplitButton button = new WebSplitButton ( getStyleId (), "Select one..." );
            button.setPopupMenu ( createSamplePopupMenu ( button, false, true ) );

            final WebSplitButton first = new WebSplitButton ( getStyleId (), "Select one..." );
            first.setMenuIcon ( DemoIcons.menu16 );
            first.setPopupMenu ( createSamplePopupMenu ( first, false, true ) );

            final WebSplitButton second = new WebSplitButton ( getStyleId (), "Select one...", WebLookAndFeel.getIcon ( 16 ) );
            second.setPopupMenu ( createSamplePopupMenu ( second, true, true ) );

            final WebSplitButton icon = new WebSplitButton ( getStyleId (), "Select one...", WebLookAndFeel.getIcon ( 16 ) );
            icon.setMenuIcon ( DemoIcons.menu16 );
            icon.setPopupMenu ( createSamplePopupMenu ( icon, true, true ) );

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
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebSplitButton button = new WebSplitButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            button.setMenuIcon ( DemoIcons.menu16 );
            button.setPopupMenu ( createSamplePopupMenu ( button, true, false ) );

            final WebSplitButton first = new WebSplitButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            first.setPopupMenu ( createSamplePopupMenu ( first, true, false ) );

            final WebSplitButton second = new WebSplitButton ( getStyleId (), WebLookAndFeel.getIcon ( 16 ) );
            second.setPopupMenu ( createSamplePopupMenu ( second, true, false ) );

            return CollectionUtils.asList ( button, new GroupPane ( first, second ) );
        }
    }

    /**
     * Returns sample popup menu.
     *
     * @param button  split button for which sample menu should be created
     * @param addIcon whether or not button should contain icon
     * @param addText whether or not button should contain text
     * @return sample popup menu
     */
    protected static WebPopupMenu createSamplePopupMenu ( final WebSplitButton button, final boolean addIcon, final boolean addText )
    {
        final PopupMenuGenerator generator = new PopupMenuGenerator ();
        createPopupMenuItem ( button, addIcon, addText, generator, DemoIcons.facebook16, "Facebook" );
        createPopupMenuItem ( button, addIcon, addText, generator, DemoIcons.twitter16, "Twitter" );
        createPopupMenuItem ( button, addIcon, addText, generator, DemoIcons.googleplus16, "Google Plus" );
        createPopupMenuItem ( button, addIcon, addText, generator, DemoIcons.linkedin16, "Linked In" );
        createPopupMenuItem ( button, addIcon, addText, generator, DemoIcons.pinterest16, "Pinterest" );
        generator.addSeparator ();
        createPopupMenuItem ( button, addIcon, addText, generator, DemoIcons.youtube16, "Youtube" );
        createPopupMenuItem ( button, addIcon, addText, generator, DemoIcons.vimeo16, "Vimeo" );
        return generator.getMenu ();
    }

    /**
     * Creates popup menu item that will update button view on selection.
     *
     * @param button    split button for which sample menu should be created
     * @param addIcon   whether or not button should contain icon
     * @param addText   whether or not button should contain text
     * @param generator popup menu generator
     * @param icon      menu item icon
     * @param text      menu item text
     */
    protected static void createPopupMenuItem ( final WebSplitButton button, final boolean addIcon, final boolean addText,
                                                final PopupMenuGenerator generator, final Icon icon, final String text )
    {
        generator.addItem ( icon, text, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( addIcon )
                {
                    button.setIcon ( icon );
                }
                if ( addText )
                {
                    button.setText ( text );
                }
            }
        } );
    }
}