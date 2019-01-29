import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MovieTitles {

  public static String request(String s) {
    String result = null;
    HttpURLConnection con = null;
    BufferedReader reader = null;
    try {
      URL url = new URL(s);
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setDoOutput(true);
      reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      StringBuilder builder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }
      result = builder.toString();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (con != null)
        con.disconnect();
      if (reader != null)
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    return result;
  }

  public static void main(String[] args) {
    ArrayList<String> titles = new ArrayList<>();
    String url = "https://jsonmock.hackerrank.com/api/movies/search/?Title=" + args[0];
    String json = request(url);
    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
    JsonArray data = jsonObject.get("data").getAsJsonArray();
    data.forEach(d -> titles.add(d.getAsJsonObject().get("Title").getAsString()));
    int totalPages = jsonObject.get("total_pages").getAsInt();
    int page = 2;
    while (page <= totalPages) {
      json = request(url + "&page=" + page);
      data = jsonObject.get("data").getAsJsonArray();
      data.forEach(d -> titles.add(d.getAsJsonObject().get("Title").getAsString()));
      page++;
    }
    String[] strings = titles.stream().toArray(String[]::new);
    for (String string : strings) {
      System.out.println(string);
    }
  }
}
