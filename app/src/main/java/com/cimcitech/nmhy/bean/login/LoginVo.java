package com.cimcitech.nmhy.bean.login;

/**
 * Created by qianghe on 2018/11/15.
 */

public class LoginVo {
    /*
    *   {
          "code": 0,
          "data": {
            "accountId": 3,
            "accountNo": "17620465672",
            "accountType": "admin",
            "userName": "admin",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50SWQiOiIzIiwiZXhwIjoxNTQyMzMyMjEyLCJpYXQiOjE1NDIyNDU4MTJ9.DAshK_ed1JoNxInb3WZwMZwORlXcS2B3xW6bZPr1LjQ"
          },
          "msg": "",
          "success": true
        }
    * */

    private String msg;
    private boolean success;
    private Data data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private int accountId;
        private String accountNo;
        private String accountType;
        private String userName;
        private String token;

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
