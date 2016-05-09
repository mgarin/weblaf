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

package com.alee.extended.magnifier;

import com.alee.global.StyleConstants;
import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.utils.*;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Custom component that allows you to display a magnifier within your application window.
 * That magnifier can zoom in any part of the UI displayed within the window.
 * <p>
 * JComponent is used instead of separate window for three reasons:
 * 1. It is easy to sink events from JComponent to underlying UI elements
 * 2. It is easy to snapshot Swing UI, but magnifying screen parts will not work like that
 * 3. Not all OS support window per-pixel translucency which will limit the usage of this feature
 * <p>
 * This component might be extended in future to support windowed mode, but for now it is limited to Swing window bounds.
 *
 * @author Mikle Garin
 */

public class MagnifierGlass extends JComponent
{
    /**
     * todo 1. Extend common panel and provide shade painting there
     * todo 2. Add custom background painter that will display zoomed area
     */

    /**
     * Icons.
     */
    public static final ImageIcon cursorIcon = new ImageIcon ( MagnifierGlass.class.getResource ( "icons/cursor.png" ) );

    /**
     * Zoom area size.
     */
    protected Dimension size = new Dimension ( 159, 159 );

    /**
     * Zoom area shape type.
     */
    protected MagnifierType type = MagnifierType.rectangular;

    /**
     * Magnifier position.
     */
    protected MagnifierPosition position = MagnifierPosition.nearCursor;

    /**
     * Magnifier shade width.
     */
    protected int shadeWidth = 25;

    /**
     * Rectangular magnifier round.
     */
    protected int round = 10;

    /**
     * Zoom factor.
     */
    protected int zoomFactor;

    /**
     * Whether or not dummy cursor should be displayed on magnified image.
     * Note that displayed cursor will not represent exact system cursor.
     */
    protected boolean displayDummyCursor = true;

    /**
     * Dummy cursor opacity.
     */
    protected float dummyCursorOpacity = 0.5f;

    /**
     * Milliseconds to forcefully update magnifier buffer.
     * Set to zero to disable forced updates.
     */
    protected long forceUpdateFrequency = 100;

    /**
     * Runtime variables.
     */
    protected JComponent zoomProvider;
    protected Cursor defaultCursor;
    protected BufferedImage buffer;
    protected BufferedImage view;
    protected AWTEventListener listener;
    protected WebTimer forceUpdater;
    protected Icon shadeIcon;
    protected int lastShadeWidth;
    protected boolean rendered;

    /**
     * Constructs magnifier that will work over the specified component.
     * Note that zoom will only display elements which are contained inside of the specified component.
     */
    public MagnifierGlass ()
    {
        this ( 4 );
    }

