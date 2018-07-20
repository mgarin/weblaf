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
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.extended.image.WebImage;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.NotificationOption;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebNotificationExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "webnotification";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "notification";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        // todo return new WebLafWikiPage ( "How to use WebNotification" );
        return super.getWikiPage ();
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicNotificationPreview ( "basic", FeatureState.release, StyleId.notification ),
                new OptionalNotificationPreview ( "optional", FeatureState.release, StyleId.notification ),
                new CustomNotificationPreview ( "custom", FeatureState.release, StyleId.notification )
        );
    }

    /**
     * Basic notification preview.
     */
    protected class BasicNotificationPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param state   preview feature state
         * @param styleId preview style ID
         */
        public BasicNotificationPreview ( final String id, final FeatureState state, final StyleId styleId )
        {
            super ( WebNotificationExample.this, id, state, styleId );
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
                    final String title = getPreviewLanguagePrefix () + "text";
                    NotificationManager.showNotification ( button, title );
                }
            } );
            return CollectionUtils.asList ( button );
        }
    }

    /**
     * Optional notification preview.
     */
    protected class OptionalNotificationPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param state   preview feature state
         * @param styleId preview style ID
         */
        public OptionalNotificationPreview ( final String id, final FeatureState state, final StyleId styleId )
        {
            super ( WebNotificationExample.this, id, state, styleId );
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
                    final String title = getPreviewLanguagePrefix () + "text";
                    NotificationManager.showNotification ( button, title, NotificationIcon.question.getIcon (),
                            NotificationOption.yes, NotificationOption.no, NotificationOption.retry );
                }
            } );
            return CollectionUtils.asList ( button );
        }
    }

    /**
     * Custom notification preview.
     */
    protected class CustomNotificationPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param state   preview feature state
         * @param styleId preview style ID
         */
        public CustomNotificationPreview ( final String id, final FeatureState state, final StyleId styleId )
        {
            super ( WebNotificationExample.this, id, state, styleId );
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
                    final WebLabel title = new WebLabel ( getPreviewLanguagePrefix () + "text", WebLabel.CENTER );
                    final WebImage logo = new WebImage ( WebLookAndFeel.getIcon ( 256 ) );
                    final GroupPanel content = new GroupPanel ( 15, false, title, logo );
                    NotificationManager.showNotification ( button, content, ( Icon ) null );
                }
            } );
            return CollectionUtils.asList ( button );
        }
    }
}