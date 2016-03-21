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

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.MergeUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.View;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebTabbedPaneUI extends WebBasicTabbedPaneUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( TabbedPanePainter.class )
    protected ITabbedPanePainter painter;

    /**
     * Style settings.
     */
    protected int tabRunIndent = WebTabbedPaneStyle.tabRunIndent;
    protected int tabOverlay = WebTabbedPaneStyle.tabOverlay;
    protected TabbedPaneStyle tabbedPaneStyle = WebTabbedPaneStyle.tabbedPaneStyle;
    protected TabStretchType tabStretchType = WebTabbedPaneStyle.tabStretchType;
    protected Insets contentInsets = WebTabbedPaneStyle.contentInsets;
    protected Insets tabInsets = WebTabbedPaneStyle.tabInsets;
    protected boolean rotateTabInsets = WebTabbedPaneStyle.rotateTabInsets;

    /**
     * Runtime variables.
     */
    protected final Map<Integer, Color> selectedForegroundAt = new HashMap<Integer, Color> ();
    protected final Map<Integer, Painter> backgroundPainterAt = new HashMap<Integer, Painter> ();
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebTabbedPaneUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebTabbedPaneUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTabbedPaneUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( tabPane );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( tabPane );

        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( tabPane );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( tabPane, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( tabPane, painter );
    }

    /**
     * Returns tabbed pane painter.
     *
     * @return tabbed pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets tabbed pane painter.
     * Pass null to remove tabbed pane painter.
     *
     * @param painter new tabbed pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( tabPane, new DataRunnable<ITabbedPanePainter> ()
        {
            @Override
            public void run ( final ITabbedPanePainter newPainter )
            {
                WebTabbedPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, ITabbedPanePainter.class, AdaptiveTabbedPanePainter.class );
    }

    public boolean isRotateTabInsets ()
    {
        return rotateTabInsets;
    }

    public void setRotateTabInsets ( final boolean rotateTabInsets )
    {
        this.rotateTabInsets = rotateTabInsets;
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
        PainterSupport.updateBorder ( painter );
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

    public Vector<View> getHtmlViews ()
    {
        return htmlViews;
    }

    public ScrollableTabSupport getTabScroller ()
    {
        return tabScroller;
    }

    public int[] getTabRuns ()
    {
        return tabRuns;
    }

    public Rectangle[] getRects ()
    {
        return rects;
    }

    public int getMaxTabHeight ()
    {
        return maxTabHeight;
    }

    public int getMaxTabWidth ()
    {
        return maxTabWidth;
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    @Override
    protected int getTabRunIndent ( final int tabPlacement, final int run )
    {
        return tabRunIndent;
    }

    @Override
    public int getTabRunOverlay ( final int tabPlacement )
    {
        return tabOverlay;
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
    public Insets getTabAreaInsets ( final int tabPlacement )
    {
        final Insets targetInsets = new Insets ( 0, 0, 0, 0 );
        rotateInsets (
                tabbedPaneStyle.equals ( TabbedPaneStyle.standalone ) ? new Insets ( tabPlacement == JTabbedPane.RIGHT ? 1 : 0, 1, 0, 2 ) :
                        new Insets ( -1, -1, 0, 0 ), targetInsets, tabPlacement );
        return targetInsets;
    }

    @Override
    protected boolean shouldRotateTabRuns ( final int tabPlacement )
    {
        // todo Requires style changes
        return true;
    }

    @Override
    protected boolean shouldPadTabRun ( final int tabPlacement, final int run )
    {
        return !tabStretchType.equals ( TabStretchType.never ) &&
                ( tabStretchType.equals ( TabStretchType.always ) || tabStretchType.equals ( TabStretchType.multiline ) && runCount > 1 );
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
    protected Insets getTabInsets ( final int tabPlacement, final int tabIndex )
    {
        final Insets insets = MergeUtils.clone ( tabInsets );
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
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
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

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        // return PainterSupport.getPreferredSize ( c, painter );
        return null;
    }
}