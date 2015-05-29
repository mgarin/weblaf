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

package com.alee.laf.label;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.StyleManager;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for JLabel component.
 *
 * @author Mikle Garin
 */

public class WebLabelUI extends BasicLabelUI implements Styleable, BorderMethods
{
    /**
     * Style settings.
     */
    protected Insets margin = WebLabelStyle.margin;
    protected boolean drawShade = WebLabelStyle.drawShade;

    /**
     * Component painter.
     */
    protected LabelPainter painter;

    /**
     * Label listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected JLabel label;

    /**
     * Returns an instance of the WebLabelUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebLabelUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebLabelUI ();
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

        // Saving label to local variable
        label = ( JLabel ) c;

        // Default settings
        SwingUtils.setOrientation ( label );

        // Applying skin
        StyleManager.applySkin ( label );

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        label.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );
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
        label.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );

        // Uninstalling applied skin
        StyleManager.removeSkin ( label );

        // Cleaning up reference
        label = null;

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
        StyleManager.applySkin ( label );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        LafUtils.updateBorder ( label, margin, painter );
    }

    /**
     * Returns whether text shade is displayed or not.
     *
     * @return true if text shade is displayed, false otherwise
     */
    public boolean isDrawShade ()
    {
        return drawShade;
    }

    /**
     * Sets whether text shade should be displayed or not.
     *
     * @param drawShade whether text shade should be displayed or not
     */
    public void setDrawShade ( final boolean drawShade )
    {
        this.drawShade = drawShade;
        if ( painter != null )
        {
            painter.setDrawShade ( drawShade );
        }
    }

    /**
     * Returns component margin.
     *
     * @return component margin
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets component margin.
     *
     * @param margin component margin
     */
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    /**
     * Returns text shade color.
     *
     * @return text shade color
     */
    public Color getShadeColor ()
    {
        final Color shadeColor = StyleManager.getPainterPropertyValue ( label, "shadeColor" );
        return shadeColor != null ? shadeColor : WebLabelStyle.shadeColor;
    }

    /**
     * Sets text shade color.
     *
     * @param shadeColor text shade color
     */
    public void setShadeColor ( final Color shadeColor )
    {
        StyleManager.setCustomPainterProperty ( label, "shadeColor", shadeColor );
    }

    /**
     * Returns label transparency.
     *
     * @return label transparency
     */
    public Float getTransparency ()
    {
        final Float transparency = StyleManager.getPainterPropertyValue ( label, "transparency" );
        return transparency != null ? transparency : WebLabelStyle.transparency;
    }

    /**
     * Sets label transparency.
     *
     * @param transparency label transparency
     */
    public void setTransparency ( final Float transparency )
    {
        StyleManager.setCustomPainterProperty ( label, "transparency", transparency );
    }

    /**
     * Returns label painter.
     *
     * @return label painter
     */
    public Painter getPainter ()
    {
        return LafUtils.getAdaptedPainter ( painter );
    }

    /**
     * Sets label painter.
     * Pass null to remove label painter.
     *
     * @param painter new label painter
     */
    public void setPainter ( final Painter painter )
    {
        // Creating adaptive painter if required
        final LabelPainter properPainter = LafUtils.getProperPainter ( painter, LabelPainter.class, AdaptiveLabelPainter.class );

        // Properly updating painter
        PainterSupport.uninstallPainter ( label, this.painter );
        final Painter oldPainter = this.painter;
        this.painter = properPainter;
        applyPainterSettings ( properPainter );
        PainterSupport.installPainter ( label, properPainter );

        // Firing painter change event
        // This is made using reflection because required method is protected within Component class
        LafUtils.firePainterChanged ( label, oldPainter, properPainter );
    }

    /**
     * Applies UI settings to this specific painter.
     *
     * @param painter label painter
     */
    protected void applyPainterSettings ( final LabelPainter painter )
    {
        if ( painter != null )
        {
            // UI settings
            painter.setDrawShade ( drawShade );
        }
    }

    /**
     * Paints label.
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return LafUtils.getPreferredSize ( c, painter );
    }
}