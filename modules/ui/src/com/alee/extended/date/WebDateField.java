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

import com.alee.api.annotations.NotNull;
import com.alee.extended.WebComponent;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.swing.Customizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom date chooser component.
 * It relies on {@link WebCalendar} component as a main date chooser.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see DateFieldDescriptor
 * @see WDateFieldUI
 * @see WebDateFieldUI
 * @see IDateFieldPainter
 * @see DateFieldPainter
 * @see WebComponent
 */
public class WebDateField extends WebComponent<WebDateField, WDateFieldUI>
{
    /**
     * Component properties.
     */
    public static final String DATE_PROPERTY = "date";
    public static final String DATE_FORMAT_PROPERTY = "dateFormat";
    public static final String ALLOW_USER_INPUT_PROPERTY = "allowUserInput";
    public static final String CALENDAR_CUSTOMIZER_PROPERTY = "calendarCustomizer";

    /**
     * Currently selected date.
     */
    protected Date date;

    /**
     * Date display format.
     */
    protected DateFormat dateFormat;

    /**
     * Indicating whether manual user input allowed or not.
     * This would commonly enable text date input field.
     */
    protected boolean allowUserInput;

    /**
     * Calendar component customizer.
     */
    protected Customizer<WebCalendar> calendarCustomizer;

    /**
     * Constructs new date field.
     */
    public WebDateField ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new date field with specified selected date.
     *
     * @param date selected date
     */
    public WebDateField ( final Date date )
    {
        this ( StyleId.auto, date );
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
        this.date = null;
        this.dateFormat = new SimpleDateFormat ( "dd.MM.yyyy", LanguageManager.getLocale () );
        this.allowUserInput = true;
        this.calendarCustomizer = null;
        updateUI ();
        setStyleId ( id );
        setDate ( date );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.datefield;
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

    /**
     * Returns the LaF object that renders this component.
     *
     * @return LabelUI object
     */
    public WDateFieldUI getUI ()
    {
        return ( WDateFieldUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WDateFieldUI}
     */
    public void setUI ( final WDateFieldUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @NotNull
    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}