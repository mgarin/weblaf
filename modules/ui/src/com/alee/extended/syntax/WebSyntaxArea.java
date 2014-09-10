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

package com.alee.extended.syntax;

import com.alee.managers.hotkey.HotkeyData;
import com.alee.utils.EventUtils;
import com.alee.utils.swing.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.List;

/**
 * Easily customizable RSyntaxTextArea extension.
 * This class is basically the same as RSyntaxTextArea but additionally accepts SyntaxPresets for fast configuration.
 *
 * @author Mikle Garin
 * @see com.alee.extended.syntax.SyntaxPreset
 * @see com.alee.extended.syntax.SyntaxTheme
 */

public class WebSyntaxArea extends RSyntaxTextArea implements EventMethods
{
    /**
     * Constructs new WebSyntaxArea.
     *
     * @param presets presets to apply
     */
    public WebSyntaxArea ( final SyntaxPreset... presets )
    {
        super ();
        applyPresets ( presets );
    }

    /**
     * Constructs new WebSyntaxArea.
     *
     * @param text    syntax area text
     * @param presets presets to apply
     */
    public WebSyntaxArea ( final String text, final SyntaxPreset... presets )
    {
        super ( text );
        applyPresets ( presets );
    }

    /**
     * Constructs new WebSyntaxArea.
     *
     * @param rows    visible rows count
     * @param cols    visible columns count
     * @param presets presets to apply
     */
    public WebSyntaxArea ( final int rows, final int cols, final SyntaxPreset... presets )
    {
        super ( rows, cols );
        applyPresets ( presets );
    }

    /**
     * Constructs new WebSyntaxArea.
     *
     * @param text    syntax area text
     * @param rows    visible rows count
     * @param cols    visible columns count
     * @param presets presets to apply
     */
    public WebSyntaxArea ( final String text, final int rows, final int cols, final SyntaxPreset... presets )
    {
        super ( text, rows, cols );
        applyPresets ( presets );
    }

    /**
     * Constructs new WebSyntaxArea.
     *
     * @param textMode text edit mode, either INSERT_MODE or OVERWRITE_MODE
     * @param presets  presets to apply
     */
    public WebSyntaxArea ( final int textMode, final SyntaxPreset... presets )
    {
        super ( textMode );
        applyPresets ( presets );
    }

    /**
     * Applies presets to this WebSyntaxArea.
     *
     * @param presets presets to apply
     */
    public void applyPresets ( final SyntaxPreset... presets )
    {
        for ( final SyntaxPreset preset : presets )
        {
            preset.apply ( this );
        }
    }

    /**
     * Applies presets to this WebSyntaxArea.
     *
     * @param presets presets to apply
     */
    public void applyPresets ( final List<SyntaxPreset> presets )
    {
        for ( final SyntaxPreset preset : presets )
        {
            preset.apply ( this );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, mouseButton, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onDoubleClick ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMenuTrigger ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, hotkey, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusGain ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusLoss ( this, runnable );
    }
}