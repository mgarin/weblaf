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

package com.alee.extended.inspector.info;

import com.alee.extended.heatmap.HeatMap;

import javax.swing.*;

/**
 * Default {@link JComponent} information provider.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class JComponentPreview<C extends JComponent> extends AWTComponentPreview<C>
{
    /**
     * Additional type icons.
     */
    public static final ImageIcon layeredPaneType = new ImageIcon ( JComponentPreview.class.getResource ( "icons/layeredpane.png" ) );
    public static final ImageIcon heatMapType = new ImageIcon ( JComponentPreview.class.getResource ( "icons/heatmap.png" ) );

    @Override
    public Icon getIconImpl ( final C component )
    {
        if ( component instanceof JLayeredPane )
        {
            return layeredPaneType;
        }
        else if ( component instanceof HeatMap )
        {
            return heatMapType;
        }
        else
        {
            return super.getIconImpl ( component );
        }
    }
}