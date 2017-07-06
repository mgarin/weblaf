package com.alee.laf.checkbox;

import com.alee.laf.button.AbstractButtonPainter;
import com.alee.laf.radiobutton.IAbstractStateButtonPainter;
import com.alee.painter.DefaultPainter;
import com.alee.painter.PainterSupport;
import com.alee.painter.SectionPainter;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.util.List;

/**
 * Abstract painter for stateful {@link AbstractButton} implementations.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public abstract class AbstractStateButtonPainter<E extends AbstractButton, U extends ButtonUI, D extends IDecoration<E, D>>
        extends AbstractButtonPainter<E, U, D> implements IAbstractStateButtonPainter<E, U>
{
    /**
     * State icon painter.
     */
    @DefaultPainter ( ButtonStatePainter.class )
    protected IButtonStatePainter checkStatePainter;

    /**
     * Runtime icon bounds.
     */
    protected transient Rectangle iconBounds;

    @Override
    protected void installSectionPainters ()
    {
        super.installSectionPainters ();
        checkStatePainter = PainterSupport.installSectionPainter ( this, checkStatePainter, null, component, ui );
    }

    @Override
    protected void uninstallSectionPainters ()
    {
        checkStatePainter = PainterSupport.uninstallSectionPainter ( checkStatePainter, component, ui );
        super.uninstallSectionPainters ();
    }

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        component.setIcon ( createIcon () );
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        component.setIcon ( null );
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected List<SectionPainter<E, U>> getSectionPainters ()
    {
        return asList ( checkStatePainter );
    }

    /**
     * Returns icon bounds.
     *
     * @return icon bounds
     */
    @Override
    public Rectangle getIconBounds ()
    {
        return iconBounds != null ? new Rectangle ( iconBounds ) : new Rectangle ();
    }

    /**
     * Creates and returns component state icon.
     *
     * @return component state icon
     */
    protected Icon createIcon ()
    {
        return new StateIcon ();
    }

    /**
     * Custom state icon.
     */
    protected class StateIcon implements Icon
    {
        @Override
        public void paintIcon ( final Component c, final Graphics g, final int x, final int y )
        {
            // Updating actual icon rect
            iconBounds = new Rectangle ( new Point ( x, y ), getSize () );

            // Painting check state icon
            if ( checkStatePainter != null )
            {
                final Graphics2D g2d = ( Graphics2D ) g;
                final Object aa = GraphicsUtils.setupAntialias ( g2d );
                PainterSupport.paintSection ( checkStatePainter, g2d, component, ui, iconBounds );
                GraphicsUtils.restoreAntialias ( g2d, aa );
            }
        }

        /**
         * Returns icon size.
         *
         * @return icon size
         */
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