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

import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Objects;
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
import com.alee.utils.ImageUtils;
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
public class WebDateFieldUI<C extends WebDateField> extends WDateFieldUI<C> implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * todo 1. Change popover to popup-based window
     * todo    Probably another variation of popover would be handy?
     */

    /**
     * Component painter.
     */
    @DefaultPainter ( DateFieldPainter.class )
    protected IDateFieldPainter painter;

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
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDateFieldUI ();
    }

    @Override
    public void installUI ( final JComponent c )
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
    public void uninstallUI ( final JComponent c )
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
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                final String property = evt.getPropertyName ();
                if ( Objects.equals ( property, WebDateField.ALLOW_USER_INPUT_PROPERTY ) )
                {
                    field.setEditable ( ( Boolean ) evt.getNewValue () );
                }
                if ( Objects.equals ( property, WebLookAndFeel.ENABLED_PROPERTY ) )
                {
                    updateEnabledState ();
                }
                if ( Objects.equals ( property, WebDateField.DATE_FORMAT_PROPERTY ) )
                {
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
            public void focusGained ( final FocusEvent e )
            {
                field.requestFocusInWindow ();
            }
        };
        dateField.addFocusListener ( focusListener );

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
            public void run ( final KeyEvent e )
            {
                showDateChooserPopup ();
                e.consume ();
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
            calendar.registerKeyboardAction ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
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
        catch ( final Exception ex )
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
    public Shape getShape ()
    {
        return PainterSupport.getShape ( dateField, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( dateField, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( dateField, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( dateField );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( dateField, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( dateField );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( dateField, padding );
    }

    /**
     * Returns date field painter.
     *
     * @return date field painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets date field painter.
     * Pass null to remove date field painter.
     *
     * @param painter new date field painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( dateField, new Consumer<IDateFieldPainter> ()
        {
            @Override
            public void accept ( final IDateFieldPainter newPainter )
            {
                WebDateFieldUI.this.painter = newPainter;
            }
        }, this.painter, painter, IDateFieldPainter.class, AdaptiveDateFieldPainter.class );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        // todo Requires proper support
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        // todo Requires proper support
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
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
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
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