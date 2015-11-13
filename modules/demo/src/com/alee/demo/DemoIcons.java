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

package com.alee.demo;

import com.alee.demo.svg.SvgIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Demo application icons.
 *
 * @author Mikle Garin
 */

public final class DemoIcons
{
    /**
     * Examples frame.
     */
    public static final ImageIcon examples = new ImageIcon ( DemoIcons.class.getResource ( "icons/frames/examples.png" ) );

    /**
     * Type icons.
     */
    public static final ImageIcon group = new ImageIcon ( DemoIcons.class.getResource ( "icons/types/group.png" ) );

    /**
     * General purpose icons.
     */
    private static final Color gc = new Color ( 100, 100, 100 );
    public static final Icon settings = new SvgIcon ( DemoIcons.class, "icons/general/settings.svg", 16, 16, gc );
    public static final Icon style = new SvgIcon ( DemoIcons.class, "icons/general/style.svg", 16, 16, gc );
    public static final Icon source = new SvgIcon ( DemoIcons.class, "icons/general/source.svg", 16, 16, gc );
    public static final Icon inspector = new SvgIcon ( DemoIcons.class, "icons/general/inspector.svg", 16, 16, gc );
    //    public static final Icon settings = new ImageIcon ( Icons.class.getResource ( "icons/general/settings.png" ) );
    //    public static final Icon style = new ImageIcon ( Icons.class.getResource ( "icons/general/style.png" ) );
    //    public static final Icon source = new ImageIcon ( Icons.class.getResource ( "icons/general/source.png" ) );
    //    public static final Icon inspector = new ImageIcon ( Icons.class.getResource ( "icons/general/inspector.png" ) );
}