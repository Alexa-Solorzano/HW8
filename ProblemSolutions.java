/******************************************************************
 *
 *   Alexa Solorzano COMP 272 001
 *
 *   This java file contains the problem solutions of canFinish and
 *   numGroups methods.
 *
 ********************************************************************/

import java.util.*;

class ProblemSolutions {

    /**
     * Method canFinish
     *
     * You are building a course curriculum along with required intermediate
     * exams certifications that must be taken by programmers in order to obtain
     * a new certification called 'master programmer'. In doing so, you are placing
     * prerequisites on intermediate exam certifications that must be taken first.
     * You are allowing the flexibility of taking the exams in any order as long as
     * any exam prerequisites are satisfied.
     *
     * Unfortunately, in the past, your predecessors have accidentally published
     * curriculums and exam schedules that were not possible to complete due to cycles
     * in prerequisites. You want to avoid this embarrassment by making sure you define
     * a curriculum and exam schedule that can be completed.
     *
     * You goal is to ensure that any student pursuing the certificate of 'master
     * programmer', can complete 'n' certification exams, each being specific to a
     * topic. Some exams have prerequisites of needing to take and pass earlier
     * certificate exams. You do not want to force any order of taking the exams, but
     * you want to make sure that at least one order is possible.
     *
     * This method will save your embarrassment by returning true or false if
     * there is at least one order that can taken of exams.
     *
     * You wrote this method, and in doing so, you represent these 'n' exams as
     * nodes in a graph, numbered from 0 to n-1. And you represent the prerequisite
     * between taking exams as directed edges between two nodes which represent
     * those two exams.
     *
     * Your method expects a 2-dimensional array of exam prerequisites, where
     * prerequisites[i] = [ai, bi] indicating that you must take exam 'bi' first
     * if you want to take exam 'ai'. For example, the pair [0, 1], indicates that
     * to take exam certification '0', you have to first have the certification for
     * exam '1'.
     *
     * The method will return true if you can finish all certification exams.
     * Otherwise, return false (e.g., meaning it is a cyclic or cycle graph).
     *
     *     Example 1:
     *     Input: numExams = 2, prerequisites = [[1,0]]
     *     Output: true
     *     Explanation: There are a total of 2 exams to take.
     *     To take exam 1 you should have completed the
     *     certification of exam 0. So, it is possible (no
     *     cyclic or cycle graph of prereqs).
     *
     *
     *     Example 2:
     *     Input: numExams = 2, prerequisites = [[1,0],[0,1]]
     *     Output: false
     *     Explanation: There are a total of 2 exams to take.
     *     To take exam 1 you should have completed the
     *     certification of exam 0, and to take exams 0 you
     *     should also have completed the certification of exam
     *     1. So, it is impossible (it is a cycle graph).
     *
     * @param numExams          - number of exams, which will produce a graph of n nodes
     * @param prerequisites     - 2-dim array of directed edges.
     * @return boolean          - True if all exams can be taken, else false.
     *
     * Next Steps:
     * The in-degree array will show which exams are ready to be taken (requiring no prerequesites left) and help process the graph in valid order. It will help detect the cycles in the graph
     * The in-degree of a node (which represents the exam) is the # of incoming edges, aka how many prerequisites exams must be taken before that exam
     * By building a in-degree array, the start will be easier to identify because nodes with an in-degree of 0 will have no pre-reqs and can be taken first
     * The array also allows to track the progress by reducing the in-degrees of its neighbors (which represent dependent exams) & when a neighbor's in-degree drops to 0 = all its prereqs are complete and can safely take it next
     * The array can detect cycles (never reaching in-degree 0) because if at the end all the nodes in the array have not been processed it will show that a cycle exists and it will return false (unable to complete all exams)
     * 
     * As per class notes, this method will be conducting a  Depth-First Search (DFS) algorithm to traverse/search through the graph
     * Specifically, topological sorting will be most beneficial. It helps "In scheduling tasks or courses (like prerequisite chains), DFS can help in topological sorting to determine the order of tasks, ensuring that dependencies are respected."
     * This will require processing all nodes/exams that have no prereqs first
     * Once one is taken, reducing the in-degrees of its dependent exams 
     * Then have that dependent exam that's no in-degree of 0 ready to be taken next
     * This process would be easy to achieve using a queue. This is because its FIFO (FIRST-IN FIRST-OUT) property which can ensure that exams are taken in the order they become available.
     *
     * Pseudocode: 
     * Build an adjacency list from the list of prereqs
     * Create & populate an in-degree array which will count the incoming edges for each node
     * Add all nodes with in-degree 0 to a queue 
     * While the queue is not empty, 
     *    Dequeue a node/exam and increment the count of processed nodes
     *    For each of its neighbors, reduce their in-degree
     *    If a neightbor's in-degree becomes 9, add it to the queue
     * After processing, if the number of processed nodes equals numExams, return true
     * Otherwise, return false (a cycle was detected)
     */

