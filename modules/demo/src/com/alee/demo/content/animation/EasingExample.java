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

package com.alee.demo.content.animation;

import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.canvas.WebCanvas;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.AlignLayout;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.animation.easing.*;
import com.alee.managers.animation.transition.Transition;
import com.alee.managers.animation.transition.TransitionAdapter;
import com.alee.utils.CollectionUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.IntTextDocument;
import com.alee.utils.swing.extensions.DocumentEventRunnable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class EasingExample extends AbstractPreviewExample
{
    @Override
    public String getId ()
    {
        return "easing";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.utility;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new WebLafWikiPage ( "How to use Easing" );
    }

    @Override
    protected LayoutManager createPreviewLayout ()
    {
        return new AlignLayout ();
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new EasingGraphPreview ()
        );
    }

    /**
     * Easing graph preview.
     */
    protected class EasingGraphPreview extends AbstractPreview
    {
        /**
         * Constructs new preview.
         */
        public EasingGraphPreview ()
        {
            super ( EasingExample.this, "graph", FeatureState.release );
        }

        @Override
        protected JComponent createPreview ( final List<Preview> previews, final int index )
        {
            // Easing algorithm graph
            final EasingViewer easingGraph = new EasingViewer ();

            // Duration chooser
            final WebTextField durationChooser = new WebTextField ( new IntTextDocument (), "1000", 4 );
            durationChooser.setHorizontalAlignment ( WebTextField.CENTER );
            durationChooser.setBoldFont ();
            durationChooser.setLeadingComponent ( new WebStyledLabel ( DemoStyles.fieldInner, getExampleLanguagePrefix () + "duration" ) );
            durationChooser.setTrailingComponent ( new WebStyledLabel ( DemoStyles.fieldInner, "weblaf.time.units.short.millisecond" ) );

            // Easing type chooser
            final WebList easingChooser = new WebList ( getAvailableEasingsAlgorithms () );
            easingChooser.setMultipleSelectionAllowed ( false );
            easingChooser.setSelectedIndex ( 0 );

            // East bar
            final WebPanel eastBar = new WebPanel ( DemoStyles.easingEastBar, new VerticalFlowLayout ( 0, 15, true, true ) );
            eastBar.add ( durationChooser );
            eastBar.add ( new WebScrollPane ( easingChooser ) );

            // South bar
            final WebPanel southBar = new WebPanel ( DemoStyles.easingSouthBar, new HorizontalFlowLayout ( 25, false ) );
            for ( final EasingPreview.Type type : EasingPreview.Type.values () )
            {
                southBar.add ( new EasingPreview ( easingGraph, type ) );
            }

            // Preview panel
            final WebPanel preview = new WebPanel ( DemoStyles.previewPanel, new BorderLayout ( 0, 0 ) );
            preview.add ( easingGraph, BorderLayout.WEST );
            preview.add ( eastBar, BorderLayout.CENTER );
            preview.add ( southBar, BorderLayout.SOUTH );

            // Update actions
            final Runnable easingUpdater = new Runnable ()
            {
                @Override
                public void run ()
                {
                    try
                    {
                        final long duration = Long.parseLong ( durationChooser.getText () );
                        final Easing easing = ( Easing ) easingChooser.getSelectedValue ();
                        easingGraph.preview ( easing, duration > 100L ? duration : 100L );
                    }
                    catch ( final Exception ignored )
                    {
                        // Ignored
                    }
                }
            };
            durationChooser.onChange ( new DocumentEventRunnable<WebTextField> ()
            {
                @Override
                public void run ( final WebTextField component, final DocumentEvent event )
                {
                    easingUpdater.run ();
                }
            } );
            final MouseAdapter mouseAdapter = new MouseAdapter ()
            {
                private int index = -1;

                @Override
                public void mousePressed ( final MouseEvent e )
                {
                    if ( SwingUtils.isLeftMouseButton ( e ) )
                    {
                        index = easingChooser.getSelectedIndex ();
                        easingUpdater.run ();
                    }
                }

                @Override
                public void mouseDragged ( final MouseEvent e )
                {
                    if ( SwingUtils.isLeftMouseButton ( e ) )
                    {
                        final int newIndex = easingChooser.getSelectedIndex ();
                        if ( newIndex != index )
                        {
                            index = newIndex;
                            easingUpdater.run ();
                        }
                    }
                }

                @Override
                public void mouseReleased ( final MouseEvent e )
                {
                    if ( SwingUtils.isLeftMouseButton ( e ) )
                    {
                        index = -1;
                    }
                }
            };
            easingChooser.addMouseListener ( mouseAdapter );
            easingChooser.addMouseMotionListener ( mouseAdapter );
            easingUpdater.run ();

            return preview;
        }

        @Override
        public void applyEnabled ( final boolean enabled )
        {
            /**
             * Not available for this example.
             */
        }
    }

    /**
     * Easing algorithm usage example.
     */
    protected static class EasingPreview extends WebCanvas
    {
        /**
         * Example icon.
         */
        private static final ImageIcon icon = WebLookAndFeel.getIcon ( 64 );

        /**
         * Easing preview graph.
         */
        protected final EasingViewer graph;

        /**
         * Easing preview example type.
         */
        protected final Type type;

        /**
         * Constructs new easing algorithm preview example.
         *
         * @param graph easing preview graph
         * @param type  easing preview example type
         */
        public EasingPreview ( final EasingViewer graph, final Type type )
        {
            super ();
            this.graph = graph;
            this.type = type;
            graph.addListener ( new TransitionAdapter<Integer> ()
            {
                @Override
                public void adjusted ( final Transition transition, final Integer value )
                {
                    EasingPreview.this.repaint ();
                }
            } );
        }

        @Override
        protected void paintComponent ( final Graphics g )
        {
            // Leave default painting untouched
            super.paintComponent ( g );

            // Using Graphics2D API
            final Graphics2D g2d = ( Graphics2D ) g;

            // Painting preview
            final Object iq = GraphicsUtils.setupImageQuality ( g2d );
            switch ( type )
            {
                case zoom:
                    paintZoom ( g2d );
                    break;

                case rotation:
                    paintRotation ( g2d );
                    break;

                case flip:
                    paintFlip ( g2d );
                    break;

                case fade:
                    paintFade ( g2d );
                    break;

                case slide:
                    paintSlide ( g2d );
                    break;
            }
            GraphicsUtils.restoreImageQuality ( g2d, iq );
        }

        /**
         * Paints zoom effect preview.
         *
         * @param g2d graphics context
         */
        protected void paintZoom ( final Graphics2D g2d )
        {
            final double zoom = graph.getEasedProgress () * 0.5;
            final int w = ( int ) Math.round ( icon.getIconWidth () * ( 0.5d + zoom ) );
            final int h = ( int ) Math.round ( icon.getIconHeight () * ( 0.5d + zoom ) );
            g2d.drawImage ( icon.getImage (), getWidth () / 2 - w / 2, getHeight () / 2 - h / 2, w, h, null );
        }

        /**
         * Paints rotation effect preview.
         *
         * @param g2d graphics context
         */
        protected void paintRotation ( final Graphics2D g2d )
        {
            final int w = icon.getIconWidth ();
            final int h = icon.getIconHeight ();
            final double rotation = Math.PI * 2 * graph.getEasedProgress ();
            g2d.rotate ( rotation, getWidth () / 2, getHeight () / 2 );
            g2d.drawImage ( icon.getImage (), getWidth () / 2 - w / 2, getHeight () / 2 - h / 2, null );
        }

        /**
         * Paints flip effect preview.
         *
         * @param g2d graphics context
         */
        protected void paintFlip ( final Graphics2D g2d )
        {
            final int w = icon.getIconWidth ();
            final int h = icon.getIconHeight ();
            final double rotation = Math.PI * 2 * graph.getEasedProgress ();
            final int ih = ( int ) Math.round ( h * Math.cos ( rotation ) );
            g2d.drawImage ( icon.getImage (), getWidth () / 2 - w / 2, getHeight () / 2 - ih / 2, w, ih, null );
        }

        /**
         * Paints fade effect preview.
         *
         * @param g2d graphics context
         */
        protected void paintFade ( final Graphics2D g2d )
        {
            final int w = icon.getIconWidth ();
            final int h = icon.getIconHeight ();
            final float opacity = MathUtils.limit ( 0f, ( float ) graph.getEasedProgress (), 1f );
            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity );
            g2d.drawImage ( icon.getImage (), getWidth () / 2 - w / 2, getHeight () / 2 - h / 2, null );
            GraphicsUtils.restoreComposite ( g2d, oc );
        }

        /**
         * Paints slide effect preview.
         *
         * @param g2d graphics context
         */
        protected void paintSlide ( final Graphics2D g2d )
        {
            final int w = icon.getIconWidth ();
            final int h = icon.getIconHeight ();
            final int location = ( int ) Math.round ( ( h + getHeight () / 2 - h / 2 ) * graph.getEasedProgress () );
            g2d.drawImage ( icon.getImage (), getWidth () / 2 - w / 2, -h + location, null );
        }

        @Override
        public Dimension getPreferredSize ()
        {
            return new Dimension ( icon.getIconWidth () * 3 / 2, icon.getIconHeight () * 3 / 2 );
        }

        /**
         * Effect type for easing algorithm preview.
         */
        public enum Type
        {
            zoom,
            rotation,
            flip,
            fade,
            slide
        }
    }

    /**
     * Returns full list of available easing algorithms.
     *
     * @return full list of available easing algorithms
     */
    protected static List<Easing> getAvailableEasingsAlgorithms ()
    {
        final List<Easing> easing = new ArrayList<Easing> ( 50 );
        easing.add ( new Linear () );
        easing.add ( new Sinusoidal.In () );
        easing.add ( new Sinusoidal.Out () );
        easing.add ( new Sinusoidal.InOut () );
        easing.add ( new Quadratic.In () );
        easing.add ( new Quadratic.Out () );
        easing.add ( new Quadratic.InOut () );
        easing.add ( new Cubic.In () );
        easing.add ( new Cubic.Out () );
        easing.add ( new Cubic.InOut () );
        easing.add ( new Quartic.In () );
        easing.add ( new Quartic.Out () );
        easing.add ( new Quartic.InOut () );
        easing.add ( new Quintic.In () );
        easing.add ( new Quintic.Out () );
        easing.add ( new Quintic.InOut () );
        easing.add ( new Exponential.In () );
        easing.add ( new Exponential.Out () );
        easing.add ( new Exponential.InOut () );
        easing.add ( new Circular.In () );
        easing.add ( new Circular.Out () );
        easing.add ( new Circular.InOut () );
        easing.add ( new Back.In () );
        easing.add ( new Back.Out () );
        easing.add ( new Back.InOut () );
        easing.add ( new Elastic.In () );
        easing.add ( new Elastic.Out () );
        easing.add ( new Elastic.InOut () );
        easing.add ( new Bounce.In () );
        easing.add ( new Bounce.Out () );
        easing.add ( new Bounce.InOut () );
        easing.add ( new Bezier ( 1d, 0d, 0d, 1d ) );
        return easing;
    }
}