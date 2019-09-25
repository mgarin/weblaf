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

package com.alee.demo.content.text.field;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.image.WebImage;
import com.alee.laf.text.WebFormattedTextField;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebFormattedTextFieldExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "webformattedtextfield";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "formattedtextfield";
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
                new BasicFormattedField ( "currency", "####", null, DemoIcons.dollar16, 10 ),
                new BasicFormattedField ( "phone", "# (###) ###-##-##", null, DemoIcons.phone16, 12 ),
                new BasicFormattedField ( "fraction", "##.##", 77.77, DemoIcons.hourglass16, 5 )
        );
    }

    /**
     * Basic formatted field preview.
     */
    protected class BasicFormattedField extends AbstractStylePreview
    {
        /**
         * Field settings.
         */
        private final String mask;
        private final Object value;
        private final Icon icon;
        private final int columns;

        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param mask    formatter mask
         * @param value   field value
         * @param icon    field icon
         * @param columns editor columns
         */
        public BasicFormattedField ( final String id, final String mask, final Object value, final Icon icon, final int columns )
        {
            super ( WebFormattedTextFieldExample.this, id, FeatureState.updated, StyleId.formattedtextfield );
            this.mask = mask;
            this.value = value;
            this.icon = icon;
            this.columns = columns;
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebFormattedTextField textField = new WebFormattedTextField ( createFormatter ( mask ) );
            textField.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            textField.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            textField.setLeadingComponent ( new WebImage ( DemoStyles.leadingImage, icon ) );
            textField.setColumns ( columns );
            textField.setValue ( value );
            return CollectionUtils.asList ( textField );
        }
    }

    /**
     * Returns sample formatter.
     *
     * @param mask formatter mask
     * @return sample formatter
     */
    protected static MaskFormatter createFormatter ( final String mask )
    {
        try
        {
            return new MaskFormatter ( mask );
        }
        catch ( final ParseException e )
        {
            throw new RuntimeException ( "Unable to parse formatter mask: " + mask, e );
        }
    }
}