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

package com.alee.managers.language;

import com.alee.managers.language.data.Tooltip;
import com.alee.managers.language.data.TooltipType;
import com.alee.managers.language.data.Value;

import javax.swing.*;
import java.awt.*;

/**
 * Swing tooltips language support.
 *
 * @author Mikle Garin
 */

public class SwingTooltipLanguageSupport implements TooltipLanguageSupport
{
    @Override
    public void setupTooltip ( final Component component, final Value value )
    {
        final boolean swingComponent = component instanceof JComponent;
        if ( swingComponent )
        {
            final JComponent jComponent = ( JComponent ) component;

            // Clearing tooltip
            jComponent.setToolTipText ( null );

            // Installing new tooltip
            if ( value != null && value.getTooltips () != null && value.getTooltips ().size () > 0 )
            {
                for ( final Tooltip tooltip : value.getTooltips () )
                {
                    if ( tooltip.getType ().equals ( TooltipType.swing ) )
                    {
                        if ( swingComponent )
                        {
                            jComponent.setToolTipText ( tooltip.getText () );
                        }
                    }
                }
            }
        }
    }
}