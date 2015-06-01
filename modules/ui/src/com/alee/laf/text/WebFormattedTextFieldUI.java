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
import com.alee.managers.style.StyleManager;
import com.alee.utils.CompareUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.MarginSupport;
import com.alee.utils.laf.PaddingSupport;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;

/**
 * Custom UI for JFormattedTextField component.
 *
 * @author Mikle Garin
 */

public class WebFormattedTextFieldUI extends BasicTextFieldUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    protected FormattedTextFieldPainter painter;

    protected String inputPrompt = WebTextFieldStyle.inputPrompt;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected JFormattedTextField textField = null;
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
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebFormattedTextFieldUI ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPropertyPrefix ()
    {
        return "FormattedTextField";
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
        this.textField = ( JFormattedTextField ) c;

        // Applying skin
        StyleManager.applySkin ( textField );

        // Setup internal components
        textField.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );
        textField.setLayout ( new TextComponentLayout ( textField ) );
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
        StyleManager.removeSkin ( textField );

        // Removing internal components
        textField.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, null );
        cleanupLeadingComponent ();
        cleanupTrailingComponent ();
        textField.setLayout ( null );

        // Removing button reference
        textField = null;

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return styleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        if ( !CompareUtils.equals ( this.styleId, id ) )
        {
            this.styleId = id;
            StyleManager.applySkin ( textField );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( textField, painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns text field painter.
     *
     * @return text field painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets text field painter.
     * Pass null to remove text field painter.
     *
     * @param painter new text field painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( textField, new DataRunnable<FormattedTextFieldPainter> ()
        {
            @Override
            public void run ( final FormattedTextFieldPainter newPainter )
            {
                WebFormattedTextFieldUI.this.painter = newPainter;
            }
        }, this.painter, painter, FormattedTextFieldPainter.class, AdaptiveFormattedTextFieldPainter.class );
    }

    @Override
    protected void propertyChange ( final PropertyChangeEvent evt )
    {
        super.propertyChange ( evt );

        if ( evt.getPropertyName ().equals ( WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            SwingUtils.setEnabledRecursively ( leadingComponent, textField.isEnabled () );
            SwingUtils.setEnabledRecursively ( trailingComponent, textField.isEnabled () );
        }
    }

    public void updateInnerComponents ()
    {
        if ( leadingComponent != null )
        {
            leadingComponent.setEnabled ( textField.isEnabled () );
        }
        if ( trailingComponent != null )
        {
            trailingComponent.setEnabled ( textField.isEnabled () );
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
            textField.add ( leadingComponent, TextComponentLayout.LEADING );

            // Updating components state
            updateInnerComponents ();
        }

        // Updating layout
        textField.revalidate ();

        // Updating border
        PainterSupport.updateBorder ( getPainter () );
    }

    private void cleanupLeadingComponent ()
    {
        if ( this.leadingComponent != null )
        {
            this.leadingComponent.removeComponentListener ( componentResizeListener );
            textField.remove ( this.leadingComponent );
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
            textField.add ( trailingComponent, TextComponentLayout.TRAILING );

            // Updating components state
            updateInnerComponents ();
        }

        // Updating layout
        textField.revalidate ();

        // Updating border
        PainterSupport.updateBorder ( getPainter () );
    }

    public String getInputPrompt ()
    {
        return inputPrompt;
    }

    public void setInputPrompt ( final String inputPrompt )
    {
        if ( inputPrompt != null && !inputPrompt.equals ( this.inputPrompt ) )
        {
            this.inputPrompt = inputPrompt;
            textField.repaint ();
        }
    }

    private void cleanupTrailingComponent ()
    {
        if ( this.trailingComponent != null )
        {
            this.trailingComponent.removeComponentListener ( componentResizeListener );
            textField.remove ( this.trailingComponent );
            this.trailingComponent = null;
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
            // todo Set painted by reflection may be ( painted = true );
            ReflectUtils.setFieldValueSafely ( this, "painted", true );
            final JComponent c = getComponent ();
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}