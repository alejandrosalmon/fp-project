import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.sql.SQLImplicits
import java.io._

import org.json4s._
import org.json4s.jackson.JsonMethods._
import mail._

object SparkDirectStream extends App{

  def jsonStrToMap(jsonStr: String): Map[String, Any] = {
    implicit val formats = org.json4s.DefaultFormats

    parse(jsonStr).extract[Map[String, Any]]
  }

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

  val topics = Array("weapon")
  val stream = KafkaUtils.createDirectStream[String, String](
    ssc,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )

  stream.map(record => record.value)
    .foreachRDD(rdd => {
      val d = rdd.collect().foreach(line => {
        if(linemap("safety") == 0) {
          send a new Mail (
            from = ("weapon@weapon.weapon", "NoName"),
            to = "localhost@local.com",
            subject = "ALERT, SAFETY OFF",
            message = s"Safety OFF for weapon id ${linemap("idn")}"
          )
        }
        if(linemap("battery").toString.toInt < 10) {
          send a new Mail (
            from = ("weapon@weapon.weapon", "NoName"),
            to = "localhost@local.com",
            subject = "ALERT, BATTERY VERY LOW",
            message = s"Battery Low for weapon id ${linemap("idn")}"
          )
        }
      })
      Thread.sleep(1)
    })

  ssc.start()
  ssc.awaitTermination()
}
