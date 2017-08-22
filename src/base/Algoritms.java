package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 20.08.2017.
 *
 * @author Serhii Petrusha aka Mr_Rism
 */
public class Algoritms {

  private long timeDiffrence = 86400L;

  private long timeOptionOne, timeOptionTwo;

  private Engine poloniexData = new Engine();

  private Map<String, List<Double>> allPrices = poloniexData.getAllPrices(timeDiffrence);

  private Map<String, Integer> positionChangeSorted;
  private Map<String, Integer> positionsChange;
  private Map<String, Double> sortedOptionOne;
  private Map<String, Double> sortedOptionTwo;
  private Map<String, Double> diffrenseOptionOne;
  private Map<String, Double> diffrenseOptionTwo;

  public Algoritms(long timeOptionOne, long timeOptionTwo) {

    this.timeOptionOne = timeOptionOne;
    this.timeOptionTwo = timeOptionTwo;

    difPlaces(timeOptionOne, timeOptionTwo);

  }

  public Map<String, List<Double>> getAllPrices() {
    return allPrices;
  }

  public Map<String, Integer> getPositionChangeSorted() {
    return positionChangeSorted;
  }

  public Map<String, Integer> getPositionsChange() {
    return positionsChange;
  }

  public Map<String, Double> getSortedOptionOne() {
    return sortedOptionOne;
  }

  public Map<String, Double> getSortedOptionTwo() {
    return sortedOptionTwo;
  }

  public Map<String, Double> getDiffrenseOptionOne() {
    return diffrenseOptionOne;
  }

  public Map<String, Double> getDiffrenseOptionTwo() {
    return diffrenseOptionTwo;
  }

  public void update() {
    allPrices = poloniexData.getAllPrices(timeDiffrence);
    difPlaces(timeOptionOne, timeOptionTwo);
  }

  public void update(long timeDiffrence) {
    this.timeDiffrence = timeDiffrence;
    allPrices = poloniexData.getAllPrices(timeDiffrence);
    difPlaces(timeOptionOne, timeOptionTwo);

  }

  public void reCalculate(long timeOptionOne, long timeOptionTwo) {

    difPlaces(timeOptionOne, timeOptionTwo);

  }

  public long getTimeDiffrence() {
    return timeDiffrence;
  }

  private Map<String, Integer> difPlaces(long timeOptionOne, long timeOptionTwo) {

    if (timeOptionOne > timeDiffrence) {

      timeOptionOne = timeDiffrence;
    }
    if (timeOptionTwo > timeDiffrence) {
      timeOptionTwo = timeDiffrence;
    }

    if (timeOptionOne < timeOptionTwo) {

      long tmp = timeOptionOne;
      timeOptionOne = timeOptionTwo;
      timeOptionTwo = tmp;

    }

    diffrenseOptionOne = new HashMap<>();
    diffrenseOptionTwo = new HashMap<>();

    for (String currency : allPrices.keySet()) {

      int optionOneIndex = (int) (allPrices.get(currency).size()-(timeOptionOne*allPrices.get(currency).size())/timeDiffrence);
      int optionTwoIndex = (int) (allPrices.get(currency).size()-(timeOptionTwo*allPrices.get(currency).size())/timeDiffrence);

      diffrenseOptionOne.put(
          currency, allPrices.get(currency).get(optionOneIndex) / allPrices.get(currency)
              .get(allPrices.get(currency).size() - 1));
      diffrenseOptionTwo.put(
          currency, allPrices.get(currency).get(optionTwoIndex) / allPrices.get(currency)
              .get(allPrices.get(currency).size() - 1));


    }
    sortedOptionOne = diffrenseOptionOne.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));

    sortedOptionTwo = diffrenseOptionTwo.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));

    positionsChange = new HashMap<>();
    for (String currency : allPrices.keySet()) {

      int positionOptionOne = new ArrayList<>(sortedOptionOne.keySet()).indexOf(currency);
      int positionOptionTwo = new ArrayList<>(sortedOptionTwo.keySet()).indexOf(currency);
      int change = positionOptionTwo - positionOptionOne;
      positionsChange.put(currency, change);

    }

    positionChangeSorted = positionsChange.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));

    return positionChangeSorted;


  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Algoritms algoritms = (Algoritms) o;

    if (timeDiffrence != algoritms.timeDiffrence) {
      return false;
    }
    if (timeOptionOne != algoritms.timeOptionOne) {
      return false;
    }
    if (timeOptionTwo != algoritms.timeOptionTwo) {
      return false;
    }
    if (poloniexData != null ? !poloniexData.equals(algoritms.poloniexData)
        : algoritms.poloniexData != null) {
      return false;
    }
    if (allPrices != null ? !allPrices.equals(algoritms.allPrices) : algoritms.allPrices != null) {
      return false;
    }
    if (positionChangeSorted != null ? !positionChangeSorted.equals(algoritms.positionChangeSorted)
        : algoritms.positionChangeSorted != null) {
      return false;
    }
    if (positionsChange != null ? !positionsChange.equals(algoritms.positionsChange)
        : algoritms.positionsChange != null) {
      return false;
    }
    if (sortedOptionOne != null ? !sortedOptionOne.equals(algoritms.sortedOptionOne)
        : algoritms.sortedOptionOne != null) {
      return false;
    }
    if (sortedOptionTwo != null ? !sortedOptionTwo.equals(algoritms.sortedOptionTwo)
        : algoritms.sortedOptionTwo != null) {
      return false;
    }
    if (diffrenseOptionOne != null ? !diffrenseOptionOne.equals(algoritms.diffrenseOptionOne)
        : algoritms.diffrenseOptionOne != null) {
      return false;
    }
    return diffrenseOptionTwo != null ? diffrenseOptionTwo.equals(algoritms.diffrenseOptionTwo)
        : algoritms.diffrenseOptionTwo == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (timeDiffrence ^ (timeDiffrence >>> 32));
    result = 31 * result + (int) (timeOptionOne ^ (timeOptionOne >>> 32));
    result = 31 * result + (int) (timeOptionTwo ^ (timeOptionTwo >>> 32));
    result = 31 * result + (poloniexData != null ? poloniexData.hashCode() : 0);
    result = 31 * result + (allPrices != null ? allPrices.hashCode() : 0);
    result = 31 * result + (positionChangeSorted != null ? positionChangeSorted.hashCode() : 0);
    result = 31 * result + (positionsChange != null ? positionsChange.hashCode() : 0);
    result = 31 * result + (sortedOptionOne != null ? sortedOptionOne.hashCode() : 0);
    result = 31 * result + (sortedOptionTwo != null ? sortedOptionTwo.hashCode() : 0);
    result = 31 * result + (diffrenseOptionOne != null ? diffrenseOptionOne.hashCode() : 0);
    result = 31 * result + (diffrenseOptionTwo != null ? diffrenseOptionTwo.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Algoritms{" +
        "timeDiffrence=" + timeDiffrence +
        ", timeOptionOne=" + timeOptionOne +
        ", timeOptionTwo=" + timeOptionTwo +
        ", poloniexData=" + poloniexData +
        ", allPrices=" + allPrices +
        ", positionChangeSorted=" + positionChangeSorted +
        ", positionsChange=" + positionsChange +
        ", sortedOptionOne=" + sortedOptionOne +
        ", sortedOptionTwo=" + sortedOptionTwo +
        ", diffrenseOptionOne=" + diffrenseOptionOne +
        ", diffrenseOptionTwo=" + diffrenseOptionTwo +
        '}';
  }
}
