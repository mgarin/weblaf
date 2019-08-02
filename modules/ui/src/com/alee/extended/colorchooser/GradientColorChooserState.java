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

package com.alee.extended.colorchooser;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * {@link WebGradientColorChooser} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see GradientColorChooserSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "GradientColorChooserState" )
public class GradientColorChooserState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link GradientData} of the {@link WebGradientColorChooser}.
     */
    protected final GradientData gradientData;

    /**
     * Constructs default {@link GradientColorChooserState}.
     */
    public GradientColorChooserState ()
    {
        this ( new GradientData () );
    }

    /**
     * Constructs new {@link GradientColorChooserState} with settings from {@link WebGradientColorChooser}.
     *
     * @param gradientColorChooser {@link WebGradientColorChooser} to retrieve settings from
     */
    public GradientColorChooserState ( final WebGradientColorChooser gradientColorChooser )
    {
        this ( gradientColorChooser.getGradientData () );
    }

    /**
     * Constructs new {@link GradientColorChooserState} with specified settings.
     *
     * @param gradientData {@link GradientData} of the {@link WebGradientColorChooser}
     */
    public GradientColorChooserState ( final GradientData gradientData )
    {
        this.gradientData = gradientData;
    }

    /**
     * Returns {@link GradientData} of the {@link WebGradientColorChooser}.
     *
     * @return {@link GradientData} of the {@link WebGradientColorChooser}
     */
    public GradientData gradientData ()
    {
        return gradientData;
    }

    /**
     * Applies this {@link GradientColorChooserState} to the specified {@link WebGradientColorChooser}.
     *
     * @param gradientColorChooser {@link WebGradientColorChooser} to apply this {@link GradientColorChooserState} to
     */
    public void apply ( final WebGradientColorChooser gradientColorChooser )
    {
        gradientColorChooser.setGradientData ( gradientData );
    }
}