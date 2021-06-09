package com.alisiikh.catharsis.bot.api

case class Chat(id: ChatId)

case class BotMessage(message_id: Long, chat: Chat, text: Option[String])
case class BotResponse[A](ok: Boolean, result: A)
case class BotUpdate(update_id: Long, message: Option[BotMessage]) {
  def chatId: Option[ChatId] = message.map(_.chat.id)
}
