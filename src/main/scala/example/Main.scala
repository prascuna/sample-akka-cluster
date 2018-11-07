package example
import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import example.actors.counter.CounterActor
import example.routes.Routes

object Main extends App {
  implicit val system = ActorSystem("ClusterSystem")
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher

//  val counterActor = system.actorOf(Props[CounterActor], "counter")

  val counterActor = system.actorOf(
    props = ClusterSingletonManager.props(
      singletonProps = Props(classOf[CounterActor]),
      terminationMessage = PoisonPill,
      settings = ClusterSingletonManagerSettings(system)//.withRole("worker")
    ),
    name = "counter"
  )

  val counterProxyActor = system.actorOf(
    props = ClusterSingletonProxy.props(
      singletonManagerPath = "/user/counter",
      settings = ClusterSingletonProxySettings(system)//.withRole("worker")
    ),
    name = "counter-proxy"
  )

  val routes = Routes(counterProxyActor)

  private val port = 8080 + sys.env("AKKA_REMOTE_PORT").toInt
  val bindingFuture = Http()
    .bindAndHandle(routes, "localhost", port)
    .map { bindings =>
      println(
        s"=========================== Service started on ${bindings.localAddress}"
      )
    }

}
