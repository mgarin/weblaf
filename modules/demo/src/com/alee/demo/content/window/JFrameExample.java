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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JFrameExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jframe";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "frame";
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
        return new OracleWikiPage ( "How to Make Frames", "frame" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new FramePreview ( "basic", FeatureState.updated, StyleId.frame ),
                new FramePreview ( "decorated", FeatureState.updated, StyleId.frameDecorated )
        );
    }

    /**
     * Simple frame preview.
     */
    protected class FramePreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param state   preview feature state
         * @param styleId preview style ID
         */
        public FramePreview ( final String id, final FeatureState state, final StyleId styleId )
        {
            super ( JFrameExample.this, id, state, styleId );
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
                    final String title = getExampleLanguagePrefix () + "content";
                    final JFrame frame = new JFrame ();
                    UILanguageManager.registerComponent ( frame.getRootPane (), title );
                    frame.getRootPane ().putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
                    frame.setIconImages ( WebLookAndFeel.getImages () );
                    frame.add ( new WebLabel ( title, WebLabel.CENTER ) );
                    frame.setAlwaysOnTop ( true );
                    frame.setSize ( 500, 400 );
                    frame.setLocationRelativeTo ( DemoApplication.getInstance () );
                    frame.setDefaultCloseOperation ( WindowConstants.DISPOSE_ON_CLOSE );
                    frame.setVisible ( true );
                }
            } );
            return CollectionUtils.asList ( button );
        }
    }
}