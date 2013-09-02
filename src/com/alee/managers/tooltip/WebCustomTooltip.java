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

package com.alee.managers.tooltip;

import com.alee.laf.WebFonts;
import com.alee.laf.label.WebLabel;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.utils.CollectionUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.FadeStateType;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 10.12.10 Time: 20:47
 */

public class WebCustomTooltip extends JComponent implements ShapeProvider
{
    // ID
    private static final String ID_PREFIX = "WCT";

    // Tooltip constants
    private static final int fadeFps = WebCustomTooltipStyle.fadeFps;
    private static final long fadeTime = WebCustomTooltipStyle.fadeTime;
    private static final int cornerLength = WebCustomTooltipStyle.cornerLength;
    private static final int cornerSideX = WebCustomTooltipStyle.cornerSideX;

    // Tooltip settings
    private String id = null;
    private JComponent tooltip = null;
    private WeakReference<Component> component = null;
    private Point displayLocation = null;
    private TooltipWay displayWay = WebCustomTooltipStyle.displayWay;
    private boolean showHotkey = WebCustomTooltipStyle.showHotkey;
    private int hotkeyLocation = WebCustomTooltipStyle.hotkeyLocation;
    private boolean defaultCloseBehavior = WebCustomTooltipStyle.defaultCloseBehavior;
    private int contentSpacing = WebCustomTooltipStyle.contentSpacing;
    private int leftRightSpacing = WebCustomTooltipStyle.leftRightSpacing;
    private int windowSideSpacing = WebCustomTooltipStyle.windowSideSpacing;
    private int round = WebCustomTooltipStyle.round;
    private int shadeWidth = WebCustomTooltipStyle.shadeWidth;
    private Color shadeColor = WebCustomTooltipStyle.shadeColor;
    private Color borderColor = WebCustomTooltipStyle.borderColor;
    private Color topBgColor = WebCustomTooltipStyle.topBgColor;
    private Color bottomBgColor = WebCustomTooltipStyle.bottomBgColor;
    private Color textColor = WebCustomTooltipStyle.textColor;
    private float trasparency = WebCustomTooltipStyle.trasparency;

    // Tooltip listeners
    private List<TooltipListener> listeners = new ArrayList<TooltipListener> ( 2 );

    // Tooltip variables
    private HotkeyTipLabel hotkey;
    private int cornerPeak = 0;

    // Animation variables
    private FadeStateType fadeStateType;
    private float fade = 0;
    private WebTimer fadeTimer;

    // Component listeners
    private AncestorListener ancestorListener;

    private static WebLabel createDefaultComponent ( Icon icon, String tooltip )
    {
        return new WebLabel ( tooltip, icon );
    }

    public WebCustomTooltip ( Component component, String tooltip )
    {
        this ( component, null, tooltip );
    }

    public WebCustomTooltip ( Component component, Icon icon, String tooltip )
    {
        this ( component, createDefaultComponent ( icon, tooltip ) );
    }

    public WebCustomTooltip ( Component component, String tooltip, TooltipWay tooltipWay )
    {
        this ( component, null, tooltip, tooltipWay );
    }

    public WebCustomTooltip ( Component component, Icon icon, String tooltip, TooltipWay tooltipWay )
    {
        this ( component, createDefaultComponent ( icon, tooltip ), tooltipWay );
    }

    public WebCustomTooltip ( Component component, String tooltip, boolean showHotkey )
    {
        this ( component, null, tooltip, showHotkey );
    }

    public WebCustomTooltip ( Component component, Icon icon, String tooltip, boolean showHotkey )
    {
        this ( component, createDefaultComponent ( icon, tooltip ), showHotkey );
    }

    public WebCustomTooltip ( Component component, String tooltip, TooltipWay tooltipWay, boolean showHotkey )
    {
        this ( component, null, tooltip, tooltipWay, showHotkey );
    }

    public WebCustomTooltip ( Component component, Icon icon, String tooltip, TooltipWay tooltipWay, boolean showHotkey )
    {
        this ( component, createDefaultComponent ( icon, tooltip ), tooltipWay, showHotkey );
    }

    public WebCustomTooltip ( Component component, JComponent tooltip )
    {
        this ( component, tooltip, WebCustomTooltipStyle.displayWay );
    }

