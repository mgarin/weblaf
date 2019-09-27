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

import com.alee.api.annotations.NotNull;
import com.alee.api.data.BoxOrientation;
import com.alee.demo.api.example.*;
import com.alee.demo.skin.DemoIcons;
import com.alee.extended.accordion.WebAccordion;
import com.alee.extended.collapsible.WebCollapsiblePane;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.text.LoremIpsum;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebAccordionExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "accordion";
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
                new AccordionPreview ( BoxOrientation.top ),
                new AccordionPreview ( BoxOrientation.bottom ),
                new AccordionPreview ( BoxOrientation.left ),
                new AccordionPreview ( BoxOrientation.right )
        );
    }

    /**
     * {@link WebCollapsiblePane} preview.
     */
    protected class AccordionPreview extends AbstractStylePreview
    {
        /**
         * Header position, also defines accordion orientation.
         */
        protected final BoxOrientation headerPosition;

        /**
         * Constructs new style preview.
         *
         * @param headerPosition header position, also defines accordion orientation
         */
        public AccordionPreview ( final BoxOrientation headerPosition )
        {
            super ( WebAccordionExample.this, headerPosition.name (), FeatureState.release, StyleId.accordion );
            this.headerPosition = headerPosition;
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebAccordion accordion = new WebAccordion ( getStyleId (), headerPosition );
            if ( headerPosition == BoxOrientation.left || headerPosition == BoxOrientation.right )
            {
                accordion.setMaximumExpandedPaneCount ( 2 );
            }

            final String titleKey = getExampleLanguageKey ( "data.title" );
            accordion.addPane ( DemoIcons.fire16, titleKey, createContent () );
            accordion.addPane ( DemoIcons.dollar16, titleKey, createContent () );
            accordion.addPane ( DemoIcons.brush16, titleKey, createContent () );

            return CollectionUtils.asList ( accordion );
        }

        /**
         * Returns single collapsible pane content.
         *
         * @return single collapsible pane content
         */
        protected JComponent createContent ()
        {
            final String text = new LoremIpsum ().getParagraphs ( 3 );
            final WebTextArea textArea = new WebTextArea ( StyleId.textareaNonOpaque, text );
            textArea.setLineWrap ( true );
            textArea.setWrapStyleWord ( true );
            final WebScrollPane scrollPane = new WebScrollPane ( StyleId.scrollpaneTransparentHovering, textArea );
            scrollPane.setPreferredSize ( 150, 100 );
            return scrollPane;
        }
    }
}