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
import com.alee.laf.colorchooser.HSBColor;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.language.*;
import com.alee.utils.CollectionUtils;
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

    private Color newColor;
    private Color oldColor;

    private HSBColor newHSBColor;
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
    public void paint ( final Graphics g )
    {
        super.paint ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        final FontMetrics fm = g2d.getFontMetrics ();

        final Map hints = SwingUtils.setupTextAntialias ( g2d );

        g2d.setPaint ( Color.GRAY );
        g2d.drawRect ( 0, 0, getWidth () - 1, getHeight () - 1 );
        g2d.setPaint ( Color.WHITE );
        g2d.drawRect ( 1, 1, getWidth () - 3, getHeight () - 3 );

        g2d.setPaint ( newColor );
        g2d.fillRect ( 2, 2, getWidth () - 4, getHeight () / 2 - 2 );

        final String newText = LM.get ( "weblaf.colorchooser.color.new" );
        final Point nts = LafUtils.getTextCenterShift ( fm, newText );
        g2d.setPaint ( newHSBColor.getBrightness () >= 0.7f && newHSBColor.getSaturation () < 0.7f ? Color.BLACK : Color.WHITE );
        g2d.drawString ( newText, getWidth () / 2 + nts.x, 2 + ( getHeight () - 4 ) / 4 + nts.y );

        g2d.setPaint ( oldColor );
        g2d.fillRect ( 2, getHeight () / 2, getWidth () - 4, getHeight () - getHeight () / 2 - 2 );

        final String currentText = LM.get ( "weblaf.colorchooser.color.current" );
        final Point cts = LafUtils.getTextCenterShift ( fm, currentText );
        g2d.setPaint ( oldHSBColor.getBrightness () >= 0.7f && oldHSBColor.getSaturation () < 0.7f ? Color.BLACK : Color.WHITE );
        g2d.drawString ( currentText, getWidth () / 2 + cts.x, 2 + ( getHeight () - 4 ) * 3 / 4 + cts.y );

        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    public Color getNewColor ()
    {
        return newColor;
    }

    public void setNewColor ( final Color newColor )
    {
        this.newColor = newColor;
        this.newHSBColor = new HSBColor ( newColor );
        this.repaint ();
    }

    public Color getOldColor ()
    {
        return oldColor;
    }

    public void setOldColor ( final Color oldColor )
    {
        this.oldColor = oldColor;
        this.oldHSBColor = new HSBColor ( oldColor );
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