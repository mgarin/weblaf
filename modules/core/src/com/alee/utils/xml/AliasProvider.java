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
 * This interface notifies that implementing class can provide its aliases through static method "provideAliases".
 * Every implementing class should have such static method added.
 * This interface might be used as on of the ways to provide aliases (like XStream annotations).
 *
 * @author Mikle Garin
 */
public interface AliasProvider
{
    /**
     * Method name for external usage.
     */
    public static final String methodName = "provideAliases";

    /**
     * Initializes implementing class aliases
     *
     * @param xStream XStream instance
     */
    // public static void provideAliases ( XStream xStream );
}