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

import com.alee.demo.api.*;
import com.alee.demo.icons.DemoIcons;
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

public class WebFormattedTextFieldExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "webformattedtextfield";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "formattedtextfield";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final BasicFormattedField e1 = new BasicFormattedField ( "currency", "####", null, DemoIcons.dollar16 );
        final BasicFormattedField e2 = new BasicFormattedField ( "phone", "# (###) ###-##-##", null, DemoIcons.phone16 );
        final BasicFormattedField e3 = new BasicFormattedField ( "fraction", "##.##", 77.77, DemoIcons.hourglass16 );
        return CollectionUtils.<Preview>asList ( e1, e2, e3 );
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

        /**
         * Constructs new style preview.
         *
         * @param id    preview ID
         * @param mask  formatter mask
         * @param value field value
         * @param icon  field icon
         */
        public BasicFormattedField ( final String id, final String mask, final Object value, final Icon icon )
        {
            super ( WebFormattedTextFieldExample.this, id, FeatureState.updated, StyleId.formattedtextfield );
            this.mask = mask;
            this.value = value;
            this.icon = icon;
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebFormattedTextField textField = new WebFormattedTextField ( createFormatter ( mask ) );
            textField.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            textField.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            textField.setLeadingComponent ( new WebImage ( icon ).setMargin ( 0, 0, 0, 4 ) );
            textField.setColumns ( 20 );
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