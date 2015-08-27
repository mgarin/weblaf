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
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.table.JTableHeader;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This enumeration represents list of Swing and WebLaF components which support WebLaF styling.
 * It also contains some references and useful settings for each component type.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.laf.WebLookAndFeel
 */

public enum StyleableComponent
{
    /**
     * Label-related components.
     */
    label ( JLabel.class, "LabelUI", WebLookAndFeel.labelUI ),
    styledLabel ( WebStyledLabel.class, "StyledLabelUI", WebLookAndFeel.styledLabelUI ),
    toolTip ( JToolTip.class, "ToolTipUI", WebLookAndFeel.toolTipUI ),

    /**
     * Button-related components.
     */
    button ( JButton.class, "ButtonUI", WebLookAndFeel.buttonUI ),
    splitButton ( WebSplitButton.class, "SplitButtonUI", WebLookAndFeel.splitButtonUI ),
    toggleButton ( JToggleButton.class, "ToggleButtonUI", WebLookAndFeel.toggleButtonUI ),
    checkBox ( JCheckBox.class, "CheckBoxUI", WebLookAndFeel.checkBoxUI ),
    tristateCheckBox ( WebTristateCheckBox.class, "TristateCheckBoxUI", WebLookAndFeel.tristateCheckBoxUI ),
    radioButton ( JRadioButton.class, "RadioButtonUI", WebLookAndFeel.radioButtonUI ),

    /**
     * Menu-related components.
     */
    menuBar ( JMenuBar.class, "MenuBarUI", WebLookAndFeel.menuBarUI ),
    menu ( JMenu.class, "MenuUI", WebLookAndFeel.menuUI ),
    popupMenu ( JPopupMenu.class, "PopupMenuUI", WebLookAndFeel.popupMenuUI ),
    menuItem ( JMenuItem.class, "MenuItemUI", WebLookAndFeel.menuItemUI ),
    checkBoxMenuItem ( JCheckBoxMenuItem.class, "CheckBoxMenuItemUI", WebLookAndFeel.checkBoxMenuItemUI ),
    radioButtonMenuItem ( JRadioButtonMenuItem.class, "RadioButtonMenuItemUI", WebLookAndFeel.radioButtonMenuItemUI ),
    popupMenuSeparator ( JPopupMenu.Separator.class, "PopupMenuSeparatorUI", WebLookAndFeel.popupMenuSeparatorUI ),

    /**
     * Separator component.
     */
    separator ( JSeparator.class, "SeparatorUI", WebLookAndFeel.separatorUI ),

    /**
     * Scroll-related components.
     */
    scrollBar ( JScrollBar.class, "ScrollBarUI", WebLookAndFeel.scrollBarUI ),
    scrollPane ( JScrollPane.class, "ScrollPaneUI", WebLookAndFeel.scrollPaneUI ),
    viewport ( JViewport.class, "ViewportUI", WebLookAndFeel.viewportUI ),

    /**
     * Text-related components.
     */
    textField ( JTextField.class, "TextFieldUI", WebLookAndFeel.textFieldUI ),
    passwordField ( JPasswordField.class, "PasswordFieldUI", WebLookAndFeel.passwordFieldUI ),
    formattedTextField ( JFormattedTextField.class, "FormattedTextFieldUI", WebLookAndFeel.formattedTextFieldUI ),
    textArea ( JTextArea.class, "TextAreaUI", WebLookAndFeel.textAreaUI ),
    editorPane ( JEditorPane.class, "EditorPaneUI", WebLookAndFeel.editorPaneUI ),
    textPane ( JTextPane.class, "TextPaneUI", WebLookAndFeel.textPaneUI ),

    /**
     * Toolbar-related components.
     */
    toolBar ( JToolBar.class, "ToolBarUI", WebLookAndFeel.toolBarUI ),
    toolBarSeparator ( JToolBar.Separator.class, "ToolBarSeparatorUI", WebLookAndFeel.toolBarSeparatorUI ),

    /**
     * Table-related components.
     */
    table ( JTable.class, "TableUI", WebLookAndFeel.tableUI ),
    tableHeader ( JTableHeader.class, "TableHeaderUI", WebLookAndFeel.tableHeaderUI ),

    /**
     * Chooser components.
     */
    colorChooser ( JColorChooser.class, "ColorChooserUI", WebLookAndFeel.colorChooserUI ),
    fileChooser ( JFileChooser.class, "FileChooserUI", WebLookAndFeel.fileChooserUI ),

    /**
     * Container-related components.
     */
    panel ( JPanel.class, "PanelUI", WebLookAndFeel.panelUI ),
    rootPane ( JRootPane.class, "RootPaneUI", WebLookAndFeel.rootPaneUI ),
    tabbedPane ( JTabbedPane.class, "TabbedPaneUI", WebLookAndFeel.tabbedPaneUI ),
    splitPane ( JSplitPane.class, "SplitPaneUI", WebLookAndFeel.splitPaneUI ),

    /**
     * Other data-related components.
     */
    progressBar ( JProgressBar.class, "ProgressBarUI", WebLookAndFeel.progressBarUI ),
    slider ( JSlider.class, "SliderUI", WebLookAndFeel.sliderUI ),
    spinner ( JSpinner.class, "SpinnerUI", WebLookAndFeel.spinnerUI ),
    tree ( JTree.class, "TreeUI", WebLookAndFeel.treeUI ),
    list ( JList.class, "ListUI", WebLookAndFeel.listUI ),
    comboBox ( JComboBox.class, "ComboBoxUI", WebLookAndFeel.comboBoxUI ),

