package me.ahsansadik;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

public class Ping extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("ping")) return;

        JDA jda = event.getJDA();
        long gatewayPing = jda.getGatewayPing();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🏓 Pong!")
                .setDescription("The bot is alive and responsive.")
                .addField("📡 WebSocket Ping", gatewayPing + " ms", true)
                .setColor(new Color(70, 130, 180))
                .setFooter("Requested by " + event.getUser().getName(), event.getUser().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
