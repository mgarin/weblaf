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
import com.alee.demo.icons.DemoIcons;
import com.alee.demo.skin.DemoApplicationSkin;
import com.alee.demo.skin.DemoStyles;
import com.alee.demo.skin.FeatureStateBackground;
import com.alee.demo.ui.examples.ExamplesFrame;
import com.alee.extended.dock.DockingPaneLayout;
import com.alee.extended.dock.WebDockablePane;
import com.alee.extended.label.WebLinkLabel;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.magnifier.MagnifierGlass;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebOverlay;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.tab.*;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.managers.language.LM;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.managers.version.VersionManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * WebLaF demo application containing various component and feature examples.
 *
 * @author Mikle Garin
 */

public final class DemoApplication extends WebFrame
{
    /**
     * Developer contacts.
     * Also used as data for some examples.
     */
    public static final String WEBLAF_EMAIL = "mgarin@alee.com";
    public static final String WEBLAF_SITE = "http://weblookandfeel.com/";
    public static final String WEBLAF_GITHUB = "https://github.com/mgarin/weblaf";
    public static final String WEBLAF_GITTER = "https://gitter.im/mgarin/weblaf";

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

        final WebPanel overlayContainer = new WebPanel ( DemoStyles.emptycontentPanel );

        final StyleId overlayLabelId = DemoStyles.emptycontentLabel.at ( overlayContainer );
        final WebStyledLabel guide = new WebStyledLabel ( overlayLabelId, "demo.content.empty", DemoIcons.compass36 ).changeFontSize ( 5 );
        final WebStyledLabel issues = new WebStyledLabel ( overlayLabelId, "demo.content.issues", DemoIcons.bug36 ).changeFontSize ( 5 );
        overlayContainer.add ( new GroupPanel ( 20, false, guide, issues ) );

        contentPane.addDocumentListener ( new DocumentAdapter<DocumentData> ()
        {
            @Override
            public void opened ( final DocumentData document, final PaneData<DocumentData> pane, final int index )
            {
                overlayContainer.setVisible ( false );
            }

            @Override
            public void closed ( final DocumentData document, final PaneData<DocumentData> pane, final int index )
            {
                overlayContainer.setVisible ( contentPane.getDocumentsCount () == 0 );
            }
        } );
        overlay.addOverlay ( overlayContainer, SwingConstants.CENTER, SwingConstants.CENTER );

        dockablePane.add ( overlay, DockingPaneLayout.CONTENT );
    }

    /**
     * Initializes status bar and its content.
     */
    private void initializeStatus ()
    {
        final WebStatusBar statusBar = new WebStatusBar ();

        final WebLinkLabel weblaf = new WebLinkLabel ( DemoStyles.resourceLink );
        weblaf.setLink ( "demo.statusbar.resources.weblaf", WEBLAF_SITE );
        weblaf.setIcon ( WebLookAndFeel.getIcon ( 16 ) );
        statusBar.add ( weblaf );

        final WebLinkLabel github = new WebLinkLabel ( DemoStyles.resourceLink );
        github.setLink ( "demo.statusbar.resources.github", WEBLAF_GITHUB );
        github.setIcon ( DemoIcons.github16 );
        statusBar.add ( github );

        final WebLinkLabel gitter = new WebLinkLabel ( DemoStyles.resourceLink );
        gitter.setLink ( "demo.statusbar.resources.gitter", WEBLAF_GITTER );
        gitter.setIcon ( DemoIcons.gitter16 );
        statusBar.add ( gitter );

        //

        final WebLabel usedMemory = new WebLabel ( "demo.statusbar.ram", DemoIcons.arrowRight16 );
        usedMemory.setHorizontalTextPosition ( WebLabel.LEADING );
        statusBar.addToEnd ( usedMemory );

        final WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( 150 );
        statusBar.addToEnd ( memoryBar );

        add ( statusBar, BorderLayout.SOUTH );

        // Custom status bar margin for notification manager
        NotificationManager.setMargin ( 0, 0, memoryBar.getPreferredSize ().height, 0 );
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
        SwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                initialize ();
                setVisible ( true );
                setExtendedState ( JFrame.MAXIMIZED_BOTH );
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
        SwingUtils.enableEventQueueLogging ();
        SwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Configuring settings location
                SettingsManager.setDefaultSettingsDirName ( ".weblaf-demo" );
                SettingsManager.setDefaultSettingsGroup ( "WebLookAndFeelDemo" );
                SettingsManager.setSaveOnChange ( true );

                // Configurting availale languages
                LanguageManager.setLanguages ( LanguageManager.ENGLISH, LanguageManager.RUSSIAN );

                // Adding demo data aliases
                XmlUtils.processAnnotations ( FeatureStateBackground.class );

                // Installing Look and Feel
                WebLookAndFeel.install ( DemoApplicationSkin.class );

                // Loading demo dictionary
                LanguageManager.addDictionary ( DemoApplication.class, "language/language.xml" );

                // Initializing demo application managers
                ExamplesManager.initialize ();

                // Starting demo application
                DemoApplication.getInstance ().display ();

                // Interface inspector used for demo aplication testing
                // InterfaceInspector.showPopOver ( DemoApplication.getInstance ().getRootPane () );
            }
        } );
    }
}