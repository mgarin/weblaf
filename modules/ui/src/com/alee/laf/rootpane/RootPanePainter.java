package com.alee.laf.rootpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.window.WebWindow;
import com.alee.managers.style.StyleException;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ProprietaryUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.util.List;

/**
 * Basic painter for {@link JRootPane} component.
 * It is used as {@link WebRootPaneUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public class RootPanePainter<C extends JRootPane, U extends WRootPaneUI, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements IRootPanePainter<C, U>
{
    /**
     * Listeners.
     */
    protected transient WindowFocusListener windowFocusListener;
    protected transient WindowStateListener frameStateListener;
    protected transient VisibilityBehavior<C> windowVisibilityBehavior;

    /**
     * Runtime variables.
     */
    protected transient boolean maximized = false;

    @Override
    protected void afterInstall ()
    {
        /**
         * Performing basic actions after installation ends.
         */
        super.afterInstall ();

        /**
         * It is very important to install decoration after all other updates.
         * Otherwise we might be missing critically-important styles here.
         */
        installWindowDecoration ();
    }

    @Override
    protected void beforeUninstall ()
    {
        /**
         * It is important to uninstall decorations before anything else.
         * Otherwise we are risking to cause issues within decoration elements.
         */
        uninstallWindowDecoration ();

        /**
         * Performing basic actions before uninstallation starts.
         */
        super.beforeUninstall ();
    }

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installWindowStateListener ();
        installVisibilityListener ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallVisibilityListener ();
        uninstallWindowStateListener ();
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected boolean usesFocusedView ()
    {
        final boolean usesFocusedView;
        final Window window = getWindow ();
        if ( window != null )
        {
            // JRootPane is not focusable by default so we need to ask window instead
            usesFocusedView = window.isFocusableWindow () && usesState ( DecorationState.focused );
        }
        else
        {
            // In case there is no window we check default conditions
            usesFocusedView = super.usesFocusedView ();
        }
        return usesFocusedView;
    }

    @Override
    protected void installFocusListeners ()
    {
        final Window window = getWindow ();
        if ( window != null && usesFocusedView () )
        {
            // Optimized focused state listeners
            focused = window.isFocused ();
            windowFocusListener = new WindowFocusListener ()
            {
                @Override
                public void windowGainedFocus ( final WindowEvent e )
                {
                    RootPanePainter.this.focusChanged ( true );
                }

                @Override
                public void windowLostFocus ( final WindowEvent e )
                {
                    RootPanePainter.this.focusChanged ( false );
                }
            };
            window.addWindowFocusListener ( windowFocusListener );
        }
        else
        {
            // Default focused state listeners
            super.installFocusListeners ();
        }
    }

    @Override
    protected void uninstallFocusListeners ()
    {
        final Window window = getWindow ();
        if ( window != null && windowFocusListener != null )
        {
            // Optimized focused state listeners
            window.removeWindowFocusListener ( windowFocusListener );
            windowFocusListener = null;
            focused = false;
        }
        else
        {
            // Default focused state listeners
            super.uninstallFocusListeners ();
        }
    }

    @Override
    protected Boolean isOpaqueUndecorated ()
    {
        // Make undecorated root panes opaque by default
        // This is important to keep any non-opaque components properly painted
        // Note that if decoration is provided root pane will not be opaque and will have painting issues on natively-decorated windows
        // todo Maybe native/non-native decoration should be taken in account when opacity is provided instead?
        return true;
    }

    @Override
    public boolean isDecorated ()
    {
        final D decoration = getDecoration ();
        return decoration != null && decoration.isVisible ();
    }

    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating focus listener
        if ( Objects.equals ( property, WebWindow.FOCUSABLE_WINDOW_STATE_PROPERTY ) )
        {
            updateFocusListeners ();
        }

        // Updating decoration according to current state
        if ( Objects.equals ( property, WebWindow.WINDOW_DECORATION_STYLE_PROPERTY ) )
        {
            // Updating decoration state first
            // This is necessary to avoid issues with state-dependant decorations
            updateDecorationState ();

            // Removing window decoration
            // This is required to disable custom window decoration upon this property change
            // We automatically rollback style here if the property was set from a custom one to JRootPane.NONE
            if ( Objects.notEquals ( oldValue, JRootPane.NONE ) && Objects.equals ( newValue, JRootPane.NONE ) &&
                    Objects.notEquals ( StyleId.get ( component ), StyleId.rootpane, StyleId.dialog, StyleId.frame ) )
            {
                // Restoring original undecorated style
                final Window window = getWindow ();
                if ( window != null )
                {
                    if ( window instanceof Frame )
                    {
                        StyleId.frame.set ( component );
                    }
                    else if ( window instanceof Dialog )
                    {
                        StyleId.dialog.set ( component );
                    }
                    else
                    {
                        StyleId.rootpane.set ( component );
                    }
                }
                else
                {
                    StyleId.rootpane.set ( component );
                }
            }

            // Installing default window decoration
            // This is an important workaround to allow Frame and Dialog decoration to be enabled according to settings
            // We only apply that workaround to non WebFrame/WebDialog based frames and dialogs which have decoration style specified
            if ( Objects.equals ( oldValue, JRootPane.NONE ) && Objects.notEquals ( newValue, JRootPane.NONE ) &&
                    Objects.equals ( StyleId.get ( component ), StyleId.rootpane, StyleId.dialog, StyleId.frame ) )
            {
                final Window window = getWindow ();
                if ( window != null )
                {
                    if ( window instanceof Frame )
                    {
                        StyleId.frameDecorated.set ( component );
                    }
                    else if ( window instanceof Dialog )
                    {
                        switch ( component.getWindowDecorationStyle () )
                        {
                            case JRootPane.COLOR_CHOOSER_DIALOG:
                            {
                                StyleId.colorchooserDialog.set ( component );
                                break;
                            }
                            case JRootPane.FILE_CHOOSER_DIALOG:
                            {
                                StyleId.filechooserDialog.set ( component );
                                break;
                            }
                            case JRootPane.INFORMATION_DIALOG:
                            {
                                StyleId.optionpaneInformationDialog.set ( component );
                                break;
                            }
                            case JRootPane.ERROR_DIALOG:
                            {
                                StyleId.optionpaneErrorDialog.set ( component );
                                break;
                            }
                            case JRootPane.QUESTION_DIALOG:
                            {
                                StyleId.optionpaneQuestionDialog.set ( component );
                                break;
                            }
                            case JRootPane.WARNING_DIALOG:
                            {
                                StyleId.optionpaneWarningDialog.set ( component );
                                break;
                            }
                            default:
                            {
                                StyleId.dialogDecorated.set ( component );
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // Window decoration type state
        states.add ( getWindowDecorationState () );

        // Window state
        if ( ui.isIconified () )
        {
            states.add ( DecorationState.iconified );
        }
        if ( ui.isMaximized () )
        {
            states.add ( DecorationState.maximized );
        }
        if ( CoreSwingUtils.isFullScreen ( component ) )
        {
            states.add ( DecorationState.fullscreen );
        }

        // Additional root pane window states
        states.addAll ( DecorationUtils.getExtraStates ( getWindow () ) );

        return states;
    }

    /**
     * Returns window decoration state.
     *
     * @return window decoration state
     */
    @NotNull
    protected String getWindowDecorationState ()
    {
        final String state;
        switch ( component.getWindowDecorationStyle () )
        {
            case JRootPane.NONE:
            {
                state = DecorationState.nativeWindow;
                break;
            }
            case JRootPane.FRAME:
            {
                state = DecorationState.frame;
                break;
            }
            case JRootPane.PLAIN_DIALOG:
            {
                state = DecorationState.dialog;
                break;
            }
            case JRootPane.COLOR_CHOOSER_DIALOG:
            {
                state = DecorationState.colorchooserDialog;
                break;
            }
            case JRootPane.FILE_CHOOSER_DIALOG:
            {
                state = DecorationState.filechooserDialog;
                break;
            }
            case JRootPane.INFORMATION_DIALOG:
            {
                state = DecorationState.informationDialog;
                break;
            }
            case JRootPane.ERROR_DIALOG:
            {
                state = DecorationState.errorDialog;
                break;
            }
            case JRootPane.QUESTION_DIALOG:
            {
                state = DecorationState.questionDialog;
                break;
            }
            case JRootPane.WARNING_DIALOG:
            {
                state = DecorationState.warningDialog;
                break;
            }
            default:
            {
                throw new StyleException ( "Unknown window decoration style: " + component.getWindowDecorationStyle () );
            }
        }
        return state;
    }

    /**
     * Installs decorations for {@link Window} that uses {@link JRootPane} represented by this painter.
     */
    protected void installWindowDecoration ()
    {
        final Window window = getWindow ();
        if ( window != null && isDecorated () )
        {
            // Enabling frame decoration
            if ( window instanceof Frame )
            {
                if ( !window.isDisplayable () )
                {
                    component.setOpaque ( false );
                    ProprietaryUtils.setWindowShape ( window, null );
                    ( ( Frame ) window ).setUndecorated ( true );
                    ProprietaryUtils.setWindowOpaque ( window, false );
                }
                if ( component.getWindowDecorationStyle () == JRootPane.NONE )
                {
                    component.setWindowDecorationStyle ( JRootPane.FRAME );
                }
            }
            else if ( window instanceof Dialog )
            {
                if ( !window.isDisplayable () )
                {
                    component.setOpaque ( false );
                    ProprietaryUtils.setWindowShape ( window, null );
                    ( ( Dialog ) window ).setUndecorated ( true );
                    ProprietaryUtils.setWindowOpaque ( window, false );
                }
                if ( component.getWindowDecorationStyle () == JRootPane.NONE )
                {
                    component.setWindowDecorationStyle ( JRootPane.PLAIN_DIALOG );
                }
            }

            // Installing UI decorations
            ui.installWindowDecorations ();
        }
    }

    /**
     * Uninstalls decoration for {@link Window} that uses {@link JRootPane} represented by this painter.
     */
    protected void uninstallWindowDecoration ()
    {
        final Window window = getWindow ();
        if ( window != null && isDecorated () )
        {
            // Uninstalling UI decorations
            ui.uninstallWindowDecorations ();

            // Disabling frame decoration
            if ( window instanceof Frame )
            {
                // Updating frame settings if it is not displayed
                if ( !window.isDisplayable () )
                {
                    ProprietaryUtils.setWindowOpaque ( window, true );
                    ( ( Frame ) window ).setUndecorated ( false );
                    component.setOpaque ( true );
                }

                // Cannot do that here as it would cause double painter uninstall call
                // This is also probably not wise to do performance-wise as it might be reused
                // if ( c.getWindowDecorationStyle () != JRootPane.NONE )
                // {
                //     c.setWindowDecorationStyle ( JRootPane.NONE );
                // }
            }
            else if ( window instanceof Dialog )
            {
                // Updating dialog settings if it is not displayed
                if ( !window.isDisplayable () )
                {
                    ProprietaryUtils.setWindowOpaque ( window, true );
                    ( ( Dialog ) window ).setUndecorated ( false );
                    component.setOpaque ( true );
                }

                // Cannot do that here as it would cause double painter uninstall call
                // This is also probably not wise to do performance-wise as it might be reused
                // if ( c.getWindowDecorationStyle () != JRootPane.NONE )
                // {
                //     c.setWindowDecorationStyle ( JRootPane.NONE );
                // }
            }
        }
    }

    /**
     * Installs {@link WindowStateListener} into {@link Window} that uses {@link JRootPane} represented by this painter.
     * It is only installed if {@link Window} is an instance of {@link Frame}, otherwise this listener is not required.
     */
    protected void installWindowStateListener ()
    {
        final Window window = getWindow ();
        if ( window instanceof Frame )
        {
            frameStateListener = new WindowStateListener ()
            {
                @Override
                public void windowStateChanged ( final WindowEvent e )
                {
                    updateDecorationState ();
                }
            };
            window.addWindowStateListener ( frameStateListener );
        }
    }

    /**
     * Uninstalls {@link WindowStateListener} from {@link Window} that uses {@link JRootPane} represented by this painter.
     * It is only uninstalled if {@link Window} is an instance of {@link Frame}, otherwise this listener is not even there.
     */
    protected void uninstallWindowStateListener ()
    {
        final Window window = getWindow ();
        if ( window != null && frameStateListener != null )
        {
            window.removeWindowStateListener ( frameStateListener );
            frameStateListener = null;
        }
    }

    /**
     * Installs {@link VisibilityBehavior} that informs {@link WebLookAndFeel} about {@link Window} visibility changes.
     */
    protected void installVisibilityListener ()
    {
        windowVisibilityBehavior = new VisibilityBehavior<C> ( component )
        {
            @Override
            protected void displayed ( @NotNull final C component )
            {
                final Window window = getWindow ();
                if ( window != null )
                {
                    WebLookAndFeel.fireWindowDisplayed ( window );
                }
            }

            @Override
            protected void hidden ( @NotNull final C component )
            {
                final Window window = getWindow ();
                if ( window != null )
                {
                    WebLookAndFeel.fireWindowHidden ( window );
                }
            }
        };
        windowVisibilityBehavior.install ();
    }

    /**
     * Uninstalls {@link VisibilityBehavior} that informs {@link WebLookAndFeel} about {@link Window} visibility changes.
     */
    protected void uninstallVisibilityListener ()
    {
        windowVisibilityBehavior.uninstall ();
        windowVisibilityBehavior = null;
    }

    /**
     * Returns {@link Window} that uses {@link JRootPane} represented by this painter.
     *
     * @return {@link Window} that uses {@link JRootPane} represented by this painter
     */
    @Nullable
    protected Window getWindow ()
    {
        final Container parent = component.getParent ();
        return parent instanceof Window ? ( Window ) parent : null;
    }
}