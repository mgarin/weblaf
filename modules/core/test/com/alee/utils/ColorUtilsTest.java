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

package com.alee.utils;

import com.alee.api.jdk.Objects;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.awt.*;

/**
 * Set of JUnit tests for {@link ColorUtils}.
 *
 * @author Mikle Garin
 */
@FixMethodOrder ( MethodSorters.JVM )
public class ColorUtilsTest
{
    /**
     * Hex color conversion test.
     */
    @Test
    public void hexColorConversion ()
    {
        // Conversion from color to hex
        assertValuesEquality (
                ColorUtils.toHex ( new Color ( 255, 0, 0 ) ),
                "#FF0000"
        );
        assertValuesEquality (
                ColorUtils.toHex ( new Color ( 255, 0, 0, 255 ) ),
                "#FF0000"
        );
        assertValuesEquality (
                ColorUtils.toHex ( new Color ( 255, 0, 0, 128 ) ),
                "#FF000080"
        );
        assertValuesEquality (
                ColorUtils.toHex ( new Color ( 255, 0, 0, 1 ) ),
                "#FF000001"
        );

        // Conversion from hex to color
        assertValuesEquality (
                ColorUtils.fromHex ( "FF0000" ),
                new Color ( 255, 0, 0 )
        );
        assertValuesEquality (
                ColorUtils.fromHex ( "#FF0000" ),
                new Color ( 255, 0, 0 )
        );
        assertValuesEquality (
                ColorUtils.fromHex ( "FF000080" ),
                new Color ( 255, 0, 0, 128 )
        );
        assertValuesEquality (
                ColorUtils.fromHex ( "#FF000080" ),
                new Color ( 255, 0, 0, 128 )
        );
    }

    /**
     * Asserts values equality.
     *
     * @param value1 first value to compare
     * @param value2 second value to compare
     */
    private void assertValuesEquality ( final Object value1, final Object value2 )
    {
        assert Objects.equals ( value1, value2 ) : valuesAreNotEqual ();
    }

    /**
     * Returns values inequality error text.
     *
     * @return values inequality error text
     */
    private static String valuesAreNotEqual ()
    {
        return "Values aren't equal";
    }
}