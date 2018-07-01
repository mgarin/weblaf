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

package com.alee.extended.date;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;
import java.util.Date;

/**
 * {@link WebDateField} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see DateFieldSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "DateFieldState" )
public class DateFieldState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link WebDateField} selected time.
     */
    @XStreamAsAttribute
    protected final Long time;

    /**
     * Constructs default {@link DateFieldState}.
     */
    public DateFieldState ()
    {
        this ( ( Date ) null );
    }

    /**
     * Constructs new {@link DateFieldState} with settings from {@link WebDateField}.
     *
     * @param dateField {@link WebDateField} to retrieve settings from
     */
    public DateFieldState ( final WebDateField dateField )
    {
        this ( dateField.getDate () );
    }

    /**
     * Constructs new {@link DateFieldState} with specified settings.
     *
     * @param date {@link WebDateField} selected date
     */
    public DateFieldState ( final Date date )
    {
        this.time = date != null ? date.getTime () : null;
    }

    /**
     * Returns {@link WebDateField} selected time
     *
     * @return {@link WebDateField} selected time
     */
    public Long time ()
    {
        return time;
    }

    /**
     * Applies this {@link DateFieldState} to the specified {@link WebDateField}.
     *
     * @param dateField {@link WebDateField} to apply this {@link DateFieldState} to
     */
    public void apply ( final WebDateField dateField )
    {
        dateField.setDate ( time != null ? new Date ( time ) : null );
    }
}