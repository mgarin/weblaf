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

package com.alee.laf.colorchooser;

import com.alee.laf.WebLookAndFeel;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicColorChooserUI;
import java.awt.*;

/**
 * User: mgarin Date: 17.08.11 Time: 22:50
 */

public class WebColorChooserUI extends BasicColorChooserUI
{
    /**
     * todo 1. Implement base JColorChooser features
     */

    private WebColorChooserPanel colorChooserPanel;
    private ColorSelectionModel selectionModel;
    private ChangeListener modelChangeListener;
    private boolean modifying = false;

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebColorChooserUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        chooser = ( JColorChooser ) c;
        selectionModel = chooser.getSelectionModel ();

        LookAndFeel.installProperty ( chooser, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        chooser.setLayout ( new BorderLayout () );

        colorChooserPanel = new WebColorChooserPanel ( false );
        colorChooserPanel.setColor ( selectionModel.getSelectedColor () );
        colorChooserPanel.addChangeListener ( new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                if ( !modifying )
                {
                    modifying = true;
                    selectionModel.setSelectedColor ( colorChooserPanel.getColor () );
                    modifying = false;
                }
            }
        } );
        chooser.add ( colorChooserPanel, BorderLayout.CENTER );

        modelChangeListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                if ( !modifying )
                {
                    modifying = true;
                    colorChooserPanel.setColor ( selectionModel.getSelectedColor () );
                    modifying = false;
                }
            }
        };
        selectionModel.addChangeListener ( modelChangeListener );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        chooser.remove ( colorChooserPanel );
        chooser.setLayout ( null );
        selectionModel.removeChangeListener ( modelChangeListener );
        modelChangeListener = null;
        colorChooserPanel = null;
        selectionModel = null;
        chooser = null;
    }

    public boolean isShowButtonsPanel ()
    {
        return colorChooserPanel.isShowButtonsPanel ();
    }

    public void setShowButtonsPanel ( final boolean showButtonsPanel )
    {
        colorChooserPanel.setShowButtonsPanel ( showButtonsPanel );
    }

    public boolean isWebOnlyColors ()
    {
        return colorChooserPanel.isWebOnlyColors ();
    }

    public void setWebOnlyColors ( final boolean webOnlyColors )
    {
        colorChooserPanel.setWebOnlyColors ( webOnlyColors );
    }

    public Color getOldColor ()
    {
        return colorChooserPanel.getOldColor ();
    }

    public void setOldColor ( final Color oldColor )
    {
        colorChooserPanel.setOldColor ( oldColor );
    }

    public void resetResult ()
    {
        colorChooserPanel.resetResult ();
    }

    public void setResult ( final int result )
    {
        colorChooserPanel.setResult ( result );
    }

    public int getResult ()
    {
        return colorChooserPanel.getResult ();
    }

    public void addColorChooserListener ( final ColorChooserListener colorChooserListener )
    {
        colorChooserPanel.addColorChooserListener ( colorChooserListener );
    }

    public void removeColorChooserListener ( final ColorChooserListener colorChooserListener )
    {
        colorChooserPanel.removeColorChooserListener ( colorChooserListener );
    }
}
