package me.ahsansadik.Moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.Map;

public class WarningsCommand extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("warnings")) return;

        User target = event.getOption("user").getAsUser();
        Map<Long, List<String>> warnings = Warning.getWarnings();

        if (!warnings.containsKey(target.getIdLong()) || warnings.get(target.getIdLong()).isEmpty()) {
            event.reply("✅ **" + target.getName() + "** has no warnings.").queue();
            return;
        }

        List<String> userWarnings = warnings.get(target.getIdLong());
        StringBuilder response = new StringBuilder("⚠ **Warnings for " + target.getName() + "**:\n");
        for (int i = 0; i < userWarnings.size(); i++) {
            response.append(i + 1).append(". ").append(userWarnings.get(i)).append("\n");
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(0xFFA500) // Orange color
                .setTitle("⚠ Warnings for " + target.getName());

        for (int i = 0; i < userWarnings.size(); i++) {
            embed.addField("Warning #" + (i + 1), userWarnings.get(i), false);
        }

        event.replyEmbeds(embed.build()).queue();

    }
}
