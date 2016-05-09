package com.alee.laf.checkbox;

import com.alee.laf.button.AbstractButtonPainter;
import com.alee.laf.radiobutton.IAbstractStateButtonPainter;
import com.alee.painter.DefaultPainter;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.PainterSupport;
import com.alee.painter.SectionPainter;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.util.List;

/**
 * @author Alexandr Zernov
 */

public abstract class AbstractStateButtonPainter<E extends AbstractButton, U extends BasicButtonUI, D extends IDecoration<E, D>>
        extends AbstractButtonPainter<E, U, D> implements IAbstractStateButtonPainter<E, U>
{
    /**
     * State icon painter.
     */
    @DefaultPainter ( ButtonStatePainter.class )
    protected IButtonStatePainter checkStatePainter;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Properly installing section painters
        this.checkStatePainter = PainterSupport.installSectionPainter ( this, checkStatePainter, null, c, ui );

        // State icon that uses {@code checkStatePainter}
        component.setIcon ( createIcon () );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing custom icon
        component.setIcon ( null );

        // Properly uninstalling section painters
        this.checkStatePainter = PainterSupport.uninstallSectionPainter ( checkStatePainter, c, ui );

        super.uninstall ( c, ui );
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
    public Rectangle getIconRect ()
    {
        return iconRect != null ? new Rectangle ( iconRect ) : new Rectangle ();
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
            iconRect = new Rectangle ( new Point ( x, y ), getSize () );

            // Painting check state icon
            if ( checkStatePainter != null )
            {
                final Graphics2D g2d = ( Graphics2D ) g;
                final Object aa = GraphicsUtils.setupAntialias ( g2d );
                checkStatePainter.paint ( g2d, iconRect, component, ui );
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