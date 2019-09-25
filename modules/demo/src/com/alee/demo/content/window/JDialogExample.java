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

package com.alee.demo.content.window;

import com.alee.api.annotations.NotNull;
import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JDialogExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jdialog";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "dialog";
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
        return new OracleWikiPage ( "How to Make Dialogs", "dialog" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new DialogPreview ( "basic", FeatureState.updated, StyleId.dialog ),
                new DialogPreview ( "decorated", FeatureState.updated, StyleId.dialogDecorated )
        );
    }

    /**
     * Simple dialog preview.
     */
    protected class DialogPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param state   preview feature state
         * @param styleId preview style ID
         */
        public DialogPreview ( final String id, final FeatureState state, final StyleId styleId )
        {
            super ( JDialogExample.this, id, state, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebButton button = new WebButton ( getExampleLanguagePrefix () + "show" );
            button.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    final Window parent = CoreSwingUtils.getWindowAncestor ( button );
                    final String title = getExampleLanguagePrefix () + "content";
                    final JDialog dialog = new JDialog ( parent );
                    UILanguageManager.registerComponent ( dialog.getRootPane (), title );
                    dialog.getRootPane ().putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
                    dialog.setIconImages ( WebLookAndFeel.getImages () );
                    dialog.add ( new WebLabel ( title, WebLabel.CENTER ) );
                    dialog.setSize ( 500, 400 );
                    dialog.setLocationRelativeTo ( DemoApplication.getInstance () );
                    dialog.setDefaultCloseOperation ( WindowConstants.DISPOSE_ON_CLOSE );
                    dialog.setVisible ( true );
                }
            } );
            return CollectionUtils.asList ( button );
        }
    }
}