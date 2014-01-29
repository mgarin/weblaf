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

package com.alee.laf.combobox;

import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.list.WebListUI;
import com.alee.laf.scroll.WebScrollBarUI;
import com.alee.laf.scroll.WebScrollPaneUI;
import com.alee.laf.text.WebTextFieldUI;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.BorderMethods;
import com.alee.utils.swing.WebDefaultCellEditor;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ListUI;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.ScrollPaneUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Custom UI for JComboBox component.
 *
 * @author Mikle Garin
 */

public class WebComboBoxUI extends BasicComboBoxUI implements ShapeProvider, BorderMethods
{
    private ImageIcon expandIcon = WebComboBoxStyle.expandIcon;
    private ImageIcon collapseIcon = WebComboBoxStyle.collapseIcon;
    private int iconSpacing = WebComboBoxStyle.iconSpacing;
    private boolean drawBorder = WebComboBoxStyle.drawBorder;
    private int round = WebComboBoxStyle.round;
    private int shadeWidth = WebComboBoxStyle.shadeWidth;
    private boolean drawFocus = WebComboBoxStyle.drawFocus;
    private boolean mouseWheelScrollingEnabled = WebComboBoxStyle.mouseWheelScrollingEnabled;

    private MouseWheelListener mouseWheelListener = null;
    private WebButton arrow = null;

    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebComboBoxUI ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        final JComboBox comboBox = ( JComboBox ) c;

        // Default settings
        SwingUtils.setOrientation ( comboBox );
        comboBox.setFocusable ( true );
        LookAndFeel.installProperty ( comboBox, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );

        // Updating border
        updateBorder ();

        // Drfault renderer
        if ( !( comboBox.getRenderer () instanceof WebComboBoxCellRenderer ) )
        {
            comboBox.setRenderer ( new WebComboBoxCellRenderer ( comboBox ) );
        }

        // Rollover scrolling listener
        mouseWheelListener = new MouseWheelListener ()
        {
            @Override
            public void mouseWheelMoved ( final MouseWheelEvent e )
            {
                if ( mouseWheelScrollingEnabled && comboBox.isEnabled () && SwingUtils.hasFocusOwner ( comboBox ) )
                {
                    // Changing selection in case index actually changed
                    final int index = comboBox.getSelectedIndex ();
                    final int newIndex = Math.min ( Math.max ( 0, index + e.getWheelRotation () ), comboBox.getModel ().getSize () - 1 );
                    if ( newIndex != index )
                    {
                        comboBox.setSelectedIndex ( newIndex );
                    }
                }
            }
        };
        comboBox.addMouseWheelListener ( mouseWheelListener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        c.removeMouseWheelListener ( mouseWheelListener );
        mouseWheelListener = null;
        arrow = null;

        super.uninstallUI ( c );
    }

    public void setEditorColumns ( final int columns )
    {
        if ( editor instanceof JTextField )
        {
            ( ( JTextField ) editor ).setColumns ( columns );
        }
    }

