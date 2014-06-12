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

package com.alee.global;

import com.alee.utils.ColorUtils;
import com.alee.utils.laf.FocusType;
import com.alee.utils.laf.ShadeType;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

/**
 * Global styles class.
 * Most of the styles listed here are used within other style classes.
 *
 * @author Mikle Garin
 */

public class StyleConstants
{
    /**
     * System text separator.
     */
    public static final String SEPARATOR = ";#&;";

    /**
     * Empty 16x16 icon.
     */
    public static final ImageIcon EMPTY_ICON = new ImageIcon ( StyleConstants.class.getResource ( "icons/empty.png" ) );

    /**
     * Transparent color constant.
     */
    public static final Color transparent = new Color ( 255, 255, 255, 0 );

    /**
     * Highlight colors constants.
     */
    public static final Color redHighlight = new Color ( 255, 0, 0, 48 );
    public static final Color greenHighlight = new Color ( 0, 255, 0, 48 );
    public static final Color blueHighlight = new Color ( 0, 0, 255, 48 );
    public static final Color yellowHighlight = new Color ( 255, 255, 0, 48 );

    /**
     * Components shade painting style.
     */
    public static ShadeType shadeType = ShadeType.simple;
    public static float simpleShadeTransparency = 0.7f;

    /**
     * Disabled component icons transparency.
     */
    public static float disabledIconsTransparency = 0.7f;

    /**
     * Alpha-background settings.
     */
    public static int ALPHA_RECT_SIZE = 10;
    public static Color DARK_ALPHA = new Color ( 204, 204, 204 );
    public static Color LIGHT_ALPHA = new Color ( 255, 255, 255 );

    /**
     * Show hidden files in choosers.
     */
    public static boolean showHiddenFiles = false;

    /**
     * Components animation settings.
     */
    public static boolean animate = true;
    public static long animationDelay = 42L; // ~24 fps
    public static long avgAnimationDelay = 28L; // ~36 fps
    public static long fastAnimationDelay = 21L; // ~48 fps
    public static long maxAnimationDelay = 10L; // 100 fps

    /**
     * Components border settings.
     */
    public static boolean drawBorder = true;
    public static boolean rolloverDarkBorderOnly = false;
    public static int borderWidth = 1;
    public static Color borderColor = new Color ( 170, 170, 170 );
    public static Color innerBorderColor = Color.WHITE;
    public static Color darkBorderColor = Color.GRAY;
    public static Color averageBorderColor = ColorUtils.getIntermediateColor ( borderColor, darkBorderColor, 0.5f );
    public static Color disabledBorderColor = Color.LIGHT_GRAY;

    /**
     * Components focus settings.
     */
    public static boolean drawFocus = true;
    public static Color focusColor = new Color ( 160, 160, 160 );
    public static Color fieldFocusColor = new Color ( 85, 142, 239 );
    public static Color transparentFieldFocusColor = new Color ( 85, 142, 239, 128 );
    public static FocusType focusType = FocusType.fieldFocus;
    public static Stroke fieldFocusStroke = new BasicStroke ( 1.5f );
    public static Stroke focusStroke = new BasicStroke ( 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[]{ 1, 2 }, 0 );

    /**
     * Toggle icon transparency settings.
     */
    public static boolean shadeToggleIcon = false;
    public static float shadeToggleIconTransparency = 0.5f;

    /**
     * Contol buttons highlight settings.
     */
    public static boolean highlightControlButtons = false;

    /**
     * Components decoration settings.
     */
    public static boolean rolloverDecoratedOnly = false;
    public static boolean undecorated = false;

    /**
     * Components corners rounding settings.
     */
    public static int smallRound = 2;
    public static int mediumRound = 3;
    public static int bigRound = 4;
    public static int largeRound = 6;
    public static int decorationRound = 8;

    /**
     * Components shade settings.
     */
    public static boolean rolloverShadeOnly = false;
    public static boolean showDisabledShade = false;
    public static boolean drawShade = true;
    public static int shadeWidth = 2;
    public static int innerShadeWidth = 2;
    public static Color shadeColor = new Color ( 210, 210, 210 );
    public static Color innerShadeColor = new Color ( 190, 190, 190 );