    /**
     * Desktop-pane-related components.
     */
    desktopPane ( JDesktopPane.class, "DesktopPaneUI", WebLookAndFeel.desktopPaneUI ),
    desktopIcon ( JInternalFrame.JDesktopIcon.class, "DesktopIconUI", WebLookAndFeel.desktopIconUI ),
    internalFrame ( JInternalFrame.class, "InternalFrameUI", WebLookAndFeel.internalFrameUI ),

    /**
     * Option pane component.
     */
    optionPane ( JOptionPane.class, "OptionPaneUI", WebLookAndFeel.optionPaneUI );

    /**
     * Component type icons cache.
     */
    private static final Map<StyleableComponent, ImageIcon> componentIcons =
            new EnumMap<StyleableComponent, ImageIcon> ( StyleableComponent.class );

    /**
     * Lazily initialized component types map by their UI class IDs.
     */
    private static final Map<String, StyleableComponent> componentByUIClassID =
            new HashMap<String, StyleableComponent> ( values ().length );

    /**
     * Lazily initialized component types map by their UI classes.
     */
    private static final Map<Class<? extends ComponentUI>, StyleableComponent> componentByUIClass =
            new HashMap<Class<? extends ComponentUI>, StyleableComponent> ( values ().length );

    /**
     * Enum constant settings.
     */
    protected final Class<? extends JComponent> componentClass;
    protected final String uiClassID;
    protected final String defaultUIClass;
    protected final StyleId defaultStyleId;

    /**
     * Constructs a reference to component with specified settings.
     *
     * @param componentClass component class for this component type
     * @param uiClassID      UI class ID used by LookAndFeel to store various settings
     * @param defaultUIClass default UI class canonical name
     */
    private StyleableComponent ( final Class<? extends JComponent> componentClass, final String uiClassID, final String defaultUIClass )
    {
        this.componentClass = componentClass;
        this.uiClassID = uiClassID;
        this.defaultUIClass = defaultUIClass;
        this.defaultStyleId = StyleId.of ( this.name ().toLowerCase () );
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
     * Returns default style ID for this component type.
     *
     * @return default style ID for this component type
     */
    public StyleId getDefaultStyleId ()
    {
        return defaultStyleId;
    }

    /**
     * Returns component type icon.
     *
     * @return component type icon
     */
    public ImageIcon getIcon ()
    {
        if ( componentIcons.containsKey ( this ) )
        {
            return componentIcons.get ( this );
        }
        else
        {
            try
            {
                final ImageIcon icon = new ImageIcon ( StyleableComponent.class.getResource ( "icons/component/" + this + ".png" ) );
                componentIcons.put ( this, icon );
                return icon;
            }
            catch ( final Throwable e )
            {
                Log.get ().error ( "Unable to find component icon: " + this, e );
                componentIcons.put ( this, null );
                return null;
            }
        }
    }

    /**
     * Returns supported component type by component.
     *
     * @param component component to determine type for
     * @return supported component type by component
     */
    public static StyleableComponent get ( final JComponent component )
    {
        return get ( component.getUIClassID () );
    }

    /**
     * Returns supported component type by UI class ID.
     *
     * @param uiClassID UI class ID
     * @return supported component type by UI class ID
     */
    public static StyleableComponent get ( final String uiClassID )
    {
        if ( componentByUIClassID.size () == 0 )
        {
            for ( final StyleableComponent supportedComponent : values () )
            {
                componentByUIClassID.put ( supportedComponent.getUIClassID (), supportedComponent );
            }
        }
        final StyleableComponent type = componentByUIClassID.get ( uiClassID );
        if ( type == null )
        {
            throw new StyleException ( "Unsupported component UI class ID: " + uiClassID );
        }
        return type;
    }

    /**
     * Returns supported component type by component UI.
     *
     * @param ui component UI
     * @return supported component type by component UI
     */
    public static StyleableComponent get ( final ComponentUI ui )
    {
        return get ( ui.getClass () );
    }

    /**
     * Returns supported component type by component UI class.
     *
     * @param uiClass UI class
     * @return supported component type by component UI class
     */
    public static StyleableComponent get ( final Class<? extends ComponentUI> uiClass )
    {
        StyleableComponent type = componentByUIClass.get ( uiClass );
        if ( type == null )
        {
            // This method uses a different cache due to possibility of overriden UI classes usage
            // Generally it caches types returned by provided UI classes instead of default ones
            for ( final StyleableComponent supportedComponent : values () )
            {
                final Class<? extends ComponentUI> typeClass = supportedComponent.getUIClass ();
                if ( ReflectUtils.isAssignable ( typeClass, uiClass ) )
                {
                    type = supportedComponent;
                    componentByUIClass.put ( uiClass, supportedComponent );
                    break;
                }
            }
        }
        if ( type == null )
        {
            throw new StyleException ( "Unsupported component UI class: " + uiClass );
        }
        return type;
    }

    /**
     * Returns list of component types which supports painters.
     *
     * @return list of component types which supports painters
     */
    public static List<StyleableComponent> list ()
    {
        return CollectionUtils.asList ( values () );
    }
}