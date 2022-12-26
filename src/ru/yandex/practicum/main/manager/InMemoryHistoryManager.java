package manager;

import models.Node;
import models.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> first;
    private Node<Task> last;

    protected final HashMap<Integer, Node> nodeHashMap = new HashMap<>();

    public InMemoryHistoryManager() {
    }


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (nodeHashMap.containsKey(task.getId())) {
            Node temp = nodeHashMap.get(task.getId());
            removeNode(temp);
            linkLast(task);
        } else {
            if (first == null) {
                Node<Task> newNode = new Node<>(null, task, null);
                first = newNode;
                last = newNode;
            } else {
                linkLast(task);
            }

            if (getTasks().size() > 10) {
                nodeHashMap.remove(first.getTask().getId());
                first.getNext();
            }
        }
        nodeHashMap.put(task.getId(), last);
    }

    private List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        Node node = first;
        while (node != null) {
            taskList.add((Task) node.getTask());
            node = node.getNext();
        }
        return taskList;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (!nodeHashMap.containsKey(id)) {
            throw new RuntimeException("История с введенным id не найдена.");
        }
        removeNode(nodeHashMap.get(id));
        nodeHashMap.remove(id);
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.getNext() == null && node.getPrev() == null) {
            first = null;
        } else if (node.getPrev() == null && node.getNext() != null) {
            first = node.getNext();
        } else if (node.getNext() == null && node.getPrev() != null) {
            removeLastNode();
        } else {
            node.getPrev()
                    .setNext(node.getNext());
        }
    }

    private void removeLastNode() {
        Node next = first;
        Node prev = first;
        while (next.getNext()!=null){
            prev = next;
            next = next.getNext();
        }
        prev.setNext(null);
    }

    private void linkLast(Task task) {
        Node<Task> oldLast = last;
        Node<Task> newNode = new Node<>(last, task, null);
        last = newNode;
        oldLast.setNext(newNode);
    }
}
