package me.ahsansadik.Moderation.Ticket;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Ticket extends ListenerAdapter {
    private static int ticketCounter = 1;
    public static final Map<Long, Long> ticketHandlers = new HashMap<>(); // GuildID -> RoleID

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ticket")) {
            Member member = event.getMember();
            if (member == null) return;

            String channelName = "ticket-" + ticketCounter++;
            event.getGuild().createTextChannel(channelName)
                    .addPermissionOverride(member, Permission.VIEW_CHANNEL.getRawValue(), 0)
                    .queue(channel -> event.reply("✅ Ticket created: " + channel.getAsMention()).queue());
        }
    }
}
