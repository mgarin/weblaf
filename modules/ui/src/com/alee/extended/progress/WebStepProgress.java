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

import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.managers.style.ShapeMethods;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.extensions.SizeMethods;
import com.alee.utils.swing.extensions.SizeMethodsImpl;

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
import java.util.Collections;
import java.util.List;

/**
 * Custom progress display component.
 * <p>
 * Progress display is based on two main things: selected step and progress value.
 * Selected step determines which step from the list of added steps is currently selected.
 * Progress determines the progress value acomplished to reach next step.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStepProgress">How to use WebStepProgress</a>
 */

public class WebStepProgress extends JComponent implements SwingConstants, ShapeMethods, SizeMethods<WebStepProgress>
{
    /**
     * todo 1. Implement ShapeCache
     */

    /**
     * Style and other settings.
     */
    protected Insets margin = WebStepProgressStyle.margin;
    protected int shadeWidth = WebStepProgressStyle.shadeWidth;
    protected int stepControlWidth = WebStepProgressStyle.stepControlWidth;
    protected int stepControlRound = WebStepProgressStyle.stepControlRound;
    protected int stepControlFillWidth = WebStepProgressStyle.stepControlFillWidth;
    protected int stepControlFillRound = WebStepProgressStyle.stepControlFillRound;
    protected int pathWidth = WebStepProgressStyle.pathWidth;
    protected int pathFillWidth = WebStepProgressStyle.pathFillWidth;
    protected Color progressColor = WebStepProgressStyle.progressColor;
    protected Color disabledProgressColor = WebStepProgressStyle.disabledProgressColor;
    protected boolean displayLabels = WebStepProgressStyle.displayLabels;
    protected int orientation = WebStepProgressStyle.orientation;
    protected int labelsPosition = WebStepProgressStyle.labelsPosition;
    protected int spacing = WebStepProgressStyle.spacing;
    protected int stepsSpacing = WebStepProgressStyle.stepsSpacing;
    protected boolean selectionEnabled = WebStepProgressStyle.selectionEnabled;
    protected StepSelectionMode selectionMode = WebStepProgressStyle.selectionMode;

    /**
     * Progress data.
     */
    protected final List<StepData> steps = new ArrayList<StepData> ();
    protected int selectedStep = 0;
    protected float progress = 0f;

    /**
     * Runtime variables.
     */
    protected boolean selecting = false;
    protected int sideWidth = 0;
    protected Shape borderShape;
    protected LinearGradientPaint fillPaint;
    protected Shape fillShape;

    /**
     * Constructs new WebStepProgress with three default steps.
     */
    public WebStepProgress ()
    {
        this ( Collections.EMPTY_LIST );
    }

    /**
     * Constructs new WebStepProgress with the specified amount of default steps.
     *
     * @param amount amount of default steps
     */
    public WebStepProgress ( final int amount )
    {
        this ( createDefaultData ( amount ) );
    }

    /**
     * Constructs new WebStepProgress with steps using specified names in labels.
     *
     * @param names label names
     */
    public WebStepProgress ( final String... names )
    {
        this ( createSteps ( names ) );
    }

    /**
     * Constructs new WebStepProgress with steps using specified labels.
     *
     * @param labels step labels
     */
    public WebStepProgress ( final Component... labels )
    {
        this ( createSteps ( labels ) );
    }

    /**
     * Constructs new WebStepProgress with the specified steps.
     *
     * @param steps steps
     */
    public WebStepProgress ( final StepData... steps )
    {
        this ( CollectionUtils.asList ( steps ) );
    }

    /**
     * Constructs new WebStepProgress with the specified steps.
     *
     * @param steps steps
     */
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

