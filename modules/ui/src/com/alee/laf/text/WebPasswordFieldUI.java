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
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link JPasswordField} component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public class WebPasswordFieldUI extends WPasswordFieldUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Input prompt text.
     */
    protected String inputPrompt;

    /**
     * Component painter.
     */
    @DefaultPainter ( PasswordFieldPainter.class )
    protected IPasswordFieldPainter painter;

    /**
     * Runtime variables.
     */
    protected transient JPasswordField field = null;
    protected transient JComponent leadingComponent = null;
    protected transient JComponent trailingComponent = null;

    /**
     * Returns an instance of the {@link WebPasswordFieldUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebPasswordFieldUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebPasswordFieldUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Saving text field reference
        // This have to be set before calling super to make sure field reference is available
        this.field = ( JPasswordField ) c;

        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( field );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( field );

        // Removing internal components
        removeLeadingComponent ();
        removeTrailingComponent ();

        super.uninstallUI ( c );

        // Removing field reference
        field = null;
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( field, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( field, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( field, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( field );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( field, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( field );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( field, padding );
    }

    /**
     * Returns field painter.
     *
     * @return field painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets field painter.
     * Pass null to remove field painter.
     *
     * @param painter new field painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( field, this, new Consumer<IPasswordFieldPainter> ()
        {
            @Override
            public void accept ( final IPasswordFieldPainter newPainter )
            {
                WebPasswordFieldUI.this.painter = newPainter;
            }
        }, this.painter, painter, IPasswordFieldPainter.class, AdaptivePasswordFieldPainter.class );
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
            field.repaint ();
        }
    }

    @Override
    public JComponent getLeadingComponent ()
    {
        return leadingComponent;
    }

    @Override
    public JComponent setLeadingComponent ( final JComponent leadingComponent )
    {
        // Component haven't changed
        if ( this.leadingComponent == leadingComponent )
        {
            return null;
        }

        // Removing old leading component
        final JComponent old = this.leadingComponent;
        if ( this.leadingComponent != null )
        {
            field.remove ( this.leadingComponent );
            this.leadingComponent = null;
        }

        // New leading component
        if ( leadingComponent != null )
        {
            this.leadingComponent = leadingComponent;
            field.add ( leadingComponent );
        }

        // Informing about leading component change
        SwingUtils.firePropertyChanged ( field, WebLookAndFeel.LEADING_COMPONENT_PROPERTY, old, leadingComponent );

        // Updating layout
        field.revalidate ();

        return old;
    }

    @Override
    public JComponent removeLeadingComponent ()
    {
        return setLeadingComponent ( null );
    }

    @Override
    public JComponent getTrailingComponent ()
    {
        return trailingComponent;
    }

    @Override
    public JComponent setTrailingComponent ( final JComponent trailingComponent )
    {
        // Component haven't changed
        if ( this.trailingComponent == trailingComponent )
        {
            return null;
        }

        // Removing old trailing component
        final JComponent old = this.trailingComponent;
        if ( this.trailingComponent != null )
        {
            field.remove ( this.trailingComponent );
            this.trailingComponent = null;
        }

        // New trailing component
        if ( trailingComponent != null )
        {
            this.trailingComponent = trailingComponent;
            field.add ( trailingComponent );
        }

        // Informing about trailing component change
        SwingUtils.firePropertyChanged ( field, WebLookAndFeel.LEADING_COMPONENT_PROPERTY, old, trailingComponent );

        // Updating layout
        field.revalidate ();

        return old;
    }

    @Override
    public JComponent removeTrailingComponent ()
    {
        return setTrailingComponent ( null );
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
        final Dimension ps = super.getPreferredSize ( c );

        // Fix for Swing bug with pointless scrolling when field's default preferred size is already reached
        ps.width += 1;

        return PainterSupport.getPreferredSize ( c, ps, painter );
    }
}