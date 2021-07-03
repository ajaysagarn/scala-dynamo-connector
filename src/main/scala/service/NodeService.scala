package com.chore.crunch
package service

import akka.actor.{Actor, ActorLogging}

object NodeService{
  case class createNode(attributes:List.type )
  case class nodeCreated(id: String)
  case class findNode(nodeId: String)
  case object findAllNodes
  case class removeNode(nodeId: String)
}

class NodeService extends Actor with ActorLogging {
  import NodeService._

  override def receive: Receive = {
    case createNode(attributes) =>
      log.info("creating node with attributes")

    case nodeCreated(id) =>{
      log.info(s"node created with id: $id")
    }

    case findNode(id) =>{
      log.info(s"finding node with id: $id")
    }

    case findAllNodes =>{
      log.info("Getting all nodes on the table")
    }

    case removeNode(id)=>{
      log.info(s"Removing node with id= $id")
    }
  }
}
