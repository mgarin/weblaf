package com.alee.laf.tabbedpane;

import com.alee.global.StyleConstants;
import com.alee.painter.AbstractPainter;
import com.alee.painter.Painter;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.WebBorder;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;
import java.util.Vector;

/**
 * Basic painter for JTabbedPane component.
 * It is used as WebTabbedPaneUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public class TabbedPanePainter<E extends JTabbedPane, U extends WebTabbedPaneUI> extends AbstractPainter<E, U>
        implements ITabbedPanePainter<E, U>
{
    /**
     * Style settings.
     */
    protected int round = WebTabbedPaneStyle.round;
    protected int shadeWidth = WebTabbedPaneStyle.shadeWidth;
    protected Color selectedTopBg = WebTabbedPaneStyle.selectedTopBg;
    protected Color selectedBottomBg = WebTabbedPaneStyle.selectedBottomBg;
    protected Color topBg = WebTabbedPaneStyle.topBg;
    protected Color bottomBg = WebTabbedPaneStyle.bottomBg;
    protected Color tabBorderColor = WebTabbedPaneStyle.tabBorderColor;
    protected Color contentBorderColor = WebTabbedPaneStyle.contentBorderColor;
    protected Color backgroundColor = WebTabbedPaneStyle.backgroundColor;
    protected boolean paintBorderOnlyOnSelectedTab = WebTabbedPaneStyle.paintBorderOnlyOnSelectedTab;
    protected boolean forceUseSelectedTabBgColors = WebTabbedPaneStyle.forceUseSelectedTabBgColors;
    protected boolean paintOnlyTopBorder = WebTabbedPaneStyle.paintOnlyTopBorder;

    /**
     * Listeners.
     */
    protected FocusAdapter focusAdapter;

    /**
     * Painting variables.
     */
    protected boolean tabsOverlapBorder = UIManager.getBoolean ( "TabbedPane.tabsOverlapBorder" );
    protected boolean tabsOpaque = UIManager.getBoolean ( "TabbedPane.tabsOpaque" );
    protected int textIconGap = UIManager.getInt ( "TabbedPane.textIconGap" );
    protected Vector htmlViews;
    protected int tabRuns[];
    protected Rectangle rects[];
    protected int maxTabHeight;
    protected int maxTabWidth;
    protected int runCount;
    protected boolean scrollableTabLayoutEnabled;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        component.setBackground ( backgroundColor );

        // Focus updater
        focusAdapter = new FocusAdapter ()
        {
            @Override
            public void focusGained ( final FocusEvent e )
            {
                component.repaint ();
            }

            @Override
            public void focusLost ( final FocusEvent e )
            {
                component.repaint ();
            }
        };
        component.addFocusListener ( focusAdapter );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        if ( focusAdapter != null )
        {
            component.removeFocusListener ( focusAdapter );
            focusAdapter = null;
        }

        super.uninstall ( c, ui );
    }

    /**
     * Updates component with complete border.
     * This border takes painter borders and component margin and padding into account.
     */
    @Override
    public void updateBorder ()
    {
        if ( component != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( component ) )
            {
                return;
            }

            final Insets bgInsets = i ( 0, 0, 0, 0 );
            if ( ui.getTabbedPaneStyle ().equals ( TabbedPaneStyle.standalone ) )
            {
                // Standalone style border
                final Insets sbi = i ( shadeWidth, shadeWidth, shadeWidth, shadeWidth );
                component.setBorder ( new WebBorder ( SwingUtils.max ( bgInsets, sbi ) ) );
            }
            else
            {
                // Attached style border
                component.setBorder ( new WebBorder ( bgInsets ) );
            }
        }
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

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
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
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Map hints = SwingUtils.setupTextAntialias ( g2d );

        runCount = component.getTabRunCount ();
        htmlViews = ui.getHtmlViews ();
        tabRuns = ui.getTabRuns ();
        rects = ui.getRects ();
        maxTabHeight = ui.getMaxTabHeight ();
        maxTabWidth = ui.getMaxTabWidth ();
        scrollableTabLayoutEnabled = component.getLayout () instanceof WebBasicTabbedPaneUI.TabbedPaneScrollLayout;

        final int selectedIndex = component.getSelectedIndex ();
        final int tabPlacement = component.getTabPlacement ();

        ensureCurrentLayout ();

        // Paint content border and tab area
        if ( tabsOverlapBorder )
        {
            paintContentBorder ( g2d, tabPlacement, selectedIndex );
        }
        // If scrollable tabs are enabled, the tab area will be
        // painted by the scrollable tab panel instead.
        //
        if ( !scrollableTabLayoutEnabled )
        { // WRAP_TAB_LAYOUT
            paintTabArea ( g2d, tabPlacement, selectedIndex );
        }
        if ( !tabsOverlapBorder )
        {
            paintContentBorder ( g2d, tabPlacement, selectedIndex );
        }

        // Cleaning
        htmlViews = null;
        tabRuns = null;
        rects = null;

        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    protected void ensureCurrentLayout ()
    {
        if ( !component.isValid () )
        {
            component.validate ();
        }
        /* If tabPane doesn't have a peer yet, the validate() call will
         * silently fail.  We handle that by forcing a layout if tabPane
         * is still invalid.  See bug 4237677.
         */
        if ( !component.isValid () )
        {
            final BasicTabbedPaneUI.TabbedPaneLayout layout = ( BasicTabbedPaneUI.TabbedPaneLayout ) component.getLayout ();
            layout.calculateLayoutInfo ();
        }
    }

    /**
     * Paints the tabs in the tab area.
     * Invoked by paint().
     * The graphics parameter must be a valid {@code Graphics}
     * object.  Tab placement may be either:
     * {@code JTabbedPane.TOP}, {@code JTabbedPane.BOTTOM},
     * {@code JTabbedPane.LEFT}, or {@code JTabbedPane.RIGHT}.
     * The selected index must be a valid tabbed pane tab index (0 to
     * tab count - 1, inclusive) or -1 if no tab is currently selected.
     * The handling of invalid parameters is unspecified.
     *
     * @param g             the graphics object to use for rendering
     * @param tabPlacement  the placement for the tabs within the JTabbedPane
     * @param selectedIndex the tab index of the selected component
     */
    protected void paintTabArea ( final Graphics g, final int tabPlacement, final int selectedIndex )
    {
        final int tabCount = component.getTabCount ();

        final Rectangle iconRect = new Rectangle ();
        final Rectangle textRect = new Rectangle ();
        final Rectangle clipRect = g.getClipBounds ();

        final int runCount = component.getTabRunCount ();

        // Paint tabRuns of tabs from back to front
        for ( int i = runCount - 1; i >= 0; i-- )
        {
            final int start = tabRuns[ i ];
            final int next = tabRuns[ ( i == runCount - 1 ) ? 0 : i + 1 ];
            final int end = next != 0 ? next - 1 : tabCount - 1;
            for ( int j = start; j <= end; j++ )
            {
                if ( j != selectedIndex && rects[ j ].intersects ( clipRect ) )
                {
                    paintTab ( g, tabPlacement, rects, j, iconRect, textRect );
                }
            }
        }

        // Paint selected tab if its in the front run
        // since it may overlap other tabs
        if ( selectedIndex >= 0 && rects[ selectedIndex ].intersects ( clipRect ) )
        {
            paintTab ( g, tabPlacement, rects, selectedIndex, iconRect, textRect );
        }
    }

    protected void paintTab ( final Graphics g, final int tabPlacement, final Rectangle[] rects, final int tabIndex,
                              final Rectangle iconRect, final Rectangle textRect )
    {
        final Rectangle tabRect = rects[ tabIndex ];
        final int selectedIndex = component.getSelectedIndex ();
        final boolean isSelected = selectedIndex == tabIndex;

        if ( tabsOpaque || component.isOpaque () )
        {
            paintTabBackground ( g, tabPlacement, tabIndex, tabRect.x, tabRect.y, tabRect.width, tabRect.height, isSelected );
        }

        //paintTabBorder ( g, tabPlacement, tabIndex, tabRect.x, tabRect.y, tabRect.width, tabRect.height, isSelected );

        final String title = component.getTitleAt ( tabIndex );
        final Font font = component.getFont ();
        final FontMetrics metrics = SwingUtils.getFontMetrics ( component, g, font );
        final Icon icon = getIconForTab ( tabIndex );

        layoutLabel ( tabPlacement, metrics, tabIndex, title, icon, tabRect, iconRect, textRect, isSelected );

        if ( component.getTabComponentAt ( tabIndex ) == null )
        {
            String clippedTitle = title;

            if ( scrollableTabLayoutEnabled )
            {
                final WebBasicTabbedPaneUI.CroppedEdge croppedEdge = ui.getTabScroller ().croppedEdge;
                if ( croppedEdge.isParamsSet () && croppedEdge.getTabIndex () == tabIndex && isHorizontalTabPlacement () )
                {
                    final int availTextWidth = croppedEdge.getCropline () - ( textRect.x - tabRect.x ) - croppedEdge.getCroppedSideWidth ();
                    clippedTitle = SwingUtils.clipStringIfNecessary ( null, metrics, title, availTextWidth );
                }
            }

            paintText ( g, tabPlacement, font, metrics, tabIndex, clippedTitle, textRect, isSelected );

            paintIcon ( g, tabPlacement, tabIndex, icon, iconRect, isSelected );
        }
    }

    protected boolean isHorizontalTabPlacement ()
    {
        return component.getTabPlacement () == JTabbedPane.TOP || component.getTabPlacement () == JTabbedPane.BOTTOM;
    }

    protected void layoutLabel ( final int tabPlacement, final FontMetrics metrics, final int tabIndex, final String title, final Icon icon,
                                 final Rectangle tabRect, final Rectangle iconRect, final Rectangle textRect, final boolean isSelected )
    {
        textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

        final View v = getTextViewForTab ( tabIndex );
        if ( v != null )
        {
            component.putClientProperty ( "html", v );
        }

        SwingUtilities
                .layoutCompoundLabel ( component, metrics, title, icon, SwingUtilities.CENTER, SwingUtilities.CENTER, SwingUtilities.CENTER,
                        SwingUtilities.TRAILING, tabRect, iconRect, textRect, textIconGap );

        component.putClientProperty ( "html", null );

        final int xNudge = getTabLabelShiftX ( tabPlacement, tabIndex, isSelected );
        final int yNudge = getTabLabelShiftY ( tabPlacement, tabIndex, isSelected );
        iconRect.x += xNudge;
        iconRect.y += yNudge;
        textRect.x += xNudge;
        textRect.y += yNudge;
    }

    @SuppressWarnings ("UnusedParameters")
    protected void paintIcon ( final Graphics g, final int tabPlacement, final int tabIndex, final Icon icon, final Rectangle iconRect,
                               final boolean isSelected )
    {
        if ( icon != null )
        {
            icon.paintIcon ( component, g, iconRect.x, iconRect.y );
        }
    }

    /**
     * Returns the text View object required to render stylized text (HTML) for
     * the specified tab or null if no specialized text rendering is needed
     * for this tab. This is provided to support html rendering inside tabs.
     *
     * @param tabIndex the index of the tab
     * @return the text view to render the tab's text or null if no
     * specialized rendering is required
     * @since 1.4
     */
    protected View getTextViewForTab ( final int tabIndex )
    {
        if ( htmlViews != null )
        {
            return ( View ) htmlViews.elementAt ( tabIndex );
        }
        return null;
    }

    protected void paintTabBackground ( final Graphics g, final int tabPlacement, final int tabIndex, final int x, final int y, final int w,
                                        final int h, final boolean isSelected )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Border shape
        final GeneralPath borderShape = createTabShape ( TabShapeType.border, tabPlacement, x, y, w, h, isSelected );

        // Tab shade
        if ( ui.getTabbedPaneStyle ().equals ( TabbedPaneStyle.standalone ) )
        {
            final GeneralPath shadeShape = createTabShape ( TabShapeType.shade, tabPlacement, x, y, w, h, isSelected );
            GraphicsUtils.drawShade ( g2d, shadeShape, new Color ( 210, 210, 210 ), shadeWidth,
                    new Rectangle2D.Double ( 0, 0, component.getWidth (), y + h ), round > 0 );
        }

        // Tab background
        final GeneralPath bgShape = createTabShape ( TabShapeType.background, tabPlacement, x, y, w, h, isSelected );
        final Painter backgroundPainterAt = ui.getBackgroundPainterAt ( tabIndex );
        if ( backgroundPainterAt != null && isSelected )
        {
            final Shape old = GraphicsUtils.intersectClip ( g2d, bgShape );
            backgroundPainterAt.paint ( g2d, new Rectangle ( x, y, w, h ), component, ui );
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
                    Color bg = component.getBackgroundAt ( tabIndex );
                    bg = bg != null ? bg : component.getBackground ();
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
        final boolean drawFocus = isSelected && component.isFocusOwner ();
        if ( ui.getTabbedPaneStyle ().equals ( TabbedPaneStyle.standalone ) )
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

    protected Icon getIconForTab ( final int tabIndex )
    {
        return ( !component.isEnabled () || !component.isEnabledAt ( tabIndex ) ) ? component.getDisabledIconAt ( tabIndex ) :
                component.getIconAt ( tabIndex );
    }

    @SuppressWarnings ("UnusedParameters")
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
            final int mnemIndex = component.getDisplayedMnemonicIndexAt ( tabIndex );

            if ( component.isEnabled () && component.isEnabledAt ( tabIndex ) )
            {
                Color fg = component.getForegroundAt ( tabIndex );
                if ( isSelected && ( fg instanceof UIResource ) )
                {
                    final Color selectedForegroundAt = ui.getSelectedForegroundAt ( tabIndex );
                    if ( selectedForegroundAt != null )
                    {
                        fg = selectedForegroundAt;
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
                g.setColor ( component.getBackgroundAt ( tabIndex ).brighter () );
                SwingUtils.drawStringUnderlineCharAt ( g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent () );
                g.setColor ( component.getBackgroundAt ( tabIndex ).darker () );
                SwingUtils.drawStringUnderlineCharAt ( g, title, mnemIndex, textRect.x - 1, textRect.y + metrics.getAscent () - 1 );
            }
        }
    }

    protected GeneralPath createTabShape ( final TabShapeType tabShapeType, final int tabPlacement, int x, final int y, int w, final int h,
                                           final boolean isSelected )
    {
        // Fix for basic layouting of selected left-sided tab x coordinate
        final Insets insets = component.getInsets ();
        if ( ui.getTabbedPaneStyle ().equals ( TabbedPaneStyle.attached ) && isSelected )
        {
            // todo fix for other tabPlacement values aswell
            if ( tabPlacement == JTabbedPane.TOP && x == insets.left )
            {
                x = x - 1;
                w = w + 1;
            }
        }

        final int actualRound = ui.getTabbedPaneStyle ().equals ( TabbedPaneStyle.standalone ) ? round : 0;
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

    protected int getChange ( final TabShapeType tabShapeType )
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

    protected Point getTopTabBgPoint ( final int tabPlacement, final int x, final int y, final int w, final int h )
    {
        if ( tabPlacement == JTabbedPane.TOP )
        {
            return p ( x, y );
        }
        else if ( tabPlacement == JTabbedPane.BOTTOM )
        {
            return p ( x, y + h );
        }
        else if ( tabPlacement == JTabbedPane.LEFT )
        {
            return p ( x, y );
        }
        else
        {
            return p ( x + w, y );
        }
    }

    protected Point getBottomTabBgPoint ( final int tabPlacement, final int x, final int y, final int w, final int h )
    {
        if ( tabPlacement == JTabbedPane.TOP )
        {
            return p ( x, y + h - 4 );
        }
        else if ( tabPlacement == JTabbedPane.BOTTOM )
        {
            return p ( x, y + 4 );
        }
        else if ( tabPlacement == JTabbedPane.LEFT )
        {
            return p ( x + w - 4, y );
        }
        else
        {
            return p ( x + 4, y );
        }
    }

    protected void paintContentBorder ( final Graphics2D g2d, final int tabPlacement, final int selectedIndex )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        final int tabAreaSize = getTabAreaLength ( tabPlacement );

        Insets bi = component.getInsets ();
        if ( tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM )
        {
            bi = i ( bi.top, bi.left, bi.bottom, bi.right + 1 );
        }
        else
        {
            bi = i ( bi.top, bi.left, bi.bottom + 1, bi.right );
        }

        // Selected tab bounds
        final Rectangle selected = selectedIndex != -1 ? ui.getTabBounds ( component, selectedIndex ) : null;

        // Background shape
        final Shape bs = createBackgroundShape ( tabPlacement, tabAreaSize, bi, selected );

        if ( ui.getTabbedPaneStyle ().equals ( TabbedPaneStyle.standalone ) )
        {
            // Proper clip
            final GeneralPath clip = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
            clip.append ( new Rectangle2D.Double ( 0, 0, component.getWidth (), component.getHeight () ), false );
            clip.append ( bs, false );
            GraphicsUtils.drawShade ( g2d, bs, new Color ( 210, 210, 210 ), shadeWidth, clip, round > 0 );

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
            final Painter backgroundPainterAt = ui.getBackgroundPainterAt ( selectedIndex );
            if ( backgroundPainterAt != null )
            {
                final Shape old = GraphicsUtils.intersectClip ( g2d, bs );
                backgroundPainterAt.paint ( g2d, bs.getBounds (), component, ui );
                GraphicsUtils.restoreClip ( g2d, old );
            }
            else
            {
                final Color bg = selectedIndex != -1 ? component.getBackgroundAt ( selectedIndex ) : null;
                g2d.setPaint ( bg != null ? bg : component.getBackground () );
                g2d.fill ( bs );
            }

            // Area border
            g2d.setPaint ( contentBorderColor );
            g2d.draw ( bs );

            // Area focus
            LafUtils.drawCustomWebFocus ( g2d, null, StyleConstants.focusType, bs, null, component.isFocusOwner () );
        }
        else
        {
            // Area background
            final Painter backgroundPainterAt = ui.getBackgroundPainterAt ( selectedIndex );
            if ( backgroundPainterAt != null )
            {
                backgroundPainterAt.paint ( g2d, bs.getBounds (), component, ui );
            }
            else
            {
                final Color bg = selectedIndex != -1 ? component.getBackgroundAt ( selectedIndex ) : null;
                g2d.setPaint ( bg != null ? bg : component.getBackground () );
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
                    if ( selected.x + selected.width < component.getWidth () - bi.right )
                    {
                        g2d.drawLine ( selected.x + selected.width, bi.top + tabAreaSize, component.getWidth () - bi.right,
                                bi.top + tabAreaSize );
                    }
                }
                else
                {
                    g2d.drawLine ( bi.left, bi.top + tabAreaSize, component.getWidth () - bi.right, bi.top + tabAreaSize );
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

    protected int getTabAreaLength ( final int tabPlacement )
    {
        return tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM ?
                calculateTabAreaHeight ( tabPlacement, runCount, maxTabHeight ) - 1 :
                calculateTabAreaWidth ( tabPlacement, runCount, maxTabWidth ) - 1;
    }

    protected int calculateTabAreaHeight ( final int tabPlacement, final int horizRunCount, final int maxTabHeight )
    {
        final Insets tabAreaInsets = ui.getTabAreaInsets ( tabPlacement );
        final int tabRunOverlay = ui.getTabRunOverlay ( tabPlacement );
        return horizRunCount > 0 ? horizRunCount * ( maxTabHeight - tabRunOverlay ) + tabRunOverlay +
                tabAreaInsets.top + tabAreaInsets.bottom : 0;
    }

    protected int calculateTabAreaWidth ( final int tabPlacement, final int vertRunCount, final int maxTabWidth )
    {
        final Insets tabAreaInsets = ui.getTabAreaInsets ( tabPlacement );
        final int tabRunOverlay = ui.getTabRunOverlay ( tabPlacement );
        return vertRunCount > 0 ? vertRunCount * ( maxTabWidth - tabRunOverlay ) + tabRunOverlay +
                tabAreaInsets.left + tabAreaInsets.right : 0;
    }

    protected Shape createBackgroundShape ( final int tabPlacement, final int tabAreaSize, final Insets bi, final Rectangle selected )
    {
        if ( ui.getTabbedPaneStyle ().equals ( TabbedPaneStyle.standalone ) )
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
                        gp.lineTo ( bi.left, component.getHeight () - bi.bottom - round );
                        gp.quadTo ( bi.left, component.getHeight () - bi.bottom, bi.left + round, component.getHeight () - bi.bottom );
                        gp.lineTo ( component.getWidth () - bi.right - round, component.getHeight () - bi.bottom );
                        gp.quadTo ( component.getWidth () - bi.right, component.getHeight () - bi.bottom, component.getWidth () - bi.right,
                                component.getHeight () - bi.bottom - round );
                    }
                    else
                    {
                        if ( paintOnlyTopBorder )
                        {
                            gp.moveTo ( component.getWidth () - bi.right, component.getHeight () - bi.bottom );
                        }
                        else
                        {
                            gp.lineTo ( bi.left, component.getHeight () - bi.bottom );
                            gp.lineTo ( component.getWidth () - bi.right, component.getHeight () - bi.bottom );
                        }
                    }
                    if ( selected.x + selected.width < component.getWidth () - bi.right - round && round > 0 )
                    {
                        gp.lineTo ( component.getWidth () - bi.right, topY + round );
                        gp.quadTo ( component.getWidth () - bi.right, topY, component.getWidth () - bi.right - round, topY );
                    }
                    else
                    {
                        if ( paintOnlyTopBorder )
                        {
                            gp.moveTo ( component.getWidth () - bi.right, topY );
                        }
                        else
                        {
                            gp.lineTo ( component.getWidth () - bi.right, topY );
                        }
                    }
                    gp.lineTo ( selected.x + selected.width, topY );
                }
                else if ( tabPlacement == JTabbedPane.BOTTOM )
                {
                    final int bottomY = component.getHeight () - bi.bottom - tabAreaSize;
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
                        gp.lineTo ( component.getWidth () - bi.right - round, bi.top );
                        gp.quadTo ( component.getWidth () - bi.right, bi.top, component.getWidth () - bi.right, bi.top + round );
                    }
                    else
                    {
                        gp.lineTo ( bi.left, bi.top );
                        gp.lineTo ( component.getWidth () - bi.right, bi.top );
                    }
                    if ( selected.x + selected.width < component.getWidth () - bi.right - round && round > 0 )
                    {
                        gp.lineTo ( component.getWidth () - bi.right, bottomY - round );
                        gp.quadTo ( component.getWidth () - bi.right, bottomY, component.getWidth () - bi.right - round, bottomY );
                    }
                    else
                    {
                        gp.lineTo ( component.getWidth () - bi.right, bottomY );
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
                        gp.lineTo ( component.getWidth () - bi.right - round, bi.top );
                        gp.quadTo ( component.getWidth () - bi.right, bi.top, component.getWidth () - bi.right, bi.top + round );
                        gp.lineTo ( component.getWidth () - bi.right, component.getHeight () - bi.bottom - round );
                        gp.quadTo ( component.getWidth () - bi.right, component.getHeight () - bi.bottom,
                                component.getWidth () - bi.right - round, component.getHeight () - bi.bottom );
                    }
                    else
                    {
                        gp.lineTo ( component.getWidth () - bi.right, bi.top );
                        gp.lineTo ( component.getWidth () - bi.right, component.getHeight () - bi.bottom );
                    }
                    if ( selected.y + selected.height < component.getHeight () - bi.bottom - round && round > 0 )
                    {
                        gp.lineTo ( leftX + round, component.getHeight () - bi.bottom );
                        gp.quadTo ( leftX, component.getHeight () - bi.bottom, leftX, component.getHeight () - bi.bottom - round );
                    }
                    else
                    {
                        gp.lineTo ( leftX, component.getHeight () - bi.bottom );
                    }
                    gp.lineTo ( leftX, selected.y + selected.height );
                }
                else
                {
                    final int rightX = component.getWidth () - bi.right - tabAreaSize;
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
                        gp.lineTo ( bi.left, component.getHeight () - bi.bottom - round );
                        gp.quadTo ( bi.left, component.getHeight () - bi.bottom, bi.left + round, component.getHeight () - bi.bottom );
                    }
                    else
                    {
                        gp.lineTo ( bi.left, bi.top );
                        gp.lineTo ( bi.left, component.getHeight () - bi.bottom );
                    }
                    if ( selected.y + selected.height < component.getHeight () - bi.bottom - round && round > 0 )
                    {
                        gp.lineTo ( rightX - round, component.getHeight () - bi.bottom );
                        gp.quadTo ( rightX, component.getHeight () - bi.bottom, rightX, component.getHeight () - bi.bottom - round );
                    }
                    else
                    {
                        gp.lineTo ( rightX, component.getHeight () - bi.bottom );
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
                        component.getWidth () - bi.left - bi.right -
                                ( left || right ? tabAreaSize : 0 ), component.getHeight () - bi.top - bi.bottom -
                        ( top || bottom ? tabAreaSize : 0 ), round * 2, round * 2 );
            }
        }
        else
        {
            final int x = bi.left + ( tabPlacement == JTabbedPane.LEFT ? tabAreaSize : 0 );
            final int y = bi.top + ( tabPlacement == JTabbedPane.TOP ? tabAreaSize : 0 );
            final int width = component.getWidth () - bi.left - bi.right -
                    ( tabPlacement == JTabbedPane.LEFT || tabPlacement == JTabbedPane.RIGHT ? tabAreaSize : 0 );
            final int height = component.getHeight () - bi.top - bi.bottom -
                    ( tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM ? tabAreaSize : 0 );
            return new Rectangle ( x, y, width + 1, height );
        }
    }

    protected int getTabLabelShiftX ( final int tabPlacement, final int tabIndex, final boolean isSelected )
    {
        if ( ui.getTabbedPaneStyle ().equals ( TabbedPaneStyle.standalone ) )
        {
            final Rectangle tabRect = rects[ tabIndex ];
            switch ( tabPlacement )
            {
                case JTabbedPane.LEFT:
                    return isSelected ? -1 : 1;

                case JTabbedPane.RIGHT:
                    return isSelected ? 1 : -1;

                case JTabbedPane.BOTTOM:
                case JTabbedPane.TOP:
                default:
                    return tabRect.width % 2;
            }
        }
        else
        {
            return 0;
        }
    }

    protected int getTabLabelShiftY ( final int tabPlacement, final int tabIndex, final boolean isSelected )
    {
        if ( ui.getTabbedPaneStyle ().equals ( TabbedPaneStyle.standalone ) )
        {
            final Rectangle tabRect = rects[ tabIndex ];
            switch ( tabPlacement )
            {
                case JTabbedPane.BOTTOM:
                    return isSelected ? 1 : -1;

                case JTabbedPane.LEFT:
                case JTabbedPane.RIGHT:
                    return tabRect.height % 2;

                case JTabbedPane.TOP:
                default:
                    return isSelected ? -1 : 1;
            }
        }
        else
        {
            return 0;
        }
    }
}