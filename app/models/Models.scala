package models

import java.util.UUID
import scala.collection.convert.WrapAsScala
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError
import com.datastax.driver.core.utils.UUIDs
import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.Row

case class Employee(id: UUID, firstName: String, lastName: String, designation: String,p4: String,p5: String,p6: String,p7: String,p8: String,p9: String,p10: String,p11: String,p12: String,p13: String,p14: String,p15: String,p16: String,p17: String,p18: String,p19: String)

class EmployeeRepository(client: SimpleClient) {

  import Utils._

  def insert(firstName: String, lastName: String, designation: String,p4: String,p5: String,p6: String,p7: String,p8: String,p9: String,p10: String,p11: String,p12: String,p13: String,p14: String,p15: String,p16: String,p17: String,p18: String,p19: String)(implicit ctxt: ExecutionContext): Future[UUID] = {
    val stmt = new BoundStatement(client.session.prepare("INSERT INTO employeeexample.employees (id, firstName, lastName, designation,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19) VALUES (?, ?, ?, ?,?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?);"))
    val id = UUIDs.timeBased
    client.session.executeAsync(stmt.bind(id, firstName, lastName, designation,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19)).toScalaFuture.map(rs => id)
  }
}

object JsonFormats {

  private def uuidReader(checkUuuidValidity: Boolean = false): Reads[java.util.UUID] = new Reads[java.util.UUID] {
    import java.util.UUID
    import scala.util.Try
    def check(s: String)(u: UUID): Boolean = (u != null && s == u.toString())
    def parseUuid(s: String): Option[UUID] = {
      val uncheckedUuid = Try(UUID.fromString(s)).toOption

      if (checkUuuidValidity) {
        uncheckedUuid filter check(s)
      } else {
        uncheckedUuid
      }
    }

    def reads(json: JsValue) = json match {
      case JsString(s) => {
        parseUuid(s).map(JsSuccess(_)).getOrElse(JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.uuid")))))
      }
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.uuid"))))
    }
  }

  private implicit val uuidReads: Reads[java.util.UUID] = uuidReader()
  private implicit val uuidWrites: Writes[UUID] = Writes { uuid => JsString(uuid.toString) }

  implicit val employeeFormat: Format[Employee] = Json.format[Employee]
  implicit val employeeFormatReads = (
    (__ \ 'firstName).read[String] and
    (__ \ 'lastName).read[String] and
    (__ \ 'designation).read[String] and 
    (__ \ 'p4).read[String] and 
	(__ \ 'p5).read[String] and 
	(__ \ 'p6).read[String] and 
	(__ \ 'p7).read[String] and 
	(__ \ 'p8).read[String] and 
	(__ \ 'p9).read[String] and 
	(__ \ 'p10).read[String] and 
	(__ \ 'p11).read[String] and 
	(__ \ 'p12).read[String] and 
	(__ \ 'p13).read[String] and 
	(__ \ 'p14).read[String] and 
	(__ \ 'p15).read[String] and 
	(__ \ 'p16).read[String] and 
	(__ \ 'p17).read[String] and 
	(__ \ 'p18).read[String] and 
	(__ \ 'p19).read[String])tupled
}