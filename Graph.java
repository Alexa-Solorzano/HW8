/******************************************************************
 *
 *   Alexa Solorzano COMP 272 001
 *
 *   Note, additional comments provided throughout this source code
 *   is for educational purposes
 *
 ********************************************************************/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 *  Graph traversal exercise
 *
 *  The Graph class is a representing an oversimplified Directed Graph of vertices
 *  (nodes) and edges. The graph is stored in an adjacency list
 */

public class Graph {
  int numVertices;                  // vertices in graph
  LinkedList<Integer>[] adjListArr; // Adjacency list
  List<Integer> vertexValues;       // vertex values

  // Constructor 
  public Graph(int numV) {
    numVertices = numV;
    adjListArr = new LinkedList[numVertices];
    vertexValues = new ArrayList<>(numVertices);

    for (int i = 0; i < numVertices; i++) {
      adjListArr[i] = new LinkedList<>();
      vertexValues.add(0);
    }
  }

  /*
   * method setValue
   * 
   * Sets a vertex's (node's) value.
   */ 
  
  public void setValue(int vertexIndex, int value) {
    if (vertexIndex >= 0 && vertexIndex < numVertices) {
      vertexValues.set(vertexIndex, value);
    } else {
      throw new IllegalArgumentException(
             "Invalid vertex index: " + vertexIndex);
    }
  }


  public void addEdge(int src, int dest) {
    adjListArr[src].add(dest);
  }

  /*
   * method printGraph
   * 
   * Prints the graph as an adjacency matrix
   */ 
  
  public void printGraph() {
    System.out.println(
         "\nAdjacency Matrix Representation:\n");
    int[][] matrix = new int[numVertices][numVertices];

    for (int i = 0; i < numVertices; i++) {
      for (Integer dest : adjListArr[i]) {
        matrix[i][dest] = 1;
      }
    }

    System.out.print("  ");
    for (int i = 0; i < numVertices; i++) {
      System.out.print(i + " ");
    }
    System.out.println();

    for (int i = 0; i < numVertices; i++) {
      System.out.print(i + " ");
      for (int j = 0; j < numVertices; j++) {
        if (matrix[i][j] == 1) {
          System.out.print("| ");
        } else {
          System.out.print(". ");
        }
      }
      System.out.println();
    }
  }


  /**
   * method findRoot
   *
   * This method returns the value of the root vertex, where root is defined in
   * this case as a node that has no incoming edges. If no root vertex is found
   * and/or more than one root vertex, then return -1.
   * 
   * Important information includes 
   * -- keeping track of how many edges point to each vertext because the root should be the only one with zero in-degrees
   * -- build a in-degree array based on the graph's structure & relationships
   * -- knowing if there are multiple zero in-degrees because that would indicate that there are multiple candidates which would be invalid
   * -- keep track of potential root candidates to return its value later
   * Pseudocode: 
   * Initialize an array to keep track of in-degrees of all vertices 
   * Traverse the adjacency list to count in-degrees for each vertex
   * Loop through in-degree array
   *  if in-digree is 0, check if a root is already found 
   *  if yes, return -1 (invalid)
   *  otherwise, store the index of this root 
   *  if no vertex with in-degree 0 was found, return -1
   *  otherwise, return the value of the root vertex
   */
  
  public int findRoot() {
    int[] inDegree = new int[numVertices]; //Initialize in-degree array
    //Count in-degrees by scanning adjacency list
    for(int current = 0; current < numVertices; current++){
      LinkedList<Integer> neighbors = adjListArr[current];
      for(int j = 0; j < neighbors.size(); j++){
        int destination = neighbors.get(j);
        inDegree[destination]++; //Count the incoming edges to destination
      }
    }

    int root = -1; //Initialize root as not found

    //Find vertex with in-degree of 0
    for(int i = 0; i < numVertices; i++){
      if(inDegree[i] == 0){
        if(root != -1){
          return -1; //More than one root found
        }
        root = i;
      }
    }
    //If no root found, return -1
    if(root == -1){
      return -1;
    }

    //Return the value of the root vertex
    return vertexValues.get(root);
  } 
}
