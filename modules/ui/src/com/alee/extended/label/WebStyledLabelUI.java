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

package com.alee.extended.label;

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

public class WebStyledLabelUI extends BasicLabelUI implements Styleable, BorderMethods, SwingConstants
{
    /**
     * Style settings.
     */
    protected Insets margin = WebStyledLabelStyle.margin;
    protected int preferredRowCount = WebStyledLabelStyle.preferredRowCount;
    protected boolean ignoreColorSettings = WebStyledLabelStyle.ignoreColorSettings;
    protected float scriptFontRatio = WebStyledLabelStyle.scriptFontRatio;
    protected String truncatedTextSuffix = WebStyledLabelStyle.truncatedTextSuffix;

    /**
     * Component painter.
     */
    protected StyledLabelPainter painter;

    /**
     * Label listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected WebStyledLabel label;

    /**
     * Returns an instance of the WebStyledLabelUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebStyledLabelUI
     */
    @SuppressWarnings ( { "UnusedDeclaration" } )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebStyledLabelUI ();
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
        label = ( WebStyledLabel ) c;

        // Default settings
        SwingUtils.setOrientation ( label );
        label.setMaximumSize ( null );

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
        // Removing label listeners
        label.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );

        // Uninstalling applied skin
        StyleManager.removeSkin ( label );

        label = null;
        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange ( final PropertyChangeEvent e )
    {
        super.propertyChange ( e );

        // Updating text ranges
        if ( WebStyledLabel.PROPERTY_STYLE_RANGE.equals ( e.getPropertyName () ) )
        {
            if ( painter != null )
            {
                painter.updateTextRanges ();
            }
        }
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
        LafUtils.updateBorder ( label, margin, null );
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
     * Returns preferred row count.
     *
     * @return preferred row count
     */
    public int getPreferredRowCount ()
    {
        return preferredRowCount;
    }

    /**
     * Sets preferred row count.
     *
     * @param rows new preferred row count
     */
    public void setPreferredRowCount ( final int rows )
    {
        this.preferredRowCount = rows;
        if ( painter != null )
        {
            painter.setPreferredRowCount ( rows );
        }
    }

    /**
     * Returns whether color settings should be ignored or not.
     *
     * @return true if color settings should be ignored, false otherwise
     */
    public boolean isIgnoreColorSettings ()
    {
        return ignoreColorSettings;
    }

    /**
     * Sets whether color settings should be ignored or not.
     *
     * @param ignore whether color settings should be ignored or not
     */
    public void setIgnoreColorSettings ( final boolean ignore )
    {
        this.ignoreColorSettings = ignore;
        if ( painter != null )
        {
            painter.setIgnoreColorSettings ( ignore );
        }
    }

    /**
     * Returns subscript and superscript font ratio.
     *
     * @return subscript and superscript font ratio
     */
    public float getScriptFontRatio ()
    {
        return scriptFontRatio;
    }

    /**
     * Sets subscript and superscript font ratio.
     *
     * @param ratio new subscript and superscript font ratio
     */
    public void setScriptFontRatio ( final float ratio )
    {
        this.scriptFontRatio = ratio;
        if ( painter != null )
        {
            painter.setScriptFontRatio ( ratio );
        }
    }

    /**
     * Returns truncated text suffix.
     *
     * @return truncated text suffix
     */
    public String getTruncatedTextSuffix ()
    {
        return truncatedTextSuffix;
    }

    /**
     * Sets truncated text suffix.
     *
     * @param suffix new truncated text suffix
     */
    public void setTruncatedTextSuffix ( final String suffix )
    {
        this.truncatedTextSuffix = suffix;
        if ( painter != null )
        {
            painter.setTruncatedTextSuffix ( suffix );
        }
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
        final StyledLabelPainter properPainter =
                LafUtils.getProperPainter ( painter, StyledLabelPainter.class, AdaptiveStyledLabelPainter.class );

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
    protected void applyPainterSettings ( final StyledLabelPainter painter )
    {
        if ( painter != null )
        {
            // UI settings
            painter.setPreferredRowCount ( preferredRowCount );
            painter.setIgnoreColorSettings ( ignoreColorSettings );
            painter.setScriptFontRatio ( scriptFontRatio );
            painter.setTruncatedTextSuffix ( truncatedTextSuffix );
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