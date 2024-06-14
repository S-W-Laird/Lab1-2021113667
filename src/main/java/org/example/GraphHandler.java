package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles the creation and manipulation of a directed weighted graph.
 * It supports functionalities such as creating a graph from text, finding bridge words,
 * generating new text based on bridge words, calculating the shortest path between words,
 * and performing random walks on the graph.
 */
public class GraphHandler {
  private Map<String, Map<String, Integer>> graph;
  private List<String> shortestPathNodes;
  private Set<String> shortestPathEdges;
  private boolean stopWalk;

  /**
   * Constructs a new GraphHandler with an empty graph.
   */
  public GraphHandler() {
    this.graph = new HashMap<>();
    this.shortestPathNodes = new ArrayList<>();
    this.shortestPathEdges = new HashSet<>();
    this.stopWalk = false;
  }

  /**
   * Creates a directed weighted graph from the input text.
   * Each word in the text is treated as a vertex, and consecutive words are
   * connected by directed edges.
   *
   * @param inputText the input text from which to create the graph
   */
  public void createGraph(String inputText) {
    graph = new HashMap<>();
    Pattern pattern = Pattern.compile("\\b[a-zA-Z]+\\b");
    Matcher matcher = pattern.matcher(inputText.toLowerCase());
    List<String> words = new ArrayList<>();
    while (matcher.find()) {
      words.add(matcher.group().toLowerCase());
    }

    for (String word : words) {
      graph.putIfAbsent(word, new HashMap<>()); // 确保每个单词都被添加为顶点
    }

    for (int i = 0; i < words.size() - 1; i++) {
      String source = words.get(i);
      String target = words.get(i + 1);
      Map<String, Integer> edges = graph.get(source);
      edges.put(target, edges.getOrDefault(target, 0) + 1);
      System.out.println("Added edge: " + source + " -> " + target);
    }
  }

  /**
   * Generates a Graphviz DOT file and renders it to an image file.
   *
   * @param filename the name of the DOT file to generate
   * @throws IOException if an I/O error occurs
   */
  public void showDirectedGraph(String filename) throws IOException {
    generateGraphvizFile(filename);
    renderGraph(filename, filename.replace(".dot", ".png"));
  }

  /**
   * Queries the bridge words between two given words in the graph.
   *
   * @param word1 the first word
   * @param word2 the second word
   * @return a string describing the bridge words between word1 and word2
   */
  public String queryBridgeWords(String word1, String word2) {
    if (word1 == null || word2 == null) {
      return "Input words cannot be null!";
    }

    if (!graph.containsKey(word1) && !graph.containsKey(word2)) {
      return "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
    } else if (!graph.containsKey(word1)) {
      return "No \"" + word1 + "\" in the graph!";
    } else if (!graph.containsKey(word2)) {
      return "No \"" + word2 + "\" in the graph!";
    }

    Set<String> edges = graph.get(word1).keySet();
    List<String> bridgeWords = new ArrayList<>();
    for (String successor : edges) {
      if (graph.containsKey(successor) && graph.get(successor).containsKey(word2)) {
        bridgeWords.add(successor);
      }
    }

    if (bridgeWords.isEmpty()) {
      return "No bridge words from " + word1 + " to " + word2 + "!";
    }
    return "The bridge words from " + word1 + " to " + word2 + " are: "
            + String.join(", ", bridgeWords);
  }

  /**
   * Generates new text by inserting bridge words between consecutive words in the input text.
   *
   * @param inputText the input text
   * @return the generated text with bridge words inserted
   */
  public String generateNewText(String inputText) {
    Pattern pattern = Pattern.compile("\\b[a-zA-Z]+\\b");
    Matcher matcher = pattern.matcher(inputText.toLowerCase());
    List<String> words = new ArrayList<>();
    while (matcher.find()) {
      words.add(matcher.group());
    }

    StringBuilder newWords = new StringBuilder();
    Random random = new Random();

    for (int i = 0; i < words.size() - 1; i++) {
      String word1 = words.get(i);
      String word2 = words.get(i + 1);
      newWords.append(word1).append(" ");

      if (graph.containsKey(word1) && graph.containsKey(word2)) {
        Set<String> edges = graph.get(word1).keySet();
        List<String> bridgeWords = new ArrayList<>();
        for (String successor : edges) {
          if (graph.containsKey(successor) && graph.get(successor).containsKey(word2)) {
            bridgeWords.add(successor);
          }
        }
        if (!bridgeWords.isEmpty()) {
          String bridgeWord = bridgeWords.get(random.nextInt(bridgeWords.size()));
          newWords.append(bridgeWord).append(" ");
        }
      }
    }
    newWords.append(words.get(words.size() - 1));
    return newWords.toString();
  }

