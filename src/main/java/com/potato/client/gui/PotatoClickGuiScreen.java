package com.potato.client.gui;

import com.potato.client.ClientModule;
import com.potato.client.ModuleRegistry;
import com.potato.client.PotatoConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;

public final class PotatoClickGuiScreen extends Screen {
    private static final List<String> CATEGORIES = List.of("Combat", "Raid", "Render", "OreSim", "Misc", "Client");
    private String selectedCategory = "Client";
    private TextFieldWidget search;
    private int panelX;
    private int panelY;
    private int panelWidth = 430;
    private int panelHeight = 300;
    private boolean dragging;
    private int dragOffsetX;
    private int dragOffsetY;

    public PotatoClickGuiScreen() {
        super(Text.literal("Potato Client"));
    }

    @Override
    protected void init() {
        panelX = Math.max(12, (width - panelWidth) / 2);
        panelY = Math.max(12, (height - panelHeight) / 2);
        search = new TextFieldWidget(textRenderer, panelX + 14, panelY + 72, panelWidth - 28, 20, Text.literal("Search modules"));
        search.setPlaceholder(Text.literal("Search modules..."));
        addDrawableChild(search);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x99000000);
        context.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0xFF141418);
        context.fill(panelX, panelY, panelX + panelWidth, panelY + 3, PotatoConfig.accent());
        context.drawTextWithShadow(textRenderer, Text.literal("POTATO CLIENT"), panelX + 14, panelY + 14, 0xFFFFFFFF);
        context.drawTextWithShadow(textRenderer, Text.literal("Fabric 1.21.11  •  Right Shift"), panelX + 14, panelY + 31, 0xFF999999);

        int categoryX = panelX + 14;
        for (String category : CATEGORIES) {
            int w = textRenderer.getWidth(category) + 16;
            int color = selectedCategory.equals(category) ? PotatoConfig.accent() : 0xFF29292F;
            context.fill(categoryX, panelY + 49, categoryX + w, panelY + 67, color);
            context.drawText(textRenderer, category, categoryX + 8, panelY + 54, 0xFFFFFFFF, false);
            categoryX += w + 5;
        }

        super.render(context, mouseX, mouseY, delta);
        int rowY = panelY + 105;
        int shown = 0;
        String query = search == null ? "" : search.getText().trim().toLowerCase();
        for (ClientModule module : ModuleRegistry.all()) {
            if (!module.category().equals(selectedCategory)) continue;
            if (!module.name().toLowerCase().contains(query)) continue;
            if (shown >= 8) break;
            int rowX = panelX + 14 + (shown % 2) * 202;
            int y = rowY + (shown / 2) * 42;
            context.fill(rowX, y, rowX + 194, y + 32, 0xFF202027);
            context.drawTextWithShadow(textRenderer, Text.literal(module.name()), rowX + 9, y + 11, 0xFFFFFFFF);
            String state = module.enabled() ? "ON" : "OFF";
            int stateColor = module.enabled() ? PotatoConfig.accent() : 0xFF777780;
            context.fill(rowX + 153, y + 7, rowX + 185, y + 25, stateColor);
            context.drawCenteredTextWithShadow(textRenderer, state, rowX + 169, y + 12, 0xFF141418);
            shown++;
        }
        context.drawTextWithShadow(textRenderer, Text.literal("Click a module to toggle • Settings save automatically"),
                panelX + 14, panelY + panelHeight - 18, 0xFF888890);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && mouseY >= panelY && mouseY <= panelY + 42 && mouseX >= panelX && mouseX <= panelX + panelWidth) {
            dragging = true;
            dragOffsetX = (int) mouseX - panelX;
            dragOffsetY = (int) mouseY - panelY;
            return true;
        }
        int categoryX = panelX + 14;
        if (button == 0 && mouseY >= panelY + 49 && mouseY <= panelY + 67) {
            for (String category : CATEGORIES) {
                int w = textRenderer.getWidth(category) + 16;
                if (mouseX >= categoryX && mouseX <= categoryX + w) {
                    selectedCategory = category;
                    if (search != null) search.setText("");
                    return true;
                }
                categoryX += w + 5;
            }
        }
        if (button == 0 && mouseY >= panelY + 105 && mouseY < panelY + panelHeight - 24) {
            int shown = 0;
            String query = search == null ? "" : search.getText().trim().toLowerCase();
            for (ClientModule module : ModuleRegistry.all()) {
                if (!module.category().equals(selectedCategory) || !module.name().toLowerCase().contains(query)) continue;
                if (shown >= 8) break;
                int rowX = panelX + 14 + (shown % 2) * 202;
                int y = panelY + 105 + (shown / 2) * 42;
                if (mouseX >= rowX && mouseX <= rowX + 194 && mouseY >= y && mouseY <= y + 32) {
                    module.toggle();
                    PotatoConfig.save();
                    return true;
                }
                shown++;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging && button == 0) {
            panelX = Math.max(0, Math.min(width - panelWidth, (int) mouseX - dragOffsetX));
            panelY = Math.max(0, Math.min(height - panelHeight, (int) mouseY - dragOffsetY));
            if (search != null) search.setPosition(panelX + 14, panelY + 72);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
