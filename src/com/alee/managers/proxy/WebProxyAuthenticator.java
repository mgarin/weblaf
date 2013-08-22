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

package com.alee.managers.proxy;

import com.alee.extended.optionpane.WebExtendedOptionPane;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.language.LanguageManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.event.KeyEvent;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * This custom proxy authenticator provides an authentication dialog with proxy host, port, login and password fields.
 * It will also remember entered proxy settings if user asks to.
 *
 * @author Mikle Garin
 */

public class WebProxyAuthenticator extends Authenticator
{
    /**
     * Authentication dialog icon.
     */
    public static final ImageIcon AUTH_ICON = new ImageIcon ( WebProxyAuthenticator.class.getResource ( "icons/auth.png" ) );

    /**
     * Authentication dialog.
     */
    private AuthDialog authDialog = null;

    /**
     * Returns custom password authentication.
     *
     * @return custom password authentication
     */
    @Override
    protected PasswordAuthentication getPasswordAuthentication ()
    {
        return getProxyAuthentification ();
    }

    /**
     * Returns new custom password authentication.
     * This method might also pop authentication dialog if needed.
     *
     * @return new custom password authentication
     */
    private PasswordAuthentication getProxyAuthentification ()
    {
        // This method cannot wait for auth dialog since it is mostly called from EDT
        ProxySettings proxySettings = ProxyManager.getProxySettings ().clone ();
        PasswordAuthentication auth;
        if ( authDialog != null )
        {
            // Ignore while auth dialog is showing
            auth = null;
        }
        else if ( proxySettings.isUseProxyAuthentification () )
        {
            // Creating auth from settings
            auth = createAuthentification ( proxySettings );
        }
        else
        {
            // Ask user for login/pass
            authDialog = new AuthDialog ( proxySettings );
            authDialog.setVisible ( true );

            if ( authDialog.getResult () == WebOptionPane.OK_OPTION )
            {
                // Update settings
                proxySettings.setUseProxyAuthentification ( true );
                proxySettings.setProxyLogin ( authDialog.getLogin () );
                proxySettings.setProxyPassword ( new String ( authDialog.getPassword () ) );

                // Setup updated settings
                ProxyManager.setProxySettings ( proxySettings, authDialog.isSaveSettings () );

                // Determined authentification
                auth = createAuthentification ( proxySettings );
            }
            else
            {
                ProxySettings updatedSettings = ProxyManager.getProxySettings ();
                if ( updatedSettings.isUseProxyAuthentification () )
                {
                    // Settings came from somewhere else
                    auth = createAuthentification ( updatedSettings );
                }
                else
                {
                    // Null authentification
                    auth = null;
                }
            }

            authDialog = null;
        }
        return auth;
    }

    /**
     * Returns newly created custom password authentication.
     *
     * @param proxySettings proxy settings
     * @return newly created custom password authentication
     */
    private PasswordAuthentication createAuthentification ( ProxySettings proxySettings )
    {
        return new PasswordAuthentication ( proxySettings.getProxyLogin (), proxySettings.getProxyPassword ().toCharArray () );
    }

    /**
     * Proxy authentication dialog.
     */
    private class AuthDialog extends WebExtendedOptionPane
    {
        /**
         * Proxy login field.
         */
        private WebTextField loginField;

        /**
         * Proxy password field.
         */
        private WebPasswordField passwordField;

        /**
         * Save settings check box.
         */
        private WebCheckBox saveSettings;

        /**
         * Constructs new proxy authentication dialog based on specified proxy settings.
         *
         * @param proxySettings proxy settings
         */
        public AuthDialog ( ProxySettings proxySettings )
        {
            super ( SwingUtils.getActiveWindow (), null, null, LanguageManager.get ( "weblaf.proxy.auth.title" ),
                    WebOptionPane.OK_CANCEL_OPTION, WebOptionPane.PLAIN_MESSAGE );


            JPanel authPanel = new JPanel ();
            authPanel.setOpaque ( false );
            TableLayout authLayout = new TableLayout ( new double[][]{ { TableLayout.PREFERRED, TableLayout.FILL },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED } } );
            authLayout.setHGap ( 4 );
            authLayout.setVGap ( 4 );
            authPanel.setLayout ( authLayout );