  /**
   * Calculates the shortest path between two words in the graph using Dijkstra's algorithm.
   *
   * @param word1 the starting word
   * @param word2 the ending word
   * @return a string describing the shortest path and its total weight
   */
  public String calcShortestPath(String word1, String word2) {
    word1 = word1.toLowerCase();  // 确保输入单词转换为小写
    word2 = word2.toLowerCase();  // 确保输入单词转换为小写

    shortestPathNodes.clear();
    shortestPathEdges.clear();

    if (!graph.containsKey(word1) && !graph.containsKey(word2)) {
      return "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
    } else if (!graph.containsKey(word1)) {
      return "No \"" + word1 + "\" in the graph!";
    } else if (!graph.containsKey(word2)) {
      return "No \"" + word2 + "\" in the graph!";
    }

    Map<String, Double> distances = new HashMap<>();
    Map<String, String> previous = new HashMap<>();
    PriorityQueue<String> nodes = new PriorityQueue<>(Comparator.comparing(distances::get));

    for (String vertex : graph.keySet()) {
      if (vertex.equals(word1)) {
        distances.put(vertex, 0.0);
      } else {
        distances.put(vertex, Double.MAX_VALUE);
      }
      nodes.add(vertex);
    }

    while (!nodes.isEmpty()) {
      String smallest = nodes.poll();
      if (distances.get(smallest) == Double.MAX_VALUE) {
        return "No path from " + word1 + " to " + word2 + "!";
      }
      if (smallest.equals(word2)) {
        List<String> path = new ArrayList<>();
        double pathLength = distances.get(smallest);  // 获取正确的路径长度
        while (previous.containsKey(smallest)) {
          path.add(smallest);
          String prev = previous.get(smallest);
          shortestPathEdges.add("\"" + prev + "\" -> \"" + smallest + "\"");
          smallest = prev;
        }
        path.add(word1);
        Collections.reverse(path);
        shortestPathNodes = path;
        return "The shortest path from " + word1 + " to " + word2 + " is: "
                + String.join(" -> ", path) + " with total weight " + pathLength;
      }

      for (Map.Entry<String, Integer> neighbor : graph.get(smallest).entrySet()) {
        double alt = distances.get(smallest) + neighbor.getValue();
        if (alt < distances.get(neighbor.getKey())) {
          distances.put(neighbor.getKey(), alt);
          previous.put(neighbor.getKey(), smallest);
          nodes.remove(neighbor.getKey());
          nodes.add(neighbor.getKey());
        }
      }
    }

    return "No path from " + word1 + " to " + word2 + "!";
  }


  /**
   * Performs a random walk starting from a randomly selected node in the graph.
   * The walk continues until there are no outgoing edges from the current node
   * or the `stopWalk` flag is set to true.
   *
   * @return a string describing the random walk sequence of nodes
   */
  public String randomWalk() {
    stopWalk = false;

    if (graph.isEmpty()) {
      return "The graph is empty!";
    }

    Random random = new Random();
    List<String> keys = new ArrayList<>(graph.keySet());
    String startNode = keys.get(random.nextInt(keys.size()));

    StringBuilder walkResult = new StringBuilder("Random walk starting from " + startNode + ": ");
    List<String> visitedNodes = new ArrayList<>();
    Set<String> visitedEdges = new HashSet<>();
    String currentNode = startNode;

    while (!stopWalk && graph.containsKey(currentNode)) {
      visitedNodes.add(currentNode);
      Set<String> outgoingEdges = graph.get(currentNode).keySet();
      if (outgoingEdges.isEmpty()) {
        break;
      }
      List<String> edges = new ArrayList<>(outgoingEdges);
      String edge = edges.get(random.nextInt(edges.size()));
      if (visitedEdges.contains(edge)) {
        break;
      }
      visitedEdges.add(edge);
      currentNode = edge;
    }

    walkResult.append(String.join(" -> ", visitedNodes));
    return walkResult.toString();
  }

  public void stopWalk() {
    this.stopWalk = true;
  }

  private void generateGraphvizFile(String filename) throws IOException {
    StringBuilder dotContent = new StringBuilder("digraph G {\n");

    for (String source : graph.keySet()) {
      // System.out.println("Vertex: " + source);  // 输出顶点信息

      for (Map.Entry<String, Integer> target : graph.get(source).entrySet()) {
        String edge = "\"" + source + "\" -> \"" + target.getKey() + "\"";
        // System.out.println("Edge: " + edge + " [label=\"" + target.getValue() + "\"]");  // 输出边信息

        dotContent.append("    ").append(edge);
        dotContent.append(" [label=\"").append(target.getValue()).append("\"");
        if (shortestPathEdges.contains(edge)) {
          dotContent.append(" color=\"red\"");
        }
        dotContent.append("];\n");
      }
    }

    dotContent.append("}");

    try (FileWriter writer = new FileWriter(filename)) {
      writer.write(dotContent.toString());
    }
  }

  private void renderGraph(String dotFilePath, String outputFilePath) {
    try {
      Process process = Runtime.getRuntime().exec("dot -Tpng "
              + dotFilePath + " -o " + outputFilePath);
      process.waitFor();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
