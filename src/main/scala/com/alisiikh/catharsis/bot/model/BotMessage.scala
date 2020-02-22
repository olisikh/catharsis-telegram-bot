package com.alisiikh.catharsis.bot.model

case class BotMessage(
    message_id: Long,
    chat: Chat,
    text: Option[String]
)
