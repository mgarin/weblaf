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

package com.alee.demo.content.label;

import com.alee.api.annotations.NotNull;
import com.alee.demo.DemoApplication;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.link.*;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebLinkExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "weblink";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "link";
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
        return new WebLafWikiPage ( "How to use WebLink" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new UrlLink ( FeatureState.release, StyleId.link ),
                new EmailLink ( FeatureState.release, StyleId.link ),
                new FileLink ( FeatureState.release, StyleId.link ),
                new ActionLink ( FeatureState.release, StyleId.link )
        );
    }

    /**
     * URL link label preview.
     */
    protected class UrlLink extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public UrlLink ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebLinkExample.this, "url", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebLink link = new WebLink ( getStyleId (), new UrlLinkAction ( DemoApplication.WEBLAF_SITE ) );
            return CollectionUtils.asList ( link );
        }
    }

    /**
     * E-mail link label preview.
     */
    protected class EmailLink extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public EmailLink ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebLinkExample.this, "email", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebLink link = new WebLink ( getStyleId (), new EmailLinkAction ( DemoApplication.WEBLAF_EMAIL ) );
            return CollectionUtils.asList ( link );
        }
    }

    /**
     * File link label preview.
     */
    protected class FileLink extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public FileLink ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebLinkExample.this, "file", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebLink link = new WebLink ( getStyleId (), new FileLinkAction ( FileUtils.getUserHome () ) );
            return CollectionUtils.asList ( link );
        }
    }

    /**
     * Action link label preview.
     */
    protected class ActionLink extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public ActionLink ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebLinkExample.this, "action", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final ImageIcon icon = WebLookAndFeel.getIcon ( 16 );
            final String text = getPreviewLanguagePrefix () + "link";
            final WebLink link = new WebLink ( getStyleId (), new AbstractLinkAction ( icon, text )
            {
                @Override
                public void linkExecuted ( final ActionEvent event )
                {
                    NotificationManager.showNotification ( ( Component ) event.getSource (), getPreviewLanguagePrefix () + "notification" );
                }
            } );
            return CollectionUtils.asList ( link );
        }
    }
}