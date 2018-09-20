package com.alee.managers.style;

import com.alee.api.Identifiable;
import com.alee.api.jdk.Objects;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * This object encapsulates style identifier of a single {@link JComponent}.
 * It might also contain a weak reference to a style {@link JComponent} parent.
 *
 * This class also contains all identifiers for basic basic styles you might find in any of default skins.
 * Almost all these identifiers are used by various WebLaF components and complex component parts.
 * They are provided to allow restyling those parts without affecting default component style.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 */
public final class StyleId implements Identifiable
{
    /**
     * {@link StyleId} client property key for {@link javax.swing.JComponent}.
     * You can put either {@link com.alee.managers.style.StyleId} or {@link java.lang.String} under this key.
     * It will be immediately applied to component as its current {@link StyleId} if component uses WebLaF UI.
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
     * Style identifiers chain separator.
     * It should be used to separate parent and children style identifiers in a complete style identifier.
     *
     * @see #getCompleteId()
     * @see #getCompleteId(javax.swing.JComponent)
     * @see #getCompleteId(java.awt.Window)
     */
    public static final String styleSeparator = ".";

    /**
     * {@link StyleId} representing basic (aka default) style of any component.
     * Used to avoid providing {@code null} within component constructors.
     * It can be provided into any component to reset its {@link StyleId} to default value.
     *
     * @see com.alee.managers.style.StyleData#setStyleId(StyleId)
     */
    public static final StyleId auto = StyleId.of ( null );

    /**
     * {@link com.alee.extended.canvas.WebCanvas} style identifiers.
     */
    public static final StyleId canvas = StyleId.of ( "canvas" );
    public static final StyleId canvasGripperNW = StyleId.of ( "gripper-nw" );
    public static final StyleId canvasGripperN = StyleId.of ( "gripper-n" );
    public static final StyleId canvasGripperNE = StyleId.of ( "gripper-ne" );
    public static final StyleId canvasGripperW = StyleId.of ( "gripper-e" );
    public static final StyleId canvasGripperC = StyleId.of ( "gripper-c" );
    public static final StyleId canvasGripperE = StyleId.of ( "gripper-w" );
    public static final StyleId canvasGripperSW = StyleId.of ( "gripper-sw" );
    public static final StyleId canvasGripperS = StyleId.of ( "gripper-s" );
    public static final StyleId canvasGripperSE = StyleId.of ( "gripper-se" );

    /**
     * {@link com.alee.extended.image.WebImage} style identifiers.
     */
    public static final StyleId image = StyleId.of ( "image" );

    /**
     * {@link com.alee.laf.label.WebLabel} style identifiers.
     */
    public static final StyleId label = StyleId.of ( "label" );
    public static final StyleId labelIcon = StyleId.of ( "icon" );
    public static final StyleId labelShadow = StyleId.of ( "shadow" );
    public static final StyleId labelTag = StyleId.of ( "tag" );
    public static final StyleId labelVerticalCCW = StyleId.of ( "vertical-ccw" );
    public static final StyleId labelVerticalCW = StyleId.of ( "vertical-cw" );
    public static final StyleId labelSeparator = StyleId.of ( "separator" );

    /**
     * {@link com.alee.extended.label.WebStyledLabel} style identifiers.
     */
    public static final StyleId styledlabel = StyleId.of ( "styledlabel" );
    public static final StyleId styledlabelIcon = StyleId.of ( "icon" );
    public static final StyleId styledlabelShadow = StyleId.of ( "shadow" );
    public static final StyleId styledlabelTag = StyleId.of ( "tag" );
    public static final StyleId styledlabelVerticalCCW = StyleId.of ( "vertical-ccw" );
    public static final StyleId styledlabelVerticalCW = StyleId.of ( "vertical-cw" );
    public static final StyleId styledlabelSeparator = StyleId.of ( "separator" );

    /**
     * {@link com.alee.laf.tooltip.WebToolTip} style identifiers.
     */
    public static final StyleId tooltip = StyleId.of ( "tooltip" );
    public static final StyleId tooltipStyled = StyleId.of ( "styled" );

    /**
     * {@link com.alee.extended.link.WebLink} style identifiers.
     */
    public static final StyleId link = StyleId.of ( "link" );
    public static final StyleId linkShadow = StyleId.of ( "shadow" );
    public static final StyleId linkTag = StyleId.of ( "tag" );
    public static final StyleId linkVerticalCCW = StyleId.of ( "vertical-ccw" );
    public static final StyleId linkVerticalCW = StyleId.of ( "vertical-cw" );

    /**
     * {@link com.alee.laf.button.WebButton} style identifiers.
     */
    public static final StyleId button = StyleId.of ( "button" );
    public static final StyleId buttonStyled = StyleId.of ( "styled" );
    public static final StyleId buttonHover = StyleId.of ( "hover" );
    public static final StyleId buttonIcon = StyleId.of ( "icon" );
    public static final StyleId buttonIconHover = StyleId.of ( "icon-hover" );
    public static final StyleId buttonUndecorated = StyleId.of ( "undecorated" );

    /**
     * {@link com.alee.extended.button.WebSplitButton} style identifiers.
     */
    public static final StyleId splitbutton = StyleId.of ( "splitbutton" );
    public static final StyleId splitbuttonStyled = StyleId.of ( "styled" );
    public static final StyleId splitbuttonHover = StyleId.of ( "hover" );
    public static final StyleId splitbuttonIcon = StyleId.of ( "icon" );
    public static final StyleId splitbuttonIconHover = StyleId.of ( "icon-hover" );
    public static final StyleId splitbuttonUndecorated = StyleId.of ( "undecorated" );

    /**
     * {@link com.alee.laf.button.WebToggleButton} style identifiers.
     */
    public static final StyleId togglebutton = StyleId.of ( "togglebutton" );
    public static final StyleId togglebuttonStyled = StyleId.of ( "styled" );
    public static final StyleId togglebuttonHover = StyleId.of ( "hover" );
    public static final StyleId togglebuttonIcon = StyleId.of ( "icon" );
    public static final StyleId togglebuttonIconHover = StyleId.of ( "icon-hover" );
    public static final StyleId togglebuttonUndecorated = StyleId.of ( "undecorated" );

    /**
     * {@link com.alee.laf.checkbox.WebCheckBox} style identifiers.
     */
    public static final StyleId checkbox = StyleId.of ( "checkbox" );
    public static final StyleId checkboxStyled = StyleId.of ( "styled" );
    public static final StyleId checkboxLink = StyleId.of ( "link" );

    /**
     * {@link com.alee.extended.checkbox.WebTristateCheckBox} style identifiers.
     */
    public static final StyleId tristatecheckbox = StyleId.of ( "tristatecheckbox" );
    public static final StyleId tristatecheckboxStyled = StyleId.of ( "styled" );
    public static final StyleId tristatecheckboxLink = StyleId.of ( "link" );

