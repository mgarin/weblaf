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

package com.alee.extended.colorchooser;

import com.alee.laf.StyleConstants;
import com.alee.laf.colorchooser.WebColorChooser;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.ColorUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 23.11.12 Time: 19:04
 */

public class WebGradientColorChooser extends JComponent implements MouseListener, MouseMotionListener, FocusListener, SettingsMethods
{
    // Style constants
    private static Color borderColor = Color.DARK_GRAY; // new Color ( 51, 51, 51 );
    private static Color disabledBorderColor = Color.LIGHT_GRAY;
    private static Color foreground = Color.BLACK;
    private static Color disabledForeground = new Color ( 178, 178, 178 );
    private static Color innerBorderColor = ColorUtils.white ( 180 );
    private static float[] overlayFractions = new float[]{ 0f, 0.4f, 0.41f, 1f };
    private static Color[] lineOverlayColors =
            new Color[]{ ColorUtils.white ( 160 ), ColorUtils.white ( 120 ), ColorUtils.white ( 100 ), ColorUtils.white ( 40 ) };
    private static Color[] controlOverlayColors =
            new Color[]{ ColorUtils.white ( 80 ), ColorUtils.white ( 50 ), ColorUtils.white ( 20 ), StyleConstants.transparent };
    private static final Stroke stripeStroke =
            new BasicStroke ( 1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{ 4f, 4f }, 0f );
    private static final float closestPoint = 0.001f;

    // Runtime data
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener> ();
    private GradientData gradientData;

    // Settings
    private int shadeWidth = WebGradientColorChooserStyle.shadeWidth;
    private int lineWidth = WebGradientColorChooserStyle.lineWidth;
    private Dimension gripperSize = WebGradientColorChooserStyle.gripperSize;
    private Insets margin = WebGradientColorChooserStyle.margin;
    private boolean paintLabels = WebGradientColorChooserStyle.paintLabels;
    private int preferredWidth = WebGradientColorChooserStyle.preferredWidth;

    // Runtime values
    private GradientColorData draggedGripper = null;
    private boolean draggedOut = false;

    public WebGradientColorChooser ()
    {
        this ( SettingsManager.getDefaultValue ( GradientData.class ) );
    }

    public WebGradientColorChooser ( GradientData gradientData )
    {
        super ();

        setGradientData ( gradientData );

        setFocusable ( true );
        setFont ( WebGradientColorChooserStyle.labelsFont );
        setForeground ( WebGradientColorChooserStyle.foreground );

        addFocusListener ( this );
        addMouseListener ( this );
        addMouseMotionListener ( this );
    }

    public void focusGained ( FocusEvent e )
    {
        repaint ();
    }

    public void focusLost ( FocusEvent e )
    {
        repaint ();
    }

    public void mouseClicked ( MouseEvent e )
    {
        if ( !isEnabled () )
        {
            return;
        }

        // Displaying color chooser dialog on double-click
        if ( SwingUtilities.isLeftMouseButton ( e ) && e.getClickCount () == 2 )
        {
            GradientColorData colorData = getColorDataUnderPoint ( e.getPoint () );
            if ( colorData != null )
            {
                Color newColor = WebColorChooser.showDialog ( WebGradientColorChooser.this, colorData.getColor () );
                if ( newColor != null )
                {
                    // Updating color
                    colorData.setColor ( newColor );

                    fireStateChanged ();
                    repaint ();
                }
            }
        }
    }

