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

import com.alee.utils.HtmlUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * User: mgarin Date: 16.05.12 Time: 17:17
 */

@XStreamAlias ("text")
@XStreamConverter (TextConverter.class)
public final class Text implements Serializable, Cloneable
{
    private String text;
    private String state;

    public Text ()
    {
        this ( "" );
    }

    public Text ( String text )
    {
        this ( text, null );
    }

    public Text ( String text, String state )
    {
        super ();
        this.text = text;
        this.state = state;
    }

    public String getText ()
    {
        return text;
    }

    public void setText ( String text )
    {
        this.text = text;
    }

    public String getState ()
    {
        return state;
    }

    public void setState ( String state )
    {
        this.state = state;
    }

    @Override
    public Text clone ()
    {
        return new Text ( text, state );
    }

    public String toString ()
    {
        return TextUtils.shortenText ( HtmlUtils.getPlainText ( text ), 50, true );
    }
}