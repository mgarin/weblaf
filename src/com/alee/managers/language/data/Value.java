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

import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 20.04.12 Time: 14:03
 */

@XStreamAlias ("value")
@XStreamConverter (ValueConverter.class)
public final class Value implements Serializable, Cloneable
{
    private String lang;
    private Character mnemonic;
    private String hotkey;
    private List<Text> texts;
    private List<Tooltip> tooltips;

    public Value ()
    {
        super ();
    }

    public Value ( String lang, String text )
    {
        super ();
        this.lang = lang;
        this.texts = CollectionUtils.copy ( new Text ( text ) );
    }

    public Value ( String lang, Character mnemonic, String text )
    {
        super ();
        this.lang = lang;
        this.mnemonic = mnemonic;
        this.texts = CollectionUtils.copy ( new Text ( text ) );
    }

    public Value ( String lang, String hotkey, String text )
    {
        super ();
        this.lang = lang;
        this.hotkey = hotkey;
        this.texts = CollectionUtils.copy ( new Text ( text ) );
    }

    public Value ( String lang, Character mnemonic, String hotkey, String text )
    {
        super ();
        this.lang = lang;
        this.mnemonic = mnemonic;
        this.hotkey = hotkey;
        this.texts = CollectionUtils.copy ( new Text ( text ) );
    }

    public String getLang ()
    {
        return lang;
    }

    public void setLang ( String lang )
    {
        this.lang = lang;
    }

    public Character getMnemonic ()
    {
        return mnemonic;
    }

    public void setMnemonic ( Character mnemonic )
    {
        this.mnemonic = mnemonic;
    }

    public String getHotkey ()
    {
        return hotkey;
    }

    public void setHotkey ( String hotkey )
    {
        this.hotkey = hotkey;
    }

    public List<Text> getTexts ()
    {
        return texts;
    }

    public void setTexts ( List<Text> texts )
    {
        this.texts = texts;
    }

    public String getText ()
    {
        return getText ( null );
    }

    public String getText ( String state )
    {
        return getText ( state, false );
    }

    public String getText ( String state, boolean defaultState )
    {
        Text text = getTextObject ( state, defaultState );
        return text != null ? text.getText () : null;
    }

    public Text getTextObject ()
    {
        return getTextObject ( null );
    }

    public Text getTextObject ( String state )
    {
        return getTextObject ( state, false );
    }

    public Text getTextObject ( String state, boolean defaultState )
    {
        if ( texts != null )
        {
            for ( Text text : texts )
            {
                if ( CompareUtils.equals ( text.getState (), state ) || defaultState && text.getState () == null )
                {
                    return text;
                }
            }
        }
        return null;
    }

    public Text getTextObject ( int index )
    {
        return texts.get ( index );
    }

    public Text addText ( String text )
    {
        return addText ( new Text ( text ) );
    }

    public Text addText ( String text, String state )
    {
        return addText ( new Text ( text, state ) );
    }

    public Text addText ( Text text )
    {
        //        for ( Text existing : texts )
        //        {
        //            if ( SwingUtils.equals ( existing.getState (), text.getState () ) )
        //            {
        //                existing.setText ( text.getText () );
        //                return existing;
        //            }
        //        }

        if ( texts == null )
        {
            texts = new ArrayList<Text> ( 1 );
        }
        this.texts.add ( text );

        return text;
    }

    public void removeText ( Text text )
    {
        this.texts.remove ( text );
    }

    public List<Tooltip> getTooltips ()
    {
        return tooltips;
    }

    public void setTooltips ( List<Tooltip> tooltips )
    {
        this.tooltips = tooltips;
    }

    public Tooltip addTooltip ( String text )
    {
        return addTooltip ( new Tooltip ( text ) );
    }

    public Tooltip addTooltip ( Integer delay, String text )
    {
        return addTooltip ( new Tooltip ( delay, text ) );
    }

    public Tooltip addTooltip ( TooltipWay way, String text )
    {
        return addTooltip ( new Tooltip ( way, text ) );
    }

    public Tooltip addTooltip ( TooltipWay way, Integer delay, String text )
    {
        return addTooltip ( new Tooltip ( way, delay, text ) );
    }

    public Tooltip addTooltip ( TooltipType type, String text )
    {
        return addTooltip ( new Tooltip ( type, text ) );
    }

    public Tooltip addTooltip ( Tooltip tooltip )
    {
        //        for ( Tooltip existing : tooltips )
        //        {
        //            if ( existing.getType () == tooltip.getType () &&
        //                    existing.getWay () == tooltip.getWay () )
        //            {
        //                existing.setDelay ( tooltip.getDelay () );
        //                existing.setText ( tooltip.getText () );
        //                return existing;
        //            }
        //        }

        if ( tooltips == null )
        {
            tooltips = new ArrayList<Tooltip> ( 1 );
        }
        tooltips.add ( tooltip );

        return tooltip;
    }

    public void removeTooltip ( Tooltip tooltip )
    {
        this.tooltips.remove ( tooltip );
    }

    public Tooltip getTooltipObject ( int index )
    {
        return tooltips.get ( index );
    }

    private String langText ()
    {
        return lang != null ? lang : "all";
    }

    private String listTexts ()
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

    @Override
    public Value clone ()
    {
        final Value value = new Value ();
        value.setLang ( lang );
        value.setMnemonic ( mnemonic );
        value.setHotkey ( hotkey );
        value.setTexts ( texts != null ? CollectionUtils.clone ( texts ) : null );
        value.setTooltips ( tooltips != null ? CollectionUtils.clone ( tooltips ) : null );
        return value;
    }

    public String toString ()
    {
        return langText () + " -> " + listTexts () + ( mnemonic != null ? " (" + mnemonic + ")" : "" ) +
                ( hotkey != null ? " (" + hotkey + ")" : "" ) +
                ( tooltips != null && tooltips.size () > 0 ? " (tooltips: " + tooltips.size () + ")" : "" );
    }
}