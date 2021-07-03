package com.chore.crunch
package main

import akka.actor.ActorSystem

object SampleClass extends App {
  println("Hidee Hoo!")
  implicit val system = ActorSystem("ChoreCrunch")
}
