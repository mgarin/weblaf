package com.alee.laf.text;

import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Abstract painter for {@link JTextComponent}-based text field implementations.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public abstract class AbstractTextFieldPainter<C extends JTextComponent, U extends BasicTextUI, D extends IDecoration<C, D>>
        extends AbstractTextEditorPainter<C, U, D> implements IAbstractTextFieldPainter<C, U>, SwingConstants
{
    /**
     * Leading and trailing components resize listener.
     */
    protected transient ComponentAdapter componentResizeListener;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installCustomSettings ();
        installLeadingTrailingResizeListener ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallLeadingTrailingResizeListener ();
        uninstallCustomLayout ();
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating leading and trailing components state
        if ( Objects.equals ( property, WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            final boolean enabled = component.isEnabled ();
            SwingUtils.setEnabledRecursively ( getLeadingComponent (), enabled );
            SwingUtils.setEnabledRecursively ( getTrailingComponent (), enabled );
        }

        // Updating listeners and borders on leading or trailing component change
        if ( Objects.equals ( property, WebLookAndFeel.LEADING_COMPONENT_PROPERTY, WebLookAndFeel.TRAILING_COMPONENT_PROPERTY ) )
        {
            uninstallResizeListener ( ( Component ) oldValue );
            installResizeListener ( ( Component ) newValue );
            updateBorder ();
        }
    }

    /**
     * Installs custom field settings.
     */
    protected void installCustomSettings ()
    {
        // Configuring enabled state handling
        SwingUtils.setHandlesEnableStateMark ( component );

        // Installing custom field layout that can handle leading and trailing components
        component.setLayout ( new TextFieldLayout ( this ) );
    }

    /**
     * Uninstalls custom field settings.
     */
    protected void uninstallCustomLayout ()
    {
        // Uninstalling custom layout from the field
        component.setLayout ( null );

        // Restoring enabled state handling
        SwingUtils.removeHandlesEnableStateMark ( component );
    }

    /**
     * Installs resize listener into leading and trailing components.
     */
    protected void installLeadingTrailingResizeListener ()
    {
        componentResizeListener = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                updateBorder ();
            }
        };
        installResizeListener ( getLeadingComponent () );
        installResizeListener ( getTrailingComponent () );
    }

    /**
     * Uninstalls resize listener from leading and trailing components.
     */
    protected void uninstallLeadingTrailingResizeListener ()
    {
        uninstallResizeListener ( getTrailingComponent () );
        uninstallResizeListener ( getLeadingComponent () );
        componentResizeListener = null;
    }

    /**
     * Installs resize listener into the specified component.
     *
     * @param component component to install listener into
     */
    protected void installResizeListener ( final Component component )
    {
        if ( component != null )
        {
            component.addComponentListener ( componentResizeListener );
        }
    }

    /**
     * Uninstalls resize listener from the specified component.
     *
     * @param component component to uninstall listener from
     */
    protected void uninstallResizeListener ( final Component component )
    {
        if ( component != null )
        {
            component.removeComponentListener ( componentResizeListener );
        }
    }

    @Override
    protected Insets getBorder ()
    {
        final Insets border = super.getBorder ();
        final Component lc = getLeadingComponent ();
        final Component tc = getTrailingComponent ();
        final Insets result;
        if ( lc != null || tc != null )
        {
            final int left = lc != null ? lc.getPreferredSize ().width : 0;
            final int right = tc != null ? tc.getPreferredSize ().width : 0;
            if ( border != null )
            {
                result = new Insets ( border.top, border.left + left, border.bottom, border.right + right );
            }
            else
            {
                result = new Insets ( 0, left, 0, right );
            }
        }
        else
        {
            result = border;
        }
        return result;
    }
}