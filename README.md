# Just IRC

Just IRC is a fabric mod that allows you to connect to an irc server in minecraft chat.

This is my very first mod I create, so it kinda sucks. I hope to improve it over time!

## Usage
- You can interact with irc servers using game commands (the most thing you need to know can be found typing "@help" or "@h")
- You can save irc informations through the config system which can be managed through the mod "modmenu" or through a keybind ("j" as default)

### When you want to connect to the server saved in the configuration file, you have to type the "@connect" command without any additional parameters.

<a href="https://github.com/8bitFra/JustIRC"><img src="https://raw.githubusercontent.com/8bitFra/JustIRC/main/images/help.png" title="help" alt="help" width="545" height="205"></a>


### Tested on irc.freenode.net and a local server created with InspIRCd

### Useful informations
If you can't connect to the irc channel, make sure no one has already joined with your minecraft name.

If you have a Minecraft name that start with a number, if you can't connect to the irc server, make sure that you haven't used illegal characters on your irc nickname. (currently in this case the mod has no checks)

If you are using the auto connect option, it will start about 10 seconds after entering a world.

### Known issues
 - <s>After a period of inactivity, the mod disconnects from the channel</s>
 - <s>Entering a wrong ip in some cases can cause the client to crash</s>
 - <s>Player death causes data loss</s>
