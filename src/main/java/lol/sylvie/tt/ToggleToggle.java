package lol.sylvie.tt;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ToggleToggle implements ClientModInitializer {
    private static KeyBinding sprintKeyBinding;
    private static KeyBinding sneakKeyBinding;

    private static final KeyBinding.Category keybindingCategory = KeyBinding.Category.create(Identifier.of("toggletoggle", "keys"));

    private void toggleOption(ClientPlayerEntity player, SimpleOption<Boolean> option, KeyBinding keybind) {
        boolean newValue = !option.getValue();
        option.setValue(newValue);
        if (!newValue) keybind.setPressed(false);

        if (player == null) return;
        player.sendMessage(Text.translatable("message.toggletoggle", option.toString(), Text.translatable(newValue ? "message.toggletoggle.enabled" : "message.toggletoggle.disabled").formatted(Formatting.YELLOW)), true);
    }

    @Override
    public void onInitializeClient() {
        sprintKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.toggletoggle.sprint",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                keybindingCategory
        ));

        sneakKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.toggletoggle.sneak",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                keybindingCategory
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (sprintKeyBinding.wasPressed())
                toggleOption(client.player, client.options.getSprintToggled(), client.options.sprintKey);

            while (sneakKeyBinding.wasPressed())
                toggleOption(client.player, client.options.getSneakToggled(), client.options.sneakKey);

        });
    }
}
