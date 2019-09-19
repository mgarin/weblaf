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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.Serializable;

/**
 * Single hotkey settings description class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-HotkeyManager">How to use HotkeyManager</a>
 * @see HotkeyManager
 */
@XStreamAlias ( "HotkeyData" )
public class HotkeyData implements Cloneable, Serializable
{
    /**
     * Whether hotkey activation requires CTRL modifier or not.
     */
    @XStreamAsAttribute
    protected final boolean isCtrl;

    /**
     * Whether hotkey activation requires ALT modifier or not.
     */
    @XStreamAsAttribute
    protected final boolean isAlt;

    /**
     * Whether hotkey activation requires SHIFT modifier or not.
     */
    @XStreamAsAttribute
    protected final boolean isShift;

    /**
     * Key code required for the hotkey activation.
     */
    @Nullable
    @XStreamAsAttribute
    protected final Integer keyCode;

    /**
     * Constructs empty hotkey data.
     */
    public HotkeyData ()
    {
        this ( false, false, false, null );
    }

    /**
     * Constructs hotkey using the specified KeyEvent.
     *
     * @param keyEvent KeyEvent to convert
     */
    public HotkeyData ( @NotNull final KeyEvent keyEvent )
    {
        this (
                SwingUtils.isCtrl ( keyEvent ),
                SwingUtils.isAlt ( keyEvent ),
                SwingUtils.isShift ( keyEvent ),
                keyEvent.getKeyCode ()
        );
    }

    /**
     * Constructs hotkey using the specified key stroke.
     *
     * @param keyStroke key stroke
     */
    public HotkeyData ( @NotNull final KeyStroke keyStroke )
    {
        this (
                SwingUtils.isCtrl ( keyStroke.getModifiers () ),
                SwingUtils.isAlt ( keyStroke.getModifiers () ),
                SwingUtils.isShift ( keyStroke.getModifiers () ),
                keyStroke.getKeyCode ()
        );
    }

    /**
     * Constructs hotkey using specified key code without any modifiers.
     *
     * @param keyCode key code required for the hotkey activation
     */
    public HotkeyData ( final Integer keyCode )
    {
        this ( false, false, false, keyCode );
    }

    /**
     * Constructs hotkey using specified modifiers and key code.
     *
     * @param isCtrl  whether hotkey activation requires CTRL modifier or not
     * @param isAlt   whether hotkey activation requires ALT modifier or not
     * @param isShift whether hotkey activation requires SHIFT modifier or not
     * @param keyCode key code required for the hotkey activation
     */
    public HotkeyData ( final boolean isCtrl, final boolean isAlt, final boolean isShift, @Nullable final Integer keyCode )
    {
        this.isCtrl = isCtrl;
        this.isAlt = isAlt;
        this.isShift = isShift;
        this.keyCode = keyCode;
    }

    /**
     * Returns whether hotkey activation requires CTRL modifier or not.
     *
     * @return true if hotkey activation requires CTRL modifier, false otherwise
     */
    public boolean isCtrl ()
    {
        return isCtrl;
    }

    /**
     * Returns whether hotkey activation requires ALT modifier or not.
     *
     * @return true if hotkey activation requires ALT modifier, false otherwise
     */
    public boolean isAlt ()
    {
        return isAlt;
    }

    /**
     * Returns whether hotkey activation requires SHIFT modifier or not.
     *
     * @return true if hotkey activation requires SHIFT modifier, false otherwise
     */
    public boolean isShift ()
    {
        return isShift;
    }

    /**
     * Returns key code required for the hotkey activation.
     *
     * @return key code required for the hotkey activation
     */
    @Nullable
    public Integer getKeyCode ()
    {
        return keyCode;
    }

    /**
     * Returns hotkey modifiers.
     *
     * @return hotkey modifiers
     */
    public int getModifiers ()
    {
        return ( isCtrl ? SwingUtils.getSystemShortcutModifier () : 0 ) |
                ( isAlt ? KeyEvent.ALT_MASK : 0 ) | ( isShift ? KeyEvent.SHIFT_MASK : 0 );
    }

    /**
     * Returns whether key code is set or not.
     *
     * @return true if key code is set, false otherwise
     */
    public boolean isHotkeySet ()
    {
        return keyCode != null;
    }

    /**
     * Returns whether hotkey is triggered by the key event or not.
     *
     * @param event processed key event
     * @return true if hotkey is triggered by the key event, false otherwise
     */
    public boolean isTriggered ( @NotNull final KeyEvent event )
    {
        return areControlsTriggered ( event ) && isKeyTriggered ( event );
    }

    /**
     * Returns whether hotkey controls are triggered by the key event or not.
     *
     * @param event processed key event
     * @return true if hotkey controls are triggered by the key event, false otherwise
     */
    public boolean areControlsTriggered ( @NotNull final KeyEvent event )
    {
        return SwingUtils.isShortcut ( event ) == isCtrl &&
                SwingUtils.isAlt ( event ) == isAlt &&
                SwingUtils.isShift ( event ) == isShift;
    }

    /**
     * Returns whether key is triggered by the key event or not.
     *
     * @param event processed key event
     * @return true if key is triggered by the key event, false otherwise
     */
    public boolean isKeyTriggered ( @NotNull final KeyEvent event )
    {
        return keyCode != null && event.getKeyCode () == keyCode;
    }

    /**
     * Returns key stroke for this hotkey.
     *
     * @return key stroke for this hotkey
     */
    @NotNull
    public KeyStroke getKeyStroke ()
    {
        if ( keyCode == null )
        {
            throw new HotkeyException ( "KeyStroke can only be retrieved from HotkeyData that contains key code" );
        }
        return KeyStroke.getKeyStroke ( keyCode, getModifiers () );
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash ( isCtrl, isAlt, isShift, keyCode );
    }

    @Override
    public boolean equals ( @Nullable final Object other )
    {
        return other instanceof HotkeyData && other.hashCode () == hashCode ();
    }

    @NotNull
    @Override
    public String toString ()
    {
        return SwingUtils.hotkeyToString ( isCtrl (), isAlt (), isShift (), getKeyCode () );
    }
}