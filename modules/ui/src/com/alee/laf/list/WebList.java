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

package com.alee.laf.list;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.list.editor.DefaultListCellEditor;
import com.alee.laf.list.editor.ListCellEditor;
import com.alee.laf.list.editor.ListEditListener;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.managers.style.Skin;
import com.alee.managers.style.Skinnable;
import com.alee.managers.style.StyleListener;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.*;
import com.alee.utils.swing.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.*;
import java.util.List;

/**
 * This JList extension class provides a direct access to WebListUI methods.
 * There is also a set of additional methods to simplify some operations with list.
 * <p>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 */

public class WebList extends JList
        implements Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, EventMethods, FontMethods<WebList>,
        SizeMethods<WebList>
{
    /**
     * todo 1. Generics usage when migrated to JDK8+
     */

    /**
     * Whether or not this list is editable.
     */
    protected boolean editable = false;

    /**
     * List cell editor.
     */
    protected ListCellEditor listCellEditor = null;

    /**
     * Currently edited cell index or -1 if none edited at the moment.
     */
    protected int editedCell = -1;

    /**
     * Whether or not list allows an empty selection.
     * Note that even if this option is set to {@code false} it doesn't provide any initial selection.
     */
    protected boolean emptySelectionAllowed = true;

    /**
     * Custom WebLaF tooltip provider.
     */
    protected ToolTipProvider<? extends WebList> toolTipProvider = null;

    /**
     * Constructs empty list.
     */
    public WebList ()
    {
        super ();
    }

    /**
     * Constructs list with the specified data.
     *
     * @param listData list data
     */
    public WebList ( final List listData )
    {
        super ( listData.toArray () );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param listData list data
     */
    public WebList ( final Vector listData )
    {
        super ( listData );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param listData list data
     */
    public WebList ( final Object[] listData )
    {
        super ( listData );
    }

    /**
     * Constructs list with the specified list model.
     *
     * @param dataModel list model
     */
    public WebList ( final ListModel dataModel )
    {
        super ( dataModel );
    }

    /**
     * Constructs empty list.
     *
     * @param id style ID
     */
    public WebList ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param id       style ID
     * @param listData list data
     */
    public WebList ( final StyleId id, final List listData )
    {
        super ( listData.toArray () );
        setStyleId ( id );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param id       style ID
     * @param listData list data
     */
    public WebList ( final StyleId id, final Vector listData )
    {
        super ( listData );
        setStyleId ( id );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param id       style ID
     * @param listData list data
     */
    public WebList ( final StyleId id, final Object[] listData )
    {
        super ( listData );
        setStyleId ( id );
    }

    /**
     * Constructs list with the specified list model.
     *
     * @param id        style ID
     * @param dataModel list model
     */
    public WebList ( final StyleId id, final ListModel dataModel )
    {
        super ( dataModel );
        setStyleId ( id );
    }

    /**
     * Sets whether multiply values selection allowed or not.
     * This call simply changes selection mode according to provided value.
     *
     * @param allowed whether multiply values selection allowed or not
     */
    public void setMultiplySelectionAllowed ( final boolean allowed )
    {
        setSelectionMode ( allowed ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION );
    }

    /**
     * Returns specific web list cell renderer or null if a custom non-web renderer is installed.
     *
     * @return specific web list cell renderer or null if a custom non-web renderer is installed
     */
    public WebListCellRenderer getWebListCellRenderer ()
    {
        final ListCellRenderer renderer = getCellRenderer ();
        return renderer instanceof WebListCellRenderer ? ( WebListCellRenderer ) renderer : null;
    }

    /**
     * Returns specific web list model or null if another type of model is used.
     *
     * @return specific web list model or null if another type of model is used
     */
    public WebListModel getWebModel ()
    {
        final ListModel model = getModel ();
        return model instanceof WebListModel ? ( WebListModel ) model : null;
    }

    /**
     * Sets selected value and scrolls view to its cell.
     *
     * @param element element to select
     */
    public void setSelectedValue ( final Object element )
    {
        setSelectedValue ( element, true );
    }

    /**
     * Selects all specified values.
     * Values which are not in the list are simply ignored.
     * This method might be slow in case model cannot provide all separate values quickly.
     *
     * @param values values to select
     */
    public void setSelectedValues ( final Object[] values )
    {
        setSelectedValues ( CollectionUtils.toList ( values ) );
    }

    /**
     * Selects all specified values.
     * Values which are not in the list are simply ignored.
     * This method might be slow in case model cannot provide all separate values quickly.
     *
     * @param values values to select
     */
    public void setSelectedValues ( final Collection values )
    {
        setSelectedValues ( new ArrayList ( values ) );
    }

    /**
     * Selects all specified values.
     * Values which are not in the list are simply ignored.
     * This method might be slow in case model cannot provide all separate values quickly.
     *
     * @param values values to select
     */
    public void setSelectedValues ( final List values )
    {
        final List<Integer> indices = new ArrayList<Integer> ();
        final ListModel model = getModel ();
        for ( int i = 0; i < model.getSize (); i++ )
        {
            if ( values.contains ( model.getElementAt ( i ) ) )
            {
                indices.add ( i );
            }
        }
        setSelectedIndices ( CollectionUtils.toArray ( indices ) );
    }

    /**
     * Returns whether this list is editable or not.
     *
     * @return true if this list is editable, false otherwise
     */
    public boolean isEditable ()
    {
        return editable;
    }

    /**
     * Sets whether this list is editable or not.
     *
     * @param editable whether this list is editable or not
     */
    public void setEditable ( final boolean editable )
    {
        this.editable = editable;

        // Installing default cell editor if none added yet
        if ( editable && listCellEditor == null )
        {
            setCellEditor ( createDefaultCellEditor () );
        }
    }

    /**
     * Installs cell editor for this list.
     *
     * @param listCellEditor new cell editor
     */
    public void setCellEditor ( final ListCellEditor listCellEditor )
    {
        removeCellEditor ();
        this.listCellEditor = listCellEditor;
        listCellEditor.install ( this );
    }

    /**
     * Returns default cell editor for this list.
     *
     * @return default cell editor for this list
     */
    protected ListCellEditor createDefaultCellEditor ()
    {
        return new DefaultListCellEditor ();
    }

    /**
     * Returns cell editor for this list.
     *
     * @return cell editor for this list
     */
    public ListCellEditor getCellEditor ()
    {
        return listCellEditor;
    }

    /**
     * Uninstalls cell editor from this list.
     */
    public void removeCellEditor ()
    {
        if ( listCellEditor != null )
        {
            listCellEditor.uninstall ( this );
            listCellEditor = null;
        }
    }

    /**
     * Forces selected cell into editing mode.
     */
    public void editSelectedCell ()
    {
        editCell ( getSelectedIndex () );
    }

    /**
     * Forces the cell under specified index into editing mode.
     *
     * @param index index for the cell to edit
     */
    public void editCell ( final int index )
    {
        final ListCellEditor cellEditor = getCellEditor ();
        if ( index != -1 && cellEditor != null )
        {
            setSelectedIndex ( index );
            cellEditor.startEdit ( this, index );
        }
    }

    /**
     * Stops cell editing if possible.
     *
     * @return true if cell editing was stopped, false otherwise
     */
    public boolean stopCellEditing ()
    {
        final ListCellEditor cellEditor = getCellEditor ();
        return cellEditor != null && cellEditor.isEditing () && cellEditor.stopEdit ( WebList.this );

    }

    /**
     * Returns whether some list cell is being edited at the moment or not.
     *
     * @return true if some list cell is being edited at the moment, false otherwise
     */
    public boolean isEditing ()
    {
        final ListCellEditor cellEditor = getCellEditor ();
        return cellEditor != null && cellEditor.isEditing ();
    }

    /**
     * Returns custom WebLaF tooltip provider.
     *
     * @return custom WebLaF tooltip provider
     */
    public ToolTipProvider<? extends WebList> getToolTipProvider ()
    {
        return toolTipProvider;
    }

    /**
     * Sets custom WebLaF tooltip provider.
     *
     * @param provider custom WebLaF tooltip provider
     */
    public void setToolTipProvider ( final ToolTipProvider<? extends WebList> provider )
    {
        this.toolTipProvider = provider;
    }

    /**
     * Returns whether or not list allows an empty selection..
     *
     * @return true if list allows an empty selection, false otherwise
     */
    public boolean isEmptySelectionAllowed ()
    {
        return emptySelectionAllowed;
    }

    /**
     * Sets whether or not list allows an empty selection.
     *
     * @param emptySelectionAllowed whether or not list allows an empty selection.
     */
    public void setEmptySelectionAllowed ( final boolean emptySelectionAllowed )
    {
        this.emptySelectionAllowed = emptySelectionAllowed;

        // Updating selection model
        final int lead = getLeadSelectionIndex ();
        final int[] selected = getSelectedIndices ();
        setSelectionModel ( emptySelectionAllowed ? new DefaultListSelectionModel () : new UnselectableListModel () );
        setSelectedIndices ( selected );
        getSelectionModel ().setLeadSelectionIndex ( lead );
    }

    /**
     * Returns list model size.
     *
     * @return list model size
     */
    public int getModelSize ()
    {
        return getModel ().getSize ();
    }

    /**
     * Returns model value at the specified cell index.
     *
     * @param index cell index
     * @param <T>   value type
     * @return model value at the specified cell index
     */
    public <T> T getValueAt ( final int index )
    {
        return ( T ) getModel ().getElementAt ( index );
    }

    /**
     * Adds a listener to the list that's notified each time a change to the data model occurs.
     *
     * @param listener the ListDataListener to be added
     */
    public void addListDataListener ( final ListDataListener listener )
    {
        getModel ().addListDataListener ( listener );
    }

    /**
     * Removes a listener from the list that's notified each time a change to the data model occurs.
     *
     * @param listener the ListDataListener to be removed
     */
    public void removeListDataListener ( final ListDataListener listener )
    {
        getModel ().removeListDataListener ( listener );
    }

    /**
     * Scrolls list to specified cell.
     *
     * @param index cell index
     */
    public void scrollToCell ( final int index )
    {
        if ( index != -1 )
        {
            final Rectangle cellBounds = getCellBounds ( index, index );
            if ( cellBounds != null )
            {
                scrollRectToVisible ( cellBounds );
            }
        }
    }

    /**
     * Returns current mousover index.
     *
     * @return current mousover index
     */
    public int getHoverIndex ()
    {
        return getWebUI ().getHoverIndex ();
    }

    /**
     * Returns tree selection style.
     *
     * @return tree selection style
     */
    public ListSelectionStyle getSelectionStyle ()
    {
        return getWebUI ().getSelectionStyle ();
    }

    /**
     * Sets tree selection style.
     *
     * @param style tree selection style
     */
    public void setSelectionStyle ( final ListSelectionStyle style )
    {
        getWebUI ().setSelectionStyle ( style );
    }

    /**
     * Returns whether or not cells should be selected on hover.
     *
     * @return true if cells should be selected on hover, false otherwise
     */
    public boolean isSelectOnHover ()
    {
        return getWebUI ().isSelectOnHover ();
    }

    /**
     * Sets whether or not cells should be selected on hover.
     *
     * @param select whether or not cells should be selected on hover
     */
    public void setSelectOnHover ( final boolean select )
    {
        getWebUI ().setSelectOnHover ( select );
    }

    /**
     * Returns whether to scroll list down to selection automatically or not.
     *
     * @return true if list is being automatically scrolled to selection, false otherwise
     */
    public boolean isScrollToSelection ()
    {
        return getWebUI ().isScrollToSelection ();
    }

    /**
     * Sets whether to scroll list down to selection automatically or not.
     *
     * @param scroll whether to scroll list down to selection automatically or not
     */
    public void setScrollToSelection ( final boolean scroll )
    {
        getWebUI ().setScrollToSelection ( scroll );
    }

    @Override
    public StyleId getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return getWebUI ().setStyleId ( id );
    }

    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Override
    public Skin setSkin ( final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Override
    public Skin setSkin ( final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Override
    public Skin restoreSkin ()
    {
        return StyleManager.restoreSkin ( this );
    }

    @Override
    public void addStyleListener ( final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Override
    public Map<String, Painter> getCustomPainters ()
    {
        return StyleManager.getCustomPainters ( this );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter getCustomPainter ( final String id )
    {
        return StyleManager.getCustomPainter ( this, id );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public Painter setCustomPainter ( final String id, final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, id, painter );
    }

    @Override
    public boolean restoreDefaultPainters ()
    {
        return StyleManager.restoreDefaultPainters ( this );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets new margin.
     *
     * @param margin new margin
     */
    public void setMargin ( final int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }

    /**
     * Sets new margin.
     *
     * @param top    new top margin
     * @param left   new left margin
     * @param bottom new bottom margin
     * @param right  new right margin
     */
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    @Override
    public Insets getPadding ()
    {
        return getWebUI ().getPadding ();
    }

    /**
     * Sets new padding.
     *
     * @param padding new padding
     */
    public void setPadding ( final int padding )
    {
        setPadding ( padding, padding, padding, padding );
    }

    /**
     * Sets new padding.
     *
     * @param top    new top padding
     * @param left   new left padding
     * @param bottom new bottom padding
     * @param right  new right padding
     */
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        setPadding ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        getWebUI ().setPadding ( padding );
    }

    /**
     * Adds hover listener.
     *
     * @param listener hover listener to add
     */
    public void addHoverListener ( final HoverListener listener )
    {
        listenerList.add ( HoverListener.class, listener );
    }

    /**
     * Removes hover listener.
     *
     * @param listener hover listener to remove
     */
    public void removeHoverListener ( final HoverListener listener )
    {
        listenerList.remove ( HoverListener.class, listener );
    }

    /**
     * Informs about hover object change.
     *
     * @param previous previous hover object
     * @param current  current hover object
     */
    public void fireHoverChanged ( final Object previous, final Object current )
    {
        for ( final HoverListener listener : listenerList.getListeners ( HoverListener.class ) )
        {
            listener.hoverChanged ( previous, current );
        }
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebListUI getWebUI ()
    {
        return ( WebListUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebListUI ) )
        {
            try
            {
                setUI ( ( WebListUI ) ReflectUtils.createInstance ( WebLookAndFeel.listUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebListUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * Repaints list cell for the specified value.
     *
     * @param value cell value
     */
    public void repaint ( final Object value )
    {
        final ListModel model = getModel ();
        if ( model instanceof WebListModel )
        {
            repaint ( ( ( WebListModel ) model ).indexOf ( value ) );
        }
        else
        {
            for ( int i = 0; i < model.getSize (); i++ )
            {
                if ( model.getElementAt ( i ) == value )
                {
                    repaint ( i );
                    break;
                }
            }
        }
    }

    /**
     * Repaints list cell under the specified index.
     *
     * @param index cell index
     */
    public void repaint ( final int index )
    {
        repaint ( index, index );
    }

    /**
     * Repaints all list cells between the specified indices.
     *
     * @param from first cell index
     * @param to   last cell index
     */
    public void repaint ( final int from, final int to )
    {
        final Rectangle cellBounds = getCellBounds ( from, to );
        if ( cellBounds != null )
        {
            repaint ( cellBounds );
        }
    }

    /**
     * Returns currently edited cell index or -1 if none edited at the moment.
     *
     * @return currently edited cell index or -1 if none edited at the moment
     */
    public int getEditedCell ()
    {
        return editedCell;
    }

    /**
     * Adds list edit listener into this list.
     *
     * @param listener list edit listener to add
     */
    public void addListEditListener ( final ListEditListener listener )
    {
        listenerList.add ( ListEditListener.class, listener );
    }

    /**
     * Removes list edit listener from this list.
     *
     * @param listener list edit listener to remove
     */
    public void removeListEditListener ( final ListEditListener listener )
    {
        listenerList.remove ( ListEditListener.class, listener );
    }

    /**
     * Informs all listener that editing was started.
     *
     * @param index edited cell index
     */
    public void fireEditStarted ( final int index )
    {
        editedCell = index;
        for ( final ListEditListener listener : listenerList.getListeners ( ListEditListener.class ) )
        {
            listener.editStarted ( index );
        }
    }

    /**
     * Informs all listener that editing was finished.
     *
     * @param index    edited cell index
     * @param oldValue old cell value
     * @param newValue new cell value
     */
    public void fireEditFinished ( final int index, final Object oldValue, final Object newValue )
    {
        editedCell = -1;
        for ( final ListEditListener listener : listenerList.getListeners ( ListEditListener.class ) )
        {
            listener.editFinished ( index, oldValue, newValue );
        }
    }

    /**
     * Informs all listener that editing was cancelled.
     *
     * @param index edited cell index
     */
    public void fireEditCancelled ( final int index )
    {
        editedCell = -1;
        for ( final ListEditListener listener : listenerList.getListeners ( ListEditListener.class ) )
        {
            listener.editCancelled ( index );
        }
    }

    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseEnter ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseExit ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMenuTrigger ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, hotkey, runnable );
    }

    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusGain ( this, runnable );
    }

    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusLoss ( this, runnable );
    }

    @Override
    public WebList setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    @Override
    public WebList setPlainFont ( final boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    @Override
    public WebList setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    @Override
    public WebList setBoldFont ( final boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    @Override
    public WebList setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    @Override
    public WebList setItalicFont ( final boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    @Override
    public WebList setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebList setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    @Override
    public WebList setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    @Override
    public WebList changeFontSize ( final int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    @Override
    public WebList setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebList setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebList setFontName ( final String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    @Override
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    @Override
    public WebList setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    @Override
    public WebList setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    @Override
    public WebList setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    @Override
    public WebList setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    @Override
    public WebList setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    @Override
    public WebList setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebList setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }
}