import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static int stunum=1000;
    static int anum=1000;
    static int bnum=1000;
    static int cnum=1000;
    static int opnum=5;//rd(1,20);
    static int libcnt=rd(1,4);
    static HashMap<Integer,Integer> abooks=new HashMap<>();
    static HashMap<Integer,Integer> bbooks=new HashMap<>();
    static HashMap<Integer,Integer> cbooks=new HashMap<>();
    static ArrayList<Stu> stus=new ArrayList<>();
    static int maxStuCnt=4;
    static int stucnt=rd(1,maxStuCnt);
    static int booknum=rd(4,5);
    static HashMap<Integer,Lib> libs=new HashMap<>();
    public static void main(String[] args){
        initBookStu();
        int i,j;
        int day=1;
        for(i=1;i<=day;i++){
            Output.updateTime();
        }
        for(i=1;i<=opnum;i++){
            for(Stu stu:stus){
                for(Integer integer:stu.bbooksPre){
                    if(stu.bbooks.size()==0){
                        stu.bbooks.add(integer);
                    }
                }
                for(Integer integer:stu.cbooksPre){
                    if(!stu.cbooks.contains(integer)){
                        stu.cbooks.add(integer);
                    }
                }
                stu.bbooksPre.clear();
                stu.cbooksPre.clear();
            }
            System.out.print("[2023-");
            if(Output.getMonth()<10){
                System.out.print(0);
            }
            System.out.print(Output.getMonth()+"-");
            if(Output.getDay()<10){
                System.out.print(0);
            }
            System.out.print(Output.getDay()+"] ");
            Stu stu=stus.get(rd(1,stucnt)-1);
            int op=rd(1,4);
            System.out.print(stu.from+"-"+stu.name+" ");
            if(stu.bbooks.size()+stu.cbooks.size()==0){
                op=1;
            }
            if(op==1){
                generateBorrow(stu);
            }
            else if(op==2){
                generateSmeard(stu);
            }
            else if(op==3||op==4){
                if(op==3)System.out.print("lost ");
                if(op==4)System.out.print("returned ");
                int which=rd(0,1);
                if(stu.cbooks.size()==0)which=0;
                if(stu.bbooks.size()==0)which=1;
                if(which==0){
                    int place=rd(0,stu.bbooks.size()-1);
                    int i1=0;
                    int whichbook = 0;
                    for(Integer integer:stu.bbooks){
                        if(i1==place){
                            whichbook=integer;
                        }
                        i1++;
                    }
                    stu.bbooks.remove(whichbook);
                    System.out.println("B-"+whichbook);
                }
                else{
                    int place=rd(0,stu.cbooks.size()-1);
                    int i1=0;
                    int whichbook = 0;
                    for(Integer integer:stu.cbooks){
                        if(i1==place){
                            whichbook=integer;
                        }
                        i1++;
                    }
                    stu.cbooks.remove(whichbook);
                    System.out.println("C-"+whichbook);
                }
            }
            int dayadd=rd(0, 1) == 0 ? 0 : rd(0, Math.min(365-day,4));
                day += dayadd;
            for(j=1;j<=dayadd;j++){
                Output.updateTime();
            }
        }
    }
    static int rd(int a,int b){
        Random r=new Random();
        return a+r.nextInt(b-a+1);
    }
    static void initBookStu(){
        int i,j;
        stus.add(new Stu(stunum,1000));
        stus.add(new Stu(stunum,1001));
        stunum++;
        for(i=2;i<=stucnt-1;i++){
            stus.add(new Stu(stunum,rd(1000,libcnt+999)));
            stunum++;
        }
        System.out.println(libcnt);
        for(i=1;i<=libcnt;i++){
            libs.put(999+i,new Lib());
        }
        for(i=1;i<=booknum/3;i++){
            int temp=rd(1,7);
            int count;
            if(temp==1){
                for(j=1;j<=libcnt;j++){
                    count=rd(1,4);
                    libs.get(j+999).abooks.put(anum,count);
                }
                anum++;
            }
            else if(temp>=2&&temp<=4){
                for(j=1;j<=libcnt;j++){
                    count=rd(1,4);
                    libs.get(j+999).bbooks.put(bnum,count);
                    libs.get(j+999).bBoAble.put(bnum, rd(0, 1) == 0);
                }
                bnum++;
            }
            else{
                for(j=1;j<=libcnt;j++){
                    count=rd(1,4);
                    libs.get(j+999).cbooks.put(cnum,count);
                    libs.get(j+999).cBoAble.put(cnum, rd(0, 1) == 0);
                }
                cnum++;
            }
        }
        for(i=1;i<=libcnt;i++){
            for(j=booknum/3+1;j<=booknum;j++){
                int temp=rd(1,7);
                int count;
                if(temp==1){
                    count=rd(1,4);
                    libs.get(i+999).abooks.put(anum,count);
                    anum++;
                }
                else if(temp>=2&&temp<=4){
                    count=rd(1,4);
                    libs.get(i+999).bbooks.put(bnum,count);
                    libs.get(i+999).bBoAble.put(bnum, rd(0, 1) == 0);
                    bnum++;
                }
                else{
                    count=rd(1,4);
                    libs.get(i+999).cbooks.put(cnum,count);
                    libs.get(i+999).cBoAble.put(cnum, rd(0, 1) == 0);
                    cnum++;
                }
            }
            libs.get(i+999).abooks.put(anum,1);
            anum++;
            libs.get(i+999).bbooks.put(bnum,1);
            libs.get(i+999).bBoAble.put(bnum,rd(0,1)==0);
            bnum++;
            libs.get(i+999).cbooks.put(cnum,1);
            libs.get(i+999).cBoAble.put(cnum,rd(0,1)==0);
            cnum++;
        }
        for(i=1;i<=libcnt;i++){
            abooks=libs.get(i+999).abooks;
            bbooks=libs.get(i+999).bbooks;
            cbooks=libs.get(i+999).cbooks;
            System.out.print((i+999)+" ");
            System.out.println(abooks.size()+bbooks.size()+cbooks.size());
            for(Integer integer:abooks.keySet()){
                System.out.println("A-"+integer+" "+abooks.get(integer)+" "+"Y");
            }
            for(Integer integer:bbooks.keySet()){
                System.out.print("B-"+integer+" "+bbooks.get(integer)+" ");
                if(libs.get(999+i).bBoAble.get(integer)){
                    System.out.println("Y");
                }
                else{
                    System.out.println("N");
                }
            }
            for(Integer integer:cbooks.keySet()){
                System.out.print("C-"+integer+" "+cbooks.get(integer)+" ");
                if(libs.get(999+i).cBoAble.get(integer)){
                    System.out.println("Y");
                }
                else{
                    System.out.println("N");
                }
            }
        }
        System.out.println(opnum);
    }
    static void generateBorrow(Stu stu){
        System.out.print("borrowed ");
        Lib lib=libs.get(stu.from);
        int which=rd(1,5);
        int whichbook;
        if(which==1){
            whichbook=rdMap(lib.abooks);
            System.out.println("A-"+whichbook);
        }
        else if(which==2||which==3){
            if(rd(0,3)==0){
                whichbook=rdMap(lib.bbooks);
            }
            else if(rd(0,1)==0){
                lib=libs.get(rd(1000,1000+libcnt-1));
                whichbook=rdMap(lib.bbooks);
                if(!checkNum(0,whichbook)){
                    lib=libs.get(rd(1000,1000+libcnt-1));
                    whichbook=rdMap(lib.bbooks);
                }
            }
            else{
                whichbook=rd(1000,bnum+booknum*4);
                if(!checkNum(0,whichbook)){
                    whichbook=rd(1000,bnum+booknum*4);
                }
            }
            System.out.println("B-"+whichbook);
            lib=libs.get(stu.from);
            if(stu.bbooks.size()==0){
                if(lib.bbooks.containsKey(whichbook)&&lib.bbooks.get(whichbook)>0){
                    stu.bbooks.add(whichbook);
                    lib.bbooks.put(whichbook,lib.bbooks.get(whichbook)-1);
                }
                else{
                    outBorrow(0,whichbook,stu);
                }
            }
        }
        else {
            if(rd(0,3)==0){
                whichbook=rdMap(lib.cbooks);
            }
            else if(rd(0,1)==0){
                lib=libs.get(rd(1000,1000+libcnt-1));
                whichbook=rdMap(lib.cbooks);
                if(!checkNum(1,whichbook)){
                    lib=libs.get(rd(1000,1000+libcnt-1));
                    whichbook=rdMap(lib.cbooks);
                }
            }
            else{
                whichbook=rd(1000,cnum+booknum*4);
                if(!checkNum(1,whichbook)){
                    whichbook=rd(1000,cnum+booknum*4);
                }
            }
            System.out.println("C-"+whichbook);
            if(!stu.cbooks.contains(whichbook)){
                if(lib.cbooks.containsKey(whichbook)&&lib.cbooks.get(whichbook)>0){
                    stu.cbooks.add(whichbook);
                    lib.cbooks.put(whichbook,lib.cbooks.get(whichbook)-1);
                }
                else{
                    outBorrow(1,whichbook,stu);
                }
            }
        }
    }
    static boolean checkNum(int type,int num){
        int cnt=0;
        if(type==0){
            for(Lib lib:libs.values()){
                if(lib.bbooks.containsKey(num)&&lib.bbooks.get(num)>0&&lib.bBoAble.get(num)){
                    cnt++;
                    if(cnt>1)return false;
                }
            }
        }
        else{
            for(Lib lib:libs.values()){
                if(lib.cbooks.containsKey(num)&&lib.cbooks.get(num)>0&&lib.cBoAble.get(num)){
                    cnt++;
                    if(cnt>1)return false;
                }
            }
        }
        return true;
    }
    static void outBorrow(int type, int num, Stu stu){
        if(type==0){
            for(Integer integer:libs.keySet()){
                Lib lib=libs.get(integer);
                if(lib.bbooks.containsKey(num)&&lib.bbooks.get(num)>0&&lib.bBoAble.get(num)){
                    lib.bbooks.put(num,lib.bbooks.get(num)-1);
                    stu.bbooksPre.add(num);
                }
            }
        }
        else{
            for(Integer integer:libs.keySet()){
                Lib lib=libs.get(integer);
                if(lib.cbooks.containsKey(num)&&lib.cbooks.get(num)>0&&lib.cBoAble.get(num)){
                    lib.cbooks.put(num,lib.cbooks.get(num)-1);
                    stu.cbooksPre.add(num);
                }
            }
        }
    }
    static void generateSmeard(Stu stu){
        System.out.print("smeard ");
        int which=rd(0,1);
        if(stu.cbooks.size()==0)which=0;
        if(stu.bbooks.size()==0)which=1;
        if(which==0){
            int place=rd(0,stu.bbooks.size()-1);
            int i1=0;
            int whichbook = 0;
            for(Integer integer:stu.bbooks){
                if(i1==place){
                    whichbook=integer;
                }
                i1++;
            }
            System.out.println("B-"+whichbook);
        }
        else{
            int place=rd(0,stu.cbooks.size()-1);
            int i1=0;
            int whichbook = 0;
            for(Integer integer:stu.cbooks){
                if(i1==place){
                    whichbook=integer;
                }
                i1++;
            }
            System.out.println("C-"+whichbook);
        }
    }
    static int rdMap(HashMap<Integer,Integer> hashMap){
        int cnt=rd(0, hashMap.size()-1);
        for(Integer integer:hashMap.keySet()){
            if(cnt==0){
                return integer;
            }
            cnt--;
        }
        return 1;
    }
}

class Stu{
    int name=0;
    int from;
    HashSet<Integer> bbooks=new HashSet<>();
    HashSet<Integer> bbooksPre=new HashSet<>();
    HashSet<Integer> cbooks=new HashSet<>();
    HashSet<Integer> cbooksPre=new HashSet<>();
    public Stu(int name,int from){
        this.name=name;
        this.from=from;
    }
}
class Lib{
    HashMap<Integer,Integer> abooks=new HashMap<>();
    HashMap<Integer,Integer> bbooks=new HashMap<>();
    HashMap<Integer,Boolean> bBoAble=new HashMap<>();
    HashMap<Integer,Integer> cbooks=new HashMap<>();
    HashMap<Integer,Boolean> cBoAble=new HashMap<>();
    public Lib(){

    }
}