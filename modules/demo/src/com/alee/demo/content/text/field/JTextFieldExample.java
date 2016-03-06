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
import java.util.List;

/**
 * @author Mikle Garin
 */

public class JTextFieldExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "jtextfield";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "textfield";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final BasicField e1 = new BasicField ( "basic", StyleId.textfield );
        final BasicField e2 = new BasicField ( "undecorated", StyleId.textfieldUndecorated );
        final BasicField e3 = new BasicField ( "nofocus", StyleId.textfieldNoFocus );
        return CollectionUtils.<Preview>asList ( e1, e2, e3 );
    }

    /**
     * Basic field preview.
     */
    protected class BasicField extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public BasicField ( final String id, final StyleId styleId )
        {
            super ( JTextFieldExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JTextField textField = new JTextField ( "Sample text", 20 );
            textField.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( textField );
        }
    }
}