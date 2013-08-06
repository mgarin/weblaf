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
import com.alee.laf.panel.WebPanel;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 19.12.12 Time: 19:31
 */

public class AlignPanel extends WebPanel implements SwingConstants
{
    public AlignPanel ( Component component, int halign, int valign )
    {
        super ( new AlignLayout () );
        setOpaque ( false );
        add ( component, halign + AlignLayout.SEPARATOR + valign );
    }
}
