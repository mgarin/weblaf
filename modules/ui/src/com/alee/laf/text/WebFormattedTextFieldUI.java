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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CompareUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;

/**
 * Custom UI for {@link JFormattedTextField} component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebFormattedTextFieldUI extends WFormattedTextFieldUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter (FormattedTextFieldPainter.class)
    protected IFormattedTextFieldPainter painter;

    /**
     * Input prompt text.
     */
    protected String inputPrompt;

    /**
     * Runtime variables.
     */
    protected JFormattedTextField field = null;
    protected Insets margin = null;
    protected Insets padding = null;
    protected JComponent leadingComponent = null;
    protected JComponent trailingComponent = null;

    /**
     * Listeners.
     */
    protected ComponentAdapter componentResizeListener;

    /**
     * Returns an instance of the WebFormattedTextFieldUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebFormattedTextFieldUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebFormattedTextFieldUI ();
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

        // Saving text field reference
        this.field = ( JFormattedTextField ) c;

        // Custom listener for leading/trailing components
        componentResizeListener = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                PainterSupport.updateBorder ( getPainter () );
            }
        };

        // Applying skin
        StyleManager.installSkin ( field );
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
        StyleManager.uninstallSkin ( field );

        // Removing internal components
        field.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, null );
        removeLeadingComponent ();
        removeTrailingComponent ();
        field.setLayout ( null );

        // Removing field reference
        field = null;

        super.uninstallUI ( c );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( field, painter );
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
     * Returns field painter.
     *
     * @return field painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets field painter.
     * Pass null to remove field painter.
     *
     * @param painter new field painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( field, new DataRunnable<IFormattedTextFieldPainter> ()
        {
            @Override
            public void run ( final IFormattedTextFieldPainter newPainter )
            {
                WebFormattedTextFieldUI.this.painter = newPainter;
            }
        }, this.painter, painter, IFormattedTextFieldPainter.class, AdaptiveFormattedTextFieldPainter.class );
    }

    @Override
    protected void propertyChange ( final PropertyChangeEvent evt )
    {
        super.propertyChange ( evt );

        if ( evt.getPropertyName ().equals ( WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            SwingUtils.setEnabledRecursively ( leadingComponent, field.isEnabled () );
            SwingUtils.setEnabledRecursively ( trailingComponent, field.isEnabled () );
        }
    }

    @Override
    public String getInputPrompt ()
    {
        return inputPrompt;
    }

    @Override
    public void setInputPrompt ( final String text )
    {
        if ( !CompareUtils.equals ( text, this.inputPrompt ) )
        {
            this.inputPrompt = text;
            field.repaint ();
        }
    }

    @Override
    public JComponent getLeadingComponent ()
    {
        return leadingComponent;
    }

    @Override
    public void setLeadingComponent ( final JComponent leadingComponent )
    {
        if ( this.leadingComponent == leadingComponent )
        {
            return;
        }

        // Removing old leading component
        removeLeadingComponent ();

        // New leading component
        if ( leadingComponent != null )
        {
            this.leadingComponent = leadingComponent;

            // Registering resize listener
            this.leadingComponent.addComponentListener ( componentResizeListener );

            // Adding component
            field.add ( leadingComponent, TextFieldLayout.LEADING );
        }

        // Updating layout
        field.revalidate ();

        // Updating border
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public void removeLeadingComponent ()
    {
        if ( this.leadingComponent != null )
        {
            this.leadingComponent.removeComponentListener ( componentResizeListener );
            field.remove ( this.leadingComponent );
            this.leadingComponent = null;
        }
    }

    @Override
    public JComponent getTrailingComponent ()
    {
        return trailingComponent;
    }

    @Override
    public void setTrailingComponent ( final JComponent trailingComponent )
    {
        if ( this.trailingComponent == trailingComponent )
        {
            return;
        }

        // Removing old trailing component
        removeTrailingComponent ();

        // New trailing component
        if ( trailingComponent != null )
        {
            this.trailingComponent = trailingComponent;

            // Registering resize listener
            this.trailingComponent.addComponentListener ( componentResizeListener );

            // Adding component
            field.add ( trailingComponent, TextFieldLayout.TRAILING );
        }

        // Updating layout
        field.revalidate ();

        // Updating border
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public void removeTrailingComponent ()
    {
        if ( this.trailingComponent != null )
        {
            this.trailingComponent.removeComponentListener ( componentResizeListener );
            field.remove ( this.trailingComponent );
            this.trailingComponent = null;
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
        final Dimension ps = super.getPreferredSize ( c );

        // Fix for Swing bug with pointless scrolling when field's default preferred size is already reached
        ps.width += 1;

        return PainterSupport.getPreferredSize ( c, ps, painter );
    }
}