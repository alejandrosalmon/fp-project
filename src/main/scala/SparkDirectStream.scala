import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.sql.SQLImplicits
import java.io._


object SparkDirectStream extends App{

  val conf = new SparkConf().setAppName("DirectStream").setMaster("local[*]")
  val ssc = new StreamingContext(conf, Seconds(1))
  val path: String = "Data/HDFS"

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "use_a_separate_group_id_for_each_stream",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )

  val topics = Array("test")
  val stream = KafkaUtils.createDirectStream[String, String](
    ssc,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )

  stream.map(record => (record.key, record.value))
    .foreachRDD(foreachFunc = rdd => {
      val d = rdd.collect().mkString("\n")
      val timestamp: Long = System.currentTimeMillis / 1000
      if (!Option(d).getOrElse("").isEmpty){
        val writer = new PrintWriter (new File (path + timestamp) )
        writer.write(d)
        print (d)
        writer.close ()
      }
    })

  ssc.start()
  ssc.awaitTermination()
}