    public boolean canFinish(int numExams, 
                             int[][] prerequisites) {
      
        int numNodes = numExams;  // # of nodes in graph

        // Build directed graph's adjacency list
        ArrayList<Integer>[] adj = getAdjList(numExams, 
                                        prerequisites); 
        //Create in-degree array to count incoming edges for each node
        int[] inDegree = new int[numNodes];
        for(int i = 0; i < numNodes; i++){ //Loop over each node to calculate in-degrees
            for(int j = 0; j < adj[i].size(); j++){ //Loop over each neighbor (outgoing edge) of node i
                int neighbor = adj[i].get(j); //Get the destination node
                inDegree[neighbor]++; //Count incoming edge for neighbor 
            }
        }

        //Initialize a queue and add nodes with in-degree 0 (no prerequisites)
        Queue<Integer> queue = new LinkedList<>();
        for(int i = 0; i < numNodes; i++){
            if(inDegree[i] == 0){
                queue.offer(i); //Node can be taken first
            }
        }
        //Process nodes in queue--topological order
        int count = 0; //Tracks how many nodes have been "taken" successfully
        
        while(!queue.isEmpty()) {
            int current = queue.poll(); //Remobe the front node from the queue
            count++;
            
            //Reduce in-degree for neighbors
            for(int j = 0; j < adj[current].size(); j++){
                int neighbor = adj[current].get(j); //Get the neighbor
                inDegree[neighbor]--; //Reduce its in-degree (one prereq is now done)
                if(inDegree[neighbor] == 0){ //If in-degree becomes 0, it's ready to be taken
                    queue.offer(neighbor); //Add it to the queue
                }
            }
        }
        //If all nodes were processed, return true. Otherwise, there is a cycle
        return count == numNodes;
    }

    /**
     * Method getAdjList
     *
     * Building an Adjacency List for the directed graph based on number of nodes
     * and passed in directed edges.
     *
     * @param numNodes      - number of nodes in graph (labeled 0 through n-1) for n nodes
     * @param edges         - 2-dim array of directed edges
     * @return ArrayList<Integer>[]  - An adjacency list representing the provided graph.
     */

    private ArrayList<Integer>[] getAdjList(
            int numNodes, int[][] edges) {

        ArrayList<Integer>[] adj 
                    = new ArrayList[numNodes];      // Create an array of ArrayList ADT

        for (int node = 0; node < numNodes; node++){
            adj[node] = new ArrayList<Integer>();   // Allocate empty ArrayList per node
        }
        for (int[] edge : edges){
            adj[edge[0]].add(edge[1]);              // Add connected node edge [1] for node [0]
        }
        return adj;
    }


