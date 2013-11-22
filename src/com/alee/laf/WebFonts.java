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

package com.alee.laf;

import com.alee.utils.SystemUtils;

import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class returns base fonts for all component types on various OS.
 * This is some kind of workaround since there is no good way to retrieve native component fonts from OS itself.
 * Usually it is native L&amp;F who setup system fonts but inside it simply has hardcoded fonts, which is not really different from my way.
 *
 * @author Mikle Garin
 */

public class WebFonts
{
    /**
     * Windows fonts.
     */
    public static final String TAHOMA = "Tahoma";
    public static final String SEGOE_UI = "Segoe UI";

    /**
     * Mac OS X fonts.
     */
    public static final String LUCIDA_GRANDE = "Lucida Grande";

    /**
     * Unix fonts.
     */
    public static final String SANS_SERIF = Font.SANS_SERIF;

    /**
     * Control components font (buttons, sliders and such).
     */
    private static final String CONTROL = "control";

    /**
     * Alert dialogs font (usually used for option pane only).
     */
    private static final String ALERT = "alert";

    /**
     * Menu components font.
     */
    private static final String MENU = "menu";

    /**
     * Accelerator text font.
     */
    private static final String ACCELERATOR = "accelerator";

    /**
     * Custom window and internal frame titles font.
     */
    private static final String TITLE = "title";

    /**
     * Text editor components font.
     */
    private static final String TEXT = "text";

    /**
     * Tooltips font.
     */
    private static final String TOOLTIP = "tooltip";

    /**
     * Current system fonts map.
     */
    private static final Map<String, FontUIResource> fonts = new HashMap<String, FontUIResource> ();

    /**
     * Initializing system fonts.
     */
    static
    {
        if ( SystemUtils.isWindows () )
        {
            // Win8
            // Win7
            fonts.put ( CONTROL, new FontUIResource ( TAHOMA, Font.PLAIN, 12 ) );
            fonts.put ( ALERT, new FontUIResource ( SEGOE_UI, Font.PLAIN, 13 ) );
            fonts.put ( MENU, new FontUIResource ( SEGOE_UI, Font.PLAIN, 12 ) );
            fonts.put ( ACCELERATOR, new FontUIResource ( SEGOE_UI, Font.PLAIN, 12 ) );
            fonts.put ( TITLE, new FontUIResource ( SEGOE_UI, Font.PLAIN, 14 ) );
            fonts.put ( TEXT, new FontUIResource ( TAHOMA, Font.PLAIN, 12 ) );
            fonts.put ( TOOLTIP, new FontUIResource ( SEGOE_UI, Font.PLAIN, 12 ) );
        }
        else if ( SystemUtils.isMac () )
        {
            // Mac OS X
            fonts.put ( CONTROL, new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 13 ) );
            fonts.put ( ALERT, new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 11 ) );
            fonts.put ( MENU, new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 14 ) );
            fonts.put ( ACCELERATOR, new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 13 ) );
            fonts.put ( TITLE, new FontUIResource ( LUCIDA_GRANDE, Font.BOLD, 14 ) );
            fonts.put ( TEXT, new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 13 ) );
            fonts.put ( TOOLTIP, new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 11 ) );
        }
        else
        {
            // Unix systems
            fonts.put ( CONTROL, new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 ) );
            fonts.put ( ALERT, new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 ) );
            fonts.put ( MENU, new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 ) );
            fonts.put ( ACCELERATOR, new FontUIResource ( SANS_SERIF, Font.PLAIN, 11 ) );
            fonts.put ( TITLE, new FontUIResource ( SANS_SERIF, Font.BOLD, 12 ) );
            fonts.put ( TEXT, new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 ) );
            fonts.put ( TOOLTIP, new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 ) );
        }
    }

    /**
     * Returns system controls font.
     *
     * @return system controls font
     */
    public static FontUIResource getSystemControlFont ()
    {
        return fonts.get ( CONTROL );
    }

    /**
     * Returns system alert dialog font.
     *
     * @return system alert dialog font
     */
    public static FontUIResource getSystemAlertFont ()
    {
        return fonts.get ( ALERT );
    }

    /**
     * Returns system menu font.
     *
     * @return system menu font
     */
    public static FontUIResource getSystemMenuFont ()
    {
        return fonts.get ( MENU );
    }

    /**
     * Returns system accelerator font.
     *
     * @return system accelerator font
     */
    public static FontUIResource getSystemAcceleratorFont ()
    {
        return fonts.get ( ACCELERATOR );
    }

    /**
     * Returns system title font.
     *
     * @return system title font
     */
    public static FontUIResource getSystemTitleFont ()
    {
        return fonts.get ( TITLE );
    }

    /**
     * Returns system text font.
     *
     * @return system text font
     */
    public static FontUIResource getSystemTextFont ()
    {
        return fonts.get ( TEXT );
    }

    /**
     * Returns system tooltip font.
     *
     * @return system tooltip font
     */
    public static FontUIResource getSystemTooltipFont ()
    {
        return fonts.get ( TOOLTIP );
    }
}
