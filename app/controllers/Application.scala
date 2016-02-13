package controllers


import play.api.mvc._
import javax.inject.Inject
import play.api.libs.ws._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
//import play.api.libs.json._

//import scala.concurrent.Future

class Application @Inject() (ws: WSClient) extends Controller {

  val request: WSRequest = ws.url("http://127.0.0.1:8000/category/?categoryId=6")
  def index = Action.async {
    request.get().map {
      response =>
        Ok(response.body)
    }
  }

}
