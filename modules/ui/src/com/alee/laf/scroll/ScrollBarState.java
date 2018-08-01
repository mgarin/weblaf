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

package com.alee.laf.scroll;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.io.Serializable;

/**
 * {@link JScrollBar} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see ScrollBarSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "ScrollBarState" )
public class ScrollBarState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link JScrollBar} value.
     */
    @XStreamAsAttribute
    protected final Integer value;

    /**
     * Constructs default {@link ScrollBarState}.
     */
    public ScrollBarState ()
    {
        this ( ( Integer ) null );
    }

    /**
     * Constructs new {@link ScrollBarState} with settings from {@link JScrollBar}.
     *
     * @param scrollBar {@link JScrollBar} to retrieve settings from
     */
    public ScrollBarState ( final JScrollBar scrollBar )
    {
        this ( scrollBar.getValue () );
    }

    /**
     * Constructs new {@link ScrollBarState} with specified settings.
     *
     * @param value {@link JScrollBar} value
     */
    public ScrollBarState ( final Integer value )
    {
        this.value = value;
    }

    /**
     * Returns {@link JScrollBar} value.
     *
     * @return {@link JScrollBar} value
     */
    public Integer value ()
    {
        return value;
    }

    /**
     * Applies this {@link ScrollBarState} to the specified {@link JScrollBar}.
     *
     * @param scrollBar {@link JScrollBar} to apply this {@link ScrollBarState} to
     */
    public void apply ( final JScrollBar scrollBar )
    {
        if ( value != null && scrollBar.getMinimum () <= value && value <= scrollBar.getMaximum () )
        {
            scrollBar.setValue ( value );
        }
    }
}