package me.ahsansadik;

import me.ahsansadik.Moderation.Features.Announcement;
import me.ahsansadik.Moderation.Features.BadWordFilter;
import me.ahsansadik.Moderation.Features.LogChannel;
import me.ahsansadik.Moderation.Features.WelcomeText;
import me.ahsansadik.Moderation.Response.ChatGPT;
import me.ahsansadik.Moderation.Response.OpenAIChat;
import me.ahsansadik.Moderation.SlashCommands.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.List;

public class Main {

    public static JDABuilder getJda() {
        return jda;
    }

    private static JDABuilder jda;

    public static void main(String[] args) {
        jda = JDABuilder.createDefault("TOKEn")
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new DefaultRoleAssign())
                .addEventListeners(new WelcomeText())
                .addEventListeners(new Ban())
                .addEventListeners(new Unban())
                .addEventListeners(new BanList())
                .addEventListeners(new Mute())
                .addEventListeners(new Unmute())
                .addEventListeners(new Warning())
                .addEventListeners(new WarningsCommand())
                .addEventListeners(new ClearWarningsCommand())
                .addEventListeners(new SlowModeCommand())
                .addEventListeners(new Kick())
                .addEventListeners(new BadWordFilter())
                .addEventListeners(new Announcement())
                .addEventListeners(new LogChannel());


        // Build JDA instance

        try {
            jda.build().awaitReady().getGuilds().forEach(Main::registerCommands);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void registerCommands(Guild guild) {
        List<CommandData> commands = List.of(
                Commands.slash("ban", "Ban a user")
                        .addOptions(new OptionData(OptionType.USER, "user", "User to ban", true))
                        .addOptions(new OptionData(OptionType.INTEGER, "duration", "Duration of ban in days (0 for permanent)", true))
                        .addOptions(new OptionData(OptionType.STRING, "reason", "Reason for ban", false)),

                Commands.slash("banlist", "Shows a list of banned users"),

                Commands.slash("unban", "Unban a user")
                        .addOptions(new OptionData(OptionType.STRING, "username", "Name of the user to unban", true)),

                Commands.slash("mute", "Muting User")
                        .addOptions(new OptionData(OptionType.USER, "mute", "Muting a user", true)),

                Commands.slash("unmute", "Unmute a user")
                        .addOptions(new OptionData(OptionType.USER, "unmute", "unmute a user", true)),

                Commands.slash("kick", "Kick a user")
                        .addOptions(new OptionData(OptionType.USER, "kick", "User to kick", true)),

                Commands.slash("warn", "Warn a user")
                        .addOptions(new OptionData(OptionType.USER, "user", "The user to warn", true))
                        .addOptions(new OptionData(OptionType.STRING, "reason", "Reason for warning", false)),

                Commands.slash("warnings", "Display warnings of a user")
                        .addOptions(new OptionData(OptionType.USER, "user", "The user whose warnings you want to see", true)),

                Commands.slash("clear_warnings", "Clear warnings of a user")
                        .addOptions(new OptionData(OptionType.USER, "user", "The user whose warnings you want to clear", true)),

                Commands.slash("default_role", "Set the default role for new members")
                        .addOptions(new OptionData(OptionType.ROLE, "role", "Role to be added", true)),

                Commands.slash("set_welcome_channel", "Set the default channel for welcoming new members"),

                Commands.slash("set_log_channel", "Set the log channel for logging any events in the guild"),

                Commands.slash("announcement", "Create a server announcement"),

                /* Commands.slash("chatgpt", "Send ChatGPT Prompts")
                        .addOptions(new OptionData(OptionType.STRING, "prompt", "Send a prompt", true)),
                */

                Commands.slash("slow_mode", "Sets slow mode for this channel")
                        .addOptions(new OptionData(OptionType.INTEGER, "duration", "Slow mode duration in seconds (0-21600)", true))
        );

        guild.updateCommands().addCommands(commands).queue();
    }
}