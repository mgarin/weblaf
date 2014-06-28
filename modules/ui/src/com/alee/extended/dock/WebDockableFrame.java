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

package com.alee.extended.dock;

import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.painter.Painter;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 28.06.12 Time: 13:31
 */

public class WebDockableFrame extends WebPanel implements LanguageMethods
{
    public static final ImageIcon dockTop = new ImageIcon ( WebDockablePane.class.getResource ( "icons/dock_top.png" ) );
    public static final ImageIcon dockLeft = new ImageIcon ( WebDockablePane.class.getResource ( "icons/dock_left.png" ) );
    public static final ImageIcon dockRight = new ImageIcon ( WebDockablePane.class.getResource ( "icons/dock_right.png" ) );
    public static final ImageIcon dockBottom = new ImageIcon ( WebDockablePane.class.getResource ( "icons/dock_bottom.png" ) );

    public static final String ID_PREFIX = "WDF";

    protected String frameId;
    protected FrameType frameType;

    protected final WebPanel titlePanel;
    protected final WebLabel titleLabel;
    protected final WebPanel buttonsPanel;
    protected final WebButton dockButton;

    public WebDockableFrame ()
    {
        this ( "" );
    }

    public WebDockableFrame ( final String frameTitle )
    {
        this ( TextUtils.generateId ( ID_PREFIX ), frameTitle );
    }

    public WebDockableFrame ( final String frameId, final String frameTitle )
    {
        this ( frameId, null, frameTitle );
    }

    public WebDockableFrame ( final Icon frameIcon )
    {
        this ( frameIcon, "" );
    }

    public WebDockableFrame ( final Icon frameIcon, final String frameTitle )
    {
        this ( TextUtils.generateId ( ID_PREFIX ), frameIcon, frameTitle );
    }

    public WebDockableFrame ( final String frameId, final Icon frameIcon )
    {
        this ( frameId, frameIcon, "" );
    }

    public WebDockableFrame ( final String frameId, final Icon frameIcon, final String frameTitle )
    {
        super ( true );

        this.frameId = frameId;

        setShadeWidth ( 0 );
        setWebColoredBackground ( false );
        setPaintSides ( false, false, false, false );

        titlePanel = new WebPanel ( "dockable-frame-title" );
        add ( titlePanel, BorderLayout.NORTH );

        titleLabel = new WebLabel ( frameTitle, frameIcon );
        titleLabel.setStyleId ( "dockable-frame-title" );
        titlePanel.add ( titleLabel, BorderLayout.CENTER );

        buttonsPanel = new WebPanel ( "dockable-frame-buttons", new HorizontalFlowLayout ( 0, false ) );
        titlePanel.add ( buttonsPanel, BorderLayout.EAST );

        dockButton = new WebButton ();
        dockButton.setLeftRightSpacing ( 0 );
        dockButton.setShadeWidth ( 0 );
        dockButton.setFocusable ( false );
        dockButton.setDrawSides ( false, true, false, false );
        buttonsPanel.add ( dockButton );
    }

    public String getFrameId ()
    {
        return frameId;
    }

    public void setFrameId ( final String frameId )
    {
        this.frameId = frameId;
    }

    public void setIcon ( final Icon icon )
    {
        titleLabel.setIcon ( icon );
    }

    public Icon getIcon ()
    {
        return titleLabel.getIcon ();
    }

    public FrameType getFrameType ()
    {
        return frameType;
    }

    public void setFrameType ( final FrameType frameType )
    {
        this.frameType = frameType;

        // Changing displayed sides
        setPaintSides ( frameType.equals ( FrameType.bottom ), frameType.equals ( FrameType.right ), frameType.equals ( FrameType.top ),
                frameType.equals ( FrameType.left ) );

        // Changing tool icons
        dockButton.setIcon ( getDockIcon ( frameType ) );
    }

    public void setTitlePainter ( final Painter painter )
    {
        titlePanel.setPainter ( painter );
    }

    public Painter getTitlePainter ()
    {
        return titlePanel.getPainter ();
    }

    public WebPanel getTitlePanel ()
    {
        return titlePanel;
    }

    public WebLabel getTitleLabel ()
    {
        return titleLabel;
    }

    public WebPanel getButtonsPanel ()
    {
        return buttonsPanel;
    }

    public void addButton ( final WebButton button )
    {
        // Styling button properly
        button.setShadeWidth ( 0 );
        button.setDrawSides ( false, true, false, false );
        if ( button.getText () == null || button.getText ().trim ().length () == 0 )
        {
            button.setLeftRightSpacing ( 0 );
        }

        // Adding new button
        buttonsPanel.add ( button, 0 );
        buttonsPanel.revalidate ();
        buttonsPanel.repaint ();
    }

    public WebButton getDockButton ()
    {
        return dockButton;
    }

    public String getTitle ()
    {
        return titleLabel.getText ();
    }

    public void setTitle ( final String title )
    {
        titleLabel.setText ( title );
    }

    protected Icon getDockIcon ( final FrameType frameType )
    {
        if ( frameType.equals ( FrameType.top ) )
        {
            return dockTop;
        }
        else if ( frameType.equals ( FrameType.left ) )
        {
            return dockLeft;
        }
        else if ( frameType.equals ( FrameType.right ) )
        {
            return dockRight;
        }
        else if ( frameType.equals ( FrameType.bottom ) )
        {
            return dockBottom;
        }
        else
        {
            return null;
        }
    }

    /**
     * Language methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }
}