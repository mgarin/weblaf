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

package com.alee.laf.scroll;

import javax.swing.*;
import java.awt.*;

/**
 * This is a ScrollPaneLayout extension that properly places scroll pane shade layer.
 *
 * @author Mikle Garin
 */

public class WebScrollPaneLayout extends ScrollPaneLayout
{
    /**
     * Shade layer component.
     */
    private final JComponent shadeLayer;

    /**
     * Constructs new WebScrollPaneLayout with the specified shade layer.
     *
     * @param shadeLayer
     */
    public WebScrollPaneLayout ( final JComponent shadeLayer )
    {
        super ();
        this.shadeLayer = shadeLayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( final Container parent )
    {
        super.layoutContainer ( parent );

        shadeLayer.setBounds ( new Rectangle ( viewport.getBounds () ) );
        parent.setComponentZOrder ( shadeLayer, 0 );
    }

    /**
     * The UI resource version of <code>WebScrollPaneLayout</code>.
     */
    public static class UIResource extends WebScrollPaneLayout implements javax.swing.plaf.UIResource
    {
        public UIResource ( final JComponent shadeLayer )
        {
            super ( shadeLayer );
        }
    }
}