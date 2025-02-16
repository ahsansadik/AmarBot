package me.ahsansadik.Moderation;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Unmute extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().updateCommands()
                .addCommands(
                        Commands.slash("unmute", "unmute a user")
                                .addOptions(new OptionData(OptionType.USER, "unmute", "unmute a user", true))
                )
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("unmute")){
            Guild guild = event.getGuild();
            Member unmutedMember = event.getOption("unmute").getAsMember();
            Role muteRole = guild.getRoleById(1335887992484794400L);
            Role defaultRole = guild.getRoleById(1335887144060715039L);
            guild.removeRoleFromMember(unmutedMember, muteRole).queue();
            guild.addRoleToMember(unmutedMember,defaultRole).queue();
            event.reply("User "+unmutedMember.getAsMention()+" unmuted").queue();
        }
    }
}

