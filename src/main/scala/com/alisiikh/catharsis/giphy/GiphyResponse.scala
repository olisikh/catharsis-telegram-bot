package com.alisiikh.catharsis.giphy

import java.net.URL

case class GiphyData(images: GiphyImages)
case class GiphyImages(original: GiphyImage)
case class GiphyImage(url: String, height: String, width: String, size: String)
case class GiphyMeta(msg: String, status: Int)

case class GiphyResponse(data: GiphyData, meta: GiphyMeta)
