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

import com.alee.demo.DemoApplication;
import com.alee.demo.api.*;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class JFrameExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "jframe";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "rootpane";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final FramePreview e1 = new FramePreview ( "basic", StyleId.frame );
        final FramePreview e2 = new FramePreview ( "decorated", StyleId.frameDecorated );
        return CollectionUtils.<Preview>asList ( e1, e2 );
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
         * @param styleId preview style ID
         */
        public FramePreview ( final String id, final StyleId styleId )
        {
            super ( JFrameExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final ImageIcon icon = loadIcon ( JFrameExample.this.getId () + "/" + getId () + ".png" );
            final WebButton showFrame = new WebButton ( getExampleLanguagePrefix () + "show", icon );
            showFrame.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    final String title = getExampleLanguagePrefix () + "content";
                    final JFrame frame = new JFrame ( title );
                    frame.getRootPane ().putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
                    frame.setIconImages ( WebLookAndFeel.getImages () );
                    frame.add ( new WebLabel ( title, WebLabel.CENTER ) );
                    frame.setAlwaysOnTop ( true );
                    frame.setSize ( 500, 400 );
                    frame.setLocationRelativeTo ( DemoApplication.getInstance () );
                    frame.setVisible ( true );
                }
            } );
            return CollectionUtils.asList ( showFrame );
        }
    }
}