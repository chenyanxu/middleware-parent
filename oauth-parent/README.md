# 说明
  本模块主要功能是用于封装了oauth2的服务,参考地址 https://github.com/ameizi/oltu-oauth2-example
# 备注
  目前支持多个接收人,还不支持cc，bc等，后续增加模板的支持
  示例如下：
  ```
  public static void main(String[] args) throws Exception {
          MailServiceImpl mail = new MailServiceImpl();
          MailContent content = new MailContent();
          content.setContent("你好, 今天全场5折, 快来抢购, 错过今天再等一年。。。");
          content.setSubject("打折钜惠");
          Map<String, String> map = new HashMap<>();
          map.put("sunlf", "minikiller@qq.com");
          map.put("meng", "1041168917@qq.com");
          map.put("gao", "576226387@qq.com");
          content.setReceivemail(map);
          mail.sendMail(content);
      }
  ```