package com.alisiikh.catharsis.bot.model

case class BotResponse[T](ok: Boolean, result: T)
