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

package com.alee.laf.slider;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.io.Serializable;

/**
 * {@link JSlider} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see SliderSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "SliderState" )
public class SliderState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link JSlider} value.
     */
    @XStreamAsAttribute
    protected final Integer value;

    /**
     * Constructs default {@link SliderState}.
     */
    public SliderState ()
    {
        this ( ( Integer ) null );
    }

    /**
     * Constructs new {@link SliderState} with settings from {@link JSlider}.
     *
     * @param slider {@link JSlider} to retrieve settings from
     */
    public SliderState ( final JSlider slider )
    {
        this ( slider.getValue () );
    }

    /**
     * Constructs new {@link SliderState} with specified settings.
     *
     * @param value {@link JSlider} value
     */
    public SliderState ( final Integer value )
    {
        this.value = value;
    }

    /**
     * Returns {@link JSlider} value.
     *
     * @return {@link JSlider} value
     */
    public Integer value ()
    {
        return value;
    }

    /**
     * Applies this {@link SliderState} to the specified {@link JSlider}.
     *
     * @param slider {@link JSlider} to apply this {@link SliderState} to
     */
    public void apply ( final JSlider slider )
    {
        if ( value != null && slider.getMinimum () <= value && value <= slider.getMaximum () )
        {
            slider.setValue ( value );
        }
    }
}