    /**
     * Returns sides margin.
     *
     * @return sides margin
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets sides margin.
     *
     * @param margin new sides margin
     */
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        revalidate ();
    }

    /**
     * Sets sides margin.
     *
     * @param top    top side margin
     * @param left   left side margin
     * @param bottom bottom side margin
     * @param right  right side margin
     */
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets sides margin.
     *
     * @param spacing sides margin
     */
    public void setMargin ( final int spacing )
    {
        setMargin ( spacing, spacing, spacing, spacing );
    }

    /**
     * Returns decoration shade width.
     *
     * @return decoration shade width
     */
    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    /**
     * Sets decoration shade width.
     *
     * @param shadeWidth decoration shade width
     */
    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        revalidate ();
    }

    /**
     * Returns step control width.
     *
     * @return step control width
     */
    public int getStepControlWidth ()
    {
        return stepControlWidth;
    }

    /**
     * Sets step control width.
     *
     * @param width step control width
     */
    public void setStepControlWidth ( final int width )
    {
        this.stepControlWidth = width;
        revalidate ();
    }

    /**
     * Returns step control round.
     *
     * @return step control round
     */
    public int getStepControlRound ()
    {
        return stepControlRound;
    }

    /**
     * Sets step control round.
     *
     * @param round step control round
     */
    public void setStepControlRound ( final int round )
    {
        this.stepControlRound = round;
        repaint ();
    }

    /**
     * Returns step control fill width.
     *
     * @return step control fill width
     */
    public int getStepControlFillWidth ()
    {
        return stepControlFillWidth;
    }

    /**
     * Sets step control fill width.
     *
     * @param width new step control fill width
     */
    public void setStepControlFillWidth ( final int width )
    {
        this.stepControlFillWidth = width;
        revalidate ();
    }

    /**
     * Returns step control fill round.
     *
     * @return step control fill round
     */
    public int getStepControlFillRound ()
    {
        return stepControlFillRound;
    }

    /**
     * Sets step control fill round.
     *
     * @param round new step control fill round
     */
    public void setStepControlFillRound ( final int round )
    {
        this.stepControlFillRound = round;
        repaint ();
    }

    /**
     * Returns path width.
     *
     * @return path width
     */
    public int getPathWidth ()
    {
        return pathWidth;
    }

    /**
     * Sets path width.
     *
     * @param width new path width
     */
    public void setPathWidth ( final int width )
    {
        this.pathWidth = width;
        revalidate ();
    }

    /**
     * Returns path fill width.
     *
     * @return path fill width
     */
    public int getPathFillWidth ()
    {
        return pathFillWidth;
    }

    /**
     * Sets path fill width.
     *
     * @param width new path fill width
     */
    public void setPathFillWidth ( final int width )
    {
        this.pathFillWidth = width;
        revalidate ();
    }

    /**
     * Returns progress fill color.
     *
     * @return progress fill color
     */
    public Color getProgressColor ()
    {
        return progressColor;
    }

    /**
     * Sets progress fill color.
     *
     * @param color new progress fill color
     */
    public void setProgressColor ( final Color color )
    {
        this.progressColor = color;
        repaint ();
    }

    /**
     * Returns disabled progress fill color.
     *
     * @return disabled progress fill color
     */
    public Color getDisabledProgressColor ()
    {
        return disabledProgressColor;
    }

    /**
     * Sets disabled progress fill color.
     *
     * @param color new disabled progress fill color
     */
    public void setDisabledProgressColor ( final Color color )
    {
        this.disabledProgressColor = color;
        repaint ();
    }

    /**
     * Returns whether step labels should be displayed or not.
     *
     * @return true if step labels should be displayed, false otherwise
     */
    public boolean isDisplayLabels ()
    {
        return displayLabels;
    }

    /**
     * Sets whether step labels should be displayed or not.
     *
     * @param displayLabels whether step labels should be displayed or not
     */
    public void setDisplayLabels ( final boolean displayLabels )
    {
        if ( this.displayLabels != displayLabels )
        {
            this.displayLabels = displayLabels;
            if ( displayLabels )
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
        }
    }

    /**
     * Returns progress orientation.
     *
     * @return progress orientation
     */
    public int getOrientation ()
    {
        return orientation;
    }

    /**
     * Sets progress orientation.
     *
     * @param orientation new progress orientation
     */
    public void setOrientation ( final int orientation )
    {
        this.orientation = orientation;
        revalidate ();
    }

    /**
     * Returns labels position relative to progress.
     *
     * @return labels position relative to progress
     */
    public int getLabelsPosition ()
    {
        return labelsPosition;
    }

    /**
     * Sets labels position relative to progress.
     *
     * @param position new labels position relative to progress
     */
    public void setLabelsPosition ( final int position )
    {
        this.labelsPosition = position;
        revalidate ();
    }

    /**
     * Returns spacing between labels and progress.
     *
     * @return spacing between labels and progress
     */
    public int getSpacing ()
    {
        return spacing;
    }

    /**
     * Sets spacing between labels and progress.
     *
     * @param spacing new spacing between labels and progress
     */
    public void setSpacing ( final int spacing )
    {
        this.spacing = spacing;
        revalidate ();
    }

    /**
     * Returns spacing between step labels.
     *
     * @return spacing between step labels
     */
    public int getStepsSpacing ()
    {
        return stepsSpacing;
    }

    /**
     * Sets spacing between step labels.
     *
     * @param spacing new spacing between step labels
     */
    public void setStepsSpacing ( final int spacing )
    {
        this.stepsSpacing = spacing;
        revalidate ();
    }

    /**
     * Returns whether progress selection is allowed or not.
     *
     * @return true if progress selection is allowed, false otherwise
     */
    public boolean isSelectionEnabled ()
    {
        return selectionEnabled;
    }

    /**
     * Sets whether progress selection is allowed or not.
     *
     * @param enabled whether progress selection is allowed or not
     */
    public void setSelectionEnabled ( final boolean enabled )
    {
        this.selectionEnabled = enabled;
    }

    /**
     * Returns progress selection mode.
     *
     * @return progress selection mode
     * @see com.alee.extended.progress.StepSelectionMode
     */
    public StepSelectionMode getSelectionMode ()
    {
        return selectionMode;
    }

    /**
     * Sets progress selection mode.
     *
     * @param mode new progress selection mode
     * @see com.alee.extended.progress.StepSelectionMode
     */
    public void setSelectionMode ( final StepSelectionMode mode )
    {
        this.selectionMode = mode;
    }

    /**
     * Returns amount of steps.
     *
     * @return amount of steps
     */
    public int getStepsAmount ()
    {
        return steps.size ();
    }

    /**
     * Returns list of existing steps.
     *
     * @return list of existing steps
     */
    public List<StepData> getSteps ()
    {
        return CollectionUtils.copy ( steps );
    }

    /**
     * Returns step at the specified index.
     *
     * @param index step index
     * @return step at the specified index
     */
    public StepData getStep ( final int index )
    {
        return steps.get ( index );
    }

    /**
     * Sets new steps with the specified names.
     *
     * @param names new steps names
     */
    public void setSteps ( final String... names )
    {
        setSteps ( createSteps ( names ) );
    }

    /**
     * Sets steps with the specified label components.
     *
     * @param labels new steps label components
     */
    public void setSteps ( final Component... labels )
    {
        setSteps ( createSteps ( labels ) );
    }

    /**
     * Sets new steps.
     *
     * @param steps new steps
     */
    public void setSteps ( final List<StepData> steps )
    {
        clearSteps ();
        addSteps ( steps );
    }

    /**
     * Adds new steps with the specified names.
     *
     * @param steps new steps names
     */
    public void addSteps ( final String... steps )
    {
        if ( steps != null )
        {
            addSteps ( createSteps ( steps ) );
        }
    }

    /**
     * Adds steps with the specified label components.
     *
     * @param steps new steps label components
     */
    public void addSteps ( final Component... steps )
    {
        if ( steps != null )
        {
            addSteps ( createSteps ( steps ) );
        }
    }

    /**
     * Adds new steps.
     *
     * @param steps new steps
     */
    public void addSteps ( final List<StepData> steps )
    {
        if ( steps != null )
        {
            this.steps.addAll ( steps );
            if ( displayLabels )
            {
                for ( final StepData step : steps )
                {
                    if ( step.getLabel () != null )
                    {
                        add ( step.getLabel () );
                    }
                }
                revalidate ();
            }
            validateSelectedStep ();
        }
    }

    /**
     * Removes step under the specified index.
     *
     * @param index step index
     */
    public void removeStep ( final int index )
    {
        removeStep ( getStep ( index ) );
    }

    /**
     * Removes specified step.
     *
     * @param stepData step to remove
     */
    public void removeStep ( final StepData stepData )
    {
        clearStep ( stepData );
        revalidate ();
    }

    /**
     * Sets specified amount of new default steps with empty label components.
     *
     * @param amount default steps amount
     */
    public void setSteps ( final int amount )
    {
        clearSteps ();
        addSteps ( createDefaultData ( amount ) );
        revalidate ();
    }

    /**
     * Clears steps.
     */
    protected void clearSteps ()
    {
        if ( steps.size () > 0 )
        {
            for ( final StepData step : CollectionUtils.copy ( steps ) )
            {
                clearStep ( step );
            }
        }
    }

    /**
     * Clears specified step.
     *
     * @param step step to clear
     */
    protected void clearStep ( final StepData step )
    {
        if ( displayLabels )
        {
            final Component label = step.getLabel ();
            if ( label != null )
            {
                label.getParent ().remove ( label );
            }
        }
        this.steps.remove ( step );
        validateSelectedStep ();
    }

    /**
     * Returns selected step.
     *
     * @return selected step
     */
    public StepData getSelectedStep ()
    {
        return steps.get ( getSelectedStepIndex () );
    }

    /**
     * Sets selected step.
     *
     * @param step new selected step
     */
    public void setSelectedStep ( final StepData step )
    {
        setSelectedStepIndex ( steps.indexOf ( step ) );
    }

    /**
     * Returns selected step index.
     *
     * @return selected step index
     */
    public int getSelectedStepIndex ()
    {
        return selectedStep;
    }

    /**
     * Sets selected step index.
     *
     * @param index new selected step index
     */
    public void setSelectedStepIndex ( int index )
    {
        // Ignore call if this step is already selected
        if ( index == this.selectedStep )
        {
            return;
        }

        // Check step correctness
        if ( index < 0 )
        {
            index = 0;
        }
        if ( index >= steps.size () )
        {
            index = steps.size () - 1;
        }

        // Update step
        this.selectedStep = index;
        this.progress = 0f;
        updateFillShape ();
    }

    /**
     * Revalidates selected step.
     * In case selected step doesn't exist selection will be corrected.
     */
    protected void validateSelectedStep ()
    {
        if ( selectedStep >= getStepsAmount () )
        {
            setSelectedStepIndex ( getStepsAmount () - 1 );
        }
        if ( selectedStep < 0 && getStepsAmount () > 0 )
        {
            setSelectedStepIndex ( 0 );
        }
    }

    /**
     * Returns progress value.
     * This is only progress between currently selected and next steps.
     * This value is always between 0.0 and 1.0.
     *
     * @return progress value
     */
    public float getProgress ()
    {
        return progress;
    }

    /**
     * Sets progress value.
     * This is only progress between currently selected and next steps.
     * Value itself should be always between 0.0 and 1.0 but you can specify lesser or greater value to affect selected steps.
     * For instance - if you provide 1.5 this will select next available step after currently selected and set progress to 0.5.
     *
     * @param progress new progress value
     */
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
            setSelectedStepIndex ( s );
        }
        else if ( progress >= 1f )
        {
            final int s = selectedStep + Math.round ( progress - progress % 1 );
            progress = s >= steps.size () ? 0 : progress % 1;
            setSelectedStepIndex ( s );
        }

        // Update progress
        this.progress = progress;
        updateFillShape ();
    }

    /**
     * Returns total progress.
     * This method combines currently selected step index and progress and returns total progress.
     *
     * @return total progress
     */
    public float getTotalProgress ()
    {
        return ( selectedStep + progress ) / ( steps.size () - 1 );
    }

    /**
     * Returns total progress for the specified point on progress component.
     *
     * @param point point to retrive total progress for
     * @return total progress for the specified point on progress component
     */
    public float getTotalProgressAt ( final Point point )
    {
        final boolean hor = orientation == HORIZONTAL;
        final Point p1 = getPathStart ();
        final Point p2 = getPathEnd ();
        final float pw = getPathLength ();
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

    /**
     * Sets total progress.
     * Progress value must be between 0.0 and 1.0.
     * It will set both - selected step and step progress values.
     *
     * @param progress total progress
     */
    public void setTotalProgress ( float progress )
    {
        progress = ( steps.size () - 1 ) * progress;
        setSelectedStepIndex ( Math.round ( progress - progress % 1 ) );
        setProgress ( progress % 1 );
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Shape shape = getBorderShape ();

        // Outer shadow
        if ( isEnabled () )
        {
            GraphicsUtils.drawShade ( g2d, shape, ColorUtils.color ( 210, 210, 210 ), shadeWidth );
        }

        // Background
        final Rectangle shapeBounds = shape.getBounds ();
        g2d.setPaint ( new GradientPaint ( 0, shapeBounds.y, Color.WHITE, 0, shapeBounds.y + shapeBounds.height,
                ColorUtils.color ( 223, 223, 223 ) ) );
        g2d.fill ( shape );

        // Border
        g2d.setPaint ( isEnabled () ? Color.GRAY : Color.LIGHT_GRAY );
        g2d.draw ( shape );

        // Progress line
        g2d.setPaint ( getFillPaint () );
        g2d.fill ( getProgressShape () );

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    @Override
    public Shape getShape ()
    {
        return getBorderShape ();
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return false;
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        throw new UnsupportedOperationException ( "Shape detection is not yet supported for WebStepProgress" );
    }

    /**
     * Updates border shape.
     */
    protected void updateBorderShape ()
    {
        borderShape = null;
        repaint ();
    }

    /**
     * Updates fill shape.
     */
    protected void updateFillShape ()
    {
        fillPaint = null;
        fillShape = null;
        repaint ();
    }

    /**
     * Updates all shapes.
     */
    protected void updateShapes ()
    {
        borderShape = null;
        fillPaint = null;
        fillShape = null;
        repaint ();
    }

    /**
     * Returns border shape.
     * If shape is null it will be created.
     *
     * @return border shape
     */
    protected Shape getBorderShape ()
    {
        if ( borderShape == null )
        {
            borderShape = createBorderShape ();
        }
        return borderShape;
    }

    /**
     * Creates and returns border shape.
     *
     * @return newly created border shape
     */
    protected Shape createBorderShape ()
    {
        final Area border = new Area ( getPathShape () );
        for ( int i = 0; i < steps.size (); i++ )
        {
            border.add ( new Area ( getStepBorderShape ( i ) ) );
        }
        return border;
    }

    /**
     * Creates and returns step border shape.
     *
     * @param step step index
     * @return newly created step border shape
     */
    protected Shape getStepBorderShape ( final int step )
    {
        final Point center = getStepCenter ( step );
        if ( stepControlRound * 2 >= stepControlWidth )
        {
            return new Ellipse2D.Double ( center.x - stepControlWidth / 2, center.y - stepControlWidth / 2, stepControlWidth,
                    stepControlWidth );
        }
        else
        {
            return new RoundRectangle2D.Double ( center.x - stepControlWidth / 2, center.y - stepControlWidth / 2, stepControlWidth,
                    stepControlWidth, stepControlRound * 2, stepControlRound * 2 );
        }
    }

    /**
     * Returns fill paint.
     * If paint is null it will be created.
     *
     * @return fill paint
     */
    protected LinearGradientPaint getFillPaint ()
    {
        if ( fillPaint == null )
        {
            fillPaint = createFillPaint ();
        }
        return fillPaint;
    }

    /**
     * Creates and returns fill paint.
     *
     * @return newly created fill paint
     */
    protected LinearGradientPaint createFillPaint ()
    {
        final Point p1 = getPathStart ();
        final float tss = selectedStep + progress;
        final int midSteps = steps.size () - 1;
        final int sw = midSteps > 0 ? getPathLength () / midSteps : 0;
        final float pw = stepControlFillWidth * 1.5f + sw * tss;
        final Color color = isEnabled () ? progressColor : disabledProgressColor;
        if ( orientation == HORIZONTAL )
        {
            final boolean ltr = getComponentOrientation ().isLeftToRight ();
            final float px = ltr ? p1.x - stepControlFillWidth / 2 : p1.x + stepControlFillWidth / 2 - pw;
            final float[] fractions = ltr ? new float[]{ 0f, ( stepControlFillWidth + sw * tss ) / pw, 1f } :
                    new float[]{ 0f, 1 - ( stepControlFillWidth + sw * tss ) / pw, 1f };
            final Color[] colors =
                    ltr ? new Color[]{ color, color, ColorUtils.transparent () } : new Color[]{ ColorUtils.transparent (), color, color };
            return new LinearGradientPaint ( px, 0, px + pw, 0, fractions, colors );
        }
        else
        {
            final float py = p1.y - stepControlFillWidth / 2;
            return new LinearGradientPaint ( 0, py, 0, py + pw, new float[]{ 0f, ( stepControlFillWidth + sw * tss ) / pw, 1f },
                    new Color[]{ color, color, ColorUtils.transparent () } );
        }
    }

    /**
     * Returns progress shape.
     * If shape is null it will be created.
     *
     * @return progress shape
     */
    protected Shape getProgressShape ()
    {
        if ( fillShape == null )
        {
            fillShape = createFillShape ();
        }
        return fillShape;
    }

    /**
     * Creates and returns progress shape.
     *
     * @return newly created progress shape
     */
    protected Shape createFillShape ()
    {
        final Area border = new Area ( getPathFillShape () );
        for ( int i = 0; i < steps.size (); i++ )
        {
            border.add ( new Area ( getStepFillShape ( i ) ) );
        }
        return border;
    }

    /**
     * Creates and returns step fill shape.
     *
     * @param step step index
     * @return newly created step fill shape
     */
    protected Shape getStepFillShape ( final int step )
    {
        final Point center = getStepCenter ( step );
        if ( stepControlFillRound * 2 >= stepControlFillWidth )
        {
            return new Ellipse2D.Double ( center.x - stepControlFillWidth / 2, center.y - stepControlFillWidth / 2, stepControlFillWidth,
                    stepControlFillWidth );
        }
        else
        {
            return new RoundRectangle2D.Double ( center.x - stepControlFillWidth / 2, center.y - stepControlFillWidth / 2,
                    stepControlFillWidth, stepControlFillWidth, stepControlFillRound * 2, stepControlFillRound * 2 );
        }
    }

    /**
     * Creates and returns path shape.
     *
     * @return newly created path shape
     */
    protected Shape getPathShape ()
    {
        final Point p1 = getPathStart ();
        final Point p2 = getPathEnd ();
        if ( orientation == HORIZONTAL )
        {
            final boolean ltr = getComponentOrientation ().isLeftToRight ();
            return new Rectangle2D.Double ( ltr ? p1.x : p2.x, p1.y - pathWidth / 2f, ltr ? p2.x - p1.x : p1.x - p2.x, pathWidth );
        }
        else
        {
            return new Rectangle2D.Double ( p1.x - pathWidth / 2f, p1.y, pathWidth, p2.y - p1.y );
        }
    }

    /**
     * Creates and returns path fill shape.
     *
     * @return newly created path fill shape
     */
    protected Shape getPathFillShape ()
    {
        final Point p1 = getPathStart ();
        final Point p2 = getPathEnd ();
        if ( orientation == HORIZONTAL )
        {
            final boolean ltr = getComponentOrientation ().isLeftToRight ();
            return new Rectangle2D.Double ( ltr ? p1.x : p2.x, p1.y + 0.5f - pathFillWidth / 2f, ltr ? p2.x - p1.x : p1.x - p2.x,
                    pathFillWidth );
        }
        else
        {
            return new Rectangle2D.Double ( p1.x + 0.5f - pathFillWidth / 2f, p1.y, pathFillWidth, p2.y - p1.y );
        }
    }

    /**
     * Returns path start point.
     *
     * @return path start point
     */
    protected Point getPathStart ()
    {
        return getStepCenter ( 0 );
    }

    /**
     * Returns path end point.
     *
     * @return path end point
     */
    protected Point getPathEnd ()
    {
        return getStepCenter ( steps.size () - 1 );
    }

    /**
     * Returns step center point.
     *
     * @param step step index
     * @return step center point
     */
    protected Point getStepCenter ( final int step )
    {
        final Dimension max = getActualLayout ().getMaximumComponentSize ();
        final boolean ltr = getComponentOrientation ().isLeftToRight ();
        final int midSteps = steps.size () - 1;

        if ( orientation == HORIZONTAL )
        {
            final int pathPart = midSteps > 0 ? getPathLength () * ( ltr ? step : midSteps - step ) / midSteps : 0;
            final int x = margin.left + sideWidth + pathPart;
            final int wh = getHeight () - margin.top - margin.bottom;
            final int sh = max.height + ( max.height > 0 ? spacing : 0 );
            final int ch = sh + stepControlWidth + shadeWidth * 2;
            final boolean lead = labelsPosition == LEADING;
            final int y = margin.top + wh / 2 - ch / 2 + ( lead ? sh : 0 ) + shadeWidth + stepControlWidth / 2;
            return new Point ( x, y );
        }
        else
        {
            final int pathPart = midSteps > 0 ? getPathLength () * step / midSteps : 0;
            final int y = margin.top + sideWidth + pathPart;
            final int ww = getWidth () - margin.left - margin.right;
            final int sw = max.width + ( max.width > 0 ? spacing : 0 );
            final int cw = sw + stepControlWidth + shadeWidth * 2;
            final boolean lead = ltr ? labelsPosition == LEADING : labelsPosition == TRAILING;
            final int x = margin.left + ww / 2 - cw / 2 + ( lead ? sw : 0 ) + shadeWidth + stepControlWidth / 2;
            return new Point ( x, y );
        }
    }

    /**
     * Returns path length.
     *
     * @return path length
     */
    protected int getPathLength ()
    {
        return orientation == HORIZONTAL ? getWidth () - sideWidth * 2 - margin.left - margin.right - 1 :
                getHeight () - sideWidth * 2 - margin.top - margin.bottom - 1;
    }

    /**
     * Returns step shape index at the specified point or -1 if none found.
     *
     * @param point point to retrive shape index for
     * @return step shape index at the specified point or -1 if none found
     */
    public int getStepShapeIndexAt ( final Point point )
    {
        for ( int i = 0; i < steps.size (); i++ )
        {
            if ( getStepBorderShape ( i ).contains ( point ) )
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @Override
    public WebStepProgress setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public WebStepProgress setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public WebStepProgress setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public WebStepProgress setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public WebStepProgress setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public WebStepProgress setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebStepProgress setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }

    /**
     * Custom mouse adapter that handles steps and progress changes.
     */
    protected class ProgressMouseAdapter extends MouseAdapter
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

        /**
         * Updates current progress.
         *
         * @param p mouse event location
         */
        protected void updateProgress ( final Point p )
        {
            final float tp = getTotalProgressAt ( p );
            switch ( selectionMode )
            {
                case step:
                {
                    setSelectedStepIndex ( Math.round ( tp ) );
                }
                break;

                case progress:
                {
                    setSelectedStepIndex ( Math.round ( tp - tp % 1 ) );
                    setProgress ( tp % 1 );
                }
                break;
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
     * Returns actual component layout.
     *
     * @return actual component layout
     */
    public ProgressLayout getActualLayout ()
    {
        return ( ProgressLayout ) getLayout ();
    }

    /**
     * Custom WebStepProgress layout that places progress labels properly.
     * It also performs component's preferred size calculations.
     */
    protected class ProgressLayout extends AbstractLayoutManager
    {
        @Override
        public void layoutContainer ( final Container container )
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
                        final int x = sc.x - ps.width / 2;
                        if ( labelsPosition == TOP || labelsPosition == LEADING )
                        {
                            final int y = sc.y - stepControlWidth / 2 - shadeWidth - spacing - ps.height;
                            label.setBounds ( x, y, ps.width, ps.height );
                        }
                        else
                        {
                            final int y = sc.y + stepControlWidth / 2 + shadeWidth + spacing;
                            label.setBounds ( x, y, ps.width, ps.height );
                        }
                    }
                    else
                    {
                        final int y = sc.y - ps.height / 2;
                        if ( labelsPosition == LEFT || ltr && labelsPosition == LEADING || !ltr && labelsPosition == TRAILING )
                        {
                            final int x = sc.x - stepControlWidth / 2 - shadeWidth - spacing - ps.width;
                            label.setBounds ( x, y, ps.width, ps.height );
                        }
                        else
                        {
                            final int x = sc.x + stepControlWidth / 2 + shadeWidth + spacing;
                            label.setBounds ( x, y, ps.width, ps.height );
                        }
                    }
                }
            }
        }

        @Override
        public Dimension preferredLayoutSize ( final Container container )
        {
            final Dimension max = getMaximumComponentSize ();
            final Dimension maxSide = getMaximumSideComponentSize ();
            final int midSteps = steps.size () - 1;

            final Dimension ps;
            if ( orientation == HORIZONTAL )
            {
                // Determine preferred side width
                sideWidth = Math.max ( maxSide.width / 2, stepControlWidth / 2 + shadeWidth );

                // Determine preferred center width
                int maxSpacing = 0;
                for ( int i = 0; i < midSteps; i++ )
                {
                    final Component label1 = steps.get ( i ).getLabel ();
                    final Dimension ps1 = label1 != null ? label1.getPreferredSize () : new Dimension ( 0, 0 );
                    final Component label2 = steps.get ( i + 1 ).getLabel ();
                    final Dimension ps2 = label2 != null ? label2.getPreferredSize () : new Dimension ( 0, 0 );
                    maxSpacing = Math.max ( maxSpacing, ps1.width / 2 + ps2.width / 2 );
                }
                final int w = ( Math.max ( shadeWidth * 2 + stepControlWidth, maxSpacing ) + stepsSpacing ) * midSteps;

                // Determine preferred height
                final int h = shadeWidth * 2 + stepControlWidth + max.height + ( max.height > 0 ? spacing : 0 );

                // Calculating preferred size
                ps = new Dimension ( margin.left + w + sideWidth * 2 + margin.right + 1, margin.top + h + margin.bottom + 1 );
            }
            else
            {
                // Determine preferred side width
                sideWidth = Math.max ( maxSide.height / 2, stepControlWidth / 2 + shadeWidth );

                // Determine preferred center height
                int maxSpacing = 0;
                for ( int i = 0; i < midSteps; i++ )
                {
                    final Component label1 = steps.get ( i ).getLabel ();
                    final Dimension ps1 = label1 != null ? label1.getPreferredSize () : new Dimension ( 0, 0 );
                    final Component label2 = steps.get ( i + 1 ).getLabel ();
                    final Dimension ps2 = label2 != null ? label2.getPreferredSize () : new Dimension ( 0, 0 );
                    maxSpacing = Math.max ( maxSpacing, ps1.height / 2 + ps2.height / 2 );
                }
                final int h = ( Math.max ( shadeWidth * 2 + stepControlWidth, maxSpacing ) + midSteps ) * midSteps;

                // Determine preferred width
                final int w = shadeWidth * 2 + stepControlWidth + max.width + ( max.width > 0 ? spacing : 0 );

                // Calculating preferred size
                ps = new Dimension ( margin.left + w + margin.right + 1, margin.top + h + sideWidth * 2 + margin.bottom + 1 );
            }
            return ps;
        }

        /**
         * Returns maximum component size.
         * Used to determine labels area size.
         *
         * @return maximum component size
         */
        public Dimension getMaximumComponentSize ()
        {
            Dimension max = new Dimension ( 0, 0 );
            if ( displayLabels && steps.size () > 0 )
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

        /**
         * Returns maximum size of side components.
         * It is used to determine progress sides spacing.
         *
         * @return maximum size of side components
         */
        public Dimension getMaximumSideComponentSize ()
        {
            if ( displayLabels && steps.size () > 0 )
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
    }

    /**
     * Returns steps with WebLabel components using the specified step names.
     *
     * @param names step names
     * @return steps with WebLabel components using the specified step names
     */
    public static List<StepData> createSteps ( final String... names )
    {
        final List<StepData> s = new ArrayList<StepData> ();
        for ( final String step : names )
        {
            s.add ( new StepData ( step ) );
        }
        return s;
    }

    /**
     * Returns steps with the specified label components.
     *
     * @param labels label components
     * @return steps with the specified label components
     */
    public static List<StepData> createSteps ( final Component... labels )
    {
        final List<StepData> s = new ArrayList<StepData> ();
        for ( final Component step : labels )
        {
            s.add ( new StepData ( step ) );
        }
        return s;
    }

    /**
     * Returns specified amount of default steps with empty label components.
     *
     * @param amount steps amount
     * @return specified amount of default steps with empty label components
     */
    public static List<StepData> createDefaultData ( final int amount )
    {
        final List<StepData> s = new ArrayList<StepData> ( amount );
        for ( int i = 0; i < amount; i++ )
        {
            s.add ( new StepData () );
        }
        return s;
    }
}