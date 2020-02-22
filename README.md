# Telegram Bot FS2

Example of a purely functional telegram bot built on top of cats-effect, fs2 and http4s client libraries.

## Launching the bot

1. Create a new bot with @BotFather: https://core.telegram.org/bots#6-botfather
2. Register an application for giphy https://developers.giphy.com/, get the token
3. Export the tokens as environment variables: 
```
export GIPHY_TOKEN=<token>
export TELEGRAM_TOKEN=<tokn>
```
4. Run `sbt run` from console
5. Chat with the bot to get gifs
