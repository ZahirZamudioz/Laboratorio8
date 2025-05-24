package avltree;

import java.util.LinkedList;
import java.util.Queue;
import complements.*;
import exceptions.*;

public class AVLTree<E extends Comparable<E>> extends BSTree<E> {

    class NodeAVL extends Node {
        protected int bf; // Factor de balanceo: altura(derecho) - altura(izquierdo)

        public NodeAVL(E data) {
            super(data);
            this.bf = 0;
        }

        @Override
        public String toString() {
            return data + " (bf=" + bf + ")";
        }
    }

    private boolean heightChanged; // Indica si la altura del subárbol cambió

    @Override
    public void insert(E x) throws ItemDuplicated {
        this.heightChanged = false;
        this.root = insert(x, (NodeAVL) this.root);
    }

    protected Node insert(E x, NodeAVL node) throws ItemDuplicated {
        NodeAVL result = node;
        
        if (node == null) {
            // Caso base: crear nuevo nodo
            this.heightChanged = true;
            result = new NodeAVL(x);
        } else {
            int comparison = x.compareTo(node.data);
            
            if (comparison == 0) {
                throw new ItemDuplicated(x + " ya se encuentra en el árbol");
            }
            
            if (comparison < 0) {
                // Insertar en subárbol izquierdo
                result.left = insert(x, (NodeAVL) node.left);
                
                if (this.heightChanged) {
                    switch (result.bf) {
                        case 1:  // Era más alto a la derecha, ahora balanceado
                            result.bf = 0;
                            this.heightChanged = false;
                            break;
                        case 0:  // Era balanceado, ahora más alto a la izquierda
                            result.bf = -1;
                            this.heightChanged = true;
                            break;
                        case -1: // Era más alto a la izquierda, ahora desbalanceado
                            result = balanceToRight(result);
                            this.heightChanged = false;
                            break;
                    }
                }
            } else {
                // Insertar en subárbol derecho
                result.right = insert(x, (NodeAVL) node.right);
                
                if (this.heightChanged) {
                    switch (result.bf) {
                        case -1: // Era más alto a la izquierda, ahora balanceado
                            result.bf = 0;
                            this.heightChanged = false;
                            break;
                        case 0:  // Era balanceado, ahora más alto a la derecha
                            result.bf = 1;
                            this.heightChanged = true;
                            break;
                        case 1:  // Era más alto a la derecha, ahora desbalanceado
                            result = balanceToLeft(result);
                            this.heightChanged = false;
                            break;
                    }
                }
            }
        }
        return result;
    }

    
    // Balancea cuando el subárbol derecho es más pesado (bf = 2)
    private NodeAVL balanceToLeft(NodeAVL node) {
        NodeAVL rightChild = (NodeAVL) node.right;
        
        if (rightChild.bf >= 0) {
            // Caso LL: Rotación simple a la izquierda
            node.bf = 0;
            rightChild.bf = 0;
            node = rotateLeft(node);
        } else {
            // Caso LR: Rotación doble (derecha-izquierda)
            NodeAVL rightLeftChild = (NodeAVL) rightChild.left;
            
            // Actualizar factores de balanceo según el factor del nieto
            switch (rightLeftChild.bf) {
                case -1:
                    node.bf = 0;
                    rightChild.bf = 1;
                    break;
                case 0:
                    node.bf = 0;
                    rightChild.bf = 0;
                    break;
                case 1:
                    node.bf = -1;
                    rightChild.bf = 0;
                    break;
            }
            rightLeftChild.bf = 0;
            
            // Realizar rotaciones
            node.right = rotateRight(rightChild);
            node = rotateLeft(node);
        }
        return node;
    }

    
    // Balancea cuando el subárbol izquierdo es más pesado (bf = -2)
    private NodeAVL balanceToRight(NodeAVL node) {
        NodeAVL leftChild = (NodeAVL) node.left;
        
        if (leftChild.bf <= 0) {
            // Caso RR: Rotación simple a la derecha
            node.bf = 0;
            leftChild.bf = 0;
            node = rotateRight(node);
        } else {
            // Caso RL: Rotación doble (izquierda-derecha)
            NodeAVL leftRightChild = (NodeAVL) leftChild.right;
            
            // Actualizar factores de balanceo según el factor del nieto
            switch (leftRightChild.bf) {
                case 1:
                    node.bf = 0;
                    leftChild.bf = -1;
                    break;
                case 0:
                    node.bf = 0;
                    leftChild.bf = 0;
                    break;
                case -1:
                    node.bf = 1;
                    leftChild.bf = 0;
                    break;
            }
            leftRightChild.bf = 0;
            
            // Realizar rotaciones
            node.left = rotateLeft(leftChild);
            node = rotateRight(node);
        }
        return node;
    }

