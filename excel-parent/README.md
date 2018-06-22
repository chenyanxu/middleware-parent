# excel-parent

jetty.xml
```
    <Get name="handler">
        <Call name="addHandler">
            <Arg>
                <New class="org.eclipse.jetty.servlet.ServletContextHandler">
                    <Set name="contextPath">/review</Set>
                    <Set name="resourceBase">D:\java-develop\tools\apache-karaf-4.2.0\reviewfiles</Set>
                    <Call name="addServlet">
                        <Arg>org.eclipse.jetty.servlet.DefaultServlet</Arg>
                        <Arg>/</Arg>
                    </Call>
                </New>
            </Arg>
        </Call>
    </Get>
```
