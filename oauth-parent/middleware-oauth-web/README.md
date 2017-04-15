# framework-webapp-cg

##### 代码自动生成时的前端实现，调用framework-plugin-cg插件，前台页面输入各种参数，后台根据参数生成调用插件所需要javaBean文件、pom.xml及jvm.config文件，代码生成后再进行代码的压缩，生成zip包，并将zip包上传到couchdb中，并返回zip包下载地址提供给前台，提供下载。
  * CodeGeneration是调用framework-plugin-cg插件的后台实现类，
  * cg-camel.xml配置了调用后台的route配置，包括karaf的目录和mvn的目录等
  * web.xml配置了servlet及servlet映射及web首页
  * index.jsp前台页面，提供所需javaBean文件的上传及各种参数的输入

# 使用
在ie等浏览器地址栏中输入

    http://localhost:8181/cg/
上传新模块的javabean文件，填写新工程的相关信息，填写完毕后点击“代码生成”按钮，生成完毕后，点击“代码下载”按钮即可下载生成的代码。

# 注意事项
  * 提供的javaBean文件需要正确编写。
  * 新工程的各个参数需要正确填写，否则生成的代码不符合规范
