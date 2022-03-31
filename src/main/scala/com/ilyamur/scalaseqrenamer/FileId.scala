package com.ilyamur.scalaseqrenamer

case class FileListId(files: List[ExistingFileId])

sealed trait FileId(val pos: Int, val path: String)

case class ExistingFileId(override val pos: Int, override val path: String, mod: Long) extends FileId(pos, path)

case class InitFileId(override val pos: Int, override val path: String) extends FileId(pos, path)
