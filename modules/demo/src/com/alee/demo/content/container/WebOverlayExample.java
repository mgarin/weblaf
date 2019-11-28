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
import com.alee.api.annotations.Nullable;
import com.alee.api.data.BoxOrientation;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.content.SampleInterface;
import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.overlay.AlignedOverlay;
import com.alee.extended.overlay.FillOverlay;
import com.alee.extended.overlay.WebOverlay;
import com.alee.extended.panel.AlignPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.text.WebTextField;
import com.alee.managers.style.StyleId;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.swing.NoOpKeyListener;
import com.alee.utils.swing.NoOpMouseListener;
import com.alee.utils.swing.extensions.DocumentEventRunnable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * {@link WebOverlay} example.
 *
 * @author Mikle Garin
 */
public class WebOverlayExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "overlay";
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
                new TextFieldOverlayPreview (),
                new BlockingOverlayPreview ()
        );
    }

    /**
     * Text field overlay preview.
     */
    protected class TextFieldOverlayPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         */
        public TextFieldOverlayPreview ()
        {
            super ( WebOverlayExample.this, "textfield", FeatureState.updated, StyleId.overlay );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebOverlay overlay = new WebOverlay ( getStyleId () );

            final WebTextField textField = new WebTextField ( 15 );
            textField.setLanguage ( getPreviewLanguageKey ( "field" ) );
            textField.setMargin ( 3, 0, 3, 0 );
            overlay.setContent ( textField );

            final WebLabel overlayLabel = new WebLabel ( DemoIcons.info14 );
            overlayLabel.setToolTip ( getPreviewLanguageKey ( "tooltip" ), TooltipWay.right );

            textField.onChange ( new DocumentEventRunnable<WebTextField> ()
            {
                @Override
                public void run ( @NotNull final WebTextField component, @Nullable final DocumentEvent event )
                {
                    CoreSwingUtils.invokeLater ( new Runnable ()
                    {
                        @Override
                        public void run ()
                        {
                            final String text = textField.getText ();
                            if ( text.length () > 0 )
                            {
                                if ( overlay.getOverlayCount () == 0 )
                                {
                                    overlay.addOverlay (
                                            new AlignedOverlay (
                                                    overlayLabel,
                                                    BoxOrientation.right,
                                                    BoxOrientation.top,
                                                    new Insets ( 0, 0, 0, 3 )
                                            )
                                    );
                                }
                            }
                            else
                            {
                                if ( overlay.getOverlayCount () > 0 )
                                {
                                    overlay.removeOverlay ( overlayLabel );
                                }
                            }
                        }
                    } );
                }
            } );

            return CollectionUtils.asList ( overlay );
        }
    }

    /**
     * Blocking overlay preview.
     */
    protected class BlockingOverlayPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         */
        public BlockingOverlayPreview ()
        {
            super ( WebOverlayExample.this, "blocking", FeatureState.updated, StyleId.overlay );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebOverlay overlay = new WebOverlay ( getStyleId (), SampleInterface.createAuthForm () );

            final WebStyledLabel blockingOverlay = new WebStyledLabel (
                    DemoStyles.blockingLayerLabel,
                    getPreviewLanguageKey ( "overlay.text" ),
                    SwingConstants.CENTER
            );
            NoOpMouseListener.install ( blockingOverlay );
            NoOpKeyListener.install ( blockingOverlay );

            final WebButton control = new WebButton ( getPreviewLanguageKey ( "overlay.show" ) );
            control.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( @NotNull final ActionEvent e )
                {
                    if ( blockingOverlay.isShowing () )
                    {
                        overlay.removeOverlay ( blockingOverlay );
                        control.setLanguage ( getPreviewLanguageKey ( "overlay.show" ) );
                    }
                    else
                    {
                        overlay.addOverlay ( new FillOverlay ( blockingOverlay ) );
                        control.setLanguage ( getPreviewLanguageKey ( "overlay.hide" ) );
                    }
                }
            } );

            return CollectionUtils.asList ( overlay, new AlignPanel ( control, SwingConstants.CENTER, SwingConstants.CENTER ) );
        }
    }
}