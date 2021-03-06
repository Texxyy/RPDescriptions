package com.rpdescriptions.plugin.listeners;

import com.rpdescriptions.plugin.cooldown.CooldownManager;
import com.rpdescriptions.plugin.cooldown.TimeSpan;
import com.rpdescriptions.plugin.utilities.Config;
import com.rpdescriptions.plugin.services.DescriptionService;
import com.rpdescriptions.plugin.services.SoundService;
import com.rpdescriptions.plugin.services.message.Message;
import com.rpdescriptions.plugin.services.message.MessageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class InteractAtEntityListener implements Listener {

    @NonNull
    private final Config             config;
    @NonNull
    private final SoundService       soundService;
    @NonNull
    private final MessageService     messageService;
    @NonNull
    private final DescriptionService descriptionService;

    @EventHandler
    private void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked().getType().equals(EntityType.PLAYER) && config.getBoolean("General.Player-Click.Enabled"))) return;
        Player target = (Player) event.getRightClicked();
        if (target.hasMetadata("NPC")) return;
        for (String worlds : config.getStringList("General.Player-Click.Disabled-Worlds")) {
            if (target.getWorld().getName().equalsIgnoreCase(worlds)) return;
        }
        Player player = event.getPlayer();
        if (CooldownManager.checkCooldown("click_" + player.getUniqueId())) {
            soundService.playSound(player, "General.Player-Click.Sound");
            messageService.sendMessage(player,
                                        Message.GENERAL_PLAYER_CLICK_MESSAGES,
                                        (s) -> s.replace("%username%", target.getName())
                                                .replace("%description%", descriptionService.getDescription(target)));
            CooldownManager.setNow("click_" + player.getUniqueId(), new TimeSpan(config.getInt("General.Player-Click.Cooldown-Ticks") * 50L, TimeUnit.MILLISECONDS));
        }
    }
}
