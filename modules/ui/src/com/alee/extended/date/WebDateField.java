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

package com.alee.extended.date;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.swing.Customizer;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Custom chooser component that provides date selection.
 *
 * @author Mikle Garin
 */

public class WebDateField extends JComponent
        implements Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, SettingsMethods, SizeMethods<WebDateField>
{
    /**
     * Component properties.
     */
    public static final String DATE_PROPERTY = "date";
    public static final String DATE_FORMAT_PROPERTY = "dateFormat";
    public static final String CALENDAR_CUSTOMIZER_PROPERTY = "calendarCustomizer";
    public static final String ALLOW_USER_INPUT_PROPERTY = "allowUserInput";

    /**
     * Date display format.
     */
    protected DateFormat dateFormat = new SimpleDateFormat ( "dd.MM.yyyy" );

    /**
     * Calendar component customizer.
     */
    protected Customizer<WebCalendar> calendarCustomizer;

    /**
     * Currently selected date.
     */
    protected Date date;

    /**
     * Indicating whether manual user input allowed or not.
     * This would commonly enable text date input field.
     */
    protected boolean allowUserInput = true;

    /**
     * Constructs new date field.
     */
    public WebDateField ()
    {
        this ( StyleId.datefield, null );
    }

    /**
     * Constructs new date field with specified selected date.
     *
     * @param date selected date
     */
    public WebDateField ( final Date date )
    {
        this ( StyleId.datefield, date );
    }

    /**
     * Constructs new date field.
     *
     * @param id style ID
     */
    public WebDateField ( final StyleId id )
    {
        this ( id, null );
    }

    /**
     * Constructs new date field with specified selected date.
     *
     * @param id   style ID
     * @param date selected date
     */
    public WebDateField ( final StyleId id, final Date date )
    {
        super ();
        setDate ( date );
        updateUI ();
        setStyleId ( id );
    }

    /**
     * Returns selected date.
     *
     * @return selected date
     */
    public Date getDate ()
    {
        return date;
    }

    /**
     * Sets selected date.
     *
     * @param date selected date
     */
    public void setDate ( final Date date )
    {
        final Date previous = this.date;
        this.date = date;
        firePropertyChange ( DATE_PROPERTY, previous, date );
        fireDateChanged ( date );
    }

    /**
     * Returns date format.
     *
     * @return date format
     */
    public DateFormat getDateFormat ()
    {
        return dateFormat;
    }

    /**
     * Sets date format.
     *
     * @param dateFormat date format
     */
    public void setDateFormat ( final DateFormat dateFormat )
    {
        final DateFormat previous = this.dateFormat;
        this.dateFormat = dateFormat;
        firePropertyChange ( DATE_FORMAT_PROPERTY, previous, dateFormat );
    }

    /**
     * Returns whether user input allowed or not.
     *
     * @return true if user input allowed, false otherwise
     */
    public boolean isAllowUserInput ()
    {
        return allowUserInput;
    }

    /**
     * Sets whether user input should be allowed or not.
     *
     * @param allowUserInput whether user input should be allowed or not
     */
    public void setAllowUserInput ( final boolean allowUserInput )
    {
        final boolean previous = this.allowUserInput;
        this.allowUserInput = allowUserInput;
        firePropertyChange ( ALLOW_USER_INPUT_PROPERTY, previous, allowUserInput );
    }

    /**
     * Returns calendar component customizer.
     *
     * @return calendar component customizer
     */
    public Customizer<WebCalendar> getCalendarCustomizer ()
    {
        return calendarCustomizer;
    }

    /**
     * Sets calendar component customizer.
     *
     * @param customizer calendar component customizer
     */
    public void setCalendarCustomizer ( final Customizer<WebCalendar> customizer )
    {
        final Customizer<WebCalendar> previous = this.calendarCustomizer;
        this.calendarCustomizer = customizer;
        firePropertyChange ( CALENDAR_CUSTOMIZER_PROPERTY, previous, calendarCustomizer );
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
     * Returns the L&amp;F object that renders this component.
     *
     * @return LabelUI object
     */
    public DateFieldUI getUI ()
    {
        return ( DateFieldUI ) ui;
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebDateFieldUI getWebUI ()
    {
        return ( WebDateFieldUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebDateFieldUI ) )
        {
            try
            {
                setUI ( ( WebDateFieldUI ) ReflectUtils.createInstance ( WebLookAndFeel.dateFieldUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebDateFieldUI () );
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
        return StyleableComponent.datefield.getUIClassID ();
    }

    @Override
    public void registerSettings ( final String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    @Override
    public void registerSettings ( final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    @Override
    public void registerSettings ( final String group, final String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    @Override
    public void registerSettings ( final String key, final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    @Override
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( this );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    @Override
    public WebDateField setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    @Override
    public WebDateField setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    @Override
    public WebDateField setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    @Override
    public WebDateField setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    @Override
    public WebDateField setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    @Override
    public WebDateField setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebDateField setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }

    /**
     * Adds date change listener.
     *
     * @param listener date change listener to add
     */
    public void addDateListener ( final DateListener listener )
    {
        listenerList.add ( DateListener.class, listener );
    }

    /**
     * Removes date change listener.
     *
     * @param listener date change listener to remove
     */
    public void removeDateListener ( final DateListener listener )
    {
        listenerList.remove ( DateListener.class, listener );
    }

    /**
     * Notifies about date selection change.
     *
     * @param date selected date
     */
    protected void fireDateChanged ( final Date date )
    {
        for ( final DateListener listener : listenerList.getListeners ( DateListener.class ) )
        {
            listener.dateChanged ( date );
        }
    }
}