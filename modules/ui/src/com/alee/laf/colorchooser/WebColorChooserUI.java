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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.style.*;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link JColorChooser} component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public class WebColorChooserUI extends WColorChooserUI
{
    /**
     * todo 1. Implement some of the missing JColorChooser features
     */

    /**
     * Runtime variables.
     */
    protected transient WebColorChooserPanel colorChooserPanel;
    protected transient ColorSelectionModel selectionModel;
    protected transient ChangeListener modelChangeListener;
    protected transient boolean modifying = false;

    /**
     * Returns an instance of the {@link WebColorChooserUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebColorChooserUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebColorChooserUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving color chooser reference
        chooser = ( JColorChooser ) c;

        // Applying skin
        StyleManager.installSkin ( chooser );

        selectionModel = chooser.getSelectionModel ();

        chooser.setLayout ( new BorderLayout () );

        colorChooserPanel = new WebColorChooserPanel ( StyleId.colorchooserContent.at ( chooser ), false );
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
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Removing content
        chooser.remove ( colorChooserPanel );
        chooser.setLayout ( null );
        selectionModel.removeChangeListener ( modelChangeListener );
        modelChangeListener = null;
        colorChooserPanel = null;
        selectionModel = null;

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( chooser );

        // Removing color chooser reference
        chooser = null;
    }

    @Override
    public boolean isShowButtonsPanel ()
    {
        return colorChooserPanel.isShowButtonsPanel ();
    }

    @Override
    public void setShowButtonsPanel ( final boolean display )
    {
        colorChooserPanel.setShowButtonsPanel ( display );
    }

    @Override
    public boolean isWebOnlyColors ()
    {
        return colorChooserPanel.isWebOnlyColors ();
    }

    @Override
    public void setWebOnlyColors ( final boolean webOnly )
    {
        colorChooserPanel.setWebOnlyColors ( webOnly );
    }

    @Override
    public Color getPreviousColor ()
    {
        return colorChooserPanel.getOldColor ();
    }

    @Override
    public void setPreviousColor ( final Color previous )
    {
        colorChooserPanel.setOldColor ( previous );
    }

    @Override
    public void resetResult ()
    {
        colorChooserPanel.resetResult ();
    }

    @Override
    public void setResult ( final int result )
    {
        colorChooserPanel.setResult ( result );
    }

    @Override
    public int getResult ()
    {
        return colorChooserPanel.getResult ();
    }

    @Override
    public void addColorChooserListener ( @NotNull final ColorChooserListener listener )
    {
        colorChooserPanel.addColorChooserListener ( listener );
    }

    @Override
    public void removeColorChooserListener ( @NotNull final ColorChooserListener listener )
    {
        colorChooserPanel.removeColorChooserListener ( listener );
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, width, height );
    }

    @NotNull
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return null;
    }
}