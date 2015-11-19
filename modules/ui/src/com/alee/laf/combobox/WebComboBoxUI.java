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
import com.alee.laf.button.WebButton;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.DataRunnable;
import com.alee.utils.swing.WebDefaultCellEditor;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Custom UI for JComboBox component.
 *
 * @author Mikle Garin
 */

public class WebComboBoxUI extends BasicComboBoxUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Default combobox renderer.
     */
    protected static ListCellRenderer DEFAULT_RENDERER;

    /**
     * Style settings.
     */
    protected ImageIcon expandIcon = WebComboBoxStyle.expandIcon;
    protected ImageIcon collapseIcon = WebComboBoxStyle.collapseIcon;
    protected boolean mouseWheelScrollingEnabled = WebComboBoxStyle.mouseWheelScrollingEnabled;
    protected boolean widerPopupAllowed = WebComboBoxStyle.widerPopupAllowed;
    protected boolean useFirstValueAsPrototype = false;

    /**
     * Component painter.
     */
    protected ComboBoxPainter painter;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebComboBoxUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebComboBoxUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebComboBoxUI ();
    }

    /**
     * Returns default list cell renderer instance.
     *
     * @return default list cell renderer instance
     */
    protected static ListCellRenderer getDefaultListCellRenderer ()
    {
        if ( DEFAULT_RENDERER == null )
        {
            DEFAULT_RENDERER = new WebComboBoxCellRenderer ();
        }
        return DEFAULT_RENDERER;
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( comboBox );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( comboBox );

        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( comboBox );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( comboBox, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( comboBox, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns combobox painter.
     *
     * @return combobox painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets combobox painter.
     * Pass null to remove combobox painter.
     *
     * @param painter new combobox painter
     */
    public void setPainter ( final Painter painter )
    {
        comboBox.hidePopup ();
        PainterSupport.setPainter ( comboBox, new DataRunnable<ComboBoxPainter> ()
        {
            @Override
            public void run ( final ComboBoxPainter newPainter )
            {
                WebComboBoxUI.this.painter = newPainter;
            }
        }, this.painter, painter, ComboBoxPainter.class, AdaptiveComboBoxPainter.class );
    }

    @Override
    protected void installComponents ()
    {
        comboBox.setLayout ( createLayoutManager () );

        arrowButton = createArrowButton ();
        configureArrowButton ();
        comboBox.add ( arrowButton, "1,0" );

        if ( comboBox.isEditable () )
        {
            addEditor ();
        }

        comboBox.add ( currentValuePane, "0,0" );
    }

    @Override
    protected ListCellRenderer createRenderer ()
    {
        return new WebComboBoxCellRenderer.UIResource ();
    }

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
        if ( e instanceof JTextField )
        {
            StyleId.comboboxEditor.at ( comboBox ).set ( ( JTextField ) e );
        }
        return editor;
    }

    @Override
    protected JButton createArrowButton ()
    {
        arrowButton = new WebButton ( StyleId.comboboxArrowButton.at ( comboBox ), expandIcon );
        arrowButton.setName ( "ComboBox.arrowButton" );
        return arrowButton;
    }

    @Override
    protected ComboPopup createPopup ()
    {
        return new BasicComboPopup ( comboBox )
        {
            @Override
            protected JList createList ()
            {
                final JList list = super.createList ();

                // todo Handle inside of the popup painter
                // Custom listener to update popup menu dropdown corner
                //                list.addListSelectionListener ( new ListSelectionListener ()
                //                {
                //                    @Override
                //                    public void valueChanged ( final ListSelectionEvent e )
                //                    {
                //                        // Checking that popup is still displaying on screen
                //                        if ( isShowing () && getUI () instanceof WebPopupMenuUI )
                //                        {
                //                            // Only do additional repaints for dropdown-styled menu
                //                            final WebPopupMenuUI ui = ( WebPopupMenuUI ) getUI ();
                //                            if ( ui.getPopupStyle () == PopupStyle.dropdown )
                //                            {
                //                                // Retrieving menu and combobox position on screen and deciding which side to repaint
                //                                final int py = getLocationOnScreen ().y;
                //                                final int cbi = comboBox.getLocationOnScreen ().y;
                //                                final Insets pi = getInsets ();
                //                                if ( py > cbi )
                //                                {
                //                                    // Repainting top corner area
                //                                    repaint ( 0, 0, getWidth (), pi.top );
                //                                }
                //                                else
                //                                {
                //                                    // Repainting bottom corner area
                //                                    repaint ( 0, getHeight () - pi.bottom, getWidth (), pi.bottom );
                //                                }
                //                            }
                //                        }
                //                    }
                //                } );

                return list;
            }

            @Override
            protected JScrollPane createScroller ()
            {
                final WebScrollPane scroll = new WebScrollPane ( StyleId.comboboxPopupScrollPane.at ( comboBox ), list );
                scroll.setHorizontalScrollBar ( null );

                // Custom list styling
                StyleId.comboboxPopupList.at ( scroll ).set ( list );

                return scroll;
            }

            @Override
            protected void configurePopup ()
            {
                super.configurePopup ();

                // Button updater
                addPopupMenuListener ( new PopupMenuListener ()
                {
                    @Override
                    public void popupMenuWillBecomeVisible ( final PopupMenuEvent e )
                    {
                        arrowButton.setIcon ( collapseIcon );

                        // Fix for combobox repaint when popup is opened
                        comboBox.repaint ();
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible ( final PopupMenuEvent e )
                    {
                        arrowButton.setIcon ( expandIcon );

                        // Fix for combobox repaint when popup is closed
                        comboBox.repaint ();
                    }

                    @Override
                    public void popupMenuCanceled ( final PopupMenuEvent e )
                    {
                        arrowButton.setIcon ( expandIcon );
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
                if ( widerPopupAllowed )
                {
                    final Dimension prefSize = comboBox.getPreferredSize ();
                    if ( prefSize.width > comboSize.width )
                    {
                        comboSize.width = prefSize.width;
                    }
                }
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

    public void setEditorColumns ( final int columns )
    {
        if ( editor instanceof JTextField )
        {
            ( ( JTextField ) editor ).setColumns ( columns );
        }
    }

    public boolean isUseFirstValueAsPrototype ()
    {
        return useFirstValueAsPrototype;
    }

    public void setUseFirstValueAsPrototype ( final boolean use )
    {
        this.useFirstValueAsPrototype = use;
        this.isMinimumSizeDirty = true;
        comboBox.revalidate ();
    }

    public ImageIcon getExpandIcon ()
    {
        return expandIcon;
    }

    public void setExpandIcon ( final ImageIcon expandIcon )
    {
        this.expandIcon = expandIcon;
        if ( arrowButton != null && !isPopupVisible ( comboBox ) )
        {
            arrowButton.setIcon ( expandIcon );
        }
    }

    public ImageIcon getCollapseIcon ()
    {
        return collapseIcon;
    }

    public void setCollapseIcon ( final ImageIcon collapseIcon )
    {
        this.collapseIcon = collapseIcon;
        if ( arrowButton != null && isPopupVisible ( comboBox ) )
        {
            arrowButton.setIcon ( collapseIcon );
        }
    }

    public boolean isMouseWheelScrollingEnabled ()
    {
        return mouseWheelScrollingEnabled;
    }

    public void setMouseWheelScrollingEnabled ( final boolean enabled )
    {
        this.mouseWheelScrollingEnabled = enabled;
    }

    public boolean isWiderPopupAllowed ()
    {
        return widerPopupAllowed;
    }

    public void setWiderPopupAllowed ( final boolean allowed )
    {
        this.widerPopupAllowed = allowed;
    }

    public JList getListBox ()
    {
        return listBox;
    }

    public void pinMinimumSizeDirty ()
    {
        isMinimumSizeDirty = true;
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.prepareToPaint ( arrowButton, currentValuePane );
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
        }
    }

    @Override
    protected LayoutManager createLayoutManager ()
    {
        return new WebComboBoxLayout ();
    }

    @Override
    public Dimension getMinimumSize ( final JComponent c )
    {
        if ( !isMinimumSizeDirty )
        {
            return new Dimension ( cachedMinimumSize );
        }

        final Dimension size = getDisplaySize ();
        final Insets insets = getInsets ();

        // Calculate the width the button
        final int buttonWidth = arrowButton.getPreferredSize ().width;

        // Adjust the size based on the button width
        size.height += insets.top + insets.bottom;
        size.width += insets.left + insets.right + 1 + buttonWidth;

        cachedMinimumSize.setSize ( size.width, size.height );
        isMinimumSizeDirty = false;

        return new Dimension ( size );
    }

    @Override
    protected Dimension getDisplaySize ()
    {
        Dimension result = new Dimension ();

        // Use default renderer
        ListCellRenderer renderer = comboBox.getRenderer ();
        if ( renderer == null )
        {
            renderer = new DefaultListCellRenderer ();
        }

        final Object prototypeValue = comboBox.getPrototypeDisplayValue ();
        if ( prototypeValue != null )
        {
            // Calculates the dimension based on the prototype value
            result = getSizeForComponent ( renderer.getListCellRendererComponent ( listBox, prototypeValue, -1, false, false ) );
        }
        else
        {
            final ComboBoxModel model = comboBox.getModel ();
            final int modelSize = model.getSize ();
            Dimension d;

            if ( modelSize > 0 )
            {
                if ( useFirstValueAsPrototype )
                {
                    // Calculates the maximum height and width based on first element
                    final Object value = model.getElementAt ( 0 );
                    final Component c = renderer.getListCellRendererComponent ( listBox, value, -1, false, false );
                    d = getSizeForComponent ( c );
                    result.width = Math.max ( result.width, d.width );
                    result.height = Math.max ( result.height, d.height );
                }
                else
                {
                    // Calculate the dimension by iterating over all the elements in the combo box list
                    for ( int i = 0; i < modelSize; i++ )
                    {
                        // Calculates the maximum height and width based on the largest element
                        final Object value = model.getElementAt ( i );
                        final Component c = renderer.getListCellRendererComponent ( listBox, value, -1, false, false );
                        d = getSizeForComponent ( c );
                        result.width = Math.max ( result.width, d.width );
                        result.height = Math.max ( result.height, d.height );
                    }
                }
            }
            else
            {
                // Calculates the maximum height and width based on default renderer
                result = getDefaultSize ();
                if ( comboBox.isEditable () )
                {
                    result.width = 100;
                }
            }
        }

        if ( comboBox.isEditable () )
        {
            final Dimension d = editor.getPreferredSize ();
            result.width = Math.max ( result.width, d.width );
            result.height = Math.max ( result.height, d.height );
        }

        return result;
    }

    @Override
    protected Dimension getDefaultSize ()
    {
        // Calculates the height and width using the default text renderer
        return getSizeForComponent ( getDefaultListCellRenderer ().getListCellRendererComponent ( listBox, " ", -1, false, false ) );
    }

    /**
     * Returns renderer component preferred size.
     *
     * @param c renderer component
     * @return renderer component preferred size
     */
    protected Dimension getSizeForComponent ( final Component c )
    {
        currentValuePane.add ( c );
        c.setFont ( comboBox.getFont () );
        final Dimension d = c.getPreferredSize ();
        currentValuePane.remove ( c );
        return d;
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter, true );
    }

    /**
     * Custom layout manager for WebComboBoxUI.
     */
    protected class WebComboBoxLayout extends AbstractLayoutManager
    {

        @Override
        public Dimension preferredLayoutSize ( final Container parent )
        {
            return parent.getPreferredSize ();
        }

        @Override
        public Dimension minimumLayoutSize ( final Container parent )
        {
            return parent.getMinimumSize ();
        }

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