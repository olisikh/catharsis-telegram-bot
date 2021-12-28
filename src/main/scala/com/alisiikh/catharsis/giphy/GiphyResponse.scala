package com.alisiikh.catharsis.giphy

import org.http4s.blaze.http.Url

case class GiphyResponse(data: GiphyData, meta: GiphyMeta)

case class GiphyData(images: GiphyImages)
case class GiphyImages(original: GiphyImage)
case class GiphyImage(url: Url, height: String, width: String, size: String)

case class GiphyMeta(msg: String, status: Int)
