package com.alee.managers.style;

import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * This class encapsulates style ID for single component.
 * <p>
 * It also contains some style IDs for basic components.
 * Some of these styles are used by various custom complex WebLaF component parts.
 * They are provided to allow restyling those parts without affecting default component style.
 *
 * @author Mikle Garin
 */

public final class StyleId
{
    /**
     * Style ID client property key for {@link javax.swing.JComponent}.
     * You can put either {@link com.alee.managers.style.StyleId} or {@link java.lang.String} under this key.
     * It will be immediately applied to component as its current style ID if component uses WebLaF UI.
     *
     * @see com.alee.managers.style.StyleData#propertyChange(java.beans.PropertyChangeEvent)
     */
    public static final String STYLE_PROPERTY = "styleId";

    /**
     * Parent style component client property key for {@link javax.swing.JComponent}.
     * You can put either {@link javax.swing.JComponent} or {@link java.lang.ref.WeakReference} to a JComponent under this key.
     * It will be immediately applied to component as its current style parent if component uses WebLaF UI.
     *
     * @see com.alee.managers.style.StyleData#propertyChange(java.beans.PropertyChangeEvent)
     */
    public static final String PARENT_STYLE_PROPERTY = "parent";

    /**
     * Style IDs chain separator.
     */
    public static final String styleSeparator = ".";

    /**
     * {@link com.alee.laf.label.WebLabel} style IDs.
     */
    public static final StyleId label = StyleId.of ( "label" );
    public static final StyleId labelShade = StyleId.of ( "shade" );
    public static final StyleId labelTag = StyleId.of ( "tag" );
    public static final StyleId labelVertical = StyleId.of ( "vertical" );

    /**
     * {@link com.alee.extended.label.WebStyledLabel} style IDs.
     */
    public static final StyleId styledlabel = StyleId.of ( "styledlabel" );
    public static final StyleId styledlabelShade = StyleId.of ( "shade" );
    public static final StyleId styledlabelVertical = StyleId.of ( "vertical" );

    /**
     * {@link com.alee.laf.tooltip.WebToolTip} style IDs.
     */
    public static final StyleId tooltip = StyleId.of ( "tooltip" );

    /**
     * {@link com.alee.laf.button.WebButton} style IDs.
     */
    public static final StyleId button = StyleId.of ( "button" );
    public static final StyleId buttonHover = StyleId.of ( "hover" );
    public static final StyleId buttonIcon = StyleId.of ( "icon" );
    public static final StyleId buttonIconHover = StyleId.of ( "icon-hover" );

    /**
     * {@link com.alee.extended.button.WebSplitButton} style IDs.
     */
    public static final StyleId splitbutton = StyleId.of ( "splitbutton" );
    public static final StyleId splitbuttonHover = StyleId.of ( "hover" );
    public static final StyleId splitbuttonIcon = StyleId.of ( "icon" );
    public static final StyleId splitbuttonIconHover = StyleId.of ( "icon-hover" );

    /**
     * {@link com.alee.laf.button.WebToggleButton} style IDs.
     */
    public static final StyleId togglebutton = StyleId.of ( "togglebutton" );
    public static final StyleId togglebuttonHover = StyleId.of ( "hover" );
    public static final StyleId togglebuttonIcon = StyleId.of ( "icon" );
    public static final StyleId togglebuttonIconHover = StyleId.of ( "icon-hover" );

    /**
     * {@link com.alee.laf.checkbox.WebCheckBox} style IDs.
     */
    public static final StyleId checkbox = StyleId.of ( "checkbox" );

    /**
     * {@link com.alee.extended.checkbox.WebTristateCheckBox} style IDs.
     */
    public static final StyleId tristatecheckbox = StyleId.of ( "tristatecheckbox" );

    /**
     * {@link com.alee.laf.radiobutton.WebRadioButton} style IDs.
     */
    public static final StyleId radiobutton = StyleId.of ( "radiobutton" );

    /**
     * {@link com.alee.laf.separator.WebSeparator} style IDs.
     */
    public static final StyleId separator = StyleId.of ( "separator" );
    public static final StyleId separatorHorizontal = StyleId.of ( "horizontal" );
    public static final StyleId separatorVertical = StyleId.of ( "vertical" );

    /**
     * {@link com.alee.laf.menu.WebMenuBar} style IDs.
     */
    public static final StyleId menubar = StyleId.of ( "menubar" );

    /**
     * {@link com.alee.laf.menu.WebMenu} style IDs.
     */
    public static final StyleId menu = StyleId.of ( "menu" );

    /**
     * {@link com.alee.laf.menu.WebPopupMenu} style IDs.
     */
    public static final StyleId popupmenu = StyleId.of ( "popupmenu" );

    /**
     * {@link com.alee.laf.menu.WebMenuItem} style IDs.
     */
    public static final StyleId menuitem = StyleId.of ( "menuitem" );

    /**
     * {@link com.alee.laf.menu.WebCheckBoxMenuItem} style IDs.
     */
    public static final StyleId checkboxmenuitem = StyleId.of ( "checkboxmenuitem" );

    /**
     * {@link com.alee.laf.menu.WebRadioButtonMenuItem} style IDs.
     */
    public static final StyleId radiobuttonmenuitem = StyleId.of ( "radiobuttonmenuitem" );

    /**
     * {@link com.alee.laf.menu.WebPopupMenuSeparator} style IDs.
     */
    public static final StyleId popupmenuseparator = StyleId.of ( "popupmenuseparator" );