    /**
     * {@link com.alee.laf.radiobutton.WebRadioButton} style identifiers.
     */
    public static final StyleId radiobutton = StyleId.of ( "radiobutton" );
    public static final StyleId radiobuttonStyled = StyleId.of ( "styled" );
    public static final StyleId radiobuttonLink = StyleId.of ( "link" );

    /**
     * {@link com.alee.laf.separator.WebSeparator} style identifiers.
     */
    public static final StyleId separator = StyleId.of ( "separator" );
    public static final StyleId separatorHorizontal = StyleId.of ( "horizontal" );
    public static final StyleId separatorVertical = StyleId.of ( "vertical" );

    /**
     * {@link com.alee.laf.menu.WebMenuBar} style identifiers.
     */
    public static final StyleId menubar = StyleId.of ( "menubar" );
    public static final StyleId menubarUndecorated = StyleId.of ( "undecorated" );

    /**
     * {@link com.alee.laf.menu.WebMenu} style identifiers.
     */
    public static final StyleId menu = StyleId.of ( "menu" );

    /**
     * {@link com.alee.laf.menu.WebPopupMenu} style identifiers.
     */
    public static final StyleId popupmenu = StyleId.of ( "popupmenu" );

    /**
     * {@link com.alee.laf.menu.WebMenuItem} style identifiers.
     */
    public static final StyleId menuitem = StyleId.of ( "menuitem" );

    /**
     * {@link com.alee.laf.menu.WebCheckBoxMenuItem} style identifiers.
     */
    public static final StyleId checkboxmenuitem = StyleId.of ( "checkboxmenuitem" );

    /**
     * {@link com.alee.laf.menu.WebRadioButtonMenuItem} style identifiers.
     */
    public static final StyleId radiobuttonmenuitem = StyleId.of ( "radiobuttonmenuitem" );

    /**
     * {@link com.alee.laf.menu.WebPopupMenuSeparator} style identifiers.
     */
    public static final StyleId popupmenuseparator = StyleId.of ( "popupmenuseparator" );

    /**
     * {@link com.alee.laf.panel.WebPanel} style identifiers.
     */
    public static final StyleId panel = StyleId.of ( "panel" );
    public static final StyleId panelNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId panelTransparent = StyleId.of ( "transparent" );
    public static final StyleId panelDecorated = StyleId.of ( "decorated" );
    public static final StyleId panelFocusable = StyleId.of ( "focusable" );

    /**
     * {@link com.alee.laf.rootpane.WebRootPane} style identifiers.
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
     * {@link com.alee.laf.window.WebWindow} style identifiers.
     */
    public static final StyleId window = StyleId.of ( "window" );
    public static final StyleId windowTransparent = StyleId.of ( "window-transparent" );

    /**
     * {@link com.alee.laf.window.WebFrame} style identifiers.
     */
    public static final StyleId frame = StyleId.of ( "frame" );
    public static final StyleId frameDecorated = StyleId.of ( "frame-decorated" );
    public static final StyleId frameUndecorated = StyleId.of ( "frame-undecorated" );
    public static final StyleId frameTransparent = StyleId.of ( "frame-transparent" );
    public static final ChildStyleId frameGlassDialog = ChildStyleId.of ( "glass-dialog" );

    /**
     * {@link com.alee.laf.window.WebDialog} style identifiers.
     */
    public static final StyleId dialog = StyleId.of ( "dialog" );
    public static final StyleId dialogDecorated = StyleId.of ( "dialog-decorated" );
    public static final StyleId dialogUndecorated = StyleId.of ( "dialog-undecorated" );
    public static final StyleId dialogTransparent = StyleId.of ( "dialog-transparent" );

    /**
     * {@link com.alee.laf.tabbedpane.WebTabbedPane} style identifiers.
     */
    public static final StyleId tabbedpane = StyleId.of ( "tabbedpane" );
    public static final StyleId tabbedpaneAttached = StyleId.of ( "attached" );

    /**
     * {@link com.alee.laf.splitpane.WebSplitPaneDivider} style identifiers.
     */
    public static final StyleId splitpanedivider = StyleId.of ( "splitpanedivider" );
    public static final ChildStyleId splitpanedividerOneTouchButton = ChildStyleId.of ( "onetouch" );
    public static final ChildStyleId splitpanedividerOneTouchLeftButton = ChildStyleId.of ( "onetouch-left" );
    public static final ChildStyleId splitpanedividerOneTouchRightButton = ChildStyleId.of ( "onetouch-right" );

    /**
     * {@link com.alee.laf.splitpane.WebSplitPane} style identifiers.
     */
    public static final StyleId splitpane = StyleId.of ( "splitpane" );
    public static final StyleId splitpaneNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId splitpaneTransparent = StyleId.of ( "transparent" );
    public static final StyleId splitpaneUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId splitpaneDecorated = StyleId.of ( "decorated" );
    public static final StyleId splitpaneFocusable = StyleId.of ( "focusable" );
    public static final ChildStyleId splitpaneContinuousDivider = ChildStyleId.of ( "continuous" );
    public static final ChildStyleId splitpaneNonContinuousDivider = ChildStyleId.of ( "non-continuous" );

    /**
     * {@link com.alee.extended.split.WebMultiSplitPaneDivider} style IDs.
     */
    public static final StyleId multisplitpanedivider = StyleId.of ( "multisplitpanedivider" );
    public static final ChildStyleId multisplitpanedividerOneTouchButton = ChildStyleId.of ( "onetouch" );
    public static final ChildStyleId multisplitpanedividerOneTouchLeftButton = ChildStyleId.of ( "onetouch-left" );
    public static final ChildStyleId multisplitpanedividerOneTouchRightButton = ChildStyleId.of ( "onetouch-right" );

    /**
     * {@link com.alee.extended.split.WebMultiSplitPane} style IDs.
     */
    public static final StyleId multisplitpane = StyleId.of ( "multisplitpane" );
    public static final StyleId multisplitpaneNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId multisplitpaneTransparent = StyleId.of ( "transparent" );
    public static final StyleId multisplitpaneUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId multisplitpaneDecorated = StyleId.of ( "decorated" );
    public static final StyleId multisplitpaneFocusable = StyleId.of ( "focusable" );
    public static final ChildStyleId multisplitpaneContinuousDivider = ChildStyleId.of ( "continuous" );
    public static final ChildStyleId multisplitpaneNonContinuousDivider = ChildStyleId.of ( "non-continuous" );

    /**
     * {@link com.alee.laf.toolbar.WebToolBar} style identifiers.
     */
    public static final StyleId toolbar = StyleId.of ( "toolbar" );
    public static final StyleId toolbarAttachedNorth = StyleId.of ( "attached-north" );
    public static final StyleId toolbarAttachedWest = StyleId.of ( "attached-west" );
    public static final StyleId toolbarAttachedEast = StyleId.of ( "attached-east" );
    public static final StyleId toolbarAttachedSouth = StyleId.of ( "attached-south" );
    public static final StyleId toolbarUndecorated = StyleId.of ( "undecorated" );

    /**
     * {@link com.alee.extended.statusbar.WebStatusBar} style identifiers.
     */
    public static final StyleId statusbar = StyleId.of ( "statusbar" );

