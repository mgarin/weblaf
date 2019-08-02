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

package com.alee.laf;

import com.alee.api.jdk.BiConsumer;
import com.alee.extended.svg.SvgIcon;
import com.alee.graphics.image.gif.GifIcon;
import com.alee.laf.edt.ExceptionNonEventThreadHandler;
import com.alee.laf.edt.NonEventThreadHandler;
import com.alee.laf.list.ListCellParameters;
import com.alee.laf.list.WebListCellRenderer;
import com.alee.managers.UIManagers;
import com.alee.managers.icon.Icons;
import com.alee.managers.icon.LazyIcon;
import com.alee.managers.style.ComponentDescriptor;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleManager;
import com.alee.painter.Painter;
import com.alee.skin.web.WebSkin;
import com.alee.utils.*;
import com.alee.utils.laf.WebBorder;
import com.alee.utils.reflection.LazyInstance;
import com.alee.utils.swing.SwingLazyValue;
import com.alee.utils.swing.WeakComponentDataList;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * LaF class containing methods to conveniently install, configure and uninstall WebLaF.
 * Most of the configurations are provided by {@link StyleManager} as it contains actual list of supported components.
 *
 * @author Mikle Garin
 * @see <a href="http://weblookandfeel.com/">WebLaF site</a>
 * @see <a href="https://github.com/mgarin/weblaf">GitHub project</a>
 * @see <a href="https://github.com/mgarin/weblaf/issues">Issues tracker</a>
 * @see <a href="https://github.com/mgarin/weblaf/wiki">Project wiki</a>
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLaF">How to use WebLaF</a>
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-build-WebLaF-from-sources">How to build WebLaF from sources</a>
 */
public class WebLookAndFeel extends BasicLookAndFeel
{
    /**
     * If this client property is set to {@link Boolean#TRUE} on a component, UI delegates should follow the typical Swing behavior of not
     * overriding a user-defined border on it.
     */
    public static final String PROPERTY_HONOR_USER_BORDER = "WebLookAndFeel.honorUserBorder";

    /**
     * If this system property is set to {@code true}, UI delegates should follow the typical Swing behavior of not overriding a
     * user-defined border if one is installed on components.
     */
    public static final String PROPERTY_HONOR_USER_BORDERS = "WebLookAndFeel.honorUserBorders";

    /**
     * Common Swing component properties.
     */
    public static final String LOOK_AND_FEEL_PROPERTY = "lookAndFeel";
    public static final String UI_PROPERTY = "UI";
    public static final String LAF_MARGIN_PROPERTY = "lafMargin";
    public static final String LAF_PADDING_PROPERTY = "lafPadding";
    public static final String COMPONENT_ORIENTATION_PROPERTY = "componentOrientation";
    public static final String MARGIN_PROPERTY = "margin";
    public static final String ENABLED_PROPERTY = "enabled";
    public static final String FOCUSABLE_PROPERTY = "focusable";
    public static final String EDITABLE_PROPERTY = "editable";
    public static final String MODEL_PROPERTY = "model";
    public static final String VIEWPORT_PROPERTY = "viewport";
    public static final String VERTICAL_SCROLLBAR_PROPERTY = "verticalScrollBar";
    public static final String HORIZONTAL_SCROLLBAR_PROPERTY = "horizontalScrollBar";
    public static final String TABLE_HEADER_PROPERTY = "tableHeader";
    public static final String FLOATABLE_PROPERTY = "floatable";
    public static final String WINDOW_DECORATION_STYLE_PROPERTY = "windowDecorationStyle";
    public static final String RESIZABLE_PROPERTY = "resizable";
    public static final String ICON_IMAGE_PROPERTY = "iconImage";
    public static final String TITLE_PROPERTY = "title";
    public static final String VISIBLE_PROPERTY = "visible";
    public static final String DOCUMENT_PROPERTY = "document";
    public static final String OPAQUE_PROPERTY = "opaque";
    public static final String OPACITY_PROPERTY = "opacity";
    public static final String BORDER_PROPERTY = "border";
    public static final String ICON_TEXT_GAP_PROPERTY = "iconTextGap";
    public static final String MINIMUM_PROPERTY = "minimum";
    public static final String MAXIMUM_PROPERTY = "maximum";
    public static final String MINOR_TICK_SPACING_PROPERTY = "minorTickSpacing";
    public static final String MAJOR_TICK_SPACING_PROPERTY = "majorTickSpacing";
    public static final String PAINTER_PROPERTY = "painter";
    public static final String RENDERER_PROPERTY = "renderer";
    public static final String TEXT_PROPERTY = "text";
    public static final String TIP_TEXT_PROPERTY = "tiptext";
    public static final String FONT_PROPERTY = "font";
    public static final String BACKGROUND_PROPERTY = "background";
    public static final String FOREGROUND_PROPERTY = "foreground";
    public static final String INDETERMINATE_PROPERTY = "indeterminate";
    public static final String DROP_LOCATION = "dropLocation";
    public static final String ORIENTATION_PROPERTY = "orientation";
    public static final String HORIZONTAL_ALIGNMENT_PROPERTY = "horizontalAlignment";
    public static final String VERTICAL_ALIGNMENT_PROPERTY = "verticalAlignment";
    public static final String LEADING_COMPONENT_PROPERTY = "leadingComponent";
    public static final String TRAILING_COMPONENT_PROPERTY = "trailingComponent";
    public static final String TABBED_PANE_STYLE_PROPERTY = "tabbedPaneStyle";
    public static final String EDITOR_PROPERTY = "editor";
    public static final String TRANSFER_HANDLER_PROPERTY = "transferHandler";

    /**
     * Bound property name for tree data provider.
     * Data provider is not supported by WebTree, but it is a base for various extensions so property is located here.
     */
    public final static String TREE_DATA_PROVIDER_PROPERTY = "dataProvider";

    /**
     * Bound property name for tree filter.
     * Filtering is not supported by WebTree, but it is a base for various extensions so property is located here.
     */
    public final static String TREE_FILTER_PROPERTY = "filter";

    /**
     * Bound property name for tree comparator.
     * Sorting is not supported by WebTree, but it is a base for various extensions so property is located here.
     */
    public final static String TREE_COMPARATOR_PROPERTY = "comparator";

    /**
     * Whether or not library should force Event Dispatch Thread usage for all UI-related operations.
     * Enabling this might allow you to find out places where you try to interact with UI elements outside of the EDT.
     * By default it is disabled to avoid issues this might cause in different applications not following proper Swing design patterns.
     *
     * JavaFX has a similar check enabled by default which would cause an exception whenever you try to access UI elements outside of the
     * JavaFX thread which you can ask to execute any code through Platform class.
     */
    protected static boolean forceSingleEventsThread = false;

    /**
     * Whether or not library should also enforce Event Dispatch Thread usage for {@link Component} events.
     * Enabling this might allow you to find out places where you are firing {@link Component} events outside of EDT.
     * By default it is disabled to avoid affecting application performance.
     */
    protected static boolean useStrictEventThreadListeners = false;

    /**
     * Forced Event Dispatch Thread lsiteners mix.
     */
    protected static final StrictEventThreadListeners STRICT_EDT_LISTENERS = new StrictEventThreadListeners ();