    public WebCustomTooltip ( Component component, JComponent tooltip, TooltipWay tooltipWay )
    {
        this ( component, tooltip, tooltipWay, WebCustomTooltipStyle.showHotkey );
    }

    public WebCustomTooltip ( Component component, JComponent tooltip, boolean showHotkey )
    {
        this ( component, tooltip, WebCustomTooltipStyle.displayWay, showHotkey );
    }

    public WebCustomTooltip ( Component component, JComponent tooltip, TooltipWay tooltipWay, boolean showHotkey )
    {
        super ();

        SwingUtils.setOrientation ( this );
        SwingUtils.setForegroundRecursively ( tooltip, textColor );
        setOpaque ( false );

        // Tooltip unique id
        this.id = TextUtils.generateId ( ID_PREFIX );

        // Component to which this tooltip attached
        this.component = new WeakReference<Component> ( component );

        // Tooltip component
        this.tooltip = tooltip;
        this.tooltip.setFont ( WebFonts.getSystemTooltipFont () );

        // Show component hotkey on tooltip
        this.showHotkey = showHotkey;

        // Tooltip display direction
        this.displayWay = tooltipWay;

        // Tooltip hotkey preview component
        hotkey = new HotkeyTipLabel ();
        hotkey.setFont ( WebFonts.getSystemAcceleratorFont () );

        // Components placement on tooltip
        setLayout ( new BorderLayout ( 6, 6 ) );
        add ( tooltip, BorderLayout.CENTER );

        // Fade in-out timer
        fadeTimer = new WebTimer ( "WebCustomTooltip.fade", 1000 / fadeFps );
        fadeTimer.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                float roundsCount = fadeTime / ( 1000f / fadeFps );
                float fadeSpeed = 1f / roundsCount;
                if ( fadeStateType.equals ( FadeStateType.fadeIn ) )
                {
                    if ( fade < 1f )
                    {
                        fade = Math.min ( fade + fadeSpeed, 1f );
                        WebCustomTooltip.this.repaint ();
                    }
                    else
                    {
                        fireTooltipFullyShown ();
                        fadeTimer.stop ();
                    }
                }
                else if ( fadeStateType.equals ( FadeStateType.fadeOut ) )
                {
                    if ( fade > 0 )
                    {
                        fade = Math.max ( fade - fadeSpeed, 0f );
                        WebCustomTooltip.this.repaint ();
                    }
                    else
                    {
                        final JComponent parent = ( JComponent ) WebCustomTooltip.this.getParent ();
                        if ( parent != null )
                        {
                            Rectangle b = WebCustomTooltip.this.getBounds ();
                            parent.remove ( WebCustomTooltip.this );
                            parent.repaint ( b );
                        }
                        fadeTimer.stop ();
                    }
                }
            }
        } );
        addAncestorListener ( new AncestorListener ()
        {
            @Override
            public void ancestorAdded ( AncestorEvent event )
            {
                // Updating tooltip hotkey
                updateHotkey ();

                // Updating tooltip layout and location
                updateBorder ();
                updateLocation ();

                // Starting fade-in animation
                fade = 0;
                fadeStateType = FadeStateType.fadeIn;
                fadeTimer.start ();

                // Informing listeners that tooltip was shown
                fireTooltipShown ();
            }

            @Override
            public void ancestorRemoved ( AncestorEvent event )
            {
                // Informing listeners that tooltip was hidden
                fireTooltipHidden ();
            }

            @Override
            public void ancestorMoved ( AncestorEvent event )
            {
                // Updating location of the tooltip
                updateBorder ();
                updateLocation ();
            }
        } );

        // Parent component ancestor listener
        if ( component instanceof JComponent )
        {
            ancestorListener = new AncestorAdapter ()
            {
                @Override
                public void ancestorRemoved ( AncestorEvent event )
                {
                    // Closing tooltip
                    closeTooltip ();
                }

                @Override
                public void ancestorMoved ( AncestorEvent event )
                {
                    // Closing tooltip
                    closeTooltip ();
                }
            };
            ( ( JComponent ) component ).addAncestorListener ( ancestorListener );
        }
    }

    /**
     * Tooltip hotkey label update
     */

    private void updateHotkey ()
    {
        String hotkeyText = HotkeyManager.getComponentHotkeysString ( getComponent () );
        if ( showHotkey && !hotkeyText.trim ().equals ( "" ) )
        {
            // Updatings hotkey
            hotkey.setText ( hotkeyText );

            // Adding or re-adding hotkey label to tooltip
            if ( WebCustomTooltip.this.getComponentZOrder ( hotkey ) != -1 )
            {
                WebCustomTooltip.this.remove ( hotkey );
            }
            WebCustomTooltip.this.add ( hotkey, getActualHotkeyLocation () );
        }
        else
        {
            // Removing hotkey label from tooltip
            WebCustomTooltip.this.remove ( hotkey );
        }
    }

    private String getActualHotkeyLocation ()
    {
        switch ( hotkeyLocation )
        {
            case SwingConstants.LEADING:
                return BorderLayout.LINE_START;
            case SwingConstants.TRAILING:
                return BorderLayout.LINE_END;
            case SwingConstants.LEFT:
                return BorderLayout.WEST;
            case SwingConstants.RIGHT:
                return BorderLayout.EAST;
            default:
                return BorderLayout.LINE_END;
        }
    }

    /**
     * Tooltip hide
     */

    public void closeTooltip ()
    {
        if ( getParent () == null )
        {
            return;
        }

        fadeStateType = FadeStateType.fadeOut;
        if ( !fadeTimer.isRunning () )
        {
            fadeTimer.start ();
        }
    }

    /**
     * Tooltip destroy
     */

    public void destroyTooltip ()
    {
        Component component = getComponent ();
        if ( component instanceof JComponent )
        {
            ( ( JComponent ) component ).removeAncestorListener ( ancestorListener );
        }
        fireTooltipDestroyed ();
    }

    /**
     * Calculated display way
     */

    public TooltipWay getActualDisplayWay ()
    {
        Component component = getComponent ();
        if ( displayWay != null )
        {
            if ( displayWay.equals ( TooltipWay.leading ) || displayWay.equals ( TooltipWay.trailing ) )
            {
                boolean ltr = component.getComponentOrientation ().isLeftToRight ();
                if ( displayWay.equals ( TooltipWay.leading ) && ltr || displayWay.equals ( TooltipWay.trailing ) && !ltr )
                {
                    return TooltipWay.left;
                }
                else
                {
                    return TooltipWay.right;
                }
            }
            return displayWay;
        }
        else
        {
            if ( !component.isShowing () )
            {
                return TooltipWay.down;
            }

            Component glassPane = SwingUtilities.getRootPane ( component ).getGlassPane ();
            Dimension rootSize = glassPane.getSize ();
            Rectangle componentBounds = SwingUtils.getRelativeBounds ( component, glassPane );
            Dimension ps = WebCustomTooltip.this.getPreferredSize ();

            if ( componentBounds.y + getTooltipPoint ( component, TooltipWay.down ).y + ps.height < rootSize.height - windowSideSpacing )
            {
                return TooltipWay.down;
            }
            else if ( componentBounds.y + getTooltipPoint ( component, TooltipWay.up ).y - ps.height > windowSideSpacing )
            {
                return TooltipWay.up;
            }
            else if ( componentBounds.x + getTooltipPoint ( component, TooltipWay.right ).x +
                    ps.width < rootSize.width - windowSideSpacing )
            {
                return TooltipWay.right;
            }
            else if ( componentBounds.x + getTooltipPoint ( component, TooltipWay.left ).x - ps.width > windowSideSpacing )
            {
                return TooltipWay.left;
            }
            else
            {
                return TooltipWay.down;
            }
        }
    }

    private Point getTooltipPoint ( Component component, TooltipWay tooltipWay )
    {
        if ( displayLocation == null )
        {
            if ( tooltipWay.equals ( TooltipWay.down ) )
            {
                return new Point ( component.getWidth () / 2, component.getHeight () - cornerLength / 2 + shadeWidth );
            }
            else if ( tooltipWay.equals ( TooltipWay.up ) )
            {
                return new Point ( component.getWidth () / 2, cornerLength / 2 - shadeWidth );
            }
            else if ( tooltipWay.equals ( TooltipWay.left ) )
            {
                return new Point ( cornerLength / 2 - shadeWidth, getHeight () / 2 );
            }
            else
            {
                return new Point ( component.getWidth () - cornerLength / 2 + shadeWidth, getHeight () / 2 );
            }
        }
        else
        {
            return displayLocation;
        }
    }

    /**
     * Tooltip layout update
     */

    public void updateBorder ()
    {
        TooltipWay displayWay = getActualDisplayWay ();

        // Default margins
        int leftSpacing = shadeWidth + contentSpacing + leftRightSpacing +
                ( displayWay.equals ( TooltipWay.right ) ? cornerLength : 0 );
        int rightSpacing = shadeWidth + contentSpacing + leftRightSpacing +
                ( displayWay.equals ( TooltipWay.left ) ? cornerLength : 0 );
        int topSpacing = shadeWidth +
                contentSpacing + ( displayWay.equals ( TooltipWay.down ) ? cornerLength : 0 );
        int bottomSpacing = shadeWidth +
                contentSpacing + ( displayWay.equals ( TooltipWay.up ) ? cornerLength : 0 );

        // Additional hotkey margins
        Insets hm = getHotkeyMargins ();

        // Updating border
        setBorder ( BorderFactory
                .createEmptyBorder ( topSpacing + hm.top, leftSpacing + hm.left, bottomSpacing + hm.bottom, rightSpacing + hm.right ) );
        revalidate ();
    }

    private Insets getHotkeyMargins ()
    {
        boolean ltr = getComponentOrientation ().isLeftToRight ();
        boolean leftHotkey = hotkeyLocation == SwingConstants.LEFT ||
                hotkeyLocation == SwingConstants.LEADING && ltr ||
                hotkeyLocation == SwingConstants.TRAILING && !ltr;
        int left = showHotkey && leftHotkey ? 0 : 2;
        int right = showHotkey && !leftHotkey ? 0 : 2;
        return new Insets ( 0, left, 0, right );
    }

    /**
     * Close tooltip on orientation change to avoid location problems
     */

    @Override
    public void applyComponentOrientation ( ComponentOrientation o )
    {
        super.applyComponentOrientation ( o );
        closeTooltip ();
    }

    /**
     * Tooltip spacing between sides and content
     */

    public int getContentSpacing ()
    {
        return contentSpacing;
    }

    public void setContentSpacing ( int contentSpacing )
    {
        this.contentSpacing = contentSpacing;
        updateBorder ();
    }

    /**
     * Additional tooltip content left/right spacing
     */

    public int getLeftRightSpacing ()
    {
        return leftRightSpacing;
    }

    public void setLeftRightSpacing ( int leftRightSpacing )
    {
        this.leftRightSpacing = leftRightSpacing;
        updateBorder ();
        updateLocation ();
    }

    /**
     * Tooltip location on glasspane update
     */

    public void updateLocation ()
    {
        Component component = getComponent ();
        if ( getParent () != null && getParent ().isShowing () && component.isShowing () )
        {
            TooltipWay displayWay = getActualDisplayWay ();

            Point p = getParent ().getLocationOnScreen ();
            Point c = component.getLocationOnScreen ();
            Dimension ps = getPreferredSize ();

            if ( displayWay.equals ( TooltipWay.up ) || displayWay.equals ( TooltipWay.down ) )
            {
                int compMiddle;
                int compTipY;
                if ( displayLocation == null )
                {
                    compMiddle = c.x - p.x + component.getWidth () / 2;
                    compTipY = c.y - p.y + ( displayWay.equals ( TooltipWay.up ) ? ( cornerLength / 2 - shadeWidth - ps.height ) :
                            ( component.getHeight () - cornerLength / 2 + shadeWidth ) );
                }
                else
                {
                    compMiddle = c.x - p.x + displayLocation.x;
                    compTipY = c.y - p.y + displayLocation.y - ( displayWay.equals ( TooltipWay.up ) ? ps.height : 0 );
                }

                if ( compMiddle - ps.width / 2 < windowSideSpacing )
                {
                    int cw = windowSideSpacing - ( compMiddle - ps.width / 2 );
                    cornerPeak = Math.max ( shadeWidth + round + cornerSideX + 1, getWidth () / 2 - cw );
                    setLocation ( windowSideSpacing, compTipY );
                }
                else if ( compMiddle + ps.width / 2 > getParent ().getWidth () - windowSideSpacing )
                {
                    int cw = ( compMiddle + ps.width / 2 ) - ( getParent ().getWidth () - windowSideSpacing );
                    cornerPeak = Math.min ( ps.width - shadeWidth - round - cornerSideX - 1, getWidth () / 2 + cw );
                    setLocation ( getParent ().getWidth () - windowSideSpacing - ps.width, compTipY );
                }
                else
                {
                    cornerPeak = getWidth () / 2;
                    setLocation ( compMiddle - ps.width / 2, compTipY );
                }
            }
            else if ( displayWay.equals ( TooltipWay.left ) || displayWay.equals ( TooltipWay.right ) )
            {
                int compMiddle;
                int compTipX;
                if ( displayLocation == null )
                {
                    compMiddle = c.y - p.y + component.getHeight () / 2;
                    compTipX = c.x - p.x + ( displayWay.equals ( TooltipWay.left ) ? ( cornerLength / 2 - shadeWidth - ps.width ) :
                            ( component.getWidth () - cornerLength / 2 + shadeWidth ) );
                }
                else
                {
                    compMiddle = c.y - p.y + displayLocation.y;
                    compTipX = c.x - p.x + displayLocation.x - ( displayWay.equals ( TooltipWay.left ) ? ps.width : 0 );
                }

                if ( compMiddle - ps.height / 2 < windowSideSpacing )
                {
                    int cw = windowSideSpacing - ( compMiddle - ps.height / 2 );
                    cornerPeak = Math.max ( shadeWidth + round + cornerSideX + 1, getHeight () / 2 - cw );
                    setLocation ( compTipX, windowSideSpacing );
                }
                else if ( compMiddle + ps.height / 2 > getParent ().getHeight () - windowSideSpacing )
                {
                    int cw = ( compMiddle + ps.height / 2 ) - ( getParent ().getHeight () - windowSideSpacing );
                    cornerPeak = Math.min ( ps.height - shadeWidth - round - cornerSideX - 1, getHeight () / 2 + cw );
                    setLocation ( compTipX, getParent ().getHeight () - windowSideSpacing - ps.height );
                }
                else
                {
                    cornerPeak = getHeight () / 2;
                    setLocation ( compTipX, compMiddle - ps.height / 2 );
                }
            }
            setSize ( getPreferredSize () );
        }
    }

    /**
     * Custom display location relative to component
     */

    public Point getDisplayLocation ()
    {
        return displayLocation;
    }

    public void setDisplayLocation ( int x, int y )
    {
        setDisplayLocation ( new Point ( x, y ) );
    }

    public void setDisplayLocation ( Point displayLocation )
    {
        this.displayLocation = displayLocation;
        updateLocation ();
    }

    /**
     * Minimal spacing between tooltip and window edge
     */

    public int getWindowSideSpacing ()
    {
        return windowSideSpacing;
    }

    public void setWindowSideSpacing ( int windowSideSpacing )
    {
        this.windowSideSpacing = windowSideSpacing;
        updateLocation ();
    }

    /**
     * Tooltip corners rounding
     */

    public int getRound ()
    {
        return round;
    }

    public void setRound ( int round )
    {
        this.round = round;
        updateLocation ();
    }

    /**
     * Component to which this tooltip attached
     */

    public Component getComponent ()
    {
        return component.get ();
    }

    /**
     * Tooltip unique id
     */

    public String getId ()
    {
        return id;
    }

    /**
     * Tooltip component
     */

    public JComponent getTooltip ()
    {
        return tooltip;
    }

    public void setTooltip ( String tooltip )
    {
        // Updating label tooltip text
        if ( this.tooltip != null && this.tooltip instanceof JLabel )
        {
            ( ( JLabel ) this.tooltip ).setText ( tooltip );
            updateBorder ();
            updateLocation ();
        }
    }

    public void setTooltip ( JComponent tooltip )
    {
        // Removing old tooltip
        if ( this.tooltip != null )
        {
            remove ( this.tooltip );
        }

        // Adding new tooltip component
        this.tooltip = tooltip;
        add ( tooltip, BorderLayout.CENTER );
        updateBorder ();
        updateLocation ();
    }

    /**
     * Tooltip display way
     */

    public TooltipWay getDisplayWay ()
    {
        return displayWay;
    }

    public void setDisplayWay ( TooltipWay displayWay )
    {
        this.displayWay = displayWay;
        updateBorder ();
        updateLocation ();
    }

    /**
     * Should display component hotkey on tooltip or not
     */

    public boolean isShowHotkey ()
    {
        return showHotkey;
    }

    public void setShowHotkey ( boolean showHotkey )
    {
        this.showHotkey = showHotkey;
        updateHotkey ();
        updateBorder ();
        updateLocation ();
    }

    /**
     * Hotkey display location
     */

    public int getHotkeyLocation ()
    {
        return hotkeyLocation;
    }

    public void setHotkeyLocation ( int hotkeyLocation )
    {
        this.hotkeyLocation = hotkeyLocation;
        updateHotkey ();
        updateBorder ();
        updateLocation ();
    }

    /**
     * Default tooltip close behavior
     */

    public boolean isDefaultCloseBehavior ()
    {
        return defaultCloseBehavior;
    }

    public void setDefaultCloseBehavior ( boolean defaultCloseBehavior )
    {
        this.defaultCloseBehavior = defaultCloseBehavior;
    }

    /**
     * Tooltip text color
     */

    public Color getTextColor ()
    {
        return textColor;
    }

    public void setTextColor ( Color textColor )
    {
        //todo
        this.textColor = textColor;
    }

    /**
     * Hotkey text color
     */

    public Color getHotkeyColor ()
    {
        return hotkey.getForeground ();
    }

    public void setHotkeyColor ( Color hotkeyColor )
    {
        hotkey.setForeground ( hotkeyColor );
    }

    /**
     * Tooltip top background color
     */

    public Color getTopBgColor ()
    {
        return topBgColor;
    }

    public void setTopBgColor ( Color topBgColor )
    {
        this.topBgColor = topBgColor;
    }

    /**
     * Tooltip bottom background color
     */

    public Color getBottomBgColor ()
    {
        return bottomBgColor;
    }

    public void setBottomBgColor ( Color bottomBgColor )
    {
        this.bottomBgColor = bottomBgColor;
    }

    /**
     * Tooltip border color
     */

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( Color borderColor )
    {
        this.borderColor = borderColor;
    }

    /**
     * Tooltip background transparency
     */

    public float getTrasparency ()
    {
        return trasparency;
    }

    public void setTrasparency ( float trasparency )
    {
        this.trasparency = trasparency;
    }

    /**
     * Shape provider
     */

    @Override
    public Shape provideShape ()
    {
        return getTooltipShape ( getActualDisplayWay (), true );
    }

    /**
     * Tooltip background painting
     */

    @Override
    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        // Draw settings
        Graphics2D g2d = ( Graphics2D ) g;
        Object aa = LafUtils.setupAntialias ( g2d );

        // Fade animation and transparency
        if ( fade < 1f )
        {
            LafUtils.setupAlphaComposite ( g2d, fade );
        }
        Composite oc = null;
        if ( trasparency < 1f )
        {
            oc = LafUtils.setupAlphaComposite ( g2d, trasparency );
        }

        // Tooltip settings
        TooltipWay displayWay = getActualDisplayWay ();

        // Initialize border and shade shape
        Shape bs = null;
        if ( shadeWidth > 0 && shadeColor != null )
        {
            bs = getTooltipShape ( displayWay, false );
        }

        // Shade
        LafUtils.drawShade ( g2d, bs, WebCustomTooltipStyle.shadeType, shadeColor, shadeWidth );

        // Background
        if ( topBgColor != null )
        {
            if ( bottomBgColor != null )
            {
                // Gradient background
                if ( displayWay.equals ( TooltipWay.down ) )
                {
                    g2d.setPaint ( new GradientPaint ( 0, getHeight () * 2 / 3, topBgColor, 0, getHeight (), bottomBgColor ) );
                }
                else
                {
                    g2d.setPaint ( new GradientPaint ( 0, getHeight () / 3, topBgColor, 0, getHeight (), bottomBgColor ) );
                }
            }
            else
            {
                // Single color background
                g2d.setPaint ( topBgColor );
            }
            //            g2d.fill ( bs );
            g2d.fill ( getTooltipShape ( displayWay, true ) );
        }

        // Border
        if ( borderColor != null )
        {
            g2d.setPaint ( borderColor );
            g2d.draw ( bs );
        }

        // Restore composite
        if ( oc != null )
        {
            LafUtils.restoreComposite ( g2d, oc );
        }

        // Restore antialias
        LafUtils.restoreAntialias ( g2d, aa );
    }

    private Shape getTooltipShape ( TooltipWay displayWay, boolean fill )
    {
        Area borderShape;

        // Fill shape extend
        float fillExtend = fill ? 1f : 0;

        // Content area shape
        float widthMinus =
                ( displayWay.equals ( TooltipWay.left ) || displayWay.equals ( TooltipWay.right ) ? cornerLength + 1 : 1 ) - fillExtend +
                        shadeWidth * 2;
        float heightMinus =
                ( displayWay.equals ( TooltipWay.up ) || displayWay.equals ( TooltipWay.down ) ? cornerLength + 1 : 1 ) - fillExtend +
                        shadeWidth * 2;
        borderShape = new Area ( new RoundRectangle2D.Double ( shadeWidth + ( displayWay.equals ( TooltipWay.right ) ? cornerLength : 0 ),
                shadeWidth + ( displayWay.equals ( TooltipWay.down ) ? cornerLength : 0 ), getWidth () - widthMinus,
                getHeight () - heightMinus, round * 2, round * 2 ) );

        // Corner shape
        fillExtend = fill ? 0.5f : 0;
        GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        if ( displayWay.equals ( TooltipWay.up ) )
        {
            gp.moveTo ( cornerPeak + fillExtend, getHeight () - shadeWidth - 1 );
            gp.lineTo ( cornerPeak - cornerSideX + fillExtend, getHeight () - shadeWidth - 1 - cornerLength );
            gp.lineTo ( cornerPeak + cornerSideX + fillExtend, getHeight () - shadeWidth - 1 - cornerLength );
        }
        else if ( displayWay.equals ( TooltipWay.down ) )
        {
            gp.moveTo ( cornerPeak, shadeWidth );
            gp.lineTo ( cornerPeak - cornerSideX, shadeWidth + cornerLength );
            gp.lineTo ( cornerPeak + cornerSideX, shadeWidth + cornerLength );
        }
        else if ( displayWay.equals ( TooltipWay.left ) )
        {
            gp.moveTo ( getWidth () - shadeWidth - 1, cornerPeak + fillExtend );
            gp.lineTo ( getWidth () - shadeWidth - 1 - cornerLength, cornerPeak + fillExtend - cornerSideX );
            gp.lineTo ( getWidth () - shadeWidth - 1 - cornerLength, cornerPeak + fillExtend + cornerSideX );
        }
        else if ( displayWay.equals ( TooltipWay.right ) )
        {
            gp.moveTo ( shadeWidth, cornerPeak );
            gp.lineTo ( shadeWidth + cornerLength, cornerPeak - cornerSideX );
            gp.lineTo ( shadeWidth + cornerLength, cornerPeak + cornerSideX );
        }
        gp.closePath ();
        borderShape.add ( new Area ( gp ) );

        return borderShape;
    }

    /**
     * Tooltip listeners
     */

    public void addTooltipListener ( TooltipListener listener )
    {
        listeners.add ( listener );
    }

    public void removeTooltipListener ( TooltipListener listener )
    {
        listeners.remove ( listener );
    }

    public void removeAllTooltipListeners ()
    {
        listeners.clear ();
    }

    private void fireTooltipShown ()
    {
        for ( TooltipListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.tooltipShowing ();
        }
    }

    private void fireTooltipFullyShown ()
    {
        for ( TooltipListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.tooltipShown ();
        }
    }

    private void fireTooltipHidden ()
    {
        for ( TooltipListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.tooltipHidden ();
        }
    }

    private void fireTooltipDestroyed ()
    {
        for ( TooltipListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.tooltipDestroyed ();
        }
    }
}
