/**
 * Created by masaakif on 2014/09/02.
 */

package switchhost

import java.io.FileInputStream

object FileOperator {
	import java.io.{File, FileWriter}
	import org.apache.commons.io.{FileUtils, LineIterator}
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

	private def combineToOneString(br:BufferedReader) = {
		Iterator.continually(br.readLine).takeWhile(_!=null).foldLeft("")((res,l) => res + l + "\r\n")
	}
	def readWholeFromResource(f:String):String = readWhole(getClass.getResource(f))
	def readWhole(f:String):String = readWhole(new File(f))
	def readWhole(u:URL):String = {
		val br = new BufferedReader(new InputStreamReader(u.openStream, "UTF-8"))
		combineToOneString(br)
	}
	def readWhole(f:File):String = {
		combineToOneString(new BufferedReader(new InputStreamReader(new FileInputStream(f))))
	}

	def write(f:String, s:String):File = write(new File(f), s)
	def write(f:File, s:String):File = {
		val fw = new FileWriter(f)
		fw.write(s)
		fw.close
		return f
	}

	def getLineIterator(f:String):LineIterator = getLineIterator(new File(f))
	def getLineIterator(f:File):LineIterator = FileUtils.lineIterator(f)

	def create(f:String):File = create(new File(f))
	def create(f:File):File = {
		f.createNewFile
		f
	}

	def makeEmpty(f:File):File = write(f,"")
}
