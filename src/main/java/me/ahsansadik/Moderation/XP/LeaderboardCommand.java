package me.ahsansadik.Moderation.XP;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("leaderboard")) return;

        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("❌ This command can only be used in a server.").setEphemeral(true).queue();
            return;
        }

        Map<String, XPManager.UserData> rawData = XPManager.getGuildXPData(guild.getId());

        // Filter users with at least 1 XP or level
        List<Map.Entry<String, XPManager.UserData>> filtered = rawData.entrySet().stream()
                .filter(entry -> entry.getValue().xp > 0 || entry.getValue().level > 0)
                .sorted((a, b) -> Integer.compare(
                        b.getValue().level * 1000 + b.getValue().xp,
                        a.getValue().level * 1000 + a.getValue().xp
                ))
                .limit(10)
                .collect(Collectors.toList());

        int height = 60 + filtered.size() * 75;
        BufferedImage image = new BufferedImage(900, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        GradientPaint bg = new GradientPaint(0, 0, new Color(10, 15, 30), 0, height, new Color(15, 23, 42));
        g.setPaint(bg);
        g.fillRect(0, 0, image.getWidth(), height);

        g.setFont(new Font("Segoe UI", Font.BOLD, 38));
        g.setColor(Color.WHITE);
        g.drawString("🏆 Leaderboard", 320, 50);

        int y = 100;
        int rank = 1;

        for (Map.Entry<String, XPManager.UserData> entry : filtered) {
            Member member = guild.getMemberById(entry.getKey());
            if (member == null) continue;

            XPManager.UserData userData = entry.getValue();

            // Draw avatar
            try {
                BufferedImage avatarRaw = ImageIO.read(new URL(member.getEffectiveAvatarUrl() + "?size=64"));
                BufferedImage avatar = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gAvatar = avatar.createGraphics();
                gAvatar.setClip(new Ellipse2D.Float(0, 0, 64, 64));
                gAvatar.drawImage(avatarRaw, 0, 0, 64, 64, null);
                gAvatar.dispose();

                g.setColor(Color.WHITE);
                g.fillOval(30, y - 10, 70, 70);
                g.drawImage(avatar, 35, y - 5, null);
            } catch (Exception ignored) {}

            // Name & level
            g.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            g.setColor(Color.WHITE);
            g.drawString("#" + rank + " " + member.getEffectiveName(), 120, y + 20);
            g.setColor(Color.CYAN);
            g.drawString("Level " + userData.level + " | XP: " + userData.xp, 120, y + 50);

            // XP bar
            int barX = 450, barY = y + 15, barWidth = 400, barHeight = 20;
            g.setColor(new Color(30, 30, 50));
            g.fillRoundRect(barX, barY, barWidth, barHeight, 20, 20);

            int nextXP = 5 * (userData.level * userData.level) + 50 * userData.level + 100;
            int filled = (int) ((userData.xp / (double) nextXP) * barWidth);

            g.setPaint(new GradientPaint(barX, barY, new Color(0, 255, 255), barX + filled, barY, new Color(170, 0, 255)));
            g.fillRoundRect(barX, barY, filled, barHeight, 20, 20);

            y += 75;
            rank++;
        }

        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            event.replyFiles(FileUpload.fromData(is, "leaderboard.png")).queue();
        } catch (Exception e) {
            event.reply("❌ Failed to generate leaderboard image.").queue();
            e.printStackTrace();
        }
    }
}
