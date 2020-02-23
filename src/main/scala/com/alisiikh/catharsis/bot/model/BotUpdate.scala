package com.alisiikh.catharsis.bot.model

import com.alisiikh.catharsis.bot.api.ChatId

case class BotUpdate(update_id: Long, message: Option[BotMessage]) {
  def chatId: Option[ChatId] = message.map(_.chat.id)
}
