import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Main {
    static int stunum = 1000;
    static int anum = 1000;
    static int bnum = 1000;
    static int cnum = 1000;
    static HashMap<Integer, Integer> abooks = new HashMap<>();
    static HashMap<Integer, Integer> bbooks = new HashMap<>();
    static HashMap<Integer, Integer> cbooks = new HashMap<>();
    static ArrayList<Stu> stus = new ArrayList<>();
    static ArrayList<Order> orders = new ArrayList<>();
    static HashMap<Integer, Integer> bbooksStore = new HashMap<>();

    static HashMap<Integer, Integer> cbooksStore = new HashMap<>();
    static int maxStuCnt = 5;
    static int stucnt = rd(1, maxStuCnt);
    static int maxBookCnt=4;
    static int maxOpCnt=150;
    static int booknum = rd(3, 14);
    static int opnum = rd(1, maxOpCnt);
    public static void main(String[] args) {
        initBookStu();
        int i, j;
        int mode=rd(0,2);
        int day = rd(1, 200);
        for (i = 1; i <= day; i++) {
            Output.updateTime();
        }
        for (i = 1; i <= opnum; i++) {
            System.out.print("[2023-");
            if (Output.getMonth() < 10) {
                System.out.print(0);
            }
            System.out.print(Output.getMonth() + "-");
            if (Output.getDay() < 10) {
                System.out.print(0);
            }
            System.out.print(Output.getDay() + "] ");
            Stu stu = stus.get(rd(1, stucnt) - 1);
            int op;
            if(mode==0){
                if(i<opnum/2)op=1;
                else{
                    op=8;
                }
            }
            else{
                op=rd(1,8);
            }
            System.out.print(stu.name + " ");
            if (stu.bbooks.size() + stu.cbooks.size() == 0) {
                op = 1;
            }
            if (op == 1||op==2) {
                generateBorrow(stu);
            } else if (op == 3||op==5) {
                generateSmeard(stu);
            } else if (op == 4) {
                generateLost(stu);
            } else {
                generateReturn(stu);
            }
            int dayadd = rd(0, 1) == 0 ? 0 : rd(0, Math.min(365 - day, 4));
            for (j = day+1; j <= day+dayadd; j++) {
                Output.updateTime();
                if((j-1)%3==0){
                    processOrder();
                }
                for (Stu stu1 : stus) {
                    stu1.cnt = 0;
                }
            }
            day += dayadd;
        }
    }

    static int rd(int a, int b) {
        Random r = new Random();
        return a + r.nextInt(b - a + 1);
    }

    static void initBookStu() {
        int i;
        for (i = 0; i <= stucnt - 1; i++) {
            stus.add(new Stu(stunum));
            stunum++;
        }
        System.out.println(booknum);
        for (i = 1; i <= booknum - 3; i++) {
            int temp = rd(1, 3);
            int count = rd(0, maxBookCnt);
            if (temp == 1) {
                abooks.put(anum, count);
                System.out.println("A-" + anum + " " + count);
                anum++;
            } else if (temp == 2) {
                bbooks.put(bnum, count);
                System.out.println("B-" + bnum + " " + count);
                bnum++;
            } else {
                cbooks.put(cnum, count);
                System.out.println("C-" + cnum + " " + count);
                cnum++;
            }
        }
        int count = rd(0, maxBookCnt);
        abooks.put(anum, count);
        System.out.println("A-" + anum + " " + count);
        bbooks.put(bnum, count);
        System.out.println("B-" + bnum + " " + count);
        cbooks.put(cnum, count);
        System.out.println("C-" + cnum + " " + count);
        System.out.println(opnum);
    }

    static void generateBorrow(Stu stu) {
        System.out.print("borrowed ");
        int which = rd(1, 7);
        if (which == 1) {
            int whichbook = rd(1000, anum);
            System.out.println("A-" + whichbook);
        } else if (which <= 4) {
            int whichbook = rd(1000, bnum);
            System.out.println("B-" + whichbook);
            if (bbooks.get(whichbook) > 0) {
                if (stu.bbooks.size() == 0) {
                    stu.bbooks.add(whichbook);
                    int len = orders.size();
                    for (int j = 0; j <= len - 1; j++) {
                        Order order = orders.get(j);
                        if (order.stuname == stu.name && order.type == 0) {
                            len--;
                            orders.remove(order);
                            if (j > len - 1) {
                                break;
                            }
                            j--;
                        }
                    }
                } else {
                    if (bbooksStore.containsKey(whichbook)) {
                        bbooksStore.put(whichbook, bbooksStore.get(whichbook) + 1);
                    } else {
                        bbooksStore.put(whichbook, 1);
                    }
                }
                bbooks.put(whichbook, bbooks.get(whichbook) - 1);
            } else {
                boolean flag = false;
                for (Order order : orders) {
                    if (order.type == 0 && order.stuname == stu.name) {
                        flag = true;
                        break;
                    }
                }
                if (stu.bbooks.size() == 0 && stu.cnt < 3 && !flag) {
                    orders.add(new Order(0, whichbook, stu.name));
                    stu.cnt++;
                }
            }
        } else{
            int whichbook = rd(1000, cnum);
            System.out.println("C-" + whichbook);
            if (cbooks.get(whichbook) > 0) {
                if (!stu.cbooks.contains(whichbook)) {
                    stu.cbooks.add(whichbook);
                } else {
                    if (!cbooksStore.containsKey(whichbook)) {
                        cbooksStore.put(whichbook, 1);
                    } else {
                        cbooksStore.put(whichbook, cbooksStore.get(whichbook) + 1);
                    }
                }
                cbooks.put(whichbook, cbooks.get(whichbook) - 1);
            } else {
                boolean flag = false;
                for (Order order : orders) {
                    if (order.type == 1 && order.stuname == stu.name && order.bookname == whichbook) {
                        flag = true;
                        break;
                    }
                }
                if (stu.cbooks.size() == 0 && stu.cnt < 3 && !flag) {
                    orders.add(new Order(1, whichbook, stu.name));
                    stu.cnt++;
                }
            }
        }
    }

    static void generateReturn(Stu stu) {
        System.out.print("returned ");
        int which = rd(0, 1);
        if (stu.cbooks.size() == 0) which = 0;
        if (stu.bbooks.size() == 0) which = 1;
        if (which == 0) {
            int place = rd(0, stu.bbooks.size() - 1);
            int i1 = 0;
            int whichbook = 0;
            for (Integer integer : stu.bbooks) {
                if (i1 == place) {
                    whichbook = integer;
                }
                i1++;
            }
            stu.bbooks.remove(whichbook);
            if (bbooksStore.containsKey(whichbook)) {
                bbooksStore.put(whichbook, bbooksStore.get(whichbook) + 1);
            } else {
                bbooksStore.put(whichbook, 1);
            }
            System.out.println("B-" + whichbook);
        } else {
            int place = rd(0, stu.cbooks.size() - 1);
            int i1 = 0;
            int whichbook = 0;
            for (Integer integer : stu.cbooks) {
                if (i1 == place) {
                    whichbook = integer;
                }
                i1++;
            }
            stu.cbooks.remove(whichbook);
            if (cbooksStore.containsKey(whichbook)) {
                cbooksStore.put(whichbook, cbooksStore.get(whichbook) + 1);
            } else {
                cbooksStore.put(whichbook, 1);
            }
            System.out.println("C-" + whichbook);
        }
    }

    static void processOrder() {
        HashSet<Integer> rStu = new HashSet<>();
        int i;
        int len = orders.size();
        for (i = 0; i <= len - 1; i++) {
            Order order = orders.get(i);
            boolean flag = false;
            if (!rStu.contains(order.stuname) && order.type == 0 && bbooksStore.containsKey(order.bookname)) {
                flag = true;
                if (bbooksStore.get(order.bookname) == 1) {
                    bbooksStore.remove(order.bookname);
                } else {
                    bbooksStore.put(order.bookname, bbooksStore.get(order.bookname) - 1);
                }
                rStu.add(order.stuname);
                stus.get(order.stuname - 1000).bbooks.add(order.bookname);
            } else if (order.type == 1 && cbooksStore.containsKey(order.bookname)) {
                flag = true;
                if (cbooksStore.get(order.bookname) == 1) {
                    cbooksStore.remove(order.bookname);
                } else {
                    cbooksStore.put(order.bookname, cbooksStore.get(order.bookname) - 1);
                }
                stus.get(order.stuname - 1000).cbooks.add(order.bookname);
            }
            if (flag) {
                orders.remove(order);
                len--;
                if (i > len - 1) break;
                i--;
            }
        }
        len = orders.size();
        for (i = 0; i <= len - 1; i++) {
            if (orders.get(i).type == 0 &&rStu.contains(orders.get(i).stuname)){
                orders.remove(i);
                len--;
                if(i>len-1)break;
                i--;
            }
        }
        for(Integer name:bbooksStore.keySet()){
            bbooks.put(name,bbooks.get(name)+bbooksStore.get(name));
        }
        for(Integer name:cbooksStore.keySet()){
            cbooks.put(name,cbooks.get(name)+cbooksStore.get(name));
        }
        bbooksStore=new HashMap<>();
        cbooksStore=new HashMap<>();
    }

    static void generateSmeard(Stu stu) {
        System.out.print("smeared ");
        int which = rd(0, 1);
        if (stu.cbooks.size() == 0) which = 0;
        if (stu.bbooks.size() == 0) which = 1;
        if (which == 0) {
            int place = rd(0, stu.bbooks.size() - 1);
            int i1 = 0;
            int whichbook = 0;
            for (Integer integer : stu.bbooks) {
                if (i1 == place) {
                    whichbook = integer;
                }
                i1++;
            }
            System.out.println("B-" + whichbook);
        } else {
            int place = rd(0, stu.cbooks.size() - 1);
            int i1 = 0;
            int whichbook = 0;
            for (Integer integer : stu.cbooks) {
                if (i1 == place) {
                    whichbook = integer;
                }
                i1++;
            }
            System.out.println("C-" + whichbook);
        }
    }

    static void generateLost(Stu stu) {
        System.out.print("lost ");
        int which = rd(0, 1);
        if (stu.cbooks.size() == 0) which = 0;
        if (stu.bbooks.size() == 0) which = 1;
        if (which == 0) {
            int place = rd(0, stu.bbooks.size() - 1);
            int i1 = 0;
            int whichbook = 0;
            for (Integer integer : stu.bbooks) {
                if (i1 == place) {
                    whichbook = integer;
                }
                i1++;
            }
            stu.bbooks.remove(whichbook);
            System.out.println("B-" + whichbook);
        } else {
            int place = rd(0, stu.cbooks.size() - 1);
            int i1 = 0;
            int whichbook = 0;
            for (Integer integer : stu.cbooks) {
                if (i1 == place) {
                    whichbook = integer;
                }
                i1++;
            }
            stu.cbooks.remove(whichbook);
            System.out.println("C-" + whichbook);
        }
    }
}

class Stu {
    int name = 0;
    int cnt = 0;
    HashSet<Integer> bbooks = new HashSet<>();
    HashSet<Integer> cbooks = new HashSet<>();

    public Stu(int name) {
        this.name = name;
    }
}

class Order {
    int type;
    int bookname;
    int stuname;

    public Order(int type, int bookname, int stuname) {
        this.type = type;
        this.bookname = bookname;
        this.stuname = stuname;
    }
}