/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BPlusTree;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author LUANNA
 */
public class BPlusTree {
    private Node root;
    private List<Node> leafNodeList;
    private int min,max;

    public BPlusTree(int n) {
        this.max = n;
        this.min = n % 2 == 0 ? (int) n / 2 : (int) (n + 1) / 2;
        this.root = new LeafNode();
        this.leafNodeList = new LinkedList<Node>();
        this.leafNodeList.add(this.root);
    }
    
    public void insert(int key, Object value) {
        Node node = choiceLeaf(key, this.getRoot());
        
        installNode(key, value, (LeafNode) node);
        
        if(((LeafNode) node).getIndexes().size() > max) {
            if(node instanceof LeafNode) {
                node = copyUp((LeafNode) node);
                if(node == getRoot()){
                    this.setRoot(node);
                }
            }
        }
    }
    
    private LeafNode choiceLeaf(int key, Node node) {
        if(node instanceof LeafNode) {
            return (LeafNode) node;
        }
        
        List<Integer> keys = ((IndexNode) node).getKeys();
        List<Node> entries = ((IndexNode) node).getEntries();
        
        int pos = 0;
        
        for (int i = 0; i < keys.size(); i++) {
            if(key >= keys.get(i)) {
                pos = i + 1;
            }
        }
        
        return choiceLeaf(key, entries.get(pos));
    }

    private NodeEntry installNode(int key, Object value, LeafNode node) {
        NodeEntry index = new NodeEntry(key, value);
        
        List<NodeEntry> indexes = node.getIndexes();
            
        for (int i = 0; i < indexes.size(); i++) {
            if(key < indexes.get(i).getKey()) {
                indexes.add(i, new NodeEntry(key, value));
                return index;
            }
        }
        
        indexes.add(new NodeEntry(key, value));
        return index;
    }
    
    private IndexNode copyUp(LeafNode node){
        IndexNode parent = new IndexNode();
        
        LeafNode node2 = new LeafNode();
        node2.getIndexes().addAll(node.getIndexes().subList(min + 1, max + 1));
        
        int key = node.getIndexes().get(min).getKey();
        leafNodeList.add(node2);
        
        for (int i = max - 1; i >= min; i--) {
            node.getIndexes().remove(i);
        }
        
        node.setParent(parent);
        node2.setParent(parent);
        
        parent.getEntries().add(node);
        parent.getEntries().add(node2);
        parent.getKeys().add(key);
        
        return parent;
    }
    
    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public List<Node> getLeafNodeList() {
        return leafNodeList;
    }

    public void setLeafNodeList(List<Node> leafNodeList) {
        this.leafNodeList = leafNodeList;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}

class IndexNode extends Node{
    private List<Integer> keys;
    private List<Node> entries;

    public IndexNode() {
        this.keys = new LinkedList<>();
        this.entries = new LinkedList<Node>();
    }
    
    public List<Integer> getKeys() {
        return keys;
    }

    public void setKeys(List<Integer> keys) {
        this.keys = keys;
    }

    public List<Node> getEntries() {
        return entries;
    }

    public void setEntries(List<Node> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "IndexNode{" + "keys=" + keys + ", entries=" + entries + '}';
    }
    
}

class LeafNode extends Node{
    private List<NodeEntry> indexes;

    public LeafNode() {
        this.indexes = new LinkedList<NodeEntry>();
    }
    
    public List<NodeEntry> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<NodeEntry> indexes) {
        this.indexes = indexes;
    }

    @Override
    public String toString() {
        return "LeafNode{" + "indexes=" + indexes + '}';
    }
    
}

abstract class Node {
    private Node parent;

    public Node() {
    }

    public Node(Node parent) {
        this.parent = parent;
    }
    
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}

class NodeEntry {
    private int key;
    private Object value;

    public NodeEntry(int key, Object value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" + key + " , " + value + '}';
    }
}