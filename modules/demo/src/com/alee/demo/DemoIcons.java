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
     * SVG icon colors.
     */
    private static final Color general = new Color ( 0, 0, 0 );
    private static final Color common = new Color ( 100, 100, 100 );
    private static final Color featured = new Color ( 100,180,100 );

    /**
     * General purpose icons.
     */
    public static final Icon legend = new SvgIcon ( DemoIcons.class, "icons/general/legend.svg", 16, 16, featured );
    public static final Icon github = new SvgIcon ( DemoIcons.class, "icons/general/github.svg", 16, 16, common );
    public static final Icon compass = new SvgIcon ( DemoIcons.class, "icons/general/compass.svg", 36, 36, common );
    public static final Icon bug = new SvgIcon ( DemoIcons.class, "icons/general/bug.svg", 36, 36, common );
    public static final Icon settings = new SvgIcon ( DemoIcons.class, "icons/general/settings.svg", 16, 16, common );
    public static final Icon style = new SvgIcon ( DemoIcons.class, "icons/general/style.svg", 16, 16, common );
    public static final Icon source = new SvgIcon ( DemoIcons.class, "icons/general/source.svg", 16, 16, common );
    public static final Icon inspector = new SvgIcon ( DemoIcons.class, "icons/general/inspector.svg", 16, 16, common );

    /**
     * Examples frame.
     */
    public static final Icon examples = new SvgIcon ( DemoIcons.class, "icons/frames/examples.svg", 16, 16, general );

    /**
     * Type icons.
     */
    public static final ImageIcon group = new ImageIcon ( DemoIcons.class.getResource ( "icons/types/group.png" ) );
}