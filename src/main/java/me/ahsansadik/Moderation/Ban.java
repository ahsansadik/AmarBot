package me.ahsansadik.Moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Duration;
import java.util.Collections;

public class Ban extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(1332267587152773142L);
        if (guild != null) {  // Ensure guild is not null
            guild.upsertCommand("ban", "Ban a user")
                    .addOptions(new OptionData(OptionType.USER, "user", "User to ban", true))
                    .addOptions(new OptionData(OptionType.INTEGER, "duration", "Duration of ban in days (0 for permanent)", true))
                    .addOptions(new OptionData(OptionType.STRING, "reason", "Reason for ban", false))
                    .queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("ban")) return;

        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("This command can only be used in a server!").setEphemeral(true).queue();
            return;
        }

        Member member = event.getMember();
        if (member == null || !member.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("You do not have permission to ban members.").setEphemeral(true).queue();
            return;
        }

        if (!guild.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("I do not have permission to ban members.").setEphemeral(true).queue();
            return;
        }

        User banUser = event.getOption("user").getAsUser();
        int duration = event.getOption("duration").getAsInt();
        String reason = event.getOption("reason") != null ? event.getOption("reason").getAsString() : "No reason provided";

        Duration banDuration = (duration == 0) ? null : Duration.ofDays(duration);

        guild.ban(Collections.singleton(UserSnowflake.fromId(banUser.getId())), banDuration)
                .reason(reason)
                .queue(
                        success -> {
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle("User Banned")
                                    .setDescription("🔨 " + banUser.getName() + " has been banned for " +
                                            (banDuration == null ? "permanently" : duration + " days") + ".")
                                    .setColor(Color.RED);
                            event.replyEmbeds(embed.build()).queue();
                        },
                        error -> event.reply("Failed to ban the user: " + error.getMessage()).setEphemeral(true).queue()
                );
    }
}
