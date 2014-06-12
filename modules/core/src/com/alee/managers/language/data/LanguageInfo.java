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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * @author Mikle Garin
 */

@XStreamAlias ("langInfo")
public class LanguageInfo implements Serializable
{
    @XStreamAsAttribute
    private String lang;

    @XStreamAsAttribute
    private String author;

    @XStreamAsAttribute
    private String info;

    @XStreamAsAttribute
    private String title;

    public LanguageInfo ()
    {
        super ();
    }

    public String getLang ()
    {
        return lang;
    }

    public void setLang ( final String lang )
    {
        this.lang = lang;
    }

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor ( final String author )
    {
        this.author = author;
    }

    public String getInfo ()
    {
        return info;
    }

    public void setInfo ( final String info )
    {
        this.info = info;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle ( final String title )
    {
        this.title = title;
    }
}