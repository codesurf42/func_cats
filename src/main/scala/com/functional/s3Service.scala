package com.functional

import cats._
import cats.data.EitherT
import cats.instances.all._
import com.functional.ETF.EitherTF
import com.functional.Error.DefaultError

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

sealed trait Error
object Error {
  final case class DefaultError(msg: String) extends Error
  final case class ConnectionError(msg: String) extends Error
}


object ETF {
  type EitherTF[A] = EitherT[Future, Error, A]

  def right[A](a: A) = EitherT.right[Future, Error, A](Future.successful(a))
  def error[A](err: Error) = EitherT.left[Future, Error, A](Future.successful(err))

  
  implicit class OptionETFOps[A](val a: Option[A]) extends AnyVal {
    def toEitherTF(errorMsg: String): EitherTF[A] = a match {
      case Some(v) => ETF.right(v)
      case None => ETF.error(DefaultError(errorMsg))
    }
  }

  implicit class FutureETFOps[A](val f: Future[A]) extends AnyVal {
    def toEitherTF(errorMsg: String): EitherTF[A] = {
//      val right = f.map {
//        case Success(s) => Right(s)
//        case Failure(ex) => Left(DefaultError(errorMsg))
//      }
      EitherT.liftT(f)
    }
  }

}


case class File(data: String)

// real use service
class s3Service {
  def downloadS3(file: String): EitherT[Future, Error, File] = {
    ETF.right(File("file1"))
  }

  def hasConfigValueasOption: Option[String] = Some("opt1")

  def someFuture: Future[String] = Future.successful("fut1")
}

object handleDownload {
  import com.functional.ETF._

  val ds = new s3Service
  val computationOk = for {
    file <- ds.downloadS3("foo")
    // lift option to ETF
    option <- ds.hasConfigValueasOption.toEitherTF("no config value")
    // lift option, just with standard EitherT helper
    option2 <- EitherT.fromOption[Future](ds.hasConfigValueasOption, DefaultError("not found"))
    // lift future to ETF
    fut1 <- ds.someFuture.toEitherTF("here could be func => error msg")
  } yield s"file=${file.data} with opt=$option, opt2=$option2 fut1: $fut1"

  computationOk.map { v =>
    println(v)
    v
  }
}