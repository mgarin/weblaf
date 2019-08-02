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

package com.alee.managers.settings;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * This class contains information about single {@link SettingsGroup} read state.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see SettingsManager
 * @see SettingsGroup
 */
@XStreamAlias ( "SettingsGroupState" )
public class SettingsGroupState implements Serializable
{
    /**
     * {@link ReadState} for {@link SettingsGroup}.
     */
    @XStreamAsAttribute
    private ReadState readState;

    /**
     * Occurred exception.
     */
    private Throwable error;

    /**
     * Constructs new {@link SettingsGroupState}.
     */
    public SettingsGroupState ()
    {
        this ( ReadState.none, null );
    }

    /**
     * Constructs new {@link SettingsGroupState}.
     *
     * @param readState {@link ReadState}
     */
    public SettingsGroupState ( final ReadState readState )
    {
        this ( readState, null );
    }

    /**
     * Constructs new {@link SettingsGroupState}.
     *
     * @param readState {@link ReadState}
     * @param error     occured exception
     */
    public SettingsGroupState ( final ReadState readState, final Throwable error )
    {
        super ();
        setReadState ( readState );
        setError ( error );
    }

    /**
     * Returns {@link ReadState} for {@link SettingsGroup}.
     *
     * @return {@link ReadState} for {@link SettingsGroup}
     */
    public ReadState getReadState ()
    {
        return readState;
    }

    /**
     * Sets {@link ReadState} for {@link SettingsGroup}.
     *
     * @param readState new {@link ReadState} for {@link SettingsGroup}
     */
    public void setReadState ( final ReadState readState )
    {
        this.readState = readState;
    }

    /**
     * Returns occurred exception.
     *
     * @return occurred exception
     */
    public Throwable getError ()
    {
        return error;
    }

    /**
     * Sets occurred exception.
     *
     * @param error occurred exception
     */
    public void setError ( final Throwable error )
    {
        this.error = error;
    }
}