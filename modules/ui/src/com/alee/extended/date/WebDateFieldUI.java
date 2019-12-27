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
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.window.PopOverAlignment;
import com.alee.extended.window.PopOverDirection;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.text.WebFormattedTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.*;
import com.alee.painter.PainterSupport;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.extensions.FocusEventRunnable;
import com.alee.utils.swing.extensions.KeyEventRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;

/**
 * Custom UI for {@link WebDateField} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class WebDateFieldUI<C extends WebDateField> extends WDateFieldUI<C>
{
    /**
     * todo 1. Change popover to popup-based window
     * todo    Probably another variation of popover would be handy?
     */

    /**
     * Listeners.
     */
    protected transient PropertyChangeListener propertyChangeListener;
    protected transient FocusAdapter focusListener;

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
    protected boolean updating = false;

    /**
     * Returns an instance of the {@link WebDateFieldUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebDateFieldUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebDateFieldUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( dateField );

        // Creating date field UI
        installComponents ();
        installActions ();
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Destroying date field UI
        uninstallActions ();
        uninstallComponents ();

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( dateField );

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

        button = new WebButton ( StyleId.datefieldButton.at ( dateField ), Icons.calendar, Icons.calendarHover );
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

        if ( calendar != null )
        {
            calendar.resetStyleId ();
            calendar = null;
        }
        if ( popup != null )
        {
            popup.resetStyleId ();
            popup = null;
        }
        button.resetStyleId ();
        button = null;
        field.resetStyleId ();
        field = null;

        SwingUtils.removeHandlesEnableStateMark ( dateField );
    }

    /**
     * Installs actions for UI elements.
     */
    protected void installActions ()
    {
        // Date field property listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( @NotNull final PropertyChangeEvent evt )
            {
                final String property = evt.getPropertyName ();
                if ( Objects.equals ( property, WebDateField.ALLOW_USER_INPUT_PROPERTY ) )
                {
                    field.setEditable ( ( Boolean ) evt.getNewValue () );
                }
                else if ( Objects.equals ( property, WebLookAndFeel.ENABLED_PROPERTY ) )
                {
                    updateEnabledState ();
                }
                else if ( Objects.equals ( property, WebLookAndFeel.FONT_PROPERTY ) )
                {
                    field.setFont ( dateField.getFont () );
                }
                else if ( Objects.equals ( property, WebLookAndFeel.BACKGROUND_PROPERTY ) )
                {
                    field.setBackground ( dateField.getBackground () );
                }
                else if ( Objects.equals ( property, WebLookAndFeel.FOREGROUND_PROPERTY ) )
                {
                    field.setForeground ( dateField.getForeground () );
                }
                else if ( Objects.equals ( property, WebDateField.DATE_FORMAT_PROPERTY ) )
                {
                    setDate ( dateField.getDate (), UpdateSource.datefield );
                    updateExpectedFieldLength ();
                }
                else if ( Objects.equals ( property, WebDateField.DATE_PROPERTY ) )
                {
                    setDate ( dateField.getDate (), UpdateSource.datefield );
                }
                else if ( Objects.equals ( property, WebDateField.CALENDAR_CUSTOMIZER_PROPERTY ) )
                {
                    customizeCalendar ();
                }
            }
        };
        dateField.addPropertyChangeListener ( propertyChangeListener );
        updateExpectedFieldLength ();

        // Date field focus handling
        focusListener = new FocusAdapter ()
        {
            @Override
            public void focusGained ( @NotNull final FocusEvent e )
            {
                field.requestFocusInWindow ();
            }
        };
        dateField.addFocusListener ( focusListener );

        // UI elements actions
        field.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                setDate ( getDate ( field.getText () ), UpdateSource.field );
            }
        } );
        field.onFocusLoss ( new FocusEventRunnable ()
        {
            @Override
            public void run ( @NotNull final FocusEvent e )
            {
                // Date from the text field
                final Date fieldDate = getDate ( field.getText () );
                final String fieldDateText = getText ( fieldDate );

                // Stored model date
                final Date modelDate = dateField.getDate ();
                final String modelDateText = getText ( modelDate );

                // Comparing date texts instead of dates
                // This is important to avoid issues with milliseconds precision when text is parsed into date
                // If we compare dates - they might be different, but visually they would be exactly same within the field
                if ( Objects.notEquals ( fieldDateText, modelDateText ) )
                {
                    setDate ( fieldDate, UpdateSource.field );
                }
                else
                {
                    field.setText ( fieldDateText );
                }
            }
        } );
        field.onKeyPress ( Hotkey.DOWN, new KeyEventRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
            {
                showDateChooserPopup ();
                e.consume ();
            }
        } );
        button.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent e )
            {
                showDateChooserPopup ();
            }
        } );
    }

    /**
     * Uninstalls actions from UI elements.
     */
    protected void uninstallActions ()
    {
        // Date field focus handling
        dateField.removeFocusListener ( focusListener );
        focusListener = null;

        // Date field property listener
        dateField.removePropertyChangeListener ( propertyChangeListener );
        propertyChangeListener = null;
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
     * todo Probably it is worth using minimum size at some point?
     */
    protected void updateExpectedFieldLength ()
    {
        final DateFormat dateFormat = dateField.getDateFormat ();
        final String sampleDateText = dateFormat.format ( new Date () );

        final Insets insets = field.getInsets ();
        final FontMetrics fm = field.getFontMetrics ( field.getFont () );
        final int stringWidth = fm.stringWidth ( sampleDateText );

        field.setPreferredWidth ( insets.left + stringWidth + 5 + insets.right );
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
                public void dateChanged ( @Nullable final Date date )
                {
                    if ( !updating )
                    {
                        setDate ( date, UpdateSource.calendar );
                        popup.setVisible ( false );
                    }
                }
            } );
            calendar.registerKeyboardAction ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( @NotNull final ActionEvent e )
                {
                    popup.setVisible ( false );
                }
            }, Hotkey.ESCAPE.getKeyStroke (), JComponent.WHEN_IN_FOCUSED_WINDOW );
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
    protected void setDate ( @Nullable final Date date, @NotNull final UpdateSource source )
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

            // Passing focus into the field
            if ( source == UpdateSource.calendar )
            {
                field.setCaretPosition ( field.getText ().length () );
                field.requestFocusInWindow ();
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
    @Nullable
    protected Date getDate ( @Nullable final String text )
    {
        Date date;
        try
        {
            if ( text != null && !text.trim ().equals ( "" ) )
            {
                date = dateField.getDateFormat ().parse ( text );
            }
            else
            {
                date = null;
            }
        }
        catch ( final Exception ex )
        {
            date = dateField.getDate ();
        }
        return date;
    }

    /**
     * Returns text date representation according to date format.
     *
     * @param date date to retrieve text from
     * @return text date representation according to date format
     */
    @NotNull
    protected String getText ( @Nullable final Date date )
    {
        return date != null ? dateField.getDateFormat ().format ( date ) : "";
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        // todo Requires proper support
        return PainterSupport.getBaseline ( c, this, width, height );
    }

    @NotNull
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        // todo Requires proper support
        return PainterSupport.getBaselineResizeBehavior ( c, this );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c );
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