import java.util.LinkedList;
import java.util.Scanner;

public class PJ3 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        String[] commands = new String[n];
        ACNode head = new ACNode((byte) 0);
        ACNode acRoot = new ACNode((byte) 0);
        head.son = acRoot;
        acRoot.son = new ACNode((byte) 98);
        String s = input.nextLine();
        for (int i = 0; i < n; i++) {
            s = input.nextLine();
            commands[i] = s;
            boolean f = true;
            ACNode acNode = acRoot;
            for (int j = 3; j < s.length(); j++) {
                byte c = (byte) s.charAt(j);
                if ((c == 32) || (c == 66)) break;
                else if (c == 67) {
                    j += 7;
                    continue;
                }
                if (c >= 97)
                    if (!f) acNode = acNode.son = new ACNode(c);
                    else {
                        if (c < acNode.son.c) {
                            ACNode newNode = new ACNode(c);
                            newNode.brother = acNode.son;
                            acNode.son = newNode;
                        }
                        acNode = acNode.son;
                        while (c != acNode.c) {
                            if (acNode.brother == null) {
                                acNode.brother = new ACNode(c);
                                acNode = acNode.brother;
                                f = false;
                                break;
                            } else if (c < acNode.brother.c) {
                                ACNode newNode = new ACNode(c);
                                newNode.brother = acNode.brother;
                                acNode.brother = newNode;
                                acNode = newNode;
                                break;
                            }
                            acNode = acNode.brother;
                        }
                        if (acNode.son == null) f = false;
                    }
            }
            if (acNode != acRoot) acNode.List = new LinkedList<>();
        }
        acRoot.fail = head;
        LinkedList<ACNode> q = new LinkedList<>();
        q.add(acRoot);
        while (q.size() > 0) {
            ACNode node = q.removeFirst();
            ACNode son = node.son;
            while (son != null) {
                byte c = son.c;
                ACNode fail = node.fail;
                while (fail != head) {
                    ACNode failSon = fail.son;
                    while (failSon != null) {
                        if (failSon.c == c) break;
                        else if (failSon.c > c) {
                            failSon = null;
                            break;
                        }
                        failSon = failSon.brother;
                    }
                    if (failSon == null) fail = fail.fail;
                    else {
                        son.fail = failSon;
                        break;
                    }
                }
                if (fail == head) son.fail = acRoot;
                q.add(son);
                son = son.brother;
            }
        }
        TrieNode root = new TrieNode((byte) 0);
        root.son = new TrieNode((byte) 98);
        for (int i = 0; i < n; i++) {
            String[] command = commands[i].split("\\s+");
            char action = command[0].charAt(0);
            if (action == 'P') {
                TrieNode node = root;
                ACNode acNode = acRoot;
                s = command[1];
                TrieNode node1 = new TrieNode((byte) 0);
                boolean f = true;
                byte c = 0;
                for (int j = 0; j < s.length(); j++) {
                    c = (byte) s.charAt(j);
                    if (j < s.length() - 1)
                        if (!f) node = node.son = new TrieNode(c);
                        else {
                            if (c < node.son.c) {
                                TrieNode newNode = new TrieNode(c);
                                newNode.brother = node.son;
                                node.son = newNode;
                            }
                            node = node.son;
                            while (c != node.c) {
                                if (node.brother == null) {
                                    node = node.brother = new TrieNode(c);
                                    f = false;
                                    break;
                                } else if (c < node.brother.c) {
                                    TrieNode newNode = new TrieNode(c);
                                    newNode.brother = node.brother;
                                    node.brother = newNode;
                                    node = newNode;
                                    break;
                                }
                                node = node.brother;
                            }
                            if (node.son == null) f = false;
                        }
                    ACNode son = acNode.son;
                    while (son != null) {
                        if (son.c > c) {
                            son = null;
                            break;
                        } else if (son.c == c) break;
                        son = son.brother;
                    }
                    if (son != null) {
                        ACNode fail = acNode;
                        while (fail != acRoot) {
                            if (fail.List != null)
                                if (fail.List.size() == 0) fail.List.add(node1);
                                else if (fail.List.getLast() != node1) fail.List.add(node1);
                            fail = fail.fail;
                        }
                        acNode = son;
                    } else {
                        boolean f1 = false;
                        ACNode fail = acNode;
                        while (fail != head) {
                            if (fail.List != null)
                                if (fail.List.size() == 0) fail.List.add(node1);
                                else if (fail.List.getLast() != node1) fail.List.add(node1);
                            if (!f1)
                                if (fail != acNode) {
                                    son = fail.son;
                                    while (son != null) {
                                        if (son.c > c) {
                                            son = null;
                                            break;
                                        } else if (son.c == c) break;
                                        son = son.brother;
                                    }
                                    if (son != null) {
                                        acNode = son;
                                        f1 = true;
                                    }
                                }
                            fail = fail.fail;
                        }
                        if (!f1) acNode = acRoot;
                    }
                }
                ACNode fail = acNode;
                while (fail != acRoot) {
                    if (fail.List != null)
                        if (fail.List.size() == 0) fail.List.add(node1);
                        else if (fail.List.getLast() != node1) fail.List.add(node1);
                    fail = fail.fail;
                }
                node1.c = c;
                if (!f) node.son = node1;
                else {
                    if (c < node.son.c) {
                        node1.brother = node.son;
                        node.son = node1;
                    }
                    node = node.son;
                    while (c != node.c) {
                        if (node.brother == null) {
                            node.brother = node1;
                            break;
                        } else if (c < node.brother.c) {
                            node1.brother = node.brother;
                            node.brother = node1;
                            break;
                        }
                        node = node.brother;
                    }
                }
                s = command[2];
                int value = 0;
                for (int j = 0; j < s.length(); j++) {
                    c = (byte) s.charAt(j);
                    value = value * 10 + (c - 48);
                }
                node1.num = value;
                node1.isWord = true;
            } else if (action == 'A') {
                if (commands[i].charAt(3) == ' ') {
                    s = command[1];
                    TrieNode node = root;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        node = node.son;
                        while (c != node.c) node = node.brother;
                    }
                    s = command[2];
                    int value = 0;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        value = value * 10 + (c - 48);
                    }
                    node.num += value;
                } else if (commands[i].charAt(10) == ' ') {
                    s = command[1];
                    ACNode acNode = acRoot;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        acNode = acNode.son;
                        while (c != acNode.c) acNode = acNode.brother;
                    }
                    s = command[2];
                    int value = 0;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        value = value * 10 + (c - 48);
                    }
                    for (int j = 0; j < acNode.List.size(); j++) acNode.List.get(j).num += value;
                } else {
                    s = command[1];
                    TrieNode node = root;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        node = node.son;
                        while (c != node.c) node = node.brother;
                    }
                    s = command[2];
                    int value = 0;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        value = value * 10 + (c - 48);
                    }
                    if (node.isWord) node.num += value;
                    add(node.son, value);
                }
            } else if (action == 'D') {
                if (commands[i].charAt(3) == ' ') {
                    s = command[1];
                    TrieNode node = root;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        node = node.son;
                        while (c != node.c) node = node.brother;
                    }
                    node.num = 0;
                } else if (commands[i].charAt(10) == ' ') {
                    s = command[1];
                    ACNode acNode = acRoot;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        acNode = acNode.son;
                        while (c != acNode.c) acNode = acNode.brother;
                    }
                    for (int j = 0; j < acNode.List.size(); j++) acNode.List.get(j).num = 0;
                } else {
                    s = command[1];
                    TrieNode node = root;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        node = node.son;
                        while (c != node.c) node = node.brother;
                    }
                    node.num += 0;
                    del(node.son);
                }
            } else if (action == 'Q') {
                if (commands[i].charAt(5) == ' ') {
                    s = command[1];
                    TrieNode node = root;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        node = node.son;
                        while (c != node.c) node = node.brother;
                    }
                    System.out.println(node.num);
                } else if (commands[i].charAt(12) == ' ') {
                    s = command[1];
                    ACNode acNode = acRoot;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        acNode = acNode.son;
                        while (c != acNode.c) acNode = acNode.brother;
                    }
                    int sum = 0;
                    for (int j = 0; j < acNode.List.size(); j++) sum += acNode.List.get(j).num;
                    System.out.println(sum);
                } else {
                    s = command[1];
                    TrieNode node = root;
                    for (int j = 0; j < s.length(); j++) {
                        byte c = (byte) s.charAt(j);
                        node = node.son;
                        while (c != node.c) node = node.brother;
                    }
                    int sum = node.num;
                    System.out.println(sum + sum(node.son));
                }
            }
            commands[i] = null;
        }
        commands = null;
    }


    public static void add(TrieNode node, int value) {
        while (node != null) {
            if (node.son != null) add(node.son, value);
            if (node.isWord) node.num += value;
            node = node.brother;
        }
    }

    public static void del(TrieNode node) {
        while (node != null) {
            if (node.son != null) del(node.son);
            node.num = 0;
            node = node.brother;
        }
    }

    public static int sum(TrieNode node) {
        int n = 0;
        while (node != null) {
            if (node.son != null) n += sum(node.son);
            n += node.num;
            node = node.brother;
        }
        return n;
    }
}

class ACNode {
    LinkedList<TrieNode> List;
    ACNode son, brother, fail;
    byte c;

    public ACNode(byte c) {
        this.c = c;
    }
}

class TrieNode {
    int num;
    boolean isWord;
    TrieNode son, brother;
    byte c;

    public TrieNode(byte c) {
        this.c = c;
    }
}
