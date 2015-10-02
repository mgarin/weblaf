package com.alee.managers.style.skin.web;

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.combobox.ComboBoxPainter;
import com.alee.laf.combobox.WebComboBoxStyle;
import com.alee.laf.combobox.WebComboBoxUI;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Alexandr Zernov
 */

public class WebComboBoxPainter<E extends JComboBox, U extends WebComboBoxUI> extends WebDecorationPainter<E, U>
        implements ComboBoxPainter<E, U>
{
    /**
     * Style settings.
     */
    protected int iconSpacing = WebComboBoxStyle.iconSpacing;
    protected Color expandedBgColor = WebComboBoxStyle.expandedBgColor;
    protected Color selectedMenuTopBg = WebComboBoxStyle.selectedMenuTopBg;
    protected Color selectedMenuBottomBg = WebComboBoxStyle.selectedMenuBottomBg;
    protected boolean webColoredBackground = WebComboBoxStyle.webColoredBackground;

    /**
     * Listeners.
     */
    protected MouseWheelListener mouseWheelListener = null;
    protected PropertyChangeListener enabledStateListener = null;

    /**
     * Painting variables.
     */
    protected JButton arrowButton = null;
    protected CellRendererPane currentValuePane = null;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

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
                    final int newIndex = Math.min ( Math.max ( 0, index + e.getWheelRotation () ), component.getModel ().getSize () - 1 );
                    if ( newIndex != index )
                    {
                        component.setSelectedIndex ( newIndex );
                    }
                }
            }
        };
        component.addMouseWheelListener ( mouseWheelListener );


        // Enabled property change listener
        // This is a workaround to allow box renderer properly inherit enabled state
        enabledStateListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                ui.getListBox ().setEnabled ( component.isEnabled () );
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, enabledStateListener );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removePropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, enabledStateListener );

        component.removeMouseWheelListener ( mouseWheelListener );
        mouseWheelListener = null;

        super.uninstall ( c, ui );
    }

    /**
     * Returns icon side spacing.
     *
     * @return icon side spacing
     */
    public int getIconSpacing ()
    {
        return iconSpacing;
    }

    /**
     * Sets icon side spacing.
     *
     * @param spacing icon side spacing
     */
    public void setIconSpacing ( final int spacing )
    {
        this.iconSpacing = spacing;
    }

    /**
     * Returns expanded background color.
     *
     * @return expanded background color
     */
    public Color getExpandedBgColor ()
    {
        return expandedBgColor;
    }

    /**
     * Sets expanded background color.
     *
     * @param color expanded background color
     */
    public void setExpandedBgColor ( final Color color )
    {
        this.expandedBgColor = color;
    }

    /**
     * Returns paint used to fill north popup menu corner when first list element is selected.
     *
     * @return paint used to fill north popup menu corner when first list element is selected
     */
    public Paint getNorthCornerFill ()
    {
        return selectedMenuTopBg;
    }

    /**
     * Returns paint used to fill south popup menu corner when last list element is selected.
     *
     * @return paint used to fill south popup menu corner when last list element is selected
     */
    public Paint getSouthCornerFill ()
    {
        return selectedMenuBottomBg;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Rectangle r = rectangleForCurrentValue ();

        // Painting decoration
        super.paint ( g2d, bounds, c, ui );

        // Selected non-editable value
        if ( !component.isEditable () )
        {
            paintSeparatorLine ( g2d );
            paintCurrentValue ( g2d, r );
        }

        arrowButton = null;
        currentValuePane = null;
    }

    @Override
    public void prepareToPaint ( final JButton arrowButton, final CellRendererPane currentValuePane )
    {
        this.arrowButton = arrowButton;
        this.currentValuePane = currentValuePane;
    }

    @Override
    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final Shape backgroundShape )
    {
        final boolean popupVisible = component.isPopupVisible ();
        if ( webColoredBackground && !popupVisible )
        {
            // Setup cached gradient paint
            final Rectangle bgBounds = backgroundShape.getBounds ();
            g2d.setPaint ( LafUtils.getWebGradientPaint ( 0, bgBounds.y, 0, bgBounds.y + bgBounds.height ) );
        }
        else
        {
            // Setup single color paint
            g2d.setPaint ( !popupVisible ? component.getBackground () : expandedBgColor );
        }
        g2d.fill ( backgroundShape );
    }

    /**
     * Returns the area that is reserved for drawing the currently selected item.
     * This method was overridden to provide additional 1px spacing for separator between combobox editor and arrow button.
     *
     * @return area that is reserved for drawing the currently selected item
     */
    protected Rectangle rectangleForCurrentValue ()
    {
        final int width = component.getWidth ();
        final int height = component.getHeight ();
        final Insets insets = component.getInsets ();

        // Calculating button size
        int buttonSize = height - ( insets.top + insets.bottom );
        if ( arrowButton != null )
        {
            buttonSize = arrowButton.getWidth ();
        }

        // Return editor
        if ( ltr )
        {
            return new Rectangle ( insets.left, insets.top, width - ( insets.left + insets.right + buttonSize ) - 1,
                    height - ( insets.top + insets.bottom ) );
        }
        else
        {
            return new Rectangle ( insets.left + buttonSize + 1, insets.top, width - ( insets.left + insets.right + buttonSize ),
                    height - ( insets.top + insets.bottom ) );
        }
    }

    /**
     * Paints the separator line.
     *
     * @param g2d graphics context
     */
    protected void paintSeparatorLine ( final Graphics2D g2d )
    {
        final Insets insets = component.getInsets ();
        final int lx = ltr ? component.getWidth () - insets.right - arrowButton.getWidth () - 1 : insets.left + arrowButton.getWidth ();

        g2d.setPaint ( component.isEnabled () ? StyleConstants.borderColor : StyleConstants.disabledBorderColor );
        g2d.drawLine ( lx, insets.top + 1, lx, component.getHeight () - insets.bottom - 2 );
    }

    /**
     * Paints the currently selected item.
     *
     * @param g2d    graphics context
     * @param bounds bounds
     */
    protected void paintCurrentValue ( final Graphics2D g2d, final Rectangle bounds )
    {
        final ListCellRenderer renderer = component.getRenderer ();
        final Component c;

        if ( focused && !component.isPopupVisible () )
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

    /**
     * Returns whether combobox or one of its children is focused or not.
     *
     * @return true if combobox or one of its children is focused, false otherwise
     */
    protected boolean isFocused ()
    {
        return SwingUtils.hasFocusOwner ( component );
    }
}