package Window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Created on 22.08.2017.
 *
 * @author Serhii Petrusha aka Mr_Rism
 */
public class Window extends JFrame {

  private final long START_TIME = 86400;

  private final int MINIMAL_TIME =  1800;

  private long referenseTime = 82200;

  private AlgoritmsUIAdapt data = new AlgoritmsUIAdapt(START_TIME, referenseTime);

  private JList<String> curTopList;
  private JList<String> tShiftList;
  private JList<String> gainersList;

  private Map<String, JCheckBox> checkBoxes = new HashMap<>();

  public Window(String title) {
    super(title);
    setSize(new Dimension(700, 500));
    JPanel mainPanel = new JPanel();
    GridLayout mainFrameLayout = new GridLayout(1, 3);
    mainPanel.setLayout(mainFrameLayout);
    add(mainPanel, BorderLayout.CENTER);
    JPanel timePanel = new JPanel();
    add(timePanel, BorderLayout.NORTH);
    JPanel statusPanel = new JPanel();
    statusPanel.setLayout(new BorderLayout());
    add(statusPanel, BorderLayout.SOUTH);
    JPanel rightStatusPanel = new JPanel();
    statusPanel.add(rightStatusPanel, BorderLayout.EAST);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    JPanel currentTopPanel = new JPanel();
    JPanel tShiftTopPanel = new JPanel();
    JPanel gainersPanel = new JPanel();
    mainPanel.add(currentTopPanel);
    mainPanel.add(tShiftTopPanel);
    mainPanel.add(gainersPanel);
    currentTopPanel.setLayout(new BorderLayout());
    currentTopPanel.add(new JLabel("Top 24h"), BorderLayout.NORTH);
    tShiftTopPanel.setLayout(new BorderLayout());
    tShiftTopPanel.add(new JLabel("Time shift top"), BorderLayout.NORTH);
    gainersPanel.setLayout(new BorderLayout());
    gainersPanel.add(new JLabel("Gainers"), BorderLayout.NORTH);
    curTopList = new JList<>();
    tShiftList = new JList<>();
    gainersList = new JList<>();
    JScrollPane curTopScroll = new JScrollPane(curTopList);
    JScrollPane tShiftScroll = new JScrollPane(tShiftList);
    JScrollPane gainersScroll = new JScrollPane(gainersList);
    currentTopPanel.add(curTopScroll, BorderLayout.CENTER);
    tShiftTopPanel.add(tShiftScroll, BorderLayout.CENTER);
    gainersPanel.add(gainersScroll, BorderLayout.CENTER);

    populateJList(curTopList, data.getSortedOptionOne());
    populateJList(tShiftList, data.getSortedOptionTwo());
    populateJList(gainersList, data.getPositionChangeSorted());

    for (JCheckBox s : checkBoxes.values()) {
      rightStatusPanel.add(s);
      s.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
          populateJList(curTopList, data.getSortedOptionOne());
          populateJList(tShiftList, data.getSortedOptionTwo());
          populateJList(gainersList, data.getPositionChangeSorted());
        }
      });
    }
    JButton refreshButton = new JButton("⟳");
    rightStatusPanel.add(refreshButton);
    refreshButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        data.refresh();
        populateJList(curTopList, data.getSortedOptionOne());
        populateJList(tShiftList, data.getSortedOptionTwo());
        populateJList(gainersList, data.getPositionChangeSorted());
      }
    });


    JSlider timeSlider = new JSlider(MINIMAL_TIME, (int) data.getTimeDiffrence(), (int) referenseTime);
    timePanel.add(timeSlider);
    timeSlider.setMajorTickSpacing((int)data.getTimeDiffrence()-MINIMAL_TIME);

    JLabel selectedTimeLable = new JLabel(""+
        timeSlider.getValue()/3600+":"+(timeSlider.getValue()%3600)/60);
    timePanel.add(selectedTimeLable);

    timeSlider.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {

      }

      @Override
      public void mousePressed(MouseEvent e) {

      }

      @Override
      public void mouseReleased(MouseEvent e) {

        data.reCalculate(data.getTimeDiffrence(), timeSlider.getValue());
        populateJList(curTopList, data.getSortedOptionOne());
        populateJList(tShiftList, data.getSortedOptionTwo());
        populateJList(gainersList, data.getPositionChangeSorted());
        selectedTimeLable.setText(timeSlider.getValue()/3600+":"+(timeSlider.getValue()%3600)/60);

      }

      @Override
      public void mouseEntered(MouseEvent e) {

      }

      @Override
      public void mouseExited(MouseEvent e) {

      }
    });



    setVisible(true);


  }

  private <T extends Number> void populateJList(JList<String> jList, Map<String, T> map) {
    if (jList != null) {
      if (map != null) {
        jList.removeAll();
        DefaultListModel<String> defaultListModel = new DefaultListModel();

        for (String s : map.keySet()) {
          String currentCuriency = s.substring(0, s.indexOf("_"));
          if (checkBoxes.get(currentCuriency) == null) {
            checkBoxes.put(currentCuriency, new JCheckBox(currentCuriency));
            checkBoxes.get(currentCuriency).setSelected(true);
          }
          if (checkBoxes.get(currentCuriency).isSelected()) {
            defaultListModel.addElement(s + " " + map.get(s));
          }
        }
        jList.setModel(defaultListModel);
      }
    }
  }

}
