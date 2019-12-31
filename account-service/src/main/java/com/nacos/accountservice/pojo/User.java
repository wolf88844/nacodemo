package com.nacos.accountservice.pojo;

/**
 * @ClassName User
 * @Author LIUHANPENG
 * @Date 2019/12/31 0031 17:01
 **/
public class User {

    private int id;
    private String name;
    private Balance balance;

    public User(int id,String name){
        this.id=id;
        this.name=name;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
}
