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

package com.alee.demo.content.tooltip;

import com.alee.demo.api.example.*;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebCustomTooltipExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "webcustomtooltip";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "customtooltip";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        // todo Change styleId to something that com.alee.managers.tooltip.WebCustomTooltip will actually use
        return CollectionUtils.<Preview>asList (
                new BasicTooltip ( FeatureState.updated, StyleId.customtooltip ),
                new InstantTooltip ( FeatureState.updated, StyleId.customtooltip ),
                new MultipleTooltips ( FeatureState.updated, StyleId.customtooltip ),
                new HotkeyTooltip ( FeatureState.updated, StyleId.customtooltip ),
                new OneTimeTooltip ( FeatureState.updated, StyleId.customtooltip ),
                new CustomTooltip ( FeatureState.updated, StyleId.customtooltip )
        );
    }

    /**
     * Custom tooltip preview.
     */
    protected class BasicTooltip extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public BasicTooltip ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebCustomTooltipExample.this, "basic", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebLabel label = new WebLabel ( getPreviewLanguagePrefix () + "text" );
            label.setToolTip ( getPreviewLanguagePrefix () + "tip" );
            return CollectionUtils.asList ( label );
        }
    }

    /**
     * Instant tooltip preview.
     */
    protected class InstantTooltip extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public InstantTooltip ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebCustomTooltipExample.this, "instant", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebLabel label = new WebLabel ( getPreviewLanguagePrefix () + "text" );
            label.setToolTip ( getPreviewLanguagePrefix () + "tip", TooltipWay.down, 0 );
            return CollectionUtils.asList ( label );
        }
    }

    /**
     * Multiple tooltips preview.
     */
    protected class MultipleTooltips extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public MultipleTooltips ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebCustomTooltipExample.this, "multiple", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebLabel label = new WebLabel ( getPreviewLanguagePrefix () + "text" );
            label.addToolTip ( getPreviewLanguagePrefix () + "tip.up", TooltipWay.up );
            label.addToolTip ( getPreviewLanguagePrefix () + "tip.leading", TooltipWay.leading );
            label.addToolTip ( getPreviewLanguagePrefix () + "tip.trailing", TooltipWay.trailing );
            label.addToolTip ( getPreviewLanguagePrefix () + "tip.down", TooltipWay.down );
            return CollectionUtils.asList ( label );
        }
    }

    /**
     * Hotkey tooltip preview.
     */
    protected class HotkeyTooltip extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public HotkeyTooltip ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebCustomTooltipExample.this, "hotkey", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebButton button = new WebButton ();
            button.setLanguage ( getPreviewLanguagePrefix () + "text", 0 );
            button.addHotkey ( Hotkey.CTRL_D );
            button.setToolTip ( getPreviewLanguagePrefix () + "tip" );
            button.addActionListener ( new ActionListener ()
            {
                private int counter = 0;

                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    counter++;
                    button.updateLanguage ( counter );
                }
            } );
            return CollectionUtils.asList ( button );
        }
    }

    /**
     * One-time tooltip preview.
     */
    protected class OneTimeTooltip extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public OneTimeTooltip ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebCustomTooltipExample.this, "onetime", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebButton button = new WebButton ( getPreviewLanguagePrefix () + "text" );
            button.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    TooltipManager.showOneTimeTooltip ( button, null, getPreviewLanguagePrefix () + "tip", TooltipWay.down );
                }
            } );
            return CollectionUtils.asList ( button );
        }
    }

    /**
     * Tooltip with custom content preview.
     */
    protected class CustomTooltip extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public CustomTooltip ( final FeatureState featureState, final StyleId styleId )
        {
            super ( WebCustomTooltipExample.this, "custom", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebLabel label = new WebLabel ( getPreviewLanguagePrefix () + "text" );
            final WebStyledLabel tooltipContent = new WebStyledLabel ( getPreviewLanguagePrefix () + "tip" );
            tooltipContent.setForeground ( Color.WHITE );
            label.setToolTip ( tooltipContent );
            return CollectionUtils.asList ( label );
        }
    }
}