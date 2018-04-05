
### xml报文格式化由如下注解完成
XmlPath：含两个属性value、 valueAttr ;其中value是默认属性,代表节点的相对xpath路径;valueAttr对应节点的属性名称,便于取节点属性的值  
BeanClass：只有一个属性value,也是默认属性，代表子节点,用于描述对象中含有子对象的情况  

```xml
示例：
------------------------------------------------------
1、简单对象
  报文字符串：
<?xml version="1.0" encoding="utf-8"?>
<msgbody>
  <CO_AMT>8000</CO_AMT>
  <APP_USER_ID>01009</APP_USER_ID>
  <APP_USER_NAME>林寻</APP_USER_NAME>
  <APP_USER_ORG_NO>HBWORK</APP_USER_ORG_NO>
</msgbody>

格式化类配置：
@XmlPath("msgbody")
public class SimpleObject {
    @XmlPath("CO_AMT")
    public Integer amt;
    @XmlPath("APP_USER_ID")
    public String userId;
    @XmlPath("APP_USER_NAME")
    public String userName;
    @XmlPath("APP_USER_ORG_NO")
    public String userOrg;    
}
------------------------------------------------------

2、多对象
报文字符串：
<?xml version="1.0" encoding="utf-8"?>
<Packet>
  <Data>
    <PUB>
      <seqnum>141221000000019165</seqnum>
      <brchno>018701</brchno>
      <userid>0187001</userid>
    </PUB>
    <Req>
      <acctno>802000031192588</acctno>
    </Req>
  </Data>
</Packet>
格式化类配置：
@XmlPath("Packet/Data")
public class MultiObject {
    @BeanClass(SimpleObject1.class)
    public SimpleObject1 object1;
    
    @BeanClass(SimpleObject2.class)
    public SimpleObject2 object2;   
}

@XmlPath("PUB")
public class SimpleObject1 {
    @XmlPath("seqnum")
    public BigDecimal seqNum;
    @XmlPath("brchno")
    public String brchNo;
    @XmlPath("userid")
    public String userId;    
}

@XmlPath("Req")
public class SimpleObject2 {
    @XmlPath("acctno")
    public String acctNo;
}
------------------------------------------------------

3、集合
报文字符串：
<?xml version="1.0" encoding="utf-8"?>
<Packet>
  <Data>
    <Req>
      <RealTimeAmtList>
        <MX>
          <acctno>02000031192588</acctno>
          <amntcd>a</amntcd>
          <tranam>960000.0</tranam>
          <ioshtg>L</ioshtg>
        </MX>
        <MX>
          <acctno>02000031192589</acctno>
          <amntcd>b</amntcd>
          <tranam>960000.02</tranam>
          <ioshtg>M</ioshtg>
        </MX>
      </RealTimeAmtList>
    </Req>
  </Data>
</Packet>

格式化类配置：
@XmlPath("Packet/Data/Req")
public class ListObject {
    @XmlPath("RealTimeAmtList")
    @BeanClass(SimpleObject.class)
    public List<SimpleObject> realTimeAmtList;  
}

@XmlPath("MX")
public class SimpleObject {
    @XmlPath("acctno")
    public String acctNo;
    @XmlPath("amntcd")
    public char amntCd;
    @XmlPath("tranam")
    public double tranAm;
    @XmlPath("ioshtg")
    public String ioshtg;  
}
------------------------------------------------------
  
4、对象和集合
报文字符串：
<?xml version="1.0" encoding="utf-8"?>
<Packet>
  <Data>
    <Req>
      <RealTimeAmtList>
        <MX>
          <acctno>02000031192588</acctno>
          <amntcd>a</amntcd>
          <tranam>960000.0</tranam>
          <ioshtg>L</ioshtg>
        </MX>
        <MX>
          <acctno>02000031192589</acctno>
          <amntcd>b</amntcd>
          <tranam>960000.02</tranam>
          <ioshtg>M</ioshtg>
        </MX>
      </RealTimeAmtList>
    </Req>
    <PUB>
      <seqnum>141221000000019165</seqnum>
      <brchno>018701</brchno>
      <userid>0187001</userid>
    </PUB>
  </Data>
</Packet>

格式化类配置：
@XmlPath("Packet/Data")
public class ListObject {
    @XmlPath("Req/RealTimeAmtList")
    @BeanClass(SimpleObject.class)
    public List<SimpleObject> realTimeAmtList;
    
    @BeanClass(SimpleObject1.class)
    public SimpleObject1 simpleObject1;
}

@XmlPath("MX")
public class SimpleObject {
    @XmlPath("acctno")
    public String acctNo;
    @XmlPath("amntcd")
    public char amntCd;
    @XmlPath("tranam")
    public double tranAm;
    @XmlPath("ioshtg")
    public String ioshtg;  
}

@XmlPath("PUB")
public class SimpleObject1 {
    @XmlPath("seqnum")
    public BigDecimal seqNum;
    @XmlPath("brchno")
    public String brchNo;
    @XmlPath("userid")
    public String userId;  
}
------------------------------------------------------
  
5、混合类型
报文字符串：
<?xml version="1.0" encoding="utf-8"?>
<root>
  <name>name1</name>
  <age>12</age>
  <phone>123456</phone>
  <info>
    <jobNo>2345</jobNo>
    <house id="68">
      <size>110</size>
    </house>
  </info>
  <houses>
    <house id="57">
      <size>567</size>
    </house>
    <house id="58">
      <size>5677</size>
    </house>
  </houses>
  <chirld name="figue">
    <age>89</age>
    <house id="54">
      <size>548</size>
    </house>
    <cars/>
  </chirld>
  <chirld name="figue2">
    <age>81</age>
    <house id="891">
      <size>8912</size>
    </house>
    <cars>
      <car>
        <carId>DD222</carId>
        <carName>凯美瑞</carName>
      </car>
      <car>
        <carId>DD2111</carId>
        <carName>凯美瑞2</carName>
      </car>
    </cars>
  </chirld>
</root>

格式化类配置：
@XmlPath("root")
public class Persion {
    @XmlPath("name")
    public String name;
    
    @XmlPath("age")
    public Integer age;
    
    @XmlPath("phone")
    public Integer phone;
    
    @BeanClass(Info.class)
    public Info info;
        
    @XmlPath("houses")
    @BeanClass(House.class)
    public List<House> houses;
    
    @BeanClass(Chirld.class)
    public List<Chirld> chirlds;
}

@XmlPath(value="info")
public class Info {
    @XmlPath("jobNo")
    public String jobNo;
    @BeanClass(House.class)
    public House house ;  
}

@XmlPath("house")
public class House {
    
    @XmlPath(valueAttr="@id")
    public String id;
  
    @XmlPath("size")
    public Integer size;
}

@XmlPath("chirld")
public class Chirld {  
    @XmlPath(valueAttr="@name")
    public String name;
    
    @XmlPath("age")
    public BigDecimal age;
    @BeanClass(House.class)
    public House house;
    
    @XmlPath("cars")
    @BeanClass(Car.class)
    public List<Car> cars; 
}

@XmlPath("car")
public class Car {
    @XmlPath("carId")
    public String carId;
    @XmlPath("carName")
    public String carName;
}
------------------------------------------------------
  
6、其他类型
报文字符串：
<?xml version="1.0" encoding="utf-8"?>
<field key="info1">
  <field key="jobNo">014397</field>
  <field key="compName" value="宇信"/>
  <field key="compLocal">
    <value>北京</value>
  </field>
</field>
格式化类配置：
@XmlPath("field[@key='info1']")
public class Info1 {
    @XmlPath("field[@key='jobNo']")
    public String jobNo;
    
    @XmlPath(value="field[@key='compName']",valueAttr="@value")
    public String compName;
    
    @XmlPath("field[@key='compLocal']/value")
    public String compLocal;
}
```


