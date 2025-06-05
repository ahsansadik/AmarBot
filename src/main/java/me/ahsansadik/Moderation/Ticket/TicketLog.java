package me.ahsansadik.Moderation.Ticket;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TicketLog extends ListenerAdapter {
    private static final Map<Long, Long> logChannels = new HashMap<>(); // GuildID -> LogChannelID

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ticket_log")) {
            Long logChannelId = event.getOption("channel").getAsChannel().getIdLong();
            logChannels.put(event.getGuild().getIdLong(), logChannelId);
            event.reply("✅ Ticket log channel set successfully!").queue();
        }
    }

    public static void logTicketClosure(TextChannel ticketChannel, Member closedBy) {
        Long logChannelId = logChannels.get(ticketChannel.getGuild().getIdLong());
        if (logChannelId == null) return;

        TextChannel logChannel = ticketChannel.getGuild().getTextChannelById(logChannelId);
        if (logChannel == null) return;

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("📌 Ticket Closed")
                .setColor(Color.RED)
                .setDescription("A ticket has been closed.")
                .addField("Ticket Name", ticketChannel.getName(), false)
                .addField("Closed By", closedBy.getAsMention(), false)
                .setFooter("Ticket System", closedBy.getUser().getAvatarUrl());

        logChannel.sendMessageEmbeds(embed.build()).queue();
    }
}