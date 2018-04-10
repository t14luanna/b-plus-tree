/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bplustreedemo;

import BPlusTree.BPlusTree;

/**
 *
 * @author LUANNA
 */
public class BPlusTreeDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int arr[] = {2, 5, 3, 1, 12};
        
        BPlusTree tree = new BPlusTree(4);
        
        for (int i = 0; i < 5; i++) {
            tree.insert(arr[i], arr[i] * 10);
            System.out.println("Insert " + arr[i] + " : " + tree.getRoot());
        }
    }
        
}
