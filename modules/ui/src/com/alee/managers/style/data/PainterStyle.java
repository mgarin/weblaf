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

import com.alee.api.clone.Clone;
import com.alee.api.merge.Mergeable;
import com.alee.api.merge.Overwriting;
import com.alee.painter.Painter;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;
import java.util.Map;

/**
 * Contains single {@link Painter} style data.
 * It stores any {@link Painter}-related data without instantiating {@link Painter} itself.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 */

@XStreamConverter ( PainterStyleConverter.class )
public final class PainterStyle implements Mergeable, Overwriting, Serializable, Cloneable
{
    /**
     * Whether or not this {@link Painter} should overwrite another one when merged.
     */
    @XStreamAsAttribute
    protected Boolean overwrite;

    /**
     * Painter ID.
     * Refers to painter type.
     * It is required since a lot of components has more than just one painter.
     */
    @Deprecated
    private String id;

    /**
     * Whether this is base component painter or not.
     */
    @Deprecated
    private boolean base;

    /**
     * Painter class canonical name.
     * Used for painter instantiation.
     */
    private String painterClass;

    /**
     * Painter properties.
     * Contains parsed painter settings.
     */
    private Map<String, Object> properties;

    /**
     * Constructs new painter style information.
     */
    public PainterStyle ()
    {
        super ();
    }

    @Override
    public boolean isOverwrite ()
    {
        return overwrite != null && overwrite;
    }

    /**
     * Sets whether or not this {@link Painter} should overwrite another one when merged.
     *
     * @param overwrite whether or not this {@link Painter} should overwrite another one when merged
     */
    public void setOverwrite ( final Boolean overwrite )
    {
        this.overwrite = overwrite;
    }

    /**
     * Returns painter ID.
     *
     * @return painter ID
     */
    @Deprecated
    public String getId ()
    {
        return id;
    }

    /**
     * Sets painter ID.
     *
     * @param id new painter ID
     */
    @Deprecated
    public void setId ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns whether this is base component painter or not.
     *
     * @return true if this is base component painter, false otherwise
     */
    @Deprecated
    public boolean isBase ()
    {
        return base;
    }

    /**
     * Sets whether this is base component painter or not.
     *
     * @param base whether this is base component painter or not
     */
    @Deprecated
    public void setBase ( final boolean base )
    {
        this.base = base;
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
    public Map<String, Object> getProperties ()
    {
        return properties;
    }

    /**
     * Sets painter properties.
     *
     * @param properties new painter properties
     */
    public void setProperties ( final Map<String, Object> properties )
    {
        this.properties = properties;
    }

    @Override
    public PainterStyle clone ()
    {
        return Clone.cloneByFieldsSafely ( this );
    }
}