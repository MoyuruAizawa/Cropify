package io.moyuru.cropifysample

sealed interface Screen {
  val name: String get() = javaClass.name
  val path: String

  object Menu : Screen {
    override val path = name
  }

  object Bitmap : Screen {
    override val path = name
  }

  object File : Screen {
    const val argUri = "uri"
    override val path = "$name/{$argUri}"
  }
}
