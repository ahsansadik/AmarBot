package me.ahsansadik.Moderation.Ticket;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TicketClose extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ticket_close")) {
            TextChannel channel = event.getChannel().asTextChannel();
            if (channel.getName().startsWith("ticket-")) {
                channel.delete().queue();
                event.reply("✅ Ticket closed successfully!").queue();
            } else {
                event.reply("❌ This is not a ticket channel!").setEphemeral(true).queue();
            }
        }
    }
}