    /**
     * {@link com.alee.laf.toolbar.WebToolBarSeparator} style identifiers.
     */
    public static final StyleId toolbarseparator = StyleId.of ( "toolbarseparator" );

    /**
     * {@link com.alee.laf.scroll.WebScrollBar} style identifiers.
     */
    public static final StyleId scrollbar = StyleId.of ( "scrollbar" );
    public static final StyleId scrollbarUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId scrollbarButtonless = StyleId.of ( "buttonless" );
    public static final StyleId scrollbarUndecoratedButtonless = StyleId.of ( "undecorated-buttonless" );
    public static final ChildStyleId scrollbarButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId scrollbarDecreaseButton = ChildStyleId.of ( "decrease" );
    public static final ChildStyleId scrollbarIncreaseButton = ChildStyleId.of ( "increase" );

    /**
     * {@link com.alee.laf.viewport.WebViewport} style identifiers.
     */
    public static final StyleId viewport = StyleId.of ( "viewport" );
    public static final StyleId viewportTransparent = StyleId.of ( "transparent" );

    /**
     * {@link com.alee.laf.scroll.WebScrollPane} style identifiers.
     */
    public static final StyleId scrollpane = StyleId.of ( "scrollpane" );
    public static final StyleId scrollpaneUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId scrollpaneNoFocus = StyleId.of ( "nofocus" );
    public static final StyleId scrollpaneTransparent = StyleId.of ( "transparent" );
    public static final StyleId scrollpaneButtonless = StyleId.of ( "buttonless" );
    public static final StyleId scrollpaneUndecoratedButtonless = StyleId.of ( "undecorated-buttonless" );
    public static final StyleId scrollpaneTransparentButtonless = StyleId.of ( "transparent-buttonless" );
    public static final StyleId scrollpaneHovering = StyleId.of ( "hovering" );
    public static final StyleId scrollpaneTransparentHovering = StyleId.of ( "transparent-hovering" );
    public static final StyleId scrollpanePopup = StyleId.of ( "popup" );
    public static final ChildStyleId scrollpaneViewport = ChildStyleId.of ( "viewport" );
    public static final ChildStyleId scrollpaneBar = ChildStyleId.of ( "scrollbar" );
    public static final ChildStyleId scrollpaneVerticalBar = ChildStyleId.of ( "vertical" );
    public static final ChildStyleId scrollpaneHorizontalBar = ChildStyleId.of ( "horizontal" );
    public static final ChildStyleId scrollpaneCorner = ChildStyleId.of ( "corner" );

    /**
     * {@link com.alee.laf.progressbar.WebProgressBar} style identifiers.
     */
    public static final StyleId progressbar = StyleId.of ( "progressbar" );

    /**
     * {@link com.alee.laf.text.WebTextField} style identifiers.
     */
    public static final StyleId textfield = StyleId.of ( "textfield" );
    public static final StyleId textfieldNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId textfieldTransparent = StyleId.of ( "transparent" );
    public static final StyleId textfieldNoFocus = StyleId.of ( "nofocus" );

    /**
     * {@link com.alee.laf.text.WebPasswordField} style identifiers.
     */
    public static final StyleId passwordfield = StyleId.of ( "passwordfield" );
    public static final StyleId passwordfieldNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId passwordfieldTransparent = StyleId.of ( "transparent" );
    public static final StyleId passwordfieldNoFocus = StyleId.of ( "nofocus" );

    /**
     * {@link com.alee.laf.text.WebFormattedTextField} style identifiers.
     */
    public static final StyleId formattedtextfield = StyleId.of ( "formattedtextfield" );
    public static final StyleId formattedtextfieldNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId formattedtextfieldTransparent = StyleId.of ( "transparent" );
    public static final StyleId formattedtextfieldNoFocus = StyleId.of ( "nofocus" );

    /**
     * {@link com.alee.extended.pathfield.WebPathField} style identifiers.
     */
    public static final StyleId pathfield = StyleId.of ( "pathfield" );
    public static final StyleId pathfieldNonOpaque = StyleId.of ( "pathfield-non-opaque" );
    public static final StyleId pathfieldTransparent = StyleId.of ( "pathfield-transparent" );
    public static final ChildStyleId pathfieldContentPanel = ChildStyleId.of ( "content" );
    public static final ChildStyleId pathfieldPathField = ChildStyleId.of ( "path-field" );
    public static final ChildStyleId pathfieldPopupScroll = ChildStyleId.of ( "scroll" );
    public static final ChildStyleId pathfieldRootButton = ChildStyleId.of ( "root" );
    public static final ChildStyleId pathfieldElementButton = ChildStyleId.of ( "element" );
    public static final ChildStyleId pathfieldMenuToggleButton = ChildStyleId.of ( "menu" );

    /**
     * {@link com.alee.extended.filechooser.WebFileChooserField} style identifiers.
     */
    public static final StyleId filechooserfield = StyleId.of ( "filechooserfield" );
    public static final StyleId filechooserfieldUndecorated = StyleId.of ( "filechooserfield-undecorated" );
    public static final ChildStyleId filechooserfieldContentPanel = ChildStyleId.of ( "content" );
    public static final ChildStyleId filechooserfieldContentScroll = ChildStyleId.of ( "scroll" );
    public static final ChildStyleId filechooserfieldFilePlate = ChildStyleId.of ( "file" );
    public static final ChildStyleId filechooserfieldFileNameLabel = ChildStyleId.of ( "name" );
    public static final ChildStyleId filechooserfieldFileRemoveButton = ChildStyleId.of ( "remove" );
    public static final ChildStyleId filechooserfieldChooseButton = ChildStyleId.of ( "choose" );

    /**
     * {@link com.alee.extended.colorchooser.WebColorChooserField} style identifiers.
     */
    public static final StyleId colorchooserfield = StyleId.of ( "colorchooserfield" );
    public static final ChildStyleId colorchooserfieldColorButton = ChildStyleId.of ( "choose" );

    /**
     * {@link com.alee.laf.text.WebTextArea} style identifiers.
     */
    public static final StyleId textarea = StyleId.of ( "textarea" );
    public static final StyleId textareaNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId textareaTransparent = StyleId.of ( "transparent" );
    public static final StyleId textareaDecorated = StyleId.of ( "decorated" );

    /**
     * {@link com.alee.laf.text.WebEditorPane} style identifiers.
     */
    public static final StyleId editorpane = StyleId.of ( "editorpane" );
    public static final StyleId editorpaneNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId editorpaneTransparent = StyleId.of ( "transparent" );
    public static final StyleId editorpaneDecorated = StyleId.of ( "decorated" );

    /**
     * {@link com.alee.laf.text.WebTextPane} style identifiers.
     */
    public static final StyleId textpane = StyleId.of ( "textpane" );
    public static final StyleId textpaneNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId textpaneTransparent = StyleId.of ( "transparent" );
    public static final StyleId textpaneDecorated = StyleId.of ( "decorated" );