    /**
     * Constructs magnifier that will work over the specified component.
     * Note that zoom will only display elements which are contained inside of the specified component.
     */
    public MagnifierGlass ( final int zoomFactor )
    {
        super ();
        setOpaque ( false );
        setBackground ( new Color ( 237, 237, 237 ) );

        // Initial zoom factor
        this.zoomFactor = zoomFactor;

        // Magnifier is initially disabled
        setEnabled ( false );

        // Forceful updater
        forceUpdater = new WebTimer ( forceUpdateFrequency, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                updatePreview ();
            }
        } );
        forceUpdater.setUseEventDispatchThread ( true );
        forceUpdater.setUseDaemonThread ( true );
        forceUpdater.setRepeats ( true );

        // AWT event listener
        listener = new AWTEventListener ()
        {
            @Override
            public void eventDispatched ( final AWTEvent event )
            {
                // Process events only if this magnifier is enabled
                if ( isEnabled () )
                {
                    // Forcing later update within EDT
                    // We are already within EDT here, but this is necessary
                    // UI update will look laggy due to event handling specifics without this
                    SwingUtilities.invokeLater ( new Runnable ()
                    {
                        @Override
                        public void run ()
                        {
                            updatePreview ();
                            restartForceUpdater ();
                        }
                    } );
                }
            }
        };
    }

    /**
     * Returns zoom area size.
     *
     * @return zoom area size
     */
    public Dimension getZoomAreaSize ()
    {
        return size;
    }

    /**
     * Sets zoom area size.
     *
     * @param size zoom area size
     */
    public void setZoomAreaSize ( final Dimension size )
    {
        if ( !CompareUtils.equals ( this.size, size ) )
        {
            this.size = size;
            updatePreview ();
        }
    }

    /**
     * Returns zoom area shape type.
     *
     * @return zoom area shape type
     */
    public MagnifierType getType ()
    {
        return type;
    }

    /**
     * Sets zoom area shape type.
     *
     * @param type zoom area shape type
     */
    public void setType ( final MagnifierType type )
    {
        if ( this.type != type )
        {
            this.type = type;
            updatePreview ();
        }
    }

    /**
     * Returns magnifier position.
     *
     * @return magnifier position
     */
    public MagnifierPosition getPosition ()
    {
        return position;
    }

    /**
     * Sets magnifier position.
     *
     * @param position magnifier position
     */
    public void setPosition ( final MagnifierPosition position )
    {
        if ( this.position != position )
        {
            if ( position == MagnifierPosition.staticComponent )
            {
                disposeFromGlassPane ();
            }
            else
            {
                displayOnGlassPane ();
            }
            this.position = position;
            updatePreview ();

        }
    }

    /**
     * Returns magnifier shade width.
     *
     * @return magnifier shade width
     */
    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    /**
     * Sets magnifier shade width.
     *
     * @param shadeWidth magnifier shade width
     */
    public void setShadeWidth ( final int shadeWidth )
    {
        if ( this.shadeWidth != shadeWidth )
        {
            this.shadeWidth = shadeWidth;
            updatePreview ();
        }
    }

    /**
     * Returns rectangular magnifier round.
     *
     * @return rectangular magnifier round
     */
    public int getRound ()
    {
        return round;
    }

    /**
     * Sets rectangular magnifier round.
     *
     * @param round rectangular magnifier round
     */
    public void setRound ( final int round )
    {
        if ( this.round != round )
        {
            this.round = round;
            updatePreview ();
        }
    }

    /**
     * Returns zoom factor.
     *
     * @return zoom factor
     */
    public int getZoomFactor ()
    {
        return zoomFactor;
    }

    /**
     * Sets zoom factor.
     *
     * @param zoomFactor zoom factor
     */
    public void setZoomFactor ( final int zoomFactor )
    {
        if ( this.zoomFactor != zoomFactor )
        {
            this.zoomFactor = zoomFactor;
            updatePreview ();
        }
    }

    /**
     * Returns whether or not dummy cursor should be displayed on magnified image.
     *
     * @return true if dummy cursor should be displayed on magnified image, false otherwise
     */
    public boolean isDisplayDummyCursor ()
    {
        return displayDummyCursor;
    }

    /**
     * Sets whether or not dummy cursor should be displayed on magnified image.
     *
     * @param display whether or not dummy cursor should be displayed on magnified image
     */
    public void setDisplayDummyCursor ( final boolean display )
    {
        if ( this.displayDummyCursor != display )
        {
            this.displayDummyCursor = display;
            updatePreview ();
        }
    }

    /**
     * Returns dummy cursor opacity.
     *
     * @return dummy cursor opacity
     */
    public float getDummyCursorOpacity ()
    {
        return dummyCursorOpacity;
    }

    /**
     * Sets dummy cursor opacity.
     *
     * @param opacity dummy cursor opacity
     */
    public void setDummyCursorOpacity ( final float opacity )
    {
        if ( this.dummyCursorOpacity != opacity )
        {
            this.dummyCursorOpacity = opacity;
            updatePreview ();
        }
    }

    /**
     * Returns milliseconds to forcefully update magnifier buffer.
     *
     * @return milliseconds to forcefully update magnifier buffer
     */
    public long getForceUpdateFrequency ()
    {
        return forceUpdateFrequency;
    }

    /**
     * Sets milliseconds to forcefully update magnifier buffer.
     *
     * @param forceUpdateFrequency milliseconds to forcefully update magnifier buffer
     */
    public void setForceUpdateFrequency ( final long forceUpdateFrequency )
    {
        if ( this.forceUpdateFrequency != forceUpdateFrequency )
        {
            this.forceUpdateFrequency = forceUpdateFrequency;
            restartForceUpdater ();
        }
    }

    /**
     * Properly restarts
     */
    protected void restartForceUpdater ()
    {
        if ( isEnabled () && forceUpdateFrequency > 0 )
        {
            forceUpdater.setDelay ( forceUpdateFrequency );
            forceUpdater.restart ();
        }
        else
        {
            forceUpdater.stop ();
        }
    }

    /**
     * Returns UI buffer image size.
     *
     * @return UI buffer image size
     */
    protected Dimension getBufferSize ()
    {
        return new Dimension ( size.width / zoomFactor, size.height / zoomFactor );
    }

    /**
     * Updates magnified UI preview.
     */
    protected void updatePreview ()
    {
        if ( isEnabled () )
        {
            // Updating buffer image if something has changed
            final Dimension bs = getBufferSize ();
            if ( buffer == null || buffer.getWidth () != bs.width || buffer.getHeight () != bs.height )
            {
                buffer = ImageUtils.createCompatibleImage ( bs.width, bs.height, Transparency.TRANSLUCENT );
            }

            // Filling-in image content
            final Point mp = MouseInfo.getPointerInfo ().getLocation ();
            final Rectangle gb = SwingUtils.getBoundsOnScreen ( zoomProvider );
            if ( gb.contains ( mp ) )
            {
                // Rendering UI snapshot
                final Graphics2D g2d = buffer.createGraphics ();
                g2d.setBackground ( StyleConstants.transparent );
                g2d.clearRect ( 0, 0, bs.width, bs.height );
                final int x = mp.x - gb.x - bs.width / 2;
                final int y = mp.y - gb.y - bs.height / 2;
                g2d.translate ( -x, -y );
                zoomProvider.paintAll ( g2d );
                if ( displayDummyCursor )
                {
                    g2d.translate ( x, y );
                    GraphicsUtils.setupAlphaComposite ( g2d, dummyCursorOpacity );
                    g2d.drawImage ( cursorIcon.getImage (), bs.width / 2, bs.height / 2, null );
                }
                g2d.dispose ();

                // Marking as rendered
                rendered = true;
            }
            else if ( rendered )
            {
                // Clearing buffer
                final Graphics2D g2d = buffer.createGraphics ();
                g2d.setBackground ( StyleConstants.transparent );
                g2d.clearRect ( 0, 0, bs.width, bs.height );
                g2d.dispose ();

                // Marking as non-rendered
                rendered = false;
            }

            // Updating magnifier location
            if ( position == MagnifierPosition.atCursor )
            {
                // Displaying magnifier straight on top of the cursor
                final WebGlassPane glassPane = GlassPaneManager.getGlassPane ( zoomProvider );
                final Point rel = SwingUtils.getRelativeLocation ( zoomProvider, glassPane );
                final Dimension ps = getPreferredSize ();
                final int mx = mp.x - gb.x + rel.x - ps.width / 2;
                final int my = mp.y - gb.y + rel.y - ps.height / 2;
                MagnifierGlass.this.setBounds ( mx, my, ps.width, ps.height );
            }
            else if ( position == MagnifierPosition.nearCursor )
            {
                final WebGlassPane glassPane = GlassPaneManager.getGlassPane ( zoomProvider );
                final Rectangle bos = SwingUtils.getBoundsOnScreen ( glassPane );
                final Point rmp = new Point ( mp.x - bos.x, mp.y - bos.y );
                final Dimension ps = getPreferredSize ();
                final int mx;
                if ( rmp.x - ps.width / 2 < 0 )
                {
                    mx = 0;
                }
                else if ( rmp.x + ps.width / 2 > bos.width )
                {
                    mx = bos.width - ps.width;
                }
                else
                {
                    mx = rmp.x - ps.width / 2;
                }
                final int my;
                if ( rmp.y + ps.height > bos.height && bos.height - rmp.y < rmp.y )
                {
                    my = rmp.y - ps.height;
                }
                else
                {
                    my = rmp.y;
                }
                MagnifierGlass.this.setBounds ( mx, my, ps.width, ps.height );
            }

            // Updating cursor on the window
            SwingUtils.getWindowAncestor ( zoomProvider )
                    .setCursor ( position == MagnifierPosition.atCursor ? SystemUtils.getTransparentCursor () : defaultCursor );

            // Repainting magnifier
            // This is required in addition to bounds change repaint
            // Otherwise magnifier will not be properly updated when bounds do not change
            repaint ();
        }
        else if ( buffer != null )
        {
            // Restoring cursor
            SwingUtils.getWindowAncestor ( zoomProvider ).setCursor ( defaultCursor );

            // Resetting buffer if magnifier is hidden
            buffer.flush ();
            buffer = null;
        }
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        if ( isEnabled () && rendered )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            GraphicsUtils.setupAntialias ( g2d );

            // Fill and border shapes
            final Shape fillShape;
            final Shape borderShape;
            if ( type == MagnifierType.rectangular )
            {
                fillShape = new RoundRectangle2D.Double ( shadeWidth, shadeWidth, size.width, size.height, round, round );
                borderShape = new RoundRectangle2D.Double ( shadeWidth, shadeWidth, size.width - 1, size.height - 1, round, round );
            }
            else
            {
                fillShape = new Ellipse2D.Double ( shadeWidth, shadeWidth, size.width, size.height );
                borderShape = new Ellipse2D.Double ( shadeWidth, shadeWidth, size.width - 1, size.height - 1 );
            }

            // Painting background
            g2d.setPaint ( getBackground () );
            g2d.fill ( fillShape );

            // Painting buffer
            final Shape oldClip = GraphicsUtils.setupClip ( g2d, fillShape, round > 0 );
            g2d.drawImage ( buffer, shadeWidth, shadeWidth, size.width, size.height, null );
            GraphicsUtils.restoreClip ( g2d, oldClip, round > 0 );

            // Painting shade
            final Icon icon = getShadeIcon ();
            if ( icon instanceof NinePatchIcon )
            {
                ( ( NinePatchIcon ) icon ).paintIcon ( g2d, 0, 0, getWidth (), getHeight () );
            }
            else
            {
                icon.paintIcon ( this, g2d, 0, 0 );
            }

            // Painting border
            g2d.setPaint ( Color.GRAY );
            g2d.draw ( borderShape );
        }
    }

    /**
     * Returns custom shade icon.
     *
     * @return custom shade icon
     */
    protected Icon getShadeIcon ()
    {
        if ( type == MagnifierType.rectangular )
        {
            if ( shadeIcon == null || shadeWidth != lastShadeWidth || !( shadeIcon instanceof NinePatchIcon ) )
            {
                shadeIcon = NinePatchUtils.createShadeIcon ( shadeWidth, round, 0.75f );
                lastShadeWidth = shadeWidth;
            }
        }
        else
        {
            if ( shadeIcon == null || shadeWidth != lastShadeWidth || !( shadeIcon instanceof ImageIcon ) )
            {
                final Dimension ps = getPreferredSize ();
                final Ellipse2D.Double shape = new Ellipse2D.Double ( shadeWidth, shadeWidth, size.width, size.height );
                shadeIcon = new ImageIcon ( ImageUtils.createShadeImage ( ps.width, ps.height, shape, shadeWidth, 0.75f, true ) );
                lastShadeWidth = shadeWidth;
            }
        }
        return shadeIcon;
    }

    /**
     * Initializes or disposes magnifier on the specified window.
     *
     * @param window magnifier window
     */
    public void displayOrDispose ( final Window window )
    {
        displayOrDispose ( getZoomProvider ( window ) );
    }

    /**
     * Initializes or disposes magnifier on the window where specified component is located.
     *
     * @param component magnifier provider
     */
    public void displayOrDispose ( final JComponent component )
    {
        if ( isEnabled () )
        {
            dispose ();
        }
        else
        {
            display ( component );
        }
    }

    /**
     * Initializes magnifier on the specified window.
     *
     * @param window magnifier window
     */
    public void display ( final Window window )
    {
        display ( getZoomProvider ( window ) );
    }

    /**
     * Initializes magnifier on the window where specified component is located.
     *
     * @param component magnifier provider
     */
    public void display ( final JComponent component )
    {
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
        if ( SwingUtils.getRootPane ( component ) == null )
        {
            // Checking rootpane existance
            throw new IllegalArgumentException ( "Provided component is not placed within any window: " + component );
        }

        // Changing visibility flag
        setEnabled ( true );

        // Retrieving component that will be providing us the area to use magnifier within
        zoomProvider = component instanceof JRootPane ? ( ( JRootPane ) component ).getLayeredPane () : component;
        defaultCursor = SwingUtils.getWindowAncestor ( zoomProvider ).getCursor ();

        // Updating buffer image
        updatePreview ();

        // Displaying magnifier
        displayOnGlassPane ();

        // Starting force updater
        restartForceUpdater ();

        // Adding global AWT event listeners
        Toolkit.getDefaultToolkit ().addAWTEventListener ( listener, AWTEvent.MOUSE_EVENT_MASK );
        Toolkit.getDefaultToolkit ().addAWTEventListener ( listener, AWTEvent.MOUSE_MOTION_EVENT_MASK );
        Toolkit.getDefaultToolkit ().addAWTEventListener ( listener, AWTEvent.MOUSE_WHEEL_EVENT_MASK );
        Toolkit.getDefaultToolkit ().addAWTEventListener ( listener, AWTEvent.KEY_EVENT_MASK );
    }

    /**
     * Hides magnifier.
     */
    public void dispose ()
    {
        // Changing visibility flag
        setEnabled ( false );

        // Updating buffer image
        updatePreview ();

        // Hiding magnifier
        disposeFromGlassPane ();

        // Stopping force updater
        restartForceUpdater ();

        // Removing global AWT event listeners
        Toolkit.getDefaultToolkit ().removeAWTEventListener ( listener );

        // Cleaning up
        zoomProvider = null;
        defaultCursor = null;
    }

    /**
     * Returns whether or not this magnifier is currently displayed.
     * Be aware that this method only says whether or not this magnifier is active.
     * Actual visibility state can be retrieved through {@link #isShowing()} method like in any other Swing component.
     *
     * @return true if this magnifier is currently displayed, false otherwise
     */
    public boolean isDisplayed ()
    {
        return isEnabled ();
    }

    /**
     * Returns zoom provider component for the specified window.
     *
     * @param window window to retrieve zoom provider for
     * @return zoom provider component for the specified window
     */
    protected JComponent getZoomProvider ( final Window window )
    {
        final JComponent component;
        if ( window instanceof JWindow )
        {
            component = ( ( JWindow ) window ).getLayeredPane ();
        }
        else if ( window instanceof JDialog )
        {
            component = ( ( JDialog ) window ).getLayeredPane ();
        }
        else if ( window instanceof JFrame )
        {
            component = ( ( JFrame ) window ).getLayeredPane ();
        }
        else
        {
            // Other types of window are not supported as they do not have layered pane
            throw new IllegalArgumentException ( "Provided window must contain JLayeredPane" );
        }
        return component;
    }

    /**
     * Display magnifier on the glass pane.
     */
    protected void displayOnGlassPane ()
    {
        if ( isEnabled () )
        {
            GlassPaneManager.getGlassPane ( zoomProvider ).showComponent ( this );
        }
    }

    /**
     * Dispose magnifier from the glass pane.
     */
    protected void disposeFromGlassPane ()
    {
        if ( isEnabled () )
        {
            GlassPaneManager.getGlassPane ( zoomProvider ).hideComponent ( this );
        }
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return isEnabled () ? new Dimension ( size.width + shadeWidth * 2, size.height + shadeWidth * 2 ) : new Dimension ( 0, 0 );
    }
}