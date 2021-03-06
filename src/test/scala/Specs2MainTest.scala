/**
 * Created by masaakif on 2014/08/29.
 */

import org.scalacheck.Prop.True
import org.specs2.mutable._
import switchhost._

class Specs2SampleTest extends Specification {
	"simple test" should {
		val keywords = "simple test"
		"be 11 length" in {
			keywords must have length(11)
		}
		"contains word 'test'" in {
			keywords must contain("test")
		}
		/*
		"raise error because not start with 'SIMPLE'" in {
			keywords must startWith("SIMPLE")
		}
		*/
	}
}

class Specs2FileOperatorTest extends Specification {
	import org.apache.commons.io.FileUtils
	import java.io.File

	val cwd = FileOperator.cwd
	val fn = "hoge.txt"
	"FileOperator" should {
		val td = FileOperator.today
		"backup file with " + td in {
			val f = FileOperator.backup(fn)
			f.getAbsoluteFile.toString must beEqualTo(cwd + "\\" + fn + "_" + td)
			f.exists == true
			FileOperator.delete(f)
			f.exists == false
		}

		"create temp file" in {
			val tf = FileOperator.createTemp(fn)
			tf.getAbsolutePath.toString must beEqualTo(cwd + "\\" + fn + "_tmp")
			tf.exists  == true
			FileOperator.delete(tf)
			tf.exists == false
		}

		"move file" in {
			val fm = "movetest.txt"
			FileOperator.move(fm, sys.env("USERPROFILE") + "\\Desktop")
			val f1 = new File("C:\\Users\\masaakif\\Desktop\\movetest.txt")
			f1.exists == true
			FileOperator.move(f1, ".")
			val f2 = new File("movetest.txt")
			f2.exists == true
		}

		"empty then write 1 line" in {
			val f = "emptytest.txt"
			val f1 = FileOperator.create(f)
			FileOperator.readWhole(f1) must beEqualTo("")
			FileOperator.write(f1, "hogehoge")
			FileOperator.readWhole(f1) must beEqualTo("hogehoge\r\n")
			FileOperator.delete(f)
			f1.exists == false
		}
	}
}

class Specs2HostSwitcherTest extends Specification {
	"HostSwitcher" should {
		val list = "111.111.111.111,200.200.200.200"
		"read IP address list" in {
			val h = HostSwitcher()
			h.importTable(list)
			h.getList must equalTo(Map(("111.111.111.111", "200.200.200.200"), ("200.200.200.200","111.111.111.111")))
		}

		"get correspondat buddy IP" in {
			val h = HostSwitcher()
			h.importTable(list)
			h.getBuddy("111.111.111.111").get must equalTo("200.200.200.200")
			h.getBuddy("200.200.200.200").get must equalTo("111.111.111.111")
		}

		"replace '200.200.200.200 hoge.host'" in {
			val h = HostSwitcher()
			h.importTable(list)
			val entry = "200.200.200.200\thoge.host"
			val replaced = h.replace(entry)
			replaced must equalTo("111.111.111.111\thoge.host")
			h.replace(replaced) must equalTo("200.200.200.200\thoge.host")
		}

		"not able to replace '123.123.123.123 nochange.host'" in {
			val h = HostSwitcher()
			h.importTable(list)
			val entry = "123.123.123.123   nochange.host"
			h.replace(entry) must equalTo(entry)
		}

		"read from resource table" in {
			val h = HostSwitcher("/ipaddresstest.txt")
			h.getBuddy("111.111.111.111").get must equalTo("200.200.200.200")
			h.getBuddy("201.201.201.201").get must equalTo("112.112.112.112")
			h.getBuddy("999.999.999.999").getOrElse("empty") must equalTo("empty")
		}

		"replace file by iptable" in {
			val h = HostSwitcher("/ipaddresstest.txt", "myhosts")
			val res1 = "#####\r\n200.200.200.200\thoge.host\thoge\r\n120.120.120.120 hoge2.host  hoge2\r\n#### sample\r\n"
			val res2 = "#####\r\n111.111.111.111\thoge.host\thoge\r\n120.120.120.120 hoge2.host  hoge2\r\n#### sample\r\n"
			h.getContents must equalTo(res1)
			h.replaceFile
			h.getContents must equalTo(res2)
			h.replaceFile
			h.getContents must equalTo(res1)
		}
	}
}
