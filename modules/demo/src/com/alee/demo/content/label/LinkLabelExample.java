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
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class LinkLabelExample extends AbstractExample
{
    @Override
    public String getId ()
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
        //        final CodeHotkeyLabel e2 = new CodeHotkeyLabel ( FeatureState.updated, StyleId.hotkeylabel );
        //        final TextHotkeyLabel e3 = new TextHotkeyLabel ( FeatureState.release, StyleId.hotkeylabel );
        return CollectionUtils.<Preview>asList ( e1 );
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
            super ( LinkLabelExample.this, "url", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId id )
        {
            return CollectionUtils.asList ( new WebLinkLabel ( getStyleId (), DemoApplication.WEBLAF_SITE ) );
        }
    }
}