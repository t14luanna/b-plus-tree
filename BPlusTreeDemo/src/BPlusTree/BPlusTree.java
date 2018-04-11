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
        
        nodeSplit(node);
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
    
    private void installNode(int key, Node entry, IndexNode node) {
        int pos = node.getKeys().size();
        
        for (int i = 0; i < node.getKeys().size(); i++) {
            if(key < node.getKeys().get(i)) {
                pos = i;
                break;
            }
        }
        node.getKeys().add(pos, key);
        ((IndexNode) entry).getEntries().get(1).setParent(node);
        node.getEntries().add(pos + 1, ((IndexNode) entry).getEntries().get(1));
    }
    
    private void nodeSplit(Node node){
        if(!checkNodeOverflow(node)){
            return;
        }
        
        Node parent = node.getParent();
        
        Node temp = null;
        
        int key = 0;
        
        if(node instanceof LeafNode){
            temp = copyUp((LeafNode) node);
            key = ((IndexNode) temp).getKeys().get(0);
        } else {
            temp = pushUp((IndexNode) node);
            key = ((IndexNode) temp).getKeys().get(0);
        }
        
        if(parent == null){
            this.root = temp;
        } else {
            installNode(key, temp, (IndexNode) parent);
            nodeSplit(parent);
        }
    }
    
    private boolean checkNodeOverflow(Node node){
        if(node instanceof LeafNode && ((LeafNode) node).getIndexes().size() > max){
            return true;
        } else if(node instanceof IndexNode && ((IndexNode) node).getKeys().size() > max) {
            return true;
        }
        
        return false;
    }
    
    private IndexNode copyUp(LeafNode node){
        IndexNode parent = new IndexNode();
        
        LeafNode node2 = new LeafNode();
        node2.getIndexes().addAll(node.getIndexes().subList(min, max + 1));
        
        int key = node.getIndexes().get(min).getKey();
        leafNodeList.add(node2);
        
        for (int i = max; i >= min; i--) {
            node.getIndexes().remove(i);
        }
        
        node.setParent(parent);
        node2.setParent(parent);
        
        parent.getEntries().add(node);
        parent.getEntries().add(node2);
        parent.getKeys().add(key);
        
        return parent;
    }
    
    private IndexNode pushUp(IndexNode node){
        IndexNode parent = new IndexNode();
        
        IndexNode node2 = new IndexNode();
        node2.getKeys().addAll(node.getKeys().subList(min + 1, max + 1));
        node2.getEntries().addAll(node.getEntries().subList(min, max + 1));
        
        int key = node.getKeys().get(min);
        
        for (int i = max - 1; i >= min - 1; i--) {
            node.getKeys().remove(i);
            node.getEntries().remove(i);
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
        this.parent = null;
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