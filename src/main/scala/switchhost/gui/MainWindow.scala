package switchhost.gui

/**
 * Created by masaakif on 2014/09/04.
 */

import java.awt.{Frame, SystemColor, Dimension, Button, BorderLayout, Label, TextArea, Font, Color}
import java.awt.event.{WindowAdapter, WindowEvent, MouseEvent, MouseAdapter}
import javax.swing.{UIManager}
import switchhost.HostSwitcher
import switchhost.FileOperator

object MyEvent extends WindowAdapter {
	override def windowClosing(e: WindowEvent):Unit = e.getWindow.dispose
}

class ExceptionWindow(e:Exception) extends WindowAdapter{
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
	private val frame = new Frame {
		setBackground(SystemColor.control)
		addWindowListener(MyEvent)
		setMinimumSize(new Dimension(640,480))
		setLayout(new BorderLayout)
		setTitle("例外が発生しました")
		val ta = new TextArea(e.getMessage + "\r\n" + e.getStackTrace.foldLeft("")((res,l) => res + l + "\r\n")) {setBackground(Color.YELLOW)}
		add(ta)
	}

	def show = frame.setVisible(true)
}


class MainWindow(h:HostSwitcher) extends WindowAdapter{
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

	val ta = new TextArea
	private def showHosts = {
		val ls = h.getContents
		val nls = ls.lines.filterNot(_=="").foldLeft(""){(res, l) =>
			val l2 = h.replace(l) match {
				case x if x == l => x
				case y => l + "\t\t\t ----> \t\t\t" + y
			}
			res + l2 + "\r\n"
		}
		ta.setText(nls)
	}

	object ButtonClickEvent extends MouseAdapter {
		override def mouseClicked(e: MouseEvent):Unit = {
			try {
				h.createBackup()
				h.replaceFile
				showHosts
			} catch {case e:Exception =>
				val ew = new switchhost.gui.ExceptionWindow(e)
				ew.show
			}
		}
	}

	private val frame = new Frame {
		setBackground(SystemColor.control)
		addWindowListener(MyEvent)
		setSize(new Dimension(640,840))
		setLayout(new BorderLayout)
		setTitle("hostsファイルの変更")
		val l = new Label("現在の" + h.hostsFilename + " の内容、および切り替え箇所")

		val b = new Button("ネットワークの切り替えを行う（OK）"){
			setFont(new Font("", Font.BOLD,18))
			addMouseListener(ButtonClickEvent)
		}

		add(l, BorderLayout.NORTH)
		add(ta, BorderLayout.CENTER)
		add(b, BorderLayout.SOUTH)
		showHosts
	}

	def update = ""
	def refreshListOfChanges:Unit = {

	}

	def show:Unit = {
		frame.setVisible(true)
		refreshListOfChanges
	}
}
