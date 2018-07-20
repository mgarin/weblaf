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

package com.alee.demo.content.container;

import com.alee.demo.api.example.*;
import com.alee.extended.panel.WebCollapsiblePane;
import com.alee.laf.text.WebTextArea;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 * @author Michka Popoff
 */
public class WebCollapsiblePaneExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "collapsiblepane";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "collapsiblepane";
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
                new BasicCollapsiblePanePreview ( WebCollapsiblePane.TOP ),
                new BasicCollapsiblePanePreview ( WebCollapsiblePane.LEFT ),
                new BasicCollapsiblePanePreview ( WebCollapsiblePane.RIGHT ),
                new BasicCollapsiblePanePreview ( WebCollapsiblePane.BOTTOM )
        );
    }

    /**
     * Right WebCollapsiblePane Example
     */
    protected class BasicCollapsiblePanePreview extends AbstractStylePreview
    {
        /**
         * Title pane position.
         */
        private final int titlePosition;

        /**
         * Constructs new style preview.
         *
         * @param titlePosition title pane position
         */
        public BasicCollapsiblePanePreview ( final int titlePosition )
        {
            super ( WebCollapsiblePaneExample.this, "right", FeatureState.beta, StyleId.collapsiblepane );
            this.titlePosition = titlePosition;
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final String textKey = getExampleLanguageKey ( "data.text" );
            final WebTextArea textArea = new WebTextArea ();
            textArea.setLanguage ( textKey );
            textArea.setPreferredSize ( 150, 100 );

            final String titleKey = getExampleLanguageKey ( "data.title" );
            final WebCollapsiblePane collapsiblePane = new WebCollapsiblePane ( titleKey, textArea );
            collapsiblePane.setTitlePanePosition ( titlePosition );

            return CollectionUtils.asList ( collapsiblePane );
        }
    }
}