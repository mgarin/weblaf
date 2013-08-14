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

package com.alee.examples;

import com.alee.examples.content.*;
import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbToggleButton;
import com.alee.extended.image.DisplayType;
import com.alee.extended.image.WebImage;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.transition.TransitionAdapter;
import com.alee.extended.transition.effects.Direction;
import com.alee.extended.transition.effects.TransitionEffect;
import com.alee.extended.transition.effects.curtain.CurtainTransitionEffect;
import com.alee.extended.transition.effects.curtain.CurtainType;
import com.alee.extended.transition.effects.fade.FadeTransitionEffect;
import com.alee.extended.window.WebProgressDialog;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.highlight.HighlightManager;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyInfo;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.tooltip.TooltipAdapter;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.managers.version.VersionInfo;
import com.alee.managers.version.VersionManager;
import com.alee.utils.*;
import com.alee.utils.reflection.JarEntry;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mgarin Date: 14.02.12 Time: 11:35
 */

public class WebLookAndFeelDemo extends WebFrame
{
    public static final ImageIcon infoIcon = new ImageIcon ( WebLookAndFeelDemo.class.getResource ( "icons/info.png" ) );
    public static final ImageIcon legendIcon = new ImageIcon ( WebLookAndFeelDemo.class.getResource ( "icons/legend.png" ) );
    public static final ImageIcon animationIcon = new ImageIcon ( WebLookAndFeelDemo.class.getResource ( "icons/animate.png" ) );
    public static final ImageIcon tabNamesIcon = new ImageIcon ( WebLookAndFeelDemo.class.getResource ( "icons/showTabNames.png" ) );
    public static final ImageIcon orientationIcon = new ImageIcon ( WebLookAndFeelDemo.class.getResource ( "icons/orientation.png" ) );
    public static final ImageIcon enabledIcon = new ImageIcon ( WebLookAndFeelDemo.class.getResource ( "icons/enabled.png" ) );
    public static final ImageIcon disabledIcon = new ImageIcon ( WebLookAndFeelDemo.class.getResource ( "icons/disabled.png" ) );

    public static final String WEBLAF_SITE = "http://weblookandfeel.com/";
    public static final String WEBLAF_EMAIL = "mgarin@alee.com";

    private ComponentTransition appearanceTransition;

    private SlidingSearch slidingSearch;

    private ComponentTransition containerTransition;
    private WebTabbedPane exampleTabs;
    private SourceViewer sourceViewer;

    private WebBreadcrumb locationBreadcrumb;
    private WebBreadcrumbToggleButton aboutButton;
    private WebBreadcrumbToggleButton demosButton;
    private WebBreadcrumbToggleButton sourcesButton;
    private WebMemoryBar memoryBar;

    private WebButton featureStateLegend;

    private static WebLookAndFeelDemo instance = null;

    public static WebLookAndFeelDemo getInstance ()
    {
        if ( instance == null )
        {
            // Creating demo instance
            instance = new WebLookAndFeelDemo ();
        }
        return instance;
    }

