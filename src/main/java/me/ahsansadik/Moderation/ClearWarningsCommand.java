package me.ahsansadik.Moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.List;

public class ClearWarningsCommand extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(1332267587152773142L);
        if (guild != null) {
            guild.upsertCommand("clear_warnings", "Clears all warnings for a user")
                    .addOptions(new OptionData(OptionType.USER, "user", "The user whose warnings you want to clear", true))
                    .queue();
        }
    }

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

