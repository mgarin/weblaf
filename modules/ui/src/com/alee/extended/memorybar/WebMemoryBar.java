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

package com.alee.extended.memorybar;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.Orientation;
import com.alee.extended.WebComponent;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.MemoryUsage;

/**
 * Simple button-like component that displays Java application heap memory usage.
 * It can also call GC when user clicks on it if configured to allow that behavior.
 *
 * @author Mikle Garin
 */
public class WebMemoryBar extends WebComponent<WebMemoryBar, WMemoryBarUI<WebMemoryBar>>
{
    /**
     * Component properties.
     */
    public static final String MODEL_PROPERTY = "model";
    public static final String ORIENTATION_PROPERTY = "orientation";
    public static final String DISPLAY_MAXIMUM_MEMORY_PROPERTY = "displayMaximumMemory";
    public static final String DISPLAY_TOOL_TIP_PROPERTY = "displayToolTip";
    public static final String REFRESH_RATE_PROPERTY = "refreshRate";

    /**
     * {@link ActionListener} that tracks {@link ButtonModel} actions.
     */
    protected final ActionListener actionListener;

    /**
     * {@link ButtonModel} used by this {@link WebMemoryBar}.
     */
    protected ButtonModel model;

    /**
     * {@link Orientation}.
     */
    protected Orientation orientation;

    /**
     * Whether or not maximum memory is displayed.
     */
    protected boolean displayMaximumMemory;

    /**
     * Whether or not extended tooltip is displayed on hover.
     */
    protected boolean displayToolTip;

    /**
     * {@link MemoryUsage} refresh rate.
     */
    protected long refreshRate;

