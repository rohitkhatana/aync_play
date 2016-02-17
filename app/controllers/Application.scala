package controllers
import play.api.data.Form
import play.api.data.Forms.{tuple, text}
import play.api.mvc._
import javax.inject.Inject
import play.api.libs.ws._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

object LoggingAction extends ActionBuilder[Request] {
  def invokeBlock[A] (request: Request[A], block: (Request[A]) => Future[Result]) = {
    println("Action composition testing")
    block(request)
  }
}

class Application @Inject() (ws: WSClient) extends Controller {
  val requestUrl: WSRequest = ws.url("http://127.0.0.1:8000/category/?categoryId=6")

  def composition = LoggingAction {
    Ok("Composition is working fine")
  }

  def index = Action.async {
    requestUrl.get().map {
      response =>
        Ok(response.json)
    } recover {
      case ex: Exception =>
        Ok(ex.getMessage)
    }
  }

  def create = Action(parse.tolerantFormUrlEncoded) {
    request =>
      val name = request.body.get("name").map(_.head)
      println(name)
      println(name.mkString(""))
      Ok(name.mkString(""))
  }

  val myFormTuple = Form(
    tuple(
      "name" -> text,
      "age" -> text
    )
  )
  def somePost = Action {
    implicit request =>
      val (name, age) = myFormTuple.bindFromRequest.get
      Ok(name + age)
  }

  def show = Action.async {
    val request2: WSRequest = ws.url("http://127.0.0.1:8000/category/")
    val httpResponses = for {
      result1 <- request2.get
      result2 <- request2.get
    } yield (result1.body + result2.body)

    httpResponses.map {
        Ok(_)
    } recover {
      case ex: Exception =>
        Ok(ex.getMessage)
    }

  }
}
