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

package com.alee.extended.painter;

import javax.swing.*;
import java.awt.*;

/**
 * Dashed border painter.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see BorderPainter
 * @see DefaultPainter
 * @see Painter
 * @since 1.4
 */

public class DashedBorderPainter<E extends JComponent> extends BorderPainter<E>
{
    /**
     * Array representing dashing pattern.
     */
    private float[] dash = DashedBorderPainterStyle.dash;

    /**
     * Offset to start dashing pattern.
     */
    private float dashPhase = DashedBorderPainterStyle.dashPhase;

    /**
     * Constructs default dashed border painter.
     */
    public DashedBorderPainter ()
    {
        super ();
        updateStroke ();
    }

    /**
     * Constructs dashed border painter with specified dashing pattern.
     *
     * @param dash dashing pattern
     */
    public DashedBorderPainter ( float[] dash )
    {
        super ();
        setDash ( dash );
    }

    /**
     * Constructs dashed border painter with specified dashing pattern and offset.
     *
     * @param dash      dashing pattern
     * @param dashPhase dashing pattern offset
     */
    public DashedBorderPainter ( float[] dash, float dashPhase )
    {
        super ();
        setDash ( dash );
        setDashPhase ( dashPhase );
    }

    /**
     * Returns dashing pattern.
     *
     * @return dashing pattern
     */
    public float[] getDash ()
    {
        return dash;
    }

    /**
     * Sets dashing pattern.
     * This will also force stroke to update and overwrite old stroke value.
     *
     * @param dash new dashing pattern
     */
    public void setDash ( float[] dash )
    {
        this.dash = dash;
        updateStroke ();
    }

    /**
     * Returns dashing pattern offset.
     *
     * @return dashing pattern offset
     */
    public float getDashPhase ()
    {
        return dashPhase;
    }

    /**
     * Sets dashing pattern offset.
     * This will also force stroke to update and overwrite old stroke value.
     *
     * @param dashPhase new dashing pattern offset
     */
    public void setDashPhase ( float dashPhase )
    {
        this.dashPhase = dashPhase;
        updateStroke ();
    }

    /**
     * Updates border stroke depending on painter settings.
     */
    protected void updateStroke ()
    {
        stroke = new BasicStroke ( getWidth (), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f, getDash (), getDashPhase () );
    }
}