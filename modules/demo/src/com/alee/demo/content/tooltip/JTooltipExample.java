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

import com.alee.demo.api.*;
import com.alee.laf.label.WebLabel;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class JTooltipExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "jtooltip";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "tooltip";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final BasicTooltip e1 = new BasicTooltip ( FeatureState.updated, StyleId.tooltip );
        return CollectionUtils.<Preview>asList ( e1 );
    }

    /**
     * Simple tooltip preview.
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
            super ( JTooltipExample.this, "basic", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebLabel label = new WebLabel ( getPreviewLanguagePrefix () + "text" );
            label.setToolTipText ( LM.get ( getPreviewLanguagePrefix () + "tip" ) );
            return CollectionUtils.asList ( label );
        }
    }
}