package com.alee.laf.combobox;

import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;
import com.alee.utils.swing.EditabilityListener;
import com.alee.utils.swing.VisibilityListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Basic painter for {@link JComboBox} component.
 * It is used as {@link WebComboBoxUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class ComboBoxPainter<E extends JComboBox, U extends WComboBoxUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IComboBoxPainter<E, U>, EditabilityListener, VisibilityListener
{
    /**
     * Painting variables.
     */
    protected CellRendererPane currentValuePane = null;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Combobox listeners
        ui.addEditabilityListener ( this );
        ui.addPopupVisibilityListener ( this );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Combobox listeners
        ui.removePopupVisibilityListener ( this );
        ui.removeEditabilityListener ( this );

        super.uninstall ( c, ui );
    }

    @Override
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating combobox popup list state
        // This is a workaround to allow box renderer properly inherit enabled state
        if ( CompareUtils.equals ( property, WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            ui.getListBox ().setEnabled ( component.isEnabled () );
        }
    }

    @Override
    public void editabilityChanged ( final boolean editable )
    {
        updateDecorationState ();
    }

    @Override
    public void visibilityChanged ( final boolean visible )
    {
        updateDecorationState ();
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.isEditable () )
        {
            states.add ( DecorationState.editable );
        }
        states.add ( ui.isPopupVisible ( component ) ? DecorationState.expanded : DecorationState.collapsed );
        return states;
    }

    @Override
    public void prepareToPaint ( final CellRendererPane currentValuePane )
    {
        this.currentValuePane = currentValuePane;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Selected non-editable value
        paintCurrentValue ( g2d, ui.getValueBounds () );

        // Cleaning up paint variables
        cleanupAfterPaint ();
    }

    /**
     * Method called when single paint operation is completed.
     */
    protected void cleanupAfterPaint ()
    {
        this.currentValuePane = null;
    }

    /**
     * Paints the currently selected item.
     *
     * @param g2d    graphics context
     * @param bounds bounds
     */
    protected void paintCurrentValue ( final Graphics2D g2d, final Rectangle bounds )
    {
        if ( !component.isEditable () )
        {
            final ListCellRenderer renderer = component.getRenderer ();
            final Component c;

            if ( isFocused () && !component.isPopupVisible () )
            {
                c = renderer.getListCellRendererComponent ( ui.getListBox (), component.getSelectedItem (), -1, true, false );
            }
            else
            {
                c = renderer.getListCellRendererComponent ( ui.getListBox (), component.getSelectedItem (), -1, false, false );
                c.setBackground ( UIManager.getColor ( "ComboBox.background" ) );
            }
            c.setFont ( component.getFont () );

            if ( component.isEnabled () )
            {
                c.setForeground ( component.getForeground () );
                c.setBackground ( component.getBackground () );
            }
            else
            {
                c.setForeground ( UIManager.getColor ( "ComboBox.disabledForeground" ) );
                c.setBackground ( UIManager.getColor ( "ComboBox.disabledBackground" ) );
            }

            boolean shouldValidate = false;
            if ( c instanceof JPanel )
            {
                shouldValidate = true;
            }

            final int x = bounds.x;
            final int y = bounds.y;
            final int w = bounds.width;
            final int h = bounds.height;

            currentValuePane.paintComponent ( g2d, c, component, x, y, w, h, shouldValidate );
        }
    }
}