    /**
     * {@link com.alee.laf.panel.WebPanel} style IDs.
     */
    public static final StyleId panel = StyleId.of ( "panel" );
    public static final StyleId panelTransparent = StyleId.of ( "transparent" );
    public static final StyleId panelWhite = StyleId.of ( "white" );
    public static final StyleId panelDecorated = StyleId.of ( "decorated" );
    public static final StyleId panelDecoratedFocusable = StyleId.of ( "focusable" );

    /**
     * {@link com.alee.laf.rootpane.WebRootPane} style IDs.
     */
    public static final StyleId rootpane = StyleId.of ( "rootpane" );
    public static final ChildStyleId rootpaneTitlePanel = ChildStyleId.of ( "title" );
    public static final ChildStyleId rootpaneTitleIcon = ChildStyleId.of ( "icon" );
    public static final ChildStyleId rootpaneTitleLabel = ChildStyleId.of ( "title" );
    public static final ChildStyleId rootpaneButtonsPanel = ChildStyleId.of ( "buttons" );
    public static final ChildStyleId rootpaneMinimizeButton = ChildStyleId.of ( "minimize" );
    public static final ChildStyleId rootpaneMaximizeButton = ChildStyleId.of ( "maximize" );
    public static final ChildStyleId rootpaneCloseButton = ChildStyleId.of ( "close" );
    public static final ChildStyleId rootpaneContent = ChildStyleId.of ( "content" );

    /**
     * {@link com.alee.laf.rootpane.WebWindow} style IDs.
     */
    public static final StyleId window = rootpane;

    /**
     * {@link com.alee.laf.rootpane.WebFrame} style IDs.
     */
    public static final StyleId frame = StyleId.of ( "frame" );
    public static final StyleId frameDecorated = StyleId.of ( "frame-decorated" );

    /**
     * {@link com.alee.laf.rootpane.WebDialog} style IDs.
     */
    public static final StyleId dialog = StyleId.of ( "dialog" );
    public static final StyleId dialogDecorated = StyleId.of ( "dialog-decorated" );

    /**
     * {@link com.alee.laf.tabbedpane.WebTabbedPane} style IDs.
     */
    public static final StyleId tabbedpane = StyleId.of ( "tabbedpane" );
    public static final StyleId tabbedpaneAttached = StyleId.of ( "attached" );

    /**
     * {@link com.alee.laf.splitpane.WebSplitPane} style IDs.
     */
    public static final StyleId splitpane = StyleId.of ( "splitpane" );
    public static final ChildStyleId splitpaneOneTouchButton = ChildStyleId.of ( "onetouch" );
    public static final ChildStyleId splitpaneOneTouchLeftButton = ChildStyleId.of ( "onetouch-left" );
    public static final ChildStyleId splitpaneOneTouchRightButton = ChildStyleId.of ( "onetouch-right" );

    /**
     * {@link com.alee.laf.toolbar.WebToolBar} style IDs.
     */
    public static final StyleId toolbar = StyleId.of ( "toolbar" );
    public static final StyleId toolbarAttached = StyleId.of ( "attached" );

    /**
     * {@link com.alee.extended.statusbar.WebStatusBar} style IDs.
     */
    public static final StyleId statusbar = StyleId.of ( "statusbar" );

    /**
     * {@link com.alee.laf.toolbar.WebToolBarSeparator} style IDs.
     */
    public static final StyleId toolbarseparator = StyleId.of ( "toolbarseparator" );

    /**
     * {@link com.alee.laf.scroll.WebScrollBar} style IDs.
     */
    public static final StyleId scrollbar = StyleId.of ( "scrollbar" );
    public static final StyleId scrollbarUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId scrollbarButtonless = StyleId.of ( "buttonless" );
    public static final StyleId scrollbarUndecoratedButtonless = StyleId.of ( "undecorated-buttonless" );
    public static final ChildStyleId scrollbarButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId scrollbarDecreaseButton = ChildStyleId.of ( "decrease" );
    public static final ChildStyleId scrollbarIncreaseButton = ChildStyleId.of ( "increase" );

    /**
     * {@link com.alee.laf.scroll.WebScrollPane} style IDs.
     */
    public static final StyleId scrollpane = StyleId.of ( "scrollpane" );
    public static final StyleId scrollpaneUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId scrollpaneNoFocus = StyleId.of ( "nofocus" );
    public static final StyleId scrollpaneTransparent = StyleId.of ( "transparent" );
    public static final StyleId scrollpaneTransparentButtonless = StyleId.of ( "transparent-buttonless" );
    public static final StyleId scrollpanePopup = StyleId.of ( "popup" );
    public static final ChildStyleId scrollpaneViewport = ChildStyleId.of ( "viewport" );
    public static final ChildStyleId scrollpaneBar = ChildStyleId.of ( "scrollbar" );
    public static final ChildStyleId scrollpaneVerticalBar = ChildStyleId.of ( "vertical" );
    public static final ChildStyleId scrollpaneHorizontalBar = ChildStyleId.of ( "horizontal" );

    /**
     * {@link com.alee.laf.progressbar.WebProgressBar} style IDs.
     */
    public static final StyleId progressbar = StyleId.of ( "progressbar" );

    /**
     * {@link com.alee.laf.viewport.WebViewport} style IDs.
     */
    public static final StyleId viewport = StyleId.of ( "viewport" );

    /**
     * {@link com.alee.laf.text.WebTextField} style IDs.
     */
    public static final StyleId textfield = StyleId.of ( "textfield" );
    public static final StyleId textfieldUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId textfieldNoFocus = StyleId.of ( "nofocus" );

