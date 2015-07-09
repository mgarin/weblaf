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

package com.alee.laf.panel;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.extended.painter.PartialDecoration;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.StyleManager;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.PainterShapeProvider;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for JPanel component.
 *
 * @author Mikle Garin
 */

public class WebPanelUI extends BasicPanelUI implements Styleable, ShapeProvider, BorderMethods, PartialDecoration
{
    /**
     * Style settings.
     */
    protected boolean undecorated = WebPanelStyle.undecorated;
    protected boolean paintFocus = WebPanelStyle.drawFocus;
    protected Insets margin = WebPanelStyle.margin;
    protected boolean paintTop = true;
    protected boolean paintLeft = true;
    protected boolean paintBottom = true;
    protected boolean paintRight = true;
    protected boolean paintTopLine = false;
    protected boolean paintLeftLine = false;
    protected boolean paintBottomLine = false;
    protected boolean paintRightLine = false;

    /**
     * Component painter.
     */
    protected PanelPainter painter;

    /**
     * Panel listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected JPanel panel;

    /**
     * Returns an instance of the WebPanelUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebPanelUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebPanelUI ();
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
        panel = ( JPanel ) c;

        // Default settings
        SwingUtils.setOrientation ( panel );

        // Applying skin
        StyleManager.applySkin ( panel );

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        panel.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Removing listeners
        panel.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );

        // Uninstalling applied skin
        StyleManager.removeSkin ( panel );

        // Cleaning up reference
        panel = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return styleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        this.styleId = id;
        StyleManager.applySkin ( panel );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        if ( painter != null && painter instanceof PainterShapeProvider )
        {
            return ( ( PainterShapeProvider ) painter ).provideShape ( panel, SwingUtils.size ( panel ) );
        }
        else
        {
            return SwingUtils.size ( panel );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        LafUtils.updateBorder ( panel, margin, painter );
    }

    /**
     * Returns whether panel is undecorated or not.
     *
     * @return true if panel is undecorated, false otherwise
     */
    public boolean isUndecorated ()
    {
        return undecorated;
    }

    /**
     * Sets whether panel should be undecorated or not.
     *
     * @param undecorated whether panel should be undecorated or not
     */
    public void setUndecorated ( final boolean undecorated )
    {
        this.undecorated = undecorated;
        if ( painter != null )
        {
            painter.setUndecorated ( undecorated );
        }
    }

    /**
     * Returns whether focus should be painted or not.
     * Panel focus is displayed when either panel or one of its children are focused.
     *
     * @return true if focus should be painted, false otherwise
     */
    public boolean isPaintFocus ()
    {
        return paintFocus;
    }

    /**
     * Sets whether focus should be painted or not.
     * Panel focus is displayed when either panel or one of its children are focused.
     *
     * @param paint whether focus should be painted or not
     */
    public void setPaintFocus ( final boolean paint )
    {
        this.paintFocus = paint;
        if ( painter != null )
        {
            painter.setPaintFocus ( paint );
        }
    }

