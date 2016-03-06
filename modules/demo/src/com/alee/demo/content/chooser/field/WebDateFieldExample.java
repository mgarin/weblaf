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

package com.alee.demo.content.chooser.field;

import com.alee.demo.api.*;
import com.alee.extended.date.DateListener;
import com.alee.extended.date.WebDateField;
import com.alee.laf.label.WebLabel;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebInnerNotification;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.Date;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebDateFieldExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "webdatefield";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "datefield";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final DateField e1 = new DateField ( StyleId.datefield );
        return CollectionUtils.<Preview>asList ( e1 );
    }

    /**
     * Date field preview.
     */
    protected class DateField extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public DateField ( final StyleId styleId )
        {
            super ( WebDateFieldExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebDateField dateField = new WebDateField ( getStyleId () );
            dateField.addDateListener ( new DateListener ()
            {
                @Override
                public void dateChanged ( final Date date )
                {
                    final String d = date != null ? dateField.getDateFormat ().format ( date ) : "null";
                    final WebInnerNotification notification = new WebInnerNotification ();
                    notification.setDisplayTime ( 3000 );
                    notification.setRequestFocusOnShow ( false );
                    notification.setContent ( new WebLabel ( getPreviewLanguagePrefix () + "event", WebLabel.CENTER, d ) );
                    notification.setFocusable ( false );
                    NotificationManager.showInnerNotification ( dateField, notification );
                }
            } );
            return CollectionUtils.asList ( dateField );
        }
    }
}