    /**
     * Special handler for exceptions thrown when any UI operation is executed outside of the Event Dispatch Thread.
     */
    protected static NonEventThreadHandler nonEventThreadHandler = new ExceptionNonEventThreadHandler ();

    /**
     * {@link WebLookAndFeel} icons.
     */
    protected static List<ImageIcon> icons = null;

    /**
     * Disabled icons cache.
     */
    protected static final Map<Icon, ImageIcon> disabledIcons = new WeakHashMap<Icon, ImageIcon> ( 50 );

    /**
     * Alt hotkey processor for application windows with menu.
     */
    protected static final AltProcessor altProcessor = new AltProcessor ();

    /**
     * Whether to hide component mnemonics by default or not.
     */
    protected static boolean isMnemonicHidden = true;

    /**
     * Whether or not component's custom {@link Shape}s should be used for better mouse events detection.
     * This option basically enhances {@link JComponent#contains(int, int)} method to be able to detect component {@link Shape}s.
     *
     * Downside of this enhancement is a slightly reduced overall mouse events performance, although it shouldn't be noticeable on the UI.
     * Actual difference is between 0.5 and 2-4 microseconds per {@link JComponent#contains(int, int)} call.
     * This might seem to be a lot and mouse movement over UI indeed generates a lot of mouse events which in turn do multiple calls to
     * {@link JComponent#contains(int, int)}, but those are still far from hitting the point where it will affect UI responsiveness.
     *
     * Best solution overall - if you want to both keep performance and have neat shapes detection - is to enable this settings only
     * for certain components while leaving this feature disabled for others. It is possible to do just that using
     * {@link com.alee.managers.style.ShapeSupport} API available in all WebLaF components and providing the setting through component
     * style - that way you will ensure that it is enabled only for components that actually use some sort of complex shape.
     *
     * @see com.alee.painter.PainterSupport#contains(JComponent, ComponentUI, Painter, int, int)
     * @see com.alee.managers.style.ShapeSupport
     */
    protected static boolean shapeDetectionEnabled = true;

    /**
     * Global {@link EventListenerList} for various listeners that can be registered for some global events.
     *
     * @see #visibleWindowListeners
     * @see #addVisibleWindowListener(VisibleWindowListener)
     * @see #removeVisibleWindowListener(VisibleWindowListener)
     */
    protected static final EventListenerList globalListeners = new EventListenerList ();

    /**
     * Special per-{@link JComponent} listeners for tracking visibility changes of any Swing windows within application.
     * Since there is no good way to track Swing {@link Window}s creation and destruction {@link com.alee.laf.rootpane.WebRootPaneUI}
     * has the tracking implementation instead as the closest element to almost any common {@link Window} being opened.
     *
     * Due to specifics of the implementation it only tracks next types of components:
     * - {@link JDialog}
     * - {@link JFrame}
     * - {@link JWindow}
     * - {@link JPopupMenu} all of its use cases
     *
     * Next types of components are not tracked (basically raw AWT types):
     * - {@link Window}
     * - {@link Dialog}
     * - {@link Frame}
     *
     * @see #addVisibleWindowListener(JComponent, VisibleWindowListener)
     * @see #removeVisibleWindowListener(JComponent, VisibleWindowListener)
     */
    protected static final WeakComponentDataList<JComponent, VisibleWindowListener> visibleWindowListeners =
            new WeakComponentDataList<JComponent, VisibleWindowListener> ( "WebLookAndFeel.VisibleWindowListener", 50 );

    /**
     * Previously installed {@link LookAndFeel}.
     * Used within {@link #uninstall()} call to restore previous LaF.
     */
    protected static Class<? extends LookAndFeel> previousLookAndFeelClass;

    /**
     * Globally applied orientation.
     * LanguageManager controls this property because usually it is the language that affects default component orientation.
     *
     * @see #getOrientation()
     * @see #setOrientation(java.awt.ComponentOrientation)
     * @see #setOrientation(boolean)
     */
    protected static ComponentOrientation orientation;

    /**
     * Reassignable LookAndFeel fonts.
     * @see NativeFonts
     */

    /**
     * @see ControlType#CONTROL
     */
    public static Font globalControlFont = NativeFonts.get ( ControlType.CONTROL );
    public static Font canvasFont;
    public static Font imageFont;
    public static Font buttonFont;
    public static Font splitButtonFont;
    public static Font toggleButtonFont;
    public static Font checkBoxFont;
    public static Font tristateCheckBoxFont;
    public static Font radioButtonFont;
    public static Font comboBoxFont;
    public static Font spinnerFont;
    public static Font textFieldFont;
    public static Font formattedTextFieldFont;
    public static Font passwordFieldFont;
    public static Font colorChooserFont;
    public static Font fileChooserFont;
    public static Font labelFont;
    public static Font styledLabelFont;
    public static Font linkFont;
    public static Font listFont;
    public static Font panelFont;
    public static Font popupFont;
    public static Font progressBarFont;
    public static Font scrollPaneFont;
    public static Font viewportFont;
    public static Font sliderFont;
    public static Font tabbedPaneFont;
    public static Font tableFont;
    public static Font tableHeaderFont;
    public static Font titledBorderFont;
    public static Font treeFont;

    /**
     * @see ControlType#TEXT
     */
    public static Font globalTextFont = NativeFonts.get ( ControlType.TEXT );
    public static Font textAreaFont;
    public static Font textPaneFont;
    public static Font editorPaneFont;

    /**
     * @see ControlType#TOOLTIP
     */
    public static Font globalTooltipFont = NativeFonts.get ( ControlType.TOOLTIP );
    public static Font toolTipFont;

    /**
     * @see ControlType#MENU
     */
    public static Font globalMenuFont = NativeFonts.get ( ControlType.MENU );
    public static Font menuBarFont;
    public static Font menuFont;
    public static Font menuItemFont;
    public static Font radioButtonMenuItemFont;
    public static Font checkBoxMenuItemFont;
    public static Font popupMenuFont;
    public static Font toolBarFont;

    /**
     * @see ControlType#MENU_SMALL
     */
    public static Font globalMenuSmallFont = NativeFonts.get ( ControlType.MENU_SMALL );
    public static Font menuAcceleratorFont;
    public static Font menuItemAcceleratorFont;
    public static Font radioButtonMenuItemAcceleratorFont;
    public static Font checkBoxMenuItemAcceleratorFont;

    /**
     * @see ControlType#WINDOW
     */
    public static Font globalWindowFont = NativeFonts.get ( ControlType.WINDOW );
    public static Font internalFrameFont;

    /**
     * @see ControlType#MESSAGE
     */
    public static Font globalMessageFont = NativeFonts.get ( ControlType.MESSAGE );
    public static Font optionPaneFont;

    /**
     * Returns {@link WebLookAndFeel} name.
     *
     * @return {@link WebLookAndFeel} name
     */
    @Override
    public String getName ()
    {
        return "WebLaF";
    }

    /**
     * Returns unique {@link WebLookAndFeel} identifier.
     *
     * @return unique {@link WebLookAndFeel} identifier
     */
    @Override
    public String getID ()
    {
        return "weblaf";
    }

