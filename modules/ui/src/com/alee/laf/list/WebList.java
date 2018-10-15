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

import com.alee.api.jdk.Objects;
import com.alee.laf.list.behavior.ListHoverSelectionBehavior;
import com.alee.laf.list.behavior.ListSelectionScrollBehavior;
import com.alee.laf.list.editor.ListCellEditor;
import com.alee.laf.list.editor.ListEditListener;
import com.alee.laf.list.editor.TextListCellEditor;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.*;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.HoverListener;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * {@link JList} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JList
 * @see WebListUI
 * @see ListPainter
 */
public class WebList extends JList implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods, EventMethods,
        LanguageMethods, LanguageEventMethods, SettingsMethods, FontMethods<WebList>, SizeMethods<WebList>
{
    /**
     * todo 1. Generics usage when migrated to JDK8+
     */

    /**
     * Whether or not this list is editable.
     */
    protected boolean editable = false;

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
     * List cell editor.
     */
    protected transient ListCellEditor listCellEditor = null;

    /**
     * Custom WebLaF tooltip provider.
     */
    protected transient ListToolTipProvider toolTipProvider = null;

    /**
     * Constructs empty list.
     */
    public WebList ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param data list data
     */
    public WebList ( final List data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param data list data
     */
    public WebList ( final Vector data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param data list data
     */
    public WebList ( final Object[] data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs list with the specified list model.
     *
     * @param model list model
     */
    public WebList ( final ListModel model )
    {
        this ( StyleId.auto, model );
    }

    /**
     * Constructs empty list.
     *
     * @param id style ID
     */
    public WebList ( final StyleId id )
    {
        this ( id, new WebListModel () );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param id   style ID
     * @param data list data
     */
    public WebList ( final StyleId id, final List data )
    {
        this ( id, new WebListModel ( data ) );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param id   style ID
     * @param data list data
     */
    public WebList ( final StyleId id, final Vector data )
    {
        this ( id, new WebListModel ( data ) );
    }

    /**
     * Constructs list with the specified data.
     *
     * @param id   style ID
     * @param data list data
     */
    public WebList ( final StyleId id, final Object[] data )
    {
        this ( id, new WebListModel ( data ) );
    }

    /**
     * Constructs list with the specified list model.
     *
     * @param id    style ID
     * @param model list model
     */
    public WebList ( final StyleId id, final ListModel model )
    {
        super ( model );
        setStyleId ( id );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.list;
    }

    /**
     * Returns whether multiple values selection allowed or not.
     *
     * @return {@code true} if multiple values selection allowed, {@code false} otherwise
     */
    public boolean isMultipleSelectionAllowed ()
    {
        return Objects.equals ( getSelectionMode (), ListSelectionModel.SINGLE_INTERVAL_SELECTION,
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
    }

    /**
     * Sets whether multiple values selection allowed or not.
     * This call simply changes selection mode according to provided value.
     *
     * @param allowed whether multiple values selection allowed or not
     */
    public void setMultipleSelectionAllowed ( final boolean allowed )
    {
        setSelectionMode ( allowed ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION );
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
        setSelectedIndices ( CollectionUtils.toIntArray ( indices ) );
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
        return new TextListCellEditor<String> ()
        {
            @Override
            protected String valueToText ( final JList list, final int index, final String value )
            {
                return value;
            }

            @Override
            protected String textToValue ( final JList list, final int index, final String oldValue, final String text )
            {
                return text;
            }
        };
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
     * Returns {@link ListToolTipProvider}.
     *
     * @return {@link ListToolTipProvider}
     */
    public ListToolTipProvider getToolTipProvider ()
    {
        return toolTipProvider;
    }

    /**
     * Sets {@link ListToolTipProvider}.
     *
     * @param provider {@link ListToolTipProvider}
     */
    public void setToolTipProvider ( final ListToolTipProvider provider )
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
     * Returns tree selection style.
     *
     * @return tree selection style
     */
    public ListSelectionStyle getSelectionStyle ()
    {
        return getUI ().getSelectionStyle ();
    }

    /**
     * Sets tree selection style.
     *
     * @param style tree selection style
     */
    public void setSelectionStyle ( final ListSelectionStyle style )
    {
        getUI ().setSelectionStyle ( style );
    }

    /**
     * Returns whether or not cells should be selected on hover.
     *
     * @return true if cells should be selected on hover, false otherwise
     */
    public boolean isSelectOnHover ()
    {
        return ListHoverSelectionBehavior.isInstalled ( this );
    }

    /**
     * Sets whether or not cells should be selected on hover.
     *
     * @param select whether or not cells should be selected on hover
     */
    public void setSelectOnHover ( final boolean select )
    {
        if ( select )
        {
            if ( !isSelectOnHover () )
            {
                ListHoverSelectionBehavior.install ( this );
            }
        }
        else
        {
            if ( isSelectOnHover () )
            {
                ListHoverSelectionBehavior.uninstall ( this );
            }
        }
    }

    /**
     * Returns whether to scroll list down to selection automatically or not.
     *
     * @return true if list is being automatically scrolled to selection, false otherwise
     */
    public boolean isScrollToSelection ()
    {
        return ListSelectionScrollBehavior.isInstalled ( this );
    }

    /**
     * Sets whether to scroll list down to selection automatically or not.
     *
     * @param scroll whether to scroll list down to selection automatically or not
     */
    public void setScrollToSelection ( final boolean scroll )
    {
        if ( scroll )
        {
            if ( !isScrollToSelection () )
            {
                ListSelectionScrollBehavior.install ( this );
            }
        }
        else
        {
            if ( isScrollToSelection () )
            {
                ListSelectionScrollBehavior.uninstall ( this );
            }
        }
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( this );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( this, id );
    }

    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( this );
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
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( this );
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
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public boolean resetCustomPainter ()
    {
        return StyleManager.resetCustomPainter ( this );
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
     * Returns hover listeners.
     *
     * @return hover listeners
     */
    public HoverListener[] getHoverListeners ()
    {
        return listenerList.getListeners ( HoverListener.class );
    }

    /**
     * Informs about hover object change.
     *
     * @param previous previous hover object
     * @param current  current hover object
     */
    public void fireHoverChanged ( final Object previous, final Object current )
    {
        for ( final HoverListener listener : getHoverListeners () )
        {
            listener.hoverChanged ( previous, current );
        }
    }

    @Override
    public Shape getShape ()
    {
        return ShapeMethodsImpl.getShape ( this );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return ShapeMethodsImpl.isShapeDetectionEnabled ( this );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        ShapeMethodsImpl.setShapeDetectionEnabled ( this, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return MarginMethodsImpl.getMargin ( this );
    }

    @Override
    public void setMargin ( final int margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        MarginMethodsImpl.setMargin ( this, top, left, bottom, right );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PaddingMethodsImpl.getPadding ( this );
    }

    @Override
    public void setPadding ( final int padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    @Override
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        PaddingMethodsImpl.setPadding ( this, top, left, bottom, right );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
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
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
    }

    @Override
    public MouseAdapter onDragStart ( final int shift, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, runnable );
    }

    @Override
    public MouseAdapter onDragStart ( final int shift, final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, mouseButton, runnable );
    }

    @Override
    public String getLanguage ()
    {
        return UILanguageManager.getComponentKey ( this );
    }

    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        UILanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( final Object... data )
    {
        UILanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        UILanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        UILanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return UILanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        UILanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        UILanguageManager.unregisterLanguageUpdater ( this );
    }

    @Override
    public void addLanguageListener ( final LanguageListener listener )
    {
        UILanguageManager.addLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListener ( final LanguageListener listener )
    {
        UILanguageManager.removeLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListeners ()
    {
        UILanguageManager.removeLanguageListeners ( this );
    }

    @Override
    public void addDictionaryListener ( final DictionaryListener listener )
    {
        UILanguageManager.addDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListener ( final DictionaryListener listener )
    {
        UILanguageManager.removeDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListeners ()
    {
        UILanguageManager.removeDictionaryListeners ( this );
    }

    @Override
    public void registerSettings ( final Configuration configuration )
    {
        UISettingsManager.registerComponent ( this, configuration );
    }

    @Override
    public void registerSettings ( final SettingsProcessor processor )
    {
        UISettingsManager.registerComponent ( this, processor );
    }

    @Override
    public void unregisterSettings ()
    {
        UISettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        UISettingsManager.loadSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        UISettingsManager.saveSettings ( this );
    }

    @Override
    public WebList setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebList setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebList setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebList setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebList setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebList setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebList setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebList setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebList setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebList changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebList setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebList setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebList setFontName ( final String fontName )
    {
        return FontMethodsImpl.setFontName ( this, fontName );
    }

    @Override
    public String getFontName ()
    {
        return FontMethodsImpl.getFontName ( this );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @Override
    public WebList setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public WebList setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public WebList setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public WebList setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public WebList setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public WebList setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebList setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WListUI} object that renders this component
     */
    @Override
    public WListUI getUI ()
    {
        return ( WListUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WListUI}
     */
    public void setUI ( final WListUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}