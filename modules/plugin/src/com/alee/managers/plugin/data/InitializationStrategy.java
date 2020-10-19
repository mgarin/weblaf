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

import com.alee.api.annotations.NotNull;
import com.alee.managers.plugin.Plugin;

/**
 * Plugin initialization strategy.
 * Used to determine plugins initialization sequence.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 */
public final class InitializationStrategy
{
    /**
     * todo 1. This is a pretty bad implementation of the plugin loading order, it has many restrictions and should be reworked
     */

    /**
     * Custom identifier for "all" plugins.
     */
    public static final String ALL_ID = "all";

    /**
     * {@link StrategyType}.
     */
    @NotNull
    private final StrategyType type;

    /**
     * {@link Plugin} identifier or {@link #ALL_ID}.
     */
    @NotNull
    private final String pluginId;

    /**
     * Constructs new {@link InitializationStrategy}.
     *
     * @param type     {@link StrategyType}
     * @param pluginId {@link Plugin} identifier
     */
    private InitializationStrategy ( @NotNull final StrategyType type, @NotNull final String pluginId )
    {
        this.type = type;
        this.pluginId = pluginId;
    }

    /**
     * Returns {@link StrategyType}.
     *
     * @return {@link StrategyType}
     */
    @NotNull
    public StrategyType getType ()
    {
        return type;
    }

    /**
     * Returns {@link Plugin} identifier or {@link #ALL_ID}.
     *
     * @return {@link Plugin} identifier or {@link #ALL_ID}
     */
    @NotNull
    public String getPluginId ()
    {
        return pluginId;
    }

    /**
     * Returns default {@link InitializationStrategy}.
     *
     * @return default {@link InitializationStrategy}
     */
    @NotNull
    public static InitializationStrategy any ()
    {
        return new InitializationStrategy ( StrategyType.any, ALL_ID );
    }

    /**
     * Returns {@link InitializationStrategy} under which {@link Plugin} must be initialized strictly after all other {@link Plugin}s.
     *
     * @return {@link InitializationStrategy} under which {@link Plugin} must be initialized strictly after all other {@link Plugin}s
     */
    @NotNull
    public static InitializationStrategy afterAll ()
    {
        return after ( ALL_ID );
    }

    /**
     * Returns {@link InitializationStrategy} under which {@link Plugin} must be initialized strictly before all other {@link Plugin}s.
     *
     * @return {@link InitializationStrategy} under which {@link Plugin} must be initialized strictly before all other {@link Plugin}s
     */
    @NotNull
    public static InitializationStrategy beforeAll ()
    {
        return before ( ALL_ID );
    }

    /**
     * Returns {@link InitializationStrategy} under which plugin must be initialized strictly after other specified {@link Plugin}.
     *
     * @param pluginId identifier of the other {@link Plugin} to initialize {@link Plugin} using the strategy after
     * @return {@link InitializationStrategy} under which plugin must be initialized strictly after other specified {@link Plugin}
     */
    @NotNull
    public static InitializationStrategy after ( @NotNull final String pluginId )
    {
        return new InitializationStrategy ( StrategyType.after, pluginId );
    }

    /**
     * Returns {@link InitializationStrategy} under which {@link Plugin} must be initialized strictly before other specified {@link Plugin}.
     *
     * @param pluginId identifier of the other {@link Plugin} to initialize {@link Plugin} using the strategy before
     * @return {@link InitializationStrategy} under which {@link Plugin} must be initialized strictly before other specified {@link Plugin}
     */
    @NotNull
    public static InitializationStrategy before ( @NotNull final String pluginId )
    {
        return new InitializationStrategy ( StrategyType.before, pluginId );
    }
}