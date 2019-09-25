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

package com.alee.demo.content.features;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.AlignLayout;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextArea;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.swing.extensions.MouseEventRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class ShapeDetectionExample extends AbstractPreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "shapedetection";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.settings;
    }

    @NotNull
    @Override
    protected LayoutManager createPreviewLayout ()
    {
        return new AlignLayout ();
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new ShapeDetectionPreview ()
        );
    }

    /**
     * Shape detection preview.
     */
    protected class ShapeDetectionPreview extends AbstractPreview
    {
        /**
         * Constructs new preview.
         */
        public ShapeDetectionPreview ()
        {
            super ( ShapeDetectionExample.this, "preview", FeatureState.release );
        }

        @NotNull
        @Override
        protected JComponent createPreview ( @NotNull final List<Preview> previews, final int index )
        {
            final double[] col = new double[]{ TableLayout.FILL, TableLayout.FILL };
            final double[] row = new double[]{ TableLayout.PREFERRED, TableLayout.PREFERRED,
                    TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, };
            final WebPanel preview = new WebPanel ( DemoStyles.previewPanel, new TableLayout ( col, row, 0, 0 ) );

            /**
             * Detection samples.
             */

            preview.add ( new WebStyledLabel ( DemoStyles.shapedetectionTitle, getExampleLanguageKey ( "examples" ), SwingConstants.CENTER )
                    .changeFontSize ( 5 ).setBoldFont (), "0,0,1,0" );

            preview.add ( new WebStyledLabel ( DemoStyles.shapedetectionSubTitle, getExampleLanguageKey ( "info1" ),
                    SwingConstants.CENTER ), "0,1,1,1" );

            final WebPanel baseTest = new WebPanel ( DemoStyles.shapedetectionTestPanelLeft, new VerticalFlowLayout ( 0, 25 ) );

            baseTest.add ( new WebStyledLabel ( getExampleLanguageKey ( "basic" ), SwingConstants.CENTER ).changeFontSize ( 4 ) );

            final WebButton baseButton = new WebButton ( DemoStyles.shapedetectionButton, getExampleLanguageKey ( "button" ) );
            baseButton.setShapeDetectionEnabled ( false );
            baseButton.changeFontSize ( 4 );
            baseTest.add ( new WebPanel ( DemoStyles.shapedetectionBoundsPanel, baseButton ) );

            final WebTextArea baseTextArea = new WebTextArea ( DemoStyles.shapedetectionTextArea, 2, 1 );
            baseTextArea.setLanguage ( getExampleLanguageKey ( "textarea" ) );
            baseTextArea.setShapeDetectionEnabled ( false );
            baseTextArea.changeFontSize ( 4 );
            baseTest.add ( new WebPanel ( DemoStyles.shapedetectionBoundsPanel, baseTextArea ) );

            preview.add ( baseTest, "0,2" );

            final WebPanel advancedTest = new WebPanel ( DemoStyles.shapedetectionTestPanelRight, new VerticalFlowLayout ( 0, 25 ) );

            advancedTest.add ( new WebStyledLabel ( getExampleLanguageKey ( "advanced" ), SwingConstants.CENTER ).changeFontSize ( 4 ) );

            final WebButton advancedButton = new WebButton ( DemoStyles.shapedetectionButton, getExampleLanguageKey ( "button" ) );
            advancedButton.setShapeDetectionEnabled ( true );
            advancedButton.changeFontSize ( 4 );
            advancedTest.add ( new WebPanel ( DemoStyles.shapedetectionBoundsPanel, advancedButton ) );

            final WebTextArea advancedTextArea = new WebTextArea ( DemoStyles.shapedetectionTextArea, 2, 1 );
            advancedTextArea.setLanguage ( getExampleLanguageKey ( "textarea" ) );
            advancedTextArea.setShapeDetectionEnabled ( true );
            advancedTextArea.changeFontSize ( 4 );
            advancedTest.add ( new WebPanel ( DemoStyles.shapedetectionBoundsPanel, advancedTextArea ) );

            preview.add ( advancedTest, "1,2" );

            /**
             * Events prevew.
             */

            preview.add ( new WebStyledLabel ( DemoStyles.shapedetectionTitle, getExampleLanguageKey ( "mouseevents" ),
                    SwingConstants.CENTER ).changeFontSize ( 5 ).setBoldFont (), "0,3,1,3" );

            final WebPanel globalTest = new WebPanel ( DemoStyles.shapedetectionTestPanel, new VerticalFlowLayout ( 0, 25 ) );


            final WebCheckBox shapeDetection = new WebCheckBox ( StyleId.checkboxStyled, getExampleLanguageKey ( "enabled" ) );
            shapeDetection.setHorizontalAlignment ( SwingConstants.CENTER );
            shapeDetection.setSelected ( WebLookAndFeel.isShapeDetectionEnabled () );
            globalTest.add ( shapeDetection );

            globalTest.add ( new WebStyledLabel ( getExampleLanguageKey ( "info2" ), SwingConstants.CENTER ) );

            final List<Point> dots = new ArrayList<Point> ( 500 );
            final WebPanel paintingArea = new WebPanel ( DemoStyles.shapedetectionDecoratedPanel )
            {
                @Override
                protected void paintComponent ( final Graphics g )
                {
                    super.paintComponent ( g );

                    final Graphics2D g2d = ( Graphics2D ) g;
                    final Object aa = GraphicsUtils.setupAntialias ( g2d );
                    final Paint paint = GraphicsUtils.setupPaint ( g2d, Color.BLACK );
                    for ( final Point dot : dots )
                    {
                        g2d.fillOval ( dot.x - 2, dot.y - 2, 5, 5 );
                    }
                    GraphicsUtils.restoreAntialias ( g2d, aa );
                    GraphicsUtils.restorePaint ( g2d, paint );
                }
            };
            paintingArea.setPreferredSize ( new Dimension ( 0, 150 ) );
            paintingArea.addMouseMotionListener ( new MouseMotionAdapter ()
            {
                @Override
                public void mouseMoved ( final MouseEvent e )
                {
                    final Point dot = e.getPoint ();
                    dots.add ( dot );
                    paintingArea.repaint ( dot.x - 3, dot.y - 3, 7, 7 );
                }
            } );
            paintingArea.onMouseClick ( new MouseEventRunnable ()
            {
                @Override
                public void run ( @NotNull final MouseEvent e )
                {
                    dots.clear ();
                    paintingArea.repaint ();
                }
            } );
            shapeDetection.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    WebLookAndFeel.setShapeDetectionEnabled ( shapeDetection.isSelected () );
                    dots.clear ();
                    paintingArea.repaint ();
                }
            } );
            globalTest.add ( new WebPanel ( DemoStyles.shapedetectionBoundsPanel, paintingArea ) );

            globalTest.add ( new WebStyledLabel ( getExampleLanguageKey ( "info3" ), SwingConstants.CENTER ) );
            globalTest.add ( new WebStyledLabel ( getExampleLanguageKey ( "info4" ), SwingConstants.CENTER ) );

            preview.add ( globalTest, "0,4,1,4" );

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
}