    // Rotación simple a la izquierda
    private NodeAVL rotateLeft(NodeAVL node) {
        NodeAVL newRoot = (NodeAVL) node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        updateBalanceFactor(node);
        updateBalanceFactor(newRoot);
        return newRoot;
    }

    // Rotacion simple a la derecha
    private NodeAVL rotateRight(NodeAVL node) {
        NodeAVL newRoot = (NodeAVL) node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        updateBalanceFactor(node);
        updateBalanceFactor(newRoot);
        return newRoot;
    }

    // Eliminación de elementos del árbol
    @Override
    public void delete(E x) throws ExceptionIsEmpty {
        if (isEmpty()) {
            throw new ExceptionIsEmpty("The tree is empty");
        }
        this.heightChanged = false;
        this.root = delete(x, (NodeAVL) this.root);
    }

    protected Node delete(E x, NodeAVL node) throws ExceptionIsEmpty {
        if (node == null) {
            return null;
        }

        int comparison = x.compareTo(node.data);

        if (comparison < 0) {
            node.left = delete(x, (NodeAVL) node.left);
            if (this.heightChanged) {
                node = balanceAfterDelete(node);
            }
        } else if (comparison > 0) {
            node.right = delete(x, (NodeAVL) node.right);
            if (this.heightChanged) {
                node = balanceAfterDelete(node);
            }
        } else {
            // Case 1: Leaf node
            if (node.left == null && node.right == null) {
                this.heightChanged = true;
                return null;
            }
            // Case 2: Node with one child
            if (node.left == null) {
                this.heightChanged = true;
                return node.right;
            }
            if (node.right == null) {
                this.heightChanged = true;
                return node.left;
            }
            // Case 3: Node with two children
            NodeAVL minNode = (NodeAVL) findMinNode(node.right);
            node.data = minNode.data;
            node.right = delete(node.data, (NodeAVL) node.right);
            if (this.heightChanged) {
                node = balanceAfterDelete(node);
            }
        }
        return node;
    }

    private NodeAVL balanceAfterDelete(NodeAVL node) {
        updateBalanceFactor(node);
        if (node.bf == 2) {
            return balanceToLeft(node);
        } else if (node.bf == -2) {
            return balanceToRight(node);
        }
        return node;
    }

    private void updateBalanceFactor(NodeAVL node) {
        int leftHeight = height((NodeAVL) node.left);
        int rightHeight = height((NodeAVL) node.right);
        node.bf = rightHeight - leftHeight;
        this.heightChanged = Math.abs(node.bf) <= 1;
    }

    // Calcular la altura del árbol
    private int height(NodeAVL node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height((NodeAVL) node.left), height((NodeAVL) node.right));
    }

    // Recorrido BFS
    public String breadthFirst() {
        StringBuilder sb = new StringBuilder();
        Queue<Node> queue = new LinkedList<>();
        if (root != null) {
            queue.offer(root);
        }
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                sb.append(current.data).append(" ");
                
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            sb.append("| "); // Separador por niveles
        }
        return sb.toString();
    }

    // Recorrido en Pre-orden
    @Override
    public String preOrder() {
        StringBuilder sb = new StringBuilder();
        preOrder((NodeAVL) root, sb);
        return sb.toString();
    }

    private void preOrder(NodeAVL node, StringBuilder sb) {
        if (node != null) {
            sb.append(node.data).append(" (bf=").append(node.bf).append(") ");
            preOrder((NodeAVL) node.left, sb);
            preOrder((NodeAVL) node.right, sb);
        }
    }
}

