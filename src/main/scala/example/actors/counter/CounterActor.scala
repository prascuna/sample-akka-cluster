package example.actors.counter

import java.time.LocalDateTime

import akka.actor.{Actor, ActorLogging}
import example.actors.counter.CounterActor.Command.{IncrementCmd, ReadValueCmd}
import example.actors.counter.CounterActor._

class CounterActor extends Actor with ActorLogging {

  private var state = CounterState()

  override def receive: Receive = {
    case IncrementCmd(value, user) =>
      state = state.copy(state.count + value, Some(user))
    case ReadValueCmd =>
      sender() ! state

    case _ =>
      log.error("Unknown command received")
  }
}

object CounterActor {

  sealed trait Command
  object Command {
    case class IncrementCmd(value: Int, updatedBy: String) extends Command
    case object ReadValueCmd extends Command
  }

  case class CounterState(count: Int = 0,
                          updatedBy: Option[String] = None,
                          date: String = LocalDateTime.now().toString)
}
