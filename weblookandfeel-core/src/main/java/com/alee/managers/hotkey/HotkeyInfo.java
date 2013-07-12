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

package com.alee.managers.hotkey;

import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.TextUtils;

import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * User: mgarin Date: 11.07.11 Time: 12:57
 */

public class HotkeyInfo
{
    public static final String ID_PREFIX = "HI";

    // Hotkey id
    private String id = null;

    // Should hotkey be hidden in tooltip
    private boolean hidden = false;

    // Top component (usually container) under which this hotkey works, if null then "forComponent" ancestor window will be used
    private WeakReference<Component> topComponent = null;

    // Component that listens for hotkey, if this and "topComponent" are null then hotkey counts as global
    private WeakReference<Component> forComponent = null;

    // Actual hotkey data
    private HotkeyData hotkeyData = null;

    // Hotkey tooltip display way, this doesn't affect component's tooltip displayWay - only sole hotkey tip one-time tooltip
    private TooltipWay hotkeyDisplayWay = null;

    // Hotkey action
    private HotkeyRunnable action = null;

    public HotkeyInfo ()
    {
        super ();
    }

    public String getId ()
    {
        if ( id == null )
        {
            setId ();
        }
        return id;
    }

    public void setId ( String id )
    {
        this.id = id;
    }

    public void setId ()
    {
        id = TextUtils.generateId ( ID_PREFIX );
    }

    public boolean isHidden ()
    {
        return hidden;
    }

    public void setHidden ( boolean hidden )
    {
        this.hidden = hidden;
    }

    public Component getTopComponent ()
    {
        return topComponent != null ? topComponent.get () : null;
    }

    public void setTopComponent ( Component topComponent )
    {
        this.topComponent = new WeakReference<Component> ( topComponent );
    }

    public Component getForComponent ()
    {
        return forComponent != null ? forComponent.get () : null;
    }

    public void setForComponent ( Component forComponent )
    {
        this.forComponent = new WeakReference<Component> ( forComponent );
    }

    public HotkeyData getHotkeyData ()
    {
        return hotkeyData;
    }

    public void setHotkeyData ( HotkeyData hotkeyData )
    {
        this.hotkeyData = hotkeyData;
    }

    public TooltipWay getHotkeyDisplayWay ()
    {
        return hotkeyDisplayWay;
    }

    public void setHotkeyDisplayWay ( TooltipWay hotkeyDisplayWay )
    {
        this.hotkeyDisplayWay = hotkeyDisplayWay;
    }

    public HotkeyRunnable getAction ()
    {
        return action;
    }

    public void setAction ( HotkeyRunnable action )
    {
        this.action = action;
    }
}
