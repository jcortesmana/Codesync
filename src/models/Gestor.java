package models;
import java.util.ArrayList;
import java.util.List;
// Clase genérica funcional para manejar listas de cualquier tipo de objeto
public class Gestor<T> {
    private List<T> elementos;

    public Gestor() {
        this.elementos = new ArrayList<>();
    }

    // Agregar un elemento
    public void agregar(T elemento) {
        elementos.add(elemento);
    }

    // Eliminar un elemento
    public void eliminar(T elemento) {
        elementos.remove(elemento);
    }

    // Obtener todos los elementos
    public List<T> getTodos() {
        return elementos;
    }

    // Buscar un elemento por índice
    public T getPorIndice(int index) {
        if (index >= 0 && index < elementos.size()) {
            return elementos.get(index);
        }
        return null;
    }

    // Verificar si un elemento está en la lista
    public boolean contiene(T elemento) {
        return elementos.contains(elemento);
    }

    // Mostrar todos los elementos en consola
    public void mostrarTodos() {
        for (T elemento : elementos) {
            System.out.println(elemento);
        }
    }

    @Override
    public String toString() {
        return "Gestor{" +
                "elementos=" + elementos +
                '}';
    }
}