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

import com.alee.demo.DemoApplication;
import com.alee.demo.api.*;
import com.alee.extended.label.WebLinkLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebLinkLabelExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "weblinklabel";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "linklabel";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final UrlLinkLabel e1 = new UrlLinkLabel ( FeatureState.updated, StyleId.label );
        final EmailLinkLabel e2 = new EmailLinkLabel ( FeatureState.updated, StyleId.label );
        final FileLinkLabel e3 = new FileLinkLabel ( FeatureState.updated, StyleId.label );
        final ActionLinkLabel e4 = new ActionLinkLabel ( FeatureState.updated, StyleId.label );
        return CollectionUtils.<Preview>asList ( e1, e2, e3, e4 );
    }

    /**
     * URL link label preview.
     */
    protected class UrlLinkLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public UrlLinkLabel ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebLinkLabelExample.this, "url", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebLinkLabel linkLabel = new WebLinkLabel ( getStyleId () );
            linkLabel.setLink ( DemoApplication.WEBLAF_SITE );
            return CollectionUtils.asList ( linkLabel );
        }
    }

    /**
     * E-mail link label preview.
     */
    protected class EmailLinkLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public EmailLinkLabel ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebLinkLabelExample.this, "email", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebLinkLabel linkLabel = new WebLinkLabel ( getStyleId () );
            linkLabel.setEmailLink ( DemoApplication.WEBLAF_EMAIL );
            return CollectionUtils.asList ( linkLabel );
        }
    }

    /**
     * File link label preview.
     */
    protected class FileLinkLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public FileLinkLabel ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebLinkLabelExample.this, "file", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebLinkLabel linkLabel = new WebLinkLabel ( getStyleId () );
            linkLabel.setFileLink ( FileUtils.getUserHome () );
            return CollectionUtils.asList ( linkLabel );
        }
    }

    /**
     * Action link label preview.
     */
    protected class ActionLinkLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public ActionLinkLabel ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebLinkLabelExample.this, "action", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebLinkLabel linkLabel = new WebLinkLabel ( getStyleId (),  WebLookAndFeel.getIcon ( 16 ) );
            linkLabel.setText ( getPreviewLanguagePrefix () + "link" );
            linkLabel.setLink ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    NotificationManager.showNotification ( linkLabel, getPreviewLanguagePrefix () + "notification" );
                }
            } );
            return CollectionUtils.asList ( linkLabel );
        }
    }
}