import java.io.*;
import java.util.*;

class BPNode{
     boolean isLeaf;
     List<Integer> keys;
     List<BPNode> children;

     BPNode(boolean isLeaf){
         this.isLeaf = isLeaf;
         this.keys = new ArrayList<>();
         this.children = new ArrayList<>();
     }
}

public class BppTree{
    static BPNode root;
    static int maxKeys;

    public static void insert(int key){
        BPNode r = root;
        if(r.keys.size() == maxKeys){
            BPNode s = new BPNode(false);
            root = s;
            s.children.add(r);
            split(s,0,r);
            insertNonFull(s,key);
        }else{
            insertNonFull(r,key);
        }
    }

    private static void insertNonFull(BPNode x, int k){
        int i = x.keys.size() -1;
        if(x.isLeaf){
            x.keys.add(0);
            while(i>=0&&k<x.keys.get(i)){
                x.keys.set(i+1,x.keys.get(i));
                i--;
            }
            x.keys.set(i+1,k);
        }else{
            while(i>=0&&k<x.keys.get(i)){
                i--;
            }
            i++;
            BPNode child = x.children.get(i);
            if(child.keys.size()==maxKeys){
                split(x,i,child);
                if(k>x.keys.get(i)){
                    i++;
                }
            }
            insertNonFull(x.children.get(i), k);
        }
    }

    private static void split(BPNode parent, int index, BPNode child){
        BPNode newChild = new BPNode(child.isLeaf);
        int t = maxKeys/2;
        parent.keys.add(index,child.keys.get(t));
        parent.children.add(index+1,newChild);

        for(int j=t+1;j<maxKeys;j++){
            newChild.keys.add(child.keys.get(j));
        }
        for(int j = maxKeys-1;j>=t;j--){
            child.keys.remove(j);
        }

        if(!child.isLeaf){
            for(int j=t+1;j<=maxKeys;j++){
                newChild.children.add(child.children.get(j));
            }
            for(int j=maxKeys;j>=t+1;j--){
                child.children.remove(j);
            }
        }
    }
    public static void display(BPNode node, BufferedWriter bw) throws IOException{
        if(node != null){
            bw.write(node.keys.toString()+"\n");
            if(!node.isLeaf){
                for(BPNode child : node.children){
                    display(child, bw);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        maxKeys = Integer.parseInt(st.nextToken());
        root = new BPNode(true);

        for(int i=0;i<n;i++){
            int key = Integer.parseInt(br.readLine().trim());
            insert(key);
        }

        display(root, bw);
        bw.flush();
        br.close();
        bw.close();

    }
}