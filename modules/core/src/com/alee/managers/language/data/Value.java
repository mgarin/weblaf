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

import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.MergeUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
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

    public Value ( final String lang, final String text )
    {
        super ();
        this.lang = lang;
        this.texts = CollectionUtils.asList ( new Text ( text ) );
    }

    public Value ( final String lang, final Character mnemonic, final String text )
    {
        super ();
        this.lang = lang;
        this.mnemonic = mnemonic;
        this.texts = CollectionUtils.asList ( new Text ( text ) );
    }

    public Value ( final String lang, final String hotkey, final String text )
    {
        super ();
        this.lang = lang;
        this.hotkey = hotkey;
        this.texts = CollectionUtils.asList ( new Text ( text ) );
    }

    public Value ( final String lang, final Character mnemonic, final String hotkey, final String text )
    {
        super ();
        this.lang = lang;
        this.mnemonic = mnemonic;
        this.hotkey = hotkey;
        this.texts = CollectionUtils.asList ( new Text ( text ) );
    }

    public String getLang ()
    {
        return lang;
    }

    public void setLang ( final String lang )
    {
        this.lang = lang;
    }

    public Character getMnemonic ()
    {
        return mnemonic;
    }

    public void setMnemonic ( final Character mnemonic )
    {
        this.mnemonic = mnemonic;
    }

    public String getHotkey ()
    {
        return hotkey;
    }

    public void setHotkey ( final String hotkey )
    {
        this.hotkey = hotkey;
    }

    public List<Text> getTexts ()
    {
        return texts;
    }

    public void setTexts ( final List<Text> texts )
    {
        this.texts = texts;
    }

    public String getText ()
    {
        return getText ( null );
    }

    public String getText ( final String state )
    {
        return getText ( state, false );
    }

    public String getText ( final String state, final boolean defaultState )
    {
        final Text text = getTextObject ( state, defaultState );
        return text != null ? text.getText () : null;
    }

    public Text getTextObject ()
    {
        return getTextObject ( null );
    }

    public Text getTextObject ( final String state )
    {
        return getTextObject ( state, false );
    }

    public Text getTextObject ( final String state, final boolean defaultState )
    {
        if ( texts != null )
        {
            for ( final Text text : texts )
            {
                if ( CompareUtils.equals ( text.getState (), state ) || defaultState && text.getState () == null )
                {
                    return text;
                }
            }
        }
        return null;
    }

    public Text getTextObject ( final int index )
    {
        return texts.get ( index );
    }

    public Text addText ( final String text )
    {
        return addText ( new Text ( text ) );
    }

    public Text addText ( final String text, final String state )
    {
        return addText ( new Text ( text, state ) );
    }

    public Text addText ( final Text text )
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

    public void removeText ( final Text text )
    {
        this.texts.remove ( text );
    }

    public List<Tooltip> getTooltips ()
    {
        return tooltips;
    }

    public void setTooltips ( final List<Tooltip> tooltips )
    {
        this.tooltips = tooltips;
    }

    public Tooltip addTooltip ( final String text )
    {
        return addTooltip ( new Tooltip ( text ) );
    }

    public Tooltip addTooltip ( final Integer delay, final String text )
    {
        return addTooltip ( new Tooltip ( delay, text ) );
    }

    public Tooltip addTooltip ( final TooltipWay way, final String text )
    {
        return addTooltip ( new Tooltip ( way, text ) );
    }

    public Tooltip addTooltip ( final TooltipWay way, final Integer delay, final String text )
    {
        return addTooltip ( new Tooltip ( way, delay, text ) );
    }

    public Tooltip addTooltip ( final TooltipType type, final String text )
    {
        return addTooltip ( new Tooltip ( type, text ) );
    }

    public Tooltip addTooltip ( final Tooltip tooltip )
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

    public void removeTooltip ( final Tooltip tooltip )
    {
        this.tooltips.remove ( tooltip );
    }

    public Tooltip getTooltipObject ( final int index )
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
        return MergeUtils.cloneByFieldsSafely ( this );
    }

    @Override
    public String toString ()
    {
        return langText () + " -> " + listTexts () + ( mnemonic != null ? " (" + mnemonic + ")" : "" ) +
                ( hotkey != null ? " (" + hotkey + ")" : "" ) +
                ( tooltips != null && tooltips.size () > 0 ? " (tooltips: " + tooltips.size () + ")" : "" );
    }
}