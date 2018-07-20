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

import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.managers.animation.easing.Quadratic;
import com.alee.managers.animation.transition.*;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JProgressBarExample extends AbstractStylePreviewExample
{
    /**
     * Progress animator.
     */
    private QueueTransition<Integer> progressAnimator;

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
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Progress Bars", "progress" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        // Transition for progress animation
        progressAnimator = new QueueTransition<Integer> ( true );
        progressAnimator.add ( new TimedTransition<Integer> ( 0, 1000, new Quadratic.Out (), 2000L ) );
        progressAnimator.add ( new IdleTransition<Integer> ( 1000, 1000L ) );
        progressAnimator.add ( new TimedTransition<Integer> ( 1000, 0, new Quadratic.Out (), 2000L ) );
        progressAnimator.add ( new IdleTransition<Integer> ( 0, 1000L ) );

        // Progress bar examples
        return CollectionUtils.<Preview>asList (
                new BasicProgress ( StyleId.progressbar, null ),
                new BasicProgress ( StyleId.progressbar, "" ),
                new BasicProgress ( StyleId.progressbar, "Sample text" ),
                new IndeterminateProgress ( StyleId.progressbar, null ),
                new IndeterminateProgress ( StyleId.progressbar, "Sample text" ),
                new VerticalProgress ( StyleId.progressbar, null ),
                new VerticalProgress ( StyleId.progressbar, "" ),
                new VerticalProgress ( StyleId.progressbar, "Sample text" )
        );
    }

    /**
     * Ensures animation only occurs when preview becomes visible.
     */
    @Override
    protected void displayed ()
    {
        progressAnimator.play ();
    }

    /**
     * Ensures animation stops when preview is hidden.
     */
    @Override
    protected void hidden ()
    {
        progressAnimator.stop ();
    }

    /**
     * Progress bar preview.
     */
    protected class BasicProgress extends AbstractStylePreview
    {
        /**
         * Progress text.
         */
        private final String text;

        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         * @param text    progress text
         */
        public BasicProgress ( final StyleId styleId, final String text )
        {
            super ( JProgressBarExample.this, "basic", FeatureState.updated, styleId );
            this.text = text;
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JProgressBar progressBar = new JProgressBar ( JProgressBar.HORIZONTAL, 0, 1000 );
            setup ( progressBar, getStyleId (), text );

            return CollectionUtils.asList ( progressBar );
        }
    }

    /**
     * Indeterminate progress bar preview.
     */
    protected class IndeterminateProgress extends AbstractStylePreview
    {
        /**
         * Progress text.
         */
        private final String text;

        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         * @param text    progress text
         */
        public IndeterminateProgress ( final StyleId styleId, final String text )
        {
            super ( JProgressBarExample.this, "indeterminate", FeatureState.updated, styleId );
            this.text = text;
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JProgressBar progressBar = new JProgressBar ( JProgressBar.HORIZONTAL );
            progressBar.setIndeterminate ( true );
            setup ( progressBar, getStyleId (), text );

            return CollectionUtils.asList ( progressBar );
        }
    }

    /**
     * Vertical progress bar preview.
     */
    protected class VerticalProgress extends AbstractStylePreview
    {
        /**
         * Progress text.
         */
        private final String text;

        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         * @param text    progress text
         */
        public VerticalProgress ( final StyleId styleId, final String text )
        {
            super ( JProgressBarExample.this, "vertical", FeatureState.updated, styleId );
            this.text = text;
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JProgressBar determinate = new JProgressBar ( JProgressBar.VERTICAL, 0, 1000 );
            determinate.setValue ( 750 );
            setup ( determinate, getStyleId (), text );

            final JProgressBar indeterminate = new JProgressBar ( JProgressBar.VERTICAL );
            indeterminate.setIndeterminate ( true );
            setup ( indeterminate, getStyleId (), text );

            return CollectionUtils.asList ( determinate, indeterminate );
        }
    }

    /**
     * Configures progress bar.
     *
     * @param progressBar progress bar instance
     * @param styleId     progress bar style ID
     * @param text        progress bar text
     */
    protected void setup ( final JProgressBar progressBar, final StyleId styleId, final String text )
    {
        // Setting style ID
        progressBar.putClientProperty ( StyleId.STYLE_PROPERTY, styleId );

        // Configuring displayed text
        if ( text != null )
        {
            progressBar.setStringPainted ( true );
            if ( !TextUtils.isEmpty ( text ) )
            {
                progressBar.setString ( text );
            }
        }

        // Value animation for non-indeterminate prob
        if ( !progressBar.isIndeterminate () )
        {
            progressAnimator.addListener ( new TransitionAdapter<Integer> ()
            {
                @Override
                public void started ( final Transition transition, final Integer value )
                {
                    progressBar.setValue ( value );
                }

                @Override
                public void adjusted ( final Transition transition, final Integer value )
                {
                    progressBar.setValue ( value );
                }
            } );
        }
    }
}