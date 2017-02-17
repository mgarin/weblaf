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
import java.awt.*;
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
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final LeftWebCollapsiblePane e1 = new LeftWebCollapsiblePane ( FeatureState.beta, StyleId.collapsiblepane );
        final RightWebCollapsiblePane e2 = new RightWebCollapsiblePane ( FeatureState.beta, StyleId.collapsiblepane );
        final TopWebCollapsiblePane e3 = new TopWebCollapsiblePane ( FeatureState.beta, StyleId.collapsiblepane );
        final BottomWebCollapsiblePane e4 = new BottomWebCollapsiblePane ( FeatureState.beta, StyleId.collapsiblepane );
        return CollectionUtils.<Preview>asList ( e1, e2, e3, e4 );
    }

    /**
     * Left WebCollapsiblePane Example
     */
    protected class LeftWebCollapsiblePane extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param id           preview style ID
         */
        public LeftWebCollapsiblePane ( final FeatureState featureState, final StyleId id )
        {
            super ( WebCollapsiblePaneExample.this, "left", featureState, id );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebCollapsiblePane e1 = new WebCollapsiblePane ( createCustomContent () );
            e1.setTitlePanePosition ( WebCollapsiblePane.LEFT );
            return CollectionUtils.asList ( e1 );
        }
    }

    /**
     * Right WebCollapsiblePane Example
     */
    protected class RightWebCollapsiblePane extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param id           preview style ID
         */
        public RightWebCollapsiblePane ( final FeatureState featureState, final StyleId id )
        {
            super ( WebCollapsiblePaneExample.this, "right", featureState, id );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebCollapsiblePane e1 = new WebCollapsiblePane ( createCustomContent () );
            e1.setTitlePanePosition ( WebCollapsiblePane.RIGHT );
            return CollectionUtils.asList ( e1 );
        }
    }

    /**
     * Top WebCollapsiblePane Example
     */
    protected class TopWebCollapsiblePane extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param id           preview style ID
         */
        public TopWebCollapsiblePane ( final FeatureState featureState, final StyleId id )
        {
            super ( WebCollapsiblePaneExample.this, "top", featureState, id );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebCollapsiblePane e1 = new WebCollapsiblePane ( createCustomContent () );
            e1.setTitlePanePosition ( WebCollapsiblePane.TOP );
            return CollectionUtils.asList ( e1 );
        }
    }

    /**
     * Bottom WebCollapsiblePane Example
     */
    protected class BottomWebCollapsiblePane extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param id           preview style ID
         */
        public BottomWebCollapsiblePane ( final FeatureState featureState, final StyleId id )
        {
            super ( WebCollapsiblePaneExample.this, "bottom", featureState, id );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebCollapsiblePane e1 = new WebCollapsiblePane ( createCustomContent () );
            e1.setTitlePanePosition ( WebCollapsiblePane.BOTTOM );
            return CollectionUtils.asList ( e1 );
        }
    }

    private static WebTextArea createCustomContent ( )
    {
        // Content text area
        final WebTextArea textArea = new WebTextArea ( );
        textArea.setText ( "Sample\nmultiline\ntext" );
        textArea.setPreferredSize ( new Dimension ( 150, 100 ) );

        return textArea;
    }
}