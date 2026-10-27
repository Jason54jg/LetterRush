# Commands

Default permission: **op level 2** (`permission_level` in config).

| Command | Description |
|---------|-------------|
| `/letterrush start` | Start a round |
| `/letterrush stop` | Stop the current round |
| `/letterrush status` | Show the active letter and elapsed time |
| `/letterrush reload` | Reload `config/letterrush.json` and reward datapacks |

## Examples

```mcfunction
/letterrush start
/letterrush status
/letterrush stop
/letterrush reload
```

If a round is already running, `start` is rejected until `stop` or a win.