    public void mousePressed ( MouseEvent e )
    {
        if ( !isEnabled () )
        {
            return;
        }

        if ( !isFocusOwner () )
        {
            requestFocusInWindow ();
        }

        if ( SwingUtilities.isLeftMouseButton ( e ) )
        {
            GradientColorData colorData = getColorDataUnderPoint ( e.getPoint () );
            if ( colorData != null )
            {
                if ( SwingUtils.isAlt ( e ) )
                {
                    // Copying and dragging copied color
                    draggedGripper = colorData.clone ();
                    draggedGripper.setLocation ( findFreeLocation ( draggedGripper.getLocation () ) );
                    gradientData.addGradientColorData ( draggedGripper );
                    draggedOut = false;

                    fireStateChanged ();
                    repaint ();
                }
                else
                {
                    // Dragging color
                    draggedGripper = colorData;
                    draggedOut = false;
                }
            }
            if ( draggedGripper == null && e.getY () > 0 )
            {
                // Checking that click location doesn't have a color
                float newLocation = getLocationForPoint ( e );
                if ( isFree ( newLocation ) )
                {
                    // Creating new color when clicked not on gripper
                    draggedGripper = new GradientColorData ( newLocation, getColorForLocation ( newLocation ) );
                    gradientData.addGradientColorData ( draggedGripper );
                    draggedOut = false;

                    fireStateChanged ();
                    repaint ();
                }
            }
        }
        else if ( ( SwingUtils.isMiddleMouseButton ( e ) || SwingUtils.isRightMouseButton ( e ) ) && gradientData.size () > 2 )
        {
            GradientColorData colorData = getColorDataUnderPoint ( e.getPoint () );
            if ( colorData != null )
            {
                // Removing color
                gradientData.removeGradientColorData ( colorData );

                fireStateChanged ();
                repaint ();
            }
        }
    }

    public GradientColorData getColorDataUnderPoint ( Point point )
    {
        GradientColorData colorData = null;
        for ( int i = gradientData.size () - 1; i >= 0; i-- )
        {
            GradientColorData gcd = gradientData.get ( i );
            if ( getGripperBounds ( gcd ).contains ( point ) )
            {
                colorData = gcd;
                break;
            }
        }
        return colorData;
    }

    public void mouseDragged ( MouseEvent e )
    {
        if ( !isEnabled () )
        {
            return;
        }

        if ( draggedGripper != null )
        {
            if ( e.getY () < -10 && gradientData.size () + ( draggedOut ? 1 : 0 ) > 2 )
            {
                if ( !draggedOut )
                {
                    // Removing color when dragged out at top
                    draggedOut = true;
                    gradientData.removeGradientColorData ( draggedGripper );

                    fireStateChanged ();
                    repaint ();
                }
            }
            else
            {
                // Restoring color when dragged back
                if ( draggedOut )
                {
                    draggedOut = false;
                    gradientData.addGradientColorData ( draggedGripper );
                }

                // Updating location
                float newLocation = getLocationForPoint ( e );
                newLocation = findFreeLocation ( newLocation );
                draggedGripper.setLocation ( newLocation );

                fireStateChanged ();
                repaint ();
            }
        }
    }

    public void mouseReleased ( MouseEvent e )
    {
        if ( !isEnabled () )
        {
            return;
        }

        if ( SwingUtilities.isLeftMouseButton ( e ) )
        {
            draggedGripper = null;
        }
    }

    public void mouseEntered ( MouseEvent e )
    {
        //
    }

    public void mouseExited ( MouseEvent e )
    {
        //
    }

    public void mouseMoved ( MouseEvent e )
    {
        //
    }

    public boolean isFree ( float location )
    {
        return !gradientData.containtsLocation ( location );
    }

    private float findFreeLocation ( float newLocation )
    {
        if ( gradientData.containtsLocation ( newLocation ) )
        {
            if ( newLocation == 1f )
            {
                while ( gradientData.containtsLocation ( newLocation ) )
                {
                    newLocation -= closestPoint;
                }
            }
            else
            {
                while ( gradientData.containtsLocation ( newLocation ) )
                {
                    newLocation += closestPoint;
                }
            }
        }
        return newLocation;
    }

    public Rectangle getGripperBounds ( int index )
    {
        return getGripperBounds ( gradientData.getGradientColorsData ().get ( index ) );
    }

    public Rectangle getGripperBounds ( GradientColorData colorData )
    {
        float location = colorData.getLocation ();
        Rectangle lineBounds = getLineBounds ();
        int x = lineBounds.x + 2 + Math.round ( ( lineBounds.width - 4 ) * location ) - gripperSize.width / 2;
        int y = lineBounds.y + lineBounds.height - gripperSize.height / 2;
        return new Rectangle ( x, y, gripperSize.width, gripperSize.height );
    }

    private float getLocationForPoint ( MouseEvent e )
    {
        return getLocationForPoint ( e.getPoint () );
    }

    public float getLocationForPoint ( Point point )
    {
        Rectangle lineBounds = getLineBounds ();
        int x = lineBounds.x + 2;
        int w = lineBounds.width - 4;
        return ( float ) Math.max ( 0, Math.min ( point.x - x, w ) ) / w;
    }