    /**
     * {@link com.alee.laf.table.WebTableHeader} style identifiers.
     */
    public static final StyleId tableheader = StyleId.of ( "tableheader" );
    public static final ChildStyleId tableheaderCellRenderer = ChildStyleId.of ( "renderer" );

    /**
     * {@link com.alee.laf.table.WebTable} style identifiers.
     */
    public static final StyleId table = StyleId.of ( "table" );
    public static final StyleId tableNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId tableTransparent = StyleId.of ( "transparent" );
    public static final ChildStyleId tableHeader = ChildStyleId.of ( "header" );
    public static final ChildStyleId tableHeaderCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId tableCorner = ChildStyleId.of ( "corner" );
    public static final ChildStyleId tableCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId tableCellRendererNumber = ChildStyleId.of ( "renderer-number" );
    public static final ChildStyleId tableCellRendererDouble = ChildStyleId.of ( "renderer-double" );
    public static final ChildStyleId tableCellRendererFloat = ChildStyleId.of ( "renderer-float" );
    public static final ChildStyleId tableCellRendererDate = ChildStyleId.of ( "renderer-date" );
    public static final ChildStyleId tableCellRendererIcon = ChildStyleId.of ( "renderer-icon" );
    public static final ChildStyleId tableCellRendererBoolean = ChildStyleId.of ( "renderer-boolean" );
    public static final ChildStyleId tableCellEditor = ChildStyleId.of ( "editor" );
    public static final ChildStyleId tableCellEditorGemeric = ChildStyleId.of ( "editor-generic" );
    public static final ChildStyleId tableCellEditorBoolean = ChildStyleId.of ( "editor-boolean" );
    public static final ChildStyleId tableCellEditorDate = ChildStyleId.of ( "editor-date" );

    /**
     * {@link com.alee.extended.filechooser.WebFileTable} style identifiers.
     */
    public static final StyleId filetable = StyleId.of ( "filetable" );
    public static final StyleId filetableNonOpaque = StyleId.of ( "filetable-non-opaque" );
    public static final StyleId filetableTransparent = StyleId.of ( "filetable-transparent" );

    /**
     * {@link com.alee.laf.slider.WebSlider} style identifiers.
     */
    public static final StyleId slider = StyleId.of ( "slider" );
    public static final ChildStyleId sliderTickLabel = ChildStyleId.of ( "tick" );

    /**
     * {@link com.alee.laf.spinner.WebSpinner} style identifiers.
     */
    public static final StyleId spinner = StyleId.of ( "spinner" );
    public static final ChildStyleId spinnerEditorContainer = ChildStyleId.of ( "editor-container" );
    public static final ChildStyleId spinnerEditor = ChildStyleId.of ( "editor" );
    public static final ChildStyleId spinnerButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId spinnerNextButton = ChildStyleId.of ( "next" );
    public static final ChildStyleId spinnerPreviousButton = ChildStyleId.of ( "previous" );

    /**
     * {@link com.alee.laf.combobox.WebComboBox} style identifiers.
     */
    public static final StyleId combobox = StyleId.of ( "combobox" );
    public static final StyleId comboboxHover = StyleId.of ( "hover" );
    public static final StyleId comboboxUndecorated = StyleId.of ( "undecorated" );
    public static final ChildStyleId comboboxEditor = ChildStyleId.of ( "editor" );
    public static final ChildStyleId comboboxSeparator = ChildStyleId.of ( "separator" );
    public static final ChildStyleId comboboxArrowButton = ChildStyleId.of ( "arrow" );
    public static final ChildStyleId comboboxPopupScrollPane = ChildStyleId.of ( "popup" );
    public static final ChildStyleId comboboxPopupList = ChildStyleId.of ( "popup" );
    public static final ChildStyleId comboboxBoxRenderer = ChildStyleId.of ( "box-renderer" );
    public static final ChildStyleId comboboxListRenderer = ChildStyleId.of ( "list-renderer" );

    /**
     * {@link com.alee.laf.list.WebList} style identifiers.
     */
    public static final StyleId list = StyleId.of ( "list" );
    public static final StyleId listNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId listTransparent = StyleId.of ( "transparent" );
    public static final ChildStyleId listCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId listTextCellRenderer = ChildStyleId.of ( "text-renderer" );
    public static final ChildStyleId listIconCellRenderer = ChildStyleId.of ( "icon-renderer" );
    public static final ChildStyleId listTextCellEditor = ChildStyleId.of ( "editor" );

    /**
     * {@link com.alee.laf.tree.WebTree} style identifiers.
     */
    public static final StyleId tree = StyleId.of ( "tree" );
    public static final StyleId treeNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId treeTransparent = StyleId.of ( "transparent" );
    public static final ChildStyleId treeCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId treeCellEditor = ChildStyleId.of ( "editor" );
    public static final ChildStyleId treeCellEditorIcon = ChildStyleId.of ( "icon" );

    /**
     * {@link com.alee.extended.tree.WebExTree} style identifiers.
     */
    public static final StyleId extree = StyleId.of ( "extree" );
    public static final StyleId extreeNonOpaque = StyleId.of ( "extree-non-opaque" );
    public static final StyleId extreeTransparent = StyleId.of ( "extree-transparent" );

    /**
     * {@link com.alee.extended.tree.WebAsyncTree} style identifiers.
     */
    public static final StyleId asynctree = StyleId.of ( "asynctree" );
    public static final StyleId asynctreeNonOpaque = StyleId.of ( "asynctree-non-opaque" );
    public static final StyleId asynctreeTransparent = StyleId.of ( "asynctree-transparent" );

    /**
     * {@link com.alee.extended.tree.WebFileTree} style identifiers.
     */
    public static final StyleId filetree = StyleId.of ( "filetree" );
    public static final StyleId filetreeNonOpaque = StyleId.of ( "filetree-non-opaque" );
    public static final StyleId filetreeTransparent = StyleId.of ( "filetree-transparent" );

    /**
     * {@link com.alee.extended.tree.WebCheckBoxTree} style identifiers.
     */
    public static final StyleId checkboxtree = StyleId.of ( "checkboxtree" );
    public static final StyleId checkboxtreeNonOpaque = StyleId.of ( "checkboxtree-non-opaque" );
    public static final StyleId checkboxtreeTransparent = StyleId.of ( "checkboxtree-transparent" );
    public static final ChildStyleId checkboxtreeCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId checkboxtreeCheckBox = ChildStyleId.of ( "check" );

    /**
     * {@link com.alee.extended.tree.WebExCheckBoxTree} style identifiers.
     */
    public static final StyleId excheckboxtree = StyleId.of ( "excheckboxtree" );
    public static final StyleId excheckboxtreeNonOpaque = StyleId.of ( "excheckboxtree-non-opaque" );
    public static final StyleId excheckboxtreeTransparent = StyleId.of ( "excheckboxtree-transparent" );

    /**
     * {@link com.alee.extended.inspector.InterfaceTree} style identifiers.
     */
    public static final StyleId interfacetree = StyleId.of ( "interfacetree" );
    public static final StyleId interfacetreeNonOpaque = StyleId.of ( "interfacetree-non-opaque" );
    public static final StyleId interfacetreeTransparent = StyleId.of ( "interfacetree-transparent" );

