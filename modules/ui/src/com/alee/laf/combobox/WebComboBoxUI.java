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
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.ImageUtils;
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
     * Expand icon.
     */
    public static ImageIcon EXPAND_ICON = new ImageIcon ( WebComboBoxUI.class.getResource ( "icons/arrow.png" ) );

    /**
     * Collapse icon.
     */
    public static ImageIcon COLLAPSE_ICON = ImageUtils.rotateImage180 ( EXPAND_ICON );

    /**
     * Default combobox renderer.
     */
    protected static ListCellRenderer DEFAULT_RENDERER;

    /**
     * Style settings.
     */
    protected ImageIcon expandIcon;
    protected ImageIcon collapseIcon;
    protected Boolean mouseWheelScrollingEnabled;
    protected Boolean widerPopupAllowed;

    /**
     * Component painter.
     */
    @DefaultPainter ( ComboBoxPainter.class )
    protected IComboBoxPainter painter;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;
    protected Dimension cachedDisplaySize = new Dimension ( 0, 0 );
    protected JSeparator separator;

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
    protected void installComponents ()
    {
        // Removing all previous components
        comboBox.removeAll ();

        // Updating layout
        comboBox.setLayout ( createLayoutManager () );

        // Arrow button
        addArrowButton ();

        // Value and button separator
        addSeparator ();

        // Value editor
        if ( comboBox.isEditable () )
        {
            addEditor ();
        }

        // Value renderer pane
        comboBox.add ( currentValuePane );
    }

    @Override
    protected void uninstallComponents ()
    {
        // Removing all components
        removeArrowButton ();
        removeSeparator ();
        removeEditor ();
        comboBox.removeAll ();
    }

    /**
     * Adding arrow button onto combobox.
     */
    protected void addArrowButton ()
    {
        arrowButton = createArrowButton ();
        configureArrowButton ();
        comboBox.add ( arrowButton );
    }

    /**
     * Removing arrow button from combobox.
     */
    protected void removeArrowButton ()
    {
        if ( arrowButton != null )
        {
            unconfigureArrowButton ();
            comboBox.remove ( arrowButton );
            arrowButton = null;
        }
    }

    /**
     * Adding separator onto combobox.
     */
    protected void addSeparator ()
    {
        separator = new WebSeparator ( StyleId.comboboxSeparator.at ( comboBox ) );
        comboBox.add ( separator );
    }

    /**
     * Removing separator from combobox.
     */
    protected void removeSeparator ()
    {
        if ( separator != null )
        {
            comboBox.remove ( separator );
            separator = null;
        }
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
        PainterSupport.setPainter ( comboBox, new DataRunnable<IComboBoxPainter> ()
        {
            @Override
            public void run ( final IComboBoxPainter newPainter )
            {
                WebComboBoxUI.this.painter = newPainter;
            }
        }, this.painter, painter, IComboBoxPainter.class, AdaptiveComboBoxPainter.class );
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
        arrowButton = new WebButton ( StyleId.comboboxArrowButton.at ( comboBox ), getExpandIcon () )
        {
            @Override
            public void setFocusable ( final boolean focusable )
            {
                // Workaround to completely disable focusability of this button
                super.setFocusable ( false );
            }
        };
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
                setOpaque ( false );
                setDoubleBuffered ( true );
                setFocusable ( false );
                setLayout ( new BoxLayout ( this, BoxLayout.Y_AXIS ) );
                add ( scroller );

                // Button updater
                addPopupMenuListener ( new PopupMenuListener ()
                {
                    @Override
                    public void popupMenuWillBecomeVisible ( final PopupMenuEvent e )
                    {
                        arrowButton.setIcon ( getCollapseIcon () );

                        // Fix for combobox repaint when popup is opened
                        comboBox.repaint ();
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible ( final PopupMenuEvent e )
                    {
                        arrowButton.setIcon ( getExpandIcon () );

                        // Fix for combobox repaint when popup is closed
                        comboBox.repaint ();
                    }

                    @Override
                    public void popupMenuCanceled ( final PopupMenuEvent e )
                    {
                        arrowButton.setIcon ( getExpandIcon () );
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
                if ( isWiderPopupAllowed () )
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

    public ImageIcon getExpandIcon ()
    {
        return expandIcon != null ? expandIcon : EXPAND_ICON;
    }

    public void setExpandIcon ( final ImageIcon expandIcon )
    {
        this.expandIcon = expandIcon;
        if ( arrowButton != null && !isPopupVisible ( comboBox ) )
        {
            arrowButton.setIcon ( getExpandIcon () );
        }
    }

    public ImageIcon getCollapseIcon ()
    {
        return collapseIcon != null ? collapseIcon : COLLAPSE_ICON;
    }

    public void setCollapseIcon ( final ImageIcon collapseIcon )
    {
        this.collapseIcon = collapseIcon;
        if ( arrowButton != null && isPopupVisible ( comboBox ) )
        {
            arrowButton.setIcon ( getCollapseIcon () );
        }
    }

    public boolean isMouseWheelScrollingEnabled ()
    {
        return mouseWheelScrollingEnabled == null || mouseWheelScrollingEnabled;
    }

    public void setMouseWheelScrollingEnabled ( final boolean enabled )
    {
        this.mouseWheelScrollingEnabled = enabled;
    }

    public boolean isWiderPopupAllowed ()
    {
        return widerPopupAllowed != null && widerPopupAllowed;
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
            painter.prepareToPaint ( currentValuePane );
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
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

        // Insets sizes
        size.height += insets.top + insets.bottom;
        size.width += insets.left + insets.right;

        // Arrow button width
        if ( arrowButton != null )
        {
            size.width += arrowButton.getPreferredSize ().width;

            // Separator width
            if ( separator != null )
            {
                size.width += separator.getPreferredSize ().width;
            }
        }

        // Saving resulting size
        cachedMinimumSize.setSize ( size.width, size.height );
        isMinimumSizeDirty = false;

        return new Dimension ( size );
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
            // Arrow button
            if ( arrowButton != null )
            {
                arrowButton.setBounds ( getArrowButtonBounds () );

                // Separator
                if ( separator != null )
                {
                    separator.setBounds ( getSeparatorBounds () );
                }
            }

            // Value editor
            if ( editor != null )
            {
                editor.setBounds ( getValueBounds () );
            }
        }
    }

    @Override
    protected Rectangle rectangleForCurrentValue ()
    {
        return getValueBounds ();
    }

    /**
     * Returns the area that is reserved for drawing currently selected item.
     *
     * @return area that is reserved for drawing currently selected item
     */
    public Rectangle getValueBounds ()
    {
        final int width = comboBox.getWidth ();
        final int height = comboBox.getHeight ();
        final Insets i = comboBox.getInsets ();
        final boolean ltr = comboBox.getComponentOrientation ().isLeftToRight ();

        int side = 0;
        if ( arrowButton != null )
        {
            side += arrowButton.getPreferredSize ().width;
            if ( separator != null )
            {
                side += separator.getPreferredSize ().width;
            }
        }

        return new Rectangle ( ltr ? i.left : i.left + side, i.top, width - i.left - i.right - side, height - i.top - i.bottom );
    }

    /**
     * Returns the area that is reserved for drawing separator between currently selected item and arrow button.
     *
     * @return area that is reserved for drawing between currently selected item and arrow button
     */
    public Rectangle getSeparatorBounds ()
    {
        final int width = comboBox.getWidth ();
        final int height = comboBox.getHeight ();
        final Insets i = comboBox.getInsets ();
        final boolean ltr = comboBox.getComponentOrientation ().isLeftToRight ();
        final int sep = separator.getPreferredSize ().width;

        int button = 0;
        if ( arrowButton != null )
        {
            button += arrowButton.getPreferredSize ().width;
        }

        return new Rectangle ( ltr ? width - i.right - button - sep : i.left + button, i.top, sep, height - i.top - i.bottom );
    }

    /**
     * Returns the area that is reserved for drawing arrow button.
     *
     * @return area that is reserved for drawing arrow button
     */
    public Rectangle getArrowButtonBounds ()
    {
        final int width = comboBox.getWidth ();
        final int height = comboBox.getHeight ();
        final Insets i = comboBox.getInsets ();
        final boolean ltr = comboBox.getComponentOrientation ().isLeftToRight ();
        final int button = arrowButton.getPreferredSize ().width;
        return new Rectangle ( ltr ? width - i.right - button : i.left, i.top, button, height - i.top - i.bottom );
    }
}