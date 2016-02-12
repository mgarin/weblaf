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

package com.alee.demo.icons;

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
    private static final Color featured = new Color ( 100, 180, 100 );
    private static final Color golden = new Color ( 228, 202, 44 );

    /**
     * General purpose 36x36 icons.
     */
    public static final SvgIcon compass36 = new SvgIcon ( DemoIcons.class, "general/compass.svg", 36, 36, common );
    public static final SvgIcon bug36 = new SvgIcon ( DemoIcons.class, "general/bug.svg", 36, 36, common );
    public static final SvgIcon github36 = new SvgIcon ( DemoIcons.class, "general/github.svg", 36, 36, common );

    /**
     * General purpose 16x16 icons.
     */
    public static final SvgIcon legend16 = new SvgIcon ( DemoIcons.class, "general/legend.svg", 16, 16, featured );
    public static final SvgIcon menu16 = new SvgIcon ( DemoIcons.class, "general/menu.svg", 16, 16, common );
    public static final SvgIcon github16 = new SvgIcon ( DemoIcons.class, "general/github.svg", 16, 16, common );
    public static final SvgIcon gitter16 = new SvgIcon ( DemoIcons.class, "general/gitter.svg", 16, 16, common );
    public static final SvgIcon settings16 = new SvgIcon ( DemoIcons.class, "general/settings.svg", 16, 16, common );
    public static final SvgIcon style16 = new SvgIcon ( DemoIcons.class, "general/style.svg", 16, 16, common );
    public static final SvgIcon source16 = new SvgIcon ( DemoIcons.class, "general/source.svg", 16, 16, common );
    public static final SvgIcon inspector16 = new SvgIcon ( DemoIcons.class, "general/inspector.svg", 16, 16, common );
    public static final SvgIcon key16 = new SvgIcon ( DemoIcons.class, "general/key.svg", 16, 16, golden );
    public static final SvgIcon dollar16 = new SvgIcon ( DemoIcons.class, "general/dollar.svg", 16, 16, common );
    public static final SvgIcon phone16 = new SvgIcon ( DemoIcons.class, "general/phone.svg", 16, 16, common );
    public static final SvgIcon hourglass16 = new SvgIcon ( DemoIcons.class, "general/hourglass.svg", 16, 16, common );
    public static final SvgIcon arrowRight16 = new SvgIcon ( DemoIcons.class, "general/arrow-right.svg", 16, 16, common );

    /**
     * Social 16x16 icons.
     */
    public static final SvgIcon facebook16 = new SvgIcon ( DemoIcons.class, "social/facebook.svg", 16, 16, new Color ( 69, 97, 157 ) );
    public static final SvgIcon twitter16 = new SvgIcon ( DemoIcons.class, "social/twitter.svg", 16, 16, new Color ( 85, 172, 238 ) );
    public static final SvgIcon googleplus16 = new SvgIcon ( DemoIcons.class, "social/googleplus.svg", 16, 16, new Color ( 219, 68, 55 ) );
    public static final SvgIcon linkedin16 = new SvgIcon ( DemoIcons.class, "social/linkedin.svg", 16, 16, new Color ( 0, 119, 181 ) );
    public static final SvgIcon pinterest16 = new SvgIcon ( DemoIcons.class, "social/pinterest.svg", 16, 16, new Color ( 188, 33, 37 ) );
    public static final SvgIcon youtube16 = new SvgIcon ( DemoIcons.class, "social/youtube.svg", 16, 16, new Color ( 230, 33, 23 ) );
    public static final SvgIcon vimeo16 = new SvgIcon ( DemoIcons.class, "social/vimeo.svg", 16, 16, new Color ( 0, 173, 239 ) );

    /**
     * Examples frame.
     */
    public static final SvgIcon examples16 = new SvgIcon ( DemoIcons.class, "frames/examples.svg", 16, 16, general );

    /**
     * Type icons.
     */
    public static final ImageIcon group = new ImageIcon ( DemoIcons.class.getResource ( "types/group.png" ) );
}