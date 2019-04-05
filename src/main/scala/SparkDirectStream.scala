import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark._
import org.apache.spark.streaming._


object SparkDirectStream extends App{

  val conf = new SparkConf().setAppName("DirectStream").setMaster("local[*]")
  val ssc = new StreamingContext(conf, Seconds(1))
  val timestamp: Long = System.currentTimeMillis / 1000
  val path: String = "Weapons/HDFS"

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
    .foreachRDD(rdd => {
        val d = rdd.collect().mkString("\n")
        //rdd.saveAsTextFile(path+timestamp)
        print(d)
    })

  ssc.start()
  ssc.awaitTermination()
}
