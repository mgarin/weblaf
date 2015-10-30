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

package com.alee.laf.radiobutton;

import com.alee.extended.checkbox.CheckState;
import com.alee.global.StyleConstants;
import com.alee.laf.checkbox.CheckIcon;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Check icon for simple radiobutton.
 *
 * @author Mikle Garin
 */

public class RadioStateIcon extends CheckIcon
{
    /**
     * Check icons for all states.
     */
    public static List<ImageIcon> CHECK_STATES = new ArrayList<ImageIcon> ();
    public static ImageIcon DISABLED_CHECK_STATE = null;

    /**
     * Check icons initialization.
     */
    static
    {
        CHECK_STATES.add ( StyleConstants.EMPTY_ICON );
        for ( int i = 1; i <= 3; i++ )
        {
            CHECK_STATES.add ( new ImageIcon ( WebRadioButtonUI.class.getResource ( "icons/c" + i + ".png" ) ) );
        }

        DISABLED_CHECK_STATE =
                ImageUtils.getDisabledCopy ( "WebRadioButton.disabled.check", CHECK_STATES.get ( CHECK_STATES.size () - 1 ) );
    }


    /**
     * Current step.
     */
    protected int step = -1;

    @Override
    public void doStep ()
    {
        step = nextState == CheckState.checked ? step + 1 : step - 1;
    }

    @Override
    public void resetStep ()
    {
        step = state == CheckState.checked ? 3 : -1;
    }

    @Override
    public boolean isTransitionCompleted ()
    {
        return nextState == CheckState.unchecked && step == -1 || nextState == CheckState.checked && step == 3;
    }

    @Override
    public void finishTransition ()
    {
        this.state = nextState;
        this.nextState = null;
    }

    @Override
    public int getIconWidth ()
    {
        return 16;
    }

    @Override
    public int getIconHeight ()
    {
        return 16;
    }

    @Override
    public void paintIcon ( final Component c, final Graphics2D g2d, final int x, final int y, final int w, final int h )
    {
        if ( step > -1 )
        {
            final ImageIcon icon = enabled ? CHECK_STATES.get ( step ) : DISABLED_CHECK_STATE;
            final int xofs = ( w - getIconWidth () ) / 2 + ( ( w & 1 ) == 1 ? 1 : 0 );
            final int yofs = ( h - getIconHeight () ) / 2 + ( h > 16 ? ( h & 1 ) == 1 && h > 18 ? 2 : 1 : 0 );
            g2d.drawImage ( icon.getImage (), x + xofs, y + yofs, null );
        }
    }
}