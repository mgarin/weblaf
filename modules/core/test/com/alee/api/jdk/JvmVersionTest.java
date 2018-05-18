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

package com.alee.api.jdk;

import com.alee.utils.SystemUtils;
import com.alee.utils.system.JavaVersion;
import org.junit.Test;

/**
 * Simple JVM version test.
 *
 * @author Mikle Garin
 */
public class JvmVersionTest
{
    /**
     * Testing that JVM we are running on is supported by the library code.
     */
    @Test
    public void supportedVersion ()
    {
        final JavaVersion javaVersion = SystemUtils.getJavaVersion ();
        if ( javaVersion.major () != 1.6 &&
                javaVersion.major () != 1.7 &&
                javaVersion.major () != 1.8 &&
                javaVersion.major () != 9.0 &&
                javaVersion.major () != 10.0 )
        {
            throw new RuntimeException ( "Unsupported JDK version: " + javaVersion );
        }
    }
}