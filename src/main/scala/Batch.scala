


.mkString("\n")
val timestamp: Long = System.currentTimeMillis / 1000
if (!Option(b).getOrElse("").isEmpty){
  val writer = new PrintWriter (new File (path + timestamp) )
  writer.write(b)
  print (b)
  writer.close ()
}