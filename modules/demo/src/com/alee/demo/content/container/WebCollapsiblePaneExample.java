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
 * @author Michka Popoff
 */
public class WebCollapsiblePaneExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "collapsiblepane";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "collapsiblepane";
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
                new CollapsiblePanePreview ( BoxOrientation.top ),
                new CollapsiblePanePreview ( BoxOrientation.left ),
                new CollapsiblePanePreview ( BoxOrientation.right ),
                new CollapsiblePanePreview ( BoxOrientation.bottom )
        );
    }

    /**
     * {@link WebCollapsiblePane} preview.
     */
    protected class CollapsiblePanePreview extends AbstractStylePreview
    {
        /**
         * Header position.
         */
        protected final BoxOrientation headerPosition;

        /**
         * Constructs new style preview.
         *
         * @param headerPosition header position
         */
        public CollapsiblePanePreview ( final BoxOrientation headerPosition )
        {
            super ( WebCollapsiblePaneExample.this, headerPosition.name (), FeatureState.release, StyleId.collapsiblepane );
            this.headerPosition = headerPosition;
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final Icon icon = DemoIcons.fire16;
            final String titleKey = getExampleLanguageKey ( "data.title" );
            final WebCollapsiblePane collapsiblePane = new WebCollapsiblePane ( getStyleId (), icon, titleKey, createContent () );
            collapsiblePane.setHeaderPosition ( headerPosition );
            return CollectionUtils.asList ( collapsiblePane );
        }

        /**
         * Returns {@link JComponent} content for {@link WebCollapsiblePane}.
         * New {@link JComponent} is created upon every call to avoid any UI issues.
         *
         * @return {@link JComponent} content for {@link WebCollapsiblePane}
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