package controllers


import play.api.libs.json.Json
import play.api.mvc._
import javax.inject.Inject
import play.api.libs.ws._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

class Application @Inject() (ws: WSClient) extends Controller {

  val request: WSRequest = ws.url("http://127.0.0.1:8000/category/?categoryId=6")

  def index = Action.async {
    request.get().map {
      response =>
        Ok(response.json)
    } recover {
      case ex: Exception =>
        Ok(ex.getMessage)
    }
  }

  def show = Action.async {
    val request2: WSRequest = ws.url("http://127.0.0.1:8000/category/")
    val httpResponses = for {
      result1 <- request2.get
      result2 <- request2.get
    } yield (result1.body + result2.body)

    httpResponses.map {
        Ok(_)
    }

  }
}
