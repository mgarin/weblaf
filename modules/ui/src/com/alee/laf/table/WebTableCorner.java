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

package com.alee.laf.table;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 22.08.11 Time: 13:07
 */

public class WebTableCorner extends JComponent
{
    // todo Create ui and painter

    private boolean right;

    public WebTableCorner ( final boolean right )
    {
        super ();
        this.right = right;
        SwingUtils.setOrientation ( this );
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );


    }


}