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

package com.alee.examples.content;

import com.alee.extended.image.WebImage;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.StyleConstants;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.ImageUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */

public class IconProgress extends WebPanel
{
    public IconProgress ()
    {
        super ( true, new HorizontalFlowLayout ( 2, false ) );
        setOpaque ( false );
        setMargin ( 2 );
        setWebColored ( false );
        setBackground ( Color.WHITE );
    }

    public void addLoadedElement ( final Icon icon )
    {
        add ( new FadeInImage ( ImageUtils.getBufferedImage ( icon ) ) );
    }

    private class FadeInImage extends WebImage
    {
        public FadeInImage ( final Image image )
        {
            super ( image );
            setTransparency ( 0f );
            addAncestorListener ( new AncestorAdapter ()
            {
                @Override
                public void ancestorAdded ( final AncestorEvent event )
                {
                    fadeIn ();
                }
            } );
        }

        private void fadeIn ()
        {
            WebTimer.repeat ( "FadeInImage.updater", StyleConstants.avgAnimationDelay, new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    final float t = getTransparency ();
                    if ( t < 1f )
                    {
                        setTransparency ( Math.min ( t + 0.05f, 1f ) );
                    }
                    else
                    {
                        ( ( WebTimer ) e.getSource () ).stop ();
                    }
                }
            } );
        }
    }
}