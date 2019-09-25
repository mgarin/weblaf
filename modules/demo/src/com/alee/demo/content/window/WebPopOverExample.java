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
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.window.PopOverDirection;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.LM;
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
public class WebPopOverExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "popover";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "popover";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new WebLafWikiPage ( "How to use WebPopOver" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new PopOverPreview ( "basic", FeatureState.beta, StyleId.popover )
        );
    }

    /**
     * Simple {@link WebPopOver} preview.
     */
    protected class PopOverPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param state   preview feature state
         * @param styleId preview style ID
         */
        public PopOverPreview ( final String id, final FeatureState state, final StyleId styleId )
        {
            super ( WebPopOverExample.this, id, state, styleId );
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
                    final WebPopOver popOver = new WebPopOver ( getStyleId (), parent );
                    popOver.setIconImages ( WebLookAndFeel.getImages () );
                    popOver.setCloseOnFocusLoss ( true );
                    popOver.setPadding ( 10 );

                    final WebPanel container = new WebPanel ( StyleId.panelTransparent, new BorderLayout ( 5, 5 ) );

                    final WebLabel label = new WebLabel ( getExampleLanguagePrefix () + "label", WebLabel.CENTER );
                    container.add ( label, BorderLayout.NORTH );

                    final String text = LM.get ( getExampleLanguagePrefix () + "text" );
                    final WebTextField field = new WebTextField ( text, 20 );
                    field.setHorizontalAlignment ( WebTextField.CENTER );
                    container.add ( field, BorderLayout.CENTER );

                    popOver.add ( container );

                    popOver.show ( button, PopOverDirection.down );
                }
            } );
            return CollectionUtils.asList ( button );
        }
    }
}