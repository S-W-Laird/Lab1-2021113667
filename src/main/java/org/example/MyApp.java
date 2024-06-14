package org.example;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * This class represents the main application for visualizing a directed graph.
 * It provides a graphical user interface (GUI) for loading text files, generating graphs,
 * finding bridge words, generating new text based on bridge words, finding the shortest path
 * between words, and performing random walks on the graph.
 */
public class MyApp extends JFrame {
  private GraphHandler graphHandler;
  private JTextField word1Box;
  private JTextField word2Box;
  private JTextField pathWord1Box;
  private JTextField pathWord2Box;
  private JTextField newTextBox;
  private JTextArea outputArea;
  private JFrame imageFrame;
  private JLabel imageLabel;
  private JScrollPane imageScrollPane;
  private JButton stopWalkButton;

  /**
   * Constructs a new MyApp instance and initializes the UI components.
   */
  public MyApp() {
    graphHandler = new GraphHandler();
    initLookAndFeel();
    initUi();
  }

  private void initLookAndFeel() {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  private void initUi() {
    setTitle("Graph Application");
    setSize(800, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    getContentPane().add(panel);

    JPanel inputPanel = new JPanel(new FlowLayout());

    JButton loadButton = new JButton("Load File");
    loadButton.setPreferredSize(new Dimension(200, 50));
    loadButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        loadFile();
      }
    });
    inputPanel.add(loadButton);

