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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.Orientation;
import com.alee.api.jdk.Objects;
import com.alee.extended.button.SplitButtonAdapter;
import com.alee.extended.button.WebSplitButton;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.magnifier.MagnifierGlass;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.split.MultiSplitConstraints;
import com.alee.extended.split.WebMultiSplitPane;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.syntax.SyntaxPreset;
import com.alee.extended.syntax.WebSyntaxArea;
import com.alee.extended.syntax.WebSyntaxScrollPane;
import com.alee.extended.tree.WebFileTree;
import com.alee.extended.window.PopOverLocation;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.colorchooser.WebColorChooser;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.menu.WebCheckBoxMenuItem;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.scroll.WebScrollBar;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.slider.WebSlider;
import com.alee.laf.spinner.WebSpinner;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.text.WebTextField;
import com.alee.laf.text.WebTextPane;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.laf.tree.TreeSelectionStyle;
import com.alee.laf.window.WebFrame;
import com.alee.managers.animation.AnimationManager;
import com.alee.managers.animation.easing.Exponential;
import com.alee.managers.animation.easing.Quadratic;
import com.alee.managers.animation.transition.*;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.style.*;
import com.alee.managers.style.data.ComponentStyleConverter;
import com.alee.managers.style.data.SkinInfo;
import com.alee.managers.style.data.SkinInfoConverter;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.skin.web.WebSkin;
import com.alee.utils.*;
import com.alee.utils.swing.IntTextDocument;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.WebTimer;
import com.alee.utils.swing.extensions.DocumentEventRunnable;
import com.alee.utils.swing.extensions.KeyEventRunnable;
import com.alee.utils.swing.extensions.MouseEventRunnable;
import com.alee.utils.text.LoremIpsum;
import com.alee.utils.xml.Resource;
import com.thoughtworks.xstream.converters.ConversionException;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import org.slf4j.LoggerFactory;

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
     * todo 1. Fix multiple major style and functional issues
     * todo 2. Move into separate module?
     * todo 3. Translate editor?
     * todo 4. Add appropriate JavaDoc
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

    protected static final String COMPONENT_DESCRIPTOR_KEY = "component.descriptor.key";
    protected static final String SINGLE_PREVIEW_KEY = "single.preview.key";
    protected static final String STYLE_ID_KEY = "style.id.key";

    /**
     * General UI elements.
     */
    protected WebPanel container;
    protected WebMultiSplitPane split;

    /**
     * Preview UI elements.
     */
    protected WebPanel previewContainer;
    protected WebToolBar toolBar;
    protected WebScrollPane previewScroll;
    protected WebPanel previewPanel;

    /**
     * XML editors UI elements.
     */
    protected WebPanel editorsContainer;
    protected WebTabbedPane editorTabs;

    /**
     * Status UI elements.
     */
    protected WebStatusBar statusBar;
    protected WebLabel statusMessage;

    protected final List<JComponent> previewComponents = new ArrayList<JComponent> ();
    protected final List<WebPanel> boundsPanels = new ArrayList<WebPanel> ();

    private final MagnifierGlass magnifierGlass = new MagnifierGlass ( 4 );
    protected int updateDelay = 500;
    protected ComponentOrientation orientation = WebLookAndFeel.getOrientation ();
    protected boolean enabled = true;
    protected boolean locate = true;
    protected boolean brush = false;
    protected boolean completeStackTrace = false;

    protected final Resource baseSkinFile;
    protected List<WebSyntaxArea> editors;

    protected Throwable lastException = null;

    /**
     * Constructs new style editor and loads specified skin for editing.
     *
     * @param skin skin resource file
     */
    public StyleEditor ( final Resource skin )
    {
        super ( StyleId.styleeditor, "Style editor" );
        setIconImages ( WebLookAndFeel.getImages () );
        baseSkinFile = skin;

        container = new WebPanel ();
        getContentPane ().add ( container, BorderLayout.CENTER );

        split = new WebMultiSplitPane ( StyleId.styleeditorSplit.at ( StyleEditor.this ), Orientation.horizontal );
        split.setOneTouchExpandable ( true );
        container.add ( split, BorderLayout.CENTER );

        // Adding preview container into split
        previewContainer = new WebPanel ( StyleId.styleeditorPreview.at ( split ), new BorderLayout () );
        split.add ( previewContainer, MultiSplitConstraints.PREFERRED );

        // Adding XML editors container into split
        editorsContainer = new WebPanel ( StyleId.styleeditorEditors.at ( split ), new BorderLayout () );
        split.add ( editorsContainer, MultiSplitConstraints.FILL );

        createPreviewToolbar ();
        createPreviewPanel ();
        createEditors ();
        createStatusBar ();

        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
        setSize ( 1200, 800 );
        setLocationRelativeTo ( null );
    }

    /**
     * Initializes preview toolbar UI.
     */
    protected void createPreviewToolbar ()
    {
        toolBar = new WebToolBar ( StyleId.styleeditorPreviewToolbar.at ( previewContainer ) );
        previewContainer.add ( toolBar, BorderLayout.NORTH );

        final StyleId toolId = StyleId.styleeditorPreviewTool.at ( toolBar );

        final WebSplitButton magnifierButton = new WebSplitButton ( toolId, "4x", magnifierIcon );
        magnifierButton.addSplitButtonListener ( new SplitButtonAdapter ()
        {
            @Override
            public void buttonClicked ( final ActionEvent e )
            {
                magnifierGlass.displayOrDispose ( StyleEditor.this );
            }
        } );
        toolBar.add ( magnifierButton );

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
                    magnifierButton.setText ( factor + "x" );
                    magnifierGlass.setZoomFactor ( factor );
                    magnifierGlass.display ( StyleEditor.this );
                }
            } );
            menu.add ( menuItem );
        }
        magnifierButton.setPopupMenu ( menu );

        final WebToggleButton boundsButton = new WebToggleButton ( toolId, boundsIcon );
        boundsButton.setToolTip ( boundsIcon, "Show/hide component bounds" );
        boundsButton.addHotkey ( Hotkey.ALT_W );
        boundsButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                for ( final WebPanel boundsPanel : boundsPanels )
                {
                    final JComponent singlePreview = ( JComponent ) boundsPanel.getClientProperty ( SINGLE_PREVIEW_KEY );
                    boundsPanel.setStyleId ( boundsButton.isSelected () ? StyleId.styleeditorPreviewSingleDashed.at ( singlePreview ) :
                            StyleId.styleeditorPreviewSingleEmpty.at ( singlePreview ) );
                }
            }
        } );
        toolBar.add ( boundsButton );

        final WebToggleButton disabledButton = new WebToggleButton ( toolId, disabledIcon );
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

        final WebToggleButton orientationButton = new WebToggleButton ( toolId, orientationIcon, !orientation.isLeftToRight () );
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

        final WebToggleButton brushButton = new WebToggleButton ( toolId, brushIcon, brush );
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
                            final Point cloc = CoreSwingUtils.locationOnScreen ( c );
                            final Component actual = CoreSwingUtils.getTopComponentAt ( c, click.x - cloc.x, click.y - cloc.y );

                            // todo GLASSPANE USAGE
                            actual.repaint ();
                        }
                    }
                }, AWTEvent.MOUSE_EVENT_MASK );
            }
        } );
        toolBar.add ( brushButton );

        final WebToggleButton locateViewButton = new WebToggleButton ( toolId, locateIcon, locate );
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

    /**
     * Initializes preview panel UI.
     */
    protected void createPreviewPanel ()
    {
        final LoremIpsum loremIpsum = new LoremIpsum ();

        previewScroll = new WebScrollPane ( StyleId.styleeditorPreviewScroll.at ( previewContainer ) );
        previewScroll.getVerticalScrollBar ().setUnitIncrement ( 15 );
        previewContainer.add ( previewScroll, BorderLayout.CENTER );

        final VerticalFlowLayout layout = new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, 15, true, false );
        previewPanel = new WebPanel ( StyleId.styleeditorPreviewPane.at ( previewScroll ), layout );
        previewScroll.setViewportView ( previewPanel );

        //

        final WebLabel label = new WebLabel ( "Just a label", WebLookAndFeel.getIcon ( 16 ) );
        addViewComponent ( "Label", label, label, true );

        //

        final String styledText = "{Simple styled label:b;c(128,128,0);u}\n" + "with {hard:b} line breaks\n" + "across the text";
        final WebStyledLabel styledLabel = new WebStyledLabel ( styledText );
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
        textArea.setText ( loremIpsum.getParagraphs ( 5 ) );

        final WebScrollPane sp = new WebScrollPane ( textArea );
        sp.setPreferredWidth ( 0 );
        addViewComponent ( "Scroll pane", sp, sp, false );

        //

        final WebTextArea textArea2 = new WebTextArea ( StyleId.textareaNonOpaque );
        textArea2.setRows ( 5 );
        textArea2.setText ( loremIpsum.getParagraphs ( 5 ) );

        final WebScrollPane hsp = new WebScrollPane ( StyleId.scrollpaneHovering, textArea2 );
        hsp.setPreferredWidth ( 0 );
        addViewComponent ( "Hovering scroll pane", hsp, hsp, false );

        //

        final WebTextPane textPane = new WebTextPane ( StyleId.textpaneDecorated );
        textPane.setText ( loremIpsum.getWords ( 5 ) + "\n" + loremIpsum.getWords ( 5, 5 ) );
        addViewComponent ( "Text pane", textPane, textPane, true );

        //

        final String[] d = new String[]{ "Mikle Garin", "Joe Phillips", "Lilly Stewart", "Alex Jackson", "Joshua Martin", "Mark Einsberg",
                "Alice Manson", "Nancy Drew", "John Linderman", "Trisha Mathew", "Annae Mendy", "Wendy Anderson", "Alex Kurovski" };
        final WebComboBox cb = new WebComboBox ( d );
        addViewComponent ( "Combo box", cb, cb, true );

        //

        final WebSlider slider = new WebSlider ();
        addViewComponent ( "Slider", slider, slider, true );

        //

        final WebSpinner spinner = new WebSpinner ();
        addViewComponent ( "Spinner", spinner, spinner, true );

        //

        final WebProgressBar progress = new WebProgressBar ();
        addViewComponent ( "Progress bar", progress, progress, true );

        final int min = progress.getMinimum ();
        final int max = progress.getMaximum ();
        final QueueTransition<Integer> progressAnimator = new QueueTransition<Integer> ( true );
        progressAnimator.add ( new TimedTransition<Integer> ( min, max, new Quadratic.Out (), 2000L ) );
        progressAnimator.add ( new IdleTransition<Integer> ( max, 1000L ) );
        progressAnimator.add ( new TimedTransition<Integer> ( max, min, new Exponential.Out (), 2000L ) );
        progressAnimator.add ( new IdleTransition<Integer> ( min, 1000L ) );
        progressAnimator.addListener ( new TransitionAdapter<Integer> ()
        {
            @Override
            public void started ( final Transition transition, final Integer value )
            {
                progress.setValue ( value );
            }

            @Override
            public void adjusted ( final Transition transition, final Integer value )
            {
                progress.setValue ( value );
            }
        } );
        AnimationManager.play ( progressAnimator );

        //

        final WebList wl = new WebList ( d );
        wl.setVisibleRowCount ( 6 );
        final WebScrollPane wlScroll = new WebScrollPane ( wl );
        addViewComponent ( "List", wlScroll, wl, false );

        //

        final WebFileTree homeFileTree = new WebFileTree ( FileUtils.getUserHomePath () );
        homeFileTree.setExpandSelected ( false );
        homeFileTree.setSelectionStyle ( TreeSelectionStyle.group );
        homeFileTree.onKeyRelease ( Hotkey.SPACE, new KeyEventRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
            {
                homeFileTree.updateVisibleNodes ();
            }
        } );
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

        final WebFrame wf = new WebFrame ( StyleId.frameDecorated, "Decorated frame" );
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

        //

        final WebColorChooser wcc = new WebColorChooser ();
        final WebButton wccb = new WebButton ( "color chooser", new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final JDialog dlg = WebColorChooser.createDialog ( ( Component ) e.getSource (), "Title", true, wcc, null, null );
                dlg.setVisible ( true );
            }
        } );
        addViewComponent ( "Color chooser", wccb, wcc, true );

        //

        final WebFileChooser wfc = new WebFileChooser ();
        final WebButton wfcb = new WebButton ( "file chooser", new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                wfc.showDialog ( ( Component ) e.getSource (), "Okay" );
            }
        } );
        addViewComponent ( "File chooser", wfcb, wfc, true );

        //

        final WebOptionPane wop = new WebOptionPane ( loremIpsum.getWords ( 5 ), WebOptionPane.INFORMATION_MESSAGE );
        final WebButton wopb = new WebButton ( "option pane", new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final JDialog dlg = wop.createDialog ( ( Component ) e.getSource (), "Title" );
                dlg.setVisible ( true );
            }
        } );
        addViewComponent ( "Option pane", wopb, wop, true );
    }

    /**
     * Initializes single component preview.
     *
     * @param title         preview title
     * @param displayedView displayed view
     * @param view          view
     * @param center        whether or not should center view
     */
    protected void addViewComponent ( final String title, final JComponent displayedView, final JComponent view, final boolean center )
    {
        addViewComponent ( title, null, displayedView, view, center );
    }

    /**
     * Initializes single component preview.
     *
     * @param title         preview title
     * @param styleId       style ID
     * @param displayedView displayed view
     * @param view          view
     * @param center        whether or not should center view
     */
    protected void addViewComponent ( final String title, final StyleId styleId, final JComponent displayedView, final JComponent view,
                                      final boolean center )
    {
        final ComponentDescriptor descriptor = StyleManager.getDescriptor ( view );

        final StyleId singleId = StyleId.styleeditorPreviewSingle.at ( previewPanel );
        final WebPanel singlePreview = new WebPanel ( singleId, new BorderLayout ( 0, 0 ) );

        final ChildStyleId titleId = StyleId.styleeditorPreviewSingleTitle;
        final WebLabel titleLabel = new WebLabel ( titleId.at ( singlePreview ), title, descriptor.getIcon (), WebLabel.LEADING );
        singlePreview.add ( titleLabel, BorderLayout.NORTH );

        final StyleId emptyId = StyleId.styleeditorPreviewSingleEmpty.at ( singlePreview );
        final WebPanel boundsPanel = new WebPanel ( emptyId, displayedView );
        boundsPanel.putClientProperty ( SINGLE_PREVIEW_KEY, singlePreview );
        boundsPanels.add ( boundsPanel );

        final StyleId viewId = StyleId.styleeditorPreviewSingleShadow.at ( singlePreview );
        final WebPanel viewPanel = new WebPanel ( viewId, center ? new CenterPanel ( boundsPanel ) : boundsPanel );
        singlePreview.add ( viewPanel, BorderLayout.CENTER );

        singlePreview.putClientProperty ( COMPONENT_DESCRIPTOR_KEY, descriptor );
        singlePreview.putClientProperty ( STYLE_ID_KEY, styleId != null ? styleId : StyleManager.getStyleId ( view ) );

        titleLabel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                viewPanel.setVisible ( !viewPanel.isVisible () );
                previewPanel.revalidate ();
                previewPanel.repaint ();
            }
        } );

        previewPanel.add ( singlePreview );
        previewComponents.add ( view );
    }

    /**
     * Initializes status bar UI.
     */
    protected void createStatusBar ()
    {
        statusBar = new WebStatusBar ( StyleId.styleeditorStatus.at ( StyleEditor.this ) );
        container.add ( statusBar, BorderLayout.SOUTH );

        final ImageIcon updateIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/update.png" ) );
        final StyleId labelId = StyleId.styleeditorStatusLabel.at ( statusBar );
        final WebLabel delayLabel = new WebLabel ( labelId, "Skin update delay:", updateIcon );
        statusBar.add ( delayLabel );

        final StyleId delayId = StyleId.styleeditorStatusDelay.at ( statusBar );
        final WebTextField delayField = new WebTextField ( delayId, new IntTextDocument (), "" + updateDelay, 3 );
        delayField.onChange ( new DocumentEventRunnable<WebTextField> ()
        {
            @Override
            public void run ( @NotNull final WebTextField component, @Nullable final DocumentEvent event )
            {
                try
                {
                    final int value = Integer.parseInt ( component.getText () );
                    updateDelay = value >= 0 ? value : 0;
                }
                catch ( final Exception ignored )
                {
                    // Leave delay unchanged
                }
            }
        } );
        statusBar.add ( delayField );

        statusBar.add ( new WebLabel ( labelId, "ms" ) );

        statusBar.addSeparator ();

        statusMessage = new WebLabel ( labelId, "Edit XML at the right side and see UI changes at the left side!", info );
        statusMessage.onMousePress ( MouseButton.left, new MouseEventRunnable ()
        {
            @Override
            public void run ( @NotNull final MouseEvent e )
            {
                if ( lastException != null )
                {
                    final String stack = ExceptionUtils.getStackTrace ( lastException );
                    final String text = stack.replaceAll ( "\t", "         " );
                    TooltipManager.showOneTimeTooltip ( statusMessage, null, text, TooltipWay.up );
                }
            }
        } );
        statusBar.add ( statusMessage );

        //

        final StyleId statusToggleId = StyleId.styleeditorStatusToggle.at ( statusBar );
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
    }

    /**
     * Initializes XML editors UI.
     */
    protected void createEditors ()
    {
        // Creating XML editors tabbed pane
        editorTabs = new WebTabbedPane ( StyleId.styleeditorEditorsTabs.at ( editorsContainer ) );
        // todo Use it once not all XML are opened at once: editorTabs.setTabLayoutPolicy ( JTabbedPane.SCROLL_TAB_LAYOUT );
        editorsContainer.add ( editorTabs, BorderLayout.CENTER );

        // Parsing all related files
        final List<String> xmlContent = new ArrayList<String> ();
        final List<String> xmlNames = new ArrayList<String> ();
        final List<Resource> xmlFiles = new ArrayList<Resource> ();
        loadSkinSources ( xmlContent, xmlNames, xmlFiles );

        // Creating editor tabs
        final int numTabs = xmlContent.size ();
        editors = new ArrayList<WebSyntaxArea> ( numTabs );
        final List<String> sortedNames = new ArrayList<String> ( numTabs );
        for ( int i = 0; i < xmlContent.size (); i++ )
        {
            final Component tabContent = createSingleXmlEditor ( xmlContent.get ( i ), xmlFiles.get ( i ) );
            final String name = xmlNames.get ( i );
            int j = 0;
            while ( j < i )
            {
                if ( sortedNames.get ( j ).compareTo ( name ) > 0 )
                {
                    break;
                }
                j++;
            }
            editorTabs.insertTab ( name, null, tabContent, null, j );
            editorTabs.setIconAt ( j, tabIcon );
            sortedNames.add ( j, name );
        }

        // Quick file search
        HotkeyManager.registerHotkey ( Hotkey.CTRL_N, new HotkeyRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
            {
                final WebPopOver popOver = new WebPopOver ( StyleEditor.this );
                popOver.setCloseOnFocusLoss ( true );

                // File name search field
                final WebTextField searchField = new WebTextField ( 25 );
                searchField.setInputPrompt ( "Jump to file..." );
                popOver.add ( searchField );

                // Jump to tabs while typing
                searchField.onChange ( new DocumentEventRunnable<WebTextField> ()
                {
                    @Override
                    public void run ( @NotNull final WebTextField component, @Nullable final DocumentEvent event )
                    {
                        final String text = component.getText ().toLowerCase ( Locale.ROOT );
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
                    public void run ( @NotNull final KeyEvent e )
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

    /**
     * Returns XML editor created for the specified XML file.
     *
     * @param xml     XML content
     * @param xmlFile XML file
     * @return XML editor created for the specified XML file
     */
    protected Component createSingleXmlEditor ( final String xml, final Resource xmlFile )
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
            public void run ( @NotNull final KeyEvent e )
            {
                xmlEditor.undoLastAction ();
            }
        } );

        // Creating editor scroll with preferred settings
        final WebSyntaxScrollPane xmlEditorScroll = new WebSyntaxScrollPane ( StyleId.syntaxareaScrollUndecorated, xmlEditor );

        // Start listening edits
        xmlEditor.onChange ( new DocumentEventRunnable<WebSyntaxArea> ()
        {
            private final WebTimer updateTimer = new WebTimer ( updateDelay, new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    SkinInfoConverter.addCustomResource ( xmlFile.getClassName (), xmlFile.getPath (), xmlEditor.getText () );
                    applySkin ();
                }
            } ).setRepeats ( false );

            @Override
            public void run ( @NotNull final WebSyntaxArea component, @Nullable final DocumentEvent event )
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

    protected void loadSkinSources ( final List<String> xmlContent, final List<String> xmlNames, final List<Resource> xmlFiles )
    {
        // Adding base skin file
        final List<Resource> resources = new ArrayList<Resource> ();
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
                LoggerFactory.getLogger ( StyleEditor.class ).error ( e.toString (), e );
            }
        }
    }

    protected void applySkin ()
    {
        try
        {
            long time = System.currentTimeMillis ();
            final String text = editors.get ( 0 ).getText ();
            final SkinInfo skinInfo = XmlUtils.fromXML ( text );
            StyleManager.setSkin ( new XmlSkin ( skinInfo ) );

            // Updating orientation
            applyViewOrientation ();

            // Updating layout
            previewPanel.revalidate ();

            // Information in status bar
            time = System.currentTimeMillis () - time;
            statusMessage.setIcon ( ok );
            statusMessage.setText ( "Style updated successfully within " + time + " ms" );
            statusMessage.setCursor ( Cursor.getDefaultCursor () );

            // Clearing tooltips
            lastException = null;
        }
        catch ( final ConversionException ex )
        {
            // Stack trace for parse exceptions
            if ( completeStackTrace )
            {
                LoggerFactory.getLogger ( StyleEditor.class ).error ( ex.toString (), ex );
            }
            else
            {
                LoggerFactory.getLogger ( StyleEditor.class ).error ( "Fix syntax problems within the XML to update styling" );
            }

            // Information in status bar
            statusMessage.setIcon ( error );
            statusMessage.setText ( "Fix syntax problems within the XML to update styling" );
            statusMessage.setCursor ( Cursor.getPredefinedCursor ( Cursor.HAND_CURSOR ) );

            // Adding tooltip with detailed message
            lastException = ex;
        }
        catch ( final Exception ex )
        {
            // Full stack trace for unknown exceptions
            if ( completeStackTrace )
            {
                LoggerFactory.getLogger ( StyleEditor.class ).error ( ex.toString (), ex );
            }
            else
            {
                LoggerFactory.getLogger ( StyleEditor.class ).error ( "Unable to update skin due to internal issues" );
            }

            // Information in status bar
            statusMessage.setIcon ( error );
            statusMessage.setText ( "Unable to update skin due to internal issues" );
            statusMessage.setCursor ( Cursor.getPredefinedCursor ( Cursor.HAND_CURSOR ) );

            // Adding tooltip with detailed message
            lastException = ex;
        }
    }

    protected void applyViewOrientation ()
    {
        // Applying orientation to whole panel first
        previewPanel.applyComponentOrientation ( orientation );

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
            locateView ( previewPanel, type, id );
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
                final ComponentDescriptor descriptor = ( ComponentDescriptor ) jc.getClientProperty ( COMPONENT_DESCRIPTOR_KEY );
                if ( descriptor != null && descriptor.getId ().equals ( type ) )
                {
                    final StyleId styleId = ( StyleId ) jc.getClientProperty ( STYLE_ID_KEY );
                    final StyleId sid = styleId != null ? styleId : StyleId.getDefault ( jc );
                    if ( Objects.equals ( sid.getCompleteId (), id ) )
                    {
                        final Dimension visible = previewPanel.getVisibleRect ().getSize ();
                        final Rectangle bounds = CoreSwingUtils.getRelativeBounds ( component, previewPanel );
                        if ( visible.height > bounds.height )
                        {
                            final int y = bounds.y + bounds.height / 2 - visible.height / 2;
                            previewPanel.scrollRectToVisible ( new Rectangle ( 0, y, visible.width, visible.height ) );
                        }
                        else
                        {
                            previewPanel.scrollRectToVisible ( new Rectangle ( 0, bounds.y, visible.width, visible.height ) );
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

    protected void loadFirstResource ( final List<Resource> resources, final List<String> xmlContent, final List<String> xmlNames,
                                       final List<Resource> xmlFiles ) throws IOException
    {
        final Resource rf = resources.get ( 0 );
        final Source xmlSource = new Source ( ReflectUtils.getClassSafely ( rf.getClassName () ).getResource ( rf.getPath () ) );
        xmlSource.setLogger ( null );
        xmlSource.fullSequentialParse ();

        final Element baseClassTag = xmlSource.getFirstElement ( SkinInfoConverter.CLASS_NODE );
        final String baseClass = baseClassTag != null ? baseClassTag.getContent ().toString () : null;

        for ( final Element includeTag : xmlSource.getAllElements ( SkinInfoConverter.INCLUDE_NODE ) )
        {
            final String includeClass = includeTag.getAttributeValue ( SkinInfoConverter.NEAR_CLASS_ATTRIBUTE );
            final String finalClass = includeClass != null ? includeClass : baseClass;
            final String src = includeTag.getContent ().toString ();
            resources.add ( new Resource ( finalClass, src ) );
        }

        xmlContent.add ( xmlSource.toString () );
        xmlNames.add ( new File ( rf.getPath () ).getName () );
        xmlFiles.add ( rf );

        resources.remove ( 0 );
    }

    /**
     * {@link StyleEditor} main launch method.
     *
     * @param args editor arguments
     */
    public static void main ( final String[] args )
    {
        final Class<? extends Skin> skinClass = WebSkin.class;

        // Custom StyleEditor skin for WebLaF
        WebLookAndFeel.install ( skinClass );

        // Edited skin file
        final Resource skin = new Resource ( skinClass, "resources/skin.xml" );

        // Displaying StyleEditor
        final StyleEditor styleEditor = new StyleEditor ( skin );
        styleEditor.setVisible ( true );
    }
}