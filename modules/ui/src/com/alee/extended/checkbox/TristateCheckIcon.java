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

package com.alee.extended.checkbox;

import com.alee.laf.checkbox.SimpleCheckIcon;
import com.alee.utils.GeometryUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.lang.ref.WeakReference;

/**
 * Check icon for tristate checkbox.
 *
 * @author Mikle Garin
 */

public class TristateCheckIcon extends SimpleCheckIcon
{
    /**
     * Painting constants.
     */
    protected static final float[] fractions = { 0f, 1f };
    protected static final Color[] colors = { new Color ( 28, 66, 97 ), new Color ( 55, 84, 108 ) };
    protected static final Color[] disabledColors = { new Color ( 112, 112, 112 ), new Color ( 150, 150, 150 ) };

    /**
     * Tristate checkbox which uses this icon.
     */
    protected WeakReference<WebTristateCheckBox> checkBox;

    /**
     * Current check step.
     */
    protected int checkStep = -1;

    /**
     * Current mixed step.
     */
    protected int mixedStep = -1;

    /**
     * Constructs new tristate check icon.
     *
     * @param checkBox tristate checkbox which uses this icon
     */
    public TristateCheckIcon ( final WebTristateCheckBox checkBox )
    {
        super ();
        this.checkBox = new WeakReference<WebTristateCheckBox> ( checkBox );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNextState ( final CheckState nextState )
    {
        super.setNextState ( nextState );
        switch ( state )
        {
            case checked:
            {
                switch ( nextState )
                {
                    case mixed:
                    {
                        // Checked -> Mixed
                        checkStep = 3;
                        mixedStep = -1;
                        break;
                    }
                    case unchecked:
                    {
                        // Checked -> Unchecked
                        checkStep = 3;
                        mixedStep = -1;
                        break;
                    }
                }
                break;
            }
            case mixed:
            {
                switch ( nextState )
                {
                    case checked:
                    {
                        // Mixed -> Checked
                        mixedStep = 3;
                        checkStep = -1;
                        break;
                    }
                    case unchecked:
                    {
                        // Mixed -> Unchecked
                        mixedStep = 3;
                        checkStep = -1;
                        break;
                    }
                }
                break;
            }
            case unchecked:
            {
                switch ( nextState )
                {
                    case checked:
                    {
                        // Unchecked -> Checked
                        mixedStep = -1;
                        checkStep = -1;
                        break;
                    }
                    case mixed:
                    {
                        // Unchecked -> Mixed
                        mixedStep = -1;
                        checkStep = -1;
                        break;
                    }
                }
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doStep ()
    {
        switch ( state )
        {
            case checked:
            {
                switch ( nextState )
                {
                    case mixed:
                    {
                        // Checked -> Mixed
                        checkStep--;
                        mixedStep++;
                        break;
                    }
                    case unchecked:
                    {
                        // Checked -> Unchecked
                        checkStep--;
                        break;
                    }
                }
                break;
            }
            case mixed:
            {
                switch ( nextState )
                {
                    case checked:
                    {
                        // Mixed -> Checked
                        mixedStep--;
                        checkStep++;
                        break;
                    }
                    case unchecked:
                    {
                        // Mixed -> Unchecked
                        mixedStep--;
                        break;
                    }
                }
                break;
            }
            case unchecked:
            {
                switch ( nextState )
                {
                    case checked:
                    {
                        // Unchecked -> Checked
                        checkStep++;
                        break;
                    }
                    case mixed:
                    {
                        // Unchecked -> Mixed
                        mixedStep++;
                        break;
                    }
                }
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetStep ()
    {
        switch ( state )
        {
            case checked:
            {
                checkStep = 3;
                mixedStep = -1;
                break;
            }
            case mixed:
            {
                mixedStep = 3;
                checkStep = -1;
                break;
            }
            case unchecked:
            {
                checkStep = -1;
                mixedStep = -1;
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTransitionCompleted ()
    {
        switch ( state )
        {
            case checked:
            {
                switch ( nextState )
                {
                    case mixed:
                    {
                        // Checked -> Mixed
                        return mixedStep == 3;
                    }
                    case unchecked:
                    {
                        // Checked -> Unchecked
                        return checkStep == -1;
                    }
                }
                break;
            }
            case mixed:
            {
                switch ( nextState )
                {
                    case checked:
                    {
                        // Mixed -> Checked
                        return checkStep == 3;
                    }
                    case unchecked:
                    {
                        // Mixed -> Unchecked
                        return mixedStep == -1;
                    }
                }
                break;
            }
            case unchecked:
            {
                switch ( nextState )
                {
                    case checked:
                    {
                        // Unchecked -> Checked
                        return checkStep == 3;
                    }
                    case mixed:
                    {
                        // Unchecked -> Mixed
                        return mixedStep == 3;
                    }
                }
                break;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintIcon ( final Component c, final Graphics2D g2d, final int x, final int y, final int w, final int h )
    {
        final WebTristateCheckBox cb = checkBox.get ();
        if ( cb != null )
        {
            // Mixed icon
            if ( mixedStep > -1 )
            {
                final int r = 3 - mixedStep;
                final int s = cb.getShadeWidth () + 3;
                final Rectangle b = new Rectangle ( x + s, y + s, w - s * 2, h - s * 2 );
                final RoundRectangle2D shape = new RoundRectangle2D.Double ( b.x + r, b.y + r, b.width - r * 2, b.height - r * 2, 3, 3 );
                final Point center = GeometryUtils.middle ( b );
                final int radius = Math.max ( b.width / 2, b.height / 2 );
                g2d.setPaint ( new RadialGradientPaint ( center, radius, fractions, cb.isEnabled () ? colors : disabledColors ) );
                g2d.fill ( shape );
            }

            // Check icon
            if ( checkStep > -1 )
            {
                final ImageIcon icon = enabled ? CHECK_STATES.get ( checkStep ) : DISABLED_CHECK_STATES.get ( checkStep );
                g2d.drawImage ( icon.getImage (), x + w / 2 - getIconWidth () / 2, y + h / 2 - getIconHeight () / 2, null );
            }
        }
    }
}