    /**
     * {@link com.alee.laf.colorchooser.WebColorChooserPanel} style identifiers.
     */
    public static final StyleId colorchooserpanel = StyleId.of ( "colorchooser" );

    /**
     * {@link com.alee.laf.colorchooser.WebColorChooser} style identifiers.
     */
    public static final StyleId colorchooser = StyleId.of ( "colorchooser" );
    public static final StyleId colorchooserDialog = StyleId.of ( "colorchooser" );
    public static final ChildStyleId colorchooserContent = ChildStyleId.of ( "content" );
    public static final ChildStyleId colorchooserLabel = ChildStyleId.of ( "label" );
    public static final ChildStyleId colorchooserControlsPanel = ChildStyleId.of ( "controls" );
    public static final ChildStyleId colorchooserWebonlyCheck = ChildStyleId.of ( "webonly" );
    public static final ChildStyleId colorchooserOkButton = ChildStyleId.of ( "ok" );
    public static final ChildStyleId colorchooserResetButton = ChildStyleId.of ( "reset" );
    public static final ChildStyleId colorchooserCancelButton = ChildStyleId.of ( "cancel" );

    /**
     * {@link com.alee.laf.filechooser.WebFileChooser} style identifiers.
     */
    public static final StyleId filechooser = StyleId.of ( "filechooser" );
    public static final StyleId filechooserDialog = StyleId.of ( "filechooser" );
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
    public static final ChildStyleId filechooserFileTable = ChildStyleId.of ( "file-table" );
    public static final ChildStyleId filechooserSouthPanel = ChildStyleId.of ( "south" );
    public static final ChildStyleId filechooserSelectedLabel = ChildStyleId.of ( "selected" );
    public static final ChildStyleId filechooserAcceptButton = ChildStyleId.of ( "accept" );
    public static final ChildStyleId filechooserCancelButton = ChildStyleId.of ( "cancel" );
    public static final ChildStyleId filechooserRemovalListPanel = ChildStyleId.of ( "removal" );

    /**
     * {@link com.alee.laf.desktoppane.WebDesktopPane} style identifiers.
     */
    public static final StyleId desktoppane = StyleId.of ( "desktoppane" );
    public static final StyleId desktoppaneTransparent = StyleId.of ( "transparent" );

    /**
     * {@link javax.swing.JInternalFrame.JDesktopIcon} style identifiers.
     */
    public static final StyleId desktopicon = StyleId.of ( "desktopicon" );

    /**
     * {@link com.alee.laf.desktoppane.WebInternalFrame} style identifiers.
     */
    public static final StyleId internalframe = StyleId.of ( "internalframe" );
    public static final ChildStyleId internalframeTitlePanel = ChildStyleId.of ( "title" );
    public static final ChildStyleId internalframeTitleIcon = ChildStyleId.of ( "icon" );
    public static final ChildStyleId internalframeTitleLabel = ChildStyleId.of ( "title" );
    public static final ChildStyleId internalframeButtonsPanel = ChildStyleId.of ( "buttons" );
    public static final ChildStyleId internalframeMinimizeButton = ChildStyleId.of ( "minimize" );
    public static final ChildStyleId internalframeMaximizeButton = ChildStyleId.of ( "maximize" );
    public static final ChildStyleId internalframeCloseButton = ChildStyleId.of ( "close" );
    public static final ChildStyleId internalframeRootpane = ChildStyleId.of ( "rootpane" );

    /**
     * {@link com.alee.extended.dock.WebDockablePane} style identifiers.
     */
    public static final StyleId dockablepane = StyleId.of ( "dockablepane" );
    public static final ChildStyleId dockablepaneEmpty = ChildStyleId.of ( "empty" );
    public static final ChildStyleId dockablepaneFloating = ChildStyleId.of ( "floating" );

    /**
     * {@link com.alee.extended.dock.WebDockableFrame} style identifiers.
     */
    public static final StyleId dockableframe = StyleId.of ( "dockableframe" );
    public static final ChildStyleId dockableframeTitlePanel = ChildStyleId.of ( "title" );
    public static final ChildStyleId dockableframeTitleLabel = ChildStyleId.of ( "title" );
    public static final ChildStyleId dockableframeTitleButtonsPanel = ChildStyleId.of ( "buttons" );
    public static final ChildStyleId dockableframeTitleSeparator = ChildStyleId.of ( "tool" );
    public static final ChildStyleId dockableframeTitleButton = ChildStyleId.of ( "tool" );
    public static final ChildStyleId dockableframeTitleIconButton = ChildStyleId.of ( "tool-icon" );
    public static final ChildStyleId dockableframeSidebarButton = ChildStyleId.of ( "sidebar" );

    /**
     * {@link com.alee.laf.optionpane.WebOptionPane} style identifiers.
     */
    public static final StyleId optionpane = StyleId.of ( "optionpane" );
    public static final StyleId optionpaneInformationDialog = StyleId.of ( "information" );
    public static final StyleId optionpaneErrorDialog = StyleId.of ( "error" );
    public static final StyleId optionpaneQuestionDialog = StyleId.of ( "question" );
    public static final StyleId optionpaneWarningDialog = StyleId.of ( "warning" );
    public static final ChildStyleId optionpaneMessageArea = ChildStyleId.of ( "message-area" );
    public static final ChildStyleId optionpaneIconLabel = ChildStyleId.of ( "message-icon" );
    public static final ChildStyleId optionpaneRealBody = ChildStyleId.of ( "real-body" );
    public static final ChildStyleId optionpaneSeparator = ChildStyleId.of ( "separator" );
    public static final ChildStyleId optionpaneBody = ChildStyleId.of ( "body" );
    public static final ChildStyleId optionpaneMessageLabel = ChildStyleId.of ( "message" );
    public static final ChildStyleId optionpaneButtonArea = ChildStyleId.of ( "button-area" );
    public static final ChildStyleId optionpaneButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId optionpaneYesButton = ChildStyleId.of ( "yes" );
    public static final ChildStyleId optionpaneNoButton = ChildStyleId.of ( "no" );
    public static final ChildStyleId optionpaneOkButton = ChildStyleId.of ( "ok" );
    public static final ChildStyleId optionpaneCancelButton = ChildStyleId.of ( "cancel" );

    /**
     * {@link com.alee.extended.window.WebPopup} style identifiers.
     */
    public static final StyleId popup = StyleId.of ( "popup" );
    public static final StyleId popupTransparent = StyleId.of ( "transparent" );
    public static final StyleId popupUndecorated = StyleId.of ( "undecorated" );

    /**
     * {@link com.alee.managers.notification.WebNotification} style identifiers.
     */
    public static final StyleId notification = StyleId.of ( "notification" );
    public static final ChildStyleId notificationOption = ChildStyleId.of ( "option" );

    /**
     * {@link com.alee.laf.grouping.GroupPane} style identifiers.
     */
    public static final StyleId grouppane = StyleId.of ( "grouppane" );

