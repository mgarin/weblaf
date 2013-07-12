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

package com.alee.laf.rootpane;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButtonStyle;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * WebRootPane style class.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public final class WebRootPaneStyle
{
    /**
     * Top window background.
     */
    public static Color topBg = new Color ( 244, 244, 244 );

    /**
     * Middle window background.
     */
    public static Color middleBg = new Color ( 235, 235, 235 );

    /**
     * Window border color.
     */
    public static Color borderColor = StyleConstants.borderColor;

    /**
     * Window inner border color.
     */
    public static Color innerBorderColor = StyleConstants.innerBorderColor;

    /**
     * Active window shade width.
     */
    public static int shadeWidth = SwingUtils.isWindowTransparencyAllowed () ? 25 : 0;

    /**
     * Inactive window shade width.
     */
    public static int inactiveShadeWidth = SwingUtils.isWindowTransparencyAllowed () ? 15 : 0;

    /**
     * Window shape rounding.
     */
    public static int round = SwingUtils.isWindowTransparencyAllowed () ? 5 : 0;

    /**
     * Window content margin.
     */
    public static Insets margin = new Insets ( 0, 0, 0, 0 );

    /**
     * Draw window background watermark.
     */
    public static boolean drawWatermark = false;

    /**
     * Custom watermark image.
     */
    public static ImageIcon watermark = null;

    /**
     * Display window title component by default.
     */
    public static boolean showTitleComponent = true;

    /**
     * Display window menu bar by default.
     */
    public static boolean showMenuBar = true;

    /**
     * Display window buttons by default.
     */
    public static boolean showWindowButtons = true;

    /**
     * Display window buttons by default.
     */
    public static boolean showMinimizeButton = true;

    /**
     * Display window buttons by default.
     */
    public static boolean showMaximizeButton = true;

    /**
     * Group window buttons.
     */
    public static boolean groupButtons = true;

    /**
     * Attach window buttons to window side.
     */
    public static boolean attachButtons = true;

    /**
     * Buttons round, if null round is taken from window round.
     */
    public static Integer buttonsRound = null;

    /**
     * Buttons shade width.
     */
    public static int buttonsShadeWidth = WebButtonStyle.shadeWidth;

    /**
     * Buttons margin.
     */
    public static Insets buttonsMargin = new Insets ( 1, 4, 1, 4 );

    /**
     * Buttons painter.
     */
    public static Painter buttonsPainter = null;

    /**
     * Display window buttons by default.
     */
    public static boolean showCloseButton = true;

    /**
     * Display window resize corner by default.
     */
    public static boolean showResizeCorner = true;
}