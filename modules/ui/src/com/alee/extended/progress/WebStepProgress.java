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

package com.alee.extended.progress;

import com.alee.global.StyleConstants;
import com.alee.utils.*;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 14.01.13 Time: 13:52
 */

public class WebStepProgress extends JComponent implements SwingConstants, ShapeProvider, SizeMethods<WebStepProgress>
{
    public static final int STEP_SELECTION = 1;
    public static final int PROGRESS_SELECTION = 2;

    // Settings
    private Insets margin = WebStepProgressStyle.margin;
    private int shadeWidth = WebStepProgressStyle.shadeWidth;
    private int stepControlSize = WebStepProgressStyle.stepControlSize;
    private int stepControlRound = WebStepProgressStyle.stepControlRound;
    private int stepControlFillSize = WebStepProgressStyle.stepControlFillSize;
    private int stepControlFillRound = WebStepProgressStyle.stepControlFillRound;
    private int pathSize = WebStepProgressStyle.pathSize;
    private int fillPathSize = WebStepProgressStyle.fillPathSize;
    private Color progressColor = WebStepProgressStyle.progressColor;
    private Color disabledProgressColor = WebStepProgressStyle.disabledProgressColor;
    private boolean showLabels = WebStepProgressStyle.showLabels;
    private int orientation = WebStepProgressStyle.orientation;
    private int labelsPosition = WebStepProgressStyle.labelsPosition;
    private int spacing = WebStepProgressStyle.spacing;
    private boolean selectionEnabled = WebStepProgressStyle.selectionEnabled;
    private int selectionMode = WebStepProgressStyle.selectionMode;

    // Progress data
    private List<StepData> steps = new ArrayList<StepData> ();
    private int selectedStep = 0;
    private float progress = 0f;

    // Runtime variables
    private boolean selecting = false;
    private int sideWidth = 0;
    private Shape borderShape;
    private LinearGradientPaint fillPaint;
    private Shape fillShape;

    public WebStepProgress ()
    {
        this ( 3 );
    }

    public WebStepProgress ( final int steps )
    {
        this ( createDefaultData ( steps ) );
    }

    public WebStepProgress ( final String... steps )
    {
        this ( createSteps ( steps ) );
    }

    public WebStepProgress ( final Component... steps )
    {
        this ( createSteps ( steps ) );
    }

    public WebStepProgress ( final StepData... steps )
    {
        this ( CollectionUtils.copy ( steps ) );
    }

