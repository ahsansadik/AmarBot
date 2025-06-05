package me.ahsansadik.Moderation.Ticket;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static me.ahsansadik.Moderation.Ticket.Ticket.ticketHandlers;

public class TicketHandler extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ticket_handler")) {
            Long roleId = event.getOption("role").getAsRole().getIdLong();
            ticketHandlers.put(event.getGuild().getIdLong(), roleId);
            event.reply("✅ Ticket handlers updated successfully!").queue();
        }
    }
}