    /**
     * {@link com.alee.extended.label.WebHotkeyLabel} style identifiers.
     */
    public static final StyleId hotkeylabel = StyleId.of ( "hotkeylabel" );

    /**
     * {@link com.alee.managers.tooltip.WebCustomTooltip} style identifiers.
     * todo Add proper parent (WebCustomTooltip should become a panel or have its own styles)
     */
    public static final StyleId customtooltip = StyleId.of ( "customtooltip" );
    public static final StyleId customtooltipLabel = StyleId.of ( "customtooltip-label" );
    public static final StyleId customtooltipHotkeyLabel = StyleId.of ( "customtooltip-hotkey" );

    /**
     * {@link com.alee.extended.button.WebSwitch} style identifiers.
     */
    public static final StyleId wswitch = StyleId.of ( "switch" );
    public static final ChildStyleId wswitchGripper = ChildStyleId.of ( "gripper" );
    public static final ChildStyleId wswitchLabel = ChildStyleId.of ( "option" );
    public static final ChildStyleId wswitchSelectedLabel = ChildStyleId.of ( "selected" );
    public static final ChildStyleId wswitchDeselectedLabel = ChildStyleId.of ( "deselected" );
    public static final ChildStyleId wswitchSelectedIconLabel = ChildStyleId.of ( "icon-selected" );
    public static final ChildStyleId wswitchDeselectedIconLabel = ChildStyleId.of ( "icon-deselected" );

    /**
     * {@link com.alee.extended.language.LanguageChooser} style identifiers.
     */
    public static final StyleId languagechooser = StyleId.of ( "languagechooser" );
    public static final StyleId languagechooserHover = StyleId.of ( "languagechooser-hover" );
    public static final StyleId languagechooserUndecorated = StyleId.of ( "languagechooser-undecorated" );

    /**
     * {@link com.alee.extended.tree.WebTreeFilterField} style identifiers.
     */
    public static final StyleId treefilterfield = StyleId.of ( "treefilterfield" );
    public static final StyleId treefilterfieldNonOpaque = StyleId.of ( "treefilterfield-non-opaque" );
    public static final StyleId treefilterfieldTransparent = StyleId.of ( "treefilterfield-transparent" );
    public static final ChildStyleId treefilterfieldSettings = ChildStyleId.of ( "settings" );

    /**
     * {@link com.alee.extended.list.WebCheckBoxList} style identifiers.
     * todo Create custom UI for this list and enclose these styles with it
     */
    public static final StyleId checkboxlist = StyleId.of ( "checkboxlist" );
    public static final StyleId checkboxlistNonOpaque = StyleId.of ( "checkboxlist-non-opaque" );
    public static final StyleId checkboxlistTransparent = StyleId.of ( "checkboxlist-transparent" );
    public static final ChildStyleId checkboxlistCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId checkboxlistCellEditor = ChildStyleId.of ( "editor" );

    /**
     * {@link com.alee.extended.list.WebFileList} style identifiers.
     */
    public static final StyleId filelist = StyleId.of ( "filelist" );
    public static final StyleId filelistNonOpaque = StyleId.of ( "filelist-non-opaque" );
    public static final StyleId filelistTransparent = StyleId.of ( "filelist-transparent" );
    public static final StyleId filelistTiles = StyleId.of ( "filelist-tiles" );
    public static final StyleId filelistTilesNonOpaque = StyleId.of ( "filelist-tiles-non-opaque" );
    public static final StyleId filelistTilesTransparent = StyleId.of ( "filelist-tiles-transparent" );
    public static final StyleId filelistIcons = StyleId.of ( "filelist-icons" );
    public static final StyleId filelistIconsNonOpaque = StyleId.of ( "filelist-icons-non-opaque" );
    public static final StyleId filelistIconsTransparent = StyleId.of ( "filelist-icons-transparent" );
    public static final ChildStyleId filelistCellRenderer = ChildStyleId.of ( "renderer" );
    public static final ChildStyleId filelistCellRendererIcon = ChildStyleId.of ( "icon" );
    public static final ChildStyleId filelistCellRendererName = ChildStyleId.of ( "name" );
    public static final ChildStyleId filelistCellRendererDescription = ChildStyleId.of ( "description" );
    public static final ChildStyleId filelistCellRendererSize = ChildStyleId.of ( "size" );
    public static final ChildStyleId filelistTileCellRenderer = ChildStyleId.of ( "tile-renderer" );
    public static final ChildStyleId filelistIconCellRenderer = ChildStyleId.of ( "icon-renderer" );
    public static final ChildStyleId filelistCellEditor = ChildStyleId.of ( "editor" );

    /**
     * {@link com.alee.extended.filechooser.WebFileDrop} style identifiers.
     */
    public static final StyleId filedrop = StyleId.of ( "filedrop" );
    public static final ChildStyleId filedropPlate = ChildStyleId.of ( "plate" );
    public static final ChildStyleId filedropPlateFileLabel = ChildStyleId.of ( "file" );
    public static final ChildStyleId filedropPlateRemoveButton = ChildStyleId.of ( "remove" );

    /**
     * {@link com.alee.extended.panel.WebCollapsiblePane} style identifiers.
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
     * {@link com.alee.extended.panel.WebAccordion} style identifiers.
     */
    public static final StyleId accordion = StyleId.of ( "accordion" );
    public static final ChildStyleId accordionPane = ChildStyleId.of ( "pane" );

    /**
     * {@link com.alee.managers.popup.WebInnerPopup} style identifiers.
     */
    public static final StyleId innerpopup = StyleId.of ( "innerpopup" );

    /**
     * {@link com.alee.extended.window.WebPopOver}
     */
    public static final StyleId popover = StyleId.of ( "popover" );

    /**
     * {@link com.alee.extended.statusbar.WebMemoryBar} style identifiers.
     */
    public static final StyleId memorybar = StyleId.of ( "memorybar" );
    public static final ChildStyleId memorybarTooltip = ChildStyleId.of ( "tooltip" );

    /**
     * {@link com.alee.extended.date.WebCalendar} style identifiers.
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
     * {@link com.alee.extended.date.WebDateField} style identifiers.
     */
    public static final StyleId datefield = StyleId.of ( "datefield" );
    public static final StyleId datefieldNonOpaque = StyleId.of ( "non-opaque" );
    public static final StyleId datefieldTransparent = StyleId.of ( "transparent" );
    public static final StyleId datefieldNoFocus = StyleId.of ( "nofocus" );
    public static final ChildStyleId datefieldField = ChildStyleId.of ( "field" );
    public static final ChildStyleId datefieldButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId datefieldPopup = ChildStyleId.of ( "popup" );
    public static final ChildStyleId datefieldCalendar = ChildStyleId.of ( "calendar" );

