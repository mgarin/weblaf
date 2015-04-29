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

package com.alee.utils;

import com.alee.managers.log.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class provides a set of utilities for time measurement and comparison.
 *
 * @author Mikle Garin
 */

public final class TimeUtils
{
    /**
     * Pinned time in milliseconds.
     */
    private static Long pinnedTime = null;

    /**
     * Last measured time in milliseconds.
     */
    private static Long lastTime = null;

    /**
     * Pinned nano time in nanoseconds.
     */
    private static Long pinnedNanoTime = null;

    /**
     * Last measured time in nanoseconds.
     */
    private static Long lastNanoTime = null;

    /**
     * Returns last pinned time.
     *
     * @return pinned time
     */
    public static Long getPinnedTime ()
    {
        return pinnedTime;
    }

    /**
     * Returns either last pinned time or last request time.
     *
     * @return last time
     */
    public static Long getLastTime ()
    {
        return lastTime;
    }

    /**
     * Pins current system time.
     */
    public static void pinTime ()
    {
        pinnedTime = currentTime ();
        lastTime = pinnedTime;
    }

    /**
     * Returns time passed since either last pin request or last time request.
     *
     * @return passed time
     */
    public static long getPassedTime ()
    {
        return getPassedTime ( false );
    }

    /**
     * Returns time passed since either last pin request or last time request if total is false.
     * Otherwise always returns time passed since last pin request.
     *
     * @param total should always return time passed since last pin request or not
     * @return passed time
     */
    public static long getPassedTime ( final boolean total )
    {
        if ( pinnedTime == null )
        {
            pinTime ();
            return 0;
        }
        else
        {
            final long time = currentTime ();
            final long passedTime = total ? time - pinnedTime : time - lastTime;
            lastTime = time;
            return passedTime;
        }
    }

    /**
     * Writes time passed since either last pin request or last time request to log.
     */
    public static void showPassedTime ()
    {
        showPassedTime ( false );
    }

    /**
     * Writes time passed since either last pin request or last time request to log with specified prefix.
     *
     * @param prefix output string prefix
     */
    public static void showPassedTime ( final String prefix )
    {
        showPassedTime ( false, prefix );
    }

    /**
     * Writes time passed since either last pin request or last time request if total is false to log.
     * Otherwise always writes time passed since last pin request.
     *
     * @param total should always write time passed since last pin request or not
     */
    public static void showPassedTime ( final boolean total )
    {
        showPassedTime ( total, "" );
    }

    /**
     * Writes time passed since either last pin request or last time request if total is false to log with specified prefix.
     * Otherwise always writes time passed since last pin request.
     *
     * @param total  should always write time passed since last pin request or not
     * @param prefix output string prefix
     */
    public static void showPassedTime ( final boolean total, final String prefix )
    {
        if ( pinnedTime == null )
        {
            Log.info ( TimeUtils.class, prefix + "0" );
            pinTime ();
        }
        else
        {
            final long time = currentTime ();
            Log.info ( TimeUtils.class, prefix + ( total ? time - pinnedTime : time - lastTime ) );
            lastTime = time;
        }
    }

    /**
     * Resets last pinned time and last request time.
     */
    public static void resetTime ()
    {
        pinnedTime = null;
        lastTime = null;
    }

    /**
     * Returns last pinned nanotime.
     *
     * @return pinned nanotime
     */
    public static Long getPinnedNanoTime ()
    {
        return pinnedNanoTime;
    }

    /**
     * Returns either last pinned nanotime or last request nanotime.
     *
     * @return last nanotime
     */
    public static Long getLastNanoTime ()
    {
        return lastNanoTime;
    }

    /**
     * Pins current system nanotime.
     */
    public static void pinNanoTime ()
    {
        pinnedNanoTime = currentNanoTime ();
        lastNanoTime = pinnedNanoTime;
    }

    /**
     * Returns nanotime passed since either last pin request or last nanotime request.
     *
     * @return passed nanotime
     */
    public static long getPassedNanoTime ()
    {
        return getPassedNanoTime ( false );
    }

    /**
     * Returns nanotime passed since either last pin request or last nanotime request if total is false.
     * Otherwise always returns nanotime passed since last pin request.
     *
     * @param total should always return time passed since last pin request or not
     * @return passed nanotime
     */
    public static long getPassedNanoTime ( final boolean total )
    {
        if ( pinnedNanoTime == null )
        {
            pinNanoTime ();
            return 0;
        }
        else
        {
            final long time = currentNanoTime ();
            final long passedTime = total ? time - pinnedNanoTime : time - lastNanoTime;
            lastNanoTime = time;
            return passedTime;
        }
    }

    /**
     * Writes nanotime passed since either last pin request or last nanotime request to log.
     */
    public static void showPassedNanoTime ()
    {
        showPassedNanoTime ( false );
    }

    /**
     * Writes nanotime passed since either last pin request or last nanotime request to log with specified prefix.
     *
     * @param prefix output string prefix
     */
    public static void showPassedNanoTime ( final String prefix )
    {
        showPassedNanoTime ( false, prefix );
    }

    /**
     * Writes nanotime passed since either last pin request or last nanotime request if total is false to log.
     * Otherwise always writes nanotime passed since last pin request.
     *
     * @param total should always write nanotime passed since last pin request or not
     */
    public static void showPassedNanoTime ( final boolean total )
    {
        showPassedNanoTime ( total, "" );
    }

