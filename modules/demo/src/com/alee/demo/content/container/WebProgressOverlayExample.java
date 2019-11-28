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

package com.alee.demo.content.container;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.overlay.WebProgressOverlay;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * {@link WebProgressOverlay} example.
 *
 * @author Mikle Garin
 */
public class WebProgressOverlayExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "progressoverlay";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "overlay";
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
        return new WebLafWikiPage ( "How to Use WebOverlay" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new TemporaryOverlayPreview (),
                new ToggledOverlayPreview ()
        );
    }

    /**
     * Temporary progress overlay preview.
     */
    protected class TemporaryOverlayPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         */
        public TemporaryOverlayPreview ()
        {
            super ( WebProgressOverlayExample.this, "temporary", FeatureState.release, StyleId.progressoverlay );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebProgressOverlay overlay = new WebProgressOverlay ( getStyleId () );

            final WebButton button = new WebButton ( getPreviewLanguageKey ( "button" ) );
            button.setPreferredWidth ( 250 );
            button.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( @NotNull final ActionEvent e )
                {
                    button.setEnabled ( false );
                    overlay.displayProgress ();

                    WebTimer.delay ( 5000, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( @NotNull final ActionEvent e )
                        {
                            button.setEnabled ( true );
                            overlay.hideProgress ();
                        }
                    } );
                }
            } );
            overlay.setContent ( button );

            return CollectionUtils.asList ( overlay );
        }
    }

    /**
     * Toggleable progress overlay preview.
     */
    protected class ToggledOverlayPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         */
        public ToggledOverlayPreview ()
        {
            super ( WebProgressOverlayExample.this, "toggled", FeatureState.release, StyleId.progressoverlay );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebProgressOverlay overlay = new WebProgressOverlay ( getStyleId () );

            final WebButton button = new WebButton ( getPreviewLanguageKey ( "show" ) );
            button.setPreferredWidth ( 250 );
            button.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( @NotNull final ActionEvent e )
                {
                    if ( overlay.isProgressDisplayed () )
                    {
                        overlay.hideProgress ();
                        button.setLanguage ( getPreviewLanguageKey ( "show" ) );
                    }
                    else
                    {
                        overlay.displayProgress ();
                        button.setLanguage ( getPreviewLanguageKey ( "hide" ) );
                    }
                }
            } );
            overlay.setContent ( button );

            return CollectionUtils.asList ( overlay );
        }
    }
}