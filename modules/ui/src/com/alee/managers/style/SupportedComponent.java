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

package com.alee.managers.style;

import com.alee.extended.button.WebSplitButton;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.label.WebMultiLineLabel;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.label.WebVerticalLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.managers.style.data.ComponentStyleConverter;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.Styleable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.table.JTableHeader;
import java.util.*;

/**
 * This enumeration represents list of Swing and WebLaF components which support WebLaF styling.
 * It also contains some references and useful settings for each component type.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.laf.WebLookAndFeel
 */

public enum SupportedComponent
{
    /**
     * Label-related components.
     */
    label ( true, JLabel.class, "LabelUI", WebLookAndFeel.labelUI ),
    // linkLabel ( true, WebLinkLabel.class, "LinkLabelUI", WebLookAndFeel.linkLabelUI ),
    verticalLabel ( true, WebVerticalLabel.class, "VerticalLabelUI", WebLookAndFeel.verticalLabelUI ),
    multiLineLabel ( false, WebMultiLineLabel.class, "MultiLineLabelUI", WebLookAndFeel.multiLineLabelUI ),
    styledLabel ( true, WebStyledLabel.class, "StyledLabelUI", WebLookAndFeel.styledLabelUI ),
    toolTip ( false, JToolTip.class, "ToolTipUI", WebLookAndFeel.toolTipUI ),

    /**
     * Button-related components.
     */
    button ( false, JButton.class, "ButtonUI", WebLookAndFeel.buttonUI ),
    splitButton ( false, WebSplitButton.class, "SplitButtonUI", WebLookAndFeel.splitButtonUI ),
    toggleButton ( false, JToggleButton.class, "ToggleButtonUI", WebLookAndFeel.toggleButtonUI ),
    checkBox ( false, JCheckBox.class, "CheckBoxUI", WebLookAndFeel.checkBoxUI ),
    tristateCheckBox ( false, WebTristateCheckBox.class, "TristateCheckBoxUI", WebLookAndFeel.tristateCheckBoxUI ),
    radioButton ( false, JRadioButton.class, "RadioButtonUI", WebLookAndFeel.radioButtonUI ),

    /**
     * Menu-related components.
     */
    menuBar ( false, JMenuBar.class, "MenuBarUI", WebLookAndFeel.menuBarUI ),
    menu ( false, JMenu.class, "MenuUI", WebLookAndFeel.menuUI ),
    popupMenu ( true, JPopupMenu.class, "PopupMenuUI", WebLookAndFeel.popupMenuUI ),
    menuItem ( false, JMenuItem.class, "MenuItemUI", WebLookAndFeel.menuItemUI ),
    checkBoxMenuItem ( false, JCheckBoxMenuItem.class, "CheckBoxMenuItemUI", WebLookAndFeel.checkBoxMenuItemUI ),
    radioButtonMenuItem ( false, JRadioButtonMenuItem.class, "RadioButtonMenuItemUI", WebLookAndFeel.radioButtonMenuItemUI ),
    popupMenuSeparator ( false, JPopupMenu.Separator.class, "PopupMenuSeparatorUI", WebLookAndFeel.popupMenuSeparatorUI ),

    /**
     * Separator component.
     */
    separator ( false, JSeparator.class, "SeparatorUI", WebLookAndFeel.separatorUI ),

    /**
     * Scroll-related components.
     */
    scrollBar ( true, JScrollBar.class, "ScrollBarUI", WebLookAndFeel.scrollBarUI ),
    scrollPane ( false, JScrollPane.class, "ScrollPaneUI", WebLookAndFeel.scrollPaneUI ),
    viewport ( false, JViewport.class, "ViewportUI", WebLookAndFeel.viewportUI ),

