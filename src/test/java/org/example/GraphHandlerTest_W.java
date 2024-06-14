package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphHandlerTest_W {
  private GraphHandler graphHandler;

  @BeforeEach
  public void setUp() {
    graphHandler = new GraphHandler();
    String inputText = "Hi, this is a small test and this test is a simple test";
    graphHandler.createGraph(inputText);
  }

  @Test
  void testCalcShortestPathNoNodes() {
    String result = graphHandler.calcShortestPath("not", "in");
    assertEquals("No \"not\" and \"in\" in the graph!", result);
  }

  @Test
  void testCalcShortestPathNoEndNode() {
    String result = graphHandler.calcShortestPath("this", "not");
    assertEquals("No \"not\" in the graph!", result);
  }

  @Test
  void testCalcShortestPathNoStartNode() {
    String result = graphHandler.calcShortestPath("not", "this");
    assertEquals("No \"not\" in the graph!", result);
  }

  @Test
  void testCalcShortestPath() {
    String result = graphHandler.calcShortestPath("this", "test");
    assertEquals("The shortest path from this to test is: this -> test with total weight 1.0", result);
  }

  @Test
  void testCalcShortestPathNoShortestPath() {
    String result = graphHandler.calcShortestPath("simple", "hi");
    assertEquals("No path from simple to hi!", result);
  }
}