    /**
     * {@link com.alee.laf.text.WebPasswordField} style IDs.
     */
    public static final StyleId passwordfield = StyleId.of ( "passwordfield" );
    public static final StyleId passwordfieldUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId passwordfieldNoFocus = StyleId.of ( "nofocus" );

    /**
     * {@link com.alee.laf.text.WebFormattedTextField} style IDs.
     */
    public static final StyleId formattedtextfield = StyleId.of ( "formattedtextfield" );
    public static final StyleId formattedtextfieldUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId formattedtextfieldNoFocus = StyleId.of ( "nofocus" );

    /**
     * {@link com.alee.extended.filechooser.WebPathField} style IDs.
     */
    public static final StyleId pathfield = StyleId.of ( "pathfield" );
    public static final ChildStyleId pathfieldContentPanel = ChildStyleId.of ( "content" );
    public static final ChildStyleId pathfieldPathField = ChildStyleId.of ( "path-field" );
    public static final ChildStyleId pathfieldPopupScroll = ChildStyleId.of ( "scroll" );
    public static final ChildStyleId pathfieldRootButton = ChildStyleId.of ( "root" );
    public static final ChildStyleId pathfieldElementButton = ChildStyleId.of ( "element" );
    public static final ChildStyleId pathfieldMenuToggleButton = ChildStyleId.of ( "menu" );

    /**
     * {@link com.alee.extended.filechooser.WebFileChooserField} style IDs.
     */
    public static final StyleId filechooserfield = StyleId.of ( "filechooserfield" );
    public static final ChildStyleId filechooserfieldContentPanel = ChildStyleId.of ( "content" );
    public static final ChildStyleId filechooserfieldContentScroll = ChildStyleId.of ( "scroll" );
    public static final ChildStyleId filechooserfieldFilePlate = ChildStyleId.of ( "file" );
    public static final ChildStyleId filechooserfieldFileNameLabel = ChildStyleId.of ( "name" );
    public static final ChildStyleId filechooserfieldFileRemoveButton = ChildStyleId.of ( "remove" );
    public static final ChildStyleId filechooserfieldChooseButton = ChildStyleId.of ( "choose" );

    /**
     * {@link com.alee.extended.colorchooser.WebColorChooserField} style IDs.
     */
    public static final StyleId colorchooserfield = StyleId.of ( "colorchooserfield" );
    public static final ChildStyleId colorchooserfieldColorButton = ChildStyleId.of ( "choose" );

    /**
     * {@link com.alee.laf.text.WebTextArea} style IDs.
     */
    public static final StyleId textarea = StyleId.of ( "textarea" );
    public static final StyleId textareaDecorated = StyleId.of ( "decorated" );

    /**
     * {@link com.alee.laf.text.WebEditorPane} style IDs.
     */
    public static final StyleId editorpane = StyleId.of ( "editorpane" );
    public static final StyleId editorpaneDecorated = StyleId.of ( "decorated" );

    /**
     * {@link com.alee.laf.text.WebTextPane} style IDs.
     */
    public static final StyleId textpane = StyleId.of ( "textpane" );
    public static final StyleId textpaneDecorated = StyleId.of ( "decorated" );

    /**
     * {@link com.alee.laf.table.WebTableHeader} style IDs.
     */
    public static final StyleId tableheader = StyleId.of ( "tableheader" );
    public static final ChildStyleId tableheaderCellRenderer = ChildStyleId.of ( "renderer" );

    /**
     * {@link com.alee.laf.table.WebTable} style IDs.
     */
    public static final StyleId table = StyleId.of ( "table" );
    public static final ChildStyleId tableHeader = ChildStyleId.of ( "header" );
    public static final ChildStyleId tableHeaderCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId tableCorner = ChildStyleId.of ( "corner" );
    public static final ChildStyleId tableCellRenderer = ChildStyleId.of ( "cell" );
    public static final ChildStyleId tableBooleanCellRenderer = ChildStyleId.of ( "cell-boolean" );
    public static final ChildStyleId tableCellEditor = ChildStyleId.of ( "editor" );
    public static final ChildStyleId tableGenericEditor = ChildStyleId.of ( "editor-generic" );
    public static final ChildStyleId tableBooleanCellEditor = ChildStyleId.of ( "editor-boolean" );
    public static final ChildStyleId tableDateCellEditor = ChildStyleId.of ( "editor-date" );

    /**
     * {@link com.alee.extended.filechooser.WebFileTable} style IDs.
     */
    public static final StyleId filetable = StyleId.of ( "filetable" );

    /**
     * {@link com.alee.laf.slider.WebSlider} style IDs.
     */
    public static final StyleId slider = StyleId.of ( "slider" );

    /**
     * {@link com.alee.laf.spinner.WebSpinner} style IDs.
     */
    public static final StyleId spinner = StyleId.of ( "spinner" );
    public static final ChildStyleId spinnerEditorContainer = ChildStyleId.of ( "editor-container" );
    public static final ChildStyleId spinnerEditor = ChildStyleId.of ( "editor" );
    public static final ChildStyleId spinnerButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId spinnerNextButton = ChildStyleId.of ( "next" );
    public static final ChildStyleId spinnerPreviousButton = ChildStyleId.of ( "previous" );

