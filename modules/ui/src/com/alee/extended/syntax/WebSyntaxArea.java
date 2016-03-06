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

import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.style.StyleId;
import com.alee.utils.EventUtils;
import com.alee.utils.general.Pair;
import com.alee.utils.swing.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RUndoManager;

import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Easily customizable RSyntaxTextArea extension.
 * This class is basically the same as RSyntaxTextArea but additionally accepts SyntaxPresets for fast configuration.
 *
 * @author Mikle Garin
 * @see com.alee.extended.syntax.SyntaxPreset
 * @see com.alee.extended.syntax.SyntaxTheme
 */

public class WebSyntaxArea extends RSyntaxTextArea implements DocumentEventMethods, EventMethods
{
    /**
     * Document history manager.
     */
    protected RUndoManager undoManager;

    /**
     * Theme preset.
     * Saved separately for usage when editor scroll being created.
     */
    protected SyntaxPreset themePreset;

    /**
     * Constructs new WebSyntaxArea.
     *
     * @param presets presets to apply
     */
    public WebSyntaxArea ( final SyntaxPreset... presets )
    {
        super ();
        initialize ( presets );
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
        initialize ( presets );
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
        initialize ( presets );
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
        initialize ( presets );
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
        initialize ( presets );
    }

    /**
     * Initializes additional custom settings.
     *
     * @param presets presets to apply
     */
    protected void initialize ( final SyntaxPreset... presets )
    {
        // Applying provided presets
        applyPresets ( presets );

        // Applying default theme if it wasn't provided
        boolean themeProvided = false;
        for ( final SyntaxPreset preset : presets )
        {
            if ( preset.getType () == PresetType.theme )
            {
                themeProvided = true;
                break;
            }
        }
        if ( !themeProvided )
        {
            applyPresets ( SyntaxPreset.ideaTheme );
        }

        // Clearing history to avoid initial text removal on undo
        clearHistory ();

        // Adding redo action
        onKeyPress ( Hotkey.CTRL_SHIFT_Z, new KeyEventRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                redoLastAction ();
            }
        } );
    }

    /**
     * Creates document history manager.
     *
     * @return document history manager
     */
    @Override
    protected RUndoManager createUndoManager ()
    {
        undoManager = super.createUndoManager ();
        return undoManager;
    }

    /**
     * Returns document history manager.
     *
     * @return document history manager
     */
    public RUndoManager getUndoManager ()
    {
        return undoManager;
    }

    /**
     * Clears document history.
     */
    public void clearHistory ()
    {
        undoManager.discardAllEdits ();
    }

    /**
     * Returns properly styled and configured scroll.
     *
     * @return properly styled and configured scroll
     */
    public WebSyntaxScrollPane createScroll ()
    {
        return createScroll ( StyleId.syntaxareaScroll );
    }

    /**
     * Returns properly styled and configured scroll.
     *
     * @param id style ID
     * @return properly styled and configured scroll
     */
    public WebSyntaxScrollPane createScroll ( final StyleId id )
    {
        // Creating editor scroll with preferred settings
        final WebSyntaxScrollPane scrollPane = new WebSyntaxScrollPane ( id, this );

        // Applying syntax area theme
        if ( themePreset != null )
        {
            themePreset.apply ( this );
        }

        return scrollPane;
    }

    /**
     * Returns currently used theme preset.
     *
     * @return currently used theme preset
     */
    public SyntaxPreset getThemePreset ()
    {
        return themePreset;
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
            applyPresetImpl ( preset );
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
            applyPresetImpl ( preset );
        }
    }

    /**
     * Applies preset to this WebSyntaxArea.
     *
     * @param preset preset to apply
     */
    protected void applyPresetImpl ( final SyntaxPreset preset )
    {
        preset.apply ( this );
        if ( preset.getType () == PresetType.theme )
        {
            this.themePreset = preset;
        }
    }

    @Override
    public Pair<DocumentChangeListener, PropertyChangeListener> onChange ( final DocumentEventRunnable runnable )
    {
        return EventUtils.onChange ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseEnter ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseExit ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMenuTrigger ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, hotkey, runnable );
    }

    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusGain ( this, runnable );
    }

    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusLoss ( this, runnable );
    }
}