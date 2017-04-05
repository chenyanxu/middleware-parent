package com.kalix.middleware.mail.api;

/**
 * Created by sunlf on 2017-04-05.
 * mail配置类
 */
public class MailConfig {
    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    private String smtpHost = "smtp.163.com";
    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
    //     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    private String account = "hanbi918@163.com";//发件人，必须加上@地址
    private String password = "hanling781230";//密码
    private boolean debug = true;//是否输出日志
    private String sendMailName = "系统管理员"; //发件人昵称

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getSendMailName() {
        return sendMailName;
    }

    public void setSendMailName(String sendMailName) {
        this.sendMailName = sendMailName;
    }
}
