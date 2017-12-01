package com.alee.extended.panel;

import com.alee.laf.panel.WebPanelUI;
import com.alee.managers.style.Bounds;
import com.alee.painter.AbstractPainter;
import com.alee.utils.ColorUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.awt.*;

/**
 * Custom painter for WebSelectablePanel panel.
 *
 * @author Mikle Garin
 */

@XStreamAlias ( "SelectablePanelPainter" )
public class SelectablePanelPainter extends AbstractPainter<WebSelectablePanel, WebPanelUI>
{
    public static final int GRIPPER_SIZE = 7;
    public static final int SINGLE_GRIPPER_STEP = 4;

    /**
     * Style settings.
     */
    protected float[] fractions = { 0f, 0.25f, 0.75f, 1f };
    protected Color[] lightColors = { ColorUtils.transparent (), Color.WHITE, Color.WHITE, ColorUtils.transparent () };
    protected Color[] darkColors = { ColorUtils.transparent (), Color.GRAY, Color.GRAY, ColorUtils.transparent () };

    @Override
    public Boolean isOpaque ()
    {
        return true;
    }

    @Override
    protected Insets getBorder ()
    {
        return new Insets ( 0, GRIPPER_SIZE, 0, 0 );
    }

    @Override
    public void paint ( final Graphics2D g2d, final WebSelectablePanel panel, final WebPanelUI ui, final Bounds b )
    {
        final boolean notFirst = panel.getIndex () > 0;
        final boolean notLast = panel.getIndex () < component.getTotal () - 1;
        final Rectangle bounds = b.get ();

        if ( panel.isFocused () )
        {
            // Background
            g2d.setPaint ( new GradientPaint ( bounds.x, bounds.y, Color.WHITE, bounds.x, bounds.y + bounds.height,
                    new Color ( 223, 223, 223 ) ) );
            g2d.fill ( bounds );

            // Borders
            final Integer shift = panel.getComponentPane ().getContainerLayout ().getComponentShift ( panel );
            final boolean moved = panel.isDragged () && shift != null && shift != 0;
            g2d.setPaint ( Color.GRAY );
            if ( notFirst || moved )
            {
                g2d.drawLine ( bounds.x, bounds.y, bounds.x + bounds.width - 1, bounds.y );
            }
            if ( notLast || moved )
            {
                g2d.drawLine ( bounds.x, bounds.y + bounds.height - 1, bounds.x + bounds.width - 1, bounds.y + bounds.height - 1 );
            }
        }

        // Painting gripper
        if ( panel.getComponentPane ().isReorderingAllowed () && panel.getComponentPane ().isShowReorderGrippers () )
        {
            // Determining coordinates
            final int minY = bounds.y + 2 + ( notFirst ? 1 : 0 );
            final int maxY = bounds.x + bounds.height - 2 - ( notLast ? 1 : 0 );
            final int x = ltr ? bounds.x + 3 : bounds.x + bounds.width - GRIPPER_SIZE + 2;
            int y = minY + ( ( maxY - minY ) % SINGLE_GRIPPER_STEP ) / 2;

            // Painters
            final Paint light = new LinearGradientPaint ( x, minY, x, maxY, fractions, lightColors );
            final Paint dark = new LinearGradientPaint ( x, minY, x, maxY, fractions, darkColors );

            // Paint cycle
            while ( y + 3 < maxY )
            {
                g2d.setPaint ( light );
                g2d.fillRect ( x + 1, y + 1, 2, 2 );
                g2d.setPaint ( dark );
                g2d.fillRect ( x, y, 2, 2 );
                y += SINGLE_GRIPPER_STEP;
            }
        }
    }
}