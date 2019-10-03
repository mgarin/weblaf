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

package com.alee.extended.button;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.menu.PopupMenuWay;
import com.alee.laf.menu.WebPopupMenuUI;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.*;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Custom button that displays an additional side (split side) that could call a popup menu.
 * You can specify the displayed menu using setPopupMenu method.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see SplitButtonDescriptor
 * @see WSplitButtonUI
 * @see WebSplitButtonUI
 * @see ISplitButtonPainter
 * @see SplitButtonPainter
 * @see JButton
 * @see #setPopupMenu(javax.swing.JPopupMenu)
 */
public class WebSplitButton extends JButton implements ActionListener, Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods,
        EventMethods, ToolTipMethods, LanguageMethods, LanguageEventMethods, SettingsMethods, FontMethods<WebSplitButton>,
        SizeMethods<WebSplitButton>
{
    /**
     * todo 1. All menu-related stuff should be moved into UI
     * todo 2. Need to provide appropriate property changes
     */

    /**
     * Component properties.
     */
    public static final String POPUP_MENU_PROPERTY = "popupMenu";
    public static final String MENU_ICON_PROPERTY = "menuIcon";

    /**
     * Whether should always display popup menu when button is clicked or not.
     * If set to false popup menu will only be displayed when split button part is clicked.
     */
    protected boolean alwaysShowMenu = false;

    /**
     * Popup menu display way.
     */
    protected PopupMenuWay popupMenuWay = PopupMenuWay.belowStart;

    /**
     * Split button popup menu.
     */
    protected JPopupMenu popupMenu;

    /**
     * Menu icon.
     */
    protected Icon menuIcon;

    /**
     * Constructs new split button.
     */
    public WebSplitButton ()
    {
        super ();
    }

    /**
     * Constructs new split button.
     *
     * @param icon button icon
     */
    public WebSplitButton ( final Icon icon )
    {
        this ( StyleId.auto, icon );
    }

    /**
     * Constructs new split button.
     *
     * @param icon         button icon
     * @param rolloverIcon button rollover icon
     */
    public WebSplitButton ( final Icon icon, final Icon rolloverIcon )
    {
        this ( StyleId.auto, icon, rolloverIcon );
    }

    /**
     * Constructs new split button.
     *
     * @param text button text
     */
    public WebSplitButton ( final String text )
    {
        this ( StyleId.auto, text );
    }

    /**
     * Constructs new split button.
     *
     * @param text button text
     * @param icon button icon
     */
    public WebSplitButton ( final String text, final Icon icon )
    {
        this ( StyleId.auto, text, icon );
    }

    /**
     * Constructs new split button.
     *
     * @param listener button action listener
     */
    public WebSplitButton ( final ActionListener listener )
    {
        this ( StyleId.auto, listener );
    }

    /**
     * Constructs new split button.
     *
     * @param icon     button icon
     * @param listener button action listener
     */
    public WebSplitButton ( final Icon icon, final ActionListener listener )
    {
        this ( StyleId.auto, icon, listener );
    }

    /**
     * Constructs new split button.
     *
     * @param text     button text
     * @param listener button action listener
     */
    public WebSplitButton ( final String text, final ActionListener listener )
    {
        this ( StyleId.auto, text, listener );
    }

    /**
     * Constructs new split button.
     *
     * @param text     button text
     * @param icon     button icon
     * @param listener button action listener
     */
    public WebSplitButton ( final String text, final Icon icon, final ActionListener listener )
    {
        this ( StyleId.auto, text, icon, listener );
    }

    /**
     * Constructs new split button.
     *
     * @param action button action
     */
    public WebSplitButton ( final Action action )
    {
        this ( StyleId.auto, action );
    }

    /**
     * Constructs new split button.
     *
     * @param id style ID
     */
    public WebSplitButton ( final StyleId id )
    {
        this ( id, null, null, null );
    }

    /**
     * Constructs new split button.
     *
     * @param id   style ID
     * @param icon button icon
     */
    public WebSplitButton ( final StyleId id, final Icon icon )
    {
        this ( id, null, icon, null );
    }

    /**
     * Constructs new split button.
     *
     * @param id   style ID
     * @param text button text
     */
    public WebSplitButton ( final StyleId id, final String text )
    {
        this ( id, text, null, null );
    }

    /**
     * Constructs new split button.
     *
     * @param id   style ID
     * @param text button text
     * @param icon button icon
     */
    public WebSplitButton ( final StyleId id, final String text, final Icon icon )
    {
        this ( id, text, icon, null );
    }

    /**
     * Constructs new split button.
     *
     * @param id       style ID
     * @param listener button action listener
     */
    public WebSplitButton ( final StyleId id, final ActionListener listener )
    {
        this ( id, null, null, listener );
    }

    /**
     * Constructs new split button.
     *
     * @param id       style ID
     * @param icon     button icon
     * @param listener button action listener
     */
    public WebSplitButton ( final StyleId id, final Icon icon, final ActionListener listener )
    {
        this ( id, null, icon, listener );
    }

    /**
     * Constructs new split button.
     *
     * @param id       style ID
     * @param text     button text
     * @param listener button action listener
     */
    public WebSplitButton ( final StyleId id, final String text, final ActionListener listener )
    {
        this ( id, text, null, listener );
    }

    /**
     * Constructs new split button.
     *
     * @param id           style ID
     * @param icon         button icon
     * @param rolloverIcon button rollover icon
     */
    public WebSplitButton ( final StyleId id, final Icon icon, final Icon rolloverIcon )
    {
        this ( id, null, icon, null );
        setRolloverIcon ( rolloverIcon );
    }

    /**
     * Constructs new split button.
     *
     * @param id     style ID
     * @param action button action
     */
    public WebSplitButton ( final StyleId id, final Action action )
    {
        this ( id, null, null, null );
        setAction ( action );
    }

    /**
     * Constructs new split button.
     *
     * @param id       style ID
     * @param text     button text
     * @param icon     button icon
     * @param listener button action listener
     */
    public WebSplitButton ( final StyleId id, final String text, final Icon icon, final ActionListener listener )
    {
        super ( text, icon );
        if ( listener != null )
        {
            addActionListener ( listener );
        }
        setStyleId ( id );
    }

    @Override
    protected void init ( final String text, final Icon icon )
    {
        super.init ( UILanguageManager.getInitialText ( text ), icon );
        UILanguageManager.registerInitialLanguage ( this, text );
        addActionListener ( this );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return getIcon () != null && getText () == null ? StyleId.splitbuttonIcon : StyleId.splitbutton;
    }

    @NotNull
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

    @NotNull
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

    @Nullable
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
    public void setMargin ( @Nullable final Insets margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Nullable
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
    public void setPadding ( @Nullable final Insets padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    /**
     * Returns the popup menu if set, null otherwise.
     *
     * @return popup menu
     */
    public JPopupMenu getPopupMenu ()
    {
        return popupMenu;
    }

    /**
     * Sets the popup menu to be displayed, when the split part of the button is clicked.
     *
     * @param popupMenu popup menu to be displayed, when the split part of the button is clicked
     */
    public void setPopupMenu ( final JPopupMenu popupMenu )
    {
        final JPopupMenu old = this.popupMenu;
        this.popupMenu = popupMenu;
        SwingUtils.firePropertyChanged ( this, POPUP_MENU_PROPERTY, old, popupMenu );
    }

    /**
     * Returns whether should always display popup menu when button is clicked or not.
     *
     * @return true if should always display popup menu when button is clicked, false otherwise
     */
    public boolean isAlwaysShowMenu ()
    {
        return alwaysShowMenu;
    }

    /**
     * Sets whether should always display popup menu when button is clicked or not.
     *
     * @param alwaysShowMenu whether should always display popup menu when button is clicked or not
     */
    public void setAlwaysShowMenu ( final boolean alwaysShowMenu )
    {
        this.alwaysShowMenu = alwaysShowMenu;
    }

    /**
     * Returns approximate popup menu display way.
     *
     * @return approximate popup menu display way
     */
    public PopupMenuWay getPopupMenuWay ()
    {
        return popupMenuWay;
    }

    /**
     * Sets approximate popup menu display way.
     * This will have effect only if WebPopupMenuUI is used as popup menu UI.
     * Otherwise this variable will have no effect on popup menu display way.
     *
     * @param way approximate popup menu display way
     */
    public void setPopupMenuWay ( final PopupMenuWay way )
    {
        this.popupMenuWay = way;
    }

    /**
     * Returns menu icon.
     *
     * @return menu icon
     */
    public Icon getMenuIcon ()
    {
        return menuIcon;
    }

    /**
     * Sets menu icon
     *
     * @param menuIcon new menu icon
     */
    public void setMenuIcon ( final Icon menuIcon )
    {
        final Icon oldIcon = this.menuIcon;
        this.menuIcon = menuIcon;
        firePropertyChange ( MENU_ICON_PROPERTY, oldIcon, menuIcon );
    }

    /**
     * Adds SplitButtonListener to the button.
     *
     * @param listener the SplitButtonListener to be added
     */
    public void addSplitButtonListener ( final SplitButtonListener listener )
    {
        listenerList.add ( SplitButtonListener.class, listener );
    }

    /**
     * Removes SplitButtonListener from the button.
     * If the listener is the currently set Action for the button, then the Action is set to null.
     *
     * @param listener the SplitButtonListener to be removed
     */
    public void removeSplitButtonListener ( final SplitButtonListener listener )
    {
        if ( listener != null && getAction () == listener )
        {
            setAction ( null );
        }
        else
        {
            listenerList.remove ( SplitButtonListener.class, listener );
        }
    }

    @Override
    public void actionPerformed ( final ActionEvent e )
    {
        if ( getUI ().isOnSplit () )
        {
            showPopupMenu ();
            fireSplitbuttonClicked ( e );
        }
        else
        {
            if ( isAlwaysShowMenu () )
            {
                showPopupMenu ();
            }
            fireButtonClicked ( e );
        }
    }

    /**
     * Returns whether or not {@link #popupMenu} is currently visible.
     *
     * @return {@code true} if {@link #popupMenu} is currently visible, {@code false} otherwise
     */
    public boolean isPopupMenuVisible ()
    {
        return popupMenu != null && popupMenu.isShowing ();
    }

    /**
     * Displays split button popup menu.
     */
    public void showPopupMenu ()
    {
        if ( popupMenu != null )
        {
            if ( popupMenu.getUI () instanceof WebPopupMenuUI )
            {
                ( ( WebPopupMenuUI ) popupMenu.getUI () ).setPopupMenuWay ( popupMenuWay );
            }
            if ( getComponentOrientation ().isLeftToRight () )
            {
                popupMenu.show ( this, 0, getHeight () );
            }
            else
            {
                popupMenu.show ( this, getWidth () - popupMenu.getPreferredSize ().width, getHeight () );
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.
     * The event instance is lazily created using the {@code event} parameter.
     *
     * @param event the {@code java.awt.event.ActionEvent} object
     * @see javax.swing.event.EventListenerList
     */
    protected void fireButtonClicked ( final ActionEvent event )
    {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList ();
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for ( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if ( listeners[ i ] == SplitButtonListener.class )
            {
                // Lazily create the event:
                if ( e == null )
                {
                    String actionCommand = event.getActionCommand ();
                    if ( actionCommand == null )
                    {
                        actionCommand = getActionCommand ();
                    }
                    e = new ActionEvent ( WebSplitButton.this, ActionEvent.ACTION_PERFORMED, actionCommand, event.getWhen (),
                            event.getModifiers () );
                }
                ( ( SplitButtonListener ) listeners[ i + 1 ] ).buttonClicked ( e );
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.
     * The event instance is lazily created using the {@code event} parameter.
     *
     * @param event the {@code java.awt.event.ActionEvent} object
     * @see javax.swing.event.EventListenerList
     */
    protected void fireSplitbuttonClicked ( final ActionEvent event )
    {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList ();
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for ( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if ( listeners[ i ] == SplitButtonListener.class )
            {
                // Lazily create the event:
                if ( e == null )
                {
                    String actionCommand = event.getActionCommand ();
                    if ( actionCommand == null )
                    {
                        actionCommand = getActionCommand ();
                    }
                    e = new ActionEvent ( WebSplitButton.this, ActionEvent.ACTION_PERFORMED, actionCommand, event.getWhen (),
                            event.getModifiers () );
                }
                ( ( SplitButtonListener ) listeners[ i + 1 ] ).splitButtonClicked ( e );
            }
        }
    }

    @NotNull
    @Override
    public MouseAdapter onMousePress ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMousePress ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseEnter ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseExit ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseDrag ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseDrag ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseClick ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseClick ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDoubleClick ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMenuTrigger ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyType ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyType ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyPress ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyPress ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyRelease ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyRelease ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public FocusAdapter onFocusGain ( @NotNull final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @NotNull
    @Override
    public FocusAdapter onFocusLoss ( @NotNull final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDragStart ( final int shift, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDragStart ( final int shift, @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, mouseButton, runnable );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public void removeToolTip ( final WebCustomTooltip tooltip )
    {
        TooltipManager.removeTooltip ( this, tooltip );
    }

    @Override
    public void removeToolTips ()
    {
        TooltipManager.removeTooltips ( this );
    }

    @Override
    public void removeToolTips ( final WebCustomTooltip... tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
    }

    @Override
    public void removeToolTips ( final List<WebCustomTooltip> tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
    }

    @Nullable
    @Override
    public String getLanguage ()
    {
        return UILanguageManager.getComponentKey ( this );
    }

    @Override
    public void setLanguage ( @NotNull final String key, @Nullable final Object... data )
    {
        UILanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( @Nullable final Object... data )
    {
        UILanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( @NotNull final String key, @Nullable final Object... data )
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
    public void setLanguageUpdater ( @NotNull final LanguageUpdater updater )
    {
        UILanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        UILanguageManager.unregisterLanguageUpdater ( this );
    }

    @Override
    public void addLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.addLanguageListener ( getRootPane (), listener );
    }

    @Override
    public void removeLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.removeLanguageListener ( getRootPane (), listener );
    }

    @Override
    public void removeLanguageListeners ()
    {
        UILanguageManager.removeLanguageListeners ( getRootPane () );
    }

    @Override
    public void addDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.addDictionaryListener ( getRootPane (), listener );
    }

    @Override
    public void removeDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.removeDictionaryListener ( getRootPane (), listener );
    }

    @Override
    public void removeDictionaryListeners ()
    {
        UILanguageManager.removeDictionaryListeners ( getRootPane () );
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
    public WebSplitButton setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebSplitButton setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebSplitButton setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebSplitButton setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebSplitButton setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebSplitButton setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebSplitButton setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebSplitButton setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebSplitButton setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebSplitButton changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebSplitButton setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebSplitButton setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebSplitButton setFontName ( final String fontName )
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

    @NotNull
    @Override
    public WebSplitButton setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @NotNull
    @Override
    public WebSplitButton setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @NotNull
    @Override
    public WebSplitButton setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @NotNull
    @Override
    public WebSplitButton setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @NotNull
    @Override
    public WebSplitButton setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @NotNull
    @Override
    public Dimension getMaximumSize ()
    {
        return SizeMethodsImpl.getMaximumSize ( this, super.getMaximumSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalMaximumSize ()
    {
        return SizeMethodsImpl.getOriginalMaximumSize ( this, super.getMaximumSize () );
    }

    @NotNull
    @Override
    public WebSplitButton setMaximumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMaximumSize ( this, width, height );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @NotNull
    @Override
    public WebSplitButton setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @NotNull
    @Override
    public WebSplitButton setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @NotNull
    @Override
    public Dimension getMinimumSize ()
    {
        return SizeMethodsImpl.getMinimumSize ( this, super.getMinimumSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalMinimumSize ()
    {
        return SizeMethodsImpl.getOriginalMinimumSize ( this, super.getMinimumSize () );
    }

    @NotNull
    @Override
    public WebSplitButton setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WSplitButtonUI} object that renders this component
     */
    @Override
    public WSplitButtonUI getUI ()
    {
        return ( WSplitButtonUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WSplitButtonUI}
     */
    public void setUI ( final WSplitButtonUI ui )
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