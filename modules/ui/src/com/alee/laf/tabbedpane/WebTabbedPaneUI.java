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

package com.alee.laf.tabbedpane;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.global.StyleConstants;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public class WebTabbedPaneUI extends BasicTabbedPaneUI implements ShapeProvider, BorderMethods
{
    private TabbedPaneStyle tabbedPaneStyle = WebTabbedPaneStyle.tabbedPaneStyle;

    private Color selectedTopBg = WebTabbedPaneStyle.selectedTopBg;
    private Color selectedBottomBg = WebTabbedPaneStyle.selectedBottomBg;
    private Color topBg = WebTabbedPaneStyle.topBg;
    private Color bottomBg = WebTabbedPaneStyle.bottomBg;
    private int round = WebTabbedPaneStyle.round;
    private int shadeWidth = WebTabbedPaneStyle.shadeWidth;
    private boolean rotateTabInsets = WebTabbedPaneStyle.rotateTabInsets;
    private Insets contentInsets = WebTabbedPaneStyle.contentInsets;
    private Insets tabInsets = WebTabbedPaneStyle.tabInsets;
    private Painter painter = WebTabbedPaneStyle.painter;
    private int tabRunIndent = WebTabbedPaneStyle.tabRunIndent;
    private int tabOverlay = WebTabbedPaneStyle.tabOverlay;
    private TabStretchType tabStretchType = WebTabbedPaneStyle.tabStretchType;
    private Color tabBorderColor = WebTabbedPaneStyle.tabBorderColor;
    private Color contentBorderColor = WebTabbedPaneStyle.contentBorderColor;
    private boolean paintBorderOnlyOnSelectedTab = WebTabbedPaneStyle.paintBorderOnlyOnSelectedTab;
    private boolean forceUseSelectedTabBgColors = WebTabbedPaneStyle.forceUseSelectedTabBgColors;
    private Color backgroundColor = WebTabbedPaneStyle.backgroundColor;
    private boolean paintOnlyTopBorder = WebTabbedPaneStyle.paintOnlyTopBorder;

    private final Map<Integer, Color> selectedForegroundAt = new HashMap<Integer, Color> ();
    private final Map<Integer, Painter> backgroundPainterAt = new HashMap<Integer, Painter> ();

    private FocusAdapter focusAdapter;
    //    private MouseAdapter mouseAdapter;
    //    private int rolloverTab = -1;

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTabbedPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( tabPane );
        tabPane.setBackground ( backgroundColor );
        PainterSupport.installPainter ( tabPane, this.painter );

        // Updating border
        updateBorder ();

        // Focus updater
        focusAdapter = new FocusAdapter ()
        {
            @Override
            public void focusGained ( final FocusEvent e )
            {
                tabPane.repaint ();
            }

            @Override
            public void focusLost ( final FocusEvent e )
            {
                tabPane.repaint ();
            }
        };
        tabPane.addFocusListener ( focusAdapter );

        //        mouseAdapter = new MouseAdapter ()
        //        {
        //            public void mouseEntered ( MouseEvent e )
        //            {
        //                updateRolloverTab ( e );
        //            }
        //
        //            public void mouseExited ( MouseEvent e )
        //            {
        //                updateRolloverTab ( e );
        //            }
        //
        //            public void mouseMoved ( MouseEvent e )
        //            {
        //                updateRolloverTab ( e );
        //            }
        //
        //            public void mouseDragged ( MouseEvent e )
        //            {
        //                updateRolloverTab ( e );
        //            }
        //        };
        //        c.addMouseListener ( mouseAdapter );
        //        c.addMouseMotionListener ( mouseAdapter );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        PainterSupport.uninstallPainter ( tabPane, this.painter );

        if ( focusAdapter != null )
        {
            c.removeFocusListener ( focusAdapter );
        }
        //        if ( mouseAdapter != null )
        //        {
        //            c.removeMouseListener ( mouseAdapter );
        //            c.removeMouseMotionListener ( mouseAdapter );
        //        }

        super.uninstallUI ( c );
    }

    @Override
    public Shape provideShape ()
    {
        return LafUtils.getWebBorderShape ( tabPane, getShadeWidth (), getRound () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( tabPane != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( tabPane ) )
            {
                return;
            }

            final Insets bgInsets = getBackgroundInsets ( tabPane );
            if ( tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) )
            {
                // Standalone style border
                tabPane.setBorder ( LafUtils.createWebBorder (
                        SwingUtils.max ( bgInsets, new Insets ( shadeWidth, shadeWidth, shadeWidth, shadeWidth ) ) ) );
            }
            else
            {
                // Attached style border
                tabPane.setBorder ( LafUtils.createWebBorder ( bgInsets ) );
            }
        }
    }

    private Insets getBackgroundInsets ( final JComponent c )
    {
        return painter != null ? painter.getMargin ( c ) : new Insets ( 0, 0, 0, 0 );
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    public boolean isRotateTabInsets ()
    {
        return rotateTabInsets;
    }

    public void setRotateTabInsets ( final boolean rotateTabInsets )
    {
        this.rotateTabInsets = rotateTabInsets;
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
    }

    public Insets getContentInsets ()
    {
        return contentInsets;
    }

    public void setContentInsets ( final Insets contentInsets )
    {
        this.contentInsets = contentInsets;
    }

    public Insets getTabInsets ()
    {
        return tabInsets;
    }

    public void setTabInsets ( final Insets tabInsets )
    {
        this.tabInsets = tabInsets;
    }

    public Color getSelectedTopBg ()
    {
        return selectedTopBg;
    }

    public void setSelectedTopBg ( final Color selectedTopBg )
    {
        this.selectedTopBg = selectedTopBg;
    }

    public Color getSelectedBottomBg ()
    {
        return selectedBottomBg;
    }

    public void setSelectedBottomBg ( final Color selectedBottomBg )
    {
        this.selectedBottomBg = selectedBottomBg;
    }

    public Color getTopBg ()
    {
        return topBg;
    }

    public void setTopBg ( final Color topBg )
    {
        this.topBg = topBg;
    }

    public Color getBottomBg ()
    {
        return bottomBg;
    }

    public void setBottomBg ( final Color bottomBg )
    {
        this.bottomBg = bottomBg;
    }

    public void setSelectedForegroundAt ( final int tabIndex, final Color foreground )
    {
        selectedForegroundAt.put ( tabIndex, foreground );
    }

    public Color getSelectedForegroundAt ( final int tabIndex )
    {
        return selectedForegroundAt.get ( tabIndex );
    }

    public void setBackgroundPainterAt ( final int tabIndex, final Painter painter )
    {
        backgroundPainterAt.put ( tabIndex, painter );
    }

    public Painter getBackgroundPainterAt ( final int tabIndex )
    {
        return backgroundPainterAt.get ( tabIndex );
    }

    public TabbedPaneStyle getTabbedPaneStyle ()
    {
        return tabbedPaneStyle;
    }

    public void setTabbedPaneStyle ( final TabbedPaneStyle tabbedPaneStyle )
    {
        this.tabbedPaneStyle = tabbedPaneStyle;
        updateBorder ();
    }

    //    private void updateRolloverTab ( MouseEvent e )
    //    {
    //        if ( tabPane != null )
    //        {
    //            int old = rolloverTab;
    //            rolloverTab = tabForCoordinate ( tabPane, e.getX (), e.getY () );
    //            if ( old != rolloverTab )
    //            {
    //                tabPane.repaint ();
    //            }
    //        }
    //    }

    public Painter getPainter ()
    {
        return painter;
    }

    public void setPainter ( final Painter painter )
    {
        PainterSupport.uninstallPainter ( tabPane, this.painter );

        this.painter = painter;
        PainterSupport.installPainter ( tabPane, this.painter );
        updateBorder ();
    }

    public int getTabRunIndent ()
    {
        return tabRunIndent;
    }

    public void setTabRunIndent ( final int tabRunIndent )
    {
        this.tabRunIndent = tabRunIndent;
    }

    public int getTabOverlay ()
    {
        return tabOverlay;
    }

    public void setTabOverlay ( final int tabOverlay )
    {
        this.tabOverlay = tabOverlay;
    }

    public TabStretchType getTabStretchType ()
    {
        return tabStretchType;
    }

    public void setTabStretchType ( final TabStretchType tabStretchType )
    {
        this.tabStretchType = tabStretchType;
    }

    public Color getTabBorderColor ()
    {
        return tabBorderColor;
    }

    public void setTabBorderColor ( final Color tabBorderColor )
    {
        this.tabBorderColor = tabBorderColor;
    }

    public Color getContentBorderColor ()
    {
        return contentBorderColor;
    }

    public void setContentBorderColor ( final Color contentBorderColor )
    {
        this.contentBorderColor = contentBorderColor;
    }

    public boolean isPaintBorderOnlyOnSelectedTab ()
    {
        return paintBorderOnlyOnSelectedTab;
    }

    public void setPaintBorderOnlyOnSelectedTab ( final boolean paintBorderOnlyOnSelectedTab )
    {
        this.paintBorderOnlyOnSelectedTab = paintBorderOnlyOnSelectedTab;
    }

    public boolean isForceUseSelectedTabBgColors ()
    {
        return forceUseSelectedTabBgColors;
    }

    public void setForceUseSelectedTabBgColors ( final boolean forceUseSelectedTabBgColors )
    {
        this.forceUseSelectedTabBgColors = forceUseSelectedTabBgColors;
    }

    public Color getBackgroundColor ()
    {
        return backgroundColor;
    }

    public void setBackgroundColor ( final Color backgroundColor )
    {
        this.backgroundColor = backgroundColor;
    }

    public boolean isPaintOnlyTopBorder ()
    {
        return paintOnlyTopBorder;
    }

    public void setPaintOnlyTopBorder ( final boolean paintOnlyTopBorder )
    {
        this.paintOnlyTopBorder = paintOnlyTopBorder;
    }

    @Override
    protected int getTabRunIndent ( final int tabPlacement, final int run )
    {
        return tabRunIndent;
    }

    @Override
    protected int getTabRunOverlay ( final int tabPlacement )
    {
        return tabOverlay;
    }

    @Override
    protected boolean shouldPadTabRun ( final int tabPlacement, final int run )
    {
        return !tabStretchType.equals ( TabStretchType.never ) &&
                ( tabStretchType.equals ( TabStretchType.always ) || tabStretchType.equals ( TabStretchType.multiline ) && runCount > 1 );
    }

    @Override
    protected boolean shouldRotateTabRuns ( final int tabPlacement )
    {
        // todo Requires style changes
        return true;
    }

    @Override
    protected Insets getContentBorderInsets ( final int tabPlacement )
    {
        if ( tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) )
        {
            final Insets insets;
            if ( tabPlacement == JTabbedPane.TOP )
            {
                insets = new Insets ( 1, 2, 1, 2 );
            }
            else if ( tabPlacement == JTabbedPane.BOTTOM )
            {
                insets = new Insets ( 2, 2, 0, 2 );
            }
            else if ( tabPlacement == JTabbedPane.LEFT )
            {
                insets = new Insets ( 2, 1, 2, 1 );
            }
            else if ( tabPlacement == JTabbedPane.RIGHT )
            {
                insets = new Insets ( 2, 2, 2, 0 );
            }
            else
            {
                insets = new Insets ( 0, 0, 0, 0 );
            }
            insets.top += contentInsets.top - 1;
            insets.left += contentInsets.left - 1;
            insets.bottom += contentInsets.bottom - 1;
            insets.right += contentInsets.right - 1;
            return insets;
        }
        else
        {
            return new Insets ( 0, 0, 0, 0 );
        }
    }

    @Override
    protected Insets getTabAreaInsets ( final int tabPlacement )
    {
        final Insets targetInsets = new Insets ( 0, 0, 0, 0 );
        rotateInsets ( tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) ? new Insets ( tabPlacement == RIGHT ? 1 : 0, 1, 0, 2 ) :
                new Insets ( -1, -1, 0, 0 ), targetInsets, tabPlacement );
        return targetInsets;
    }

    @Override
    protected Insets getTabInsets ( final int tabPlacement, final int tabIndex )
    {
        final Insets insets = SwingUtils.copy ( tabInsets );
        if ( tabIndex == 0 && tabPane.getSelectedIndex () == 0 )
        {
            // Fix for 1st element
            insets.left -= 1;
            insets.right += 1;
        }
        if ( rotateTabInsets )
        {
            final Insets targetInsets = new Insets ( 0, 0, 0, 0 );
            rotateInsets ( insets, targetInsets, tabPlacement );
            return targetInsets;
        }
        else
        {
            return insets;
        }
    }

    @Override
    protected Insets getSelectedTabPadInsets ( final int tabPlacement )
    {
        final Insets targetInsets = new Insets ( 0, 0, 0, 0 );
        rotateInsets ( tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) ? new Insets ( 2, 2, 2, 1 ) : new Insets ( 0, 0, 0, 0 ),
                targetInsets, tabPlacement );
        return targetInsets;
    }

    @Override
    protected int getTabLabelShiftX ( final int tabPlacement, final int tabIndex, final boolean isSelected )
    {
        if ( tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) )
        {
            return super.getTabLabelShiftX ( tabPlacement, tabIndex, isSelected );
        }
        else
        {
            return 0;
        }
    }

    @Override
    protected int getTabLabelShiftY ( final int tabPlacement, final int tabIndex, final boolean isSelected )
    {
        if ( tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) )
        {
            return super.getTabLabelShiftY ( tabPlacement, tabIndex, isSelected );
        }
        else
        {
            return 0;
        }
    }

    @Override
    protected void paintTabBorder ( final Graphics g, final int tabPlacement, final int tabIndex, final int x, final int y, final int w,
                                    final int h, final boolean isSelected )
    {
        // We don't need this one
    }

    @Override
    protected void paintTabBackground ( final Graphics g, final int tabPlacement, final int tabIndex, final int x, final int y, final int w,
                                        final int h, final boolean isSelected )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Border shape
        final GeneralPath borderShape = createTabShape ( TabShapeType.border, tabPlacement, x, y, w, h, isSelected );

        // Tab shade
        if ( tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) )
        {
            final GeneralPath shadeShape = createTabShape ( TabShapeType.shade, tabPlacement, x, y, w, h, isSelected );
            GraphicsUtils.drawShade ( g2d, shadeShape, StyleConstants.shadeColor, shadeWidth,
                    new Rectangle2D.Double ( 0, 0, tabPane.getWidth (), y + h ), round > 0 );
        }

        // Tab background
        final GeneralPath bgShape = createTabShape ( TabShapeType.background, tabPlacement, x, y, w, h, isSelected );
        if ( backgroundPainterAt.containsKey ( tabIndex ) && isSelected )
        {
            final Shape old = GraphicsUtils.intersectClip ( g2d, bgShape );
            final Painter bp = backgroundPainterAt.get ( tabIndex );
            bp.paint ( g2d, new Rectangle ( x, y, w, h ), tabPane );
            GraphicsUtils.restoreClip ( g2d, old );
        }
        else
        {
            final Point topPoint = getTopTabBgPoint ( tabPlacement, x, y, w, h );
            final Point bottomPoint = getBottomTabBgPoint ( tabPlacement, x, y, w, h );
            if ( isSelected )
            {
                if ( forceUseSelectedTabBgColors )
                {
                    g2d.setPaint (
                            new GradientPaint ( topPoint.x, topPoint.y, selectedTopBg, bottomPoint.x, bottomPoint.y, selectedBottomBg ) );
                }
                else
                {
                    Color bg = tabPane.getBackgroundAt ( tabIndex );
                    bg = bg != null ? bg : tabPane.getBackground ();
                    g2d.setPaint ( new GradientPaint ( topPoint.x, topPoint.y, selectedTopBg, bottomPoint.x, bottomPoint.y, bg ) );
                }
            }
            else
            {
                g2d.setPaint ( new GradientPaint ( topPoint.x, topPoint.y, topBg, bottomPoint.x, bottomPoint.y, bottomBg ) );
            }
            g2d.fill ( isSelected ? borderShape : bgShape );
        }

        // Tab border
        g2d.setPaint ( tabBorderColor );
        g2d.draw ( borderShape );

        // Tab focus
        final boolean drawFocus = isSelected && tabPane.isFocusOwner ();
        if ( tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) )
        {
            LafUtils.drawCustomWebFocus ( g2d, null, StyleConstants.focusType, borderShape, null, drawFocus );
        }
        //        else if ( drawFocus )
        //        {
        //            g2d.setPaint ( StyleConstants.fieldFocusColor );
        //            g2d.drawLine ( x, y + 1, x + w, y + 1 );
        //        }

        //todo Paint selected tab together with area

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    @Override
    protected void paintText ( final Graphics g, final int tabPlacement, final Font font, final FontMetrics metrics, final int tabIndex,
                               final String title, final Rectangle textRect, final boolean isSelected )
    {
        g.setFont ( font );
        final View v = getTextViewForTab ( tabIndex );
        if ( v != null )
        {
            // html
            v.paint ( g, textRect );
        }
        else
        {
            // plain text
            final int mnemIndex = tabPane.getDisplayedMnemonicIndexAt ( tabIndex );

            if ( tabPane.isEnabled () && tabPane.isEnabledAt ( tabIndex ) )
            {
                Color fg = tabPane.getForegroundAt ( tabIndex );
                if ( isSelected && ( fg instanceof UIResource ) )
                {
                    if ( selectedForegroundAt.containsKey ( tabIndex ) )
                    {
                        fg = selectedForegroundAt.get ( tabIndex );
                    }
                    else
                    {
                        final Color selectedFG = UIManager.getColor ( "TabbedPane.selectedForeground" );
                        if ( selectedFG != null )
                        {
                            fg = selectedFG;
                        }
                    }
                }
                g.setColor ( fg );
                SwingUtils.drawStringUnderlineCharAt ( g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent () );

            }
            else
            {
                // tab disabled
                g.setColor ( tabPane.getBackgroundAt ( tabIndex ).brighter () );
                SwingUtils.drawStringUnderlineCharAt ( g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent () );
                g.setColor ( tabPane.getBackgroundAt ( tabIndex ).darker () );
                SwingUtils.drawStringUnderlineCharAt ( g, title, mnemIndex, textRect.x - 1, textRect.y + metrics.getAscent () - 1 );
            }
        }
    }

    private GeneralPath createTabShape ( final TabShapeType tabShapeType, final int tabPlacement, int x, final int y, int w, final int h,
                                         final boolean isSelected )
    {
        // Fix for basic layouting of selected left-sided tab x coordinate
        final Insets insets = tabPane.getInsets ();
        if ( tabbedPaneStyle.equals ( TabbedPaneStyle.attached ) && isSelected )
        {
            // todo fix for other tabPlacement values aswell
            if ( tabPlacement == TOP && x == insets.left )
            {
                x = x - 1;
                w = w + 1;
            }
        }

        final int actualRound = tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) ? round : 0;
        final GeneralPath bgShape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );

        if ( !isSelected && paintBorderOnlyOnSelectedTab )
        {
            return bgShape;
        }

        if ( tabPlacement == JTabbedPane.TOP )
        {
            bgShape.moveTo ( x, y + h + getChange ( tabShapeType ) );
            bgShape.lineTo ( x, y + actualRound );
            bgShape.quadTo ( x, y, x + actualRound, y );
            bgShape.lineTo ( x + w - actualRound, y );
            bgShape.quadTo ( x + w, y, x + w, y + actualRound );
            bgShape.lineTo ( x + w, y + h + getChange ( tabShapeType ) );
        }
        else if ( tabPlacement == JTabbedPane.BOTTOM )
        {
            bgShape.moveTo ( x, y - getChange ( tabShapeType ) );
            bgShape.lineTo ( x, y + h - actualRound );
            bgShape.quadTo ( x, y + h, x + actualRound, y + h );
            bgShape.lineTo ( x + w - actualRound, y + h );
            bgShape.quadTo ( x + w, y + h, x + w, y + h - actualRound );
            bgShape.lineTo ( x + w, y - getChange ( tabShapeType ) );
        }
        else if ( tabPlacement == JTabbedPane.LEFT )
        {
            bgShape.moveTo ( x + w + getChange ( tabShapeType ), y );
            bgShape.lineTo ( x + actualRound, y );
            bgShape.quadTo ( x, y, x, y + actualRound );
            bgShape.lineTo ( x, y + h - actualRound );
            bgShape.quadTo ( x, y + h, x + actualRound, y + h );
            bgShape.lineTo ( x + w + getChange ( tabShapeType ), y + h );
        }
        else
        {
            bgShape.moveTo ( x - getChange ( tabShapeType ), y );
            bgShape.lineTo ( x + w - actualRound, y );
            bgShape.quadTo ( x + w, y, x + w, y + actualRound );
            bgShape.lineTo ( x + w, y + h - actualRound );
            bgShape.quadTo ( x + w, y + h, x + w - actualRound, y + h );
            bgShape.lineTo ( x - getChange ( tabShapeType ), y + h );
        }
        return bgShape;
    }

    private int getChange ( final TabShapeType tabShapeType )
    {
        if ( tabShapeType.equals ( TabShapeType.shade ) )
        {
            return -( round > 0 ? round : 1 );
        }
        else if ( tabShapeType.equals ( TabShapeType.border ) )
        {
            return -1;
        }
        else if ( tabShapeType.equals ( TabShapeType.backgroundPainter ) )
        {
            return 2;
        }
        else
        {
            return 0;
        }
    }

    private enum TabShapeType
    {
        shade,
        background,
        backgroundPainter,
        border
    }

    private Point getTopTabBgPoint ( final int tabPlacement, final int x, final int y, final int w, final int h )
    {
        if ( tabPlacement == JTabbedPane.TOP )
        {
            return new Point ( x, y );
        }
        else if ( tabPlacement == JTabbedPane.BOTTOM )
        {
            return new Point ( x, y + h );
        }
        else if ( tabPlacement == JTabbedPane.LEFT )
        {
            return new Point ( x, y );
        }
        else
        {
            return new Point ( x + w, y );
        }
    }

    private Point getBottomTabBgPoint ( final int tabPlacement, final int x, final int y, final int w, final int h )
    {
        if ( tabPlacement == JTabbedPane.TOP )
        {
            return new Point ( x, y + h - 4 );
        }
        else if ( tabPlacement == JTabbedPane.BOTTOM )
        {
            return new Point ( x, y + 4 );
        }
        else if ( tabPlacement == JTabbedPane.LEFT )
        {
            return new Point ( x + w - 4, y );
        }
        else
        {
            return new Point ( x + 4, y );
        }
    }

    @Override
    protected void paintContentBorder ( final Graphics g, final int tabPlacement, final int selectedIndex )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        final int tabAreaSize = getTabAreaLength ( tabPlacement );

        final Insets bi = tabPane.getInsets ();
        if ( tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM )
        {
            bi.right += 1;
        }
        else
        {
            bi.bottom += 1;
        }

        // Selected tab bounds
        final Rectangle selected = selectedIndex != -1 ? getTabBounds ( tabPane, selectedIndex ) : null;

        // Background shape
        final Shape bs = createBackgroundShape ( tabPlacement, tabAreaSize, bi, selected );

        if ( tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) )
        {
            // Proper clip
            final GeneralPath clip = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
            clip.append ( new Rectangle2D.Double ( 0, 0, tabPane.getWidth (), tabPane.getHeight () ), false );
            clip.append ( bs, false );
            GraphicsUtils.drawShade ( g2d, bs, StyleConstants.shadeColor, shadeWidth, clip, round > 0 );

            //            // Corners covering for large round
            //            if ( round > 2 )
            //            {
            //                Insets tai = getTabAreaInsets ( tabPlacement );
            //                if ( tabPlacement == TOP )
            //                {
            //                    if ( selected.x > bi.left )
            //                    {
            //                        g2d.setPaint ( bottomBg );
            //                        g2d.fillRect ( bi.left + tai.left, bi.top + tabAreaSize, round, round );
            //
            //                        g2d.setPaint ( StyleConstants.darkBorderColor );
            //                        g2d.drawLine ( bi.left + tai.left, bi.top + tabAreaSize, bi.left + tai.left,
            //                                bi.top + tabAreaSize + round );
            //                    }
            ////                    if ( selected.x + selected.width < tabPane.getWidth () - bi.right )
            ////                    {
            ////                        g2d.setPaint ( bottomBg );
            ////                        g2d.fillRect ( bi.rightbi.left + tai.left, bi.top + tabAreaSize, round, round );
            ////
            ////                        g2d.setPaint ( StyleConstants.darkBorderColor );
            ////                        g2d.drawLine ( bi.left + tai.left, bi.top + tabAreaSize, bi.left + tai.left,
            ////                                bi.top + tabAreaSize + round );
            ////                    }
            //                }
            //            }

            // Area background
            if ( backgroundPainterAt.containsKey ( selectedIndex ) )
            {
                final Shape old = GraphicsUtils.intersectClip ( g2d, bs );
                backgroundPainterAt.get ( selectedIndex ).paint ( g2d, bs.getBounds (), tabPane );
                GraphicsUtils.restoreClip ( g2d, old );
            }
            else
            {
                final Color bg = selectedIndex != -1 ? tabPane.getBackgroundAt ( selectedIndex ) : null;
                g2d.setPaint ( bg != null ? bg : tabPane.getBackground () );
                g2d.fill ( bs );
            }

            // Area border
            g2d.setPaint ( contentBorderColor );
            g2d.draw ( bs );

            // Area focus
            LafUtils.drawCustomWebFocus ( g2d, null, StyleConstants.focusType, bs, null, tabPane.isFocusOwner () );
        }
        else
        {
            // Area background
            if ( backgroundPainterAt.containsKey ( selectedIndex ) )
            {
                backgroundPainterAt.get ( selectedIndex ).paint ( g2d, bs.getBounds (), tabPane );
            }
            else
            {
                final Color bg = selectedIndex != -1 ? tabPane.getBackgroundAt ( selectedIndex ) : null;
                g2d.setPaint ( bg != null ? bg : tabPane.getBackground () );
                g2d.fill ( bs );
            }

            // todo draw for other tabPlacement values aswell
            // Area border
            g2d.setPaint ( contentBorderColor );
            if ( tabPlacement == JTabbedPane.TOP )
            {
                if ( selected != null )
                {
                    if ( bi.left < selected.x )
                    {
                        g2d.drawLine ( bi.left, bi.top + tabAreaSize, selected.x, bi.top + tabAreaSize );
                    }
                    if ( selected.x + selected.width < tabPane.getWidth () - bi.right )
                    {
                        g2d.drawLine ( selected.x + selected.width, bi.top + tabAreaSize, tabPane.getWidth () - bi.right,
                                bi.top + tabAreaSize );
                    }
                }
                else
                {
                    g2d.drawLine ( bi.left, bi.top + tabAreaSize, tabPane.getWidth () - bi.right, bi.top + tabAreaSize );
                }
            }
            //            else if ( tabPlacement == JTabbedPane.BOTTOM )
            //            {
            //                //
            //            }
            //            else if ( tabPlacement == JTabbedPane.LEFT )
            //            {
            //                //
            //            }
            //            else if ( tabPlacement == JTabbedPane.RIGHT )
            //            {
            //                //
            //            }
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    public int getTabAreaLength ( final int tabPlacement )
    {
        return tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM ?
                calculateTabAreaHeight ( tabPlacement, runCount, maxTabHeight ) - 1 :
                calculateTabAreaWidth ( tabPlacement, runCount, maxTabWidth ) - 1;
    }

    private Shape createBackgroundShape ( final int tabPlacement, final int tabAreaSize, final Insets bi, final Rectangle selected )
    {
        if ( tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) )
        {
            if ( selected != null )
            {
                final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                if ( tabPlacement == JTabbedPane.TOP )
                {
                    final int topY = bi.top + tabAreaSize;
                    gp.moveTo ( selected.x, topY );
                    if ( selected.x > bi.left + round && round > 0 )
                    {
                        gp.lineTo ( bi.left + round, topY );
                        gp.quadTo ( bi.left, topY, bi.left, topY + round );
                    }
                    else
                    {
                        gp.lineTo ( bi.left, topY );
                    }
                    if ( round > 0 )
                    {
                        gp.lineTo ( bi.left, tabPane.getHeight () - bi.bottom - round );
                        gp.quadTo ( bi.left, tabPane.getHeight () - bi.bottom, bi.left + round, tabPane.getHeight () - bi.bottom );
                        gp.lineTo ( tabPane.getWidth () - bi.right - round, tabPane.getHeight () - bi.bottom );
                        gp.quadTo ( tabPane.getWidth () - bi.right, tabPane.getHeight () - bi.bottom, tabPane.getWidth () - bi.right,
                                tabPane.getHeight () - bi.bottom - round );
                    }
                    else
                    {
                        if ( paintOnlyTopBorder )
                        {
                            gp.moveTo ( tabPane.getWidth () - bi.right, tabPane.getHeight () - bi.bottom );
                        }
                        else
                        {
                            gp.lineTo ( bi.left, tabPane.getHeight () - bi.bottom );
                            gp.lineTo ( tabPane.getWidth () - bi.right, tabPane.getHeight () - bi.bottom );
                        }
                    }
                    if ( selected.x + selected.width < tabPane.getWidth () - bi.right - round && round > 0 )
                    {
                        gp.lineTo ( tabPane.getWidth () - bi.right, topY + round );
                        gp.quadTo ( tabPane.getWidth () - bi.right, topY, tabPane.getWidth () - bi.right - round, topY );
                    }
                    else
                    {
                        if ( paintOnlyTopBorder )
                        {
                            gp.moveTo ( tabPane.getWidth () - bi.right, topY );
                        }
                        else
                        {
                            gp.lineTo ( tabPane.getWidth () - bi.right, topY );
                        }
                    }
                    gp.lineTo ( selected.x + selected.width, topY );
                }
                else if ( tabPlacement == JTabbedPane.BOTTOM )
                {
                    final int bottomY = tabPane.getHeight () - bi.bottom - tabAreaSize;
                    gp.moveTo ( selected.x, bottomY );
                    if ( selected.x > bi.left + round && round > 0 )
                    {
                        gp.lineTo ( bi.left + round, bottomY );
                        gp.quadTo ( bi.left, bottomY, bi.left, bottomY - round );
                    }
                    else
                    {
                        gp.lineTo ( bi.left, bottomY );
                    }
                    if ( round > 0 )
                    {
                        gp.lineTo ( bi.left, bi.top + round );
                        gp.quadTo ( bi.left, bi.top, bi.left + round, bi.top );
                        gp.lineTo ( tabPane.getWidth () - bi.right - round, bi.top );
                        gp.quadTo ( tabPane.getWidth () - bi.right, bi.top, tabPane.getWidth () - bi.right, bi.top + round );
                    }
                    else
                    {
                        gp.lineTo ( bi.left, bi.top );
                        gp.lineTo ( tabPane.getWidth () - bi.right, bi.top );
                    }
                    if ( selected.x + selected.width < tabPane.getWidth () - bi.right - round && round > 0 )
                    {
                        gp.lineTo ( tabPane.getWidth () - bi.right, bottomY - round );
                        gp.quadTo ( tabPane.getWidth () - bi.right, bottomY, tabPane.getWidth () - bi.right - round, bottomY );
                    }
                    else
                    {
                        gp.lineTo ( tabPane.getWidth () - bi.right, bottomY );
                    }
                    gp.lineTo ( selected.x + selected.width, bottomY );
                }
                else if ( tabPlacement == JTabbedPane.LEFT )
                {
                    final int leftX = bi.left + tabAreaSize;
                    gp.moveTo ( leftX, selected.y );
                    if ( selected.y > bi.top + round && round > 0 )
                    {
                        gp.lineTo ( leftX, bi.top + round );
                        gp.quadTo ( leftX, bi.top, leftX + round, bi.top );
                    }
                    else
                    {
                        gp.lineTo ( leftX, bi.top );
                    }
                    if ( round > 0 )
                    {
                        gp.lineTo ( tabPane.getWidth () - bi.right - round, bi.top );
                        gp.quadTo ( tabPane.getWidth () - bi.right, bi.top, tabPane.getWidth () - bi.right, bi.top + round );
                        gp.lineTo ( tabPane.getWidth () - bi.right, tabPane.getHeight () - bi.bottom - round );
                        gp.quadTo ( tabPane.getWidth () - bi.right, tabPane.getHeight () - bi.bottom,
                                tabPane.getWidth () - bi.right - round, tabPane.getHeight () - bi.bottom );
                    }
                    else
                    {
                        gp.lineTo ( tabPane.getWidth () - bi.right, bi.top );
                        gp.lineTo ( tabPane.getWidth () - bi.right, tabPane.getHeight () - bi.bottom );
                    }
                    if ( selected.y + selected.height < tabPane.getHeight () - bi.bottom - round && round > 0 )
                    {
                        gp.lineTo ( leftX + round, tabPane.getHeight () - bi.bottom );
                        gp.quadTo ( leftX, tabPane.getHeight () - bi.bottom, leftX, tabPane.getHeight () - bi.bottom - round );
                    }
                    else
                    {
                        gp.lineTo ( leftX, tabPane.getHeight () - bi.bottom );
                    }
                    gp.lineTo ( leftX, selected.y + selected.height );
                }
                else
                {
                    final int rightX = tabPane.getWidth () - bi.right - tabAreaSize;
                    gp.moveTo ( rightX, selected.y );
                    if ( selected.y > bi.top + round && round > 0 )
                    {
                        gp.lineTo ( rightX, bi.top + round );
                        gp.quadTo ( rightX, bi.top, rightX - round, bi.top );
                    }
                    else
                    {
                        gp.lineTo ( rightX, bi.top );
                    }
                    if ( round > 0 )
                    {
                        gp.lineTo ( bi.left + round, bi.top );
                        gp.quadTo ( bi.left, bi.top, bi.left, bi.top + round );
                        gp.lineTo ( bi.left, tabPane.getHeight () - bi.bottom - round );
                        gp.quadTo ( bi.left, tabPane.getHeight () - bi.bottom, bi.left + round, tabPane.getHeight () - bi.bottom );
                    }
                    else
                    {
                        gp.lineTo ( bi.left, bi.top );
                        gp.lineTo ( bi.left, tabPane.getHeight () - bi.bottom );
                    }
                    if ( selected.y + selected.height < tabPane.getHeight () - bi.bottom - round && round > 0 )
                    {
                        gp.lineTo ( rightX - round, tabPane.getHeight () - bi.bottom );
                        gp.quadTo ( rightX, tabPane.getHeight () - bi.bottom, rightX, tabPane.getHeight () - bi.bottom - round );
                    }
                    else
                    {
                        gp.lineTo ( rightX, tabPane.getHeight () - bi.bottom );
                    }
                    gp.lineTo ( rightX, selected.y + selected.height );
                }
                return gp;
            }
            else
            {
                final boolean top = tabPlacement == JTabbedPane.TOP;
                final boolean bottom = tabPlacement == JTabbedPane.BOTTOM;
                final boolean left = tabPlacement == JTabbedPane.LEFT;
                final boolean right = tabPlacement == JTabbedPane.RIGHT;
                return new RoundRectangle2D.Double ( bi.left + ( left ? tabAreaSize : 0 ), bi.top + ( top ? tabAreaSize : 0 ),
                        tabPane.getWidth () - bi.left - bi.right -
                                ( left || right ? tabAreaSize : 0 ), tabPane.getHeight () - bi.top - bi.bottom -
                        ( top || bottom ? tabAreaSize : 0 ), round * 2, round * 2 );
            }
        }
        else
        {
            final int x = bi.left + ( tabPlacement == JTabbedPane.LEFT ? tabAreaSize : 0 );
            final int y = bi.top + ( tabPlacement == JTabbedPane.TOP ? tabAreaSize : 0 );
            final int width = tabPane.getWidth () - bi.left - bi.right -
                    ( tabPlacement == JTabbedPane.LEFT || tabPlacement == JTabbedPane.RIGHT ? tabAreaSize : 0 );
            final int height = tabPane.getHeight () - bi.top - bi.bottom -
                    ( tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM ? tabAreaSize : 0 );
            return new Rectangle ( x, y, width + 1, height );
        }
    }

    public Shape getContentClip ()
    {
        Shape clip = null;

        final int tabPlacement = tabPane.getTabPlacement ();
        final int tabAreaLength = getTabAreaLength ( tabPlacement );
        final Insets insets = tabPane.getInsets ();

        if ( tabPlacement == JTabbedPane.TOP )
        {
            clip = new RoundRectangle2D.Double ( insets.left, insets.top + tabAreaLength, tabPane.getWidth () - insets.left - insets.right,
                    tabPane.getHeight () - insets.top - tabAreaLength - insets.bottom, round * 2, round * 2 );
        }
        else if ( tabPlacement == JTabbedPane.BOTTOM )
        {
            clip = new RoundRectangle2D.Double ( insets.left, insets.top, tabPane.getWidth () - insets.left - insets.right,
                    tabPane.getHeight () - insets.top - tabAreaLength - insets.bottom, round * 2, round * 2 );
        }
        else if ( tabPlacement == JTabbedPane.LEFT )
        {
            clip = new RoundRectangle2D.Double ( insets.left + tabAreaLength, insets.top,
                    tabPane.getWidth () - insets.left - tabAreaLength - insets.right, tabPane.getHeight () - insets.top - insets.bottom,
                    round * 2, round * 2 );
        }
        else if ( tabPlacement == JTabbedPane.RIGHT )
        {
            clip = new RoundRectangle2D.Double ( insets.left, insets.top, tabPane.getWidth () - insets.left - tabAreaLength - insets.right,
                    tabPane.getHeight () - insets.top - insets.bottom, round * 2, round * 2 );
        }

        return clip;
    }

    @Override
    protected void paintFocusIndicator ( final Graphics g, final int tabPlacement, final Rectangle[] rects, final int tabIndex,
                                         final Rectangle iconRect, final Rectangle textRect, final boolean isSelected )
    {
        // We don't need this one
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        // Background painter
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c );
        }

        // Basic paintings
        final Map hints = SwingUtils.setupTextAntialias ( g );
        super.paint ( g, c );
        SwingUtils.restoreTextAntialias ( g, hints );
    }

    //    protected void setRolloverTab ( int index )
    //    {
    //        super.setRolloverTab ( index );
    //
    //        // todo Animate rollover
    //    }

    //    public Dimension getPreferredSize ( JComponent c )
    //    {
    //        if ( tabPane.getTabPlacement () == JTabbedPane.TOP ||
    //                tabPane.getTabPlacement () == JTabbedPane.BOTTOM )
    //        {
    //            getTab
    //        }else {
    //
    //        }
    //    }

    //    protected LayoutManager createLayoutManager ()
    //    {
    //        return new TabbedPaneLayout ();
    //    }
    //
    //    public class TabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout
    //    {
    //
    //        public TabbedPaneLayout ()
    //        {
    //            WebTabbedPaneUI.this.super ();
    //        }
    //
    //        protected void normalizeTabRuns ( int tabPlacement, int tabCount, int start, int max )
    //        {
    //            // Only normalize the runs for top & bottom;  normalizing
    //            // doesn't look right for Metal's vertical tabs
    //            // because the last run isn't padded and it looks odd to have
    //            // fat tabs in the first vertical runs, but slimmer ones in the
    //            // last (this effect isn't noticeable for horizontal tabs).
    //            if ( tabPlacement == TOP || tabPlacement == BOTTOM )
    //            {
    //                super.normalizeTabRuns ( tabPlacement, tabCount, start, max );
    //            }
    //        }
    //
    //        // Don't rotate runs!
    //        protected void rotateTabRuns ( int tabPlacement, int selectedRun )
    //        {
    //        }
    //
    //        // Don't pad selected tab
    //        protected void padSelectedTab ( int tabPlacement, int selectedIndex )
    //        {
    //        }
    //    }

    //    public static void main ( String[] args )
    //    {
    //        new TestFrame ( new WebTabbedPane (  ){
    //            {
    //                getWebUI ().setTabbedPaneStyle ( TabbedPaneStyle.attached );
    //                addTab ( "Tab 1", new WebTabbedPane (  ){
    //                    {
    //                        setTabPlacement ( LEFT );
    //                        getWebUI ().setTabbedPaneStyle ( TabbedPaneStyle.attached );
    //                        addTab ( "Tab 1", new JLabel (  ) );
    //                        addTab ( "Tab 2", new JLabel (  ) );
    //                        addTab ( "Tab 3", new JLabel (  ) );
    //                        addTab ( "Tab 4", new JLabel (  ) );
    //                    }
    //                } );
    //                addTab ( "Tab 2",  new WebTabbedPane (  ){
    //                    {
    //                        setTabPlacement ( RIGHT );
    //                        getWebUI ().setTabbedPaneStyle ( TabbedPaneStyle.attached );
    //                        addTab ( "Tab 1", new JLabel (  ) );
    //                        addTab ( "Tab 2", new JLabel (  ) );
    //                        addTab ( "Tab 3", new JLabel (  ) );
    //                        addTab ( "Tab 4", new JLabel (  ) );
    //                    }
    //                } );
    //                addTab ( "Tab 3",  new WebTabbedPane (  ){
    //                    {
    //                        setTabPlacement ( BOTTOM );
    //                        getWebUI ().setTabbedPaneStyle ( TabbedPaneStyle.attached );
    //                        addTab ( "Tab 1", new JLabel (  ) );
    //                        addTab ( "Tab 2", new JLabel (  ) );
    //                        addTab ( "Tab 3", new JLabel (  ) );
    //                        addTab ( "Tab 4", new JLabel (  ) );
    //                    }
    //                } );
    //                addTab ( "Tab 4", new JLabel (  ) );
    //            }
    //        });
    //    }
}
