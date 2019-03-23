package com.kalix.middleware.xml.biz;

import com.kalix.middleware.xml.api.biz.IXmlService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringReader;


/**
 * Created by sunlf on 2018/12/8.
 */
public class XmlServiceImpl implements IXmlService {

    /**
     * marshal class to xml
     */
    @Override
    public void marshal() {
        Customer customer = new Customer();
        customer.setId(100L);
        customer.setName("mkyong");
        customer.setAge("29");

        try {

//            File file = new File("d:\\file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

//            jaxbMarshaller.marshal(customer, file);
            jaxbMarshaller.marshal(customer, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * unmarshal xml to class
     */
    @Override
    public void unmarshal() {
        try {
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<customer>\n" +
                    "    <id>100</id>\n" +
                    "    <name>mkyong</name>\n" +
                    "    <age>29</age>\n" +
                    "</customer>\n";
//            File file = new File("d:\\file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            Customer customer = (Customer) jaxbUnmarshaller.unmarshal(file);
            Customer customer = (Customer) jaxbUnmarshaller.unmarshal(new StringReader(xml));
            System.out.println(customer);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}

@XmlRootElement // 必须要标明这个元素
@XmlAccessorType(XmlAccessType.FIELD)
class Customer {
    private Long id;
    private String name;
    private String age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
