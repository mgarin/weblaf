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

package com.alee.demo.content.text.area;

import com.alee.demo.api.example.*;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebTextAreaExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "webtextarea";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "textarea";
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
                new PromptArea ( "prompt", StyleId.textarea ),
                new DecoratedArea ( "decorated", StyleId.textareaDecorated )
        );
    }

    /**
     * Text area input prompt preview.
     */
    protected class PromptArea extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public PromptArea ( final String id, final StyleId styleId )
        {
            super ( WebTextAreaExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTextArea textArea = new WebTextArea ( getStyleId (), 3, 20 );
            textArea.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            return CollectionUtils.asList ( new WebScrollPane ( textArea ) );
        }
    }

    /**
     * Decorated text area preview.
     */
    protected class DecoratedArea extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public DecoratedArea ( final String id, final StyleId styleId )
        {
            super ( WebTextAreaExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTextArea textArea = new WebTextArea ( getStyleId (), "Sample\nmultiline\ntext", 3, 20 );
            return CollectionUtils.asList ( textArea );
        }
    }
}