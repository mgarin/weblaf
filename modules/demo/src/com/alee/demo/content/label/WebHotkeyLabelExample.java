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
import com.alee.demo.api.example.*;
import com.alee.extended.label.WebHotkeyLabel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebHotkeyLabelExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "webhotkeylabel";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "hotkeylabel";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new TextHotkeyLabel ( FeatureState.updated, StyleId.hotkeylabel ),
                new CodeHotkeyLabel ( FeatureState.updated, StyleId.hotkeylabel ),
                new DataHotkeyLabel ( FeatureState.release, StyleId.hotkeylabel )
        );
    }

    /**
     * Text-based hotkey label preview.
     */
    protected class TextHotkeyLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public TextHotkeyLabel ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebHotkeyLabelExample.this, "text", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebHotkeyLabel p1 = new WebHotkeyLabel ( getStyleId (), "Shift" );
            final WebHotkeyLabel p2 = new WebHotkeyLabel ( getStyleId (), "Escape" );
            final WebHotkeyLabel p3 = new WebHotkeyLabel ( getStyleId (), "Shift+Escape" );
            return CollectionUtils.asList ( p1, p2, p3 );
        }
    }

    /**
     * Code-based hotkey label preview.
     */
    protected class CodeHotkeyLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public CodeHotkeyLabel ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebHotkeyLabelExample.this, "code", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebHotkeyLabel p1 = new WebHotkeyLabel ( getStyleId (), KeyEvent.VK_SPACE );
            final WebHotkeyLabel p3 = new WebHotkeyLabel ( getStyleId (), KeyEvent.VK_SPACE, KeyEvent.CTRL_MASK );
            return CollectionUtils.asList ( p1, p3 );
        }
    }

    /**
     * HotkeyData-based hotkey label preview.
     */
    protected class DataHotkeyLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public DataHotkeyLabel ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebHotkeyLabelExample.this, "data", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebHotkeyLabel p1 = new WebHotkeyLabel ( getStyleId (), Hotkey.ALT );
            final WebHotkeyLabel p2 = new WebHotkeyLabel ( getStyleId (), Hotkey.F4 );
            final WebHotkeyLabel p3 = new WebHotkeyLabel ( getStyleId (), Hotkey.ALT_F4 );
            return CollectionUtils.asList ( p1, p2, p3 );
        }
    }
}