            WebLabel hostLabel = new WebLabel ();
            hostLabel.setLanguage ( "weblaf.proxy.auth.host" );
            hostLabel.setHorizontalAlignment ( WebLabel.RIGHT );
            hostLabel.setEnabled ( false );
            authPanel.add ( hostLabel, "0,0" );

            WebTextField hostValue = new WebTextField ( proxySettings.getProxyHost (), 10 );
            hostValue.setEnabled ( false );
            hostValue.putClientProperty ( GroupPanel.FILL_CELL, true );

            WebLabel portLabel = new WebLabel ();
            portLabel.setLanguage ( "weblaf.proxy.auth.port" );
            portLabel.setHorizontalAlignment ( WebLabel.RIGHT );
            portLabel.setEnabled ( false );

            WebTextField portValue = new WebTextField ( proxySettings.getProxyPort () );
            portValue.setEnabled ( false );

            authPanel.add ( new GroupPanel ( 2, hostValue, portLabel, portValue ), "1,0" );


            authPanel.add ( new WebSeparator ( WebSeparator.HORIZONTAL ), "0,1,1,1" );


            WebLabel loginLabel = new WebLabel ();
            loginLabel.setLanguage ( "weblaf.proxy.auth.login" );
            loginLabel.setHorizontalAlignment ( WebLabel.RIGHT );
            authPanel.add ( loginLabel, "0,2" );

            loginField = new WebTextField ( 12 );
            loginField.addAncestorListener ( new AncestorAdapter ()
            {
                @Override
                public void ancestorAdded ( AncestorEvent event )
                {
                    loginField.requestFocusInWindow ();
                }
            } );
            authPanel.add ( loginField, "1,2" );


            WebLabel passLabel = new WebLabel ();
            passLabel.setLanguage ( "weblaf.proxy.auth.pass" );
            passLabel.setHorizontalAlignment ( WebLabel.RIGHT );
            authPanel.add ( passLabel, "0,3" );

            passwordField = new WebPasswordField ( 12 );
            authPanel.add ( passwordField, "1,3" );


            HotkeyManager.registerHotkey ( authPanel, authPanel, Hotkey.ENTER, new HotkeyRunnable ()
            {
                @Override
                public void run ( KeyEvent e )
                {
                    clickOk ();
                }
            } );
            HotkeyManager.registerHotkey ( authPanel, authPanel, Hotkey.ESCAPE, new HotkeyRunnable ()
            {
                @Override
                public void run ( KeyEvent e )
                {
                    clickCancel ();
                }
            } );

            SwingUtils.equalizeComponentsHeights ( hostValue, loginField, passwordField );

            setContent ( authPanel );


            saveSettings = new WebCheckBox ( "Save proxy settings" );
            saveSettings.registerSettings ( ProxyManager.SETTINGS_GROUP, ProxyManager.SAVE_SETTINGS, true );
            setSpecialComponent ( saveSettings );
        }

        /**
         * Returns proxy login.
         *
         * @return proxy login
         */
        public String getLogin ()
        {
            return loginField.getText ();
        }

        /**
         * Returns proxy password.
         *
         * @return proxy password
         */
        public char[] getPassword ()
        {
            return passwordField.getPassword ();
        }

        /**
         * Returns whether save proxy settings or not.
         *
         * @return true if save proxy settings, false otherwise
         */
        public boolean isSaveSettings ()
        {
            return saveSettings.isSelected ();
        }

        /**
         * Returns large dialog icon.
         *
         * @param messageType dialog message type
         * @return large dialog icon
         */
        @Override
        protected ImageIcon getLargeIcon ( int messageType )
        {
            return AUTH_ICON;
        }
    }
}