    /**
     * Constructs new {@link WebMemoryBar}.
     */
    public WebMemoryBar ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link WebMemoryBar}.
     *
     * @param gcEnabled whether or not GC action is enabled
     */
    public WebMemoryBar ( final boolean gcEnabled )
    {
        this ( StyleId.auto, gcEnabled );
    }

    /**
     * Constructs new {@link WebMemoryBar}.
     *
     * @param id {@link StyleId}
     */
    public WebMemoryBar ( @NotNull final StyleId id )
    {
        this ( id, true );
    }

    /**
     * Constructs new {@link WebMemoryBar}.
     *
     * @param id        {@link StyleId}
     * @param gcEnabled whether or not GC action is enabled
     */
    public WebMemoryBar ( @NotNull final StyleId id, final boolean gcEnabled )
    {
        this.actionListener = new ActionListener ()
        {
            @Override
            public void actionPerformed ( @NotNull final ActionEvent event )
            {
                doGC ();
            }
        };
        setModel ( new DefaultButtonModel () );
        setGCEnabled ( gcEnabled );
        setOrientation ( Orientation.horizontal );
        setDisplayMaximumMemory ( false );
        setDisplayToolTip ( true );
        setRefreshRate ( 1000L );
        updateUI ();
        setStyleId ( id );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.memorybar;
    }

    /**
     * Returns {@link ButtonModel} used by this {@link WebMemoryBar}.
     *
     * @return {@link ButtonModel} used by this {@link WebMemoryBar}
     */
    @NotNull
    public ButtonModel getModel ()
    {
        return model;
    }

    /**
     * Sets {@link ButtonModel} to be used by this {@link WebMemoryBar}.
     *
     * @param model {@link ButtonModel} to be used by this {@link WebMemoryBar}
     */
    public void setModel ( @NotNull final ButtonModel model )
    {
        if ( this.model != model )
        {
            final ButtonModel old = this.model;
            if ( old != null )
            {
                old.removeActionListener ( actionListener );
            }

            this.model = model;
            model.setEnabled ( old == null || old.isEnabled () );
            model.addActionListener ( actionListener );

            firePropertyChange ( MODEL_PROPERTY, old, model );
        }
    }

    /**
     * Returns whether or not GC action is enabled.
     *
     * @return {@code true} if GC action is enabled, {@code false} otherwise
     */
    public boolean isGCEnabled ()
    {
        return getModel ().isEnabled ();
    }

    /**
     * Sets whether or not GC action is enabled.
     *
     * @param enabled whether or not GC action is enabled
     */
    public void setGCEnabled ( final boolean enabled )
    {
        getModel ().setEnabled ( enabled );
    }

    /**
     * Returns memory bar {@link Orientation}.
     *
     * @return memory bar {@link Orientation}
     */
    @NotNull
    public Orientation getOrientation ()
    {
        return orientation != null ? orientation : Orientation.horizontal;
    }

    /**
     * Sets memory bar {@link Orientation}.
     *
     * @param orientation new memory bar {@link Orientation}
     */
    public void setOrientation ( @Nullable final Orientation orientation )
    {
        if ( this.orientation != orientation )
        {
            final Orientation old = this.orientation;
            this.orientation = orientation;
            firePropertyChange ( ORIENTATION_PROPERTY, old, orientation );
        }
    }

    /**
     * Returns whether or not maximum memory is displayed.
     *
     * @return {@code true} if maximum memory is displayed, {@code false} otherwise
     */
    public boolean isMaximumMemoryDisplayed ()
    {
        return displayMaximumMemory;
    }

    /**
     * Sets whether or not maximum memory should be displayed.
     *
     * @param displayMaximumMemory whether or not maximum memory should be displayed
     */
    public void setDisplayMaximumMemory ( final boolean displayMaximumMemory )
    {
        if ( this.displayMaximumMemory != displayMaximumMemory )
        {
            final boolean old = this.displayMaximumMemory;
            this.displayMaximumMemory = displayMaximumMemory;
            firePropertyChange ( DISPLAY_MAXIMUM_MEMORY_PROPERTY, old, displayMaximumMemory );
        }
    }

    /**
     * Returns whether or not extended tooltip is displayed on hover.
     *
     * @return {@code true} if extended tooltip is displayed on hover, {@code false} otherwise
     */
    public boolean isToolTipDisplayed ()
    {
        return displayToolTip;
    }

    /**
     * Sets whether or not extended tooltip should be displayed on hover.
     *
     * @param displayToolTip whether or not extended tooltip should be displayed on hover
     */
    public void setDisplayToolTip ( final boolean displayToolTip )
    {
        if ( this.displayToolTip != displayToolTip )
        {
            final boolean old = this.displayToolTip;
            this.displayToolTip = displayToolTip;
            firePropertyChange ( DISPLAY_TOOL_TIP_PROPERTY, old, displayToolTip );
        }
    }

    /**
     * Returns {@link MemoryUsage} refresh rate.
     *
     * @return {@link MemoryUsage} refresh rate
     */
    public long getRefreshRate ()
    {
        return refreshRate;
    }

    /**
     * Sets {@link MemoryUsage} refresh rate.
     *
     * @param refreshRate {@link MemoryUsage} refresh rate
     */
    public void setRefreshRate ( final long refreshRate )
    {
        if ( this.refreshRate != refreshRate )
        {
            final long old = this.refreshRate;
            this.refreshRate = refreshRate;
            firePropertyChange ( REFRESH_RATE_PROPERTY, old, refreshRate );
        }
    }

    /**
     * Returns currently displayed {@link MemoryUsage}.
     *
     * @return currently displayed {@link MemoryUsage}
     */
    @NotNull
    public MemoryUsage getMemoryUsage ()
    {
        return getUI ().getMemoryUsage ();
    }

    /**
     * Returns currently displayed text.
     *
     * @return currently displayed text
     */
    @NotNull
    public String getText ()
    {
        return LM.get ( getTextKey (), getTextData ( getMemoryUsage () ) );
    }

    /**
     * Returns language key for currently displayed text.
     *
     * @return language key for currently displayed text
     */
    @NotNull
    protected String getTextKey ()
    {
        return "weblaf.ex.memorybar.text";
    }

    /**
     * Returns language data for currently displayed text.
     *
     * @param memoryUsage {@link MemoryUsage}
     * @return language data for currently displayed text
     */
    @NotNull
    protected Object[] getTextData ( @NotNull final MemoryUsage memoryUsage )
    {
        return new Object[]{
                getMemorySizeString ( memoryUsage.getUsed () ),
                getMemorySizeString ( isMaximumMemoryDisplayed () ? memoryUsage.getMax () : memoryUsage.getCommitted () )
        };
    }

    /**
     * Returns language key for currently displayed tooltip.
     *
     * @return language key for currently displayed tooltip
     */
    @NotNull
    protected String getToolTipKey ()
    {
        return "weblaf.ex.memorybar.tooltip";
    }

    /**
     * Returns language data for currently displayed tooltip.
     *
     * @param memoryUsage {@link MemoryUsage}
     * @return language data for currently displayed tooltip
     */
    @NotNull
    protected Object[] getToolTipData ( @NotNull final MemoryUsage memoryUsage )
    {
        return new Object[]{
                getMemorySizeString ( memoryUsage.getCommitted () ),
                getMemorySizeString ( memoryUsage.getUsed () ),
                getMemorySizeString ( memoryUsage.getMax () )
        };
    }

    /**
     * Returns memory size {@link String}.
     *
     * @param used memory size
     * @return memory size {@link String}
     */
    @NotNull
    protected String getMemorySizeString ( final long used )
    {
        return FileUtils.getFileSizeString ( used, getDigits ( used ) );
    }

    /**
     * Returns amount of memory size digits to display after dot.
     *
     * @param size memory size
     * @return amount of memory size digits to display after dot
     */
    protected int getDigits ( final long size )
    {
        return size < FileUtils.GB ? 0 : 2;
    }

    /**
     * Performs GC action.
     */
    public void doGC ()
    {
        fireGCCalled ();
        System.gc ();
        getUI ().updateMemoryUsage ();
        fireGCCompleted ();
        fireActionPerformed ();
    }

    /**
     * Adds {@link ActionListener} for this {@link WebMemoryBar} GC action.
     *
     * @param listener {@link ActionListener} to add
     */
    public void addActionListener ( @NotNull final ActionListener listener )
    {
        listenerList.add ( ActionListener.class, listener );
    }

    /**
     * Removes {@link ActionListener} from this {@link WebMemoryBar} GC action.
     *
     * @param listener {@link ActionListener} to remove
     */
    public void removeActionListener ( @NotNull final ActionListener listener )
    {
        listenerList.remove ( ActionListener.class, listener );
    }

    /**
     * Informs all {@link ActionListener}s about performed action.
     */
    public void fireActionPerformed ()
    {
        ActionEvent event = null;
        for ( final ActionListener listener : listenerList.getListeners ( ActionListener.class ) )
        {
            if ( event == null )
            {
                event = new ActionEvent (
                        WebMemoryBar.this,
                        ActionEvent.ACTION_PERFORMED,
                        getModel ().getActionCommand (),
                        System.currentTimeMillis (),
                        CoreSwingUtils.getCurrentEventModifiers ()
                );
            }
            listener.actionPerformed ( event );
        }
    }

    /**
     * Adds {@link MemoryBarListener} for this {@link WebMemoryBar} GC action.
     *
     * @param listener {@link MemoryBarListener} to add
     */
    public void addMemoryBarListener ( @NotNull final MemoryBarListener listener )
    {
        listenerList.add ( MemoryBarListener.class, listener );
    }

    /**
     * Removes {@link MemoryBarListener} from this {@link WebMemoryBar} GC action.
     *
     * @param listener {@link MemoryBarListener} to remove
     */
    public void removeMemoryBarListener ( @NotNull final MemoryBarListener listener )
    {
        listenerList.remove ( MemoryBarListener.class, listener );
    }

    /**
     * Informs all {@link MemoryBarListener}s about GC action being invoked.
     */
    public void fireGCCalled ()
    {
        for ( final MemoryBarListener listener : listenerList.getListeners ( MemoryBarListener.class ) )
        {
            listener.gcCalled ( this );
        }
    }

    /**
     * Informs all {@link MemoryBarListener}s about GC action being completed.
     */
    public void fireGCCompleted ()
    {
        for ( final MemoryBarListener listener : listenerList.getListeners ( MemoryBarListener.class ) )
        {
            listener.gcCompleted ( this );
        }
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WMemoryBarUI} object that renders this component
     */
    public WMemoryBarUI getUI ()
    {
        return ( WMemoryBarUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WMemoryBarUI}
     */
    public void setUI ( final WMemoryBarUI ui )
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