    /**
     * Returns short {@link WebLookAndFeel} description.
     *
     * @return short {@link WebLookAndFeel} description
     */
    @Override
    public String getDescription ()
    {
        return "Styleable cross-platform Look and Feel for Swing applications";
    }

    /**
     * Always returns {@code false} since {@link WebLookAndFeel} is not native for any platform.
     *
     * @return {@code false}
     */
    @Override
    public boolean isNativeLookAndFeel ()
    {
        return false;
    }

    /**
     * Always returns {@code true} since {@link WebLookAndFeel} supports any platform which can run Java applications.
     *
     * @return {@code true}
     */
    @Override
    public boolean isSupportedLookAndFeel ()
    {
        return true;
    }

    /**
     * Always returns {@code true} since {@link WebLookAndFeel} supports window decorations under all platforms.
     *
     * @return {@code true} since {@link WebLookAndFeel} supports window decorations under all platforms
     */
    @Override
    public boolean getSupportsWindowDecorations ()
    {
        return true;
    }

    /**
     * Initializes custom WebLookAndFeel features.
     */
    @Override
    public void initialize ()
    {
        // Initializing WebLaF managers
        initializeManagers ();

        // Inititalizes default LaF settings
        super.initialize ();

        // Listening to ALT key for menubar quick focusing
        KeyboardFocusManager.getCurrentKeyboardFocusManager ().addKeyEventPostProcessor ( altProcessor );
    }

    /**
     * Uninititalizes custom WebLookAndFeel features.
     */
    @Override
    public void uninitialize ()
    {
        // Uninititalizes default LaF settings
        super.uninitialize ();

        // Removing alt processor
        KeyboardFocusManager.getCurrentKeyboardFocusManager ().removeKeyEventPostProcessor ( altProcessor );
    }

    /**
     * Initializes {@link WebLookAndFeel} UI classes.
     *
     * @param table {@link UIDefaults} table
     */
    @Override
    protected void initClassDefaults ( final UIDefaults table )
    {
        // Dynamically provided UI classes
        for ( final ComponentDescriptor descriptor : StyleManager.getDescriptors () )
        {
            // Asking each descriptor to perform an update
            descriptor.updateDefaults ( table );
        }
    }

    /**
     * Adds some default colors to the {@code UIDefaults} that are not used by WebLookAndFeel directly, but will help custom
     * components that assume BasicLookAndFeel conventions.
     *
     * @param table UIDefaults table
     */
    @Override
    protected void initSystemColorDefaults ( final UIDefaults table )
    {
        super.initSystemColorDefaults ( table );

        final String menuColor = ColorUtils.toHex ( Color.WHITE );
        final String textColor = ColorUtils.toHex ( Color.BLACK );
        final String textHighlightColor = ColorUtils.toHex ( new Color ( 210, 210, 210 ) );
        final String inactiveTextColor = ColorUtils.toHex ( new Color ( 160, 160, 160 ) );

        final String[] defaultSystemColors = {
                "menu", menuColor,
                "menuText", textColor,
                "textHighlight", textHighlightColor,
                "textHighlightText", textColor,
                "textInactiveText", inactiveTextColor,
                "controlText", textColor,
        };

        loadSystemColors ( table, defaultSystemColors, isNativeLookAndFeel () );
    }

