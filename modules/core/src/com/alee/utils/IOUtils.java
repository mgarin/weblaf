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

import com.alee.api.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO utilities.
 * Some methods are taken from Apache Commons IO, particularly from IOUtils.
 *
 * @author Mikle Garin
 */
public final class IOUtils
{
    /**
     * Default buffer size.
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 10;

    /**
     * Represents the end-of-file or stream.
     */
    public static final int EOF = -1;

    /**
     * Returns contents of the specified {@link InputStream} as a byte array.
     *
     * @param inputStream {@link InputStream}
     * @return contents of the specified {@link InputStream} as a byte array
     */
    public static byte[] toByteArray ( @NotNull final InputStream inputStream )
    {
        final ByteArrayOutputStream output = new ByteArrayOutputStream ();
        copy ( inputStream, output );
        return output.toByteArray ();
    }

    /**
     * Copies bytes from the {@link InputStream} to an {@link OutputStream}.
     *
     * @param input  {@link InputStream}
     * @param output {@link OutputStream}
     * @return number of bytes copied
     */
    public static long copy ( @NotNull final InputStream input, @NotNull final OutputStream output )
    {
        return copy ( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Copies bytes from the {@link InputStream} to an {@link OutputStream}.
     *
     * @param input      {@link InputStream}
     * @param output     {@link OutputStream}
     * @param bufferSize size of buffer used to copy bytes from the {@link InputStream} to the {@link OutputStream}
     * @return number of bytes copied
     */
    public static long copy ( @NotNull final InputStream input, @NotNull final OutputStream output, final int bufferSize )
    {
        return copy ( input, output, new byte[ bufferSize ] );
    }

    /**
     * Copies bytes from the {@link InputStream} to an {@link OutputStream}.
     *
     * @param input  {@link InputStream}
     * @param output {@link OutputStream}
     * @param buffer buffer used to copy bytes from the {@link InputStream} to the {@link OutputStream}
     * @return number of bytes copied
     */
    public static long copy ( @NotNull final InputStream input, @NotNull final OutputStream output, @NotNull final byte[] buffer )
    {
        try
        {
            long count = 0;
            int n;
            while ( EOF != ( n = input.read ( buffer ) ) )
            {
                output.write ( buffer, 0, n );
                count += n;
            }
            return count;
        }
        catch ( final Exception e )
        {
            throw new UtilityException ( "Unable to copy InputStream to OutputStream: " + input, e );
        }
    }
}