package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphHandlerTest {
  private GraphHandler graphHandler;

  @BeforeEach
  public void setUp() {
    graphHandler = new GraphHandler();
    String inputText = "this is a test this test is a simple test";
    graphHandler.createGraph(inputText);
  }

  @Test
  public void testQueryBridgeWordsBothWordsNull() {
    String result = graphHandler.queryBridgeWords("this", null);
    assertEquals("Input words cannot be null!", result);
  }

  @Test
  public void testQueryBridgeWordsBothWordsNotInGraph() {
    String result = graphHandler.queryBridgeWords("not", "exist");
    assertEquals("No \"not\" and \"exist\" in the graph!", result);
  }

  @Test
  public void testQueryBridgeWordsFirstWordNotInGraph() {
    String result = graphHandler.queryBridgeWords("aaa", "a");
    assertEquals("No \"aaa\" in the graph!", result);
  }

  @Test
  public void testQueryBridgeWordsSecondWordNotInGraph() {
    String result = graphHandler.queryBridgeWords("this", "not");
    assertEquals("No \"not\" in the graph!", result);
  }

  @Test
  public void testQueryBridgeWordsNoBridgeWords() {
    String result = graphHandler.queryBridgeWords("this", "simple");
    assertEquals("No bridge words from this to simple!", result);
  }

  @Test
  public void testQueryBridgeWordsWithBridgeWords() {
    String result = graphHandler.queryBridgeWords("this", "is");
    assertEquals("The bridge words from this to is are: test", result);
  }
}