    /**
     * {@link com.alee.laf.combobox.WebComboBox} style IDs.
     */
    public static final StyleId combobox = StyleId.of ( "combobox" );
    public static final StyleId comboboxUndecorated = StyleId.of ( "undecorated" );
    public static final ChildStyleId comboboxEditor = ChildStyleId.of ( "editor" );
    public static final ChildStyleId comboboxSeparator = ChildStyleId.of ( "separator" );
    public static final ChildStyleId comboboxArrowButton = ChildStyleId.of ( "arrow" );
    public static final ChildStyleId comboboxPopupScrollPane = ChildStyleId.of ( "popup" );
    public static final ChildStyleId comboboxPopupList = ChildStyleId.of ( "popup" );
    public static final ChildStyleId comboboxBoxRenderer = ChildStyleId.of ( "box-renderer" );
    public static final ChildStyleId comboboxListRenderer = ChildStyleId.of ( "list-renderer" );

    /**
     * {@link com.alee.laf.list.WebList} style IDs.
     */
    public static final StyleId list = StyleId.of ( "list" );
    public static final ChildStyleId listCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId listTextCellRenderer = ChildStyleId.of ( "text-renderer" );
    public static final ChildStyleId listIconCellRenderer = ChildStyleId.of ( "icon-renderer" );
    public static final ChildStyleId listCellEditor = ChildStyleId.of ( "editor" );

    /**
     * {@link com.alee.laf.tree.WebTree} style IDs.
     */
    public static final StyleId tree = StyleId.of ( "tree" );
    public static final StyleId treeTransparent = StyleId.of ( "transparent" );
    public static final ChildStyleId treeCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId treeCellEditor = ChildStyleId.of ( "editor" );

    /**
     * {@link com.alee.extended.tree.WebExTree} style IDs.
     */
    public static final StyleId extree = StyleId.of ( "extree" );

    /**
     * {@link com.alee.extended.tree.WebAsyncTree} style IDs.
     */
    public static final StyleId asynctree = StyleId.of ( "asynctree" );

    /**
     * {@link com.alee.extended.tree.WebFileTree} style IDs.
     */
    public static final StyleId filetree = StyleId.of ( "filetree" );

    /**
     * {@link com.alee.extended.tree.WebCheckBoxTree} style IDs.
     */
    public static final StyleId checkboxtree = StyleId.of ( "checkboxtree" );
    public static final ChildStyleId checkboxtreeCellRenderer = ChildStyleId.of ( "renderer" );

    /**
     * {@link com.alee.laf.colorchooser.WebColorChooser} style IDs.
     */
    public static final StyleId colorchooser = StyleId.of ( "colorchooser" );
    public static final StyleId colorchooserPanel = StyleId.of ( "colorchooser" );
    public static final ChildStyleId colorchooserLabel = ChildStyleId.of ( "label" );
    public static final ChildStyleId colorchooserControlsPanel = ChildStyleId.of ( "controls" );
    public static final ChildStyleId colorchooserWebonlyCheck = ChildStyleId.of ( "webonly" );
    public static final ChildStyleId colorchooserOkButton = ChildStyleId.of ( "ok" );
    public static final ChildStyleId colorchooserResetButton = ChildStyleId.of ( "reset" );
    public static final ChildStyleId colorchooserCancelButton = ChildStyleId.of ( "cancel" );

    /**
     * {@link com.alee.laf.filechooser.WebFileChooser} style IDs.
     */
    public static final StyleId filechooser = StyleId.of ( "filechooser" );
    public static final StyleId filechooserPanel = StyleId.of ( "filechooser" );
    public static final ChildStyleId filechooserToolbar = ChildStyleId.of ( "decorated-bar" );
    public static final ChildStyleId filechooserUndecoratedToolbar = ChildStyleId.of ( "undecorated-bar" );
    public static final ChildStyleId filechooserToolbarButton = ChildStyleId.of ( "tool" );
    public static final ChildStyleId filechooserPathField = ChildStyleId.of ( "path" );
    public static final ChildStyleId filechooserHistoryScrollPane = ChildStyleId.of ( "history" );
    public static final ChildStyleId filechooserCenterPanel = ChildStyleId.of ( "center" );
    public static final ChildStyleId filechooserCenterSplit = ChildStyleId.of ( "center" );
    public static final ChildStyleId filechooserNavScroll = ChildStyleId.of ( "nav" );
    public static final ChildStyleId filechooserFileTree = ChildStyleId.of ( "file" );
    public static final ChildStyleId filechooserViewScroll = ChildStyleId.of ( "view" );
    public static final ChildStyleId filechooserFileListTiles = ChildStyleId.of ( "file-tiles" );
    public static final ChildStyleId filechooserFileListIcons = ChildStyleId.of ( "file-icons" );
    public static final ChildStyleId filechooserFileTable = ChildStyleId.of ( "file" );
    public static final ChildStyleId filechooserSouthPanel = ChildStyleId.of ( "south" );
    public static final ChildStyleId filechooserSelectedLabel = ChildStyleId.of ( "selected" );
    public static final ChildStyleId filechooserAcceptButton = ChildStyleId.of ( "accept" );
    public static final ChildStyleId filechooserCancelButton = ChildStyleId.of ( "cancel" );
    public static final ChildStyleId filechooserRemovalListPanel = ChildStyleId.of ( "removal" );

    /**
     * {@link com.alee.laf.desktoppane.WebDesktopPane} style IDs.
     */
    public static final StyleId desktoppane = StyleId.of ( "desktoppane" );

    /**
     * {@link com.alee.laf.desktoppane.WebDesktopIcon} style IDs.
     */
    public static final StyleId desktopicon = StyleId.of ( "desktopicon" );

