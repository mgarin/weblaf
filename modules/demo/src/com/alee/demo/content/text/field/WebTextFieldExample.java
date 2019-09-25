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
import com.alee.laf.text.WebTextField;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebTextFieldExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "webtextfield";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "textfield";
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
                new InputPromptField ( StyleId.textfield ),
                new LeadingComponentField ( StyleId.textfield ),
                new TrailingComponentField ( StyleId.textfield )
        );
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTextField textField = new WebTextField ( getStyleId (), 18 );
            textField.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            textField.setLeadingComponent ( new WebImage ( DemoStyles.leadingImage, DemoIcons.key16 ) );
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTextField textField = new WebTextField ( getStyleId (), 18 );
            textField.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            textField.setTrailingComponent ( new WebImage ( DemoStyles.trailingImage, DemoIcons.github16 ) );
            return CollectionUtils.asList ( textField );
        }
    }
}