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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.behavior.DocumentChangeBehavior;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.style.Skin;
import com.alee.managers.style.SkinListener;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.skin.dark.DarkSkin;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RUndoManager;

import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.List;

/**
 * Easily customizable {@link RSyntaxTextArea} extension.
 * This class is basically the same as RSyntaxTextArea but additionally accepts {@link SyntaxPreset}s for fast configuration.
 *
 * @author Mikle Garin
 * @see com.alee.extended.syntax.SyntaxPreset
 * @see com.alee.extended.syntax.SyntaxTheme
 */
public class WebSyntaxArea extends RSyntaxTextArea implements DocumentEventMethods<WebSyntaxArea>, EventMethods
{
    /**
     * Document history manager.
     */
    protected RUndoManager undoManager;

    /**
     * Theme preset.
     * Saved separately for usage when editor scroll being created.
     */
    @Nullable
    protected SyntaxPreset themePreset;

    /**
     * Constructs new WebSyntaxArea.
     *
     * @param presets presets to apply
     */
    public WebSyntaxArea ( @NotNull final SyntaxPreset... presets )
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
    public WebSyntaxArea ( @Nullable final String text, @NotNull final SyntaxPreset... presets )
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
    public WebSyntaxArea ( final int rows, final int cols, @NotNull final SyntaxPreset... presets )
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
    public WebSyntaxArea ( @Nullable final String text, final int rows, final int cols, @NotNull final SyntaxPreset... presets )
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
    public WebSyntaxArea ( final int textMode, @NotNull final SyntaxPreset... presets )
    {
        super ( textMode );
        initialize ( presets );
    }

    /**
     * Initializes additional custom settings.
     *
     * @param presets presets to apply
     */
    protected void initialize ( @NotNull final SyntaxPreset... presets )
    {
        // Applying provided presets
        applyPresets ( presets );

        // Applying default theme if it wasn't provided
        if ( themePreset == null )
        {
            // todo A temporary solution for initial theme selected according to skin
            applyPresets ( StyleManager.getSkin () instanceof DarkSkin ? SyntaxPreset.darkTheme : SyntaxPreset.ideaTheme );

            // todo A temporary solution for switching theme according to skin
            StyleManager.addSkinListener ( new SkinListener ()
            {
                @Override
                public void skinChanged ( @Nullable final Skin previous, @NotNull final Skin current )
                {
                    applyPresets ( StyleManager.getSkin () instanceof DarkSkin ? SyntaxPreset.darkTheme : SyntaxPreset.ideaTheme );
                }
            } );
        }

        // Clearing history to avoid initial text removal on undo
        clearHistory ();

        // Adding redo action
        onKeyPress ( Hotkey.CTRL_SHIFT_Z, new KeyEventRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
    public WebSyntaxScrollPane createScroll ( @NotNull final StyleId id )
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
    @Nullable
    public SyntaxPreset getThemePreset ()
    {
        return themePreset;
    }

    /**
     * Applies presets to this WebSyntaxArea.
     *
     * @param presets presets to apply
     */
    public void applyPresets ( @NotNull final SyntaxPreset... presets )
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
    public void applyPresets ( @NotNull final List<SyntaxPreset> presets )
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
    protected void applyPresetImpl ( @NotNull final SyntaxPreset preset )
    {
        preset.apply ( this );
        if ( preset.getType () == PresetType.theme )
        {
            this.themePreset = preset;
        }
    }

    @NotNull
    @Override
    public DocumentChangeBehavior<WebSyntaxArea> onChange ( @NotNull final DocumentEventRunnable<WebSyntaxArea> runnable )
    {
        return DocumentEventMethodsImpl.onChange ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMousePress ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMousePress ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseEnter ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseExit ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseDrag ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseDrag ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseClick ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseClick ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDoubleClick ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMenuTrigger ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyType ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyType ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyPress ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyPress ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyRelease ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyRelease ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public FocusAdapter onFocusGain ( @NotNull final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @NotNull
    @Override
    public FocusAdapter onFocusLoss ( @NotNull final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDragStart ( final int shift, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDragStart ( final int shift, @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, mouseButton, runnable );
    }
}