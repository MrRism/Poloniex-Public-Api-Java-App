package Window;

import base.Algoritms;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Created on 22.08.2017.
 *
 * @author Serhii Petrusha aka Mr_Rism
 */
public class AlgoritmsUIAdapt{

  private Algoritms delegate;

  private MassageWindow message = new MassageWindow("Loading from Poloniex...May take couple of minutes");

  public AlgoritmsUIAdapt(long timeOptionOne, long timeOptionTwo) {
    message.showMassege();
    delegate = new Algoritms(timeOptionOne, timeOptionTwo);
    message.hideMassage();
      }

  public Map<String, List<Double>> getAllPrices() {
    return delegate.getAllPrices();
  }

  public Map<String, Integer> getPositionChangeSorted() {
    return delegate.getPositionChangeSorted();
  }

  public Map<String, Integer> getPositionsChange() {
    return delegate.getPositionsChange();
  }

  public Map<String, Double> getSortedOptionOne() {
    return delegate.getSortedOptionOne();
  }

  public Map<String, Double> getSortedOptionTwo() {
    return delegate.getSortedOptionTwo();
  }

  public Map<String, Double> getDiffrenseOptionOne() {
    return delegate.getDiffrenseOptionOne();
  }

  public Map<String, Double> getDiffrenseOptionTwo() {
    return delegate.getDiffrenseOptionTwo();
  }

  public void refresh() {
    message.showMassege();
    delegate.update();
    message.hideMassage();

  }

  public void reCalculate(long timeOptionOne, long timeOptionTwo) {

    delegate.reCalculate(timeOptionOne, timeOptionTwo);

  }

  public long getTimeDiffrence() {
    return delegate.getTimeDiffrence();
  }

  private class MassageWindow extends JFrame{
    MassageWindow(String textToDisplay){
      super(textToDisplay);
      JPanel mainPanel = new JPanel();
      mainPanel.setAlignmentX(0.5f);
      add(mainPanel);
      mainPanel.add(new JLabel(textToDisplay));
      setResizable(false);
      setUndecorated(true);
      setSize(new Dimension(300,80));
      setLocationRelativeTo(null);

    }
    void showMassege(){
      setLocationRelativeTo(null);
      setVisible(true);
    }
    void hideMassage(){
      setVisible(false);
    }
  }


}
