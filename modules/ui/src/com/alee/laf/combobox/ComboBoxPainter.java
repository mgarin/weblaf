package com.alee.laf.combobox;

import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.swing.PopupMenuAdapter;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

/**
 * @author Alexandr Zernov
 */

public class ComboBoxPainter<E extends JComboBox, U extends WebComboBoxUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IComboBoxPainter<E, U>
{
    /**
     * Listeners.
     */
    protected PopupMenuAdapter menuListener;
    protected MouseWheelListener mouseWheelListener = null;

    /**
     * Painting variables.
     */
    protected CellRendererPane currentValuePane = null;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Menu visibility listener
        menuListener = new PopupMenuAdapter ()
        {
            @Override
            public void popupMenuWillBecomeVisible ( final PopupMenuEvent e )
            {
                SwingUtilities.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        updateDecorationState ();
                    }
                } );
            }

            @Override
            public void popupMenuWillBecomeInvisible ( final PopupMenuEvent e )
            {
                SwingUtilities.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        updateDecorationState ();
                    }
                } );
            }
        };
        component.addPopupMenuListener ( menuListener );

        // Rollover scrolling listener
        mouseWheelListener = new MouseWheelListener ()
        {
            @Override
            public void mouseWheelMoved ( final MouseWheelEvent e )
            {
                if ( ui.isMouseWheelScrollingEnabled () && component.isEnabled () && isFocused () )
                {
                    // Changing selection in case index actually changed
                    final int index = component.getSelectedIndex ();
                    final int newIndex = MathUtils.limit ( 0, index + e.getWheelRotation (), component.getModel ().getSize () - 1 );
                    if ( newIndex != index )
                    {
                        component.setSelectedIndex ( newIndex );
                    }
                }
            }
        };
        component.addMouseWheelListener ( mouseWheelListener );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removeMouseWheelListener ( mouseWheelListener );
        mouseWheelListener = null;
        component.removePopupMenuListener ( menuListener );
        menuListener = null;

        super.uninstall ( c, ui );
    }

    @Override
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChange ( property, oldValue, newValue );

        // Updating combobox popup list state
        // This is a workaround to allow box renderer properly inherit enabled state
        if ( CompareUtils.equals ( property, WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            ui.getListBox ().setEnabled ( component.isEnabled () );
        }
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
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