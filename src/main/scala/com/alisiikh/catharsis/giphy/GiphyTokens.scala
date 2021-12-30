package com.alisiikh.catharsis.giphy

object GiphyTokens:
  opaque type GiphyToken = String

  def make(value: String): GiphyToken = value

  extension (x: GiphyToken) def value: String = x.trim