    /**
     * Text-related components.
     */
    textField ( false, JTextField.class, "TextFieldUI", WebLookAndFeel.textFieldUI ),
    passwordField ( false, JPasswordField.class, "PasswordFieldUI", WebLookAndFeel.passwordFieldUI ),
    formattedTextField ( false, JFormattedTextField.class, "FormattedTextFieldUI", WebLookAndFeel.formattedTextFieldUI ),
    textArea ( false, JTextArea.class, "TextAreaUI", WebLookAndFeel.textAreaUI ),
    editorPane ( false, JEditorPane.class, "EditorPaneUI", WebLookAndFeel.editorPaneUI ),
    textPane ( false, JTextPane.class, "TextPaneUI", WebLookAndFeel.textPaneUI ),

    /**
     * Toolbar-related components.
     */
    toolBar ( false, JToolBar.class, "ToolBarUI", WebLookAndFeel.toolBarUI ),
    toolBarSeparator ( false, JToolBar.Separator.class, "ToolBarSeparatorUI", WebLookAndFeel.toolBarSeparatorUI ),

    /**
     * Table-related components.
     */
    table ( false, JTable.class, "TableUI", WebLookAndFeel.tableUI ),
    tableHeader ( false, JTableHeader.class, "TableHeaderUI", WebLookAndFeel.tableHeaderUI ),

    /**
     * Chooser components.
     */
    colorChooser ( false, JColorChooser.class, "ColorChooserUI", WebLookAndFeel.colorChooserUI ),
    fileChooser ( false, JFileChooser.class, "FileChooserUI", WebLookAndFeel.fileChooserUI ),

    /**
     * Container-related components.
     */
    panel ( true, JPanel.class, "PanelUI", WebLookAndFeel.panelUI ),
    rootPane ( false, JRootPane.class, "RootPaneUI", WebLookAndFeel.rootPaneUI ),
    tabbedPane ( false, JTabbedPane.class, "TabbedPaneUI", WebLookAndFeel.tabbedPaneUI ),
    splitPane ( false, JSplitPane.class, "SplitPaneUI", WebLookAndFeel.splitPaneUI ),

    /**
     * Other data-related components.
     */
    progressBar ( false, JProgressBar.class, "ProgressBarUI", WebLookAndFeel.progressBarUI ),
    slider ( false, JSlider.class, "SliderUI", WebLookAndFeel.sliderUI ),
    spinner ( false, JSpinner.class, "SpinnerUI", WebLookAndFeel.spinnerUI ),
    tree ( false, JTree.class, "TreeUI", WebLookAndFeel.treeUI ),
    list ( false, JList.class, "ListUI", WebLookAndFeel.listUI ),
    comboBox ( false, JComboBox.class, "ComboBoxUI", WebLookAndFeel.comboBoxUI ),

    /**
     * Desktop-pane-related components.
     */
    desktopPane ( false, JDesktopPane.class, "DesktopPaneUI", WebLookAndFeel.desktopPaneUI ),
    desktopIcon ( false, JInternalFrame.JDesktopIcon.class, "DesktopIconUI", WebLookAndFeel.desktopIconUI ),
    internalFrame ( false, JInternalFrame.class, "InternalFrameUI", WebLookAndFeel.internalFrameUI ),

    /**
     * Option pane component.
     */
    optionPane ( false, JOptionPane.class, "OptionPaneUI", WebLookAndFeel.optionPaneUI );

    /**
     * Enum constant settings.
     */
    protected final boolean supportsPainters;
    protected final Class<? extends JComponent> componentClass;
    protected final String uiClassID;
    protected final String defaultUIClass;

    /**
     * Constructs a reference to component with specified settings.
     *
     * @param supportsPainters whether this component supports painters or not
     * @param componentClass   component class for this component type
     * @param uiClassID        UI class ID used by LookAndFeel to store various settings
     * @param defaultUIClass   default UI class canonical name
     */
    private SupportedComponent ( final boolean supportsPainters, final Class<? extends JComponent> componentClass, final String uiClassID,
                                 final String defaultUIClass )
    {
        this.supportsPainters = supportsPainters;
        this.componentClass = componentClass;
        this.uiClassID = uiClassID;
        this.defaultUIClass = defaultUIClass;
    }