    public Color getColorForLocation ( float location )
    {
        return gradientData.getColorForLocation ( location );
    }

    public GradientData getGradientData ()
    {
        return gradientData;
    }

    public void setGradientData ( GradientData gradientData )
    {
        if ( !CompareUtils.equals ( this.gradientData, gradientData ) )
        {
            this.gradientData = gradientData;
            this.draggedGripper = null;
            fireStateChanged ();
            repaint ();
        }
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    public int getLineWidth ()
    {
        return lineWidth;
    }

    public void setLineWidth ( int lineWidth )
    {
        this.lineWidth = lineWidth;
    }

    public Dimension getGripperSize ()
    {
        return gripperSize;
    }

    public void setGripperSize ( Dimension gripperSize )
    {
        this.gripperSize = gripperSize;
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    public void setMargin ( int top, int left, int bottom, int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setMargin ( int spacing )
    {
        setMargin ( spacing, spacing, spacing, spacing );
    }

    private void updateBorder ()
    {
        setBorder ( BorderFactory.createEmptyBorder ( margin.top, margin.left, margin.bottom, margin.right ) );
    }

    public boolean isPaintLabels ()
    {
        return paintLabels;
    }

    public void setPaintLabels ( boolean paintLabels )
    {
        this.paintLabels = paintLabels;
    }

    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        Graphics2D g2d = ( Graphics2D ) g;
        LafUtils.setupAntialias ( g2d );

        // Line bounds
        Rectangle lineBounds = getLineBounds ();

        // Painting main gradient line
        paintBorderedBackground ( g2d, lineBounds, getLineBackgroundPainter ( lineBounds ), getLineOverlayPainter ( lineBounds ), false );

        // Painting controls
        paintControls ( g2d, lineBounds );
    }

    private void paintControls ( Graphics2D g2d, Rectangle lineBounds )
    {
        int x = lineBounds.x + 2;
        int x2 = lineBounds.x + lineBounds.width - 2;
        int w = lineBounds.width - 4;
        int y = lineBounds.y + lineBounds.height - gripperSize.height / 2;
        int textY = y + gripperSize.height;
        FontMetrics fm = g2d.getFontMetrics ();
        int lineY = textY + fm.getDescent () - fm.getHeight () / 2;
        for ( int i = 0; i < gradientData.size (); i++ )
        {
            GradientColorData gradientColorData = gradientData.get ( i );
            float currentLocation = gradientColorData.getLocation ();
            int currentX = x + Math.round ( w * currentLocation ) - gripperSize.width / 2;
            Rectangle controlBounds = new Rectangle ( currentX, y, gripperSize.width, gripperSize.height );

            // Painting control
            paintBorderedBackground ( g2d, controlBounds, gradientColorData.getColor (), getControlOverlayPainter ( controlBounds ), true );

            // Painting text and separators between controls if there is enough space
            if ( paintLabels && i < gradientData.size () - 1 )
            {
                GradientColorData nextGradientColorData = gradientData.get ( i + 1 );
                int gapX1 = currentX + gripperSize.width;
                int gapX2 = x + Math.round ( w * nextGradientColorData.getLocation () ) - gripperSize.width / 2;
                int gapInPercents = Math.round ( ( nextGradientColorData.getLocation () - currentLocation ) * 100 );
                int textWidth = fm.stringWidth ( gapInPercents + "" );
                if ( gapX2 - gapX1 > textWidth + 4 )
                {
                    int textX = ( gapX1 + gapX2 ) / 2 - textWidth / 2;

                    // Painting text
                    g2d.setPaint ( isEnabled () ? foreground : disabledForeground );
                    g2d.drawString ( gapInPercents + "", textX, textY );

                    // Painting separators
                    if ( gapX2 - gapX1 > textWidth + 14 )
                    {
                        g2d.setPaint ( new GradientPaint ( gapX1 + 2, 0, StyleConstants.transparent, textX - 2, 0, Color.LIGHT_GRAY ) );
                        g2d.drawLine ( gapX1 + 2, lineY, textX - 2, lineY );
                        g2d.setPaint ( new GradientPaint ( textX + textWidth + 2, 0, Color.LIGHT_GRAY, gapX2 - 2, 0,
                                StyleConstants.transparent ) );
                        g2d.drawLine ( textX + textWidth + 2, lineY, gapX2 - 2, lineY );
                    }
                }
            }
        }
    }

    private void paintBorderedBackground ( Graphics2D g2d, Rectangle bounds, Paint background, Paint overlay, boolean control )
    {
        // Shade & focus
        if ( isEnabled () && !control )
        {
            LafUtils.drawShade ( g2d, new RoundRectangle2D.Double ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, 4, 4 ),
                    isFocusOwner () ? StyleConstants.fieldFocusColor : StyleConstants.borderColor, shadeWidth );
        }

        // Background
        g2d.setPaint ( background );
        g2d.fillRoundRect ( bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2, 2, 2 );

        // Inner border
        g2d.setPaint ( innerBorderColor );
        g2d.drawRoundRect ( bounds.x + 1, bounds.y + 1, bounds.width - 3, bounds.height - 3, 2, 2 );

        // Additional control overlay
        if ( control )
        {
            g2d.setPaint ( new GradientPaint ( bounds.x + 2, bounds.y + 2, ColorUtils.white ( 180 ), bounds.x + bounds.width * 2 / 3,
                    bounds.y + bounds.height - 2, StyleConstants.transparent ) );
            g2d.fillRect ( bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4 );
        }

        // Overlay
        g2d.setPaint ( overlay );
        g2d.fillRect ( bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4 );

        // Border
        g2d.setPaint ( isEnabled () ? borderColor : disabledBorderColor );
        g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, 4, 4 );
    }

