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
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class JProgressBarExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "jprogressbar";
    }

    @Override
    protected String getStyleFileName ()
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
        final BasicProgress e1 = new BasicProgress ( StyleId.progressbar );
        final IndeterminateProgress e2 = new IndeterminateProgress ( StyleId.progressbar );
        final VerticalProgress e3 = new VerticalProgress ( StyleId.progressbar );
        return CollectionUtils.<Preview>asList ( e1, e2, e3 );
    }

    /**
     * Progress bar preview.
     */
    protected class BasicProgress extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public BasicProgress ( final StyleId styleId )
        {
            super ( JProgressBarExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JProgressBar progressBar = new JProgressBar ( JProgressBar.HORIZONTAL, 0, 100 );
            progressBar.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            progressBar.setValue ( 75 );
            return CollectionUtils.asList ( progressBar );
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
            super ( JProgressBarExample.this, "indeterminate", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JProgressBar progressBar = new JProgressBar ( JProgressBar.HORIZONTAL );
            progressBar.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            progressBar.setIndeterminate ( true );
            return CollectionUtils.asList ( progressBar );
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
            super ( JProgressBarExample.this, "vertical", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JProgressBar determinate = new JProgressBar ( JProgressBar.VERTICAL, 0, 100 );
            determinate.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            determinate.setValue ( 75 );

            final JProgressBar indeterminate = new JProgressBar ( JProgressBar.VERTICAL );
            indeterminate.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            indeterminate.setIndeterminate ( true );

            return CollectionUtils.asList ( determinate, indeterminate );
        }
    }
}