    /**
     * Returns whether this component type supports painters or not.
     *
     * @return true if this component type supports painters, false otherwise
     */
    public boolean supportsPainters ()
    {
        return supportsPainters;
    }

    /**
     * Returns component class for this component type.
     *
     * @return component class for this component type
     */
    public Class<? extends JComponent> getComponentClass ()
    {
        return componentClass;
    }

    /**
     * Returns UI class ID used by LookAndFeel to store various settings.
     *
     * @return UI class ID
     */
    public String getUIClassID ()
    {
        return uiClassID;
    }

    /**
     * Returns default UI class canonical name.
     * This value is used in WebLookAndFeel to provide default UI classes.
     * However they can be reassigned before WebLookAndFeel installation.
     *
     * @return default UI class canonical name
     */
    public String getDefaultUIClass ()
    {
        return defaultUIClass;
    }

    /**
     * Returns style identifier for the specified component.
     * This identifier might be customized in component to force StyleManager provide another style for that specific component.
     *
     * @return component identifier used within style in skin descriptor
     */
    public String getComponentStyleId ( final JComponent component )
    {
        final Styleable styleable = LafUtils.getStyleable ( component );
        final String styleId = styleable != null ? styleable.getStyleId () : ComponentStyleConverter.DEFAULT_STYLE_ID;
        return styleId != null ? styleId : ComponentStyleConverter.DEFAULT_STYLE_ID;
    }

    /**
     * Returns UI class for this component type.
     * Result of this method is not cached because UI classes might be changed in runtime.
     *
     * @return UI class for this component type
     */
    public Class<? extends ComponentUI> getUIClass ()
    {
        final Class type = ReflectUtils.getClassSafely ( UIManager.getString ( getUIClassID () ) );
        final Class defaultType = ReflectUtils.getClassSafely ( getDefaultUIClass () );
        return ReflectUtils.isAssignable ( defaultType, type ) ? type : defaultType;
    }

    /**
     * Component type icons cache.
     */
    private static final Map<SupportedComponent, ImageIcon> componentIcons =
            new EnumMap<SupportedComponent, ImageIcon> ( SupportedComponent.class );

    /**
     * Returns component type icon.
     *
     * @return component type icon
     */
    public ImageIcon getIcon ()
    {
        ImageIcon icon = componentIcons.get ( this );
        if ( icon == null )
        {
            try
            {
                icon = new ImageIcon ( SupportedComponent.class.getResource ( "icons/component/" + this + ".png" ) );
                componentIcons.put ( this, icon );
            }
            catch ( final Throwable e )
            {
                Log.get ().error ( "Unable to find component icon: " + this, e );
                componentIcons.put ( this, null );
            }
        }
        return icon;
    }

    /**
     * Lazily initialized component types map by their UI class IDs.
     */
    private static final Map<String, SupportedComponent> componentByUIClassID =
            new HashMap<String, SupportedComponent> ( values ().length );

    /**
     * Returns supported component type by UI class ID.
     *
     * @param uiClassID UI class ID
     * @return supported component type by UI class ID
     */
    public static SupportedComponent getComponentTypeByUIClassID ( final String uiClassID )
    {
        if ( componentByUIClassID.size () == 0 )
        {
            for ( final SupportedComponent supportedComponent : values () )
            {
                componentByUIClassID.put ( supportedComponent.getUIClassID (), supportedComponent );
            }
        }
        return componentByUIClassID.get ( uiClassID );
    }

    /**
     * Returns list of component types which supports painters.
     *
     * @return list of component types which supports painters
     */
    public static List<SupportedComponent> getPainterSupportedComponents ()
    {
        final List<SupportedComponent> supportedComponents = new ArrayList<SupportedComponent> ();
        for ( final SupportedComponent sc : SupportedComponent.values () )
        {
            if ( sc.supportsPainters () )
            {
                supportedComponents.add ( sc );
            }
        }
        return supportedComponents;
    }
}