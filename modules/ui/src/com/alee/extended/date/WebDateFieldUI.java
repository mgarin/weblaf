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

import com.alee.extended.window.PopOverAlignment;
import com.alee.extended.window.PopOverDirection;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.text.WebFormattedTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CompareUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.DataRunnable;
import com.alee.utils.swing.FocusEventRunnable;
import com.alee.utils.swing.KeyEventRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;

/**
 * Custom UI for WebDateField component.
 *
 * @author Mikle Garin
 */

public class WebDateFieldUI extends DateFieldUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport, PropertyChangeListener
{
    /**
     * todo 1. Change popover to popup-based window. Probably another variation of popover would be handy?
     */

    /**
     * Component painter.
     */
    @DefaultPainter (DateFieldPainter.class)
    protected IDateFieldPainter painter;

    /**
     * UI elements.
     */
    protected WebFormattedTextField field;
    protected WebButton button;
    protected WebPopOver popup;
    protected WebCalendar calendar;

    /**
     * Runtime variables.
     */
    protected WebDateField dateField;
    protected Insets margin = null;
    protected Insets padding = null;
    protected boolean updating = false;

    /**
     * Returns an instance of the WebDateFieldUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebDateFieldUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDateFieldUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Saving date field reference
        dateField = ( WebDateField ) c;

        // Creating date field UI
        installComponents ();
        installActions ();

        // Applying skin
        StyleManager.installSkin ( dateField );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( dateField );

        // Destroying date field UI
        uninstallActions ();
        uninstallComponents ();

        // Removing date field reference
        dateField.removePropertyChangeListener ( this );
        dateField = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * Installs date field UI elements.
     */
    protected void installComponents ()
    {
        SwingUtils.setHandlesEnableStateMark ( dateField );

        dateField.setLayout ( new BorderLayout ( 0, 0 ) );

        field = new WebFormattedTextField ( StyleId.datefieldField.at ( dateField ) );
        dateField.add ( field, BorderLayout.CENTER );

        final ImageIcon icon = ImageUtils.getImageIcon ( WebDateFieldUI.class.getResource ( "icons/date.png" ) );
        button = new WebButton ( StyleId.datefieldButton.at ( dateField ), icon );
        dateField.add ( button, BorderLayout.LINE_END );
    }

    /**
     * Uninstalls date field UI elements.
     */
    protected void uninstallComponents ()
    {
        dateField.removeAll ();
        dateField.revalidate ();
        dateField.repaint ();
        calendar = null;
        popup = null;
        button = null;
        field = null;
        SwingUtils.removeHandlesEnableStateMark ( dateField );
    }

