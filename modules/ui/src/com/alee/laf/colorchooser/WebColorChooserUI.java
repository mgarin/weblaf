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

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicColorChooserUI;
import java.awt.*;

/**
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebColorChooserUI extends BasicColorChooserUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * todo 1. Implement base JColorChooser features
     */

    /**
     * Component painter.
     */
    @DefaultPainter ( ColorChooserPainter.class )
    protected IColorChooserPainter painter;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;
    protected WebColorChooserPanel colorChooserPanel;
    protected ColorSelectionModel selectionModel;
    protected ChangeListener modelChangeListener;
    protected boolean modifying = false;

    /**
     * Returns an instance of the WebColorChooserUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebColorChooserUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebColorChooserUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Saving color chooser reference
        chooser = ( JColorChooser ) c;

        // Applying skin
        StyleManager.installSkin ( chooser );

        selectionModel = chooser.getSelectionModel ();

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

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( chooser );

        // Removing content
        chooser.remove ( colorChooserPanel );
        chooser.setLayout ( null );
        selectionModel.removeChangeListener ( modelChangeListener );
        modelChangeListener = null;
        colorChooserPanel = null;
        selectionModel = null;

        // Removing color chooser reference
        chooser = null;
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( chooser );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( chooser, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( chooser, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns color chooser painter.
     *
     * @return color chooser painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets color chooser painter.
     * Pass null to remove color chooser painter.
     *
     * @param painter new color chooser painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( chooser, new DataRunnable<IColorChooserPainter> ()
        {
            @Override
            public void run ( final IColorChooserPainter newPainter )
            {
                WebColorChooserUI.this.painter = newPainter;
            }
        }, this.painter, painter, IColorChooserPainter.class, AdaptiveColorChooserPainter.class );
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

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}