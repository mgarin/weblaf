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

import com.alee.demo.api.*;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebEditorPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebEditorPaneExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "webeditorpane";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "editorpane";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final PromptPane e1 = new PromptPane ( "prompt", StyleId.editorpane );
        final DecoratedPane e2 = new DecoratedPane ( "decorated", StyleId.editorpaneDecorated );
        return CollectionUtils.<Preview>asList ( e1, e2 );
    }

    /**
     * Text pane input prompt preview.
     */
    protected class PromptPane extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public PromptPane ( final String id, final StyleId styleId )
        {
            super ( WebEditorPaneExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebEditorPane textPane = new WebEditorPane ( getStyleId () );
            textPane.setInputPrompt ( getPreviewLanguagePrefix () + "prompt" );
            return CollectionUtils.asList ( new WebScrollPane ( textPane ).setPreferredSize ( 200, 100 ) );
        }
    }

    /**
     * Decorated text pane preview.
     */
    protected class DecoratedPane extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         */
        public DecoratedPane ( final String id, final StyleId styleId )
        {
            super ( WebEditorPaneExample.this, id, FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebEditorPane textPane = new WebEditorPane ( getStyleId (), "text/html", createHtmlText () );
            return CollectionUtils.asList ( textPane );
        }
    }

    /**
     * Returns html text.
     *
     * @return html text
     */
    protected String createHtmlText ()
    {
        return "<html><b>Sample HTML content</b><br>" + "<i>with multiple lines</i><br>" +
                "<font color=red>and custom font style</font></html>";
    }
}