package com.ilyamur.scalaseqrenamer

sealed trait FileAction

case class Create(src: FileId, trg: FileId) extends FileAction

case class Overwrite(src: FileId, trg: FileId) extends FileAction

case class Delete(trg: FileId) extends FileAction
