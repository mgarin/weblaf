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

import com.alee.demo.api.example.*;
import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.image.WebImage;
import com.alee.laf.text.WebPasswordField;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebPasswordFieldExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "webpasswordfield";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "passwordfield";
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
                new InputPromptField ( StyleId.passwordfield ),
                new LeadingComponentField ( StyleId.passwordfield ),
                new TrailingComponentField ( StyleId.passwordfield )
        );
    }

    /**
     * Password field with input prompt preview.
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
            super ( WebPasswordFieldExample.this, "inputprompt", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebPasswordField textField = new WebPasswordField ( getStyleId (), 20 );
            textField.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            return CollectionUtils.asList ( textField );
        }
    }

    /**
     * Password field with leading component preview.
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
            super ( WebPasswordFieldExample.this, "leading", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebPasswordField textField = new WebPasswordField ( getStyleId (), 18 );
            textField.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            textField.setLeadingComponent ( new WebImage ( DemoStyles.leadingImage, DemoIcons.key16 ) );
            return CollectionUtils.asList ( textField );
        }
    }

    /**
     * Password field with trailing component preview.
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
            super ( WebPasswordFieldExample.this, "trailing", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebPasswordField textField = new WebPasswordField ( getStyleId (), 18 );
            textField.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            textField.setTrailingComponent ( new WebImage ( DemoStyles.trailingImage, DemoIcons.github16 ) );
            return CollectionUtils.asList ( textField );
        }
    }
}