    /**
     * Returns panel margin.
     *
     * @return panel margin
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets panel margin.
     *
     * @param margin new panel margin
     */
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    /**
     * Returns whether should paint top side or not.
     *
     * @return true if should paint top side, false otherwise
     */
    public boolean isPaintTop ()
    {
        return paintTop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintTop ( final boolean top )
    {
        this.paintTop = top;
        if ( painter != null )
        {
            painter.setPaintTop ( top );
        }
    }

    /**
     * Returns whether should paint left side or not.
     *
     * @return true if should paint left side, false otherwise
     */
    public boolean isPaintLeft ()
    {
        return paintLeft;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintLeft ( final boolean left )
    {
        this.paintLeft = left;
        if ( painter != null )
        {
            painter.setPaintLeft ( left );
        }
    }

    /**
     * Returns whether should paint bottom side or not.
     *
     * @return true if should paint bottom side, false otherwise
     */
    public boolean isPaintBottom ()
    {
        return paintBottom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintBottom ( final boolean bottom )
    {
        this.paintBottom = bottom;
        if ( painter != null )
        {
            painter.setPaintBottom ( bottom );
        }
    }

    /**
     * Returns whether should paint right side or not.
     *
     * @return true if should paint right side, false otherwise
     */
    public boolean isPaintRight ()
    {
        return paintRight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintRight ( final boolean right )
    {
        this.paintRight = right;
        if ( painter != null )
        {
            painter.setPaintRight ( right );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        this.paintTop = top;
        this.paintLeft = left;
        this.paintBottom = bottom;
        this.paintRight = right;
        if ( painter != null )
        {
            painter.setPaintSides ( top, left, bottom, right );
        }
    }

    /**
     * Returns whether should paint top side line or not.
     *
     * @return true if should paint top side line, false otherwise
     */
    public boolean isPaintTopLine ()
    {
        return paintTopLine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintTopLine ( final boolean top )
    {
        this.paintTopLine = top;
        if ( painter != null )
        {
            painter.setPaintTopLine ( top );
        }
    }

    /**
     * Returns whether should paint left side line or not.
     *
     * @return true if should paint left side line, false otherwise
     */
    public boolean isPaintLeftLine ()
    {
        return paintLeftLine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintLeftLine ( final boolean left )
    {
        this.paintLeftLine = left;
        if ( painter != null )
        {
            painter.setPaintLeftLine ( left );
        }
    }

    /**
     * Returns whether should paint bottom side line or not.
     *
     * @return true if should paint bottom side line, false otherwise
     */
    public boolean isPaintBottomLine ()
    {
        return paintBottomLine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintBottomLine ( final boolean bottom )
    {
        this.paintBottomLine = bottom;
        if ( painter != null )
        {
            painter.setPaintBottomLine ( bottom );
        }
    }

    /**
     * Returns whether should paint right side line or not.
     *
     * @return true if should paint right side line, false otherwise
     */
    public boolean isPaintRightLine ()
    {
        return paintRightLine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintRightLine ( final boolean right )
    {
        this.paintRightLine = right;
        if ( painter != null )
        {
            painter.setPaintRightLine ( right );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintSideLines ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        this.paintTopLine = top;
        this.paintLeftLine = left;
        this.paintBottomLine = bottom;
        this.paintRightLine = right;
        if ( painter != null )
        {
            painter.setPaintSideLines ( top, left, bottom, right );
        }
    }

    /**
     * Returns decoration corners rounding.
     *
     * @return decoration corners rounding
     */
    public int getRound ()
    {
        final Integer round = StyleManager.getPainterPropertyValue ( panel, "round" );
        return round != null ? round : WebPanelStyle.round;
    }

    /**
     * Sets decoration corners rounding.
     *
     * @param round decoration corners rounding
     */
    public void setRound ( final int round )
    {
        StyleManager.setCustomPainterProperty ( panel, "round", round );
    }

    /**
     * Returns decoration shade width.
     *
     * @return decoration shade width
     */
    public int getShadeWidth ()
    {
        final Integer shadeWidth = StyleManager.getPainterPropertyValue ( panel, "shadeWidth" );
        return shadeWidth != null ? shadeWidth : WebPanelStyle.shadeWidth;
    }

    /**
     * Sets decoration shade width.
     *
     * @param shadeWidth decoration shade width
     */
    public void setShadeWidth ( final int shadeWidth )
    {
        StyleManager.setCustomPainterProperty ( panel, "shadeWidth", shadeWidth );
    }

    /**
     * Returns decoration shade transparency.
     *
     * @return decoration shade transparency
     */
    public float getShadeTransparency ()
    {
        final Float shadeTransparency = StyleManager.getPainterPropertyValue ( panel, "shadeTransparency" );
        return shadeTransparency != null ? shadeTransparency : WebPanelStyle.shadeTransparency;
    }

    /**
     * Sets decoration shade transparency.
     *
     * @param transparency new decoration shade transparency
     */
    public void setShadeTransparency ( final float transparency )
    {
        StyleManager.setCustomPainterProperty ( panel, "shadeTransparency", transparency );
    }

    /**
     * Returns decoration border stroke.
     *
     * @return decoration border stroke
     */
    public Stroke getBorderStroke ()
    {
        final Stroke borderStroke = StyleManager.getPainterPropertyValue ( panel, "borderStroke" );
        return borderStroke != null ? borderStroke : WebPanelStyle.borderStroke;
    }

    /**
     * Sets decoration border stroke.
     *
     * @param stroke decoration border stroke
     */
    public void setBorderStroke ( final Stroke stroke )
    {
        StyleManager.setCustomPainterProperty ( panel, "borderStroke", stroke );
    }

    /**
     * Returns decoration border color.
     *
     * @return decoration border color
     */
    public Color getBorderColor ()
    {
        final Color borderColor = StyleManager.getPainterPropertyValue ( panel, "borderColor" );
        return borderColor != null ? borderColor : WebPanelStyle.borderColor;
    }

    /**
     * Sets decoration border color.
     *
     * @param color decoration border color
     */
    public void setBorderColor ( final Color color )
    {
        StyleManager.setCustomPainterProperty ( panel, "borderColor", color );
    }

    /**
     * Returns decoration disabled border color.
     *
     * @return decoration disabled border color
     */
    public Color getDisabledBorderColor ()
    {
        final Color disabledBorderColor = StyleManager.getPainterPropertyValue ( panel, "disabledBorderColor" );
        return disabledBorderColor != null ? disabledBorderColor : WebPanelStyle.disabledBorderColor;
    }

    /**
     * Sets decoration disabled border color.
     *
     * @param color decoration disabled border color
     */
    public void setDisabledBorderColor ( final Color color )
    {
        StyleManager.setCustomPainterProperty ( panel, "disabledBorderColor", color );
    }

    /**
     * Returns whether should paint decoration background or not.
     *
     * @return true if should paint decoration background, false otherwise
     */
    public boolean isPaintBackground ()
    {
        final Boolean paintBackground = StyleManager.getPainterPropertyValue ( panel, "paintBackground" );
        return paintBackground != null ? paintBackground : WebPanelStyle.paintBackground;
    }

    /**
     * Sets whether should paint decoration background or not.
     *
     * @param paint whether should paint decoration background or not
     */
    public void setPaintBackground ( final boolean paint )
    {
        StyleManager.setCustomPainterProperty ( panel, "paintBackground", paint );
    }

    /**
     * Returns whether should paint web-styled background or not.
     *
     * @return true if should paint web-styled background, false otherwise
     */
    public boolean isWebColoredBackground ()
    {
        final Boolean webColored = StyleManager.getPainterPropertyValue ( panel, "webColoredBackground" );
        return webColored != null ? webColored : WebPanelStyle.webColoredBackground;
    }

    /**
     * Sets whether should paint web-styled background or not.
     *
     * @param webColored whether should paint web-styled background or not
     */
    public void setWebColoredBackground ( final boolean webColored )
    {
        StyleManager.setCustomPainterProperty ( panel, "webColoredBackground", webColored );
    }

    /**
     * Returns panel painter.
     *
     * @return panel painter
     */
    public Painter getPainter ()
    {
        return LafUtils.getAdaptedPainter ( painter );
    }

    /**
     * Sets panel painter.
     * Pass null to remove panel painter.
     *
     * @param painter new panel painter
     */
    public void setPainter ( final Painter painter )
    {
        // Creating adaptive painter if required
        final PanelPainter properPainter = LafUtils.getProperPainter ( painter, PanelPainter.class, AdaptivePanelPainter.class );

        // Properly updating painter
        PainterSupport.uninstallPainter ( panel, this.painter );
        final Painter oldPainter = this.painter;
        this.painter = properPainter;
        applyPainterSettings ( properPainter );
        PainterSupport.installPainter ( panel, properPainter );

        // Firing painter change event
        // This is made using reflection because required method is protected within Component class
        LafUtils.firePainterChanged ( panel, oldPainter, properPainter );
    }

    /**
     * Applies UI settings to this specific painter.
     *
     * @param painter panel painter
     */
    private void applyPainterSettings ( final PanelPainter painter )
    {
        if ( painter != null )
        {
            // UI settings
            painter.setUndecorated ( undecorated );
            painter.setPaintFocus ( paintFocus );
            painter.setPaintSides ( paintTop, paintLeft, paintBottom, paintRight );
            painter.setPaintSideLines ( paintTopLine, paintLeftLine, paintBottomLine, paintRightLine );
        }
    }

    /**
     * Paints panel.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c );
        }
    }
}