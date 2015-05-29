package com.alee.managers.style.skin.web;

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.combobox.ComboBoxPainter;
import com.alee.laf.combobox.WebComboBoxCellRenderer;
import com.alee.laf.combobox.WebComboBoxStyle;
import com.alee.laf.combobox.WebComboBoxUI;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.RendererListener;

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
    // Style
    protected int iconSpacing = WebComboBoxStyle.iconSpacing;
    protected Color expandedBgColor = WebComboBoxStyle.expandedBgColor;
    protected Color selectedMenuTopBg = WebComboBoxStyle.selectedMenuTopBg;
    protected Color selectedMenuBottomBg = WebComboBoxStyle.selectedMenuBottomBg;
    protected boolean webColoredBackground = WebComboBoxStyle.webColoredBackground;
    protected boolean drawFocus = WebComboBoxStyle.drawFocus;
    protected boolean drawBorder = WebComboBoxStyle.drawBorder;

    /**
     * Listeners.
     */
    protected MouseWheelListener mouseWheelListener = null;
    protected RendererListener rendererListener = null;
    protected PropertyChangeListener rendererChangeListener = null;
    protected PropertyChangeListener enabledStateListener = null;

    /**
     * Painting variables.
     */
    protected boolean hasFocus = false;
    protected JButton arrowButton = null;
    protected CellRendererPane currentValuePane = null;

    /**
     * {@inheritDoc}
     */
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

        // Renderer change listener
        // Used to provide feedback from the renderer
        rendererListener = new RendererListener ()
        {
            @Override
            public void repaint ()
            {
                component.repaint ();
                ui.getListBox ().repaint ();
            }

            @Override
            public void revalidate ()
            {
                ui.pinMinimumSizeDirty ();
                component.revalidate ();
                ui.getListBox ().revalidate ();
            }
        };
        installRendererListener ( component.getRenderer () );
        rendererChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent e )
            {
                uninstallRendererListener ( e.getOldValue () );
                installRendererListener ( e.getNewValue () );
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.RENDERER_PROPERTY, rendererChangeListener );

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removePropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, enabledStateListener );

        component.removePropertyChangeListener ( WebLookAndFeel.RENDERER_PROPERTY, rendererChangeListener );
        uninstallRendererListener ( component.getRenderer () );
        rendererListener = null;

        component.removeMouseWheelListener ( mouseWheelListener );
        mouseWheelListener = null;

        super.uninstall ( c, ui );
    }

    /**
     * Installs RendererListener into specified renderer if possible.
     *
     * @param renderer RendererListener to install
     */
    protected void installRendererListener ( final Object renderer )
    {
        if ( renderer != null && renderer instanceof WebComboBoxCellRenderer )
        {
            ( ( WebComboBoxCellRenderer ) renderer ).addRendererListener ( rendererListener );
        }
    }

    /**
     * Uninstalls RendererListener from specified renderer if possible.
     *
     * @param renderer RendererListener to uninstall
     */
    protected void uninstallRendererListener ( final Object renderer )
    {
        if ( renderer != null && renderer instanceof WebComboBoxCellRenderer )
        {
            ( ( WebComboBoxCellRenderer ) renderer ).removeRendererListener ( rendererListener );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        hasFocus = component.hasFocus ();
        final Rectangle r = rectangleForCurrentValue ();

        // Background
        paintCurrentValueBackground ( g2d, r, hasFocus );

        // Selected non-editable value
        if ( !component.isEditable () )
        {
            paintCurrentValue ( g2d, r, hasFocus );
        }

        arrowButton = null;
        currentValuePane = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareToPaint ( final JButton arrowButton, final CellRendererPane currentValuePane )
    {
        this.arrowButton = arrowButton;
        this.currentValuePane = currentValuePane;
    }

    /**
     * Returns the area that is reserved for drawing the currently selected item.
     * This method was overridden to provide additional 1px spacing for separator between combobox editor and arrow button.
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
            // todo provide
            buttonSize = arrowButton.getWidth ();
        }

        // Return editor
        if ( component.getComponentOrientation ().isLeftToRight () )
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
     * {@inheritDoc}
     */
    public void paintCurrentValueBackground ( final Graphics2D g2d, final Rectangle bounds, final boolean hasFocus )
    {
        if ( drawBorder )
        {
            // Border and background
            final Color shadeColor = drawFocus && isFocused () ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor;
            final boolean popupVisible = component.isPopupVisible ();
            final Color backgroundColor = !popupVisible ? component.getBackground () : expandedBgColor;
            LafUtils.drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, true, webColoredBackground && !popupVisible,
                    StyleConstants.darkBorderColor, StyleConstants.disabledBorderColor, backgroundColor, 1f );
        }
        else
        {
            // Simple background
            final boolean pressed = component.isPopupVisible ();
            final Rectangle cb = SwingUtils.size ( component );
            g2d.setPaint ( new GradientPaint ( 0, shadeWidth, pressed ? StyleConstants.topSelectedBgColor : StyleConstants.topBgColor, 0,
                    component.getHeight () - shadeWidth, pressed ? StyleConstants.bottomSelectedBgColor : StyleConstants.bottomBgColor ) );
            g2d.fillRect ( cb.x, cb.y, cb.width, cb.height );
        }

        // Separator line
        if ( component.isEditable () )
        {
            final boolean ltr = component.getComponentOrientation ().isLeftToRight ();
            final Insets insets = component.getInsets ();
            final int lx = ltr ? component.getWidth () - insets.right - arrowButton.getWidth () - 1 : insets.left + arrowButton.getWidth ();

            g2d.setPaint ( component.isEnabled () ? StyleConstants.borderColor : StyleConstants.disabledBorderColor );
            g2d.drawLine ( lx, insets.top + 1, lx, component.getHeight () - insets.bottom - 2 );
        }
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

    /**
     * {@inheritDoc}
     */
    public void paintCurrentValue ( final Graphics g, final Rectangle bounds, final boolean hasFocus )
    {
        final ListCellRenderer renderer = component.getRenderer ();
        final Component c;

        if ( hasFocus && !component.isPopupVisible () )
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

        currentValuePane.paintComponent ( g, c, component, x, y, w, h, shouldValidate );
    }
}
