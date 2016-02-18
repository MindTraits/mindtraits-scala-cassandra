package models

import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.ResultSetFuture
import com.datastax.driver.core.Session
import scala.collection.JavaConversions._
import play.api.Logger
import com.datastax.driver.core.Metadata



/**
 * Simple cassandra client, following the datastax documentation
 * (http://www.datastax.com/documentation/developer/java-driver/2.0/java-driver/quick_start/qsSimpleClientCreate_t.html).
 */
class SimpleClient(node: String) {

  private val cluster = Cluster.builder().addContactPoint(node).build()
  log(cluster.getMetadata())
  val session = cluster.connect()

  private def log(metadata: Metadata): Unit = {
    Logger.info(s"Connected to cluster: ${metadata.getClusterName}")
    for (host <- metadata.getAllHosts()) {
      Logger.info(s"Datatacenter: ${host.getDatacenter()}; Host: ${host.getAddress()}; Rack: ${host.getRack()}")
    }
  }

  def createSchema(): Unit = {
    session.execute("CREATE KEYSPACE IF NOT EXISTS employeeexample WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};")

    //Execute statements to create two new tables, songs and playlists. Add to the createSchema method:
    session.execute(
      """CREATE TABLE IF NOT EXISTS employeeexample.employees (
        id uuid PRIMARY KEY,
        firstName text,
        lastName text,
        designation text,
        );""")
  }

  def loadData() = {
    session.execute(
      """INSERT INTO employeeexample.employees (id, title, album, artist, tags) 
      VALUES (
          756716f7-2e54-4715-9f00-91dcbea6cf50,
          'Ruthvick',
          'MS',
          'Software Engineer'
          );""");
  }

  def close() {
    session.close
    cluster.close
  }

}

object CassandraDAO extends App {
  val client = new SimpleClient("127.0.0.1")
  client.createSchema
  client.close
}
