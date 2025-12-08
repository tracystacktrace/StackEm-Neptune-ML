
<div align="center" style="text-align: center;">

![](https://github.com/tracystacktrace/StackEm-Neptune-ML/raw/master/docs/mod_logo.png)

![GitHub License](https://img.shields.io/github/license/tracystacktrace/StackEm-Neptune-ML?label=License&color=blue) ![GitHub Tag](https://img.shields.io/github/v/tag/tracystacktrace/StackEm-Neptune-ML?label=Version)

</div>

![](https://github.com/tracystacktrace/StackEm-Neptune-ML/raw/master/docs/screenshot_b173_0.png)

<!-- 
[![Available on - Modrinth](https://img.shields.io/badge/Available_on-Modrinth-4bab62?logo=modrinth&logoColor=white)](https://modrinth.com/mod/icy-rind)
-->

**Stack 'Em Neptune** is a compact and lightweight version of original [**Stack 'Em**](https://modrinth.com/mod/stack-em) mod that is designed to provide maximum compatibility for most vanilla/modded minecraft versions.

This is done by using either ModLoader or Forge entrypoints with some Java reflections performed in some places.

**Why so?** Because original Stack 'Em has so many features (a lot :3) available it'd be hard to port and maintain these features in other minecraft versions. Sorry!

## Features

Available Features:
- **Texture Stacking:** you can use several textures by order simultaneously!
- **Runtime Item Gluing:** now if two texturepacks change the different parts of the same texture (see below), they will be "glued" together.


## Compatibility

For now, supported versions are:
- **ModLoader `b1.7.3`**: `-ml` suffix.
- **NFC `b1.7.3`**: `-nfc` suffix.

More versions coming soon!

## Development Environment

The mod is built within the RetroMCP-Java generated workspace with ModLoader included in jars, and with an applied [ModLoader patch](https://github.com/coffeenotfound/ModloaderFix-b1.7.3).

I don't think it's very hard to set up one, [follow the guidelines on their github page!](https://github.com/MCPHackers/RetroMCP-Java)

## License

Licensed under Apache License 2.0.