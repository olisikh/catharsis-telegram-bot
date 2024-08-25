package com.alisiikh.catharsis.telegram

case class ChatId(value: Long) extends AnyVal:
  override def toString: String = value.toString

case class Offset(value: Long) extends AnyVal:
  def inc: Offset = copy(value = value + 1)

case class Chat(id: ChatId)

case class User(
    id: Integer,
    is_bot: Boolean,
    first_name: String,
    last_name: Option[String],
    username: Option[String]
)
case class Message(
    message_id: Long,
    from: Option[User],
    chat: Chat,
    text: Option[String],
    forward_from: Option[User]
):
  def forwarded: Boolean = forward_from.isDefined
  def tokenized: List[String] =
    text.toList
      .flatMap(text => (if text.startsWith("/") then text.drop(1) else text).split(' '))
      .map(_.trim)
      .filterNot(_.isBlank)

case class TelegramUpdate(
    update_id: Long,
    message: Option[Message]
):
  def chatId: Option[ChatId] = message.map(_.chat.id)

case class TelegramResponse[A](ok: Boolean, result: A)
