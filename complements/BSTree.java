package complements;

import exceptions.*;

public class BSTree<E extends Comparable<E>> implements BinarySearchTree<E> {

    public class Node {
        public E data;
        public Node left;
        public Node right;

        public Node(E data) {
            this(data, null, null);
        }

        public Node(E data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    public Node root; // Atributo root de tipo Node

    public BSTree() {
        this.root = null;
    }

    // Inserta un elemento en el árbol
    @Override
    public void insert(E data) throws ItemDuplicated {
        this.root = insertRec(this.root, data);
    }

    private Node insertRec(Node actual, E data) throws ItemDuplicated {
        if (actual == null) {
            return new Node(data);
        }

        int comparacion = data.compareTo(actual.data);

        if (comparacion < 0) {
            actual.left = insertRec(actual.left, data);
        } else if (comparacion > 0) {
            actual.right = insertRec(actual.right, data);
        } else {
            throw new ItemDuplicated("El elemento ya existe en el árbol");
        }
        return actual;
    }

    // Busca un elemento en el árbol
    @Override
    public E search(E data) throws ItemNotFound {
        Node result = searchRec(this.root, data);
        if (result == null) {
            throw new ItemNotFound("El elemento no se encuentra en el árbol");
        }
        return result.data;
    }

    private Node searchRec(Node actual, E data) {
        if (actual == null) {
            return null;
        }

        int comparacion = data.compareTo(actual.data);

        if (comparacion == 0) {
            return actual;
        } else if (comparacion < 0) {
            return searchRec(actual.left, data);
        } else {
            return searchRec(actual.right, data);
        }
    }

    // Calcula la altura del árbol
    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) {
            return -1; // Height of an empty tree is -1
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // Borra el elemento de un nodo
    @Override
    public void delete(E data) throws ExceptionIsEmpty {
        if (isEmpty()) {
            throw new ExceptionIsEmpty("El árbol está vacío");
        }

        this.root = deleteRec(this.root, data);
    }

    private Node deleteRec(Node actual, E data) {
        if (actual == null) {
            return null;
        }

        int comparacion = data.compareTo(actual.data);

        if (comparacion < 0) {
            actual.left = deleteRec(actual.left, data);
        } else if (comparacion > 0) {
            actual.right = deleteRec(actual.right, data);
        } else {
            // Caso 1: Nodo hoja (sin hijos)
            if (actual.left == null && actual.right == null) {
                return null;
            }

            // Caso 2: Nodo con un solo hijo
            if (actual.left == null) {
                return actual.right;
            }
            if (actual.right == null) {
                return actual.left;
            }

            // Caso 3: Nodo con dos hijos
            // Encontrar el sucesor (el menor elemento en el subárbol derecho)
            Node minNode = findMinNode(actual.right);
            actual.data = minNode.data;

            // Eliminar el sucesor
            actual.right = deleteRec(actual.right, actual.data);
        }
        return actual;
    }

    // Verifica si el árbol está vacío
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    // Método ToString para mostrar el contenido del árbol
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(root, sb, 0);
        return sb.toString();
    }

    private void toString(Node node, StringBuilder sb, int nivel) {
        if (node != null) {
            // Visitar subárbol derecho
            toString(node.right, sb, nivel + 1);

            // Visitar nodo actual
            for (int i = 0; i < nivel; i++) {
                sb.append("    ");
            }
            sb.append(node.data).append("\n");

            // Visitar subárbol izquierdo
            toString(node.left, sb, nivel + 1);
        }
    }

    // RECORRIDO IN ORDEN
    public String inOrder() {
        StringBuilder sb = new StringBuilder();
        inOrder(root, sb);
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node != null) {
            // Visitar subárbol izquierdo
            inOrder(node.left, sb);

            // Visitar nodo actual
            sb.append(node.data).append(" ");

            // Visitar subárbol derecho
            inOrder(node.right, sb);
        }
    }

    // RECORRIDO EN PRE ORDEN
    public String preOrder() {
        StringBuilder sb = new StringBuilder();
        preOrder(root, sb);
        return sb.toString();
    }

    private void preOrder(Node node, StringBuilder sb) {
        if (node != null) {
            // Visitar nodo actual
            sb.append(node.data).append(" ");

            // Visitar subárbol izquierdo
            preOrder(node.left, sb);

            // Visitar subárbol derecho
            preOrder(node.right, sb);
        }
    }

    // RECORRIDO EN POST ORDEN
    public String postOrder() {
        StringBuilder sb = new StringBuilder();
        postOrder(root, sb);
        return sb.toString();
    }

    private void postOrder(Node node, StringBuilder sb) {
        if (node != null) {
            // Visitar subárbol izquierdo
            postOrder(node.left, sb);

            // Visitar subárbol derecho
            postOrder(node.right, sb);

            // Visitar nodo actual
            sb.append(node.data).append(" ");
        }
    }

    // Ecuentra el menor valor del árbol
    public Node findMinNode(Node node) {
        if (node == null) {
            return null;
        }

        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Método público que devuelve el valor mínimo
    public E findMin() throws ItemNotFound {
        if (isEmpty()) {
            throw new ItemNotFound("El árbol está vacío");
        }

        Node minNode = findMinNode(root);
        return minNode.data;
    }

    // Encuentra el mayor valor del árbol
    private E findMaxNode(Node node) throws ItemNotFound {
        if (node == null) {
            throw new ItemNotFound("El subárbol está vacío");
        }

        // Descender al nodo más a la derecha
        while (node.right != null) {
            node = node.right;
        }
        return search(node.data);
    }

    public E findMax() throws ItemNotFound {
        if (isEmpty()) {
            throw new ItemNotFound("El árbol está vacío");
        }
        return findMaxNode(root);
    }

    // Destruye todos los nodos
    public void destroyNodes() throws ExceptionIsEmpty {
        if (isEmpty()) {
            throw new ExceptionIsEmpty("El árbol ya está vacío");
        }

        // Establece la raíz como nula, lo que elimina todas las referencias
        this.root = null;
    }

    
}
