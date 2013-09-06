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

import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.transition.TransitionAdapter;
import com.alee.extended.transition.effects.Direction;
import com.alee.extended.transition.effects.fade.FadeTransitionEffect;
import com.alee.extended.transition.effects.slide.SlideTransitionEffect;
import com.alee.extended.transition.effects.slide.SlideType;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TimeUtils;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User: mgarin Date: 01.11.11 Time: 14:56
 */

public class WebCalendar extends WebPanel
{
    public static final ImageIcon previousSkipIcon = new ImageIcon ( WebCalendar.class.getResource ( "icons/previous_skip.png" ) );
    public static final ImageIcon previousIcon = new ImageIcon ( WebCalendar.class.getResource ( "icons/previous.png" ) );
    public static final ImageIcon nextIcon = new ImageIcon ( WebCalendar.class.getResource ( "icons/next.png" ) );
    public static final ImageIcon nextSkipIcon = new ImageIcon ( WebCalendar.class.getResource ( "icons/next_skip.png" ) );

    private List<DateSelectionListener> dateSelectionListeners = new ArrayList<DateSelectionListener> ( 1 );

    private SimpleDateFormat titleFormat = new SimpleDateFormat ( "MMMM yyyy" );
    private Date date = new Date ();
    private Date shownDate = new Date ();
    private Date oldShownDate = new Date ( System.currentTimeMillis () );

    private boolean startWeekFromSunday = false;

    private boolean animate = true;
    private boolean horizontalSlide = true;

    private DateCustomizer dateCustomizer = null;

    private Color otherMonthColor = new Color ( 90, 90, 90 );
    private Color weekendsColor = new Color ( 160, 0, 0 );

    private WebButton previousSkip;
    private WebButton previous;
    private ComponentTransition titlePanel;
    private WebButton next;
    private WebButton nextSkip;

    private WebPanel weekHeaders;

    private WebPanel monthDays;
    private ComponentTransition monthDaysTransition;

    private WebToggleButton lastSelectedDayButton;

    public WebCalendar ()
    {
        this ( null );
    }

    public WebCalendar ( Date date )
    {
        super ( true );

        this.date = date != null ? new Date ( date.getTime () ) : null;
        this.shownDate = date != null ? new Date ( date.getTime () ) : new Date ();

        setDrawFocus ( true );
        setRound ( StyleConstants.smallRound );
        setLayout ( new BorderLayout ( 0, 0 ) );
        putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );

        // Main layout
        WebPanel centerPanel = new WebPanel ();
        centerPanel.setOpaque ( false );
        add ( centerPanel, BorderLayout.CENTER );


        // Header panel
        WebPanel header = new WebPanel ();
        header.setOpaque ( false );
        add ( header, BorderLayout.NORTH );