    /**
     * {@link com.alee.laf.desktoppane.WebInternalFrame} style IDs.
     */
    public static final StyleId internalframe = StyleId.of ( "internalframe" );
    public static final ChildStyleId internalframeTitleLabel = ChildStyleId.of ( "title" );
    public static final ChildStyleId internalframeButtonsPanel = ChildStyleId.of ( "buttons" );
    public static final ChildStyleId internalframeMinimizeButton = ChildStyleId.of ( "minimize" );
    public static final ChildStyleId internalframeMaximizeButton = ChildStyleId.of ( "maximize" );
    public static final ChildStyleId internalframeCloseButton = ChildStyleId.of ( "close" );
    public static final ChildStyleId internalframeRootpane = ChildStyleId.of ( "rootpane" );

    /**
     * {@link com.alee.laf.optionpane.WebOptionPane} style IDs.
     */
    public static final StyleId optionpane = StyleId.of ( "optionpane" );
    public static final ChildStyleId optionpaneButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId optionpaneYesButton = ChildStyleId.of ( "yes" );
    public static final ChildStyleId optionpaneNoButton = ChildStyleId.of ( "no" );
    public static final ChildStyleId optionpaneOkButton = ChildStyleId.of ( "ok" );
    public static final ChildStyleId optionpaneCancelButton = ChildStyleId.of ( "cancel" );

    /**
     * {@link com.alee.extended.window.WebPopup} style IDs.
     */
    public static final StyleId popup = StyleId.of ( "popup" );

    /**
     * {@link com.alee.managers.notification.WebNotification} style IDs.
     */
    public static final StyleId notification = StyleId.of ( "notification" );
    public static final ChildStyleId notificationOption = ChildStyleId.of ( "option" );

    /**
     * {@link com.alee.laf.grouping.GroupPane} style IDs.
     */
    public static final StyleId grouppane = StyleId.of ( "grouppane" );

    /**
     * {@link com.alee.extended.label.WebHotkeyLabel} style IDs.
     */
    public static final StyleId hotkeylabel = StyleId.of ( "hotkeylabel" );

    /**
     * {@link com.alee.managers.tooltip.WebCustomTooltip} style IDs.
     * todo Add proper parent (WebCustomTooltip should become a panel or have its own styles)
     */
    public static final StyleId customtooltipLabel = StyleId.of ( "customtooltip-label" );
    public static final StyleId customtooltipHotkeyLabel = StyleId.of ( "customtooltip-hotkey" );

    /**
     * {@link com.alee.extended.button.WebSwitch} style IDs.
     */
    public static final StyleId wswitch = StyleId.of ( "switch" );
    public static final ChildStyleId wswitchGripper = ChildStyleId.of ( "gripper" );
    public static final ChildStyleId wswitchLabel = ChildStyleId.of ( "option" );
    public static final ChildStyleId wswitchSelectedLabel = ChildStyleId.of ( "selected" );
    public static final ChildStyleId wswitchDeselectedLabel = ChildStyleId.of ( "deselected" );
    public static final ChildStyleId wswitchSelectedIconLabel = ChildStyleId.of ( "icon-selected" );
    public static final ChildStyleId wswitchDeselectedIconLabel = ChildStyleId.of ( "icon-deselected" );

    /**
     * {@link com.alee.extended.tree.WebTreeFilterField} style IDs.
     */
    public static final StyleId treefilterfield = StyleId.of ( "treefilterfield" );
    public static final StyleId treefilterfieldInline = StyleId.of ( "treefilterfield-inline" );

    /**
     * {@link com.alee.extended.list.WebCheckBoxList} style IDs.
     * todo Create custom UI for this list and enclose these styles with it
     */
    public static final StyleId checkboxlist = StyleId.of ( "checkboxlist" );
    public static final ChildStyleId checkboxlistCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId checkboxlistCellEditor = ChildStyleId.of ( "editor" );

    /**
     * {@link com.alee.extended.list.WebFileList} style IDs.
     */
    public static final StyleId filelist = StyleId.of ( "filelist" );
    public static final StyleId filelistTiles = StyleId.of ( "filelist-tiles" );
    public static final StyleId filelistIcons = StyleId.of ( "filelist-icons" );
    public static final ChildStyleId filelistCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId filelistTileCellRenderer = ChildStyleId.of ( "tile-renderer" );
    public static final ChildStyleId filelistIconCellRenderer = ChildStyleId.of ( "icon-renderer" );
    public static final ChildStyleId filelistCellEditor = ChildStyleId.of ( "editor" );

    /**
     * {@link com.alee.extended.filechooser.WebFileDrop} style IDs.
     */
    public static final StyleId filedrop = StyleId.of ( "filedrop" );
    public static final ChildStyleId filedropPlate = ChildStyleId.of ( "plate" );
    public static final ChildStyleId filedropPlateFileLabel = ChildStyleId.of ( "file" );
    public static final ChildStyleId filedropPlateRemoveButton = ChildStyleId.of ( "remove" );

    /**
     * {@link com.alee.extended.panel.WebCollapsiblePane} style IDs.
     */
    public static final StyleId collapsiblepane = StyleId.of ( "collapsiblepane" );
    public static final ChildStyleId collapsiblepaneHeaderPanel = ChildStyleId.of ( "header" );
    public static final ChildStyleId collapsiblepaneTitleLabel = ChildStyleId.of ( "title" );
    public static final ChildStyleId collapsiblepaneTopTitleLabel = ChildStyleId.of ( "title-top" );
    public static final ChildStyleId collapsiblepaneLeftTitleLabel = ChildStyleId.of ( "title-left" );
    public static final ChildStyleId collapsiblepaneBottomTitleLabel = ChildStyleId.of ( "title-bottom" );
    public static final ChildStyleId collapsiblepaneRightTitleLabel = ChildStyleId.of ( "title-right" );
    public static final ChildStyleId collapsiblepaneExpandButton = ChildStyleId.of ( "expand" );
    public static final ChildStyleId collapsiblepaneContentPanel = ChildStyleId.of ( "content" );

