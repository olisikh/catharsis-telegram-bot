package com.alisiikh.catharsis.bot.api

case class ChatId(value: Long) extends AnyVal
case class Offset(value: Long) extends AnyVal

case class Chat(id: ChatId)

case class BotUser(
    id: Integer,
    is_bot: Boolean,
    first_name: String,
    last_name: Option[String],
    username: Option[String]
)
case class BotMessage(
    message_id: Long,
    from: Option[BotUser],
    chat: Chat,
    text: Option[String],
    forward_from: Option[BotUser]
) {
  def forwarded: Boolean = forward_from.isDefined
}

case class BotUpdate(
    update_id: Long,
    message: Option[BotMessage]
) {
  def chatId: Option[ChatId] = message.map(_.chat.id)
}

case class BotResponse[A](ok: Boolean, result: A)