    private WebLookAndFeelDemo ()
    {
        super ();

        // Base content pane
        final WebPanel contentPane = new WebPanel ();

        // Exampler loading dialog
        WebProgressDialog progress = createProgressDialog ();
        progress.addWindowListener ( new WindowAdapter ()
        {
            public void windowClosed ( WindowEvent e )
            {
                // Stop loading demo on dialog close
                System.exit ( 0 );
            }
        } );
        progress.setVisible ( true );

        // Loading default demo dialog settings
        progress.setText ( "Configuring demo..." );
        setTitle ( getDemoTitle () );
        setIconImages ( WebLookAndFeel.getImages () );
        setLayout ( new BorderLayout () );
        HotkeyManager.installShowAllHotkeysAction ( getRootPane (), Hotkey.F1 );

        // Creating main content
        exampleTabs = ExamplesManager.createExampleTabs ( WebLookAndFeelDemo.this, progress );

        // Jar class structure creation
        sourceViewer = new SourceViewer ( ExamplesManager.createJarStructure ( progress ) );

        // Content
        containerTransition = new ComponentTransition ( exampleTabs );
        containerTransition.setTransitionEffect ( new FadeTransitionEffect () );
        containerTransition.addTransitionListener ( new TransitionAdapter ()
        {
            public void transitionFinished ()
            {
                // To show back tooltip once
                if ( !isSourceTipShownOnce () && containerTransition.getContent () == sourceViewer )
                {
                    // Marking the fact we already seen this tip
                    setSourceTipShownOnce ();

                    // Showing helpful tip
                    TooltipManager.showOneTimeTooltip ( locationBreadcrumb.getComponent ( 0 ), null, infoIcon,
                            "You can go back to demos at anytime " + "using this breadcrumb", TooltipWay.up );
                }
            }
        } );

        contentPane.add ( containerTransition, BorderLayout.CENTER );

        // Status bar
        contentPane.add ( createStatusBar (), BorderLayout.SOUTH );
        exampleTabs.setSelectedIndex ( 0 );

        // Base content
        appearanceTransition = new ComponentTransition ( createBackgroundPanel () )
        {
            public Dimension getPreferredSize ()
            {
                return contentPane.getPreferredSize ();
            }
        };
        CurtainTransitionEffect effect = new CurtainTransitionEffect ();
        effect.setDirection ( Direction.down );
        effect.setType ( CurtainType.fade );
        appearanceTransition.setTransitionEffect ( effect );
        appearanceTransition.addAncestorListener ( new AncestorAdapter ()
        {
            public void ancestorAdded ( AncestorEvent event )
            {
                appearanceTransition.delayTransition ( 1000, contentPane );
            }
        } );
        appearanceTransition.addTransitionListener ( new TransitionAdapter ()
        {
            public void transitionFinished ()
            {
                // Search tip
                if ( !isSearchTipShownOnce () )
                {
                    setSearchTipShownOnce ();

                    JRootPane rootPane = WebLookAndFeelDemo.this.getRootPane ();
                    final WebCustomTooltip searchTip = TooltipManager
                            .showOneTimeTooltip ( rootPane, new Point ( rootPane.getWidth () / 2, 0 ), SlidingSearch.searchIcon,
                                    "You can quickly navigate through components using search (Ctrl+F)", TooltipWay.down );

                    final HotkeyInfo searchTipHide = HotkeyManager.registerHotkey ( Hotkey.CTRL_F, new HotkeyRunnable ()
                    {
                        public void run ( KeyEvent e )
                        {
                            searchTip.closeTooltip ();
                        }
                    } );
                    searchTip.addTooltipListener ( new TooltipAdapter ()
                    {
                        public void tooltipDestroyed ()
                        {
                            HotkeyManager.unregisterHotkey ( searchTipHide );
                        }
                    } );
                }
            }
        } );
        add ( appearanceTransition, BorderLayout.CENTER );

        // Search
        installSearch ();

        // Finishing load text
        progress.setText ( "Starting demo..." );

        // Creating a small delay to not blink with windows too fast
        ThreadUtils.sleepSafely ( 500 );

        // Configuring demo window
        pack ();
        setLocationRelativeTo ( null );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );

