import java.util.Random;

class SkipListNode<T> {
    T value;
    int key;
    SkipListNode<T>[] forward;

    @SuppressWarnings("unchecked")
    public SkipListNode(int key, T value, int level){
        this.key = key;
        this.value = value;
        this.forward = new SkipListNode[level+1];
    }
}

public class SkipList<T>{
    private static final double PROBABILITY = 0.5;
    private static final int MAX_LEVEL = 16;
    private final SkipListNode<T> head;
    private int level;
    private final Random random;

    public SkipList(){
        this.head = new SkipListNode<>(Integer.MIN_VALUE,null,MAX_LEVEL);
        this.level = 0;
        this.random = new Random();
    }

    private int randomLevel(){
        int lvl =0;
        while(lvl<MAX_LEVEL&&random.nextDouble()<PROBABILITY){
            lvl++;
        }
        return lvl;
    }

    public void insert(int key, T value){
        SkipListNode<T>[] update = new SkipListNode[MAX_LEVEL+1];
        SkipListNode<T> current = head;

        for(int i = level;i>=0;i--){
            while (current.forward[i]!=null&&current.forward[i].key<key){
                current = current.forward[i];
            }
            update[i] = current;
        }
        current = current.forward[0];

        if(current==null||current.key!=key){
            int newLevel = randomLevel();
            if(newLevel>level){
                for(int i = level+1;i<=newLevel;i++){
                    update[i]=head;
                }
                level = newLevel;
            }
            SkipListNode<T> newNode = new SkipListNode<>(key, value, newLevel);
            for(int i = 0;i<=newLevel;i++){
                newNode.forward[i]=update[i].forward[i];
                update[i].forward[i]=newNode;
            }
        }
    }
    public void delete(int key){
        SkipListNode<T>[] update = new SkipListNode[MAX_LEVEL+1];
        SkipListNode<T> current = head;

        for(int i = level; i>=0; i--){
            while(current.forward[i]!=null&& current.forward[i].key<key){
                current = current.forward[i];
            }
            update[i]=current;
        }
        current = current.forward[0];

        if(current!=null&&current.key==key){
            for(int i = 0;i<=level;i++){
                if(update[i].forward[i]!=current)break;
                update[i].forward[i]=current.forward[i];
            }
            while (level>0&&head.forward[level]==null){
                level--;
            }
        }
    }
    public T search(int key){
        SkipListNode<T> current =head;

        for(int i = level;i>=0;i--){
            while (current.forward[i]!=null&&current.forward[i].key<key){
                current = current.forward[i];
            }
        }

        current = current.forward[0];

        if(current!=null&&current.key==key){
            return current.value;
        }
        return null;
    }

    public void printList(){
        for(int i = 0;i<=level;i++){
            SkipListNode<T> current = head.forward[i];
            System.out.print("Level "+i+": ");
            while (current!=null){
                System.out.print(current.key+" ");
                current = current.forward[i];
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        SkipList<String> skipList = new SkipList<>();
        skipList.insert(3, "three");
        skipList.insert(1, "one");
        skipList.insert(7, "seven");
        skipList.insert(5, "five");
        
        System.out.println("Skip List: ");
        skipList.printList();
        
        System.out.println("\nSearch for key 3: "+skipList.search(3));
        System.out.println("Search for key 6: "+skipList.search(6));
        
        System.out.println("\nDeleting key 3.");
        skipList.delete(3);
        skipList.printList();
    }
}