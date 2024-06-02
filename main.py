from graph import GraphHandler
from PyQt5.QtWidgets import QApplication, QVBoxLayout, QHBoxLayout, QWidget, QPushButton, QLineEdit, QLabel, QFileDialog
from PyQt5.QtGui import QPixmap
from PyQt5.QtCore import Qt
import sys
#Add in B1

#Add in C4

class MyApp(QWidget):
    def __init__(self):
        super().__init__()
        self.initUI()
        self.random_walk_active = False

    def initUI(self):
        self.setWindowTitle('lab1')
        self.setGeometry(100, 100, 1618, 1000)
        self.center()

        layout = QVBoxLayout()

        # Create input box and button layout
        input_layout = QHBoxLayout()

        load_button = QPushButton('Load File', self)
        load_button.setFixedHeight(50)
        load_button.setFixedWidth(200)
        load_button.clicked.connect(self.load_file)
        input_layout.addWidget(load_button)

        generate_button = QPushButton('Generate', self)
        generate_button.setFixedHeight(50)
        generate_button.setFixedWidth(200)
        generate_button.clicked.connect(self.generate_graph)
        input_layout.addWidget(generate_button)

        layout.addLayout(input_layout)

        # Bridge words input box and button layout
        bridge_layout = QHBoxLayout()

        self.word1_box = QLineEdit(self)
        self.word1_box.setPlaceholderText("Word 1")
        self.word1_box.setFixedHeight(50)
        bridge_layout.addWidget(self.word1_box)

        self.word2_box = QLineEdit(self)
        self.word2_box.setPlaceholderText("Word 2")
        self.word2_box.setFixedHeight(50)
        bridge_layout.addWidget(self.word2_box)

        find_bridge_button = QPushButton('Find Bridge Words', self)
        find_bridge_button.setFixedHeight(50)
        find_bridge_button.setFixedWidth(200)
        find_bridge_button.clicked.connect(self.find_bridge_words)
        bridge_layout.addWidget(find_bridge_button)

        layout.addLayout(bridge_layout)

        self.bridge_result_label = QLabel(self)
        layout.addWidget(self.bridge_result_label)

        # Shortest path input box and button layout
        path_layout = QHBoxLayout()

        self.path_word1_box = QLineEdit(self)
        self.path_word1_box.setPlaceholderText("Word 1")
        self.path_word1_box.setFixedHeight(50)
        path_layout.addWidget(self.path_word1_box)

        self.path_word2_box = QLineEdit(self)
        self.path_word2_box.setPlaceholderText("Word 2")
        self.path_word2_box.setFixedHeight(50)
        path_layout.addWidget(self.path_word2_box)

        find_path_button = QPushButton('Find Shortest Path', self)
        find_path_button.setFixedHeight(50)
        find_path_button.setFixedWidth(200)
        find_path_button.clicked.connect(self.find_shortest_path)
        path_layout.addWidget(find_path_button)

        layout.addLayout(path_layout)

        self.path_result_label = QLabel(self)
        layout.addWidget(self.path_result_label)

        # Text generation layout
        generate_text_layout = QHBoxLayout()

        self.new_text_box = QLineEdit(self)
        self.new_text_box.setPlaceholderText("Enter new text (e.g., Seek to explore new and exciting synergies)")
        self.new_text_box.setFixedHeight(50)
        generate_text_layout.addWidget(self.new_text_box)

        generate_text_button = QPushButton('Generate New Text', self)
        generate_text_button.setFixedHeight(50)
        generate_text_button.setFixedWidth(200)
        generate_text_button.clicked.connect(self.generate_new_text)
        generate_text_layout.addWidget(generate_text_button)

        layout.addLayout(generate_text_layout)

        self.new_text_result_label = QLabel(self)
        layout.addWidget(self.new_text_result_label)

        # Random walk layout
        random_walk_layout = QHBoxLayout()

        self.start_node_box = QLineEdit(self)
        self.start_node_box.setPlaceholderText("Start Node")
        self.start_node_box.setFixedHeight(50)
        random_walk_layout.addWidget(self.start_node_box)

        random_walk_button = QPushButton('Start Random Walk', self)
        random_walk_button.setFixedHeight(50)
        random_walk_button.setFixedWidth(200)
        random_walk_button.clicked.connect(self.start_random_walk)
        random_walk_layout.addWidget(random_walk_button)

        stop_walk_button = QPushButton('Stop Walk', self)
        stop_walk_button.setFixedHeight(50)
        stop_walk_button.setFixedWidth(200)
        stop_walk_button.clicked.connect(self.stop_walk)
        random_walk_layout.addWidget(stop_walk_button)

        layout.addLayout(random_walk_layout)

        self.random_walk_result_label = QLabel(self)
        layout.addWidget(self.random_walk_result_label)

        # Image display
        self.image_label = QLabel(self)
        layout.addWidget(self.image_label)

        self.setLayout(layout)

        self.graph_handler = GraphHandler()
        self.random_walk_active = False

    def center(self):
        qr = self.frameGeometry()
        cp = QApplication.primaryScreen().availableGeometry().center()
        qr.moveCenter(cp)
        self.move(qr.topLeft())

    def load_file(self):
        options = QFileDialog.Options()
        options |= QFileDialog.ReadOnly
        file_name, _ = QFileDialog.getOpenFileName(self, "Open Text File", "", "Text Files (*.txt);;All Files (*)", options=options)
        if file_name:
            with open(file_name, 'r') as file:
                self.input_text = file.read().replace('\n', ' ')
    
    def generate_graph(self):
        '''展示图'''
        if hasattr(self, 'input_text'):
            self.graph_handler.create_graph(self.input_text)
            self.graph_handler.show_graph()

            pixmap = QPixmap('image/graph.png')
            self.image_label.setPixmap(pixmap)
            self.image_label.setScaledContents(True)
    
    def find_bridge_words(self):
        '''查询桥接词'''
        word1 = self.word1_box.text()
        word2 = self.word2_box.text()
        result = self.graph_handler.find_bridge_words(word1, word2)
        self.bridge_result_label.setText(result)

    def generate_new_text(self):
        '''Generate new text with bridge words'''
        new_text = self.new_text_box.text()
        result, _ = self.graph_handler.generate_new_text(new_text)
        self.new_text_result_label.setText(result)
        self.new_text_result_label.setAlignment(Qt.AlignTop)
        self.new_text_result_label.setWordWrap(True)
        self.new_text_result_label.setTextFormat(Qt.RichText)

    def find_shortest_path(self):
        '''Find shortest path'''
        word1 = self.path_word1_box.text()
        word2 = self.path_word2_box.text()
        result, path, edges = self.graph_handler.shortest_path(word1, word2)
        self.path_result_label.setText(result)

        if path and edges:
            self.graph_handler.show_graph(highlight_nodes=path, highlight_edges=edges)

            pixmap = QPixmap('image/graph.png')
            self.image_label.setPixmap(pixmap)
            self.image_label.setScaledContents(True)

    def start_random_walk(self):
        '''Start random walk'''
        start_node = self.start_node_box.text()
        self.random_walk_active = True
        result, nodes, edges = self.graph_handler.random_walk(start_node)
        self.random_walk_result_label.setText(result)
        self.graph_handler.show_graph(highlight_nodes=nodes, highlight_edges=edges)

        with open('./text/random_walk.txt', 'w') as file:
            file.write(result)

        pixmap = QPixmap('image/graph.png')
        self.image_label.setPixmap(pixmap)
        self.image_label.setScaledContents(True)

    def stop_walk(self):
        '''Stop random walk'''
        if self.random_walk_active:
            self.graph_handler.stop_walk()
            self.random_walk_active = False
            self.random_walk_result_label.setText("Random walk stopped.")

if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = MyApp()
    ex.show()
    sys.exit(app.exec_())
