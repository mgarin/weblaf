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
import com.alee.api.annotations.Nullable;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JFormattedTextFieldExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jformattedtextfield";
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
        return FeatureType.swing;
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Formatted Text Fields", "formattedtextfield" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicFormattedField ( "currency", StyleId.formattedtextfield, "####$", 1024 ),
                new BasicFormattedField ( "phone", StyleId.formattedtextfield, "# (###) ###-##-##", null ),
                new BasicFormattedField ( "fraction", StyleId.formattedtextfield, "##.##", 77.77 )
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
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