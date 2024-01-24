import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;

public class SegregationFrame extends JFrame {
    private int size = 30;
    private int DEFAULT_WIDTH = size * 21;
    private int DEFAULT_HEIGHT = size * 27 + 10; // change frame size due to size
    private ArrayList<ArrayList<JButton>> g_City = new ArrayList<ArrayList<JButton>>(size); // 2nd Dimension ArrayList for House(JButton)
    private ArrayList<JLabel> sutja = new ArrayList<JLabel>(); // ArrayList for changing label
    private double emptyRate = 0.25;
    private double tolerance = 0.35;
    private boolean diversity = false;
    private int n = 0; // n for step
    private Segregation sim = new Segregation(size, emptyRate); // Segregation Object

    class nemoPanel extends JPanel { // Jpanel for Houses(JButton)
        private int DEFAULT_WIDTH = size * 21;
        private int DEFAULT_HEIGHT = size * 21;
        private LineBorder puh = new LineBorder(Color.WHITE, 1); // White border for each House

        public nemoPanel() { // constructor
            for (int i = 0; i < size; i++)
                g_City.add(new ArrayList<JButton>()); // set 2nd Dimension ArrayList

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (sim.getTypeAt(i, j) == myType.HEADS) {
                        JButton button = new JButton();
                        button.setBackground(Color.BLUE);
                        button.setBorder(puh);
                        add(button); // make blue button & add to nemoPanel
                        g_City.get(i).add(j, button); // add button to g_City

                    } else if (sim.getTypeAt(i, j) == myType.TAILS) {
                        JButton button = new JButton();
                        button.setBackground(Color.RED);
                        button.setBorder(puh);
                        add(button); // make red button & add to nemoPanel
                        g_City.get(i).add(j, button); // add button to g_City

                    } else { // Empty
                        JButton button = new JButton();
                        button.setBackground(Color.WHITE);
                        button.setBorder(puh);
                        add(button); // make white button & add to nemoPanel
                        g_City.get(i).add(j, button); // add button to g_City

                    }
                }
            }
        } // complete constructing nemoPanel & g_City

        public Dimension getPreferredSize() {
            return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }

    class wPanel extends JPanel {
        private int DEFAULT_WIDTH = size * 10;
        private int DEFAULT_HEIGHT = size * 5;

        public wPanel() { // constructor
            double u = (double) sim.countStates()[1]; // unhappy
            double h = (double) sim.countStates()[2]; // happy
            double sum = (double) sim.countStates()[1] + sim.countStates()[2]; // sum
            double ph = h / sum * 100; // probability of happy
            double puh = u / sum * 100; // probability of unhappy
            double f = (double) sim.countTypes()[0] + sim.countTypes()[1]; // filled
            double seg = (double) sim.countSegregated(); // segregated
            double ps = seg / f * 100; // probability of segregated

            String ph_s = String.format("%.2f", ph);
            String puh_s = String.format("%.2f", puh);
            String ps_s = String.format("%.2f", ps); // String representation with .2f format

            /*----------------------------------------Creating & adding labels----------------------------------------*/
            add(new JLabel("Size", SwingConstants.RIGHT));
            JLabel sizeLabel = new JLabel(size + "", SwingConstants.RIGHT);
            Border lined = BorderFactory.createLineBorder(Color.BLUE);
            sizeLabel.setBorder(lined);
            add(sizeLabel);

            add(new JLabel("Step", SwingConstants.RIGHT));
            JLabel stepLabel = new JLabel(n + "", SwingConstants.RIGHT);
            stepLabel.setBorder(lined);
            add(stepLabel);

            JLabel hLabel = new JLabel("Heads", SwingConstants.RIGHT);
            hLabel.setOpaque(true);
            hLabel.setBackground(Color.BLUE);
            hLabel.setForeground(Color.WHITE);
            add(hLabel);
            JLabel headLabel = new JLabel(sim.countTypes()[0] + "", SwingConstants.RIGHT);
            headLabel.setBorder(lined);
            add(headLabel);

            add(new JLabel("UnHappy", SwingConstants.RIGHT));
            JLabel unhappyLabel = new JLabel(sim.countStates()[1] + "(" + puh_s + ")", SwingConstants.RIGHT);
            unhappyLabel.setBorder(lined);
            add(unhappyLabel);

            JLabel tLabel = new JLabel("Tails", SwingConstants.RIGHT);
            tLabel.setOpaque(true);
            tLabel.setBackground(Color.RED);
            tLabel.setForeground(Color.WHITE);
            add(tLabel);
            JLabel tailLabel = new JLabel(sim.countTypes()[1] + "", SwingConstants.RIGHT);
            tailLabel.setBorder(lined);
            add(tailLabel);

            add(new JLabel("Happy", SwingConstants.RIGHT));
            JLabel happyLabel = new JLabel(sim.countStates()[2] + "(" + ph_s + ")", SwingConstants.RIGHT);
            happyLabel.setBorder(lined);
            add(happyLabel);

            add(new JLabel("Empty", SwingConstants.RIGHT));
            JLabel emptyLabel = new JLabel(sim.countTypes()[2] + "", SwingConstants.RIGHT);
            emptyLabel.setBorder(lined);
            add(emptyLabel);

            add(new JLabel("Segregated", SwingConstants.RIGHT));
            JLabel segregatedLabel = new JLabel(sim.countSegregated() + "(" + ps_s + ")", SwingConstants.RIGHT);
            segregatedLabel.setBorder(lined);
            add(segregatedLabel);

            /*----------------------------------------Creating & adding labels----------------------------------------*/

            sutja.add(sizeLabel);
            sutja.add(stepLabel);
            sutja.add(headLabel);
            sutja.add(unhappyLabel);
            sutja.add(tailLabel);
            sutja.add(happyLabel);
            sutja.add(emptyLabel);
            sutja.add(segregatedLabel); // Adding labels to ArrayList
        }

        public Dimension getPreferredSize() {
            return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }

    class ePanel extends JPanel implements ActionListener { // East Panel implements ActionListener
        private int DEFAULT_WIDTH = size * 11;
        private int DEFAULT_HEIGHT = size * 5;
        private JCheckBox checkBox = new JCheckBox("Diversity (at least 1 must be different)");
        private JSlider slider = new JSlider(0, 100, 35);
        private String text3 = "";
        JLabel label = new JLabel("Tolerence");
        JLabel label2 = new JLabel(slider.getValue() + "%");
        JLabel label3 = new JLabel();
        // empty label for placing slider at center of ePanel

        public ePanel() {
            Border lined = BorderFactory.createLineBorder(Color.BLUE);
            checkBox.addActionListener(this); // add this ActionListener

            slider.addChangeListener(new ChangeListener() { // add new ChangeListener at slider
                public void stateChanged(ChangeEvent c) {
                    tolerance = (double) slider.getValue() / 100;
                    label2.setText(slider.getValue() + "%");
                } // which set tolerance and set text due to slider's value
            });

            for (int i = 0; i < size * 11; i++)
                text3 += " ";
            label3.setText(text3);

            add(label3);
            add(label);
            add(slider);
            label2.setBorder(lined);
            add(label2);
            add(checkBox);
        }

        public void actionPerformed(ActionEvent e) { // actionPerformed method for checkbox
            if (checkBox.isSelected())
                diversity = true;
            else if (!checkBox.isSelected())
                diversity = false;
        }

        public Dimension getPreferredSize() {
            return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }

    class sPanel extends JPanel implements ActionListener { // south panel implements ActionListener
        private int DEFAULT_WIDTH = size * 21;
        private int DEFAULT_HEIGHT = size + 10;
        private JButton resetButton = new JButton("Reset");
        private JButton runButton = new JButton("Run");
        private JButton stepButton = new JButton("Step");
        private Segregation mySim; // Segregation variable for reseting

        public sPanel() { // constructor

            add(resetButton);
            add(runButton);
            add(stepButton);

            resetButton.addActionListener(this); // add this ActionListener
            runButton.addActionListener(this); // add this ActionListener
            stepButton.addActionListener(this); // add this ActionListener
        }

        public void actionPerformed(ActionEvent e) { // actionPerformed

            if (e.getSource() == resetButton) { // reset
                Runnable r1 = new Runnable() {
                    public void run() {
                        n = 0; // step = 0
                        mySim = new Segregation(size, emptyRate); // Create new Segregation object
                        sim = mySim; // apply
                        sim.updateState(tolerance, diversity);
                        double u = (double) sim.countStates()[1]; // unhappy
                        double h = (double) sim.countStates()[2]; // happy
                        double sum = (double) sim.countStates()[1] + sim.countStates()[2]; // sum
                        double ph = h / sum * 100; // probability of happy
                        double puh = u / sum * 100; // probability of unhappy
                        double f = (double) sim.countTypes()[0] + sim.countTypes()[1]; // filled
                        double seg = (double) sim.countSegregated(); // segregated
                        double ps = seg / f * 100; // probability of segregated

                        String ph_s = String.format("%.2f", ph);
                        String puh_s = String.format("%.2f", puh);
                        String ps_s = String.format("%.2f", ps); // String representation with .2f format

                        for (int i = 0; i < size; i++) {
                            for (int j = 0; j < size; j++) {
                                if (sim.getTypeAt(i, j) == myType.HEADS)
                                    g_City.get(i).get(j).setBackground(Color.BLUE);
                                else if (sim.getTypeAt(i, j) == myType.TAILS)
                                    g_City.get(i).get(j).setBackground(Color.RED);
                                else
                                    g_City.get(i).get(j).setBackground(Color.WHITE);
                            }
                        } // g_City.get(i).get(j) gets button(House) at (i,j) location

                        sutja.get(0).setText(size + "");
                        sutja.get(1).setText("0");
                        sutja.get(2).setText(sim.countTypes()[0] + "");
                        sutja.get(3).setText(sim.countStates()[1] + "(" + puh_s + ")");
                        sutja.get(4).setText(sim.countTypes()[1] + "");
                        sutja.get(5).setText(sim.countStates()[2] + "(" + ph_s + ")");
                        sutja.get(6).setText(sim.countTypes()[2] + "");
                        sutja.get(7).setText(sim.countSegregated() + "(" + ps_s + ")"); // set each label's text due to updated city
                    }
                };
                Thread t = new Thread(r1);
                t.start();
            }

            else if (e.getSource() == runButton) { // run till 0 unHappy House
                Runnable r2 = new Runnable() {
                    public void run() {
                        while (true) {
                            sim.move();
                            sim.updateState(tolerance, diversity);
                            for (int i = 0; i < size; i++) {
                                for (int j = 0; j < size; j++) {
                                    if (sim.getTypeAt(i, j) == myType.HEADS)
                                        g_City.get(i).get(j).setBackground(Color.BLUE);
                                    else if (sim.getTypeAt(i, j) == myType.TAILS)
                                        g_City.get(i).get(j).setBackground(Color.RED);
                                    else
                                        g_City.get(i).get(j).setBackground(Color.WHITE);
                                }
                            } // g_City.get(i).get(j) gets button(House) at (i,j) location
                            n++; // one move, one step ++

                            double u = (double) sim.countStates()[1]; // unhappy
                            double h = (double) sim.countStates()[2]; // happy
                            double sum = (double) sim.countStates()[1] + sim.countStates()[2]; // sum
                            double ph = h / sum * 100;
                            double puh = u / sum * 100;
                            double f = (double) sim.countTypes()[0] + sim.countTypes()[1]; // filled
                            double seg = (double) sim.countSegregated(); // segregated
                            double ps = seg / f * 100;
                            String ps_s = String.format("%.2f", ps);
                            String ph_s = String.format("%.2f", ph);
                            String puh_s = String.format("%.2f", puh);

                            sutja.get(0).setText(size + "");
                            sutja.get(1).setText(n + "");
                            sutja.get(2).setText(sim.countTypes()[0] + "");
                            sutja.get(3).setText(sim.countStates()[1] + "(" + puh_s + ")");
                            sutja.get(4).setText(sim.countTypes()[1] + "");
                            sutja.get(5).setText(sim.countStates()[2] + "(" + ph_s + ")");
                            sutja.get(6).setText(sim.countTypes()[2] + "");
                            sutja.get(7).setText(sim.countSegregated() + "(" + ps_s + ")");
                            int[] counts = sim.countStates();
                            if (counts[State.UNHAPPY.ordinal()] == 0) {
                                n--; // prevent n increses when pressing run button but city already has no
                                     // unHappy house.
                                break;
                            }
                        }
                    }
                };
                Thread t = new Thread(r2);
                t.start();
            }

            else if (e.getSource() == stepButton) { // 1 click, 1 move
                // no while loop
                sim.move();
                sim.updateState(tolerance, diversity);

                double u = (double) sim.countStates()[1]; // unhappy
                double h = (double) sim.countStates()[2]; // happy
                double sum = (double) sim.countStates()[1] + sim.countStates()[2]; // sum
                double ph = h / sum * 100;
                double puh = u / sum * 100;
                double f = (double) sim.countTypes()[0] + sim.countTypes()[1]; // filled
                double seg = (double) sim.countSegregated(); // segregated
                double ps = seg / f * 100;
                String ps_s = String.format("%.2f", ps);
                String ph_s = String.format("%.2f", ph);
                String puh_s = String.format("%.2f", puh);

                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (sim.getTypeAt(i, j) == myType.HEADS)
                            g_City.get(i).get(j).setBackground(Color.BLUE);
                        else if (sim.getTypeAt(i, j) == myType.TAILS)
                            g_City.get(i).get(j).setBackground(Color.RED);
                        else
                            g_City.get(i).get(j).setBackground(Color.WHITE);
                    }
                } // g_City.get(i).get(j) gets button(House) at (i,j) location

                n++;
                sutja.get(0).setText(size + "");
                sutja.get(1).setText(n + "");
                sutja.get(2).setText(sim.countTypes()[0] + "");
                sutja.get(3).setText(sim.countStates()[1] + "(" + puh_s + ")");
                sutja.get(4).setText(sim.countTypes()[1] + "");
                sutja.get(5).setText(sim.countStates()[2] + "(" + ph_s + ")");
                sutja.get(6).setText(sim.countTypes()[2] + "");
                sutja.get(7).setText(sim.countSegregated() + "(" + ps_s + ")");
                int[] counts = sim.countStates();
                if (counts[State.UNHAPPY.ordinal()] == 0)
                    n--; // prevent n increses when pressing step button but city already has no unHappy
                         // house.
            }
        }

        public Dimension getPreferredSize() {
            return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }

    public SegregationFrame() { // constructor of frame
        sim.updateState(tolerance, diversity);
        JPanel nemoPanel = new nemoPanel();
        JPanel wPanel = new wPanel();
        nemoPanel.setLayout(new GridLayout(size, size));
        wPanel.setLayout(new GridLayout(4, 4));
        add(nemoPanel, BorderLayout.NORTH);
        add(wPanel, BorderLayout.WEST);
        add(new ePanel(), BorderLayout.EAST);
        add(new sPanel(), BorderLayout.SOUTH);
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

}