package lol.sylvie.tt;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ToggleToggle implements ClientModInitializer {
    private static KeyMapping sprintKeyBinding;
    private static KeyMapping sneakKeyBinding;

    private static final KeyMapping.Category keybindingCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("toggletoggle", "keys"));

    private void toggleOption(LocalPlayer player, OptionInstance<Boolean> option, KeyMapping keybind) {
        boolean newValue = !option.get();
        option.set(newValue);
        if (!newValue) keybind.setDown(false);

        if (player == null) return;
        player.displayClientMessage(Component.translatable("message.toggletoggle", option.toString(), Component.translatable(newValue ? "message.toggletoggle.enabled" : "message.toggletoggle.disabled").withStyle(ChatFormatting.YELLOW)), true);
    }

    @Override
    public void onInitializeClient() {
        sprintKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.toggletoggle.sprint",
                InputConstants.Type.KEYSYM,
                InputConstants.UNKNOWN.getValue(),
                keybindingCategory
        ));

        sneakKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.toggletoggle.sneak",
                InputConstants.Type.KEYSYM,
                InputConstants.UNKNOWN.getValue(),
                keybindingCategory
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (sprintKeyBinding.consumeClick())
                toggleOption(client.player, client.options.toggleSprint(), client.options.keySprint);

            while (sneakKeyBinding.consumeClick())
                toggleOption(client.player, client.options.toggleCrouch(), client.options.keyShift);

        });
    }
}
