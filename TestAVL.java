
import avltree.AVLTree;
import complements.BSTree;

public class TestAVL {

    public static void main(String[] args) {

        try {
            AVLTree<Integer> avl0 = new AVLTree<>();

            System.out.println("Actividad 1: Pruebas de inserciones\n");

            // Prueba 1: Inserción básica
            System.out.println("1. Inserción básica:");
            avl0.insert(10);
            avl0.insert(5);
            avl0.insert(15);
            System.out.println("Insertados: 10, 5, 15");
            System.out.println("Raíz: " + avl0.root);

            // Prueba 2: Rotación LL
            System.out.println("\n2. Rotación LL:");
            avl0.insert(20);
            avl0.insert(30);
            System.out.println("Insertados: 20, 30");
            System.out.println("Raíz: " + avl0.root);

            // Prueba 3: Duplicado
            System.out.println("\n3. Duplicado:");

            avl0.insert(10);
            System.out.println("ERROR: No detectó duplicado");

            // Ejercicios 1,2,3,4,5
            // Diferencia entre BST y AVL
            System.out.println("Diferencia de altura en BST y AVL");
            BSTree<Integer> bst = new BSTree<>();
            AVLTree<Integer> avl = new AVLTree<>();

            // Inserción de elementos de prueba
            Integer[] values = { 1, 2, 3, 4, 5 };
            for (Integer value : values) {
                bst.insert(value);
                avl.insert(value);
            }

            System.out.println("BST: " + bst.inOrder());
            System.out.println("AVL: " + avl.inOrder());
            System.out.println("BST Height: " + bst.height());
            System.out.println("AVL Height: " + avl.height());

            // Eliminación de elementos con rotaciones
            System.out.println("\nEliminación con rotaciones");
            avl = new AVLTree<>();
            // Create a tree that will require rotations after deletion
            Integer[] rotationValues = { 50, 30, 70, 20, 40, 60, 80 };
            for (Integer value : rotationValues) {
                avl.insert(value);
            }

            System.out.println("AVL inicial: " + avl.preOrder());
            System.out.println("Borrando el 70...");
            avl.delete(70);
            System.out.println("AVL despues de la eliminación: " + avl.preOrder());

            // Recorrido en amplitud (BFS)
            System.out.println("\nRecorrido en amplitud BSF");
            System.out.println("BFS Traversal: " + avl.breadthFirst());

            // Recorrido Pre-orden AVL
            System.out.println("\nRecorrido Pre-orden");
            System.out.println("Recorrido preorden: " + avl.preOrder());

            // Pruebas de rotaciones
            System.out.println("\nRotaciones");
            avl = new AVLTree<>();

            System.out.println("Rotacion simple a la izquierda:");
            avl.insert(30);
            avl.insert(20);
            avl.insert(10);
            System.out.println("Después de la rotación: " + avl.preOrder());

            avl = new AVLTree<>();
            System.out.println("\nRotacion simple a la derecha:");
            avl.insert(10);
            avl.insert(20);
            avl.insert(30);
            System.out.println("Después de la rotación: " + avl.preOrder());

            avl = new AVLTree<>();
            System.out.println("\nRotacion doble derecha:");
            avl.insert(30);
            avl.insert(10);
            avl.insert(20);
            System.out.println("Después de la rotación: " + avl.preOrder());

            avl = new AVLTree<>();
            System.out.println("\nRotación doble izquierda:");
            avl.insert(10);
            avl.insert(30);
            avl.insert(20);
            System.out.println("Después de la rotación: " + avl.preOrder());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}