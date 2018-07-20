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

import com.alee.demo.api.example.*;
import com.alee.extended.colorchooser.WebColorChooserField;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebColorChooserFieldExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "webcolorchooserfield";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "colorchooserfield";
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
                new ColorChooserField ( StyleId.colorchooserfield )
        );
    }

    /**
     * Color chooser field preview.
     */
    protected class ColorChooserField extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public ColorChooserField ( final StyleId styleId )
        {
            super ( WebColorChooserFieldExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebColorChooserField colorChooserField = new WebColorChooserField ( getStyleId (), Color.RED );
            return CollectionUtils.asList ( colorChooserField );
        }
    }
}