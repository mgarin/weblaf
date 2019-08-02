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
import com.alee.api.jdk.Consumer;

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
public class WebColorChooserUI extends WColorChooserUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * todo 1. Implement some of the missing JColorChooser features
     */

    /**
     * Component painter.
     */
    @DefaultPainter ( ColorChooserPainter.class )
    protected IColorChooserPainter painter;

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
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebColorChooserUI ();
    }

    @Override
    public void installUI ( final JComponent c )
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
    public void uninstallUI ( final JComponent c )
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
    public Shape getShape ()
    {
        return PainterSupport.getShape ( chooser, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( chooser, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( chooser, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( chooser );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( chooser, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( chooser );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( chooser, padding );
    }

    /**
     * Returns color chooser painter.
     *
     * @return color chooser painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets color chooser painter.
     * Pass null to remove color chooser painter.
     *
     * @param painter new color chooser painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( chooser, new Consumer<IColorChooserPainter> ()
        {
            @Override
            public void accept ( final IColorChooserPainter newPainter )
            {
                WebColorChooserUI.this.painter = newPainter;
            }
        }, this.painter, painter, IColorChooserPainter.class, AdaptiveColorChooserPainter.class );
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
    public void addColorChooserListener ( final ColorChooserListener listener )
    {
        colorChooserPanel.addColorChooserListener ( listener );
    }

    @Override
    public void removeColorChooserListener ( final ColorChooserListener listener )
    {
        colorChooserPanel.removeColorChooserListener ( listener );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        // return PainterSupport.getPreferredSize ( c, painter );
        return null;
    }
}