    public WebStepProgress ( final List<StepData> steps )
    {
        super ();

        // Initial data
        setSteps ( steps );

        // Custom layout to place labels properly
        setLayout ( new ProgressLayout () );

        // Step updater and selector
        final ProgressMouseAdapter pma = new ProgressMouseAdapter ();
        addMouseListener ( pma );
        addMouseMotionListener ( pma );

        // Shapes cache updater
        addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                updateShapes ();
            }
        } );
    }

    private class ProgressLayout implements LayoutManager
    {
        @Override
        public void addLayoutComponent ( final String name, final Component comp )
        {
            //
        }

        @Override
        public void removeLayoutComponent ( final Component comp )
        {
            //
        }

        @Override
        public void layoutContainer ( final Container parent )
        {
            final boolean ltr = getComponentOrientation ().isLeftToRight ();
            for ( int i = 0; i < steps.size (); i++ )
            {
                final Component label = getStep ( i ).getLabel ();
                if ( label != null )
                {
                    final Point sc = getStepCenter ( i );
                    final Dimension ps = label.getPreferredSize ();
                    if ( orientation == HORIZONTAL )
                    {
                        if ( labelsPosition == LEADING )
                        {
                            label.setBounds ( sc.x - ps.width / 2, sc.y - stepControlSize / 2 - shadeWidth - spacing - ps.height, ps.width,
                                    ps.height );
                        }
                        else
                        {
                            label.setBounds ( sc.x - ps.width / 2, sc.y + stepControlSize / 2 + shadeWidth + spacing, ps.width, ps.height );
                        }
                    }
                    else
                    {
                        if ( ltr ? labelsPosition == LEADING : labelsPosition == TRAILING )
                        {
                            label.setBounds ( sc.x - stepControlSize / 2 - shadeWidth - spacing - ps.width, sc.y - ps.height / 2, ps.width,
                                    ps.height );
                        }
                        else
                        {
                            label.setBounds ( sc.x + stepControlSize / 2 + shadeWidth + spacing, sc.y - ps.height / 2, ps.width,
                                    ps.height );
                        }
                    }
                }
            }
        }

        @Override
        public Dimension preferredLayoutSize ( final Container parent )
        {
            return null;
        }

        @Override
        public Dimension minimumLayoutSize ( final Container parent )
        {
            return null;
        }
    }

    private class ProgressMouseAdapter extends MouseAdapter
    {
        @Override
        public void mousePressed ( final MouseEvent e )
        {
            if ( isEnabled () && isSelectionEnabled () && SwingUtils.isLeftMouseButton ( e ) )
            {
                selecting = true;
                updateProgress ( e.getPoint () );
            }
        }

        @Override
        public void mouseDragged ( final MouseEvent e )
        {
            if ( selecting && SwingUtils.isLeftMouseButton ( e ) )
            {
                updateProgress ( e.getPoint () );
            }
        }

        private void updateProgress ( final Point p )
        {
            final float tp = getTotalProgressAt ( p );
            if ( selectionMode == PROGRESS_SELECTION )
            {
                setSelectedStep ( Math.round ( tp - tp % 1 ) );
                setProgress ( tp % 1 );
            }
            else
            {
                setSelectedStep ( Math.round ( tp ) );
            }
        }

        @Override
        public void mouseReleased ( final MouseEvent e )
        {
            if ( selecting && SwingUtils.isLeftMouseButton ( e ) )
            {
                selecting = false;
            }
        }
    }

    /**
     * Component global margin
     */

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        revalidate ();
        repaint ();
    }

    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setMargin ( final int spacing )
    {
        setMargin ( spacing, spacing, spacing, spacing );
    }

    /**
     * Shade width
     */

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        revalidate ();
        repaint ();
    }

    /**
     * Step control sizes
     */

    public int getStepControlSize ()
    {
        return stepControlSize;
    }

    public void setStepControlSize ( final int stepControlSize )
    {
        this.stepControlSize = stepControlSize;
        revalidate ();
        repaint ();
    }

    public int getStepControlRound ()
    {
        return stepControlRound;
    }

    public void setStepControlRound ( final int stepControlRound )
    {
        this.stepControlRound = stepControlRound;
        repaint ();
    }

    public int getStepControlFillSize ()
    {
        return stepControlFillSize;
    }

    public void setStepControlFillSize ( final int stepControlFillSize )
    {
        this.stepControlFillSize = stepControlFillSize;
        revalidate ();
        repaint ();
    }

    public int getStepControlFillRound ()
    {
        return stepControlFillRound;
    }

    public void setStepControlFillRound ( final int stepControlFillRound )
    {
        this.stepControlFillRound = stepControlFillRound;
        repaint ();
    }

    /**
     * Progress path sizes
     */

    public int getPathSize ()
    {
        return pathSize;
    }

    public void setPathSize ( final int pathSize )
    {
        this.pathSize = pathSize;
        revalidate ();
        repaint ();
    }

    public int getFillPathSize ()
    {
        return fillPathSize;
    }

    public void setFillPathSize ( final int fillPathSize )
    {
        this.fillPathSize = fillPathSize;
        revalidate ();
        repaint ();
    }

    /**
     * Show step labels
     */

    public boolean isShowLabels ()
    {
        return showLabels;
    }

    public void setShowLabels ( final boolean showLabels )
    {
        if ( this.showLabels != showLabels )
        {
            this.showLabels = showLabels;
            if ( showLabels )
            {
                for ( final StepData step : steps )
                {
                    if ( step.getLabel () != null )
                    {
                        add ( step.getLabel () );
                    }
                }
            }
            else
            {
                for ( final StepData step : steps )
                {
                    if ( step.getLabel () != null )
                    {
                        remove ( step.getLabel () );
                    }
                }
            }
            revalidate ();
            repaint ();
        }
    }

    /**
     * Step progress bar orientation
     */

    public int getOrientation ()
    {
        return orientation;
    }

    public void setOrientation ( final int orientation )
    {
        this.orientation = orientation;
        revalidate ();
        repaint ();
    }

    /**
     * Step labels positioning
     */

    public int getLabelsPosition ()
    {
        return labelsPosition;
    }

    public void setLabelsPosition ( final int labelsPosition )
    {
        this.labelsPosition = labelsPosition;
        revalidate ();
        repaint ();
    }

    /**
     * Spacing between labels and progress
     */

    public int getSpacing ()
    {
        return spacing;
    }

    public void setSpacing ( final int spacing )
    {
        this.spacing = spacing;
        revalidate ();
        repaint ();
    }

    /**
     * Selection
     */

    public boolean isSelectionEnabled ()
    {
        return selectionEnabled;
    }

    public void setSelectionEnabled ( final boolean selectionEnabled )
    {
        this.selectionEnabled = selectionEnabled;
    }

    public int getSelectionMode ()
    {
        return selectionMode;
    }

    public void setSelectionMode ( final int selectionMode )
    {
        this.selectionMode = selectionMode;
    }

    /**
     * Steps modification methods
     */

    public int getStepsAmount ()
    {
        return steps.size ();
    }

    public List<StepData> getSteps ()
    {
        return steps;
    }

    public StepData getStep ( final int index )
    {
        return steps.get ( index );
    }

    public void setSteps ( final String... steps )
    {
        setSteps ( createSteps ( steps ) );
    }

    public void setSteps ( final Component... steps )
    {
        setSteps ( createSteps ( steps ) );
    }

    public void setSteps ( final List<StepData> steps )
    {
        clearSteps ();
        addSteps ( steps );
        validateSelectedStep ();
    }

    public void addSteps ( final String... steps )
    {
        if ( steps != null )
        {
            addSteps ( createSteps ( steps ) );
        }
    }

    public void addSteps ( final Component... steps )
    {
        if ( steps != null )
        {
            addSteps ( createSteps ( steps ) );
        }
    }

    public void addSteps ( final List<StepData> steps )
    {
        if ( steps != null )
        {
            this.steps.addAll ( steps );
            if ( showLabels )
            {
                for ( final StepData step : steps )
                {
                    if ( step.getLabel () != null )
                    {
                        add ( step.getLabel () );
                    }
                }
                revalidate ();
                repaint ();
            }
        }
    }

    public void removeStep ( final int index )
    {
        removeStep ( getStep ( index ) );
    }

    public void removeStep ( final StepData stepData )
    {
        clearStep ( stepData );
        validateSelectedStep ();
        revalidate ();
        repaint ();
    }

    public void setStepsAmount ( final int stepsAmount )
    {
        clearSteps ();
        addSteps ( createDefaultData ( stepsAmount ) );
        validateSelectedStep ();
        revalidate ();
        repaint ();
    }

    private void clearSteps ()
    {
        if ( steps.size () > 0 )
        {
            for ( final StepData step : CollectionUtils.copy ( steps ) )
            {
                clearStep ( step );
            }
        }
    }

    private void clearStep ( final StepData step )
    {
        if ( showLabels )
        {
            final Component label = step.getLabel ();
            if ( label != null )
            {
                label.getParent ().remove ( label );
            }
        }
        this.steps.remove ( step );
    }

    /**
     * Step operations
     */

    public int getSelectedStep ()
    {
        return selectedStep;
    }

    public void setSelectedStep ( int selectedStep )
    {
        // Check step correctness
        if ( selectedStep == this.selectedStep )
        {
            return;
        }
        if ( selectedStep < 0 )
        {
            selectedStep = 0;
        }
        if ( selectedStep >= steps.size () )
        {
            selectedStep = steps.size () - 1;
        }

        // Update step
        this.selectedStep = selectedStep;
        this.progress = 0f;
        updateFillShape ();
    }

    private void validateSelectedStep ()
    {
        if ( selectedStep < getStepsAmount () )
        {
            setSelectedStep ( 0 );
        }
    }

    /**
     * Progress operations
     */

    public float getProgress ()
    {
        return progress;
    }

    public void setProgress ( float progress )
    {
        // Check progress correctness
        if ( progress == this.progress )
        {
            return;
        }
        if ( progress < 0f )
        {
            final float p = Math.abs ( progress );
            final int s = selectedStep - Math.round ( p - p % 1 ) - 1;
            progress = s < 0 ? 0 : 1 - p % 1;
            setSelectedStep ( s );
        }
        else if ( progress >= 1f )
        {
            final int s = selectedStep + Math.round ( progress - progress % 1 );
            progress = s >= steps.size () ? 0 : progress % 1;
            setSelectedStep ( s );
        }

        // Update progress
        this.progress = progress;
        updateFillShape ();
    }

    /**
     * Assistance methods
     */

    /**
     * Total progress change
     */

    public float getTotalProgress ()
    {
        return ( selectedStep + progress ) / ( steps.size () - 1 );
    }

    public float getTotalProgressAt ( final Point point )
    {
        final boolean hor = orientation == HORIZONTAL;
        final Point p1 = getPathStart ();
        final Point p2 = getPathEnd ();
        final float pw = getPathWidth ();
        if ( hor )
        {
            if ( getComponentOrientation ().isLeftToRight () )
            {
                if ( point.x < p1.x )
                {
                    return 0f;
                }
                else if ( point.x > p2.x )
                {
                    return steps.size () - 1;
                }
                else
                {
                    return ( steps.size () - 1 ) * ( point.x - p1.x ) / pw;
                }
            }
            else
            {
                if ( point.x > p1.x )
                {
                    return 0f;
                }
                else if ( point.x < p2.x )
                {
                    return steps.size () - 1;
                }
                else
                {
                    return ( steps.size () - 1 ) * ( pw - ( point.x - p2.x ) ) / pw;
                }
            }
        }
        else
        {
            if ( point.y < p1.y )
            {
                return 0f;
            }
            else if ( point.y > p2.y )
            {
                return steps.size () - 1;
            }
            else
            {
                return ( steps.size () - 1 ) * ( point.y - p1.y ) / pw;
            }
        }
    }

    public void setTotalProgress ( float totalProgress )
    {
        totalProgress = ( steps.size () - 1 ) * totalProgress;
        setSelectedStep ( Math.round ( totalProgress - totalProgress % 1 ) );
        setProgress ( totalProgress % 1 );
    }

    /**
     * Custom painting method
     */

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Border and background
        LafUtils.drawCustomWebBorder ( g2d, this, getBorderShape (), StyleConstants.shadeColor, shadeWidth, true, true );

        // Progress line
        g2d.setPaint ( getFillPaint () );
        g2d.fill ( getProgressShape () );

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Basic component shape
     */

    @Override
    public Shape provideShape ()
    {
        return getBorderShape ();
    }

    /**
     * Shapes cache update methods
     */

    protected void updateBorderShape ()
    {
        borderShape = null;
        repaint ();
    }

    protected void updateFillShape ()
    {
        fillPaint = null;
        fillShape = null;
        repaint ();
    }

    protected void updateShapes ()
    {
        borderShape = null;
        fillPaint = null;
        fillShape = null;
        repaint ();
    }

    /**
     * Border painting data
     */

    private Shape getBorderShape ()
    {
        if ( borderShape == null )
        {
            borderShape = createBorderShape ();
        }
        return borderShape;
    }

    private Shape createBorderShape ()
    {
        final Area border = new Area ( getPathShape () );
        for ( int i = 0; i < steps.size (); i++ )
        {
            border.add ( new Area ( getStepBorderCircle ( i ) ) );
        }
        return border;
    }

    private Shape getStepBorderCircle ( final int step )
    {
        final Point center = getStepCenter ( step );
        if ( stepControlRound * 2 >= stepControlSize )
        {
            return new Ellipse2D.Double ( center.x - stepControlSize / 2, center.y - stepControlSize / 2, stepControlSize,
                    stepControlSize );
        }
        else
        {
            return new RoundRectangle2D.Double ( center.x - stepControlSize / 2, center.y - stepControlSize / 2, stepControlSize,
                    stepControlSize, stepControlRound * 2, stepControlRound * 2 );
        }
    }

    /**
     * Progress painting data
     */

    private LinearGradientPaint getFillPaint ()
    {
        if ( fillPaint == null )
        {
            fillPaint = createFillPaint ();
        }
        return fillPaint;
    }

    private LinearGradientPaint createFillPaint ()
    {
        final Point p1 = getPathStart ();
        final float tss = selectedStep + progress;
        final int sw = getPathWidth () / ( steps.size () - 1 );
        final float pw = stepControlFillSize * 1.5f + sw * tss;
        final Color color = isEnabled () ? progressColor : disabledProgressColor;
        if ( orientation == HORIZONTAL )
        {
            final boolean ltr = getComponentOrientation ().isLeftToRight ();
            final float px = ltr ? ( p1.x - stepControlFillSize / 2 ) : ( p1.x + stepControlFillSize / 2 - pw );
            final float[] fractions = ltr ? new float[]{ 0f, ( stepControlFillSize + sw * tss ) / pw, 1f } :
                    new float[]{ 0f, 1 - ( stepControlFillSize + sw * tss ) / pw, 1f };
            final Color[] colors =
                    ltr ? new Color[]{ color, color, StyleConstants.transparent } : new Color[]{ StyleConstants.transparent, color, color };
            return new LinearGradientPaint ( px, 0, px + pw, 0, fractions, colors );
        }
        else
        {
            final float py = p1.y - stepControlFillSize / 2;
            return new LinearGradientPaint ( 0, py, 0, py + pw, new float[]{ 0f, ( stepControlFillSize + sw * tss ) / pw, 1f },
                    new Color[]{ color, color, StyleConstants.transparent } );
        }
    }

    private Shape getProgressShape ()
    {
        if ( fillShape == null )
        {
            fillShape = createFillShape ();
        }
        return fillShape;
    }

    private Shape createFillShape ()
    {
        final Area border = new Area ( getPathFillShape () );
        for ( int i = 0; i < steps.size (); i++ )
        {
            border.add ( new Area ( getStepFillCircle ( i ) ) );
        }
        return border;
    }

    private Shape getStepFillCircle ( final int step )
    {
        final Point center = getStepCenter ( step );
        if ( stepControlFillRound * 2 >= stepControlFillSize )
        {
            return new Ellipse2D.Double ( center.x - stepControlFillSize / 2, center.y - stepControlFillSize / 2, stepControlFillSize,
                    stepControlFillSize );
        }
        else
        {
            return new RoundRectangle2D.Double ( center.x - stepControlFillSize / 2, center.y - stepControlFillSize / 2,
                    stepControlFillSize, stepControlFillSize, stepControlFillRound * 2, stepControlFillRound * 2 );
        }
    }

    /**
     * Base controls positioning
     */

    private Shape getPathShape ()
    {
        final Point p1 = getPathStart ();
        final Point p2 = getPathEnd ();
        if ( orientation == HORIZONTAL )
        {
            final boolean ltr = getComponentOrientation ().isLeftToRight ();
            return new Rectangle2D.Double ( ltr ? p1.x : p2.x, p1.y - pathSize / 2f, ltr ? ( p2.x - p1.x ) : ( p1.x - p2.x ), pathSize );
        }
        else
        {
            return new Rectangle2D.Double ( p1.x - pathSize / 2f, p1.y, pathSize, p2.y - p1.y );
        }
    }

    private Shape getPathFillShape ()
    {
        final Point p1 = getPathStart ();
        final Point p2 = getPathEnd ();
        if ( orientation == HORIZONTAL )
        {
            final boolean ltr = getComponentOrientation ().isLeftToRight ();
            return new Rectangle2D.Double ( ltr ? p1.x : p2.x, p1.y + 0.5f - fillPathSize / 2f, ltr ? ( p2.x - p1.x ) : ( p1.x - p2.x ),
                    fillPathSize );
        }
        else
        {
            return new Rectangle2D.Double ( p1.x + 0.5f - fillPathSize / 2f, p1.y, fillPathSize, p2.y - p1.y );
        }
    }

    private Point getPathStart ()
    {
        return getStepCenter ( 0 );
    }

    private Point getPathEnd ()
    {
        return getStepCenter ( steps.size () - 1 );
    }

    private Point getStepCenter ( final int step )
    {
        final Dimension max = getMaximumComponentSize ();
        final boolean ltr = getComponentOrientation ().isLeftToRight ();
        if ( orientation == HORIZONTAL )
        {
            final int x = margin.left + sideWidth + getPathWidth () * ( ltr ? step : ( steps.size () - 1 - step ) ) / ( steps.size () - 1 );
            final int wh = getHeight () - margin.top - margin.bottom;
            final int sh = max.height + ( max.height > 0 ? spacing : 0 );
            final int ch = sh + stepControlSize + shadeWidth * 2;
            final int y = margin.top + wh / 2 - ch / 2 + ( labelsPosition == LEADING ? sh : 0 ) + shadeWidth + stepControlSize / 2;
            return new Point ( x, y );
        }
        else
        {
            final int y = margin.top + sideWidth + getPathWidth () * step / ( steps.size () - 1 );
            final int ww = getWidth () - margin.left - margin.right;
            final int sw = max.width + ( max.width > 0 ? spacing : 0 );
            final int cw = sw + stepControlSize + shadeWidth * 2;
            final int x = margin.left + ww / 2 - cw / 2 + ( ( ltr ? labelsPosition == LEADING : labelsPosition == TRAILING ) ? sw : 0 ) +
                    shadeWidth + stepControlSize / 2;
            return new Point ( x, y );
        }
    }

    private int getPathWidth ()
    {
        if ( orientation == HORIZONTAL )
        {
            return getWidth () - sideWidth * 2 - margin.left - margin.right - 1;
        }
        else
        {
            return getHeight () - sideWidth * 2 - margin.top - margin.bottom - 1;
        }
    }

    public int getStepCircleAt ( final Point point )
    {
        for ( int i = 0; i < steps.size (); i++ )
        {
            if ( getStepBorderCircle ( i ).contains ( point ) )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Size methods.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStepProgress setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStepProgress setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStepProgress setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStepProgress setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension ps;
        if ( isPreferredSizeSet () )
        {
            ps = super.getPreferredSize ();
        }
        else
        {
            // todo Move to custom UI
            final Dimension max = getMaximumComponentSize ();
            final Dimension maxSide = getMaximumSideComponentSize ();
            if ( orientation == HORIZONTAL )
            {
                sideWidth = Math.max ( maxSide.width / 2, stepControlSize / 2 + shadeWidth );
                final int w =
                        margin.left + shadeWidth * ( steps.size () - 1 ) * 2 + stepControlSize * ( steps.size () - 1 ) + sideWidth * 2 +
                                margin.right + 1;
                final int h =
                        margin.top + shadeWidth * 2 + stepControlSize + max.height + ( max.height > 0 ? spacing : 0 ) + margin.bottom + 1;
                ps = new Dimension ( w, h );
            }
            else
            {
                sideWidth = Math.max ( maxSide.height / 2, stepControlSize / 2 + shadeWidth );
                final int w =
                        margin.left + shadeWidth * 2 + stepControlSize + max.width + ( max.width > 0 ? spacing : 0 ) + margin.right + 1;
                final int h =
                        margin.top + shadeWidth * ( steps.size () - 1 ) * 2 + stepControlSize * ( steps.size () - 1 ) + sideWidth * 2 +
                                margin.bottom + 1;
                ps = new Dimension ( w, h );
            }
        }
        return SizeUtils.getPreferredSize ( this, ps );
    }

    private Dimension getMaximumComponentSize ()
    {
        Dimension max = new Dimension ( 0, 0 );
        if ( showLabels && steps.size () > 0 )
        {
            for ( final StepData step : steps )
            {
                if ( step.getLabel () != null )
                {
                    max = SwingUtils.max ( max, step.getLabel ().getPreferredSize () );
                }
            }
        }
        return max;
    }

    private Dimension getMaximumSideComponentSize ()
    {
        if ( showLabels && steps.size () > 0 )
        {
            final Component l1 = steps.get ( 0 ).getLabel ();
            final Component l2 = steps.get ( steps.size () - 1 ).getLabel ();
            if ( l1 != null && l2 != null )
            {
                return SwingUtils.max ( l1.getPreferredSize (), l2.getPreferredSize () );
            }
            else if ( l1 != null )
            {
                return l1.getPreferredSize ();
            }
            else if ( l2 != null )
            {
                return l2.getPreferredSize ();
            }
            else
            {
                return new Dimension ( 0, 0 );
            }
        }
        else
        {
            return new Dimension ( 0, 0 );
        }
    }

    /**
     * Additional useful methods
     */

    public static List<StepData> createSteps ( final String[] steps )
    {
        final List<StepData> s = new ArrayList<StepData> ();
        for ( final String step : steps )
        {
            s.add ( new StepData ( step ) );
        }
        return s;
    }

    public static List<StepData> createSteps ( final Component[] steps )
    {
        final List<StepData> s = new ArrayList<StepData> ();
        for ( final Component step : steps )
        {
            s.add ( new StepData ( step ) );
        }
        return s;
    }

    public static List<StepData> createDefaultData ( final int stepsAmount )
    {
        final List<StepData> s = new ArrayList<StepData> ( stepsAmount );
        for ( int i = 0; i < stepsAmount; i++ )
        {
            s.add ( new StepData () );
        }
        return s;
    }
}