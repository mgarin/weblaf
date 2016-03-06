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
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Encode and decode key.
     */
    @SuppressWarnings ( "SpellCheckingInspection" )
    private static final String key = "aZCVKk3mospdfm12pk4fcFD43d435ccCDgHKPQMQ23x7zkq03";

    /**
     * Returns text encrypted through xor and encoded using base64.
     *
     * @param text text to encrypt
     * @return encrypted text
     */
    public static String encrypt ( final String text )
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
    public static String encrypt ( final String text, final String key )
    {
        return base64encode ( xorText ( text, key ) );
    }

    /**
     * Returns text decoded using base64 and decrypted through xor.
     *
     * @param text text to decrypt
     * @return decrypted text
     */
    public static String decrypt ( final String text )
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
    public static String decrypt ( final String text, final String key )
    {
        return xorText ( base64decode ( text ), key );
    }

    /**
     * Returns text encrypted using xor.
     *
     * @param text to encrypt
     * @return encrypted text
     */
    public static String xorText ( final String text )
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
    public static String xorText ( final String text, final String key )
    {
        if ( text == null )
        {
            return null;
        }

        final char[] keys = key.toCharArray ();
        final char[] msg = text.toCharArray ();
        final int ml = msg.length;
        final int kl = keys.length;
        final char[] newMsg = new char[ ml ];
        for ( int i = 0; i < ml; i++ )
        {
            newMsg[ i ] = ( char ) ( msg[ i ] ^ keys[ i % kl ] );
        }
        return new String ( newMsg );
    }

    /**
     * Returns text encoded with base64.
     *
     * @param text text to encode
     * @return text encoded with base64
     */
    public static String base64encode ( final String text )
    {
        try
        {
            return Base64.encode ( text.getBytes ( DEFAULT_ENCODING ) );
        }
        catch ( final UnsupportedEncodingException e )
        {
            return null;
        }
    }

    /**
     * Returns text decoded with base64.
     *
     * @param text text to decoded
     * @return text decoded with base64
     */
    public static String base64decode ( final String text )
    {
        try
        {
            return new String ( Base64.decode ( text ), DEFAULT_ENCODING );
        }
        catch ( final IOException e )
        {
            return null;
        }
    }
}