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

package com.alee.demo.content.progress;

import com.alee.demo.api.*;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class ProgressBarExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "progressbar";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final SimpleProgress e1 = new SimpleProgress ( StyleId.progressbar );
        final IndeterminateProgress e2 = new IndeterminateProgress ( StyleId.progressbar );
        final VerticalProgress e3 = new VerticalProgress ( StyleId.progressbar );
        return CollectionUtils.<Preview>asList ( e1, e2, e3 );
    }

    /**
     * Progress bar preview.
     */
    protected class SimpleProgress extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public SimpleProgress ( final StyleId styleId )
        {
            super ( ProgressBarExample.this, "simple", FeatureState.updated, styleId );
        }

        @Override
        protected JComponent createPreviewContent ( final StyleId id )
        {
            final WebProgressBar progressBar = new WebProgressBar ( getStyleId (), WebProgressBar.HORIZONTAL, 0, 100 );
            progressBar.setValue ( 75 );
            return progressBar;
        }
    }

    /**
     * Indeterminate progress bar preview.
     */
    protected class IndeterminateProgress extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public IndeterminateProgress ( final StyleId styleId )
        {
            super ( ProgressBarExample.this, "indeterminate", FeatureState.updated, styleId );
        }

        @Override
        protected JComponent createPreviewContent ( final StyleId id )
        {
            final WebProgressBar progressBar = new WebProgressBar ( getStyleId (), WebProgressBar.HORIZONTAL );
            progressBar.setIndeterminate ( true );
            return progressBar;
        }
    }

    /**
     * Vertical progress bar preview.
     */
    protected class VerticalProgress extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public VerticalProgress ( final StyleId styleId )
        {
            super ( ProgressBarExample.this, "vertical", FeatureState.updated, styleId );
        }

        @Override
        protected JComponent createPreviewContent ( final StyleId id )
        {
            final WebProgressBar determinate = new WebProgressBar ( getStyleId (), WebProgressBar.VERTICAL, 0, 100 );
            determinate.setValue ( 75 );

            final WebProgressBar indeterminate = new WebProgressBar ( getStyleId (), WebProgressBar.VERTICAL );
            indeterminate.setIndeterminate ( true );

            return new GroupPanel ( id, 8, determinate, indeterminate );
        }
    }
}