        // Displaying demo
        progress.setVisible ( false );
    }

    private JComponent createBackgroundPanel ()
    {
        WebImage wi = new WebImage ( WebLookAndFeelDemo.class, "icons/text.png" )
        {
            protected void paintComponent ( Graphics g )
            {
                Graphics2D g2d = ( Graphics2D ) g;
                g2d.setPaint ( new LinearGradientPaint ( 0, 0, 0, getHeight (), new float[]{ 0f, 0.4f, 0.6f, 1f },
                        new Color[]{ StyleConstants.bottomBgColor, Color.WHITE, Color.WHITE, StyleConstants.bottomBgColor } ) );
                g2d.fill ( g2d.getClip () != null ? g2d.getClip () : getVisibleRect () );

                super.paintComponent ( g );
            }
        };
        wi.setDisplayType ( DisplayType.preferred );
        wi.setHorizontalAlignment ( SwingConstants.CENTER );
        wi.setVerticalAlignment ( SwingConstants.CENTER );
        return wi;
    }

    public void displayDemo ()
    {
        SwingUtilities.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                setVisible ( true );
            }
        } );
    }

    private ImageIcon getUpdateIcon ()
    {
        String os = SystemUtils.getShortOsName ();
        return new ImageIcon ( WebLookAndFeelDemo.class.getResource ( "icons/update/" + ( os != null ? os : "other" ) + ".png" ) );
    }

    private String getDemoTitle ()
    {
        return VersionManager.getLibraryVersion ().toString () + " - showcase";
    }

    public static boolean isSearchTipShownOnce ()
    {
        return SettingsManager.get ( "searchTipShown", false );
    }

    public static void setSearchTipShownOnce ()
    {
        SettingsManager.set ( "searchTipShown", true );
    }

    public static boolean isSourceTipShownOnce ()
    {
        return SettingsManager.get ( "sourceTipShown", false );
    }

    public static void setSourceTipShownOnce ()
    {
        SettingsManager.set ( "sourceTipShown", true );
    }

    private WebProgressDialog createProgressDialog ()
    {
        // Progress dialog
        WebProgressDialog progress = new WebProgressDialog ( null, "Loading showcase..." );
        progress.setIconImages ( WebLookAndFeel.getImages () );
        progress.setShowProgressBar ( false );

        IconProgress loadedIcons = new IconProgress ();

        List<ExampleGroup> eg = ExamplesManager.getExampleGroups ();
        Insets m = loadedIcons.getInsets ();
        int w = m.left + eg.size () * 16 + ( eg.size () - 1 ) * 2 + m.right;
        int h = m.top + 16 + m.bottom;
        loadedIcons.setPreferredSize ( new Dimension ( w, h ) );

        progress.setMiddleComponent ( loadedIcons );

        return progress;
    }

    private WebStatusBar createStatusBar ()
    {
        // Window status bar
        final WebStatusBar statusBar = new WebStatusBar ();

        // Location breadcrumb
        statusBar.add ( getLocationBreadcrumb () );

        // Group description
        final ExampleGroup sg = getSelectedGroup ();

        statusBar.addSpacing ();

        FeatureState fgs = sg.getFeatureGroupState ();
        final WebLabel featureState = new WebLabel ();
        TooltipManager.setTooltip ( featureState, fgs.getIcon (), fgs.getDescription () );
        featureState.setIcon ( fgs.getIcon () );
        featureState.addMouseListener ( new MouseAdapter ()
        {
            public void mousePressed ( MouseEvent e )
            {
                showLegend ( featureState, getSelectedGroup ().getFeatureGroupState () );
            }
        } );
        statusBar.add ( featureState );

        statusBar.addSpacing ();

        final WebLabel groupDescription = new WebLabel ();
        groupDescription.setText ( sg.getGroupDescription () );
        statusBar.add ( groupDescription );

        exampleTabs.addChangeListener ( new ChangeListener ()
        {
            public void stateChanged ( ChangeEvent e )
            {
                ExampleGroup sg = getSelectedGroup ();
                FeatureState fgs = sg.getFeatureGroupState ();
                TooltipManager.removeTooltips ( featureState );
                TooltipManager.setTooltip ( featureState, fgs.getIcon (), fgs.getDescription () );
                featureState.setIcon ( fgs.getIcon () );
                groupDescription.setText ( sg.getGroupDescription () );
            }
        } );

        // Update button
        final WebButton update = WebButton.createIconWebButton ( getUpdateIcon () );
        update.setVisible ( false );
        update.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                WebUtils.browseSiteSafely ( WEBLAF_SITE + "download/" );
            }
        } );
        statusBar.addToMiddle ( update );

        // Version checker
        WebTimer.repeat ( "WebLookAndFeelDemo.versionCheck", 60000, 10000, new ActionListener ()
        {
            private VersionInfo lastVersion = null;

            public void actionPerformed ( ActionEvent e )
            {
                VersionInfo lv = getLastVersion ();
                if ( lv != null && lv.compareTo ( VersionManager.getLibraryVersion () ) > 0 )
                {
                    // Displaying update icon
                    update.setVisible ( true );

                    // Updating tips
                    ImageIcon updateIcon = getUpdateIcon ();

                    final WebCustomTooltip versionTip = TooltipManager
                            .showOneTimeTooltip ( update, null, updateIcon, "New library version available: " + lv.toString () );
                    update.addMouseListener ( new MouseAdapter ()
                    {
                        public void mouseEntered ( MouseEvent e )
                        {
                            versionTip.closeTooltip ();
                            update.removeMouseListener ( this );
                        }
                    } );

                    TooltipManager.setTooltip ( update, updateIcon, "Download new version: " + lv.toString () );

                    // Finishing updater thread
                    ( ( WebTimer ) e.getSource () ).stop ();
                }
            }

            public VersionInfo getLastVersion ()
            {
                if ( lastVersion == null )
                {
                    try
                    {
                        final String versionUrl = WebLookAndFeelDemo.WEBLAF_SITE + "downloads/version.xml";
                        lastVersion = XmlUtils.fromXML ( new URL ( versionUrl ) );
                    }
                    catch ( Throwable e )
                    {
                        //
                    }
                }
                return lastVersion;
            }
        } );

        // todo Language chooser button
        //        final WebButton language = new WebButton ( LanguageManager.getLanguage (),
        //                LanguageManager.getLanguageIcon ( LanguageManager.getLanguage () ) );
        //        language.setRound ( StyleConstants.smallRound );
        //        language.setLeftRightSpacing ( 2 );
        //        TooltipManager.setTooltip ( language, language.getIcon (), "Application language" );
        //        language.addActionListener ( new ActionListener ()
        //        {
        //            public void actionPerformed ( ActionEvent e )
        //            {
        //                WebButtonPopup wbp = new WebButtonPopup ( language, PopupWay.upCenter );
        //
        //                WebButton otherLanguage = new WebButton ( LanguageManager.RUSSIAN,
        //                        LanguageManager.getLanguageIcon ( LanguageManager.RUSSIAN ) );
        //                otherLanguage.setHorizontalAlignment ( WebButton.LEFT );
        //                otherLanguage.setUndecorated ( true );
        //                otherLanguage.setMargin ( 3, 3, 0, 3 );
        //                otherLanguage.setLeftRightSpacing ( 2 );
        //                wbp.setContent ( otherLanguage );
        //
        //                PopupManager.showPopup ( language, wbp );
        //            }
        //        } );
        //        statusBar.addToEnd ( language );

        // Enabled state toggle
        final WebToggleButton enabled = WebToggleButton.createIconWebButton ( enabledIcon );
        enabled.addHotkey ( WebLookAndFeelDemo.this, Hotkey.ALT_E );
        enabled.setSelected ( true );
        final Runnable enabledUpdater = new Runnable ()
        {
            public void run ()
            {
                boolean e = enabled.isSelected ();
                enabled.setIcon ( e ? enabledIcon : disabledIcon );

                TooltipManager.removeTooltips ( enabled );
                TooltipManager.setTooltip ( enabled, e ? enabledIcon : disabledIcon,
                        e ? "All examples are enabled" : "All examples are disabled" );
            }
        };
        enabledUpdater.run ();
        enabled.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                // Updating button
                enabledUpdater.run ();

                // Updating examples
                boolean enable = enabled.isSelected ();
                for ( int i = 0; i < exampleTabs.getTabCount (); i++ )
                {
                    Component tabContent = exampleTabs.getComponentAt ( i );

                    // Workaround to keep focus in window
                    if ( SwingUtils.hasFocusOwner ( tabContent ) )
                    {
                        memoryBar.requestFocusInWindow ();
                    }

                    // Enabling/disabling examples
                    SwingUtils.setEnabledRecursively ( tabContent, enable );
                }
            }
        } );

        // Animation toggle
        final WebToggleButton animate = WebToggleButton.createIconWebButton ( animationIcon );
        TooltipManager.setTooltip ( animate, animationIcon, "Allow heavy animation" );
        animate.addHotkey ( WebLookAndFeelDemo.this, Hotkey.ALT_A );
        animate.setSelected ( true );
        animate.addActionListener ( new ActionListener ()
        {
            private List<TransitionEffect> oldEffects;

            public void actionPerformed ( ActionEvent e )
            {
                if ( animate.isSelected () )
                {
                    containerTransition.setTransitionEffects ( oldEffects );
                }
                else
                {
                    oldEffects = containerTransition.getTransitionEffects ();
                    containerTransition.clearTransitionEffects ();
                }
            }
        } );

        // Tab names toggle
        final WebToggleButton displayTabTitles = WebToggleButton.createIconWebButton ( tabNamesIcon );
        TooltipManager.setTooltip ( displayTabTitles, tabNamesIcon, "Display tab titles" );
        displayTabTitles.addHotkey ( WebLookAndFeelDemo.this, Hotkey.ALT_T );
        displayTabTitles.setSelected ( true );
        displayTabTitles.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                if ( displayTabTitles.isSelected () )
                {
                    List<ExampleGroup> groups = ExamplesManager.getExampleGroups ();
                    for ( int i = 0; i < exampleTabs.getTabCount (); i++ )
                    {
                        exampleTabs.setTitleAt ( i, groups.get ( i ).getGroupName () );
                    }
                }
                else
                {
                    for ( int i = 0; i < exampleTabs.getTabCount (); i++ )
                    {
                        exampleTabs.setTitleAt ( i, "" );
                    }
                }
            }
        } );

        // Component orientation toggle
        final WebToggleButton ltrOrientation = WebToggleButton.createIconWebButton ( orientationIcon );
        TooltipManager.setTooltip ( ltrOrientation, orientationIcon, "Use LTR components orientation" );
        ltrOrientation.setSelected ( LanguageManager.isLeftToRight () );
        ltrOrientation.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                LanguageManager.changeOrientation ();
            }
        } );

        // Special application-wide (global) hotkey
        HotkeyManager.registerHotkey ( Hotkey.ALT_R, new HotkeyRunnable ()
        {
            public void run ( KeyEvent e )
            {
                ltrOrientation.doClick ();
            }
        } );

        statusBar.addToEnd ( enabled );
        statusBar.addToEnd ( animate );
        statusBar.addToEnd ( displayTabTitles );
        statusBar.addToEnd ( ltrOrientation );

        statusBar.addSeparatorToEnd ();

        // Memory bar
        memoryBar = new WebMemoryBar ();
        memoryBar.setShowMaximumMemory ( false );
        memoryBar.setPreferredWidth ( memoryBar.getPreferredSize ().width + 20 );
        statusBar.addToEnd ( memoryBar );

        return statusBar;
    }

    private WebCustomTooltip lastTip = null;
    private Component lastComponent = null;
    private WebPanel legendPanel = null;

    public void showFullLegend ()
    {
        if ( lastTip != null && lastTip.isShowing () )
        {
            lastTip.closeTooltip ();
            if ( lastComponent == featureStateLegend )
            {
                return;
            }
        }
        if ( legendPanel == null )
        {
            legendPanel = new WebPanel ( new VerticalFlowLayout () );
            legendPanel.setOpaque ( false );

            legendPanel.add ( new WebLabel ( "<html><center>Every feature is marked with a colored leaf.<br>" +
                    "Each leaf color reflects feature development state.</center></html>", WebLabel.CENTER ) );
            legendPanel.add ( createLegendSeparator () );

            FeatureState[] values = FeatureState.values ();
            for ( FeatureState fs : values )
            {
                legendPanel.add ( SwingUtils.setBoldFont ( new WebLabel ( fs.getDescription (), fs.getIcon (), WebLabel.CENTER ) ) );
                legendPanel.add ( new WebLabel ( fs.getFullDescription (), WebLabel.CENTER ) );

                if ( !fs.equals ( values[ values.length - 1 ] ) )
                {
                    legendPanel.add ( createLegendSeparator () );
                }
            }
        }
        lastTip = TooltipManager.showOneTimeTooltip ( featureStateLegend, null, legendPanel, TooltipWay.up );
        lastComponent = featureStateLegend;
    }

    private JComponent createLegendSeparator ()
    {
        WebSeparator s = new WebSeparator ( WebSeparator.HORIZONTAL, true );
        s.setDrawSideLines ( false );
        return SwingUtils.setBorder ( s, 4, 0, 4, 0 );
    }

    private Map<FeatureState, WebPanel> legendCache = new HashMap<FeatureState, WebPanel> ();

    public void showLegend ( JComponent component, FeatureState featureState )
    {
        if ( lastTip != null && lastTip.isShowing () )
        {
            if ( lastComponent == component )
            {
                return;
            }
        }
        WebPanel legendPanel;
        if ( legendCache.containsKey ( featureState ) )
        {
            legendPanel = legendCache.get ( featureState );
        }
        else
        {
            legendPanel = new WebPanel ( new VerticalFlowLayout () );
            legendPanel.setOpaque ( false );
            legendPanel.add ( SwingUtils
                    .setBoldFont ( new WebLabel ( featureState.getDescription (), featureState.getIcon (), WebLabel.CENTER ) ) );
            legendPanel.add ( new WebLabel ( featureState.getFullDescription (), WebLabel.CENTER ) );
            legendCache.put ( featureState, legendPanel );
        }
        lastTip = TooltipManager.showOneTimeTooltip ( component, null, legendPanel, TooltipWay.up );
        lastComponent = component;
    }

    private WebBreadcrumb getLocationBreadcrumb ()
    {
        locationBreadcrumb = new WebBreadcrumb ( true );
        ButtonGroup locationGroup = new ButtonGroup ();

        demosButton = new WebBreadcrumbToggleButton ();
        demosButton.setText ( "Demos" );
        demosButton.setSelected ( true );
        demosButton.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                containerTransition.performTransition ( exampleTabs );
            }
        } );
        locationBreadcrumb.add ( demosButton );
        locationGroup.add ( demosButton );

        sourcesButton = new WebBreadcrumbToggleButton ();
        sourcesButton.setIcon ( JarEntry.javaIcon );
        sourcesButton.setText ( "Source code" );
        sourcesButton.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                sourceViewer.updateClassPath ( getSelectedGroup ().getClass () );
                containerTransition.performTransition ( sourceViewer );
            }
        } );
        locationBreadcrumb.add ( sourcesButton );
        locationGroup.add ( sourcesButton );

        updateCurrentDemo ();
        exampleTabs.addChangeListener ( new ChangeListener ()
        {
            public void stateChanged ( ChangeEvent e )
            {
                updateCurrentDemo ();
            }
        } );

        return locationBreadcrumb;
    }

    private void updateCurrentDemo ()
    {
        ExampleGroup group = getSelectedGroup ();

        // Updating demos button
        demosButton.setIcon ( group.getGroupIcon () );
        TooltipManager.removeTooltips ( demosButton );
        TooltipManager.setTooltip ( demosButton, group.getGroupIcon (), group.getGroupName () );

        // Updating sources button
        TooltipManager.removeTooltips ( sourcesButton );
        TooltipManager.setTooltip ( sourcesButton, JarEntry.javaIcon, ReflectUtils.getJavaClassName ( group ) );
    }

    public SourceViewer getSourceViewer ()
    {
        return sourceViewer;
    }

    public void addViewListener ( ViewListener listener )
    {
        this.sourceViewer.addViewListener ( listener );
    }

    public void removeViewListener ( ViewListener listener )
    {
        this.sourceViewer.removeViewListener ( listener );
    }

    public void showSource ( Class showFor )
    {
        slidingSearch.hideSearch ();
        sourcesButton.setSelected ( true );
        sourceViewer.updateClassPath ( showFor );
        containerTransition.performTransition ( sourceViewer );
    }

    public void closeSource ( Class closeFor )
    {
        sourceViewer.closeEntryView ( sourceViewer.getJarStructure ().getClassEntry ( closeFor ) );
    }

    private ExampleGroup getSelectedGroup ()
    {
        int index = exampleTabs.getSelectedIndex ();
        return ExamplesManager.getExampleGroups ().get ( index );
    }

    private void installSearch ()
    {
        // Configuring highlight base for main window
        GlassPaneManager.getGlassPane ( WebLookAndFeelDemo.this ).setHighlightBase ( exampleTabs );

        // Installing sliding out search component for demo window layered pane
        slidingSearch = new SlidingSearch ( getLayeredPane () )
        {
            protected boolean isSearchEnabled ()
            {
                return exampleTabs.isShowing ();
            }
        };

        // Search action
        slidingSearch.getSearchField ().addCaretListener ( new CaretListener ()
        {
            public void caretUpdate ( CaretEvent e )
            {
                List<Component> found =
                        HighlightManager.highlightComponentsWithText ( slidingSearch.getSearchField ().getText (), getContentPane () );
                HighlightManager.removeHigligtedComponent ( exampleTabs );
                if ( found.size () > 0 )
                {
                    boolean anyShown = false;
                    for ( Component c : found )
                    {
                        if ( c.isVisible () && c.isShowing () )
                        {
                            anyShown = true;
                            break;
                        }
                    }
                    if ( !anyShown )
                    {
                        Component toShow = found.get ( 0 );
                        for ( int i = 0; i < exampleTabs.getTabCount (); i++ )
                        {
                            Component component = exampleTabs.getComponentAt ( i );
                            if ( component instanceof Container && ( ( Container ) component ).isAncestorOf ( toShow ) )
                            {
                                exampleTabs.setSelectedIndex ( i );
                                break;
                            }
                        }
                    }
                }
            }
        } );
    }

    public static void runDemo ()
    {
        // Exampler settings location
        SettingsManager.setDefaultSettingsDirName ( ".weblaf-demo" );
        SettingsManager.setDefaultSettingsGroup ( "WebLookAndFeelDemo" );
        SettingsManager.setSaveOnChange ( true );

        // Default demo language for now
        LanguageManager.DEFAULT = LanguageManager.ENGLISH;

        // Look and Feel
        WebLookAndFeel.install ();

        // Displaying main frame
        WebLookAndFeelDemo.getInstance ().displayDemo ();
    }

    public static void main ( String[] args )
    {
        // To enable accelerated rendering pipelines:
        // -Dsun.java2d.d3d=true
        // -Dsun.java2d.opengl=true

        // Custom fonts
        // WebLookAndFeel.globalControlFont = new Font ( "Comic Sans MS", Font.PLAIN, 13 );
        // WebLookAndFeel.globalAcceleratorFont = new Font ( "Comic Sans MS", Font.PLAIN, 12 );
        // WebLookAndFeel.globalTitleFont = new Font ( "Comic Sans MS", Font.BOLD, 13 );
        // WebLookAndFeel.globalTextFont = new Font ( "Comic Sans MS", Font.PLAIN, 13 );

        // Custom styles
        // StyleConstants.smallRound = 0;
        // StyleConstants.mediumRound = 0;
        // StyleConstants.bigRound = 0;
        // StyleConstants.largeRound = 0;
        // StyleConstants.decorationRound = 0;

        // Google-styled tooltips
        // WebCustomTooltipStyle.round = 0;
        // WebCustomTooltipStyle.shadeWidth = 0;
        // WebCustomTooltipStyle.trasparency = 1f;

        // Running demo application
        runDemo ();
    }
}