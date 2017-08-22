package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created on 20.08.2017.
 *
 * @author Serhii Petrusha aka Mr_Rism
 */
public class Engine {

  public Map<String, List<Double>> getAllPrices(long timeInterval) {
    Map<String, List<Double>> allChanges = new HashMap<>();
    for (String s : getPairs()) {

      List<Double> prices = poloniexGetPricesTillNow(s, timeInterval);

      if (prices.size() < 10) {
        try {

          Thread.sleep(500);
        } catch (InterruptedException e) {
          System.exit(3);
        }

        System.out.println("Unsuccessful " + s);


      }

      allChanges.put(s, prices);

      try {

        Thread.sleep(80);
      } catch (InterruptedException e) {
        System.exit(3);
      }
      System.out.println("Successful " + s);


    }
    return allChanges;
  }

  private List<Double> poloniexGetPricesTillNow(final String currency, final long timeDiffrence) {
    StringBuilder a = new StringBuilder();
    try {
      Time curDate = new Time(System.currentTimeMillis() / 1000);
      Time fromDate = new Time(curDate.getTime() - timeDiffrence);
      String sURL =
          "https://poloniex.com/public?command=returnChartData&currencyPair=" + currency + "&start="
              + fromDate.getTime() + "&end=9999999999&period=" + Consts.TRADING_PERIOD;

      URL url = new URL(sURL);
      HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
      BufferedReader in = new BufferedReader(
          new InputStreamReader(httpCon.getInputStream(), "UTF-8"));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        a.append(inputLine);
      }
      in.close();
      httpCon.disconnect();
    } catch (IOException e) {
      System.exit(403);
    }

    return closePriceToList(a.toString());
  }


  private Set<String> getPairs() {
    StringBuilder a = new StringBuilder();
    try {
      String sURL = "https://poloniex.com/public?command=returnTicker";
      URL url = new URL(sURL);
      HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
      BufferedReader in = new BufferedReader(
          new InputStreamReader(httpCon.getInputStream(), "UTF-8"));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        a.append(inputLine);
      }
      in.close();

      httpCon.disconnect();
    } catch (IOException e) {
      System.exit(403);
    }

    return tikerToSet(a.toString());
  }

  private Set<String> tikerToSet(final String input) {
    Pattern pattern = Pattern.compile("\"([A-Z]|_){7,11}\"");

    Matcher matcher = pattern.matcher(input);
    Set<String> matches = new TreeSet<>();
    while (matcher.find()) {
      matches.add(matcher.group().replaceAll("\"", ""));


    }
    return matches;
  }

  private List<Double> closePriceToList(final String input) {
    Pattern pattern = Pattern.compile("\"close\":(\\d|\\.){3,}");

    Matcher matcher = pattern.matcher(input);
    List<Double> matches = new ArrayList<>();
    while (matcher.find()) {

      matches.add(Double.parseDouble(matcher.group().replaceAll("\"close\":", "")));
    }
    return matches;
  }
}