    /**
     * Writes nanotime passed since either last pin request or last nanotime request if total is false to log with specified prefix.
     * Otherwise always writes nanotime passed since last pin request.
     *
     * @param total  should always write nanotime passed since last pin request or not
     * @param prefix output string prefix
     */
    public static void showPassedNanoTime ( final boolean total, final String prefix )
    {
        if ( pinnedNanoTime == null )
        {
            Log.info ( TimeUtils.class, prefix + "0" );
            pinNanoTime ();
        }
        else
        {
            final long time = currentNanoTime ();
            Log.info ( TimeUtils.class, prefix + ( total ? time - pinnedNanoTime : time - lastNanoTime ) );
            lastNanoTime = time;
        }
    }

    /**
     * Resets last pinned nanotime and last request nanotime.
     */
    public static void resetNanoTime ()
    {
        pinnedNanoTime = null;
        lastNanoTime = null;
    }

    /**
     * Returns current system time.
     *
     * @return system time
     */
    public static long currentTime ()
    {
        return System.currentTimeMillis ();
    }

    /**
     * Returns current system nano time.
     *
     * @return system nanotime
     */
    public static long currentNanoTime ()
    {
        return System.nanoTime ();
    }

    /**
     * Returns current system date.
     *
     * @return system date
     */
    public static Date currentDate ()
    {
        return new Date ( currentTime () );
    }

    /**
     * Returns true if both of the dates represent the same day.
     *
     * @param date1 first date
     * @param date2 second date
     * @return day comparison result
     */
    public static boolean isSameDay ( final Date date1, final Date date2 )
    {
        return isSameDay ( date1.getTime (), date2.getTime () );
    }

    /**
     * Returns true if both of the time represent the same day.
     *
     * @param time1 first time
     * @param time2 second time
     * @return day comparison result
     */
    public static boolean isSameDay ( final Long time1, final Long time2 )
    {
        final Calendar calendar = Calendar.getInstance ();

        // Saving calendar time
        final Date tmp = calendar.getTime ();

        calendar.setTimeInMillis ( time1 );
        final boolean same = isSameDay ( calendar, time2 );

        // Restoring calendar time
        calendar.setTime ( tmp );

        return same;
    }

    /**
     * Returns true if date contained in Calendar and specified date both represent the same day.
     *
     * @param calendar calendar
     * @param date     date
     * @return day comparison result
     */
    public static boolean isSameDay ( final Calendar calendar, final Date date )
    {
        return isSameDay ( calendar, date.getTime () );
    }

    /**
     * Returns true if date contained in Calendar and specified time both represent the same day.
     *
     * @param calendar calendar
     * @param date     date
     * @return day comparison result
     */
    public static boolean isSameDay ( final Calendar calendar, final Long date )
    {
        // Saving calendar time
        final long time = calendar.getTimeInMillis ();

        final int year = calendar.get ( Calendar.YEAR );
        final int month = calendar.get ( Calendar.MONTH );
        final int day = calendar.get ( Calendar.DAY_OF_MONTH );

        calendar.setTimeInMillis ( date );
        final boolean sameDay = year == calendar.get ( Calendar.YEAR ) &&
                month == calendar.get ( Calendar.MONTH ) &&
                day == calendar.get ( Calendar.DAY_OF_MONTH );

        // Restoring calendar time
        calendar.setTimeInMillis ( time );

        return sameDay;
    }

    /**
     * Returns start-of-day date.
     *
     * @param date date to process
     * @return start-of-day date
     */
    public static Date getStartOfDay ( final Date date )
    {
        final Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( date );
        calendar.set ( Calendar.HOUR_OF_DAY, 0 );
        calendar.set ( Calendar.MINUTE, 0 );
        calendar.set ( Calendar.SECOND, 0 );
        calendar.set ( Calendar.MILLISECOND, 1 );
        return calendar.getTime ();
    }

    /**
     * Returns end-of-day date.
     *
     * @param date date to process
     * @return end-of-day date
     */
    public static Date getEndOfDay ( final Date date )
    {
        final Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( date );
        calendar.set ( Calendar.HOUR_OF_DAY, 23 );
        calendar.set ( Calendar.MINUTE, 59 );
        calendar.set ( Calendar.SECOND, 59 );
        calendar.set ( Calendar.MILLISECOND, 999 );
        return calendar.getTime ();
    }

    /**
     * Increases calendar date by one day.
     *
     * @param calendar calendar that should be changed
     */
    public static void increaseByDay ( final Calendar calendar )
    {
        changeByDays ( calendar, 1 );
    }

    /**
     * Decreases calendar date by one day.
     *
     * @param calendar calendar that should be changed
     */
    public static void decreaseByDay ( final Calendar calendar )
    {
        changeByDays ( calendar, -1 );
    }

    /**
     * Changes calendar date by the specified days amount.
     *
     * @param calendar calendar that should be changed
     * @param days     days amount
     */
    public static void changeByDays ( final Calendar calendar, final int days )
    {
        calendar.set ( Calendar.DAY_OF_MONTH, calendar.get ( Calendar.DAY_OF_MONTH ) + days );
    }

    /**
     * Returns formatted representation of current date.
     *
     * @param format date format to use
     * @return formatted representation of current date
     */
    public static String formatCurrentDate ( final String format )
    {
        return formatDate ( format, new Date () );
    }

    /**
     * Returns formatted representation of specified date.
     *
     * @param format date format to use
     * @param date   date to format
     * @return formatted representation of specified date
     */
    public static String formatDate ( final String format, final Date date )
    {
        return new SimpleDateFormat ( format ).format ( date );
    }
}