    /*
     * Assignment Graphing - Number of groups.
     *
     * There are n people. Some of them are connected
     * as friends forming a group. If person 'a' is
     * connected to person 'b', and person 'b' is
     * connected to person 'c', they form a connected
     * group.
     *
     * Not all groups are interconnected, meaning there
     * can be 1 or more groups depending on how people
     * are connected.
     *
     * This example can be viewed as a graph problem,
     * where people are represented as nodes, and
     * edges between them represent people being
     * connected. In this problem, we are representing
     * this graph externally as an non-directed
     * Adjacency Matrix. And the graph itself may not
     * be fully connected, it can have 1 or more
     * non-connected compoents (subgraphs).
     *
     * Example 1:
     *   Input :
         AdjMatrix = [[0,1,0], [1,0,0], [0,0,0]]
     *   Output: 2
     *   Explanation: The Adjacency Matrix defines an
     *   undirected graph of 3 nodes (indexed 0 to 2).
     *   Where nodes 0 and 1 aee connected, and node 2
     *   is NOT connected. This forms two groups of
     *   nodes.
     *
     * Example 2:
     *   Input : AdjMatrix = [ [0,0,0], [0,0,0], [0,0,0]]
     *   Output: 3
     *   Explanation: The Adjacency Matrix defines an
     *   undirected graph of 3 nodes (indexed 0 to 2).
     *   There are no connected nodes, hence forming
     *   three groups.
     *
     * Example 3:
     *   Input : AdjMatrix = [[0,1,0], [1,0,0], [0,1,0]]
     *   Output: 1
     *   Explanation, The adjacency Matrix defined an
     *   undirected graph of 3 nodes (index 0 to 2).
     *   All three nodes are connected by at least one
     *   edge. So they form on large group.
     *
     * Pseudocode: 
     * Convert the adjacency matric to an adjacency list 
     * Ensure all nodes exist in the list--even if they have no edges
     * Initialize a visited array to keep track of explored nodes
     * Initialize a group counter to 0
     * For each unvisited node,
     *    perform DFS to visit all nodes in its component
     *    increment group counter
     * Return the total number of groups count 
     */

    public int numGroups(int[][] adjMatrix) {
        int numNodes = adjMatrix.length;
        Map<Integer,List<Integer>> graph = new HashMap();
        int i = 0, j =0;

        /*
         * Converting the Graph Adjacency Matrix to
         * an Adjacency List representation. This
         * sample code illustrates a technique to do so.
         */

        for(i = 0; i < numNodes ; i++){
            for(j = 0; j < numNodes; j++){
                if( adjMatrix[i][j] == 1 && i != j ){
                    // Add AdjList for node i if not there
                    graph.putIfAbsent(i, new ArrayList());
                    // Add AdjList for node j if not there
                    graph.putIfAbsent(j, new ArrayList());

                    // Update node i adjList to include node j
                    graph.get(i).add(j);
                    // Update node j adjList to include node i
                    graph.get(j).add(i);
                }
            }
        }

        //Ensure every node has an empty adjacency list if not already added
        for(int node = 0; node < numNodes; node++){
            graph.putIfAbsent(node, new ArrayList<>()); //Create empty list id not present
        }

        //Helper function to perform Depth First Search (DFS) and mark visited nodes
        boolean[] visited = new boolean[numNodes];
        int numGroups = 0; //Counter to track # of groups--connected components
        //Iterate through all nodes
        for(int node = 0; node < numNodes; node++){
            if(!visited[node]){ //If node has not been visited yet
                //perform DFS from this node to mark all connected nodes
                dfs(node, graph, visited);
                numGroups++; //Increment the number of groups--connected components)
            }
        }
        return numGroups;
    }

    /*
     * Pseudocode for the DFS helper metod 
     * It will perform Depth First Search to visit all connected nodes in the same component
     * node - The current node to visit
     * graph - The adjacency list 
     * visited - Array tracking whether each node has been visited
     * Mark the current node as visited
     * For each neighbor of the node, 
     *     If the neightbor hasn't been visited
     *        Recursively call DFS on that neighbor 
     */
    private void dfs(int node, Map<Integer, List<Integer>> graph, boolean[] visited){
        visited[node] = true; //Mark node as visited
        
        //Explore all the neighbors of the node
        for(int neighbor : graph.get(node)){
            if(!visited[neighbor]){
                dfs(neighbor, graph, visited);
            }
        }
    }
}
