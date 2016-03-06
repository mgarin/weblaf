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
 * This class contains information about single SettingsGroup read state.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsGroup
 */

@XStreamAlias ("SettingsGroupState")
public class SettingsGroupState implements Serializable
{
    /**
     * SettingsGroup read state.
     */
    @XStreamAsAttribute
    private ReadState readState;

    /**
     * Occurred error.
     */
    private Throwable error;

    /**
     * Constructs none SettingsGroupState.
     */
    public SettingsGroupState ()
    {
        this ( ReadState.none, null );
    }

    /**
     * Constructs SettingsGroupState with the specified read state.
     *
     * @param readState read state
     */
    public SettingsGroupState ( final ReadState readState )
    {
        this ( readState, null );
    }

    /**
     * Constructs SettingsGroupState with the specified read state and occurred error.
     *
     * @param readState read state
     * @param error     occurred error
     */
    public SettingsGroupState ( final ReadState readState, final Throwable error )
    {
        super ();
        setReadState ( readState );
        setError ( error );
    }

    /**
     * Returns SettingsGroup read state.
     *
     * @return SettingsGroup read state
     */
    public ReadState getReadState ()
    {
        return readState;
    }

    /**
     * Sets SettingsGroup read state.
     *
     * @param readState new SettingsGroup read state
     */
    public void setReadState ( final ReadState readState )
    {
        this.readState = readState;
    }

    /**
     * Returns occurred error.
     *
     * @return occurred error
     */
    public Throwable getError ()
    {
        return error;
    }

    /**
     * Sets occurred error.
     *
     * @param error occurred error
     */
    public void setError ( final Throwable error )
    {
        this.error = error;
    }
}