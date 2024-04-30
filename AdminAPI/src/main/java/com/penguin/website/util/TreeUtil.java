package com.penguin.website.util;

import com.penguin.website.model.domain.Departments;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形结构
 */
public class TreeUtil {
    //将list集合转成树形结构的list集合
    public static List<Departments> listToTree(List<Departments> list) {
        //用递归找子。
        System.out.println("tree" +list);
        List<Departments> treeList = new ArrayList<Departments>();
        for (Departments tree : list) {
            System.out.println("tree" +tree);
            if (tree.getParentId()==0) {
                treeList.add(findChildren(tree, list));
            }
        }

        return treeList;
    }

    //寻找子节点
    private static Departments findChildren(Departments tree, List<Departments> list) {
        for (Departments node : list) {
            if (node.getParentId()==tree.getDid()) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<Departments>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }

}
