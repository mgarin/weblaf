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

package com.alee.demo.content.menu;

import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.window.WebDialog;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.extensions.ComponentEventRunnable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JMenuBarExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "jmenubar";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "menubar";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Menus", "menu" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new MenuBarPreview ( "basic", FeatureState.updated, StyleId.menubar ),
                new MenuBarPreview ( "undecorated", FeatureState.updated, StyleId.menubarUndecorated )
        );
    }

    /**
     * Simple menu bar preview.
     */
    protected class MenuBarPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param state   preview feature state
         * @param styleId preview style ID
         */
        public MenuBarPreview ( final String id, final FeatureState state, final StyleId styleId )
        {
            super ( JMenuBarExample.this, id, state, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            // Menu bar
            final JMenuBar menuBar = createMenuBar ();

            // Menu bar in native dialog
            final WebToggleButton nativeDialog = createDialogButton ( "native", StyleId.dialog );

            // Menu bar in decorated dialog
            final WebToggleButton decoratedDialog = createDialogButton ( "decorated", StyleId.dialogDecorated );

            return CollectionUtils.asList ( menuBar, nativeDialog, decoratedDialog );
        }

        /**
         * Returns new dialog preview button.
         *
         * @param text          button text
         * @param dialogStyleId dialog style ID
         * @return new dialog preview button
         */
        protected WebToggleButton createDialogButton ( final String text, final StyleId dialogStyleId )
        {
            final String showText = getExampleLanguagePrefix () + text;
            final WebToggleButton showButton = new WebToggleButton ( showText, Icons.external );
            showButton.addActionListener ( new ActionListener ()
            {
                /**
                 * Dialog containing menu bar.
                 */
                private WebDialog dialog;

                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    if ( dialog == null || !dialog.isVisible () )
                    {
                        if ( dialog == null )
                        {
                            dialog = new WebDialog ( dialogStyleId, DemoApplication.getInstance (), getTitle () );
                            dialog.setJMenuBar ( createMenuBar () );

                            final String contentText = getExampleLanguagePrefix () + "content";
                            final WebLabel contentLabel = new WebLabel ( contentText, SwingConstants.CENTER );
                            dialog.add ( contentLabel.setPreferredSize ( 300, 250 ) );

                            dialog.setDefaultCloseOperation ( WindowConstants.DISPOSE_ON_CLOSE );
                            dialog.setModal ( false );

                            dialog.onClose ( new ComponentEventRunnable ()
                            {
                                @Override
                                public void run ( final ComponentEvent e )
                                {
                                    if ( showButton.isSelected () )
                                    {
                                        showButton.setSelected ( false );
                                    }
                                }
                            } );
                        }
                        dialog.pack ();
                        dialog.setLocationRelativeTo ( DemoApplication.getInstance () );
                        dialog.setVisible ( true );
                    }
                    else if ( dialog.isVisible () )
                    {
                        dialog.dispose ();
                    }
                }
            } );
            return showButton;
        }

        /**
         * Returns new menu bar.
         *
         * @return new menu bar
         */
        protected JMenuBar createMenuBar ()
        {
            // Creating new menu bar
            final JMenuBar menuBar = new JMenuBar ();

            // Providing appropriate style
            menuBar.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            // Filling menu bar with items
            return MenusGroup.fillMenuBar ( menuBar );
        }
    }
}