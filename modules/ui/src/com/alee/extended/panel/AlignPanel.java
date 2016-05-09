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

package com.alee.extended.panel;

import com.alee.extended.layout.AlignLayout;
import com.alee.managers.style.StyleId;
import com.alee.laf.panel.WebPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Custom panel that aligns contained component.
 *
 * @author Mikle Garin
 */

public class AlignPanel extends WebPanel implements SwingConstants
{
    /**
     * Constructs new align panel.
     *
     * @param component component to align
     * @param halign    horizontal alignment
     * @param valign    vertical alignment
     */
    public AlignPanel ( final Component component, final int halign, final int valign )
    {
        super ( StyleId.panelTransparent, new AlignLayout () );
        add ( component, halign + AlignLayout.SEPARATOR + valign );
    }
}