package me.ahsansadik.Moderation.XP;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class XPCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("xp")) return;

        if (!event.isFromGuild()) {
            event.reply("❌ This command can only be used in a server.").setEphemeral(true).queue();
            return;
        }

        String guildId = event.getGuild().getId();
        User user = event.getUser();

        XPManager.UserData data = XPManager.getXP(guildId, user.getId());

        int nextXP = 5 * (data.level * data.level) + 50 * data.level + 100;
        double percent = Math.min(1.0, (double) data.xp / nextXP);

        try {
            BufferedImage card = new BufferedImage(800, 240, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = card.createGraphics();

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            GradientPaint backgroundGradient = new GradientPaint(
                    0, 0, new Color(15, 23, 42),
                    0, 240, new Color(10, 15, 30)
            );
            g.setPaint(backgroundGradient);
            g.fillRect(0, 0, 800, 240);

            g.setColor(new Color(50, 75, 150));
            g.setStroke(new BasicStroke(4));
            g.drawRoundRect(2, 2, 796, 236, 30, 30);

            BufferedImage avatarRaw = ImageIO.read(new URL(user.getEffectiveAvatarUrl() + "?size=128"));
            BufferedImage avatar = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);

            Graphics2D gAvatar = avatar.createGraphics();
            gAvatar.setClip(new Ellipse2D.Float(0, 0, 128, 128));
            gAvatar.drawImage(avatarRaw, 0, 0, 128, 128, null);
            gAvatar.dispose();

            int avatarX = 30, avatarY = 55;

            g.setColor(Color.WHITE);
            g.fillOval(avatarX - 5, avatarY - 5, 138, 138);
            g.drawImage(avatar, avatarX, avatarY, null);

            Font usernameFont = new Font("Segoe UI", Font.BOLD, 30);
            Font infoFont = new Font("Segoe UI", Font.PLAIN, 22);
            g.setFont(usernameFont);

            int textX = 180;
            String username = user.getName();

            // Shadow for username
            g.setColor(new Color(0, 0, 0, 150));
            g.drawString(username, textX + 3, 80 + 3);

            // Username
            g.setColor(Color.WHITE);
            g.drawString(username, textX, 80);

            g.setFont(infoFont);
            String levelText = "Level: " + data.level;
            String xpText = "XP: " + data.xp + " / " + nextXP;

            // Shadow for level and XP
            g.setColor(new Color(0, 0, 0, 130));
            g.drawString(levelText, textX + 2, 125 + 2);
            g.drawString(xpText, textX + 2, 155 + 2);

            // Level and XP text
            g.setColor(new Color(0, 255, 255));
            g.drawString(levelText, textX, 125);
            g.drawString(xpText, textX, 155);

            // Get user rank in guild
            int rank = getUserRank(guildId, user.getId());

            g.setFont(new Font("Segoe UI", Font.BOLD, 28));
            String rankText = "Server Rank: #" + rank;

            // Shadow for rank
            g.setColor(new Color(0, 0, 0, 130));
            g.drawString(rankText, 550 + 2, 125 + 2);

            // Rank text
            g.setColor(new Color(255, 215, 0));
            g.drawString(rankText, 550, 125);

            // XP Bar background
            int barX = 180, barY = 180, barWidth = 500, barHeight = 35;
            RoundRectangle2D barBackground = new RoundRectangle2D.Float(barX, barY, barWidth, barHeight, 20, 20);

            g.setColor(new Color(30, 30, 50));
            g.fill(barBackground);

            // XP Bar fill
            int fillWidth = (int) (barWidth * percent);
            if (fillWidth > 0) {
                GradientPaint barGradient = new GradientPaint(
                        barX, barY, new Color(0, 255, 255),
                        barX + fillWidth, barY, new Color(170, 0, 255)
                );
                g.setPaint(barGradient);

                RoundRectangle2D barFill = new RoundRectangle2D.Float(barX, barY, fillWidth, barHeight, 20, 20);
                g.fill(barFill);
            }

            // XP Bar border with transparency
            g.setColor(new Color(0, 255, 255, 80));
            g.setStroke(new BasicStroke(10));
            g.drawRoundRect(barX - 3, barY - 3, barWidth + 6, barHeight + 6, 25, 25);

            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(card, "png", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());

            event.reply("📛 XP Card for " + user.getName())
                    .addFiles(FileUpload.fromData(is, "xp_card.png"))
                    .queue();

        } catch (Exception e) {
            event.reply("❌ Failed to generate XP card.").setEphemeral(true).queue();
            e.printStackTrace();
        }
    }

    private int getUserRank(String guildId, String userId) {
        List<String> sortedUsers = XPManager.getGuildXPData(guildId).entrySet()
                .stream()
                .sorted((a, b) -> {
                    XPManager.UserData dataA = a.getValue();
                    XPManager.UserData dataB = b.getValue();
                    int levelCompare = Integer.compare(dataB.level, dataA.level);
                    return levelCompare != 0 ? levelCompare : Integer.compare(dataB.xp, dataA.xp);
                })
                .map(e -> e.getKey())
                .collect(Collectors.toList());

        int index = sortedUsers.indexOf(userId);
        return index >= 0 ? index + 1 : sortedUsers.size() + 1;
    }
}
