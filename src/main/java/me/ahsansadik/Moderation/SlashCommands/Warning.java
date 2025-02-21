package me.ahsansadik.Moderation.SlashCommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Warning extends ListenerAdapter {
    private static final Map<Long, List<String>> warnings = new HashMap<>();


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("warn")) return;

        Member target = event.getOption("user").getAsMember();
        String reason = event.getOption("reason") != null ? event.getOption("reason").getAsString() : "No reason provided.";

        if (target == null) {
            event.reply("User not found!").setEphemeral(true).queue();
            return;
        }

        warnings.putIfAbsent(target.getIdLong(), new ArrayList<>());
        warnings.get(target.getIdLong()).add(reason);

        event.replyEmbeds(new EmbedBuilder()
                .setColor(0xFF0000) // Red color
                .setTitle("⚠ Warning Issued")
                .setDescription("**" + target.getEffectiveName() + "** has been warned.")
                .addField("Reason", reason, false)
                .build()
        ).queue();
    }

    public static Map<Long, List<String>> getWarnings() {
        return warnings;
    }
}

