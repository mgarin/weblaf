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

import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.rootpane.WebWindow;
import com.alee.laf.text.WebFormattedTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.settings.SettingsMethods;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mgarin Date: 03.11.11 Time: 10:24
 */

public class WebDateField extends WebFormattedTextField implements ShapeProvider, SettingsMethods
{
    public static final ImageIcon selectDateIcon = new ImageIcon ( WebDateField.class.getResource ( "icons/date.png" ) );

    private List<DateSelectionListener> dateSelectionListeners = new ArrayList<DateSelectionListener> ();

    private SimpleDateFormat dateFormat = new SimpleDateFormat ( "dd.MM.yyyy" );
    private Date date = null;

    private int preferredWidth = -1;

    private WebButton popupButton;

    private WebWindow popup;
    private WebCalendar calendar;

    public WebDateField ()
    {
        this ( null );
    }

    public WebDateField ( boolean drawBorder )
    {
        this ( drawBorder, null );
    }

    public WebDateField ( Date initialDate )
    {
        this ( WebDateFieldStyle.drawBorder, initialDate );
    }

    public WebDateField ( boolean drawBorder, Date initialDate )
    {
        super ();

        this.date = initialDate;

        // Basic field settings
        setOpaque ( false );
        setWebColored ( WebDateFieldStyle.webColored );
        setDrawBackground ( WebDateFieldStyle.drawBackground );
        setBackground ( WebDateFieldStyle.backgroundColor );
        setWebColored ( WebDateFieldStyle.webColored );
        setDrawFocus ( WebDateFieldStyle.drawFocus );

        // Popup button
        popupButton = WebButton.createIconWebButton ( selectDateIcon, WebDateFieldStyle.round );
        popupButton.setFocusable ( false );
        popupButton.setShadeWidth ( 0 );
        popupButton.setMoveIconOnPress ( false );
        popupButton.setRolloverDecoratedOnly ( true );
        popupButton.setCursor ( Cursor.getDefaultCursor () );
        popupButton.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                showCalendarPopup ();
            }
        } );
        setTrailingComponent ( popupButton );

        // Actions
        addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                updateDateFromField ();
            }
        } );
        addMouseListener ( new MouseAdapter ()
        {
            public void mousePressed ( MouseEvent e )
            {
                if ( isEnabled () && SwingUtilities.isRightMouseButton ( e ) )
                {
                    showCalendarPopup ();
                }
            }
        } );
        addFocusListener ( new FocusAdapter ()
        {
            public void focusLost ( FocusEvent e )
            {
                updateDateFromField ();
            }
        } );
        addKeyListener ( new KeyAdapter ()
        {
            public void keyReleased ( KeyEvent e )
            {
                if ( isEnabled () )
                {
                    if ( Hotkey.ESCAPE.isTriggered ( e ) )
                    {
                        updateFieldFromDate ();
                    }
                    else if ( Hotkey.DOWN.isTriggered ( e ) )
                    {
                        showCalendarPopup ();
                    }
                }
            }
        } );
        addAncestorListener ( new AncestorListener ()
        {
            public void ancestorAdded ( AncestorEvent event )
            {
                hideCalendarPopup ();
            }

            public void ancestorRemoved ( AncestorEvent event )
            {
                hideCalendarPopup ();
            }

            public void ancestorMoved ( AncestorEvent event )
            {
                hideCalendarPopup ();
            }
        } );
        addComponentListener ( new ComponentAdapter ()
        {
            public void componentHidden ( ComponentEvent e )
            {
                hideCalendarPopup ();
            }
        } );

        // Initial field date
        updateFieldFromDate ();

        // Initial styling settings
        setDrawBorder ( drawBorder );
        setRound ( WebDateFieldStyle.round );
        setShadeWidth ( WebDateFieldStyle.shadeWidth );
    }

    public void setRound ( int round )
    {
        super.setRound ( round );
        popupButton.setRound ( round );
    }

    public void setDrawBorder ( boolean drawBorder )
    {
        super.setDrawBorder ( drawBorder );
        updateMargin ();
    }

    private void updateMargin ()
    {
        setMargin ( isDrawBorder () ? WebDateFieldStyle.margin : WebDateFieldStyle.undecoratedMargin );
    }

    private void showCalendarPopup ()
    {
        // Checking that component is eligable for focus request
        if ( !requestFocusInWindow () && !isFocusOwner () )
        {
            // Cancel operation if component is not eligable for focus yet
            // This might occur if some other component input verifier holds the focus or in some other rare cases
            return;
        }

        // Update date from field if it was changed
        updateDateFromField ();

        // Create popup if it doesn't exist
        if ( popup == null || calendar == null )
        {
            Window ancestor = SwingUtils.getWindowAncestor ( this );

            // Calendar
            calendar = new WebCalendar ( date );
            calendar.setDrawFocus ( false );
            calendar.setRound ( StyleConstants.smallRound );
            calendar.setShadeWidth ( 0 );

            // Popup window
            popup = new WebWindow ( ancestor );
            popup.setLayout ( new BorderLayout () );
            popup.add ( calendar );
            popup.setCloseOnFocusLoss ( true );
            popup.pack ();

            // Make popup background transparent
            SwingUtils.setWindowOpaque ( popup, false );

            // Correct popup positioning
            updatePopupLocation ();
            ancestor.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, new PropertyChangeListener ()
            {
                public void propertyChange ( PropertyChangeEvent evt )
                {
                    if ( popup.isShowing () )
                    {
                        updatePopupLocation ();
                    }
                }
            } );

            // Selection listener
            calendar.addDateSelectionListener ( new DateSelectionListener ()
            {
                public void dateSelected ( Date date )
                {
                    hideCalendarPopup ();
                    setDate ( date );
                    requestFocusInWindow ();
                }
            } );
        }
        else
        {
            // Updating window location
            updatePopupLocation ();

            // todo Allow null date in WebCalendar
            // Updating date
            calendar.setDate ( date != null ? date : new Date (), false );
        }

        // Applying orientation to popup
        SwingUtils.copyOrientation ( WebDateField.this, popup );

        // Showing popup and changing focus
        popup.setVisible ( true );
        calendar.requestFocusInWindow ();
    }

    private void hideCalendarPopup ()
    {
        popup.setVisible ( false );
    }

    private void updatePopupLocation ()
    {
        final Point los = WebDateField.this.getLocationOnScreen ();
        final Rectangle gb = popup.getGraphicsConfiguration ().getBounds ();
        final int shadeWidth = isDrawBorder () ? getShadeWidth () : 0;
        final boolean ltr = WebDateField.this.getComponentOrientation ().isLeftToRight ();
        final int w = WebDateField.this.getWidth ();
        final int h = WebDateField.this.getHeight ();

        final int x;
        if ( ltr )
        {
            if ( los.x + shadeWidth + popup.getWidth () <= gb.x + gb.width )
            {
                x = los.x + shadeWidth;
            }
            else
            {
                x = los.x + w - shadeWidth - popup.getWidth ();
            }
        }
        else
        {
            if ( los.x + w - shadeWidth - popup.getWidth () >= gb.x )
            {
                x = los.x + w - shadeWidth - popup.getWidth ();
            }
            else
            {
                x = los.x + shadeWidth;
            }
        }

        final int y;
        if ( los.y + h + popup.getHeight () <= gb.y + gb.height )
        {
            y = los.y + h + ( isDrawBorder () ? 0 : 1 );
        }
        else
        {
            y = los.y - popup.getHeight () - ( isDrawBorder () ? 0 : 1 );
        }

        popup.setLocation ( x, y );
    }

    private void updateDateFromField ()
    {
        setDate ( getDateFromField () );
    }

    private Date getDateFromField ()
    {
        try
        {
            String text = getText ();
            if ( text != null && !text.trim ().equals ( "" ) )
            {
                return dateFormat.parse ( text );
            }
            else
            {
                return null;
            }
        }
        catch ( Throwable ex )
        {
            return date;
        }
    }

    private void updateFieldFromDate ()
    {
        setText ( getTextDate () );
    }

    private String getTextDate ()
    {
        return date != null ? dateFormat.format ( date ) : "";
    }

    public Date getDate ()
    {
        return date;
    }

    public void setDate ( Date date )
    {
        if ( !CompareUtils.equals ( this.date, date ) )
        {
            this.date = date;
            fireDateSelected ( date );
        }
        updateFieldFromDate ();
    }

    public SimpleDateFormat getDateFormat ()
    {
        return dateFormat;
    }

    public void setDateFormat ( SimpleDateFormat dateFormat )
    {
        this.dateFormat = dateFormat;
        updateFieldFromDate ();
    }

    public void setEnabled ( boolean enabled )
    {
        super.setEnabled ( enabled );
        popupButton.setEnabled ( enabled );
    }

    public WebButton getPopupButton ()
    {
        return popupButton;
    }

    public void addDateSelectionListener ( DateSelectionListener dateSelectionListener )
    {
        dateSelectionListeners.add ( dateSelectionListener );
    }

    public void removeDateSelectionListener ( DateSelectionListener dateSelectionListener )
    {
        dateSelectionListeners.remove ( dateSelectionListener );
    }

    private void fireDateSelected ( Date date )
    {
        for ( DateSelectionListener listener : CollectionUtils.copy ( dateSelectionListeners ) )
        {
            listener.dateSelected ( date );
        }
    }

    public void setPreferredWidth ( int preferredWidth )
    {
        this.preferredWidth = preferredWidth;
    }

    public int getPreferredWidth ()
    {
        return preferredWidth;
    }

    public Dimension getPreferredSize ()
    {
        Dimension ps = super.getPreferredSize ();
        if ( preferredWidth != -1 )
        {
            ps.width = preferredWidth;
        }
        return ps;
    }
}