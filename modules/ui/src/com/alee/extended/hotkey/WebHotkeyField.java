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

package com.alee.extended.hotkey;

import com.alee.laf.text.WebTextField;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LM;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebHotkeyField extends WebTextField
{
    /**
     * todo 1. Requires rework into a proper standalone component
     * todo 2. Add LanguageUpdater support
     */

    private static final String EMPTY_HOTKEY_TEXT_KEY = "weblaf.ex.hotkeyfield.press";

    private final List<Integer> keys = new ArrayList<Integer> ();

    private boolean isCtrl = false;
    private boolean isAlt = false;
    private boolean isShift = false;
    private Integer keyCode = null;

    public WebHotkeyField ()
    {
        super ();

        updateFieldText ();
        setEditable ( false );
        setBackground ( Color.WHITE );
        setHorizontalAlignment ( SwingConstants.CENTER );
        setSelectionColor ( getBackground () );
        setSelectedTextColor ( getForeground () );

        addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyPressed ( final KeyEvent e )
            {
                if ( keys.size () == 0 )
                {
                    clearData ();
                }

                keys.add ( e.getKeyCode () );
                updateKeys ( e );
                updateFieldText ();
            }

            @Override
            public void keyReleased ( final KeyEvent e )
            {
                keys.remove ( ( Object ) e.getKeyCode () );
                updateFieldText ();
            }

            private void updateKeys ( final KeyEvent e )
            {
                isCtrl = SwingUtils.isCtrl ( e );
                isAlt = SwingUtils.isAlt ( e );
                isShift = SwingUtils.isShift ( e );
                if ( e.getKeyCode () != KeyEvent.VK_CONTROL && e.getKeyCode () != KeyEvent.VK_ALT && e.getKeyCode () != KeyEvent.VK_SHIFT )
                {
                    keyCode = e.getKeyCode ();
                }
            }
        } );

        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                if ( isEnabled () && SwingUtils.isDoubleClick ( e ) )
                {
                    clearData ();
                    updateFieldText ();
                }
            }
        } );
    }

    public void updateFieldText ()
    {
        setText ( isEmpty () ? LM.get ( EMPTY_HOTKEY_TEXT_KEY ) : getHotkeyData ().toString () );
    }

    @Override
    public boolean isEmpty ()
    {
        return keyCode == null && ( !isCtrl && !isAlt && !isShift || keys.size () == 0 );
    }

    public boolean isCtrl ()
    {
        return isCtrl;
    }

    public void setCtrl ( final boolean ctrl )
    {
        isCtrl = ctrl;
    }

    public boolean isAlt ()
    {
        return isAlt;
    }

    public void setAlt ( final boolean alt )
    {
        isAlt = alt;
    }

    public boolean isShift ()
    {
        return isShift;
    }

    public void setShift ( final boolean shift )
    {
        isShift = shift;
    }

    public Integer getKeyCode ()
    {
        return keyCode;
    }

    public void setKeyCode ( final Integer keyCode )
    {
        this.keyCode = keyCode;
        updateFieldText ();
    }

    public HotkeyData getHotkeyData ()
    {
        return new HotkeyData ( isCtrl (), isAlt (), isShift (), getKeyCode () );
    }

    public void setHotkeyData ( final HotkeyData hotkeyData )
    {
        setCtrl ( hotkeyData.isCtrl () );
        setAlt ( hotkeyData.isAlt () );
        setShift ( hotkeyData.isShift () );
        setKeyCode ( hotkeyData.getKeyCode () );
    }

    public void clearData ()
    {
        this.keyCode = null;
        this.isCtrl = false;
        this.isAlt = false;
        this.isShift = false;
    }
}