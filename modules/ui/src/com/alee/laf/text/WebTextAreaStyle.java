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

import javax.swing.*;
import java.awt.*;

/**
 * WebTextArea style class.
 *
 * @author Mikle Garin
 */

public final class WebTextAreaStyle
{
    /**
     * Text area margin.
     */
    public static Insets margin = new Insets ( 2, 2, 2, 2 );

    /**
     * Input prompt text.
     * Set to null to disable input prompt.
     */
    public static String inputPrompt = null;

    /**
     * Input prompt text font.
     * Set to null to use the same font area uses.
     */
    public static Font inputPromptFont = null;

    /**
     * Input prompt text foreground.
     * Set to null to use the same foreground area uses.
     */
    public static Color inputPromptForeground = new Color ( 160, 160, 160 );

    /**
     * Input prompt text position.
     */
    public static int inputPromptHorizontalPosition = SwingConstants.CENTER;

    /**
     * Input prompt text position.
     */
    public static int inputPromptVerticalPosition = SwingConstants.CENTER;

    /**
     * Whether should hide input prompt when field is focused or not.
     */
    public static boolean hideInputPromptOnFocus = true;

    /**
     * Text area painter.
     * If set it will override WebLaF styling.
     */
    public static Painter painter = null;

    /**
     * Background color.
     */
    public static Color backgroundColor = Color.WHITE;

    /**
     * Foreground color.
     */
    public static Color foregroundColor = Color.BLACK;

    /**
     * Selected text color.
     */
    public static Color selectedTextColor = Color.BLACK;

    /**
     * Selected text color.
     */
    public static Color caretColor = Color.GRAY;
}