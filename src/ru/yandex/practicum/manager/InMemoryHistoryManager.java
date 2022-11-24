package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.Node;
import ru.yandex.practicum.models.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node first = null;
    private Node last = null;

    protected final HashMap<Integer, Node> nodeHashMap = new HashMap<>();

    public InMemoryHistoryManager() {
    }


    @Override
    public void add(Task task) {
        if (task == null){
            return;
        }

        if (nodeHashMap.containsKey(task.getId())) {
            Node temp = nodeHashMap.get(task.getId());
            removeNode(temp);
            Node node = linkLast(task);
            nodeHashMap.put(task.getId(), node);
        }
        Node node = first;
        if (node == null) {
            first = new Node(task, null, null);
            node = first;
        } else {
            node = linkLast(task);
        }
        if (getTasks().size() > 10) {
            nodeHashMap.remove(first.getTask().getId());
            first = first.getNext();
        }
        nodeHashMap.put(task.getId(), node);
    }

    private List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        Node node = first;
        while (node != null) {
            taskList.add(node.getTask());
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
        removeNode(nodeHashMap.get(id));
        nodeHashMap.remove(id);
    }

    private void removeNode(Node node) {
        if (node == null){
            return;
        }
        if (node.getNext() == null && node.getPrev() == null) {
            first = null;
        } else if (node.getPrev() == null && node.getNext() != null) {
            first = node.getNext();
        } else if (node.getNext() == null && node.getPrev() != null) {
            last = node.getPrev();
        } else {
            node.getPrev()
                    .setNext(node.getNext());
        }
    }

    public Node linkLast(Task task) {
        Node node = first;
        while (node.getNext() != null) {
            node = node.getNext();
        }
        node.setNext(new Node(task, null, node));
        return node.getNext();
    }
}
