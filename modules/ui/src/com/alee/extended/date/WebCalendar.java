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

import com.alee.extended.layout.TableLayout;
import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.transition.TransitionAdapter;
import com.alee.extended.transition.effects.Direction;
import com.alee.extended.transition.effects.slide.SlideTransitionEffect;
import com.alee.extended.transition.effects.slide.SlideType;
import com.alee.managers.style.StyleId;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TimeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This is a custom calendar component.
 *
 * @author Mikle Garin
 * @see WebDateField
 */

public class WebCalendar extends WebPanel
{
    /**
     * Used icons.
     */
    public static final ImageIcon previousSkipIcon = new ImageIcon ( WebCalendar.class.getResource ( "icons/previous_skip.png" ) );
    public static final ImageIcon previousIcon = new ImageIcon ( WebCalendar.class.getResource ( "icons/previous.png" ) );
    public static final ImageIcon nextIcon = new ImageIcon ( WebCalendar.class.getResource ( "icons/next.png" ) );
    public static final ImageIcon nextSkipIcon = new ImageIcon ( WebCalendar.class.getResource ( "icons/next_skip.png" ) );

    /**
     * Date selection listeners.
     */
    protected List<DateSelectionListener> dateSelectionListeners = new ArrayList<DateSelectionListener> ( 1 );

    /**
     * Calendar title format.
     * Usually displays currently visible month and year.
     */
    protected SimpleDateFormat titleFormat = new SimpleDateFormat ( "MMMM yyyy" );

    /**
     * Whether sunday should be the first day of week or not.
     */
    protected boolean startWeekFromSunday = false;

    /**
     * Whether should animate month transitions or not.
     */
    protected boolean animate = true;

    /**
     * Whether should perform horizontal slide animation or not.
     * todo Replace with transition panel customizer
     */
    protected boolean horizontalSlide = true;

    /**
     * Other month date buttons foreground.
     */
    protected Color otherMonthForeground = new Color ( 90, 90, 90 );

    /**
     * Current month date buttons foreground.
     */
    protected Color currentMonthForeground = Color.BLACK;

    /**
     * Weekends date buttons foreground.
     */
    protected Color weekendsForeground = new Color ( 160, 0, 0 );

    /**
     * Date buttons customizer.
     */
    protected DateCustomizer dateCustomizer;

    /**
     * Currently selected date.
     */
    protected Date date;

    /**
     * Displayed month date.
     */
    protected Date shownDate;

    /**
     * Previously displayed month date.
     * Used to perform animation in a specific direction.
     */
    protected Date oldShownDate;

    /**
     * UI components.
     */
    protected WebButton previousSkip;
    protected WebButton previous;
    protected WebLabel titleLabel;
    protected WebButton next;
    protected WebButton nextSkip;
    protected WebPanel weekHeaders;
    protected WebPanel monthDays;
    protected ComponentTransition monthDaysTransition;
    protected WebToggleButton lastSelectedDayButton;

    /**
     * Constructs new calendar without selected date.
     */
    public WebCalendar ()
    {
        this ( StyleId.calendar, null );
    }

    /**
     * Constructs new calendar with the specified selected date.
     *
     * @param date selected date
     */
    public WebCalendar ( final Date date )
    {
        this ( StyleId.calendar, date );
    }

    /**
     * Constructs new calendar without selected date.
     *
     * @param id style ID
     */
    public WebCalendar ( final StyleId id )
    {
        this ( id, null );
    }

    /**
     * Constructs new calendar with the specified selected date.
     *
     * @param id   style ID
     * @param date selected date
     */
    public WebCalendar ( final StyleId id, final Date date )
    {
        super ( id, new BorderLayout ( 0, 0 ) );
        putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );

        this.date = date != null ? new Date ( date.getTime () ) : null;
        this.shownDate = date != null ? new Date ( date.getTime () ) : new Date ();

        // Header panel
        final WebPanel header = new WebPanel ( StyleId.of ( StyleId.calendarHeaderPanel, this ) );
        add ( header, BorderLayout.NORTH );

