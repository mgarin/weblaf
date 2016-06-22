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

import com.alee.demo.api.*;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebStyledLabelExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "webstyledlabel";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "label";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final StyledLabel e1 = new StyledLabel ( "basic", FeatureState.updated, StyleId.styledlabel );
        final StyledLabel e2 = new StyledLabel ( "shadow", FeatureState.updated, StyleId.labelShadow );
        final StyledLabel e3 = new StyledLabel ( "ccw", FeatureState.release, StyleId.labelVerticalCCW );
        final StyledLabel e4 = new StyledLabel ( "cw", FeatureState.release, StyleId.labelVerticalCW );
        return CollectionUtils.<Preview>asList ( e1, e2, e3, e4 );
    }

    /**
     * Button preview.
     */
    protected class StyledLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public StyledLabel ( final String id, final FeatureState featureState, final StyleId styleId )
        {
            super ( WebStyledLabelExample.this, id, featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final String labelText = "{Custom:b} {styled:c(120,80,200)}{2:sup} text" + "\n" + "And {another:b} row";
            final WebStyledLabel label = new WebStyledLabel ( getStyleId (), labelText );

            final String iconText = "{Iconed:b} text";
            final WebStyledLabel icon = new WebStyledLabel ( getStyleId (), iconText, WebLookAndFeel.getIcon ( 16 ) );

            return CollectionUtils.asList ( label, icon );
        }
    }
}