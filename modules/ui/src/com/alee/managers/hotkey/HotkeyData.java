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

import com.alee.utils.MergeUtils;
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
 */

@XStreamAlias ("HotkeyData")
public class HotkeyData implements Serializable, Cloneable
{
    /**
     * Whether hotkey activation requires CTRL modifier or not.
     */
    @XStreamAsAttribute
    protected boolean isCtrl;

    /**
     * Whether hotkey activation requires ALT modifier or not.
     */
    @XStreamAsAttribute
    protected boolean isAlt;

    /**
     * Whether hotkey activation requires SHIFT modifier or not.
     */
    @XStreamAsAttribute
    protected boolean isShift;

    /**
     * Key code required for the hotkey activation.
     */
    @XStreamAsAttribute
    protected Integer keyCode;

    /**
     * Kept in runtime hotkey hash code.
     */
    protected transient Integer hashCode;

    /**
     * Constructs empty hotkey data.
     */
    public HotkeyData ()
    {
        super ();
        this.isCtrl = false;
        this.isAlt = false;
        this.isShift = false;
        this.keyCode = null;
        this.hashCode = null;
    }

    /**
     * Constructs hotkey using the specified KeyEvent.
     *
     * @param keyEvent KeyEvent to convert
     */
    public HotkeyData ( final KeyEvent keyEvent )
    {
        super ();
        this.isCtrl = SwingUtils.isCtrl ( keyEvent );
        this.isAlt = SwingUtils.isAlt ( keyEvent );
        this.isShift = SwingUtils.isShift ( keyEvent );
        this.keyCode = keyEvent.getKeyCode ();
        this.hashCode = null;
    }

    /**
     * Constructs hotkey using specified key code without any modifiers.
     *
     * @param keyCode key code required for the hotkey activation
     */
    public HotkeyData ( final Integer keyCode )
    {
        super ();
        this.isCtrl = false;
        this.isAlt = false;
        this.isShift = false;
        this.keyCode = keyCode;
        this.hashCode = null;
    }

    /**
     * Constructs hotkey using specified modifiers and key code.
     *
     * @param isCtrl  whether hotkey activation requires CTRL modifier or not
     * @param isAlt   whether hotkey activation requires ALT modifier or not
     * @param isShift whether hotkey activation requires SHIFT modifier or not
     * @param keyCode key code required for the hotkey activation
     */
    public HotkeyData ( final boolean isCtrl, final boolean isAlt, final boolean isShift, final Integer keyCode )
    {
        super ();
        this.isCtrl = isCtrl;
        this.isAlt = isAlt;
        this.isShift = isShift;
        this.keyCode = keyCode;
        this.hashCode = null;
    }

    /**
     * Constructs hotkey using the specified key stroke.
     *
     * @param keyStroke key stroke
     */
    public HotkeyData ( final KeyStroke keyStroke )
    {
        super ();
        setModifiers ( keyStroke.getModifiers () );
        this.keyCode = keyStroke.getKeyCode ();
        this.hashCode = null;
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
     * Sets whether hotkey activation should require CTRL modifier or not.
     *
     * @param ctrl whether hotkey activation should require CTRL modifier or not
     */
    public void setCtrl ( final boolean ctrl )
    {
        isCtrl = ctrl;
        this.hashCode = null;
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
     * Sets whether hotkey activation should require ALT modifier or not.
     *
     * @param alt whether hotkey activation should require ALT modifier or not
     */
    public void setAlt ( final boolean alt )
    {
        isAlt = alt;
        this.hashCode = null;
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
     * Sets whether hotkey activation should require SHIFT modifier or not.
     *
     * @param shift whether hotkey activation should require SHIFT modifier or not
     */
    public void setShift ( final boolean shift )
    {
        isShift = shift;
        this.hashCode = null;
    }

    /**
     * Returns key code required for the hotkey activation.
     *
     * @return key code required for the hotkey activation
     */
    public Integer getKeyCode ()
    {
        return keyCode;
    }

    /**
     * Sets key code required for the hotkey activation.
     *
     * @param keyCode key code required for the hotkey activation
     */
    public void setKeyCode ( final Integer keyCode )
    {
        this.keyCode = keyCode;
        this.hashCode = null;
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
    public boolean isTriggered ( final KeyEvent event )
    {
        return areControlsTriggered ( event ) && isKeyTriggered ( event );
    }

    /**
     * Returns whether hotkey controls are triggered by the key event or not.
     *
     * @param event processed key event
     * @return true if hotkey controls are triggered by the key event, false otherwise
     */
    public boolean areControlsTriggered ( final KeyEvent event )
    {
        return SwingUtils.isShortcut ( event ) == isCtrl && SwingUtils.isAlt ( event ) == isAlt && SwingUtils.isShift ( event ) == isShift;
    }

    /**
     * Returns whether key is triggered by the key event or not.
     *
     * @param event processed key event
     * @return true if key is triggered by the key event, false otherwise
     */
    public boolean isKeyTriggered ( final KeyEvent event )
    {
        // todo Fix for other command keys (like cmd on Mac OS X)
        return event.getKeyCode () == keyCode;
    }

    /**
     * Returns key stroke for this hotkey.
     *
     * @return key stroke for this hotkey
     */
    public KeyStroke getKeyStroke ()
    {
        return KeyStroke.getKeyStroke ( keyCode, getModifiers () );
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
     * Sets hotkey modifiers.
     *
     * @param modifiers modifiers
     */
    public void setModifiers ( final int modifiers )
    {
        isCtrl = SwingUtils.isCtrl ( modifiers );
        isAlt = SwingUtils.isAlt ( modifiers );
        isShift = SwingUtils.isShift ( modifiers );
    }

    /**
     * Indicates whether other hotkey is equal to this one.
     *
     * @param obj other hotkey
     * @return true if other hotkey is equal to this one, false otherwise
     */
    @Override
    public boolean equals ( final Object obj )
    {
        return obj != null && obj instanceof HotkeyData && obj.hashCode () == hashCode ();
    }

    /**
     * Returns hotkey text representation.
     *
     * @return hotkey text representation
     */
    @Override
    public String toString ()
    {
        return SwingUtils.hotkeyToString ( this );
    }

    /**
     * Returns hotkey hash code.
     *
     * @return hotkey hash code
     */
    @Override
    public int hashCode ()
    {
        if ( hashCode == null )
        {
            hashCode = toString ().hashCode ();
        }
        return hashCode;
    }

    /**
     * Returns cloned HotkeyData instance.
     *
     * @return cloned HotkeyData instance
     */
    @Override
    protected HotkeyData clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }
}