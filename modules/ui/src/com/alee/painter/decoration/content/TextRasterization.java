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

package com.alee.painter.decoration.content;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * This enumeration represents available text rasterization options.
 * Its main purpose is to conveniently provide access to the different text rendering hints.
 *
 * @author Mikle Garin
 * @see <a href="https://en.wikipedia.org/wiki/Font_rasterization">Font rasterization</a>
 */

public enum TextRasterization
{
    /**
     * Rasterization option providing no additional rendering hints.
     * This might only be useful if you don't want to use any kind of rendering hints for your text for any reason.
     */
    none ( 0 ),

    /**
     * Basic rasterization option offering default text rendering hints.
     * This option is usually better for larger/bold fonts and worse for smaller/thin ones.
     */
    basic ( 1 ),

    /**
     * Rasterization option providing subpixel text rendering hints.
     * This option is usually better for smaller/thin fonts and worse for larger/bold ones.
     *
     * Note that this option might not be available on some systems and will then be replaced with {@link #basic} one.
     * Also note that this option only works when text is painted on an opaque destination, for example on an opaque image.
     * In case this option is chosen but painting is performed on a non-opaque destination it will be replaced by {@link #basic} option.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Subpixel_rendering">Subpixel rendering</a>
     * @see <a href="https://github.com/mgarin/weblaf/issues/130">Text rendering issues on non-opaque destination</a>
     */
    subpixel ( 2 );

    /**
     * Text rendering hints offered by this text rasterization option.
     */
    private Map renderingHints;

    /**
     * Constructs text rasterization option.
     *
     * @param type rasterization type
     */
    private TextRasterization ( final int type )
    {
        if ( type == 0 )
        {
            // No rendering hints
            setupHints ( new RenderingHints ( new HashMap<RenderingHints.Key, Object> ( 0 ) ) );
        }
        else if ( type == 1 )
        {
            // Default rendering hints
            setupHints ( getDefaultHints () );
        }
        else if ( type == 2 )
        {
            try
            {
                // Native rendering hints, mostly providing subpixel rendering
                final Toolkit toolkit = Toolkit.getDefaultToolkit ();
                final String hintsProperty = "awt.font.desktophints";
                setupHints ( ( RenderingHints ) toolkit.getDesktopProperty ( hintsProperty ) );

                // Ensure native hints are updated on change
                toolkit.addPropertyChangeListener ( hintsProperty, new PropertyChangeListener ()
                {
                    @Override
                    public void propertyChange ( final PropertyChangeEvent evt )
                    {
                        if ( evt.getNewValue () instanceof RenderingHints )
                        {
                            setupHints ( ( RenderingHints ) evt.getNewValue () );
                        }
                    }
                } );
            }
            catch ( final Exception e )
            {
                // Fallback to default rendering hints
                setupHints ( getDefaultHints () );
            }
        }
    }

    /**
     * Setups provided rendering hints.
     *
     * @param hints rendering hints
     */
    private void setupHints ( final Map hints )
    {
        if ( hints != null )
        {
            renderingHints = hints;
        }
        else
        {
            renderingHints = getDefaultHints ();
        }
    }

    /**
     * Returns default rendering hints.
     *
     * @return default rendering hints
     */
    private Map getDefaultHints ()
    {
        return new RenderingHints ( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    }

    /**
     * Returns text rendering hints offered by this text rasterization option.
     *
     * @return text rendering hints offered by this text rasterization option
     */
    public Map getRenderingHints ()
    {
        return renderingHints;
    }
}