    /**
     * {@link com.alee.extended.breadcrumb.WebBreadcrumb} style identifiers.
     */
    public static final StyleId breadcrumb = StyleId.of ( "breadcrumb" );
    public static final StyleId breadcrumbUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId breadcrumbSeparated = StyleId.of ( "separated" );
    public static final StyleId breadcrumbSeparatedUndecorated = StyleId.of ( "separated-undecorated" );
    public static final ChildStyleId breadcrumbLabel = ChildStyleId.of ( "label" );
    public static final ChildStyleId breadcrumbStyledLabel = ChildStyleId.of ( "styledlabel" );
    public static final ChildStyleId breadcrumbLink = ChildStyleId.of ( "link" );
    public static final ChildStyleId breadcrumbButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId breadcrumbToggleButton = ChildStyleId.of ( "togglebutton" );
    public static final ChildStyleId breadcrumbSplitButton = ChildStyleId.of ( "splitbutton" );
    public static final ChildStyleId breadcrumbComboBox = ChildStyleId.of ( "combobox" );
    public static final ChildStyleId breadcrumbDateField = ChildStyleId.of ( "datefield" );
    public static final ChildStyleId breadcrumbCheckBox = ChildStyleId.of ( "checkbox" );
    public static final ChildStyleId breadcrumbTristateCheckBox = ChildStyleId.of ( "tristatecheckbox" );
    public static final ChildStyleId breadcrumbRadioButton = ChildStyleId.of ( "radiobutton" );
    public static final ChildStyleId breadcrumbTextField = ChildStyleId.of ( "textfield" );
    public static final ChildStyleId breadcrumbFormattedTextField = ChildStyleId.of ( "formattedtextfield" );
    public static final ChildStyleId breadcrumbPasswordField = ChildStyleId.of ( "passwordfield" );
    public static final ChildStyleId breadcrumbPanel = ChildStyleId.of ( "panel" );

    /**
     * {@link com.alee.extended.syntax.WebSyntaxArea} and {@link com.alee.extended.syntax.WebSyntaxScrollPane} style identifiers.
     */
    public static final StyleId syntaxareaScroll = StyleId.of ( "syntaxarea-scroll" );
    public static final StyleId syntaxareaScrollUndecorated = StyleId.of ( "syntaxarea-scroll-undecorated" );
    public static final ChildStyleId syntaxareaScrollGutter = ChildStyleId.of ( "gutter" );

    /**
     * {@link com.alee.extended.syntax.WebSyntaxPanel} style identifiers.
     */
    public static final StyleId syntaxpanel = StyleId.of ( "syntaxpanel" );

    /**
     * {@link com.alee.extended.panel.WebComponentPane} style identifiers.
     */
    public static final StyleId componentpane = StyleId.of ( "componentpane" );
    public static final ChildStyleId componentpanePanel = ChildStyleId.of ( "panel" );

    /**
     * {@link com.alee.extended.filechooser.WebDirectoryChooser} style identifiers.
     */
    public static final StyleId directorychooser = StyleId.of ( "directorychooser" );
    public static final ChildStyleId directorychooserToolbar = ChildStyleId.of ( "toolbar" );
    public static final ChildStyleId directorychooserToolButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId directorychooserFolderUpButton = ChildStyleId.of ( "folderup" );
    public static final ChildStyleId directorychooserHomeButton = ChildStyleId.of ( "home" );
    public static final ChildStyleId directorychooserDesktopButton = ChildStyleId.of ( "desktop" );
    public static final ChildStyleId directorychooserDriveButton = ChildStyleId.of ( "drive" );
    public static final ChildStyleId directorychooserRefreshButton = ChildStyleId.of ( "refresh" );
    public static final ChildStyleId directorychooserNewFolderButton = ChildStyleId.of ( "new-folder" );
    public static final ChildStyleId directorychooserDeleteButton = ChildStyleId.of ( "delete" );
    public static final ChildStyleId directorychooserPathField = ChildStyleId.of ( "path" );
    public static final ChildStyleId directorychooserScroll = ChildStyleId.of ( "scroll" );
    public static final ChildStyleId directorychooserFileSystem = ChildStyleId.of ( "file-system" );
    public static final ChildStyleId directorychooserControlsPanel = ChildStyleId.of ( "controls" );
    public static final ChildStyleId directorychooserControlButton = ChildStyleId.of ( "button" );
    public static final ChildStyleId directorychooserAcceptButton = ChildStyleId.of ( "accept" );
    public static final ChildStyleId directorychooserCancelButton = ChildStyleId.of ( "cancel" );

    /**
     * {@link com.alee.extended.ninepatch.NinePatchEditor} style identifiers.
     */
    public static final StyleId ninepatcheditor = StyleId.of ( "ninepatcheditor" );
    public static final ChildStyleId ninepatcheditorToolbar = ChildStyleId.of ( "toolbar" );
    public static final ChildStyleId ninepatcheditorZoomSlider = ChildStyleId.of ( "zoom" );
    public static final ChildStyleId ninepatcheditorFloatEditorSlider = ChildStyleId.of ( "editor-float" );
    public static final ChildStyleId ninepatcheditorPreviewField = ChildStyleId.of ( "preview" );
    public static final ChildStyleId ninepatcheditorPreviewBackground = ChildStyleId.of ( "preview-background" );

    /**
     * {@link com.alee.extended.tab.WebDocumentPane} style identifiers.
     */
    public static final StyleId documentpane = StyleId.of ( "documentpane" );
    public static final ChildStyleId documentpaneTabbedPane = ChildStyleId.of ( "tabbedpane" );
    public static final ChildStyleId documentpaneCloseButton = ChildStyleId.of ( "close" );
    public static final ChildStyleId documentpaneMenu = ChildStyleId.of ( "menu" );

    /**
     * {@link com.alee.extended.inspector.InterfaceInspector} style identifiers.
     */
    public static final StyleId inspector = StyleId.of ( "inspector" );
    public static final StyleId inspectorPopover = StyleId.of ( "inspector-popover" );
    public static final ChildStyleId inspectorFilter = ChildStyleId.of ( "filter" );
    public static final ChildStyleId inspectorInspect = ChildStyleId.of ( "inspect" );
    public static final ChildStyleId inspectorSeparator = ChildStyleId.of ( "separator" );
    public static final ChildStyleId inspectorScroll = ChildStyleId.of ( "scroll" );
    public static final ChildStyleId inspectorTree = ChildStyleId.of ( "tree" );

    /**
     * {@link com.alee.extended.style.StyleEditor} style identifiers.
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
    public static final ChildStyleId styleeditorPreviewSingleShadow = ChildStyleId.of ( "shadow" );
    public static final ChildStyleId styleeditorPreviewSingleDashed = ChildStyleId.of ( "dashed" );
    public static final ChildStyleId styleeditorPreviewSingleEmpty = ChildStyleId.of ( "empty" );
    public static final ChildStyleId styleeditorEditors = ChildStyleId.of ( "editors" );
    public static final ChildStyleId styleeditorEditorsTabs = ChildStyleId.of ( "tabs" );
    public static final ChildStyleId styleeditorStatus = ChildStyleId.of ( "status" );
    public static final ChildStyleId styleeditorStatusLabel = ChildStyleId.of ( "label" );
    public static final ChildStyleId styleeditorStatusDelay = ChildStyleId.of ( "delay" );
    public static final ChildStyleId styleeditorStatusToggle = ChildStyleId.of ( "toggle" );

    /**
     * Style identifier.
     * Identifies specific {@link JComponent} style.
     *
     * @see #getId()
     * @see #getCompleteId()
     */
    private final String id;

