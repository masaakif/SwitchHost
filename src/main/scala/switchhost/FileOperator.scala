/**
 * Created by masaakif on 2014/09/02.
 */

package switchhost

object FileOperator {
	import java.io.File
	import org.apache.commons.io.FileUtils
	import java.util.Date
	import java.net.URL
	import java.io.{InputStreamReader, BufferedReader, InputStream}

	def cwd = new File(".").getAbsoluteFile.getParent
	def today = "%tY%<tm%<td" format new Date
	def delete(f:String) = FileUtils.deleteQuietly(new File(cwd +  "//" + f))
	def delete(f:File) = FileUtils.deleteQuietly(f)

	private def copy(src:File, ext:String) = {
		val dest = new File(src.getAbsoluteFile.toString + ext)
		FileUtils.copyFile(src, dest)
		dest
	}

	def move(f:String, dest:String):Unit = move(new File(f), dest)
	def move(f:File, dest:String):Unit = FileUtils.moveToDirectory(f, new File(dest), false)

	def backup(f:String):File = backup(new File(f))
	def backup(f:File):File = copy(f, "_" + today)

	def createTemp(f:String):File = createTemp(new File(f))
	def createTemp(f:File):File = copy(f, "_tmp")

	def readWhole(f:String):String = readWhole(getClass.getResource(f))
	def readWhole(u:URL):String = {
		val br = new BufferedReader(new InputStreamReader(u.openStream, "UTF-8"))
		val lines = Iterator.continually(br.readLine).takeWhile(_!=null).foldLeft("")((res,l) => res + "\r\n" + l)
		lines
	}
}