### json报文格式化由如下注解完成：
JsonPath:只有一个属性value,也是默认属性,用于描述此对象在json字符串中的key  
BeanClass：只有一个属性value,也是默认属性，代表子对象,用于描述对象中含有子对象的情况  
```xml
示例：
1、简单对象
报文字符串：
{
    "carName": "凯美瑞", 
    "carId": "S0001"
}
格式化类配置：
public class Car {
    @JsonPath("carId")
    public String carId;
    @JsonPath("carName")
    public String carName;
}
------------------------------------------------------

2、多对象
报文字符串：
{
    "CAR": {
        "carName": "凯美瑞", 
        "carId": "S0001"
    }, 
    "HOUSE": {
        "address": "江苏", 
        "size": 100
    }
}
格式化类配置：
public class MultiObject {
    @BeanClass(Car.class)
    public Car car;
    
    @BeanClass(House.class)
    public House house;
}

@JsonPath("CAR")
public class Car {
    @JsonPath("carId")
    public String carId;
    @JsonPath("carName")
    public String carName;
}

@JsonPath("HOUSE")
public class House {
    @JsonPath("address")
    public String address;
    @JsonPath("size")
    public Integer size;
}
------------------------------------------------------

3、集合
报文字符串：
{
    "LIST": [
        {
            "carName": "凯美瑞", 
            "carId": "S0001"
        }, 
        {
            "carName": "凯美瑞2", 
            "carId": "S0002"
        }
    ]
}

格式化类配置：
public class ListObject {  
    @JsonPath("LIST")
    @BeanClass(Car.class)
    public List<Car> list;
}

@JsonPath("CAR")
public class Car {
    @JsonPath("carId")
    public String carId;
    @JsonPath("carName")
    public String carName;
}
------------------------------------------------------
  
4、对象和集合
报文字符串：
{
    "HOUSE": {
        "address": "江苏", 
        "size": 100
    }, 
    "LIST": [
        {
            "carName": "凯美瑞", 
            "carId": "S0001"
        }, 
        {
            "carName": "凯美瑞2", 
            "carId": "S0002"
        }
    ]
}
格式化类配置：
public class ListObject {
    @BeanClass(House.class)
    public House house;
    
    @JsonPath("LIST")
    @BeanClass(Car.class)
    public List<Car> list;
}

@JsonPath("HOUSE")
public class House {
    @JsonPath("address")
    public String address;
    @JsonPath("size")
    public Integer size;
}

@JsonPath("CAR")
public class Car {
    @JsonPath("carId")
    public String carId;
    @JsonPath("carName")
    public String carName;
}
------------------------------------------------------
  
5、混合类型
报文字符串：
{
    "chirlds": [
        {
            "name": "json1", 
            "house": {
                "size": 11, 
                "id": "cTP001"
            }, 
            "age": 1
        }, 
        {
            "cars": [
                {
                    "carName": "凯美瑞", 
                    "carId": "DDDDD2"
                }
            ], 
            "name": "json2", 
            "house": {
                "size": 2, 
                "id": "TP002"
            }, 
            "age": 2
        }
    ], 
    "phone": 1328974033, 
    "name": "周杰伦", 
    "houses": [
        {
            "size": 3, 
            "id": "TP003"
        }
    ], 
    "age": 37, 
    "info": {
        "jobNo": "014397", 
        "house": {
            "size": 4, 
            "id": "TP004"
        }
    }
}
格式化类配置：
public class Persion {
    @JsonPath("name")
    public String name;
    
    @JsonPath("age")
    public Integer age;
    
    @JsonPath("phone")
    public Integer phone;
    
    @BeanClass(Info.class)
    public Info info = new Info();
    
       @JsonPath("houses")
    @BeanClass(House.class)
    public List<House> houses;
    
    @JsonPath("chirlds")
    @BeanClass(Chirld.class)
    public List<Chirld> chirlds = new ArrayList<Chirld>(); 
}

@JsonPath("info")
public class Info {
    @JsonPath(value="jobNo")
    public String jobNo;
    @BeanClass(House.class)
    public House house = new House();   
}

@JsonPath("house")
public class House {   
    @JsonPath("id")
    public String id;
    
    @JsonPath(value="size")
    public Integer size;  
}

@JsonPath(value="chirld")
public class Chirld {
    
    @JsonPath(value="name")
    public String name;
    
    @JsonPath(value="age")
    public BigDecimal age;
    
    @JsonPath(value="cars")
    @BeanClass(Car.class)
    public List<Car> cars;
    @BeanClass(House.class)
    public House house;   
}

@JsonPath("car")
public class Car {
    @JsonPath("carId")
    public String carId;
    @JsonPath("carName")
    public String carName;    
}
```