    JButton generateButton = new JButton("Generate Graph");
    generateButton.setPreferredSize(new Dimension(200, 50));
    generateButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        generateGraph();
      }
    });
    inputPanel.add(generateButton);

    panel.add(inputPanel);

    JPanel bridgePanel = new JPanel(new FlowLayout());

    JLabel word1Label = new JLabel("Word 1:");
    word1Box = new JTextField();
    word1Box.setPreferredSize(new Dimension(200, 50));
    bridgePanel.add(word1Label);
    bridgePanel.add(word1Box);

    JLabel word2Label = new JLabel("Word 2:");
    word2Box = new JTextField();
    word2Box.setPreferredSize(new Dimension(200, 50));
    bridgePanel.add(word2Label);
    bridgePanel.add(word2Box);

    JButton findBridgeButton = new JButton("Find Bridge Words");
    findBridgeButton.setPreferredSize(new Dimension(200, 50));
    findBridgeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        findBridgeWords();
      }
    });
    bridgePanel.add(findBridgeButton);

    panel.add(bridgePanel);

    JPanel pathPanel = new JPanel(new FlowLayout());

    JLabel pathWord1Label = new JLabel("Word 1:");
    pathWord1Box = new JTextField();
    pathWord1Box.setPreferredSize(new Dimension(200, 50));
    pathPanel.add(pathWord1Label);
    pathPanel.add(pathWord1Box);

    JLabel pathWord2Label = new JLabel("Word 2:");
    pathWord2Box = new JTextField();
    pathWord2Box.setPreferredSize(new Dimension(200, 50));
    pathPanel.add(pathWord2Label);
    pathPanel.add(pathWord2Box);

    JButton findPathButton = new JButton("Find Shortest Path");
    findPathButton.setPreferredSize(new Dimension(200, 50));
    findPathButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        findShortestPath();
      }
    });
    pathPanel.add(findPathButton);

    panel.add(pathPanel);

    JPanel generateTextPanel = new JPanel(new FlowLayout());

    JLabel newTextLabel = new JLabel("New Text:");
    newTextBox = new JTextField();
    newTextBox.setPreferredSize(new Dimension(400, 50));
    generateTextPanel.add(newTextLabel);
    generateTextPanel.add(newTextBox);

    JButton generateTextButton = new JButton("Generate New Text");
    generateTextButton.setPreferredSize(new Dimension(200, 50));
    generateTextButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        generateNewText();
      }
    });
    generateTextPanel.add(generateTextButton);

    panel.add(generateTextPanel);

    JPanel randomWalkPanel = new JPanel(new FlowLayout());

    JButton randomWalkButton = new JButton("Start Random Walk");
    randomWalkButton.setPreferredSize(new Dimension(200, 50));
    randomWalkButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        startRandomWalk();
      }
    });
    randomWalkPanel.add(randomWalkButton);

    stopWalkButton = new JButton("Stop Random Walk");
    stopWalkButton.setPreferredSize(new Dimension(200, 50));
    stopWalkButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        stopRandomWalk();
      }
    });
    stopWalkButton.setEnabled(false);
    randomWalkPanel.add(stopWalkButton);

    panel.add(randomWalkPanel);

    outputArea = new JTextArea(10, 50);
    outputArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(outputArea);
    panel.add(scrollPane);

    initImageFrame();

    setVisible(true);
  }

  private void initImageFrame() {
    imageFrame = new JFrame("Directed Graph");
    imageFrame.setSize(1100, 800);
    imageFrame.setLocationRelativeTo(null);
    imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    imageLabel = new JLabel();
    imageScrollPane = new JScrollPane(imageLabel);
    imageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    imageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    imageFrame.add(imageScrollPane);

    imageFrame.setVisible(true);
  }

  private void loadFile() {
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      try {
        BufferedReader br = new BufferedReader(new FileReader(selectedFile));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
          sb.append(line).append(" ");
        }
        br.close();
        String inputText = sb.toString();
        graphHandler.createGraph(inputText);
        outputArea.append("File loaded successfully.\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void generateGraph() {
    try {
      String timestamp = String.valueOf(System.currentTimeMillis());
      String dotFilePath = "graph/graph_" + timestamp + ".dot";
      String pngFilePath = "graph/graph_" + timestamp + ".png";
      graphHandler.showDirectedGraph(dotFilePath);
      displayImage(pngFilePath);
      outputArea.append("Graph generated successfully.\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void findBridgeWords() {
    String word1 = word1Box.getText();
    String word2 = word2Box.getText();
    String result = graphHandler.queryBridgeWords(word1, word2);
    outputArea.append(result + "\n");
  }

  private void generateNewText() {
    String inputText = newTextBox.getText();
    String result = graphHandler.generateNewText(inputText);
    outputArea.append(result + "\n");
  }

  private void findShortestPath() {
    String word1 = pathWord1Box.getText();
    String word2 = pathWord2Box.getText();
    String result = graphHandler.calcShortestPath(word1, word2);
    outputArea.append(result + "\n");
    try {
      String timestamp = String.valueOf(System.currentTimeMillis());
      String dotFilePath = "graph/graph_" + timestamp + ".dot";
      String pngFilePath = "graph/graph_" + timestamp + ".png";
      graphHandler.showDirectedGraph(dotFilePath);
      displayImage(pngFilePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void startRandomWalk() {
    stopWalkButton.setEnabled(true);
    String result = graphHandler.randomWalk();
    outputArea.append(result + "\n");
    stopWalkButton.setEnabled(false);
    try {
      String timestamp = String.valueOf(System.currentTimeMillis());
      String dotFilePath = "graph/graph_" + timestamp + ".dot";
      String pngFilePath = "graph/graph_" + timestamp + ".png";
      graphHandler.showDirectedGraph(dotFilePath);
      displayImage(pngFilePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void stopRandomWalk() {
    graphHandler.stopWalk();
    stopWalkButton.setEnabled(false);
    outputArea.append("Random walk stopped.\n");
  }

  private void displayImage(String filePath) {
    ImageIcon icon = new ImageIcon(filePath);
    imageLabel.setIcon(icon);
    imageLabel.setSize(icon.getIconWidth(), icon.getIconHeight());
    imageLabel.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    imageLabel.revalidate();
    imageLabel.repaint();
    imageScrollPane.revalidate();
    imageScrollPane.repaint();
  }

  /**
   * The main entry point for the application.
   * Initializes and displays the MyApp GUI.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        MyApp ex = new MyApp();
        ex.setVisible(true);
      }
    });
  }
}
