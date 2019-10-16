package com.alee.laf.checkbox;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.laf.button.AbstractButtonPainter;
import com.alee.laf.radiobutton.IAbstractStateButtonPainter;
import com.alee.painter.DefaultPainter;
import com.alee.painter.SectionPainter;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.util.List;

/**
 * Abstract painter for stateful {@link AbstractButton} implementations.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public abstract class AbstractStateButtonPainter<C extends AbstractButton, U extends ButtonUI, D extends IDecoration<C, D>>
        extends AbstractButtonPainter<C, U, D> implements IAbstractStateButtonPainter<C, U>
{
    /**
     * State icon painter.
     */
    @DefaultPainter ( ButtonStatePainter.class )
    protected IButtonStatePainter checkStatePainter;

    /**
     * Runtime icon bounds.
     */
    @Nullable
    protected transient Rectangle iconBounds;

    @Nullable
    @Override
    protected List<SectionPainter<C, U>> getSectionPainters ()
    {
        return asList ( checkStatePainter );
    }

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installStateIcon ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallStateIcon ();
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Restore state icon upon icon removal
        if ( Objects.equals ( property, AbstractButton.ICON_CHANGED_PROPERTY ) )
        {
            // Installing state icon only if button doesn't have one
            if ( newValue == null )
            {
                installStateIcon ();
            }
        }
    }

    /**
     * Installs state {@link Icon} if current button {@link Icon} is {@code null} or {@link UIResource}.
     */
    protected void installStateIcon ()
    {
        if ( component.getIcon () == null || component.getIcon () instanceof UIResource )
        {
            component.setIcon ( createIcon () );
        }
    }

    /**
     * Uninstalls state {@link Icon} if current button {@link Icon} is {@link UIResource}.
     */
    protected void uninstallStateIcon ()
    {
        if ( component.getIcon () instanceof UIResource )
        {
            component.setIcon ( null );
        }
    }

    /**
     * Returns icon bounds.
     *
     * @return icon bounds
     */
    @Nullable
    @Override
    public Rectangle getIconBounds ()
    {
        return iconBounds != null ? new Rectangle ( iconBounds ) : null;
    }

    /**
     * Creates and returns component state icon.
     *
     * @return component state icon
     */
    @NotNull
    protected Icon createIcon ()
    {
        return new StateIcon ();
    }

    /**
     * Custom state icon.
     */
    protected class StateIcon implements Icon, UIResource
    {
        @Override
        public void paintIcon ( @NotNull final Component c, @NotNull final Graphics g, final int x, final int y )
        {
            // Updating actual icon rect
            iconBounds = new Rectangle ( new Point ( x, y ), getSize () );

            // Painting check state icon
            if ( checkStatePainter != null )
            {
                final Graphics2D g2d = ( Graphics2D ) g;
                final Object aa = GraphicsUtils.setupAntialias ( g2d );
                paintSection ( checkStatePainter, g2d, iconBounds );
                GraphicsUtils.restoreAntialias ( g2d, aa );
            }
        }

        /**
         * Returns icon size.
         *
         * @return icon size
         */
        @NotNull
        protected Dimension getSize ()
        {
            return checkStatePainter != null ? checkStatePainter.getPreferredSize () : new Dimension ( 16, 16 );
        }

        @Override
        public int getIconWidth ()
        {
            return getSize ().width;
        }

        @Override
        public int getIconHeight ()
        {
            return getSize ().height;
        }
    }
}