    /**
     * Components opacity settings.
     */
    public static float fullyTransparent = 0f;
    public static float mediumTransparent = 0.85f;
    public static float slightlyTransparent = 0.95f;
    public static float opaque = 1f;

    /**
     * Components content spacing.
     */
    public static int smallLeftRightSpacing = 2;
    public static int leftRightSpacing = 4;
    public static int largeLeftRightSpacing = 8;
    public static Insets emptyMargin = new Insets ( 0, 0, 0, 0 );

    /**
     * Container components settings.
     */
    public static int spacing = 2;
    public static int smallContentSpacing = 1;
    public static int contentSpacing = 2;
    public static int mediumContentSpacing = 4;
    public static int largeContentSpacing = 20;
    public static Color backgroundColor = new Color ( 237, 237, 237 );
    public static Color darkBackgroundColor = new Color ( 230, 230, 230 );

    /**
     * Components text settings.
     */
    public static Color textSelectionColor = new Color ( 210, 210, 210 );
    public static Color textColor = Color.BLACK;
    public static Color selectedTextColor = Color.BLACK;
    public static Color disabledTextColor = new Color ( 160, 160, 160 );
    public static Color infoTextColor = Color.GRAY;
    public static Color disabledInfoTextColor = Color.LIGHT_GRAY;
    public static Color tooltipTextColor = Color.WHITE;

    /**
     * Default text rendering hints.
     */
    public static Map defaultTextRenderingHints =
            new RenderingHints ( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

    /**
     * Text rendering hints.
     */
    public static Map textRenderingHints = defaultTextRenderingHints;

    static
    {
        final Toolkit tk = Toolkit.getDefaultToolkit ();
        textRenderingHints = ( Map ) tk.getDesktopProperty ( "awt.font.desktophints" );
        tk.addPropertyChangeListener ( "awt.font.desktophints", new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                if ( evt.getNewValue () instanceof RenderingHints )
                {
                    textRenderingHints = ( RenderingHints ) evt.getNewValue ();
                }
            }
        } );
    }

    /**
     * Components background settings
     */
    public static Color topBgColor = Color.WHITE;
    public static Color topDarkBgColor = new Color ( 242, 242, 242 );
    public static Color bottomBgColor = new Color ( 223, 223, 223 );
    public static Color topFocusedBgColor = new Color ( 73, 149, 193 );
    public static Color bottomFocusedBgColor = new Color ( 45, 115, 158 );
    public static Color selectedBgColor = new Color ( 223, 220, 213 );
    public static Color topSelectedBgColor = new Color ( 242, 242, 242 );
    public static Color bottomSelectedBgColor = new Color ( 213, 213, 213 );
    public static Color bottomLightSelectedBgColor = Color.WHITE;
    public static Color shineColor = Color.WHITE;

    /**
     * Menu settings.
     */
    public static Color menuSelectionColor = selectedBgColor;
    public static Color rolloverMenuBorderColor = new Color ( 160, 160, 160 );

    /**
     * Separator settings.
     */
    public static Color separatorLightUpperColor = new Color ( 255, 255, 255, 5 );
    public static Color separatorLightColor = Color.WHITE;
    public static Color separatorUpperColor = new Color ( 176, 182, 188, 5 );
    public static Color separatorColor = new Color ( 176, 182, 188 );

    /**
     * Nine-patch editor settings.
     */
    public static Stroke guidelinesStroke = new BasicStroke ( 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[]{ 4, 4 }, 0 );

    /**
     * HTML renderer icons.
     */
    public static ImageIcon htmlPendingIcon = new ImageIcon ( StyleConstants.class.getResource ( "icons/html/pendingImage.png" ) );
    public static ImageIcon htmlMissingIcon = new ImageIcon ( StyleConstants.class.getResource ( "icons/html/missingImage.png" ) );

    /**
     * Debug option.
     */
    public static Font DEBUG_FONT = new Font ( "Dialog", Font.BOLD, 8 );
    public static NumberFormat DEBUG_FORMAT = new DecimalFormat ( "#0.00" );
}