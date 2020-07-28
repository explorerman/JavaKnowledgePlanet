package leetcode_tree;

/**
 * @author 李杰
 * @version 1.0
 * @Description 二叉树中的最大路径和 [1,-2,-3,1,3,-2,null,-1]
 * @package
 * @file ${fileName.java}
 * @createTime: 创建时间: 2020/7/23 11:33
 * @title 标题: 二叉树中的最大路径和
 * @module 模块: 模块名称
 * @reviewer 审核人
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class number124_2 {
    int res = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
        if(root == null) return 0;
        dfs(root);
        return res;
    }

    private int dfs(TreeNode root) {
        if(root == null) return 0;
        int left = Math.max(dfs(root.left), 0);
        int right = Math.max(dfs(root.right), 0);
        res = Math.max(res, root.val + left + right);
        return root.val + Math.max(left, right);
    }


}

