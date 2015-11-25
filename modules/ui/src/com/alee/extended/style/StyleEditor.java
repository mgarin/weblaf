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

package com.alee.extended.style;

import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbLabel;
import com.alee.extended.breadcrumb.WebBreadcrumbPanel;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.label.StyleRange;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.magnifier.MagnifierGlass;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.syntax.SyntaxPreset;
import com.alee.extended.syntax.WebSyntaxArea;
import com.alee.extended.syntax.WebSyntaxScrollPane;
import com.alee.extended.tree.WebFileTree;
import com.alee.extended.window.PopOverLocation;
import com.alee.extended.window.WebPopOver;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.grouping.GroupPane;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.menu.WebCheckBoxMenuItem;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollBar;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.text.WebTextField;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.laf.tree.TreeSelectionStyle;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.Styleable;
import com.alee.managers.style.StyleableComponent;
import com.alee.managers.style.data.ComponentStyleConverter;
import com.alee.managers.style.data.SkinInfo;
import com.alee.managers.style.data.SkinInfoConverter;
import com.alee.managers.style.skin.CustomSkin;
import com.alee.utils.*;
import com.alee.utils.swing.*;
import com.alee.utils.text.LoremIpsum;
import com.alee.utils.xml.ResourceFile;
import com.alee.utils.xml.ResourceLocation;
import com.thoughtworks.xstream.converters.ConversionException;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * WebLaF style editor application.
 * It allows you to edit and preview WebLaF skins in runtime.
 *
 * @author Mikle Garin
 */

public class StyleEditor extends WebFrame
{
    /**
     * todo 1. Translate editor
     * todo 2. Add JavaDoc
     */

    protected static final ImageIcon magnifierIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/magnifier.png" ) );
    protected static final ImageIcon boundsIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/bounds.png" ) );
    protected static final ImageIcon disabledIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/disabled.png" ) );
    protected static final ImageIcon orientationIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/orientation.png" ) );
    protected static final ImageIcon brushIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/brush.png" ) );
    protected static final ImageIcon locateIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/locate.png" ) );

    protected static final ImageIcon info = new ImageIcon ( StyleEditor.class.getResource ( "icons/status/info.png" ) );
    protected static final ImageIcon ok = new ImageIcon ( StyleEditor.class.getResource ( "icons/status/ok.png" ) );
    protected static final ImageIcon error = new ImageIcon ( StyleEditor.class.getResource ( "icons/status/error.png" ) );

    protected static final ImageIcon tabIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/tab.png" ) );

