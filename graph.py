import random
import networkx as nx
import matplotlib.pyplot as plt
import re

# Add in R4
# Add in R5
# Add in B2
class GraphHandler:
    def __init__(self):
        self.G = None

    def create_graph(self, input_text):
        self.G = nx.DiGraph()
        words = re.findall(r'\b[a-zA-Z]+\b', input_text.lower())

        for i, word in enumerate(words):
            self.G.add_node(word)

        for i in range(len(words) - 1):
            if self.G.has_edge(words[i], words[i + 1]):
                self.G[words[i]][words[i + 1]]['weight'] += 1
            else:
                self.G.add_edge(words[i], words[i + 1], weight=1)

    def show_graph(self, highlight_nodes=None, highlight_edges=None):
        if highlight_nodes is None:
            highlight_nodes = []
        if highlight_edges is None:
            highlight_edges = []

        plt.figure(figsize=(16, 10))
        pos = nx.spring_layout(self.G)
        weights = [self.G[u][v]['weight'] for u, v in self.G.edges()]

        node_colors = ['red' if node in highlight_nodes else 'skyblue' for node in self.G.nodes()]
        edge_colors = ['red' if edge in highlight_edges else 'black' for edge in self.G.edges()]

        nx.draw(self.G, pos, with_labels=True, arrows=True, node_size=1800, node_color=node_colors, font_size=18, font_weight='bold', width=weights, edge_color=edge_colors)
        edge_labels = {(u, v): d['weight'] for u, v, d in self.G.edges(data=True)}
        nx.draw_networkx_edge_labels(self.G, pos, edge_labels=edge_labels, font_size=18)
        plt.savefig('image/graph.png')
        plt.close()

    def find_bridge_words(self, word1, word2):
        if word1 not in self.G and word2 not in self.G:
            return f"No \"{word1}\" and \"{word2}\" in the graph!"
        elif word1 not in self.G:
            return f"No \"{word1}\" in the graph!"
        elif word2 not in self.G:
            return f"No \"{word2}\" in the graph!"
        
        bridge_words = [n for n in self.G.successors(word1) if word2 in self.G.successors(n)]
        if not bridge_words:
            return "No bridge words from {} to {}!".format(word1, word2)
        
        return "The bridge words from {} to {} are: {}".format(word1, word2, ', '.join(bridge_words))

    def generate_new_text(self, new_text):
        words = re.findall(r'\b[a-zA-Z]+\b', new_text.lower())
        new_words = []
        bridge_words_list = []

        for i in range(len(words) - 1):
            word1 = words[i]
            word2 = words[i + 1]
            new_words.append(word1)

            if word1 in self.G and word2 in self.G:
                bridge_words = [n for n in self.G.successors(word1) if word2 in self.G.successors(n)]
                if bridge_words:
                    bridge_word = random.choice(bridge_words)
                    new_words.append(f'<span style="color:red">{bridge_word}</span>')
                    bridge_words_list.append(bridge_word)

        new_words.append(words[-1])
        return ' '.join(new_words), bridge_words_list

    def shortest_path(self, word1, word2):
        if word1 not in self.G:
            return f"No {word1} in the graph!", None, None
        if word2 not in self.G:
            return f"No {word2} in the graph!", None, None

        try:
            path = nx.shortest_path(self.G, source=word1, target=word2, weight='weight')
            length = nx.shortest_path_length(self.G, source=word1, target=word2, weight='weight')
            edges = [(path[i], path[i+1]) for i in range(len(path)-1)]
            return f"The shortest path from {word1} to {word2} is: {' -> '.join(path)} with total weight {length}", path, edges
        except nx.NetworkXNoPath:
            return f"No path from {word1} to {word2}!", None, None

    def random_walk(self, start_node):
        if self.G is None or len(self.G.nodes) == 0:
            return "The graph is empty!", [], []

        if start_node not in self.G:
            return f"No {start_node} in the graph!", [], []

        self.walk_active = True
        current_node = start_node
        visited_nodes = [current_node]
        visited_edges = []

        while self.walk_active:
            successors = list(self.G.successors(current_node))
            if not successors:
                break
            next_node = random.choice(successors)
            edge = (current_node, next_node)
            if edge in visited_edges:
                break
            visited_nodes.append(next_node)
            visited_edges.append(edge)
            current_node = next_node

        self.walk_active = False
        return f"Random walk starting from {start_node}: {' -> '.join(visited_nodes)}", visited_nodes, visited_edges

    def stop_walk(self):
        self.walk_active = False
