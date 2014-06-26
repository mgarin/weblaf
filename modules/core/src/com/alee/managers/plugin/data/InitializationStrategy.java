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

package com.alee.managers.plugin.data;

/**
 * Plugin initialization strategy.
 * Used to determine plugins initialization sequence.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 */

public class InitializationStrategy
{
    /**
     * Custom ID for "all" plugins.
     */
    public static final String ALL_ID = "all";

    /**
     * Strategy type.
     */
    private final StrategyType type;

    /**
     * Plugin ID.
     */
    private final String id;

    /**
     * Constructs new initialization strategy.
     *
     * @param type strategy type
     * @param id   plugin ID
     */
    private InitializationStrategy ( final StrategyType type, final String id )
    {
        super ();
        this.type = type;
        this.id = id;
    }

    /**
     * Returns plugin initialization strategy type.
     *
     * @return plugin initialization strategy type
     */
    public StrategyType getType ()
    {
        return type;
    }

    /**
     * Returns strategy plugin ID.
     *
     * @return strategy plugin ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Returns default initialization strategy.
     *
     * @return default initialization strategy
     */
    public static InitializationStrategy any ()
    {
        return new InitializationStrategy ( StrategyType.any, ALL_ID );
    }

    /**
     * Plugin must be initialized strictly after all other plugins.
     *
     * @return "after all" initialization strategy
     */
    public static InitializationStrategy afterAll ()
    {
        return after ( ALL_ID );
    }

    /**
     * Plugin must be initialized strictly before all other plugins.
     *
     * @return "before all" initialization strategy
     */
    public static InitializationStrategy beforeAll ()
    {
        return before ( ALL_ID );
    }

    /**
     * Plugin must be initialized strictly after specified plugin.
     *
     * @param pluginId plugin ID
     * @return "after plugin" initialization strategy
     */
    public static InitializationStrategy after ( final String pluginId )
    {
        return new InitializationStrategy ( StrategyType.after, pluginId );
    }

    /**
     * Plugin must be initialized strictly before specified plugin.
     *
     * @param pluginId plugin ID
     * @return "before plugin" initialization strategy
     */
    public static InitializationStrategy before ( final String pluginId )
    {
        return new InitializationStrategy ( StrategyType.before, pluginId );
    }
}