    /**
     * {@link com.alee.extended.panel.WebAccordion} style IDs.
     */
    public static final StyleId accordion = StyleId.of ( "accordion" );
    public static final ChildStyleId accordionPane = ChildStyleId.of ( "pane" );

    /**
     * {@link com.alee.managers.popup.WebInnerPopup} style IDs.
     */
    public static final StyleId innerpopup = StyleId.of ( "innerpopup" );

    /**
     * {@link com.alee.extended.window.WebPopOver}
     */
    public static final StyleId popover = StyleId.of ( "popover" );

    /**
     * {@link com.alee.extended.statusbar.WebMemoryBar} style IDs.
     */
    public static final StyleId memorybar = StyleId.of ( "memorybar" );
    public static final ChildStyleId memorybarTooltip = ChildStyleId.of ( "tooltip" );

    /**
     * {@link com.alee.extended.date.WebCalendar} style IDs.
     */
    public static final StyleId calendar = StyleId.of ( "calendar" );
    public static final ChildStyleId calendarSeparator = ChildStyleId.of ( "separator" );
    public static final ChildStyleId calendarHeaderPanel = ChildStyleId.of ( "header" );
    public static final ChildStyleId calendarButton = ChildStyleId.of ( "control" );
    public static final ChildStyleId calendarPrevYearButton = ChildStyleId.of ( "prev-year" );
    public static final ChildStyleId calendarPrevMonthButton = ChildStyleId.of ( "prev-month" );
    public static final ChildStyleId calendarNextMonthButton = ChildStyleId.of ( "next-month" );
    public static final ChildStyleId calendarNextYearButton = ChildStyleId.of ( "next-year" );
    public static final ChildStyleId calendarTitleLabel = ChildStyleId.of ( "title" );
    public static final ChildStyleId calendarWeekTitlesPanel = ChildStyleId.of ( "week-titles" );
    public static final ChildStyleId calendarWeekTitleSeparator = ChildStyleId.of ( "separator" );
    public static final ChildStyleId calendarWeekTitleLabel = ChildStyleId.of ( "title" );
    public static final ChildStyleId calendarMonthPanel = ChildStyleId.of ( "month" );
    public static final ChildStyleId calendarMonthDateSeparator = ChildStyleId.of ( "separator" );
    public static final ChildStyleId calendarMonthDateToggleButton = ChildStyleId.of ( "date" );
    public static final ChildStyleId calendarPreviousMonthDateToggleButton = ChildStyleId.of ( "previous-date" );
    public static final ChildStyleId calendarCurrentMonthDateToggleButton = ChildStyleId.of ( "current-date" );
    public static final ChildStyleId calendarWeekendMonthDateToggleButton = ChildStyleId.of ( "weekend-date" );
    public static final ChildStyleId calendarNextMonthDateToggleButton = ChildStyleId.of ( "next-date" );

    /**
     * {@link com.alee.extended.date.WebDateField} style IDs.
     */
    public static final StyleId datefield = StyleId.of ( "datefield" );
    public static final ChildStyleId datefieldField = ChildStyleId.of ( "field" );
    public static final ChildStyleId datefieldButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId datefieldPopup = ChildStyleId.of ( "popup" );
    public static final ChildStyleId datefieldCalendar = ChildStyleId.of ( "calendar" );

    /**
     * {@link com.alee.extended.breadcrumb.WebBreadcrumb} style IDs.
     */
    public static final StyleId breadcrumb = StyleId.of ( "breadcrumb" );
    public static final StyleId breadcrumbLabel = StyleId.of ( "breadcrumb-label" );
    public static final StyleId breadcrumbButton = StyleId.of ( "breadcrumb-button" );
    public static final StyleId breadcrumbToggleButton = StyleId.of ( "breadcrumb-togglebutton" );
    public static final StyleId breadcrumbPanel = StyleId.of ( "breadcrumb-panel" );

    /**
     * {@link com.alee.extended.syntax.WebSyntaxArea} and {@link com.alee.extended.syntax.WebSyntaxScrollPane} style IDs.
     */
    public static final StyleId syntaxareaScroll = StyleId.of ( "syntaxarea-scroll" );
    public static final StyleId syntaxareaScrollUndecorated = StyleId.of ( "syntaxarea-scroll-undecorated" );
    public static final ChildStyleId syntaxareaScrollGutter = ChildStyleId.of ( "gutter" );

    /**
     * {@link com.alee.extended.syntax.WebSyntaxPanel} style IDs.
     */
    public static final StyleId syntaxpanel = StyleId.of ( "syntaxpanel" );

    /**
     * {@link com.alee.extended.panel.WebComponentPane} style IDs.
     */
    public static final StyleId componentpane = StyleId.of ( "componentpane" );
    public static final ChildStyleId componentpanePanel = ChildStyleId.of ( "panel" );

