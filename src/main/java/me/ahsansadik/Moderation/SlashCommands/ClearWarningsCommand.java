package me.ahsansadik.Moderation.SlashCommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.User;

import java.util.Map;
import java.util.List;

public class ClearWarningsCommand extends ListenerAdapter {

   @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("clear_warnings")) return;

        User target = event.getOption("user").getAsUser();
        Map<Long, List<String>> warnings = Warning.getWarnings();

        if (!warnings.containsKey(target.getIdLong()) || warnings.get(target.getIdLong()).isEmpty()) {
            event.reply("✅ **" + target.getName() + "** has no warnings to clear.").queue();
            return;
        }

        warnings.remove(target.getIdLong());
        event.replyEmbeds(new EmbedBuilder()
                .setColor(0x00FF00) // Green color
                .setTitle("🗑 Warnings Cleared")
                .setDescription("All warnings for **" + target.getName() + "** have been removed.")
                .build()
        ).queue();

    }
}

