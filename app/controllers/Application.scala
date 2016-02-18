package controllers

import play.api._
import play.api.mvc._
import models.SimpleClient
import models.EmployeeRepository
import play.api.libs.json.Json
import com.datastax.driver.core.utils.UUIDs
import models.Employee
import play.api.libs.json.JsError
import scala.concurrent.Future
import java.util.UUID
import scala.util.Try

class Application() extends Controller {
	val client = new SimpleClient("127.0.0.1")
	var employeeRepo = new  EmployeeRepository(client)
	import play.api.libs.concurrent.Execution.Implicits.defaultContext
	import models.JsonFormats._
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

    def createEmployee= Action.async(parse.json) { implicit request =>
	    // Json Format defined in models.JsonFormats.songDataReads
	    request.body.validate[(String, String, String,String, String, String,String, String, String,String,String,String,String,String,String,String,String,String,String)].map {
	      case (firstName, lastName, designation,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19) => {
	        employeeRepo.insert(firstName, lastName, designation,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19).map( id =>
	          Created
	        )
	      }
	    }.recoverTotal {
	      e => Future.successful(BadRequest("Detected error:" + JsError.toFlatJson(e)))
	    }
	}

}
