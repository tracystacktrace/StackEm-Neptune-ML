
## [1.2] - 2025/12/12
- Fixed a critical bug where texture gluing was referencing to incorrect coordinates
- Replaced slow reflects accessors with `sun.misc.Unsafe` alternatives
- The mod now doesn't enforce ModLoader textures load, as ML does by itself (automatically)
- A different structure of assets files, just for convenience

## [1.1] - 2025/12/08
- Replaced website button with SHA-256 "copy to clipboard" button
- Added forced re-texture of ModLoader override textures
- Removed lots of dead code, left after porting from ReIndev

## [1.0] - 2025/12/04
- Initial release!