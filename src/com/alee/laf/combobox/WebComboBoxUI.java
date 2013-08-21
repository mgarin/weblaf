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
 * User: mgarin Date: 01.06.11 Time: 14:36
 */

public class WebComboBoxUI extends BasicComboBoxUI implements ShapeProvider
{
    private ImageIcon expandIcon = WebComboBoxStyle.expandIcon;
    private ImageIcon collapseIcon = WebComboBoxStyle.collapseIcon;
    private int iconSpacing = WebComboBoxStyle.iconSpacing;
    private boolean drawBorder = WebComboBoxStyle.drawBorder;
    private int round = WebComboBoxStyle.round;
    private int shadeWidth = WebComboBoxStyle.shadeWidth;
    private boolean drawFocus = WebComboBoxStyle.drawFocus;
    private boolean mouseWheelScrollingEnabled = WebComboBoxStyle.mouseWheelScrollingEnabled;

    private MouseWheelListener mwl = null;
    private WebButton arrow = null;

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebComboBoxUI ();
    }

    @Override
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        final JComboBox comboBox = ( JComboBox ) c;

        // Default settings
        SwingUtils.setOrientation ( comboBox );
        comboBox.setFocusable ( true );
        comboBox.setOpaque ( false );

        // Updating border
        updateBorder ();

        // Drfault renderer
        if ( !( comboBox.getRenderer () instanceof WebComboBoxCellRenderer ) )
        {
            comboBox.setRenderer ( new WebComboBoxCellRenderer ( comboBox ) );
        }

        // Rollover scrolling listener
        mwl = new MouseWheelListener ()
        {
            @Override
            public void mouseWheelMoved ( MouseWheelEvent e )
            {
                if ( mouseWheelScrollingEnabled && comboBox.isEnabled () )
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
        comboBox.addMouseWheelListener ( mwl );
    }

    @Override
    public void uninstallUI ( JComponent c )
    {
        c.removeMouseWheelListener ( mwl );
        arrow = null;

        super.uninstallUI ( c );
    }

    public void setEditorColumns ( int columns )
    {
        if ( editor instanceof JTextField )
        {
            ( ( JTextField ) editor ).setColumns ( columns );
        }
    }

    private void updateBorder ()
    {
        if ( drawBorder )
        {
            comboBox.setBorder ( BorderFactory.createEmptyBorder ( shadeWidth + 1, shadeWidth + 1, shadeWidth + 1, shadeWidth + 1 ) );
        }
        else
        {
            comboBox.setBorder ( null );
        }
    }

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

    @Override
    protected ComboBoxEditor createEditor ()
    {
        final ComboBoxEditor editor = super.createEditor ();
        Component e = editor.getEditorComponent ();
        e.addFocusListener ( new FocusAdapter ()
        {
            @Override
            public void focusGained ( FocusEvent e )
            {
                comboBox.repaint ();
            }

            @Override
            public void focusLost ( FocusEvent e )
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
            JTextField textField = ( JTextField ) e;
            textField.setUI ( new WebTextFieldUI ( textField, false ) );
            textField.setMargin ( new Insets ( 0, 1, 0, 1 ) );
        }
        return editor;
    }

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

    @Override
    public void configureArrowButton ()
    {
        super.configureArrowButton ();
        if ( arrowButton != null )
        {
            arrowButton.setFocusable ( false );
        }
    }

    @Override
    protected ComboPopup createPopup ()
    {
        return new BasicComboPopup ( comboBox )
        {
            @Override
            protected JScrollPane createScroller ()
            {
                JScrollPane scroll = super.createScroller ();
                if ( WebLookAndFeel.isInstalled () )
                {
                    scroll.setOpaque ( false );
                    scroll.getViewport ().setOpaque ( false );
                }

                ScrollPaneUI scrollPaneUI = scroll.getUI ();
                if ( scrollPaneUI instanceof WebScrollPaneUI )
                {
                    WebScrollPaneUI webScrollPaneUI = ( WebScrollPaneUI ) scrollPaneUI;
                    webScrollPaneUI.setDrawBorder ( false );

                    ScrollBarUI scrollBarUI = scroll.getVerticalScrollBar ().getUI ();
                    if ( scrollBarUI instanceof WebScrollBarUI )
                    {
                        WebScrollBarUI webScrollBarUI = ( WebScrollBarUI ) scrollBarUI;
                        webScrollBarUI.setScrollBorder ( webScrollPaneUI.getDarkBorder () );
                    }
                }

                return scroll;
            }

            @Override
            protected JList createList ()
            {
                JList list = super.createList ();
                //  list.setOpaque ( false );

                ListUI listUI = list.getUI ();
                if ( listUI instanceof WebListUI )
                {
                    ( ( WebListUI ) listUI ).setHighlightRolloverCell ( false );
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
                    @Override
                    public void popupMenuWillBecomeVisible ( PopupMenuEvent e )
                    {
                        arrow.setIcon ( collapseIcon );

                        // Fix for combobox repaint on popup opening
                        comboBox.repaint ();
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible ( PopupMenuEvent e )
                    {
                        arrow.setIcon ( expandIcon );
                    }

                    @Override
                    public void popupMenuCanceled ( PopupMenuEvent e )
                    {
                        arrow.setIcon ( expandIcon );
                    }
                } );
            }

            @Override
            public void show ()
            {
                // informing listeners
                comboBox.firePopupMenuWillBecomeVisible ();

                // Updating list selection
                setListSelection ( comboBox.getSelectedIndex () );

                // Updating popup size
                boolean cellEditor = isComboboxCellEditor ();
                setupPopupSize ( cellEditor );

                // Displaying popup
                ComponentOrientation orientation = comboBox.getComponentOrientation ();
                int sideShear = ( drawBorder ? shadeWidth : ( cellEditor ? -1 : 0 ) );
                int topShear = ( drawBorder ? shadeWidth : 0 ) - ( cellEditor ? 0 : 1 );
                show ( comboBox, orientation.isLeftToRight () ? sideShear : comboBox.getWidth () - getWidth () - sideShear,
                        comboBox.getHeight () - topShear );
            }

            private void setupPopupSize ( boolean cellEditor )
            {
                Dimension popupSize = comboBox.getSize ();
                if ( drawBorder )
                {
                    popupSize.width -= shadeWidth * 2;
                }
                if ( cellEditor )
                {
                    popupSize.width += 2;
                }

                Insets insets = getInsets ();
                popupSize.setSize ( popupSize.width - ( insets.right + insets.left ),
                        getPopupHeightForRowCount ( comboBox.getMaximumRowCount () ) );

                Rectangle popupBounds = computePopupBounds ( 0, comboBox.getBounds ().height, popupSize.width, popupSize.height );
                Dimension scrollSize = popupBounds.getSize ();

                scroller.setMaximumSize ( scrollSize );
                scroller.setPreferredSize ( scrollSize );
                scroller.setMinimumSize ( scrollSize );

                list.revalidate ();
            }

            private void setListSelection ( int selectedIndex )
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
        };
    }

    public boolean isComboboxCellEditor ()
    {
        if ( comboBox != null )
        {
            Object cellEditor = comboBox.getClientProperty ( WebDefaultCellEditor.COMBOBOX_CELL_EDITOR );
            return cellEditor != null && ( Boolean ) cellEditor;
        }
        else
        {
            return false;
        }
    }

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

    public void setExpandIcon ( ImageIcon expandIcon )
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

    public void setCollapseIcon ( ImageIcon collapseIcon )
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

    public void setIconSpacing ( int iconSpacing )
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

    public void setDrawBorder ( boolean drawBorder )
    {
        this.drawBorder = drawBorder;
        updateBorder ();
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( boolean drawFocus )
    {
        this.drawFocus = drawFocus;
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( int round )
    {
        this.round = round;
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    public boolean isMouseWheelScrollingEnabled ()
    {
        return mouseWheelScrollingEnabled;
    }

    public void setMouseWheelScrollingEnabled ( boolean enabled )
    {
        this.mouseWheelScrollingEnabled = enabled;
    }

    @Override
    public void paint ( Graphics g, JComponent c )
    {
        hasFocus = comboBox.hasFocus ();
        Rectangle r = rectangleForCurrentValue ();

        // Background
        paintCurrentValueBackground ( g, r, hasFocus );

        // Selected uneditable value
        if ( !comboBox.isEditable () )
        {
            paintCurrentValue ( g, r, hasFocus );
        }
    }

    @Override
    public void paintCurrentValueBackground ( Graphics g, Rectangle bounds, boolean hasFocus )
    {
        Graphics2D g2d = ( Graphics2D ) g;

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
            boolean pressed = isPopupVisible ( comboBox );
            Rectangle cb = SwingUtils.size ( comboBox );
            g2d.setPaint ( new GradientPaint ( 0, shadeWidth, pressed ? StyleConstants.topSelectedBgColor : StyleConstants.topBgColor, 0,
                    comboBox.getHeight () - shadeWidth, pressed ? StyleConstants.bottomSelectedBgColor : StyleConstants.bottomBgColor ) );
            g2d.fillRect ( cb.x, cb.y, cb.width, cb.height );
        }

        // Separator line
        if ( comboBox.isEditable () )
        {
            boolean ltr = comboBox.getComponentOrientation ().isLeftToRight ();
            Insets insets = comboBox.getInsets ();
            int lx = ltr ? comboBox.getWidth () - insets.right - arrow.getWidth () - 1 : insets.left + arrow.getWidth ();

            g2d.setPaint ( comboBox.isEnabled () ? StyleConstants.borderColor : StyleConstants.disabledBorderColor );
            g2d.drawLine ( lx, insets.top + 1, lx, comboBox.getHeight () - insets.bottom - 2 );
        }
    }

    @Override
    public void paintCurrentValue ( Graphics g, Rectangle bounds, boolean hasFocus )
    {
        ListCellRenderer renderer = comboBox.getRenderer ();
        Component c;

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

        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;

        currentValuePane.paintComponent ( g, c, comboBox, x, y, w, h, shouldValidate );
    }

    @Override
    protected LayoutManager createLayoutManager ()
    {
        return new WebComboBoxLayout ();
    }

    private class WebComboBoxLayout implements LayoutManager
    {
        @Override
        public void addLayoutComponent ( String name, Component comp )
        {
            //
        }

        @Override
        public void removeLayoutComponent ( Component comp )
        {
            //
        }

        @Override
        public Dimension preferredLayoutSize ( Container parent )
        {
            return parent.getPreferredSize ();
        }

        @Override
        public Dimension minimumLayoutSize ( Container parent )
        {
            return parent.getMinimumSize ();
        }

        @Override
        public void layoutContainer ( Container parent )
        {
            JComboBox cb = ( JComboBox ) parent;
            int width = cb.getWidth ();
            int height = cb.getHeight ();

            if ( arrowButton != null )
            {
                Insets insets = getInsets ();
                int buttonHeight = height - ( insets.top + insets.bottom );
                int buttonWidth = arrowButton.getPreferredSize ().width;
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