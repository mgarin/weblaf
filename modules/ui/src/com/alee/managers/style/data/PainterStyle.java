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

package com.alee.managers.style.data;

import com.alee.api.merge.Overwriting;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Contains single {@link com.alee.painter.Painter} style data.
 * It stores any {@link com.alee.painter.Painter}-related data without instantiating {@link com.alee.painter.Painter} itself.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 */
@XStreamConverter ( PainterStyleConverter.class )
public final class PainterStyle implements Overwriting, Cloneable, Serializable
{
    /**
     * Whether or not this {@link com.alee.painter.Painter} should overwrite another one when merged.
     */
    @XStreamAsAttribute
    private Boolean overwrite;

    /**
     * Painter class canonical name.
     * Used for painter instantiation.
     */
    @XStreamAsAttribute
    private String painterClass;

    /**
     * Painter properties.
     * Contains parsed painter settings.
     */
    private LinkedHashMap<String, Object> properties;

    @Override
    public boolean isOverwrite ()
    {
        return overwrite != null && overwrite;
    }

    /**
     * Sets whether or not this {@link com.alee.painter.Painter} should overwrite another one when merged.
     *
     * @param overwrite whether or not this {@link com.alee.painter.Painter} should overwrite another one when merged
     */
    public void setOverwrite ( final Boolean overwrite )
    {
        this.overwrite = overwrite;
    }

    /**
     * Returns painter class canonical name.
     *
     * @return painter class canonical name
     */
    public String getPainterClass ()
    {
        return painterClass;
    }

    /**
     * Sets painter class canonical name.
     *
     * @param painterClass new painter class canonical name
     */
    public void setPainterClass ( final String painterClass )
    {
        this.painterClass = painterClass;
    }

    /**
     * Returns painter properties.
     *
     * @return painter properties
     */
    public LinkedHashMap<String, Object> getProperties ()
    {
        return properties;
    }

    /**
     * Sets painter properties.
     *
     * @param properties new painter properties
     */
    public void setProperties ( final LinkedHashMap<String, Object> properties )
    {
        this.properties = properties;
    }
}