    /**
     * Parent styleable {@link JComponent}.
     * It is used to to build complete style identifier based on {@link #id} and parent complete style identifier.
     *
     * For example: if you have button with "close" style identifier and a parent with "buttons" style identifier is specified - the final
     * style identifier for your button will be "buttons.close" and it should be provided within the installed skin to avoid styling issues.
     *
     * @see #getCompleteId()
     */
    private final WeakReference<JComponent> parent;

    /**
     * Constructs new {@link StyleId}.
     *
     * @param id style identifier
     */
    private StyleId ( final String id )
    {
        this ( id, null );
    }

    /**
     * Constructs new {@link StyleId}.
     *
     * @param id     style identifier
     * @param parent parent styleable component
     */
    private StyleId ( final String id, final JComponent parent )
    {
        super ();
        this.id = id;
        this.parent = parent != null ? new WeakReference<JComponent> ( parent ) : null;
    }

    /**
     * Returns style identifier.
     *
     * @return style identifier
     */
    @Override
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
     * Returns complete style identifier.
     * Not that it will also include types of each of the parents.
     *
     * @return complete style identifier
     * @see com.alee.managers.style.data.ComponentStyle#getCompleteId()
     */
    public String getCompleteId ()
    {
        final JComponent parent = getParent ();
        return parent != null ? get ( parent ).getPathId ( parent ) + styleSeparator + getId () : getId ();
    }

    /**
     * Returns path for complete style identifier.
     * Not that it will also include types of each of the parents.
     *
     * @param component component for current part of the path
     * @return path for complete style identifier
     * @see com.alee.managers.style.data.ComponentStyle#getPathId()
     */
    private String getPathId ( final JComponent component )
    {
        // Full identifier for this part of the path
        final ComponentDescriptor<JComponent, ComponentUI> descriptor = StyleManager.getDescriptor ( component );
        final String fullId = descriptor.getId () + ":" + getId ();

        // Combined identifiers path
        final JComponent parent = getParent ();
        return parent != null ? get ( parent ).getPathId ( parent ) + styleSeparator + fullId : fullId;
    }

    /**
     * Sets this {@link StyleId} into specified {@link JComponent}.
     *
     * @param component component to set this {@link StyleId} for
     * @return previously used {@link StyleId}
     */
    public StyleId set ( final JComponent component )
    {
        return StyleManager.setStyleId ( component, this );
    }

    /**
     * Sets this {@link StyleId} into specified {@link Window}.
     * Note that this will only succeed for {@link Window}s that contain {@link JRootPane}.
     *
     * @param window component to set this {@link StyleId} for
     * @return previously used {@link StyleId}
     */
    public StyleId set ( final Window window )
    {
        return set ( getRootPane ( window ) );
    }

    @Override
    public boolean equals ( final Object obj )
    {
        boolean equals = false;
        if ( obj instanceof StyleId )
        {
            final StyleId other = ( StyleId ) obj;
            equals = Objects.equals ( getId (), other.getId () ) && getParent () == other.getParent ();
        }
        return equals;
    }

    @Override
    public String toString ()
    {
        if ( this != auto )
        {
            return String.format ( "StyleId [ id: '%s'; parent: %s ]", getCompleteId (), parent );
        }
        else
        {
            return "StyleId [ auto ]";
        }
    }

    /**
     * Returns new {@link StyleId} instance.
     *
     * @param id style identifier
     * @return new {@link StyleId} instance
     */
    public static StyleId of ( final String id )
    {
        return new StyleId ( id );
    }

    /**
     * Returns new {@link StyleId} instance with the specified parent component.
     *
     * @param id     style identifier
     * @param parent parent component
     * @return new {@link StyleId} instance with the specified parent component
     */
    public static StyleId of ( final String id, final JComponent parent )
    {
        return new StyleId ( id, parent );
    }

    /**
     * Returns new {@link StyleId} instance with the specified parent window.
     *
     * @param id     style identifier
     * @param parent parent window
     * @return new {@link StyleId} instance with the specified parent window
     */
    public static StyleId of ( final String id, final Window parent )
    {
        return of ( id, getRootPane ( parent ) );
    }

    /**
     * Returns {@link StyleId} set in the specified component.
     *
     * @param component component to retrieve {@link StyleId} from
     * @return {@link StyleId} set in the specified component
     */
    public static StyleId get ( final JComponent component )
    {
        return StyleManager.getStyleId ( component );
    }

    /**
     * Returns {@link StyleId} set in the specified window.
     *
     * @param window window to retrieve {@link StyleId} from
     * @return {@link StyleId} set in the specified window
     */
    public static StyleId get ( final Window window )
    {
        return get ( getRootPane ( window ) );
    }

    /**
     * Returns default {@link StyleId} for the specified component.
     *
     * @param component component to retrieve default {@link StyleId} for
     * @return default {@link StyleId} for the specified component
     */
    public static StyleId getDefault ( final JComponent component )
    {
        final ComponentDescriptor descriptor = StyleManager.getDescriptor ( component );
        return descriptor.getDefaultStyleId ( component );
    }

    /**
     * Returns default {@link StyleId} for the specified window.
     *
     * @param window window to retrieve default {@link StyleId} for
     * @return default {@link StyleId} for the specified window
     */
    public static StyleId getDefault ( final Window window )
    {
        return getDefault ( getRootPane ( window ) );
    }

    /**
     * Returns complete style identifier for the specified component.
     * This identifier might be customized in component to force StyleManager provide another style for that specific component.
     *
     * @param component component to retrieve complete style identifier for
     * @return identifier used within component style in skin descriptor
     */
    public static String getCompleteId ( final JComponent component )
    {
        return get ( component ).getCompleteId ();
    }

    /**
     * Returns complete style identifier for the specified window.
     * This identifier might be customized in window to force StyleManager provide another style for that specific window.
     *
     * @param window window to retrieve complete style identifier for
     * @return identifier used within window style in skin descriptor
     */
    public static String getCompleteId ( final Window window )
    {
        return getCompleteId ( getRootPane ( window ) );
    }

    /**
     * Returns window root pane.
     * Used instead of {@link CoreSwingUtils#getRootPane(java.awt.Component)} method to throw style exception.
     * {@link StyleId} can only be installed into windows which use {@link JRootPane} component.
     *
     * @param window window to get root pane from
     * @return window root pane
     */
    private static JRootPane getRootPane ( final Window window )
    {
        final JRootPane rootPane = CoreSwingUtils.getRootPane ( window );
        if ( rootPane == null )
        {
            final String msg = "Unable to retrieve root pane for Window: %s";
            throw new StyleException ( String.format ( msg, window ) );
        }
        return rootPane;
    }
}