    @Override
    public void updateBorder ()
    {
        // Preserve old borders
        if ( SwingUtils.isPreserveBorders ( comboBox ) )
        {
            return;
        }

        final Insets m = new Insets ( 0, 0, 0, 0 );
        if ( drawBorder )
        {
            m.top += shadeWidth + 1;
            m.left += shadeWidth + 1;
            m.bottom += shadeWidth + 1;
            m.right += shadeWidth + 1;
        }
        comboBox.setBorder ( LafUtils.createWebBorder ( m ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void installComponents ()
    {
        comboBox.setLayout ( createLayoutManager () );

        arrowButton = createArrowButton ();
        comboBox.add ( arrowButton, "1,0" );
        if ( arrowButton != null )
        {
            configureArrowButton ();
        }

        if ( comboBox.isEditable () )
        {
            addEditor ();
        }

        comboBox.add ( currentValuePane, "0,0" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ComboBoxEditor createEditor ()
    {
        final ComboBoxEditor editor = super.createEditor ();
        final Component e = editor.getEditorComponent ();
        e.addFocusListener ( new FocusAdapter ()
        {
            @Override
            public void focusGained ( final FocusEvent e )
            {
                comboBox.repaint ();
            }

            @Override
            public void focusLost ( final FocusEvent e )
            {
                comboBox.repaint ();
            }
        } );
        if ( e instanceof JComponent )
        {
            ( ( JComponent ) e ).setOpaque ( false );
        }
        if ( e instanceof JTextField )
        {
            final JTextField textField = ( JTextField ) e;
            final WebTextFieldUI textFieldUI = new WebTextFieldUI ();
            textFieldUI.setDrawBorder ( false );
            textField.setUI ( textFieldUI );
            textField.setMargin ( new Insets ( 0, 1, 0, 1 ) );
        }
        return editor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JButton createArrowButton ()
    {
        arrow = new WebButton ();
        arrow.setUndecorated ( true );
        arrow.setDrawFocus ( false );
        arrow.setMoveIconOnPress ( false );
        arrow.setName ( "ComboBox.arrowButton" );
        arrow.setIcon ( expandIcon );
        arrow.setLeftRightSpacing ( iconSpacing );
        return arrow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configureArrowButton ()
    {
        super.configureArrowButton ();
        if ( arrowButton != null )
        {
            arrowButton.setFocusable ( false );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ComboPopup createPopup ()
    {
        return new BasicComboPopup ( comboBox )
        {
            @Override
            protected JScrollPane createScroller ()
            {
                final JScrollPane scroll = super.createScroller ();
                if ( WebLookAndFeel.isInstalled () )
                {
                    scroll.setOpaque ( false );
                    scroll.getViewport ().setOpaque ( false );
                }

                final ScrollPaneUI scrollPaneUI = scroll.getUI ();
                if ( scrollPaneUI instanceof WebScrollPaneUI )
                {
                    final WebScrollPaneUI webScrollPaneUI = ( WebScrollPaneUI ) scrollPaneUI;
                    webScrollPaneUI.setDrawBorder ( false );

                    final ScrollBarUI scrollBarUI = scroll.getVerticalScrollBar ().getUI ();
                    if ( scrollBarUI instanceof WebScrollBarUI )
                    {
                        final WebScrollBarUI webScrollBarUI = ( WebScrollBarUI ) scrollBarUI;
                        webScrollBarUI.setMargin ( WebComboBoxStyle.scrollBarMargin );
                        webScrollBarUI.setThumbRound ( WebComboBoxStyle.scrollBarThumbRound );
                        webScrollBarUI.setButtonsVisible ( WebComboBoxStyle.scrollBarButtonsVisible );
                        webScrollBarUI.setDrawTrack ( WebComboBoxStyle.scrollBarTrackVisible );
                    }
                }

                return scroll;
            }

            @Override
            protected JList createList ()
            {
                final JList list = super.createList ();
                //  list.setOpaque ( false );

                final ListUI listUI = list.getUI ();
                if ( listUI instanceof WebListUI )
                {
                    final WebListUI webListUI = ( WebListUI ) listUI;
                    webListUI.setHighlightRolloverCell ( false );
                    webListUI.setDecorateSelection ( false );
                }

                return list;
            }

            @Override
            protected void configurePopup ()
            {
                super.configurePopup ();

                // Button updater
                addPopupMenuListener ( new PopupMenuListener ()
                {
                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public void popupMenuWillBecomeVisible ( final PopupMenuEvent e )
                    {
                        arrow.setIcon ( collapseIcon );

                        // Fix for combobox repaint on popup opening
                        comboBox.repaint ();
                    }

                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public void popupMenuWillBecomeInvisible ( final PopupMenuEvent e )
                    {
                        arrow.setIcon ( expandIcon );
                    }

                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public void popupMenuCanceled ( final PopupMenuEvent e )
                    {
                        arrow.setIcon ( expandIcon );
                    }
                } );
            }

            @Override
            public void show ()
            {
                comboBox.firePopupMenuWillBecomeVisible ();

                setListSelection ( comboBox.getSelectedIndex () );

                final Point location = getPopupLocation ();
                show ( comboBox, location.x, location.y );
            }

            private void setListSelection ( final int selectedIndex )
            {
                if ( selectedIndex == -1 )
                {
                    list.clearSelection ();
                }
                else
                {
                    list.setSelectedIndex ( selectedIndex );
                    list.ensureIndexIsVisible ( selectedIndex );
                }
            }

            private Point getPopupLocation ()
            {
                final Dimension comboSize = comboBox.getSize ();
                comboSize.setSize ( comboSize.width - 2, getPopupHeightForRowCount ( comboBox.getMaximumRowCount () ) );
                final Rectangle popupBounds = computePopupBounds ( 0, comboBox.getBounds ().height, comboSize.width, comboSize.height );

                final Dimension scrollSize = popupBounds.getSize ();
                scroller.setMaximumSize ( scrollSize );
                scroller.setPreferredSize ( scrollSize );
                scroller.setMinimumSize ( scrollSize );
                list.revalidate ();

                return popupBounds.getLocation ();
            }
        };
    }

    public boolean isComboboxCellEditor ()
    {
        if ( comboBox != null )
        {
            final Object cellEditor = comboBox.getClientProperty ( WebDefaultCellEditor.COMBOBOX_CELL_EDITOR );
            return cellEditor != null && ( Boolean ) cellEditor;
        }
        else
        {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        if ( drawBorder )
        {
            return LafUtils.getWebBorderShape ( comboBox, shadeWidth, round );
        }
        else
        {
            return SwingUtils.size ( comboBox );
        }
    }

    public ImageIcon getExpandIcon ()
    {
        return expandIcon;
    }

    public void setExpandIcon ( final ImageIcon expandIcon )
    {
        this.expandIcon = expandIcon;
        if ( arrow != null && !isPopupVisible ( comboBox ) )
        {
            arrow.setIcon ( expandIcon );
        }
    }

    public ImageIcon getCollapseIcon ()
    {
        return collapseIcon;
    }

    public void setCollapseIcon ( final ImageIcon collapseIcon )
    {
        this.collapseIcon = collapseIcon;
        if ( arrow != null && isPopupVisible ( comboBox ) )
        {
            arrow.setIcon ( collapseIcon );
        }
    }

    public int getIconSpacing ()
    {
        return iconSpacing;
    }

    public void setIconSpacing ( final int iconSpacing )
    {
        this.iconSpacing = iconSpacing;
        if ( arrow != null )
        {
            arrow.setLeftRightSpacing ( iconSpacing );
        }
    }

    public boolean isDrawBorder ()
    {
        return drawBorder;
    }

    public void setDrawBorder ( final boolean drawBorder )
    {
        this.drawBorder = drawBorder;
        updateBorder ();
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( final boolean drawFocus )
    {
        this.drawFocus = drawFocus;
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    public boolean isMouseWheelScrollingEnabled ()
    {
        return mouseWheelScrollingEnabled;
    }

    public void setMouseWheelScrollingEnabled ( final boolean enabled )
    {
        this.mouseWheelScrollingEnabled = enabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        hasFocus = comboBox.hasFocus ();
        final Rectangle r = rectangleForCurrentValue ();

        // Background
        paintCurrentValueBackground ( g, r, hasFocus );

        // Selected uneditable value
        if ( !comboBox.isEditable () )
        {
            paintCurrentValue ( g, r, hasFocus );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintCurrentValueBackground ( final Graphics g, final Rectangle bounds, final boolean hasFocus )
    {
        final Graphics2D g2d = ( Graphics2D ) g;

        if ( drawBorder )
        {
            // Border and background
            comboBox.setBackground ( StyleConstants.selectedBgColor );
            LafUtils.drawWebStyle ( g2d, comboBox,
                    drawFocus && SwingUtils.hasFocusOwner ( comboBox ) ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor,
                    shadeWidth, round, true, !isPopupVisible ( comboBox ) );
        }
        else
        {
            // Simple background
            final boolean pressed = isPopupVisible ( comboBox );
            final Rectangle cb = SwingUtils.size ( comboBox );
            g2d.setPaint ( new GradientPaint ( 0, shadeWidth, pressed ? StyleConstants.topSelectedBgColor : StyleConstants.topBgColor, 0,
                    comboBox.getHeight () - shadeWidth, pressed ? StyleConstants.bottomSelectedBgColor : StyleConstants.bottomBgColor ) );
            g2d.fillRect ( cb.x, cb.y, cb.width, cb.height );
        }

        // Separator line
        if ( comboBox.isEditable () )
        {
            final boolean ltr = comboBox.getComponentOrientation ().isLeftToRight ();
            final Insets insets = comboBox.getInsets ();
            final int lx = ltr ? comboBox.getWidth () - insets.right - arrow.getWidth () - 1 : insets.left + arrow.getWidth ();

            g2d.setPaint ( comboBox.isEnabled () ? StyleConstants.borderColor : StyleConstants.disabledBorderColor );
            g2d.drawLine ( lx, insets.top + 1, lx, comboBox.getHeight () - insets.bottom - 2 );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintCurrentValue ( final Graphics g, final Rectangle bounds, final boolean hasFocus )
    {
        final ListCellRenderer renderer = comboBox.getRenderer ();
        final Component c;

        if ( hasFocus && !isPopupVisible ( comboBox ) )
        {
            c = renderer.getListCellRendererComponent ( listBox, comboBox.getSelectedItem (), -1, true, false );
        }
        else
        {
            c = renderer.getListCellRendererComponent ( listBox, comboBox.getSelectedItem (), -1, false, false );
            c.setBackground ( UIManager.getColor ( "ComboBox.background" ) );
        }
        c.setFont ( comboBox.getFont () );

        if ( comboBox.isEnabled () )
        {
            c.setForeground ( comboBox.getForeground () );
            c.setBackground ( comboBox.getBackground () );
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

        currentValuePane.paintComponent ( g, c, comboBox, x, y, w, h, shouldValidate );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LayoutManager createLayoutManager ()
    {
        return new WebComboBoxLayout ();
    }

    /**
     * Custom layout manager for WebComboBoxUI.
     */
    private class WebComboBoxLayout extends AbstractLayoutManager
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Dimension preferredLayoutSize ( final Container parent )
        {
            return parent.getPreferredSize ();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Dimension minimumLayoutSize ( final Container parent )
        {
            return parent.getMinimumSize ();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void layoutContainer ( final Container parent )
        {
            final JComboBox cb = ( JComboBox ) parent;
            final int width = cb.getWidth ();
            final int height = cb.getHeight ();

            if ( arrowButton != null )
            {
                final Insets insets = getInsets ();
                final int buttonHeight = height - ( insets.top + insets.bottom );
                final int buttonWidth = arrowButton.getPreferredSize ().width;
                if ( cb.getComponentOrientation ().isLeftToRight () )
                {
                    arrowButton.setBounds ( width - ( insets.right + buttonWidth ), insets.top, buttonWidth, buttonHeight );
                }
                else
                {
                    arrowButton.setBounds ( insets.left, insets.top, buttonWidth, buttonHeight );
                }
            }

            if ( editor != null )
            {
                editor.setBounds ( rectangleForCurrentValue () );
            }
        }
    }
}