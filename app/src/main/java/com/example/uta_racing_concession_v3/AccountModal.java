package com.example.uta_racing_concession_v3;



public class AccountModal {
    private String name;
    private Integer id;
    private Double balance;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public AccountModal(Integer id,
                        String name,
                        Double balance)
    {
        this.id=id;
        this.name=name;
        this.balance=balance;
    }
}
