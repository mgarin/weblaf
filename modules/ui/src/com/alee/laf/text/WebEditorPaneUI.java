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
import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Objects;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
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
public class WebEditorPaneUI extends WEditorPaneUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Input prompt text.
     */
    protected String inputPrompt;

    /**
     * Component painter.
     */
    @DefaultPainter ( EditorPanePainter.class )
    protected IEditorPanePainter painter;

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
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebEditorPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Saving editor pane reference
        editorPane = ( JEditorPane ) c;

        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( editorPane );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( editorPane );

        super.uninstallUI ( c );

        // Removing editor pane reference
        editorPane = null;
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( editorPane, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( editorPane, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( editorPane, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( editorPane );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( editorPane, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( editorPane );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( editorPane, padding );
    }

    /**
     * Returns editor pane painter.
     *
     * @return editor pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets editor pane painter.
     * Pass null to remove editor pane painter.
     *
     * @param painter new editor pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( editorPane, this, new Consumer<IEditorPanePainter> ()
        {
            @Override
            public void accept ( final IEditorPanePainter newPainter )
            {
                WebEditorPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IEditorPanePainter.class, AdaptiveEditorPanePainter.class );
    }

    @Override
    public String getInputPrompt ()
    {
        return inputPrompt;
    }

    @Override
    public void setInputPrompt ( final String text )
    {
        if ( Objects.notEquals ( text, this.inputPrompt ) )
        {
            this.inputPrompt = text;
            editorPane.repaint ();
        }
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    protected void paintSafely ( final Graphics g )
    {
        if ( painter != null )
        {
            // Updating painted field
            // This is important for proper basic UI usage
            ReflectUtils.setFieldValueSafely ( this, "painted", true );

            // Painting text component
            final JComponent c = getComponent ();
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}