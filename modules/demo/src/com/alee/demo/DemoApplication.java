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

package com.alee.demo;

import com.alee.demo.api.Example;
import com.alee.demo.api.ExampleData;
import com.alee.demo.api.ExamplesManager;
import com.alee.demo.skin.DemoApplicationSkin;
import com.alee.demo.ui.examples.ExamplesFrame;
import com.alee.extended.dock.DockingPaneLayout;
import com.alee.extended.dock.WebDockablePane;
import com.alee.extended.magnifier.MagnifierGlass;
import com.alee.extended.panel.WebOverlay;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.tab.*;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.managers.language.LM;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleManager;
import com.alee.managers.version.VersionManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * WebLaF demo application containing various component and feature examples.
 *
 * @author Mikle Garin
 */

public class DemoApplication extends WebFrame
{
    /**
     * Demo application instance.
     */
    private static DemoApplication instance;

    /**
     * Magnifier used by examples across this application.
     */
    private final MagnifierGlass magnifier = new MagnifierGlass ();
    private final List<MagnifierListener> magnifierListeners = new ArrayList<MagnifierListener> ();

    /**
     * Demo application base UI elements.
     */
    private WebDockablePane dockablePane;
    private ExamplesFrame examplesFrame;
    private WebDocumentPane<DocumentData> contentPane;

    /**
     * Returns demo application instance.
     *
     * @return demo application instance
     */
    public static DemoApplication getInstance ()
    {
        if ( instance == null )
        {
            instance = new DemoApplication ();
        }
        return instance;
    }

    /**
     * Initializes demo application content.
     */
    private void initialize ()
    {
        updateTitle ();
        initializeDocks ();
        initializeContent ();
        initializeStatus ();
        setupFrame ();
    }

    /**
     * Initializes demo application dockable frames.
     */
    private void initializeDocks ()
    {
        dockablePane = new WebDockablePane ();
        add ( dockablePane, BorderLayout.CENTER );

        examplesFrame = new ExamplesFrame ();
        dockablePane.add ( examplesFrame, DockingPaneLayout.LEFT_FRAME );
    }

    /**
     * Initializes demo application content pane.
     */
    private void initializeContent ()
    {
        contentPane = new WebDocumentPane<DocumentData> ();
        contentPane.setCloseable ( true );
        contentPane.setDragEnabled ( true );
        contentPane.setDragBetweenPanesEnabled ( false );
        contentPane.setSplitEnabled ( true );
        contentPane.onDocumentSelection ( new DocumentDataRunnable<DocumentData> ()
        {
            @Override
            public void run ( final DocumentData document, final PaneData<DocumentData> pane, final int index )
            {
                updateTitle ();
            }
        } );

        final WebOverlay overlay = new WebOverlay ( contentPane );
        final WebLabel overlayContent = new WebLabel ( "demo.content.empty" );
        contentPane.addDocumentListener ( new DocumentAdapter<DocumentData> ()
        {
            @Override
            public void opened ( final DocumentData document, final PaneData<DocumentData> pane, final int index )
            {
                overlayContent.setVisible ( false );
            }

            @Override
            public void closed ( final DocumentData document, final PaneData<DocumentData> pane, final int index )
            {
                overlayContent.setVisible ( contentPane.getDocumentsCount () == 0 );
            }
        } );
        overlay.addOverlay ( overlayContent, SwingConstants.CENTER, SwingConstants.CENTER );

        dockablePane.add ( overlay, DockingPaneLayout.CONTENT );
    }

    /**
     * Initializes status bar and its content.
     */
    private void initializeStatus ()
    {
        final WebStatusBar statusBar = new WebStatusBar ();

        final WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( 150 );
        statusBar.addToEnd ( memoryBar );

        add ( statusBar, BorderLayout.SOUTH );
    }

    /**
     * Initializes demo application frame.
     */
    private void setupFrame ()
    {
        setIconImages ( WebLookAndFeel.getImages () );
        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
        setSize ( 1200, 800 );
        setLocationRelativeTo ( null );
    }

    /**
     * Updates demo application title.
     */
    public void updateTitle ()
    {
        final DocumentData doc = contentPane != null ? contentPane.getSelectedDocument () : null;
        setTitle ( VersionManager.getLibraryVersion ().toString () + ( doc != null ? " - " + LM.get ( doc.getTitle () ) : "" ) );
    }

    /**
     * Opens specified example in content pane.
     *
     * @param example example to open
     */
    public void open ( final Example example )
    {
        contentPane.openDocument ( ExampleData.forExample ( example ) );
    }

    /**
     * Displays demo application.
     */
    public void display ()
    {
        SwingUtilities.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                initialize ();
                setVisible ( true );
            }
        } );
    }

    /**
     * Displays or hides magnifier.
     */
    public void switchMagnifier ()
    {
        // Switching magnifier state
        magnifier.displayOrDispose ( getInstance () );

        // Informing listeners
        for ( final MagnifierListener listener : magnifierListeners )
        {
            listener.switched ( magnifier.isEnabled () );
        }
    }

    /**
     * Returns whether or not magnifier is enabled.
     *
     * @return true if magnifier is enabled, false otherwise
     */
    public boolean isMagnifierEnabled ()
    {
        return magnifier.isEnabled ();
    }

    /**
     * Adds magnifier state change listener.
     *
     * @param listener magnifier state change listener
     */
    public void onMagnifierSwitch ( final MagnifierListener listener )
    {
        magnifierListeners.add ( listener );
    }

    /**
     * Custom magnifier switch listener.
     */
    public static interface MagnifierListener
    {
        /**
         * Informs about magnifier enabled state change.
         *
         * @param enabled whether or not magnifier is enabled
         */
        public void switched ( final boolean enabled );
    }

    /**
     * Properly launches demo application.
     *
     * @param args launch arguments
     */
    public static void main ( final String[] args )
    {
        // Exampler settings location
        SettingsManager.setDefaultSettingsDirName ( ".weblaf-demo" );
        SettingsManager.setDefaultSettingsGroup ( "WebLookAndFeelDemo" );
        SettingsManager.setSaveOnChange ( true );

        // Demo application languages
        LanguageManager.setLanguages ( LanguageManager.ENGLISH, LanguageManager.RUSSIAN );

        // Demo application skin
        // It extends default WebLaF skin and adds some custom styling
        StyleManager.setDefaultSkin ( DemoApplicationSkin.class.getCanonicalName () );

        // Look and Feel
        WebLookAndFeel.install ();

        // Loading demo dictionary
        LanguageManager.addDictionary ( DemoApplication.class, "language/language.xml" );

        // Initializing demo application managers
        ExamplesManager.initialize ();

        // Starting demo application
        DemoApplication.getInstance ().display ();

        // Interface inspector used for demo aplication testing
        // InterfaceInspector.showPopOver ( DemoApplication.getInstance ().getRootPane () );
    }
}