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
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.*;
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
public class WebPasswordFieldUI extends WPasswordFieldUI
{
    /**
     * Input prompt text.
     */
    protected String inputPrompt;

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
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebPasswordFieldUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving text field reference
        // This have to be set before calling super to make sure field reference is available
        this.field = ( JPasswordField ) c;

        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( field );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
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
            field.repaint ();
        }
    }

    @Nullable
    @Override
    public JComponent getLeadingComponent ()
    {
        return leadingComponent;
    }

    @Nullable
    @Override
    public JComponent setLeadingComponent ( @Nullable final JComponent leadingComponent )
    {
        final JComponent old = this.leadingComponent;
        if ( leadingComponent != this.leadingComponent )
        {
            // Removing old leading component
            if ( this.leadingComponent != null )
            {
                field.remove ( this.leadingComponent );
                this.leadingComponent = null;
            }

            // Adding new leading component
            if ( leadingComponent != null )
            {
                this.leadingComponent = leadingComponent;
                field.add ( leadingComponent );
            }

            // Informing about leading component change
            SwingUtils.firePropertyChanged ( field, WebLookAndFeel.LEADING_COMPONENT_PROPERTY, old, leadingComponent );

            // Updating layout
            field.revalidate ();
        }
        return old;
    }

    @Nullable
    @Override
    public JComponent removeLeadingComponent ()
    {
        return setLeadingComponent ( null );
    }

    @Nullable
    @Override
    public JComponent getTrailingComponent ()
    {
        return trailingComponent;
    }

    @Nullable
    @Override
    public JComponent setTrailingComponent ( @Nullable final JComponent trailingComponent )
    {
        final JComponent old = this.trailingComponent;
        if ( trailingComponent != this.trailingComponent )
        {
            // Removing old trailing component
            if ( this.trailingComponent != null )
            {
                field.remove ( this.trailingComponent );
                this.trailingComponent = null;
            }

            // Adding new trailing component
            if ( trailingComponent != null )
            {
                this.trailingComponent = trailingComponent;
                field.add ( trailingComponent );
            }

            // Informing about trailing component change
            SwingUtils.firePropertyChanged ( field, WebLookAndFeel.LEADING_COMPONENT_PROPERTY, old, trailingComponent );

            // Updating layout
            field.revalidate ();
        }
        return old;
    }

    @Nullable
    @Override
    public JComponent removeTrailingComponent ()
    {
        return setTrailingComponent ( null );
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
        final Dimension ps = super.getPreferredSize ( c );

        // Fix for Swing bug with pointless scrolling when field's default preferred size is already reached
        ps.width += 1;

        return PainterSupport.getPreferredSize ( c, ps );
    }
}