        previousSkip = new WebButton ( StyleId.of ( StyleId.calendarPrevYearButton, header ), previousSkipIcon );
        previousSkip.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                changeYear ( -1 );
            }
        } );

        previous = new WebButton ( StyleId.of ( StyleId.calendarPrevMonthButton, header ), previousIcon );
        previous.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                changeMonth ( -1 );
            }
        } );

        final WebPanel leftHeader = new WebPanel ( new BorderLayout () );
        leftHeader.setOpaque ( false );
        leftHeader.add ( previousSkip, BorderLayout.WEST );
        leftHeader.add ( previous, BorderLayout.EAST );
        header.add ( leftHeader, BorderLayout.WEST );

        titleLabel = new WebLabel ( StyleId.of ( StyleId.calendarTitleLabel, header ) );
        titleLabel.setBoldFont ();
        updateTitleLabel ();
        header.add ( titleLabel, BorderLayout.CENTER );

        next = new WebButton ( StyleId.of ( StyleId.calendarNextMonthButton, header ), nextIcon );
        next.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                changeMonth ( 1 );
            }
        } );

        nextSkip = new WebButton ( StyleId.of ( StyleId.calendarNextYearButton, header ), nextSkipIcon );
        nextSkip.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                changeYear ( 1 );
            }
        } );

        final WebPanel rightHeader = new WebPanel ( new BorderLayout () );
        rightHeader.setOpaque ( false );
        rightHeader.add ( next, BorderLayout.WEST );
        rightHeader.add ( nextSkip, BorderLayout.EAST );
        header.add ( rightHeader, BorderLayout.EAST );

        // Week days
        final double f = TableLayout.FILL;
        final double p = TableLayout.PREFERRED;
        final double[] cols = { f, p, f, p, f, p, f, p, f, p, f, p, f };
        final double[] rows = { p };
        final StyleId weekHeadersId = StyleId.of ( StyleId.calendarWeekTitlesPanel, this );
        weekHeaders = new WebPanel ( weekHeadersId, new TableLayout ( new double[][]{ cols, rows } ) );
        add ( weekHeaders, BorderLayout.NORTH );
        updateWeekHeaders ();

        // Month days panel
        monthDays = createMonthPanel ();
        updateMonth ( monthDays );

        // Days panel transition
        monthDaysTransition = new ComponentTransition ( monthDays );
        monthDaysTransition.setOpaque ( false );
        monthDaysTransition.addTransitionListener ( new TransitionAdapter ()
        {
            @Override
            public void transitionFinished ()
            {
                requestFocusToSelected ();
            }
        } );
        add ( monthDaysTransition, BorderLayout.CENTER );
    }

    /**
     * Switches to new title label.
     * In case animation is disabled simply changes title text.
     */
    protected void updateTitleLabel ()
    {
        titleLabel.setText ( titleFormat.format ( shownDate ) );
    }

    /**
     * Switches view to new displayed month.
     *
     * @param animate whether should animate transition or not
     */
    protected void updateMonth ( final boolean animate )
    {
        // Even if someone is asking to animate transition we have to honor calendar settings
        // If it isn't set to be animated then we should never animate any transitions within it
        if ( animate && isAnimate () )
        {
            // Creating new dates panel
            monthDays = createMonthPanel ();
            updateMonth ( monthDays );

            // Setting collapse transition effects
            final boolean ltr = getComponentOrientation ().isLeftToRight ();

            // Transition effect
            final SlideTransitionEffect effect = new SlideTransitionEffect ();
            effect.setType ( SlideType.moveBoth );
            effect.setDirection ( oldShownDate.getTime () > shownDate.getTime () ? getNextDirection ( ltr ) : getPrevDirection ( ltr ) );
            effect.setSpeed ( 20 );
            monthDaysTransition.setTransitionEffect ( effect );

            // Starting animated transition
            monthDaysTransition.performTransition ( monthDays );
        }
        else
        {
            // Simply updating current dates panel
            updateMonth ( monthDays );
            requestFocusToSelected ();
        }
    }

    /**
     * Changes displayed month.
     *
     * @param change months change amount
     */
    protected void changeMonth ( int change )
    {
        // Reverse date change due to reversed orientation
        if ( !getComponentOrientation ().isLeftToRight () )
        {
            change = -change;
        }

        final Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( shownDate );
        calendar.set ( Calendar.DAY_OF_MONTH, 1 );
        calendar.set ( Calendar.MONTH, calendar.get ( Calendar.MONTH ) + change );
        setShownDate ( calendar.getTime () );
    }

    /**
     * Changes displayed year.
     *
     * @param change years change amount
     */
    protected void changeYear ( int change )
    {
        // Reverse date change due to reversed orientation
        if ( !getComponentOrientation ().isLeftToRight () )
        {
            change = -change;
        }

        final Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( shownDate );
        calendar.set ( Calendar.DAY_OF_MONTH, 1 );
        calendar.set ( Calendar.YEAR, calendar.get ( Calendar.YEAR ) + change );
        setShownDate ( calendar.getTime () );
    }

    /**
     * Creates and returns month panel.
     *
     * @return created month panel
     */
    protected WebPanel createMonthPanel ()
    {
        final double f = TableLayout.FILL;
        final double p = TableLayout.PREFERRED;
        final double[] cols = { f, p, f, p, f, p, f, p, f, p, f, p, f };
        final double[] rows = { f, f, f, f, f, f };
        return new WebPanel ( StyleId.of ( StyleId.calendarMonthPanel, this ), new TableLayout ( new double[][]{ cols, rows }, 0, 0 ) );
    }

    /**
     * Requests focus to last selected date button.
     */
    protected void requestFocusToSelected ()
    {
        if ( lastSelectedDayButton != null )
        {
            lastSelectedDayButton.requestFocusInWindow ();
        }
    }

    /**
     * Returns next month transition direction.
     *
     * @param ltr whether LTR orientation or not
     * @return next month transition direction
     */
    protected Direction getNextDirection ( final boolean ltr )
    {
        return horizontalSlide ? ltr ? Direction.right : Direction.left : Direction.up;
    }

    /**
     * Returns previous month transition direction.
     *
     * @param ltr whether LTR orientation or not
     * @return previous month transition direction
     */
    protected Direction getPrevDirection ( final boolean ltr )
    {
        return horizontalSlide ? ltr ? Direction.left : Direction.right : Direction.down;
    }

    /**
     * Updates week headers.
     */
    protected void updateWeekHeaders ()
    {
        weekHeaders.removeAll ();
        for ( int i = 1; i <= 7; i++ )
        {
            final int day = !startWeekFromSunday ? i : i == 1 ? 7 : i - 1;

            final StyleId dayOfWeekId = StyleId.of ( StyleId.calendarWeekTitleLabel, weekHeaders );
            final WebLabel dayOfWeekLabel = new WebLabel ( dayOfWeekId, "weblaf.ex.calendar.dayOfWeek." + day );
            dayOfWeekLabel.setFontSizeAndStyle ( 10, Font.BOLD );
            weekHeaders.add ( dayOfWeekLabel, ( i - 1 ) * 2 + ",0" );

            if ( i < 7 )
            {
                weekHeaders.add ( new WebSeparator ( WebSeparator.VERTICAL ), ( ( i - 1 ) * 2 + 1 ) + ",0" );
            }
        }
        weekHeaders.revalidate ();
    }

    /**
     * Updates displayed month date buttons.
     *
     * @param monthDays panel to update
     */
    protected void updateMonth ( final JPanel monthDays )
    {
        monthDays.removeAll ();
        lastSelectedDayButton = null;

        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "1,0,1,5" );
        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "3,0,3,5" );
        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "5,0,5,5" );
        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "7,0,7,5" );
        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "9,0,9,5" );
        monthDays.add ( new WebSeparator ( WebSeparator.VERTICAL ), "11,0,11,5" );

        final ButtonGroup dates = new ButtonGroup ();

        final Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( shownDate );
        calendar.set ( Calendar.DAY_OF_MONTH, 1 );

        int col = 0;
        int row = 0;

        // Month before
        final int dayOfWeek = calendar.get ( Calendar.DAY_OF_WEEK );
        final int shift;
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
            final StyleId dayId = StyleId.of ( StyleId.calendarPreviousMonthDateToggleButton, monthDays );
            final WebToggleButton day = new WebToggleButton ( dayId, "" + calendar.get ( Calendar.DAY_OF_MONTH ) );
            day.setForeground ( otherMonthForeground );
            day.addItemListener ( new ItemListener ()
            {
                @Override
                public void itemStateChanged ( final ItemEvent e )
                {
                    final WebToggleButton dayButton = ( WebToggleButton ) e.getSource ();
                    if ( dayButton.isSelected () )
                    {
                        setDateImpl ( thisDate );
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
            final StyleId dayId = StyleId.of ( StyleId.calendarCurrentMonthDateToggleButton, monthDays );
            final WebToggleButton day = new WebToggleButton ( dayId, "" + calendar.get ( Calendar.DAY_OF_MONTH ) );
            day.setForeground ( weekend ? weekendsForeground : currentMonthForeground );
            day.setSelected ( selected );
            day.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    lastSelectedDayButton = ( WebToggleButton ) e.getSource ();
                    setDateImpl ( thisDate );
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
        final int left = 6 * 7 - ( monthDays.getComponentCount () - 6 );
        for ( int i = 1; i <= left; i++ )
        {
            final Date thisDate = calendar.getTime ();
            final StyleId dayId = StyleId.of ( StyleId.calendarNextMonthDateToggleButton, monthDays );
            final WebToggleButton day = new WebToggleButton ( dayId, "" + calendar.get ( Calendar.DAY_OF_MONTH ) );
            day.setForeground ( otherMonthForeground );
            day.addItemListener ( new ItemListener ()
            {
                @Override
                public void itemStateChanged ( final ItemEvent e )
                {
                    final WebToggleButton dayButton = ( WebToggleButton ) e.getSource ();
                    if ( dayButton.isSelected () )
                    {
                        setDateImpl ( thisDate );
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

    /**
     * Returns title format.
     *
     * @return title format
     */
    public SimpleDateFormat getTitleFormat ()
    {
        return titleFormat;
    }

    /**
     * Sets title format.
     *
     * @param titleFormat title format
     */
    public void setTitleFormat ( final SimpleDateFormat titleFormat )
    {
        this.titleFormat = titleFormat;
        updateTitleLabel ();
    }

    /**
     * Returns currently selected date.
     *
     * @return currently selected date
     */
    public Date getDate ()
    {
        return date;
    }

    /**
     * Sets currently selected and displayed date.
     *
     * @param date date to select and display
     */
    public void setDate ( final Date date )
    {
        setDate ( date, isAnimate () );
    }

    /**
     * Sets currently selected and displayed date.
     *
     * @param date    date to select and display
     * @param animate whether should animate month transition or not
     */
    public void setDate ( final Date date, final boolean animate )
    {
        if ( !CompareUtils.equals ( this.date, date ) )
        {
            setDateImpl ( date, animate );
        }
    }

    /**
     * Sets currently selected and displayed date.
     *
     * @param date date to select and display
     */
    protected void setDateImpl ( final Date date )
    {
        setDateImpl ( date, isAnimate () );
    }

    /**
     * Sets currently selected and displayed date.
     *
     * @param date    date to select and display
     * @param animate whether should animate month transition or not
     */
    protected void setDateImpl ( final Date date, final boolean animate )
    {
        this.date = date;
        setShownDate ( date, animate );
        fireDateSelected ( date );
    }

    /**
     * Returns displayed month date.
     *
     * @return displayed month date
     */
    public Date getShownDate ()
    {
        return shownDate;
    }

    /**
     * Sets displayed month date.
     *
     * @param date displayed month date
     */
    public void setShownDate ( final Date date )
    {
        setShownDate ( date, isAnimate () );
    }

    /**
     * Sets displayed month date.
     *
     * @param date    displayed month date
     * @param animate whether should animate month transition or not
     */
    public void setShownDate ( Date date, final boolean animate )
    {
        // Do not allow displayed date to be null
        if ( date == null )
        {
            date = new Date ();
        }

        this.oldShownDate = this.shownDate;
        this.shownDate = date;

        final Calendar calendar = Calendar.getInstance ();

        calendar.setTime ( oldShownDate );
        final int oldMonth = calendar.get ( Calendar.MONTH );
        final int oldYear = calendar.get ( Calendar.YEAR );

        calendar.setTime ( date );
        final int newMonth = calendar.get ( Calendar.MONTH );
        final int newYear = calendar.get ( Calendar.YEAR );

        if ( oldMonth != newMonth || oldYear != newYear )
        {
            updateTitleLabel ();
            updateMonth ( animate );
        }
    }

    /**
     * Returns whether sunday should be the first day of week or not.
     *
     * @return true if sunday should be the first day of week, false otherwise
     */
    public boolean isStartWeekFromSunday ()
    {
        return startWeekFromSunday;
    }

    /**
     * Sets whether sunday should be the first day of week or not.
     *
     * @param startWeekFromSunday whether sunday should be the first day of week or not
     */
    public void setStartWeekFromSunday ( final boolean startWeekFromSunday )
    {
        this.startWeekFromSunday = startWeekFromSunday;
        updateWeekHeaders ();
        updateMonth ( monthDays );
    }

    /**
     * Returns whether should animate month transitions or not.
     *
     * @return true if should animate month transitions, false otherwise
     */
    public boolean isAnimate ()
    {
        return animate;
    }

    /**
     * Sets whether should animate month transitions or not.
     *
     * @param animate whether should animate month transitions or not
     */
    public void setAnimate ( final boolean animate )
    {
        this.animate = animate;
    }

    /**
     * Returns whether should perform horizontal slide animation or not.
     *
     * @return true if should perform horizontal slide animation, false otherwise
     */
    public boolean isHorizontalSlide ()
    {
        return horizontalSlide;
    }

    /**
     * Sets whether should perform horizontal slide animation or not.
     *
     * @param horizontalSlide whether should perform horizontal slide animation or not
     */
    public void setHorizontalSlide ( final boolean horizontalSlide )
    {
        this.horizontalSlide = horizontalSlide;
    }

    /**
     * Returns other month date buttons foreground.
     *
     * @return other month date buttons foreground
     */
    public Color getOtherMonthForeground ()
    {
        return otherMonthForeground;
    }

    /**
     * Sets other month date buttons foreground.
     *
     * @param color other month date buttons foreground
     */
    public void setOtherMonthForeground ( final Color color )
    {
        this.otherMonthForeground = color;
        updateMonth ( monthDays );
    }

    /**
     * Returns current month date buttons foreground.
     *
     * @return current month date buttons foreground
     */
    public Color getCurrentMonthForeground ()
    {
        return currentMonthForeground;
    }

    /**
     * Sets current month date buttons foreground.
     *
     * @param color current month date buttons foreground
     */
    public void setCurrentMonthForeground ( final Color color )
    {
        this.currentMonthForeground = color;
        updateMonth ( monthDays );
    }

    /**
     * Returns weekends date buttons foreground.
     *
     * @return weekends date buttons foreground
     */
    public Color getWeekendsForeground ()
    {
        return weekendsForeground;
    }

    /**
     * Sets weekends date buttons foreground.
     *
     * @param color weekends date buttons foreground
     */
    public void setWeekendsForeground ( final Color color )
    {
        this.weekendsForeground = color;
        updateMonth ( monthDays );
    }

    /**
     * Returns date buttons customizer.
     *
     * @return date buttons customizer
     */
    public DateCustomizer getDateCustomizer ()
    {
        return dateCustomizer;
    }

    /**
     * Sets date buttons customizer.
     *
     * @param dateCustomizer date buttons customizer
     */
    public void setDateCustomizer ( final DateCustomizer dateCustomizer )
    {
        this.dateCustomizer = dateCustomizer;
        updateMonth ( monthDays );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled ( final boolean enabled )
    {
        super.setEnabled ( enabled );
        SwingUtils.setEnabledRecursively ( this, enabled, true );
    }

    /**
     * Adds date selection listener.
     *
     * @param listener date selection listener
     */
    public void addDateSelectionListener ( final DateSelectionListener listener )
    {
        dateSelectionListeners.add ( listener );
    }

    /**
     * Removes date selection listener.
     *
     * @param listener date selection listener
     */
    public void removeDateSelectionListener ( final DateSelectionListener listener )
    {
        dateSelectionListeners.remove ( listener );
    }

    /**
     * Informs about date selection change.
     *
     * @param date new selected date
     */
    public void fireDateSelected ( final Date date )
    {
        for ( final DateSelectionListener listener : CollectionUtils.copy ( dateSelectionListeners ) )
        {
            listener.dateSelected ( date );
        }
    }
}