        previousSkip = WebButton.createIconWebButton ( previousSkipIcon, StyleConstants.smallRound, true );
        previousSkip.setDrawFocus ( false );
        previousSkip.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                changeYear ( -1 );
            }
        } );

        previous = WebButton.createIconWebButton ( previousIcon, StyleConstants.smallRound, true );
        previous.setDrawFocus ( false );
        previous.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                changeMonth ( -1 );
            }
        } );

        WebPanel leftHeader = new WebPanel ( new BorderLayout () );
        leftHeader.setOpaque ( false );
        leftHeader.add ( previousSkip, BorderLayout.WEST );
        leftHeader.add ( previous, BorderLayout.EAST );
        header.add ( leftHeader, BorderLayout.WEST );

        titlePanel = new ComponentTransition ( createTitleLabel () );
        titlePanel.setOpaque ( false );
        titlePanel.setTransitionEffect ( new FadeTransitionEffect () );
        titlePanel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) )
                {
                    setShownDate ( new Date (), true );
                }
            }
        } );
        header.add ( titlePanel, BorderLayout.CENTER );

        next = WebButton.createIconWebButton ( nextIcon, StyleConstants.smallRound, true );
        next.setDrawFocus ( false );
        next.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                changeMonth ( 1 );
            }
        } );

        nextSkip = WebButton.createIconWebButton ( nextSkipIcon, StyleConstants.smallRound, true );
        nextSkip.setDrawFocus ( false );
        nextSkip.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                changeYear ( 1 );
            }
        } );

        WebPanel rightHeader = new WebPanel ( new BorderLayout () );
        rightHeader.setOpaque ( false );
        rightHeader.add ( next, BorderLayout.WEST );
        rightHeader.add ( nextSkip, BorderLayout.EAST );
        header.add ( rightHeader, BorderLayout.EAST );


        // Week days
        weekHeaders = new WebPanel ();
        weekHeaders.setUndecorated ( false );
        weekHeaders.setDrawSides ( true, false, true, false );
        weekHeaders.setShadeWidth ( 0 );
        weekHeaders.setOpaque ( false );
        weekHeaders.setMargin ( StyleConstants.shadeWidth, StyleConstants.shadeWidth - 1, StyleConstants.shadeWidth + 1,
                StyleConstants.shadeWidth - 1 );
        weekHeaders.setLayout ( new TableLayout ( new double[][]{
                { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED,
                        TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL,
                        TableLayout.PREFERRED, TableLayout.FILL }, { TableLayout.PREFERRED } } ) );
        centerPanel.add ( weekHeaders, BorderLayout.NORTH );
        updateWeekHeaders ();

        // Month days
        monthDays = createMonthDaysPanel ();
        updateMonthDays ( monthDays );
        monthDaysTransition = new ComponentTransition ( monthDays );
        monthDaysTransition.setOpaque ( false );
        monthDaysTransition.addTransitionListener ( new TransitionAdapter ()
        {
            @Override
            public void transitionFinished ()
            {
                // Retrieve focus to selected date if it is visible
                requestFocusToSelected ();
            }
        } );
        centerPanel.add ( monthDaysTransition, BorderLayout.CENTER );

        // todo Enter hotkey
        // Date selection by enter
        //        HotkeyManager.registerHotkey ( Hotkey.ENTER, new HotkeyRunnable ()
        //        {
        //            @Override
        //            public void run ( KeyEvent e )
        //            {
        //                fireDateSelected ( WebCalendar.this.date );
        //            }
        //        } );
    }

    private WebLabel createTitleLabel ()
    {
        return createTitleLabel ( titleFormat.format ( shownDate ) );
    }

    private WebLabel createTitleLabel ( String title )
    {
        WebLabel titleLabel = new WebLabel ( title );
        titleLabel.setBoldFont ();
        titleLabel.setDrawShade ( true );
        titleLabel.setHorizontalAlignment ( WebLabel.CENTER );
        titleLabel.setVerticalAlignment ( WebLabel.CENTER );
        return titleLabel;
    }

    private void changeMonth ( int change )
    {
        // Reverese date change due to reversed orientation
        if ( !WebCalendar.this.getComponentOrientation ().isLeftToRight () )
        {
            change = -change;
        }

        Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( shownDate );
        calendar.set ( Calendar.MONTH, calendar.get ( Calendar.MONTH ) + change );
        setShownDate ( calendar.getTime (), true );
    }

    private void changeYear ( int change )
    {
        // Reverese date change due to reversed orientation
        if ( !WebCalendar.this.getComponentOrientation ().isLeftToRight () )
        {
            change = -change;
        }

        Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( shownDate );
        calendar.set ( Calendar.YEAR, calendar.get ( Calendar.YEAR ) + change );
        setShownDate ( calendar.getTime (), true );
    }

    private WebPanel createMonthDaysPanel ()
    {
        WebPanel monthDays = new WebPanel ();
        monthDays.setOpaque ( false );
        monthDays.setMargin ( StyleConstants.shadeWidth - 1, StyleConstants.shadeWidth - 1, StyleConstants.shadeWidth - 1,
                StyleConstants.shadeWidth - 1 );
        final TableLayout layout = new TableLayout ( new double[][]{
                { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED,
                        TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL,
                        TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL } } );
        layout.setHGap ( 0 );
        layout.setVGap ( 0 );
        monthDays.setLayout ( layout );
        return monthDays;
    }

    private void switchMonthDays ( boolean animate )
    {
        if ( this.animate && animate )
        {
            // Creating new dates panel
            WebPanel newMonthDays = createMonthDaysPanel ();
            updateMonthDays ( newMonthDays );

            // Setting collapse transition effects
            boolean ltr = WebCalendar.this.getComponentOrientation ().isLeftToRight ();

            // Transition effect
            SlideTransitionEffect effect = new SlideTransitionEffect ();
            effect.setType ( SlideType.moveBoth );
            effect.setDirection ( oldShownDate.getTime () > shownDate.getTime () ? getNextDirection ( ltr ) : getPrevDirection ( ltr ) );
            effect.setSpeed ( 20 );
            monthDaysTransition.setTransitionEffect ( effect );

            // Starting animated transition
            monthDaysTransition.performTransition ( newMonthDays );
        }
        else
        {
            // Simply updating current dates panel
            updateMonthDays ( monthDays );
            requestFocusToSelected ();
        }
    }

    private void requestFocusToSelected ()
    {
        if ( lastSelectedDayButton != null )
        {
            lastSelectedDayButton.requestFocusInWindow ();
        }
    }

    private Direction getNextDirection ( boolean ltr )
    {
        if ( horizontalSlide )
        {
            return ltr ? Direction.right : Direction.left;
        }
        else
        {
            return Direction.up;
        }
    }

    private Direction getPrevDirection ( boolean ltr )
    {
        if ( horizontalSlide )
        {
            return ltr ? Direction.left : Direction.right;
        }
        else
        {
            return Direction.down;
        }
    }

    private void updateMonthDays ( JPanel monthDays )
    {
        monthDays.removeAll ();
        lastSelectedDayButton = null;

        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "1,0,1,5" );
        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "3,0,3,5" );
        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "5,0,5,5" );
        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "7,0,7,5" );
        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "9,0,9,5" );
        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "11,0,11,5" );

        ButtonGroup dates = new ButtonGroup ();

        Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( shownDate );
        calendar.set ( Calendar.DAY_OF_MONTH, 1 );

        int col = 0;
        int row = 0;

        // Month before
        int dayOfWeek = calendar.get ( Calendar.DAY_OF_WEEK );
        int shift;
        switch ( dayOfWeek )
        {
            case Calendar.MONDAY:
                shift = startWeekFromSunday ? 1 : 7;
                break;
            case Calendar.TUESDAY:
                shift = startWeekFromSunday ? 2 : 1;
                break;
            case Calendar.WEDNESDAY:
                shift = startWeekFromSunday ? 3 : 2;
                break;
            case Calendar.THURSDAY:
                shift = startWeekFromSunday ? 4 : 3;
                break;
            case Calendar.FRIDAY:
                shift = startWeekFromSunday ? 5 : 4;
                break;
            case Calendar.SATURDAY:
                shift = startWeekFromSunday ? 6 : 5;
                break;
            case Calendar.SUNDAY:
                shift = startWeekFromSunday ? 7 : 6;
                break;
            default:
                shift = 0;
                break;
        }
        TimeUtils.changeByDays ( calendar, -shift );
        while ( calendar.get ( Calendar.DAY_OF_MONTH ) > 1 )
        {
            final Date thisDate = calendar.getTime ();
            final WebToggleButton day = new WebToggleButton ();
            day.setFont ( day.getFont ().deriveFont ( Font.PLAIN ) );
            day.setForeground ( otherMonthColor );
            day.setText ( "" + calendar.get ( Calendar.DAY_OF_MONTH ) );
            day.setRolloverDecoratedOnly ( true );
            day.setHorizontalAlignment ( WebButton.RIGHT );
            day.setRound ( StyleConstants.smallRound );
            day.setDrawFocus ( false );
            day.addItemListener ( new ItemListener ()
            {
                @Override
                public void itemStateChanged ( ItemEvent e )
                {
                    if ( day.isSelected () )
                    {
                        setDateImpl ( thisDate, animate );
                    }
                }
            } );
            if ( dateCustomizer != null )
            {
                dateCustomizer.customize ( day, thisDate );
            }
            monthDays.add ( day, col * 2 + "," + row );
            dates.add ( day );

            TimeUtils.increaseByDay ( calendar );

            col++;
            if ( col > 6 )
            {
                col = 0;
                row++;
            }
        }

        // Current month
        do
        {
            final boolean weekend = calendar.get ( Calendar.DAY_OF_WEEK ) == 1 || calendar.get ( Calendar.DAY_OF_WEEK ) == 7;
            final boolean selected = date != null && TimeUtils.isSameDay ( calendar, date.getTime () );

            final Date thisDate = calendar.getTime ();
            final WebToggleButton day = new WebToggleButton ();
            day.setForeground ( weekend ? weekendsColor : Color.BLACK );
            day.setText ( "" + calendar.get ( Calendar.DAY_OF_MONTH ) );
            day.setSelected ( selected );
            day.setRolloverDecoratedOnly ( true );
            day.setHorizontalAlignment ( WebButton.RIGHT );
            day.setRound ( StyleConstants.smallRound );
            day.setDrawFocus ( false );
            day.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( ActionEvent e )
                {
                    lastSelectedDayButton = day;
                    setDateImpl ( thisDate, animate );
                }
            } );
            if ( dateCustomizer != null )
            {
                dateCustomizer.customize ( day, thisDate );
            }
            monthDays.add ( day, col * 2 + "," + row );
            dates.add ( day );

            if ( selected )
            {
                lastSelectedDayButton = day;
            }

            TimeUtils.increaseByDay ( calendar );

            col++;
            if ( col > 6 )
            {
                col = 0;
                row++;
            }
        }
        while ( calendar.get ( Calendar.DAY_OF_MONTH ) > 1 );

        // Month after
        int left = 6 * 7 - ( monthDays.getComponentCount () - 6 );
        for ( int i = 1; i <= left; i++ )
        {
            final Date thisDate = calendar.getTime ();
            final WebToggleButton day = new WebToggleButton ();
            day.setFont ( day.getFont ().deriveFont ( Font.PLAIN ) );
            day.setForeground ( otherMonthColor );
            day.setText ( "" + calendar.get ( Calendar.DAY_OF_MONTH ) );
            day.setRolloverDecoratedOnly ( true );
            day.setHorizontalAlignment ( WebButton.RIGHT );
            day.setRound ( StyleConstants.smallRound );
            day.setDrawFocus ( false );
            day.addItemListener ( new ItemListener ()
            {
                @Override
                public void itemStateChanged ( ItemEvent e )
                {
                    if ( day.isSelected () )
                    {
                        setDateImpl ( thisDate, animate );
                    }
                }
            } );
            if ( dateCustomizer != null )
            {
                dateCustomizer.customize ( day, thisDate );
            }
            monthDays.add ( day, col * 2 + "," + row );
            dates.add ( day );

            TimeUtils.increaseByDay ( calendar );

            col++;
            if ( col > 6 )
            {
                col = 0;
                row++;
            }
        }

        monthDays.revalidate ();
    }

    private void updateWeekHeaders ()
    {
        weekHeaders.removeAll ();
        for ( int i = 1; i <= 7; i++ )
        {
            int day = startWeekFromSunday ? ( i == 1 ? 7 : i - 1 ) : i;
            WebLabel dayOfWeekLabel = new WebLabel ();
            dayOfWeekLabel.setLanguage ( "weblaf.ex.calendar.dayOfWeek." + day );
            dayOfWeekLabel.setDrawShade ( true );
            dayOfWeekLabel.setHorizontalAlignment ( WebLabel.CENTER );
            dayOfWeekLabel.setFont ( dayOfWeekLabel.getFont ().deriveFont ( 10f ).deriveFont ( Font.BOLD ) );
            weekHeaders.add ( dayOfWeekLabel, ( i - 1 ) * 2 + ",0" );

            if ( i < 7 )
            {
                weekHeaders.add ( new WebSeparator ( WebSeparator.VERTICAL ), ( ( i - 1 ) * 2 + 1 ) + ",0" );
            }
        }
        weekHeaders.revalidate ();
    }

    private void updateTitleLabel ()
    {
        titlePanel.performTransition ( createTitleLabel () );
    }

    public Date getDate ()
    {
        return date;
    }

    public void setDate ( Date date )
    {
        setDate ( date, animate );
    }

    public void setDate ( Date date, boolean animate )
    {
        if ( !CompareUtils.equals ( this.date, date ) )
        {
            setDateImpl ( date, animate );
        }
    }

    private void setDateImpl ( Date date, boolean animate )
    {
        this.date = date;
        setShownDate ( date, animate );
        fireDateSelected ( date );
    }

    public Date getShownDate ()
    {
        return shownDate;
    }

    public void setShownDate ( Date shownDate, boolean animate )
    {
        this.oldShownDate = this.shownDate;
        this.shownDate = shownDate;

        Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( oldShownDate );
        int oldMonth = calendar.get ( Calendar.MONTH );
        int oldYear = calendar.get ( Calendar.YEAR );
        calendar.setTime ( shownDate );
        int newMonth = calendar.get ( Calendar.MONTH );
        int newYear = calendar.get ( Calendar.YEAR );
        if ( oldMonth != newMonth || oldYear != newYear )
        {
            updateTitleLabel ();
            switchMonthDays ( animate );
        }
    }

    public boolean isStartWeekFromSunday ()
    {
        return startWeekFromSunday;
    }

    public void setStartWeekFromSunday ( boolean startWeekFromSunday )
    {
        this.startWeekFromSunday = startWeekFromSunday;
        updateWeekHeaders ();
        updateMonthDays ( monthDays );
    }

    public boolean isAnimate ()
    {
        return animate;
    }

    public void setAnimate ( boolean animate )
    {
        this.animate = animate;
    }

    public boolean isHorizontalSlide ()
    {
        return horizontalSlide;
    }

    public void setHorizontalSlide ( boolean horizontalSlide )
    {
        this.horizontalSlide = horizontalSlide;
    }

    public Color getOtherMonthColor ()
    {
        return otherMonthColor;
    }

    public void setOtherMonthColor ( Color otherMonthColor )
    {
        this.otherMonthColor = otherMonthColor;
        updateMonthDays ( monthDays );
    }

    public Color getWeekendsColor ()
    {
        return weekendsColor;
    }

    public void setWeekendsColor ( Color weekendsColor )
    {
        this.weekendsColor = weekendsColor;
        updateMonthDays ( monthDays );
    }

    public DateCustomizer getDateCustomizer ()
    {
        return dateCustomizer;
    }

    public void setDateCustomizer ( DateCustomizer dateCustomizer )
    {
        this.dateCustomizer = dateCustomizer;
        updateMonthDays ( monthDays );
    }

    @Override
    public void setEnabled ( boolean enabled )
    {
        SwingUtils.setEnabledRecursively ( this, enabled, true );
        super.setEnabled ( enabled );
    }

    public void addDateSelectionListener ( DateSelectionListener listener )
    {
        dateSelectionListeners.add ( listener );
    }

    public void removeDateSelectionListener ( DateSelectionListener listener )
    {
        dateSelectionListeners.remove ( listener );
    }

    public void fireDateSelected ( Date date )
    {
        for ( DateSelectionListener listener : CollectionUtils.copy ( dateSelectionListeners ) )
        {
            listener.dateSelected ( date );
        }
    }
}