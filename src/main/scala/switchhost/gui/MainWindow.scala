package switchhost.gui

/**
 * Created by masaakif on 2014/09/04.
 */

import java.awt.{Frame, SystemColor, Dimension, Button, BorderLayout, Label, TextArea, Font}
import java.awt.event.{WindowAdapter, WindowEvent, MouseEvent, MouseAdapter}
import javax.swing.{UIManager}
import switchhost.HostSwitcher

object MyEvent extends WindowAdapter {
	override def windowClosing(e: WindowEvent):Unit = e.getWindow.dispose
}


class MainWindow(h:HostSwitcher) extends WindowAdapter{
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

	private val frame = new Frame {
		setBackground(SystemColor.control)
		addWindowListener(MyEvent)
		setMinimumSize(new Dimension(640,480))
		setLayout(new BorderLayout)
		setTitle("hostsファイルの変更")
		val ta = new TextArea()
		val l = new Label("変更箇所")

		object ButtonClickEvent extends MouseAdapter {
			override def mouseClicked(e: MouseEvent):Unit = {
				ta.setText(ta.getText + "hogehoge\n\r")
			}
		}
		val b = new Button("変更を反映させる"){
			setFont(new Font("", Font.BOLD,18))
			addMouseListener(ButtonClickEvent)
		}

		add(l, BorderLayout.NORTH)
		add(ta, BorderLayout.CENTER)
		add(b, BorderLayout.SOUTH)
	}

	def update = ""
	def refreshListOfChanges:Unit = {

	}

	def show:Unit = {
		frame.setVisible(true)
		refreshListOfChanges
	}
}