    private LinearGradientPaint getLineBackgroundPainter ( Rectangle lineBounds )
    {
        return new LinearGradientPaint ( lineBounds.x + 1, 0, lineBounds.x + lineBounds.width - 1, 0, gradientData.getFractions (),
                gradientData.getColors () );
    }

    private LinearGradientPaint getLineOverlayPainter ( Rectangle bounds )
    {
        return getOverlayPainter ( bounds, overlayFractions, lineOverlayColors );
    }

    private LinearGradientPaint getControlOverlayPainter ( Rectangle bounds )
    {
        return getOverlayPainter ( bounds, overlayFractions, controlOverlayColors );
    }

    private LinearGradientPaint getOverlayPainter ( Rectangle bounds, float[] overlayFractions, Color[] overlayColors )
    {
        return new LinearGradientPaint ( 0, bounds.y + 2, 0, bounds.y + bounds.height - 2, overlayFractions, overlayColors );
    }

    private Rectangle getLineBounds ()
    {
        Insets insets = getInsets ();
        return new Rectangle ( insets.left + Math.max ( gripperSize.width / 2, shadeWidth ) - 2, insets.top + shadeWidth,
                getWidth () - insets.left - insets.right - Math.max ( gripperSize.width, shadeWidth * 2 ) + 4, lineWidth );
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
        Insets insets = getInsets ();
        Dimension ps =
                new Dimension ( insets.left + gripperSize.width * 5 + Math.max ( gripperSize.width, shadeWidth * 2 ) - 4 + insets.right,
                        insets.top + shadeWidth * 2 + lineWidth + gripperSize.height / 2 + ( gripperSize.height % 2 == 0 ? 0 : 1 ) +
                                insets.bottom );
        if ( preferredWidth != -1 )
        {
            ps.width = preferredWidth;
        }
        return ps;
    }

    public List<ChangeListener> getChangeListeners ()
    {
        return changeListeners;
    }

    public void addChangeListener ( ChangeListener dataChangeListener )
    {
        changeListeners.add ( dataChangeListener );
    }

    public void removeChangeListener ( ChangeListener dataChangeListener )
    {
        changeListeners.remove ( dataChangeListener );
    }

    private void fireStateChanged ()
    {
        ChangeEvent changeEvent = new ChangeEvent ( WebGradientColorChooser.this );
        for ( ChangeListener dataChangeListener : changeListeners )
        {
            dataChangeListener.stateChanged ( changeEvent );
        }
    }

    /**
     * Settings methods
     */

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String group, String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String group, String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass, boolean loadInitialSettings,
                                                            boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key, Object defaultValue, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass,
                                                            boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String group, String key, Object defaultValue, boolean loadInitialSettings,
                                   boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    /**
     * {@inheritDoc}
     */
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    /**
     * {@inheritDoc}
     */
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( this );
    }
}