    /**
     * {@link com.alee.extended.filechooser.WebDirectoryChooser} style IDs.
     */
    public static final StyleId directorychooser = StyleId.of ( "directorychooser" );
    public static final ChildStyleId directorychooserToolbar = ChildStyleId.of ( "toolbar" );
    public static final ChildStyleId directorychooserToolButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId directorychooserFolderUpButton = ChildStyleId.of ( "folderup" );
    public static final ChildStyleId directorychooserHomeButton = ChildStyleId.of ( "home" );
    public static final ChildStyleId directorychooserDriveButton = ChildStyleId.of ( "drive" );
    public static final ChildStyleId directorychooserRefreshButton = ChildStyleId.of ( "refresh" );
    public static final ChildStyleId directorychooserNewFolderButton = ChildStyleId.of ( "new-folder" );
    public static final ChildStyleId directorychooserDeleteButton = ChildStyleId.of ( "delete" );
    public static final ChildStyleId directorychooserControlsPanel = ChildStyleId.of ( "controls" );
    public static final ChildStyleId directorychooserControlButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId directorychooserAcceptButton = ChildStyleId.of ( "accept" );
    public static final ChildStyleId directorychooserCancelButton = ChildStyleId.of ( "cancel" );

    /**
     * {@link com.alee.extended.ninepatch.NinePatchEditor} style IDs.
     */
    public static final StyleId ninepatcheditor = StyleId.of ( "ninepatcheditor" );
    public static final ChildStyleId ninepatcheditorToolbar = ChildStyleId.of ( "toolbar" );
    public static final ChildStyleId ninepatcheditorZoomSlider = ChildStyleId.of ( "zoom" );
    public static final ChildStyleId ninepatcheditorFloatEditorSlider = ChildStyleId.of ( "editor-float" );
    public static final ChildStyleId ninepatcheditorPreviewField = ChildStyleId.of ( "preview" );
    public static final ChildStyleId ninepatcheditorPreviewBackground = ChildStyleId.of ( "preview-background" );

    /**
     * {@link com.alee.extended.tab.WebDocumentPane} style IDs.
     */
    public static final StyleId documentpane = StyleId.of ( "documentpane" );
    public static final ChildStyleId documentpaneTabbedPane = ChildStyleId.of ( "tabbedpane" );
    public static final ChildStyleId documentpaneCloseButton = ChildStyleId.of ( "close" );
    public static final ChildStyleId documentpaneMenu = ChildStyleId.of ( "menu" );

    /**
     * {@link com.alee.extended.dock.WebDockablePane} and {@link com.alee.extended.dock.WebDockableFrame} style IDs.
     */
    public static final StyleId dockablepane = StyleId.of ( "dockablepane" );
    public static final StyleId dockableframe = StyleId.of ( "dockableframe" );
    public static final StyleId dockableframeTop = StyleId.of ( "dockableframe-top" );
    public static final StyleId dockableframeLeft = StyleId.of ( "dockableframe-left" );
    public static final StyleId dockableframeBottom = StyleId.of ( "dockableframe-bottom" );
    public static final StyleId dockableframeRight = StyleId.of ( "dockableframe-right" );
    public static final ChildStyleId dockableframeTitlePanel = ChildStyleId.of ( "title" );
    public static final ChildStyleId dockableframeTitleLabel = ChildStyleId.of ( "title" );
    public static final ChildStyleId dockableframeTitleButtons = ChildStyleId.of ( "buttons" );
    public static final ChildStyleId dockableframeTitleButton = ChildStyleId.of ( "tool-button" );
    public static final ChildStyleId dockableframeTitleIconButton = ChildStyleId.of ( "tool-icon-button" );

    /**
     * {@link com.alee.extended.inspector.InterfaceInspector} style IDs.
     */
    public static final StyleId inspector = StyleId.of ( "inspector" );
    public static final StyleId inspectorPopover = StyleId.of ( "inspector-popover" );
    public static final ChildStyleId inspectorFilter = ChildStyleId.of ( "filter" );
    public static final ChildStyleId inspectorSeparator = ChildStyleId.of ( "separator" );
    public static final ChildStyleId inspectorScroll = ChildStyleId.of ( "scroll" );
    public static final ChildStyleId inspectorTree = ChildStyleId.of ( "tree" );

    /**
     * {@link com.alee.extended.style.StyleEditor} style IDs.
     */
    public static final StyleId styleeditor = StyleId.of ( "styleeditor" );
    public static final ChildStyleId styleeditorSplit = ChildStyleId.of ( "split" );
    public static final ChildStyleId styleeditorPreview = ChildStyleId.of ( "preview" );
    public static final ChildStyleId styleeditorPreviewToolbar = ChildStyleId.of ( "toolbar" );
    public static final ChildStyleId styleeditorPreviewTool = ChildStyleId.of ( "tool" );
    public static final ChildStyleId styleeditorPreviewScroll = ChildStyleId.of ( "scroll" );
    public static final ChildStyleId styleeditorPreviewPane = ChildStyleId.of ( "pane" );
    public static final ChildStyleId styleeditorPreviewSingle = ChildStyleId.of ( "single" );
    public static final ChildStyleId styleeditorPreviewSingleTitle = ChildStyleId.of ( "title" );
    public static final ChildStyleId styleeditorPreviewSingleShade = ChildStyleId.of ( "shade" );
    public static final ChildStyleId styleeditorPreviewSingleDashed = ChildStyleId.of ( "dashed" );
    public static final ChildStyleId styleeditorPreviewSingleEmpty = ChildStyleId.of ( "empty" );
    public static final ChildStyleId styleeditorEditors = ChildStyleId.of ( "editors" );
    public static final ChildStyleId styleeditorEditorsTabs = ChildStyleId.of ( "tabs" );
    public static final ChildStyleId styleeditorStatus = ChildStyleId.of ( "status" );
    public static final ChildStyleId styleeditorStatusLabel = ChildStyleId.of ( "label" );
    public static final ChildStyleId styleeditorStatusDelay = ChildStyleId.of ( "delay" );
    public static final ChildStyleId styleeditorStatusToggle = ChildStyleId.of ( "toggle" );

