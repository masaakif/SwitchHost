package switchhost

/**
 * Created by masaakif on 2014/09/03.
 */

import scala.collection.mutable.Map
import java.io.File
import java.net.URL

object Main extends App {
	try {
		val sysroot = System.getenv("SYSTEMROOT")
		val hosts = sysroot + "\\system32\\drivers\\etc\\hosts"
		val ips = "/textfiles/ipaddresses.txt"
		val mw = new switchhost.gui.MainWindow(HostSwitcher(ips, hosts))
		mw.show
	} catch { case e:Exception =>
		val ew = new switchhost.gui.ExceptionWindow(e)
		ew.show
	}
}

object HostSwitcher{
	def apply(ips:String="", hosts:String="") = new HostSwitcher(ips,hosts)
	def apply = new HostSwitcher("","")
}

class HostSwitcher private(ips:String, hosts:String="") {
	type ippair = (String, String)
	type ipmap = Map[String, String]

	val hostsFilename = hosts
	private var ipTable:ipmap = ips match {
		case "" => Map.empty[String, String]
		case _  => {
			val lists = FileOperator.readWholeFromResource(ips)
			lists.lines.filterNot(_ == "").foldLeft(Map.empty[String, String]) {
				(it, ip_pair) =>
					val a: Array[String] = ip_pair.split("[ |\t]*[,| |\t][ |\t]*").filterNot(_ == "")
					it + (a(0) -> a(1)) + (a(1) -> a(0))
			}
		}
	}

	def importTable(ips:String):Unit = {
		ipTable = (Map.empty[String, String] /: ips.lines.filterNot(_=="")) {(it, ip_pair) =>
			val a:Array[String] = ip_pair.split("[ |\t]*[,| |\t][ |\t]*").filterNot(_=="")
			it + (a(0) -> a(1)) + (a(1) -> a(0))
		}
	}

	def createBackup(f:String = "") = f match {
		case "" => FileOperator.backup(hosts)
		case _ => FileOperator.backup(f)
	}

	def getList = ipTable
	def getBuddy(ip:String):Option[String] = ipTable.get(ip)
	def getContents:String = FileOperator.readWhole(hosts)
	def replace(entry:String):String = {
		val a_entry = entry.split("[ |\t]+")
		getBuddy(a_entry(0)) match {
			case Some(x) => (x /: a_entry.drop(1)){(e,a) => e + "\t" + a}
			case None => entry
		}
	}

	def replaceFile:Unit = replaceFile(hosts)
	def replaceFile(f:String):Unit = replaceFile(new File(f))
	def replaceFile(f:File):Unit = {
		import scala.collection.JavaConversions._

		val tmp = FileOperator.createTemp(f)
		FileOperator.makeEmpty(f)
		val convertedLines = FileOperator.getLineIterator(tmp).foldLeft(""){(res, l) =>
			res + replace(l) + "\r\n"
		}

		FileOperator.write(f, convertedLines)
		FileOperator.delete(tmp)
	}
}

