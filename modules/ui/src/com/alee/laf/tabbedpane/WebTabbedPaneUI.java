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

import com.alee.api.clone.Clone;
import com.alee.api.jdk.Consumer;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.painter.SectionPainter;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.View;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Custom UI for {@link JTabbedPane} component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public class WebTabbedPaneUI extends WTabbedPaneUI implements ShapeSupport, MarginSupport, PaddingSupport
{
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
     * Component painter.
     */
    @DefaultPainter ( TabbedPanePainter.class )
    protected ITabbedPanePainter painter;

    /**
     * Runtime variables.
     */
    protected transient final Map<Integer, SectionPainter> backgroundPainterAt = new HashMap<Integer, SectionPainter> ();

    /**
     * Returns an instance of the {@link WebTabbedPaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebTabbedPaneUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTabbedPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( tabPane );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( tabPane );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( tabPane, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( tabPane, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( tabPane, painter, enabled );
    }

    /**
     * Returns tabbed pane painter.
     *
     * @return tabbed pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets tabbed pane painter.
     * Pass null to remove tabbed pane painter.
     *
     * @param painter new tabbed pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( tabPane, new Consumer<ITabbedPanePainter> ()
        {
            @Override
            public void accept ( final ITabbedPanePainter newPainter )
            {
                WebTabbedPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, ITabbedPanePainter.class, AdaptiveTabbedPanePainter.class );
    }

    @Override
    public TabbedPaneStyle getTabbedPaneStyle ()
    {
        return tabbedPaneStyle;
    }

    @Override
    public void setTabbedPaneStyle ( final TabbedPaneStyle tabbedPaneStyle )
    {
        final TabbedPaneStyle old = this.tabbedPaneStyle;
        this.tabbedPaneStyle = tabbedPaneStyle;
        SwingUtils.firePropertyChanged ( tabPane, WebLookAndFeel.TABBED_PANE_STYLE_PROPERTY, old, tabbedPaneStyle );
    }

    @Override
    public TabStretchType getTabStretchType ()
    {
        return tabStretchType;
    }

    @Override
    public void setTabStretchType ( final TabStretchType tabStretchType )
    {
        this.tabStretchType = tabStretchType;
    }

    @Override
    public Vector<View> getHtmlViews ()
    {
        return htmlViews;
    }

    @Override
    public ScrollableTabSupport getTabScroller ()
    {
        return tabScroller;
    }

    @Override
    public int[] getTabRuns ()
    {
        return tabRuns;
    }

    @Override
    public Rectangle[] getRects ()
    {
        return rects;
    }

    @Override
    public int getMaxTabHeight ()
    {
        return maxTabHeight;
    }

    @Override
    public int getMaxTabWidth ()
    {
        return maxTabWidth;
    }

    @Override
    public SectionPainter getBackgroundPainterAt ( final int tabIndex )
    {
        return backgroundPainterAt.get ( tabIndex );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
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
        final Insets insets = Clone.basic ().clone ( tabInsets );
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
        return PainterSupport.getMargin ( tabPane );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( tabPane, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( tabPane );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( tabPane, padding );
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