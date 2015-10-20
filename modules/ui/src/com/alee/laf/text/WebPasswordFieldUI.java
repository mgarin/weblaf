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

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CompareUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.managers.style.MarginSupport;
import com.alee.managers.style.PaddingSupport;
import com.alee.managers.style.ShapeProvider;
import com.alee.managers.style.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;

/**
 * @author Mikle Garin
 */

public class WebPasswordFieldUI extends BasicPasswordFieldUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    protected PasswordFieldPainter painter;

    /**
     * Input prompt text.
     */
    protected String inputPrompt = WebTextFieldStyle.inputPrompt;

    /**
     * Runtime variables.
     */
    protected JPasswordField passwordField = null;
    protected Insets margin = null;
    protected Insets padding = null;
    protected JComponent leadingComponent = null;
    protected JComponent trailingComponent = null;

    /**
     * Listeners.
     */
    protected ComponentAdapter componentResizeListener;

    /**
     * Returns an instance of the WebPasswordFieldUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebPasswordFieldUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebPasswordFieldUI ();
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
        this.passwordField = ( JPasswordField ) c;

        // Applying skin
        StyleManager.installSkin ( passwordField );

        // Setup internal components
        passwordField.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );
        passwordField.setLayout ( new TextComponentLayout ( passwordField ) );
        componentResizeListener = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                PainterSupport.updateBorder ( getPainter () );
            }
        };
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
        StyleManager.uninstallSkin ( passwordField );

        // Removing internal components
        passwordField.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, null );
        cleanupLeadingComponent ();
        cleanupTrailingComponent ();
        passwordField.setLayout ( null );

        // Removing button reference
        passwordField = null;

        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( passwordField );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( passwordField, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( passwordField, painter );
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
     * Returns password field painter.
     *
     * @return text field painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets password field painter.
     * Pass null to remove password field painter.
     *
     * @param painter new password field painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( passwordField, new DataRunnable<PasswordFieldPainter> ()
        {
            @Override
            public void run ( final PasswordFieldPainter newPainter )
            {
                WebPasswordFieldUI.this.painter = newPainter;
            }
        }, this.painter, painter, PasswordFieldPainter.class, AdaptivePasswordFieldPainter.class );
    }

    @Override
    protected void propertyChange ( final PropertyChangeEvent evt )
    {
        super.propertyChange ( evt );

        if ( evt.getPropertyName ().equals ( WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            SwingUtils.setEnabledRecursively ( leadingComponent, passwordField.isEnabled () );
            SwingUtils.setEnabledRecursively ( trailingComponent, passwordField.isEnabled () );
        }
    }

    public void updateInnerComponents ()
    {
        if ( leadingComponent != null )
        {
            leadingComponent.setEnabled ( passwordField.isEnabled () );
        }
        if ( trailingComponent != null )
        {
            trailingComponent.setEnabled ( passwordField.isEnabled () );
        }
    }

    public JComponent getLeadingComponent ()
    {
        return leadingComponent;
    }

    public void setLeadingComponent ( final JComponent leadingComponent )
    {
        if ( this.leadingComponent == leadingComponent )
        {
            return;
        }

        // Removing old leading component
        cleanupLeadingComponent ();

        // New leading component
        if ( leadingComponent != null )
        {
            this.leadingComponent = leadingComponent;

            // Registering resize listener
            this.leadingComponent.addComponentListener ( componentResizeListener );

            // Adding component
            passwordField.add ( leadingComponent, TextComponentLayout.LEADING );

            // Updating components state
            updateInnerComponents ();
        }

        // Updating layout
        passwordField.revalidate ();

        // Updating border
        PainterSupport.updateBorder ( getPainter () );
    }

    private void cleanupLeadingComponent ()
    {
        if ( this.leadingComponent != null )
        {
            this.leadingComponent.removeComponentListener ( componentResizeListener );
            passwordField.remove ( this.leadingComponent );
            this.leadingComponent = null;
        }
    }

    public JComponent getTrailingComponent ()
    {
        return trailingComponent;
    }

    public void setTrailingComponent ( final JComponent trailingComponent )
    {
        if ( this.trailingComponent == trailingComponent )
        {
            return;
        }

        // Removing old trailing component
        cleanupTrailingComponent ();

        // New trailing component
        if ( trailingComponent != null )
        {
            this.trailingComponent = trailingComponent;

            // Registering resize listener
            this.trailingComponent.addComponentListener ( componentResizeListener );

            // Adding component
            passwordField.add ( trailingComponent, TextComponentLayout.TRAILING );

            // Updating components state
            updateInnerComponents ();
        }

        // Updating layout
        passwordField.revalidate ();

        // Updating border
        PainterSupport.updateBorder ( getPainter () );
    }

    private void cleanupTrailingComponent ()
    {
        if ( this.trailingComponent != null )
        {
            this.trailingComponent.removeComponentListener ( componentResizeListener );
            passwordField.remove ( this.trailingComponent );
            this.trailingComponent = null;
        }
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
            passwordField.repaint ();
        }
    }

    /**
     * Sets painter here because paint method is final
     *
     * @param g gra
     */
    @Override
    protected void paintSafely ( final Graphics g )
    {
        if ( painter != null )
        {
            ReflectUtils.setFieldValueSafely ( this, "painted", true );
            final JComponent c = getComponent ();
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
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