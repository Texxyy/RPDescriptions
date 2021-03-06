package com.rpdescriptions.plugin.services;

import com.rpdescriptions.plugin.utilities.Config;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SoundService {

    @NonNull
    private final Config config;

    public void playSound(Player player, String soundRoot) {
        if (config.getBoolean(soundRoot + ".Enabled"))
            player.playSound(player.getLocation(), Sound.valueOf(config.getString(soundRoot + ".Type").toUpperCase()), 1, 1);
    }
}