    /**
     * Installs actions for UI elements.
     */
    protected void installActions ()
    {
        // Date field property listener
        dateField.addPropertyChangeListener ( this );
        updateExpectedFieldLength ();

        // UI elements actions
        field.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                setDate ( getDate ( field.getText () ), UpdateSource.field );
            }
        } );
        field.onFocusLoss ( new FocusEventRunnable ()
        {
            @Override
            public void run ( final FocusEvent e )
            {
                final Date date = getDate ( field.getText () );
                if ( !CompareUtils.equals ( date, dateField.getDate () ) )
                {
                    setDate ( date, UpdateSource.field );
                }
                else
                {
                    field.setText ( getText ( date ) );
                }
            }
        } );
        field.onKeyPress ( Hotkey.DOWN, new KeyEventRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                showDateChooserPopup ();
            }
        } );
        button.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                showDateChooserPopup ();
            }
        } );
    }

    /**
     * Uninstalls actions for UI elements.
     */
    protected void uninstallActions ()
    {
        dateField.removePropertyChangeListener ( this );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( CompareUtils.equals ( property, WebDateField.ALLOW_USER_INPUT_PROPERTY ) )
        {
            field.setEditable ( ( Boolean ) evt.getNewValue () );
        }
        if ( CompareUtils.equals ( property, WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            updateEnabledState ();
        }
        if ( CompareUtils.equals ( property, WebDateField.DATE_FORMAT_PROPERTY ) )
        {
            updateExpectedFieldLength ();
        }
        else if ( CompareUtils.equals ( property, WebDateField.DATE_PROPERTY ) )
        {
            setDate ( dateField.getDate (), UpdateSource.datefield );
        }
        else if ( CompareUtils.equals ( property, WebDateField.CALENDAR_CUSTOMIZER_PROPERTY ) )
        {
            customizeCalendar ();
        }
    }

    /**
     * Updates sub-components enabled state
     */
    protected void updateEnabledState ()
    {
        field.setEnabled ( dateField.isEnabled () );
        button.setEnabled ( dateField.isEnabled () );
    }

    /**
     * Updates expected text field length.
     */
    protected void updateExpectedFieldLength ()
    {
        final DateFormat dateFormat = dateField.getDateFormat ();
        field.setColumns ( dateFormat.format ( new Date () ).length () );
    }

    /**
     * Displays date chooser popup.
     */
    protected void showDateChooserPopup ()
    {
        if ( popup == null )
        {
            popup = new WebPopOver ( StyleId.datefieldPopup.at ( dateField ), dateField );
            popup.setCloseOnFocusLoss ( true );
            popup.setMovable ( false );

            calendar = new WebCalendar ( StyleId.datefieldCalendar.at ( popup ), dateField.getDate () );
            calendar.addDateSelectionListener ( new DateListener ()
            {
                @Override
                public void dateChanged ( final Date date )
                {
                    setDate ( date, UpdateSource.calendar );
                    popup.setVisible ( false );
                }
            } );
            popup.add ( calendar );

            customizeCalendar ();
        }
        popup.show ( dateField, PopOverDirection.down, PopOverAlignment.centered );
        popup.requestFocusInWindow ();
    }

    /**
     * Performs calendar customizations.
     */
    protected void customizeCalendar ()
    {
        if ( calendar != null && dateField.getCalendarCustomizer () != null )
        {
            dateField.getCalendarCustomizer ().customize ( calendar );
        }
    }

    /**
     * Sets currently selected date and updates component depending on update source.
     *
     * @param date   new selected date
     * @param source date update source
     */
    protected void setDate ( final Date date, final UpdateSource source )
    {
        if ( !updating )
        {
            // Mark to avoid consecutive updates
            updating = true;

            // Updating date field value
            if ( source != UpdateSource.datefield )
            {
                dateField.setDate ( date );
            }

            // Updating text value
            if ( source != UpdateSource.field )
            {
                // Simply updating text value and resetting caret position
                field.setText ( getText ( date ) );
                field.setCaretPosition ( 0 );
            }
            else
            {
                // Updating text value to fit date format and restoring caret position
                final int pos = field.getCaretPosition ();
                final String text = getText ( date );
                field.setText ( text );
                field.setCaretPosition ( pos <= text.length () ? pos : text.length () );
            }

            // Updating calendar value
            if ( source != UpdateSource.calendar && calendar != null )
            {
                calendar.setDate ( date );
            }

            // Resetting mark
            updating = false;
        }
    }

    /**
     * Returns date specified in text field.
     *
     * @param text text to retrieve date from
     * @return date specified in text field
     */
    protected Date getDate ( final String text )
    {
        try
        {
            final DateFormat format = dateField.getDateFormat ();
            if ( text != null && !text.trim ().equals ( "" ) )
            {
                return format.parse ( text );
            }
            else
            {
                return null;
            }
        }
        catch ( final Throwable ex )
        {
            return dateField.getDate ();
        }
    }

    /**
     * Returns text date representation according to date format.
     *
     * @param date date to retrieve text from
     * @return text date representation according to date format
     */
    protected String getText ( final Date date )
    {
        return date != null ? dateField.getDateFormat ().format ( date ) : "";
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( dateField );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( dateField, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( dateField, painter );
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
     * Returns date field painter.
     *
     * @return date field painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets date field painter.
     * Pass null to remove date field painter.
     *
     * @param painter new date field painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( dateField, new DataRunnable<IDateFieldPainter> ()
        {
            @Override
            public void run ( final IDateFieldPainter newPainter )
            {
                WebDateFieldUI.this.painter = newPainter;
            }
        }, this.painter, painter, IDateFieldPainter.class, AdaptiveDateFieldPainter.class );
    }

    /**
     * Paints date field.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, painter );
    }

    /**
     * This enumeration represents the type of source that caused view update.
     */
    protected enum UpdateSource
    {
        /**
         * Date field source.
         */
        datefield,

        /**
         * Text field source.
         */
        field,

        /**
         * Calendar source.
         */
        calendar
    }
}