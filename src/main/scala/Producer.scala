import WeaponUtils.Weapon
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd._
import scala.io.Source


object Producer extends App {

  import java.util.Properties

  import org.apache.kafka.clients.producer._

  val  props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")

  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  val TOPIC="weapon"

  val filename = "Weapons/data.txt"

  for(line<-Source.fromFile(filename).getLines){
    val record = new ProducerRecord(TOPIC, "weapon", line)
    producer.send(record)
    Thread.sleep(1)
  }

  producer.close()
}