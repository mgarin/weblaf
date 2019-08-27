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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
    @NotNull
    @XStreamImplicit
    private List<Text> texts;

    /**
     * Contructs new {@link Value}.
     * {@link Locale#getDefault()} is used instead of {@link LanguageManager#getLocale()}
     */
    public Value ()
    {
        this ( Locale.getDefault (), new ArrayList<Text> ( 0 ) );
    }

    /**
     * Contructs new {@link Value}.
     *
     * @param locale {@link Locale} for this {@link Value}
     */
    public Value ( @NotNull final Locale locale )
    {
        this ( locale, new ArrayList<Text> ( 0 ) );
    }

    /**
     * Contructs new {@link Value}.
     *
     * @param locale {@link Locale} for this {@link Value}
     * @param texts  {@link Text}s for this {@link Value}
     */
    public Value ( @NotNull final Locale locale, @NotNull final Text... texts )
    {
        this ( locale, CollectionUtils.asList ( texts ) );
    }

    /**
     * Contructs new {@link Value}.
     *
     * @param locale {@link Locale} for this {@link Value}
     * @param texts  {@link Text}s for this {@link Value}
     */
    public Value ( @NotNull final Locale locale, @NotNull final List<Text> texts )
    {
        this.locale = locale;
        this.texts = texts;
    }

    /**
     * Returns {@link Locale} of this {@link Value}.
     *
     * @return {@link Locale} of this {@link Value}
     */
    @NotNull
    public Locale getLocale ()
    {
        return locale;
    }

    /**
     * Sets {@link Locale} for this {@link Value}.
     *
     * @param locale new {@link Locale} for this {@link Value}
     */
    public void setLocale ( @NotNull final Locale locale )
    {
        this.locale = locale;
    }

    /**
     * Returns {@link Text}s of this {@link Value}.
     *
     * @return {@link Text}s of this {@link Value}
     */
    @NotNull
    public List<Text> getTexts ()
    {
        return texts;
    }

    /**
     * Sets {@link Text}s for this {@link Value}.
     *
     * @param texts new {@link Text}s for this {@link Value}
     */
    public void setTexts ( @NotNull final List<Text> texts )
    {
        this.texts = texts;
    }

    /**
     * Adds {@link Text} for this {@link Value}.
     *
     * @param text new {@link Text} for this {@link Value}
     */
    public void addText ( @NotNull final Text text )
    {
        texts.add ( text );
    }

    /**
     * Removes {@link Text} from this {@link Value}
     *
     * @param text {@link Text} to remove
     */
    public void removeText ( @NotNull final Text text )
    {
        texts.remove ( text );
    }

    /**
     * Removes all {@link Text}s from this {@link Value}.
     */
    public void clearTexts ()
    {
        texts.clear ();
    }

    /**
     * Returns amount of {@link Text}s within this {@link Value}.
     *
     * @return amount of {@link Text}s within this {@link Value}
     */
    public int textsCount ()
    {
        return texts.size ();
    }

    /**
     * Returns {@link Text} for default state.
     *
     * @return {@link Text} for default state
     */
    @Nullable
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
    @Nullable
    public Text getText ( @Nullable final String state )
    {
        Text result = null;
        for ( final Text text : texts )
        {
            if ( Objects.equals ( text.getState (), state ) )
            {
                result = text;
                break;
            }
        }
        return result;
    }

    @NotNull
    @Override
    public String toString ()
    {
        return LanguageUtils.toString ( getLocale () ) + " -> {" + TextUtils.listToString ( texts, ";" ) + "}";
    }
}