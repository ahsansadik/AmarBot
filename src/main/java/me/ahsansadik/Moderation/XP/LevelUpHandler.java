package me.ahsansadik.Moderation.XP;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LevelUpHandler extends ListenerAdapter {

    private final File storage = new File("levelup_channels.json");
    private final Map<String, String> levelupChannels = new HashMap<>();
    private final Gson gson = new Gson();

    public LevelUpHandler() {
        loadChannelSettings();
    }

    public void handleLevelUp(Guild guild, Member member, int newLevel) {
        String channelId = levelupChannels.get(guild.getId());
        if (channelId == null) return;

        TextChannel channel = guild.getTextChannelById(channelId);
        if (channel == null) return;

        try {
            BufferedImage img = new BufferedImage(800, 240, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();

            // Gradient background
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            GradientPaint bg = new GradientPaint(0, 0, new Color(10, 15, 30), 0, 240, new Color(15, 23, 42));
            g.setPaint(bg);
            g.fillRect(0, 0, 800, 240);

            // Border
            g.setColor(new Color(50, 75, 150));
            g.setStroke(new BasicStroke(4));
            g.drawRoundRect(2, 2, 796, 236, 30, 30);

            // Avatar with circle
            BufferedImage avatarRaw = ImageIO.read(new URL(member.getEffectiveAvatarUrl() + "?size=128"));
            BufferedImage avatar = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gAvatar = avatar.createGraphics();
            gAvatar.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 128, 128));
            gAvatar.drawImage(avatarRaw, 0, 0, 128, 128, null);
            gAvatar.dispose();
            g.setColor(Color.WHITE);
            g.fillOval(30 - 5, 55 - 5, 138, 138);
            g.drawImage(avatar, 30, 55, null);

            // Message
            g.setFont(new Font("Segoe UI", Font.BOLD, 36));
            g.setColor(Color.WHITE);
            g.drawString("Congratulations!", 180, 85);

            g.setFont(new Font("Segoe UI", Font.PLAIN, 28));
            g.setColor(new Color(0, 255, 255));
            g.drawString(member.getEffectiveName() + " reached Level " + newLevel + "!", 180, 135);

            // XP Bar (cosmetic)
            int barX = 180, barY = 170, barWidth = 500, barHeight = 35;
            g.setColor(new Color(30, 30, 50));
            g.fillRoundRect(barX, barY, barWidth, barHeight, 20, 20);
            GradientPaint fill = new GradientPaint(barX, barY, new Color(0, 255, 255), barX + barWidth, barY, new Color(170, 0, 255));
            g.setPaint(fill);
            g.fillRoundRect(barX, barY, barWidth, barHeight, 20, 20);

            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());

            channel.sendFiles(FileUpload.fromData(is, "levelup.png")).queue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("set_levelup_channel")) return;

        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("❌ This command can only be used in a server.").setEphemeral(true).queue();
            return;
        }

        TextChannel current = event.getChannel().asTextChannel();
        levelupChannels.put(guild.getId(), current.getId());
        saveChannelSettings();

        event.reply("✅ Level-up messages will now be sent to this channel.").queue();
    }

    private void loadChannelSettings() {
        if (!storage.exists()) return;
        try (Reader reader = new FileReader(storage)) {
            Map<String, String> map = gson.fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
            if (map != null) levelupChannels.putAll(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveChannelSettings() {
        try (Writer writer = new FileWriter(storage)) {
            gson.toJson(levelupChannels, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
