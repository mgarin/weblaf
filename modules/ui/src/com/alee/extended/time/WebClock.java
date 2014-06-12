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

package com.alee.extended.time;

import com.alee.laf.label.WebLabel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.swing.WebTimer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This component can be used to provide time tracking visual feedback.
 * It can either display
 *
 * @author Mikle Garin
 */

// todo Rewrite this class, its really-really bad
public class WebClock extends WebLabel
{
    /**
     * Timer clock action listeners.
     */
    protected List<ActionListener> listeners = new ArrayList<ActionListener> ();

    /**
     * Date display format.
     */
    protected SimpleDateFormat dateFormat = new SimpleDateFormat ( "HH:mm:ss" );

    /**
     * Clock type.
     */
    protected ClockType clockType = ClockType.clock;

    /**
     * Time left counter.
     */
    protected long timeLeft = 0;

    /**
     * Initial time left counter.
     */
    protected long initialTimeLeft = 0;

    /**
     * Time updater.
     */
    protected WebTimer timer;

    public WebClock ()
    {
        super ();
        initClock ();
    }

    protected void initClock ()
    {
        this.timer = new WebTimer ( 100, new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( clockType.equals ( ClockType.stopwatch ) )
                {
                    timeLeft += 100;
                }
                else if ( clockType.equals ( ClockType.timer ) )
                {
                    timeLeft -= 100;
                    if ( timeLeft <= 1000 )
                    {
                        timeLeft = 0;
                        fireActionPerformed ();
                        timer.stop ();
                    }
                }
                updateTime ();
            }
        } );
    }

    protected void updateTime ()
    {
        if ( clockType.equals ( ClockType.stopwatch ) || clockType.equals ( ClockType.timer ) )
        {
            Calendar calendar = Calendar.getInstance ();
            calendar.set ( Calendar.YEAR, 0 );
            calendar.set ( Calendar.DAY_OF_YEAR, 1 );
            calendar.set ( Calendar.HOUR_OF_DAY, 0 );
            calendar.set ( Calendar.MINUTE, 0 );
            calendar.set ( Calendar.SECOND, 0 );
            calendar.set ( Calendar.MILLISECOND, 0 );
            calendar.setTimeInMillis ( calendar.getTimeInMillis () + timeLeft );
            setText ( dateFormat.format ( calendar.getTime () ) );
        }
        else
        {
            setText ( dateFormat.format ( System.currentTimeMillis () ) );
        }
    }

    public String getTimePattern ()
    {
        return dateFormat.toPattern ();
    }

    public void setTimePattern ( String timePattern )
    {
        this.dateFormat.applyPattern ( timePattern );
        updateTime ();
    }

    public ClockType getClockType ()
    {
        return clockType;
    }

    public void setClockType ( final ClockType clockType )
    {
        this.clockType = clockType;
        updateTime ();
    }

    public long getTimeLeft ()
    {
        return timeLeft;
    }

    public void setTimeLeft ( long timeLeft )
    {
        if ( clockType.equals ( ClockType.timer ) )
        {
            this.timeLeft = timeLeft;
            this.initialTimeLeft = timeLeft;
        }
        else
        {
            this.timeLeft = 0;
            this.initialTimeLeft = 0;
        }
        updateTime ();
    }

    public void start ()
    {
        this.timer.start ();
        updateTime ();
    }

    public void pause ()
    {
        this.timer.stop ();
        updateTime ();
    }

    public void stop ()
    {
        this.timer.stop ();
        this.timeLeft = initialTimeLeft;
        updateTime ();
    }

    public List<ActionListener> getActionListeners ()
    {
        return listeners;
    }

    public void addActionListener ( ActionListener timerFinished )
    {
        this.listeners.add ( timerFinished );
    }

    public void removeActionListener ( ActionListener timerFinished )
    {
        this.listeners.remove ( timerFinished );
    }

    public void fireActionPerformed ()
    {
        final ActionEvent actionEvent = new ActionEvent ( this, 0, "Timer finished" );
        for ( ActionListener timerFinished : CollectionUtils.copy ( listeners ) )
        {
            timerFinished.actionPerformed ( actionEvent );
        }
    }
}