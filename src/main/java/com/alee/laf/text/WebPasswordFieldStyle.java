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

package com.alee.laf.text;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 18.12.12 Time: 15:37
 */

public final class WebPasswordFieldStyle
{
    /**
     * Should draw border
     */
    public static boolean drawBorder = StyleConstants.drawBorder;

    /**
     * Should draw focus
     */
    public static boolean drawFocus = StyleConstants.drawFocus;

    /**
     * Default corners rounding
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Should draw shade
     */
    public static boolean drawShade = StyleConstants.drawShade;

    /**
     * Default shade width
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Fill decoration background
     */
    public static boolean drawBackground = true;

    /**
     * Web-styled background
     */
    public static boolean webColored = false;

    /**
     * Default label background painter
     */
    public static Painter painter = StyleConstants.painter;

    /**
     * Default content spacing
     */
    public static int componentSpacing = StyleConstants.contentSpacing;

    /**
     * Default margin
     */
    public static Insets margin = new Insets ( 0, 0, 0, 0 );

    /**
     * Default field margin
     */
    public static Insets fieldMargin = new Insets ( 2, 2, 2, 2 );

    /**
     * Input prompt text (null = none)
     */
    public static String inputPrompt = null;

    /**
     * Input prompt text font (null = same as the text component font)
     */
    public static Font inputPromptFont = null;

    /**
     * Input prompt text foreground (null = same as the text component)
     */
    public static Color inputPromptForeground = new Color ( 180, 180, 180 );

    /**
     * Input prompt text position
     */
    public static int inputPromptPosition = SwingConstants.LEADING;

    /**
     * Hide input prompt text on focus
     */
    public static boolean hideInputPromptOnFocus = true;
}