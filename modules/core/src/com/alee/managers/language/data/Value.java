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

package com.alee.managers.language.data;

import com.alee.api.jdk.Objects;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageUtils;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * {@link Value} can store multiple {@link Text}s for different states.
 * It can also provide single {@link Text} for any specific state if it exists.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 * @see com.alee.managers.language.Language
 * @see Record
 * @see Text
 */
@XStreamAlias ( "value" )
@XStreamConverter ( ValueConverter.class )
public final class Value implements Cloneable, Serializable
{
    /**
     * {@link Locale} of this {@link Value}.
     */
    @XStreamAsAttribute
    @XStreamAlias ( "lang" )
    private Locale locale;

    /**
     * {@link Text}s of this {@link Value}.
     */
    @XStreamImplicit
    private List<Text> texts;

    /**
     * Contructs new {@link Value}.
     * {@link Locale#getDefault()} is used instead of {@link LanguageManager#getLocale()}
     */
    public Value ()
    {
        this ( Locale.getDefault () );
    }

    /**
     * Contructs new {@link Value}.
     *
     * @param locale {@link Locale} for this {@link Value}
     */
    public Value ( final Locale locale )
    {
        this ( locale, new ArrayList<Text> ( 0 ) );
    }

    /**
     * Contructs new {@link Value}.
     *
     * @param locale {@link Locale} for this {@link Value}
     * @param texts  {@link Text}s for this {@link Value}
     */
    public Value ( final Locale locale, final Text... texts )
    {
        this ( locale, CollectionUtils.asList ( texts ) );
    }

    /**
     * Contructs new {@link Value}.
     *
     * @param locale {@link Locale} for this {@link Value}
     * @param texts  {@link Text}s for this {@link Value}
     */
    public Value ( final Locale locale, final List<Text> texts )
    {
        super ();
        this.locale = locale;
        this.texts = texts;
    }

    /**
     * Returns {@link Locale} of this {@link Value}.
     *
     * @return {@link Locale} of this {@link Value}
     */
    public Locale getLocale ()
    {
        return locale;
    }

    /**
     * Sets {@link Locale} for this {@link Value}.
     *
     * @param locale new {@link Locale} for this {@link Value}
     */
    public void setLocale ( final Locale locale )
    {
        this.locale = locale;
    }

    /**
     * Returns {@link Text}s of this {@link Value}.
     *
     * @return {@link Text}s of this {@link Value}
     */
    public List<Text> getTexts ()
    {
        return texts;
    }

    /**
     * Sets {@link Text}s for this {@link Value}.
     *
     * @param texts new {@link Text}s for this {@link Value}
     */
    public void setTexts ( final List<Text> texts )
    {
        this.texts = texts;
    }

    /**
     * Adds {@link Text} for this {@link Value}.
     *
     * @param text new {@link Text} for this {@link Value}
     * @return added {@link Text}
     */
    public Text addText ( final Text text )
    {
        if ( texts == null )
        {
            texts = new ArrayList<Text> ( 1 );
        }
        texts.add ( text );
        return text;
    }

    /**
     * Removes {@link Text} from this {@link Value}
     *
     * @param text {@link Text} to remove
     */
    public void removeText ( final Text text )
    {
        if ( texts != null )
        {
            texts.remove ( text );
        }
    }

    /**
     * Removes all {@link Text}s from this {@link Value}.
     */
    public void clearTexts ()
    {
        if ( CollectionUtils.notEmpty ( texts ) )
        {
            texts.clear ();
        }
    }

    /**
     * Returns amount of {@link Text}s within this {@link Value}.
     *
     * @return amount of {@link Text}s within this {@link Value}
     */
    public int textsCount ()
    {
        return texts != null ? texts.size () : 0;
    }

    /**
     * Returns {@link Text} for default state.
     *
     * @return {@link Text} for default state
     */
    public Text getText ()
    {
        return getText ( null );
    }

    /**
     * Returns {@link Text} for the specified state.
     *
     * @param state {@link Text} state
     * @return {@link Text} for the specified state
     */
    public Text getText ( final String state )
    {
        Text result = null;
        if ( texts != null )
        {
            for ( final Text text : texts )
            {
                if ( Objects.equals ( text.getState (), state ) )
                {
                    result = text;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public String toString ()
    {
        return LanguageUtils.toString ( getLocale () ) + " -> " + toString ( texts );
    }

    /**
     * Returns text representation of {@link List} of {@link Text}s.
     *
     * @param texts {@link List} of {@link Text}
     * @return text representation of {@link List} of {@link Text}s
     */
    private String toString ( final List<Text> texts )
    {
        if ( texts == null || texts.size () == 0 )
        {
            return "";
        }
        else if ( texts.size () == 1 )
        {
            return texts.get ( 0 ).toString ();
        }
        else
        {
            return "{" + TextUtils.listToString ( texts, ";" ) + "}";
        }
    }
}