    protected static final BufferedImage magnifier =
            ImageUtils.getBufferedImage ( new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/magnifierImage.png" ) ) );

    protected static final ImageIcon completeStackTraceIcon =
            new ImageIcon ( StyleEditor.class.getResource ( "icons/status/completeStackTrace.png" ) );

    protected static final String COMPONENT_TYPE_KEY = "component.type.key";
    protected static final String STYLE_ID_KEY = "style.id.key";

    protected WebToolBar toolBar;
    protected WebPanel container;
    protected WebSplitPane split;
    protected WebPanel componentViewer;
    protected WebScrollPane previewScroll;
    protected WebTabbedPane editorTabs;
    protected WebPanel editorsContainer;

    protected WebStatusBar statusBar;
    protected WebBreadcrumbLabel statusMessage;

    protected final List<JComponent> previewComponents = new ArrayList<JComponent> ();
    protected final List<WebPanel> boundsPanels = new ArrayList<WebPanel> ();

    private final MagnifierGlass magnifierGlass = new MagnifierGlass ();
    protected int updateDelay = 500;
    protected ComponentOrientation orientation = WebLookAndFeel.getOrientation ();
    protected boolean enabled = true;
    protected boolean locate = true;
    protected boolean brush = false;
    protected boolean completeStackTrace = false;

    protected final ResourceFile baseSkinFile;
    protected List<WebSyntaxArea> editors;

    /**
     * Constructs new style editor and loads specified skin for editing.
     *
     * @param skin skin resource file
     */
    public StyleEditor ( final ResourceFile skin )
    {
        super ( "WebLaF skin editor" );
        setIconImages ( WebLookAndFeel.getImages () );

        // todo Make changeable through constructor
        baseSkinFile = skin;

        initializeContainer ();
        initializeToolBar ();
        initializeStatusBar ();
        initializeViewer ();
        initializeEditors ();

        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
        setSize ( 1200, 800 );
        setLocationRelativeTo ( null );
    }

    protected void initializeToolBar ()
    {
        toolBar = new WebToolBar ( StyleId.of ( "preview-toolbar" ) );

        final StyleId toggleId = StyleId.of ( "preview-tool-toggle-button" );
        final StyleId textId = StyleId.of ( "preview-tool-text-button" );

        final WebToggleButton magnifierButton = new WebToggleButton ( toggleId, magnifierIcon );
        magnifierButton.setToolTip ( magnifierIcon, "Show/hide magnifier tool" );
        magnifierButton.addHotkey ( Hotkey.ALT_Q );
        magnifierButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                magnifierGlass.displayOrDispose ( StyleEditor.this );
                magnifierGlass.setZoomFactor ( 4 );
            }
        } );

        final WebButton zoomFactorButton = new WebButton ( textId, "4x" );
        zoomFactorButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebPopupMenu menu = new WebPopupMenu ();
                for ( int i = 2; i <= 6; i++ )
                {
                    final int factor = i;
                    final JMenuItem menuItem = new WebMenuItem ( i + "x zoom" );
                    menuItem.addActionListener ( new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            magnifierGlass.setZoomFactor ( factor );
                            zoomFactorButton.setText ( factor + "x" );
                        }
                    } );
                    menu.add ( menuItem );
                }
                menu.showBelowMiddle ( zoomFactorButton );
            }
        } );

        toolBar.add ( new GroupPane ( magnifierButton, zoomFactorButton ) );

        final WebToggleButton boundsButton = new WebToggleButton ( toggleId, boundsIcon );
        boundsButton.setToolTip ( boundsIcon, "Show/hide component bounds" );
        boundsButton.addHotkey ( Hotkey.ALT_W );
        boundsButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                for ( final WebPanel boundsPanel : boundsPanels )
                {
                    boundsPanel.setStyleId ( StyleId.of ( boundsButton.isSelected () ? "dashed-border" : "empty-border" ) );
                }
            }
        } );
        toolBar.add ( boundsButton );

        final WebToggleButton disabledButton = new WebToggleButton ( toggleId, disabledIcon );
        disabledButton.setToolTip ( disabledIcon, "Disable/enable components" );
        disabledButton.addHotkey ( Hotkey.ALT_D );
        disabledButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                enabled = !enabled;
                applyViewEnabledState ();
            }
        } );
        toolBar.add ( disabledButton );

        final WebToggleButton orientationButton = new WebToggleButton ( toggleId, orientationIcon, !orientation.isLeftToRight () );
        orientationButton.setToolTip ( orientationIcon, "Change components orientation" );
        orientationButton.addHotkey ( Hotkey.ALT_R );
        orientationButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                orientation = orientation.isLeftToRight () ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT;
                applyViewOrientation ();
            }
        } );
        toolBar.add ( orientationButton );

        toolBar.addSeparator ();

        final WebToggleButton brushButton = new WebToggleButton ( toggleId, brushIcon, brush );
        brushButton.setToolTip ( brushIcon, "Apply component style ID" );
        brushButton.addHotkey ( Hotkey.ALT_S );
        brushButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                brush = true;
                Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
                {
                    @Override
                    public void eventDispatched ( final AWTEvent event )
                    {
                        final MouseEvent e = ( MouseEvent ) event;
                        if ( e.getButton () == MouseEvent.BUTTON1 && e.getID () == MouseEvent.MOUSE_PRESSED )
                        {
                            brush = false;
                            Toolkit.getDefaultToolkit ().removeAWTEventListener ( this );
                            brushButton.setSelected ( false );

                            final Component c = e.getComponent ();
                            final Point click = e.getLocationOnScreen ();
                            final Point cloc = c.getLocationOnScreen ();
                            final Component actual = SwingUtils.getTopComponentAt ( c, click.x - cloc.x, click.y - cloc.y );

                            // todo GLASSPANE USAGE
                            System.out.println ( actual );
                        }
                    }
                }, AWTEvent.MOUSE_EVENT_MASK );
            }
        } );
        toolBar.add ( brushButton );

        final WebToggleButton locateViewButton = new WebToggleButton ( toggleId, locateIcon, locate );
        locateViewButton.setToolTip ( locateIcon, "Locate component view when navigating XML" );
        locateViewButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                locate = locateViewButton.isSelected ();
                locateView ();
            }
        } );
        toolBar.addToEnd ( locateViewButton );
    }

    protected void initializeContainer ()
    {
        container = new WebPanel ();
        getContentPane ().add ( container, BorderLayout.CENTER );

        split = new WebSplitPane ( WebSplitPane.HORIZONTAL_SPLIT, true );
        split.setDividerLocation ( 350 );
        split.setDividerSize ( 8 );
        split.setDrawDividerBorder ( true );
        split.setOneTouchExpandable ( true );
        container.add ( split, BorderLayout.CENTER );
    }

    protected void initializeStatusBar ()
    {
        statusBar = new WebStatusBar ();

        final WebBreadcrumb updateBreadcrumb = new WebBreadcrumb ( StyleId.of ( "status-breadcrumb" ) );
        updateBreadcrumb.setEncloseLastElement ( false );

        final ImageIcon updateIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/update.png" ) );
        final WebLabel delayLabel = new WebLabel ( "Skin update delay:", updateIcon );
        final WebTextField delayField = new WebTextField ( StyleId.of ( "delay-field" ), new IntTextDocument (), "" + updateDelay, 3 );
        delayField.setHorizontalAlignment ( WebTextField.CENTER );
        delayField.getDocument ().addDocumentListener ( new IntDocumentChangeListener ()
        {
            @Override
            public void documentChanged ( final Integer newValue, final DocumentEvent e )
            {
                updateDelay = newValue != null ? newValue : updateDelay;
                if ( updateDelay < 0 )
                {
                    updateDelay = 0;
                }
            }
        } );
        final WebLabel msLabel = new WebLabel ( "ms" );
        final WebBreadcrumbPanel panel = new WebBreadcrumbPanel ();
        panel.setLayout ( new HorizontalFlowLayout ( 4, false ) );
        panel.add ( delayLabel, new CenterPanel ( delayField, false, true ), msLabel );
        updateBreadcrumb.add ( panel );

        final StyleId statusMessageId = StyleId.of ( "status-message" );
        statusMessage = new WebBreadcrumbLabel ( statusMessageId, "Edit XML at the right side and see UI changes at the left side!", info );
        updateBreadcrumb.add ( statusMessage );

        statusBar.add ( updateBreadcrumb );

        final StyleId statusToggleId = StyleId.of ( "statusbar-toggle-button" );
        final WebToggleButton completeStackTraceButton = new WebToggleButton ( statusToggleId, completeStackTraceIcon, completeStackTrace );
        completeStackTraceButton.setToolTip ( completeStackTraceIcon, "Output complete style parsing stack trace" );
        completeStackTraceButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                completeStackTrace = completeStackTraceButton.isSelected ();
            }
        } );
        statusBar.addToEnd ( completeStackTraceButton );

        statusBar.addToEnd ( new WebMemoryBar ().setPreferredWidth ( 200 ) );

        container.add ( statusBar, BorderLayout.SOUTH );
    }

    protected void initializeViewer ()
    {
        final VerticalFlowLayout layout = new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, 15, true, false );
        componentViewer = new WebPanel ( StyleId.of ( "preview-pane" ), layout );
        componentViewer.setMargin ( 10 );

        previewScroll = new WebScrollPane ( StyleId.scrollpaneUndecorated, componentViewer );
        previewScroll.getVerticalScrollBar ().setUnitIncrement ( 15 );

        split.setLeftComponent ( new GroupPanel ( GroupingType.fillLast, 0, false, toolBar, previewScroll ) );

        //

        final WebLabel label = new WebLabel ( "Just a label", WebLookAndFeel.getIcon ( 16 ) );
        addViewComponent ( "Label", label, label, true );

        //

        final String styledText = "{Simple styled label:b;c(128,128,0);u}\n" + "with {hard:b} line breaks\n" + "across the text";
        final WebStyledLabel styledLabel = new WebStyledLabel ( styledText );
        styledLabel.addStyleRange ( new StyleRange ( 1, 3, Font.ITALIC ) );
        addViewComponent ( "Styled label", styledLabel, styledLabel, true );

        //

        final WebButton button = new WebButton ( "Simple button", WebLookAndFeel.getIcon ( 16 ) );
        addViewComponent ( "Button", button, button, true );

        //

        final WebButton iconButton = new WebButton ( WebLookAndFeel.getIcon ( 24 ) );
        addViewComponent ( "Icon button", iconButton, iconButton, true );

        //

        final WebToggleButton toggleButton = new WebToggleButton ( "Toggle me", WebLookAndFeel.getIcon ( 16 ) );
        addViewComponent ( "Toggle button", toggleButton, toggleButton, true );

        //

        final WebToggleButton iconToggleButton = new WebToggleButton ( WebLookAndFeel.getIcon ( 24 ) );
        addViewComponent ( "Icon toggle button", iconToggleButton, iconToggleButton, true );

        //

        final WebCheckBox checkBox = new WebCheckBox ( "Check me" );
        addViewComponent ( "Checkbox", checkBox, checkBox, true );

        //

        final WebTristateCheckBox tristateCheckBox = new WebTristateCheckBox ( "Check me more" );
        addViewComponent ( "Tristate checkbox", tristateCheckBox, tristateCheckBox, true );

        //

        final WebRadioButton radioButton1 = new WebRadioButton ( "Radio button 1" );
        final WebRadioButton radioButton2 = new WebRadioButton ( "Radio button 2" );
        final WebRadioButton radioButton3 = new WebRadioButton ( "Radio button 3" );
        SwingUtils.groupButtons ( radioButton1, radioButton2, radioButton3 );
        final GroupPanel radioGroup = new GroupPanel ( false, radioButton1, radioButton2, radioButton3 );
        addViewComponent ( "Radio button", radioGroup, radioGroup, true );

        //

        final WebScrollBar hsb = new WebScrollBar ( WebScrollBar.HORIZONTAL, 45, 10, 0, 100 );
        addViewComponent ( "Horizontal scroll bar", hsb, hsb, false );

        //

        final WebScrollBar vsb = new WebScrollBar ( WebScrollBar.VERTICAL, 45, 10, 0, 100 ).setPreferredHeight ( 100 );
        addViewComponent ( "Vertical scroll bar", vsb, vsb, true );

        //

        final WebTextArea textArea = new WebTextArea ();
        textArea.setRows ( 5 );

        final LoremIpsum loremIpsum = new LoremIpsum ();
        textArea.setText ( loremIpsum.getParagraphs ( 5 ) );

        final WebScrollPane sp = new WebScrollPane ( textArea );
        sp.setPreferredWidth ( 0 );
        addViewComponent ( "Scroll pane", sp, sp, false );

        //

        final String[] d = new String[]{ "Mikle Garin", "Joe Phillips", "Lilly Stewart", "Alex Jackson", "Joshua Martin", "Mark Einsberg",
                "Alice Manson", "Nancy Drew", "John Linderman", "Trisha Mathew", "Annae Mendy", "Wendy Anderson", "Alex Kurovski" };
        final WebComboBox cb = new WebComboBox ( d );
        addViewComponent ( "Combo box", cb, cb, true );

        //

        final WebList wl = new WebList ( d );
        final WebScrollPane wlScroll = new WebScrollPane ( wl );
        wlScroll.setPreferredSize ( new Dimension ( 200, 150 ) );
        addViewComponent ( "List", wlScroll, wl, false );

        //

        final WebFileTree homeFileTree = new WebFileTree ( FileUtils.getUserHomePath () );
        homeFileTree.setAutoExpandSelectedNode ( false );
        homeFileTree.setShowsRootHandles ( true );
        homeFileTree.setSelectionStyle ( TreeSelectionStyle.group );
        final WebScrollPane homeFileTreeScroll = new WebScrollPane ( homeFileTree );
        homeFileTreeScroll.setPreferredSize ( new Dimension ( 200, 150 ) );
        addViewComponent ( "Tree", homeFileTreeScroll, homeFileTree, false );

        //

        final WebPopupMenu popupMenu = new WebPopupMenu ();
        popupMenu.add ( new WebCheckBoxMenuItem ( "Check item", WebLookAndFeel.getIcon ( 16 ) ) );
        popupMenu.addSeparator ();
        popupMenu.add ( new WebMenuItem ( "Item 1", WebLookAndFeel.getIcon ( 16 ) ) );
        popupMenu.add ( new WebMenuItem ( "Item 2" ) );
        popupMenu.add ( new WebMenuItem ( "Item 3" ), Hotkey.ALT_F4 );
        popupMenu.addSeparator ();
        final WebMenu menu = new WebMenu ( "Sub-menu", WebLookAndFeel.getIcon ( 16 ) );
        menu.add ( new WebMenuItem ( "Item 1", WebLookAndFeel.getIcon ( 16 ) ) );
        menu.add ( new WebMenuItem ( "Item 2", Hotkey.CTRL_X ) );
        menu.add ( new WebMenuItem ( "Item 3" ) );
        popupMenu.add ( menu );

        final WebButton popupButton = new WebButton ( "Show popup menu", new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                popupMenu.showBelowMiddle ( ( WebButton ) e.getSource () );
            }
        } );
        addViewComponent ( "Popup menu", popupButton, popupMenu, true );

        //

        final WebTextField textField = new WebTextField ( "Some text..." );
        addViewComponent ( "Text field", textField, textField, false );

        //

        final WebPasswordField passwordField = new WebPasswordField ();
        passwordField.setInputPrompt ( "Enter the password..." );
        addViewComponent ( "Password field", passwordField, passwordField, false );

        //

        final String[] headers = { "Header 1", "Header 2", "Header 3", "Header 4", "Header 5", "Header 6" };
        final String[][] data =
                { { "1", "2", "3", "4", "5", "6" }, { "7", "8", "9", "10", "11", "12" }, { "13", "14", "15", "16", "17", "18" },
                        { "19", "20", "21", "22", "23", "24" }, { "25", "26", "27", "28", "29", "30" },
                        { "31", "32", "33", "34", "35", "36" }, { "37", "38", "39", "40", "41", "42" },
                        { "43", "44", "45", "46", "47", "48" }, { "49", "50", "51", "52", "53", "54" } };

        final WebTable table = new WebTable ( data, headers );
        table.setEditable ( true );
        table.setAutoResizeMode ( WebTable.AUTO_RESIZE_OFF );
        table.setRowSelectionAllowed ( true );
        table.setColumnSelectionAllowed ( true );
        table.setPreferredScrollableViewportSize ( new Dimension ( 200, 100 ) );
        final WebScrollPane tableScroll = new WebScrollPane ( table );
        addViewComponent ( "Table", tableScroll, table, false );

        //

        final WebFrame wf = new WebFrame ( StyleId.rootpaneDecorated, "Simple frame" );
        wf.setSize ( 400, 200 );

        final WebButton wfb = new WebButton ( "frame", new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                wf.center ();
                wf.setVisible ( true );
            }
        } );

        addViewComponent ( "Decorated frame", wfb, wf.getRootPane (), true );
    }

    protected void addViewComponent ( final String title, final JComponent displayedView, final JComponent view, final boolean center )
    {
        addViewComponent ( title, null, displayedView, view, center );
    }

    protected void addViewComponent ( final String title, final StyleId styleId, final JComponent displayedView, final JComponent view,
                                      final boolean center )
    {
        final StyleableComponent type = StyleableComponent.get ( view );

        final WebLabel titleLabel = new WebLabel ( StyleId.of ( "preview-title" ), title, type.getIcon (), WebLabel.LEADING );

        final WebPanel boundsPanel = new WebPanel ( StyleId.of ( "empty-border" ), displayedView );
        boundsPanels.add ( boundsPanel );

        final WebPanel viewPanel = new WebPanel ( StyleId.of ( "inner-shade" ), center ? new CenterPanel ( boundsPanel ) : boundsPanel );

        final WebPanel container = new WebPanel ( StyleId.panelTransparent, new BorderLayout ( 0, 0 ) );
        container.add ( titleLabel, BorderLayout.NORTH );
        container.add ( viewPanel, BorderLayout.CENTER );
        componentViewer.add ( container );

        container.putClientProperty ( COMPONENT_TYPE_KEY, type );
        if ( styleId != null )
        {
            container.putClientProperty ( STYLE_ID_KEY, styleId );
        }
        else if ( view instanceof Styleable )
        {
            container.putClientProperty ( STYLE_ID_KEY, ( ( Styleable ) view ).getStyleId () );
        }

        titleLabel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                viewPanel.setVisible ( !viewPanel.isVisible () );
                componentViewer.revalidate ();
                componentViewer.repaint ();
            }
        } );

        previewComponents.add ( view );
    }

    protected void initializeEditors ()
    {
        // Creating XML editors tabbed pane
        editorTabs = new WebTabbedPane ( StyleId.of ( "editor-tabs" ) );
        editorsContainer = new WebPanel ( editorTabs );

        // Loading editor code theme
        final Theme theme = loadXmlEditorTheme ();

        // Parsing all related files
        final List<String> xmlContent = new ArrayList<String> ();
        final List<String> xmlNames = new ArrayList<String> ();
        final List<ResourceFile> xmlFiles = new ArrayList<ResourceFile> ();
        loadSkinSources ( xmlContent, xmlNames, xmlFiles );

        // Creating editor tabs
        editors = new ArrayList<WebSyntaxArea> ( xmlContent.size () );
        for ( int i = 0; i < xmlContent.size (); i++ )
        {
            final WebPanel tabContent = new WebPanel ();
            tabContent.add ( new TabContentSeparator (), BorderLayout.NORTH );
            tabContent.add ( createSingleXmlEditor ( theme, xmlContent.get ( i ), xmlFiles.get ( i ) ), BorderLayout.CENTER );
            editorTabs.addTab ( xmlNames.get ( i ), tabContent );
            editorTabs.setIconAt ( i, tabIcon );
        }

        // Adding XML editors container into split
        split.setRightComponent ( editorsContainer );

        // Quick file search
        HotkeyManager.registerHotkey ( Hotkey.CTRL_N, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                final WebPopOver popOver = new WebPopOver ( StyleEditor.this );
                popOver.setCloseOnFocusLoss ( true );

                // File name search field
                final WebTextField searchField = new WebTextField ( 25 );
                searchField.setInputPrompt ( "Jump to file..." );
                popOver.add ( searchField );

                // Jump to tabs while typing
                searchField.getDocument ().addDocumentListener ( new DocumentChangeListener ()
                {
                    @Override
                    public void documentChanged ( final DocumentEvent e )
                    {
                        final String text = searchField.getText ().toLowerCase ( Locale.ROOT );
                        if ( !TextUtils.isEmpty ( text ) )
                        {
                            for ( final String name : xmlNames )
                            {
                                if ( name.toLowerCase ( Locale.ROOT ).contains ( text ) )
                                {
                                    editorTabs.setSelectedIndex ( xmlNames.indexOf ( name ) );
                                    break;
                                }
                            }
                        }
                    }
                } );

                // Close pop-over on ENTER or ESCAPE
                final KeyEventRunnable closeRunnable = new KeyEventRunnable ()
                {
                    @Override
                    public void run ( final KeyEvent e )
                    {
                        popOver.dispose ();
                        editors.get ( editorTabs.getSelectedIndex () ).requestFocusInWindow ();
                    }
                };
                searchField.onKeyPress ( Hotkey.ENTER, closeRunnable );
                searchField.onKeyPress ( Hotkey.ESCAPE, closeRunnable );

                popOver.show ( PopOverLocation.center );
            }
        } );
    }

    protected Component createSingleXmlEditor ( final Theme theme, final String xml, final ResourceFile xmlFile )
    {
        final WebSyntaxArea xmlEditor = new WebSyntaxArea ( xml, SyntaxPreset.xml );
        xmlEditor.applyPresets ( SyntaxPreset.base );
        xmlEditor.applyPresets ( SyntaxPreset.margin );
        xmlEditor.applyPresets ( SyntaxPreset.size );
        xmlEditor.applyPresets ( SyntaxPreset.historyLimit );
        xmlEditor.setCaretPosition ( 0 );
        xmlEditor.setHyperlinksEnabled ( true );
        xmlEditor.setLinkGenerator ( new CodeLinkGenerator ( xmlEditor ) );

        HotkeyManager.registerHotkey ( xmlEditor, xmlEditor, Hotkey.CTRL_SHIFT_Z, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                xmlEditor.undoLastAction ();
            }
        } );

        // Creating editor scroll with preferred settings
        final WebSyntaxScrollPane xmlEditorScroll = new WebSyntaxScrollPane ( xmlEditor );

        // Applying editor theme after scroll creation
        theme.apply ( xmlEditor );

        // Start listening edits
        xmlEditor.onChange ( new DocumentEventRunnable ()
        {
            private final WebTimer updateTimer = new WebTimer ( updateDelay, new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    SkinInfoConverter.addCustomResource ( xmlFile.getClassName (), xmlFile.getSource (), xmlEditor.getText () );
                    applySkin ();
                }
            } ).setRepeats ( false );

            @Override
            public void run ( final DocumentEvent e )
            {
                updateTimer.restart ( updateDelay );
            }
        } );

        // Locating view for the source
        xmlEditor.addCaretListener ( new CaretListener ()
        {
            @Override
            public void caretUpdate ( final CaretEvent e )
            {
                locateView ();
            }
        } );

        editors.add ( xmlEditor );
        return xmlEditorScroll;
    }

    protected void loadSkinSources ( final List<String> xmlContent, final List<String> xmlNames, final List<ResourceFile> xmlFiles )
    {
        // Adding base skin file
        final List<ResourceFile> resources = new ArrayList<ResourceFile> ();
        resources.add ( baseSkinFile );

        // Parsing all related skin files
        while ( resources.size () > 0 )
        {
            try
            {
                loadFirstResource ( resources, xmlContent, xmlNames, xmlFiles );
            }
            catch ( final IOException e )
            {
                Log.error ( this, e );
            }
        }
    }

    protected void applySkin ()
    {
        try
        {
            long time = System.currentTimeMillis ();
            StyleManager.setSkin ( new CustomSkin ( ( SkinInfo ) XmlUtils.fromXML ( editors.get ( 0 ).getText () ) ) );

            // Updating orientation
            applyViewOrientation ();

            // Updating layout
            componentViewer.revalidate ();

            // Information in status bar
            time = System.currentTimeMillis () - time;
            statusMessage.setIcon ( ok );
            statusMessage.setText ( "Style updated successfully within " + time + " ms" );
        }
        catch ( final ConversionException ex )
        {
            // Stack trace for parse exceptions
            if ( completeStackTrace )
            {
                Log.error ( this, ex );
            }
            else
            {
                Log.error ( this, "Fix syntax problems within the XML to update styling" );
            }

            // Information in status bar
            statusMessage.setIcon ( error );
            statusMessage.setText ( "Fix syntax problems within the XML to update styling" );
        }
        catch ( final Throwable ex )
        {
            // Full stack trace for unknown exceptions
            if ( completeStackTrace )
            {
                Log.error ( this, ex );
            }
            else
            {
                Log.error ( this, "Unable to update skin due to internal issues" );
            }

            // Information in status bar
            statusMessage.setIcon ( error );
            statusMessage.setText ( "Unable to update skin due to internal issues" );
        }
    }

    protected void applyViewOrientation ()
    {
        // Applying orientation to whole panel first
        componentViewer.applyComponentOrientation ( orientation );

        // Applying orientation to separate components as they might not be visible on panel
        for ( final JComponent component : previewComponents )
        {
            component.applyComponentOrientation ( orientation );
        }
    }

    protected void applyViewEnabledState ()
    {
        // Applying enabled state to separate components as they might not be visible on panel
        for ( final JComponent component : previewComponents )
        {
            SwingUtils.setEnabledRecursively ( component, enabled );
        }
    }

    protected void locateView ()
    {
        if ( !locate )
        {
            return;
        }

        final WebSyntaxArea syntaxArea = editors.get ( editorTabs.getSelectedIndex () );
        final String xml = syntaxArea.getText ();

        final Source xmlSource = new Source ( xml );
        xmlSource.setLogger ( null );
        xmlSource.fullSequentialParse ();

        final StartTag tag = xmlSource.getPreviousStartTag ( syntaxArea.getCaretPosition (), "style" );
        if ( tag != null )
        {
            // todo Won't work with new scheme, have to go all the way up and gather all style IDs
            final String type = tag.getAttributeValue ( ComponentStyleConverter.COMPONENT_TYPE_ATTRIBUTE );
            final String id = tag.getAttributeValue ( ComponentStyleConverter.STYLE_ID_ATTRIBUTE );
            locateView ( componentViewer, type, id );
        }
    }

    protected boolean locateView ( final Container container, final String type, final String id )
    {
        for ( int i = 0; i < container.getComponentCount (); i++ )
        {
            final Component component = container.getComponent ( i );
            if ( component instanceof JComponent )
            {
                final JComponent jc = ( JComponent ) component;
                final StyleableComponent sc = ( StyleableComponent ) jc.getClientProperty ( COMPONENT_TYPE_KEY );
                if ( sc != null && sc == StyleableComponent.valueOf ( type ) )
                {
                    final StyleId styleId = ( StyleId ) jc.getClientProperty ( STYLE_ID_KEY );
                    final StyleId sid = styleId == null ? sc.getDefaultStyleId () : styleId;
                    if ( CompareUtils.equals ( sid.getCompleteId (), id ) )
                    {
                        final Dimension visible = componentViewer.getVisibleRect ().getSize ();
                        final Rectangle bounds = SwingUtils.getRelativeBounds ( component, componentViewer );
                        if ( visible.height > bounds.height )
                        {
                            final int y = bounds.y + bounds.height / 2 - visible.height / 2;
                            componentViewer.scrollRectToVisible ( new Rectangle ( 0, y, visible.width, visible.height ) );
                        }
                        else
                        {
                            componentViewer.scrollRectToVisible ( new Rectangle ( 0, bounds.y, visible.width, visible.height ) );
                        }
                        return true;
                    }
                }
            }
            if ( component instanceof Container )
            {
                if ( locateView ( ( Container ) component, type, id ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    protected void loadFirstResource ( final List<ResourceFile> resources, final List<String> xmlContent, final List<String> xmlNames,
                                       final List<ResourceFile> xmlFiles ) throws IOException
    {
        final ResourceFile rf = resources.get ( 0 );
        final Source xmlSource = new Source ( ReflectUtils.getClassSafely ( rf.getClassName () ).getResource ( rf.getSource () ) );
        xmlSource.setLogger ( null );
        xmlSource.fullSequentialParse ();

        final Element baseClassTag = xmlSource.getFirstElement ( SkinInfoConverter.CLASS_NODE );
        final String baseClass = baseClassTag != null ? baseClassTag.getContent ().toString () : null;

        for ( final Element includeTag : xmlSource.getAllElements ( SkinInfoConverter.INCLUDE_NODE ) )
        {
            final String includeClass = includeTag.getAttributeValue ( SkinInfoConverter.NEAR_CLASS_ATTRIBUTE );
            final String finalClass = includeClass != null ? includeClass : baseClass;
            final String src = includeTag.getContent ().toString ();
            resources.add ( new ResourceFile ( ResourceLocation.nearClass, src, finalClass ) );
        }

        xmlContent.add ( xmlSource.toString () );
        xmlNames.add ( new File ( rf.getSource () ).getName () );
        xmlFiles.add ( rf );

        resources.remove ( 0 );
    }

    protected Theme loadXmlEditorTheme ()
    {
        try
        {
            return Theme.load ( StyleEditor.class.getResourceAsStream ( "resources/xml-syntax-theme.xml" ) );
        }
        catch ( final IOException e )
        {
            Log.error ( this, e );
            return null;
        }
    }

    /**
     * Custom tab content separator.
     */
    protected class TabContentSeparator extends JComponent
    {
        @Override
        protected void paintComponent ( final Graphics g )
        {
            g.setColor ( new Color ( 237, 237, 237 ) );
            g.fillRect ( 0, 0, getWidth (), getHeight () - 1 );
            g.setColor ( StyleConstants.darkBorderColor );
            g.drawLine ( 0, getHeight () - 1, getWidth () - 1, getHeight () - 1 );
        }

        @Override
        public Dimension getPreferredSize ()
        {
            return new Dimension ( 0, 4 );
        }
    }

    /**
     * StyleEditor main method used to launch editor.
     *
     * @param args arguments
     */
    public static void main ( final String[] args )
    {
        // Custom StyleEditor skin for WebLaF
        StyleManager.setDefaultSkin ( StyleEditorSkin.class.getCanonicalName () );
        WebLookAndFeel.install ();

        // Displaying StyleEditor
        final ResourceFile skin = new ResourceFile ( ResourceLocation.nearClass, "resources/skin.xml", StyleEditorSkin.class );
        final StyleEditor styleEditor = new StyleEditor ( skin );
        styleEditor.setVisible ( true );
    }
}