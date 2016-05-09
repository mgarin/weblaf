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

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.menu.PopupMenuWay;
import com.alee.laf.menu.WebPopupMenuUI;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.LanguageUtils;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.managers.style.Skin;
import com.alee.managers.style.Skinnable;
import com.alee.managers.style.StyleListener;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.EventUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

/**
 * Custom button that displays an additional side (split side) that could call a popup menu.
 * You can specify the displayed menu using setPopupMenu method.
 *
 * @author Mikle Garin
 * @see com.alee.extended.button.WebSplitButtonUI
 * @see #setPopupMenu(javax.swing.JPopupMenu)
 */

public class WebSplitButton extends JButton
        implements ActionListener, Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, EventMethods,
        ToolTipMethods, LanguageMethods, FontMethods<WebSplitButton>, SizeMethods<WebSplitButton>
{
    /**
     * Default split button icon.
     */
    public static final ImageIcon defaultSplitIcon = new ImageIcon ( WebSplitButton.class.getResource ( "icons/splitIcon.png" ) );

    /**
     * Custom component properties.
     */
    public static final String SPLIT_ICON_PROPERTY = "splitIcon";

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
     * Split button icon.
     */
    protected Icon splitIcon;

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
        super ( icon );
        setStyleId ( StyleId.splitbuttonIcon );
    }

    /**
     * Constructs new split button.
     *
     * @param icon         button icon
     * @param rolloverIcon button rollover icon
     */
    public WebSplitButton ( final Icon icon, final Icon rolloverIcon )
    {
        super ( icon );
        setRolloverIcon ( rolloverIcon );
        setStyleId ( StyleId.splitbuttonIcon );
    }

    /**
     * Constructs new split button.
     *
     * @param text button text
     */
    public WebSplitButton ( final String text )
    {
        super ( text );
    }

    /**
     * Constructs new split button.
     *
     * @param text button text
     * @param icon button icon
     */
    public WebSplitButton ( final String text, final Icon icon )
    {
        super ( text, icon );
    }

    /**
     * Constructs new split button.
     *
     * @param listener button action listener
     */
    public WebSplitButton ( final ActionListener listener )
    {
        super ();
        addActionListener ( listener );
    }

    /**
     * Constructs new split button.
     *
     * @param icon     button icon
     * @param listener button action listener
     */
    public WebSplitButton ( final Icon icon, final ActionListener listener )
    {
        super ( icon );
        setStyleId ( StyleId.splitbuttonIcon );
        addActionListener ( listener );
    }

    /**
     * Constructs new split button.
     *
     * @param text     button text
     * @param listener button action listener
     */
    public WebSplitButton ( final String text, final ActionListener listener )
    {
        super ( text );
        addActionListener ( listener );
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
        super ( text, icon );
        addActionListener ( listener );
    }

    /**
     * Constructs new split button.
     *
     * @param action button action
     */
    public WebSplitButton ( final Action action )
    {
        super ( action );
    }

    /**
     * Constructs new split button.
     *
     * @param id style ID
     */
    public WebSplitButton ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Constructs new split button.
     *
     * @param id   style ID
     * @param icon button icon
     */
    public WebSplitButton ( final StyleId id, final Icon icon )
    {
        super ( icon );
        setStyleId ( id );
    }

    /**
     * Constructs new split button.
     *
     * @param id   style ID
     * @param text button text
     */
    public WebSplitButton ( final StyleId id, final String text )
    {
        super ( text );
        setStyleId ( id );
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
        super ( text, icon );
        setStyleId ( id );
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
        super ( icon );
        setRolloverIcon ( rolloverIcon );
        setStyleId ( id );
    }

    /**
     * Constructs new split button.
     *
     * @param id       style ID
     * @param listener button action listener
     */
    public WebSplitButton ( final StyleId id, final ActionListener listener )
    {
        super ();
        setStyleId ( id );
        addActionListener ( listener );
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
        super ( icon );
        setStyleId ( id );
        addActionListener ( listener );
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
        super ( text );
        setStyleId ( id );
        addActionListener ( listener );
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
        addActionListener ( listener );
        setStyleId ( id );
    }

    /**
     * Constructs new split button.
     *
     * @param id     style ID
     * @param action button action
     */
    public WebSplitButton ( final StyleId id, final Action action )
    {
        super ( action );
        setStyleId ( id );
    }

    @Override
    protected void init ( final String text, final Icon icon )
    {
        super.init ( LanguageUtils.getInitialText ( text ), icon );
        LanguageUtils.registerInitialLanguage ( this, text );
        addActionListener ( this );
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
        this.popupMenu = popupMenu;
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
     * Returns split button icon.
     *
     * @return split button icon
     */
    public Icon getSplitIcon ()
    {
        return splitIcon != null ? splitIcon : defaultSplitIcon;
    }

    /**
     * Sets split button icon
     *
     * @param splitIcon new split button icon
     */
    public void setSplitIcon ( final Icon splitIcon )
    {
        final Icon oldIcon = this.splitIcon;
        this.splitIcon = splitIcon;
        firePropertyChange ( SPLIT_ICON_PROPERTY, oldIcon, splitIcon );
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
        if ( ( listener != null ) && ( getAction () == listener ) )
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
        if ( getWebUI ().isOnSplit () )
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

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebSplitButtonUI getWebUI ()
    {
        return ( WebSplitButtonUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebSplitButtonUI ) )
        {
            try
            {
                setUI ( ( WebSplitButtonUI ) ReflectUtils.createInstance ( WebLookAndFeel.splitButtonUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebSplitButtonUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public String getUIClassID ()
    {
        return StyleableComponent.splitbutton.getUIClassID ();
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

    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }

    @Override
    public WebSplitButton setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    @Override
    public WebSplitButton setPlainFont ( final boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    @Override
    public WebSplitButton setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    @Override
    public WebSplitButton setBoldFont ( final boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    @Override
    public WebSplitButton setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    @Override
    public WebSplitButton setItalicFont ( final boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    @Override
    public WebSplitButton setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebSplitButton setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    @Override
    public WebSplitButton setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    @Override
    public WebSplitButton changeFontSize ( final int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    @Override
    public WebSplitButton setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebSplitButton setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebSplitButton setFontName ( final String fontName )
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
    public WebSplitButton setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    @Override
    public WebSplitButton setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    @Override
    public WebSplitButton setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    @Override
    public WebSplitButton setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    @Override
    public WebSplitButton setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    @Override
    public WebSplitButton setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebSplitButton setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }
}