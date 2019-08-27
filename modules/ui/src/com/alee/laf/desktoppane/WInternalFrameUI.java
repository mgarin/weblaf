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

package com.alee.laf.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.WebUI;
import com.alee.utils.LafUtils;
import com.alee.utils.ProprietaryUtils;

import javax.swing.*;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Pluggable look and feel interface for any component based on {@link JInternalFrame}.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class WInternalFrameUI<C extends JInternalFrame> extends InternalFrameUI implements WebUI<C>
{
    /**
     * Shared {@link DesktopManager}.
     */
    protected static DesktopManager sharedDesktopManager;

    /**
     * {@link InternalFrameInputListener} for the {@link JInternalFrame}.
     */
    protected InternalFrameInputListener<C> inputListener;

    /**
     * Runtime variables.
     */
    protected C internalFrame;
    protected Component northPane;
    protected Component southPane;
    protected Component westPane;
    protected Component eastPane;

    /**
     * Constructs new {@link WInternalFrameUI}.
     */
    public WInternalFrameUI ()
    {
        ProprietaryUtils.installAWTEventListener ();
    }

    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "InternalFrame.";
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving component reference
        internalFrame = ( C ) c;

        // Installing default component settings
        installDefaults ();

        // Installing default component listeners
        installListeners ();

        // Installing default component elements
        installComponents ();
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling default component elements
        uninstallComponents ();

        // Uninstalling default component listeners
        uninstallListeners ();

        // Uninstalling default component settings
        uninstallDefaults ();

        // Removing component reference
        internalFrame = null;
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LafUtils.installDefaults ( internalFrame, getPropertyPrefix () );

        internalFrame.setLayout ( createLayoutManager () );

        final Icon frameIcon = internalFrame.getFrameIcon ();
        if ( frameIcon == null || frameIcon instanceof UIResource )
        {
            internalFrame.setFrameIcon ( UIManager.getIcon ( "InternalFrame.icon" ) );
        }
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        final Icon frameIcon = internalFrame.getFrameIcon ();
        if ( frameIcon instanceof UIResource )
        {
            internalFrame.setFrameIcon ( null );
        }

        internalFrame.setLayout ( null );

        Cursor s = internalFrame.getLastCursor ();
        if ( s == null )
        {
            s = Cursor.getPredefinedCursor ( Cursor.DEFAULT_CURSOR );
        }
        internalFrame.setCursor ( s );

        LafUtils.uninstallDefaults ( internalFrame );
    }

    /**
     * Returns {@link LayoutManager} for {@link JInternalFrame}.
     *
     * @return {@link LayoutManager} for {@link JInternalFrame}
     */
    protected LayoutManager createLayoutManager ()
    {
        return new InternalFrameLayout ();
    }

    /**
     * Installs UI elements.
     */
    protected void installComponents ()
    {
        northPane = createNorthPane ();
        if ( northPane != null )
        {
            internalFrame.add ( northPane, InternalFrameLayout.NORTH_PANE );
            inputListener.installPane ( northPane );
        }
        southPane = createSouthPane ();
        if ( southPane != null )
        {
            internalFrame.add ( southPane, InternalFrameLayout.SOUTH_PANE );
            inputListener.installPane ( southPane );
        }
        westPane = createWestPane ();
        if ( westPane != null )
        {
            internalFrame.add ( westPane, InternalFrameLayout.WEST_PANE );
            inputListener.installPane ( westPane );
        }
        eastPane = createEastPane ();
        if ( eastPane != null )
        {
            internalFrame.add ( eastPane, InternalFrameLayout.EAST_PANE );
            inputListener.installPane ( eastPane );
        }
    }

    /**
     * Uninstalls UI elements.
     */
    protected void uninstallComponents ()
    {
        if ( northPane != null )
        {
            inputListener.uninstallPane ( northPane );
            internalFrame.remove ( northPane );
        }
        if ( southPane != null )
        {
            inputListener.uninstallPane ( southPane );
            internalFrame.remove ( southPane );
        }
        if ( westPane != null )
        {
            inputListener.uninstallPane ( westPane );
            internalFrame.remove ( westPane );
        }
        if ( eastPane != null )
        {
            inputListener.uninstallPane ( eastPane );
            internalFrame.remove ( eastPane );
        }
    }

    /**
     * Returns newly created north pane {@link Component} or {@code null} if none available.
     *
     * @return newly created north pane {@link Component} or {@code null} if none available
     */
    @Nullable
    protected Component createNorthPane ()
    {
        return new WebInternalFrameTitlePane ( internalFrame, internalFrame );
    }

    /**
     * Returns newly created south pane {@link Component} or {@code null} if none available.
     *
     * @return newly created south pane {@link Component} or {@code null} if none available
     */
    @Nullable
    protected Component createSouthPane ()
    {
        return null;
    }

    /**
     * Returns newly created west pane {@link Component} or {@code null} if none available.
     *
     * @return newly created west pane {@link Component} or {@code null} if none available
     */
    @Nullable
    protected Component createWestPane ()
    {
        return null;
    }

    /**
     * Returns newly created east pane {@link Component} or {@code null} if none available.
     *
     * @return newly created east pane {@link Component} or {@code null} if none available
     */
    @Nullable
    protected Component createEastPane ()
    {
        return null;
    }

    /**
     * Returns north pane {@link Component} or {@code null} if none available.
     *
     * @return north pane {@link Component} or {@code null} if none available
     */
    @Nullable
    public Component getNorthPane ()
    {
        return northPane;
    }

    /**
     * Returns south pane {@link Component} or {@code null} if none available.
     *
     * @return south pane {@link Component} or {@code null} if none available
     */
    @Nullable
    public Component getSouthPane ()
    {
        return southPane;
    }

    /**
     * Returns west pane {@link Component} or {@code null} if none available.
     *
     * @return west pane {@link Component} or {@code null} if none available
     */
    @Nullable
    public Component getWestPane ()
    {
        return westPane;
    }

    /**
     * Returns east pane {@link Component} or {@code null} if none available.
     *
     * @return east pane {@link Component} or {@code null} if none available
     */
    @Nullable
    public Component getEastPane ()
    {
        return eastPane;
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        inputListener = createInternalFrameInputListener ();
        inputListener.install ( internalFrame );
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        inputListener.uninstall ( internalFrame );
        inputListener = null;
    }

    /**
     * Returns {@link InternalFrameInputListener} for the {@link JInternalFrame}.
     *
     * @return {@link InternalFrameInputListener} for the {@link JInternalFrame}
     */
    protected InternalFrameInputListener<C> createInternalFrameInputListener ()
    {
        return new WInternalFrameInputListener<C, WInternalFrameUI<C>> ();
    }

    /**
     * Returns {@link DesktopManager}.
     * Attempts to retrieve it from {@link JDesktopPane} first, otherwise creates or returns cached static {@link DesktopManager}.
     *
     * @return {@link DesktopManager}
     */
    public DesktopManager getDesktopManager ()
    {
        final DesktopManager desktopManager;
        if ( internalFrame.getDesktopPane () != null && internalFrame.getDesktopPane ().getDesktopManager () != null )
        {
            desktopManager = internalFrame.getDesktopPane ().getDesktopManager ();
        }
        else
        {
            if ( sharedDesktopManager == null )
            {
                sharedDesktopManager = createDesktopManager ();
            }
            desktopManager = sharedDesktopManager;
        }
        return desktopManager;
    }

    /**
     * Returns newly created shared {@link DesktopManager}.
     *
     * @return newly created shared {@link DesktopManager}
     */
    protected DesktopManager createDesktopManager ()
    {
        return new DefaultDesktopManager ();
    }

    /**
     * This method is called when the user wants to close the frame.
     * The {@code playCloseSound} Action is fired.
     * This action is delegated to the desktopManager.
     */
    public void closeFrame ()
    {
        // Internal Frame Auditory Cue Activation
        WebLookAndFeel.playSound ( internalFrame, "InternalFrame.closeSound" );

        // Delegate to desktop manager
        getDesktopManager ().closeFrame ( internalFrame );
    }

    /**
     * This method is called when the user wants to maximize the frame.
     * The {@code playMaximizeSound} Action is fired.
     * This action is delegated to the desktopManager.
     */
    public void maximizeFrame ()
    {
        // Internal Frame Auditory Cue Activation
        WebLookAndFeel.playSound ( internalFrame, "InternalFrame.maximizeSound" );

        // Delegate to desktop manager
        getDesktopManager ().maximizeFrame ( internalFrame );
    }

    /**
     * This method is called when the user wants to minimize the frame.
     * The {@code playRestoreDownSound} Action is fired.
     * This action is delegated to the desktopManager.
     */
    public void minimizeFrame ()
    {
        // Internal Frame Auditory Cue Activation
        if ( !internalFrame.isIcon () )
        {
            // This method seems to regularly get called after an
            // internal frame is iconified. Don't play this sound then.
            WebLookAndFeel.playSound ( internalFrame, "InternalFrame.restoreDownSound" );
        }

        // Delegate to desktop manager
        getDesktopManager ().minimizeFrame ( internalFrame );
    }

    /**
     * This method is called when the user wants to iconify the frame.
     * The {@code playMinimizeSound} Action is fired.
     * This action is delegated to the desktopManager.
     */
    public void iconifyFrame ()
    {
        // Internal Frame Auditory Cue Activation
        WebLookAndFeel.playSound ( internalFrame, "InternalFrame.minimizeSound" );

        // Delegate to desktop manager
        getDesktopManager ().iconifyFrame ( internalFrame );
    }

    /**
     * This method is called when the user wants to deiconify the frame.
     * The {@code playRestoreUpSound} Action is fired.
     * This action is delegated to the desktopManager.
     */
    public void deiconifyFrame ()
    {
        // Internal Frame Auditory Cue Activation
        if ( !internalFrame.isMaximum () )
        {
            // This method seems to regularly get called after an
            // internal frame is maximized. Don't play this sound then.
            WebLookAndFeel.playSound ( internalFrame, "InternalFrame.restoreUpSound" );
        }

        // Delegate to desktop manager
        getDesktopManager ().deiconifyFrame ( internalFrame );
    }

    /**
     * This method is called when the frame becomes selected.
     * This action is delegated to the desktopManager.
     */
    public void activateFrame ()
    {
        getDesktopManager ().activateFrame ( internalFrame );
    }

    /**
     * This method is called when the frame is no longer selected.
     * This action is delegated to the desktopManager.
     */
    public void deactivateFrame ()
    {
        getDesktopManager ().deactivateFrame ( internalFrame );
    }
}