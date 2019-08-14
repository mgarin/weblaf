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
import com.alee.api.annotations.Nullable;
import com.alee.utils.encryption.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * This class provides a set of utilities to encode and decode data.
 *
 * @author Mikle Garin
 */
public final class EncryptionUtils
{
    /**
     * Default text encoding.
     */
    @NotNull
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Encode and decode key.
     */
    @NotNull
    private static final String key = "aZCVKk3mospdfm12pk4fcFD43d435ccCDgHKPQMQ23x7zkq03";

    /**
     * Private constructor to avoid instantiation.
     */
    private EncryptionUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns text encrypted through xor and encoded using base64.
     *
     * @param text text to encrypt
     * @return encrypted text
     */
    @Nullable
    public static String encrypt ( @Nullable final String text )
    {
        return encrypt ( text, key );
    }

    /**
     * Returns text encrypted through xor and encoded using base64.
     *
     * @param text text to encrypt
     * @param key  xor key
     * @return encrypted text
     */
    @Nullable
    public static String encrypt ( @Nullable final String text, @NotNull final String key )
    {
        return base64encode ( xorText ( text, key ) );
    }

    /**
     * Returns text decoded using base64 and decrypted through xor.
     *
     * @param text text to decrypt
     * @return decrypted text
     */
    @Nullable
    public static String decrypt ( @Nullable final String text )
    {
        return decrypt ( text, key );
    }

    /**
     * Returns text decoded using base64 and decrypted through xor.
     *
     * @param text text to decrypt
     * @param key  xor key
     * @return decrypted text
     */
    public static String decrypt ( @Nullable final String text, @NotNull final String key )
    {
        return xorText ( base64decode ( text ), key );
    }

    /**
     * Returns text encrypted using xor.
     *
     * @param text to encrypt
     * @return encrypted text
     */
    @Nullable
    public static String xorText ( @Nullable final String text )
    {
        return xorText ( text, key );
    }

    /**
     * Returns text encrypted using xor.
     *
     * @param text to encrypt
     * @param key  xor key
     * @return encrypted text
     */
    @Nullable
    public static String xorText ( @Nullable final String text, @NotNull final String key )
    {
        final String result;
        if ( text != null )
        {
            final char[] keys = key.toCharArray ();
            final char[] msg = text.toCharArray ();
            final int ml = msg.length;
            final int kl = keys.length;
            final char[] newMsg = new char[ ml ];
            for ( int i = 0; i < ml; i++ )
            {
                newMsg[ i ] = ( char ) ( msg[ i ] ^ keys[ i % kl ] );
            }
            result = new String ( newMsg );
        }
        else
        {
            result = null;
        }
        return result;
    }

    /**
     * Returns text encoded with base64.
     *
     * @param text text to encode
     * @return text encoded with base64
     */
    @Nullable
    public static String base64encode ( @Nullable final String text )
    {
        try
        {
            return text != null ? Base64.encode ( text.getBytes ( DEFAULT_ENCODING ) ) : null;
        }
        catch ( final UnsupportedEncodingException e )
        {
            throw new RuntimeException ( "Unable to encode text using Base64", e );
        }
    }

    /**
     * Returns text decoded with base64.
     *
     * @param text text to decoded
     * @return text decoded with base64
     */
    @Nullable
    public static String base64decode ( @Nullable final String text )
    {
        try
        {
            final byte[] bytes = Base64.decode ( text );
            return bytes != null ? new String ( bytes, DEFAULT_ENCODING ) : null;
        }
        catch ( final IOException e )
        {
            throw new RuntimeException ( "Unable to decode text using Base64", e );
        }
    }
}