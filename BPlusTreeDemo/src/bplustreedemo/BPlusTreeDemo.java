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
        int arr[] = {2, 7, 4, 1, 12, 6, 3, 8, 5, 9, 10, 11};
        
        BPlusTree tree = new BPlusTree(3);
        
        for (int i = 0; i < 12; i++) {
            tree.insert(arr[i], arr[i] * 10);
            System.out.println("Insert " + arr[i] + " : " + tree.getRoot());
        }
    }
        
}
