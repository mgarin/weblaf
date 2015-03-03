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

import com.alee.managers.language.data.TooltipWay;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * @author Mikle Garin
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
    private WeakReference<JComponent> forComponent = null;

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

    public void setId ( final String id )
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

    public HotkeyInfo setHidden ( final boolean hidden )
    {
        this.hidden = hidden;
        return this;
    }

    public Component getTopComponent ()
    {
        return topComponent != null ? topComponent.get () : null;
    }

    public HotkeyInfo setTopComponent ( final Component topComponent )
    {
        this.topComponent = new WeakReference<Component> ( topComponent );
        return this;
    }

    public JComponent getForComponent ()
    {
        return forComponent != null ? forComponent.get () : null;
    }

    public HotkeyInfo setForComponent ( final JComponent forComponent )
    {
        this.forComponent = new WeakReference<JComponent> ( forComponent );
        return this;
    }

    public HotkeyData getHotkeyData ()
    {
        return hotkeyData;
    }

    public HotkeyInfo setHotkeyData ( final HotkeyData hotkeyData )
    {
        this.hotkeyData = hotkeyData;
        return this;
    }

    public TooltipWay getHotkeyDisplayWay ()
    {
        return hotkeyDisplayWay;
    }

    public HotkeyInfo setHotkeyDisplayWay ( final TooltipWay hotkeyDisplayWay )
    {
        this.hotkeyDisplayWay = hotkeyDisplayWay;
        return this;
    }

    public HotkeyRunnable getAction ()
    {
        return action;
    }

    public HotkeyInfo setAction ( final HotkeyRunnable action )
    {
        this.action = action;
        return this;
    }
}
