package xyz.shy

//import com.alibaba.fastjson.JSON
//import org.apache.http.client.entity.UrlEncodedFormEntity
//import org.apache.http.client.methods.HttpPost
//import org.apache.http.message.BasicNameValuePair
//import org.apache.http.util.EntityUtils


/**
  * Created by AnonYmous_shY on 2016/7/8.
  */
object FirstScala {

  def add(x: Int, y: Int): Int = x + y

  def sayHello(name: String) {
    println("Hello" + name)
  }

  def helloWorld {
    println("Hello World")
  }

  /*def extractWords(contenttext: String): String = {
    val httpClient = HttpClients.createDefault
    val inner_interface = "http://10.44.153.64/process/getResults"
    val outer_interface = "http://101.200.185.42/process/getResults"
    val post = new HttpPost(outer_interface)

    import java.util.{ArrayList => JavaArrayList}

    val nvps = new JavaArrayList[BasicNameValuePair]()
    nvps.add(new BasicNameValuePair("contenttext", contenttext))
    val data = new UrlEncodedFormEntity(nvps, "utf-8")
    post.setEntity(data)
    val response: CloseableHttpResponse = httpClient.execute(post)
    try {
      val entity = response.getEntity
      val result = EntityUtils.toString(entity, "utf-8")
      val jSONObject = JSON.parseObject(result).get("tags").asInstanceOf[String]
      jSONObject
    } finally {
      response.close()
      httpClient.close()
    }
  }*/
  def main(args: Array[String]) {

    //    println(add(1, 2))
    //    sayHello("shy")
    //    helloWorld

    //    val arr = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
    //    for (i <- 0 until arr.length)
    //      println(arr(i))
    //
    //    for (i <- arr)
    //      println(i)

    val map = Map("shy" -> 27, "emma" -> 26, "taylor" -> 27)
    for ((k, v) <- map)
      println("name: " + k + ",age: " + v)
    println(map.keySet)
    println(map.values)
  }
}
