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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.colorchooser.HSBColor;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.language.LM;
import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageListener;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ColorUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mikle Garin
 */
public class DoubleColorField extends WebPanel
{
    private final List<DoubleColorFieldListener> listeners = new ArrayList<DoubleColorFieldListener> ( 1 );

    @Nullable
    private Color newColor;

    @Nullable
    private HSBColor newHSBColor;

    @Nullable
    private Color oldColor;

    @Nullable
    private HSBColor oldHSBColor;

    public DoubleColorField ()
    {
        super ();

        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) )
                {
                    if ( e.getY () <= getHeight () / 2 )
                    {
                        newColorPressed ();
                    }
                    else
                    {
                        oldColorPressed ();
                    }
                }
            }
        } );

        addLanguageListener ( new LanguageListener ()
        {
            @Override
            public void languageChanged ( @NotNull final Language oldLanguage, @NotNull final Language newLanguage )
            {
                revalidate ();
                repaint ();
            }
        } );
    }

    @Override
    protected void paintComponent ( @NotNull final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        final Map hints = SwingUtils.setupTextAntialias ( g2d );

        g2d.setPaint ( getBackground () );
        g2d.fillRect ( 0, 0, getWidth (), getHeight () );
        g2d.setPaint ( Color.GRAY );
        g2d.drawRect ( 0, 0, getWidth () - 1, getHeight () - 1 );
        g2d.setPaint ( Color.WHITE );
        g2d.drawRect ( 1, 1, getWidth () - 3, getHeight () - 3 );
        g2d.drawLine ( 1, getHeight () / 2, getWidth () - 3, getHeight () / 2 );

        paintColorRectangle (
                g2d,
                new Rectangle ( 2, 2, getWidth () - 5, getHeight () / 2 - 3 ),
                this.newColor,
                this.newHSBColor,
                LM.get ( "weblaf.colorchooser.color.new" )
        );
        paintColorRectangle (
                g2d,
                new Rectangle ( 2, getHeight () / 2 + 1, getWidth () - 5, getHeight () / 2 - 4 ),
                this.oldColor,
                this.oldHSBColor,
                LM.get ( "weblaf.colorchooser.color.current" )
        );

        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    private void paintColorRectangle ( @NotNull final Graphics2D g2d, @NotNull final Rectangle bounds, @Nullable final Color color,
                                       @Nullable final HSBColor hsbColor, @NotNull final String text )
    {
        final FontMetrics fm = g2d.getFontMetrics ();

        if ( color != null )
        {
            g2d.setPaint ( color );
            g2d.fillRect ( bounds.x, bounds.y, bounds.width + 1, bounds.height + 1 );
        }
        else
        {
            g2d.setPaint ( Color.GRAY );
            g2d.drawRect ( bounds.x, bounds.y, bounds.width, bounds.height );
            g2d.drawLine ( bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height );
            g2d.drawLine ( bounds.x + bounds.width, bounds.y, bounds.x, bounds.y + bounds.height );
        }

        final Point nts = LafUtils.getTextCenterShift ( fm, text );
        final boolean newColorIsLight = hsbColor == null || hsbColor.getBrightness () >= 0.7f && hsbColor.getSaturation () < 0.7f;
        if ( color == null )
        {
            final int fw = fm.stringWidth ( text );
            final int fh = fm.getHeight ();
            g2d.setPaint ( new RadialGradientPaint (
                    bounds.x + bounds.width / 2,
                    bounds.y + bounds.height / 2, bounds.width / 2,
                    new float[]{ 0f, 1f },
                    new Color[]{ getBackground (), ColorUtils.transparent () },
                    MultipleGradientPaint.CycleMethod.NO_CYCLE
            ) );
            g2d.fillRect ( bounds.x + bounds.width / 2 - fw / 2, bounds.y + bounds.height / 2 - fh / 2 + 1, fw, fh );
        }
        g2d.setPaint ( newColorIsLight ? Color.BLACK : Color.WHITE );
        g2d.drawString ( text, bounds.x + bounds.width / 2 + nts.x, bounds.y + bounds.height / 2 + nts.y );
    }

    @Nullable
    public Color getNewColor ()
    {
        return newColor;
    }

    public void setNewColor ( @Nullable final Color newColor )
    {
        this.newColor = newColor;
        this.newHSBColor = newColor != null ? new HSBColor ( newColor ) : null;
        this.repaint ();
    }

    @Nullable
    public Color getOldColor ()
    {
        return oldColor;
    }

    public void setOldColor ( @Nullable final Color oldColor )
    {
        this.oldColor = oldColor;
        this.oldHSBColor = oldColor != null ? new HSBColor ( oldColor ) : null;
        this.repaint ();
    }

    public void addDoubleColorFieldListener ( final DoubleColorFieldListener listener )
    {
        listeners.add ( listener );
    }

    public void removeDoubleColorFieldListener ( final DoubleColorFieldListener listener )
    {
        listeners.remove ( listener );
    }

    private void newColorPressed ()
    {
        for ( final DoubleColorFieldListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.newColorPressed ( newColor );
        }
    }

    private void oldColorPressed ()
    {
        for ( final DoubleColorFieldListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.oldColorPressed ( oldColor );
        }
    }
}