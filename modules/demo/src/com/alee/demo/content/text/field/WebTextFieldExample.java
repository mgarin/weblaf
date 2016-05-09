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
import com.alee.laf.text.WebTextField;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebTextFieldExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "webtextfield";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "textfield";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final InputPromptField e1 = new InputPromptField ( StyleId.textfield );
        final LeadingComponentField e2 = new LeadingComponentField ( StyleId.textfield );
        final TrailingComponentField e3 = new TrailingComponentField ( StyleId.textfield );
        return CollectionUtils.<Preview>asList ( e1, e2, e3 );
    }

    /**
     * Field with input prompt preview.
     */
    protected class InputPromptField extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public InputPromptField ( final StyleId styleId )
        {
            super ( WebTextFieldExample.this, "inputprompt", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebTextField textField = new WebTextField ( getStyleId (), 20 );
            textField.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            return CollectionUtils.asList ( textField );
        }
    }

    /**
     * Field with leading component preview.
     */
    protected class LeadingComponentField extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public LeadingComponentField ( final StyleId styleId )
        {
            super ( WebTextFieldExample.this, "leading", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebTextField textField = new WebTextField ( getStyleId (), 18 );
            textField.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            textField.setLeadingComponent ( new WebImage ( DemoIcons.key16 ).setMargin ( 0, 0, 0, 4 ) );
            return CollectionUtils.asList ( textField );
        }
    }

    /**
     * Field with trailing component preview.
     */
    protected class TrailingComponentField extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public TrailingComponentField ( final StyleId styleId )
        {
            super ( WebTextFieldExample.this, "trailing", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebTextField textField = new WebTextField ( getStyleId (), 18 );
            textField.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            textField.setTrailingComponent ( new WebImage ( DemoIcons.github16 ).setMargin ( 0, 4, 0, 0 ) );
            return CollectionUtils.asList ( textField );
        }
    }
}