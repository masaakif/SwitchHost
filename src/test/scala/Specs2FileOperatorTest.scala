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
		"raise error because not start with 'SIMPLE'" in {
			keywords must startWith("SIMPLE")
		}
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
	}
}
