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

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CompareUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import java.awt.*;

/**
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebEditorPaneUI extends BasicEditorPaneUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( EditorPanePainter.class )
    protected IEditorPanePainter painter;

    /**
     * Input prompt text.
     */
    protected String inputPrompt;

    /**
     * Runtime variables.
     */
    protected JEditorPane editorPane = null;
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebEditorPaneUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebEditorPaneUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebEditorPaneUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Saving editor pane reference
        editorPane = ( JEditorPane ) c;

        // Applying skin
        StyleManager.installSkin ( editorPane );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( editorPane );

        // Removing editor pane reference
        editorPane = null;

        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( editorPane );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( editorPane, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( editorPane, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns editor pane painter.
     *
     * @return editor pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets editor pane painter.
     * Pass null to remove editor pane painter.
     *
     * @param painter new editor pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( editorPane, new DataRunnable<IEditorPanePainter> ()
        {
            @Override
            public void run ( final IEditorPanePainter newPainter )
            {
                WebEditorPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IEditorPanePainter.class, AdaptiveEditorPanePainter.class );
    }

    /**
     * Returns input prompt text.
     *
     * @return input prompt text
     */
    public String getInputPrompt ()
    {
        return inputPrompt;
    }

    /**
     * Sets input prompt text.
     *
     * @param text input prompt text
     */
    public void setInputPrompt ( final String text )
    {
        if ( !CompareUtils.equals ( text, this.inputPrompt ) )
        {
            this.inputPrompt = text;
            editorPane.repaint ();
        }
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
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}