    /**
     * Initializes WebLookAndFeel defaults (like default renderers, component borders and such).
     * This method will be called only in case WebLookAndFeel is installed through UIManager as current application LookAndFeel.
     *
     * @param table UI defaults table
     */
    @Override
    protected void initComponentDefaults ( final UIDefaults table )
    {
        super.initComponentDefaults ( table );

        // Global text antialiasing
        ProprietaryUtils.setupAATextInfo ( table );

        // Fonts
        initializeFonts ( table );

        // Button mnemonics display
        table.put ( "Button.showMnemonics", Boolean.TRUE );
        // Button should become default in frame
        table.put ( "Button.defaultButtonFollowsFocus", Boolean.FALSE );

        // Split button
        table.put ( "SplitButton.defaultButtonFollowsFocus", Boolean.FALSE );
        table.put ( "SplitButton.focusInputMap", new UIDefaults.LazyInputMap ( new Object[]{
                "SPACE", "pressed",
                "released SPACE", "released",
                "ENTER", "pressed",
                "released ENTER", "released"
        } ) );

        // Tristate checkbox
        table.put ( "TristateCheckBox.focusInputMap", new UIDefaults.LazyInputMap ( new Object[]{
                "SPACE", "pressed",
                "released SPACE", "released"
        } ) );

        // Split pane
        table.put ( "SplitPane.dividerSize", 2 );

        // Option pane
        table.put ( "OptionPane.isYesLast", SystemUtils.isMac () ? Boolean.TRUE : Boolean.FALSE );

        // HTML image icons
        table.put ( "html.pendingImage", Icons.hourglass );
        table.put ( "html.missingImage", Icons.broken );

        // Tree icons
        table.put ( "Tree.closedIcon", Icons.folder );
        table.put ( "Tree.openIcon", Icons.folderOpen );
        table.put ( "Tree.leafIcon", Icons.leaf );
        table.put ( "Tree.collapsedIcon", Icons.squarePlus );
        table.put ( "Tree.expandedIcon", Icons.squareMinus );
        // Tree default selection style
        table.put ( "Tree.textForeground", new ColorUIResource ( Color.BLACK ) );
        table.put ( "Tree.textBackground", new ColorUIResource ( new Color ( 255, 255, 255, 0 ) ) );
        table.put ( "Tree.selectionForeground", new ColorUIResource ( Color.BLACK ) );
        table.put ( "Tree.selectionBackground", new ColorUIResource ( new Color ( 255, 255, 255, 0 ) ) );
        table.put ( "Tree.selectionBorderColor", new ColorUIResource ( new Color ( 255, 255, 255, 0 ) ) );
        table.put ( "Tree.dropCellBackground", new ColorUIResource ( new Color ( 255, 255, 255, 0 ) ) );
        // Tree default renderer content margins
        table.put ( "Tree.rendererFillBackground", Boolean.FALSE );
        table.put ( "Tree.drawsFocusBorderAroundIcon", Boolean.FALSE );
        table.put ( "Tree.drawDashedFocusIndicator", Boolean.FALSE );
        // Tree lines indent
        table.put ( "Tree.leftChildIndent", 12 );
        table.put ( "Tree.rightChildIndent", 12 );
        table.put ( "Tree.lineTypeDashed", Boolean.TRUE );

        // Menu expand spacing
        // Up-down menu expand
        table.put ( "Menu.menuPopupOffsetX", 0 );
        table.put ( "Menu.menuPopupOffsetY", 0 );
        // Left-right menu expand
        table.put ( "Menu.submenuPopupOffsetX", 0 );
        table.put ( "Menu.submenuPopupOffsetY", 0 );

        // OptionPane
        table.put ( "OptionPane.buttonClickThreshold", 500 );

        // Table defaults
        table.put ( "Table.cellNoFocusBorder", new WebBorder ( 1, 1, 1, 1 ) );
        table.put ( "Table.focusSelectedCellHighlightBorder", new WebBorder ( 1, 1, 1, 1 ) );
        table.put ( "Table.focusCellHighlightBorder", new WebBorder ( 1, 1, 1, 1 ) );
        table.put ( "Table.scrollPaneBorder", null );
        // Table header defaults
        table.put ( "TableHeader.cellBorder", new WebBorder ( 0, 10, 1, 10 ) );
        table.put ( "TableHeader.focusCellBorder", new WebBorder ( 0, 10, 1, 10 ) );

        // Default list renderer
        table.put ( "List.cellRenderer", new UIDefaults.ActiveValue ()
        {
            @Override
            public Object createValue ( final UIDefaults table )
            {
                return new WebListCellRenderer.UIResource<Object, JList, ListCellParameters<Object, JList>> ();
            }
        } );

        // ComboBox selection foregrounds
        table.put ( "ComboBox.selectionForeground", new ColorUIResource ( Color.BLACK ) );
        // ComboBox non-square arrow
        table.put ( "ComboBox.squareButton", false );
        // ComboBox empty padding
        table.put ( "ComboBox.padding", null );

        // Default components borders
        table.put ( "ProgressBar.border", null );
        table.put ( "Button.border", null );

        // TextField actions
        table.put ( "TextField.focusInputMap", new UIDefaults.LazyInputMap ( new Object[]{
                "control C", DefaultEditorKit.copyAction,
                "control V", DefaultEditorKit.pasteAction,
                "control X", DefaultEditorKit.cutAction,
                "COPY", DefaultEditorKit.copyAction,
                "PASTE", DefaultEditorKit.pasteAction,
                "CUT", DefaultEditorKit.cutAction,
                "control INSERT", DefaultEditorKit.copyAction,
                "shift INSERT", DefaultEditorKit.pasteAction,
                "shift DELETE", DefaultEditorKit.cutAction,
                "control A", DefaultEditorKit.selectAllAction,
                "control BACK_SLASH", "unselect" /*DefaultEditorKit.unselectAction*/,
                "shift LEFT", DefaultEditorKit.selectionBackwardAction,
                "shift RIGHT", DefaultEditorKit.selectionForwardAction,
                "control LEFT", DefaultEditorKit.previousWordAction,
                "control RIGHT", DefaultEditorKit.nextWordAction,
                "control shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
                "control shift RIGHT", DefaultEditorKit.selectionNextWordAction,
                "HOME", DefaultEditorKit.beginAction,
                "END", DefaultEditorKit.endAction,
                "shift HOME", DefaultEditorKit.selectionBeginLineAction,
                "shift END", DefaultEditorKit.selectionEndLineAction,
                "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                "ctrl H", DefaultEditorKit.deletePrevCharAction,
                "DELETE", DefaultEditorKit.deleteNextCharAction,
                "ctrl DELETE", DefaultEditorKit.deleteNextWordAction,
                "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction,
                "RIGHT", DefaultEditorKit.forwardAction,
                "LEFT", DefaultEditorKit.backwardAction,
                "KP_RIGHT", DefaultEditorKit.forwardAction,
                "KP_LEFT", DefaultEditorKit.backwardAction,
                "ENTER", JTextField.notifyAction,
                "control shift O", "toggle-componentOrientation" /*DefaultEditorKit.toggleComponentOrientation*/
        } ) );

        // PasswordField actions
        table.put ( "PasswordField.focusInputMap", new UIDefaults.LazyInputMap ( new Object[]{
                "control C", DefaultEditorKit.copyAction,
                "control V", DefaultEditorKit.pasteAction,
                "control X", DefaultEditorKit.cutAction,
                "COPY", DefaultEditorKit.copyAction,
                "PASTE", DefaultEditorKit.pasteAction,
                "CUT", DefaultEditorKit.cutAction,
                "control INSERT", DefaultEditorKit.copyAction,
                "shift INSERT", DefaultEditorKit.pasteAction,
                "shift DELETE", DefaultEditorKit.cutAction,
                "control A", DefaultEditorKit.selectAllAction,
                "control BACK_SLASH", "unselect" /*DefaultEditorKit.unselectAction*/,
                "shift LEFT", DefaultEditorKit.selectionBackwardAction,
                "shift RIGHT", DefaultEditorKit.selectionForwardAction,
                "control LEFT", DefaultEditorKit.beginLineAction,
                "control RIGHT", DefaultEditorKit.endLineAction,
                "control shift LEFT", DefaultEditorKit.selectionBeginLineAction,
                "control shift RIGHT", DefaultEditorKit.selectionEndLineAction,
                "HOME", DefaultEditorKit.beginAction,
                "END", DefaultEditorKit.endAction,
                "shift HOME", DefaultEditorKit.selectionBeginLineAction,
                "shift END", DefaultEditorKit.selectionEndLineAction,
                "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                "ctrl H", DefaultEditorKit.deletePrevCharAction,
                "DELETE", DefaultEditorKit.deleteNextCharAction,
                "RIGHT", DefaultEditorKit.forwardAction,
                "LEFT", DefaultEditorKit.backwardAction,
                "KP_RIGHT", DefaultEditorKit.forwardAction,
                "KP_LEFT", DefaultEditorKit.backwardAction,
                "ENTER", JTextField.notifyAction,
                "control shift O", "toggle-componentOrientation" /*DefaultEditorKit.toggleComponentOrientation*/
        } ) );

        // FormattedTextField actions
        table.put ( "FormattedTextField.focusInputMap", new UIDefaults.LazyInputMap ( new Object[]{
                "ctrl C", DefaultEditorKit.copyAction,
                "ctrl V", DefaultEditorKit.pasteAction,
                "ctrl X", DefaultEditorKit.cutAction,
                "COPY", DefaultEditorKit.copyAction,
                "PASTE", DefaultEditorKit.pasteAction,
                "CUT", DefaultEditorKit.cutAction,
                "control INSERT", DefaultEditorKit.copyAction,
                "shift INSERT", DefaultEditorKit.pasteAction,
                "shift DELETE", DefaultEditorKit.cutAction,
                "shift LEFT", DefaultEditorKit.selectionBackwardAction,
                "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
                "shift RIGHT", DefaultEditorKit.selectionForwardAction,
                "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,
                "ctrl LEFT", DefaultEditorKit.previousWordAction,
                "ctrl KP_LEFT", DefaultEditorKit.previousWordAction,
                "ctrl RIGHT", DefaultEditorKit.nextWordAction,
                "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction,
                "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
                "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction,
                "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction,
                "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction,
                "ctrl A", DefaultEditorKit.selectAllAction,
                "HOME", DefaultEditorKit.beginAction,
                "END", DefaultEditorKit.endAction,
                "shift HOME", DefaultEditorKit.selectionBeginLineAction,
                "shift END", DefaultEditorKit.selectionEndLineAction,
                "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                "ctrl H", DefaultEditorKit.deletePrevCharAction,
                "DELETE", DefaultEditorKit.deleteNextCharAction,
                "ctrl DELETE", DefaultEditorKit.deleteNextWordAction,
                "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction,
                "RIGHT", DefaultEditorKit.forwardAction,
                "LEFT", DefaultEditorKit.backwardAction,
                "KP_RIGHT", DefaultEditorKit.forwardAction,
                "KP_LEFT", DefaultEditorKit.backwardAction,
                "ENTER", JTextField.notifyAction,
                "ctrl BACK_SLASH", "unselect",
                "control shift O", "toggle-componentOrientation",
                "ESCAPE", "reset-field-edit",
                "UP", "increment",
                "KP_UP", "increment",
                "DOWN", "decrement",
                "KP_DOWN", "decrement",
        } ) );

        // TextAreas actions
        final Object multilineInputMap = new UIDefaults.LazyInputMap ( new Object[]{
                "control C", DefaultEditorKit.copyAction,
                "control V", DefaultEditorKit.pasteAction,
                "control X", DefaultEditorKit.cutAction,
                "COPY", DefaultEditorKit.copyAction,
                "PASTE", DefaultEditorKit.pasteAction,
                "CUT", DefaultEditorKit.cutAction,
                "control INSERT", DefaultEditorKit.copyAction,
                "shift INSERT", DefaultEditorKit.pasteAction,
                "shift DELETE", DefaultEditorKit.cutAction,
                "shift LEFT", DefaultEditorKit.selectionBackwardAction,
                "shift RIGHT", DefaultEditorKit.selectionForwardAction,
                "control LEFT", DefaultEditorKit.previousWordAction,
                "control RIGHT", DefaultEditorKit.nextWordAction,
                "control shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
                "control shift RIGHT", DefaultEditorKit.selectionNextWordAction,
                "control A", DefaultEditorKit.selectAllAction,
                "control BACK_SLASH", "unselect" /*DefaultEditorKit.unselectAction*/,
                "HOME", DefaultEditorKit.beginLineAction,
                "END", DefaultEditorKit.endLineAction,
                "shift HOME", DefaultEditorKit.selectionBeginLineAction,
                "shift END", DefaultEditorKit.selectionEndLineAction,
                "control HOME", DefaultEditorKit.beginAction,
                "control END", DefaultEditorKit.endAction,
                "control shift HOME", DefaultEditorKit.selectionBeginAction,
                "control shift END", DefaultEditorKit.selectionEndAction,
                "UP", DefaultEditorKit.upAction,
                "DOWN", DefaultEditorKit.downAction,
                "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                "ctrl H", DefaultEditorKit.deletePrevCharAction,
                "DELETE", DefaultEditorKit.deleteNextCharAction,
                "ctrl DELETE", DefaultEditorKit.deleteNextWordAction,
                "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction,
                "RIGHT", DefaultEditorKit.forwardAction,
                "LEFT", DefaultEditorKit.backwardAction,
                "KP_RIGHT", DefaultEditorKit.forwardAction,
                "KP_LEFT", DefaultEditorKit.backwardAction,
                "PAGE_UP", DefaultEditorKit.pageUpAction,
                "PAGE_DOWN", DefaultEditorKit.pageDownAction,
                "shift PAGE_UP", "selection-page-up",
                "shift PAGE_DOWN", "selection-page-down",
                "ctrl shift PAGE_UP", "selection-page-left",
                "ctrl shift PAGE_DOWN", "selection-page-right",
                "shift UP", DefaultEditorKit.selectionUpAction,
                "shift DOWN", DefaultEditorKit.selectionDownAction,
                "ENTER", DefaultEditorKit.insertBreakAction,
                "TAB", DefaultEditorKit.insertTabAction,
                "control T", "next-link-action",
                "control shift T", "previous-link-action",
                "control SPACE", "activate-link-action",
                "control shift O", "toggle-componentOrientation" /*DefaultEditorKit.toggleComponentOrientation*/
        } );
        table.put ( "TextArea.focusInputMap", multilineInputMap );
        table.put ( "TextPane.focusInputMap", multilineInputMap );
        table.put ( "EditorPane.focusInputMap", multilineInputMap );

        // Slider on-focus actions
        table.put ( "Slider.focusInputMap", new UIDefaults.LazyInputMap ( new Object[]{
                "RIGHT", "positiveUnitIncrement",
                "KP_RIGHT", "positiveUnitIncrement",
                "DOWN", "negativeUnitIncrement",
                "KP_DOWN", "negativeUnitIncrement",
                "PAGE_DOWN", "negativeBlockIncrement",
                "ctrl PAGE_DOWN", "negativeBlockIncrement",
                "LEFT", "negativeUnitIncrement",
                "KP_LEFT", "negativeUnitIncrement",
                "UP", "positiveUnitIncrement",
                "KP_UP", "positiveUnitIncrement",
                "PAGE_UP", "positiveBlockIncrement",
                "ctrl PAGE_UP", "positiveBlockIncrement",
                "HOME", "minScroll",
                "END", "maxScroll"
        } ) );

        // ComboBox actions
        table.put ( "ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap ( new Object[]{
                "ESCAPE", "hidePopup",
                "PAGE_UP", "pageUpPassThrough",
                "PAGE_DOWN", "pageDownPassThrough",
                "HOME", "homePassThrough",
                "END", "endPassThrough",
                "DOWN", "selectNext",
                "KP_DOWN", "selectNext",
                "alt DOWN", "togglePopup",
                "alt KP_DOWN", "togglePopup",
                "alt UP", "togglePopup",
                "alt KP_UP", "togglePopup",
                "SPACE", "spacePopup",
                "ENTER", "enterPressed",
                "UP", "selectPrevious",
                "KP_UP", "selectPrevious"
        } ) );

        // FileChooser actions
        table.put ( "FileChooser.ancestorInputMap", new UIDefaults.LazyInputMap ( new Object[]{
                "ESCAPE", "cancelSelection",
                "F2", "editFileName",
                "F5", "refresh",
                "BACK_SPACE", "Go Up",
                "ENTER", "approveSelection",
                "ctrl ENTER", "approveSelection"
        } ) );
    }

    /**
     * Initializes all default component fonts.
     *
     * @param table UIDefaults table
     */
    protected static void initializeFonts ( final UIDefaults table )
    {
        /**
         * @see ControlType#CONTROL
         */
        initializeFont ( table, "Canvas.font", canvasFont, globalControlFont );
        initializeFont ( table, "Image.font", imageFont, globalControlFont );
        initializeFont ( table, "Button.font", buttonFont, globalControlFont );
        initializeFont ( table, "SplitButton.font", splitButtonFont, globalControlFont );
        initializeFont ( table, "ToggleButton.font", toggleButtonFont, globalControlFont );
        initializeFont ( table, "CheckBox.font", checkBoxFont, globalControlFont );
        initializeFont ( table, "TristateCheckBox.font", tristateCheckBoxFont, globalControlFont );
        initializeFont ( table, "RadioButton.font", radioButtonFont, globalControlFont );
        initializeFont ( table, "ComboBox.font", comboBoxFont, globalControlFont );
        initializeFont ( table, "Spinner.font", spinnerFont, globalControlFont );
        initializeFont ( table, "TextField.font", textFieldFont, globalControlFont );
        initializeFont ( table, "FormattedTextField.font", formattedTextFieldFont, globalControlFont );
        initializeFont ( table, "PasswordField.font", passwordFieldFont, globalControlFont );
        initializeFont ( table, "ColorChooser.font", colorChooserFont, globalControlFont );
        initializeFont ( table, "FileChooser.font", fileChooserFont, globalControlFont );
        initializeFont ( table, "Label.font", labelFont, globalControlFont );
        initializeFont ( table, "StyledLabel.font", styledLabelFont, globalControlFont );
        initializeFont ( table, "Link.font", linkFont, globalControlFont );
        initializeFont ( table, "List.font", listFont, globalControlFont );
        initializeFont ( table, "Panel.font", panelFont, globalControlFont );
        initializeFont ( table, "Popup.font", popupFont, globalControlFont );
        initializeFont ( table, "ProgressBar.font", progressBarFont, globalControlFont );
        initializeFont ( table, "ScrollPane.font", scrollPaneFont, globalControlFont );
        initializeFont ( table, "Viewport.font", viewportFont, globalControlFont );
        initializeFont ( table, "Slider.font", sliderFont, globalControlFont );
        initializeFont ( table, "TabbedPane.font", tabbedPaneFont, globalControlFont );
        initializeFont ( table, "Table.font", tableFont, globalControlFont );
        initializeFont ( table, "TableHeader.font", tableHeaderFont, globalControlFont );
        initializeFont ( table, "TitledBorder.font", titledBorderFont, globalControlFont );
        initializeFont ( table, "Tree.font", treeFont, globalControlFont );

        /**
         * @see ControlType#TEXT
         */
        initializeFont ( table, "TextArea.font", textAreaFont, globalTextFont );
        initializeFont ( table, "TextPane.font", textPaneFont, globalTextFont );
        initializeFont ( table, "EditorPane.font", editorPaneFont, globalTextFont );

        /**
         * @see ControlType#TOOLTIP
         */
        initializeFont ( table, "ToolTip.font", toolTipFont, globalTooltipFont );

        /**
         * @see ControlType#MENU
         */
        initializeFont ( table, "PopupMenu.font", popupMenuFont, globalMenuFont );
        initializeFont ( table, "MenuBar.font", menuBarFont, globalMenuFont );
        initializeFont ( table, "Menu.font", menuFont, globalMenuFont );
        initializeFont ( table, "MenuItem.font", menuItemFont, globalMenuFont );
        initializeFont ( table, "RadioButtonMenuItem.font", radioButtonMenuItemFont, globalMenuFont );
        initializeFont ( table, "CheckBoxMenuItem.font", checkBoxMenuItemFont, globalMenuFont );
        initializeFont ( table, "ToolBar.font", toolBarFont, globalMenuFont );

        /**
         * @see ControlType#MENU_SMALL
         */
        initializeFont ( table, "Menu.acceleratorFont", menuAcceleratorFont, globalMenuSmallFont );
        initializeFont ( table, "MenuItem.acceleratorFont", menuItemAcceleratorFont, globalMenuSmallFont );
        initializeFont ( table, "RadioButtonMenuItem.acceleratorFont", radioButtonMenuItemAcceleratorFont, globalMenuSmallFont );
        initializeFont ( table, "CheckBoxMenuItem.acceleratorFont", checkBoxMenuItemAcceleratorFont, globalMenuSmallFont );

        /**
         * @see ControlType#WINDOW
         */
        initializeFont ( table, "InternalFrame.titleFont", internalFrameFont, globalWindowFont );

        /**
         * @see ControlType#MESSAGE
         */
        initializeFont ( table, "OptionPane.font", optionPaneFont, globalMessageFont );
    }

    /**
     * Initializes single component font.
     *
     * @param table      UIDefaults table
     * @param key        component font key
     * @param font       custom font
     * @param globalFont global font
     */
    protected static void initializeFont ( final UIDefaults table, final String key, final Font font, final Font globalFont )
    {
        table.put ( key, createLazyFont ( font != null ? font : globalFont ) );
    }

    /**
     * Returns SwingLazyValue for specified font.
     *
     * @param font font
     * @return SwingLazyValue for specified font
     */
    protected static SwingLazyValue createLazyFont ( final Font font )
    {
        return new SwingLazyValue ( "javax.swing.plaf.FontUIResource", null, new Object[]{ font } );
    }

    /**
     * Hides or displays button mnemonics.
     *
     * @param hide whether hide button mnemonics or not
     */
    public static void setMnemonicHidden ( final boolean hide )
    {
        isMnemonicHidden = !UIManager.getBoolean ( "Button.showMnemonics" ) && hide;
    }

    /**
     * Returns whether button mnemonics are hidden or not.
     *
     * @return true if button mnemonics are hidden, false otherwise
     */
    public static boolean isMnemonicHidden ()
    {
        if ( UIManager.getBoolean ( "Button.showMnemonics" ) )
        {
            isMnemonicHidden = false;
        }
        return isMnemonicHidden;
    }

    /**
     * Returns whether or not component's custom {@link Shape}s are used for better mouse events detection.
     *
     * @return {@code true} if component's custom {@link Shape}s are used for better mouse events detection, {@code false} otherwise
     */
    public static boolean isShapeDetectionEnabled ()
    {
        return shapeDetectionEnabled;
    }

    /**
     * Sets whether or not component's custom {@link Shape}s should be used for better mouse events detection.
     *
     * @param enabled whether or not component's custom {@link Shape}s should be used for better mouse events detection
     */
    public static void setShapeDetectionEnabled ( final boolean enabled )
    {
        WebLookAndFeel.shapeDetectionEnabled = enabled;
    }

    /**
     * Initializes library managers separately.
     */
    public static void initializeManagers ()
    {
        UIManagers.initialize ();
    }

    /**
     * Installs {@link WebLookAndFeel} in one call.
     *
     * @throws LookAndFeelException when unable to install {@link WebLookAndFeel}
     */
    public static void install () throws LookAndFeelException
    {
        install ( WebSkin.class );
    }

    /**
     * Installs {@link WebLookAndFeel} in one call.
     *
     * @param skin      {@link Skin} class
     * @param arguments {@link Skin} constructor arguments
     * @throws LookAndFeelException when unable to install {@link WebLookAndFeel}
     */
    public static void install ( final Class<? extends Skin> skin, final Object... arguments ) throws LookAndFeelException
    {
        install ( new LazyInstance<Skin> ( skin, arguments ) );
    }

    /**
     * Installs {@link WebLookAndFeel} in one call.
     *
     * @param skin {@link LazyInstance} for {@link Skin}
     * @throws LookAndFeelException when unable to install {@link WebLookAndFeel}
     */
    public static void install ( final LazyInstance<? extends Skin> skin ) throws LookAndFeelException
    {
        // Event Dispatch Thread check
        checkEventDispatchThread ();

        // Saving previous installed LaF class
        previousLookAndFeelClass = UIManager.getLookAndFeel ().getClass ();

        // Preparing initial skin
        StyleManager.setDefaultSkin ( skin );

        // Installing LookAndFeel
        LafUtils.setupLookAndFeel ( WebLookAndFeel.class );
    }

    /**
     * Returns whether {@link WebLookAndFeel} is installed or not.
     *
     * @return {@code true} if WebLookAndFeel is installed, {@code false} otherwise
     */
    public static boolean isInstalled ()
    {
        return WebLookAndFeel.class.isInstance ( UIManager.getLookAndFeel () );
    }

    /**
     * Restores previously installed {@link LookAndFeel}.
     *
     * @throws LookAndFeelException if {@link WebLookAndFeel} is not installed or there was no previously installed {@link LookAndFeel}
     */
    public static void uninstall () throws LookAndFeelException
    {
        // Event Dispatch Thread check
        checkEventDispatchThread ();

        // Trying to perform uninstall
        if ( isInstalled () )
        {
            if ( previousLookAndFeelClass != null )
            {
                // Installing previous LookAndFeel
                LafUtils.setupLookAndFeel ( previousLookAndFeelClass );

                // Cleaning up previous LaF whatever the result is
                previousLookAndFeelClass = null;
            }
            else
            {
                // Wrong order of calls, we need to inform about it
                throw new LookAndFeelException ( "There was no previously installed LaF" );
            }
        }
        else
        {
            // Wrong order of calls, we need to inform about it
            throw new LookAndFeelException ( "WebLookAndFeel was not installed yet" );
        }
    }

    /**
     * Whether or not library should force Event Dispatch Thread usage for all UI-related operations.
     *
     * @return {@code true} if library should force Event Dispatch Thread usage for all UI-related operations, {@code false} otherwise
     */
    public static boolean isForceSingleEventsThread ()
    {
        return forceSingleEventsThread;
    }

    /**
     * Sets whether or not library should force Event Dispatch Thread usage for all UI-related operations.
     *
     * @param enforce whether or not library should force Event Dispatch Thread usage for all UI-related operations
     */
    public static void setForceSingleEventsThread ( final boolean enforce )
    {
        WebLookAndFeel.forceSingleEventsThread = enforce;
    }

    /**
     * Returns whether or not library should enforce Event Dispatch Thread usage for {@link Component} events.
     *
     * @return {@code true} if library should enforce Event Dispatch Thread usage for {@link Component} events, {@code false} otherwise
     */
    public static boolean isUseStrictEventThreadListeners ()
    {
        return useStrictEventThreadListeners;
    }

    /**
     * Sets whether or not library should enforce Event Dispatch Thread usage for {@link Component} events.
     *
     * @param strict hether or not library should enforce Event Dispatch Thread usage for {@link Component} events
     */
    public static void setUseStrictEventThreadListeners ( final boolean strict )
    {
        WebLookAndFeel.useStrictEventThreadListeners = strict;
    }

    /**
     * Returns special handler for exceptions thrown when any UI operation is executed outside of the Event Dispatch Thread.
     *
     * @return special handler for exceptions thrown when any UI operation is executed outside of the Event Dispatch Thread.
     */
    public static NonEventThreadHandler getNonEventThreadHandler ()
    {
        return nonEventThreadHandler;
    }

    /**
     * Sets special handler for exceptions thrown when any UI operation is executed outside of the Event Dispatch Thread..
     *
     * @param handler special handler for exceptions thrown when any UI operation is executed outside of the Event Dispatch Thread.
     */
    public static void setNonEventThreadHandler ( final NonEventThreadHandler handler )
    {
        WebLookAndFeel.nonEventThreadHandler = handler;
    }

    /**
     * Perform Event Dispatch Thread usage check if required.
     */
    public static void checkEventDispatchThread ()
    {
        if ( isForceSingleEventsThread () && !CoreSwingUtils.isEventDispatchThread () )
        {
            final NonEventThreadHandler handler = getNonEventThreadHandler ();
            if ( handler != null )
            {
                final String msg = "This operation is only permitted on the Event Dispatch Thread. Current thread is: %s";
                handler.handle ( new LookAndFeelException ( String.format ( msg, Thread.currentThread ().getName () ) ) );
            }
        }
    }

    /**
     * Installs listeners checking Event Dispatch Thread into the specified {@link Component}.
     *
     * @param component {@link Component}
     */
    public static void installEventDispatchThreadCheckers ( final Component component )
    {
        WebLookAndFeel.checkEventDispatchThread ();
        if ( isForceSingleEventsThread () && isUseStrictEventThreadListeners () )
        {
            component.addHierarchyListener ( STRICT_EDT_LISTENERS );
            component.addPropertyChangeListener ( STRICT_EDT_LISTENERS );
            component.addComponentListener ( STRICT_EDT_LISTENERS );
            component.addMouseListener ( STRICT_EDT_LISTENERS );
            component.addMouseWheelListener ( STRICT_EDT_LISTENERS );
            component.addMouseMotionListener ( STRICT_EDT_LISTENERS );
            component.addKeyListener ( STRICT_EDT_LISTENERS );
            component.addFocusListener ( STRICT_EDT_LISTENERS );
        }
    }

    /**
     * Uninstalls listeners checking Event Dispatch Thread from the specified {@link Component}.
     *
     * @param component {@link Component}
     */
    public static void uninstallEventDispatchThreadCheckers ( final Component component )
    {
        WebLookAndFeel.checkEventDispatchThread ();
        if ( isForceSingleEventsThread () && isUseStrictEventThreadListeners () )
        {
            component.removeFocusListener ( STRICT_EDT_LISTENERS );
            component.removeKeyListener ( STRICT_EDT_LISTENERS );
            component.removeMouseMotionListener ( STRICT_EDT_LISTENERS );
            component.removeMouseWheelListener ( STRICT_EDT_LISTENERS );
            component.removeMouseListener ( STRICT_EDT_LISTENERS );
            component.removeComponentListener ( STRICT_EDT_LISTENERS );
            component.removePropertyChangeListener ( STRICT_EDT_LISTENERS );
            component.removeHierarchyListener ( STRICT_EDT_LISTENERS );
        }
    }

    /**
     * Returns a list of square WebLookAndFeel images that can be used as window icons on any OS.
     *
     * @return list of square WebLookAndFeel images
     */
    public static List<Image> getImages ()
    {
        loadIcons ();
        return ImageUtils.toImagesList ( icons );
    }

    /**
     * Returns a list of square WebLookAndFeel icons that can be used as window icons on any OS.
     *
     * @return list of square WebLookAndFeel icons
     */
    public static List<ImageIcon> getIcons ()
    {
        loadIcons ();
        return CollectionUtils.copy ( icons );
    }

    /**
     * Returns square WebLookAndFeel image of specified size.
     *
     * @param size square WebLookAndFeel image size
     * @return square WebLookAndFeel image
     */
    public static Image getImage ( final int size )
    {
        return getIcon ( size ).getImage ();
    }

    /**
     * Returns square WebLookAndFeel icon of specified size.
     *
     * @param size square WebLookAndFeel icon size
     * @return square WebLookAndFeel icon
     */
    public static ImageIcon getIcon ( final int size )
    {
        loadIcons ();
        for ( final ImageIcon icon : icons )
        {
            if ( icon.getIconWidth () == size )
            {
                return icon;
            }
        }
        return null;
    }

    /**
     * Loads square WebLookAndFeel icons listed in icons.xml file in resources folder.
     * Loaded icons will have the same order they have in that xml file.
     */
    protected static void loadIcons ()
    {
        if ( icons == null )
        {
            final int[] sizes = { 16, 24, 32, 48, 64, 128, 256, 512 };
            icons = new ArrayList<ImageIcon> ( sizes.length );
            for ( final int size : sizes )
            {
                icons.add ( new ImageIcon ( WebLookAndFeel.class.getResource ( "icons/icon" + size + ".png" ) ) );
            }
        }
    }

    /**
     * Returns a better disabled icon than BasicLookAndFeel offers.
     * Generated disabled icons are cached within a weak hash map under icon key.
     *
     * @param component component that requests disabled icon
     * @param icon      normal icon
     * @return disabled icon
     */
    @Override
    public Icon getDisabledIcon ( final JComponent component, final Icon icon )
    {
        if ( icon != null && icon.getIconWidth () > 0 && icon.getIconHeight () > 0 )
        {
            if ( disabledIcons.containsKey ( icon ) )
            {
                return disabledIcons.get ( icon );
            }
            else
            {
                final ImageIcon disabledIcon;
                if ( icon instanceof ImageIcon || icon instanceof SvgIcon || icon instanceof GifIcon || icon instanceof LazyIcon )
                {
                    // todo Different disabled implementation for different icon types?
                    // todo For example ImageIcon, SvgIcon, GifIcon etc.
                    final BufferedImage image = ImageUtils.getBufferedImage ( icon );
                    final BufferedImage disabled = ImageUtils.createDisabledCopy ( image );
                    disabledIcon = new ImageIcon ( disabled );
                }
                else
                {
                    disabledIcon = null;
                }
                disabledIcons.put ( icon, disabledIcon );
                return disabledIcon;
            }
        }
        else
        {
            return icon;
        }
    }

    /**
     * Forces global components UI update in all existing application windows.
     */
    public static void updateAllComponentUIs ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Updating component UIs
        for ( final Window window : Window.getWindows () )
        {
            SwingUtilities.updateComponentTreeUI ( window );
        }
    }

    /**
     * Returns custom WebLookAndFeel layout style.
     *
     * @return custom WebLookAndFeel layout style
     */
    @Override
    public LayoutStyle getLayoutStyle ()
    {
        return WebLayoutStyle.getInstance ();
    }

    /**
     * Returns whether per-pixel transparent windows usage is allowed on Linux systems or not.
     *
     * @return true if per-pixel transparent windows usage is allowed on Linux systems, false otherwise
     */
    public static boolean isAllowLinuxTransparency ()
    {
        return ProprietaryUtils.isAllowLinuxTransparency ();
    }

    /**
     * Sets whether per-pixel transparent windows usage is allowed on Linux systems or not.
     * This might be an unstable feature so it is disabled by default. Use it at your own risk.
     *
     * @param allow whether per-pixel transparent windows usage is allowed on Linux systems or not
     */
    public static void setAllowLinuxTransparency ( final boolean allow )
    {
        ProprietaryUtils.setAllowLinuxTransparency ( allow );
    }

    /**
     * Returns whether LTR is current global component orientation or not.
     *
     * @return true if LTR is current global component orientation, false otherwise
     */
    public static boolean isLeftToRight ()
    {
        return getOrientation ().isLeftToRight ();
    }

    /**
     * Returns current global component orientation.
     *
     * @return current global component orientation
     */
    public static ComponentOrientation getOrientation ()
    {
        if ( orientation != null )
        {
            return orientation;
        }
        else
        {
            return ComponentOrientation.getOrientation ( Locale.getDefault () );
        }
    }

    /**
     * Returns orientation opposite to current global component orientation.
     *
     * @return orientation opposite to current global component orientation
     */
    public static ComponentOrientation getOppositeOrientation ()
    {
        return isLeftToRight () ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT;
    }

    /**
     * Sets current global component orientation.
     *
     * @param leftToRight whether should set LTR orientation or RTL one
     */
    public static void setOrientation ( final boolean leftToRight )
    {
        setOrientation ( leftToRight ? ComponentOrientation.LEFT_TO_RIGHT : ComponentOrientation.RIGHT_TO_LEFT );
    }

    /**
     * Sets current global component orientation.
     *
     * @param orientation new global component orientation
     */
    public static void setOrientation ( final ComponentOrientation orientation )
    {
        WebLookAndFeel.orientation = orientation;
        SwingUtils.updateGlobalOrientation ();
    }

    /**
     * Changes current global component orientation to opposite one.
     */
    public static void changeOrientation ()
    {
        setOrientation ( !getOrientation ().isLeftToRight () );
    }

    /**
     * Adds global {@link VisibleWindowListener}.
     *
     * @param listener {@link VisibleWindowListener}
     */
    public static void addVisibleWindowListener ( final VisibleWindowListener listener )
    {
        globalListeners.add ( VisibleWindowListener.class, listener );
    }

    /**
     * Removes global {@link VisibleWindowListener}.
     *
     * @param listener {@link VisibleWindowListener}
     */
    public static void removeVisibleWindowListener ( final VisibleWindowListener listener )
    {
        globalListeners.remove ( VisibleWindowListener.class, listener );
    }

    /**
     * Adds global {@link VisibleWindowListener}.
     *
     * @param component {@link JComponent} using the listener
     * @param listener  {@link VisibleWindowListener}
     */
    public static void addVisibleWindowListener ( final JComponent component, final VisibleWindowListener listener )
    {
        visibleWindowListeners.add ( component, listener );
    }

    /**
     * Removes global {@link VisibleWindowListener}.
     *
     * @param component {@link JComponent} using the listener
     * @param listener  {@link VisibleWindowListener}
     */
    public static void removeVisibleWindowListener ( final JComponent component, final VisibleWindowListener listener )
    {
        visibleWindowListeners.remove ( component, listener );
    }

    /**
     * Inform about {@link Window} becoming visible.
     *
     * @param window {@link Window}
     */
    public static void fireWindowDisplayed ( final Window window )
    {
        for ( final VisibleWindowListener listener : globalListeners.getListeners ( VisibleWindowListener.class ) )
        {
            listener.windowDisplayed ( window );
        }
        visibleWindowListeners.forEachData ( new BiConsumer<JComponent, VisibleWindowListener> ()
        {
            @Override
            public void accept ( final JComponent component, final VisibleWindowListener listener )
            {
                listener.windowDisplayed ( window );
            }
        } );
    }

    /**
     * Inform about {@link Window} becoming hidden or disposed.
     *
     * @param window {@link Window}
     */
    public static void fireWindowHidden ( final Window window )
    {
        for ( final VisibleWindowListener listener : globalListeners.getListeners ( VisibleWindowListener.class ) )
        {
            listener.windowHidden ( window );
        }
        visibleWindowListeners.forEachData ( new BiConsumer<JComponent, VisibleWindowListener> ()
        {
            @Override
            public void accept ( final JComponent component, final VisibleWindowListener listener )
            {
                listener.windowHidden ( window );
            }
        } );
    }
}