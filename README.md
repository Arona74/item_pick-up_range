# Item Pick-Up Range Mod - Extended Edition for 1.20.1 by Arona74

**Minecraft Version:** 1.20.1  
**Fabric Loader Version:** 0.16.14+  
**Original Author:** wenhao  
**This version Author:** Arona74  
**License:** MIT  

---

## Description

The **Item Pick-Up Range** mod allows players to adjust the range at which they automatically pick up items in Minecraft (default is 1.0). Each player can set their own pickup radius individually, and server admins can define configurable minimum and maximum limits to keep ranges balanced.  

The mod supports per-player customization, persists values between sessions, and automatically clamps existing player values when configuration limits are changed.

---

## Features

- **Per-player pickup range** – Each player can set their own range.  
- **Configurable limits** – Server admins can define `minPickupRange` and `maxPickupRange` in a JSON config file.  
- **Automatic clamping** – Player ranges are automatically adjusted if they exceed new config limits.  
- **Persistent values** – Player pickup ranges are saved and loaded from NBT.  
- **Simple commands** – All features accessible through one root command: `/PickupRange`.  

---

## Installation

1. Install **Fabric Loader 0.16.14+** for Minecraft 1.20.1.  
2. Install the **Fabric API** for Minecraft 1.20.1.  
3. Place the compiled `.jar` of this mod into your `mods/` folder.  
4. Launch Minecraft with the Fabric profile.  

---

## Configuration

The mod configuration is stored in:

```text
config/item_pickup_range.json
```

```json
{
  "minPickupRange": 0.5,
  "maxPickupRange": 5.0
}
```
- **minPickupRange**: minimum allowed pickup range
- **maxPickupRange**: maximum allowed pickup range

Changing the values and reloading the config automatically adjusts players' current ranges.

---

## Commands

All commands are under the root command: `/PickupRange`

| Command | Description | Permissions |
|---------|-------------|-------------|
| `/PickupRange set <value>` | Set your personal item pickup range (within configured limits) | All players |
| `/PickupRange get` | Display your current pickup range | All players |
| `/PickupRange reload` | Reload the config and clamp all players’ ranges | OP only (permission level 2) |

---

## How it works

- Player ranges are stored individually and saved to NBT, so they persist between sessions.  
- The mod automatically clamps player values when the server starts or when the config is reloaded.  
- Players are notified if their pickup range is adjusted due to new limits.  

---

## Development

- Language: Java  
- Build System: Gradle with Fabric Loom  
- Mixins used for injecting pickup range logic into `PlayerEntity`.  

---

## License

This mod is released under the [MIT License](LICENSE). You are free to use, modify, and redistribute it, as long as the license terms are included.
