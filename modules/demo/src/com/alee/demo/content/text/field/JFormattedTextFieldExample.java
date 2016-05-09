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
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class JFormattedTextFieldExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "jformattedtextfield";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "formattedtextfield";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final BasicFormattedField e1 = new BasicFormattedField ( "currency", StyleId.formattedtextfield, "####$", 1024 );
        final BasicFormattedField e2 = new BasicFormattedField ( "phone", StyleId.formattedtextfield, "# (###) ###-##-##", null );
        final BasicFormattedField e3 = new BasicFormattedField ( "fraction", StyleId.formattedtextfield, "##.##", 77.77 );
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

        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         * @param mask    formatter mask
         * @param value   field value
         */
        public BasicFormattedField ( final String id, final StyleId styleId, final String mask, final Object value )
        {
            super ( JFormattedTextFieldExample.this, id, FeatureState.updated, styleId );
            this.mask = mask;
            this.value = value;
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JFormattedTextField textField = new JFormattedTextField ( createFormatter ( mask ) );
            textField.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
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