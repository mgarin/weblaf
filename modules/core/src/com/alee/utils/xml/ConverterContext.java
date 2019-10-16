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

package com.alee.utils.xml;

/**
 * Custom context for passing {@link ThreadLocal} settings for XStream converters.
 * It is necessary because XStream doesn't pass it's own context into some converters.
 *
 * @author Mikle Garin
 */
public final class ConverterContext
{
    /**
     * {@link ConverterContext} instance.
     */
    private static ConverterContext instance;

    /**
     * Context variable containing a {@link Boolean} marker that can be either {@code true}, {@code false} or {@code null}.
     * It enables conversion of read objects into {@link javax.swing.plaf.UIResource}s when possible (supported by converter).
     */
    private final ThreadLocal<Boolean> uiResource;

    /**
     * Returns {@link ConverterContext} instance.
     *
     * @return {@link ConverterContext} instance
     */
    public static ConverterContext get ()
    {
        if ( instance == null )
        {
            instance = new ConverterContext ();
        }
        return instance;
    }

    /**
     * Constructs new {@link ConverterContext} instance.
     */
    private ConverterContext ()
    {
        uiResource = new ThreadLocal<Boolean> ();
    }

    /**
     * Returns whether or not converter should read it's value as {@link javax.swing.plaf.UIResource}.
     *
     * @return {@code true} if converter should read it's value as {@link javax.swing.plaf.UIResource}, {@code false} otherwise
     */
    public boolean isUIResource ()
    {
        final Boolean uiResource = this.uiResource.get ();
        return uiResource != null && uiResource;
    }

    /**
     * Sets whether or not converter should read it's value as {@link javax.swing.plaf.UIResource}.
     *
     * @param uiResource whether or not converter should read it's value as {@link javax.swing.plaf.UIResource}
     */
    public void setUIResource ( final boolean uiResource )
    {
        this.uiResource.set ( uiResource );
    }
}