package com.alisiikh.catharsis.telegram

trait TelegramBotAlgebra[F[_]]:
  def requestUpdates(offset: Offset): F[(Offset, TelegramResponse[List[TelegramUpdate]])]
  def sendAnimation(chatId: ChatId, message: String): F[Unit]
  def sendMessage(chatId: ChatId, text: String): F[Unit]
