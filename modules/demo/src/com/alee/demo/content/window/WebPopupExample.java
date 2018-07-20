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

import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.layout.AlignLayout;
import com.alee.extended.window.WebPopup;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebPopupExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "popup";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "popup";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new WebLafWikiPage ( "How to use WebPopup" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new PopupPreview ( "basic", FeatureState.release, StyleId.popup ),
                new PopupPreview ( "undecorated", FeatureState.release, StyleId.popupUndecorated )
        );
    }

    /**
     * Simple popup preview.
     */
    protected class PopupPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param state   preview feature state
         * @param styleId preview style ID
         */
        public PopupPreview ( final String id, final FeatureState state, final StyleId styleId )
        {
            super ( WebPopupExample.this, id, state, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebButton button = new WebButton ( getExampleLanguagePrefix () + "show" );
            button.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    final WebPopup popup = new WebPopup ( getStyleId (), new AlignLayout () );
                    popup.setPadding ( 10 );
                    popup.setResizable ( true );
                    popup.setDraggable ( true );

                    final WebPanel container = new WebPanel ( StyleId.panelTransparent, new BorderLayout ( 5, 5 ) );

                    final WebLabel label = new WebLabel ( getExampleLanguagePrefix () + "label", WebLabel.CENTER );
                    container.add ( label, BorderLayout.NORTH );

                    final String text = LM.get ( getExampleLanguagePrefix () + "text" );
                    final WebTextField field = new WebTextField ( text, 20 );
                    field.setHorizontalAlignment ( WebTextField.CENTER );
                    container.add ( field, BorderLayout.CENTER );

                    popup.add ( container );

                    popup.pack ();
                    popup.setResizable ( true );
                    popup.showPopup ( button, 0, button.getHeight () );
                }
            } );
            return CollectionUtils.asList ( button );
        }
    }
}