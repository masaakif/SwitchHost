import sbtassembly.Plugin._
import AssemblyKeys._

name := "SwitchHost"

version := "1.0"

// import dependencies


libraryDependencies ++= Seq(
    "commons-io" % "commons-io" % "2.4",
		"org.specs2" %% "specs2" % "2.3.4" % "test"
)

val jar2exe = inputKey[Unit]("Use jsmooth to create exe from jar")

jar2exe := { import scala.sys.process.Process
	val jsmooth = """C:\D-Drive\developments\environments\Java\jsmooth\jsmoothcmd.exe"""
	Process(s"$jsmooth ${baseDirectory.value}\\${name.value}.jsmooth").run()
	println(s"Finished making ${name.value} exe file.") }

// Assembly
assemblySettings

jarName := name.value + "_fat.jar"

excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
	cp filter { x => x.data.getName match {
		case "stax-api-1.0.1.jar"     => true
		case "dom4j-1.6.1.jar"        => true
		case "commons-codec-1.5.jar"  => true
		case "xml-apis-1.0.b2.jar"    => true
		case "xmlbeans-2.3.0.jar"     => true
		case "poi-ooxml-3.10-FINAL.jar" => true
		case "poi-ooxml-schemas-3.10-FINAL.jar" => true
		case _ => false
	}}
}

val assemblyAll = inputKey[Unit]("Do assembly then jar2exe")

assemblyAll <<= jar2exe dependsOn assembly