    /**
     * Style ID.
     * Identifies some specific component style.
     */
    private final String id;

    /**
     * Related parent styleable component.
     * It is used to to build complete component ID based on {@link #id} and parent complete style ID.
     * <p>
     * For example: if you have button with ID "close" and a parent with ID "buttons" is specified - the final ID for your button will be
     * "buttons.close" and it should be provided within the installed skin to avoid styling issues.
     */
    private final WeakReference<JComponent> parent;

    /**
     * Constructs new style ID container.
     *
     * @param id style ID
     */
    private StyleId ( final String id )
    {
        this ( id, null );
    }

    /**
     * Constructs new style ID container.
     *
     * @param id     style ID
     * @param parent parent styleable component
     */
    private StyleId ( final String id, final JComponent parent )
    {
        super ();
        this.id = id;
        this.parent = parent != null ? new WeakReference<JComponent> ( parent ) : null;
    }

    /**
     * Returns style ID.
     *
     * @return style ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Returns parent styleable component.
     *
     * @return parent styleable component
     */
    public JComponent getParent ()
    {
        return parent != null ? parent.get () : null;
    }

    /**
     * Returns complete style ID.
     *
     * @return complete style ID
     */
    public String getCompleteId ()
    {
        return getParent () != null ? get ( getParent () ).getCompleteId () + styleSeparator + getId () : getId ();
    }

    /**
     * Sets new component style ID.
     *
     * @param component component to set style ID for
     * @return previously used style ID
     */
    public StyleId set ( final JComponent component )
    {
        return StyleManager.setStyleId ( component, this );
    }

    /**
     * Sets new window style ID.
     *
     * @param window component to set style ID for
     * @return previously used style ID
     */
    public StyleId set ( final Window window )
    {
        return set ( getRootPane ( window ) );
    }

    @Override
    public boolean equals ( final Object obj )
    {
        boolean equals = false;
        if ( obj != null && obj instanceof StyleId )
        {
            final StyleId other = ( StyleId ) obj;
            equals = CompareUtils.equals ( getId (), other.getId () ) && getParent () == other.getParent ();
        }
        return equals;
    }

    @Override
    public String toString ()
    {
        return "StyleId [ id: \"" + getCompleteId () + "\"; parent: " + parent + " ]";
    }

    /**
     * Returns new style ID instance.
     *
     * @param id style ID
     * @return new style ID instance
     */
    public static StyleId of ( final String id )
    {
        return new StyleId ( id );
    }

    /**
     * Returns new style ID instance with the specified parent component.
     *
     * @param id     style ID
     * @param parent parent component
     * @return new style ID instance with the specified parent component
     */
    public static StyleId of ( final String id, final JComponent parent )
    {
        return new StyleId ( id, parent );
    }

    /**
     * Returns new style ID instance with the specified parent window.
     *
     * @param id     style ID
     * @param parent parent window
     * @return new style ID instance with the specified parent window
     */
    public static StyleId of ( final String id, final Window parent )
    {
        return of ( id, getRootPane ( parent ) );
    }

    /**
     * Returns style ID set in the specified component.
     *
     * @param component component to retrieve style ID from
     * @return style ID set in the specified component
     */
    public static StyleId get ( final JComponent component )
    {
        return StyleManager.getStyleId ( component );
    }

    /**
     * Returns style ID set in the specified window.
     *
     * @param window window to retrieve style ID from
     * @return style ID set in the specified window
     */
    public static StyleId get ( final Window window )
    {
        return get ( getRootPane ( window ) );
    }

    /**
     * Returns default style ID for the specified component.
     *
     * @param component component to retrieve default style ID for
     * @return default style ID for the specified component
     */
    public static StyleId getDefault ( final JComponent component )
    {
        return StyleableComponent.get ( component ).getDefaultStyleId ();
    }

    /**
     * Returns default style ID for the specified window.
     *
     * @param window window to retrieve default style ID for
     * @return default style ID for the specified window
     */
    public static StyleId getDefault ( final Window window )
    {
        return getDefault ( getRootPane ( window ) );
    }

    /**
     * Returns complete style ID for the specified component.
     * This identifier might be customized in component to force StyleManager provide another style for that specific component.
     *
     * @param component component to retrieve complete style ID for
     * @return identifier used within component style in skin descriptor
     */
    public static String getCompleteId ( final JComponent component )
    {
        return get ( component ).getCompleteId ();
    }

    /**
     * Returns complete style ID for the specified window.
     * This identifier might be customized in window to force StyleManager provide another style for that specific window.
     *
     * @param window window to retrieve complete style ID for
     * @return identifier used within window style in skin descriptor
     */
    public static String getCompleteId ( final Window window )
    {
        return getCompleteId ( getRootPane ( window ) );
    }

    /**
     * Returns window root pane.
     * Used instead of {@link com.alee.utils.SwingUtils#getRootPane(java.awt.Component)} method to throw style exception.
     * Style ID can only be installed into windows which use {@link javax.swing.JRootPane} component.
     *
     * @param window window to get root pane from
     * @return window root pane
     */
    private static JRootPane getRootPane ( final Window window )
    {
        final JRootPane rootPane = SwingUtils.getRootPane ( window );
        if ( rootPane == null )
        {
            throw new StyleException ( "Unable to retrieve Window root pane: " + window );
        }
        return rootPane;
    }
}