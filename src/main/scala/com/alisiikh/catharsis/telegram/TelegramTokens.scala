package com.alisiikh.catharsis.telegram

object TelegramTokens:
  opaque type TelegramToken = String

  def make(value: String): TelegramToken = value

  extension (x: TelegramToken) def value: String = x
