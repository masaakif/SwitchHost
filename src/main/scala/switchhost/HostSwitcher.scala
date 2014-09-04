package switchhost

/**
 * Created by masaakif on 2014/09/03.
 */

import scala.collection.mutable.Map

object HostSwitcher{
	def apply(ips:String) = new HostSwitcher(ips)
}

class HostSwitcher private(ips:String) {
	type ipmap = Map[String, String]

	private val ipTable:ipmap = (Map.empty[String, String] /: ips.split("\n")) {(it, ip_pair) =>
		val a:Array[String] = ip_pair.split("[ |\t]*[,| |\t][ |\t]*").filterNot(_=="")
		it + (a(0) -> a(1)) + (a(1) -> a(0))
	}

	def getList = ipTable
	def getBuddy(ip:String):Option[String] = ipTable.get(ip)
	def replace(entry:String):String = {
		val a_entry = entry.split("[ |\t]+")
		getBuddy(a_entry(0)) match {
			case Some(x) => (x /: a_entry.drop(1)){(e,a) => e + "\t" + a}
			case None => entry
		}
	}
}

object Main extends App {
	val hosts = "c:\\windows\\system32\\drivers\\etc\\hosts"
	val mw = new switchhost.gui.MainWindow
	mw.show

}