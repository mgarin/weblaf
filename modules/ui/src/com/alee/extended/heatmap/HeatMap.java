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

package com.alee.extended.heatmap;

import com.alee.extended.magnifier.MagnifierGlass;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.*;
import com.alee.utils.concurrent.DaemonThreadFactory;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Simple UI painting speed visualizer.
 * You can instantiate it and call upon {@link #display(Component)} to run visualization over the component's window.
 *
 * @author Mikle Garin
 */

public class HeatMap extends JComponent
{
    /**
     * {@link HeatMap} layer on {@link JLayeredPane}.
     */
    protected static final Integer HEAT_MAP_LAYER = 10000;

    /**
     * {@link HeatMap} intensity colors.
     */
    protected static final Color[] HEAT_COLORS = new Color[]{
            new Color ( 0, 0, 255, 32 ),
            new Color ( 0, 255, 255, 32 ),
            new Color ( 0, 255, 0, 32 ),
            new Color ( 255, 255, 0, 32 ),
            new Color ( 255, 0, 0, 32 )
    };

    /**
     * {@link HeatMap} data display mode.
     *
     * @see Mode
     */
    protected Mode mode;

    /**
     * Delay between realtime {@link HeatMap} updates.
     */
    protected long updateDeplay;

    /**
     * Size of each separate {@link HeatMap} sector.
     * It is only meaningful for {@link Mode#grid}.
     */
    protected Dimension sectorSize;

    /**
     * Whether or not sector time metrics should be displayed.
     * Metrics currently only include sector rendering time.
     */
    protected boolean displayTimeMetrics;

    /**
     * Resize listener for {@link JRootPane}.
     * It tracks size changes to update {@link HeatMap} accordingly.
     */
    protected transient ComponentAdapter resizeListener;

    /**
     * {@link HeatMap} buffer image.
     * It can be quite big depending on benchmarked area.
     */
    protected transient BufferedImage buffer;

    /**
     * Rendering buffer image used to calculate rendering time.
     * It is normally quite small and will only have size of one sector.
     */
    protected transient BufferedImage renderer;

    /**
     * Separate image used to render output onto {@link #buffer}.
     * This helps to avoid excessive rendering times on the main buffer.
     */
    protected transient BufferedImage merger;

    /**
     * {@link HeatMap} buffer image updates scheduler.
     * Used for realtime buffer image updates only.
     */
    protected transient final WebTimer updater;

    /**
     * {@link JRootPane} this {@link HeatMap} is displayed on.
     */
    protected transient JRootPane rootPane;

    /**
     * Special marker that is set whenever rendering measurement is ongoing.
     * It is used to avoid calculating painting time of this {@link HeatMap} in resulting times.
     */
    protected transient volatile boolean measuring;

    /**
     * Special {@link ExecutorService} for {@link HeatMap} buffer image updates.
     * It will only execute one update at a time and will only keep one update in the queue at a time.
     */
    protected transient final ExecutorService EXECUTOR = new ThreadPoolExecutor (
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable> ( 1 ),
            new DaemonThreadFactory ( "HeatMap" ),
            new ThreadPoolExecutor.DiscardOldestPolicy ()
    );

    /**
     * Constructs new {@link HeatMap} that can display UI bottlenecks.
     */
    public HeatMap ()
    {
        super ();

        // Heat map should never be opaque
        setOpaque ( false );

        // Heat map is initially disabled
        setEnabled ( false );

        // Initial settings
        this.mode = Mode.grid;
        this.updateDeplay = 250L;
        this.sectorSize = new Dimension ( 40, 40 );
        this.displayTimeMetrics = true;

        // Forceful updater
        updater = new WebTimer ( updateDeplay, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Updating heat map
                updateHeatMap ();
            }
        } );
        updater.setUseEventDispatchThread ( false );
        updater.setUseDaemonThread ( true );
        updater.setRepeats ( false );

        // Layered pane resize listener
        resizeListener = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                // Updating heat map
                updateHeatMap ();
            }
        };
    }

    /**
     * Returns {@link HeatMap} data display mode.
     *
     * @return {@link HeatMap} data display mode
     * @see Mode
     */
    public Mode getMode ()
    {
        return mode;
    }

    /**
     * Sets {@link HeatMap} data display mode.
     *
     * @param mode new {@link HeatMap} data display mode
     * @see Mode
     */
    public void setMode ( final Mode mode )
    {
        this.mode = mode;
        updateHeatMap ();
    }

    /**
     * Returns delay between realtime {@link HeatMap} updates.
     *
     * @return delay between realtime {@link HeatMap} updates
     */
    public long getUpdateDeplay ()
    {
        return updateDeplay;
    }

    /**
     * Sets delay between realtime {@link HeatMap} updates.
     *
     * @param delay new delay between realtime {@link HeatMap} updates
     */
    public void setUpdateDeplay ( final long delay )
    {
        this.updateDeplay = delay;
        updateHeatMap ();
    }

    /**
     * Returns size of each separate {@link HeatMap} sector.
     * It is only meaningful for {@link Mode#grid}.
     *
     * @return size of each separate {@link HeatMap} sector
     */
    public Dimension getSectorSize ()
    {
        return sectorSize;
    }

    /**
     * Sets size of each separate {@link HeatMap} sector.
     * It is only meaningful for {@link Mode#grid}.
     *
     * @param size size of each separate {@link HeatMap} sector
     */
    public void setSectorSize ( final Dimension size )
    {
        this.sectorSize = size;
        updateHeatMap ();
    }

    /**
     * Returns whether or not sector time metrics are displayed.
     * Metrics currently only include sector rendering time.
     *
     * @return {@code true} if sector time metrics are displayed, {@code false} otherwise
     */
    public boolean isDisplayTimeMetrics ()
    {
        return displayTimeMetrics;
    }

    /**
     * Sets whether or not sector time metrics should be displayed.
     * Metrics currently only include sector rendering time.
     *
     * @param display whether or not sector time metrics should be displayed
     */
    public void setDisplayTimeMetrics ( final boolean display )
    {
        this.displayTimeMetrics = display;
        updateHeatMap ();
    }

    /**
     * Performs {@link HeatMap} {@link #buffer} update according to {@link HeatMap} current state.
     */
    protected void updateHeatMap ()
    {
        EXECUTOR.submit ( new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( isDisplayed () )
                {
                    // Repainting heat map
                    repaintHeatMap ();

                    // Re-scheduling updater
                    updater.setDelay ( updateDeplay );
                    updater.restart ();
                }
                else
                {
                    // Disposing heat map
                    disposeHeatMap ();

                    // Stopping updater
                    updater.stop ();
                }
            }
        } );
    }

    /**
     * Performs full {@link HeatMap} buffer repaint.
     * This is a heavy operation that takes quite a while to complete.
     */
    protected void repaintHeatMap ()
    {
        // Checking renderer existence and size validity
        if ( renderer == null || renderer.getWidth () != sectorSize.width || renderer.getHeight () != sectorSize.height )
        {
            // Cleanup renderer image
            if ( renderer != null )
            {
                renderer.flush ();
                renderer = null;
            }

            // Create new renderer image
            // It is important to make it OPAQUE to match font rendering speed
            // todo Practically speaking it should depend on window transparency settings
            renderer = ImageUtils.createCompatibleImage ( sectorSize.width, sectorSize.height, Transparency.OPAQUE );
        }

        // Renderer image graphics
        final Graphics2D rendererGraphics = renderer.createGraphics ();
        rendererGraphics.setClip ( 0, 0, sectorSize.width, sectorSize.height );

        // Measuring separate sectors
        final JLayeredPane layeredPane = rootPane.getLayeredPane ();
        final Dimension size = layeredPane.getSize ();
        final int w = size.width / sectorSize.width + 1;
        final int h = size.height / sectorSize.height + 1;
        final long[][] times = new long[ w ][ h ];
        long min = 0;
        long max = 0;
        for ( int x = 0; x < w; x++ )
        {
            for ( int y = 0; y < h; y++ )
            {
                // Additional break to avoid pointless operations
                if ( !isDisplayed () )
                {
                    return;
                }

                // Measuring single sector rendering
                long time = benchmarkSector ( layeredPane, rendererGraphics, x * sectorSize.width, y * sectorSize.height );

                // Extra measurement for extraordinary results
                // This is important to reduce random factor of GC time kicking in
                // This will basically rerender any element that extraordinarily long time
                if ( min != max && time - min > ( max - min ) * 1.4 )
                {
                    // Measuring same sector rendering again for checked results
                    time = benchmarkSector ( layeredPane, rendererGraphics, x * sectorSize.width, y * sectorSize.height );
                }

                // Saving rendering time
                times[ x ][ y ] = time;

                // Checking min and max times
                min = Math.min ( min, time );
                max = Math.max ( max, time );
            }
        }

        // Checking buffer existence and size validity
        if ( buffer == null || buffer.getWidth () != size.width || buffer.getHeight () != size.height )
        {
            // Cleanup buffer image
            if ( buffer != null )
            {
                buffer.flush ();
            }

            // Create new buffer image
            buffer = ImageUtils.createCompatibleImage ( size.width, size.height, Transparency.TRANSLUCENT );
        }


        // Buffer image graphics
        final Graphics2D bufferGraphics = buffer.createGraphics ();
        bufferGraphics.setClip ( 0, 0, size.width, size.height );
        bufferGraphics.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC ) );

        // Checking merger existence and size validity
        if ( merger == null || merger.getWidth () != sectorSize.width || merger.getHeight () != sectorSize.height )
        {
            // Cleanup merger image
            if ( merger != null )
            {
                merger.flush ();
                merger = null;
            }

            // Create new merger image
            // It is important to make it TRANSLUCENT so it stays non-opaque
            merger = ImageUtils.createCompatibleImage ( sectorSize.width, sectorSize.height, Transparency.TRANSLUCENT );
        }

        final Graphics2D m2d = merger.createGraphics ();
        m2d.setFont ( new Font ( "Tahoma", Font.PLAIN, 9 ) );
        m2d.setBackground ( new Color ( 255, 255, 255, 0 ) );
        m2d.setClip ( 0, 0, sectorSize.width, sectorSize.height );

        // Updaing displayed buffer
        final DecimalFormat df = new DecimalFormat ( "0.00" );
        final FontMetrics fm = m2d.getFontMetrics ();
        final int ty = sectorSize.height / 2 + fm.getAscent () / 2;
        for ( int x = 0; x < w; x++ )
        {
            for ( int y = 0; y < h; y++ )
            {
                // Additional break to avoid pointless operations
                if ( !isDisplayed () )
                {
                    return;
                }

                final long time = times[ x ][ y ];

                // Drawing sector heat
                final Color color = getHeatColor ( min, max, time );
                m2d.setPaint ( color );
                m2d.clearRect ( 0, 0, sectorSize.width, sectorSize.height );
                m2d.fillRect ( 0, 0, sectorSize.width, sectorSize.height );

                // Drawing sector rendering time
                if ( displayTimeMetrics )
                {
                    final String ms = df.format ( ( double ) time / 1000000 );
                    final int tx = sectorSize.width / 2 - fm.stringWidth ( ms ) / 2;
                    m2d.setPaint ( Color.BLACK );
                    m2d.drawString ( ms, tx, ty );
                }

                bufferGraphics.drawImage ( merger, x * sectorSize.width, y * sectorSize.height, null );
            }
        }
        m2d.dispose ();
        bufferGraphics.dispose ();

        // Updating heat map location
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                final Rectangle b = new Rectangle ( 0, 0, size.width, size.height );
                if ( !getBounds ().equals ( b ) )
                {
                    setBounds ( b );
                }
                repaint ();
            }
        } );
    }

    /**
     * Disposes {@link HeatMap} buffer and any rendering resources.
     * This is only done upon {@link HeatMap} becoming hidden so it doesn't keep any unnecessary resources.
     */
    protected void disposeHeatMap ()
    {
        // Cleanup buffer image
        if ( buffer != null )
        {
            buffer.flush ();
            buffer = null;
        }

        // Cleanup renderer image
        if ( renderer != null )
        {
            renderer.flush ();
            renderer = null;
        }

        // Cleanup merger image
        if ( merger != null )
        {
            merger.flush ();
            merger = null;
        }
    }

    /**
     * Performs {@link JComponent} sector benchmarking.
     * This will simply paint small {@link JComponent} sector on the provided {@link Graphics2D} and measure operation time.
     * Painting operation is performed within EDT to ensure we do not mess anything up within {@link JComponent}.
     *
     * @param component component to use for benchmarking
     * @param g2d       graphics to use for benchmarking
     * @param x         sector X coordinate
     * @param y         sector Y coordinate
     * @return returns resulting sector time
     */
    protected long benchmarkSector ( final JComponent component, final Graphics2D g2d, final int x, final int y )
    {
        try
        {
            // Performing and waiting for rendering in EDT
            // This is necessary to ensure we do not cause any damage for the component's painting flow
            final long[] time = new long[]{ 0 };
            CoreSwingUtils.invokeAndWait ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    // Put measurment process marker
                    // This will allow us to ignore heat map painting speed in calculations
                    // It can be involved since we are painting layered pane where it usually is placed
                    measuring = true;

                    // Clearing graphics to avoid affecting render time with previous artifacts
                    g2d.clearRect ( 0, 0, sectorSize.width, sectorSize.height );

                    // Measuring rendering time
                    g2d.translate ( -x, -y );
                    time[ 0 ] = System.nanoTime ();
                    component.paintAll ( g2d );
                    time[ 0 ] = System.nanoTime () - time[ 0 ];
                    g2d.translate ( x, y );

                    // Reset measurment process marker
                    measuring = false;
                }
            }, false );
            return time[ 0 ];
        }
        catch ( final Exception e )
        {
            // Throw a separate exception
            throw new UtilityException ( "Unable to render sector: " + x + "," + y, e );
        }
        finally
        {
            // Reset measurment process marker
            // It is needed to properly reset state upon exception
            measuring = false;
        }
    }

    /**
     * Returns heat color for the specified sector parameters.
     *
     * @param min   overall minimum value
     * @param max   overall maximum value
     * @param value sector value
     * @return heat color for the specified sector parameters
     */
    protected Color getHeatColor ( final long min, final long max, final long value )
    {
        final float progress = ( float ) ( value - min ) / ( max - min );
        final int floor = ( int ) Math.round ( Math.floor ( ( HEAT_COLORS.length - 1 ) * progress ) );
        final int ceil = ( int ) Math.round ( Math.ceil ( ( HEAT_COLORS.length - 1 ) * progress ) );
        return ColorUtils.intermediate ( HEAT_COLORS[ floor ], HEAT_COLORS[ ceil ], ( HEAT_COLORS.length - 1 ) * progress - floor );
    }

    /**
     * Paints {@link HeatMap} when it is available and displayed.
     * Simple placeholder background is painted whenever {@link HeatMap} is displayed but not yet available.
     */
    @Override
    protected void paintComponent ( final Graphics g )
    {
        // We only paint something when heat map is displayed
        // Also we do not paint anything if we are within measurement call
        if ( isDisplayed () && !measuring )
        {
            if ( buffer != null )
            {
                // Painting buffer image
                g.drawImage ( buffer, 0, 0, null );
            }
            else
            {
                // Painting simple placeholder background
                g.setColor ( HEAT_COLORS[ 0 ] );
                g.fillRect ( 0, 0, getWidth (), getHeight () );
            }
        }
    }

    /**
     * Initializes or disposes {@link HeatMap} on the {@link Window} where specified component is located.
     *
     * @param component {@link Component} to determine {@link Window} for {@link HeatMap}
     */
    public void displayOrDispose ( final Component component )
    {
        if ( !isDisplayed () )
        {
            display ( component );
        }
        else
        {
            dispose ();
        }
    }

    /**
     * Initializes {@link HeatMap} on the {@link Window} where specified component is located.
     *
     * @param component {@link Component} to determine {@link Window} for {@link HeatMap}
     */
    public void display ( final Component component )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Performing various checks
        if ( component == null )
        {
            // Component must be provided
            throw new IllegalArgumentException ( "Provided component must not be null" );
        }
        if ( !component.isShowing () )
        {
            // Checking that component is currently displayed
            throw new IllegalArgumentException ( "Provided component is not displayed on screen: " + component );
        }
        if ( CoreSwingUtils.getRootPane ( component ) == null )
        {
            // Checking rootpane existence
            throw new IllegalArgumentException ( "Provided component is not placed within any window: " + component );
        }

        // Retrieving JRootPane
        rootPane = CoreSwingUtils.getRootPane ( component );
        rootPane.addComponentListener ( resizeListener );

        // Displaying heat map
        displayOnLayeredPane ();

        // Updating heat map
        updateHeatMap ();
    }

    /**
     * Display {@link MagnifierGlass} on the glass pane.
     */
    protected void displayOnLayeredPane ()
    {
        if ( !isDisplayed () )
        {
            // Enabling heat map
            setEnabled ( true );

            // Adding heat map to layered pane
            final JLayeredPane layeredPane = rootPane.getLayeredPane ();
            layeredPane.add ( this, HEAT_MAP_LAYER );

            // Updating heat map bounds
            final Dimension size = layeredPane.getSize ();
            final Rectangle b = new Rectangle ( 0, 0, size.width, size.height );
            if ( !getBounds ().equals ( b ) )
            {
                setBounds ( b );
            }

            // Updating view
            layeredPane.revalidate ();
            layeredPane.repaint ();
        }
    }

    /**
     * Disposes {@link HeatMap}.
     */
    public void dispose ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Hiding heat map
        disposeFromLayeredPane ();

        // Cleaning-up references
        rootPane.removeComponentListener ( resizeListener );
        rootPane = null;

        // Updating heat map
        updateHeatMap ();
    }

    /**
     * Dispose {@link MagnifierGlass} from the glass pane.
     */
    protected void disposeFromLayeredPane ()
    {
        if ( isDisplayed () )
        {
            // Removing heat map from layered pane
            final JLayeredPane layeredPane = rootPane.getLayeredPane ();
            layeredPane.remove ( this );

            // Updating view
            layeredPane.revalidate ();
            layeredPane.repaint ();

            // Disabling heat map
            setEnabled ( false );
        }
    }

    /**
     * Returns whether or not this {@link HeatMap} is currently displayed.
     * Be aware that this method only says whether or not this {@link HeatMap} is active.
     * Actual visibility state can be retrieved through {@link #isShowing()} method like in any other Swing component.
     *
     * @return {@code true} if this {@link HeatMap} is currently displayed, {@code false} otherwise
     */
    public boolean isDisplayed ()
    {
        return isEnabled ();
    }

    /**
     * Prevents unnecessary preferred size calculations.
     * This component doesn't have preferred size and doesn't need one.
     * It is always resized to fit benchmarked area size.
     */
    @Override
    public Dimension getPreferredSize ()
    {
        return new Dimension ( 0, 0 );
    }

    /**
     * Prevents {@link HeatMap} from absorbing any kinds of events.
     * This is necessary to allow normal interactions with UI below.
     */
    @Override
    public boolean contains ( final int x, final int y )
    {
        return false;
    }

    /**
     * {@link HeatMap} data display mode.
     *
     * @author Mikle Garin
     */
    public enum Mode
    {
        /**
         * Displays heat map for sections on a grid.
         */
        grid,

        /**
         * Displays heat map for the components tree.
         */
        /* component */
    }
}