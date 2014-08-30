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

package com.alee.managers.language.updaters;

import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.managers.language.data.Value;

import javax.swing.*;
import java.awt.*;

/**
 * This class provides language default updates for Dialog component.
 *
 * @author Mikle Garin
 */

public class WebDialogLU extends DefaultLanguageUpdater<Dialog>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void update ( final Dialog c, final String key, final Value value, final Object... data )
    {
        if ( c instanceof JDialog )
        {
            final JRootPane rootPane = ( ( JDialog ) c ).getRootPane ();
            if ( rootPane.getUI () instanceof WebRootPaneUI )
            {
                final JComponent titleComponent = ( ( WebRootPaneUI ) rootPane.getUI () ).getTitleComponent ();
                if ( titleComponent != null )
                {
                    titleComponent.repaint ();

                    // final JLabel title = SwingUtils.getFirst ( titleComponent, JLabel.class );
                    // title.setText ( getDefaultText ( value, data ) );
                }
            }
        }
        c.setTitle ( getDefaultText ( value, data ) );
    }
}