import com.google.gson._

object WeaponUtils {

  case class Weapon (
                      id          : String,
                      location    : String,
                      battery     : Int,
                      working     : Boolean,
                      safety      : Boolean,
                      shots       : Int
                    )

  def parseFromJson(lines:Iterator[String])={
    val gson = new Gson
    lines.map( line =>  gson.fromJson(line, classOf[Weapon]))
  }

}
