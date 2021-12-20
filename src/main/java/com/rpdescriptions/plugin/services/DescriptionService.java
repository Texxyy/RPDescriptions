package com.rpdescriptions.plugin.services;

import com.rpdescriptions.plugin.misc.Config;
import com.rpdescriptions.plugin.misc.Utils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class DescriptionService {

    @NonNull
    private final Config databaseConfig;

    public boolean hasDescription(Player player) {
        return databaseConfig.isSet("Descriptions." + player.getUniqueId());
    }

    public String getDescription(Player player) {
        if (!hasDescription(player)) return Utils.color("&cNo description set!");
        String description = databaseConfig.getString("Descriptions." + player.getUniqueId());
        return databaseConfig.getBoolean("General.Colored-Descriptions") ? Utils.color(description) : description;
    }
}
