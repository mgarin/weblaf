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

package com.alee.laf.text;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.managers.style.StyleManager;
import com.alee.painter.PainterSupport;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link JEditorPane} component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public class WebEditorPaneUI extends WEditorPaneUI
{
    /**
     * Input prompt text.
     */
    protected String inputPrompt;

    /**
     * Runtime variables.
     */
    protected transient JEditorPane editorPane = null;

    /**
     * Returns an instance of the {@link WebEditorPaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebEditorPaneUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebEditorPaneUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving editor pane reference
        editorPane = ( JEditorPane ) c;

        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( editorPane );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( editorPane );

        super.uninstallUI ( c );

        // Removing editor pane reference
        editorPane = null;
    }

    @Nullable
    @Override
    public String getInputPrompt ()
    {
        return inputPrompt;
    }

    @Override
    public void setInputPrompt ( @Nullable final String text )
    {
        if ( Objects.notEquals ( text, this.inputPrompt ) )
        {
            this.inputPrompt = text;
            editorPane.repaint ();
        }
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    protected void paintSafely ( @NotNull final Graphics g )
    {
        // Updating painted field
        // This is important for proper basic UI usage
        ReflectUtils.setFieldValueSafely ( this, "painted", true );

        // Painting text component
        PainterSupport.paint ( g, getComponent (), this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ) );
    }
}