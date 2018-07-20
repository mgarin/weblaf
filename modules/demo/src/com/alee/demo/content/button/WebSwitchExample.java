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

package com.alee.demo.content.button;

import com.alee.demo.api.example.*;
import com.alee.demo.skin.DemoIcons;
import com.alee.extended.button.WebSwitch;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebSwitchExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "switch";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "switch";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new TextSwitch ( StyleId.wswitch ),
                new IconSwitch ( StyleId.wswitch )
        );
    }

    /**
     * Text switch preview.
     */
    protected class TextSwitch extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public TextSwitch ( final StyleId styleId )
        {
            super ( WebSwitchExample.this, "text", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebSwitch wswitch = new WebSwitch ( getStyleId () );
            wswitch.setSwitchComponents ( getPreviewLanguagePrefix () + "on", getPreviewLanguagePrefix () + "off" );
            return CollectionUtils.asList ( wswitch );
        }
    }

    /**
     * Icon switch preview.
     */
    protected class IconSwitch extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public IconSwitch ( final StyleId styleId )
        {
            super ( WebSwitchExample.this, "icon", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebSwitch wswitch = new WebSwitch ( getStyleId (), true );
            wswitch.setSwitchComponents ( DemoIcons.facebook16, DemoIcons.googleplus16 );
            return CollectionUtils.asList ( wswitch );
        }
    }
}